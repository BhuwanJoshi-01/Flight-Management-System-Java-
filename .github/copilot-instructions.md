# Flight Booking System - AI Coding Agent Instructions

## Architecture Overview

This is a Java-based Flight Booking System with both CLI and GUI interfaces, using text-file persistence and implementing the Command Pattern.

**Core Architecture:**
- **MVC Pattern**: Model (`model/`), GUI Views (`gui/`), Commands as Controllers (`commands/`)
- **Command Pattern**: All operations implement `Command` interface with `execute(FlightBookingSystem)` method
- **Data Access Layer**: `DataManager` implementations handle text file I/O with `::` field separator
- **Dual Interface**: CLI via `Main.java` and GUI via `LoadGUI` command

## Key Package Structure

```
bcu.cmp5332.bookingsystem/
├── main/           # Entry points (Main.java, CommandParser.java)
├── model/          # Core entities (Flight, Customer, Booking, FlightBookingSystem)
├── commands/       # Command pattern implementations
├── data/           # Persistence layer (DataManager implementations)
└── gui/            # Swing GUI components
```

## Critical Patterns & Conventions

### Data Persistence Format
All data files use `::` as field separator with trailing `::`. Format examples:
- **flights.txt**: `id::flightNumber::origin::destination::date::capacity::price::deleted::`
- **customers.txt**: `id::name::phone::email::deleted::`
- **bookings.txt**: `customerId::outboundFlightId::returnFlightId::bookingDate::cancelled::cancellationFee::`

### Command Pattern Implementation
Every user operation extends `Command` interface:
```java
public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException;
```
Commands are parsed in [CommandParser.java](src/bcu/cmp5332/bookingsystem/main/CommandParser.java) using string splitting.

### Soft Delete Pattern
Entities use `deleted` boolean field - never physically remove data, only mark as deleted:
- Use `getFlights()` for active flights, `getAllFlights()` for persistence
- Filter deleted entities in display methods, include all in data managers

### ID Management
- Auto-increment IDs in each entity collection (TreeMap<Integer, Entity>)
- ID generation: `Collections.max(existingIds) + 1` pattern
- IDs are unique within entity type but not globally

### Constructor Backward Compatibility
Models have multiple constructors for backward compatibility:
```java
// Original constructor
public Flight(int id, String flightNumber, String origin, String destination, LocalDate departureDate)
// Extended constructor  
public Flight(int id, String flightNumber, String origin, String destination, LocalDate departureDate, int capacity, double price)
```

## Development Workflows

### Testing
- Uses JUnit 5 framework
- Test classes mirror package structure in `test/` directory
- Run via IDE or command line: `java -cp classes:junit5.jar org.junit.platform.console.ConsoleLauncher`

### Data Loading/Storing
System automatically loads from [resources/data/](resources/data/) on startup and saves on exit:
```java
FlightBookingSystem fbs = FlightBookingSystemData.load();  // Auto-loads all DataManagers
FlightBookingSystemData.store(fbs);  // Auto-saves all DataManagers
```

### GUI Integration
CLI and GUI share the same model layer:
- Use `loadgui` command to switch from CLI to GUI
- GUI components in separate panels: [DashboardPanel](src/bcu/cmp5332/bookingsystem/gui/DashboardPanel.java), [FlightsPanel](src/bcu/cmp5332/bookingsystem/gui/FlightsPanel.java), etc.

## Key Files for Understanding

- [FlightBookingSystem.java](src/bcu/cmp5332/bookingsystem/model/FlightBookingSystem.java) - Central model with system date (fixed: 2024-11-11)
- [FlightBookingSystemData.java](src/bcu/cmp5332/bookingsystem/data/FlightBookingSystemData.java) - Data access orchestration
- [Command.java](src/bcu/cmp5332/bookingsystem/commands/Command.java) - Command interface with help text
- [MainWindow.java](src/bcu/cmp5332/bookingsystem/gui/MainWindow.java) - Modern dashboard-style GUI

## Special Features

### Dynamic Pricing
[DynamicPricing.java](src/bcu/cmp5332/bookingsystem/model/DynamicPricing.java) calculates flight prices based on:
- Time until departure (time sensitivity factor α)
- Capacity utilization (capacity sensitivity factor β)
- Configurable booking window (default 30 days)

### Return Flight Bookings
[Booking.java](src/bcu/cmp5332/bookingsystem/model/Booking.java) supports both one-way and return flights:
- `outboundFlight` (required) + `returnFlight` (optional)
- Cancellation fees calculated as 10% of total booking value

### Exception Handling
Custom `FlightBookingSystemException` for domain-specific errors. Always provide meaningful error messages for CLI users.

## Common Pitfalls

1. **Data Format**: Always append `::` to data file lines - missing separator breaks parsing
2. **Date Format**: Use `LocalDate.parse()` with ISO format (YYYY-MM-DD)
3. **Soft Delete**: Check `isDeleted()` in display methods, but include deleted entities in data persistence
4. **ID Conflicts**: Generate new IDs using max existing ID + 1, not hardcoded values
5. **Command Parsing**: Commands are case-sensitive and space-delimited - follow existing patterns in [CommandParser.java](src/bcu/cmp5332/bookingsystem/main/CommandParser.java)