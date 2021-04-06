/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.api.script;

import java.io.File;
import java.util.HashMap;

import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.engine.api.impl.RunTask;

public class PageScriptHandlerTest extends EngineCase {
	static final String PAGE_SCRIPT_HANDLER = "org/eclipse/birt/report/engine/api/script/PageScriptTest.xml";
	static final String PAGE_SCRIPT_JAR = "org/eclipse/birt/report/engine/api/script/script.jar";
	static final String REPORT_DESIGN = "design.rptdesign";
	static final String JAR = "script.jar";
	static final String REPORT_DOCUMENT = "./reportdocument.folder/";

	static final String SCRIPT_CANCEL = "org/eclipse/birt/report/engine/api/ScriptCancel.xml";

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public IReportEngine createReportEngine() {
		EngineConfig config = new EngineConfig();
		HashMap map = new HashMap();
		File jar = new File(JAR);
		map.put(EngineConstants.PROJECT_CLASSPATH_KEY, jar.getAbsolutePath());
		config.setAppContext(map);
		// assume we has in the platform
		Object factory = Platform.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
		if (factory instanceof IReportEngineFactory) {
			return ((IReportEngineFactory) factory).createReportEngine(config);
		}
		return null;
	}

	public void testScriptCancel() throws Exception {
		copyResource(PAGE_SCRIPT_HANDLER, REPORT_DESIGN);
		copyResource(PAGE_SCRIPT_JAR, JAR);
		removeFile(REPORT_DOCUMENT);
		IReportRunnable runnable = engine.openReportDesign(REPORT_DESIGN);
		IRunTask runTask = engine.createRunTask(runnable);
		runTask.run(REPORT_DOCUMENT);
		IReportContext reportContext = ((RunTask) runTask).getReportContext();
		assertTrue(convertToBoolean(reportContext.getGlobalVariable("REPORT_ONPAGESTART")));
		assertTrue(convertToBoolean(reportContext.getGlobalVariable("REPORT_ONPAGEEND")));
		assertTrue(convertToBoolean(reportContext.getGlobalVariable("MASTERPAGE_ONPAGESTART")));
		assertTrue(convertToBoolean(reportContext.getGlobalVariable("MASTERPAGE_ONPAGEEND")));
		runTask.close();
		removeFile(REPORT_DESIGN);
		removeFile(JAR);
		removeFile(REPORT_DOCUMENT);

	}

	protected boolean convertToBoolean(Object obj) {
		if (obj != null && obj instanceof Boolean) {
			return ((Boolean) obj).booleanValue();
		}
		return false;
	}

}
