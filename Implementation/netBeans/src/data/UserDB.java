package data;

import models.User;
import utils.DBUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 * Created by Leonti on 2016-03-05.
 */
public class UserDB {
    public static void insertUser(User user) {
        EntityManager manager = DBUtil.getEMFactory().createEntityManager();
        EntityTransaction transaction = manager.getTransaction();
        try {
            transaction.begin();
            manager.persist(user);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            manager.close();
        }
    }
}
