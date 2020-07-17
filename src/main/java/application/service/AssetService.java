package application.service;

import application.model.entity.Asset;
import application.model.request.AssetAddRequest;

import java.util.List;

public interface AssetService {
    List<Asset> findAll();

    Asset insert(AssetAddRequest request);

}
