/*******************************************************************************
 * Copyright (c) 2004, 2008Actuate Corporation.
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
/**
 * 
 */
/*******************************************************************************
 * Copyright (c) 2004, 2008Actuate Corporation.
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

package org.eclipse.birt.report.engine.emitter.excel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.engine.content.IReportContent;

public interface IExcelWriter {

	public void start(IReportContent report, Map<StyleEntry, Integer> styles,
			// TODO: style ranges.
			// List<ExcelRange> styleRanges,
			HashMap<String, BookmarkDef> bookmarkList) throws IOException;

	public void end() throws IOException;

	public void startSheet(double[] coordinates, String pageHeader, String pageFooter, String sheetName)
			throws IOException;

	public void startSheet(String sheetName) throws IOException;

	public void endSheet(double[] coordinates, String orientation, int pageWidth, int pageHeight, float leftMargin,
			float rightMargin, float topMargin, float bottomMargin);

	public void endSheet();

	public void startRow(double rowHeight);

	public void startRow();

	public void endRow();

	public void outputData(SheetData data, StyleEntry style, int column, int colSpan) throws IOException;

	public void outputData(String sheet, SheetData data, StyleEntry style, int column, int colSpan) throws IOException;

	void outputData(int col, int row, int type, Object value);

	String defineName(String cells);
}
