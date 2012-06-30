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

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.List;
import java.util.Map;

import org.amanzi.awe.ui.events.EventStatus;
import org.amanzi.awe.ui.events.impl.AWEStartedEvent;
import org.amanzi.awe.ui.listener.IAWEEventListenter;
import org.amanzi.awe.ui.manager.internal.Listener1;
import org.amanzi.awe.ui.manager.internal.Listener2;
import org.amanzi.awe.ui.manager.internal.Listener3;
import org.amanzi.testing.AbstractIntegrationTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * TODO Purpose of
 * <p>
 * </p>
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class AWEEventManagerTest extends AbstractIntegrationTest {

    private AWEEventManager eventManager;

    @Override
    @Before
    public void setUp() {
        super.setUp();

        eventManager = AWEEventManager.getManager();
        eventManager.getListeners().clear();
    }

    @Override
    @After
    public void tearDown() {
        eventManager.getListeners().clear();
        super.tearDown();
    }

    @Test
    public void testCheckAddListener() {
        Listener1 listener = new Listener1();

        eventManager.addListener(listener, EventStatus.AWE_STARTED);

        Map<EventStatus, List<IAWEEventListenter>> listenersMap = eventManager.getListeners();
        assertEquals("Unexpected size of listened events", 1, listenersMap.size());

        List<IAWEEventListenter> listenerList = listenersMap.get(EventStatus.AWE_STARTED);
        assertNotNull("Unexpected listeners list for AWE STARTED", listenerList);
        assertEquals("Unexpected size of listeners", 1, listenerList.size());

        IAWEEventListenter result = listenerList.get(0);
        assertEquals("Unexpected listener", listener, result);
    }

    @Test
    public void testCheckAddMultiEventListener() {
        Listener1 listener = new Listener1();

        eventManager.addListener(listener, EventStatus.values());

        Map<EventStatus, List<IAWEEventListenter>> listenersMap = eventManager.getListeners();
        assertEquals("Unexpected size of listened events", EventStatus.values().length, listenersMap.size());

        for (EventStatus singleStatus : EventStatus.values()) {
            List<IAWEEventListenter> listenerList = listenersMap.get(singleStatus);
            assertNotNull("Unexpected listeners list for " + singleStatus, listenerList);
            assertEquals("Unexpected size of listeners", 1, listenerList.size());

            IAWEEventListenter result = listenerList.get(0);
            assertEquals("Unexpected listener", listener, result);
        }
    }

    @Test
    public void testCheckAddMultiEventListeners() {
        Listener1 listener1 = new Listener1();
        Listener2 listener2 = new Listener2();
        Listener3 listener3 = new Listener3();

        eventManager.addListener(listener1, EventStatus.AWE_STARTED);
        eventManager.addListener(listener2, EventStatus.PROJECT_CHANGED);
        eventManager.addListener(listener3, EventStatus.AWE_STARTED, EventStatus.PROJECT_CHANGED);

        Map<EventStatus, List<IAWEEventListenter>> listenersMap = eventManager.getListeners();
        assertEquals("Unexpected size of listened events", 2, listenersMap.size());

        // check awe started
        List<IAWEEventListenter> listenerList = listenersMap.get(EventStatus.AWE_STARTED);
        assertNotNull("Unexpected listeners list for AWE_STARTED", listenerList);
        assertEquals("Unexpected size of listeners", 2, listenerList.size());

        assertTrue("Unexpected listener1", listenerList.contains(listener1));
        assertTrue("Unexpected listener3", listenerList.contains(listener3));

        // check project changed
        listenerList = listenersMap.get(EventStatus.PROJECT_CHANGED);
        assertNotNull("Unexpected listeners list for PROJECT CHANGED", listenerList);
        assertEquals("Unexpected size of listeners", 2, listenerList.size());

        assertTrue("Unexpected listener2", listenerList.contains(listener2));
        assertTrue("Unexpected listener3", listenerList.contains(listener3));
    }

    @Test
    public void testCheckEventFiring() {
        IAWEEventListenter listener1 = mock(IAWEEventListenter.class);
        IAWEEventListenter listener2 = mock(IAWEEventListenter.class);
        IAWEEventListenter listener3 = mock(IAWEEventListenter.class);

        eventManager.addListener(listener1, EventStatus.AWE_STARTED);
        eventManager.addListener(listener2, EventStatus.PROJECT_CHANGED);
        eventManager.addListener(listener3, EventStatus.AWE_STARTED, EventStatus.PROJECT_CHANGED);

        eventManager.fireAWEStartedEvent();

        verify(listener1).onEvent(eq(new AWEStartedEvent()));
        verify(listener3).onEvent(eq(new AWEStartedEvent()));
        verifyNoMoreInteractions(listener1, listener2, listener3);
    }

    @Test
    public void testCheckDuplicatedListener() {
        Listener1 listener = new Listener1();

        eventManager.addListener(listener, EventStatus.AWE_STARTED);

        List<IAWEEventListenter> listeners = eventManager.getListeners().get(EventStatus.AWE_STARTED);

        eventManager.addListener(listener, EventStatus.AWE_STARTED);

        assertEquals("size should not increase since listener is duplicated", listeners.size(),
                eventManager.getListeners().get(EventStatus.AWE_STARTED).size());
    }
}
