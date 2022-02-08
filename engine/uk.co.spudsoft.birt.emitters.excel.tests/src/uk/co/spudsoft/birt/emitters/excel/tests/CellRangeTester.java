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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;

public class CellRangeTester extends ReportRunner {

	protected Pattern pattern = Pattern.compile("R(\\d+)C(\\d+)-R(\\d+)C(\\d+): .*");

	protected void validateCellRange(Matcher matcher, Cell cell) {
		int desiredR1 = Integer.parseInt(matcher.group(1));
		int desiredC1 = Integer.parseInt(matcher.group(2));
		int desiredR2 = Integer.parseInt(matcher.group(3));
		int desiredC2 = Integer.parseInt(matcher.group(4));

		int actualR1 = cell.getRowIndex() + 1;
		int actualC1 = cell.getColumnIndex() + 1;
		int actualR2 = actualR1;
		int actualC2 = actualC1;

		for (int i = 0; i < cell.getSheet().getNumMergedRegions(); ++i) {
			CellRangeAddress cra = cell.getSheet().getMergedRegion(i);
			if ((cra.getFirstRow() == cell.getRowIndex()) && (cra.getFirstColumn() == cell.getColumnIndex())) {
				assertEquals(actualR1, actualR2);
				assertEquals(actualC1, actualC2);
				actualR2 = cra.getLastRow() + 1;
				actualC2 = cra.getLastColumn() + 1;
			}
		}
		assertEquals(desiredR1, actualR1);
		assertEquals(desiredC1, actualC1);
		assertEquals(desiredR2, actualR2);
		assertEquals(desiredC2, actualC2);
	}

}
