Hệ thống backend REST API cho ứng dụng thương mại điện tử MakotoDecor - một nền tảng quản lý và bán hàng trang trí nội thất.

## 📋 Mục Lục

- [Tổng Quan](#tổng-quan)
- [Công Nghệ](#công-nghệ)
- [Tính Năng](#tính-năng)
- [Kiến Trúc](#kiến-trúc)
- [Cơ Sở Dữ Liệu](#cơ-sở-dữ-liệu)
- [API Endpoints](#api-endpoints)
- [Cài Đặt Môi Trường](#cài-đặt-môi-trường)
- [Chạy Dự Án](#chạy-dự-án)
- [Triển Khai](#triển-khai)
- [Tài Liệu API](#tài-liệu-api)

## 🎯 Tổng Quan

MakotoDecor Backend là một REST API được xây dựng bằng Spring Boot, cung cấp các dịch vụ backend hoàn chỉnh cho một hệ thống thương mại điện tử bao gồm quản lý sản phẩm, xác thực người dùng, giỏ hàng, đơn hàng và tải lên hình ảnh.

## 🚀 Công Nghệ

### Core Framework & Language

- **Java 17** - Programming language
- **Spring Boot 3.5.6** - Application framework
- **Maven** - Dependency management & build tool

### Security & Authentication

- **Spring Security** - Security framework
- **JWT (JSON Web Tokens)** - Token-based authentication
  - jjwt 0.12.3 - JWT implementation

### Database & Persistence

- **PostgreSQL** - Production database
- **Spring Data JPA** - Data access layer
- **Hibernate** - ORM framework
- **Flyway** - Database migration tool
- **QueryDSL 5.1.0** - Type-safe query builder

### API & Documentation

- **OpenAPI 3.0** - API specification
- **SpringDoc OpenAPI 2.7.0** - API documentation
- **Swagger UI** - Interactive API documentation

### Cloud Services

- **Cloudinary** - Cloud-based image management and CDN

### Development Tools

- **Lombok** - Boilerplate code reduction
- **MapStruct 1.5.3** - Object mapping
- **Spring Boot DevTools** - Development utilities (optional)
- **Spring Boot Configuration Processor** - Configuration metadata

### Other Libraries

- **Jackson Databind Nullable** - Enhanced JSON processing

## ✨ Tính Năng

### 🔐 Authentication & Authorization

- Đăng ký người dùng với validation đầy đủ
- Đăng nhập với JWT token (Access + Refresh token)
- Refresh token để gia hạn phiên làm việc
- Đổi mật khẩu cho người dùng đã đăng nhập
- Phân quyền theo vai trò (USER, STAFF, ADMIN)
- Bảo mật API với Spring Security

### 🛍️ Product Management

- Lấy danh sách sản phẩm (có phân trang và lọc)
- Xem chi tiết sản phẩm
- Tạo sản phẩm mới (Admin)
- Cập nhật thông tin sản phẩm (Admin)
- Quản lý trạng thái sản phẩm (ACTIVE, INACTIVE, OUT_OF_STOCK, DISCONTINUED)
- Quản lý giá theo màu sắc và kích thước
- Quản lý hình ảnh sản phẩm theo loại

### 🖼️ File Upload

- Upload hình ảnh đơn lẻ
- Upload nhiều hình ảnh cùng lúc
- Tích hợp Cloudinary để lưu trữ và tối ưu hóa hình ảnh
- Hỗ trợ tổ chức file theo thư mục

### 🏷️ Category & Classification

- Quản lý danh mục sản phẩm
- Quản lý loại hình ảnh
- Phân loại sản phẩm theo category
- Quản lý trạng thái category

### 🛒 Shopping Cart (Database Schema Ready)

- Cấu trúc database cho giỏ hàng
- Quản lý cart items
- Liên kết với user

### 📦 Order Management (Database Schema Ready)

- Cấu trúc database cho đơn hàng
- Quản lý trạng thái đơn hàng (NEW, DEPOSITED, CANCELLED, COMPLETED)
- Quản lý order items
- Lịch sử đơn hàng

### 🌍 Internationalization (i18n)

- Hỗ trợ đa ngôn ngữ (Tiếng Anh & Tiếng Việt)
- Messages tùy chỉnh cho validation và error
- Dễ dàng mở rộng thêm ngôn ngữ mới

### 📊 Additional Features

- Access counting
- Pagination utilities
- Comprehensive exception handling
- CORS configuration
- API rate limiting ready

## 🏗️ Kiến Trúc

### Layered Architecture

```
┌─────────────────────────────────────┐
│         Controller Layer            │  ← REST API Endpoints
│    (AuthController, ProductController, etc.)
├─────────────────────────────────────┤
│          Service Layer              │  ← Business Logic
│   (AuthService, ProductService, etc.)
├─────────────────────────────────────┤
│        Repository Layer             │  ← Data Access (JPA)
│  (UserRepository, ProductRepository, etc.)
├─────────────────────────────────────┤
│         Entity Layer                │  ← Domain Models
│    (User, Product, Order, etc.)
└─────────────────────────────────────┘
```

### Package Structure

```
com.makotodecor/
├── config/              # Configuration classes
│   ├── security/        # Security configurations
│   ├── SecurityConfig   # Main security config
│   ├── CloudinaryConfig # Cloudinary setup
│   ├── CorsConfig       # CORS settings
│   ├── I18nConfiguration# Internationalization
│   ├── QuerydslConfig   # QueryDSL setup
│   └── SwaggerConfig    # API documentation config
├── controller/          # REST Controllers
├── service/            # Business logic
│   └── impl/           # Service implementations
├── repository/         # Data access layer
├── model/              # Data models
│   ├── entity/         # JPA entities
│   ├── dto/            # Data transfer objects
│   ├── enums/          # Enumerations
│   ├── type/           # Custom types
│   └── converter/      # Type converters
├── mapper/             # MapStruct mappers
├── exceptions/         # Custom exceptions
│   └── base/           # Base exception classes
└── util/               # Utility classes
```

### Code Generation

- **OpenAPI Generator**: Tự động generate API interfaces từ OpenAPI spec
- **QueryDSL**: Tự động generate Q-classes cho type-safe queries
- **MapStruct**: Tự động generate mapper implementations
- **Lombok**: Compile-time code generation cho boilerplate

## 🗄️ Cơ Sở Dữ Liệu

### Database: PostgreSQL

### Schema Overview

#### Core Tables

- **users** - Thông tin người dùng và authentication
- **products** - Thông tin sản phẩm
- **categories** - Danh mục sản phẩm
- **colors** - Màu sắc sản phẩm (với giá theo màu)
- **sizes** - Kích thước sản phẩm (với giá theo size)
- **imgs** - Hình ảnh sản phẩm
- **img_types** - Loại hình ảnh (thumbnail, detail, etc.)

#### E-commerce Tables

- **carts** - Giỏ hàng
- **cart_items** - Sản phẩm trong giỏ hàng
- **orders** - Đơn hàng
- **order_items** - Sản phẩm trong đơn hàng

#### Utility Tables

- **access_counts** - Theo dõi lượt truy cập

### Custom PostgreSQL Enums

- `role`: USER, STAFF, ADMIN
- `order_status`: NEW, DEPOSITED, CANCELLED, COMPLETED
- `product_status`: ACTIVE, INACTIVE, OUT_OF_STOCK, DISCONTINUED
- `category_status`: ACTIVE, INACTIVE
- `img_type_status`: ACTIVE, INACTIVE
- `user_status`: ACTIVE, INACTIVE

### Database Migration

- Sử dụng **Flyway** để quản lý database versioning
- Migration files: `src/main/resources/db/migration/`
- Tự động chạy khi khởi động ứng dụng

## 📡 API Endpoints

### Authentication Service

| Method | Endpoint                       | Description           | Auth Required |
| ------ | ------------------------------ | --------------------- | ------------- |
| POST   | `/v1/api/auth/register`        | Đăng ký tài khoản mới | ❌            |
| POST   | `/v1/api/auth/login`           | Đăng nhập             | ❌            |
| POST   | `/v1/api/auth/refresh`         | Làm mới access token  | ❌            |
| POST   | `/v1/api/auth/change-password` | Đổi mật khẩu          | ✅            |

### Product Service

| Method | Endpoint                       | Description                         | Auth Required |
| ------ | ------------------------------ | ----------------------------------- | ------------- |
| GET    | `/v1/api/products`             | Lấy danh sách sản phẩm (phân trang) | ❌            |
| GET    | `/v1/api/products/{productId}` | Lấy chi tiết sản phẩm               | ❌            |
| POST   | `/v1/api/products`             | Tạo sản phẩm mới                    | ✅ (Admin)    |
| PUT    | `/v1/api/products/{productId}` | Cập nhật sản phẩm                   | ✅ (Admin)    |

**Query Parameters cho GET /products:**

- `page`: Số trang (default: 0)
- `size`: Kích thước trang (default: 20)
- `orderBy`: Sắp xếp theo trường
- `name`: Tìm kiếm theo tên
- `status`: Lọc theo trạng thái
- `category`: Lọc theo danh mục

### File Upload Service

| Method | Endpoint                        | Description       | Auth Required |
| ------ | ------------------------------- | ----------------- | ------------- |
| POST   | `/v1/api/files/upload`          | Upload một file   | ✅            |
| POST   | `/v1/api/files/upload/multiple` | Upload nhiều file | ✅            |

### Image Type Service

| Method | Endpoint            | Description                 | Auth Required |
| ------ | ------------------- | --------------------------- | ------------- |
| GET    | `/v1/api/img-types` | Lấy danh sách loại hình ảnh | ✅            |

### API Documentation Endpoints

- **Swagger UI**: `/swagger-ui.html`
- **OpenAPI Docs**: `/api-docs`
- **API Docs JSON**: `/v3/api-docs`

## ⚙️ Cài Đặt Môi Trường

### Prerequisites

- **Java 17** hoặc cao hơn
- **Maven 3.6+** (hoặc sử dụng Maven wrapper đi kèm)
- **PostgreSQL 12+**
- **Git**

### Environment Variables

Tạo file `.env` hoặc cấu hình các biến môi trường sau:

#### Database Configuration

```properties
DATABASE_URL=jdbc:postgresql://[HOST]:[PORT]/[DATABASE_NAME]
DATABASE_USERNAME=your_db_username
DATABASE_PASSWORD=your_db_password
```

#### JWT Configuration

```properties
JWT_SECRET=your_jwt_secret_key_here_at_least_256_bits
JWT_EXPIRATION=900000
JWT_REFRESH_EXPIRATION=604800000
JWT_EXPIRATION_HOURS=24
```

#### CORS Configuration

```properties
CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:5173
CORS_ALLOWED_METHODS=GET,POST,PUT,DELETE,OPTIONS
CORS_ALLOWED_HEADERS=*
CORS_ALLOW_CREDENTIALS=true
CORS_MAX_AGE=3600
```

#### Cloudinary Configuration

```properties
CLOUDINARY_CLOUD_NAME=your_cloud_name
CLOUDINARY_API_KEY=your_api_key
CLOUDINARY_API_SECRET=your_api_secret
CLOUDINARY_BASE_FOLDER=makotodecor
```

**⚠️ Lưu ý:**

- KHÔNG commit file chứa thông tin nhạy cảm vào Git
- Sử dụng `.gitignore` để loại trừ các file chứa secrets
- Trong production, sử dụng secret management service

### Local Development Setup

Để phát triển local, bạn có thể tạo file `application-local.properties` với các giá trị cấu hình riêng và active profile `local`:

```bash
# Run with local profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

## 🏃 Chạy Dự Án

### 1. Clone Repository

```bash
git clone <repository-url>
cd makotodecor
```

### 2. Configure Database

Đảm bảo PostgreSQL đang chạy và tạo database:

```sql
CREATE DATABASE makotodecor;
```

### 3. Set Environment Variables

Cấu hình các biến môi trường như mô tả ở [phần trên](#environment-variables).

### 4. Build Project

```bash
# Clean và build project
./mvnw clean package

# Hoặc skip tests nếu cần
./mvnw clean package -DskipTests
```

### 5. Run Application

```bash
# Chạy với Maven
./mvnw spring-boot:run

# Hoặc chạy JAR file
java -jar target/makotodecor-0.0.1-SNAPSHOT.jar
```

### 6. Verify Installation

- Application: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- API Docs: `http://localhost:8080/api-docs`

## 🐳 Chạy với Docker

### Build Docker Image

```bash
docker build -t makotodecor-backend .
```

### Run Container

```bash
docker run -p 8080:8080 \
  -e DATABASE_URL=your_db_url \
  -e DATABASE_USERNAME=your_username \
  -e DATABASE_PASSWORD=your_password \
  -e JWT_SECRET=your_jwt_secret \
  # ... other environment variables
  makotodecor-backend
```

## 🚀 Triển Khai

### Render.com Deployment

Project đã được cấu hình sẵn để deploy lên Render.com với file `render.yaml`.

**Steps:**

1. Push code lên GitHub repository
2. Kết nối repository với Render.com
3. Render sẽ tự động detect `render.yaml` và tạo service
4. Cấu hình Environment Variables trong Render Dashboard:
   - Database configuration
   - JWT secrets
   - Cloudinary credentials
   - CORS settings

**Build Command:** `./mvnw clean package -DskipTests`

**Start Command:** `java -jar target/makotodecor-0.0.1-SNAPSHOT.jar`

### Other Cloud Platforms

Project có thể deploy lên các platform khác như:

- **Heroku**: Sử dụng `Procfile`
- **AWS**: Elastic Beanstalk hoặc ECS
- **Google Cloud**: App Engine hoặc Cloud Run
- **Azure**: App Service

## 📚 Tài Liệu API

### Swagger Documentation

Sau khi chạy ứng dụng, truy cập Swagger UI tại:

```
http://localhost:8080/swagger-ui.html
```

### OpenAPI Specification

API specification được định nghĩa tại:

```
src/main/resources/api-yamls/makotodecor.yaml
```

### Testing API

Sử dụng Swagger UI hoặc tools như:

- **Postman**: Import OpenAPI spec
