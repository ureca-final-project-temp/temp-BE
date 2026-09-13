# UBot-BE

통신사 고객 상담용 RAG 챗봇 UBot의 백엔드 서버입니다.

- 스택: Spring Boot 4.1.1 · Java 21 · PostgreSQL 17 + pgvector · Ollama(BGE-M3)
- 대상: 이 저장소를 개발하는 팀원

## Quick start

요구사항: Java 21, Docker Desktop, Ollama, Git

```powershell
Copy-Item .env.example .env     # POSTGRES_PASSWORD를 원하는 값으로 수정
docker compose up -d
ollama pull bge-m3
.\gradlew.bat bootRun
```

정상 동작 확인:

```powershell
curl.exe http://localhost:8080/actuator/health
# 기대 결과: "status":"UP", components.db.status도 "UP"
```

단계별 설명은 [docs/quickstart.md](docs/quickstart.md)를 참고하세요.

## 문서 지도

| 목적 | 문서 |
|---|---|
| 처음 로컬에서 실행하기 | [docs/quickstart.md](docs/quickstart.md) |
| DB 직접 조회하기 (psql, DBeaver) | [docs/how-to/db-access.md](docs/how-to/db-access.md) |
| 환경변수·프로필 설정값 찾기 | [docs/reference/configuration.md](docs/reference/configuration.md) |
| 실행이 안 될 때 | [docs/troubleshooting.md](docs/troubleshooting.md) |
| 브랜치·PR·CI 규칙 | [CONTRIBUTING.md](CONTRIBUTING.md) |

## 호환성

| 구성요소 | 버전 |
|---|---|
| Java | 21 |
| Spring Boot | 4.1.1 |
| PostgreSQL | 17 (`pgvector/pgvector:pg17`) |
| Embedding 모델 | `bge-m3` (1024차원) |
