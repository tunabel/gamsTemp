package application.controller;

import application.model.entity.Asset;
import application.model.request.AssetAddRequest;
import application.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/assets/")
public class AssetController {

    @Autowired
    AssetService assetService;

    @GetMapping(value = "/")
    public ResponseEntity<Map<String, Object>> getAllAsset() {
        List<Asset> assetList = assetService.findAll();

        Map<String, Object> response = new HashMap<>();
        response.put("assets", assetList);
        response.put("totalNumberOfRecords", assetList.size());
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

}
