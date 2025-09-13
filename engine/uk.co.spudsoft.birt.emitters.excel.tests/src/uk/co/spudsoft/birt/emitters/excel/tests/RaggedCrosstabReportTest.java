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
import org.junit.Ignore;
import org.junit.Test;

public class RaggedCrosstabReportTest extends ReportRunner {

	@Test
	@Ignore // FIXME
	public void testRunReport() throws BirtException, IOException {

		InputStream inputStream = runAndRenderReport("RaggedCrosstab.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {

			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());
			assertEquals("Ragged Crosstab Report", workbook.getSheetAt(0).getSheetName());

			Sheet sheet = workbook.getSheetAt(0);
			assertEquals(9, firstNullRow(sheet));
		} finally {
			inputStream.close();
		}
	}
}
