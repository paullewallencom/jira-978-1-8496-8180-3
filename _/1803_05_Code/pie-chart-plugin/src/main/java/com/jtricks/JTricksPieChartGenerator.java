package com.jtricks;

import java.util.HashMap;
import java.util.Map;

import org.jfree.data.general.DefaultPieDataset;

import com.atlassian.jira.charts.Chart;
import com.atlassian.jira.charts.jfreechart.ChartHelper;
import com.atlassian.jira.charts.jfreechart.PieChartGenerator;
import com.atlassian.jira.web.bean.I18nBean;
import com.opensymphony.user.User;

public class JTricksPieChartGenerator {

	public Chart generateChart(User remoteUser, int width, int height) {

		try {
			final Map<String, Object> params = new HashMap<String, Object>();

			// Create Dataset
			DefaultPieDataset dataset = new DefaultPieDataset();

			dataset.setValue("One", 10L);
			dataset.setValue("Two", 15L);

			final I18nBean i18nBean = new I18nBean(remoteUser);

			final ChartHelper helper = new PieChartGenerator(dataset, i18nBean).generateChart();
			helper.generate(width, height);

			params.put("chart", helper.getLocation());
			params.put("chartDataset", dataset);
			params.put("imagemap", helper.getImageMap());
			params.put("imagemapName", helper.getImageMapName());
			params.put("width", width);
			params.put("height", height);

			return new Chart(helper.getLocation(), helper.getImageMap(), helper.getImageMapName(), params);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error generating chart", e);
		}

	}

}
