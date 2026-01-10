# Banking System Development Prompts
## Complete Firebase Backend + Room Database Integration

This document contains step-by-step prompts to transform your MyBank app from static data to a fully dynamic banking system with Firebase backend and Room local database.

---

## 📋 **PHASE 1: Firebase Setup & Configuration**

### Prompt 1.1: Firebase Firestore Database Structure
```
Set up Firebase Firestore database structure for the MyBank application with the following collections:

1. **users** collection:
   - userId (document ID)
   - email, name, phone, address
   - profileImageUrl
   - createdAt, updatedAt timestamps
   - fcmToken (for push notifications)

2. **accounts** collection:
   - accountId (document ID)
   - userId (reference)
   - accountNumber, accountName, accountType (CHECKING/SAVINGS/CREDIT)
   - balance, currency, iban
   - isActive, createdAt

3. **transactions** collection:
   - transactionId (document ID)
   - accountId, userId (references)
   - type (DEBIT/CREDIT), category, amount, currency
   - description, recipientName, recipientAccount
   - timestamp, status, balanceAfter

4. **notifications** collection:
   - notificationId (document ID)
   - userId (reference)
   - type, title, message
   - timestamp, isRead
   - relatedTransactionId, relatedAccountId

5. **cards** collection:
   - cardId (document ID)
   - accountId, userId (references)
   - cardNumber (encrypted), cardType (DEBIT/CREDIT)
   - expiryDate, cvv (encrypted), cardHolderName
   - isActive, dailyLimit, monthlyLimit

Create Firestore security rules to ensure users can only access their own data.
```

### Prompt 1.2: Firebase Storage Setup
```
Configure Firebase Storage for the MyBank app:

1. Create storage buckets:
   - /profile_images/{userId}/
   - /documents/{userId}/
   - /receipts/{transactionId}/

2. Set up storage security rules:
   - Users can only upload/read their own files
   - Maximum file size: 5MB for images, 10MB for documents
   - Allowed file types: jpg, png, pdf

3. Implement image compression before upload
4. Generate thumbnail versions for profile images
```

### Prompt 1.3: Firebase Cloud Functions
```
Create Firebase Cloud Functions for the MyBank backend:

1. **onUserCreate**: Triggered when new user registers
   - Create default checking account
   - Send welcome notification
   - Initialize user preferences

2. **onTransactionCreate**: Triggered on new transaction
   - Update account balance
   - Create notification
   - Check for low balance alerts
   - Update transaction history

3. **processTransfer**: HTTP callable function
   - Validate sender and recipient accounts
   - Check sufficient balance
   - Create debit transaction for sender
   - Create credit transaction for recipient
   - Update both account balances atomically

4. **generateAccountStatement**: HTTP callable function
   - Fetch transactions for date range
   - Generate PDF statement
   - Upload to Storage
   - Return download URL

5. **scheduledBalanceCheck**: Scheduled function (daily)
   - Check all accounts for low balance
   - Send notifications if balance < threshold
```

---

## 📋 **PHASE 2: Room Database Enhancement**

### Prompt 2.1: Complete Room Database Schema
```
Update the Room database schema in MyBankDatabase.kt to include all entities with proper relationships:

1. Add @Entity for Card model:
   - cardId, accountId, userId
   - cardNumber (encrypted), cardType, expiryDate
   - cvv (encrypted), cardHolderName
   - isActive, dailyLimit, monthlyLimit, currentDailySpent

2. Add @Entity for TransactionReceipt:
   - receiptId, transactionId
   - receiptUrl, uploadedAt

3. Create @Dao interfaces:
   - CardDao: CRUD operations for cards
   - TransactionReceiptDao: manage receipts

4. Add database migrations for version updates

5. Implement database views for:
   - Monthly spending summary
   - Category-wise expenses
   - Account balance history
```

### Prompt 2.2: Room Database Sync Strategy
```
Implement offline-first sync strategy between Room and Firebase:

1. Create SyncManager class:
   - Track last sync timestamp
   - Queue pending operations (create/update/delete)
   - Sync when network available

2. Implement conflict resolution:
   - Server timestamp wins for conflicts
   - Merge local changes with server data

3. Add sync status indicators:
   - Synced, Pending, Failed states
   - Show sync status in UI

4. Background sync worker:
   - Use WorkManager for periodic sync
   - Sync every 15 minutes when online
   - Immediate sync on critical operations (transfers)
```

---

## 📋 **PHASE 3: Repository Layer - Firebase Integration**

### Prompt 3.1: Enhanced AccountRepository
```
Update AccountRepository.kt to fully integrate Firebase Firestore:

1. Replace static data with Firestore queries:
   - getUserAccounts(): Listen to Firestore collection, cache in Room
   - createAccount(): Create in Firestore, sync to Room
   - updateAccount(): Update Firestore, sync to Room
   - deleteAccount(): Soft delete in Firestore

2. Implement real-time listeners:
   - Listen to account balance changes
   - Update Room database on Firestore changes
   - Emit Flow updates to UI

3. Add offline support:
   - Return Room data immediately
   - Fetch from Firestore in background
   - Update Room when Firestore data arrives

4. Error handling:
   - Network errors: use cached Room data
   - Firestore errors: log and show user-friendly messages
```

### Prompt 3.2: Enhanced TransactionRepository
```
Update TransactionRepository.kt for dynamic transaction management:

1. Implement transaction operations:
   - createTransaction(): Create in Firestore, update account balance
   - getTransactions(): Real-time listener with pagination
   - filterTransactions(): By date, type, category, amount range
   - searchTransactions(): Full-text search in descriptions

2. Add transaction analytics:
   - getMonthlySpending(): Group by month
   - getCategoryBreakdown(): Spending by category
   - getIncomeVsExpense(): Compare income and expenses

3. Implement transaction receipts:
   - uploadReceipt(): Upload to Firebase Storage
   - attachReceiptToTransaction(): Link receipt URL
   - downloadReceipt(): Fetch from Storage

4. Batch operations:
   - Import transactions from CSV
   - Export transactions to PDF/Excel
```

### Prompt 3.3: Enhanced NotificationRepository
```
Create NotificationRepository.kt for managing notifications:

1. Firestore integration:
   - getUserNotifications(): Real-time listener
   - markAsRead(): Update Firestore and Room
   - deleteNotification(): Remove from both databases

2. FCM (Firebase Cloud Messaging):
   - saveFcmToken(): Store in Firestore user document
   - sendNotification(): Trigger Cloud Function
   - handleNotificationClick(): Navigate to relevant screen

3. Notification types:
   - Transaction alerts (immediate)
   - Low balance warnings (threshold-based)
   - Security alerts (suspicious activity)
   - Promotional messages (scheduled)

4. Notification preferences:
   - Enable/disable notification types
   - Set quiet hours
   - Customize notification sounds
```

### Prompt 3.4: CardRepository
```
Create CardRepository.kt for card management:

1. CRUD operations:
   - getUserCards(): Fetch from Firestore, cache in Room
   - addCard(): Create in Firestore with encrypted data
   - updateCard(): Update limits, status
   - deleteCard(): Soft delete (mark as inactive)

2. Card security:
   - Encrypt card number and CVV before storing
   - Decrypt only when needed (show last 4 digits)
   - Implement card tokenization

3. Card limits:
   - trackDailySpending(): Update on each transaction
   - checkLimit(): Validate before transaction
   - resetDailyLimit(): Scheduled at midnight

4. Card features:
   - freezeCard(): Temporarily disable
   - reportLost(): Mark as lost, create new card
   - requestReplacement(): Order new card
```

---

## 📋 **PHASE 4: ViewModel Updates - Remove Static Data**

### Prompt 4.1: HomeViewModel Dynamic Data
```
Update HomeViewModel.kt to use dynamic data from repositories:

1. Remove all static/hardcoded data
2. Load data from repositories:
   - Accounts: accountRepository.getUserAccounts()
   - Transactions: transactionRepository.getRecentTransactions()
   - Total balance: accountRepository.getTotalBalance()

3. Implement pull-to-refresh:
   - Force sync with Firestore
   - Show loading indicator
   - Update UI with fresh data

4. Add error handling:
   - Network errors: show cached data with indicator
   - Empty state: show "No accounts" message
   - Loading state: show skeleton screens

5. Real-time updates:
   - Listen to Firestore changes
   - Update UI automatically when data changes
```

### Prompt 4.2: AccountsViewModel Dynamic Data
```
Update AccountsViewModel.kt for dynamic account management:

1. Replace static account list with Firestore data
2. Implement account operations:
   - createAccount(): Show dialog, create in Firestore
   - updateAccount(): Edit account details
   - deleteAccount(): Confirm and soft delete

3. Account filtering and sorting:
   - Sort by balance, name, type
   - Filter by account type
   - Search by account name/number

4. Account analytics:
   - Calculate total balance across accounts
   - Show balance trends (last 30 days)
   - Display account activity summary
```

### Prompt 4.3: TransactionsViewModel Dynamic Data
```
Update TransactionsViewModel.kt for dynamic transaction display:

1. Remove static transaction data
2. Load transactions from repository:
   - Paginated loading (20 transactions per page)
   - Real-time updates from Firestore
   - Cache in Room for offline access

3. Implement filters:
   - Date range picker
   - Transaction type (income/expense)
   - Category filter
   - Amount range filter

4. Search functionality:
   - Search by description, recipient name
   - Debounce search input (300ms)
   - Show search results in real-time

5. Transaction details:
   - Show full transaction info on click
   - Display receipt if available
   - Option to download/share receipt
```

### Prompt 4.4: TransfersViewModel
```
Create TransfersViewModel.kt for money transfers:

1. Transfer operations:
   - validateRecipient(): Check account exists
   - checkBalance(): Ensure sufficient funds
   - processTransfer(): Call Firebase Cloud Function
   - confirmTransfer(): Show confirmation dialog

2. Transfer types:
   - Internal transfer (between own accounts)
   - External transfer (to other users)
   - Scheduled transfers (future date)
   - Recurring transfers (monthly, weekly)

3. Transfer history:
   - Recent transfers
   - Favorite recipients
   - Transfer templates

4. Security:
   - Require PIN/biometric for transfers > $500
   - Two-factor authentication for large amounts
   - Transaction limits (daily/monthly)
```

### Prompt 4.5: NotificationsViewModel Dynamic Data
```
Update NotificationsViewModel.kt for dynamic notifications:

1. Remove static notification data
2. Load from NotificationRepository:
   - Real-time listener for new notifications
   - Group by date (Today, Yesterday, Older)
   - Show unread count badge

3. Notification actions:
   - markAsRead(): Update Firestore
   - markAllAsRead(): Batch update
   - deleteNotification(): Remove from Firestore
   - clearAll(): Delete all notifications

4. Notification filtering:
   - Filter by type (transaction, security, promo)
   - Show only unread
   - Search notifications
```

---

## 📋 **PHASE 5: UI Updates - Dynamic Data Binding**

### Prompt 5.1: HomeScreen Dynamic UI
```
Update HomeScreen.kt to display dynamic data:

1. Replace hardcoded values:
   - User name: from Firebase Auth currentUser
   - Total balance: from uiState.totalBalance
   - Cards: from uiState.accounts (map to CardData)
   - Transactions: from uiState.recentTransactions

2. Add loading states:
   - Shimmer effect while loading
   - Skeleton cards for accounts
   - Loading indicator for transactions

3. Empty states:
   - "No accounts" with "Add Account" button
   - "No transactions" with illustration

4. Error states:
   - Network error banner
   - Retry button
   - Offline mode indicator

5. Pull-to-refresh:
   - SwipeRefresh composable
   - Trigger viewModel.refresh()
```

### Prompt 5.2: AccountsScreen Dynamic UI
```
Update AccountsScreen.kt for dynamic account display:

1. Remove sample data:
   - Use uiState.accounts from ViewModel
   - Calculate totalBalance dynamically

2. Add account creation dialog:
   - Input fields: name, type, initial balance
   - Validation: required fields, valid amount
   - Submit: call viewModel.createAccount()

3. Account actions:
   - Long press: show options (edit, delete, freeze)
   - Swipe: quick actions
   - Click: navigate to account details

4. Loading and empty states:
   - Show shimmer while loading
   - Empty state with "Add Account" CTA
```

### Prompt 5.3: AllTransactionsScreen Dynamic UI
```
Update AllTransactionsScreen.kt for dynamic transactions:

1. Replace static transaction list:
   - Use uiState.transactions from ViewModel
   - Group by date dynamically
   - Calculate totals (income/expense) from actual data

2. Implement pagination:
   - Load more on scroll to bottom
   - Show loading indicator at bottom
   - Handle end of list

3. Filter UI:
   - Date range picker dialog
   - Category chips (dynamic from data)
   - Amount range slider

4. Search implementation:
   - Search bar with debounce
   - Highlight search terms in results
   - Clear search button

5. Transaction details bottom sheet:
   - Show full transaction info
   - Display receipt image if available
   - Share/download options
```

### Prompt 5.4: TransfersScreen Dynamic UI
```
Create TransfersScreen.kt for money transfers:

1. Transfer form:
   - From account dropdown (user's accounts)
   - To account input (account number or email)
   - Amount input with currency
   - Description/note field
   - Schedule date picker (optional)

2. Recipient selection:
   - Recent recipients list
   - Favorite recipients
   - Search contacts
   - Scan QR code

3. Transfer confirmation:
   - Show transfer summary
   - Require PIN/biometric
   - Processing indicator
   - Success/failure feedback

4. Transfer history:
   - List recent transfers
   - Filter by status (completed, pending, failed)
   - Repeat transfer option
```

### Prompt 5.5: CardsScreen Dynamic UI
```
Create CardsScreen.kt for card management:

1. Card list:
   - Display user's cards from Firestore
   - Show card type, last 4 digits, expiry
   - Card status indicator (active/frozen/expired)

2. Card details:
   - Flip animation to show CVV
   - Spending limits (daily/monthly)
   - Current spending progress
   - Recent transactions on card

3. Card actions:
   - Freeze/unfreeze card
   - Set spending limits
   - Report lost/stolen
   - Request replacement

4. Add new card:
   - Card number input with validation
   - Expiry date picker
   - CVV input (masked)
   - Link to account
```

---

## 📋 **PHASE 6: Authentication Flow**

### Prompt 6.1: Enhanced Login Screen
```
Update LoginScreen.kt for Firebase Authentication:

1. Email/Password login:
   - Call authRepository.loginWithFirebase()
   - Show loading indicator
   - Handle errors (invalid credentials, network)
   - Navigate to home on success

2. Google Sign-In:
   - Integrate Google Sign-In SDK
   - Call authRepository.signInWithGoogle()
   - Handle sign-in flow

3. Biometric authentication:
   - Check if biometric available
   - Prompt for fingerprint/face
   - Auto-fill last logged email

4. Offline login:
   - Detect network status
   - Use Room cached credentials
   - Show "Offline Mode" indicator

5. Remember me:
   - Save email in DataStore
   - Auto-fill on next launch
```

### Prompt 6.2: Enhanced Register Screen
```
Update RegisterScreen.kt for user registration:

1. Registration form:
   - Name, email, password, confirm password
   - Phone number (optional)
   - Terms and conditions checkbox

2. Validation:
   - Email format validation
   - Password strength indicator
   - Password match check
   - Phone number format

3. Registration process:
   - Call authRepository.registerWithFirebase()
   - Create user document in Firestore
   - Create default checking account
   - Send verification email

4. Post-registration:
   - Show welcome screen
   - Navigate to home
   - Send welcome notification
```

### Prompt 6.3: Profile & Settings
```
Create ProfileScreen.kt and update SettingsScreen.kt:

1. Profile screen:
   - Display user info from Firestore
   - Edit profile (name, phone, address)
   - Upload profile picture to Storage
   - Change password

2. Settings screen:
   - Notification preferences
   - Security settings (PIN, biometric)
   - Language selection
   - Currency preference
   - Theme (light/dark)

3. Account management:
   - Linked accounts
   - Payment methods
   - Transaction limits
   - Privacy settings

4. Logout:
   - Confirm logout dialog
   - Clear session
   - Navigate to login
```

---

## 📋 **PHASE 7: Advanced Features**

### Prompt 7.1: Transaction Analytics
```
Create AnalyticsScreen.kt for financial insights:

1. Spending overview:
   - Monthly spending chart (bar/line)
   - Category breakdown (pie chart)
   - Income vs Expense comparison

2. Trends:
   - Spending trends (last 6 months)
   - Category trends
   - Predict next month spending

3. Budgets:
   - Set monthly budget by category
   - Track budget progress
   - Alert when approaching limit

4. Reports:
   - Generate monthly/yearly reports
   - Export to PDF/Excel
   - Email reports
```

### Prompt 7.2: Bill Payments
```
Create BillPaymentsScreen.kt for utility payments:

1. Bill categories:
   - Electricity, water, gas
   - Internet, phone
   - Insurance, loans

2. Add biller:
   - Biller name, account number
   - Payment amount, due date
   - Auto-pay option

3. Payment scheduling:
   - One-time payment
   - Recurring payments
   - Payment reminders

4. Payment history:
   - List past payments
   - Download receipts
   - Dispute payment
```

### Prompt 7.3: Savings Goals
```
Create SavingsGoalsScreen.kt for financial goals:

1. Create goal:
   - Goal name (vacation, car, house)
   - Target amount, deadline
   - Linked savings account

2. Track progress:
   - Progress bar
   - Amount saved vs target
   - Days remaining
   - Projected completion date

3. Contributions:
   - Manual contributions
   - Automatic transfers
   - Round-up savings

4. Goal insights:
   - Suggest monthly contribution
   - Adjust deadline based on progress
   - Celebrate milestones
```

### Prompt 7.4: QR Code Payments
```
Implement QR code payment feature:

1. Generate QR code:
   - Encode account number, amount
   - Display QR code for scanning
   - Share QR code

2. Scan QR code:
   - Open camera scanner
   - Decode payment info
   - Confirm payment details
   - Process payment

3. Payment links:
   - Generate payment link
   - Share via messaging apps
   - Track link payments
```

### Prompt 7.5: Push Notifications
```
Implement Firebase Cloud Messaging (FCM):

1. FCM setup:
   - Register device token
   - Save token to Firestore
   - Handle token refresh

2. Notification types:
   - Transaction alerts (real-time)
   - Low balance warnings
   - Bill payment reminders
   - Security alerts

3. Notification handling:
   - Show notification in system tray
   - Handle notification click
   - Navigate to relevant screen
   - Update notification badge

4. Notification preferences:
   - Enable/disable by type
   - Set quiet hours
   - Notification sound
```

---

## 📋 **PHASE 8: Security & Optimization**

### Prompt 8.1: Security Enhancements
```
Implement security features:

1. Data encryption:
   - Encrypt sensitive data in Room (card numbers, CVV)
   - Use Android Keystore
   - Encrypt data before Firestore upload

2. PIN/Biometric authentication:
   - Set up PIN on first launch
   - Require PIN for sensitive operations
   - Biometric authentication option

3. Session management:
   - Auto-logout after inactivity
   - Require re-authentication for transfers
   - Detect suspicious activity

4. Secure communication:
   - Use HTTPS for all API calls
   - Certificate pinning
   - Validate SSL certificates
```

### Prompt 8.2: Performance Optimization
```
Optimize app performance:

1. Database optimization:
   - Add indexes to frequently queried fields
   - Implement pagination for large lists
   - Use database views for complex queries

2. Image optimization:
   - Compress images before upload
   - Use Coil for efficient image loading
   - Cache images locally

3. Network optimization:
   - Implement request caching
   - Batch API requests
   - Use Firestore offline persistence

4. UI optimization:
   - Use LazyColumn for long lists
   - Implement remember and derivedStateOf
   - Avoid unnecessary recompositions
```

### Prompt 8.3: Error Handling & Logging
```
Implement comprehensive error handling:

1. Error types:
   - Network errors (no internet, timeout)
   - Authentication errors (invalid token)
   - Validation errors (invalid input)
   - Server errors (500, 503)

2. Error display:
   - User-friendly error messages
   - Retry mechanisms
   - Fallback to cached data

3. Logging:
   - Log errors to Firebase Crashlytics
   - Track user actions
   - Monitor app performance

4. Analytics:
   - Track screen views
   - Track user events (login, transfer, etc.)
   - Monitor conversion funnels
```

---

## 📋 **PHASE 9: Testing**

### Prompt 9.1: Unit Tests
```
Write unit tests for repositories and ViewModels:

1. Repository tests:
   - Test CRUD operations
   - Test error handling
   - Mock Firestore and Room
   - Test offline scenarios

2. ViewModel tests:
   - Test state updates
   - Test user actions
   - Mock repositories
   - Test error states

3. Use libraries:
   - JUnit for test framework
   - Mockito/MockK for mocking
   - Turbine for Flow testing
```

### Prompt 9.2: Integration Tests
```
Write integration tests:

1. Database tests:
   - Test Room migrations
   - Test complex queries
   - Test transactions

2. API tests:
   - Test Firestore operations
   - Test Cloud Functions
   - Test authentication flow

3. End-to-end tests:
   - Test complete user flows
   - Test offline scenarios
   - Test sync mechanisms
```

---

## 📋 **PHASE 10: Deployment**

### Prompt 10.1: Production Setup
```
Prepare app for production:

1. Firebase configuration:
   - Set up production Firestore database
   - Configure security rules
   - Set up Cloud Functions
   - Enable Firebase Analytics

2. App configuration:
   - Update app version
   - Configure ProGuard rules
   - Enable code obfuscation
   - Sign APK/AAB

3. Testing:
   - Test on multiple devices
   - Test different Android versions
   - Test offline scenarios
   - Performance testing

4. Release:
   - Create release build
   - Upload to Google Play Console
   - Write release notes
   - Submit for review
```

---

## 🎯 **Implementation Order**

Follow this order for systematic development:

1. **Week 1-2**: Phase 1 (Firebase Setup) + Phase 2 (Room Enhancement)
2. **Week 3-4**: Phase 3 (Repository Layer) + Phase 4 (ViewModel Updates)
3. **Week 5-6**: Phase 5 (UI Updates) + Phase 6 (Authentication)
4. **Week 7-8**: Phase 7 (Advanced Features)
5. **Week 9**: Phase 8 (Security & Optimization)
6. **Week 10**: Phase 9 (Testing) + Phase 10 (Deployment)

---

## 📝 **Notes**

- Each prompt can be used independently with Amazon Q or any AI assistant
- Test each phase thoroughly before moving to the next
- Keep Firebase costs in mind (use Firestore wisely)
- Implement proper error handling at every step
- Follow Android best practices and Material Design guidelines
- Use Kotlin coroutines for async operations
- Implement proper dependency injection with Hilt
- Keep user experience smooth with loading states and animations

---

## 🔗 **Additional Resources**

- Firebase Documentation: https://firebase.google.com/docs
- Room Database Guide: https://developer.android.com/training/data-storage/room
- Jetpack Compose: https://developer.android.com/jetpack/compose
- Material Design 3: https://m3.material.io/
- Kotlin Coroutines: https://kotlinlang.org/docs/coroutines-overview.html
