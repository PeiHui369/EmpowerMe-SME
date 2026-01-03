package com.empowerme.spring.service;

import com.empowerme.spring.domain.ContentAsset;
import com.empowerme.spring.domain.ContentStatus;
import com.empowerme.spring.repository.ContentAssetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentService {

    private final ContentAssetRepository repository;

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final String RESTRICTED_EXTENSION = ".exe";

    public ContentService(ContentAssetRepository repository) {
        this.repository = repository;
    }

    public List<ContentAsset> getAllAssets() {
        return repository.findAll();
    }

    public ContentAsset createAsset(ContentAsset asset) {
        validate(asset);
        asset.setProductionCost(calculateCost(asset.getType()));
        if (asset.getStatus() == null) {
            asset.setStatus(ContentStatus.DRAFT);
        }
        return repository.save(asset);
    }

    public ContentAsset pushToLive(Long id) {
        ContentAsset asset = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asset not found"));
        
        asset.setStatus(ContentStatus.LIVE);
        return repository.save(asset);
    }

    private void validate(ContentAsset asset) {
        if (asset.getFileSize() != null && asset.getFileSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size exceeds limit of 5MB");
        }
        if (asset.getFileExtension() != null && asset.getFileExtension().toLowerCase().endsWith(RESTRICTED_EXTENSION)) {
            // Requirement says "throw error or flag it". Let's flag it for now as per one option, 
            // or just throw exception to be safe. Let's throwing exception for strict validation.
            throw new IllegalArgumentException("File type .exe is restricted");
        }
    }

    private Double calculateCost(String type) {
        if (type == null) return 0.0;
        if (type.equalsIgnoreCase("VIDEO")) {
            return 100.0;
        } else if (type.equalsIgnoreCase("IMAGE")) {
            return 50.0;
        } else {
            return 0.0;
        }
    }
}
