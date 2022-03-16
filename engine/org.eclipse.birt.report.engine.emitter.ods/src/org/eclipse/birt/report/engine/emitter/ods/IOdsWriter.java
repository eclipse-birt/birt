/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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
package org.eclipse.birt.report.engine.emitter.ods;

import java.io.IOException;
import java.util.HashMap;

import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.odf.style.StyleEntry;

public interface IOdsWriter {

	void start(IReportContent report, HashMap<String, BookmarkDef> bookmarkList) throws IOException;

	void end() throws IOException;

	void startSheet(StyleEntry tableStyle, StyleEntry[] colStyles, String sheetName) throws IOException;

	void startSheet(String sheetName) throws IOException;

	void endSheet();

	void startRow(StyleEntry rowStyle);

	void startRow();

	void endRow();

	void outputData(SheetData data, StyleEntry style, int column, int colSpan) throws IOException;

	void outputData(int col, int row, int type, Object value);

	String defineName(String cells);
}
