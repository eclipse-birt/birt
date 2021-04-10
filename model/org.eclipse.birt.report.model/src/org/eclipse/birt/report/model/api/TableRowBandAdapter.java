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
 * tables.
 */

public class TableRowBandAdapter extends RowBandAdapter {

	/**
	 * The element where the copy/paste operation occurs.
	 */

	protected TableHandle element;

	TableRowBandAdapter() {
	}

	TableRowBandAdapter(TableHandle element) {
		this.element = element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.TableRowBandAdapter#getElementHandle()
	 */

	protected ReportItemHandle getElementHandle() {
		return element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.TableRowBandAdapter#getColumnCount()
	 */

	protected int getColumnCount() {
		return element.getColumnCount();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.TableRowBandAdapter#getRowCount()
	 */

	protected int getRowCount() {
		// treat the table as a regular layout.

		int numOfRows = 0;
		numOfRows += element.getHeader().getCount();

		SlotHandle groups = element.getGroups();
		for (int i = 0; i < groups.getCount(); i++) {
			GroupHandle group = (GroupHandle) groups.get(i);
			numOfRows += group.getHeader().getCount();
			numOfRows += group.getFooter().getCount();
		}

		numOfRows += element.getDetail().getCount();
		numOfRows += element.getFooter().getCount();

		return numOfRows;
	}

}
