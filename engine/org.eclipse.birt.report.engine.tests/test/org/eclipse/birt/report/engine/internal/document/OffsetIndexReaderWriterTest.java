/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.internal.document;

import java.io.IOException;

import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.engine.internal.document.OffsetIndexReader;
import org.eclipse.birt.report.engine.internal.document.OffsetIndexWriter;

public class OffsetIndexReaderWriterTest extends EngineCase {

	final static String INDEX_STREAM = "org.eclipse.birt.report.engine.OffsetIndexReaderWriterTest.index";

	public void setUp() {
		removeFile(INDEX_STREAM);
	}

	public void tearDown() {
		removeFile(INDEX_STREAM);
	}

	public void testReadWrite() {
		try {
			doCreateIndex();
			doReadIndex();
		} catch (IOException ex) {
			ex.printStackTrace();
			fail();
		}
	}

	protected void doCreateIndex() throws IOException {
		OffsetIndexWriter writer = new OffsetIndexWriter(INDEX_STREAM);
		writer.open();
		writer.write(0, 0);
		writer.write(10, 10);
		writer.write(20, 20);
		writer.write(30, 30);
		writer.write(40, 40);
		writer.close();
	}

	protected void doReadIndex() throws IOException {
		OffsetIndexReader reader = new OffsetIndexReader(INDEX_STREAM);
		reader.open();
		assertEquals(-1, reader.find(-1));
		assertEquals(0, reader.find(0));
		assertEquals(10, reader.find(10));
		assertEquals(20, reader.find(20));
		assertEquals(30, reader.find(30));
		assertEquals(40, reader.find(40));
		assertEquals(-1, reader.find(90));
		reader.close();
	}

}
