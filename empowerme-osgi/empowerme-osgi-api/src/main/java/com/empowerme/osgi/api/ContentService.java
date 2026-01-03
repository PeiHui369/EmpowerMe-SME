package com.empowerme.osgi.api;

import java.util.List;

public interface ContentService {
    void addAsset(ContentAsset asset);
    List<ContentAsset> getAssets();
}
