package application.service;

import application.model.requestdto.AssetCreateRequestDto;
import application.model.responsedto.AssetGetAllResponseDto;
import application.model.responsedto.AssetGetOneResponseDto;
import application.model.responsedto.AssetShortResponseDto;

import java.util.List;

public interface AssetService {
    List<AssetGetAllResponseDto> findAll();

    AssetGetOneResponseDto insert(AssetCreateRequestDto request, boolean isAnInsert);

    AssetGetOneResponseDto findById(String id);

    List<AssetGetAllResponseDto> findListByFields(String code, String name, String group, String owner);

    List<AssetShortResponseDto> findAssocByName(String name);

}
