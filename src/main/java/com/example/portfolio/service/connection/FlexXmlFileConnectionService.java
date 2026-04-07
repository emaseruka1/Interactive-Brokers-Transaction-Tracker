package com.example.portfolio.service.connection;

import com.fasterxml.jackson.databind.JsonNode;

public interface FlexXmlFileConnectionService {

    JsonNode parseFlexXmlFileToJson();
}
