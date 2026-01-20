## 🚀 설치 및 실행 가이드 (Getting Started)

### 1. 프로젝트 클론 (Clone)
터미널에서 아래 명령어를 입력하여 프로젝트를 내려받습니다.
```bash
git clone <레포지토리_URL>
cd back-end
```

### 2. 데이터베이스 생성 (MySQL Setup)
프로젝트 실행 전, 로컬 MySQL에 접속하여 사용할 데이터베이스(Schema)를 생성해야 합니다.
MySQL Workbench나 터미널에서 아래 SQL을 실행하세요.

```sql
-- 1. 데이터베이스 생성 (이름: crosspay_db)
CREATE DATABASE crosspay_db CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- 2. 생성 확인
SHOW DATABASES;
```

### 3. 비밀 설정 파일 작성 (Secret Configuration)
이 프로젝트는 보안이 필요한 DB 접속 정보와 API 키를 application-secret.yml에서 관리합니다.
src/main/resources/ 폴더 안에 application-secret.yml 파일을 생성하고 내용을 작성하세요.

### 4. 서버 실행 (Run)
설정이 완료되었다면 서버를 실행합니다.

* **IntelliJ:** `src/main/java/com/potping/PotpingApplication.java` 파일 열기 → `Run` 버튼(▶) 클릭
* **터미널:** `./gradlew bootRun` (Windows는 `gradlew.bat bootRun`)

## 📚 API 명세서 (Swagger)

서버가 정상적으로 실행되면 브라우저에서 아래 주소로 접속하여 API를 테스트할 수 있습니다.

👉 **[Swagger UI 바로가기](http://localhost:8080/swagger-ui/index.html)**
* 주소: `http://localhost:8080/swagger-ui/index.html`

---

# 오류 상태코드

| 상태코드 | 설명 |
|----------|------|
| 401 | 토큰 없이 보호된 API 접근 |
| 403 | 관리자 전용 API에 일반 사용자 접근 |
| 404 | 존재하지 않는 URL 경로, 삭제되거나 찾을 수 없는 데이터, 잘못된 ID로 조회 |
| 405 | GET만 지원하는 API에 POST 요청 |
| 409 | 중복, 이미 처리된 요청 재시도 |
| 415 | 지원하지 않는 Content-Type |

### 깃허브 규칙

**브랜치 전략**

<aside>

main - dev - 닉네임/fix, feat/(이슈번호)-user-(맡은 기능 간단하게)

ex) feat/1-user-token

</aside>

### 개발 구조

- Dto → Record(ResponseTripDto, RequestTripDto)
- Controller API 리턴 → ResponseEntity
- 디렉터리 구조 → domain, global
