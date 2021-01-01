package com.jtricks;

import java.rmi.RemoteException;

public interface JTricksSoapService {

	String login(String username, String password);

	// Method to return Project Categories
	RemoteCategory[] getProjectCategories(String token) throws RemoteException;

}
