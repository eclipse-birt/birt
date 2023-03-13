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

package org.eclipse.birt.report.model.adapter.oda.api;

import org.eclipse.birt.report.model.adapter.oda.ModelOdaAdapter;
import org.eclipse.birt.report.model.adapter.oda.util.BaseTestCase;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.OdaDesignerStateHandle;
import org.eclipse.datatools.connectivity.oda.design.DataSourceDesign;
import org.eclipse.datatools.connectivity.oda.design.DesignFactory;
import org.eclipse.datatools.connectivity.oda.design.DesignerState;
import org.eclipse.datatools.connectivity.oda.design.DesignerStateContent;
import org.eclipse.datatools.connectivity.oda.design.Properties;

/**
 * Test cases to test the function of ModelOdaAdapter.
 */

public class OdaDataSourceAdapterTest extends BaseTestCase {

	private final static String INPUT_FILE = "OdaDataSourceConvertTest.xml"; //$NON-NLS-1$
	private final static String INPUT_FILE_WITH_EMPTY_PROPS = "OdaDataSourceEmptyProps.xml"; //$NON-NLS-1$
	private final static String GOLDEN_FILE1_WITH_EMPTY_PROPS = "OdaDataSourceEmptyProps_golden_1.xml"; //$NON-NLS-1$

	private final static String DATA_SOURCE_EXTENSIONID = "org.eclipse.birt.report.data.oda.jdbc"; //$NON-NLS-1$

	/**
	 * Test case: <br>
	 * To read a design file, uses adapter to create a data source design. Checks
	 * values of the created data source design.
	 *
	 * @throws Exception
	 */

	public void testROMDataSourceToODADataSource() throws Exception {
		openDesign(INPUT_FILE);
		OdaDataSourceHandle sourceHandle = (OdaDataSourceHandle) designHandle.findDataSource("myDataSource1"); //$NON-NLS-1$

		DataSourceDesign sourceDesign = new ModelOdaAdapter().createDataSourceDesign(sourceHandle);

		assertEquals("myDataSource1", sourceDesign.getName()); //$NON-NLS-1$
		assertEquals(DATA_SOURCE_EXTENSIONID, sourceDesign.getOdaExtensionId());

		assertEquals("My Data Source One", sourceDesign.getDisplayName()); //$NON-NLS-1$
		Properties props = sourceDesign.getPublicProperties();
		// add OdaConnProfileName, OdaConnProfileStorePath properties.
		assertEquals(9, props.getProperties().size());

		assertEquals("com.mysql.jdbc.Driver", props //$NON-NLS-1$
				.findProperty("odaDriverClass").getValue()); //$NON-NLS-1$
		assertEquals("jdbc:mysql://localhost:3306/birt", props //$NON-NLS-1$
				.findProperty("odaURL").getValue()); //$NON-NLS-1$
		assertNull(props.findProperty("odaUser").getValue()); //$NON-NLS-1$
		assertNull(props.findProperty("odaPassword") //$NON-NLS-1$
				.getValue());

		props = sourceDesign.getPrivateProperties();
		assertEquals(2, props.getProperties().size());

		assertEquals("User", props.findProperty("odaUser").getValue()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("Password", props.findProperty("odaPassword") //$NON-NLS-1$ //$NON-NLS-2$
				.getValue());
	}

	/**
	 * Test case: <br>
	 *
	 * <ul>
	 * <li>Create a data source design, uses the adapter to create a data source
	 * handle. Saves the new datasource handle to the design file.
	 * <li>No ActivityStack action should be invovled.
	 * </ul>
	 *
	 * @throws Exception
	 */

	public void testODADataSourceToROMDataSource() throws Exception {
		DataSourceDesign sourceDesign = createDataSourceDesign();

		createDesign();

		OdaDataSourceHandle sourceHandle = new ModelOdaAdapter().createDataSourceHandle(sourceDesign, designHandle);
		assertFalse(designHandle.getCommandStack().canUndo());
		assertFalse(designHandle.getCommandStack().canRedo());

		designHandle.getDataSources().add(sourceHandle);

		/*
		 * save( ); assertTrue( compareTextFile( GOLDEN_FILE ) );
		 */
		verifyDataSource();
	}

	/**
	 * Test case: <br>
	 *
	 * <ul>
	 * <li>Have a data source design and a data source handle, copied all values
	 * from the ODA element to ROM element.
	 * <li>ActivityStack action should be invovled as a transaction.
	 * </ul>
	 *
	 * @throws Exception
	 */

	public void testUpdateROMDataSourceWithODADataSource() throws Exception {
		openDesign(INPUT_FILE_WITH_EMPTY_PROPS);

		OdaDataSourceHandle sourceHandle = (OdaDataSourceHandle) designHandle.findDataSource("myDataSource1"); //$NON-NLS-1$
		DataSourceDesign sourceDesign = createDataSourceDesign();

		new ModelOdaAdapter().updateDataSourceHandle(sourceDesign, sourceHandle);

		assertTrue(designHandle.getCommandStack().canUndo());
		assertFalse(designHandle.getCommandStack().canRedo());

		designHandle.getCommandStack().undo();

		assertFalse(designHandle.getCommandStack().canUndo());
		assertTrue(designHandle.getCommandStack().canRedo());

		/*
		 * save( ); assertTrue( compareTextFile( GOLDEN_FILE_WITH_EMPTY_PROPS ) );
		 */
		verifyDataSourceWithEmptyProp();

		designHandle.getCommandStack().redo();

		save();
		assertTrue(compareTextFile(GOLDEN_FILE1_WITH_EMPTY_PROPS));
	}

	private void verifyDataSourceWithEmptyProp() throws Exception {
		OdaDataSourceHandle sourceHandle = (OdaDataSourceHandle) designHandle.findDataSource("myDataSource1");
		assertNotNull(sourceHandle);
		assertNotNull(sourceHandle);
		assertNull(sourceHandle.getProperty("privateDriverProperties"));
		assertNull(sourceHandle.getProperty("userProperties"));
	}

	/**
	 * Creates a new <code>DataSourceDesign</code>.
	 *
	 * @return an object of <code>DataSourceDesign</code>.
	 */

	private DataSourceDesign createDataSourceDesign() {
		DataSourceDesign sourceDesign = DesignFactory.eINSTANCE.createDataSourceDesign();
		sourceDesign.setName("my data source design"); //$NON-NLS-1$
		sourceDesign.setDisplayName("data source display name"); //$NON-NLS-1$
		sourceDesign.setOdaExtensionId(DATA_SOURCE_EXTENSIONID);

		Properties props = DesignFactory.eINSTANCE.createProperties();
		props.setProperty("odaDriverClass", "new drivers"); //$NON-NLS-1$//$NON-NLS-2$
		props.setProperty("odaURL", "jdbc:sqlserver://localhost"); //$NON-NLS-1$//$NON-NLS-2$
		props.setProperty("odaUser", "new user"); //$NON-NLS-1$ //$NON-NLS-2$
		sourceDesign.setPublicProperties(props);

		props = DesignFactory.eINSTANCE.createProperties();
		props.setProperty("odaDriverClass", "new drivers"); //$NON-NLS-1$ //$NON-NLS-2$
		props.setProperty("odaPassword", "new password"); //$NON-NLS-1$ //$NON-NLS-2$
		sourceDesign.setPrivateProperties(props);

		return sourceDesign;
	}

	private void verifyDataSource() throws Exception {
		saveAndOpenDesign();
		OdaDataSourceHandle sourceHandle = (OdaDataSourceHandle) designHandle.findDataSource("my data source design");
		assertNotNull(sourceHandle);
		assertEquals("data source display name", sourceHandle.getDisplayName());
		assertEquals(DATA_SOURCE_EXTENSIONID, sourceHandle.getExtensionID());

		assertEquals("new drivers", sourceHandle.getProperty("odaDriverClass"));
		assertEquals("jdbc:sqlserver://localhost", sourceHandle.getProperty("odaURL"));
		assertEquals("new user", sourceHandle.getProperty("odaUser"));

		assertEquals("new drivers", sourceHandle.getPrivateDriverProperty("odaDriverClass"));
		assertEquals("new password", sourceHandle.getPrivateDriverProperty("odaPassword"));
	}

	/**
	 * Tests functions to convert Designer State on a set Design.
	 *
	 * @throws Exception
	 */

	public void testDesignerState() throws Exception {
		openDesign(INPUT_FILE);
		OdaDataSourceHandle sourceHandle = (OdaDataSourceHandle) designHandle.findDataSource("myDataSource1"); //$NON-NLS-1$

		DesignerState designerState = new ModelOdaAdapter().newOdaDesignerState(sourceHandle);

		assertEquals("1.1", designerState.getVersion()); //$NON-NLS-1$
		DesignerStateContent stateContent = designerState.getStateContent();

		assertEquals("content as blob", new String(stateContent //$NON-NLS-1$
				.getStateContentAsBlob(), "8859_1")); //$NON-NLS-1$
		assertNull(stateContent.getStateContentAsString());

		designerState.setVersion("2.0"); //$NON-NLS-1$
		designerState.setNewStateContentAsString("content as string 2.0"); //$NON-NLS-1$

		new ModelOdaAdapter().updateROMDesignerState(designerState, sourceHandle);
		compareTextFile("UpdateROMDesignerState_golden.xml"); //$NON-NLS-1$

		sourceHandle.setDesignerState(null);
		assertNull(sourceHandle.getDesignerState());

		new ModelOdaAdapter().updateROMDesignerState(designerState, sourceHandle);

		OdaDesignerStateHandle romDesignerState = sourceHandle.getDesignerState();
		assertNotNull(romDesignerState);

		assertEquals("2.0", romDesignerState.getVersion()); //$NON-NLS-1$
	}
}
