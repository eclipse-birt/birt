/*******************************************************************************
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

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.model;

import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;

/**
 * Factory to create the crosstab cell adapter
 */
public interface ICrosstabCellAdapterFactory {

	// for the level handel to creat the cell handle adapter, maybe there are
	// different cell adapter between
	// the first level handle and other level handl
	public static final String CELL_LEVEL_HANDLE = "level_handle";//$NON-NLS-1$

	public static final String CELL_FIRST_LEVEL_HANDLE = "first_level_handle";//$NON-NLS-1$

	// for the sub total to create the cell handle adapter
	public static final String CELL_SUB_TOTAL = "sub_total";//$NON-NLS-1$

	// for the grand total to create the cell handle adapter
	public static final String CELL_GRAND_TOTAL = "grand_total";//$NON-NLS-1$

	// for the mesure header to create the cell handle adapter, maybe some
	// measure cell adapter
	// share the one AbstractCrosstabItemHandle
	public static final String CELL_MEASURE_HEADER = "measure_header";//$NON-NLS-1$

	public static final String CROSSTAB_HEADER = "crosstab_header";//$NON-NLS-1$

	public static final String CELL_MEASURE = "cell_measure";//$NON-NLS-1$

	public static final String CELL_MEASURE_AGGREGATION = "measure_aggregation";//$NON-NLS-1$

	// If the AbstractCrosstabItemHandle is null, creat the virtual cell
	// adapter. it has the different
	// area ( see the VirtualCrosstabCellAdapter)
	public static final String CELL_ROW_VIRTUAL = "row_virtual";//$NON-NLS-1$

	public static final String CELL_COLUMN_VIRTUAL = "column_virtual";//$NON-NLS-1$

	public static final String CELL_MEASURE_VIRTUAL = "measure_virtual";//$NON-NLS-1$

	/**
	 * Create the CrosstabCellAdapter
	 * 
	 * @return
	 */
	public CrosstabCellAdapter createCrosstabCellAdapter(String type, CrosstabCellHandle handle, int rowNumber,
			int rowSpan, int columnNumber, int columnSpan, boolean isConvert);
}
