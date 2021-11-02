/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/
/**
 *
 */

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.ModuleOption;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * </p>
 * Expected JDBC data source properties to appear as top-level properties so
 * that they can be used in scripting.
 *
 */
public class Regression_75539 extends BaseTestCase {

	private final static String OUTPUT = "Reg_75539.out"; //$NON-NLS-1$
	private final static String GOLDEN = "Reg_75539.golden"; //$NON-NLS-1$

	/**
	 * @throws Exception
	 *
	 */

	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyGoldenToFile(GOLDEN_FOLDER + "/" + GOLDEN);
	}

	/*
	 * public void tearDown( ) { removeResource( ); }
	 */
	public void test_regression_75539() throws Exception {
		sessionHandle = new DesignEngine(new DesignConfig()).newSessionHandle(ULocale.ENGLISH);
		ModuleOption options = new ModuleOption();
		options.setProperty(ModuleOption.BLANK_CREATION_KEY, true);
		designHandle = sessionHandle.createDesign(null, options);

		ElementFactory factory = designHandle.getElementFactory();
		DataSourceHandle dsourceHandle = factory.newOdaDataSource("dsource", "org.eclipse.birt.report.data.oda.jdbc"); //$NON-NLS-1$//$NON-NLS-2$
		dsourceHandle.setProperty("odaDriverClass", "org.eclipse.birt.report.data.oda.sampledb.Driver"); //$NON-NLS-1$//$NON-NLS-2$
		dsourceHandle.setProperty("odaURL", "jdbc:classicmodels:sampledb"); //$NON-NLS-1$//$NON-NLS-2$
		dsourceHandle.setProperty("odaUser", "ClassicModels"); //$NON-NLS-1$//$NON-NLS-2$

		designHandle.getDataSources().add(dsourceHandle);

		// saveAs( OUTPUT ); //$NON-NLS-1$
		// assertTrue( compareTextFile( GOLDEN,
		// this.getFullQualifiedClassName()+"/"+OUTPUT_FOLDER+"/"+OUTPUT ) );
		String TempFile = this.genOutputFile(OUTPUT);
		designHandle.saveAs(TempFile);
		assertTrue(compareTextFile(GOLDEN, OUTPUT));
	}

}
