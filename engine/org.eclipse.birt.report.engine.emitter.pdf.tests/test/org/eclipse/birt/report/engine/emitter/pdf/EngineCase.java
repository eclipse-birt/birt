/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.pdf;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;

import junit.framework.TestCase;

abstract public class EngineCase extends TestCase {

	protected static final String REPORT_DESIGN = "design.rptdesign";
	protected static final String REPORT_DOCUMENT = "reportdocument";

	protected IReportEngine engine;

	@Override
	protected void setUp() throws Exception {
		engine = createReportEngine();
	}

	public IReportEngine createReportEngine() throws MalformedURLException, URISyntaxException {
		return createReportEngine(null);
	}

	public IReportEngine createReportEngine(EngineConfig config) throws MalformedURLException, URISyntaxException {
		if (config == null) {
			config = new EngineConfig();
			config.setFontConfig(
					new java.net.URI("file:test/org/eclipse/birt/report/engine/emitter/pdf/fonts/fontsConfig.xml")
							.toURL());
		}

		// assume we has in the platform
		Object factory = Platform.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
		if (factory instanceof IReportEngineFactory) {
			return ((IReportEngineFactory) factory).createReportEngine(config);
		}
		return null;
	}

	/**
	 * Create run and render result for the design file.
	 *
	 * @param designFile
	 * @return run and render task.
	 * @throws EngineException
	 */
	protected IRunAndRenderTask createRunAndRenderTask(String designFile) throws EngineException {
		IReportRunnable reportDesign = engine.openReportDesign(designFile);
		IRunAndRenderTask runAndRenderTask = engine.createRunAndRenderTask(reportDesign);
		return runAndRenderTask;
	}

}
