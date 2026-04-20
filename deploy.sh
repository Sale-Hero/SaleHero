#!/bin/bash

echo "🚀 SaleHero Backend 배포 시작..."

# 기존 컨테이너 정지 및 제거
echo "⏹️ 기존 컨테이너 정지 중..."
docker compose down || true

# 이미지 빌드
echo "🔨 Docker 이미지 빌드 중..."
docker compose build --no-cache

# 컨테이너 실행
echo "▶️ 컨테이너 실행 중..."
docker compose up -d

# 컨테이너 상태 확인
echo "✅ 컨테이너 상태 확인..."
docker compose ps

echo "🎉 SaleHero Backend 배포 완료!"
echo "📍 API 엔드포인트: http://localhost:8880"