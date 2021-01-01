package com.jtricks;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.search.Searcher;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueFactory;
import com.atlassian.jira.issue.search.SearchException;
import com.atlassian.jira.issue.search.SearchProvider;
import com.atlassian.jira.issue.search.SearchProviderFactory;
import com.atlassian.jira.issue.search.SearchRequest;
import com.atlassian.jira.issue.statistics.util.DocumentHitCollector;
import com.atlassian.jira.issue.views.util.IssueWriterHitCollector;
import com.atlassian.jira.plugin.searchrequestview.AbstractSearchRequestView;
import com.atlassian.jira.plugin.searchrequestview.SearchRequestParams;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.util.JiraVelocityUtils;
import com.atlassian.jira.util.map.EasyMap;
import com.opensymphony.user.User;

public class SimpleSearchRequestHTMLView extends AbstractSearchRequestView {

	private final JiraAuthenticationContext authenticationContext;
	private final SearchProviderFactory searchProviderFactory;
	private final IssueFactory issueFactory;
	private final SearchProvider searchProvider;

	public SimpleSearchRequestHTMLView(JiraAuthenticationContext authenticationContext,
			SearchProviderFactory searchProviderFactory, IssueFactory issueFactory, SearchProvider searchProvider) {
		this.authenticationContext = authenticationContext;
		this.searchProviderFactory = searchProviderFactory;
		this.issueFactory = issueFactory;
		this.searchProvider = searchProvider;
	}

	@Override
	public void writeSearchResults(final SearchRequest searchRequest, final SearchRequestParams searchRequestParams,
			final Writer writer) throws SearchException {
		final Map defaultParams = JiraVelocityUtils.getDefaultVelocityParams(authenticationContext);

		try {
			// Need to put the filtername into the velocity context. This may be
			// null if this is an anoymous filter.
			final Map headerParams = new HashMap(defaultParams);
			headerParams.put("filtername", searchRequest.getName());
			// Let us put the user as well
			User user = authenticationContext.getUser();
			headerParams.put("user", user);

			// First we need to write the header
			writer.write(descriptor.getHtml("header", headerParams));

			// now lets write the search results. This basically iterates over
			// each issue in the search results and writes
			// it to the writer using the format defined by this plugin. To
			// ensure that this doesn't result in huge
			// memory consumption only one issue should be loaded into memory at
			// a time. This can be guaranteed by using a
			// Hitcollector.
			final Searcher searcher = searchProviderFactory.getSearcher(SearchProviderFactory.ISSUE_INDEX);
			final Map issueParams = new HashMap(defaultParams);
			// This hit collector is responsible for writing out each issue as
			// it is encountered in the search results.
			// It will be called for each search result by the underlying Lucene
			// search code.
			final DocumentHitCollector hitCollector = new IssueWriterHitCollector(searcher, writer, issueFactory) {
				protected void writeIssue(Issue issue, Writer writer) throws IOException {
					// put the current issue into the velocity context and
					// render the single issue view
					issueParams.put("issue", issue);
					writer.write(descriptor.getHtml("body", issueParams));
				}
			};
			// now run the search that's defined in the issue navigator and pass
			// in the hitcollector from above which will
			// write out each issue in the format specified in this plugin.
			searchProvider.searchAndSort(searchRequest.getQuery(), user, hitCollector,
					searchRequestParams.getPagerFilter());

			// finally lets write the footer. Let's put the user again ;)
			writer.write(descriptor.getHtml("footer", EasyMap.build("user", user)));
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (SearchException e) {
			throw new RuntimeException(e);
		}
	}

}
