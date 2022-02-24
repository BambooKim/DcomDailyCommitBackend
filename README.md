# Dcom Daily Commit Backend

Spring Project of Backend Part for DCOM Daily Commit Study 2022 System

## API

### /api/register-user

> 초기 유저 등록에 쓰이는 API. 추후 Github oauth 전환 시 변경 예정.

**Method**: POST

**Request**: x-www-form-urlencoded

```
githubId: {githubId}
korName: {korName}
userCode: {userCode}
accessCode: {accessCode}
```

**Response**: 
```json
{
  "id": "깃허브 아이디",
  "name": "실명",
  "paidFine": "낸 벌금",
  "startedAt": "등록한 날짜",
  "unpaidFine": "내지않은 벌금",
  "commitsInARow": "최근 연속 커밋 횟수",
  "totalCommits": "총 커밋 횟수",
  "elapsedDay": "참여일(등록 날짜부터 1일, 현재 날짜까지)" ,
  "rankPower": "comitsInARow * 10 + commitDayCount * 5 + totalCommits - (unpaidFine/50) 의 계산 값",
  "rank": -1,
  "userImg": "유저 이미지 주소",
  "commitDayCount": "등록한 날짜 이후로 커밋한 날 카운트",
  "isCommitToday": "오늘 커밋했으면 1, 없으면 0"
}
```

---

### /api/fetch-user

> 크롤링하지 않고, 기존의 데이터베이스에 있는 유저 정보들을 그대로 가져올 때 쓰는 API.

**Method**: GET

**Request**: None.

**Response**:

```json
[
    {
        "id": "깃허브 아이디",
        "name": "실명",
        "paidFine": "낸 벌금",
        "startedAt": "등록한 날짜",
        "unpaidFine": "내지않은 벌금",
        "commitsInARow": "최근 연속 커밋 횟수",
        "totalCommits": "총 커밋 횟수",
        "elapsedDay": "참여일(등록 날짜부터 1일, 현재 날짜까지)" ,
        "rankPower": "comitsInARow * 10 + commitDayCount * 5 + totalCommits - (unpaidFine/50) 의 계산 값",
        "rank": "현재 랭크",
        "userImg": "유저 이미지 주소",
        "commitDayCount": "등록한 날짜 이후로 커밋한 날 카운트",
        "isCommitToday": "오늘 커밋했으면 1, 없으면 0"
    },
    {
    	"Another user data here"
    }
]
```



---

### /api/refresh-user

> 신규 등록이나 데이터 동기화 등 데이터 업데이트가 필요할 때, 크롤링을 실행한 뒤 처리한 데이터들을 데이터베이스에 업데이트한 뒤, 업데이트한 정보들을 받아올 때 쓰는 API.

**Method**: GET

**Request**: None.

**Response**: [/api/fetch-user](###/api/fetch-user)와 동일

---

### /api/delete-user

**Method**: POST

**Request**: x-www-form-urlencoded

```
githubId: {githubId}
userCode: {userCode}
```

**Response**:

```
String - {githubId}
```


