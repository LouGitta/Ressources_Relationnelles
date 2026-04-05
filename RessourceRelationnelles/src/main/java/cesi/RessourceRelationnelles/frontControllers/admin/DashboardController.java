package cesi.RessourceRelationnelles.frontControllers.admin;

import cesi.RessourceRelationnelles.services.FriendService;

import java.io.IOException;
import java.io.PrintWriter;
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

        PrintWriter writer = response.getWriter();
        writer.print('\ufeff');

        writer.println("Type de Statistique;Valeur");

        writer.println("Total Ressources;" + ressourceService.countAll());
        writer.println("Total Utilisateurs;" + userService.countAll());
        writer.println("Total Amis;" + friendService.countAll());
        writer.println("Total Commentaires;" + commentService.countAll());
        writer.println("Total Catégories actives;" + categoryService.countAll());
        writer.println("Total Relations actives;" + relationService.countAll());
        writer.println("Total Types actifs;" + typeService.countAll());

        writer.println(";");

        writer.println("RÉPARTITION PAR CATÉGORIE;");
        writer.println("Catégorie;Nombre");
        List<Object[]> statsByCategory = ressourceService.countRessourcesByCategory();
        for (Object[] stat : statsByCategory) {
            writer.println(stat[0] + ";" + stat[1]);
        }

        writer.println(";");

        writer.println("RÉPARTITION PAR STATUT;");
        writer.println("STATUT;Nombre");
        List<Object[]> statsByStatus = ressourceService.countByStatus();
        for (Object[] stat : statsByStatus) {
            writer.println(stat[0] + ";" + stat[1]);
        }
        writer.println(";");

        writer.println("RÉPARTITION PAR VISIBILITÉ;");
        writer.println("Visibilité;Nombre");
        List<Object[]> statsByVisibility = ressourceService.countByVisibility();
        for (Object[] stat : statsByVisibility) {
            writer.println(stat[0] + ";" + stat[1]);
        }

        writer.flush();
        writer.close();
    }

}