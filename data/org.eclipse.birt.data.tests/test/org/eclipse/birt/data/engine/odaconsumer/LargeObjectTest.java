/*
 *************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *
 *************************************************************************
 */

package org.eclipse.birt.data.engine.odaconsumer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.Random;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.ResultClass;
import org.eclipse.birt.data.engine.executor.ResultObject;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.datatools.connectivity.oda.IBlob;
import org.eclipse.datatools.connectivity.oda.IClob;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.impl.Blob;
import org.eclipse.datatools.connectivity.oda.impl.Clob;
import org.eclipse.datatools.connectivity.oda.impl.SimpleResultSet;
import org.osgi.framework.Bundle;

import testutil.JDBCOdaDataSource;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for BLOB and CLOB support in the BIRT ODA consumer, DTP-to-BIRT ODA
 * adapter, flat file driver, and JDBC driver.
 */
public class LargeObjectTest extends ConnectionTest {

	private static final String DTP_FLATFILE_DATASET_ID = "org.eclipse.datatools.connectivity.oda.flatfile.dataSet";
	private static final String DTP_FLATFILE_DATASOURCE_ID = "org.eclipse.datatools.connectivity.oda.flatfile";
	private static final String BIRT_FLATFILE_DATASOURCE_ID = "org.eclipse.birt.report.data.oda.flatfile";

	private static Connection m_flatFileConnection = null;

	private static String sm_pluginFile = null;
	private static String sm_manifestsDir = null;
	private static String sm_pluginFileName = "plugin.xml"; //$NON-NLS-1$
	private static String sm_dtpFlatfileId = DTP_FLATFILE_DATASOURCE_ID;
	private static String sm_birtFlatfileId = BIRT_FLATFILE_DATASOURCE_ID;
	private static boolean sm_pluginTest = org.eclipse.core.runtime.Platform.isRunning();

	private static final Bundle dataBundle = org.eclipse.core.runtime.Platform.getBundle("org.eclipse.birt.data");
	private static final Bundle driverBundle = org.eclipse.core.runtime.Platform.getBundle(sm_dtpFlatfileId);

	@BeforeClass
	public static void largeObjectSetUp() throws Exception {
		setupDirectories();

		// only the last two runs require custom plugin manifests
		/*
		 * if ( getName().equals( "testFlatfileGetBlob" ) || getName().equals(
		 * "testFlatfileGetClob" ) ) setPluginFile();
		 */
		// set up flatfile test tables
		TestUtil.createTestFile();
		Properties prop = new Properties();
		System.out.println("Test database: " + new File("testdatabase").getAbsolutePath());
		prop.setProperty(TestUtil.CONN_HOME_DIR_PROP, new File("testdatabase").getAbsolutePath());
		prop.setProperty(TestUtil.CONN_CHARSET, TestUtil.CHARSET);
		m_flatFileConnection = ConnectionManager.getInstance().openConnection(DTP_FLATFILE_DATASOURCE_ID, prop, null);
	}

	@AfterClass
	public static void largeObjectTearDown() throws Exception {
		if (m_flatFileConnection != null && m_flatFileConnection.isOpen()) {
			m_flatFileConnection.close();
		}

		// if a backup plugin manifest exists, restore it
		/*
		 * if ( getName().equals( "testFlatfileGetBlob" ) || getName().equals(
		 * "testFlatfileGetClob" ) ) restorePluginFile();
		 */
	}

	/*
	 * Tests blob and clob data type support in the jdbc driver. The jdbc plugin.xml
	 * file does not have any data type mapping specified for blob and clob, so they
	 * will be mapped to the java class String.
	 */
	@Test
	public void testJdbc() throws Exception {
		String command = "select * from \"testtable_lob\"";
		PreparedStatement stmt = getConnection().prepareStatement(command, JDBCOdaDataSource.DATA_SET_TYPE);

		assertTrue(stmt.execute());
		ResultSet resultSet = stmt.getResultSet();
		assertNotNull(resultSet);

		IResultObject resultObject = resultSet.fetch();
		IResultClass resultClass = resultObject.getResultClass();

		assertEquals(2, resultClass.getFieldCount());

		String[] names = resultClass.getFieldNames();
		assertEquals("blob1", names[0]);
		assertEquals("clob1", names[1]);

		// both BLOB and CLOB native types map to java.lang.String
		assertEquals("BLOB", resultClass.getFieldNativeTypeName(1));
		assertEquals("CLOB", resultClass.getFieldNativeTypeName(2));
		assertEquals(IBlob.class, resultClass.getFieldValueClass(1));
		assertEquals(IClob.class, resultClass.getFieldValueClass(2));

		Object obj = resultObject.getFieldValue(1);
		assertNull(obj);

		obj = resultObject.getFieldValue(2);
		assertTrue(obj instanceof String);
		assertEquals("abcdefg", obj.toString());
		assertEquals("bcd", obj.toString().substring(1, 4));

		stmt.close();
	}

	/**
	 * Get substring value from Clob object
	 * 
	 * @param clob
	 * @return the substring value of clob
	 */
//    private String getClobValue( IClob clob, long pos, int len )
//	{
//        String subString = null;
//		try
//		{
//		    subString = clob.getSubString( pos, len );
//		}
//		catch ( OdaException e )
//		{
//			fail( "Failed to get Clob substring." );
//		}
//		return subString;
//	}
//
//    private String getClobValue( IClob clob )
//	{
//		BufferedReader in = null;
//		try
//		{
//			in = new BufferedReader( clob.getCharacterStream( ) );
//		}
//		catch ( OdaException e )
//		{
//			fail( "Failed to get Clob value." );
//		}
//
//		StringBuffer buffer = new StringBuffer( );
//		try
//		{
//			String str;
//			while ( ( str = in.readLine( ) ) != null )
//			{
//				buffer.append( str );
//			}
//			in.close( );
//		}
//		catch ( IOException e )
//		{
//			fail( "fail to get clob value" );
//		}
//		return buffer.toString( );
//	}

	/*
	 * Tests implementation of blob data type in flatfile driver. The blob native
	 * type is mapped to the oda data type String.
	 */
	@Test
	public void testFlatfileBlob() throws Exception {

		String command = "select blob_col from table1";
		PreparedStatement stmt = m_flatFileConnection.prepareStatement(command, DTP_FLATFILE_DATASET_ID);
		assertTrue(stmt.execute());

		ResultSet resultSet = stmt.getResultSet();
		assertNotNull(resultSet);
		IResultClass resultClass = resultSet.getMetaData();
		assertEquals(1, resultClass.getFieldCount());

		String[] names = resultClass.getFieldNames();
		assertEquals("blob_col", names[0]);
		assertEquals("BLOB", resultClass.getFieldNativeTypeName(1));
		assertEquals(String.class, resultClass.getFieldValueClass(1));

		IResultObject resultObject = resultSet.fetch();
		Object obj = resultObject.getFieldValue(1);
		assertTrue(obj instanceof String);
		assertEquals("0123456789", obj.toString());

		stmt.close();
	}

	/*
	 * Tests implementation of clob data type in flatfile driver. The clob native
	 * type is mapped to the oda data type String.
	 */
	@Test
	public void testFlatfileClob() throws Exception {
		String command = "select clob_col from table1";
		PreparedStatement stmt = m_flatFileConnection.prepareStatement(command, DTP_FLATFILE_DATASET_ID);
		assertTrue(stmt.execute());

		ResultSet resultSet = stmt.getResultSet();
		assertNotNull(resultSet);
		IResultObject resultObject = resultSet.fetch();
		IResultClass resultClass = resultObject.getResultClass();

		assertEquals(1, resultClass.getFieldCount());

		String[] names = resultClass.getFieldNames();
		assertEquals("clob_col", names[0]);

		assertEquals("CLOB", resultClass.getFieldNativeTypeName(1));

		assertEquals(String.class, resultClass.getFieldValueClass(1));

		Object obj = resultObject.getFieldValue(1);
		assertTrue(obj instanceof String);
		assertEquals("abcdefghijklmnopqrstuvwxyz", obj.toString());

		stmt.close();
	}

	/*
	 * Tests implementation of blob data type in flatfile driver by hardcoding an
	 * IBlob value returned from the resultset. The blob native type is mapped to
	 * the oda data type Blob.
	 */
	@Test
	public void testFlatfileGetBlob() throws Exception {
		/*
		 * TODO - replace with customized test driver, instead of using a testing
		 * plugin.xml String command = "select blob_col from table1"; PreparedStatement
		 * stmt = m_flatFileConnection.prepareStatement( command,
		 * DTP_FLATFILE_DATASET_ID ); assertTrue( stmt.execute() );
		 * 
		 * ResultSet resultSet = new ResultSetLob( stmt.getResultSet().getMetaData() );
		 * assertNotNull( resultSet ); IResultObject resultObject = resultSet.fetch();
		 * IResultClass resultClass = resultObject.getResultClass();
		 * 
		 * assertEquals( 1, resultClass.getFieldCount() );
		 * 
		 * String[] names = resultClass.getFieldNames(); assertEquals( "blob_col",
		 * names[0] );
		 * 
		 * assertEquals( "BLOB", resultClass.getFieldNativeTypeName( 1 ) );
		 * 
		 * assertEquals( org.eclipse.datatools.connectivity.oda.IBlob.class,
		 * resultClass.getFieldValueClass( 1 ) );
		 * 
		 * Object obj = resultObject.getFieldValue( 1 ); assertTrue( obj instanceof
		 * byte[] ); byte[] objValue = (byte[]) obj; for ( int i = 0; i <
		 * objValue.length; i += 1 ) assertEquals( i, objValue[i] ); // IBlob blob =
		 * (IBlob) obj; // InputStream stream = blob.getBinaryStream(); // for( int i =
		 * 0 , c = stream.read(); c != -1; i += 1, c = stream.read() ) // assertEquals(
		 * i, c ); stmt.close();
		 */ }

	/*
	 * Tests implementation of clob data type in flatfile driver by hardcoding an
	 * IClob value returned from the resultset. The clob native type is mapped to
	 * the oda data type Clob.
	 */
	@Test
	public void testFlatfileGetClob() throws Exception {
		/*
		 * TODO - replace with customized test driver, instead of using a testing
		 * plugin.xml String command = "select clob_col from table1"; PreparedStatement
		 * stmt = m_flatFileConnection.prepareStatement( command,
		 * DTP_FLATFILE_DATASET_ID ); assertTrue( stmt.execute() );
		 * 
		 * ResultSet resultSet = new ResultSetLob( stmt.getResultSet().getMetaData() );
		 * assertNotNull( resultSet ); IResultObject resultObject = resultSet.fetch();
		 * IResultClass resultClass = resultObject.getResultClass();
		 * 
		 * assertEquals( 1, resultClass.getFieldCount() );
		 * 
		 * String[] names = resultClass.getFieldNames(); assertEquals( "clob_col",
		 * names[0] );
		 * 
		 * assertEquals( "CLOB", resultClass.getFieldNativeTypeName( 1 ) );
		 * 
		 * assertEquals( org.eclipse.datatools.connectivity.oda.IClob.class,
		 * resultClass.getFieldValueClass( 1 ) );
		 * 
		 * Object obj = resultObject.getFieldValue( 1 ); assertTrue( obj instanceof
		 * String ); char[] chs = obj.toString( ).toCharArray( ); for ( int i = 0; i <
		 * chs.length; i += 1 ) { assertEquals( 'a' + i, chs[i] ); }
		 * 
		 * // assertTrue( obj instanceof IClob ); // IClob clob = (IClob) obj; // Reader
		 * reader = clob.getCharacterStream(); // assertEquals( 26, clob.length() ); //
		 * for ( int i = 0; i < clob.length(); i += 1) // { // assertEquals( 'a' + i,
		 * reader.read() ); // }
		 * 
		 * stmt.close();
		 */ }

	private static void setupDirectories() throws IOException {
		String dataDir;

		if (sm_pluginFile != null && sm_manifestsDir != null)
			return; // already set, so return early

		if (sm_pluginTest) {
			// set directory where test plugin.xml files are stored
			URL url = dataBundle.getEntry("/");
			dataDir = FileLocator.toFileURL(url).getPath();

			// set location where plugin.xml will be parsed
			URL jdbcUrl = driverBundle.getEntry("/");
			sm_pluginFile = FileLocator.toFileURL(jdbcUrl).getPath() + sm_pluginFileName;
		} else {
			// set directory where test plugin.xml files are stored
			dataDir = ".";

			// set location where plugin.xml will be parsed
			sm_pluginFile = dataDir + "/test/plugins/" + sm_dtpFlatfileId + "/" + sm_pluginFileName;
			System.setProperty("BIRT_HOME", dataDir + "/test");
		}

		sm_manifestsDir = dataDir + "/test/plugins/" + sm_birtFlatfileId + "/manifests/";
	}

	/*
	 * This class is used to access the protected member variable 'platform' in the
	 * Platform class. This testclass uses the 'setPlatform' method to create new
	 * ServerPlatform objects, each testing a different ExtensionRegistry.
	 */
//    private static class PlatformAccessor extends Platform
//    {
//        public static void setPlatform( IPlatform p )
//        {
//            platform = p;
//        }
//    }

	/*
	 * ResultSetLob test class for hard-coded BLOB and CLOB values
	 */
	private class ResultSetLob extends ResultSet {
		private IResultClass m_resultClass = null;

		public ResultSetLob(IResultClass resultClass) {
			// create a placeholder IResultSet object,
			// which isn't used in this test
			super(new SimpleResultSet(), resultClass);

			m_resultClass = resultClass;
		}

		public IResultObject fetch() throws DataException {
			int columnCount = m_resultClass.getFieldCount();
			int[] driverPositions = ((ResultClass) m_resultClass).getFieldDriverPositions();
			assert (columnCount == driverPositions.length);

			Object[] fields = new Object[columnCount];

			for (int i = 1; i <= columnCount; i++) {
				if (m_resultClass.isCustomField(i) == true)
					continue;

				Class dataType = m_resultClass.getFieldValueClass(i);
				Object colValue = null;

				if (dataType == IClob.class)
					colValue = getClob();
				else if (dataType == IBlob.class)
					colValue = getBlob();
				else
					assert false;

				fields[i - 1] = colValue;
			}

			IResultObject ret = new ResultObject(m_resultClass, fields);

			return ret;
		}

		private Object getBlob() {
			byte[] bytes = new byte[] { 0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9 };
			return new Blob(bytes);
		}

		private Object getClob() {
			return new Clob("abcdefghijklmnopqrstuvwxyz");
		}
	}

	/*
	 * TestUtil class for generating test data, taken from flatfile test package
	 */
	private static class TestUtil {

		public static final String CHARSET = "UTF-16BE";

		public static final String CONN_HOME_DIR_PROP = "HOME"; //$NON-NLS-1$

		public static final String CONN_CHARSET = "CHARSET"; //$NON-NLS-1$

		public static final String CONN_INCLTYPELINE = "INCLTYPELINE"; //$NON-NLS-1$

		public static void createTestFile() throws OdaException {
			createTestFileDirectory();
			createTestFile_test1();
		}

		private static void createTestFileDirectory() throws OdaException {
			File file = new File("testdatabase");
			if (file.exists())
				return;
			try {
				file.mkdirs();
			} catch (SecurityException e) {
				throw new OdaException(e.getMessage());
			}
		}

		private static void createTestFile_test1() throws OdaException {
			File file = new File("testdatabase" + File.separator + "table1");
			if (file.exists())
				return;
			try {
				FileOutputStream fos = new FileOutputStream(file);// "test1.csv");
				OutputStreamWriter osw = new OutputStreamWriter(fos, CHARSET);

				Random r = new Random();
				String comma = new String(",");
				String endOfLine = new String("\n");
				osw.flush();
				String header = "INT0_COL,DOUBLE0_COL,CLOB_COL,DATE_COL,TIME_COL,"
						+ "TIMESTAMP_COL,BLOB_COL,INT1_COL,DOUBLE1_COL,BIGDECIMAL_COL\n";
				String types = "INT, DOUBLE, CLOB, DATE, TIME, TIMESTAMP, BLOB, INT, DOUBLE, BIGDECIMAL\n";

				osw.write(header);
				osw.write(types);
				for (int i = 0; i < 1234; i++) {
					for (int j = 0; j < 10; j++) {
						if (j == 0)
							osw.write(Integer.toString(i));
						if (j == 1 || j == 8)
							osw.write(Double.toString(r.nextDouble()));
						if (j == 2)
							osw.write("abcdefghijklmnopqrstuvwxyz");
						if (j == 3) {
							int year = 1000 + i;
							int month = i % 12 + 1;
							int day = i % 28 + 1;
							String s = Integer.toString(year) + "-" + Integer.toString(month) + "-"
									+ Integer.toString(day);
							osw.write(s);
						}

						if (j == 4)
							osw.write(Time.valueOf(i % 24 + ":" + j + ":00").toString());
						if (j == 5)
							osw.write(Integer.toString(new Timestamp(System.currentTimeMillis()).getNanos()));
						if (j == 6) {
							osw.write(new String("0123456789"));
						}
						if (j == 7)
							osw.write(Integer.toString(r.nextInt()));
						if (j == 9)
							osw.write(Double.toString(r.nextDouble()));
						if (j < 9)
							osw.write(comma);
					}
					osw.write(endOfLine);
				}
				osw.close();
			} catch (Exception e) {
				throw new OdaException(e.getMessage());
			}
		}
	}

}
