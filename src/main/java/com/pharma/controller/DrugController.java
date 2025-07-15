package com.pharma.controller;

import com.pharma.model.Drug;
import com.pharma.service.DrugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class DrugController {

    @Autowired
    private DrugService drugService;

    // Home page with drug list and optional search
    @GetMapping("/")
    public String index(@RequestParam(value = "search", required = false) String search, Model model) {
        List<Drug> drugs = drugService.getAllDrugs();

        if (search != null && !search.isBlank()) {
            drugs = drugs.stream()
                    .filter(d -> d.getName().toLowerCase().contains(search.toLowerCase()) ||
                                 d.getManufacturer().toLowerCase().contains(search.toLowerCase()))
                    .toList();
        }

        model.addAttribute("drugs", drugs);
        return "index";
    }

    // Drug details page
    @GetMapping("/drug/{name}")
    public String drugDetail(@PathVariable String name, Model model) {
        Drug drug = drugService.getDrugByName(name);
        model.addAttribute("drug", drug);
        return "detail";
    }
}
