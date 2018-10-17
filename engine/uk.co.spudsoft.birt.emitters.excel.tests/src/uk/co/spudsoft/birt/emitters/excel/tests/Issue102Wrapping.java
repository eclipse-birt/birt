package uk.co.spudsoft.birt.emitters.excel.tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.birt.core.exception.BirtException;
import org.junit.Test;

public class Issue102Wrapping extends ReportRunner {

	@Test
	public void testRunReport() throws BirtException, IOException {

		removeEmptyRows = false;
		InputStream inputStream = runAndRenderReport("Issue102Wrapping.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);
			
			assertEquals( 1, workbook.getNumberOfSheets() );
			
			Sheet sheet = workbook.getSheetAt(0);
			
			assertEquals( "Sheet0", sheet.getSheetName());
			assertEquals( 4231, sheet.getColumnWidth(0) );
			
			assertTrue( sheet.getRow(0).getCell(0).getCellStyle().getWrapText());
			assertTrue( sheet.getRow(1).getCell(0).getCellStyle().getWrapText());

			assertTrue( ! sheet.getRow(2).getCell(0).getCellStyle().getWrapText());
			assertTrue( ! sheet.getRow(3).getCell(0).getCellStyle().getWrapText());
			assertTrue( ! sheet.getRow(4).getCell(0).getCellStyle().getWrapText());
			
			assertEquals( 300, sheet.getRow(0).getHeight() );
			assertEquals( 300, sheet.getRow(2).getHeight() );
			
			assertEquals( 116, firstNullRow(sheet));
		} finally {
			inputStream.close();
		}
	}

}
