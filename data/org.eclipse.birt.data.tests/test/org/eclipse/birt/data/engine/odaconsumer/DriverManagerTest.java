/*
 *************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.data.engine.odaconsumer;

import java.sql.Types;
import org.eclipse.datatools.connectivity.oda.IDriver;

import testutil.JDBCOdaDataSource;

import static org.junit.Assert.*;

public class DriverManagerTest extends OdaconsumerTestCase {
	public final void testGetInstance() {
		assertNotNull(DriverManager.getInstance());
	}

	public final void testGetDriver() throws Exception {
		IDriver driverHelper = DriverManager.getInstance().getDriverHelper(JDBCOdaDataSource.DATA_SOURCE_TYPE);
		assertNotNull(driverHelper);

		// we got the odaconsumer manager wrapper, which should be loaded by the same
		// classloader as this test
		/*
		 * assertEquals( this.getClass().getClassLoader(),
		 * driverHelper.getClass().getClassLoader() );
		 */ }

	public final void testGetExtensionDataSourceType() throws Exception {
		String dataSourceType = DriverManager.getInstance()
				.getExtensionDataSourceId(JDBCOdaDataSource.DATA_SOURCE_TYPE);
		assertEquals(JDBCOdaDataSource.DATA_SOURCE_TYPE, dataSourceType);
	}

	public final void testGetNativeToOdaMapping() throws Exception {
		testConversion(Types.BIGINT, Types.DECIMAL);
		testConversion(Types.BINARY, Types.BLOB);
		testConversion(Types.BIT, Types.INTEGER);
		testConversion(Types.BOOLEAN, Types.BOOLEAN);
		testConversion(Types.CHAR, Types.CHAR);
		testConversion(Types.DATE, Types.DATE);
		testConversion(Types.DECIMAL, Types.DECIMAL);
		testConversion(Types.DOUBLE, Types.DOUBLE);
		testConversion(Types.FLOAT, Types.DOUBLE);
		testConversion(Types.INTEGER, Types.INTEGER);
		testConversion(Types.LONGVARBINARY, Types.BLOB);
		testConversion(Types.LONGVARCHAR, Types.CHAR);
		testConversion(Types.NUMERIC, Types.DECIMAL);
		testConversion(Types.REAL, Types.DOUBLE);
		testConversion(Types.SMALLINT, Types.INTEGER);
		testConversion(Types.TIME, Types.TIME);
		testConversion(Types.TIMESTAMP, Types.TIMESTAMP);
		testConversion(Types.TINYINT, Types.INTEGER);
		testConversion(Types.VARBINARY, Types.BLOB);
		testConversion(Types.VARCHAR, Types.CHAR);
	}

	private void testConversion(int nativeType, int expectedOdaType) throws Exception {
		assertEquals(expectedOdaType, DataTypeUtil.toOdaType(nativeType, JDBCOdaDataSource.DATA_SOURCE_TYPE,
				JDBCOdaDataSource.DATA_SET_TYPE));
	}
}
