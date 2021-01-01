package com.jtricks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.ofbiz.core.entity.GenericValue;

import com.atlassian.jira.issue.search.SearchException;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.plugins.rest.common.security.AnonymousAllowed;

@Path("/category")
public class CategoryResource {
	
	private final ProjectManager projectManager;
	
	public CategoryResource(ProjectManager projectManager) {
		this.projectManager = projectManager;
	}

	@GET
	@AnonymousAllowed
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getCategories(@QueryParam("dummyParam") String dummyParam) throws SearchException {
		System.out.println("This is just a dummyParam to show how parameters can be passed to REST methods:"+dummyParam);
		
		Collection<GenericValue> categories = this.projectManager.getProjectCategories();
		
		List<Category> categoryList =  new ArrayList<Category>();		
		for (GenericValue category : categories) {
			categoryList.add(new Category(category.getString("id"), category.getString("name")));
		}
		
		Response.ResponseBuilder responseBuilder = Response.ok(new Categories(categoryList));
		return responseBuilder.build();
	}
	
	@GET
	@AnonymousAllowed
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/{id}")
	public Response getCategoryFromId(@PathParam("id") String id) throws SearchException {
		GenericValue category = this.projectManager.getProjectCategory(new Long(id));
		
		Response.ResponseBuilder responseBuilder = Response.ok(new Category(category.getString("id"), category.getString("name")));
		return responseBuilder.build();
	}
	
	@XmlRootElement
	public static class Category{
		@XmlElement
		private String id;
		@XmlElement
		private String name;
		
		public Category(){			
		}

		public Category(String id, String name) {
			this.id = id;
			this.name = name;
		}			
	}
	
	@XmlRootElement
	public static class Categories{
		@XmlElement
		private List<Category> categories;
		
		public Categories(){			
		}

		public Categories(List<Category> categories) {
			this.categories = categories;
		}		
	}

}
