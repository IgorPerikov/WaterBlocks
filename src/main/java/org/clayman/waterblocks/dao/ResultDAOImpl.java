package org.clayman.waterblocks.dao;

import org.clayman.waterblocks.domain.Result;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ResultDAOImpl implements ResultDAO {
    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public long save(Result result) {
        return (long) sessionFactory.getCurrentSession().save(result);
    }

    @Override
    public Result getById(long id) {
        return (Result) sessionFactory.getCurrentSession().get(Result.class, id);
    }

    @Override
    public List<Result> getList() {
        return (List<Result>) sessionFactory.getCurrentSession().createCriteria(Result.class).addOrder(Order.desc("added")).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
    }
}
