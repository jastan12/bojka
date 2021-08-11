package pl.bojka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.bojka.model.BuoyDTO;
import pl.bojka.service.BuoyDataService;

import java.util.Collections;
import java.util.List;

@Controller
public class BuoyDataController {

    private final BuoyDataService buoyDataService;

    @Autowired
    public BuoyDataController(BuoyDataService buoyDataService) {
        this.buoyDataService = buoyDataService;
    }

    @GetMapping
    public String test(Model model){
        List<BuoyDTO> listToDisplay = buoyDataService.getDataToDisplay();
        model.addAttribute("bojki", listToDisplay);
        return "data";
    }
}
