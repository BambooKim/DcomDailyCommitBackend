# Dcom Daily Commit Backend

Spring Project of Backend Part for DCOM Daily Commit Study 2022 System

## Exceptions

### Register 001

> 입력한 id가 Github에 존재하지 않을 때

**Error Code**: REG-001

**Status**: 404 NOT_FOUND

**Response**: 
```json
{
    "code": "REG-001",
    "message": "{message}"
}
```

---
### Register 002

> 입력한 access code가 올바르지 않을 때

**Error Code**: REG-002

**Status**: 403 FORBIDDEN

**Response**:
```json
{
    "code": "REG-002",
    "message": "{message}"
}
```

---
### Register 003

> 입력한 Github Id가 이미 서버에 존재할 때 

**Error Code**: REG-003

**Status**: 403 FORBIDDEN

**Response**:
```json
{
    "code": "REG-003",
    "message": "{message}"
}
```

---
### Delete 001

> 입력한 ID에 대한 유저 정보가 서버에 존재하지 않을 때

**Error Code**: DEL-001

**Status**: 404 NOT_FOUND

**Response**:
```json
{
    "code": "DEL-001",
    "message": "{message}"
}
```

---
### Delete 002

> 입력한 User Code가 잘못되었을 때

**Error Code**: DEL-002

**Status**: 404 NOT_FOUND

**Response**:
```json
{
    "code": "DEL-002",
    "message": "{message}"
}
```

---