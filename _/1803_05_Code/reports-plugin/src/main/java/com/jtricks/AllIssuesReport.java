package com.jtricks;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.atlassian.jira.ComponentManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.search.SearchException;
import com.atlassian.jira.issue.search.SearchResults;
import com.atlassian.jira.jql.builder.JqlQueryBuilder;
import com.atlassian.jira.plugin.report.impl.AbstractReport;
import com.atlassian.jira.web.action.ProjectActionSupport;
import com.atlassian.jira.web.bean.PagerFilter;
import com.atlassian.query.Query;
import com.opensymphony.user.User;

public class AllIssuesReport extends AbstractReport {

	@Override
	public void validate(ProjectActionSupport action, Map reqParams) {
		// Do your validation here if you have any!
		final String projectid = (String) reqParams.get("projectId");
		final Long pid = new Long(projectid);
		if (ComponentManager.getInstance().getProjectManager().getProjectObj(pid) == null) {
			action.addError("projectId", "Invalid project Selected");
		}

		super.validate(action, reqParams);
	}

	public String generateReportHtml(ProjectActionSupport action, Map reqParams) throws Exception {
		//printRequestValues(reqParams); // Examples of Object Configurable Properties
		final Map<String, Object> velocityParams = getVelocityParams(action, reqParams);
		return descriptor.getHtml("view", velocityParams);
	}
	
	private void printRequestValues(Map reqParams) {
		// TODO Auto-generated method stub
		final String testString = (String) reqParams.get("testString");
		final String testLong = (String) reqParams.get("testLong");
		final String testHidden = (String) reqParams.get("testHidden");
		final String testDate = (String) reqParams.get("testDate");
		final String testUser = (String) reqParams.get("testUser");
		final String testText = (String) reqParams.get("testText");
		final String[] testMultiSelect = (String[]) reqParams.get("testMultiSelect");
		final String testCheckBox = (String) reqParams.get("testCheckBox");
		final String testFilterPicker = (String) reqParams.get("testFilterPicker");
		final String testFilterProjectPicker = (String) reqParams.get("testFilterProjectPicker");
		final String testSelect = (String) reqParams.get("testSelect");
		final String testCascadingSelect = (String) reqParams.get("testCascadingSelect");
		
		System.out.println("Object Configurable Properties Demo");
		System.out.println("***********************************");
		System.out.println("Test String:"+testString);
		System.out.println("Test Long:"+testLong);
		System.out.println("Test Hidden:"+testHidden);
		System.out.println("Test Date:"+testDate);
		System.out.println("Test User:"+testUser);
		System.out.println("Test Text:"+testText);
		System.out.println("Test Multi Select:"+Arrays.asList(testMultiSelect));
		System.out.println("Test Checkbox:"+testCheckBox);
		System.out.println("Test Filter Picker:"+testFilterPicker);
		System.out.println("Test Filter Project Picker:"+testFilterProjectPicker);
		System.out.println("Test Select:"+testSelect);
		System.out.println("Test Cascading Select:"+testCascadingSelect);
		System.out.println("***********************************");
	}

	@Override
	public String generateReportExcel(ProjectActionSupport action, Map reqParams) throws Exception {
		final Map<String, Object> velocityParams = getVelocityParams(action, reqParams);
		return descriptor.getHtml("excel", velocityParams);
	}
	
	@Override
	public boolean isExcelViewSupported() {
		return true;
	}

	private Map<String, Object> getVelocityParams(ProjectActionSupport action, Map reqParams) throws SearchException {
		final String projectid = (String) reqParams.get("projectId");
		final Long pid = new Long(projectid);

		final Map<String, Object> velocityParams = new HashMap<String, Object>();
		velocityParams.put("report", this);
		velocityParams.put("action", action);
		velocityParams.put("issues", getIssuesFromProject(pid));
		return velocityParams;
	}

	List<Issue> getIssuesFromProject(Long pid) throws SearchException {
		JqlQueryBuilder builder = JqlQueryBuilder.newBuilder();
		builder.where().project(pid);
		Query query = builder.buildQuery();

		SearchResults results = ComponentManager
				.getInstance()
				.getSearchService()
				.search(ComponentManager.getInstance().getJiraAuthenticationContext().getUser(), query,
						PagerFilter.getUnlimitedFilter());
		return results.getIssues();
	}
	
	@Override
	public boolean showReport() {
		User user = ComponentManager.getInstance().getJiraAuthenticationContext().getUser();
		return ComponentManager.getInstance().getUserUtil().getAdministrators().contains(user);
	}
}
