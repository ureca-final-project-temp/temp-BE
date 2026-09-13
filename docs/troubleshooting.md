# Troubleshooting

해결 후에는 항상 [quickstart 6단계](quickstart.md#6-동작-확인)의 health check를 다시 통과하는지 확인하세요.

## password authentication failed

**증상**

```text
FATAL: password authentication failed for user "ubot"
```

**원인**

`POSTGRES_PASSWORD`는 DB 볼륨이 **처음 생성될 때만** 적용됩니다. 컨테이너를 한 번 띄운 뒤 `.env`의 비밀번호를 바꾸면 DB와 `.env`의 값이 달라집니다.

**해결 (데이터 유지)**

DB의 비밀번호를 `.env`와 같은 값으로 바꿉니다.

```powershell
docker exec ubot-postgres psql -U ubot -d ubot -c "ALTER USER ubot WITH PASSWORD '.env에 적은 값';"
```

**해결 (데이터 초기화)**

로컬 DB 데이터가 모두 삭제됩니다.

```powershell
docker compose down -v
docker compose up -d
```

## Could not resolve placeholder 'POSTGRES_PASSWORD'

**원인**

Spring이 `.env`를 찾지 못했습니다.

**확인할 것**

1. 프로젝트 루트에 `.env` 파일이 있는지 (`.env.example`만 있으면 안 됨)
2. `.env`에 `POSTGRES_PASSWORD=` 줄이 있는지
3. 프로젝트 루트에서 실행했는지. Eclipse라면 Run Configuration → Arguments → Working directory가 프로젝트 루트인지

## 5432 포트가 이미 사용 중

**증상**

`docker compose up` 시 `port is already allocated` 또는 `bind: address already in use`

**원인**

PC에 PostgreSQL이 따로 설치되어 실행 중인 경우가 많습니다.

**해결**

`.env`에서 포트를 바꿉니다. Spring도 같은 값을 읽으므로 다른 곳은 수정하지 않아도 됩니다.

```dotenv
POSTGRES_PORT=5433
```

```powershell
docker compose up -d
```

## 컨테이너가 healthy가 되지 않음

```powershell
docker compose logs --tail=100 postgres
```

- `Cannot connect to the Docker daemon` → Docker Desktop이 실행 중인지 확인
- `.env` 관련 경고(`variable is not set`) → 프로젝트 루트에 `.env`가 있는지 확인

## vector extension이 없음

**원인**

`infra/postgres/init.sql`은 볼륨이 **처음 생성될 때만** 실행됩니다.

**해결 (데이터 유지)**

```powershell
docker exec ubot-postgres psql -U ubot -d ubot -c "CREATE EXTENSION IF NOT EXISTS vector;"
```

## 그래도 해결되지 않으면

아래 정보를 팀 채널에 공유하세요. 비밀번호는 지우고 올립니다.

```powershell
docker compose ps
docker compose logs --tail=100 postgres
.\gradlew.bat bootRun --stacktrace
```
