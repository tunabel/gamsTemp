package application.repository.impl;

import application.model.common.UserField;
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
        Criteria criteria = Criteria.where(UserField.ACTIVE.getName()).is(true);
        return mongoTemplate.find(Query.query(criteria), User.class);
    }

    @Override
    public Page<User> findAllActive(Pageable pageable) {
        Criteria criteria = Criteria.where(UserField.ACTIVE.getName()).is(true);
        Query query = new Query(criteria).with(pageable);
        List<User> userList = mongoTemplate.find(query, User.class);
        return new PageImpl<User>(userList, pageable, findAllActive().size());
    }

    @Override
    public Page<User> findByQuery(String input, Pageable pageable) {

        Criteria criteria = new Criteria();

        criteria.andOperator(
                new Criteria().orOperator(
                        Criteria.where(UserField.FIRSTNAME.getName()).regex(input, "i"),
                        Criteria.where(UserField.SURNAME.getName()).regex(input, "i"),
                        Criteria.where(UserField.EMAIL.getName()).regex(input, "i"),
                        Criteria.where(UserField.BIRTHPLACE.getName()).regex(input, "i"),
                        Criteria.where(UserField.DEPARTMENT.getName()).regex(input, "i")
                ),
                Criteria.where(UserField.ACTIVE.getName()).is(true)
        );
        Query query = new Query(criteria).with(pageable);
        List<User> userList = mongoTemplate.find(query, User.class);

        return new PageImpl<User>(userList, pageable, userList.size());
    }

    @Override
    public List<User> findByQuery(String input) {

        Criteria criteria = new Criteria().andOperator(
                new Criteria().orOperator(
                        Criteria.where(UserField.FIRSTNAME.getName()).regex(input, "i"),
                        Criteria.where(UserField.SURNAME.getName()).regex(input, "i"),
                        Criteria.where(UserField.EMAIL.getName()).regex(input, "i"),
                        Criteria.where(UserField.BIRTHPLACE.getName()).regex(input, "i"),
                        Criteria.where(UserField.DEPARTMENT.getName()).regex(input, "i")
                ),
                Criteria.where(UserField.ACTIVE.getName()).is(true)
        );
        Query query = new Query(criteria);
        return mongoTemplate.find(query, User.class);
    }

    @Override
    public long countByField(String field, String value) {
        Criteria criteria = new Criteria();

        criteria.andOperator(
                Criteria.where(field).regex(value, "i"),
                Criteria.where(UserField.ACTIVE.getName()).is(true)
        );
        Query query = new Query().addCriteria(criteria);
        return mongoTemplate.count(query, User.class);
    }

    @Override
    public List<User> findByEmail(String email) {
        Criteria criteria = new Criteria();

        criteria.andOperator(
                Criteria.where(UserField.EMAIL.getName()).is(email),
                Criteria.where(UserField.ACTIVE.getName()).is(true)
        );
        Query query = new Query().addCriteria(criteria);
        return mongoTemplate.find(query, User.class);
    }

    @Override
    public List<User> findByFieldWithFixedValue(String field, String value) {
        Criteria criteria = new Criteria();

        criteria.andOperator(
                Criteria.where(field).is(value),
                Criteria.where(UserField.ACTIVE.getName()).is(true)
        );
        Query query = new Query().addCriteria(criteria);
        return mongoTemplate.find(query, User.class);
    }
}
