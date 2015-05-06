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

package org.eclipse.birt.report.model.metadata;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IArgumentInfo;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IMethodInfo;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.OdaDataSet;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.SimpleDataSet;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.i18n.ThreadResources;

import com.ibm.icu.util.ULocale;

/**
 * Test case for ElementDefn.
 * 
 */
public class ElementDefnTest extends AbstractMetaTest
{

	private ElementDefn def = null;

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp( ) throws Exception
	{
		super.setUp( );
		def = new ElementDefn( );
		ThreadResources.setLocale( ULocale.ENGLISH );
	}

	/**
	 * test getters ans setters.
	 */
	public void testGetterAndSetter( )
	{
		MetadataTestUtil.setAllowsUserProperties( def, true );
		MetadataTestUtil.setCanExtends( def, true );
		assertEquals( true, def.canExtend( ) );
		MetadataTestUtil.setDisplayNameKey( def, "Element.ReportDesign" ); //$NON-NLS-1$
		MetadataTestUtil.setExtends( def, "ReportElement" ); //$NON-NLS-1$
		MetadataTestUtil.setHasStyle( def, true );
		MetadataTestUtil.setName( def, "Name" ); //$NON-NLS-1$
		MetadataTestUtil.setNameOption( def, 2 );
		MetadataTestUtil.setNameSpaceID( def, "4" );

		assertEquals( false, def.isAbstract( ) );
		assertEquals( true, def.allowsUserProperties( ) );
		assertEquals( "Report Design", def.getDisplayName( ) ); //$NON-NLS-1$
		assertEquals( "Element.ReportDesign", def.getDisplayNameKey( ) ); //$NON-NLS-1$
		assertEquals( "ReportElement", def.getExtends( ) ); //$NON-NLS-1$
		assertEquals( true, def.hasStyle( ) );
		assertEquals( "Name", def.getName( ) ); //$NON-NLS-1$
		assertEquals( 2, def.getNameOption( ) );
		assertEquals( "4", def.getNameSpaceID( ) );

	}

	/**
	 * Test get localized group names.
	 */
	public void testGetGroupNames( )
	{
		ThreadResources.setLocale( ULocale.ENGLISH );
		IElementDefn elemDefn = MetaDataDictionary.getInstance( ).getElement(
				MetaDataConstants.STYLE_NAME );
		assertNotNull( elemDefn );

		List groupNames = elemDefn.getGroupNames( );
		assertEquals( 6, groupNames.size( ) );
	}

	/**
	 * test adding two properties with same name.
	 */
	public void testAddSameProperties( )
	{
		SystemPropertyDefn propertyA = new SystemPropertyDefn( );
		SystemPropertyDefn propertyB = new SystemPropertyDefn( );

		propertyA.setName( "ABC" ); //$NON-NLS-1$
		propertyB.setName( "ABC" ); //$NON-NLS-1$

		try
		{
			def.addProperty( propertyA );
			def.addProperty( propertyB );

			// Can not run the following statement

			fail( );
		}
		catch ( MetaDataException e )
		{
		}
	}

	/**
	 * test adding three properties and checking their existance.
	 * 
	 * @throws MetaDataException
	 */
	public void testAddThreeProperties( ) throws MetaDataException
	{
		SystemPropertyDefn propertyA = new SystemPropertyDefn( );
		SystemPropertyDefn propertyB = new SystemPropertyDefn( );
		SystemPropertyDefn propertyC = new SystemPropertyDefn( );

		propertyA.setName( "ABC" ); //$NON-NLS-1$
		propertyB.setName( "ABCDEF" ); //$NON-NLS-1$
		propertyC.setName( "ABCDEFGHI" ); //$NON-NLS-1$

		def.addProperty( propertyA );
		def.addProperty( propertyB );
		def.addProperty( propertyC );

		List list = def.getLocalProperties( );
		assertTrue( list.contains( propertyA ) );
		assertTrue( list.contains( propertyB ) );
		assertTrue( list.contains( propertyC ) );

	}

	/**
	 * test adding two properties and geting them with their name.
	 * 
	 * @throws MetaDataException
	 */
	public void testGetProperty( ) throws MetaDataException
	{
		assertNull( def.getProperty( "NotExisting" ) ); //$NON-NLS-1$

		ElementDefn reportItem = (ElementDefn) MetaDataDictionary.getInstance( )
				.getElement( ReportDesignConstants.REPORT_ITEM );
		ElementDefn label = (ElementDefn) MetaDataDictionary.getInstance( )
				.getElement( ReportDesignConstants.LABEL_ITEM );

		SystemPropertyDefn p = (SystemPropertyDefn) reportItem
				.getProperty( ReportItem.BOOKMARK_PROP );
		assertNotNull( p );
		assertEquals( "Element.ReportItem.bookmark", p.getDisplayNameID( ) ); //$NON-NLS-1$
		assertSame( reportItem, label.getParent( ) );
	}

	/**
	 * test three elements with hierarchy.
	 */
	public void testGetLocalPropertiesAndGetProperties( )
	{
		// Get them from dictionary

		ElementDefn reportElement = (ElementDefn) MetaDataDictionary
				.getInstance( ).getElement( "ReportElement" ); //$NON-NLS-1$
		ElementDefn reportItem = (ElementDefn) MetaDataDictionary.getInstance( )
				.getElement( ReportDesignConstants.REPORT_ITEM );
		ElementDefn label = (ElementDefn) MetaDataDictionary.getInstance( )
				.getElement( ReportDesignConstants.LABEL_ITEM );

		SystemPropertyDefn displayNameProperty = (SystemPropertyDefn) label
				.getProperty( DesignElement.DISPLAY_NAME_PROP );
		SystemPropertyDefn fontColorProperty = (SystemPropertyDefn) label
				.getProperty( Style.COLOR_PROP );
		SystemPropertyDefn fontFamilyProperty = (SystemPropertyDefn) label
				.getProperty( Style.FONT_FAMILY_PROP );

		assertFalse( label.getLocalProperties( ).contains( displayNameProperty ) );
		assertTrue( label.getLocalProperties( ).contains( fontColorProperty ) );
		assertTrue( label.getLocalProperties( ).contains( fontFamilyProperty ) );

		assertNotNull( reportElement
				.getProperty( DesignElement.DISPLAY_NAME_PROP ) );
		assertNotNull( reportItem.getProperty( DesignElement.DISPLAY_NAME_PROP ) );
		assertNotNull( label.getProperty( DesignElement.DISPLAY_NAME_PROP ) );

		assertTrue( label.getProperties( ).contains( displayNameProperty ) );
		assertTrue( label.getProperties( ).contains( fontColorProperty ) );
		assertTrue( label.getProperties( ).contains( fontFamilyProperty ) );

		// All is kind of ReportElement

		assertTrue( reportElement.isKindOf( MetaDataDictionary.getInstance( )
				.getElement( "ReportElement" ) ) ); //$NON-NLS-1$
		assertTrue( reportItem.isKindOf( MetaDataDictionary.getInstance( )
				.getElement( "ReportElement" ) ) ); //$NON-NLS-1$
		assertTrue( label.isKindOf( MetaDataDictionary.getInstance( )
				.getElement( "ReportElement" ) ) ); //$NON-NLS-1$

		// ReportItem and Label are kind of ReportItem

		assertFalse( reportElement.isKindOf( MetaDataDictionary.getInstance( )
				.getElement( ReportDesignConstants.REPORT_ITEM ) ) );
		assertTrue( reportItem.isKindOf( MetaDataDictionary.getInstance( )
				.getElement( ReportDesignConstants.REPORT_ITEM ) ) );
		assertTrue( label.isKindOf( MetaDataDictionary.getInstance( )
				.getElement( ReportDesignConstants.REPORT_ITEM ) ) );

		// Only Label is kind of Label

		assertFalse( reportElement.isKindOf( MetaDataDictionary.getInstance( )
				.getElement( ReportDesignConstants.LABEL_ITEM ) ) );
		assertFalse( reportItem.isKindOf( MetaDataDictionary.getInstance( )
				.getElement( ReportDesignConstants.LABEL_ITEM ) ) );
		assertTrue( label.isKindOf( MetaDataDictionary.getInstance( )
				.getElement( ReportDesignConstants.LABEL_ITEM ) ) );
	}

	/**
	 * Tests getting local methods and getting methods.
	 * 
	 */

	public void testGetLocalMethodsAndGetMethods( )
	{
		IElementDefn simpleDataSetDefn = MetaDataDictionary.getInstance( )
				.getElement( ReportDesignConstants.SIMPLE_DATA_SET_ELEMENT );
		IElementDefn extendedDataSetDefn = MetaDataDictionary.getInstance( )
				.getElement( ReportDesignConstants.ODA_DATA_SET );
		IElementDefn freeFormDefn = MetaDataDictionary.getInstance( )
				.getElement( ReportDesignConstants.FREE_FORM_ITEM );

		int i = 0;

		List list = simpleDataSetDefn.getMethods( );
		assertEquals( 5, list.size( ) );
		assertEquals( SimpleDataSet.BEFORE_OPEN_METHOD, ( (PropertyDefn) list
				.get( i++ ) ).getName( ) );
		assertEquals( SimpleDataSet.BEFORE_CLOSE_METHOD, ( (PropertyDefn) list
				.get( i++ ) ).getName( ) );
		assertEquals( SimpleDataSet.ON_FETCH_METHOD, ( (PropertyDefn) list
				.get( i++ ) ).getName( ) );
		assertEquals( SimpleDataSet.AFTER_OPEN_METHOD, ( (PropertyDefn) list
				.get( i++ ) ).getName( ) );
		assertEquals( SimpleDataSet.AFTER_CLOSE_METHOD, ( (PropertyDefn) list
				.get( i++ ) ).getName( ) );

		i = 0;
		list = simpleDataSetDefn.getLocalMethods( );
		assertEquals( 5, list.size( ) );
		assertEquals( SimpleDataSet.BEFORE_OPEN_METHOD, ( (PropertyDefn) list
				.get( i++ ) ).getName( ) );
		assertEquals( SimpleDataSet.BEFORE_CLOSE_METHOD, ( (PropertyDefn) list
				.get( i++ ) ).getName( ) );
		assertEquals( SimpleDataSet.ON_FETCH_METHOD, ( (PropertyDefn) list
				.get( i++ ) ).getName( ) );
		assertEquals( SimpleDataSet.AFTER_OPEN_METHOD, ( (PropertyDefn) list
				.get( i++ ) ).getName( ) );
		assertEquals( SimpleDataSet.AFTER_CLOSE_METHOD, ( (PropertyDefn) list
				.get( i++ ) ).getName( ) );

		i = 0;
		list = extendedDataSetDefn.getMethods( );
		assertEquals( 5, list.size( ) );
		assertEquals( SimpleDataSet.BEFORE_OPEN_METHOD, ( (PropertyDefn) list
				.get( i++ ) ).getName( ) );
		assertEquals( SimpleDataSet.BEFORE_CLOSE_METHOD, ( (PropertyDefn) list
				.get( i++ ) ).getName( ) );
		assertEquals( SimpleDataSet.ON_FETCH_METHOD, ( (PropertyDefn) list
				.get( i++ ) ).getName( ) );
		assertEquals( SimpleDataSet.AFTER_OPEN_METHOD, ( (PropertyDefn) list
				.get( i++ ) ).getName( ) );
		assertEquals( SimpleDataSet.AFTER_CLOSE_METHOD, ( (PropertyDefn) list
				.get( i++ ) ).getName( ) );

		i = 0;
		list = extendedDataSetDefn.getLocalMethods( );
		assertEquals( 0, list.size( ) );

		i = 0;
		list = freeFormDefn.getMethods( );
		assertEquals( 0, list.size( ) );

		i = 0;
		list = freeFormDefn.getLocalMethods( );
		assertEquals( 0, list.size( ) );
	}

	/**
	 * Tests getting local expression and getting expression.
	 * 
	 */

	public void testGetLocalExpressionsAndGetExpression( )
	{
		IElementDefn freeFormDefn = MetaDataDictionary.getInstance( )
				.getElement( ReportDesignConstants.FREE_FORM_ITEM );

		List list = freeFormDefn.getExpressions( );
		assertEquals( 3, list.size( ) );

		assertEquals( ReportItem.BOOKMARK_PROP, ( (PropertyDefn) list.get( 0 ) )
				.getName( ) );

		assertEquals( 0, freeFormDefn.getLocalExpressions( ).size( ) );
	}

	/**
	 * test slot access for the element with one slot.
	 */
	public void testElementWithOneSlot( )
	{
		ElementDefn container = (ElementDefn) MetaDataDictionary.getInstance( )
				.getElement( ReportDesignConstants.FREE_FORM_ITEM );

		assertEquals( "Element.FreeForm", container.getDisplayNameKey( ) ); //$NON-NLS-1$

		assertEquals( 1, container.getSlotCount( ) );
		assertFalse( container.hasSlot( -1 ) );
		assertTrue( container.hasSlot( 0 ) );
		assertFalse( container.hasSlot( 4 ) );

		assertNull( container.getSlot( -1 ) );
		assertNotNull( container.getSlot( 0 ) );
		assertNull( container.getSlot( 4 ) );

		assertEquals( "reportItems", container.getSlot( 0 ).getName( ) ); //$NON-NLS-1$
		assertEquals(
				"Element.FreeForm.slot.reportItems", ( (SlotDefn) container.getSlot( 0 ) ) //$NON-NLS-1$
						.getDisplayNameID( ) );
		assertEquals( true, container.getSlot( 0 ).isMultipleCardinality( ) );
		assertTrue( container.getSlot( 0 ).canContain(
				MetaDataDictionary.getInstance( ).getElement(
						ReportDesignConstants.FREE_FORM_ITEM ) ) );
		assertTrue( container.getSlot( 0 ).canContain(
				MetaDataDictionary.getInstance( ).getElement(
						ReportDesignConstants.LABEL_ITEM ) ) );

		assertFalse( container.canContain( -1, MetaDataDictionary.getInstance( )
				.getElement( ReportDesignConstants.LABEL_ITEM ) ) );
		assertTrue( container.canContain( 0, MetaDataDictionary.getInstance( )
				.getElement( ReportDesignConstants.LABEL_ITEM ) ) );
		assertFalse( container.canContain( 0, MetaDataDictionary.getInstance( )
				.getElement( ReportDesignConstants.DATA_SET_ELEMENT ) ) );
	}

	/**
	 * test slot access for the element with no slot.
	 */
	public void testElementWithNoSlot( )
	{
		// Get an element from dictionary

		IElementDefn label = MetaDataDictionary.getInstance( ).getElement(
				ReportDesignConstants.LABEL_ITEM );

		assertEquals( 0, label.getSlotCount( ) );
		assertFalse( label.hasSlot( -1 ) );
		assertFalse( label.hasSlot( 0 ) );
		assertNull( label.getSlot( -1 ) );
		assertNull( label.getSlot( 0 ) );

		assertFalse( label.canContain( -1, MetaDataDictionary.getInstance( )
				.getElement( ReportDesignConstants.DATA_SOURCE_ELEMENT ) ) );
		assertFalse( label.canContain( 0, MetaDataDictionary.getInstance( )
				.getElement( ReportDesignConstants.DATA_SOURCE_ELEMENT ) ) );

	}

	/**
	 * test adding one slot to element.
	 */
	public void testAddSlotToElement( )
	{
		// Get an element from dictionary

		ElementDefn label = (ElementDefn) MetaDataDictionary.getInstance( )
				.getElement( ReportDesignConstants.LABEL_ITEM );
		assertEquals( 0, label.getSlotCount( ) );

		// Add one slot

		SlotDefn slot = new SlotDefn( );
		MetadataTestUtil.setDisplayNameKey( slot, "Data Sets" ); //$NON-NLS-1$
		MetadataTestUtil.setName( slot, "dataSets" ); //$NON-NLS-1$
		MetadataTestUtil.setMultipleCardinality( slot, true );
		MetadataTestUtil.addType( slot,
				ReportDesignConstants.DATA_SOURCE_ELEMENT );
		MetadataTestUtil.addSlot( label, slot );
		assertEquals( 1, label.getSlotCount( ) );

		// Add another slot

		MetadataTestUtil.addSlot( label, slot );
		assertEquals( 2, label.getSlotCount( ) );
	}

	/**
	 * test building.
	 * 
	 * @throws MetaDataException
	 */
	public void testBuild( ) throws MetaDataException
	{
		// element without parent and style

		def = new ElementDefn( );
		MetadataTestUtil.setName( def, "TestElement1" ); //$NON-NLS-1$
		MetadataTestUtil.setAbstract( def, true );
		MetadataTestUtil.setHasStyle( def, false );
		MetadataTestUtil.setNameOption( def, MetaDataConstants.NO_NAME );
		MetadataTestUtil.build( def );

		// element without parent but with style

		def = new ElementDefn( );
		MetadataTestUtil.setName( def, "TestElement2" ); //$NON-NLS-1$
		MetadataTestUtil.setHasStyle( def, true );
		MetadataTestUtil.addStyleProp( def, Style.FONT_SIZE_PROP );
		MetadataTestUtil.addStyleProp( def, Style.DATE_TIME_FORMAT_PROP );
		MetadataTestUtil.setNameOption( def, MetaDataConstants.NO_NAME );
		MetadataTestUtil.setAbstract( def, true );
		MetadataTestUtil.build( def );

		// element with parent and style

		def = new ElementDefn( );
		MetadataTestUtil.setAbstract( def, true );
		MetadataTestUtil.setName( def, "TestElement3" ); //$NON-NLS-1$
		MetadataTestUtil.setExtends( def, "ReportElement" ); //$NON-NLS-1$
		MetadataTestUtil.setHasStyle( def, true );
		MetadataTestUtil.addStyleProp( def, Style.FONT_SIZE_PROP );
		MetadataTestUtil.addStyleProp( def, Style.DATE_TIME_FORMAT_PROP );
		MetadataTestUtil.setNameOption( def, MetaDataConstants.NO_NAME );
		MetadataTestUtil.build( def );
	}

	/**
	 * Test method definition and argument definition.
	 * 
	 * @throws MetaDataParserException
	 */
	public void testMethod( ) throws MetaDataParserException
	{
		ThreadResources.setLocale( ULocale.getDefault( ) );
		loadMetaData( ElementDefnTest.class
				.getResourceAsStream( "input/ElementDefnTest.def" ) ); //$NON-NLS-1$

		MetaDataDictionary dd = MetaDataDictionary.getInstance( );
		IElementDefn element = dd.getElement( "ReportElement" ); //$NON-NLS-1$
		assertNotNull( element );

		IMethodInfo method1 = element.getProperty( "method1" ).getMethodInfo( ); //$NON-NLS-1$
		assertNotNull( method1 );
		assertEquals( "display-name-id", method1.getDisplayNameKey( ) ); //$NON-NLS-1$
		assertEquals( "tool-tip-id", method1.getToolTipKey( ) ); //$NON-NLS-1$
		assertEquals( "string", method1.getReturnType( ) ); //$NON-NLS-1$

		Iterator iter = method1.argumentListIterator( );
		ArgumentInfoList argumentList = (ArgumentInfoList) iter.next( );

		IArgumentInfo arg = argumentList.getArgument( "arg0" ); //$NON-NLS-1$
		assertNotNull( arg );
		assertEquals( "method1.arg0", arg.getDisplayNameKey( ) ); //$NON-NLS-1$
		assertEquals( "int", arg.getType( ) ); //$NON-NLS-1$
		arg = argumentList.getArgument( "arg1" ); //$NON-NLS-1$
		assertNotNull( arg );
		assertEquals( "method1.arg1", arg.getDisplayNameKey( ) ); //$NON-NLS-1$
		assertEquals( "string", arg.getType( ) ); //$NON-NLS-1$

		IMethodInfo method2 = element.getProperty( "method2" ).getMethodInfo( ); //$NON-NLS-1$
		assertNotNull( method2 );

	}

	/**
	 * Test the style property can not be retrieved from the container which can
	 * not have style.
	 */

	public void testGetStyleProperty( )
	{
		IElementDefn reportDesign = MetaDataDictionary.getInstance( )
				.getElement( ReportDesignConstants.REPORT_DESIGN_ELEMENT );

		assertNull( reportDesign.getProperty( Style.BACKGROUND_ATTACHMENT_PROP ) );

		Iterator iter = reportDesign.getProperties( ).iterator( );
		while ( iter.hasNext( ) )
		{
			ElementPropertyDefn prop = (ElementPropertyDefn) iter.next( );

			assertFalse( prop.isStyleProperty( ) );
		}

	}

	/**
	 * Test the isVisible attribute of an extended dataset.
	 * <code>ExtendedDataSet.PRIVATE_DRIVER_DESIGN_STATE_PROP</code> and
	 * <code>ExtendedDataSet.PRIVATE_DRIVER_PROPERTIES_PROP</code> are invisible
	 * to users.
	 */

	public void testPropertyVisiblity( )
	{
		IElementDefn dataSetDefn = MetaDataDictionary.getInstance( )
				.getElement( ReportDesignConstants.ODA_DATA_SET );

		Iterator iter = dataSetDefn.getLocalProperties( ).iterator( );
		while ( iter.hasNext( ) )
		{
			ElementPropertyDefn prop = (ElementPropertyDefn) iter.next( );

			if ( OdaDataSet.RESULT_SET_NAME_PROP.equalsIgnoreCase( prop
					.getName( ) )
					|| OdaDataSet.PRIVATE_DRIVER_PROPERTIES_PROP
							.equalsIgnoreCase( prop.getName( ) )
					|| OdaDataSet.DESIGNER_STATE_PROP.equalsIgnoreCase( prop
							.getName( ) )
					|| OdaDataSet.EXTENSION_ID_PROP.equalsIgnoreCase( prop
							.getName( ) )
					|| OdaDataSet.RESULT_SET_PROP.equalsIgnoreCase( prop
							.getName( ) )
					|| OdaDataSet.PARAMETERS_PROP.equalsIgnoreCase( prop
							.getName( ) ) )
				assertFalse( dataSetDefn.isPropertyVisible( prop.getName( ) ) );
			else
				assertTrue( dataSetDefn.isPropertyVisible( prop.getName( ) ) );
		}

		IElementDefn reportItemDefn = MetaDataDictionary.getInstance( )
				.getElement( ReportDesignConstants.REPORT_ITEM );

		assertFalse( reportItemDefn
				.isPropertyVisible( IReportItemModel.CUBE_PROP ) );

		IElementDefn dataDefn = MetaDataDictionary.getInstance( ).getElement(
				ReportDesignConstants.DATA_ITEM );
		assertFalse( dataDefn.isPropertyVisible( IReportItemModel.CUBE_PROP ) );

	}
}