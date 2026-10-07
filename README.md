AI Expense Manager
==================

An Android expense management application that uses Gemini AI
to extract structured expense information from natural-language
user input and stores the expense data locally using Room.

## Current Features

- Manual expense entry
- View expense history
- Edit expenses
- Delete expenses
- Natural-language expense input using Gemini AI
- Structured JSON response from Gemini
- AI response parsing with Gson
- AI expense validation
- Room database persistence
- Hilt dependency injection
- Jetpack Compose UI
- MVVM architecture
- Kotlin Coroutines and StateFlow

## Example

User input:

"I spent ₹450 on dinner at a restaurant yesterday"

Gemini extracts:

- Amount: ₹450
- Category: Food & Dining
- Merchant: restaurant
- Description: dinner at a restaurant
- Date: yesterday's date

## Architecture

Compose UI
    ↓
ViewModel
    ↓
Repository
    ↓
Room Database

AI flow:

Compose UI
    ↓
ViewModel
    ↓
Gemini
    ↓
Structured JSON
    ↓
Parser
    ↓
Validator
    ↓
Mapper
    ↓
Room

## Tech Stack

- Kotlin
- Jetpack Compose
- Material 3
- MVVM
- StateFlow
- Coroutines
- Room
- Hilt
- Firebase AI Logic
- Gemini
- Gson

## Planned Features

- AI spending insights
- Spending analysis by category
- AI financial assistant
- Monthly spending summaries
- FastAPI backend
- PostgreSQL
- Automated tests

## Project Status

🚧 Active development

Current milestone:
AI-powered expense extraction and local expense management.

<img width="1080" height="2400" alt="Screenshot_20261007_181211" src="https://github.com/user-attachments/assets/8b0891c4-300d-4ea4-9357-d76c6b5ae081" />
<img width="1080" height="2400" alt="Screenshot_20261007_181224" src="https://github.com/user-attachments/assets/874389d9-101d-4a1e-9c34-4b694a4452cc" />


