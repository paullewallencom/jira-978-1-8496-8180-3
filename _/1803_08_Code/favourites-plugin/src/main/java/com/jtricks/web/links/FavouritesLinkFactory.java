package com.jtricks.web.links;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.atlassian.jira.plugin.webfragment.SimpleLinkFactory;
import com.atlassian.jira.plugin.webfragment.descriptors.SimpleLinkFactoryModuleDescriptor;
import com.atlassian.jira.plugin.webfragment.model.SimpleLink;
import com.atlassian.jira.plugin.webfragment.model.SimpleLinkImpl;
import com.opensymphony.user.User;

public class FavouritesLinkFactory implements SimpleLinkFactory {

	public List<SimpleLink> getLinks(User user, Map<String, Object> arg1) {
		List<SimpleLink> links = new ArrayList<SimpleLink>();

		if (user != null) {
			links.add(new SimpleLinkImpl("id1", "Favourites 1", "My Favourite One", null, null,
					"http://www.google.com", null));
			links.add(new SimpleLinkImpl("id2", "Favourites 2", "My Favourite Two", null, null,
					"http://www.j-tricks.com", null));
		} else {
			links.add(new SimpleLinkImpl("id1", "Favourite Link", "My Default Favourite", null, null,
					"http://www.google.com", null));
		}

		return links;
	}

	public void init(SimpleLinkFactoryModuleDescriptor arg0) {
	}

}
