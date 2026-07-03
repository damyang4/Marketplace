# 🐝 Local Artisans Marketplace

A specialized full-stack e-commerce platform designed to connect local farmers and artisans directly with customers. The platform allows producers to list homemade and organic goods—such as raw honey, artisanal marmalades, and farm-fresh staples—while providing buyers with a smooth local-shopping experience.

## 📸 Screenshots
Note: A demo video will be available soon

| Landing Page | Product Catalog | Product Details |
| :---: | :---: | :---: |
| <img width="1901" height="820" alt="landing_page" src="https://github.com/user-attachments/assets/74a4fdc5-dc85-4dc7-ab9b-692ff06d09f0" /> | <img width="1919" height="811" alt="product_catalog" src="https://github.com/user-attachments/assets/e0739165-0888-4c9e-ad20-dffa30101b2c" /> | <img width="1919" height="846" alt="product_details" src="https://github.com/user-attachments/assets/0b63232c-09e0-4aa9-bd82-84bdbb0d2f99" /> |
| *Welcome landing page and core discovery layout* | *Filterable storefront hub for local artisanal goods* | *Dedicated product descriptions, parameter adjustments, and cart injection* |

| Navbar Mini-Cart | Full Shopping Cart | Login Page |
| :---: | :---: | :---: |
| <img width="1919" height="873" alt="mini_cart" src="https://github.com/user-attachments/assets/f32e056a-87aa-4459-a598-bfe5e4968bb9" /> | <img width="1919" height="555" alt="shopping_cart" src="https://github.com/user-attachments/assets/b03f2bbd-4965-4158-8073-ca309b32f0d3" /> | <img width="1901" height="863" alt="login" src="https://github.com/user-attachments/assets/fde0777c-dbc5-45cf-bf47-493e5bb1669e" /> |
| *Live item tracking from the global navigation bar* | *Comprehensive shopping cart dashboard with session token state tracking* | *Secure portal handling user login, logout, and token provisioning* |

| Add Product Dashboard | Stripe Payment Form | Order Success Receipt |
| :---: | :---: | :---: |
| <img width="1901" height="867" alt="add_product" src="https://github.com/user-attachments/assets/9ee20c2b-b009-46b3-b96f-77e4575f8925" /> | <img width="1083" height="867" alt="stripe_form" src="https://github.com/user-attachments/assets/64b7647e-54c6-4cbc-82a6-c9fc873794bd" /> | <img width="1919" height="659" alt="order_info" src="https://github.com/user-attachments/assets/7cc4c0ed-f3ed-4b30-8e00-2ede11e8cf68" /> |
| *Administrative product entry interface with multipart file upload* | *Secure checkout gateway backed by a decoupled backend service layer* | *Finalized transactional state locking in snapshot prices and inventory counts* |

## 👥 Team Contributions

### 💳 What I Built (@damyang4)
* **The Full Cart System**: I built both the quick-view mini cart in the navbar and the main checkout cart page. I also wrote the logic that lets guest users add items to their cart without being logged in, which automatically saves to their account once they sign in.

* **Stripe Payments & Orders**: I handled the integration with Stripe to process payments securely. Once a payment goes through, the backend automatically generates a clean order receipt, locks in the current item prices, updates stock counts, and links the purchase to the user's profile.

* **Dual-Token System**: I set up the security logic for the entire user lifecycle. If someone visits the shop anonymously, the system generates a temporary guest_token to track their session. Once they log in or register, the app issues a secure JWT token to authenticate them, seamlessly upgrading their session.

* **Login & Logout Flow**: I handled the complete backend and frontend logic for logging users in securely (including password hashing) and clearing their tokens and sessions safely upon logout.

### 🍯 What Maxim Built (@MaximHr)
* **Home Page & Product Flow**: Maxim designed the landing page and the core product discovery layout.

* **Product Catalog & Filters**: He built the database and frontend UI for the catalog, specifically focusing on the main products page where users can filter through local categories like honey, marmalades, and fresh farm goods.

* **Product Details Page**: He created the dedicated description page that opens when you click on a product. This page displays the item's details and lets users adjust quantities right before adding them to the cart.

* **Product Images & Cloud Storage**: He set up the entire cloud image workflow. He connected the backend to Cloudflare R2 storage, built the endpoint to accept multiple file uploads at once, and wrote the logic to assign unique filenames, stream images safely to the cloud, and link them to products in the database.

## 🛠️ Tech Stack
### Backend
- Java 23
- Spring Boot
- Spring Security
- Spring Data JPA (Hibernate)
- PostgreSQL
- JWT Authentication
- Maven

### Frontend
- Angular
- TypeScript
- HTML5
- CSS3

### Third-Party Services
- Stripe
- Cloudflare R2

## 🚀 How to Run the Project Locally

### 📋 Prerequisites
Before starting, ensure you have the following installed on your machine:
* **Java 23** SDK
* **Node.js** (LTS version recommended)
* **Angular CLI** (Install globally via `npm install -g @angular/cli`)
* **PostgreSQL** Server (Running locally on port 5432)

### 🗄️ Step 1: Database Setup
You do not need an external manager like pgAdmin to run this app, but your local PostgreSQL service must be active.
1. Open your terminal or a database tool and create an empty database named `marketplace`.
2. Ensure your local PostgreSQL service is running on its default port (`5432`).
3. Spring Data JPA will automatically generate all the required relational database tables on startup.

### 🔑 Step 2: Configure Environment Variables

The backend relies on external services for authentication, file storage, payment processing, and database access. Before running the application, create a .env file in the backend/ directory (or configure the same variables in your IDE's Run Configuration) with the following values:

```env
# Database Credentials
DB_URL=jdbc:postgresql://localhost:5432/marketplace
DB_USERNAME=your_postgres_username
DB_PASSWORD=your_postgres_password

# Cloudflare R2 Storage (S3-Compatible API)
R2_ACCESS_KEY=your_r2_api_access_key
R2_SECRET_ACCESS_KEY=your_r2_api_secret_access_key
R2_ENDPOINT=https://<your-cloudflare-account-id>.r2.cloudflarestorage.com
R2_BUCKET_NAME=your_bucket_name

# Security & CORS Settings
JWT_SECRET_KEY=your_super_secure_random_secret_key_at_least_32_characters_long
STORE_FRONT_URL=http://localhost:4200

# Stripe Processing Infrastructure
STRIPE_SECRET_KEY=sk_test_your_secret_stripe_test_key
STRIPE_WEBHOOK_SECRET=whsec_your_stripe_webhook_signing_secret
```
> ⚠️ **Important:** Make sure your `backend/src/main/resources/application.properties` file points to these placeholders using environmental references (e.g., `spring.datasource.password=${DB_PASSWORD}`). Never commit your actual secret keys to your public GitHub repository!

### 🍃 Step 3: Start the Backend
You can run the backend directly from your IDE or use the command line.

#### Option A: Inside IntelliJ IDEA (Recommended)
1. Navigate into the `backend/` folder and let IntelliJ load the `pom.xml` configuration file.
2. Open `src/main/java/com/fmi/springcourse/marketplace/ServerApplication.java`.
3. Click the green **Run** play button next to the `main` method.

#### Option B: Via the Terminal
Open your terminal, navigate directly to the backend directory, and run the Maven wrapper command:

```bash
cd backend
./mvnw spring-boot:run
(On Windows command prompt, use mvnw.cmd spring-boot:run)
```

### 🅰️ Step 4: Launch the Angular Frontend
Once the backend is securely up and running, open a new terminal window to launch the customer interface.

```bash
# Navigate to the frontend workspace in terminal
cd frontend

# Install the necessary dependencies
npm install

# Boot up the local Angular development server
ng serve
```

🎉 Open your browser and navigate to http://localhost:4200 to explore the local artisanal shop!
