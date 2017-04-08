package eLibrary;

import java.util.List;

import javax.ejb.Local;

import entities.Account;

@Local
public interface AccountFacadeLocal {
	public List<Account> getAllAccounts();
}
