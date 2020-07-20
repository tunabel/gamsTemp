package application.service;

import application.model.entity.Asset;
import application.model.request.AssetAddRequest;
import application.model.response.AssetGetAllResponse;
import application.model.response.AssetGetOneResponse;
import application.model.response.AssociatedAssetGetResponse;

import java.util.List;

public interface AssetService {
    List<AssetGetAllResponse> findAll();

    Asset insert(AssetAddRequest request);

    AssetGetOneResponse findById(String id);

    List<AssociatedAssetGetResponse> findAssocByName(String name);

}
