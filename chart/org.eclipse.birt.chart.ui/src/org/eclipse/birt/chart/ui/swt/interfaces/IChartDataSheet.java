/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.interfaces;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * Data sheet integration interface for chart builder. Implement this UI
 * interface to integrate chart builder with various data bindings.
 */

public interface IChartDataSheet
{

	/**
	 * Event type indicates outside update.
	 */
	int EVENT_UPDATE = 1;

	/**
	 * Sets chart model.
	 * 
	 * @param cm
	 *            chart model
	 */
	void setChartModel( Chart cm );

	/**
	 * Creates data selector to select data set and etc.
	 * 
	 * @param parent
	 *            parent composite
	 * @return new composite
	 */
	Composite createDataSelector( Composite parent );

	/**
	 * Creates the UI which could be used as drag-and-drop source during data
	 * binding.
	 * 
	 * @param parent
	 *            parent composite
	 * @return new composite
	 */
	Composite createDataDragSource( Composite parent );

	/**
	 * Creates the UI which includes buttons to trigger some actions.
	 * 
	 * @param parent
	 *            parent composite
	 * @return new composite
	 */
	Composite createActionButtons( Composite parent );

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when an event of the given type occurs. When the event does occur in the
	 * widget, the listener is notified by sending it the
	 * <code>handleEvent()</code> message. The event type is one of the event
	 * constants defined in class <code>SWT</code>.
	 * 
	 * @param eventType
	 *            the type of event to listen for
	 * @param listener
	 *            the listener which should be notified when the event occurs
	 * 
	 * 
	 * @see Listener
	 * @see #removeListener(int, Listener)
	 * @see #notifyListeners(int, Event)
	 */
	void addListener( int eventType, Listener listener );

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when an event of the given type occurs. The event type is one of
	 * the event constants defined in class <code>SWT</code>.
	 * 
	 * @param eventType
	 *            the type of event to listen for
	 * @param listener
	 *            the listener which should no longer be notified when the event
	 *            occurs
	 * 
	 * 
	 * @see Listener
	 * @see #addListener(int, Listener)
	 * @see #notifyListeners(int, Event)
	 */
	void removeListener( int eventType, Listener listener );

	/**
	 * Notifies all of the receiver's listeners for events of the given type
	 * that one such event has occurred by invoking their
	 * <code>handleEvent()</code> method. The event type is one of the event
	 * constants defined in class <code>SWT</code>.
	 * 
	 * @param eventType
	 *            the type of event which has occurred
	 * @param event
	 *            the event data
	 * 
	 * 
	 * @see #addListener(int, Listener)
	 * @see #removeListener(int, Listener)
	 */
	void notifyListeners( int eventType, Event event );

	void dispose( );
}
