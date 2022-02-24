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

package org.eclipse.birt.report.engine.internal.executor.doc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;

public class ReportPageReaderTest extends EngineCase {
	/**
	 * Test pagination mechanism works when HTMLRenderOption is set as
	 * following:<br>
	 * <table border="solid:1px">
	 * <tr>
	 * <td>htmlPagination=true<br>
	 * masterPageContent=true</td>
	 * <td>Every pages is displayed and master page header and footer are<br>
	 * displayed around each page.</td>
	 * </tr>
	 * <tr>
	 * <td>htmlPagination=true<br>
	 * masterPageContent=false</td>
	 * <td>Every pages is displayed and between horizontal lines are<br>
	 * displayed page.</td>
	 * </tr>
	 * <tr>
	 * <td>htmlPagination=false<br>
	 * masterPageContent=true</td>
	 * <td>All items in specifed pages are displayed as they are in one page<br>
	 * and master page header is only displayed before all the items, master<br>
	 * page footer is only displayed after all items.</td>
	 * </tr>
	 * <tr>
	 * <td>htmlPagination=false<br>
	 * masterPageContent=false</td>
	 * <td>All items in specifed pages are displayed as they are in one page<br>
	 * and no master page content and horizontal lines are displayed.</td>
	 * </tr>
	 * </table>
	 * 
	 * @throws EngineException
	 * @throws IOException
	 */
	public void testHtmlPagination() throws EngineException, IOException {
		String designFile = "org/eclipse/birt/report/engine/internal/executor/doc/report-page-reader.xml";
		String result = getRenderResult(designFile, true, true);
		assertEquals(2, getOccurCount(result, "pageHeader"));
		assertEquals(2, getOccurCount(result, "pageFooter"));
		assertEquals(2, getOccurCount(result, "tableHeader"));
		assertEquals(1, getOccurCount(result, "tableFooter"));
		assertEquals(1, getOccurCount(result, "itemA"));
		assertEquals(1, getOccurCount(result, "itemB"));

		result = getRenderResult(designFile, true, false);
		assertEquals(0, getOccurCount(result, "pageHeader"));
		assertEquals(0, getOccurCount(result, "pageFooter"));
		assertEquals(1, getOccurCount(result, "<hr>"));
		assertEquals(2, getOccurCount(result, "tableHeader"));
		assertEquals(1, getOccurCount(result, "tableFooter"));
		assertEquals(1, getOccurCount(result, "itemA"));
		assertEquals(1, getOccurCount(result, "itemB"));

		result = getRenderResult(designFile, false, true);
		assertEquals(1, getOccurCount(result, "pageHeader"));
		assertEquals(1, getOccurCount(result, "pageFooter"));
		assertEquals(1, getOccurCount(result, "tableHeader"));
		assertEquals(1, getOccurCount(result, "tableFooter"));
		assertEquals(1, getOccurCount(result, "itemA"));
		assertEquals(1, getOccurCount(result, "itemB"));

		result = getRenderResult(designFile, false, false);
		assertEquals(0, getOccurCount(result, "pageHeader"));
		assertEquals(0, getOccurCount(result, "pageFooter"));
		assertEquals(0, getOccurCount(result, "<hr>"));
		assertEquals(1, getOccurCount(result, "tableHeader"));
		assertEquals(1, getOccurCount(result, "tableFooter"));
		assertEquals(1, getOccurCount(result, "itemA"));
		assertEquals(1, getOccurCount(result, "itemB"));
	}

	private int getOccurCount(String string, String subString) {
		if (subString == null) {
			return 0;
		}
		String container = string;
		String match = subString;
		int count = 0;
		int index = -1;
		while ((index = container.indexOf(match)) >= 0) {
			++count;
			container = container.substring(index + subString.length());
		}
		return count;
	}

	private String getRenderResult(String designFile, boolean pagination, boolean showMasterPage)
			throws EngineException, IOException {
		HTMLRenderOption options = new HTMLRenderOption();
		options.setHtmlPagination(pagination);
		options.setMasterPageContent(showMasterPage);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		options.setOutputStream(out);
		render(designFile, options);
		String content = new String(out.toByteArray());
		out.close();
		return content;
	}
}
