/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * [regression]Fail to setCurrentView to chart in table onPrepare method.[1302]
 * </p>
 * Test description:
 * <p>
 * </p>
 */
public class Regression_225252 extends BaseTestCase {
	private final static String REPORT = "regression_225252.xml";

	@Override
	protected void setUp() throws Exception {
//		super.setUp( );
		removeResource();
		copyInputToFile(INPUT_FOLDER + "/" + REPORT);
		copyGoldenToFile(GOLDEN_FOLDER + "/" + REPORT);
	}

	@Override
	public void tearDown() {
		removeResource();
	}

	/**
	 * @throws Exception
	 *
	 */

	public void test_regression_225252() throws Exception {
		// TODO:fix error
		/*
		 * DesignEngine engine=new DesignEngine(new DesignConfig()); InputStream
		 * is=getResource( INPUT_FOLDER + "/"+ REPORT ).openStream( ); IReportDesign
		 * report=engine.openDesign(INPUT_FOLDER + "/" +REPORT, is, null );
		 *
		 * ITable table=report.getTable( "mytable" ); try{ table.setCurrentView(
		 * report.getReportElement( "NewChart" ) ); }catch(Exception e){ fail(); } File
		 * f=new File(getTempFolder( )+"/"+OUTPUT_FOLDER ); if(!f.exists( )) f.mkdirs(
		 * ); report.saveAs( getTempFolder( )+"/"+OUTPUT_FOLDER + "/"+ REPORT);
		 * is.close( );
		 *
		 * assertTrue(compareFile( REPORT, REPORT ));
		 */

	}
}
