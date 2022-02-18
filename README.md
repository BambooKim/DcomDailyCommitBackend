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
    "participationRate": "커밋일 + '/'+ 참여일" ,
    "rankPower": "comitsInARow * 10 + commitDayCount * 5 + totalCommits - (unpaidFine/50) 의 계산 값",
    "rank": -1,
    "userImg": "유저 이미지 주소",
    "commitDayCount": "등록한 날짜 이후로 커밋한 날 카운트"
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
        "participationRate": "커밋일 + '/'+ 참여일" ,
        "rankPower": "comitsInARow * 10 + commitDayCount * 5 + totalCommits - (unpaidFine/50) 의 계산 값",
        "rank": "현재 랭크",
        "userImg": "유저 이미지 주소",
        "commitDayCount": "등록한 날짜 이후로 커밋한 날 카운트"
    },
    {
    	"Another user data here"
    }
]
```

**Response 예시**

```json
[
    {
        "id": "justkode",
        "name": "김수한무",
        "paidFine": 0,
        "startedAt": 1636785193000,
        "unpaidFine": 46000,
        "commitsInARow": 43,
        "totalCommits": 131,
        "participationRate": "67 / 92",
        "rankPower": 896,
        "rank": 1,
        "userImg": "https://avatars.githubusercontent.com/u/28499550?v=4",
        "commitDayCount": 67
    },
    {
        "id": "codeisneverodd",
        "name": "김경현",
        "paidFine": 0,
        "startedAt": 1639317384000,
        "unpaidFine": 31500,
        "commitsInARow": 0,
        "totalCommits": 79,
        "participationRate": "43 / 63",
        "rankPower": 294,
        "rank": 2,
        "userImg": "https://avatars.githubusercontent.com/u/54318460?v=4",
        "commitDayCount": 43
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
```

**Response**:

```
String - "deleted"
```

추후 변경 가능.

