package run.geoffrey.walton.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import run.geoffrey.walton.dto.WorkshopDTO;
import run.geoffrey.walton.service.WorkshopService;

import java.util.List;

/**
 * Controller for the home page.
 */
@Controller
public class HomeController {

    private final WorkshopService workshopService;

    @Autowired
    public HomeController(WorkshopService workshopService) {
        this.workshopService = workshopService;
    }

    /**
     * Displays the home page with featured workshops.
     *
     * @param model The model to add attributes to
     * @return The name of the view to render
     */
    @GetMapping("/")
    public String home(Model model) {
        List<WorkshopDTO> availableWorkshops = workshopService.getAvailableWorkshops();
        List<WorkshopDTO> upcomingWorkshops = workshopService.getUpcomingWorkshops();
        
        model.addAttribute("availableWorkshops", availableWorkshops);
        model.addAttribute("upcomingWorkshops", upcomingWorkshops);
        
        return "home";
    }
}