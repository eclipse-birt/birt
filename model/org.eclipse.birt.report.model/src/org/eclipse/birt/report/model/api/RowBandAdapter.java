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

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.TableRow;

/**
 * Represents an object for copy/paste in Gird/Table. The copy/paste between
 * Grid/Table must follow the following rules:
 *
 * <ul>
 * <li>Copy/paste operations must occur among the same type of elements, like
 * among grid elements. A copy/paste operation between Grid/Table is not
 * allowed.
 * <li>Current copy/paste operations do not support cells with "drop"
 * properties.
 * <li>Each time, only one row can be copied/pasted.
 * <li>Slot layouts between the source grid/table and the target grid/table must
 * be same.
 * </ul>
 *
 */

public abstract class RowBandAdapter {

	/**
	 * Returns the element where the copy/paste operation occurs.
	 *
	 * @return the element
	 */

	protected abstract ReportItemHandle getElementHandle();

	/**
	 * Returns the module where the element belongs to.
	 *
	 * @return the module
	 */

	protected Module getModule() {
		return getElementHandle().getModule();
	}

	/**
	 * Returns the number of columns in the element.
	 *
	 * @return the number of columns in the element
	 */

	protected abstract int getColumnCount();

	/**
	 * Returns count of rows.
	 *
	 * @return count of rows.
	 */
	protected abstract int getRowCount();

	/**
	 * Computes column count in one row.
	 *
	 * @param row
	 * @return column count in one row.
	 */
	protected int computeRowCount(TableRow row) {
		List contents = row.getContentsSlot();
		Iterator cellIter = contents.iterator();
		int count = 0;
		while (cellIter.hasNext()) {
			Cell cell = (Cell) cellIter.next();
			int columnSpan = cell.getColSpan(null);
			count = count + columnSpan;
		}
		return count;
	}

	/**
	 * Computes column count in one row.
	 *
	 * @param rowHandle
	 * @return column count in one row.
	 */
	protected int computeRowCount(RowHandle rowHandle) {
		TableRow row = (TableRow) rowHandle.getElement();
		return computeRowCount(row);
	}

	/**
	 * Checks element has parent or not.
	 *
	 * @return <code>true</code>if has parent, else return <code>false</code>
	 */

	protected boolean hasParent() {
		if (getElementHandle().getElement().isVirtualElement() || (getElementHandle().getExtends() != null)) {
			return true;
		}
		return false;
	}

}
