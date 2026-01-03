package com.empowerme.spring.controller;

import com.empowerme.spring.domain.ContentAsset;
import com.empowerme.spring.service.ContentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/content")
public class ContentController {

    private final ContentService service;

    public ContentController(ContentService service) {
        this.service = service;
    }

    @GetMapping
    public List<ContentAsset> getAllAssets() {
        return service.getAllAssets();
    }

    @PostMapping
    public ResponseEntity<?> createAsset(@RequestBody ContentAsset asset) {
        try {
            ContentAsset created = service.createAsset(asset);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/live")
    public ResponseEntity<?> pushToLive(@PathVariable Long id) {
        try {
            ContentAsset updated = service.pushToLive(id);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
