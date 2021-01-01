package com.jtricks;

import static com.atlassian.jira.util.dbc.Assertions.notNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.atlassian.jira.JiraDataType;
import com.atlassian.jira.JiraDataTypes;
import com.atlassian.jira.jql.operand.QueryLiteral;
import com.atlassian.jira.jql.query.QueryCreationContext;
import com.atlassian.jira.plugin.jql.function.AbstractJqlFunction;
import com.atlassian.jira.plugin.jql.function.ClauseSanitisingJqlFunction;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.security.Permissions;
import com.atlassian.jira.util.MessageSet;
import com.atlassian.jira.util.MessageSetImpl;
import com.atlassian.query.clause.TerminalClause;
import com.atlassian.query.operand.FunctionOperand;
import com.opensymphony.user.User;

public class ProjectsFunction extends AbstractJqlFunction implements ClauseSanitisingJqlFunction {

	private final ProjectManager projectManager;
	private final PermissionManager permissionManager;

	public ProjectsFunction(ProjectManager projectManager, PermissionManager permissionManager) {
		this.projectManager = projectManager;
		this.permissionManager = permissionManager;
	}

	public JiraDataType getDataType() {
		return JiraDataTypes.PROJECT;
	}

	public int getMinimumNumberOfExpectedArguments() {
		return 1;
	}

	public List<QueryLiteral> getValues(QueryCreationContext context, FunctionOperand operand,
			TerminalClause terminalClause) {
		notNull("queryCreationContext", context);
		List<QueryLiteral> literals = new LinkedList<QueryLiteral>();
		List<String> projectKeys = operand.getArgs();
		for (String projectKey : projectKeys) {
			Project project = projectManager.getProjectObjByKey(projectKey);
			if (project != null) {
				literals.add(new QueryLiteral(operand, project.getId()));
			}
		}
		return literals;
	}

	public MessageSet validate(User searcher, FunctionOperand operand, TerminalClause terminalClause) {
		List<String> projectKeys = operand.getArgs();
		MessageSet messages = new MessageSetImpl();
		if (projectKeys.isEmpty()) {
			messages.addErrorMessage("Atleast one project key needed");
		} else {
			for (String projectKey : projectKeys) {
				if (projectManager.getProjectObjByKey(projectKey) == null) {
					messages.addErrorMessage("Invalid Project Key:" + projectKey);
				}
			}
		}
		return messages;
	}

	public FunctionOperand sanitiseOperand(User user, FunctionOperand functionOperand) {
		final List<String> pKeys = functionOperand.getArgs();

		boolean argChanged = false;
		final List<String> newArgs = new ArrayList<String>(pKeys.size());

		for (final String pKey : pKeys) {
			Project project = projectManager.getProjectObjByKey(pKey);
			if (project != null && !permissionManager.hasPermission(Permissions.BROWSE, project, user)) {
				newArgs.add(project.getId().toString());
				argChanged = true;
			} else {
				newArgs.add(pKey);
			}
		}

		if (argChanged) {
			return new FunctionOperand(functionOperand.getName(), newArgs);
		} else {
			return functionOperand;
		}
	}

}
