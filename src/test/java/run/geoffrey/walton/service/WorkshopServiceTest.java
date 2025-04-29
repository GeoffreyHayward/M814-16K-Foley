package run.geoffrey.walton.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import run.geoffrey.walton.dto.WorkshopDTO;
import run.geoffrey.walton.model.Workshop;
import run.geoffrey.walton.repository.WorkshopRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WorkshopServiceTest {

    @Mock
    private WorkshopRepository workshopRepository;

    @InjectMocks
    private WorkshopService workshopService;

    private Workshop availableWorkshop;
    private Workshop fullyBookedWorkshop;
    private Workshop pastWorkshop;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create test workshops
        LocalDateTime now = LocalDateTime.now();

        // Available workshop (has available slots and is in the future)
        availableWorkshop = new Workshop();
        availableWorkshop.setId(1);
        availableWorkshop.setName("Available Workshop");
        availableWorkshop.setDescription("This workshop has available slots");
        availableWorkshop.setDateTime(now.plusDays(7));
        availableWorkshop.setCapacity(20);
        availableWorkshop.setCurrentBookings(10);

        // Fully booked workshop (no available slots but is in the future)
        fullyBookedWorkshop = new Workshop();
        fullyBookedWorkshop.setId(2);
        fullyBookedWorkshop.setName("Fully Booked Workshop");
        fullyBookedWorkshop.setDescription("This workshop is fully booked");
        fullyBookedWorkshop.setDateTime(now.plusDays(7));
        fullyBookedWorkshop.setCapacity(20);
        fullyBookedWorkshop.setCurrentBookings(20);

        // Past workshop
        pastWorkshop = new Workshop();
        pastWorkshop.setId(3);
        pastWorkshop.setName("Past Workshop");
        pastWorkshop.setDescription("This workshop is in the past");
        pastWorkshop.setDateTime(now.minusDays(7));
        pastWorkshop.setCapacity(20);
        pastWorkshop.setCurrentBookings(15);
    }

    @Test
    void testGetAllWorkshops() {
        // Arrange
        when(workshopRepository.findAll()).thenReturn(Arrays.asList(availableWorkshop, fullyBookedWorkshop, pastWorkshop));

        // Act
        List<WorkshopDTO> result = workshopService.getAllWorkshops();

        // Assert
        assertEquals(3, result.size());
        verify(workshopRepository, times(1)).findAll();
    }

    @Test
    void testGetUpcomingWorkshops() {
        // Arrange
        when(workshopRepository.findByDateTimeAfterOrderByDateTimeAsc(any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(availableWorkshop, fullyBookedWorkshop));

        // Act
        List<WorkshopDTO> result = workshopService.getUpcomingWorkshops();

        // Assert
        assertEquals(2, result.size());
        verify(workshopRepository, times(1)).findByDateTimeAfterOrderByDateTimeAsc(any(LocalDateTime.class));
    }

    @Test
    void testGetPastWorkshops() {
        // Arrange
        when(workshopRepository.findByDateTimeBeforeOrderByDateTimeDesc(any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(pastWorkshop));

        // Act
        List<WorkshopDTO> result = workshopService.getPastWorkshops();

        // Assert
        assertEquals(1, result.size());
        verify(workshopRepository, times(1)).findByDateTimeBeforeOrderByDateTimeDesc(any(LocalDateTime.class));
    }

    @Test
    void testGetAvailableWorkshops() {
        // Arrange
        when(workshopRepository.findAvailableWorkshops()).thenReturn(Arrays.asList(availableWorkshop));

        // Act
        List<WorkshopDTO> result = workshopService.getAvailableWorkshops();

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.get(0).isAvailable());
        verify(workshopRepository, times(1)).findAvailableWorkshops();
    }

    @Test
    void testGetFullyBookedWorkshops() {
        // Arrange
        when(workshopRepository.findFullyBookedWorkshops()).thenReturn(Arrays.asList(fullyBookedWorkshop));

        // Act
        List<WorkshopDTO> result = workshopService.getFullyBookedWorkshops();

        // Assert
        assertEquals(1, result.size());
        assertFalse(result.get(0).isAvailable());
        verify(workshopRepository, times(1)).findFullyBookedWorkshops();
    }

    @Test
    void testGetWorkshopById() {
        // Arrange
        when(workshopRepository.findById(1)).thenReturn(Optional.of(availableWorkshop));

        // Act
        Optional<WorkshopDTO> result = workshopService.getWorkshopById(1);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Available Workshop", result.get().getName());
        verify(workshopRepository, times(1)).findById(1);
    }

    @Test
    void testIsWorkshopAvailable() {
        // Arrange
        when(workshopRepository.findById(1)).thenReturn(Optional.of(availableWorkshop));
        when(workshopRepository.findById(2)).thenReturn(Optional.of(fullyBookedWorkshop));
        when(workshopRepository.findById(3)).thenReturn(Optional.of(pastWorkshop));

        // Act & Assert
        assertTrue(workshopService.isWorkshopAvailable(1));
        assertFalse(workshopService.isWorkshopAvailable(2));
        assertFalse(workshopService.isWorkshopAvailable(3));

        verify(workshopRepository, times(1)).findById(1);
        verify(workshopRepository, times(1)).findById(2);
        verify(workshopRepository, times(1)).findById(3);
    }

    @Test
    void testGetAvailableSlots() {
        // Arrange
        when(workshopRepository.findById(1)).thenReturn(Optional.of(availableWorkshop));

        // Act
        int result = workshopService.getAvailableSlots(1);

        // Assert
        assertEquals(10, result); // capacity (20) - currentBookings (10)
        verify(workshopRepository, times(1)).findById(1);
    }
}
