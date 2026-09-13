# 설정 Reference

## 설정 파일

```text
src/main/resources/
├── application.yml         # 모든 환경 공통
├── application-local.yml   # 로컬 개발 (기본 프로필)
└── application-test.yml    # CI
```

| 프로필 | 사용 환경 | 활성화 방법 | 환경변수 공급원 |
|---|---|---|---|
| `local` | 개발자 PC (`bootRun`, Eclipse, 로컬 `gradlew test`) | 기본값 (`spring.profiles.default: local`) | 프로젝트 루트의 `.env` |
| `test` | GitHub Actions CI | `SPRING_PROFILES_ACTIVE=test` | `.github/workflows/ci.yml`의 `env:` |

> 테스트 클래스에 `@ActiveProfiles`가 없으므로 로컬에서 `gradlew test`를 실행하면 `local` 프로필이 사용됩니다.

## 환경변수

| 변수 | 사용처 | Spring 기본값 | 설명 |
|---|---|---|---|
| `POSTGRES_HOST` | Spring | `localhost` | DB 호스트 |
| `POSTGRES_PORT` | Docker, Spring | `5432` | DB 포트. Docker는 이 포트로 컨테이너를 노출합니다 |
| `POSTGRES_DB` | Docker, Spring | `ubot` | DB 이름 |
| `POSTGRES_USER` | Docker, Spring | `ubot` | DB 사용자 |
| `POSTGRES_PASSWORD` | Docker, Spring | **없음 (필수)** | DB 비밀번호 |
| `OLLAMA_BASE_URL` | Spring (`local`) | `http://localhost:11434` | Ollama 주소 |
| `OLLAMA_EMBEDDING_MODEL` | Spring (`local`) | `bge-m3` | Embedding 모델 이름 |
| `SPRING_PROFILES_ACTIVE` | — | — | `.env.example`에 있지만 현재 효과 없음 (아래 참고) |

### 값을 읽는 순서

- `local` 프로필은 `spring.config.import: optional:file:.env[.properties]`로 `.env`를 읽습니다.
- OS 환경변수가 `.env`보다 우선합니다. 나중에 실제 환경변수로 주입하면 코드 수정 없이 그 값이 사용됩니다.
- `.env`는 **실행 위치 기준 상대경로**로 찾습니다. 프로젝트 루트가 아닌 곳에서 실행하면 읽지 못합니다.
- Docker Compose도 같은 `.env`를 읽으므로, Docker와 Spring이 항상 같은 비밀번호를 사용합니다.

### `POSTGRES_PASSWORD`에 기본값이 없는 이유

비밀번호를 코드(Git)에 남기지 않기 위해서입니다. 값이 없으면 기동 시점에 `Could not resolve placeholder 'POSTGRES_PASSWORD'` 오류로 즉시 실패합니다.

### `SPRING_PROFILES_ACTIVE`가 `.env`에서 효과 없는 이유

`.env`는 `local` 프로필 설정 안에서 읽히므로, 읽는 시점에는 이미 프로필이 결정되어 있습니다. 프로필을 바꾸려면 OS 환경변수나 실행 인자로 지정해야 합니다.

## 고정 설정값

| 설정 | 값 | 파일 |
|---|---|---|
| pgvector 차원 | `1024` (BGE-M3 출력 차원과 일치해야 함) | `application-local.yml` |
| pgvector 인덱스 | `HNSW` | `application-local.yml` |
| 거리 함수 | `COSINE_DISTANCE` | `application-local.yml` |
| JPA `ddl-auto` | `none` | `application-local.yml`, `application-test.yml` |
| Actuator 노출 endpoint | `health`만 | `application.yml` |
| Health 상세 표시 | `always` | `application-local.yml` |

> Embedding 모델을 바꾸면 pgvector 차원도 해당 모델의 출력 차원으로 함께 바꿔야 합니다.
