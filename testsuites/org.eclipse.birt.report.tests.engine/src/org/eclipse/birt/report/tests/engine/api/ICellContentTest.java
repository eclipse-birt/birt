
package org.eclipse.birt.report.tests.engine.api;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.tests.engine.BaseEmitter;

public class ICellContentTest extends BaseEmitter {

	private ICellContent cell = null;
	private String reportName = "ICellContentTest.rptdesign";
	private static int count = 0;

	protected String getReportName() {
		return reportName;
	}

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(reportName, reportName);
		cell = new ReportContent().createCellContent();
	}

	public void tearDown() {
		removeResource();
	}

	/**
	 * Test set/getColSpan() method
	 */
	public void testColSpan() {
		cell.setColSpan(3);
		assertEquals(3, cell.getColSpan());
	}

	/**
	 * Test set/getRowSpan() method
	 */
	public void testRowSpan() {
		cell.setRowSpan(3);
		assertEquals(3, cell.getRowSpan());
	}

	/**
	 * Test set/getColumn() method
	 */
	public void testColumn() {
		cell.setColumn(5);
		assertEquals(5, cell.getColumn());
	}

	/**
	 * Test set/getDisplayGroupIcon() method
	 */
	public void testDisplayGroupIcon() {
		cell.setDisplayGroupIcon(true);
		assertTrue(cell.getDisplayGroupIcon());

		cell.setDisplayGroupIcon(false);
		assertFalse(cell.getDisplayGroupIcon());
	}

	public void testICellContentFromReport() throws EngineException {
		runandrender_emitter(EMITTER_HTML, false);
	}

	public void endCell(ICellContent cell) {
		if (count == 0) {
			assertEquals(2, cell.getColSpan());
			assertEquals(2, cell.getRowSpan());
			assertEquals(0, cell.getRow());
			assertEquals(0, cell.getColumn());
			IColumn column = cell.getColumnInstance();
			assertEquals(5, column.getInstanceID().getComponentID());
		}
		if (count == 1) {
			assertEquals(1, cell.getColSpan());
			assertEquals(1, cell.getRowSpan());
			assertEquals(2, cell.getColumn());
		}
		if (count == 2) {
			assertEquals(1, cell.getColSpan());
			assertEquals(1, cell.getRowSpan());
			assertEquals(1, cell.getRow());
		}
		count++;

	}
}
