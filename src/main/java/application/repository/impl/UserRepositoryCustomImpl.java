package application.repository.impl;

import application.model.common.UserField;
import application.model.entity.User;
import application.repository.UserRepositoryCustom;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    private List<User> exportUserListFromCriteria(Criteria criteria) {
        Query query = new Query(criteria);
        return mongoTemplate.find(query, User.class);
    }

    @Override
    public List<User> findAllActive() {
        Criteria criteria = Criteria.where(UserField.ACTIVE.getName()).is(true);
        return mongoTemplate.find(Query.query(criteria), User.class);
    }

    @Override
    public List<User> findActiveByQueryWithPagination(String input, Pageable pageable) {

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
        return exportUserListFromCriteria(criteria);
    }

    @Override
    public List<User> findActiveByQueryingAllTextFields(String input) {

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
        return exportUserListFromCriteria(criteria);
    }

    @Override
    public long countActiveByField(String field, String value) {
        Criteria criteria = new Criteria();

        criteria.andOperator(
                Criteria.where(field).regex(value, "i"),
                Criteria.where(UserField.ACTIVE.getName()).is(true)
        );
        Query query = new Query().addCriteria(criteria);
        return mongoTemplate.count(query, User.class);
    }

    //find by Email will check for duplication among deactivated users, so I removed the check for active users.
    @Override
    public List<User> findByEmail(String email) {
        Criteria criteria = new Criteria();

        criteria.andOperator(
                Criteria.where(UserField.EMAIL.getName()).is(email)
        );
        return exportUserListFromCriteria(criteria);
    }

    @Override
    public List<User> findActiveByFieldWithFixedValue(String field, String value) {
        Criteria criteria = new Criteria();

        criteria.andOperator(
                Criteria.where(field).is(value),
                Criteria.where(UserField.ACTIVE.getName()).is(true)
        );
        return exportUserListFromCriteria(criteria);
    }

    @Override
    public List<User> findByBothName(String search) {
        Criteria criteria = new Criteria().andOperator(
                new Criteria().orOperator(
                        Criteria.where(UserField.FIRSTNAME.getName()).regex(search, "i"),
                        Criteria.where(UserField.SURNAME.getName()).regex(search, "i")
                ),
                Criteria.where(UserField.ACTIVE.getName()).is(true)
        );
        return exportUserListFromCriteria(criteria);
    }

    @Override
    public boolean isConnectionOK() {
        MongoDatabase db = mongoTemplate.getDb();
        return !db.getName().isEmpty();
    }
}
