# Currency Exchange and Discount Calculation

## Description
Develop a Spring Boot application that integrates with a third-party currency exchange API to retrieve real-time exchange rates. The application should calculate the total payable amount for a bill in a specified currency after applying applicable discounts.

## Requirements

### 1. Third-Party API Integration
- Integrate with **ExchangeRate-API** or **Open Exchange Rates** to get real-time exchange rates.
- Example API URL:
  ```
  https://open.er-api.com/v6/latest/{base_currency}?apikey=your-api-key
  ```

### 2. Discounts and Currency Conversion Logic
- **Discount Rules:**
    - Employee: **30%** discount.
    - Affiliate: **10%** discount.
    - Customer for 2+ years: **5%** discount.
    - $5 discount for every **$100** on the bill.
    - **No percentage discounts** on groceries.
    - Only **one** percentage discount applies.

- **Steps:**
    - Apply discounts.
    - Convert total from **original currency** to **target currency**.
    - Return the final payable amount.

### 3. Authentication
- Secure the API with authentication.

### 4. API Endpoint
#### **POST /api/calculate**
**Request Body:**
```json
{
  "items": [
    {"name": "Laptop", "category": "Electronics", "price": 1200},
    {"name": "Apple", "category": "Groceries", "price": 20}
  ],
  "totalAmount": 1220,
  "userType": "employee",
  "customerTenure": 3,
  "originalCurrency": "USD",
  "targetCurrency": "INR"
}
```
**Response:**
```json
{
  "payableAmount": 8500.50,
  "currency": "INR"
}
```

### 5. Design and Testing
- Follow **OOP principles**.
- Provide a **UML class diagram**.
- Write **unit tests** with mocking.
- Keep the code **simple and maintainable**.

## Setup Instructions
### Prerequisites
- Java 11+, Spring Boot, Maven, API key.

### Steps
1. **Clone the repository:**
   ```bash
   git clone https://github.com/your-username/currency-exchange.git
   cd currency-exchange
   ```
2. **Update API key in properties file:**
   ```
   exchange.api.key=your-api-key
   ```
3. **Run the application:**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
4. **API available at:** `http://localhost:8080/api/calculate`

## Author
Anu Dharshini B
