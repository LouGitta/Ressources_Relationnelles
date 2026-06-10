package cesi.RessourceRelationnelles.services;

import cesi.RessourceRelationnelles.dtos.StatItemDTO;
import cesi.RessourceRelationnelles.models.RessourceStatus;
import cesi.RessourceRelationnelles.models.Visibility;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CsvExportServiceTest {

    @Test
    public void testWriteDashboardStats() {
        CsvExportService csvExportService = new CsvExportService();
        StringWriter writer = new StringWriter();

        List<StatItemDTO> statsByCategory = Arrays.asList(
                new StatItemDTO("Technology", 5L),
                new StatItemDTO("Health", 3L)
        );
        List<StatItemDTO> statsByStatus = Arrays.asList(
                new StatItemDTO(RessourceStatus.published, 6L),
                new StatItemDTO(RessourceStatus.pending, 2L)
        );
        List<StatItemDTO> statsByVisibility = Arrays.asList(
                new StatItemDTO(Visibility.public_visibility, 7L),
                new StatItemDTO(Visibility.private_visibility, 1L)
        );

        csvExportService.writeDashboardStats(
                writer,
                8, 10, 15, 20, 4, 3, 2,
                statsByCategory,
                statsByStatus,
                statsByVisibility
        );

        String csvOutput = writer.toString();

        // Verify some content features
        assertTrue(csvOutput.contains("Type de Statistique;Valeur"));
        assertTrue(csvOutput.contains("Total Ressources;8"));
        assertTrue(csvOutput.contains("Total Utilisateurs;10"));
        assertTrue(csvOutput.contains("Total Amis;15"));
        assertTrue(csvOutput.contains("Total Commentaires;20"));
        assertTrue(csvOutput.contains("Total Catégories actives;4"));
        assertTrue(csvOutput.contains("Total Relations actives;3"));
        assertTrue(csvOutput.contains("Total Types actifs;2"));

        assertTrue(csvOutput.contains("Technology;5"));
        assertTrue(csvOutput.contains("Health;3"));
        assertTrue(csvOutput.contains("published;6"));
        assertTrue(csvOutput.contains("pending;2"));
        assertTrue(csvOutput.contains("public_visibility;7"));
        assertTrue(csvOutput.contains("private_visibility;1"));
    }
}
