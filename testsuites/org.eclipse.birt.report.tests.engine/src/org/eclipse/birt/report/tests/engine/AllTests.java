/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.tests.engine;

import org.eclipse.birt.report.tests.engine.api.DefaultStatusHandlerTest;
import org.eclipse.birt.report.tests.engine.api.EngineConfigTest;
import org.eclipse.birt.report.tests.engine.api.FORenderOptionTest;
import org.eclipse.birt.report.tests.engine.api.HTMLActionHandlerTest;
import org.eclipse.birt.report.tests.engine.api.HTMLCompleteImageHandlerTest;
import org.eclipse.birt.report.tests.engine.api.HTMLEmitterConfigTest;
import org.eclipse.birt.report.tests.engine.api.HTMLRenderContextTest;
import org.eclipse.birt.report.tests.engine.api.HTMLRenderOptionTest;
import org.eclipse.birt.report.tests.engine.api.HTMLServerImageHandlerTest;
import org.eclipse.birt.report.tests.engine.api.RenderOptionBaseTest;
import org.eclipse.birt.report.tests.engine.api.ReportEngineTest;
import org.eclipse.birt.report.tests.engine.api.ReportParameterConverterTest;
import org.eclipse.birt.report.tests.engine.api.RunAndRenderTaskTest;

import junit.framework.Test;
import junit.framework.TestSuite;



/**
 *  Put your comments  
 *
 * @ @version $Revision: 1.4 $Date: 2005-6-27 
 */
public class AllTests {


	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.eclipse.birt.report.tests.engine");
		//$JUnit-BEGIN$
		suite.addTestSuite(DefaultStatusHandlerTest.class);
		suite.addTestSuite(EngineConfigTest.class);
		suite.addTestSuite(FORenderOptionTest.class);
		suite.addTestSuite(HTMLActionHandlerTest.class);
		suite.addTestSuite(HTMLCompleteImageHandlerTest.class);
		suite.addTestSuite(HTMLEmitterConfigTest.class);
		suite.addTestSuite(HTMLRenderContextTest.class);
		suite.addTestSuite(HTMLRenderOptionTest.class);
		suite.addTestSuite(HTMLServerImageHandlerTest.class);
		suite.addTestSuite(RenderOptionBaseTest.class);
		suite.addTestSuite(ReportEngineTest.class);
		suite.addTestSuite(ReportParameterConverterTest.class);
		suite.addTestSuite(RunAndRenderTaskTest.class);
		
		//$JUnit-END$
		return suite;
	}
}
