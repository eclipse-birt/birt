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

import java.io.InputStream;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

public class Issue55GroupsNotWorkingCorrectly extends ReportRunner {

	@Test
	public void testHeader() throws Exception {

		debug = false;
		groupSummaryHeader = true;
		InputStream inputStream = runAndRenderReport("Issue55.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());

			XSSFSheet sheet0 = workbook.getSheetAt(0);

			assertEquals(!groupSummaryHeader, sheet0.getRowSumsBelow());

			assertEquals(0, sheet0.getRow(0).getCTRow().getOutlineLevel());
			assertEquals(0, sheet0.getRow(1).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(2).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(3).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(4).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(5).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(6).getCTRow().getOutlineLevel());
			assertEquals(0, sheet0.getRow(7).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(8).getCTRow().getOutlineLevel());

		} finally {
			inputStream.close();
		}

	}

	@Test
	public void testFooter() throws Exception {

		debug = false;
		groupSummaryHeader = false;
		InputStream inputStream = runAndRenderReport("Issue55.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());
			assertEquals(!groupSummaryHeader, workbook.getSheetAt(0).getRowSumsBelow());

			XSSFSheet sheet0 = workbook.getSheetAt(0);

			assertEquals(!groupSummaryHeader, sheet0.getRowSumsBelow());

			assertEquals(0, sheet0.getRow(0).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(1).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(2).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(3).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(4).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(5).getCTRow().getOutlineLevel());
			assertEquals(0, sheet0.getRow(6).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(7).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(8).getCTRow().getOutlineLevel());

		} finally {
			inputStream.close();
		}

	}

	@Test
	public void testHeaderHierarchy() throws Exception {

		debug = false;
		groupSummaryHeader = true;
		InputStream inputStream = runAndRenderReport("Issue55GroupHierarchy.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());

			XSSFSheet sheet0 = workbook.getSheetAt(0);

			assertEquals(!groupSummaryHeader, sheet0.getRowSumsBelow());

			/*
			 * for( int i = 0; i < 64; ++i ) { System.out.println( "assertEquals( " +
			 * sheet0.getRow( i ).getCTRow().getOutlineLevel() + ", sheet0.getRow( " + i +
			 * " ).getCTRow().getOutlineLevel() );" ); }
			 */
			assertEquals(0, sheet0.getRow(0).getCTRow().getOutlineLevel());
			assertEquals(0, sheet0.getRow(1).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(2).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(3).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(4).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(5).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(6).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(7).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(8).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(9).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(10).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(11).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(12).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(13).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(14).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(15).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(16).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(17).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(18).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(19).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(20).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(21).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(22).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(23).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(24).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(25).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(26).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(27).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(28).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(29).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(30).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(31).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(32).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(33).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(34).getCTRow().getOutlineLevel());
			assertEquals(0, sheet0.getRow(35).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(36).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(37).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(38).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(39).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(40).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(41).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(42).getCTRow().getOutlineLevel());
			assertEquals(0, sheet0.getRow(43).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(44).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(45).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(46).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(47).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(48).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(49).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(50).getCTRow().getOutlineLevel());
			assertEquals(0, sheet0.getRow(51).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(52).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(53).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(54).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(55).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(56).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(57).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(58).getCTRow().getOutlineLevel());
			assertEquals(0, sheet0.getRow(59).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(60).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(61).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(62).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(63).getCTRow().getOutlineLevel());

		} finally {
			inputStream.close();
		}

	}

	@Test
	public void testFooterHierarchyWithHeader() throws Exception {

		/*
		 * Note that the results of this test are a yucky mess
		 *
		 * Excel groups with a summary row above or below the data rows. Each group has
		 * to have a distinct summary row - if you have two groups that try to end with
		 * the same summary row Excel represents them like the image on the right (you
		 * can still hide with the controls on the top of the grouping column, but not
		 * with the individual expand/collapse controls).
		 *
		 * You can do one of three things:
		 *
		 * Give each group level a header row and set ExcelEmitter.GroupSummaryHeader.
		 * Give each group level a footer row and don't set
		 * ExcelEmitter.GroupSummaryHeader. Accept that you are going to get something
		 * odd :)
		 *
		 */

		debug = false;
		groupSummaryHeader = false;
		InputStream inputStream = runAndRenderReport("Issue55GroupHierarchy.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());
			assertEquals(!groupSummaryHeader, workbook.getSheetAt(0).getRowSumsBelow());

			XSSFSheet sheet0 = workbook.getSheetAt(0);

			assertEquals(!groupSummaryHeader, sheet0.getRowSumsBelow());

			/*
			 * for( int i = 0; i < 64; ++i ) { System.out.println( "assertEquals( " +
			 * sheet0.getRow( i ).getCTRow().getOutlineLevel() + ", sheet0.getRow( " + i +
			 * " ).getCTRow().getOutlineLevel() );" ); }
			 */
			assertEquals(0, sheet0.getRow(0).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(1).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(2).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(3).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(4).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(5).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(6).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(7).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(8).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(9).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(10).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(11).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(12).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(13).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(14).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(15).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(16).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(17).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(18).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(19).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(20).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(21).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(22).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(23).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(24).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(25).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(26).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(27).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(28).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(29).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(30).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(31).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(32).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(33).getCTRow().getOutlineLevel());
			assertEquals(0, sheet0.getRow(34).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(35).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(36).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(37).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(38).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(39).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(40).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(41).getCTRow().getOutlineLevel());
			assertEquals(0, sheet0.getRow(42).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(43).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(44).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(45).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(46).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(47).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(48).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(49).getCTRow().getOutlineLevel());
			assertEquals(0, sheet0.getRow(50).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(51).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(52).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(53).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(54).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(55).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(56).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(57).getCTRow().getOutlineLevel());
			assertEquals(0, sheet0.getRow(58).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(59).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(60).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(61).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(62).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(63).getCTRow().getOutlineLevel());

		} finally {
			inputStream.close();
		}

	}

	@Test
	public void testFooterHierarchy() throws Exception {

		debug = false;
		groupSummaryHeader = false;
		InputStream inputStream = runAndRenderReport("Issue55GroupHierarchyBelow.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());
			assertEquals(!groupSummaryHeader, workbook.getSheetAt(0).getRowSumsBelow());

			XSSFSheet sheet0 = workbook.getSheetAt(0);

			assertEquals(!groupSummaryHeader, sheet0.getRowSumsBelow());

			/*
			 * for( int i = 0; i < 64; ++i ) { System.out.println( "assertEquals( " +
			 * sheet0.getRow( i ).getCTRow().getOutlineLevel() + ", sheet0.getRow( " + i +
			 * " ).getCTRow().getOutlineLevel() );" ); }
			 */
			assertEquals(0, sheet0.getRow(0).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(1).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(2).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(3).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(4).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(5).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(6).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(7).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(8).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(9).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(10).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(11).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(12).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(13).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(14).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(15).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(16).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(17).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(18).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(19).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(20).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(21).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(22).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(23).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(24).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(25).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(26).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(27).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(28).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(29).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(30).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(31).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(32).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(33).getCTRow().getOutlineLevel());
			assertEquals(0, sheet0.getRow(34).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(35).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(36).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(37).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(38).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(39).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(40).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(41).getCTRow().getOutlineLevel());
			assertEquals(0, sheet0.getRow(42).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(43).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(44).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(45).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(46).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(47).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(48).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(49).getCTRow().getOutlineLevel());
			assertEquals(0, sheet0.getRow(50).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(51).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(52).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(53).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(54).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(55).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(56).getCTRow().getOutlineLevel());
			assertEquals(1, sheet0.getRow(57).getCTRow().getOutlineLevel());
			assertEquals(0, sheet0.getRow(58).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(59).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(60).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(61).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(62).getCTRow().getOutlineLevel());
			assertEquals(2, sheet0.getRow(63).getCTRow().getOutlineLevel());

		} finally {
			inputStream.close();
		}

	}
}
