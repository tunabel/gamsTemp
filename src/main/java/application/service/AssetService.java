package application.service;

import application.model.entity.Asset;
import application.model.requestdto.AssetCreateRequestDto;
import application.model.responsedto.AssetGetAllResponseDtoDto;
import application.model.responsedto.AssetGetOneResponseDtoDto;
import application.model.responsedto.AssociatedAssetGetResponseDto;

import java.util.List;

public interface AssetService {
    List<AssetGetAllResponseDtoDto> findAll();

    Asset insert(AssetCreateRequestDto request);

    AssetGetOneResponseDtoDto findById(String id);

    List<AssociatedAssetGetResponseDto> findAssocByName(String name);

}
