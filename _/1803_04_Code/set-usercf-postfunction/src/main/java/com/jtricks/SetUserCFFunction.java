package com.jtricks;

import java.util.Map;

import com.atlassian.jira.ComponentManager;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.ModifiedValue;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutItem;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutStorageException;
import com.atlassian.jira.issue.util.DefaultIssueChangeHolder;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.util.UserUtil;
import com.atlassian.jira.workflow.function.issue.AbstractJiraFunctionProvider;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.user.User;
import com.opensymphony.workflow.WorkflowException;

public class SetUserCFFunction extends AbstractJiraFunctionProvider {

	private final CustomFieldManager customFieldManager;
	private final JiraAuthenticationContext authContext;
	private final UserUtil userUtil;

	public SetUserCFFunction(CustomFieldManager customFieldManager, JiraAuthenticationContext authContext,UserUtil userUtil) {
		this.customFieldManager = customFieldManager;
		this.authContext = authContext;
		this.userUtil = userUtil;
	}

	public void execute(Map transientVars, Map args, PropertySet ps) throws WorkflowException {
		MutableIssue issue = getIssue(transientVars);

		User user = null;
		
		System.out.println("User from args:"+args.get("user"));

		if (args.get("user") != null) {
			String userName = (String) args.get("user");
			if (userName.equals("Current User")){
				// Set the current user here!
				user = authContext.getUser();
			} else {
				user = userUtil.getUser(userName);
			}
		} else {
			// Set the current user here!
			user = authContext.getUser();
		}
		
		System.out.println("user Set:"+user);

		// Now set the user value to the custom field
		CustomField userField = customFieldManager.getCustomFieldObjectByName("Test User");
		if (userField != null) {
			try {
				setUserValue(issue, user, userField);
			} catch (FieldLayoutStorageException e) {
				System.out.println("Error while setting the user Field");
			}
		}
		
		System.out.println("Done!");
	}

	private void setUserValue(MutableIssue issue, User user, CustomField userField) throws FieldLayoutStorageException {
		issue.setCustomFieldValue(userField, user);

		Map modifiedFields = issue.getModifiedFields();
		FieldLayoutItem fieldLayoutItem = ComponentManager.getInstance().getFieldLayoutManager().getFieldLayout(issue).getFieldLayoutItem(
				userField);
		DefaultIssueChangeHolder issueChangeHolder = new DefaultIssueChangeHolder();
		final ModifiedValue modifiedValue = (ModifiedValue) modifiedFields.get(userField.getId());

		userField.updateValue(fieldLayoutItem, issue, modifiedValue, issueChangeHolder);
	}

}
