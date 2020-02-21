package net.thumbtack.onlineshop.controller;


import net.thumbtack.onlineshop.dto.Response.SettingsDtoResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static net.thumbtack.onlineshop.utils.propfilecheck.PropertiesFileChecker.getAppProperties;


@RestController
@RequestMapping("/settings")
public class SettingsController {

    private int maxNameLength = getAppProperties().get("max_name_length");
    private int minPasswordLength = getAppProperties().get("min_password_length");


    @GetMapping
    public SettingsDtoResponse getServerSettings(Authentication auth) {
        SettingsDtoResponse settings = new SettingsDtoResponse(maxNameLength, minPasswordLength);
        if (auth != null && auth.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")))
//            Available settings for admin
            return settings;
        else if (auth != null && auth.getAuthorities().contains(new SimpleGrantedAuthority("USER")))
//            Available settings for customer
            return settings;
        else
//            Available settings before login
            return settings;
    }
}
