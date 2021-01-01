package com.jtricks;

import java.util.Collection;
import java.util.Vector;

import org.ofbiz.core.entity.GenericValue;

import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.rpc.RpcUtils;
import com.atlassian.jira.rpc.auth.TokenManager;
import com.atlassian.jira.rpc.exception.RemoteAuthenticationException;
import com.atlassian.jira.rpc.exception.RemotePermissionException;
import com.opensymphony.user.User;

public class XmlRpcServiceImpl implements XmlRpcService {

	private final TokenManager tokenManager;
	private final ProjectManager projectManager;

	public XmlRpcServiceImpl(TokenManager tokenManager, ProjectManager projectManager) {
		this.tokenManager = tokenManager;
		this.projectManager = projectManager;
	}

	public String login(String username, String password) throws Exception {
		try {
			return tokenManager.login(username, password);
		} catch (RemoteAuthenticationException e) {
			throw new RuntimeException("Error Authenticating!," + e.toString());
		} catch (com.atlassian.jira.rpc.exception.RemoteException e) {
			throw new RuntimeException("Couldn't login," + e.toString());
		}
	}

	public Vector getprojectCategories(String token) {
		validateToken(token);

		Collection<GenericValue> categories = projectManager.getProjectCategories();
		RemoteCategory[] remoteCategories = new RemoteCategory[categories.size()];

		int i = 0;
		for (GenericValue category : categories) {
			remoteCategories[i++] = new RemoteCategory(category);
		}
		return RpcUtils.makeVector(remoteCategories);
	}

	private void validateToken(String token) {
		try {
			User user = tokenManager.retrieveUser(token);
		} catch (RemoteAuthenticationException e) {
			throw new RuntimeException("Error Authenticating!," + e.toString());
		} catch (RemotePermissionException e) {
			throw new RuntimeException("User does not have permission for this operation," + e.toString());
		}
	}

}
