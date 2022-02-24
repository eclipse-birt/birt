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
 * AggregationCellViewAdapter
 */
public abstract class AggregationCellViewAdapter implements IAggregationCellViewProvider {

	protected int defalutUpdateType = SWITCH_VIEW_TYPE;

	public void setDefaultUpdateType(int type) {
		defalutUpdateType = type;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.item.crosstab.ui.extension.
	 * IAggregationCellViewProvider#matchView(org.eclipse.birt.report.item.crosstab.
	 * core.de.AggregationCellHandle)
	 */
	@Override
	public boolean matchView(AggregationCellHandle cell) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.item.crosstab.ui.extension.
	 * IAggregationCellViewProvider#restoreView(org.eclipse.birt.report.item.
	 * crosstab.core.de.AggregationCellHandle)
	 */
	@Override
	public void restoreView(AggregationCellHandle cell) {
	}

	/**
	 * @deprecated use {@link #switchView(SwitchCellInfo)}
	 */
	@Deprecated
	@Override
	public void switchView(AggregationCellHandle cell) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.item.crosstab.ui.extension.
	 * IAggregationCellViewProvider#switchView(org.eclipse.birt.report.item.crosstab
	 * .ui.extension.SwitchCellInfo)
	 */
	@Override
	public void switchView(SwitchCellInfo info) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.item.crosstab.ui.extension.
	 * IAggregationCellViewProvider#updateView(org.eclipse.birt.report.item.crosstab
	 * .core.de.AggregationCellHandle)
	 */
	@Override
	public void updateView(AggregationCellHandle cell) {
		updateView(cell, defalutUpdateType);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.item.crosstab.ui.extension.
	 * IAggregationCellViewProvider#updateView(org.eclipse.birt.report.item.crosstab
	 * .core.de.AggregationCellHandle, int)
	 */
	@Override
	public void updateView(AggregationCellHandle cell, int type) {

	}

	/**
	 * @deprecated use {@link #canSwitch(SwitchCellInfo)}
	 */
	@Deprecated
	@Override
	public boolean canSwitch(AggregationCellHandle cell) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.item.crosstab.ui.extension.
	 * IAggregationCellViewProvider#canSwitch(org.eclipse.birt.report.item.crosstab.
	 * ui.extension.SwitchCellInfo)
	 */
	@Override
	public boolean canSwitch(SwitchCellInfo info) {
		return true;
	}

	@Override
	public int getDefaultUpdateType() {
		return defalutUpdateType;
	}

}
