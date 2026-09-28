# ShareSphere — AI-Enabled Campus Asset Exchange & Rental Platform

> **Share More. Spend Less.**  
> A full-stack Java Spring Boot marketplace for campus students.

---

## 🚀 Features

| Feature | Details |
|---|---|
| 🔐 Auth | JWT-based register/login, BCrypt passwords, role-based access |
| 📦 Listings | Buy / Sell / Rent / Lend with images and categories |
| 🤖 AI Search | Natural language → smart filters + recommendations |
| 🔄 Rentals | Full workflow: REQUESTED → ACCEPTED → ACTIVE → COMPLETED |
| 🛒 Orders | Buy items with simulated payment confirmation |
| 💬 Messages | 1:1 user conversations |
| 🔔 Notifications | Real-time in-app alerts for all events |
| ❤️ Favorites | Save and track preferred items |
| ⭐ Reviews | Rate items and users after transactions |
| 🛡️ Admin | Dashboard with stats, user management, moderation |
| 📊 14 Categories | Electronics, Books, Cycles, Lab Equipment, and more |

---

## 🛠 Technology Stack

- **Backend**: Java 21, Spring Boot 3.3.5, Spring Security, Spring Data JPA
- **Database**: MySQL 8+
- **Auth**: JWT (JJWT 0.12.x), BCrypt
- **Frontend**: Vanilla HTML/CSS/JS (dark theme, responsive)
- **AI**: Java rule-based NLP engine (plug-in interface for OpenAI/Gemini)
- **Build**: Maven

---

## 📁 Project Structure

```
sharesphere/
├── src/main/java/com/sharesphere/
│   ├── ai/             # AI recommendation interface + rule-based impl
│   ├── config/         # Security, WebMvc, DataSeeder
│   ├── controller/     # REST controllers
│   ├── dto/            # Request + Response DTOs
│   ├── entity/         # JPA entities + enums
│   ├── exception/      # Global handler + custom exceptions
│   ├── repository/     # Spring Data JPA repositories
│   ├── security/       # JWT filter + UserDetailsService
│   └── service/        # Interfaces + implementations
├── src/main/resources/
│   ├── static/         # Frontend HTML, CSS, JS
│   └── application.properties
└── pom.xml
```

---

## ⚙️ Prerequisites

- Java 21+
- Maven 3.9+
- MySQL 8+

---

## 🗄️ Database Setup

```sql
CREATE DATABASE sharesphere;
-- App creates tables automatically via JPA ddl-auto=update
```

---

## 🔧 Configuration

Edit `src/main/resources/application.properties` or set environment variables:

| Variable | Default | Description |
|---|---|---|
| `DB_URL` | `jdbc:mysql://localhost:3306/sharesphere?...` | MySQL URL |
| `DB_USERNAME` | `root` | DB username |
| `DB_PASSWORD` | `password` | DB password |
| `JWT_SECRET` | (built-in default) | JWT signing key |
| `UPLOAD_DIR` | `uploads` | File upload directory |
| `AI_API_KEY` | *(empty)* | Optional external AI API key |

---

## ▶️ How to Run

```bash
# 1. Clone / navigate to project
cd sharesphere

# 2. Make sure MySQL is running and database exists

# 3. Build
mvn clean install

# 4. Run
mvn spring-boot:run

# 5. Open browser
open http://localhost:8080
```

---

## 🔑 Demo Credentials (auto-seeded)

| Role | Email | Password |
|---|---|---|
| Admin | admin@sharesphere.com | Admin@123 |
| Student | rahul@college.edu | Student@123 |
| Student | priya@college.edu | Student@123 |
| Student | arjun@college.edu | Student@123 |
| Student | sneha@college.edu | Student@123 |
| Student | dev@college.edu | Student@123 |

---

## 📡 Key API Endpoints

```
POST /api/auth/register       — Register
POST /api/auth/login          — Login (returns JWT)

GET  /api/items/search        — Search/filter items
POST /api/items               — Create listing
POST /api/items/{id}/images   — Upload image

POST /api/rentals             — Request rental
POST /api/rentals/{id}/accept — Accept rental
POST /api/rentals/{id}/return — Request return
POST /api/rentals/{id}/complete — Complete rental

POST /api/orders              — Buy item
POST /api/orders/{id}/pay     — Confirm payment

GET  /api/ai/search?query=... — AI natural language search
GET  /api/ai/recommendations  — Personalised recommendations

GET  /api/admin/stats         — Platform statistics (ADMIN only)
```

---

## 🤖 AI Search Examples

```
"I need a calculator for my semester exam"
→ Category: Calculators, Listing: SELL_AND_RENT

"camera for 3 days near hostel"
→ Category: Electronics, Type: RENT, Duration: 3, Location: hostel

"cheap engineering drawing kit"
→ Category: Engineering Tools, maxPrice derived
```

---

## 🧪 Run Tests

```bash
mvn test
```

---

## 🚀 Demo Flow

1. Open http://localhost:8080
2. Click **Get Started** → Register as a student
3. Browse items or use **AI Search**
4. Click an item → **Rent** or **Buy**
5. Login as another student → Accept/Reject in Rentals
6. Track status in Dashboard

---

## 📸 Screenshots

> Add screenshots here after first run.

---

## 🔮 Future Improvements

- WebSocket real-time chat
- Google OAuth2 login
- UPI/Razorpay payment integration
- Push notifications
- Mobile app (React Native)
- External LLM AI integration (Gemini/OpenAI)
