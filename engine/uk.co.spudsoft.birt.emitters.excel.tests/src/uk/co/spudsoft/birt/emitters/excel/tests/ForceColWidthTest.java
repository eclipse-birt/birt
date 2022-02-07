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

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.birt.core.exception.BirtException;
import org.junit.Test;

public class ForceColWidthTest extends ReportRunner {

	@Test
	public void testRunReportXlsx() throws BirtException, IOException {

		InputStream inputStream = runAndRenderReport("GappyData.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {

			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(2, workbook.getNumberOfSheets());
			assertEquals("Summary", workbook.getSheetAt(0).getSheetName());
			assertEquals("Data", workbook.getSheetAt(1).getSheetName());

			Sheet summary = workbook.getSheetAt(0);

			assertEquals(5522, summary.getColumnWidth(0));
			assertEquals(1353, summary.getColumnWidth(1));
			assertEquals(529, summary.getColumnWidth(2));
			assertEquals(773, summary.getColumnWidth(3));
			assertEquals(773, summary.getColumnWidth(4));
			assertEquals(0, summary.getColumnWidth(5));
			assertEquals(0, summary.getColumnWidth(6));
			assertEquals(3437, summary.getColumnWidth(7));
			assertEquals(3437, summary.getColumnWidth(8));
			assertEquals(3437, summary.getColumnWidth(9));
			assertEquals(3437, summary.getColumnWidth(10));
			assertEquals(3437, summary.getColumnWidth(11));

			Sheet data = workbook.getSheetAt(1);

			assertEquals(4460, data.getColumnWidth(0));
			assertEquals(1353, data.getColumnWidth(1));
			assertEquals(538, data.getColumnWidth(2));
			assertEquals(773, data.getColumnWidth(3));
			assertEquals(773, data.getColumnWidth(4));
			assertEquals(0, data.getColumnWidth(5));
			assertEquals(0, data.getColumnWidth(6));
			assertEquals(2048, data.getColumnWidth(7));
			assertEquals(2048, data.getColumnWidth(8));
			assertEquals(2048, data.getColumnWidth(9));
			assertEquals(2048, data.getColumnWidth(10));
			assertEquals(2048, data.getColumnWidth(11));

		} finally {
			inputStream.close();
		}
	}

}
