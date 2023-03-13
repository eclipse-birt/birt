/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.report.tests.engine.api;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllApiTests {

	public static Test suite() {
		TestSuite test = new TestSuite();

		// add all test classes here
		// org.eclipse.birt.report.tests.engine.api
		test.addTestSuite(DataExtractionTaskTest.class);
		test.addTestSuite(DataIDTest.class);
		test.addTestSuite(DataSetIDTest.class);
		test.addTestSuite(DefaultStatusHandlerTest.class);
		test.addTestSuite(EngineConfigTest.class);
		test.addTestSuite(HTMLCompleteImageHandlerTest.class);
		test.addTestSuite(HTMLEmitterConfigTest.class);
		test.addTestSuite(HTMLRenderContextTest.class);
		test.addTestSuite(HTMLRenderOptionTest.class);
		test.addTestSuite(HTMLServerImageHandlerTest.class);
		test.addTestSuite(IActionTest.class);
		test.addTestSuite(IAutoTextContentTest.class);
		test.addTestSuite(IBandContentTest.class);
		test.addTestSuite(ICellContentTest.class);
		test.addTestSuite(IColumnTest.class);
		test.addTestSuite(IContentTest.class);
		test.addTestSuite(IDataContentTest.class);
		test.addTestSuite(IDataIteratorTest.class);
		test.addTestSuite(IElementTest.class);
		test.addTestSuite(IEmitterServicesTest.class);
		test.addTestSuite(IGetParameterDefinitionTaskTest.class);
		test.addTestSuite(IGroupContentTest.class);
		test.addTestSuite(IScalarParameterDefnTest.class);
		test.addTestSuite(RenderFolderDocumentTest.class);
		test.addTestSuite(RenderOptionBaseTest.class);
		test.addTestSuite(RenderTaskTest.class);
		test.addTestSuite(RenderUnfinishedReportDoc.class);
		test.addTestSuite(ReportDocumentTest.class);
		test.addTestSuite(ReportEngineTest.class);
		test.addTestSuite(ReportParameterConverterTest.class);
		test.addTestSuite(ResourceLocatorTest.class);
		test.addTestSuite(RunAndRenderTaskTest.class);
		test.addTestSuite(RunTaskTest.class);

		return test;
	}
}
