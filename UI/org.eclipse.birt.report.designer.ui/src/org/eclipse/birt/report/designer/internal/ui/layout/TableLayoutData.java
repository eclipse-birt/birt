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

package org.eclipse.birt.report.designer.internal.ui.layout;

import java.awt.Insets;

import org.eclipse.birt.report.designer.core.model.schematic.ColumnHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.RowHandleAdapter;

/**
 * The layout data for cells, rows and columns
 * 
 * @author Dazhen Gao
 * @version $Revision: #1 $ $Date: 2005/01/24 $
 */
public class TableLayoutData {

	/**
	 * Keeps the layout data for cells when initilized from model.
	 * 
	 * @author Dazhen Gao
	 * @version $Revision: #1 $ $Date: 2005/01/24 $
	 */
	public static class CellData {

		public int rowNumber;

		public int columnNumber;

		public int rowSpan = 1;

		public int columnSpan = 1;

		public Insets insets;
	}

	/**
	 * Keeps the layout data for rows when initilized from model.
	 * 
	 * @author Dazhen Gao
	 * @version $Revision: #1 $ $Date: 2005/01/24 $
	 */
	public static class RowData {

		public int rowNumber;

		public int height;

		public double percentageHeight;

		public boolean isPercentage;

		public boolean isAuto;

		public boolean isForce;

		public int minRowHeight = RowHandleAdapter.DEFAULT_MINHEIGHT;

		public int trueMinRowHeight = RowHandleAdapter.DEFAULT_MINHEIGHT;

		public boolean isSetting;
	}

	/**
	 * Keeps the layout data for columns when initilized from model.
	 * 
	 * @author Dazhen Gao
	 * @version $Revision: #1 $ $Date: 2005/01/24 $
	 */
	public static class ColumnData {

		public int columnNumber;

		public int width;

		public double percentageWidth;

		public boolean isPercentage;

		public boolean isAuto;

		public boolean isForce;

		public int minColumnWidth = ColumnHandleAdapter.DEFAULT_MINWIDTH;

		public int trueMinColumnWidth = ColumnHandleAdapter.DEFAULT_MINWIDTH;

		public boolean isSetting;
	}
}
