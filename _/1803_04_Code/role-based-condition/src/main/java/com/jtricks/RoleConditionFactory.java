package com.jtricks;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.atlassian.core.util.map.EasyMap;
import com.atlassian.jira.plugin.workflow.AbstractWorkflowPluginFactory;
import com.atlassian.jira.plugin.workflow.WorkflowPluginConditionFactory;
import com.atlassian.jira.security.roles.ProjectRole;
import com.atlassian.jira.security.roles.ProjectRoleManager;
import com.opensymphony.workflow.loader.AbstractDescriptor;
import com.opensymphony.workflow.loader.ConditionDescriptor;

public class RoleConditionFactory extends AbstractWorkflowPluginFactory implements WorkflowPluginConditionFactory{

	private static final String ROLE = "role";
	private static final String ROLES = "roles";
	private static final String NOT_DEFINED = "Not Defined";
	
	private final ProjectRoleManager projectRoleManager;	

	public RoleConditionFactory(ProjectRoleManager projectRoleManager) {
		this.projectRoleManager = projectRoleManager;
	}

	@Override
	protected void getVelocityParamsForEdit(Map<String, Object> velocityParams, AbstractDescriptor descriptor) {
		velocityParams.put(ROLE, getRole(descriptor));
		velocityParams.put(ROLES, getProjectRoles());
	}

	@Override
	protected void getVelocityParamsForInput(Map<String, Object> velocityParams) {
		velocityParams.put(ROLES, getProjectRoles());
	}

	@Override
	protected void getVelocityParamsForView(Map<String, Object> velocityParams, AbstractDescriptor descriptor) {
		velocityParams.put(ROLE, getRole(descriptor));
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getDescriptorParams(Map<String, Object> conditionParams) {
		if (conditionParams != null && conditionParams.containsKey(ROLE))
        {
            return EasyMap.build(ROLE, extractSingleParam(conditionParams, ROLE));
        }

        // Create a 'hard coded' parameter
        return EasyMap.build();
	}
	
	private ProjectRole getRole(AbstractDescriptor descriptor){
		if (!(descriptor instanceof ConditionDescriptor)) {
			throw new IllegalArgumentException("Descriptor must be a ConditionDescriptor.");
		}
		
		ConditionDescriptor functionDescriptor = (ConditionDescriptor) descriptor;
		
		String role = (String) functionDescriptor.getArgs().get(ROLE);
		if (role!=null && role.trim().length()>0)
			return getProjectRole(role);
		else 
			return null;
	}
	
	private ProjectRole getProjectRole(String role) {
		return projectRoleManager.getProjectRole(new Long(role));
	}

	private Collection<ProjectRole> getProjectRoles(){
		Collection<ProjectRole> projRoles = projectRoleManager.getProjectRoles();
		return Collections.unmodifiableCollection(projRoles);
	}

}
