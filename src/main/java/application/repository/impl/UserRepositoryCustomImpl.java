package application.repository.impl;

import application.model.entity.User;
import application.repository.UserRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<User> findAllActive() {
        Criteria criteria = Criteria.where("active").is(true);
        return mongoTemplate.find(Query.query(criteria), User.class);
    }

    @Override
    public Page<User> findAllActive(Pageable pageable) {
        Criteria criteria = Criteria.where("active").is(true);
        Query query = new Query(criteria).with(pageable);
        List<User> userList = mongoTemplate.find(query, User.class);
        long count = findAllActive().size();
        return new PageImpl<User>(userList, pageable, count);
    }

    @Override
    public List<User> findByQuery() {
        
        return new ArrayList<User>();
    }
}
