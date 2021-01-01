package com.jtricks;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;

import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.NullProgressMonitor;
import com.atlassian.jira.rest.client.domain.Comment;
import com.atlassian.jira.rest.client.domain.Issue;
import com.atlassian.jira.rest.client.domain.Transition;
import com.atlassian.jira.rest.client.domain.input.FieldInput;
import com.atlassian.jira.rest.client.domain.input.TransitionInput;
import com.atlassian.jira.rest.client.internal.jersey.JerseyJiraRestClientFactory;

public class RESTClient {

	public static void main(String[] args) throws URISyntaxException {
		final JerseyJiraRestClientFactory factory = new JerseyJiraRestClientFactory();
		final URI uri = new URI("http://localhost:8080/jira");
		final JiraRestClient jiraRestClient = factory.createWithBasicHttpAuthentication(uri, "username", "password");
		final NullProgressMonitor nullProgressMonitor = new NullProgressMonitor();
		final Issue issue = jiraRestClient.getIssueClient().getIssue("TST-10", nullProgressMonitor);

		System.out.println(issue);

		// Vote Issue
		jiraRestClient.getIssueClient().vote(issue.getVotesUri(), nullProgressMonitor);

		// Watch Issue
		jiraRestClient.getIssueClient().watch(issue.getWatchers().getSelf(), nullProgressMonitor);

		// Progress on workflow
		final Iterable<Transition> transitions = jiraRestClient.getIssueClient().getTransitions(
				issue.getTransitionsUri(), nullProgressMonitor);
		final Transition startProgressTransition = getTransitionByName(transitions, "Close");
		if (startProgressTransition != null) {
			Collection<FieldInput> fieldInputs = Arrays.asList(new FieldInput("resolution", "Done"));
			jiraRestClient.getIssueClient().transition(issue.getTransitionsUri(),
					new TransitionInput(startProgressTransition.getId(), fieldInputs, Comment.valueOf("New comment")),
					nullProgressMonitor);
		} else {
			System.out.println("No Matching transition!");
		}

		System.out.println("Done");
	}

	private static Transition getTransitionByName(Iterable<Transition> transitions, String transitionName) {
		for (Transition transition : transitions) {
			if (transition.getName().equals(transitionName)) {
				return transition;
			}
		}
		return null;
	}

}
