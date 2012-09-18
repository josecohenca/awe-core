/* AWE - Amanzi Wireless Explorer
 * http://awe.amanzi.org
 * (C) 2008-2009, AmanziTel AB
 *
 * This library is provided under the terms of the Eclipse Public License
 * as described at http://www.eclipse.org/legal/epl-v10.html. Any use,
 * reproduction or distribution of the library constitutes recipient's
 * acceptance of this agreement.
 *
 * This library is distributed WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */

package org.amanzi.awe.ui.manager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.amanzi.awe.ui.events.EventStatus;
import org.amanzi.awe.ui.events.IEvent;
import org.amanzi.awe.ui.events.impl.AWEStartedEvent;
import org.amanzi.awe.ui.events.impl.AWEStoppedEvent;
import org.amanzi.awe.ui.events.impl.DataUpdatedEvent;
import org.amanzi.awe.ui.events.impl.InitialiseEvent;
import org.amanzi.awe.ui.events.impl.ProjectNameChangedEvent;
import org.amanzi.awe.ui.events.impl.ShowElementsOnMap;
import org.amanzi.awe.ui.events.impl.ShowGISOnMap;
import org.amanzi.awe.ui.listener.IAWEEventListenter;
import org.amanzi.awe.ui.listener.IAWEEventListenter.Priority;
import org.amanzi.awe.ui.util.ActionUtil;
import org.amanzi.neo.dto.IDataElement;
import org.amanzi.neo.models.render.IGISModel;
import org.amanzi.neo.models.render.IRenderableModel;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.geotools.geometry.jts.ReferencedEnvelope;

/**
 * TODO Purpose of
 * <p>
 * </p>
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public final class AWEEventManager {

    private static final Logger LOGGER = Logger.getLogger(AWEEventManager.class);

    private static class AWEEventManagerInstanceHandler {
        private static volatile AWEEventManager instance = new AWEEventManager();
    }

    private static final String AWE_LISTENER_EXTENSION_POINT_ID = "org.amanzi.awe.ui.listeners";

    private static final String EVENT_STATUS_CHILD = "eventStatus";

    private static final String CLASS_ATTRIBUTE = "class";

    private static final String STATUS_ATTRIBUTE = "status";

    private static final IEvent AWE_STARTED_EVENT = new AWEStartedEvent(null);

    private static final IEvent AWE_STOPPED_EVENT = new AWEStoppedEvent(null);

    public static final IEvent DATA_UPDATED_EVENT = new DataUpdatedEvent(null);

    private static final IEvent INITIALISE_EVENT = new InitialiseEvent(null);

    private final Map<Priority, Map<EventStatus, Set<IAWEEventListenter>>> listeners = new HashMap<Priority, Map<EventStatus, Set<IAWEEventListenter>>>();

    private final IExtensionRegistry registry;

    protected AWEEventManager() {
        this(Platform.getExtensionRegistry());
    }

    protected AWEEventManager(final IExtensionRegistry registry) {
        this.registry = registry;
        initializeExtensionPointListeners();
    }

    public static AWEEventManager getManager() {
        return AWEEventManagerInstanceHandler.instance;
    }

    public synchronized void addListener(final IAWEEventListenter listener, final EventStatus... statuses) {
        assert listener != null;
        assert statuses != null;

        Map<EventStatus, Set<IAWEEventListenter>> prioritizedListeners = listeners.get(listener.getPriority());
        if (prioritizedListeners == null) {
            prioritizedListeners = new HashMap<EventStatus, Set<IAWEEventListenter>>();
            listeners.put(listener.getPriority(), prioritizedListeners);
        }

        for (EventStatus eventStatus : statuses) {
            Set<IAWEEventListenter> eventListeners = prioritizedListeners.get(eventStatus);

            if (eventListeners == null) {
                eventListeners = new HashSet<IAWEEventListenter>();
                prioritizedListeners.put(eventStatus, eventListeners);
            }

            eventListeners.add(listener);
        }
    }

    public synchronized void removeListener(final IAWEEventListenter listener) {
        Map<EventStatus, Set<IAWEEventListenter>> prioritizedListeners = listeners.get(listener.getPriority());

        if (prioritizedListeners != null) {
            for (Entry<EventStatus, Set<IAWEEventListenter>> listenerEntry : prioritizedListeners.entrySet()) {
                listenerEntry.getValue().remove(listener);
            }
        }
    }

    private void fireEvent(final IEvent event, final boolean inDisplay) {
        for (Priority priority : Priority.getSortedPriorities()) {
            Map<EventStatus, Set<IAWEEventListenter>> prioritizedListeners = listeners.get(priority);

            if (prioritizedListeners != null) {
                Set<IAWEEventListenter> eventListeners = prioritizedListeners.get(event.getStatus());

                if (eventListeners != null) {
                    for (IAWEEventListenter singleListener : eventListeners) {
                        run(event, singleListener, inDisplay);
                    }
                }
            }
        }
    }

    /**
     * @param event
     * @param singleListener
     * @param inDisplay
     */
    private void run(IEvent event, IAWEEventListenter singleListener, boolean inDisplay) {
        if (event.getSource() != null && event.getSource().equals(singleListener)) {
            return;
        }

        if (inDisplay) {
            runEventListener(event, singleListener);
        } else {
            singleListener.onEvent(event);
        }
    }

    private void runEventListener(final IEvent event, final IAWEEventListenter singleListener) {
        ActionUtil.getInstance().runTask(new Runnable() {

            @Override
            public void run() {
                singleListener.onEvent(event);
            }
        }, event.isAsync());
    }

    public synchronized void fireAWEStartedEvent() {
        fireEvent(AWE_STARTED_EVENT, true);
    }

    public synchronized void fireAWEStoppedEvent() {
        fireEvent(AWE_STOPPED_EVENT, true);
    }

    public synchronized void fireProjectNameChangedEvent(final String newProjectName, Object source) {
        fireEvent(new ProjectNameChangedEvent(newProjectName, source), true);
    }

    public synchronized void fireDataUpdatedEvent(Object source) {
        fireEvent(new DataUpdatedEvent(source), true);
    }

    public synchronized void fireShowOnMapEvent(final IGISModel model, Object source) {
        fireEvent(new ShowGISOnMap(model, source), true);
    }

    public synchronized void fireShowOnMapEvent(final IRenderableModel model, final Set<IDataElement> elements, Object source) {
        fireEvent(new ShowElementsOnMap(model, elements, source), true);
    }

    public synchronized void fireShowOnMapEvent(final IRenderableModel model, final Set<IDataElement> elements,
            final ReferencedEnvelope bounds, Object source) {
        fireEvent(new ShowElementsOnMap(model, elements, bounds, source), true);
    }

    public synchronized void fireInitialiseEvent() {
        fireEvent(INITIALISE_EVENT, true);
    }

    public synchronized void fireEventChain(final EventChain eventChain) {
        ActionUtil.getInstance().runTask(new Runnable() {

            @Override
            public void run() {
                for (IEvent event : eventChain.getEvents()) {
                    fireEvent(event, false);
                }
            }
        }, eventChain.isAsync());
    }

    private void initializeExtensionPointListeners() {
        for (IConfigurationElement listenerElement : registry.getConfigurationElementsFor(AWE_LISTENER_EXTENSION_POINT_ID)) {
            try {
                initializeListenerFromElement(listenerElement);
            } catch (CoreException e) {
                LOGGER.error("Error on initialization Listener <" + listenerElement.getAttribute(CLASS_ATTRIBUTE) + ">", e);
            } catch (ClassCastException e) {
                LOGGER.error("Class <" + listenerElement.getAttribute(CLASS_ATTRIBUTE) + "> is not a Listener class", e);
            }
        }
    }

    private void initializeListenerFromElement(final IConfigurationElement element) throws CoreException {
        IAWEEventListenter listener = (IAWEEventListenter)element.createExecutableExtension(CLASS_ATTRIBUTE);

        IConfigurationElement[] eventStatusElement = element.getChildren(EVENT_STATUS_CHILD);

        for (IConfigurationElement singleEventStatus : eventStatusElement) {
            EventStatus status = getEventStatusFromElement(singleEventStatus);

            if (status != null) {
                addListener(listener, status);
            }
        }
    }

    private EventStatus getEventStatusFromElement(final IConfigurationElement element) {
        String statusName = element.getAttribute(STATUS_ATTRIBUTE);

        return EventStatus.valueOf(statusName);
    }

    protected Map<EventStatus, Set<IAWEEventListenter>> getListeners(final Priority priority) {
        return listeners.get(priority);
    }

}
