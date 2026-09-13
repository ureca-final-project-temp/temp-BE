# Contributing

## 브랜치

| 브랜치 | 용도 |
|---|---|
| `main` | 배포 기준 |
| `develop` | 개발 통합. 필수 Status Check가 통과해야 merge 가능 |
| 작업 브랜치 | `develop`에서 분기. 예: `chore/2-infra-setup` |

## 이슈

GitHub 이슈 템플릿 중 작업에 맞는 것을 선택합니다.

`FeatureRequest` · `Fix` · `Refactor` · `Docs` · `Chore`

## Pull Request

### 제목 형식

```text
[Type] 한글 설명
```

- Type: `Feat`, `Fix`, `Refactor`, `Docs`, `Test`, `Chore`, `Style`
- 설명에 한글이 한 글자 이상 있어야 합니다.
- 예: `[Feat] JWT 로그인 구현`

형식이 틀리면 `Validate PR Title` 체크가 실패합니다.

### 본문

PR 템플릿의 항목(관련 이슈, 관련 도메인, 작업 내용, 체크리스트, 테스트 결과)을 채웁니다.

## PR 전 로컬 검증

```powershell
.\gradlew.bat test
.\gradlew.bat build
```

테스트는 로컬 DB에 접속하므로 `docker compose up -d`로 PostgreSQL이 실행 중이어야 합니다.

## CI

`develop`, `main` 대상 push와 PR에서 `Backend CI`가 실행됩니다.

```text
Checkout → Java 21 설정 → PostgreSQL + pgvector 서비스 기동 → gradlew test → gradlew build -x test
```

- CI는 `test` 프로필을 사용하며 DB 접속 정보는 `.github/workflows/ci.yml`의 `env:`로 주입됩니다.
- CI의 DB 비밀번호는 작업이 끝나면 사라지는 임시 DB용 값입니다. 로컬 비밀번호로 재사용하지 마세요.

## 비밀정보

- 비밀번호 등 실제 값은 `.env`에만 적고 커밋하지 않습니다.
- 새 환경변수를 추가하면 `.env.example`과 [configuration reference](docs/reference/configuration.md)에 함께 추가합니다.
