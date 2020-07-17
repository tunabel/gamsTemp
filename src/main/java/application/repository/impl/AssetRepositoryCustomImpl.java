package application.repository.impl;

import application.model.entity.Asset;
import application.repository.AssetRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class AssetRepositoryCustomImpl implements AssetRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public int countByAssetGroup(String groupId) {

        Criteria criteria = Criteria.where("assetGroupId").is(groupId);

        return (int) mongoTemplate.count(Query.query(criteria), Asset.class);
    }
}
