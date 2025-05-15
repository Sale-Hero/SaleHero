CREATE TABLE chatbot_conversation
(
    chat_history_idx BIGINT NOT NULL,
    id               BIGINT AUTO_INCREMENT,
    answer           VARCHAR(255),
    question         VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE chat_history
(
    created_at TIMESTAMP(6),
    id         BIGINT AUTO_INCREMENT,
    ip         VARCHAR(255),
    referer    VARCHAR(255),
    session_id VARCHAR(255),
    user_agent VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE news_letter
(
    is_sent    varchar(1),
    created_at TIMESTAMP(6),
    id         BIGINT AUTO_INCREMENT,
    sent_at    TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6),
    title      VARCHAR(255),
    content    LONGTEXT,
    PRIMARY KEY (id)
);

CREATE TABLE subscriber
(
    is_subscribed varchar(1),
    created_at    TIMESTAMP(6),
    id            BIGINT AUTO_INCREMENT,
    updated_at    TIMESTAMP(6),
    user_email    VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE users
(
    is_active  varchar(1),
    created_at TIMESTAMP(6),
    id         BIGINT AUTO_INCREMENT,
    updated_at TIMESTAMP(6),
    user_name  VARCHAR(50)  NOT NULL,
    user_email VARCHAR(100) NOT NULL,
    nick_name  VARCHAR(10)  NOT NULL,
    role       ENUM ('ADMIN','USER'),
    PRIMARY KEY (id)
);

alter table subscriber
    add is_marketing_agreed varchar(1) default 'N' null;

CREATE TABLE community
(
    id         BIGINT AUTO_INCREMENT,
    created_at TIMESTAMP(6),
    updated_at TIMESTAMP(6),
    category   TINYINT CHECK (category BETWEEN 0 AND 1),
    content    VARCHAR(255),
    title      VARCHAR(255),
    view_count BIGINT NOT NULL,
    writer_id  BIGINT NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE community
    ADD COLUMN is_deleted VARCHAR(1);

CREATE TABLE support
(
    id         BIGINT AUTO_INCREMENT,
    created_at TIMESTAMP(6),
    updated_at TIMESTAMP(6),
    cell_phone VARCHAR(255),
    content    VARCHAR(255),
    email      VARCHAR(255),
    name       VARCHAR(255),
    title      VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE week_day
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    days          ENUM ('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY') NOT NULL,
    subscriber_id BIGINT                                                                    NOT NULL,
    createdAt     TIMESTAMP(6),
    UNIQUE KEY uk_subscriber_day (subscriber_id, days)
);

CREATE TABLE user_authentication
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    email     VARCHAR(30) NOT NULL,
    code      VARCHAR(8)  NOT NULL,
    created_at TIMESTAMP(6),
    updated_at TIMESTAMP(6)
);
