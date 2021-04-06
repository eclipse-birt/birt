
package org.eclipse.birt.report.tests.engine.api;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.content.ITableGroupContent;
import org.eclipse.birt.report.engine.ir.TableGroupDesign;
import org.eclipse.birt.report.tests.engine.BaseEmitter;

public class GroupLevelTest extends BaseEmitter {

	private String reportName = "groupLevelTest.rptdesign";

	protected String getReportName() {
		return reportName;
	}

	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(reportName, reportName);
	}

	public void tearDown() {
		removeResource();
	}

	public void testGetGroupLevel() throws EngineException {
		runandrender_emitter(EMITTER_HTML, false);
	}

	public void startTableGroup(ITableGroupContent group) {
		if (((TableGroupDesign) group.getGenerateBy()).getName().equals("NewTableGroup1"))
			assertEquals(0, group.getGroupLevel());
		else {
			assertEquals(1, group.getGroupLevel());
		}
	}

}
