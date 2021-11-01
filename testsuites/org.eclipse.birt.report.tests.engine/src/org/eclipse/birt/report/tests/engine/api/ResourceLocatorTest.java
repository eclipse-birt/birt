
package org.eclipse.birt.report.tests.engine.api;

import java.io.File;
import java.util.HashMap;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.core.framework.PlatformConfig;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.tests.engine.EngineCase;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <b>Custom resource locator test</b>
 * <p>
 * This case tests resource locatore defined by customer can be found and
 * loaded.
 */
public class ResourceLocatorTest extends EngineCase {

	private String root_path, path;
	private String separator = System.getProperty("file.separator");

	public ResourceLocatorTest(String name) {
		super(name);
	}

	public static Test Suite() {
		return new TestSuite(ResourceLocatorTest.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT("resources/aa.jpg", "resources/aa.jpg");
		copyResource_INPUT("resources/resource_a.properties", "resources/resource_a.properties");
		copyResource_INPUT("resources/resource_library.rptlibrary", "resources/resource_library.rptlibrary");

		root_path = this.getFullQualifiedClassName() + separator;
	}

	public void tearDown() throws Exception {
		super.tearDown();
		removeResource();
	}

	public void testResourceImage() {
		path = "file://" + this.genInputFolder() + separator + "resources" + separator;
		// path="http://192.168.218.234:88/resource/";
		IResourceLocator locator = new EngineResourceLocator(path);
		try {
			renderReport("resource_image", locator);
		} catch (BirtException e) {
			e.printStackTrace();
			fail("Failed to find image resource from custom resource locator");
		}
		File f = new File(this.genOutputFile("resource_image.html"));
		assertTrue("Failed render report from image resource", f.exists());
	}

	public void testResourceProperties() {
		path = "file://" + this.genInputFolder() + separator + "resources" + separator;
		// path="http://192.168.218.234:88/resource/";
		IResourceLocator locator = new EngineResourceLocator(path);
		try {
			renderReport("resource_properties", locator);
		} catch (BirtException e) {
			e.printStackTrace();
			fail("Failed to find library resource from custom resource locator");
		}
		File f = new File(this.genOutputFile("resource_properties.html"));
		assertTrue("Failed render report from properties resource", f.exists());
	}

	public void testResourceLibrary() {
		path = "file://" + this.genInputFolder() + separator + "resources" + separator;
		// path="http://192.168.218.234:88/resource/";
		IResourceLocator locator = new EngineResourceLocator(path);
		try {
			renderReport("resource_library", locator);
		} catch (BirtException e) {
			e.printStackTrace();
			fail("Failed to find properties resource from custom resource locator");
		}

		File f = new File(this.genOutputFile("resource_library.html"));
		assertTrue("Failed render report from library", f.exists());
	}

	private void renderReport(String reportName, IResourceLocator locator) throws BirtException {
		IReportEngine engine_locator = null;
		EngineConfig config = null;
		String input = this.genInputFolder() + separator + reportName + ".rptdesign";
		copyResource_INPUT(reportName + ".rptdesign", reportName + ".rptdesign");
		String output = this.genOutputFile(reportName + ".html");

		config = new EngineConfig();
		config.setResourceLocator(locator);

		Platform.startup(new PlatformConfig());
		// assume we has in the platform
		Object factory = Platform.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
		engine_locator = ((IReportEngineFactory) factory).createReportEngine(config);

		IReportRunnable runnable = engine_locator.openReportDesign(input);
		IRunAndRenderTask rrTask = engine_locator.createRunAndRenderTask(runnable);

		HTMLRenderOption option = new HTMLRenderOption();
		option.setOutputFileName(output);
		option.setOutputFormat("html");
		rrTask.setRenderOption(option);
		rrTask.setAppContext(new HashMap());

		rrTask.run();
		rrTask.close();

		engine_locator.destroy();

	}
}
