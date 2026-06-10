package cesi.RessourceRelationnelles.services;

import cesi.RessourceRelationnelles.dtos.StatItemDTO;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;

/**
 * Service pour formater et exporter les statistiques du dashboard au format CSV.
 * Découple le contrôleur du protocole de présentation des données.
 */
@Service
public class CsvExportService {

    public void writeDashboardStats(
            Writer writer,
            long totalRessources,
            long totalUsers,
            long totalFriends,
            long totalComments,
            long totalCategories,
            long totalRelations,
            long totalTypes,
            List<StatItemDTO> statsByCategory,
            List<StatItemDTO> statsByStatus,
            List<StatItemDTO> statsByVisibility) {
        
        PrintWriter printWriter = new PrintWriter(writer);
        // BOM UTF-8
        printWriter.print('\ufeff');
        
        printWriter.println("Type de Statistique;Valeur");
        printWriter.println("Total Ressources;" + totalRessources);
        printWriter.println("Total Utilisateurs;" + totalUsers);
        printWriter.println("Total Amis;" + totalFriends);
        printWriter.println("Total Commentaires;" + totalComments);
        printWriter.println("Total Catégories actives;" + totalCategories);
        printWriter.println("Total Relations actives;" + totalRelations);
        printWriter.println("Total Types actifs;" + totalTypes);
        printWriter.println(";");

        printWriter.println("RÉPARTITION PAR CATÉGORIE;");
        printWriter.println("Catégorie;Nombre");
        for (StatItemDTO stat : statsByCategory) {
            printWriter.println(stat.getKey() + ";" + stat.getCount());
        }
        printWriter.println(";");

        printWriter.println("RÉPARTITION PAR STATUT;");
        printWriter.println("STATUT;Nombre");
        for (StatItemDTO stat : statsByStatus) {
            printWriter.println(stat.getKey() + ";" + stat.getCount());
        }
        printWriter.println(";");

        printWriter.println("RÉPARTITION PAR VISIBILITÉ;");
        printWriter.println("Visibilité;Nombre");
        for (StatItemDTO stat : statsByVisibility) {
            printWriter.println(stat.getKey() + ";" + stat.getCount());
        }
        
        printWriter.flush();
    }
}
