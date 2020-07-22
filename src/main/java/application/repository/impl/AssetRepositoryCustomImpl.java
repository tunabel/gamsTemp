package application.repository.impl;

import application.model.entity.Asset;
import application.model.entity.AssetWithName;
import application.repository.AssetRepositoryCustom;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AssetRepositoryCustomImpl implements AssetRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    private List<AggregationOperation> createBaseAssetAggregationList() {
        List<AggregationOperation> aggregationList = new ArrayList<>();

        ProjectionOperation projectionPic = Aggregation.project(
                "assetCode",
                "assetTypeId",
                "assetGroupId",
                "name",
                "unit",
                "pic",
                "owner",
                "note",
                "officeSiteId",
                "manufacturerId",
                "supplierId",
                "price",
                "purchaseDate",
                "warrantyInMonth",
                "assetStatusId",
                "ciaC",
                "ciaI",
                "ciaA",
                "ciaSum",
                "ciaImportance",
                "ciaNote",
                "assignDateStart",
                "assignDateEnd",
                "overdue");
        LookupOperation lookupAssoc = Aggregation.lookup("assets", "associatedAsset", "assetCode", "associatedAsset");
        LookupOperation lookupType = Aggregation.lookup("assettypes", "assetTypeId", "_id", "assetType");
        LookupOperation lookupGroup = Aggregation.lookup("assetgroups", "assetGroupId", "_id", "assetGroup");
        LookupOperation lookupStatus = Aggregation.lookup("assetstatus", "assetStatusId", "_id", "assetStatus");
        LookupOperation lookupOffice = Aggregation.lookup("officesites", "officeSiteId", "_id", "officeSite");
        LookupOperation lookupManufacturer = Aggregation.lookup("manufacturers", "manufacturerId", "_id", "manufacturer");
        LookupOperation lookupSupplier = Aggregation.lookup("suppliers", "supplierId", "_id", "supplier");
        ProjectionOperation projectionFirstIndex = Aggregation.project(
                "assetCode",
                "name",
                "unit",
                "note",
                "associatedAsset",
                "price",
                "purchaseDate",
                "warrantyInMonth",
                "ciaC",
                "ciaI",
                "ciaA",
                "ciaSum",
                "ciaImportance",
                "ciaNote",
                "assignDateStart",
                "assignDateEnd",
                "pic",
                "owner",
                "overdue")
//                .andArrayOf(new Document("$arrayElemAt", Arrays.asList("$picUser", 0))).as("pic")
//                .andArrayOf(new Document("$arrayElemAt", Arrays.asList("$ownerUser", 0))).as("owner")
                .andArrayOf(new Document("$arrayElemAt", Arrays.asList("$assetType", 0))).as("assetType")
                .andArrayOf(new Document("$arrayElemAt", Arrays.asList("$assetGroup", 0))).as("assetGroup")
                .andArrayOf(new Document("$arrayElemAt", Arrays.asList("$assetStatus", 0))).as("assetStatus")
                .andArrayOf(new Document("$arrayElemAt", Arrays.asList("$manufacturer", 0))).as("manufacturer")
                .andArrayOf(new Document("$arrayElemAt", Arrays.asList("$supplier", 0))).as("supplier")
                .andArrayOf(new Document("$arrayElemAt", Arrays.asList("$officeSite", 0))).as("officeSite");

        aggregationList.addAll(
                Arrays.asList(
                        projectionPic
                        , lookupAssoc, lookupType, lookupGroup, lookupStatus, lookupOffice, lookupManufacturer, lookupSupplier
                        , projectionFirstIndex
                )
        );

        return aggregationList;
    }

    @Override
    public List<Asset> getSimpleListOfIdByAssetGroupIdOrAll(String assetGroupId) {
        List<AggregationOperation> aggregationList = new ArrayList<>();
        if (assetGroupId != null) {
            Criteria criteria = Criteria.where("assetGroupId").is(assetGroupId);
            MatchOperation match1 = Aggregation.match(criteria);
            aggregationList.add(match1);
        }

        ProjectionOperation projection1 = Aggregation.project("_id");
        aggregationList.add(projection1);
        Aggregation aggregation = Aggregation.newAggregation(aggregationList);
        AggregationResults<Asset> results = mongoTemplate.aggregate(aggregation, "assets", Asset.class);
        return results.getMappedResults();
    }

    @Override
    public List<AssetWithName> findAllWithName() {
        List<AggregationOperation> aggregationOperationList = createAssetGetAllAggregationList();

        Aggregation aggregation = Aggregation.newAggregation(aggregationOperationList);
        AggregationResults<AssetWithName> results = mongoTemplate.aggregate(aggregation, "assets", AssetWithName.class);

        return results.getMappedResults();
    }

    @Override
    public Asset findByIdCustom(String id) {

        List<AggregationOperation> aggregationOperationList = createBaseAssetAggregationList();

        MatchOperation matchId = Aggregation.match(Criteria.where("_id").is(id));
        ProjectionOperation projectionGetId = Aggregation.project(
                "assetCode",
                "name",
                "unit",
                "note",
                "associatedAsset",
                "price",
                "purchaseDate",
                "warrantyInMonth",
                "ciaC",
                "ciaI",
                "ciaA",
                "ciaSum",
                "ciaImportance",
                "ciaNote",
                "assignDateStart",
                "assignDateEnd",
                "pic",
                "owner",
                "overdue")
                .and("$assetType.value").as("assetTypeId")
                .and("$assetGroup.value").as("assetGroupId")
                .and("$assetStatus.value").as("assetStatusId")
                .and("$officeSite.value").as("officeSiteId")
                .and("$supplier.value").as("supplierId")
                .and("$manufacturer.value").as("manufacturerId");

        aggregationOperationList.add(0, matchId);
        aggregationOperationList.add(projectionGetId);
        Aggregation aggregation = Aggregation.newAggregation(aggregationOperationList);
        AggregationResults<Asset> results = mongoTemplate.aggregate(aggregation, "assets", Asset.class);

        Asset asset = results.getUniqueMappedResult();

        return asset;
    }

    @Override
    public List<Asset> findAssocByName(String req) {

        Criteria criteria = new Criteria().andOperator(
                Criteria.where("assetGroupId").is("28"),
                Criteria.where("name").regex("^" + req, "i")
                , Criteria.where("assetStatusId").is("0")
        );

        MatchOperation matchOperation = Aggregation.match(criteria);
        ProjectionOperation projectionOperation = Aggregation.project(
                "name", "assetCode"
        );

        AggregationResults<Asset> results = mongoTemplate.aggregate(Aggregation.newAggregation(matchOperation, projectionOperation), "assets", Asset.class);

        return results.getMappedResults();
    }

    @Override
    public List<AssetWithName> findListByFields(String code, String name, String group, String owner) {

        List<AggregationOperation> aggregationOperationList = createAssetGetAllAggregationList();
        Criteria criteria;
        //I WAS HAVING AN ERROR because owner field was originally null and after aggregation's projections it comes out as some datatype I can't catch to transform into empty string.
        if (owner == null) {
            criteria = new Criteria().andOperator(
                    Criteria.where("assetCode").regex((code == null) ? "" : code, "i"),
                    Criteria.where("name").regex((name == null) ? "" : name, "i"),
                    Criteria.where("assetGroup").regex((group == null) ? "" : group, "i")
            );
        } else {
            criteria = new Criteria().andOperator(
                    Criteria.where("assetCode").regex((code == null) ? "" : code, "i"),
                    Criteria.where("name").regex((name == null) ? "" : name, "i"),
                    Criteria.where("assetGroup").regex((group == null) ? "" : group, "i"),
                    Criteria.where("owner").regex(owner, "i")
            );
        }

        MatchOperation matchQuery = Aggregation.match(criteria);

        aggregationOperationList.add(matchQuery);

        Aggregation aggregation = Aggregation.newAggregation(aggregationOperationList);
        AggregationResults<AssetWithName> results = mongoTemplate.aggregate(aggregation, "assets", AssetWithName.class);

        return results.getMappedResults();
    }


    private List<AggregationOperation> createAssetGetAllAggregationList() {
        List<AggregationOperation> aggregationOperationList = createBaseAssetAggregationList();

        ProjectionOperation projectionOperation = Aggregation.project(
                "assetCode",
                "name",
                "ciaC",
                "ciaI",
                "ciaA",
                "ciaSum",
                "overdue")
                .andArrayOf(new Document("$concat", Arrays.asList("$owner.firstName", " ", "$owner.surName", " - ", "$owner.department"))).as("owner")
                .and("$assetType.name").as("assetType")
                .and("$assetGroup.name").as("assetGroup")
                .and("$assetStatus.name").as("assetStatus")
                .and("$officeSite.name").as("officeSite");

        aggregationOperationList.add(projectionOperation);

        return aggregationOperationList;

    }

}
