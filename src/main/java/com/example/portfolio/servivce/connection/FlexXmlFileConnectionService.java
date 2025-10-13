package com.example.portfolio.servivce.connection;

import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FlexXmlFileConnectionService {

    @Value("${flex.query.directory}")
    private String flexFileDirectory;

    @Value("${flex.query.name}")
    private String filename;

    public File getTransactionXmlFile(){
        Path filepath = Paths.get(flexFileDirectory,filename);

        File transactionsXmlFile = filepath.toFile();

        return transactionsXmlFile;
    }

    public void deleteTransactionXmlFile(){

        //delete file after using it

    }
}
