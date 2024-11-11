# TIPZIP_BE

## Java & Database
- **Java version**: 17
- **Database**: MySQL

---

## Branch Naming Convention

Branch 이름은 작업 목적과 연관된 이슈 번호를 포함하는 방식으로 정합니다. 일반적인 패턴은 다음과 같습니다:
<타입>/<이슈 번호>-<간단한 설명>

예시:
- `feature/1234-add-user-login`
- `bugfix/5678-fix-login-error`
- `release/1.2.0`

### 주요 Branch 타입
- `feature/` - 새로운 기능 개발 시 사용
- `bugfix/` - 버그 수정 시 사용
- `hotfix/` - 긴급한 버그 수정 시 사용 (보통 프로덕션 환경에서 발생)
- `release/` - 릴리즈 준비 시 사용
- `chore/` - 빌드 및 기타 작업 자동화, 문서 작업 등 코드와 관련 없는 작업 시 사용

---

## Git Commit Message Convention

Git Commit 메시지는 명확한 구조를 유지하며, 아래와 같은 형식을 따릅니다:

<타입>(<모듈>): <변경 내용 요약> (#이슈 번호)

markdown
코드 복사

### 예시:
- `feat(auth): add login functionality (#1234)`
- `fix(profile): correct user profile update error (#5678)`
- `docs: update README with new instructions (#91011)`

### Commit 타입
- `feat` - 새로운 기능 추가
- `fix` - 버그 수정
- `refactor` - 코드 리팩토링 (기능 변경 없이 구조 개선)
- `style` - 코드 포맷팅, 세미콜론 누락 등 (비즈니스 로직에 영향이 없는 변경)
- `test` - 테스트 추가 또는 수정
- `docs` - 문서 추가 및 수정
- `chore` - 빌드 작업, 패키지 관리 등

---

## GitHub Issue 연동

GitHub Issue와 연동하기 위해 이슈 번호를 Branch 이름과 Commit 메시지에 포함시키는 것이 중요합니다. 또한, 커밋 메시지에 `#이슈번호`를 명시하면 자동으로 해당 이슈와 링크됩니다.
