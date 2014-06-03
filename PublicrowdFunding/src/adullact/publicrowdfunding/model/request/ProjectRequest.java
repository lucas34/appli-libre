package adullact.publicrowdfunding.model.request;

import java.util.Date;

import adullact.publicrowdfunding.model.event.ProjectEvent;
import adullact.publicrowdfunding.shared.Project;

public abstract class ProjectRequest<TRequest extends ProjectRequest<TRequest, TEvent>, TEvent extends ProjectEvent<TEvent, TRequest>> extends Request<TRequest, TEvent> {
	private Project m_project;

	public ProjectRequest(String name, String description, String requestedFunding, Date beginDate, Date endDate) {
		super();
		
		this.m_project = new Project(name, description, requestedFunding, new Date(), beginDate, endDate);
		
	}
	
	public ProjectRequest(Project project) {
		super();
		
		this.m_project = project;
		
	}
	
	public Project project() {
		return m_project;
	}

}