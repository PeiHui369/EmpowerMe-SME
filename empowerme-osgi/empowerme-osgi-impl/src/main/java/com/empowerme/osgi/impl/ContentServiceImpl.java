package com.empowerme.osgi.impl;

import com.empowerme.osgi.api.ContentAsset;
import com.empowerme.osgi.api.ContentService;
import org.osgi.service.component.annotations.Component;

import java.util.ArrayList;
import java.util.List;

@Component(service = ContentService.class)
public class ContentServiceImpl implements ContentService {

    private final List<ContentAsset> assets = new ArrayList<>();
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    public ContentServiceImpl() {
        // Seed some data
        addAsset(new ContentAsset(1L, "OSGi Demo Image", "IMAGE", 1024L, 0.0, true));
    }

    @Override
    public void addAsset(ContentAsset asset) {
        // Validation
        if (asset.getFileSize() > MAX_FILE_SIZE) {
            System.err.println("Error: File size too large: " + asset.getTitle());
            return;
        }

        // Cost Calculation
        if ("VIDEO".equalsIgnoreCase(asset.getType())) {
            asset.setCost(100.0);
        } else if ("IMAGE".equalsIgnoreCase(asset.getType())) {
            asset.setCost(50.0);
        } else {
            asset.setCost(0.0);
        }

        assets.add(asset);
        System.out.println("Asset Added: " + asset.getTitle() + " (Cost: $" + asset.getCost() + ")");
    }

    @Override
    public List<ContentAsset> getAssets() {
        return new ArrayList<>(assets); // simple copy
    }
}
