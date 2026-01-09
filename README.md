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
