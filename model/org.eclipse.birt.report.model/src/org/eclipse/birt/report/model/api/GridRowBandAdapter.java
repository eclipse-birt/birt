/**
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.model.api;

/**
 * Represents an object of copied objects when do copy/paste operations between
 * grids.
 */

public class GridRowBandAdapter extends RowBandAdapter {
	/**
	 * The element where the copy/paste operation occurs.
	 */

	protected GridHandle element;

	GridRowBandAdapter() {
	}

	GridRowBandAdapter(GridHandle element) {
		this.element = element;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.GridRowBandAdapter#getElementHandle()
	 */
	@Override
	protected ReportItemHandle getElementHandle() {
		return element;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.GridRowBandAdapter#getRowCount()
	 */
	@Override
	protected int getRowCount() {
		return element.getRows().getCount();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.GridRowBandAdapter#getColumnCount()
	 */
	@Override
	protected int getColumnCount() {
		return element.getColumnCount();
	}

}
