/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

import org.eclipse.birt.chart.ui.util.ChartUIUtil.EAttributeAccessor;
import org.eclipse.swt.widgets.Listener;

/**
 * A utility class to display Expression builder and button
 */

public interface IExpressionButton {

	/**
	 * Returns the expression that's saved in model
	 *
	 * @return the expression that's saved in model
	 */
	String getExpression();

	/**
	 * Sets the expression to a binding expression created with the given binding
	 * name and the current expression type.
	 *
	 * @param bindingName   the binding name
	 * @param bNotifyEvents indicates whether the listeners needed to notified.
	 */
	void setBindingName(String bindingName, boolean bNotifyEvents);

	/**
	 * Sets the expression that's saved in model, no notifications will be sent.
	 *
	 * @param expr the expression that's saved in model
	 */
	void setExpression(String expr);

	/**
	 * Sets the expression that's saved in model
	 *
	 * @param expr          the expression that's saved in model
	 * @param bNotifyEvents indicates whether the listeners needed to notified.
	 */
	void setExpression(String expr, boolean bNotifyEvents);

	/**
	 * Returns the display string in expression builder. This may be different from
	 * the value saved in model.
	 *
	 * @return the display string in expression builder
	 */
	String getDisplayExpression();

	/**
	 * Sets the enabled state
	 *
	 * @param bEnabled enabled state
	 */
	void setEnabled(boolean bEnabled);

	/**
	 * Returns the enabled state
	 *
	 * @return the enabled state
	 */
	boolean isEnabled();

	/**
	 * Adds a listener, which will be notified with a SWT.Mofigy event if the
	 * expression text has changed.
	 *
	 * @param listener
	 */
	void addListener(Listener listener);

	/**
	 * Sets the accessor, with which the expression will be load from and save to.
	 *
	 * @param accessor
	 */
	void setAccessor(EAttributeAccessor<String> accessor);

	/**
	 * Returns the type of the expression.
	 *
	 * @return The the type of the expression.
	 */
	String getExpressionType();

	/**
	 * Returns whether the chart is using a cube.
	 *
	 * @return Whether the chart is using a cube.
	 */
	boolean isCube();

	/**
	 * Set the AssistField.
	 *
	 * @param assistField
	 */
	void setAssitField(IAssistField assistField);

	/**
	 *
	 * @param predefinedQuery
	 */
	void setPredefinedQuery(Object[] predefinedQuery);

}
