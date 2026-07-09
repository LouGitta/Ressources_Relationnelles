package cesi.RessourceRelationnelles.frontControllers.admin;

import cesi.RessourceRelationnelles.services.CsvExportService;
import cesi.RessourceRelationnelles.services.FriendService;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import cesi.RessourceRelationnelles.services.RelationService;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Contrôleur du tableau de bord d'administration.
 * Affiche les statistiques globales de l'application et permet l'export CSV.
 */
@Controller
@RequestMapping("/admin/home")
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

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

    /**
     * Affiche le tableau de bord avec les statistiques globales.
     *
     * @param model Le modèle pour la vue
     * @return La vue "admin/dashboard"
     */
    @GetMapping
    public String showDashboard(Model model) {
        logger.debug("Chargement du tableau de bord admin");

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

        logger.info("Tableau de bord admin chargé");
        return "admin/dashboard";
    }

    /**
     * Exporte les statistiques du tableau de bord au format CSV.
     *
     * @param response La réponse HTTP pour l'envoi du fichier
     * @throws IOException En cas d'erreur d'écriture
     */
    @GetMapping("/export")
    public void exportDashboardStatsToCSV(HttpServletResponse response) throws IOException {
        logger.info("Export CSV des statistiques du dashboard");

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