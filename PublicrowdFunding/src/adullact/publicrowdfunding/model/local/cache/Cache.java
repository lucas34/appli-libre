package adullact.publicrowdfunding.model.local.cache;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import adullact.publicrowdfunding.model.local.callback.WhatToDo;
import adullact.publicrowdfunding.model.local.ressource.Resource;
import adullact.publicrowdfunding.model.server.event.RetrieveEvent;

/**
 * Created by Ferrand on 18/07/2014.
 */
public class Cache<TResource extends Resource<TResource, ?, ?>> {
    private Sync<TResource> m_resource;
    private DateTime m_dateTime;

    public Cache(TResource resource) {
        this.m_resource = new Sync<TResource>(resource);
        this.m_dateTime = null;
    }

    public String getResourceId() {
        return m_resource.id;
    }

    public Cache<TResource> declareUpToDate() {
        m_dateTime = DateTime.now();

        return this;
    }

    public Cache<TResource> forceRetrieve() {
        m_dateTime = null;

        return this;
    }

    public void toResource(final WhatToDo<TResource> whatToDo) {
        boolean timeToRetrieve;
        if(m_dateTime == null) {
            timeToRetrieve = true;
        }
        else {
            Duration duration = new Duration(m_dateTime, DateTime.now());
            timeToRetrieve = (duration.getStandardMinutes() > 15);// Data may be outdated
       }
        if(timeToRetrieve) {
            final RetrieveEvent<TResource> event = new RetrieveEvent<TResource>() {

                @Override
                public void errorResourceIdDoesNotExists(String id) {
                    m_resource.setState(Sync.State.deleted);
                    whatToDo.give(m_resource);
                }

                @Override
                public void onRetrieve(TResource resource) {
                    if(m_resource.resource.hasChanged()) {
                        m_resource.setState(Sync.State.changed);
                    }
                    else {
                        m_resource.setState(Sync.State.unchanged);
                    }
                    m_dateTime = DateTime.now();
                    whatToDo.give(m_resource);
                }
            };
            m_resource.resource.serverRetrieve(event);
        }
        else {
            whatToDo.give(m_resource);
        }
    }
}
