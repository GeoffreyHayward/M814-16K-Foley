package run.geoffrey.walton.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import run.geoffrey.walton.dto.WorkshopDTO;
import run.geoffrey.walton.service.WorkshopService;

import java.util.List;

/**
 * Controller for workshop-related endpoints.
 */
@Controller
@RequestMapping("/workshops")
public class WorkshopController {

    private final WorkshopService workshopService;

    @Autowired
    public WorkshopController(WorkshopService workshopService) {
        this.workshopService = workshopService;
    }

    /**
     * Displays a list of all workshops.
     *
     * @param model The model to add attributes to
     * @return The name of the view to render
     */
    @GetMapping
    public String listWorkshops(Model model) {
        List<WorkshopDTO> upcomingWorkshops = workshopService.getUpcomingWorkshops();
        List<WorkshopDTO> pastWorkshops = workshopService.getPastWorkshops();

        model.addAttribute("upcomingWorkshops", upcomingWorkshops);
        model.addAttribute("pastWorkshops", pastWorkshops);
        model.addAttribute("title", "All Workshops");

        return "workshops/list";
    }

    /**
     * Displays a list of available workshops.
     *
     * @param model The model to add attributes to
     * @return The name of the view to render
     */
    @GetMapping("/available")
    public String listAvailableWorkshops(Model model) {
        List<WorkshopDTO> availableWorkshops = workshopService.getAvailableWorkshops();

        model.addAttribute("workshops", availableWorkshops);
        model.addAttribute("title", "Available Workshops");
        model.addAttribute("showAvailability", true);

        return "workshops/available";
    }

    /**
     * Displays details of a specific workshop.
     *
     * @param id    The ID of the workshop to display
     * @param model The model to add attributes to
     * @return The name of the view to render
     */
    @GetMapping("/{id}")
    public String viewWorkshop(@PathVariable Integer id, Model model) {
        return workshopService.getWorkshopById(id)
                .map(workshop -> {
                    model.addAttribute("workshop", workshop);
                    return "workshops/detail";
                })
                .orElse("redirect:/workshops");
    }

    /**
     * Checks the availability of a specific workshop.
     *
     * @param id    The ID of the workshop to check
     * @param model The model to add attributes to
     * @return The name of the view to render
     */
    @GetMapping("/{id}/availability")
    public String checkWorkshopAvailability(@PathVariable Integer id, Model model) {
        return workshopService.getWorkshopById(id)
                .map(workshop -> {
                    model.addAttribute("workshop", workshop);
                    model.addAttribute("isAvailable", workshop.isAvailable());
                    model.addAttribute("availableSlots", workshop.getAvailableSlots());
                    return "workshops/availability";
                })
                .orElse("redirect:/workshops");
    }
}
