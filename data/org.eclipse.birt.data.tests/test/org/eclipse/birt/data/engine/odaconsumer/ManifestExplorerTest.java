/*
 *******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.data.engine.odaconsumer;

import java.util.Hashtable;
import java.util.Properties;

import org.eclipse.datatools.connectivity.oda.util.manifest.ManifestExplorer;
import org.eclipse.datatools.connectivity.oda.util.manifest.DataSetType;
import org.eclipse.datatools.connectivity.oda.util.manifest.DataTypeMapping;
import org.eclipse.datatools.connectivity.oda.util.manifest.ExtensionManifest;
import org.eclipse.datatools.connectivity.oda.util.manifest.JavaRuntimeInterface;
import org.eclipse.datatools.connectivity.oda.util.manifest.RuntimeInterface;
import org.eclipse.datatools.connectivity.oda.util.manifest.TraceLogging;
import org.eclipse.datatools.connectivity.oda.util.manifest.Property;

import testutil.JDBCOdaDataSource;

import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

@Ignore("Ignore tests that require manual setup")
public class ManifestExplorerTest extends OdaconsumerTestCase {
	private final String m_jdbcId = JDBCOdaDataSource.DATA_SOURCE_TYPE;
	private final String m_expectedJdbcDataSetId = JDBCOdaDataSource.DATA_SET_TYPE;
	private final String m_testDriverId = "org.eclipse.birt.data.engine.odaconsumer.testdriver";

	@Test
	public void testGetDataSourceNames() throws Exception {
		Properties names = ManifestExplorer.getInstance().getDataSourceIdentifiers();

		assertNotNull(names.getProperty(m_testDriverId));
	}

	@Test
	public void testGetExtensionConfigs() throws Exception {
		ExtensionManifest[] configs = ManifestExplorer.getInstance().getExtensionManifests();
		assertTrue(configs.length > 0);
	}

	@Test
	public void testGetExtensionConfig() throws Exception {
		ExtensionManifest config = ManifestExplorer.getInstance().getExtensionManifest(m_jdbcId);
		verifyExtensionConfig(config);
	}

	@Test
	public void testGetExtensionConfigCompatibility() throws Exception {
		ExtensionManifest config = ManifestExplorer.getInstance().getExtensionManifest(m_jdbcId);
		verifyExtensionConfig(config);
	}

	void verifyExtensionConfig(ExtensionManifest config) throws Exception {
		assertNotNull(config);
		assertEquals(m_jdbcId, config.getDataSourceElementID());
		assertEquals("JDBC Data Source", config.getDataSourceDisplayName());
		assertEquals("3.1", config.getOdaVersion());

		RuntimeInterface runtime = config.getRuntimeInterface();
		assertTrue(runtime instanceof JavaRuntimeInterface);
		JavaRuntimeInterface javaRuntime = (JavaRuntimeInterface) runtime;
		assertEquals("org.eclipse.birt.report.data.oda.jdbc.OdaJdbcDriver", javaRuntime.getDriverClass());
		assertFalse(javaRuntime.needSetThreadContextClassLoader());
		assertEquals(RuntimeInterface.JAVA_TYPE, javaRuntime.getInterfaceType());
		assertTrue(javaRuntime.getLibraryLocation().toString().indexOf("org.eclipse.birt.report.data.oda.jdbc") > 0);

		TraceLogging traceLogging = config.getTraceLogging();
		assertNull(traceLogging);

		myTestGetExtensionProperties(config);

		String[] dataSetTypeIds = config.getDataSetTypeIDs();
		assertTrue(dataSetTypeIds.length == 2);

		assertEquals(m_expectedJdbcDataSetId, dataSetTypeIds[0]);
		myTestGetDataSetTypes(config);
	}

	void myTestGetExtensionProperties(ExtensionManifest config) {
		Property[] dataSourceProps = config.getProperties();
		assertTrue(dataSourceProps.length >= 5);

		Hashtable propsList = new Hashtable(dataSourceProps.length);
		for (int i = 0; i < dataSourceProps.length; i++) {
			Property prop = dataSourceProps[i];
			propsList.put(prop.getName(), prop);
		}

		String expectedGroupName = "connectionProperties";
		String expectedGroupDisplayName = "Connection Properties";

		Property aProp = (Property) propsList.get("odaDriverClass");
		myTestPropertyAttributes(aProp, "JDBC Driver &Class", expectedGroupName, expectedGroupDisplayName, "string",
				true, null, false);
		aProp = (Property) propsList.get("odaURL");
		myTestPropertyAttributes(aProp, "JDBC Driver U&RL", expectedGroupName, expectedGroupDisplayName, "string", true,
				null, false);
		aProp = (Property) propsList.get("odaDataSource");
		myTestPropertyAttributes(aProp, "Data Source", expectedGroupName, expectedGroupDisplayName, "string", true,
				null, false);
		aProp = (Property) propsList.get("odaUser");
		myTestPropertyAttributes(aProp, "User &Name", expectedGroupName, expectedGroupDisplayName, "string", true, null,
				false);
		aProp = (Property) propsList.get("odaPassword");
		myTestPropertyAttributes(aProp, "Pass&word", expectedGroupName, expectedGroupDisplayName, "string", true, null,
				true);
	}

	void myTestPropertyAttributes(Property aProp, String expectedDisplayName, String expectedGroupName,
			String expectedGroupDisplayName, String expectedType, boolean expectedInheritability,
			String expectedDefaultValue, boolean expectedIsEncryptable) {
		assertNotNull(aProp);
		assertEquals(expectedDisplayName, aProp.getDisplayName());
		assertEquals(expectedGroupName, aProp.getGroupName());
		assertEquals(expectedGroupDisplayName, aProp.getGroupDisplayName());
		assertEquals(expectedType, aProp.getType());
		assertEquals(expectedInheritability, aProp.canInherit());
		assertEquals(expectedDefaultValue, aProp.getDefaultValue());
		assertEquals(expectedIsEncryptable, aProp.isEncryptable());
	}

	void myTestGetDataSetTypes(ExtensionManifest config) throws Exception {
		DataSetType[] dataSetTypes = config.getDataSetTypes();
		assertTrue(dataSetTypes.length == 2);

		DataSetType dataSetType = config.getDataSetType(m_expectedJdbcDataSetId);
		assertEquals(m_expectedJdbcDataSetId, dataSetType.getID());
		assertEquals("SQL Select Query", dataSetType.getDisplayName());

		for (int i = 0, n = sm_mappings.length; i < n; i++) {
			Integer s = (Integer) sm_mappings[i][0];
			DataTypeMapping mapping = dataSetType.getDataTypeMapping(s.shortValue());
			assertNotNull(mapping);
			assertEquals(s.intValue(), mapping.getNativeTypeCode());
			assertEquals(sm_mappings[i][1], mapping.getNativeType());
			assertEquals(sm_mappings[i][2], mapping.getOdaScalarDataType());
			assertTrue(mapping.getAlternativeOdaDataTypes().length == 0);
		}

		Property[] dataSetProps = dataSetType.getProperties();
		assertEquals(1, dataSetProps.length);
		String expectedGroupName = "queryProperties";
		String expectedGroupDisplayName = "Query Properties";

		Property aProp = dataSetProps[0];
		assertEquals("queryTimeOut", aProp.getName());
		myTestPropertyAttributes(aProp, "&Query Time Out (in seconds)", expectedGroupName, expectedGroupDisplayName,
				"string", true, null, false);

	}

	private static Object[][] sm_mappings = { { new Integer((int) -7), "BIT", "Integer" },
			{ new Integer((int) -6), "TINYINT", "Integer" }, { new Integer((int) 5), "SMALLINT", "Integer" },
			{ new Integer((int) 4), "INTEGER", "Integer" }, { new Integer((int) -5), "BIGINT", "Decimal" },
			{ new Integer((int) 6), "FLOAT", "Double" }, { new Integer((int) 7), "REAL", "Double" },
			{ new Integer((int) 8), "DOUBLE", "Double" }, { new Integer((int) 2), "NUMERIC", "Decimal" },
			{ new Integer((int) 3), "DECIMAL", "Decimal" }, { new Integer((int) 1), "CHAR", "String" },
			{ new Integer((int) 12), "VARCHAR", "String" }, { new Integer((int) -1), "LONGVARCHAR", "String" },
			{ new Integer((int) 91), "DATE", "Date" }, { new Integer((int) 92), "TIME", "Time" },
			{ new Integer((int) 93), "TIMESTAMP", "Timestamp" }, { new Integer((int) -2), "BINARY", "Blob" },
			{ new Integer((int) -3), "VARBINARY", "Blob" }, { new Integer((int) -4), "LONGVARBINARY", "Blob" },
			{ new Integer((int) 16), "BOOLEAN", "Boolean" } };
}
