/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	public void start(IReportContent report, HashMap<String, BookmarkDef> bookmarkList) throws IOException;

	public void end() throws IOException;

	public void startSheet(StyleEntry tableStyle, StyleEntry[] colStyles, String sheetName) throws IOException;

	public void startSheet(String sheetName) throws IOException;

	public void endSheet();

	public void startRow(StyleEntry rowStyle);

	public void startRow();

	public void endRow();

	public void outputData(SheetData data, StyleEntry style, int column, int colSpan) throws IOException;

	void outputData(int col, int row, int type, Object value);

	String defineName(String cells);
}
