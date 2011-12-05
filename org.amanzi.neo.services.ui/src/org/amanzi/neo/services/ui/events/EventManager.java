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

package org.amanzi.neo.services.ui.events;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.amanzi.neo.services.ui.enums.EventsType;
import org.amanzi.neo.services.ui.neoclipse.manager.NeoclipseListenerManager;
import org.amanzi.neo.services.ui.utils.ActionUtil;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

/**
 * <p>
 * controll events
 * </p>
 * 
 * @author Vladislav_Kondratenko
 * @since 1.0.0
 */
public class EventManager {
    /**
     * manager instance;
     */
    private static EventManager manager;

    /**
     * appropriation with event and listeners colections
     */
    private Map<EventsType, Set<IEventsListener< ? extends AbstractEvent>>> listenersCollections;

    /**
     * get instance of event manager
     * 
     * @return
     */
    public static EventManager getInstance() {
        if (manager == null) {
            manager = new EventManager();
        }
        return manager;
    }

    /**
     * create class instance
     */
    private EventManager() {
        listenersCollections = new HashMap<EventsType, Set<IEventsListener< ? extends AbstractEvent>>>();
        NeoclipseListenerManager.initialiseNeoclipseListeners(this);
    }

    /**
     * add listener to event
     * 
     * @param eventType
     * @param eventsListeners
     */
    public void addListener(EventsType eventType, IEventsListener< ? extends AbstractEvent>... eventsListeners) {
        if (!listenersCollections.containsKey(eventType)) {
            listenersCollections.put(eventType, new HashSet<IEventsListener< ? extends AbstractEvent>>());
        }
        listenersCollections.get(eventType).addAll(Arrays.asList(eventsListeners));

    }

    /**
     * fire event
     * 
     * @param event
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public <T extends AbstractEvent> void fireEvent(final T event) {
        EventsType type = event.getType();
        final Object fireTarget = event.getTarget();
        showView((String)fireTarget);
        final Set<IEventsListener<T>> eventListeners = (Set)listenersCollections.get(type);
        // fire events in main thread
        ActionUtil.getInstance().runTask(new Runnable() {
            @Override
            public void run() {
                if (eventListeners != null) {
                    for (final IEventsListener<T> listeners : eventListeners) {
                        if ((fireTarget == null && listeners.getSource() == null) || fireTarget.equals(listeners.getSource())) {
                            listeners.handleEvent(event);
                        }
                    }
                }
                fireRelatedEvent(event);
            }
        }, true);
    }

    /**
     * try to search and open view by id
     */
    public void showView(final String id) {
        if (id == null || id.isEmpty()) {
            return;
        }
        try {
            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(id);
        } catch (PartInitException e) {
        }
    }

    /**
     * fire related event for example event PROJECT_CHANGE should also fire UPDATE_DATA
     * 
     * @param event
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T extends AbstractEvent> void fireRelatedEvent(final T event) {
        EventsType type = event.getType();
        Set<IEventsListener<T>> eventListeners = null;
        T relatedEvent = null;
        switch (type) {
        case CHANGE_PROJECT:
            type = EventsType.UPDATE_DATA;
            relatedEvent = (T)new UpdateDataEvent();
            break;
        default:
            return;
        }
        eventListeners = (Set)listenersCollections.get(type);
        if (eventListeners != null) {
            for (final IEventsListener<T> listeners : eventListeners) {
                listeners.handleEvent(relatedEvent);
            }
        }
    }
}