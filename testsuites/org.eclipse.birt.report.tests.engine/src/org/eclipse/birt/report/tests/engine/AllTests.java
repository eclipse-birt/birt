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

import java.util.Locale;

//import org.eclipse.birt.report.model.elements.ReportDesign;
//import org.eclipse.birt.report.model.i18n.ThreadResources;
//import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
//import org.eclipse.birt.report.model.metadata.MetaDataParserException;
//import org.eclipse.birt.report.model.metadata.MetaDataReader;

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
import org.eclipse.birt.report.tests.engine.api.RunTaskTest;
import org.eclipse.birt.report.tests.engine.api.RenderTaskTest;
import org.eclipse.birt.report.tests.engine.api.DataPreviewTaskTest;
import org.eclipse.birt.report.tests.engine.api.ReportDocumentTest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;



/**
 *  Put your comments  
 *
 * @ @version $Revision: 1.7 $Date: 2005-6-27 
 */
public class AllTests extends TestCase{

	public AllTests(String name){
		super(name);
	}
	
	protected void setUp( ) 
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown( ) 
	{

	}

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.eclipse.birt.report.tests.engine");
		//$JUnit-BEGIN$
		suite.addTestSuite(DefaultStatusHandlerTest.class);
		suite.addTestSuite(EngineConfigTest.class);
		suite.addTestSuite(FORenderOptionTest.class);
		//suite.addTestSuite(HTMLActionHandlerTest.class);
		suite.addTestSuite(HTMLCompleteImageHandlerTest.class);
		suite.addTestSuite(HTMLEmitterConfigTest.class);
		suite.addTestSuite(HTMLRenderContextTest.class);
		suite.addTestSuite(HTMLRenderOptionTest.class);
		suite.addTestSuite(HTMLServerImageHandlerTest.class);
		suite.addTestSuite(RenderOptionBaseTest.class);
		suite.addTestSuite(ReportEngineTest.class);
		suite.addTestSuite(ReportParameterConverterTest.class);
		suite.addTestSuite(RunAndRenderTaskTest.class);
		//added 12/27
//		suite.addTestSuite(DataPreviewTaskTest.class);
//		suite.addTestSuite(RunTaskTest.class);
//		suite.addTestSuite(ReportDocumentTest.class);
//		suite.addTestSuite(RenderTaskTest.class);
		//$JUnit-END$
		return suite;
	}
}
