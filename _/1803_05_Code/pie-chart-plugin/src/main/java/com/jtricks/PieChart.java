package com.jtricks;

import static com.atlassian.jira.charts.ChartFactory.REPORT_IMAGE_HEIGHT;
import static com.atlassian.jira.charts.ChartFactory.REPORT_IMAGE_WIDTH;

import java.util.HashMap;
import java.util.Map;

import com.atlassian.jira.charts.Chart;
import com.atlassian.jira.plugin.report.impl.AbstractReport;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.web.action.ProjectActionSupport;

public class PieChart extends AbstractReport {
	
	private final JiraAuthenticationContext authenticationContext;
	
	public PieChart(JiraAuthenticationContext authenticationContext) {
		this.authenticationContext = authenticationContext;
	}


	public String generateReportHtml(ProjectActionSupport action, Map reqParams) throws Exception {
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("report", this);
		params.put("action", action);
		params.put("user", authenticationContext.getUser());
		
		final Chart chart = new JTricksPieChartGenerator().generateChart(authenticationContext.getUser(),
				REPORT_IMAGE_WIDTH, REPORT_IMAGE_HEIGHT);
		params.putAll(chart.getParameters());

		return descriptor.getHtml("view", params);
	}

}
