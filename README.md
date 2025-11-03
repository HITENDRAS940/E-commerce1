# E-commerce Backend API

A comprehensive e-commerce REST API built with Spring Boot, featuring JWT authentication, role-based authorization, and complete shopping cart functionality.

## 🚀 Features

- **User Authentication & Authorization**
  - JWT-based authentication with cookie and header support
  - Role-based access control (ADMIN, USER, SELLER roles)
  - Secure password handling with Spring Security

- **Product Management**
  - CRUD operations for products and categories
  - Product search and filtering capabilities
  - Image upload and management
  - Seller-specific product management

- **Shopping Cart**
  - Add/remove products to/from cart
  - Update product quantities
  - Calculate total prices with discounts

- **Order Management**
  - Complete order processing workflow
  - Order history and tracking
  - Multiple address management

- **Payment Integration**
  - Payment gateway integration support
  - Payment status tracking
  - Transaction history

## 🛠️ Technology Stack

- **Framework**: Spring Boot 3.5.5
- **Language**: Java 21
- **Database**: PostgreSQL
- **Security**: Spring Security with JWT
- **Documentation**: Swagger/OpenAPI 3
- **Build Tool**: Maven
- **ORM**: Spring Data JPA/Hibernate

## 📚 Dependencies

- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- Spring Boot Starter Security
- Spring Boot Starter Validation
- PostgreSQL Driver
- JWT (io.jsonwebtoken)
- Lombok
- ModelMapper
- SpringDoc OpenAPI

## ⚙️ Setup & Installation

### Prerequisites
- Java 21 or higher
- Maven 3.6+
- PostgreSQL 12+

### Installation Steps

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Ecommerce
   ```

2. **Database Setup**
   - Create a PostgreSQL database named `Ecommerce`
   - Update database credentials in `application.properties`

3. **Configure Application Properties**
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/Ecommerce
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

4. **Build and Run**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

5. **Access the Application**
   - API Base URL: `http://localhost:5000`
   - Swagger UI: `http://localhost:5000/swagger-ui.html`

## 📋 API Endpoints

### Authentication
- `POST /api/auth/signup` - User registration
- `POST /api/auth/signin` - User login
- `POST /api/auth/signout` - User logout
- `GET /api/auth/user` - Get current user details

### Products
- `GET /api/products` - Get all products
- `GET /api/products/{id}` - Get product by ID
- `POST /api/products` - Create new product (SELLER/ADMIN)
- `PUT /api/products/{id}` - Update product (SELLER/ADMIN)
- `DELETE /api/products/{id}` - Delete product (ADMIN)

### Categories
- `GET /api/categories` - Get all categories
- `POST /api/categories` - Create category (ADMIN)
- `PUT /api/categories/{id}` - Update category (ADMIN)
- `DELETE /api/categories/{id}` - Delete category (ADMIN)

### Cart
- `GET /api/cart` - Get user's cart
- `POST /api/cart/products/{productId}` - Add product to cart
- `PUT /api/cart/products/{productId}` - Update cart item quantity
- `DELETE /api/cart/products/{productId}` - Remove product from cart

### Orders
- `GET /api/orders` - Get user's orders
- `POST /api/orders` - Place new order
- `GET /api/orders/{id}` - Get order details

### Addresses
- `GET /api/addresses` - Get user's addresses
- `POST /api/addresses` - Add new address
- `PUT /api/addresses/{id}` - Update address
- `DELETE /api/addresses/{id}` - Delete address

## 🏗️ Project Structure

```
src/
├── main/
│   ├── java/com/hitendra/ecommerce/
│   │   ├── config/          # Configuration classes
│   │   ├── controller/      # REST controllers
│   │   ├── exceptions/      # Custom exceptions
│   │   ├── model/          # Entity classes
│   │   ├── payload/        # DTOs and request/response objects
│   │   ├── repository/     # Data access layer
│   │   ├── security/       # Security configuration
│   │   ├── service/        # Business logic layer
│   │   └── utils/          # Utility classes
│   └── resources/
│       ├── application.properties
│       └── data.sql        # Initial data
└── test/                   # Test classes
```

## 🔐 Security

- JWT tokens for stateless authentication
- Password encryption using BCrypt
- Role-based authorization
- CORS configuration for cross-origin requests
- Input validation and sanitization

## 📊 Database Schema

The application uses the following main entities:

- **User**: User account information
- **Role**: User roles (USER, SELLER, ADMIN)
- **Product**: Product catalog
- **Category**: Product categories
- **Cart**: Shopping cart
- **CartItem**: Items in shopping cart
- **Order**: Customer orders
- **OrderItem**: Items in orders
- **Address**: User addresses
- **Payment**: Payment information

For detailed ER diagram, see [ER_DIAGRAM.md](./ER_DIAGRAM.md)

## 🧪 Testing

Run tests using Maven:
```bash
mvn test
```

## 📖 API Documentation

Once the application is running, visit:
- Swagger UI: `http://localhost:5000/swagger-ui.html`
- OpenAPI JSON: `http://localhost:5000/v3/api-docs`

## 🚀 Deployment

### Using Docker (Optional)
1. Create a Dockerfile
2. Build the image: `docker build -t ecommerce-api .`
3. Run: `docker run -p 5000:5000 ecommerce-api`

### Environment Variables
For production deployment, use environment variables:
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `SPRING_APP_JWT_SECRET`

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📝 Author

Hitendra Singh

## 📞 Support

For support and questions, please create an issue in the repository.

- **Product Management**
  - CRUD operations for products
  - Category management
  - Product image storage

- **Shopping Cart**
  - Add/remove items from cart
  - Update item quantities
  - Cart persistence per user

- **Order Management**
  - Place orders from cart
  - Order history tracking
  - Payment processing integration

- **User Profile**
  - Address management
  - Multiple shipping addresses support
  - User profile management

## 🛠️ Technology Stack

- **Java 21**
- **Spring Boot 3.5.5**
- **Spring Security** - Authentication & Authorization
- **Spring Data JPA** - Data persistence
- **PostgreSQL** - Database
- **JWT (JSON Web Tokens)** - Secure authentication
- **Lombok** - Reduce boilerplate code
- **ModelMapper** - Object mapping
- **Swagger/OpenAPI** - API documentation
- **Maven** - Dependency management

## 📋 Prerequisites

- Java 21 or higher
- Maven 3.6+
- PostgreSQL 12+
- Your favorite IDE (IntelliJ IDEA, Eclipse, VS Code)

## ⚙️ Installation & Setup

### 1. Clone the repository

```bash
git clone <repository-url>
cd Ecommerce
```

### 2. Configure PostgreSQL Database

Create a PostgreSQL database:

```sql
CREATE DATABASE Ecommerce;
```

### 3. Update application.properties

Configure your database credentials in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/Ecommerce
spring.datasource.username=your_username
spring.datasource.password=your_password
```

**Security Note**: For production, update the JWT secret:

```properties
spring.app.jwtSecret=your_secure_secret_key_here
spring.app.jwtExpirationMs=3000000
```

### 4. Build the project

```bash
./mvnw clean install
```

### 5. Run the application

```bash
./mvnw spring-boot:run
```

Alternatively, run the JAR file:

```bash
java -jar target/Ecommerce-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:5000`

## 📚 API Documentation

Once the application is running, access the Swagger UI documentation at:

```
http://localhost:5000/swagger-ui.html
```

### Main API Endpoints

#### Authentication
- `POST /api/auth/signup` - Register a new user
- `POST /api/auth/signin` - Login and receive JWT token
- `POST /api/auth/signout` - Logout

#### Products
- `GET /api/products` - Get all products
- `GET /api/products/{id}` - Get product by ID
- `POST /api/products` - Create new product (ADMIN)
- `PUT /api/products/{id}` - Update product (ADMIN)
- `DELETE /api/products/{id}` - Delete product (ADMIN)

#### Categories
- `GET /api/categories` - Get all categories
- `GET /api/categories/{id}` - Get category by ID
- `POST /api/categories` - Create new category (ADMIN)
- `PUT /api/categories/{id}` - Update category (ADMIN)
- `DELETE /api/categories/{id}` - Delete category (ADMIN)

#### Cart
- `GET /api/cart` - Get user's cart
- `POST /api/cart/products/{productId}` - Add product to cart
- `PUT /api/cart/products/{productId}` - Update cart item quantity
- `DELETE /api/cart/products/{productId}` - Remove item from cart

#### Orders
- `GET /api/orders` - Get user's orders
- `GET /api/orders/{id}` - Get order by ID
- `POST /api/orders` - Place new order
- `PUT /api/orders/{id}` - Update order status (ADMIN)

#### Addresses
- `GET /api/addresses` - Get user's addresses
- `POST /api/addresses` - Add new address
- `PUT /api/addresses/{id}` - Update address
- `DELETE /api/addresses/{id}` - Delete address

## 🔐 Authentication

The API uses JWT (JSON Web Tokens) for authentication. After successful login, you'll receive a JWT token that should be included in subsequent requests.

### Using JWT Token

Include the token in the request header:

```
Authorization: Bearer <your_jwt_token>
```

Alternatively, the token is also stored in a cookie named `SpringBootEcom` for automatic authentication.

## 🗂️ Project Structure

```
src/main/java/com/hitendra/ecommerce/
├── config/              # Configuration classes
│   ├── AppConfig.java
│   ├── AppConstants.java
│   └── SwaggerConfig.java
├── controller/          # REST API endpoints
│   ├── AddressController.java
│   ├── AuthController.java
│   ├── CartController.java
│   ├── CategoryController.java
│   ├── OrderController.java
│   └── ProductController.java
├── exceptions/          # Custom exceptions & handlers
│   ├── APIException.java
│   ├── MyGlobalExceptionHandler.java
│   └── ResourceNotFoundException.java
├── model/              # JPA entities
│   ├── Address.java
│   ├── AppRole.java
│   ├── Cart.java
│   ├── CartItem.java
│   ├── Category.java
│   ├── Order.java
│   ├── OrderItem.java
│   ├── Payment.java
│   ├── Product.java
│   ├── Role.java
│   └── User.java
├── payload/            # DTOs and request/response objects
├── repository/         # Spring Data JPA repositories
├── security/           # Security configuration & JWT utilities
└── service/            # Business logic layer
```

## 🔧 Configuration

### Application Properties

Key configurations in `application.properties`:

- **Server Port**: `5000`
- **Database**: PostgreSQL
- **JPA**: Auto-create schema on startup
- **JWT**: Token expiration set to 50 minutes (3000000 ms)
- **Logging**: Configurable levels for debugging

### Initial Data

The application uses `data.sql` for initial database seeding. Modify this file to customize initial roles, categories, or users.

## 🧪 Testing

Run the test suite:

```bash
./mvnw test
```

## 🚢 Deployment

### Building for Production

1. Update `application.properties` with production database credentials
2. Change `spring.jpa.hibernate.ddl-auto` to `validate` or `update`
3. Build the production JAR:

```bash
./mvnw clean package -DskipTests
```

4. Run the JAR:

```bash
java -jar target/Ecommerce-0.0.1-SNAPSHOT.jar
```

### Docker Deployment (Optional)

Create a `Dockerfile`:

```dockerfile
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/Ecommerce-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 5000
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Build and run:

```bash
docker build -t ecommerce-api .
docker run -p 5000:5000 ecommerce-api
```

## 📝 Environment Variables

For production, use environment variables instead of hardcoding credentials:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.app.jwtSecret=${JWT_SECRET}
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request


## 👤 Author

**Hitendra Singh**

## 🐛 Known Issues

- Ensure PostgreSQL is running before starting the application
- Update JWT secret for production use
- Configure CORS settings for frontend integration

## 📞 Support

For support, email hitendras940@gmail.com or open an issue in the repository.

## 🎯 Future Enhancements

- [ ] Product reviews and ratings
- [ ] Wishlist functionality
- [ ] Advanced search and filtering
- [ ] Email notifications
- [ ] Payment gateway integration
- [ ] Admin dashboard
- [ ] Product inventory management
- [ ] Discount and coupon system
- [ ] Order tracking

---

**Happy Coding! 🎉**

