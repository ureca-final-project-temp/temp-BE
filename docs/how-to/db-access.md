# DB 직접 조회하기

로컬 PostgreSQL에 접속해 데이터나 pgvector 상태를 확인하는 방법입니다.
애플리케이션 실행에는 필요하지 않은 선택 단계입니다.

전제: [quickstart](../quickstart.md)의 3단계까지 완료되어 `ubot-postgres` 컨테이너가 실행 중이어야 합니다.

## psql로 접속하기

별도 설치 없이 컨테이너 안의 psql을 사용합니다.

```powershell
docker exec -it ubot-postgres psql -U ubot -d ubot
```

컨테이너 내부 접속이라 비밀번호를 묻지 않습니다. `.env`에서 `POSTGRES_USER`나 `POSTGRES_DB`를 바꿨다면 `-U`, `-d` 값도 맞춰 바꾸세요.

종료: `\q`

## DBeaver로 접속하기

새 PostgreSQL Connection을 만들고 아래 값을 입력합니다.

| 항목 | 값 |
|---|---|
| Host | `localhost` |
| Port | `.env`의 `POSTGRES_PORT` (기본 `5432`) |
| Database | `.env`의 `POSTGRES_DB` |
| Username | `.env`의 `POSTGRES_USER` |
| Password | `.env`의 `POSTGRES_PASSWORD` |

`Test Connection`이 성공하면 완료입니다.

## pgvector 활성화 확인

psql 또는 DBeaver에서 실행합니다.

```sql
SELECT extname, extversion
FROM pg_extension
WHERE extname = 'vector';
```

`vector` 행이 한 줄 조회되면 정상입니다. 조회되지 않으면 [troubleshooting](../troubleshooting.md#vector-extension이-없음)을 참고하세요.

## 주의사항

- 테이블 구조를 DBeaver나 psql로 직접 변경하지 않습니다. 스키마 변경은 추후 migration 도구로 관리할 예정입니다.
- 애플리케이션은 `ddl-auto: none`이라 테이블을 자동으로 만들거나 바꾸지 않습니다.
