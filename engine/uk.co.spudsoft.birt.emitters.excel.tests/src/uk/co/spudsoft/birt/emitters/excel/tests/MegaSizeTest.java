/*************************************************************************************
 * Copyright (c) 2011, 2012, 2013 James Talbut.
 *  jim-emitters@spudsoft.co.uk
 *
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     James Talbut - Initial implementation.
 ************************************************************************************/

package uk.co.spudsoft.birt.emitters.excel.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.birt.core.exception.BirtException;
import org.junit.Ignore;
import org.junit.Test;

public class MegaSizeTest extends ReportRunner {

	@Test
	@Ignore // FIXME
	public void testWarmup() throws BirtException, IOException {

		debug = false;
		InputStream inputStream = runAndRenderReport("MegaSize.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {

		} finally {
			inputStream.close();
		}
	}

	@Test
	@Ignore // FIXME
	public void testMegaXlsx() throws BirtException, IOException {

		debug = false;
		InputStream inputStream = runAndRenderReport("MegaSize.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {

		} finally {
			inputStream.close();
		}
	}

	@Test
	@Ignore // FIXME
	public void testMegaXls() throws BirtException, IOException {

		try {
			runAndRenderReport("MegaSize.rptdesign", "xls");
			fail("Should have failed!");
		} catch (Throwable ex) {
			assertEquals("Error happened while running the report.", ex.getMessage());
			ex = ex.getCause();
			assertEquals("Invalid row number (65536) outside allowable range (0..65535)", ex.getMessage());
		}
	}

}
