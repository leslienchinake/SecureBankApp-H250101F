# Secure Banking Application

## Description
A console-based Java banking application developed for ISS 2101. It demonstrates object-oriented programming and secure coding through user registration, login, bank account management, and transaction processing.

## Student Details
- Name: CHINAKE LESLIE N.
- Registration Number: H250101F
- Institution: Harare Institute of Technology
- Course: Information Security and Assurance
- Module: ISS 2101

## Features
- User registration and login
- Password hashing with a unique random salt (PBKDF2)
- Bank account creation
- Balance checking
- Deposits and withdrawals with input validation
- Prevention of withdrawals above the available balance
- Transaction history with date and time
- File-based persistence for users, accounts, and transactions

## Requirements
- JDK 17 or later
- Visual Studio Code (optional)

## How to Compile and Run
Run these commands from the project root folder:

```bash
javac -d out src/*.java
java -cp out Main
```

## Data Files
The application stores data in the project root in these files:
- `users.txt` — usernames, salts, and password hashes
- `accounts.txt` — account numbers, usernames, and balances
- `transactions.txt` — transaction records and timestamps

These files are created or updated when the application saves data.

## Important Note
This is an educational demonstration, not real banking software. It uses simple text-file storage and `double` values for money; a production system would need stronger storage controls, audit protections, and decimal-safe monetary calculations.
