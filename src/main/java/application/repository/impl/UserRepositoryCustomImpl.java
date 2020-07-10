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
        return new PageImpl<User>(userList, pageable, findAllActive().size());
    }

    @Override
    public Page<User> findByQuery(String input, Pageable pageable) {

        Criteria criteria = new Criteria();

        criteria.andOperator(
                new Criteria().orOperator(
                        Criteria.where("firstName").regex(input, "i"),
                        Criteria.where("surName").regex(input, "i"),
                        Criteria.where("email").regex(input, "i"),
                        Criteria.where("birthPlace").regex(input, "i"),
                        Criteria.where("department").regex(input, "i")
                ),
                Criteria.where("active").is(true)
        );
        Query query = new Query(criteria).with(pageable);
        List<User> userList = mongoTemplate.find(query, User.class);

        return new PageImpl<User>(userList, pageable, userList.size());
    }

    @Override
    public long countByField(String field, String value) {
        Criteria criteria = new Criteria();

        criteria.andOperator(
                Criteria.where(field).regex(value, "i"),
                Criteria.where("active").is(true)
        );
        Query query = new Query().addCriteria(criteria);
        return mongoTemplate.count(query, User.class);
    }
}
