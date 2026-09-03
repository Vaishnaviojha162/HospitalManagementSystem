# Global Hospital Management System (HMS)

A state-of-the-art, premium Hospital Management System built to streamline operations for Patients, Doctors, and Receptionists/Administrators. This repository contains a complete multi-role system featuring a Spring Boot backend, a MongoDB database, and a rich, responsive static frontend served over a local server.

---

## 🏥 Project Overview

The Global Hospital Management System is designed to automate and simplify clinical workflows. It provides custom, secure portals for three main roles:
1. **Patients:** Register/login, find doctors and view their fees, book active/emergency appointments, reschedule or cancel bookings, and access medical prescriptions.
2. **Doctors:** View patient appointments chronologically with automated emergency prioritizations, register prescriptions (detailing diseases, allergies, and instructions), and track patient lists.
3. **Receptionists (Admins):** Access high-level hospital statistics (overview counts), register or decommission doctors, monitor all global appointments, sort records dynamically, and review patient inquiry messages.

---

## ⚡ Key Highlights & Advanced Features

- **Double-Collision Doctor Booking Prevention:** When a patient books or reschedules an appointment, the system checks the database for conflicts. It prevents booking if the doctor has another active appointment scheduled within 15 minutes of the requested slot.
- **Automated Overdue/Missed Status Transition:** Every time appointments are fetched, the backend dynamically evaluates the appointment datetime against the system clock. If an active appointment is older than 50 minutes past its scheduled time, it is automatically flagged as missed (Status Code `3`) in the database.
- **Cross-Origin Resource Sharing (CORS) Configuration:** Securely allows standard web API calls from any port or environment (such as Python HTTP server, Live Server, or node serve).

---

## 🛠️ Custom Algorithms Implemented

To provide high performance and demonstrate core data-structure principles, this project implements custom algorithms directly rather than relying solely on database queries:

### 1. Custom Min-Heap Priority Queue (`AppointmentQueue.java`)
- **Purpose:** Used for patient and doctor dashboards to retrieve appointments sorted by schedule.
- **Logic:** Prioritizes emergency appointments over normal appointments. If both appointments are of the same type (both emergencies or both normal), they are sorted chronologically by combining date and time into an ISO-style compare string (`yyyy-MM-ddTHH:mm`).
- **Complexity:** $O(\log N)$ insertion and poll.

### 2. Custom Merge Sort (`AppointmentSorter.java`)
- **Purpose:** Used in the Receptionist/Admin view to sort all system appointments.
- **Logic:** Employs a divide-and-conquer merge sort algorithm. It supports three sorting configurations:
  - `date`: Chronological sorting (prioritizing emergencies first).
  - `name`: Alphabetical sorting based on the patient's full name.
  - `fees`: Numerical sorting in ascending order of doctor fees.
- **Complexity:** Guarantee of $O(N \log N)$ time complexity.

### 3. Custom Patient Searcher (`PatientSearcher.java`)
- **Purpose:** Used in the backend endpoint to search patient details by contact number.
- **Logic:** Performs a custom stable **Insertion Sort** to sort the patient list by contact string first, then executes a **Binary Search** to locate the target patient.
- **Complexity:** $O(N^2)$ for sorting (insertion sort is stable and efficient for small/nearly sorted lists) and $O(\log N)$ for searching.

---

## 📂 Project Structure

```
HospitalmanagementSystem 3/
├── run.ps1                 # Windows PowerShell setup and run script
├── README.md               # Complete project documentation (this file)
├── backend/
│   ├── pom.xml             # Maven dependencies configuration
│   ├── .maven/             # Downloaded local Apache Maven instance
│   └── src/
│       └── main/
│           ├── java/com/hospital/
│           │   ├── HospitalApplication.java   # Spring Boot Main Class
│           │   ├── config/
│           │   │   └── CorsConfig.java        # CORS settings for all origins
│           │   ├── controller/
│           │   │   ├── AuthController.java    # Handles authentication endpoints
│           │   │   ├── PatientController.java # Patient directory & search
│           │   │   ├── DoctorController.java  # Doctor registration & lists
│           │   │   ├── AppointmentController.java # Booking & sorting logic
│           │   │   ├── PrescriptionController.java# Prescription records
│           │   │   ├── ContactQueryController.java# Contact form queries
│           │   │   └── StatsController.java   # System dashboard statistics
│           │   ├── model/
│           │   │   ├── Patient.java           # Patient Document Schema
│           │   │   ├── Doctor.java            # Doctor Document Schema
│           │   │   ├── Appointment.java       # Appointment Document Schema
│           │   │   ├── Prescription.java      # Prescription Document Schema
│           │   │   └── ContactQuery.java      # Contact Message Schema
│           │   ├── repository/
│           │   │   ├── PatientRepository.java
│           │   │   ├── DoctorRepository.java
│           │   │   ├── AppointmentRepository.java
│           │   │   ├── PrescriptionRepository.java
│           │   │   └── ContactQueryRepository.java
│           │   └── service/
│           │       ├── AppointmentQueue.java  # Min-Heap Priority Queue
│           │       ├── AppointmentSorter.java # Merge Sort utility
│           │       └── PatientSearcher.java   # Binary Search utility
│           └── resources/
│               └── application.properties     # Database connection property file
└── frontend/
    ├── index.html          # Main portal (Home, Register, Login)
    ├── about.html          # Information page
    ├── contact.html        # Public query forms
    ├── dashboard-patient.html # Patient dashboard panel
    ├── dashboard-doctor.html  # Doctor medical dashboard
    ├── dashboard-receptionist.html # Admin panel for doctor/query tracking
    ├── css/
    │   └── style.css       # Main stylesheets
    ├── js/
    │   └── main.js         # API integration logic
    └── image/              # Styling assets & graphics
```

---

## 🗄️ Database Schemas (MongoDB Collections)

The application communicates with a local MongoDB database named `GlobalHospital` on port `27017` using the following collections:

### 1. `patients`
- **Fields:**
  - `id` (String - auto-generated ID)
  - `fname` (String - first name)
  - `lname` (String - last name)
  - `gender` (String)
  - `email` (String - unique login username)
  - `contact` (String - 10-digit number)
  - `password` (String)

### 2. `doctors`
- **Fields:**
  - `id` (String - auto-generated ID)
  - `username` (String - unique login username)
  - `password` (String)
  - `email` (String)
  - `spec` (String - specialization, e.g., Cardiologist, Pediatrician)
  - `docFees` (Integer - fees in currency units)

### 3. `appointments`
- **Fields:**
  - `id` (String - auto-generated ID)
  - `pid` (String - patient ID reference)
  - `fname` (String - patient first name)
  - `lname` (String - patient last name)
  - `gender` (String - patient gender)
  - `email` (String - patient email)
  - `contact` (String - patient contact)
  - `doctor` (String - doctor username reference)
  - `docFees` (Integer - copied fee amount)
  - `appdate` (String - date in `yyyy-MM-dd` format)
  - `apptime` (String - time in `HH:mm` format)
  - `userStatus` (Integer - `1` = Active, `0` = Cancelled, `3` = Missed)
  - `doctorStatus` (Integer - `1` = Active, `0` = Cancelled, `3` = Missed)
  - `emergency` (Boolean - urgent appointment flag)

### 4. `prescriptions`
- **Fields:**
  - `id` (String)
  - `appointmentId` (String - appointment reference)
  - `pid` (String - patient ID reference)
  - `fname` (String - patient first name)
  - `lname` (String - patient last name)
  - `disease` (String - diagnosed illness)
  - `allergy` (String - patient allergies)
  - `prescription` (String - prescribed medicines and dosage details)
  - `doctor` (String - prescribing doctor's username)
  - `appdate` (String)
  - `apptime` (String)

### 5. `queries` (Contact inquiries)
- **Fields:**
  - `id` (String)
  - `name` (String - sender's name)
  - `email` (String - sender's email)
  - `contact` (String)
  - `message` (String - message text)

---

## 📡 REST API Endpoints

### Authentication (`/api/auth`)
* `POST /api/auth/patient/register` : Register a new patient.
* `POST /api/auth/patient/login` : Login as a patient (expects email & password).
* `POST /api/auth/doctor/login` : Login as a doctor (expects username & password).
* `POST /api/auth/receptionist/login` : Login as receptionist (expects username & password).

### Patients (`/api/patients`)
* `GET /api/patients/all` : Retrieve a list of all registered patients.
* `GET /api/patients/search?contact={number}` : Look up a patient by contact number (performs insertion-sort and binary search).

### Doctors (`/api/doctors`)
* `GET /api/doctors/all` : Retrieve all registered doctors.
* `POST /api/doctors/add` : Add a new doctor (admin utility).
* `DELETE /api/doctors/delete/{id}` : Decommission/delete a doctor from the system.

### Appointments (`/api/appointments`)
* `POST /api/appointments/book` : Book an appointment. Performs collision checks.
* `GET /api/appointments/patient/{pid}` : Fetch a patient's appointments sorted using custom Min-Heap.
* `GET /api/appointments/doctor/{doctor}` : Fetch a doctor's appointments sorted using custom Min-Heap.
* `GET /api/appointments/all?sortBy={date|name|fees}` : Fetch all appointments sorted via custom Merge Sort.
* `PUT /api/appointments/reschedule/{id}` : Reschedule an appointment. Receives `newDate` and `newTime` in request body. Performs collision checks.
* `PUT /api/appointments/cancel/{id}` : Set appointment statuses to cancelled (`0`).

### Prescriptions (`/api/prescriptions`)
* `POST /api/prescriptions/create` : Generate a prescription document. Marks the matching appointment as complete/written.
* `GET /api/prescriptions/patient/{pid}` : Fetch prescriptions belonging to a patient.
* `GET /api/prescriptions/doctor/{doctor}` : Fetch prescriptions written by a doctor.

### Inquiries (`/api/queries`)
* `POST /api/queries/submit` : Submit a contact query message.
* `GET /api/queries/all` : Retrieve all queries submitted to the desk.

### Stats (`/api/stats`)
* `GET /api/stats/overview` : Returns overall counts for doctors, patients, appointments, and queries.

---

## 🚀 Running the Project

### Prerequisites
1. **Java Development Kit (JDK 21 or higher):** Confirm installation with `java -version`.
2. **MongoDB:** A local MongoDB server instance must be running on port `27017`.
3. **Python 3 / Node.js:** Used to serve the static frontend over HTTP.

### Running Backend (Spring Boot)
Open a terminal in the `/backend` folder. Since Maven is provided locally in this repository under `.maven/`, compile and start the server using:

**On macOS / Linux:**
```bash
./.maven/apache-maven-3.9.6/bin/mvn spring-boot:run
```

**On Windows (Command Prompt):**
```cmd
.maven\apache-maven-3.9.6\bin\mvn spring-boot:run
```

**On Windows (PowerShell):**
```powershell
.\.maven\apache-maven-3.9.6\bin\mvn spring-boot:run
```

*The backend server will run on `http://localhost:8080`.*

### Running Frontend
To prevent browser security blocks related to origin policies (CORS) when loading index files directly, serve the static frontend files over a local web server.

Open a terminal in the `/frontend` folder and run one of the following:

**Using Python:**
```bash
python3 -m http.server 3000
```

**Using Node.js (`npx`):**
```bash
npx -y serve -l 3000
```

*The frontend application will run on `http://localhost:3000`.* Open this URL in your web browser to access the application.

---

## 🔒 Default Login Credentials

For testing and demonstration, use the following logins:
* **Receptionist/Admin:**
  * **Username:** `admin`
  * **Password:** `admin`
* **Doctor:**
  * Register a doctor through the receptionist portal, then log in using the username and password you defined.
* **Patient:**
  * Create a patient profile directly from the homepage registration form, then log in using the registered email and password.
