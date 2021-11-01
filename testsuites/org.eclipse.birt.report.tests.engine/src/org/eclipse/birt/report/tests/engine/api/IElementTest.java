
package org.eclipse.birt.report.tests.engine.api;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IElement;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.tests.engine.BaseEmitter;

/**
 * Test IElement API methods No case for getChildren() method because it's not
 * implemented completely and is not recommended to use.
 */
public class IElementTest extends BaseEmitter {

	private String report = "IElementTest.rptdesign";

	/**
	 * Test set/getParent() methods.
	 */
	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(report, report);
	}

	public void tearDown() throws Exception {
		super.tearDown();
		removeResource();
	}

	public void testParent() {
		IElement element = new ReportContent().createContainerContent();
		IElement parent = new ReportContent().createContainerContent();
		element.setParent(parent);
		assertEquals(parent, element.getParent());

		element.setParent(null);
		assertNull(element.getParent());
	}

	public void testIElement() throws EngineException {
		runandrender_emitter(EMITTER_HTML, false);
	}

	public void endContainer(IContainerContent container) {
		System.out.println(container);
	}

	public void endCell(ICellContent cell) {
		assertTrue(cell.getParent() instanceof IRowContent);
	}

	public void endRow(IRowContent row) {
		assertTrue(row.getParent() instanceof ITableContent);
	}

	protected String getReportName() {
		return report;
	}

}
