/**
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	protected ReportItemHandle getElementHandle() {
		return element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.GridRowBandAdapter#getRowCount()
	 */
	protected int getRowCount() {
		return element.getRows().getCount();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.GridRowBandAdapter#getColumnCount()
	 */
	protected int getColumnCount() {
		return element.getColumnCount();
	}

}
