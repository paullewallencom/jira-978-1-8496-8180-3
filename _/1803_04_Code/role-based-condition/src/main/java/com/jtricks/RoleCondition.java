package com.jtricks;

import java.util.Map;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.security.roles.ProjectRoleManager;
import com.atlassian.jira.workflow.condition.AbstractJiraCondition;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.user.User;
import com.opensymphony.workflow.WorkflowException;

public class RoleCondition extends AbstractJiraCondition{
	
	private static final String ROLE = "role";
	
	private final ProjectRoleManager projectRoleManager;	

	public RoleCondition(ProjectRoleManager projectRoleManager) {
		this.projectRoleManager = projectRoleManager;
	}

	public boolean passesCondition(Map transientVars, Map args, PropertySet ps) throws WorkflowException {
		
		Issue issue = getIssue(transientVars);
		User user = getCaller(transientVars, args);
		
		Project project = issue.getProjectObject();
		
		String role = (String)args.get(ROLE);
		Long roleId = new Long(role);
		
		return projectRoleManager.isUserInProjectRole(user, projectRoleManager.getProjectRole(roleId), project);
	}

}
