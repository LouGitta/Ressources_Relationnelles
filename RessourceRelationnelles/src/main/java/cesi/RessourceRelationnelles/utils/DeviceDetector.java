package cesi.RessourceRelationnelles.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

/**
 * Utilitaire pour détecter le type d'appareil (mobile ou ordinateur)
 * en analysant le User-Agent HTTP
 */
@Component
public class DeviceDetector {

    /**
     * Détecte si la requête provient d'un appareil mobile
     * 
     * @param request la requête HTTP
     * @return true si c'est un mobile, false sinon
     */
    public boolean isMobile(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        
        // Si pas de User-Agent disponible, considère que ce n'est pas mobile
        if (userAgent == null) {
            return false;
        }
        
        // Convertit en minuscules pour une comparaison insensible à la casse
        String ua = userAgent.toLowerCase();
        
        // Vérifie les patterns typiques des appareils mobiles
        return ua.contains("mobile") || 
               ua.contains("android") || 
               ua.contains("iphone") || 
               ua.contains("ipad") ||
               ua.contains("windows phone") ||
               ua.contains("blackberry") ||
               ua.contains("opera mini");
    }
}
