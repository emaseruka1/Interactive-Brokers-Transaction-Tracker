package com.example.portfolio.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.portfolio.service.mapper.FlexAndGoogleSheetModalsMapper;

@RestController
public class RunJob {

    private FlexAndGoogleSheetModalsMapper flexAndGoogleSheetModalsMapper;

    @GetMapping("/run")
    public String runJob(){

        flexAndGoogleSheetModalsMapper.sendTransactionListToGoogleSheets();

        return "Job completed";
    }
}
