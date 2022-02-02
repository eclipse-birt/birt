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
package org.eclipse.birt.report.engine.odf;

import org.eclipse.birt.report.engine.odf.style.StyleEntry;

public interface IOdfWriter {

	void endTable();

	void endTableCell();

	void endTableHeader();

	void endTableRow();

	void startTable(String name, StyleEntry style);

	void startTableCell(StyleEntry cellStyle, SpanInfo info);

	void startTableHeader();

	void startTableRow(StyleEntry rowStyle);

	void writeAutoText(int type);

	void writeColumn(StyleEntry[] colStyles);

	void writeSpanCell(SpanInfo info);

	void writeString(String txt);

	void writeEmptyCell();

	void close() throws Exception;

	public abstract void endTableRowGroup();

	public abstract void startTableRowGroup();
}
