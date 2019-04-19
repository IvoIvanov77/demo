# demo
Simple spring RESTful Web Service
Futures
## Project features list:
1. Register user:
- URL - http://localhost:8080/users/register
- request body: 
```javascript
{
    "username": "yourUsername",
    "password":"yourPassword",
    "confirmPassword":"yourPassword",
    "email":"your@email.com"
}
```
- response body: 
```javascript
{
    "username": "yourUsername",  
    "email":"your@email.com"
}
```
2. Login:
- URL - http://localhost:8080/users/login
- request body: 
```javascript
{
    "username": "yourUsername",
    "password":"yourPassword"   
}
```
- response body: 
```javascript
{
    "username": "yourUsername",  
    "token":"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJpdm8iLCJyb2xlcyI6W3siaWQiOjEsImF1dGhvcml0eSI6IlJPTEVfVVNFUiJ9LHsiaWQiOjIsImF1dGhvcml0eSI6IlJPTEVfQURNSU4ifV0sImlhdCI6MTU1NTY3NTU0MiwiZXhwIjoxNTU2MjgwMzQyfQ.3jBY0SERNe53w6p2hOc3etqE1QVBSJ92Y2OVt_J7jAs
}
```
### There is two user roles - "ROLE_ADMIN" and "ROLE_USER"
First registered user in application take all roles.   
Next registerd users takes only "ROLE_USER"      

#### Regular users can:
3. Get all employees:
- URL - http://localhost:8080/emloyees
- request body: empty
- response body: 
```javascript
{
    "_embedded": {
        "employeeList": [
            {
                "id": 1,
                "name": "ABC",
                "role": "ZZZ",
                "salary": 100,
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/employees/1"
                    },
                    "employees": {
                        "href": "http://localhost:8080/employees"
                    }
                }
            },
            {
                "id": 2,
                "name": "ABD",
                "role": "ZZY",
                "salary": 1000,
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/employees/2"
                    },
                    "employees": {
                        "href": "http://localhost:8080/employees"
                    }
                }
            },
            ................................................
``` 
4. Get single employee by id:
- URL - http://localhost:8080/emloyees/{id}
- request body: empty
- response body: 
```javascript
{
    "id": 1,
    "name": "ABC",
    "role": "ZZZ",
    "salary": 100,
    "_links": {
        "self": {
            "href": "http://localhost:8080/employees/1"
        },
        "employees": {
            "href": "http://localhost:8080/employees"
        }
    }
}
``` 
5. Get employees list by search query, frderd by salary descending - exammle:
- URL -http://localhost:8080/employees/search?name=A&role=Z&minSalary=100&maxSalary=600
- request body: empty
- response body: 
```javascript
[
    {
        "id": 4,
        "name": "ACD",
        "role": "ZDD",
        "salary": 300
    },
    {
        "id": 1,
        "name": "ABC",
        "role": "ZZZ",
        "salary": 100
    }
]
``` 
#### Admin can perform all CRUD opretions:
6. Create new employee:
- URL -http://localhost:8080/employees/search?name=A&role=Z&minSalary=100&maxSalary=600
- request body: empty
- response body: 
```javascript
[
    {
        "id": 4,
        "name": "ACD",
        "role": "ZDD",
        "salary": 300
    },
    {
        "id": 1,
        "name": "ABC",
        "role": "ZZZ",
        "salary": 100
    }
]
``` 
