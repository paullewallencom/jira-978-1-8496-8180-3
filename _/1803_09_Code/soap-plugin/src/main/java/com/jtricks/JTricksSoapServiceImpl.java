package com.jtricks;

import java.rmi.RemoteException;
import java.util.Collection;

import org.ofbiz.core.entity.GenericValue;

import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.rpc.auth.TokenManager;
import com.atlassian.jira.rpc.exception.RemoteAuthenticationException;
import com.atlassian.jira.rpc.exception.RemotePermissionException;
import com.opensymphony.user.User;

public class JTricksSoapServiceImpl implements JTricksSoapService {
	
	private final ProjectManager projectManager;
	private final TokenManager tokenManager;

	public JTricksSoapServiceImpl(ProjectManager projectManager, TokenManager tokenManager) {
		this.projectManager = projectManager;
		this.tokenManager = tokenManager;
	}

	public RemoteCategory[] getProjectCategories(String token) throws RemoteException {
		validateToken(token);

		Collection<GenericValue> categories = projectManager.getProjectCategories();
		RemoteCategory[] remoteCategories = new RemoteCategory[categories.size()];

		int i = 0;
		for (GenericValue category : categories) {
			remoteCategories[i++] = new RemoteCategory(category);
		}
		return remoteCategories;
	}

	private void validateToken(String token) {
		try {
			User user = tokenManager.retrieveUser(token);
		} catch (RemoteAuthenticationException e) {
			throw new RuntimeException("Error Authenticating!,"+e.toString());
		} catch (RemotePermissionException e) {
			throw new RuntimeException("User does not have permission for this operation,"+e.toString());
		}
	}

	public String login(String username, String password) {
		try {
			return tokenManager.login(username, password);
		} catch (RemoteAuthenticationException e) {
			throw new RuntimeException("Error Authenticating!,"+e.toString());
		} catch (com.atlassian.jira.rpc.exception.RemoteException e) {
			throw new RuntimeException("Couldn't login,"+e.toString());
		}
	}

}
