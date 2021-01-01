package com.jtricks;

import java.util.Map;

import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.fields.CustomField;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.InvalidInputException;
import com.opensymphony.workflow.Validator;
import com.opensymphony.workflow.WorkflowException;

public class FieldValidator implements Validator{
	
	private final CustomFieldManager customFieldManager;
	
	private static final String FIELD_NAME = "field";	

	public FieldValidator(CustomFieldManager customFieldManager) {
		this.customFieldManager = customFieldManager;
	}

	public void validate(Map transientVars, Map args, PropertySet ps) throws InvalidInputException, WorkflowException {
		Issue issue = (Issue) transientVars.get("issue");
		String field = (String) args.get(FIELD_NAME);	
		
		CustomField customField = customFieldManager.getCustomFieldObjectByName(field);
		
		if (customField!=null){
			//Check if the custom field value is NULL
			if (issue.getCustomFieldValue(customField) == null){
				throw new InvalidInputException("The field:"+field+" is required!");
			}
		}
	}

}
