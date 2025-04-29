# Workshop Availability - Developer Guide

This document provides technical information about the implementation of the workshop availability feature in the Walton Sustainability Centre application.

## Overview

The workshop availability feature implements REQ-ID: FR-32, which states: "The product shall check the availability of the workshop." This feature allows users to check if a workshop has available slots for booking.

The implementation also considers NFR-8, which requires that the availability check be performed quickly (in less than a second for 95% of the time).

## Architecture

The feature follows the layered architecture of the application:

1. **Model Layer**: Defines the Workshop entity with availability-related fields and methods
2. **Repository Layer**: Provides data access methods for retrieving workshops
3. **Service Layer**: Contains business logic for checking workshop availability
4. **Controller Layer**: Handles HTTP requests and responses
5. **View Layer**: Thymeleaf templates for displaying workshop availability

## Key Components

### Workshop Entity

The `Workshop` class in the model layer represents a workshop and includes methods to check availability:

```java
@Entity
@Table(name = "workshops")
public class Workshop {
    // Fields...
    
    public boolean isAvailable() {
        return currentBookings < capacity && dateTime.isAfter(LocalDateTime.now());
    }
    
    public boolean isPast() {
        return dateTime.isBefore(LocalDateTime.now());
    }
    
    public int getAvailableSlots() {
        return capacity - currentBookings;
    }
    
    // Other methods...
}
```

### WorkshopDTO

The `WorkshopDTO` class transfers workshop data between the service and controller layers, including availability information:

```java
public class WorkshopDTO {
    // Fields...
    
    public WorkshopDTO(Workshop workshop) {
        // Field mapping...
        this.availableSlots = workshop.getAvailableSlots();
        this.available = workshop.isAvailable();
        this.past = workshop.isPast();
    }
    
    // Methods...
}
```

### WorkshopRepository

The `WorkshopRepository` interface provides methods for retrieving workshops based on availability criteria:

```java
@Repository
public interface WorkshopRepository extends JpaRepository<Workshop, Long> {
    // Methods for finding workshops by date...
    
    @Query("SELECT w FROM Workshop w WHERE w.currentBookings < w.capacity AND w.dateTime > CURRENT_TIMESTAMP ORDER BY w.dateTime ASC")
    List<Workshop> findAvailableWorkshops();
    
    @Query("SELECT w FROM Workshop w WHERE w.currentBookings >= w.capacity AND w.dateTime > CURRENT_TIMESTAMP ORDER BY w.dateTime ASC")
    List<Workshop> findFullyBookedWorkshops();
}
```

### WorkshopService

The `WorkshopService` class contains business logic for checking workshop availability:

```java
@Service
public class WorkshopService {
    // Dependencies and methods...
    
    public boolean isWorkshopAvailable(Long id) {
        return workshopRepository.findById(id)
                .map(Workshop::isAvailable)
                .orElse(false);
    }
    
    public int getAvailableSlots(Long id) {
        return workshopRepository.findById(id)
                .map(Workshop::getAvailableSlots)
                .orElse(0);
    }
    
    // Other methods...
}
```

### WorkshopController

The `WorkshopController` class handles HTTP requests related to workshops and their availability:

```java
@Controller
@RequestMapping("/workshops")
public class WorkshopController {
    // Dependencies and methods...
    
    @GetMapping("/{id}/availability")
    public String checkWorkshopAvailability(@PathVariable Long id, Model model) {
        return workshopService.getWorkshopById(id)
                .map(workshop -> {
                    model.addAttribute("workshop", workshop);
                    model.addAttribute("isAvailable", workshop.isAvailable());
                    model.addAttribute("availableSlots", workshop.getAvailableSlots());
                    return "workshops/availability";
                })
                .orElse("redirect:/workshops");
    }
    
    // Other methods...
}
```

## Database Schema

The workshop availability feature uses the `workshops` table with the following schema:

```sql
CREATE TABLE workshops (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    date_time TIMESTAMP NOT NULL,
    capacity INTEGER NOT NULL,
    current_bookings INTEGER NOT NULL
);
```

## Performance Considerations

To meet the performance requirements specified in NFR-8, the implementation:

1. Uses indexed database queries for efficient retrieval
2. Calculates availability at the entity level to avoid complex queries
3. Uses DTOs to transfer only the necessary data
4. Implements caching where appropriate

## Testing

The feature includes unit tests for the service layer to verify that workshop availability is correctly determined:

```java
@Test
void testIsWorkshopAvailable() {
    // Arrange
    when(workshopRepository.findById(1L)).thenReturn(Optional.of(availableWorkshop));
    when(workshopRepository.findById(2L)).thenReturn(Optional.of(fullyBookedWorkshop));
    when(workshopRepository.findById(3L)).thenReturn(Optional.of(pastWorkshop));

    // Act & Assert
    assertTrue(workshopService.isWorkshopAvailable(1L));
    assertFalse(workshopService.isWorkshopAvailable(2L));
    assertFalse(workshopService.isWorkshopAvailable(3L));
    
    // Verify...
}
```

## Future Enhancements

Potential enhancements to the workshop availability feature:

1. Implement real-time availability updates using WebSockets
2. Add waitlist functionality for fully booked workshops
3. Implement notifications when a spot becomes available in a previously fully booked workshop
4. Add analytics to track workshop popularity and booking patterns

## Related Requirements

- **FR-32**: "The product shall check the availability of the workshop"
- **NFR-8**: "The product shall check the availability of workshop or class as quickly as possible"