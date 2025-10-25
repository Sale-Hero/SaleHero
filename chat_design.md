# 익명 채팅방 기능 백엔드 설계서

## 1. 개요

사용자들이 별도의 인증 없이 익명으로 참여하여 실시간으로 대화를 나눌 수 있는 "킬링타임용" 채팅방 기능의 백엔드를 구현한다. 프론트엔드는 React로 별도 구현된다.

- **목표:** 간단하고 재미있는 소통 공간을 위한 백엔드 API 제공 및 채팅 내역 저장
- **핵심 기능:** 익명 닉네임 부여, 실시간 메시지 교환, 접속/퇴장 알림, 채팅 내역 저장
- **기술:** Spring WebSocket (STOMP), Spring Data JPA, Spring Data Redis

## 2. 기술 스택

| 구분 | 기술 | 사유 |
| --- | --- | --- |
| **Backend** | Spring Boot, Kotlin, Spring WebSocket, Spring Data JPA, Spring Data Redis | 현재 프로젝트 스택을 활용하며, STOMP 브로커를 통해 pub/sub 메시징을 쉽게 구현할 수 있다. 메시지 영속성을 위해 Spring Data JPA를 사용하고, 실시간 메시지 버퍼링 및 고성능 처리를 위해 Redis를 활용한다. |
| **Database** | MySQL | 채팅 내역의 장기 저장을 위한 관계형 데이터베이스. |
| **Cache/Message Buffer** | Redis | 실시간 채팅 메시지의 고속 저장 및 조회, 그리고 비동기 영속화를 위한 버퍼 역할. |

## 3. 상세 설계

### 3.1. WebSocket 통신 흐름

1. Client: 서버 WebSocket Endpoint로 연결 시도 (/api/ws-chat)
   -> 서버는 SockJS 핸드셰이크 후 세션 수립

2. Server: 연결 수립 시, 해당 세션에 익명 ID 부여 (e.g., "익명-12345")
   -> "입장" 메시지를 생성하여 Redis에 저장 후 모든 구독자에게 브로드캐스팅

3. Client: 연결 성공 후, 채팅 메시지를 수신할 Topic 구독 (/topic/chat)

4. Client: 메시지 입력 후 STOMP를 통해 메시지를 서버의 특정 경로로 전송 (/app/chat.sendMessage)

5. Server: @MessageMapping이 설정된 컨트롤러가 메시지 수신
   -> 메시지를 Redis에 저장
   -> 메시지 내용(보낸이, 내용 등)을 담아 /topic/chat 토픽으로 브로드캐스팅

6. Clients: /topic/chat을 구독 중인 모든 클라이언트가 메시지를 수신

7. Client: 브라우저 종료 또는 연결 해제
   -> 서버는 Disconnect 이벤트 감지 후 "퇴장" 메시지를 Redis에 저장 후 모든 구독자에게 브로드캐스팅

8. Background Task: 주기적으로 Redis에서 메시지를 읽어와 MySQL에 배치(Batch) 저장하여 영속화.

### 3.2. 서버 사이드 (Backend)

#### 3.2.1. 의존성 추가 (`build.gradle.kts`)

#### 3.2.2. WebSocket 설정 (`/src/main/kotlin/.../config/WebSocketConfig.kt`)

- STOMP 프로토콜을 사용하기 위한 WebSocket 메시지 브로커를 설정한다.

#### 3.2.3. 메시지 DTO (`/src/main/kotlin/.../chat/dto/ChatMessage.kt`)

- 클라이언트와 서버 간에 주고받을 메시지 객체.

#### 3.2.4. 메시지 엔티티 (`/src/main/kotlin/.../chat/domain/ChatMessageEntity.kt`)

- 채팅 메시지를 데이터베이스에 저장하기 위한 엔티티.

#### 3.2.5. 메시지 레포지토리 (`/src/main/kotlin/.../chat/domain/ChatMessageRepository.kt`)

- `ChatMessageEntity`의 데이터베이스 CRUD 작업을 위한 레포지토리.

#### 3.2.6. 채팅 컨트롤러 (`/src/main/kotlin/.../chat/controller/ChatController.kt`)

- 메시지 발행(pub) 처리 및 접속 이벤트를 관리한다.

#### 3.2.7. 메시지 캐싱 및 비동기 영속화 전략

-   **Redis를 활용한 실시간 메시지 버퍼링:**
    -   모든 채팅 메시지(CHAT, JOIN, LEAVE)는 실시간으로 Redis의 List 자료구조에 JSON 형태로 저장된다.
    -   Redis는 인메모리 데이터 스토어로, 고속의 쓰기/읽기 성능을 제공하여 실시간 채팅의 부하를 효과적으로 분산한다.
    -   최근 N개의 메시지를 Redis에서 직접 조회하여 클라이언트에 제공할 수 있다.
-   **MySQL을 활용한 장기 영속화:**
    -   별도의 스케줄링된 백그라운드 작업(예: Spring `@Scheduled`)이 주기적으로 Redis에서 일정량의 메시지를 가져온다.
    -   가져온 메시지들은 배치(Batch) 형태로 MySQL 데이터베이스에 저장된다.
    -   이 방식은 MySQL에 대한 직접적인 쓰기 부하를 줄이고, 대량의 메시지를 효율적으로 영속화할 수 있게 한다.

#### 3.2.8. 채팅 영속화 서비스 (`/src/main/kotlin/.../chat/service/ChatPersistenceService.kt`)

- Redis에 저장된 메시지를 주기적으로 MySQL에 영속화하는 서비스.

## 4. 개발 순서

1.  **Backend:** `build.gradle.kts`에 `spring-boot-starter-websocket`, `spring-boot-starter-data-jpa`, `spring-boot-starter-data-redis` 및 `mysql-connector-j` 의존성을 추가한다.
2.  **Backend:** `WebSocketConfig.kt` 파일을 생성하여 WebSocket 엔드포인트와 메시지 브로커를 설정한다.
3.  **Backend:** `ChatMessage.kt` DTO 파일을 생성한다.
4.  **Backend:** `ChatMessageEntity.kt` 엔티티 파일을 생성한다.
5.  **Backend:** `ChatMessageRepository.kt` 레포지토리 파일을 생성한다.
6.  **Backend:** `ChatController.kt`를 수정하여 메시지를 Redis에 JSON 형태로 저장하도록 변경한다.
7.  **Backend:** `ChatPersistenceService.kt`를 생성하여 Redis에서 메시지를 읽어 MySQL에 비동기적으로 배치 저장하는 로직을 구현한다. (애플리케이션 메인 클래스에 `@EnableScheduling` 어노테이션 추가 필요)
8.  **Backend:** `application.yml` 또는 `application-dev.yml`에 MySQL 및 Redis 연결 정보를 설정한다.
9.  **실행 및 테스트:** 애플리케이션을 실행하고 WebSocket 클라이언트(e.g., Postman, test client)를 통해 기능이 정상 동작하는지 확인한다. Redis에 메시지가 저장되고, 주기적으로 MySQL에 영속화되는지 확인한다.

## 5. 추가 고려사항

-   **확장성:** Redis와 MySQL의 분리 및 배치 처리를 통해 높은 트래픽에도 안정적으로 메시지를 처리하고 영속화할 수 있다. Redis 클러스터 및 MySQL 레플리케이션을 통해 더욱 확장성을 확보할 수 있다.
-   **보안:** 현재는 아무나 접속 가능하므로, 필요시 Spring Security와 통합하여 특정 권한을 가진 사용자만 채팅에 참여하도록 제한할 수 있다.
-   **UX 개선:** 백엔드 관점에서 타이핑 중 표시, 사용자 목록 표시, 메시지 시간 표시, 그리고 **과거 채팅 내역 조회 기능 (MySQL에서 조회)** 등 사용자 경험을 개선할 수 있는 기능을 추가할 수 있다.
-   **Redis 데이터 관리:** Redis에 너무 많은 메시지가 쌓이지 않도록 적절한 `fixedDelay` 및 `BATCH_SIZE` 설정이 중요하며, Redis의 메모리 사용량 모니터링이 필요하다. 또한, Redis List 대신 Stream을 사용하여 메시지 처리의 안정성과 확장성을 높이는 방안도 고려할 수.
