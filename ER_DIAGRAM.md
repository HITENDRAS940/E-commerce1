# E-Commerce Application - Entity Relationship Diagram

## Overview
This document describes the complete entity relationship diagram for the E-Commerce Spring Boot application.

## Entities and Relationships

```
                                    ER DIAGRAM - ECOMMERCE APPLICATION

                    ┌─────────────────────────────────────────────────────────────┐
                    │                          USER                               │
                    │─────────────────────────────────────────────────────────────│
                    │ PK │ user_id: Long                                         │
                    │    │ username: String (UNIQUE, NOT NULL)                   │
                    │    │ email: String (UNIQUE, NOT NULL)                      │
                    │    │ password: String (NOT NULL)                           │
                    └─────────────────────────────────────────────────────────────┘
                              │                    │                    │
                              │                    │                    │
                        ┌─────▼─────┐       ┌─────▼─────┐       ┌─────▼─────┐
                        │ USER_ROLE │       │  ADDRESS  │       │    CART   │
                        │           │       │           │       │           │
                        └─────┬─────┘       └─────┬─────┘       └─────┬─────┘
                              │                   │                   │
                        ┌─────▼─────┐             │             ┌─────▼─────┐
                        │   ROLE    │             │             │ CART_ITEM │
                        │           │             │             │           │
                        └───────────┘             │             └─────┬─────┘
                                                  │                   │
                    ┌─────────────────────────────▼─────────────────▼─┐
                    │                       PRODUCT                   │
                    │─────────────────────────────────────────────────│
                    │ PK │ product_id: Long                           │
                    │    │ product_name: String (NOT NULL)            │
                    │    │ image: String                              │
                    │    │ description: String (NOT NULL)             │
                    │    │ quantity: Integer                          │
                    │    │ price: Double                              │
                    │    │ discount: Double                           │
                    │    │ special_price: Double                      │
                    │ FK │ category_id: Long                          │
                    │ FK │ seller_id: Long (References User)          │
                    └─────────────────────────────────────────────────┘
                              │                    │
                              │                    │
                        ┌─────▼─────┐       ┌─────▼─────┐
                        │ CATEGORY  │       │ORDER_ITEM │
                        │           │       │           │
                        └───────────┘       └─────┬─────┘
                                                  │
                                            ┌─────▼─────┐
                                            │   ORDER   │
                                            │           │
                                            └─────┬─────┘
                                                  │
                                            ┌─────▼─────┐
                                            │  PAYMENT  │
                                            │           │
                                            └───────────┘
```

## Detailed Entity Descriptions

### 1. USER Entity
- **Primary Key**: user_id (Auto-generated)
- **Unique Constraints**: username, email
- **Relationships**:
  - Many-to-Many with ROLE (through USER_ROLE junction table)
  - One-to-Many with ADDRESS
  - One-to-One with CART
  - One-to-Many with PRODUCT (as seller)

### 2. ROLE Entity
- **Primary Key**: role_id (Auto-generated)
- **Enum Values**: ROLE_USER, ROLE_SELLER, ROLE_ADMIN
- **Relationships**:
  - Many-to-Many with USER (through USER_ROLE junction table)

### 3. ADDRESS Entity
- **Primary Key**: address_id (Auto-generated)
- **Fields**: street, building_name, city, state, country, pincode
- **Relationships**:
  - Many-to-One with USER
  - One-to-Many with ORDER

### 4. CATEGORY Entity
- **Primary Key**: category_id (Auto-generated)
- **Fields**: category_name
- **Relationships**:
  - One-to-Many with PRODUCT

### 5. PRODUCT Entity
- **Primary Key**: product_id (Auto-generated)
- **Fields**: product_name, image, description, quantity, price, discount, special_price
- **Relationships**:
  - Many-to-One with CATEGORY
  - Many-to-One with USER (seller)
  - One-to-Many with CART_ITEM
  - One-to-Many with ORDER_ITEM

### 6. CART Entity
- **Primary Key**: cart_id (Auto-generated)
- **Fields**: total_price
- **Relationships**:
  - One-to-One with USER
  - One-to-Many with CART_ITEM

### 7. CART_ITEM Entity
- **Primary Key**: cart_item_id (Auto-generated)
- **Fields**: quantity, discount, product_price
- **Relationships**:
  - Many-to-One with CART
  - Many-to-One with PRODUCT

### 8. ORDER Entity
- **Primary Key**: order_id (Auto-generated)
- **Fields**: email, order_date, order_price, order_status
- **Relationships**:
  - One-to-Many with ORDER_ITEM
  - One-to-One with PAYMENT
  - Many-to-One with ADDRESS

### 9. ORDER_ITEM Entity
- **Primary Key**: order_item_id (Auto-generated)
- **Fields**: quantity, discount, ordered_product_price
- **Relationships**:
  - Many-to-One with PRODUCT
  - Many-to-One with ORDER

### 10. PAYMENT Entity
- **Primary Key**: payment_id (Auto-generated)
- **Fields**: payment_method, pg_gateway, pg_payment_id, pg_status, pg_response_message, pg_name
- **Relationships**:
  - One-to-One with ORDER

## Relationship Types and Cardinalities

### Many-to-Many Relationships
1. **USER ↔ ROLE**
   - Junction Table: USER_ROLE
   - A user can have multiple roles (USER, SELLER, ADMIN)
   - A role can be assigned to multiple users

### One-to-Many Relationships
1. **USER → ADDRESS**
   - One user can have multiple addresses
   - Each address belongs to one user

2. **USER → PRODUCT** (as seller)
   - One seller can have multiple products
   - Each product has one seller

3. **CATEGORY → PRODUCT**
   - One category can contain multiple products
   - Each product belongs to one category

4. **CART → CART_ITEM**
   - One cart can have multiple cart items
   - Each cart item belongs to one cart

5. **PRODUCT → CART_ITEM**
   - One product can be in multiple cart items
   - Each cart item references one product

6. **ORDER → ORDER_ITEM**
   - One order can have multiple order items
   - Each order item belongs to one order

7. **PRODUCT → ORDER_ITEM**
   - One product can be in multiple order items
   - Each order item references one product

8. **ADDRESS → ORDER**
   - One address can be used for multiple orders
   - Each order has one delivery address

### One-to-One Relationships
1. **USER ↔ CART**
   - Each user has exactly one cart
   - Each cart belongs to exactly one user

2. **ORDER ↔ PAYMENT**
   - Each order has exactly one payment
   - Each payment is for exactly one order

## Business Rules

1. **User Authentication**:
   - Username and email must be unique
   - Default role is ROLE_USER if no roles specified during signup

2. **Product Management**:
   - Products must belong to a category
   - Products have a seller (user with SELLER role)
   - Special price is calculated based on price and discount

3. **Shopping Cart**:
   - Each user automatically gets a cart upon registration
   - Cart items track quantity and pricing information

4. **Order Processing**:
   - Orders contain multiple order items
   - Each order must have a delivery address
   - Orders are linked to payment information

5. **Payment Processing**:
   - Payment gateway integration for transaction processing
   - Payment status tracking and response handling

## Database Tables Summary

1. **users** - User authentication and profile data
2. **roles** - System roles (USER, SELLER, ADMIN)
3. **user_role** - Junction table for user-role relationships
4. **addresses** - User delivery addresses
5. **categories** - Product categories
6. **products** - Product catalog
7. **carts** - User shopping carts
8. **cart_items** - Items in shopping carts
9. **orders** - Customer orders
10. **order_item** - Items within orders
11. **payment** - Payment transaction details

This ER diagram represents a complete e-commerce system with user management, product catalog, shopping cart, order processing, and payment handling capabilities.
