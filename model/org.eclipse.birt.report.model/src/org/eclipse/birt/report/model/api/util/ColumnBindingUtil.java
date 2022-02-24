/*******************************************************************************
 * Copyright (c) 2004 - 2011 Actuate Corporation.
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

package org.eclipse.birt.report.model.api.util;

import java.util.List;

import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.util.BoundDataColumnUtil;

/**
 * The utility class for bound data columns migration.
 */

public class ColumnBindingUtil {

	/**
	 * Binds a column to the given element. The column must have a name. The method
	 * will not generate a name for the binding. An exception will be thrown when
	 * trying to bind a column without a name. If the column has been bound, the
	 * exist bound column will be returned. If the column doesn't exist but has a
	 * duplicated name with exist columns, a new unique name will be assigned.
	 * 
	 * @param handle the handle of the element to bind
	 * @param column the column to bind
	 * @return the handle of the bound column
	 * @throws SemanticException
	 */
	public static ComputedColumnHandle addColumnBinding(ReportItemHandle handle, ComputedColumn column)
			throws SemanticException {
		if (handle == null || column == null)
			return null;

		PropertyHandle propHandle = handle.getPropertyHandle(IReportItemModel.BOUND_DATA_COLUMNS_PROP);
		List<ComputedColumn> columns = (List<ComputedColumn>) propHandle.getValue();
		if (columns != null) {
			ComputedColumn matchedColumn = BoundDataColumnUtil.getColumn(columns, column);
			if (matchedColumn == null) {
				String name = column.getName();
				if (!StringUtil.isEmpty(name)) {
					String uniqueName = BoundDataColumnUtil.makeUniqueName(handle, name, column);
					if (!name.equals(uniqueName))
						column.setName(uniqueName);
				}
			} else
				return (ComputedColumnHandle) matchedColumn.handle(propHandle, columns.indexOf(matchedColumn));
		}
		return (ComputedColumnHandle) propHandle.addItem(column);
	}
}
