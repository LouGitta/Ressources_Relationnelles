package cesi.RessourceRelationnelles.frontControllers.admin;

import cesi.RessourceRelationnelles.services.FriendService;
import cesi.RessourceRelationnelles.services.CsvExportService;
import cesi.RessourceRelationnelles.dtos.StatItemDTO;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import cesi.RessourceRelationnelles.services.CategoryService;
import cesi.RessourceRelationnelles.services.CommentService;
import cesi.RessourceRelationnelles.services.RessourceService;
import cesi.RessourceRelationnelles.services.TypeService;
import cesi.RessourceRelationnelles.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import cesi.RessourceRelationnelles.services.RelationService;

@Controller
@RequestMapping("/admin/home")
public class DashboardController {

    @Autowired
    private RessourceService ressourceService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private FriendService friendService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private RelationService relationService;
    @Autowired
    private CsvExportService csvExportService;

    @GetMapping
    public String showDashboard(Model model) {
        model.addAttribute("totalRessources", ressourceService.countAll());
        model.addAttribute("totalUsers", userService.countAll());
        model.addAttribute("totalFriends", friendService.countAll());
        model.addAttribute("totalComments", commentService.countAll());
        model.addAttribute("totalTypes", typeService.countAll());
        model.addAttribute("totalRelations", relationService.countAll());
        model.addAttribute("totalCategories", categoryService.countAll());
        model.addAttribute("ressourcesByCategory", ressourceService.countRessourcesByCategory());
        model.addAttribute("ressourcesByStatus", ressourceService.countByStatus());
        model.addAttribute("ressourcesByVisibility", ressourceService.countByVisibility());
        return "admin/dashboard";
    }

    @GetMapping("/export")
    public void exportDashboardStatsToCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"statistiques_dashboard.csv\"");

        csvExportService.writeDashboardStats(
            response.getWriter(),
            ressourceService.countAll(),
            userService.countAll(),
            friendService.countAll(),
            commentService.countAll(),
            categoryService.countAll(),
            relationService.countAll(),
            typeService.countAll(),
            ressourceService.countRessourcesByCategory(),
            ressourceService.countByStatus(),
            ressourceService.countByVisibility()
        );
    }
}