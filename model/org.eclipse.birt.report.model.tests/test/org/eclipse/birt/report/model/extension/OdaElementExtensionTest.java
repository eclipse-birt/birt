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

package org.eclipse.birt.report.model.extension;

import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.command.ExtendsException;
import org.eclipse.birt.report.model.elements.interfaces.IOdaExtendableElementModel;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Tests ODA element extension.
 */

public class OdaElementExtensionTest extends BaseTestCase {

	private final static String DATASOURCE_EXTENSION_ID = "org.eclipse.birt.report.data.oda.jdbc"; //$NON-NLS-1$
	private final static String DATASET_EXTENSION_ID = "org.eclipse.birt.report.data.oda.jdbc.JdbcSelectDataSet"; //$NON-NLS-1$

	/*
	 * @see BaseTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Tests the parsing function.
	 * 
	 * @throws DesignFileException
	 */

	public void testParser() throws DesignFileException {
		openDesign("ODAElementExtensionTest.xml"); //$NON-NLS-1$

		OdaDataSourceHandle dataSourceHandle = (OdaDataSourceHandle) designHandle.findDataSource("dataSource");//$NON-NLS-1$

		assertEquals("org.eclipse.birt.report.data.oda.jdbc", dataSourceHandle.getExtensionID());//$NON-NLS-1$
		assertEquals("User", dataSourceHandle.getStringProperty("odaUser"));//$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("DoNotKnow", dataSourceHandle.getStringProperty("odaPassword"));//$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("URL", dataSourceHandle.getStringProperty("odaURL"));//$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("DriverClass", dataSourceHandle.getStringProperty("odaDriverClass"));//$NON-NLS-1$ //$NON-NLS-2$

		OdaDataSetHandle dataSetHandle = (OdaDataSetHandle) designHandle.findDataSet("dataSet");//$NON-NLS-1$

		assertEquals(DATASET_EXTENSION_ID, dataSetHandle.getExtensionID());
		assertEquals("dataSource", dataSetHandle.getStringProperty(OdaDataSetHandle.DATA_SOURCE_PROP));//$NON-NLS-1$
		assertEquals("select * from customers", dataSetHandle.getStringProperty(OdaDataSetHandle.QUERY_TEXT_PROP));//$NON-NLS-1$
		assertEquals("30", dataSetHandle.getStringProperty("queryTimeOut"));//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Tests the writing function.
	 * 
	 * @throws Exception
	 */

	public void testWriter() throws Exception {
		openDesign("ODAElementExtensionTest.xml"); //$NON-NLS-1$

		OdaDataSourceHandle dataSourceHandle = (OdaDataSourceHandle) designHandle.findDataSource("dataSource");//$NON-NLS-1$

		dataSourceHandle.setProperty("odaUser", "NewUser");//$NON-NLS-1$ //$NON-NLS-2$
		dataSourceHandle.setProperty("odaPassword", "NewPassword");//$NON-NLS-1$ //$NON-NLS-2$
		dataSourceHandle.setProperty("odaURL", "NewURL");//$NON-NLS-1$ //$NON-NLS-2$
		dataSourceHandle.setProperty("odaDriverClass", "NewDriverClass");//$NON-NLS-1$ //$NON-NLS-2$

		OdaDataSetHandle dataSetHandle = (OdaDataSetHandle) designHandle.findDataSet("dataSet");//$NON-NLS-1$
		dataSetHandle.setProperty(OdaDataSetHandle.QUERY_TEXT_PROP, "select * from cities");//$NON-NLS-1$
		dataSetHandle.setProperty("queryTimeOut", "60");//$NON-NLS-1$ //$NON-NLS-2$

		save();
		assertTrue(compareFile("ODAElementExtensionTest_golden.xml"));//$NON-NLS-1$
	}

	/**
	 * Test cases:
	 * 
	 * <ul>
	 * <li>to create an oda data set from an existed oda data set. extension id
	 * should not be null.
	 * <li>to create an oda data source from an existed oda data source. extension
	 * id should not be null.
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testNewElementFrom() throws Exception {
		openDesign("ODAElementExtensionTest.xml"); //$NON-NLS-1$

		OdaDataSourceHandle dataSource = (OdaDataSourceHandle) designHandle.findDataSource("dataSource");//$NON-NLS-1$
		assertEquals(DATASOURCE_EXTENSION_ID, dataSource.getExtensionID());

		OdaDataSourceHandle extendsSource = (OdaDataSourceHandle) designHandle.getElementFactory()
				.newElementFrom(dataSource, "dataSource1"); //$NON-NLS-1$
		assertEquals(DATASOURCE_EXTENSION_ID, extendsSource.getExtensionID());

		OdaDataSetHandle dataSet = (OdaDataSetHandle) designHandle.findDataSet("dataSet");//$NON-NLS-1$
		assertEquals(DATASET_EXTENSION_ID, dataSet.getExtensionID());

		OdaDataSetHandle extendsSet = (OdaDataSetHandle) designHandle.getElementFactory().newElementFrom(dataSet,
				"dataSet1"); //$NON-NLS-1$
		assertEquals(DATASET_EXTENSION_ID, extendsSet.getExtensionID());
	}

	/**
	 * Tests to extends for the oda datasource
	 * 
	 * <ul>
	 * <li>dataSource1 has extendsion id, dataSource1 hasn't. Exception is thrown.
	 * <li>if both have extension id, no exception.
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testSetExtends() throws Exception {
		sessionHandle = DesignEngine.newSession(ULocale.ENGLISH);

		designHandle = sessionHandle.createDesign();
		OdaDataSourceHandle dataSource1 = designHandle.getElementFactory().newOdaDataSource("ds1", //$NON-NLS-1$
				DATASOURCE_EXTENSION_ID);
		designHandle.getDataSources().add(dataSource1);

		OdaDataSourceHandle dataSource2 = designHandle.getElementFactory().newOdaDataSource("ds2", null); //$NON-NLS-1$
		designHandle.getDataSources().add(dataSource2);

		try {
			dataSource2.setExtends(dataSource1);
			fail();
		} catch (ExtendsException e) {
			assertEquals(ExtendsException.DESIGN_EXCEPTION_WRONG_TYPE, e.getErrorCode());
		}

		try {
			dataSource1.setExtends(dataSource2);
			fail();
		} catch (ExtendsException e) {
			assertEquals(ExtendsException.DESIGN_EXCEPTION_WRONG_TYPE, e.getErrorCode());
		}

		dataSource2.getElement().setProperty(IOdaExtendableElementModel.EXTENSION_ID_PROP, DATASOURCE_EXTENSION_ID);

		dataSource2.setExtends(dataSource1);
	}
}
