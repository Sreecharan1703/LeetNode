<div align="center">

# LeetNode

**Wanna grind LeetCode but don't know where to start? Here's a gift for you.**

Predict a problem's rating, get problems picked for your level, and filter questions by rating.

### 🔗 [leetnode.my.to](https://leetnode.my.to)

![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?logo=springboot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?logo=mysql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?logo=docker&logoColor=white)
![Machine Learning](https://img.shields.io/badge/Machine%20Learning-2B579A?logo=python&logoColor=white)

</div>

---

## Table of contents

- [About](#about)
- [Features](#features)
- [Tech stack](#tech-stack)
- [User guide](#user-guide)
- [How it works](#how-it-works)
- [API reference](#api-reference)
- [Project structure](#project-structure)
- [Run it locally](#run-it-locally)
- [Deployment](#deployment)
- [Limitations](#limitations)
- [Author](#author)

---

## About

LeetCode shows Easy / Medium / Hard, but inside each of those labels the difficulty varies a lot. **LeetNode** gives every problem an estimated numeric rating (like a contest rating) so you can choose problems that match your level instead of guessing.

## Features

| | Feature | What it does |
|---|---|---|
| 🎯 | **Rating predictor** | Enter a LeetCode question number and get its estimated rating, difficulty, acceptance rate, likes, dislikes, submission counts and topic tags. |
| 🧭 | **Personal recommendations** | Enter your LeetCode username to see your contest stats and a list of problems matched to your rating (from `rating - 50` to `rating + 200`). |
| 🔎 | **Rating filter** | Pick a rating range and browse every problem inside it, 10 per page. |
| 🔗 | **One-click solve** | Every problem has a **Solve** button that opens it on LeetCode. |
| 📊 | **View counter** | Total views and views today, stored in the database so restarts don't reset them. |
| 🌗 | **Light and dark mode** | switch between website themes. |
| 📱 | **Responsive** | Works on phones, tablets and desktops. |

## Tech stack

**Backend**

| Technology | Used for |
|---|---|
| **Java 17** | Language |
| **Spring Boot** | Application framework, REST APIs, embedded Tomcat |
| **Spring Data JPA / Hibernate** | Database access and automatic schema updates |
| **MySQL** | Relational database for questions and view counts |
| **Spring Cache (`@Cacheable`)** | Faster repeated lookups |
| **Thymeleaf** | Server-rendered pages and shared error pages |
| **Docker** | Packaging and deployment |

**Frontend (Made By AI)**

| Technology | Used for |
|---|---|
| **HTML5 + CSS3** | Layout and styling (CSS variables, grid, flexbox, `prefers-color-scheme`) |
| **Vanilla JavaScript** | `fetch` calls to the REST API, pagination, input validation |
| **Google Fonts** | Bricolage Grotesque (headings) and Instrument Sans (text) |
| **Inline SVG** | Logo and icons, no image files needed |

## User guide

A step-by-step walkthrough of every page.

### 1. Home page

The starting point. Choose what you want to do.

1. Open **[leetnode.my.to](https://leetnode.my.to)**.
2. Pick any of the available cards: **Predict rating**, **Get recommendations**, or **Filter questions**.
3. The footer shows the total views and today's views, plus the credits.

<img width="1901" height="911" alt="Screenshot 2026-10-23 182557" src="https://github.com/user-attachments/assets/db687f38-4253-4d50-9102-f7fa04445ec8" />
<img width="1897" height="812" alt="Screenshot 2026-10-23 182647" src="https://github.com/user-attachments/assets/4d9dc314-1b13-4500-b8c0-3d47cf646dbc" />
<img width="1901" height="758" alt="Screenshot 2026-10-23 182710" src="https://github.com/user-attachments/assets/dbf50365-d3d1-4fd1-9405-504aacb90c72" />

---

### 2. Predict a problem's rating

1. Open **Predict rating** from the home page.
2. Type the question number (the number shown before the title on LeetCode, for example `1`).
3. Click **Get Rating**.
4. The result shows:
   - the problem title and its Easy / Medium / Hard label
   - the **estimated rating**, with a bar showing where it sits between easier and harder
   - acceptance rate, likes, dislikes, total accepted and total submissions
   - topic tags
5. Click **New Search** to try another problem.

<img width="1900" height="907" alt="Screenshot 2026-10-23 182815" src="https://github.com/user-attachments/assets/1a1c2e6f-da7f-4925-8145-f448e9553017" />
<img width="1900" height="832" alt="Screenshot 2026-10-23 182858" src="https://github.com/user-attachments/assets/836d983f-ab98-47c7-83be-5208095243d8" />

---

### 3. Get recommendations

1. Open **Get recommendations** from the home page.
2. Enter your **LeetCode username**, the name in your profile link.
3. Click **Get Recommendations**.
4. You will see:
   - your **contest rating**, with a bar showing your standing and a **Top x%** badge
   - contests attended, global ranking and total participants
   - a list of problems rated from **your rating - 50 to your rating + 200**
5. Click **Solve** next to a problem to open it on LeetCode in a new tab.
6. Use **Previous / Next** to move through more problems.

<img width="1900" height="910" alt="Screenshot 2026-10-23 183010" src="https://github.com/user-attachments/assets/21000db9-8c8b-470c-adce-aacb29488a24" />
<img width="1900" height="907" alt="Screenshot 2026-10-23 183051" src="https://github.com/user-attachments/assets/2539190d-4202-41ae-9653-a612a7696154" />
<img width="1900" height="831" alt="Screenshot 2026-10-23 183147" src="https://github.com/user-attachments/assets/375f42be-984c-4d1f-aa48-409f659945fd" />




**No contests yet?** Recommendations are based on contest performance. If the account hasn't attended enough contests, the page says so and links to LeetCode's contest list.

---

### 4. Filter questions by rating

1. Open **Filter** from the home page.
2. Enter a **minimum** and a **maximum** rating, or tap a quick pick such as `1500–1800`.
3. Click **Find Problems**.
4. Browse the results, 10 per page, and use **Previous / Next** to change pages.
5. Click **Solve** to open a problem on LeetCode.

<img width="1901" height="911" alt="Screenshot 2026-10-23 183306" src="https://github.com/user-attachments/assets/b0504751-a727-404b-af1d-dca37a192aac" />
<img width="1902" height="908" alt="Screenshot 2026-10-23 183337" src="https://github.com/user-attachments/assets/47dc607e-fac5-4883-919a-7dca89ae7bac" />
<img width="1900" height="376" alt="Screenshot 2026-10-23 183356" src="https://github.com/user-attachments/assets/ad09f734-a223-4fba-b5b6-74dff38a8bb8" />


**Input rules:** only whole numbers from 0 to 9999 are accepted. Letters, signs, decimals and exponents (like `1e32`) are removed as you type, and the minimum can't be higher than the maximum.

---

### 5. Error page

If something goes wrong (for example a problem number that doesn't exist), you are taken to a friendly error page that shows what happened and a button back to the search.

<img width="1917" height="910" alt="Screenshot 2026-10-23 183512" src="https://github.com/user-attachments/assets/a360ead6-b5cf-49b2-8120-5b1ab7bd596d" />


## How it works

```
Browser (HTML / CSS / JS)
        │  fetch()
        ▼
Spring Boot REST controllers ──► Service layer ──► Spring Cache (@Cacheable)
                                        │   ▲
                                        ▼   │
                                 Spring Data JPA ──► MySQL
                                        │   ▲
                                        ▼   │
                                 Prediction using XGBoost Model
```

- **Server-rendered shell, API-driven content.** Thymeleaf serves the pages, and JavaScript calls the REST endpoints to fill in results without reloading.
- **Caching.** Repeated lookups are served from cache with `@Cacheable` instead of being recomputed or re-fetched. Ratings corresponding to Id's are cached.
- **Recommendations.** After the user's contest rating is fetched, the app calls the same range endpoint the filter page uses, with `rating - 50` to `rating + 200`.
- **Pagination.** The filter endpoint returns 10 questions per page and takes a 1-based `pageNumber`.
- **View counter.** One row per day in a `daily_views` table. Each visit runs a single atomic `INSERT ... ON DUPLICATE KEY UPDATE` statement. Today's views are that day's row, and the total is the sum of all rows. The date is calculated in `Asia/Kolkata`, so "today" resets at midnight IST with no scheduled job.
- **Visit counting.** The first load in a browser session sends a `POST` to record a view, and later loads only `GET`, so refreshing doesn't inflate the count.
- **Errors.** Failed API calls redirect to a shared error page with the message in the URL.

## API reference

### Pages

| Path | Page |
|---|---|
| `/` | Home |
| `/rating` | Rating predictor |
| `/profile` | Recommendations |
| `/filter` | Rating filter |
| `/api-error` | Error page (`?message=...`) |

### REST endpoints

| Method | Endpoint | Parameters | Returns |
|---|---|---|---|
| `GET` | `/api/search` | `id` (question number) | Problem details: title, difficulty, estimated rating, acceptance rate, likes, dislikes, totals, tags |
| `GET` | `/api/user` | `userId` (LeetCode username) | Contest stats: `userId`, `attendedContestsCount`, `userRating`, `globalRanking`, `totalParticipants`, `topPercentage` |
| `GET` | `/api/filter` | `minRange`, `maxRange`, `pageNumber` (starts at 1) | List of up to 10 questions: `id`, `title`, `rating`, `titleSlug` |
| `GET` | `/api/views` | none | `{ "totalViews": 0, "todayViews": 0 }` |
| `POST` | `/api/views` | none | Records one view, returns the same JSON |

**Example**

```bash
curl "https://leetnode.my.to/api/filter?minRange=2440&maxRange=2480&pageNumber=1"
```

```json
[
  {
    "id": 2117,
    "rating": 2479,
    "title": "Abbreviating the Product of a Range",
    "titleSlug": "abbreviating-the-product-of-a-range"
  },
  {
    "id": 2421,
    "rating": 2442,
    "title": "Number of Good Paths",
    "titleSlug": "number-of-good-paths"
  }
]
```

Error responses use a JSON body with a `message` field.

## Project structure

```
.
├── src/main/java/com/example/demo
│   ├── controller/        # REST + page controllers
│   ├── service/           # business logic (including ViewService)
│   ├── repository/        # Spring Data JPA repositories
│   ├── entity/            # JPA entities (Question, DailyViews)
│   └── dto/               # response objects (UserData, ViewDto)
│   ├── exception/         # Handling Exceptions
|
├── src/main/resources
│   ├── templates/
│   │   ├── homePage.html
│   │   ├── ratingPage.html
│   │   ├── profilePage.html
│   │   ├── filterPage.html
│   │   ├── runtimeErrorPage.html
│   │   └── defaultErrorPage.html
│   └── application.properties
├
├── ml_requirements
│   ├── mlPredictor.py
│   ├── mlbFile.pkl
│   ├── modelFile.pkl
│   ├── ml_dependencies.txt
│   
├── Dockerfile
└── README.md
```

## Run it locally

### Prerequisites

- Java 17+
- Docker (optional, or else need to install python packages manually)
- A MySQL database (local or hosted)

### 1. Clone

```bash
git clone https://github.com/Sreecharan1703/LeetNode.git
cd LeetNode
```

### 2. Configure the database

Set these environment variables, or the matching properties in `application.properties`:

```bash
DB_USER=your_chosen_username
DB_PASSWORD=your_chosen_password
DB_ROOT_PASSWORD=your_chosen_password
SPRING_JPA_HIBERNATE_DDL_AUTO=update
```

`ddl-auto=update` creates and updates the tables automatically on startup.

### 3a. Run with Docker

```bash
docker compose up --build
```

### 3a. Terminate Process

```bash
docker compose down
```

Then open **http://localhost:80**.

## Deployment

The live site runs as a Docker container on a free hosting plan (Render) with a hosted MySQL-compatible database (TiDB). Free instances go to sleep when idle, so a i wrote a app-script code to keep the instance awake. View counts are stored in the database, so they survive restarts.

## Limitations

- Ratings are **estimates**. They can be off by about **±120 points** and are roughly **85% accurate**.
- Ratings can be skewed by heavy submission spamming on a problem.
- Recommendations need at least one contest in the user's history.

## Author

**Made by Sreecharan**

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0A66C2?logo=linkedin&logoColor=white)](https://www.linkedin.com/in/sreecharan-kesireddy)
[![LeetCode](https://img.shields.io/badge/LeetCode-FFA116?logo=leetcode&logoColor=black)](https://leetcode.com/u/sreecharan_17)

---

<div align="center">

If this helped your grind, consider leaving a ⭐ on the repo.

</div>
