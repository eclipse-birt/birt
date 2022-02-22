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

package org.eclipse.birt.report.item.crosstab.ui.extension;

import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;

/**
 * This inteface is used to create alternative view for aggregation cell in
 * crosstab.
 */
public interface IAggregationCellViewProvider {
	int SWITCH_VIEW_TYPE = 0;
	int CHANGE_ORIENTATION_TYPE = 1;

	int defalutUpdateType = SWITCH_VIEW_TYPE;

	int getDefaultUpdateType();

	/**
	 * Return the name of this view
	 */
	String getViewName();

	/**
	 * Return the display name of this view
	 */
	String getViewDisplayName();

	/**
	 * Returns if the given aggregation cell matches this view
	 */
	boolean matchView(AggregationCellHandle cell);

	/**
	 * @deprecated use {@link #switchView(SwitchCellInfo)}
	 */
	@Deprecated
	void switchView(AggregationCellHandle cell);

	/**
	 * Switches given aggregation cell to this view
	 */
	void switchView(SwitchCellInfo info);

	/**
	 * Restores given aggregation cell to previous view
	 */
	void restoreView(AggregationCellHandle cell);

	/**
	 * Updates current view when necessary
	 */
	void updateView(AggregationCellHandle cell);

	/**
	 * Updates current view when necessary with specified type.
	 */
	void updateView(AggregationCellHandle cell, int type);

	/**
	 * @deprecated use {@link #canSwitch(SwitchCellInfo)}
	 */
	@Deprecated
	boolean canSwitch(AggregationCellHandle cell);

	/**
	 * check whether can switch to this view
	 */
	boolean canSwitch(SwitchCellInfo info);

}
