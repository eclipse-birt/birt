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
package org.eclipse.birt.report.engine.emitter.odt;

import java.io.IOException;
import java.util.List;

import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.emitter.odt.OdtEmitter.InlineFlag;
import org.eclipse.birt.report.engine.emitter.odt.OdtEmitter.TextFlag;
import org.eclipse.birt.report.engine.odf.DiagonalLineInfo;
import org.eclipse.birt.report.engine.odf.IOdfWriter;
import org.eclipse.birt.report.engine.odf.style.HyperlinkInfo;
import org.eclipse.birt.report.engine.odf.style.StyleEntry;

public interface IOdtWriter extends IOdfWriter {
	void drawDiagonalLine(DiagonalLineInfo diagonalLineInfo);

	void drawImage(String uri, double height, double width, HyperlinkInfo hyper, StyleEntry style, StyleEntry pStyle,
			InlineFlag inlineFlag, String altText, String bookmark, TocInfo tocInfo);

	void end() throws IOException;

	void endPage();

	void endParagraph();

	void insertHiddenParagraph();

	void start(boolean rtl) throws IOException;

	void startPage();

	/**
	 * Output an empty paragraph with the passed bookmark list.
	 *
	 * @param bm bookmark names list
	 */
	void writeMarkersParagraph(List<String> bm, List<TocInfo> tableTocs);

	void writeCaption(String txt, StyleEntry style);

	void writeContent(int type, String txt, StyleEntry style, StyleEntry inlineStyle, String fontFamily,
			HyperlinkInfo info, InlineFlag inlineFlag, TextFlag flag, int paragraphWidth, boolean runIsRtl,
			List<String> bookmark, List<TocInfo> tocs);

	void writeForeign(IForeignContent foreignContent);

	void writeTOC(TocInfo tocInfo);

	void writeTableToc(List<TocInfo> tableTocs);
}
