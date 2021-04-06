/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.render;

import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.data.Action;

/**
 * This interface defines the methods to process the trigger actions during
 * rendering.
 */
public interface IActionRenderer {

	String LEGEND_ITEM_DATA = "legendItemData";//$NON-NLS-1$

	/**
	 * Expression evaluated in Legend trigger action.
	 */
	String LEGEND_ITEM_TEXT = "legendItemText"; //$NON-NLS-1$

	String LEGEND_ITEM_VALUE = "legendItemValue";//$NON-NLS-1$

	String AXIS_LABEL = "axisLabel"; //$NON-NLS-1$

	/**
	 * Process the action with given source object.
	 * 
	 * @param action Action Object.
	 * @param source Source Object
	 * @param rtc    chart runtime context
	 */
	void processAction(Action action, StructureSource source, RunTimeContext rtc);

	/**
	 * Process the action with given source object.
	 * 
	 * @param action Action Object.
	 * @param source Source Object
	 * @deprecated use
	 *             {@link #processAction(Action, StructureSource, RunTimeContext)}
	 *             instead
	 */
	void processAction(Action action, StructureSource source);
}
