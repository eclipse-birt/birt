
package org.eclipse.birt.report.tests.engine.api;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IScalarParameterDefn;
import org.eclipse.birt.report.tests.engine.EngineCase;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <b>IScalarParameterDefn test</b>
 * <p>
 * This case tests methods in IScalarParameterDefn API.
 */
public class IScalarParameterDefnTest extends EngineCase {

	private String INPUT = "scalarparameters.rptdesign";
	private IGetParameterDefinitionTask paramTask = null;
	private IScalarParameterDefn scalarDefn = null;
	private String report = this.genInputFile(INPUT);

	public IScalarParameterDefnTest(String name) throws EngineException {
		super(name);
	}

	public static Test Suite() {
		return new TestSuite(IScalarParameterDefnTest.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(INPUT, INPUT);
		IReportRunnable runnable;
		runnable = engine.openReportDesign(report);
		paramTask = engine.createGetParameterDefinitionTask(runnable);

	}

	protected void tearDown() throws Exception {
		paramTask.close();
		engine.destroy();
		super.tearDown();
		removeResource();
	}

	/*
	 * test allowNull method
	 */
	public void testallowNull() {
		scalarDefn = (IScalarParameterDefn) paramTask.getParameterDefn("p_allowbn");
		assertTrue("AllowNull method failed", scalarDefn.allowNull());

		scalarDefn = (IScalarParameterDefn) paramTask.getParameterDefn("p_string");
		assertFalse("AllowNull method failed", scalarDefn.allowNull());
	}

	/*
	 * test allowBlank method
	 */
	public void testallowBlank() {
		scalarDefn = (IScalarParameterDefn) paramTask.getParameterDefn("p_allowbn");
		assertTrue("AllowBlank method failed", scalarDefn.allowBlank());

		scalarDefn = (IScalarParameterDefn) paramTask.getParameterDefn("p_string");
		assertFalse("AllowBlank method failed", scalarDefn.allowBlank());
	}

	/*
	 * test getSelectionListType method (SELECTION_LIST_STATIC is @deprecated.)
	 */
	public void testGetSelectionListType_none() {
		// Static
		scalarDefn = (IScalarParameterDefn) paramTask.getParameterDefn("p_string");
		assertEquals("GetSelectionListType method failed to get none type", IScalarParameterDefn.SELECTION_LIST_STATIC,
				scalarDefn.getSelectionListType());
	}
}