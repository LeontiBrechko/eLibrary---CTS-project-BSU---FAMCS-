package utils;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Leonti on 2016-03-04.
 */
public class DBUtil {
    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("eLibrary");

    public static EntityManagerFactory getEMFactory() {
        return emf;
    }
}
