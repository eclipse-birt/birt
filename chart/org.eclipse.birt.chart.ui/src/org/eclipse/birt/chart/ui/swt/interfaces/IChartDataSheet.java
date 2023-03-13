/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.interfaces;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.ITask;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.IWizardContext;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * Data sheet integration interface for chart builder. Implement this UI
 * interface to integrate chart builder with various data bindings.
 */

public interface IChartDataSheet {

	/**
	 * Event type indicates refreshing preview.
	 */
	int EVENT_PREVIEW = 1;

	/**
	 * Event type indicates updating predefined queries.
	 */
	int EVENT_QUERY = 2;

	/** The field indicates the component color of query should be updated. */
	int DETAIL_UPDATE_COLOR = 3;

	/** The field indicates the component color and text should be updated. */
	int DETAIL_UPDATE_COLOR_AND_TEXT = 4;

	/**
	 * Sets chart model.
	 *
	 * @param cm chart model
	 */
	void setChartModel(Chart cm);

	/**
	 * Sets chart context.
	 *
	 * @param context chart context
	 */
	void setContext(IWizardContext context);

	/**
	 * Creates the customized UI to maintain left, right and bottom parts in data
	 * sheet.
	 *
	 * @param task data sheet task
	 * @return customized UI
	 */
	ISelectDataCustomizeUI createCustomizeUI(ITask task);

	/**
	 * Creates data selector to select data set and etc.
	 *
	 * @param parent parent composite
	 * @return new composite
	 */
	Composite createDataSelector(Composite parent);

	/**
	 * Creates the UI which could be used as drag-and-drop source during data
	 * binding.
	 *
	 * @param parent parent composite
	 * @return new composite
	 */
	Composite createDataDragSource(Composite parent);

	/**
	 * Creates the UI which includes buttons to trigger some actions.
	 *
	 * @param parent parent composite
	 * @return new composite
	 */
	Composite createActionButtons(Composite parent);

	/**
	 * Adds the listener to the collection of listeners who will be notified when an
	 * event of the given type occurs. When the event does occur in the widget, the
	 * listener is notified by sending it the <code>handleEvent()</code> message.
	 * The event type is one of the event constants defined in class
	 * <code>SWT</code>.
	 *
	 * @param listener the listener which should be notified when the event occurs
	 *
	 *
	 * @see Listener
	 * @see #removeListener(Listener)
	 * @see #notifyListeners(Event)
	 */
	void addListener(Listener listener);

	/**
	 * Removes the listener from the collection of listeners who will be notified
	 * when an event of the given type occurs. The event type is one of the event
	 * constants defined in class <code>SWT</code>.
	 *
	 * @param listener the listener which should no longer be notified when the
	 *                 event occurs
	 *
	 *
	 * @see Listener
	 * @see #addListener(Listener)
	 * @see #notifyListeners(Event)
	 */
	void removeListener(Listener listener);

	/**
	 * Notifies all of the receiver's listeners for events of the given type that
	 * one such event has occurred by invoking their <code>handleEvent()</code>
	 * method. The event type is one of the event constants defined in class
	 * <code>SWT</code>.
	 *
	 * @param event the event data
	 *
	 *
	 * @see #addListener(Listener)
	 * @see #removeListener(Listener)
	 */
	void notifyListeners(Event event);

	/**
	 * Disposes the resources if needed.
	 */
	void dispose();
}
