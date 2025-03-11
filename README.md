# Currency Exchange and Discount Calculation

## Description
A Spring Boot application that integrates with a third-party currency exchange API to retrieve real-time exchange rates. The application should calculate the total payable amount for a bill in a specified currency after applying applicable discounts.

## Currency Exchange LLD
<img src="images/LLD.png" width="600" height="300">


### Prerequisites
- Java 17, Spring Boot, Maven, API key.

### 1. Third-Party API Integration
- Integrate with **ExchangeRate-API** or **Open Exchange Rates** to get real-time exchange rates.
- Example API URL:
  ```
  https://v6.exchangerate-api.com/v6/{api_key}/latest/{base_currency}
  ```
### 2. API Endpoint
#### **POST /api/calculate**
**Request Body:**
```json
{
  "items": [
    {
      "name": "Laptop",
      "price": 1000.00,
      "grocery": false
    },
    {
      "name": "Milk",
      "price": 50.00,
      "grocery": true
    },
    {
      "name": "Headphones",
      "price": 200.00,
      "grocery": false
    }
  ],
  "originalCurrency": "EUR",
  "targetCurrency": "INR",
  "user": {
    "userType": "EMPLOYEE",
    "tenureInYears": 3
  }
}

```
**Response:**
```json
{
  "originalBillAmount": 1250,
  "finalCost": 80758.11433,
  "originalCurrency": "EUR",
  "targetCurrency": "INR"
}
```
### Authentication
All API requests require an **API Key** in the request header.

#### **Example Request (with API Key)**
```sh
curl -X POST "http://localhost:8080/api/calculate" \
     -H "X-API-KEY: your-secret-api-key" \
     -H "Content-Type: application/json" \
     -d '{ "items": [...], "originalCurrency": "USD", "targetCurrency": "INR", "user": {...} }'
```

To get the **API Key**, configure it in `application.yml`:
```yaml
api:
  key: your-secret-api-key
```

### Running Tests & Generating Code Coverage
To run the tests and generate a **JaCoCo coverage report**, execute:
```sh
mvn clean verify
```
Once the tests are complete, you can find the **coverage report** here:
```
target/site/jacoco/index.html
```
Open this file in your browser to view the detailed **test coverage report**.

## SonarCloud Integration
This project uses **SonarCloud** for static code analysis and test coverage.

- **SonarCloud Dashboard**: [Click Here](https://sonarcloud.io/dashboard?id=Anudharshini_currency-exchange)
- **Code Quality Checks:** Runs automatically in **GitHub Actions** on each push to `main`.
- **To trigger a manual scan**, run:
```sh
mvn sonar:sonar
```
## CI/CD Workflow
This project uses **GitHub Actions** for automated testing & SonarCloud analysis.

- **Workflow File:** `.github/workflows/sonar.yml`
- **Runs on:** Every push to `main`
- **Includes:**  
  ✅ Build & Test  
  ✅ Code Coverage Report (JaCoCo)  
  ✅ SonarCloud Static Code Analysis

To manually trigger it:
```sh
git push origin main
```
View logs in the **GitHub Actions** tab.


## Author
Anu Dharshini B
