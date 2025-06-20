package com.siddhu.controller;

import com.siddhu.service.MCPServerMongoServiceClient;
import org.bson.Document;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mongo")
public class MongoController {

    private final MCPServerMongoServiceClient mongoService;

    public MongoController(MCPServerMongoServiceClient mongoService) {
        this.mongoService = mongoService;
    }

    @GetMapping("/databases")
    public List<String> listDatabases() {
        return mongoService.listDatabases();
    }

    @GetMapping("/collections")
    public List<String> listCollections(@RequestParam String dbName) {
        return mongoService.listCollections(dbName);
    }

    @GetMapping("/indexes")
    public List<Document> listIndexes(@RequestParam String dbName, @RequestParam String collectionName) {
        return mongoService.listIndexes(dbName, collectionName);
    }

    @PostMapping("/create-collection")
    public String createCollection(@RequestParam String dbName, @RequestParam String collectionName) {
        return mongoService.createCollection(dbName, collectionName);
    }
}