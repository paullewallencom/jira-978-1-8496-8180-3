package com.jtricks;

import java.util.Map;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.customfields.converters.StringConverter;
import com.atlassian.jira.issue.customfields.impl.TextCFType;
import com.atlassian.jira.issue.customfields.manager.GenericConfigManager;
import com.atlassian.jira.issue.customfields.persistence.CustomFieldValuePersister;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutItem;
import com.atlassian.jira.security.JiraAuthenticationContext;

public class ReadOnlyUserCF extends TextCFType{
	
	private final JiraAuthenticationContext authContext;

	public ReadOnlyUserCF(CustomFieldValuePersister customFieldValuePersister, StringConverter stringConverter,
			GenericConfigManager genericConfigManager, JiraAuthenticationContext authContext) {
		super(customFieldValuePersister, stringConverter, genericConfigManager);
		this.authContext = authContext;
	}

	@Override
	public Map getVelocityParameters(Issue issue, CustomField field, FieldLayoutItem fieldLayoutItem) {
		Map params = super.getVelocityParameters(issue, field, fieldLayoutItem);
		params.put("currentUser", authContext.getUser().getName());
		return params;
	}
	
	@Override
	public String getChangelogValue(CustomField field, Object value) {
		return "";
	}
	
	@Override
	public String getChangelogString(CustomField field, Object value) {
		String val = (String) value;
		if (val != null && val.length() > 100){
			val = val.substring(0, 100) + "....";
		}
		return val;
	}

}
