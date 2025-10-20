package com.example.portfolio.service.connection;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class GoogleSheetConnectionService {

    public List<LocalDate> getDatesOfPastTransactionsOnGoogleSheets(){
        List<LocalDate> dates = new ArrayList<>();
        return dates;
    }
}
