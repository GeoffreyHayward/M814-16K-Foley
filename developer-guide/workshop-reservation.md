# Workshop Reservation Implementation

This document describes the implementation of the workshop reservation functionality in the Walton Sustainability Centre application.

## Overview

The workshop reservation feature allows users to reserve a spot in an available workshop. The implementation follows the layered architecture pattern:

- **Model**: Represents the reservation data
- **Repository**: Provides data access methods
- **Service**: Contains business logic
- **Controller**: Handles HTTP requests
- **Templates**: Provides the user interface

## Database Schema

The reservation data is stored in the `reservations` table, which has the following structure:

```sql
CREATE TABLE reservations (
    id SERIAL PRIMARY KEY,
    workshop_id INTEGER NOT NULL,
    attendee_name VARCHAR(255) NOT NULL,
    attendee_email VARCHAR(255) NOT NULL,
    reservation_date TIMESTAMP NOT NULL,
    FOREIGN KEY (workshop_id) REFERENCES workshops(id)
);
```

Indexes are created for faster lookups:

```sql
CREATE INDEX idx_reservations_workshop_id ON reservations(workshop_id);
CREATE INDEX idx_reservations_attendee_email ON reservations(attendee_email);
```

## Model

The `Reservation` class represents a reservation entity:

```java
@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "workshop_id", nullable = false)
    private Workshop workshop;

    @Column(nullable = false)
    private String attendeeName;

    @Column(nullable = false)
    private String attendeeEmail;

    @Column(nullable = false)
    private LocalDateTime reservationDate;

    // Constructors, getters, setters, equals, hashCode, toString
}
```

## Repository

The `ReservationRepository` interface provides data access methods:

```java
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findByWorkshop(Workshop workshop);
    List<Reservation> findByWorkshopId(Integer workshopId);
    List<Reservation> findByAttendeeEmail(String email);
    long countByWorkshop(Workshop workshop);
    long countByWorkshopId(Integer workshopId);
}
```

## DTO

The `ReservationDTO` class transfers reservation data between layers:

```java
public class ReservationDTO {
    private Integer id;
    private Integer workshopId;
    private String workshopName;
    private String attendeeName;
    private String attendeeEmail;
    private LocalDateTime reservationDate;
    private String formattedReservationDate;
    private String workshopDateTime;

    // Constructors, getters, setters
}
```

## Service

The `ReservationService` class contains the business logic:

```java
@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final WorkshopRepository workshopRepository;

    // Constructor with dependency injection

    // Methods for getting reservations
    public List<ReservationDTO> getAllReservations() { ... }
    public List<ReservationDTO> getReservationsByWorkshopId(Integer workshopId) { ... }
    public List<ReservationDTO> getReservationsByAttendeeEmail(String email) { ... }
    public Optional<ReservationDTO> getReservationById(Integer id) { ... }

    // Methods for creating and canceling reservations
    @Transactional
    public Optional<ReservationDTO> createReservation(Integer workshopId, String attendeeName, String attendeeEmail) { ... }

    @Transactional
    public boolean cancelReservation(Integer id) { ... }

    // Method for counting reservations
    public long countReservationsByWorkshopId(Integer workshopId) { ... }
}
```

The key methods are:

- `createReservation`: Creates a new reservation if the workshop is available, increments the workshop's current bookings, and returns the created reservation
- `cancelReservation`: Cancels a reservation, decrements the workshop's current bookings, and returns true if successful

## Controller

The `ReservationController` class handles HTTP requests:

```java
@Controller
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;
    private final WorkshopService workshopService;

    // Constructor with dependency injection

    // Endpoints for creating reservations
    @GetMapping("/create/{workshopId}")
    public String showReservationForm(@PathVariable Integer workshopId, Model model) { ... }

    @PostMapping("/create/{workshopId}")
    public String showBookingSummary(@PathVariable Integer workshopId, @RequestParam String attendeeName, @RequestParam String attendeeEmail, Model model, RedirectAttributes redirectAttributes) { ... }

    // Endpoint for processing payment and creating reservation
    @PostMapping("/process-payment")
    public String processPaymentAndCreateReservation(@RequestParam Integer workshopId, @RequestParam String attendeeName, @RequestParam String attendeeEmail, RedirectAttributes redirectAttributes) { ... }

    // Endpoint for showing confirmation
    @GetMapping("/confirmation/{id}")
    public String showConfirmation(@PathVariable Integer id, Model model) { ... }

    // Endpoints for listing reservations
    @GetMapping("/workshop/{workshopId}")
    public String listReservationsByWorkshop(@PathVariable Integer workshopId, Model model) { ... }

    // Endpoints for canceling reservations
    @GetMapping("/cancel/{id}")
    public String showCancelForm(@PathVariable Integer id, Model model) { ... }

    @PostMapping("/cancel/{id}")
    public String cancelReservation(@PathVariable Integer id, RedirectAttributes redirectAttributes) { ... }
}
```

## Templates

The following Thymeleaf templates provide the user interface:

- `reservations/create.html`: Form for creating a reservation
- `reservations/summary.html`: Booking summary page before payment
- `reservations/confirmation.html`: Confirmation page after successful reservation
- `reservations/cancel.html`: Form for canceling a reservation
- `reservations/list.html`: List of reservations for a workshop (for administrative purposes)

## Reservation Flow

The reservation process follows these steps:

1. **Reservation Form**: The user fills out a form with their name and email.
2. **Booking Summary**: The user is shown a summary of their booking details before proceeding to payment.
3. **Payment Processing**: The user enters payment details and completes the payment.
4. **Reservation Creation**: After successful payment, the reservation is created in the database.
5. **Confirmation**: The user is shown a confirmation page with their reservation details.

This flow ensures that users can review their booking details before making a payment, as required by FR-16.

## Integration with Workshop Feature

The reservation feature integrates with the existing workshop feature:

- The "Book Now" button on the workshop detail page links to the reservation form
- When a reservation is created, the workshop's current bookings are incremented
- When a reservation is canceled, the workshop's current bookings are decremented
- The workshop's availability is checked before creating a reservation

## Transaction Management

The `@Transactional` annotation is used on methods that modify data to ensure atomicity. For example, when creating a reservation:

1. The workshop's current bookings are incremented
2. The reservation is created

If either operation fails, the entire transaction is rolled back, ensuring data consistency.

## Testing

The `ReservationServiceTest` class tests the business logic:

```java
class ReservationServiceTest {
    // Tests for getting reservations
    void testGetAllReservations() { ... }
    void testGetReservationsByWorkshopId() { ... }
    void testGetReservationsByAttendeeEmail() { ... }
    void testGetReservationById() { ... }

    // Tests for creating reservations
    void testCreateReservation_Available() { ... }
    void testCreateReservation_FullyBooked() { ... }

    // Tests for canceling reservations
    void testCancelReservation() { ... }
    void testCancelReservation_NotFound() { ... }

    // Test for counting reservations
    void testCountReservationsByWorkshopId() { ... }
}
```
