# 💵 Currency Converter App

A full-stack currency conversion application with a **Spring Boot backend**, **React frontend**, and **Cypress E2E testing**. The app allows users to convert currencies using real-time exchange rates, which are provided by a free tier of https://swop.cx/ website.

## 🚀 Features
- Convert between different currencies
- Secure backend with CSRF protection
- React-based frontend with form validation
- Cypress tests for end-to-end verification
- Dockerized setup

## ⚙️ Tech Stack
- **Backend**: Java, Spring Boot, Gradle, Spotless, Caffeine Cache
- **Frontend**: React, TypeScript, React Hook Form, Bootstrap
- **Testing**: Cypress for E2E tests, JUnit for unit and integration tests
- **Containerization**: Docker & Docker Compose

## 📖 Setup Guide

### **🖥️ Running Locally (Without Docker)**
> **Prerequisites**:
> - Install Java. Built and tested on v21.0.6.
> - Install Gradle. Built and tested on v8.12.1.
> - Install Node.js. Built and tested on v22.14.0.

### **1️⃣ Clone the Repository**
```sh
git clone https://github.com/dmozhaev/currency-exchange.git
cd currency-exchange
```
Alternatively, download the zip archive manually and extract it to folder `currency-exchange`.

### **2️⃣ Backend Setup**
```sh
cd backend
export API_KEY=your-secret-api-key  # Set API key provided by SWOP here
./gradlew bootRun
```
Backend runs on http://localhost:8080.

### **3️⃣ Frontend Setup**
```sh
cd frontend
npm install
npm run dev
```
Frontend runs on http://localhost:5173.

---

### **🐳 Running with Docker**
> **Prerequisites**:
> - Ensure you have Docker & Docker Compose installed.

### **1️⃣ Set API Key**
Create a `.env` file at the project root:
```sh
echo "API_KEY=your-secret-api-key" > .env  # Set API key provided by SWOP here
```

### **2️⃣ Start Backend and Frontend with Docker Compose**
```sh
docker-compose up -d --build
```
App will be available at http://localhost:5173.

## 🧪 Running tests

### **1️⃣ Unit and Integration Tests**
Unit and integration tests reside in the backend folder and are designed using JUnit.

Unit tests only:
```sh
cd backend
./gradlew test --tests "com.example.demo.util.ConversionUtilTest" --tests "com.example.demo.validator.ConversionValidatorTest"
```

Integration tests only:
```sh
cd backend
./gradlew test --tests "com.example.demo.integration.ConvertCurrencyIntegrationTest"
```

All automated tests at once:
```sh
cd backend
./gradlew test
```

### **2️⃣ Cypress E2E Tests**
Cypress tests come in 2 different modes: normal mode (with UI) and headless mode (no UI provided).

#### Normal mode
```sh
cd e2e
npx cypress open
```
Then, select the browser and proceed with selecting e2e test in UI.

#### Headless mode
```sh
cd e2e
npx cypress run
```

## 🛠️ Code formatting

### **1️⃣ Backend**
On the backend side, [Spotless](https://github.com/diffplug/spotless) is used to format and check the code.

Code checking can be performed by running:
```sh
cd backend
./gradlew spotlessCheck
```

Code autofixing can be performed by running:
```sh
cd backend
./gradlew spotlessApply
```

### **2️⃣ Frontend**
On the frontend side, [ESlint](https://eslint.org) is used to format and check the code.

Code checking can be performed by running:
```sh
cd frontend
npx eslint . --quiet
```

Code autofixing can be performed by running:
```sh
cd frontend
npx eslint . --fix
```

## 🔗 API Documentation
The backend exposes a REST API for currency conversion.

### **📌 Base URL**
```sh
http://localhost:8080
```

### **1️⃣ Get CSRF Token**
> - **Endpoint**: `GET /security/csrf`

Request:
```sh
curl -X GET http://localhost:8080/security/csrf
```

Response example:
```sh
{
  "parameterName": "_csrf",
  "token": "tg9PdISRmg2si5K-9CGJ_v_zdqaEMFPTocFBNPsvP-HIi6bs1Wx5FeHyqGiBs6HcxQy9zs2WW568BDX-lqVxDM4ZB4LxvsXf",
  "headerName": "X-XSRF-TOKEN"
}
```

### **2️⃣ Convert Currency**
> - **Endpoint**: `POST /convert/`
> - **Headers**: `Content-Type: application/json`

Body example:
```sh
{
  "sourceCurrency": "EUR",
  "targetCurrency": "USD",
  "amount": 100
}
```

Request example:
```sh
curl -X POST http://localhost:8080/convert \
     -H "Content-Type: application/json" \
     -H "X-CSRF-Token: your-csrf-token" \
     -d '{"sourceCurrency": "EUR", "targetCurrency": "USD", "amount": 100}'
```

Response example:
```sh
{
  "sourceCurrency": "EUR",
  "targetCurrency": "USD",
  "amount": 100,
  "result": 104.81
}
```

### **3️⃣ API Manual Testing**
In order to test the whole process, `CRSF` token needs to be obtained first and after that it can be used in the `POST` request. In this section, some script examples are provided in order to test the process on different platforms.

#### Linux / Mac
```sh
#!/bin/bash

BASE_URL="http://localhost:8080"
COOKIE_FILE="cookies.txt"

csrf_response=$(curl -s -c $COOKIE_FILE "$BASE_URL/security/csrf")
csrf_token=$(echo "$csrf_response" | grep -oP '"token":"\K[^"]+')

headers=(
    -H "X-XSRF-TOKEN: $csrf_token"
    -H "Content-Type: application/json"
)

body='{"sourceCurrency": "EUR", "targetCurrency": "USD", "amount": 100.00}'

response=$(curl -s -X POST "http://localhost:8080/convert/" -b "$COOKIE_FILE" -c $COOKIE_FILE "${headers[@]}" -d "$body")

echo "Response: $response"
```

#### Windows
Powershell example:

```sh
$csrfResponse = Invoke-RestMethod -Uri "http://localhost:8080/security/csrf" -Method GET -SessionVariable session
$csrfToken = $csrfResponse.token

$headers = @{
    "X-XSRF-TOKEN" = $csrfToken
    "Content-Type" = "application/json"
}

$body = @{
    "sourceCurrency" = "EUR"
    "targetCurrency" = "USD"
    "amount" = 100.00
} | ConvertTo-Json -Depth 2

Invoke-RestMethod -Uri "http://localhost:8080/convert/" -Method Post -Headers $headers -Body $body -WebSession $session
```

## 📜 License ##
This project is licensed under the MIT License.

## 👤 Author ##
Dmitry Mozhaev
