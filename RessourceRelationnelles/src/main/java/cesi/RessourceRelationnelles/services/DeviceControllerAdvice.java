package cesi.RessourceRelationnelles.services;

import cesi.RessourceRelationnelles.utils.DeviceDetector;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(basePackages = "cesi.RessourceRelationnelles.frontControllers")
public class DeviceControllerAdvice {

    private final DeviceDetector deviceDetector;

    @Autowired
    public DeviceControllerAdvice(DeviceDetector deviceDetector) {
        this.deviceDetector = deviceDetector;
    }

    @ModelAttribute
    public void addDeviceAttributes(Model model, HttpServletRequest request) {
        model.addAttribute("isMobile", deviceDetector.isMobile(request));
    }
}