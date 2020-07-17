package application.service;

import application.model.entity.Asset;
import application.model.request.AssetAddRequest;
import application.model.response.AssetGetResponse;

import java.util.List;

public interface AssetService {
    List<AssetGetResponse> findAll();

    Asset insert(AssetAddRequest request);

    AssetGetResponse findById(String id);

}
