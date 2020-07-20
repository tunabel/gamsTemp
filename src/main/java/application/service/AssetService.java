package application.service;

import application.model.entity.Asset;
import application.model.requestdto.AssetCreateRequestDto;
import application.model.responsedto.AssetGetAllResponseDto;
import application.model.responsedto.AssetGetOneResponseDto;
import application.model.responsedto.AssociatedAssetGetResponseDto;

import java.util.List;

public interface AssetService {
    List<AssetGetAllResponseDto> findAll();

    Asset insert(AssetCreateRequestDto request);

    AssetGetOneResponseDto findById(String id);

    List<AssociatedAssetGetResponseDto> findAssocByName(String name);

}
