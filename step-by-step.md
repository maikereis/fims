

curl -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d "{\"username\": \"adminstrator\", \"password\": \"password\"}"

{"token":"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbnN0cmF0b3IiLCJpYXQiOjE3NjI2MjU0NTQsImV4cCI6MTc2MjcxMTg1NH0.IE-ns-pbOxHWdc3yLs2-7RU-THyw-bNMDMN35j7Whn0","type":"Bearer","id":1,"username":"adminstrator","email":"adminstrator@email.com","roles":["ROLE_ADMIN"]}

curl -X GET http://localhost:8080/api/auth/me -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbnN0cmF0b3IiLCJpYXQiOjE3NjI2MjU0NTQsImV4cCI6MTc2MjcxMTg1NH0.IE-ns-pbOxHWdc3yLs2-7RU-THyw-bNMDMN35j7Whn0"

++++++


curl -X POST http://localhost:8080/api/auth/signup -H "Content-Type: application/json" -d "{\"username\": \"database_user\", \"email\": \"sa@example.com\", \"password\": \"database_password\", \"roles\": [\"ADMIN\"]}"


curl -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d "{\"username\": \"database_user\", \"password\": \"database_password\"}"


{"token":"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkYXRhYmFzZV91c2VyIiwiaWF0IjoxNzYyNjE3ODgwLCJleHAiOjE3NjI3MDQyODB9.0Ai1cBllQTsAubQz_EtiIUW49V5VMXcqHqsF01OxP7o","type":"Bearer","id":1,"username":"database_user","email":"sa@example.com","roles":["ROLE_ADMIN"]}


curl -X GET http://localhost:8080/api/auth/me -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkYXRhYmFzZV91c2VyIiwiaWF0IjoxNzYyNjE3ODgwLCJleHAiOjE3NjI3MDQyODB9.0Ai1cBllQTsAubQz_EtiIUW49V5VMXcqHqsF01OxP7o"
