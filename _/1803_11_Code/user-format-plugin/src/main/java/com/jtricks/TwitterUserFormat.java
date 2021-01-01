package com.jtricks;

import java.util.Map;

import com.atlassian.jira.plugin.profile.UserFormat;
import com.atlassian.jira.plugin.profile.UserFormatModuleDescriptor;
import com.atlassian.jira.util.collect.MapBuilder;

public class TwitterUserFormat implements UserFormat {

	private final UserFormatModuleDescriptor moduleDescriptor;

	public TwitterUserFormat(UserFormatModuleDescriptor moduleDescriptor) {
		this.moduleDescriptor = moduleDescriptor;
	}

	public String format(String username, String id) {
		final Map<String, Object> params = getInitialParams(username, id);
		return moduleDescriptor.getHtml(VIEW_TEMPLATE, params);
	}

	public String format(String username, String id, Map<String, Object> params) {
		final Map<String, Object> velocityParams = getInitialParams(username, id);
		velocityParams.putAll(params);

		return moduleDescriptor.getHtml(VIEW_TEMPLATE, velocityParams);
	}

	private Map<String, Object> getInitialParams(final String username, final String id) {
		final Map<String, Object> params = MapBuilder.<String, Object> newBuilder().add("username", username)
				.toMutableMap();
		return params;
	}

}
