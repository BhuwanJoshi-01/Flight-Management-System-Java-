# Flight Booking System - Implementation Report

## Overview

This document describes the comprehensive enhancements made to the Java Swing Flight Booking System, including the complete Customer Portal, enhanced Admin Portal, and supporting infrastructure changes.

---

## 1. Architecture Overview

### 1.1 Dual Portal System

The system now supports two distinct user interfaces:

| Portal | Users | Entry Point | Features |
|--------|-------|-------------|----------|
| **Admin Portal** | Administrators | `MainWindow.java` | Full system management, revenue analytics, settings |
| **Customer Portal** | Customers | `CustomerMainWindow.java` | Flight search, bookings, profile management |

### 1.2 Role-Based Authentication Flow

```
LoginWindow.java
    ↓
AuthService.authenticate()
    ↓
UserSession.getInstance().isAdmin()?
    ├── true  → MainWindow (Admin Portal)
    └── false → CustomerMainWindow (Customer Portal)
```

---

## 2. New Components Created

### 2.1 Customer Portal Components

| File | Purpose | Key Features |
|------|---------|--------------|
| `CustomerMainWindow.java` | Main container for Customer Portal | Sidebar navigation, header with tier/points display, logout handling |
| `CustomerDashboardPanel.java` | Customer dashboard | KPI cards, upcoming trips, featured flights, quick search |
| `FlightSearchPanel.java` | Flight search and booking | Origin/destination filters, date selection, price range, booking flow |
| `MyBookingsPanel.java` | Booking management | Tabbed view (Upcoming/Completed/Cancelled), cancellation, e-ticket download |
| `CustomerProfilePanel.java` | Profile management | Personal info, security (password change), notification preferences |
| `CustomerNotificationsPanel.java` | Notification center | Booking confirmations, flight updates, promotional notifications |

### 2.2 Admin Portal Enhancements

| File | Purpose | Key Features |
|------|---------|--------------|
| `AdminRevenuePanel.java` | Revenue analytics | KPI cards, bar chart visualization, top routes, revenue by customer tier |
| `AdminSettingsPanel.java` | System configuration | Dynamic pricing parameters (α, β), cancellation fees, booking rules |

### 2.3 Modified Existing Components

| File | Changes Made |
|------|--------------|
| `MainWindow.java` | Added Revenue and Settings panels to sidebar navigation |
| `UIStyles.java` | Added `ICON_REVENUE` and `ICON_SETTINGS` icon constants |
| `LoginWindow.java` | Role-based routing to Admin or Customer portal |
| `DynamicPricing.java` | Added configurable parameters with getters/setters |

---

## 3. Feature Details

### 3.1 Customer Dashboard (`CustomerDashboardPanel.java`)

**KPI Cards:**
- Upcoming Flights count
- Total Bookings count
- Reward Points (calculated from booking history)
- Customer Tier (Bronze/Silver/Gold/Platinum)

**Upcoming Trips:**
- Displays next 5 upcoming flights
- Shows flight number, route, departure date
- "View All" navigation to My Bookings panel

**Featured Flights:**
- Displays affordable future flights
- Shows current pricing with dynamic pricing applied
- Quick "View Details" and "Book Now" actions

### 3.2 Flight Search (`FlightSearchPanel.java`)

**Search Filters:**
- Origin/Destination text fields
- Departure date picker (JDateChooser)
- Max price filter
- Clear filters functionality

**Results Table:**
- Flight number, route, date, seats available
- Dynamic price display
- "Book" button for each flight

**Booking Flow:**
1. Select flight from search results
2. Click "Book" button
3. Confirmation dialog with price details
4. Creates booking with current date
5. Awards reward points (10 per booking)
6. Refreshes all panels

### 3.3 My Bookings (`MyBookingsPanel.java`)

**Tabbed Organization:**
- **Upcoming**: Active bookings with future flights
- **Completed**: Past flights
- **Cancelled**: Cancelled bookings

**Actions:**
- **Cancel Booking**: Calculates 10% cancellation fee, confirms with user
- **View Details**: Popup with full booking information
- **Download E-Ticket**: Generates text-based e-ticket file

**Table Columns:**
- Booking ID, Flight Number, Route, Date, Price, Status, Actions

### 3.4 Customer Profile (`CustomerProfilePanel.java`)

**Personal Information Tab:**
- Name, Email, Phone display
- Rewards card showing tier and points
- Tier progress bar

**Security Tab:**
- Current password verification
- New password with strength validation
- Password confirmation matching

**Preferences Tab:**
- Notification toggles (Email, SMS, Promotions)
- Communication preferences

### 3.5 Admin Revenue Analytics (`AdminRevenuePanel.java`)

**KPI Cards:**
- Total Revenue (sum of all booking prices)
- Average Booking Value
- Active Flights count
- Total Customers count

**Bar Chart Visualization:**
- Custom painted bar chart
- Top 5 routes by revenue
- Color-coded bars with values

**Revenue by Customer Tier:**
- Breakdown by Bronze/Silver/Gold/Platinum
- Percentage of total revenue
- Visual progress bars

### 3.6 Admin Settings (`AdminSettingsPanel.java`)

**General Settings Tab:**
- System Name configuration
- Admin Email
- Support Contact Phone

**Pricing Settings Tab:**
- Time Sensitivity (α): 0.0-1.0 slider
- Capacity Sensitivity (β): 0.0-1.0 slider
- Cancellation Fee percentage: 0-50%
- Booking Window days: 1-90
- Live preview of pricing formula

**Booking Rules Tab:**
- Maximum bookings per customer
- Minimum advance booking days
- Business rules configuration

**About Tab:**
- System version information
- Build details
- Credits

---

## 4. Dynamic Pricing Configuration

### 4.1 Formula

```
finalPrice = basePrice × (1 + α × timeFactor + β × capacityFactor)
```

Where:
- **α (timeSensitivity)**: How much price increases as departure approaches (default: 0.5)
- **β (capacitySensitivity)**: How much price increases as seats fill up (default: 0.8)
- **timeFactor**: `1 - (daysUntilDeparture / bookingWindowDays)`
- **capacityFactor**: `bookedSeats / totalCapacity`

### 4.2 Configurable Parameters

| Parameter | Type | Default | Range | Description |
|-----------|------|---------|-------|-------------|
| `timeSensitivity` | double | 0.5 | 0.0-1.0 | Time-based price increase factor |
| `capacitySensitivity` | double | 0.8 | 0.0-1.0 | Capacity-based price increase factor |
| `bookingWindowDays` | int | 30 | 1-90 | Days before departure for pricing calculation |

### 4.3 API Changes to `DynamicPricing.java`

```java
// New static fields
private static double timeSensitivity = 0.5;
private static double capacitySensitivity = 0.8;
private static int bookingWindowDays = 30;

// New getters/setters
public static double getTimeSensitivity()
public static void setTimeSensitivity(double value)
public static double getCapacitySensitivity()
public static void setCapacitySensitivity(double value)
public static int getBookingWindowDays()
public static void setBookingWindowDays(int value)
```

---

## 5. Customer Rewards System

### 5.1 Tier Levels

| Tier | Points Required | Color |
|------|-----------------|-------|
| Bronze | 0-99 | #CD7F32 |
| Silver | 100-499 | #C0C0C0 |
| Gold | 500-999 | #FFD700 |
| Platinum | 1000+ | #E5E4E2 |

### 5.2 Points Calculation

- **Per Booking**: 10 reward points
- **Total Points**: `customerBookings.size() × 10`
- Points are recalculated dynamically from booking history

---

## 6. File Structure

```
src/bcu/cmp5332/bookingsystem/gui/
├── MainWindow.java              # Admin Portal main window (MODIFIED)
├── CustomerMainWindow.java      # Customer Portal main window (NEW)
├── DashboardPanel.java          # Admin dashboard
├── CustomerDashboardPanel.java  # Customer dashboard (NEW)
├── FlightsPanel.java            # Admin flights management
├── FlightSearchPanel.java       # Customer flight search (NEW)
├── CustomersPanel.java          # Admin customers management
├── BookingsPanel.java           # Admin bookings management
├── MyBookingsPanel.java         # Customer bookings (NEW)
├── CustomerProfilePanel.java    # Customer profile (NEW)
├── CustomerNotificationsPanel.java # Customer notifications (NEW)
├── AdminRevenuePanel.java       # Admin revenue analytics (NEW)
├── AdminSettingsPanel.java      # Admin settings (NEW)
├── LoginWindow.java             # Login window (MODIFIED)
├── LoginFormPanel.java          # Login form
├── RegisterFormPanel.java       # Registration form
├── ForgotPasswordPanel.java     # Password recovery
├── HeroPanel.java               # Login hero section
└── UIStyles.java                # Centralized styling (MODIFIED)
```

---

## 7. Data Persistence

### 7.1 File Formats

All data files use `::` as field separator with trailing `::`.

**customers.txt:**
```
id::name::phone::email::deleted::passwordHash::
```

**bookings.txt:**
```
customerId::outboundFlightId::returnFlightId::bookingDate::cancelled::cancellationFee::
```

### 7.2 Backward Compatibility

- Original constructors preserved for data loading
- New fields added as optional parameters
- `deleted` flag for soft delete pattern maintained

---

## 8. Navigation Flow

### 8.1 Admin Portal Navigation

```
MainWindow
├── Dashboard     → DashboardPanel
├── Flights       → FlightsPanel
├── Customers     → CustomersPanel
├── Bookings      → BookingsPanel
├── Revenue       → AdminRevenuePanel
├── Settings      → AdminSettingsPanel
└── Exit          → Save & Close
```

### 8.2 Customer Portal Navigation

```
CustomerMainWindow
├── Dashboard     → CustomerDashboardPanel
├── Search Flights → FlightSearchPanel
├── My Bookings   → MyBookingsPanel
├── My Profile    → CustomerProfilePanel
├── Notifications → CustomerNotificationsPanel
└── Logout        → LoginWindow
```

---

## 9. UI/UX Enhancements

### 9.1 Consistent Styling via UIStyles

- Primary Color: `#1A73E8` (Blue)
- Success Color: `#34A853` (Green)
- Warning Color: `#FBBC04` (Yellow)
- Danger Color: `#EA4335` (Red)
- Sidebar Color: `#2C3E50` (Dark)
- Background: `#F8FAFC` (Light Gray)

### 9.2 Responsive Design

- GridBagLayout for proportional sizing
- CardLayout for panel switching
- Dynamic table sizing with scroll support

### 9.3 User Feedback

- Status bar messages
- Confirmation dialogs for destructive actions
- Error handling with user-friendly messages

---

## 10. Testing Checklist

### 10.1 Customer Portal

- [ ] Login as customer routes to CustomerMainWindow
- [ ] Dashboard shows correct KPIs
- [ ] Flight search returns correct results
- [ ] Booking creation works with reward points
- [ ] Booking cancellation calculates fees correctly
- [ ] E-ticket download generates file
- [ ] Profile password change validates strength
- [ ] Notifications display correctly

### 10.2 Admin Portal

- [ ] Login as admin routes to MainWindow
- [ ] Revenue analytics calculate correctly
- [ ] Settings changes persist to DynamicPricing
- [ ] All sidebar panels accessible

### 10.3 Authentication

- [ ] Role detection works correctly
- [ ] Logout returns to LoginWindow
- [ ] Session clears on logout

---

## 11. Future Enhancements

1. **Email Integration**: Send actual booking confirmations
2. **Payment Processing**: Integrate payment gateway
3. **Seat Selection**: Visual seat map during booking
4. **Multi-leg Flights**: Support for connections
5. **Loyalty Program**: More sophisticated rewards calculation
6. **Report Export**: PDF generation for revenue reports
7. **Database Migration**: Move from text files to proper database

---

## 12. Credits

- **Framework**: Java Swing/AWT
- **Architecture**: Command Pattern, MVC
- **Authentication**: SHA-256 password hashing
- **Date Handling**: Java 8 LocalDate API
- **Design Reference**: React frontend mockups in `Frontend_Design/`

---

*Report generated for Flight Booking System Enhancement Project*
*Last Updated: 2025*
