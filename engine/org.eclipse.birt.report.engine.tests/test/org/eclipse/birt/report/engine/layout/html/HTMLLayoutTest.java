/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.layout.html;

import java.util.List;

import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.TOCNode;

public class HTMLLayoutTest extends EngineCase {

	/**
	 * Tests a container will be output in the same page as the first non-container
	 * descent. Refer to
	 * <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=157411">Bugzilla bug
	 * 157411</a>
	 * 
	 * @throws EngineException
	 */
	public void testPageBreak() throws EngineException {
		String designFile = "org/eclipse/birt/report/engine/layout/html/HTMLLayoutTest_1.xml";
		IReportDocument document = createReportDocument(designFile);
		assertEquals(1l, getPageNumber(document, "Australia"));
		assertEquals(1l, getPageNumber(document, "France"));
		assertEquals(2l, getPageNumber(document, "Japan"));
		assertEquals(2l, getPageNumber(document, "UK"));
		assertEquals(3l, getPageNumber(document, "USA"));
		document.close();
	}

	/**
	 * Tests a container will be output in the same page as the first non-container
	 * descent. Refer to
	 * <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=157411">Bugzilla bug
	 * 157411</a>
	 * 
	 * @throws EngineException
	 */
	public void testPageBreak2() throws EngineException {
		String designFile = "org/eclipse/birt/report/engine/layout/html/HTMLLayoutTest_2.xml";
		IReportDocument document = createReportDocument(designFile);
		checkBookmark(document, "Australia", 2);
		checkBookmark(document, "France", 3);
		checkBookmark(document, "Japan", 4);
		checkBookmark(document, "UK", 5);
		checkBookmark(document, "USA", 6);
		document.close();
	}

	/**
	 * Check there are 2 bookmarks with name <code>bookmark</code> int the document.
	 * And there are both in the specified page.
	 * 
	 * @param document
	 * @param bookmark
	 */
	private void checkBookmark(IReportDocument document, String bookmark, long pageNumber) {
		List tocs = document.findTOCByName(bookmark);
		assertEquals(2, tocs.size());
		TOCNode toc0 = (TOCNode) tocs.get(0);
		TOCNode toc1 = (TOCNode) tocs.get(1);
		long pageNumber0 = document.getPageNumber(toc0.getNodeID());
		long pageNumber1 = document.getPageNumber(toc1.getNodeID());
		assertEquals(pageNumber, pageNumber0);
		assertEquals(pageNumber, pageNumber1);
	}

	private long getPageNumber(IReportDocument document, String bookmark) {
		List tocs = document.findTOCByName(bookmark);
		TOCNode toc = (TOCNode) tocs.get(0);
		return document.getPageNumber(toc.getBookmark());
	}
}
