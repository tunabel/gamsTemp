package application.controller;

import application.model.requestdto.AssetCreateRequestDto;
import application.model.responsedto.AssetGetAllResponseDto;
import application.model.responsedto.AssetGetOneResponseDto;
import application.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/assets/")
public class AssetController {

    @Autowired
    AssetService assetService;

    //Check access asset list per user right.
    //get role from token > if EMP: only get asset with same owner / pic id. DUL : asset with owner id in the same department.
    // GRL: asset with owner id in specific departments. Group 1 = DU 1 & 11, Group 2 = ...
    @GetMapping(value = "/")
    public ResponseEntity<Map<String, Object>> getAll() {
        List<AssetGetAllResponseDto> responseList = assetService.findAll();

        Map<String, Object> response = new HashMap<>();
        response.put("assets", responseList);
        response.put("totalNumberOfRecords", responseList.size());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/")
    @PreAuthorize("hasRole('ADM') or hasRole('SAD')")
    public ResponseEntity<Map<String, Object>> insertOne(@Valid @RequestBody AssetCreateRequestDto dto) {
        AssetGetOneResponseDto asset = assetService.insert(dto, true);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "New asset created.");
        response.put("assetId", asset.getId());
        response.put("asset", asset);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping(value = "/import/")
    @PreAuthorize("hasRole('ADM') or hasRole('SAD')")
    public ResponseEntity<Map<String, Object>> insertList(@Valid @RequestBody List<AssetCreateRequestDto> request) {

        List<AssetGetOneResponseDto> assetList = new ArrayList<>();

        for (AssetCreateRequestDto asset : request) {
            AssetGetOneResponseDto newAsset = assetService.insert(asset, true);
            assetList.add(newAsset);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("message", "New asset list inserted.");
        response.put("count", assetList.size());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping(value = "/asset/{id}")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable String id) {
        AssetGetOneResponseDto asset = assetService.findById(id);

        Map<String, Object> response = new HashMap<>();
        response.put("asset", asset);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/search/")
    public ResponseEntity<Map<String, Object>> searchByFields(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String group,
            @RequestParam(required = false) String owner
            ) {
        List<AssetGetAllResponseDto> assetList = assetService.findListByFields(code,name,group,owner);

        Map<String, Object> response = new HashMap<>();
        response.put("assets", assetList);
        response.put("totalNumberOfRecords", assetList.size());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/asset/assoc/")
    public ResponseEntity<Map<String, Object>> getAssociable(@RequestParam String search) {

        Map<String, Object> response = new HashMap<>();
        response.put("results", assetService.findAssocByName(search));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping(value = "/asset/")
    public ResponseEntity<Map<String, Object>> update(@Valid @RequestBody AssetCreateRequestDto request) {

        AssetGetOneResponseDto asset = assetService.insert(request,false);

        Map<String, Object> response = new HashMap<>();
        response.put("results", asset);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
