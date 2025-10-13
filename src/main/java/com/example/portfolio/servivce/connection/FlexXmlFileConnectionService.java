package com.example.portfolio.servivce.connection;

import com.example.portfolio.model.FlexXmlModal;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FlexXmlFileConnectionService {

    @Value("${flex.query.directory}")
    private String flexFileDirectory;

    @Value("${flex.query.filename}")
    private String filename;

    private final XmlMapper xmlMapper;

    public FlexXmlFileConnectionService(XmlMapper xmlMapper){

        this.xmlMapper= new XmlMapper();
    }

    public File getFlexXmlFile(){

        Path filepath = Paths.get(flexFileDirectory,filename);
        File FlexXmlFile = filepath.toFile();
        return FlexXmlFile;
    }

    public parseFlexXmlFile(){

        File FlexXmlFile = getFlexXmlFile();
        //xmlMapper.readValue(FlexXmlFile, FlexXmlModal.class);

    }
}
