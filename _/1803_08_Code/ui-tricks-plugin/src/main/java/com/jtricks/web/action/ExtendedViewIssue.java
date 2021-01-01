package com.jtricks.web.action;

import com.atlassian.jira.bc.issue.attachment.AttachmentService;
import com.atlassian.jira.bc.issue.comment.CommentService;
import com.atlassian.jira.config.SubTaskManager;
import com.atlassian.jira.issue.AttachmentManager;
import com.atlassian.jira.issue.RendererManager;
import com.atlassian.jira.issue.attachment.AttachmentZipKit;
import com.atlassian.jira.issue.comments.CommentManager;
import com.atlassian.jira.issue.fields.FieldManager;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutManager;
import com.atlassian.jira.issue.fields.screen.FieldScreenRendererFactory;
import com.atlassian.jira.issue.link.IssueLinkManager;
import com.atlassian.jira.issue.pager.PagerManager;
import com.atlassian.jira.issue.thumbnail.ThumbnailManager;
import com.atlassian.jira.issue.util.AggregateTimeTrackingCalculatorFactory;
import com.atlassian.jira.plugin.webfragment.SimpleLinkManager;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.security.roles.ProjectRoleManager;
import com.atlassian.jira.trackback.TrackbackManager;
import com.atlassian.jira.web.action.issue.ViewIssue;
import com.atlassian.jira.web.bean.NonZipExpandableExtensions;
import com.atlassian.jira.web.bean.TimeTrackingGraphBeanFactory;
import com.atlassian.plugin.PluginAccessor;
import com.atlassian.plugin.web.WebInterfaceManager;
import com.atlassian.plugin.webresource.WebResourceManager;

public class ExtendedViewIssue extends ViewIssue{
	
	public ExtendedViewIssue(TrackbackManager trackbackManager, ThumbnailManager thumbnailManager,
			SubTaskManager subTaskManager, IssueLinkManager issueLinkManager, PluginAccessor pluginAccessor,
			FieldManager fieldManager, FieldScreenRendererFactory fieldScreenRendererFactory,
			FieldLayoutManager fieldLayoutManager, RendererManager rendererManager, CommentManager commentManager,
			ProjectRoleManager projectRoleManager, CommentService commentService, AttachmentManager attachmentManager,
			AttachmentService attachmentService,
			AggregateTimeTrackingCalculatorFactory aggregateTimeTrackingCalculatorFactory, PagerManager pagerManager,
			WebResourceManager webResourceManager, SimpleLinkManager simpleLinkManager,
			TimeTrackingGraphBeanFactory timeTrackingGraphBeanFactory, AttachmentZipKit attachmentZipKit,
			NonZipExpandableExtensions nonZipExpandableExtensions, WebInterfaceManager webInterfaceManager,
			PermissionManager permissionManager) {
		super(trackbackManager, thumbnailManager, subTaskManager, issueLinkManager, pluginAccessor, fieldManager,
				fieldScreenRendererFactory, fieldLayoutManager, rendererManager, commentManager, projectRoleManager,
				commentService, attachmentManager, attachmentService, aggregateTimeTrackingCalculatorFactory, pagerManager,
				webResourceManager, simpleLinkManager, timeTrackingGraphBeanFactory, attachmentZipKit,
				nonZipExpandableExtensions, webInterfaceManager, permissionManager);
		// TODO Auto-generated constructor stub
	}

	public boolean hasNoSubtasks(){
		return getIssueObject().isSubTask() && getIssueObject().getSubTaskObjects().isEmpty();
	}

}
