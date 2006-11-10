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

package org.eclipse.birt.report.model.parser;

import java.util.Iterator;

import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.HighlightRuleHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.PrivateStyleHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.TextDataHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.elements.DataItem;
import org.eclipse.birt.report.model.elements.interfaces.IDataSetModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Tests parser compatibility.
 */

public class ParserCompatibilityTest extends BaseTestCase
{

	private String resultSetClearFileName = "CompatibleResultSetClearTest.xml";//$NON-NLS-1$
	private String resultSetHintClearFileName = "CompatibleResultSetHintClearTest.xml";//$NON-NLS-1$

	/*
	 * @see BaseTestCase#setUp()
	 */

	protected void setUp( ) throws Exception
	{
		Platform.startup( null );
		DesignEngine.newSession( ULocale.ENGLISH );
	}

	/**
	 * Test clear 'resultSet' property before version 3.2.2
	 * 
	 * @throws Exception
	 */

	public void testClearResultSet( ) throws Exception
	{
		openDesign( resultSetClearFileName );
		OdaDataSetHandle odaHandle = (OdaDataSetHandle) designHandle
				.getElementByID( 5 );
		assertNull( odaHandle.getListProperty( IDataSetModel.RESULT_SET_PROP ) );

	}

	/**
	 * Test clear 'resultSetHint' property between version 3.2.2 and 3.2.6
	 * 
	 * @throws Exception
	 */

	public void testClearResultSetHint( ) throws Exception
	{
		openDesign( resultSetHintClearFileName );
		OdaDataSetHandle odaHandle = (OdaDataSetHandle) designHandle
				.getElementByID( 5 );
		assertNull( odaHandle
				.getListProperty( IDataSetModel.RESULT_SET_HINTS_PROP ) );

	}

	/**
	 * Tests the compatibility for private and public driver properties in ODA
	 * data source.
	 * 
	 * @throws Exception
	 *             if any exception
	 */

	public void testPrivateOdaDriverProperties( ) throws Exception
	{
		openDesign( "CompatiblePrivateOdaDriverProperties.xml" ); //$NON-NLS-1$
		save();

		assertTrue( compareFile(
				"CompatiblePrivateOdaDriverProperties_golden.xml") );//$NON-NLS-1$

		openDesign( "CompatiblePublicOdaDriverProperties.xml" ); //$NON-NLS-1$
		save();

		assertTrue( compareFile(
				"CompatiblePublicOdaDriverProperties_golden.xml") );//$NON-NLS-1$
	}

	/**
	 * Tests the compatibility for old ODA driver model property name in ODA
	 * data source.
	 * 
	 * @throws Exception
	 *             if any exception
	 */

	public void testOldOdaDriverModelPropertyName( ) throws Exception
	{
		openDesign( "CompatibleOldOdaDriverModelProperty.xml" );//$NON-NLS-1$

		save();

		assertTrue( compareFile(
				"CompatibleOldOdaDriverModelProperty_golden.xml") );//$NON-NLS-1$
	}

	/**
	 * Tests the compatibility for the design file created from the deprecated
	 * extension point odaDriverModel.
	 * 
	 * @throws Exception
	 *             if any exception
	 */
	public void testOdaDriverModelExtensionPoint( ) throws Exception
	{
		openDesign( "CompatibleOdaDriverModelProperty.xml" );//$NON-NLS-1$

		save();

		assertTrue( compareFile(
				"CompatibleOdaDriverModelProperty_golden.xml") );//$NON-NLS-1$
	}

	/**
	 * Tests the compatibility for encrypted property.
	 * 
	 * @throws Exception
	 *             if any exception
	 */

	public void testEncryptedProperty( ) throws Exception
	{
		openDesign( "CompatibleEncryptedProperty.xml" );//$NON-NLS-1$

		save();

		// Now it's hard to test.
		// assertTrue( compareTextFile(
		// "CompatibleEncryptedProperty_golden.xml") );//$NON-NLS-1$

	}

	/**
	 * Tests the compatibility for "columnName" member of ComputedColumn
	 * structure. "columnName" is renamed to "name".
	 * 
	 * @throws Exception
	 */

	public void testComputedColumnsProperty( ) throws Exception
	{
		openDesign( "CompatibleComputedColumnProperty.xml" );//$NON-NLS-1$
		save(); 
		assertTrue( compareFile(
				"CompatibleComputedColumnProperty_golden.xml") );//$NON-NLS-1$

	}

	/**
	 * @throws Exception
	 */

	public void testOdaDataSetproperty( ) throws Exception
	{
		openDesign( "CompatibleOdaDataSetProperty.xml" );//$NON-NLS-1$

		OdaDataSetHandle dataSetHandle = (OdaDataSetHandle) designHandle
				.findDataSet( "dataset1" ); //$NON-NLS-1$
		assertNotNull( dataSetHandle );
		assertEquals( null, dataSetHandle.getQueryScript( ) );

		save(); 
		assertTrue( compareFile( "CompatibleOdaDataSetProperty_golden.xml") );//$NON-NLS-1$
	}

	/**
	 * Old version: <property name="msgBaseName">message </property> New
	 * version: <property name="includeResource">message </property>
	 * <p>
	 * Old version: <property name="cheetSheet">cheet sheet </property> <br>
	 * New version: <property name="cheatSheet">cheat sheet </property>
	 * 
	 * @throws Exception
	 */

	public void testReportProperty( ) throws Exception
	{
		openDesign( "CompatibleReportProperties.xml" );//$NON-NLS-1$
		assertEquals( "message", this.designHandle.getIncludeResource( ) ); //$NON-NLS-1$
		assertEquals( "cheet sheet", this.designHandle.getCheatSheet( ) ); //$NON-NLS-1$

		save(); 
		assertTrue( compareFile( "CompatibleReportProperties_golden.xml") );//$NON-NLS-1$
	}

	/**
	 * Old version: <property name="groupStart">message </property> New version:
	 * <property name="intervalBase">message </property>
	 * 
	 * @throws Exception
	 */

	public void testListingGroupProperty( ) throws Exception
	{
		openDesign( "CompatibleListingGroupProperties.xml" );//$NON-NLS-1$
		ListHandle list = (ListHandle) designHandle.findElement( "My List" ); //$NON-NLS-1$
		SlotHandle groupSlot = list.getGroups( );
		GroupHandle group = (GroupHandle) groupSlot.get( 0 );

		assertEquals( "2004/12/12", group.getGroupStart( ) ); //$NON-NLS-1$
		group.setGroupStart( "101" ); //$NON-NLS-1$

		save(); 
		assertTrue( compareFile(
				"CompatibleListingGroupProperties_golden.xml") );//$NON-NLS-1$
	}

	/**
	 * Old version: <expression name="contentTypeExpr">a.row </expression>
	 * <p>
	 * New version: <property name="contentType">html </property>
	 * 
	 * @throws Exception
	 *             if any exception
	 */

	public void testTextDataProperty( ) throws Exception
	{
		openDesign( "CompatibleTextDataProperties.xml" );//$NON-NLS-1$
		TextDataHandle dataHandle = (TextDataHandle) designHandle
				.findElement( "Multi Line Data" ); //$NON-NLS-1$
		assertNotNull( dataHandle.getElement( ) );
		assertEquals( "value expr", dataHandle.getValueExpr( ) ); //$NON-NLS-1$
		assertEquals( "content type expr", dataHandle.getContentTypeExpr( ) ); //$NON-NLS-1$
		assertEquals( "content type expr", dataHandle.getContentType( ) ); //$NON-NLS-1$

		dataHandle = (TextDataHandle) designHandle.findElement( "Text Data" ); //$NON-NLS-1$
		assertEquals( DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_RTF,
				dataHandle.getContentType( ) );
		dataHandle
				.setContentType( DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_PLAIN );

		save(); 
		assertTrue( compareFile( "CompatibleTextDataProperties_golden.xml") );//$NON-NLS-1$
	}

	/**
	 * Tests OdaDataSource with driver name or extension name.
	 * 
	 * @throws Exception
	 *             if any exception
	 */

	public void testOdaDataSourceWithDriverNameAndExtensionName( )
			throws Exception
	{
		openDesign( "CompatibleOdaDataSourceWithDriverNameOrExtensionName.xml" );//$NON-NLS-1$

		save();

		assertTrue( compareFile(
				"CompatibleOdaDataSourceWithDriverNameOrExtensionName_golden.xml") );//$NON-NLS-1$
	}

	/**
	 * Tests OdaDataSet with type
	 * 
	 * @throws Exception
	 *             if any exception
	 */

	public void testOdaDataSetWithType( ) throws Exception
	{
		openDesign( "CompatibleOdaDataSetWithType.xml" );//$NON-NLS-1$

		save();

		assertTrue( compareFile( "CompatibleOdaDataSetWithType_golden.xml") );//$NON-NLS-1$
	}

	/**
	 * Tests DataSetParam structure with isNullable.
	 * 
	 * @throws Exception
	 *             if any exception
	 */

	public void testDataSetParamWithIsNullable( ) throws Exception
	{
		openDesign( "CompatibleDataSetParamWithIsNullable.xml" );//$NON-NLS-1$

		save();

		assertTrue( compareFile(
				"CompatibleDataSetParamWithIsNullable_golden.xml") );//$NON-NLS-1$
	}

	/**
	 * @throws Exception
	 */
	public void testTableItemHighlightRule( ) throws Exception
	{

		openDesign( "TableItemCompatibleTest.xml" ); //$NON-NLS-1$
		TableHandle tableHandle = (TableHandle) designHandle
				.findElement( "My table" ); //$NON-NLS-1$
		assertNotNull( tableHandle );

		PrivateStyleHandle styleHandle = (PrivateStyleHandle) tableHandle
				.getPrivateStyle( );

		Iterator highlightHandles = styleHandle.highlightRulesIterator( );

		HighlightRuleHandle highlightHandle = (HighlightRuleHandle) highlightHandles
				.next( );
		assertNotNull( highlightHandle );
		assertEquals( "[this]", highlightHandle.getTestExpression( ) ); //$NON-NLS-1$
		assertEquals( "is-null", highlightHandle.getOperator( ) ); //$NON-NLS-1$
		save(); 

		assertTrue( compareFile( "TableHighlightRuleCompatible_golden.xml") ); //$NON-NLS-1$

	}

	/**
	 * @throws Exception
	 */
	public void testDataHighlightRule( ) throws Exception
	{
		openDesign( "DataItemCompatibleTest.xml" ); //$NON-NLS-1$
		DataItem data = (DataItem) design.findElement( "Body Data" ); //$NON-NLS-1$
		DataItemHandle dataHandle = (DataItemHandle) data.getHandle( design );
		assertNotNull( dataHandle );

		PrivateStyleHandle styleHandle = (PrivateStyleHandle) dataHandle
				.getPrivateStyle( );

		Iterator highlightHandles = styleHandle.highlightRulesIterator( );

		HighlightRuleHandle highlightHandle = (HighlightRuleHandle) highlightHandles
				.next( );
		assertNotNull( highlightHandle );
		assertEquals( "[this]", highlightHandle.getTestExpression( ) ); //$NON-NLS-1$
		assertEquals( "is-null", highlightHandle.getOperator( ) ); //$NON-NLS-1$
		save(); 

		assertTrue( compareFile(
				"DataItemHighlightRuleCompatible_golden.xml") ); //$NON-NLS-1$

	}

	/**
	 * @throws Exception
	 */
	public void testWrongExtensionID( ) throws Exception
	{
		openDesign( "WrongExtensionID.xml" );//$NON-NLS-1$

		save();

		assertTrue( compareFile( "WrongExtensionID_golden.xml") );//$NON-NLS-1$
	}

	/**
	 * Test cases:
	 * 
	 * "onRow" property value of table/list is set to be "onCreate" of detail
	 * rows.
	 * 
	 * "onFinish", "onStart" property values are ignored.
	 * 
	 * @throws Exception
	 */

	public void testOnMumbleProperty( ) throws Exception
	{
		openDesign( "TableOnMumbleCompatibleTest.xml" ); //$NON-NLS-1$
		save();

		assertTrue( compareFile( "TableOnMumbleCompatibleTest_golden.xml") ); //$NON-NLS-1$
	}

	/**
	 * @throws Exception
	 */
	public void testIncludedLibraryCompatible( ) throws Exception
	{

		openDesign( "IncludedLibraryCompatibleTest.xml" ); //$NON-NLS-1$
		save();

		assertTrue( compareFile(
				"IncludedLibraryCompatibleTest_golden.xml") ); //$NON-NLS-1$

	}

	/**
	 * Test compatibility of page breaks.
	 * 
	 * @throws Exception
	 * 
	 */

	public void testCompatibilityOfPageBreaks( ) throws Exception
	{
		openDesign( "CompatiblePageBreaks.xml" ); //$NON-NLS-1$
		TableHandle table = (TableHandle) designHandle.findElement( "table1" ); //$NON-NLS-1$
		assertEquals( DesignChoiceConstants.PAGE_BREAK_AFTER_ALWAYS,
				( (GroupHandle) table.getGroups( ).get( 0 ) )
						.getProperty( IStyleModel.PAGE_BREAK_AFTER_PROP ) );
		assertEquals( DesignChoiceConstants.PAGE_BREAK_AFTER_AUTO, table
				.getProperty( IStyleModel.PAGE_BREAK_AFTER_PROP ) );
		assertEquals( DesignChoiceConstants.PAGE_BREAK_BEFORE_ALWAYS, table
				.getProperty( IStyleModel.PAGE_BREAK_BEFORE_PROP ) );
	}

	/**
	 * Test cases:
	 * 
	 * "onRow" property value of table/list is set to be "onCreate" of detail
	 * rows.
	 * 
	 * "onFinish", "onStart" property values are ignored.
	 * 
	 * @throws Exception
	 */

	public void testScalarParameterFormatCompatible( ) throws Exception
	{
		openDesign( "CompatibleScalarParameterFormat.xml" ); //$NON-NLS-1$
		save();

		assertTrue( compareFile(
				"CompatibleScalarParameterFormat_golden.xml") ); //$NON-NLS-1$
	}

	/**
	 * Test all properties of the obsolete multi-line-data.
	 * 
	 * @throws Exception
	 *             if opening design file failed.
	 */

	public void testObsoleteParser( ) throws Exception
	{
		openDesign( "TextDataItemParseTest_obsolete.xml" ); //$NON-NLS-1$
		TextDataHandle dataHandle = (TextDataHandle) designHandle
				.findElement( "Multi Line Data" ); //$NON-NLS-1$
		assertNotNull( dataHandle.getElement( ) );
		assertEquals( "value expr", dataHandle.getValueExpr( ) ); //$NON-NLS-1$
		assertEquals( "content type expr", dataHandle.getContentTypeExpr( ) ); //$NON-NLS-1$
	}

	/**
	 * @throws Exception
	 */

	public void testImageName( ) throws Exception
	{
		openDesign( "CompatibleImageNameParseTest.xml" ); //$NON-NLS-1$
		ImageHandle image = (ImageHandle) designHandle.findElement( "Image1" ); //$NON-NLS-1$
		assertNull( image
				.getProperty( ReportItemHandle.BOUND_DATA_COLUMNS_PROP ) );
		assertEquals( "image.jpg1", image.getImageName( ) ); //$NON-NLS-1$
	}

	/**
	 * The backward compatibility for the old design in BIRT 1.0 or before. The
	 * flat file extension is not datatools.connectivity.oda....
	 * 
	 * @throws Exception
	 */

	public void testFlatfileExtendsionId( ) throws Exception
	{
		openDesign( "CompatibleFlatFileExtensionId.xml" ); //$NON-NLS-1$
		OdaDataSourceHandle source = (OdaDataSourceHandle) designHandle
				.findDataSource( "Data Source1" ); //$NON-NLS-1$
		assertEquals( "org.eclipse.datatools.connectivity.oda.flatfile", source //$NON-NLS-1$
				.getExtensionID( ) );

		OdaDataSetHandle set = (OdaDataSetHandle) designHandle
				.findDataSet( "Data Set1" ); //$NON-NLS-1$
		assertEquals(
				"org.eclipse.datatools.connectivity.oda.flatfile.dataSet", set //$NON-NLS-1$
						.getExtensionID( ) );
	}

	/**
	 * In BIRT 2.1.1, OdaDataSet.resultSetHints was removed. And the obsolete
	 * OdaDataSet.resultSetHints and OdaDataSet.resultSet are merged into the
	 * new OdaDataSet.resultSet.
	 * <p>
	 * The rule is by taking 1) the current ResultSet�s column name as the
	 * �nativeName�, and 2) the ResultSetHints�s column name as the �name�, in
	 * the merged OdaResultSetColumn structure.
	 * 
	 * 
	 * @throws Exception
	 */

	public void testOdaResultSets( ) throws Exception
	{
		openDesign( "CompatibleOdaResultSetTest.xml" ); //$NON-NLS-1$

		save(); 

		assertTrue( compareFile( "CompatibleOdaResultSet_golden.xml") ); //$NON-NLS-1$
	}

	/**
	 * ScriptdaDataSet.resultSet was removed. And the obsolete
	 * ScriptdaDataSet.resultSet are renamed to the new
	 * ScriptdaDataSet.resultSetHints.
	 * 
	 * @throws Exception
	 * 
	 */

	public void testScriptDataSetResultSets( ) throws Exception
	{
		openDesign( "CompatibleScriptDataSetResultSets.xml" ); //$NON-NLS-1$

		save(); 

		assertTrue( compareFile(
				"CompatibleScriptDataSetResultSets_golden.xml") ); //$NON-NLS-1$
	}

	/**
	 * Tests parse odadataset and odadatasouce if the extensionid is deprecated ,
	 * convert it to new one. this function apply after version 3.2.7
	 * 
	 * @throws Exception
	 */

	public void testMigrateNameSpace( ) throws Exception
	{
		openDesign( "CompatibleConvertExtensionId.xml" );//$NON-NLS-1$
		OdaDataSourceHandle dataSource = (OdaDataSourceHandle) designHandle
				.findDataSource( "Data Source" ); //$NON-NLS-1$
		assertNotNull( dataSource );
		assertEquals(
				"org.eclipse.datatools.enablement.oda.xml", dataSource.getExtensionID( ) );//$NON-NLS-1$
		
		OdaDataSetHandle dataSet = (OdaDataSetHandle) designHandle
				.findDataSet( "Data Set" ); //$NON-NLS-1$
		assertNotNull( dataSet );
		assertEquals(
				"org.eclipse.datatools.enablement.oda.xml.dataSet", dataSet.getExtensionID( ) );//$NON-NLS-1$

	}
}
