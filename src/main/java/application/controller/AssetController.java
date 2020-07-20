package application.controller;

import application.model.entity.Asset;
import application.model.request.AssetAddRequest;
import application.model.request.AssociableAssetRequest;
import application.model.response.AssetGetAllResponse;
import application.model.response.AssetGetResponse;
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
    public ResponseEntity<Map<String, Object>> getAllAsset() {
        List<AssetGetAllResponse> responseList = assetService.findAll();

        Map<String, Object> response = new HashMap<>();
        response.put("assets", responseList);
        response.put("totalNumberOfRecords", responseList.size());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/")
    @PreAuthorize("hasRole('ADM') or hasRole('SAD')")
    public ResponseEntity<Map<String, Object>> insertAsset(@Valid @RequestBody AssetAddRequest request) {
        Asset asset = assetService.insert(request);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "New asset created.");
        response.put("assetId", asset.getId());
        response.put("asset", asset);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping(value = "/import/")
    @PreAuthorize("hasRole('ADM') or hasRole('SAD')")
    public ResponseEntity<Map<String, Object>> insertAssetList(@Valid @RequestBody List<AssetAddRequest> request) {

        List<Asset> assetList = new ArrayList<>();

        for (AssetAddRequest asset : request) {
            Asset newAsset = assetService.insert(asset);
            assetList.add(newAsset);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("message", "New asset list inserted.");
        response.put("count", assetList.size());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping(value = "/asset/{id}")
    public ResponseEntity<Map<String, Object>> insertAsset(@PathVariable String id) {
        AssetGetResponse asset = assetService.findById(id);

        Map<String, Object> response = new HashMap<>();
        response.put("asset", asset);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/asset/assoc")
    public ResponseEntity<Map<String, Object>> getAssociableAsset(@RequestBody AssociableAssetRequest request) {

        Map<String, Object> response = new HashMap<>();
        response.put("results", assetService.findAssocByName(request.getName()));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
