
package org.eclipse.birt.report.tests.engine.api;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.birt.report.tests.engine.EngineCase;

public class CancelOnErrorTest extends EngineCase {

	private String inputName = "cancel-on-error.rptdesign";

	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(inputName, inputName);
	}

	public void tearDown() throws Exception {
		super.tearDown();
		removeResource();
	}

	public void testRunRenderCancelOnError() {
		String input = this.genInputFile(inputName);
		int cancelSize = 0, continueSize = 0;
		try {
			IReportRunnable runnable = engine.openReportDesign(new FileInputStream(new File(input)));
			IRunAndRenderTask task = engine.createRunAndRenderTask(runnable);

			// continue on error
			IRenderOption options = new RenderOption();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			options.setOutputStream(baos);
			task.setRenderOption(options);
			task.setErrorHandlingOption(IEngineTask.CONTINUE_ON_ERROR);
			task.run();
			task.close();
			continueSize = baos.size();
			baos.reset();

			// cancel on error
			task = engine.createRunAndRenderTask(runnable);
			task.setRenderOption(options);
			task.setErrorHandlingOption(IEngineTask.CANCEL_ON_ERROR);
			task.run();
			task.close();
			cancelSize = baos.size();
			baos.reset();
			baos.close();

			assertTrue(continueSize > cancelSize);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

	}

}
