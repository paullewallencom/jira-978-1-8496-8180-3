package com.jtricks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.atlassian.configurable.ValuesGenerator;
import com.atlassian.jira.ComponentManager;
import com.atlassian.jira.project.Project;

public class ProjectValuesGenerator implements ValuesGenerator{

	public Map<String, String> getValues(Map userParams) {
		Map<String, String> projectMap = new HashMap<String, String>();

		List<Project> allProjects = ComponentManager.getInstance().getProjectManager().getProjectObjects();

		for (Project project : allProjects) {

			projectMap.put(project.getId().toString(), project.getName());
		}

		return projectMap;

	}

}
