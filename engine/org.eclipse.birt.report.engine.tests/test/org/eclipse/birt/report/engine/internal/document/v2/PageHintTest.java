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

package org.eclipse.birt.report.engine.internal.document.v2;

import java.util.ArrayList;

import org.eclipse.birt.core.archive.FileArchiveReader;
import org.eclipse.birt.core.archive.FileArchiveWriter;
import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.presentation.IPageHint;
import org.eclipse.birt.report.engine.presentation.InstanceIndex;
import org.eclipse.birt.report.engine.presentation.PageHint;
import org.eclipse.birt.report.engine.presentation.PageSection;

public class PageHintTest extends EngineCase {

	final static String REPORT_DOCUMENT_NAME = ".internal.test.rptdocument";
	final static String REPORT_DOCUMENT_V0 = "org/eclipse/birt/report/engine/internal/document/v2/pagehint_v0.rptdocument";

	@Override
	public void setUp() {
		removeFile(REPORT_DOCUMENT_NAME);
	}

	@Override
	public void tearDown() {
		removeFile(REPORT_DOCUMENT_NAME);
	}

	public void testReadWrite() throws Exception {
		doWrite();
		doReadV1();
	}

	public void testReadeV0() throws Exception {
		copyResource(REPORT_DOCUMENT_V0, REPORT_DOCUMENT_NAME);
		doReadV0();
	}

	protected void doWrite() throws Exception {
		FileArchiveWriter archive = new FileArchiveWriter(REPORT_DOCUMENT_NAME);
		PageHintWriterV2 hintWriter = new PageHintWriterV2(archive);
		ArrayList hints = createPageHintV1();
		for (int i = 0; i < hints.size(); i++) {
			hintWriter.writePageHint((PageHint) hints.get(i));
		}
		hintWriter.writeTotalPage(hints.size());
		hintWriter.close();
		archive.finish();
	}

	protected void doReadV1() throws Exception {
		FileArchiveReader archive = new FileArchiveReader(REPORT_DOCUMENT_NAME);
		PageHintReaderV2 reader = new PageHintReaderV2(archive);
		long pageNumber = reader.getTotalPage();
		ArrayList hints = new ArrayList();
		for (int i = 1; i <= pageNumber; i++) {
			hints.add(reader.getPageHint(i));
		}
		reader.close();
		archive.close();
		checkPageHintV1(hints);
	}

	protected ArrayList createPageHintV1() {
		ArrayList hints = new ArrayList();
		for (int i = 0; i < 2; i++) {
			long pageNumber = i + 1;
			PageHint hint = new PageHint(pageNumber, pageNumber * 100);
			PageSection section = new PageSection();
			section.starts = new InstanceIndex[] { new InstanceIndex(InstanceID.parse("/3"), 100) };
			section.ends = new InstanceIndex[] { new InstanceIndex(InstanceID.parse("/4"), 100) };
			hint.addSection(section);
			hints.add(hint);
		}
		return hints;
	}

	protected void checkPageHintV1(ArrayList hints) {
		assertEquals(2, hints.size());
		for (int i = 0; i < 2; i++) {
			long pageNumber = i + 1;
			PageHint hint = (PageHint) hints.get(i);
			assertEquals(pageNumber, hint.getPageNumber());
			assertEquals(pageNumber * 100, hint.getOffset());
			assertEquals(1, hint.getSectionCount());
			PageSection section = hint.getSection(0);
			assertEquals("/3", section.starts[0].getInstanceID().toString());
			assertEquals(100, section.starts[0].getOffset());
			assertEquals("/4", section.ends[0].getInstanceID().toString());
			assertEquals(100, section.ends[0].getOffset());
		}
	}

	protected void doReadV0() throws Exception {
		FileArchiveReader archive = new FileArchiveReader(REPORT_DOCUMENT_NAME);
		PageHintReaderV2 reader = new PageHintReaderV2(archive);
		long pageNumber = reader.getTotalPage();
		assertEquals(pageNumber, 2);
		IPageHint hint = reader.getPageHint(1);
		checkPageHintV0(hint, 1, 0, 0, 500);
		hint = reader.getPageHint(2);
		checkPageHintV0(hint, 2, 200, 600, 1000);

		reader.close();
		archive.close();
	}

	protected void checkPageHintV0(IPageHint hint, long number, long offset, long start, long end) {
		assertTrue(hint != null);
		assertEquals(number, hint.getPageNumber());
		assertEquals(offset, hint.getOffset());
		assertEquals(1, hint.getSectionCount());
		assertEquals(start, hint.getSectionStart(0));
		assertEquals(end, hint.getSectionEnd(0));
	}
}
