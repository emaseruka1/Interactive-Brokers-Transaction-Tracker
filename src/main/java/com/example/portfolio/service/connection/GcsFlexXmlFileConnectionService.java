package com.example.portfolio.service.connection;

import com.example.portfolio.controller.RunJob;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.cloud.ReadChannel;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;

@Service
@Profile("!test")
public class GcsFlexXmlFileConnectionService implements FlexXmlFileConnectionService {

    @Value("${flex.query.bucket}")
    private String flexFileBucket;

    @Value("${flex.query.filename}")
    private String filename;

    private final Storage storage = StorageOptions.getDefaultInstance().getService();

    private static final Logger log = LoggerFactory.getLogger(GcsFlexXmlFileConnectionService.class);

    public InputStream getFlexXmlStream() {

        Blob blob = storage.get(flexFileBucket, filename);
        if (blob == null) {
            throw new RuntimeException("File not found in GCS");
        }
        ReadChannel googleCloudStorageBlobChannel = blob.reader();

        InputStream googleCloudStorageBlobStream = Channels.newInputStream(googleCloudStorageBlobChannel);

        return googleCloudStorageBlobStream;
    }

    public JsonNode parseFlexXmlFileToJson() {

        XmlMapper xmlMapper = new XmlMapper();

        log.info("Fetching and Parsing IBKR XML data to JSON");

        try (InputStream googleCloudStorageBlobStream = getFlexXmlStream()) {

            JsonNode FlexIbkrJsonData = xmlMapper.readTree(googleCloudStorageBlobStream);

            log.info("FlexIbkrJsonData: {}",FlexIbkrJsonData);

            return FlexIbkrJsonData;

        } catch (IOException e) {
            throw new RuntimeException("Failed to parse XML from GCS", e);
        }
    }
}
