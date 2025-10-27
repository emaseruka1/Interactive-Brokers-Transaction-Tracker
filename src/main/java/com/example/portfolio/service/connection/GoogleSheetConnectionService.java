package com.example.portfolio.service.connection;

import com.example.portfolio.utils.DateUtils;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.*;

@Service
public class GoogleSheetConnectionService {

    private static final String credentialsFilePath = "/credentials.json";
    private static final String tokenKeyDirectoryPath = "tokens";
    private static final String applicationName = "IBKR Portfolio Tracker App";
    private static final GsonFactory jsonToJavaObjectsConvertor = GsonFactory.getDefaultInstance();
    private static final List<String> readAndWriteScope = Collections.singletonList(SheetsScopes.SPREADSHEETS);


    private final Sheets googleSheetsApiClient;
    private final String spreadsheetId;

    public GoogleSheetConnectionService(@Value("${google.sheet.id}") String spreadsheetId) throws IOException, GeneralSecurityException {

        this.spreadsheetId = spreadsheetId;

        final NetHttpTransport httpTransportConnectionToGoogleServersAPI = GoogleNetHttpTransport.newTrustedTransport();
        this.googleSheetsApiClient = new Sheets.Builder(httpTransportConnectionToGoogleServersAPI, jsonToJavaObjectsConvertor, getCredentials(httpTransportConnectionToGoogleServersAPI))
                .setApplicationName(applicationName)
                .build();
    }

    public List<LocalDate> getDatesOfPastTransactionsOnGoogleSheets() throws IOException {

        String googleSheetDateColumn = "trackerData!A2:A";
        ValueRange response = googleSheetsApiClient.spreadsheets().values()
                .get(spreadsheetId, googleSheetDateColumn)
                .execute();

        List<List<Object>> rowsOfDateColumn = response.getValues();

        List<LocalDate> DatesOfPastTransactionsOnGoogleSheets = new ArrayList<>();

        if (rowsOfDateColumn == null){return DatesOfPastTransactionsOnGoogleSheets;}

        for (List<Object> row : rowsOfDateColumn) {
            if (!row.isEmpty()){

                DatesOfPastTransactionsOnGoogleSheets.add(DateUtils.stringToLocalDate(row.get(0).toString()));
            }
        }
        return DatesOfPastTransactionsOnGoogleSheets;
    }

    public void appendRowValuesToGoogleSheets(List<Object> rowValues) throws IOException {
        ValueRange appendBody = new ValueRange().setValues(Collections.singletonList(rowValues));

        googleSheetsApiClient.spreadsheets().values()
                .append(spreadsheetId, "trackerData!A:P", appendBody)
                .setValueInputOption("RAW")
                .execute();
    }

    private static HttpRequestInitializer getCredentials(NetHttpTransport httpTransport) throws IOException {
        InputStream in = GoogleSheetConnectionService.class.getResourceAsStream(credentialsFilePath);
        if (in == null) throw new FileNotFoundException("Resource not found: " + credentialsFilePath);

        GoogleCredentials credentials = GoogleCredentials.fromStream(in)
                .createScoped(Collections.singletonList(SheetsScopes.SPREADSHEETS));

        return new HttpCredentialsAdapter(credentials);
    }

}
