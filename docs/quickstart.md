# 로컬에서 UBot-BE 실행하기

## 완료 기준

이 문서를 마치면 `http://localhost:8080/actuator/health`에서 애플리케이션과 DB가 모두 `UP`으로 표시됩니다.

## 요구사항

| 프로그램 | 확인 명령 |
|---|---|
| Java 21 | `java -version` |
| Docker Desktop (실행 중이어야 함) | `docker compose version` |
| Ollama | `ollama --version` |
| Git | `git --version` |

사용 포트 `5432`(PostgreSQL), `8080`(Spring Boot), `11434`(Ollama)가 비어 있어야 합니다.

> 명령은 Windows PowerShell 기준입니다. macOS/Linux에서는 `Copy-Item` 대신 `cp`, `.\gradlew.bat` 대신 `./gradlew`를 사용하세요.

## 1. 저장소 받기

```powershell
git clone https://github.com/ureca-final-project-temp/UBot-BE.git
cd UBot-BE
```

이후 모든 명령은 **프로젝트 루트**(`UBot-BE/`)에서 실행합니다.

## 2. `.env` 만들기

```powershell
Copy-Item .env.example .env
```

`.env`를 열어 `POSTGRES_PASSWORD`를 원하는 값으로 바꿉니다. 나머지 값은 그대로 두어도 됩니다.

```dotenv
POSTGRES_PASSWORD=나만의-비밀번호
```

> **다음 단계로 넘어가기 전에 비밀번호를 정하세요.** 이 값은 DB 컨테이너가 **처음 만들어질 때만** 적용됩니다. 나중에 바꾸는 방법은 [troubleshooting](troubleshooting.md#password-authentication-failed)을 참고하세요.

`.env`는 Git에 올라가지 않습니다(`.gitignore` 등록됨). 비밀번호는 이 파일에만 적습니다.

## 3. PostgreSQL + pgvector 실행

```powershell
docker compose up -d
docker compose ps
```

`ubot-postgres`의 STATUS가 `(healthy)`가 될 때까지 기다립니다. 보통 10초 안에 바뀝니다.

첫 실행 시 `infra/postgres/init.sql`이 `vector` extension을 자동으로 생성합니다.

## 4. Embedding 모델 받기

Ollama가 실행 중인 상태에서:

```powershell
ollama pull bge-m3
ollama list
```

목록에 `bge-m3`가 보이면 완료입니다.

## 5. Spring Boot 실행

```powershell
.\gradlew.bat bootRun
```

Eclipse에서 `UbotBeApplication`을 실행해도 됩니다. 이 경우 Run Configuration의 작업 디렉터리가 프로젝트 루트여야 `.env`를 읽을 수 있습니다(기본값이 프로젝트 루트입니다).

별도 설정이 없으면 `local` 프로필로 실행됩니다.

## 6. 동작 확인

새 터미널에서:

```powershell
curl.exe http://localhost:8080/actuator/health
```

기대 결과(일부):

```json
{
  "status": "UP",
  "components": {
    "db": { "status": "UP", ... },
    ...
  }
}
```

- `status`가 `UP` → 애플리케이션 기동 성공
- `components.db.status`가 `UP` → DB 연결 성공

둘 다 `UP`이면 로컬 환경 구성이 끝났습니다.

## 실패했다면

증상별 해결 방법은 [troubleshooting.md](troubleshooting.md)에 있습니다.

## 다음 단계

- DB를 직접 조회하려면 → [how-to/db-access.md](how-to/db-access.md)
- 설정값의 의미가 궁금하면 → [reference/configuration.md](reference/configuration.md)
- 코드를 수정하고 PR을 올리려면 → [../CONTRIBUTING.md](../CONTRIBUTING.md)

## 정리하기

```powershell
docker compose down      # 컨테이너 중지 (데이터 유지)
```

> `docker compose down -v`는 DB 볼륨까지 삭제합니다. 데이터를 모두 지우고 처음부터 다시 만들 때만 사용하세요.
