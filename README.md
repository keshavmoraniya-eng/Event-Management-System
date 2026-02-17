# Event Management System - Spring Boot Backend

## 🎯 Project Overview
Complete REST API backend for an Event Management System built with Spring Boot, MySQL, and JWT authentication.

## 🏗️ Architecture
```
├── Model Layer (Entities)
├── Repository Layer (JPA)
├── Service Layer (Business Logic)
├── Controller Layer (REST APIs)
├── Security Layer (JWT + Spring Security)
└── Exception Handling (Global)
```

## 📦 Tech Stack
- **Framework:** Spring Boot 3.2.0
- **Security:** Spring Security + JWT
- **Database:** MySQL
- **ORM:** JPA/Hibernate
- **Build Tool:** Maven
- **Java Version:** 17

## 🚀 Quick Start

### Prerequisites
- JDK 17+
- MySQL 8.0+
- Maven 3.6+

### 1. Database Setup
```sql
mysql -u root -p
CREATE DATABASE event_management_db;
```

### 2. Configure Application
Update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/event_management_db?createDatabaseIfNotExist=true
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD

jwt.secret=YOUR_JWT_SECRET_KEY_HERE_MUST_BE_LONG_ENOUGH
```

### 3. Initialize Roles
Run this SQL after first startup:
```sql
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');
INSERT INTO roles (name) VALUES ('ROLE_ORGANIZER');
INSERT INTO roles (name) VALUES ('ROLE_ATTENDEE');
```

### 4. Run Application
```bash
mvn clean install
mvn spring-boot:run
```

Server runs on: http://localhost:8080

## 📡 API Endpoints

### Authentication
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/api/auth/signup` | Register new user | Public |
| POST | `/api/auth/signin` | Login user | Public |

### Events
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/api/events/public` | Get all published events | Public |
| GET | `/api/events/public/{id}` | Get event by ID | Public |
| GET | `/api/events/public/search?keyword=` | Search events | Public |
| GET | `/api/events/public/upcoming` | Get upcoming events | Public |
| POST | `/api/events` | Create event | Organizer/Admin |
| PUT | `/api/events/{id}` | Update event | Organizer/Admin |
| DELETE | `/api/events/{id}` | Delete event | Organizer/Admin |
| GET | `/api/events/my-events` | Get my events | Organizer/Admin |

## 🔐 Authentication Flow

### 1. Register
```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe",
    "phoneNumber": "1234567890",
    "role": ["organizer"]
  }'
```

### 2. Login
```bash
curl -X POST http://localhost:8080/api/auth/signin \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "password123"
  }'
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "john_doe",
  "email": "john@example.com",
  "roles": ["ROLE_ORGANIZER"]
}
```

### 3. Use Token in Requests
```bash
curl -X POST http://localhost:8080/api/events \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "title": "Tech Conference 2024",
    "description": "Annual tech conference",
    "startDateTime": "2024-06-15T09:00:00",
    "endDateTime": "2024-06-15T17:00:00",
    "category": "Technology",
    "maxAttendees": 500,
    "isPublished": true
  }'
```

## 📊 Database Schema

### Core Tables
- **users** - User accounts
- **roles** - User roles (Admin, Organizer, Attendee)
- **user_roles** - Many-to-many relationship
- **events** - Event information
- **venues** - Venue details
- **tickets** - Ticket types for events
- **bookings** - User bookings
- **payments** - Payment transactions
- **notifications** - Email/SMS notifications

## 🔧 Project Structure
```
src/main/java/com/eventmanagement/
├── model/                  # Entity classes
│   ├── User.java
│   ├── Role.java
│   ├── Event.java
│   ├── Venue.java
│   ├── Ticket.java
│   ├── Booking.java
│   └── Payment.java
├── repository/             # JPA repositories
│   ├── UserRepository.java
│   ├── EventRepository.java
│   └── ...
├── service/                # Business logic
│   ├── EventService.java
│   └── ...
├── controller/             # REST controllers
│   ├── AuthController.java
│   ├── EventController.java
│   └── ...
├── dto/                    # Data Transfer Objects
│   ├── EventRequest.java
│   ├── EventResponse.java
│   └── ...
├── security/               # Security configuration
│   ├── WebSecurityConfig.java
│   ├── jwt/
│   └── services/
└── exception/              # Exception handling
    ├── GlobalExceptionHandler.java
    └── ...
```

## ✅ Completed Features
- ✅ User authentication & authorization (JWT)
- ✅ Role-based access control (Admin, Organizer, Attendee)
- ✅ Event CRUD operations
- ✅ Event search & filtering
- ✅ Database entities & relationships
- ✅ Exception handling
- ✅ CORS configuration

## 🚧 To-Do Features (Extend as needed)
- [ ] Booking system implementation
- [ ] Payment integration (Razorpay/Stripe)
- [ ] QR code generation for tickets
- [ ] Email notifications
- [ ] Venue management
- [ ] Admin dashboard analytics
- [ ] File upload for event images
- [ ] Rate limiting
- [ ] API documentation (Swagger)

## 📝 Testing

### Using Postman
1. Import the provided Postman collection
2. Create environment variables:
    - `baseUrl`: http://localhost:8080
    - `token`: (auto-set after login)

### Example Test Flow
1. Register a new organizer
2. Login to get JWT token
3. Create an event
4. Fetch all published events (public)
5. Update event details
6. Search for events

## 🐛 Common Issues

### Issue: "Table doesn't exist"
**Solution:** Ensure `spring.jpa.hibernate.ddl-auto=update` is set and MySQL is running.

### Issue: "JWT token expired"
**Solution:** Token expires in 24h by default. Login again to get new token.

### Issue: "403 Forbidden"
**Solution:** Check if you're using correct role and token in Authorization header.

## 🔐 Security Notes
- JWT secret should be strong and stored in environment variables in production
- Passwords are encrypted using BCrypt
- Endpoints are protected with role-based authorization
- CORS is configured for localhost:3000 (React) and localhost:5173 (Vite)

## 📚 Additional Resources
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [JWT.io](https://jwt.io/)
- [Spring Security](https://spring.io/projects/spring-security)

## 🤝 Next Steps
1. Complete booking & payment services
2. Add remaining controllers (Booking, Venue, Payment)
3. Implement notification system
4. Add unit & integration tests
5. Create API documentation with Swagger
6. Deploy to cloud (Heroku/AWS)

---

**Note:** This is the MVP backend. Extend with additional services and controllers as needed for complete functionality.