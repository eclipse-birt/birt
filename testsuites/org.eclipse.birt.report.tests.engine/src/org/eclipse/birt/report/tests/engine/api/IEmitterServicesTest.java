
package org.eclipse.birt.report.tests.engine.api;

import java.util.HashMap;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.HTMLEmitterConfig;
import org.eclipse.birt.report.engine.api.HTMLRenderContext;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.tests.engine.BaseEmitter;

/**
 * Test IEmitterServices API methods.
 */
public class IEmitterServicesTest extends BaseEmitter {

	private String report = "IEmitterServicesTest.rptdesign";
	private Object emitterConfig;

	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(report, report);
	}

	public void tearDown() {
		removeResource();
	}

	protected String getReportName() {
		return report;
	}

	/**
	 * Test IEmitterServices methods.
	 * 
	 * @throws BirtException
	 */
	public void testIEmitterServices() throws BirtException {
		EngineConfig config = new EngineConfig();
		emitterConfig = new HTMLEmitterConfig();
		config.setEmitterConfiguration(EMITTER_HTML, emitterConfig);
		emitterConfig = config.getEmitterConfigs().get(EMITTER_HTML);
		Platform.startup(config);
		IReportEngineFactory factory = (IReportEngineFactory) Platform
				.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
		IReportEngine reportEngine = factory.createReportEngine(config);
		IReportRunnable reportRunnable = engine.openReportDesign(this.genInputFile(report));

		IRenderOption options = new HTMLRenderOption();
		options.setOutputFormat(EMITTER_HTML);
		options.setOutputFileName(this.genOutputFile("myService.html"));
		HTMLRenderContext renderContext = new HTMLRenderContext();
		renderContext.setImageDirectory("myImage"); //$NON-NLS-1$
		HashMap appContext = new HashMap();
		appContext.put(EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT, renderContext);
		appContext.put("emitter_class", this);

		IRunAndRenderTask rrTask = reportEngine.createRunAndRenderTask(reportRunnable);
		rrTask.setRenderOption(options);
		rrTask.setAppContext(appContext);
		rrTask.run();
		rrTask.close();
	}

	public void initialize(IEmitterServices service) {
		assertNotNull(service.getEmitterConfig());
		assertEquals(emitterConfig, service.getEmitterConfig().get(EMITTER_HTML));

		/*
		 * method is deprecated IReportExecutor executor = service.getExecutor( );
		 * assertNotNull( executor );
		 */

		assertEquals("emitter_html", service.getOption("Format"));

		assertTrue(((HashMap) service.getRenderContext()).get("HTML_RENDER_CONTEXT") instanceof HTMLRenderContext);
		HTMLRenderContext renderContext = (HTMLRenderContext) ((HashMap) service.getRenderContext())
				.get("HTML_RENDER_CONTEXT");
		assertEquals("myImage", renderContext.getImageDirectory());

		IReportContext context = service.getReportContext();
		assertTrue(context.getAppContext()
				.get(EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT) instanceof HTMLRenderContext);
		renderContext = (HTMLRenderContext) context.getAppContext().get(EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT);
		assertEquals("myImage", renderContext.getImageDirectory());

		assertEquals("emitter_html", service.getRenderOption().getOutputFormat());

		String name = service.getReportName();
		name = name.substring(name.lastIndexOf("/") + 1);
		assertEquals(report, name);

		IReportRunnable reportRunnable = service.getReportRunnable();
		assertEquals(service.getReportName(), reportRunnable.getReportName());
		/*
		 * method is deprecated IEngineTask task = service.getTask( ); assertTrue( task
		 * instanceof IRunAndRenderTask );
		 */
	}

}
