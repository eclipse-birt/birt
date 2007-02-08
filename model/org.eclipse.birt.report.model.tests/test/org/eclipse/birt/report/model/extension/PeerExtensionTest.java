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

package org.eclipse.birt.report.model.extension;

import java.util.List;

import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IDesignElement;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.IColorConstants;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.interfaces.IImageItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ExtensionElementDefn;
import org.eclipse.birt.report.model.metadata.ExtensionPropertyDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * New cases for report item extension should be added here.
 * <p>
 * Tests the cases for the peer extension enhancement: slot definition.
 */

public class PeerExtensionTest extends BaseTestCase
{

	protected static final String HEADER_PROP = "header"; //$NON-NLS-1$
	protected static final String DETAIL_PROP = "detail"; //$NON-NLS-1$
	protected static final String FOOTER_PROP = "footer"; //$NON-NLS-1$
	protected static final String TESTING_BOX_NAME = "TestingBox"; //$NON-NLS-1$
	private static final String FILE_NAME = "PeerExtensionTest.xml"; //$NON-NLS-1$
	private static final String FILE_NAME_1 = "PeerExtensionTest_1.xml"; //$NON-NLS-1$
	private static final String FILE_NAME_2 = "PeerExtensionTest_2.xml"; //$NON-NLS-1$
	private static final String FILE_NAME_3 = "PeerExtensionTest_3.xml"; //$NON-NLS-1$
	private static final String FILE_NAME_5 = "PeerExtensionTest_5.xml";//$NON-NLS-1$

	private static final String POINTS_PROP_NAME = "points"; //$NON-NLS-1$

	private static final String TESTING_TABLE_NAME = "TestingTable"; //$NON-NLS-1$

	/**
	 * The extension do not have its own model.
	 */

	private static final String FILE_NAME_4 = "PeerExtensionTest_4.xml"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */

	protected void setUp( ) throws Exception
	{
		super.setUp( );
		ThreadResources.setLocale( ULocale.ENGLISH );
	}

	/**
	 * Test initializeReportItem method. when element has virtual parent, also
	 * can be initialized.
	 * 
	 * @throws Exception
	 */

	public void testVirtualExtension( ) throws Exception
	{
		openDesign( FILE_NAME_5 );

		ExtendedItemHandle handle = (ExtendedItemHandle) designHandle
				.findElement( "newHeaderMatrix" ); //$NON-NLS-1$
		assertNotNull( handle );
	}

	/**
	 * Tests the parser for the extension and the TestPeer--implementation of
	 * IPeer.
	 */

	public void testExtensionMeta( )
	{
		MetaDataDictionary dd = MetaDataDictionary.getInstance( );
		assertTrue( dd.getExtensions( ).size( ) >= 2 );

		ExtensionElementDefn extDefn = (ExtensionElementDefn) dd
				.getExtension( TESTING_BOX_NAME );
		assertNotNull( extDefn );
		assertEquals( "TestingBox", extDefn.getDisplayName( ) ); //$NON-NLS-1$
		assertNull( extDefn.getDisplayNameKey( ) );
		assertEquals( TESTING_BOX_NAME, extDefn.getName( ) );
		assertEquals( MetaDataConstants.REQUIRED_NAME, extDefn.getNameOption( ) );
		assertEquals( true, extDefn.allowsUserProperties( ) );
		assertEquals( TESTING_BOX_NAME, extDefn.getName( ) );
		assertEquals( extDefn.getXmlName( ), ( (ElementDefn) dd
				.getElement( ReportDesignConstants.EXTENDED_ITEM ) )
				.getXmlName( ) );

		// test the list property type
		PropertyDefn propDefn = (PropertyDefn) extDefn
				.getProperty( POINTS_PROP_NAME );
		assertEquals( PropertyType.LIST_TYPE, propDefn.getTypeCode( ) );
		assertEquals( PropertyType.FLOAT_TYPE, propDefn.getSubTypeCode( ) );

		// get the slot property definitions
		assertTrue( extDefn.isContainer( ) );
		assertEquals( 0, extDefn.getSlotCount( ) );
		// header slot
		PropertyDefn slotPropertyDefn = (PropertyDefn) extDefn
				.getProperty( HEADER_PROP );
		assertEquals( IPropertyType.ELEMENT_TYPE, slotPropertyDefn
				.getTypeCode( ) );
		assertEquals(
				"Element.TestingBox.slot.header", slotPropertyDefn.getDisplayNameID( ) ); //$NON-NLS-1$
		assertEquals( "defaultHeader", slotPropertyDefn.getDisplayName( ) ); //$NON-NLS-1$
		assertFalse( slotPropertyDefn.isList( ) );
		List allowedElements = slotPropertyDefn.getAllowedElements( false );
		assertEquals( 3, allowedElements.size( ) );
		assertTrue( allowedElements.contains( dd
				.getElement( ReportDesignConstants.LABEL_ITEM ) ) );
		assertTrue( allowedElements.contains( dd
				.getElement( ReportDesignConstants.GRID_ITEM ) ) );
		assertTrue( allowedElements.contains( dd.getElement( "TestingMatrix" ) ) ); //$NON-NLS-1$
		// detail slot
		slotPropertyDefn = (PropertyDefn) extDefn.getProperty( DETAIL_PROP );
		assertEquals(
				"Element.TestingBox.slot.detail", slotPropertyDefn.getDisplayNameID( ) ); //$NON-NLS-1$
		assertEquals( "defaultDetail", slotPropertyDefn.getDisplayName( ) ); //$NON-NLS-1$
		assertTrue( slotPropertyDefn.isList( ) );
		assertEquals( 4, slotPropertyDefn.getAllowedElements( false ).size( ) );
		assertTrue( slotPropertyDefn.getAllowedElements( ).size( ) > 4 );
		// footer slot
		slotPropertyDefn = (PropertyDefn) extDefn.getProperty( FOOTER_PROP );
		assertEquals( "footer", slotPropertyDefn.getName( ) ); //$NON-NLS-1$
		assertEquals(
				"Element.TestingBox.slot.footer", slotPropertyDefn.getDisplayNameID( ) ); //$NON-NLS-1$
		assertEquals( "defaultFooter", slotPropertyDefn.getDisplayName( ) ); //$NON-NLS-1$
		assertFalse( slotPropertyDefn.isList( ) );

	}

	/**
	 * 
	 * @throws Exception
	 */

	public void testParser( ) throws Exception
	{
		openDesign( FILE_NAME );
		ExtendedItemHandle extendedItem = (ExtendedItemHandle) designHandle
				.findElement( "testBox" ); //$NON-NLS-1$
		assertNotNull( extendedItem );

		// test the list property

		List points = (List) extendedItem.getProperty( POINTS_PROP_NAME );
		assertEquals( 3, points.size( ) );
		assertEquals( 13.1, ( (Double) points.get( 0 ) ).doubleValue( ), 0.001 );
		assertEquals( 14, ( (Double) points.get( 1 ) ).doubleValue( ), 0.001 );
		assertEquals( 15.678, ( (Double) points.get( 2 ) ).doubleValue( ),
				0.001 );

		// test header slot: value and the content element is added to namespace
		// and id-map
		Object slotPropertyVaiue = extendedItem.getProperty( HEADER_PROP );
		ExtendedItemHandle contentExtendedItem = (ExtendedItemHandle) slotPropertyVaiue;
		assertEquals( "headerMatrix", contentExtendedItem.getName( ) ); //$NON-NLS-1$
		assertEquals( contentExtendedItem, designHandle
				.findElement( "headerMatrix" ) ); //$NON-NLS-1$
		assertEquals( contentExtendedItem, designHandle
				.getElementByID( contentExtendedItem.getID( ) ) );
		// it is a single slot, can not contain any item
		PropertyHandle propHandle = extendedItem
				.getPropertyHandle( HEADER_PROP );
		assertTrue( extendedItem.getPropertyDefn( HEADER_PROP ).canContain(
				MetaDataDictionary.getInstance( ).getElement(
						ReportDesignConstants.LABEL_ITEM ) ) );
		assertFalse( propHandle.canContain( ReportDesignConstants.LABEL_ITEM ) );

		// test detail slot
		propHandle = extendedItem.getPropertyHandle( DETAIL_PROP );
		TableHandle table = (TableHandle) propHandle.get( 0 );
		assertEquals( "testTable", table.getName( ) ); //$NON-NLS-1$
		// get the cell content slot of the table detail row
		// TODO getCell(int, int)
		SlotHandle slot = table.getDetail( ).get( 0 ).getSlot( 0 ).get( 0 )
				.getSlot( 0 );
		contentExtendedItem = (ExtendedItemHandle) slot.get( 0 );
		assertEquals( "detailBox", contentExtendedItem.getName( ) ); //$NON-NLS-1$

		// test footer slot
		propHandle = extendedItem.getPropertyHandle( FOOTER_PROP );
		GridHandle grid = (GridHandle) propHandle.get( 0 );
		assertEquals( "footerGrid", grid.getName( ) ); //$NON-NLS-1$

		openDesign( FILE_NAME_4 );
		extendedItem = (ExtendedItemHandle) designHandle
				.findElement( "testTable" ); //$NON-NLS-1$
		assertNotNull( extendedItem );

		assertNull( extendedItem.getReportItem( ) );
		assertEquals( TESTING_TABLE_NAME, extendedItem.getExtensionName( ) );
		assertNotNull( extendedItem.getDefn( ) );

		ExtensionPropertyDefn propDefn = (ExtensionPropertyDefn) extendedItem
				.getPropertyDefn( "customComments" ); //$NON-NLS-1$
		assertFalse( propDefn.hasOwnModel( ) );
	}

	/**
	 * Tests the writer for peer extension slot.
	 * 
	 * @throws Exception
	 */

	public void testWriter( ) throws Exception
	{
		openDesign( FILE_NAME );
		ExtendedItemHandle extendedItem = (ExtendedItemHandle) designHandle
				.findElement( "testBox" ); //$NON-NLS-1$
		assertNotNull( extendedItem );

		// add some items to detail slot and then save
		PropertyHandle propHandle = extendedItem
				.getPropertyHandle( DETAIL_PROP );
		LabelHandle label = designHandle.getElementFactory( ).newLabel(
				"addLabel" ); //$NON-NLS-1$
		propHandle.add( label );
		IDesignElement clonedExtendedItem = extendedItem.copy( );
		designHandle.rename( clonedExtendedItem.getHandle( design ) );
		propHandle.paste( clonedExtendedItem );

		// add a testing table
		ExtendedItemHandle extendedTable = designHandle.getElementFactory( )
				.newExtendedItem( "testExtendedTable", "TestingTable" ); //$NON-NLS-1$//$NON-NLS-2$
		extendedTable.setProperty( TableItem.CAPTION_PROP, "table caption" ); //$NON-NLS-1$
		extendedTable.setProperty( TableItem.DATA_SET_PROP, "tableDataSet" ); //$NON-NLS-1$
		extendedTable.setProperty( IStyleModel.COLOR_PROP, IColorConstants.RED );
		extendedTable.setProperty( "usage", "testusagevalue" ); //$NON-NLS-1$//$NON-NLS-2$
		designHandle.getBody( ).add( extendedTable );

		save( );
		assertTrue( compareFile( "PeerExtensionTest_golden.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * Tests the property search with support of extension slot.
	 * 
	 * @throws Exception
	 */

	public void testPropertySearch( ) throws Exception
	{
		openDesign( FILE_NAME_1 );
		ExtendedItemHandle extendedItem = (ExtendedItemHandle) designHandle
				.findElement( "testBox" ); //$NON-NLS-1$
		assertNotNull( extendedItem );

		// top extended-item get the property from the selector"extended-item"
		assertEquals( IColorConstants.RED, extendedItem
				.getStringProperty( IStyleModel.COLOR_PROP ) );

		// the extended-item in the header slot of outer extension
		ExtendedItemHandle contentExtendedItem = (ExtendedItemHandle) designHandle
				.findElement( "headerMatrix" ); //$NON-NLS-1$
		assertEquals( IColorConstants.RED, contentExtendedItem
				.getStringProperty( IStyleModel.COLOR_PROP ) );

		// test the table in the extended detail slot, extends color from the
		// container -- extendedItem
		TableHandle table = (TableHandle) designHandle
				.findElement( "testTable" ); //$NON-NLS-1$
		assertEquals( extendedItem, table.getContainer( ) );
		assertEquals( IColorConstants.RED, extendedItem
				.getStringProperty( IStyleModel.COLOR_PROP ) );
		// local properties in table
		assertEquals( DesignChoiceConstants.FONT_FAMILY_FANTASY, table
				.getStringProperty( IStyleModel.FONT_FAMILY_PROP ) );
		assertEquals( DesignChoiceConstants.FONT_SIZE_LARGER, table
				.getStringProperty( IStyleModel.FONT_SIZE_PROP ) );
		// TODO:table properties get from the extended-item detail slot selector
		// assertEquals( DesignChoiceConstants.FONT_WEIGHT_BOLD, table
		// .getStringProperty( IStyleModel.FONT_WEIGHT_PROP ) );
		// assertEquals( DesignChoiceConstants.FONT_STYLE_ITALIC, table
		// .getStringProperty( IStyleModel.FONT_STYLE_PROP ) );

		// test the label in the contained extended-item header slot
		LabelHandle label = (LabelHandle) designHandle
				.findElement( "testLabel" ); //$NON-NLS-1$
		assertEquals( IColorConstants.RED, label
				.getStringProperty( IStyleModel.COLOR_PROP ) );
		assertEquals( DesignChoiceConstants.FONT_FAMILY_FANTASY, label
				.getStringProperty( IStyleModel.FONT_FAMILY_PROP ) );
		assertEquals( DesignChoiceConstants.FONT_SIZE_LARGER, label
				.getStringProperty( IStyleModel.FONT_SIZE_PROP ) );
		assertEquals( DesignChoiceConstants.FONT_WEIGHT_NORMAL, label
				.getStringProperty( IStyleModel.FONT_WEIGHT_PROP ) );
		assertEquals( DesignChoiceConstants.FONT_STYLE_NORMAL, label
				.getStringProperty( IStyleModel.FONT_STYLE_PROP ) );
	}

	/**
	 * Tests the content commands.
	 * 
	 * @throws Exception
	 */

	public void testCommand( ) throws Exception
	{
		openDesign( FILE_NAME );
		ExtendedItemHandle extendedItem = (ExtendedItemHandle) designHandle
				.findElement( "testBox" ); //$NON-NLS-1$
		assertNotNull( extendedItem );

		// test the list property operations
		PropertyHandle points = extendedItem
				.getPropertyHandle( POINTS_PROP_NAME );
		try
		{
			// add an invalid float
			points.addItem( "p16" ); //$NON-NLS-1$
			fail( );
		}
		catch ( SemanticException e )
		{
			assertEquals(
					PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e
							.getErrorCode( ) );
		}
		points.removeItem( 1 );
		assertEquals( 2, points.getListValue( ).size( ) );
		points.addItem( "18.9" ); //$NON-NLS-1$

		// test header slot
		PropertyHandle propHandle = extendedItem
				.getPropertyHandle( HEADER_PROP );
		ExtendedItemHandle contentExtendedItem = (ExtendedItemHandle) propHandle
				.get( 0 );
		// it is a single slot, can not contain any item
		LabelHandle label = designHandle.getElementFactory( ).newLabel(
				"label1" ); //$NON-NLS-1$
		try
		{
			propHandle.add( label );
			fail( );
		}
		catch ( SemanticException e )
		{
		}
		contentExtendedItem.drop( );
		assertEquals( 0, propHandle.getContentCount( ) );
		propHandle.add( label );
		assertEquals( 1, propHandle.getContentCount( ) );
		assertEquals( extendedItem, label.getContainer( ) );

		// test detail slot
		propHandle = extendedItem.getPropertyHandle( DETAIL_PROP );
		TableHandle table = (TableHandle) propHandle.get( 0 );
		TableGroupHandle tableGroup = designHandle.getElementFactory( )
				.newTableGroup( );
		table.getGroups( ).add( tableGroup );
		assertEquals( table, tableGroup.getContainer( ) );
		ListHandle list = designHandle.getElementFactory( ).newList( "list" ); //$NON-NLS-1$
		propHandle.add( list );
		assertEquals( 2, propHandle.getContentCount( ) );
		// add element to detail directly
		save( );
		assertTrue( compareFile( "PeerExtensionTest_golden_1.xml" ) ); //$NON-NLS-1$
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void testParserErrorRecover( ) throws Exception
	{
		openDesign( FILE_NAME_2 );
		ExtendedItemHandle extendedItem = (ExtendedItemHandle) designHandle
				.findElement( "testBox" ); //$NON-NLS-1$
		assertNotNull( extendedItem );
		assertEquals( "nonExistingExtension", extendedItem.getExtensionName( ) ); //$NON-NLS-1$
		// rom-defined properties still read properly
		assertEquals(
				"1.2mm", extendedItem.getStringProperty( ExtendedItem.X_PROP ) ); //$NON-NLS-1$
		assertEquals(
				"11.2mm", extendedItem.getStringProperty( ExtendedItem.Y_PROP ) ); //$NON-NLS-1$
		assertEquals(
				"firstDataSet", extendedItem.getStringProperty( ExtendedItem.DATA_SET_PROP ) ); //$NON-NLS-1$

		// the table in the extended item is not parsed to the tree
		assertNull( designHandle.findElement( "testTable" ) ); //$NON-NLS-1$

		// add a label to the body
		LabelHandle label = designHandle.getElementFactory( ).newLabel(
				"testLabel" ); //$NON-NLS-1$
		assertEquals( "testLabel1", label.getName( ) ); //$NON-NLS-1$
		designHandle.getBody( ).add( label );

		save( );
		assertTrue( compareFile( "PeerExtensionTest_golden_2.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * Tests an extension has 'extensionName' property even if it does not
	 * extend from 'ExtendedItem'.
	 * 
	 * @throws Exception
	 */

	public void testExtensionNameProp( ) throws Exception
	{
		MetaDataDictionary dd = MetaDataDictionary.getInstance( );
		assertTrue( dd.getExtensions( ).size( ) >= 3 );

		ElementDefn extendedCell = (ElementDefn) dd
				.getExtension( "TestingTable" ); //$NON-NLS-1$
		assertNotNull( extendedCell );
		assertEquals( dd.getElement( ReportDesignConstants.TABLE_ITEM ),
				extendedCell.getParent( ) );

		PropertyDefn extensionName = (PropertyDefn) extendedCell
				.getProperty( ExtendedItem.EXTENSION_NAME_PROP );
		assertNotNull( extensionName );
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void testActionHandleInExtension( ) throws Exception
	{
		openDesign( FILE_NAME_3 );
		ExtendedItemHandle extendedItem = (ExtendedItemHandle) designHandle
				.findElement( "testBox" ); //$NON-NLS-1$
		assertNotNull( extendedItem );
		PropertyDefn actionDefn = (PropertyDefn) extendedItem
				.getPropertyDefn( IImageItemModel.ACTION_PROP );
		assertNotNull( actionDefn );

		ImageHandle image = (ImageHandle) designHandle
				.findElement( "testImage" ); //$NON-NLS-1$
		ActionHandle imageAction = image.getActionHandle( );
		assertNotNull( imageAction );
		String actionString = ModuleUtil.serializeAction( imageAction );

		ActionHandle extendedAction = ModuleUtil.deserializeAction(
				actionString, extendedItem );
		assertNotNull( extendedAction );
		assertNotNull( extendedItem.getProperty( IImageItemModel.ACTION_PROP ) );
		assertEquals( extendedItem, extendedAction.getElementHandle( ) );

	}

	/**
	 * Tests the error handler of extension loader.
	 * 
	 * @throws Exception
	 */
	public void testExtensionLoaderErrorHandler( ) throws Exception
	{
		MetaDataDictionary dd = MetaDataDictionary.getInstance( );
		assertTrue( dd.getExtensions( ).size( ) >= 2 );
		assertNull( dd.getExtension( "wrongTestExtension" ) ); //$NON-NLS-1$
	}

	protected static final String TESTING_TABLE = "TestingTable"; //$NON-NLS-1$

	protected static final String TABLE = "Table";//$NON-NLS-1$

	/**
	 * Tests extension allowed units.
	 * 
	 * @throws Exception
	 */

	public void testExtensionAllowedUnits( ) throws Exception
	{
		
		//Test get allowed units in metadata.
		
		MetaDataDictionary dd = MetaDataDictionary.getInstance( );

		ExtensionElementDefn extDefn = (ExtensionElementDefn) dd
				.getExtension( TESTING_TABLE );
		IPropertyDefn defn = extDefn.getProperty( "width" ); //$NON-NLS-1$
		IChoiceSet set = defn.getAllowedChoices( );
		assertNotNull( set.findChoice( "in" ) );//$NON-NLS-1$
		assertNotNull( set.findChoice( "cm" ) );//$NON-NLS-1$
		assertNull( set.findChoice( "mm" ) );//$NON-NLS-1$
		assertNull( set.findChoice( "pt" ) );//$NON-NLS-1$

		set = dd.getElement( TABLE )
				.findProperty( "width" ).getAllowedChoices( ); //$NON-NLS-1$

		assertNotNull( set.findChoice( "in" ) );//$NON-NLS-1$
		assertNotNull( set.findChoice( "cm" ) );//$NON-NLS-1$
		assertNotNull( set.findChoice( "mm" ) );//$NON-NLS-1$
		assertNotNull( set.findChoice( "pt" ) );//$NON-NLS-1$
		
		//Test 'getPropertyDefn' method in DesignElementHandle class.
		
		openDesign( FILE_NAME_4 );
		ExtendedItemHandle extendedItem = (ExtendedItemHandle) designHandle
				.findElement( "testTable" ); //$NON-NLS-1$
		
		defn = extendedItem.getPropertyDefn( "width" ); //$NON-NLS-1$
		set = defn.getAllowedChoices( );
		
		assertNotNull( set.findChoice( "in" ) );//$NON-NLS-1$
		assertNotNull( set.findChoice( "cm" ) );//$NON-NLS-1$
		assertNull( set.findChoice( "mm" ) );//$NON-NLS-1$
		assertNull( set.findChoice( "pt" ) );//$NON-NLS-1$
		
	}
	
	public void testCrosstab() throws Exception
	{
		openDesign("CrosstabSample.xml"); //$NON-NLS-1$
		DesignElementHandle crosstab = designHandle.getBody( ).get( 0 );
		assertTrue( crosstab != null );
	}
}
