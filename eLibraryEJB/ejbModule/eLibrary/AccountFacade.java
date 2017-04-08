package eLibrary;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import entities.Account;

/**
 * Session Bean implementation class AccountFacade
 */
@Stateless
@LocalBean
public class AccountFacade implements AccountFacadeLocal {
	@PersistenceContext(name = "eLibraryJPA")
	EntityManager em;

    /**
     * Default constructor. 
     */
    public AccountFacade() {
        // TODO Auto-generated constructor stub
    }
    
    public List<Account> getAllAccounts() {
		Query q = em.createNamedQuery("Account.findAll");
		return q.getResultList();
	}

}
