package com.example.portfolio.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.portfolio.service.mapper.FlexAndGoogleSheetModalsMapper;

@RestController
public class RunJob {

    private FlexAndGoogleSheetModalsMapper flexAndGoogleSheetModalsMapper;
    private static final Logger log = LoggerFactory.getLogger(RunJob.class);

    public RunJob(FlexAndGoogleSheetModalsMapper flexAndGoogleSheetModalsMapper){

        this.flexAndGoogleSheetModalsMapper = flexAndGoogleSheetModalsMapper;
    }

    @GetMapping("/run")
    public String runJob(){

        log.info("Starting Job");

        flexAndGoogleSheetModalsMapper.sendTransactionListToGoogleSheets();

        return "Job completed";
    }
}
