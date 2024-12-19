package com.eureka_server.eureka_server.controllers;

import com.netflix.discovery.shared.Applications;
import com.netflix.eureka.EurekaServerContextHolder;
import com.netflix.eureka.registry.PeerAwareInstanceRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Controller
public class AdminController {

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        PeerAwareInstanceRegistry registry = EurekaServerContextHolder.getInstance().getServerContext().getRegistry();
        Applications applications = registry.getApplications();

        Map<String, Integer> serviceInstances = new HashMap<>();
        applications.getRegisteredApplications().forEach(app -> 
            serviceInstances.put(app.getName(), app.getInstances().size())
        );

        model.addAttribute("serviceInstances", serviceInstances);
        model.addAttribute("totalServices", applications.size());
        model.addAttribute("serverStatus", "UP");
        model.addAttribute("lastUpdate", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        return "admin/dashboard";
    }
} 