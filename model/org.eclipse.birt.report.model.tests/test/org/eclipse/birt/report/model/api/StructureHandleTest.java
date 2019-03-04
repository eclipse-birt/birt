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

package org.eclipse.birt.report.model.api;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ConfigVariable;
import org.eclipse.birt.report.model.api.elements.structures.CustomColor;
import org.eclipse.birt.report.model.api.elements.structures.DimensionJoinCondition;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.elements.structures.HideRule;
import org.eclipse.birt.report.model.api.elements.structures.IncludeScript;
import org.eclipse.birt.report.model.api.elements.structures.IncludedLibrary;
import org.eclipse.birt.report.model.api.elements.structures.MapRule;
import org.eclipse.birt.report.model.api.elements.structures.PropertyMask;
import org.eclipse.birt.report.model.api.elements.structures.SelectionChoice;
import org.eclipse.birt.report.model.api.elements.structures.SortKey;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ListingElement;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.ScalarParameter;
import org.eclipse.birt.report.model.elements.SimpleDataSet;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Tests the all structure operation and demos. The structure operations include
 * adding, inserting, removing and so on.
 */

public class StructureHandleTest extends BaseTestCase
{

	/**
	 * Tests all structure operations. They are:
	 * <ul>
	 * <li>Add new structure
	 * <li>Insert new structure
	 * <li>Move structure to another position
	 * <li>Replace the old structure with the new one
	 * <li>Drop structure
	 * </ul>
	 * 
	 * @throws SemanticException
	 *             if any exception.
	 */

	public void testOperations( ) throws SemanticException
	{
		createDesign( );

		PropertyHandle colorPaletteHandle = designHandle
				.getPropertyHandle( ReportDesign.COLOR_PALETTE_PROP );

		// Create three custom color, and set name and value.
		// Note: These setters can not be undone, and property type check
		// is not performed.

		CustomColor red = StructureFactory.createCustomColor( );
		red.setName( "myred" ); //$NON-NLS-1$
		red.setColor( "0xFF0000" ); //$NON-NLS-1$

		CustomColor green = StructureFactory.createCustomColor( );
		green.setName( "mygreen" ); //$NON-NLS-1$
		green.setColor( "0x00FF00" ); //$NON-NLS-1$

		CustomColor blue = StructureFactory.createCustomColor( );
		blue.setName( "myblue" ); //$NON-NLS-1$
		blue.setColor( "0x0000FF" ); //$NON-NLS-1$

		// Add them to color palette

		colorPaletteHandle.addItem( red );
		colorPaletteHandle.addItem( blue );

		// Insert green between red and blue.

		colorPaletteHandle.insertItem( green, 1 );

		// Get their CustomColorHandle from CustomColor

		CustomColorHandle redHandle = (CustomColorHandle) red
				.getHandle( colorPaletteHandle );
		CustomColorHandle greenHandle = (CustomColorHandle) green
				.getHandle( colorPaletteHandle );
		CustomColorHandle blueHandle = (CustomColorHandle) blue
				.getHandle( colorPaletteHandle );

		List colorList = colorPaletteHandle.getListValue( );
		assertTrue( redHandle.getName( ).equals(
				( (CustomColor) colorList.get( 0 ) ).getName( ) ) );
		assertTrue( greenHandle.getName( ).equals(
				( (CustomColor) colorList.get( 1 ) ).getName( ) ) );
		assertTrue( blueHandle.getName( ).equals(
				( (CustomColor) colorList.get( 2 ) ).getName( ) ) );

		// Set display name via CustomColorHandle
		// Note: These setters can be undone, and property type check is
		// performed.

		redHandle.setDisplayName( "Red" ); //$NON-NLS-1$
		greenHandle.setDisplayName( "Green" ); //$NON-NLS-1$
		blueHandle.setDisplayName( "Blue" ); //$NON-NLS-1$

		// Move structure from one position to another
		// red, green, blue -> green, blue, red
		// You can see the CustomColorHandle is invalid after moving, so
		// handles should be taken again! This case applies all position related
		// operation.

		colorPaletteHandle.moveItem( 0, 3 );
		colorList = colorPaletteHandle.getListValue( );
		assertEquals( 3, colorList.size( ) );
		assertTrue( greenHandle.getName( ).equals(
				( (CustomColor) colorList.get( 0 ) ).getName( ) ) );
		assertTrue( blueHandle.getName( ).equals(
				( (CustomColor) colorList.get( 1 ) ).getName( ) ) );
		assertTrue( redHandle.getName( ).equals(
				( (CustomColor) colorList.get( 2 ) ).getName( ) ) );

		// Getting handles should be performed again. These handles reflect
		// the current structure.

		redHandle = (CustomColorHandle) red.getHandle( colorPaletteHandle );
		greenHandle = (CustomColorHandle) green.getHandle( colorPaletteHandle );
		blueHandle = (CustomColorHandle) blue.getHandle( colorPaletteHandle );
		assertTrue( greenHandle.getName( ).equals(
				( (CustomColor) colorList.get( 0 ) ).getName( ) ) );
		assertTrue( blueHandle.getName( ).equals(
				( (CustomColor) colorList.get( 1 ) ).getName( ) ) );
		assertTrue( redHandle.getName( ).equals(
				( (CustomColor) colorList.get( 2 ) ).getName( ) ) );

		// Replace old structure with new one

		CustomColor black = StructureFactory.createCustomColor( );
		black.setName( "myblack" ); //$NON-NLS-1$
		black.setColor( "000000" ); //$NON-NLS-1$

		colorPaletteHandle.replaceItem( blue, black );

		CustomColorHandle blackHandle = (CustomColorHandle) black
				.getHandle( colorPaletteHandle );
		colorList = colorPaletteHandle.getListValue( );
		assertEquals( 3, colorList.size( ) );
		assertTrue( greenHandle.getName( ).equals(
				( (CustomColor) colorList.get( 0 ) ).getName( ) ) );
		assertTrue( blackHandle.getName( ).equals(
				( (CustomColor) colorList.get( 1 ) ).getName( ) ) );
		assertTrue( redHandle.getName( ).equals(
				( (CustomColor) colorList.get( 2 ) ).getName( ) ) );

		// Drop custom color green.

		colorPaletteHandle.removeItem( 0 );
		colorList = colorPaletteHandle.getListValue( );
		redHandle = (CustomColorHandle) red.getHandle( colorPaletteHandle );
		blackHandle = (CustomColorHandle) black.getHandle( colorPaletteHandle );
		assertTrue( blackHandle.getName( ).equals(
				( (CustomColor) colorList.get( 0 ) ).getName( ) ) );
		assertTrue( redHandle.getName( ).equals(
				( (CustomColor) colorList.get( 1 ) ).getName( ) ) );

	}

	/**
	 * Tests a SortKey and FilterCondition structure handles.
	 * 
	 * @throws SemanticException
	 */

	public void testSortAndFilterHandle( ) throws SemanticException
	{
		createDesign( );

		ElementFactory factory = new ElementFactory( design );
		TableHandle tableHandle = factory.newTableItem( "table 1" ); //$NON-NLS-1$
		PropertyHandle propHandle = tableHandle
				.getPropertyHandle( ListingElement.SORT_PROP );

		SortKey sortKey = StructureFactory.createSortKey( );
		sortKey.setKey( "expression" ); //$NON-NLS-1$
		propHandle.addItem( sortKey );

		Iterator iter = propHandle.iterator( );
		SortKeyHandle sortHandle = (SortKeyHandle) iter.next( );

		sortHandle.setKey( "new column" ); //$NON-NLS-1$
		sortHandle.setDirection( DesignChoiceConstants.SORT_DIRECTION_DESC );
		sortHandle.setStrength( 10 );
		sortHandle.setLocale( ULocale.GERMAN );

		assertEquals( "new column", sortHandle.getKey( ) ); //$NON-NLS-1$
		assertEquals( DesignChoiceConstants.SORT_DIRECTION_DESC, sortHandle
				.getDirection( ) );
		assertEquals( 10, sortHandle.getStrength( ) );
		assertEquals( ULocale.GERMAN, sortHandle.getLocale( ) );

		propHandle = tableHandle.getPropertyHandle( ListingElement.FILTER_PROP );

		FilterCondition filter = StructureFactory.createFilterCond( );
		filter.setExpr( "expression" ); //$NON-NLS-1$
		filter.setOperator( DesignChoiceConstants.MAP_OPERATOR_BETWEEN );
		propHandle.addItem( filter );

		iter = propHandle.iterator( );
		FilterConditionHandle filterHandle = (FilterConditionHandle) iter
				.next( );

		filterHandle.setExpr( "new expression" ); //$NON-NLS-1$
		filterHandle.setOperator( DesignChoiceConstants.FILTER_OPERATOR_FALSE );
		filterHandle.setValue1( "new value 1" ); //$NON-NLS-1$
		filterHandle.setValue2( "new value 2" ); //$NON-NLS-1$
		filterHandle.setOptional( true );

		assertEquals( "new expression", filterHandle.getExpr( ) ); //$NON-NLS-1$
		assertEquals( DesignChoiceConstants.FILTER_OPERATOR_FALSE, filterHandle
				.getOperator( ) );
		assertEquals( "new value 1", filterHandle.getValue1( ) ); //$NON-NLS-1$
		assertEquals( "new value 2", filterHandle.getValue2( ) ); //$NON-NLS-1$
		assertTrue( filterHandle.isOptional( ) );

		filterHandle
				.setOperator( DesignChoiceConstants.FILTER_OPERATOR_BETWEEN );
		assertEquals( "new value 1", filterHandle.getValue1( ) ); //$NON-NLS-1$
		assertEquals( "new value 2", filterHandle.getValue2( ) ); //$NON-NLS-1$

		filterHandle.setOperator( DesignChoiceConstants.FILTER_OPERATOR_EQ );
		assertEquals( "new value 1", filterHandle.getValue1( ) ); //$NON-NLS-1$
		assertNull( filterHandle.getValue2( ) );

		filterHandle.setOperator( DesignChoiceConstants.FILTER_OPERATOR_FALSE );
		assertNull( filterHandle.getValue1( ) );
		assertNull( filterHandle.getValue2( ) );

	}

	/**
	 * Tests a SelectionChoice structure handle.
	 * 
	 * @throws SemanticException
	 */

	public void testSelectionChoiceHandle( ) throws SemanticException
	{
		createDesign( );

		ElementFactory factory = new ElementFactory( design );
		ScalarParameterHandle paramHandle = factory
				.newScalarParameter( "param 1" ); //$NON-NLS-1$
		PropertyHandle propHandle = paramHandle
				.getPropertyHandle( ScalarParameter.SELECTION_LIST_PROP );

		SelectionChoice structure = StructureFactory.createSelectionChoice( );
		propHandle.addItem( structure );

		Iterator iter = propHandle.iterator( );
		SelectionChoiceHandle structureHandle = (SelectionChoiceHandle) iter
				.next( );

		structureHandle.setLabel( "new label" ); //$NON-NLS-1$
		structureHandle.setLabelKey( "new label key" ); //$NON-NLS-1$
		structureHandle.setValue( "new value" ); //$NON-NLS-1$

		assertEquals( "new label", structureHandle.getLabel( ) ); //$NON-NLS-1$
		assertEquals( "new label key", structureHandle.getLabelKey( ) ); //$NON-NLS-1$
		assertEquals( "new value", structureHandle.getValue( ) ); //$NON-NLS-1$

	}

	/**
	 * Tests a PropertyMask structure handle.
	 * 
	 * @throws SemanticException
	 */

	public void testPropertyMaskHandle( ) throws SemanticException
	{
		createDesign( );

		ElementFactory factory = new ElementFactory( design );
		DataSetHandle dataSetHandle = factory.newScriptDataSet( "data set 1" ); //$NON-NLS-1$
		PropertyHandle propHandle = dataSetHandle
				.getPropertyHandle( DesignElement.PROPERTY_MASKS_PROP );

		PropertyMask structure = StructureFactory.createPropertyMask( );
		structure.setName( DesignElement.COMMENTS_PROP );
		propHandle.addItem( structure );

		Iterator iter = propHandle.iterator( );
		PropertyMaskHandle structureHandle = (PropertyMaskHandle) iter.next( );

		structureHandle.setMask( DesignChoiceConstants.PROPERTY_MASK_TYPE_LOCK );
		structureHandle.setName( SimpleDataSet.COMMENTS_PROP );

		assertEquals( DesignChoiceConstants.PROPERTY_MASK_TYPE_LOCK,
				structureHandle.getMask( ) );
		assertEquals( SimpleDataSet.COMMENTS_PROP, structureHandle.getName( ) );
	}

	/**
	 * Tests a IncludeScript, IncludeLibrary, ConfigVariable, EmbeddedImage
	 * structures handle.
	 * 
	 * @throws SemanticException
	 * @throws UnsupportedEncodingException
	 */

	public void testStructureHandleOnReportDesign( ) throws SemanticException,
			UnsupportedEncodingException
	{
		createDesign( );

		PropertyHandle propHandle = designHandle
				.getPropertyHandle( ReportDesign.LIBRARIES_PROP );

		IncludedLibrary structure = StructureFactory.createIncludeLibrary( );
		structure.setFileName( "a.xml" ); //$NON-NLS-1$
		structure.setNamespace( "a" ); //$NON-NLS-1$
		propHandle.addItem( structure );

		Iterator iter = propHandle.iterator( );

		propHandle = designHandle
				.getPropertyHandle( ReportDesign.INCLUDE_SCRIPTS_PROP );

		IncludeScript structure1 = StructureFactory.createIncludeScript( );
		structure1.setFileName( "script.js" ); //$NON-NLS-1$
		propHandle.addItem( structure1 );

		iter = propHandle.iterator( );
		IncludeScriptHandle structureHandle1 = (IncludeScriptHandle) iter
				.next( );

		structureHandle1.setFileName( "new script name" ); //$NON-NLS-1$
		assertEquals( "new script name", structureHandle1.getFileName( ) ); //$NON-NLS-1$

		propHandle = designHandle.getPropertyHandle( ReportDesign.IMAGES_PROP );

		EmbeddedImage structure2 = StructureFactory.createEmbeddedImage( );
		structure2.setName( "myImage" ); //$NON-NLS-1$
		structure2.setData( "data".getBytes( EmbeddedImage.CHARSET ) ); //$NON-NLS-1$

		try
		{
			// we allow empty image.
			structure2.setData( null );
		}
		catch ( Throwable e )
		{
			fail( );
		}

		propHandle.addItem( structure2 );

		iter = propHandle.iterator( );
		EmbeddedImageHandle structureHandle2 = (EmbeddedImageHandle) iter
				.next( );

		structureHandle2.setName( "new embedded image name" ); //$NON-NLS-1$
		structureHandle2.setType( DesignChoiceConstants.IMAGE_TYPE_IMAGE_BMP );

		byte[] data = {1, 2, 3, 4};
		structureHandle2.setData( data );

		assertEquals( "new embedded image name", structureHandle2.getName( ) ); //$NON-NLS-1$
		assertEquals( DesignChoiceConstants.IMAGE_TYPE_IMAGE_BMP,
				structureHandle2.getType( ) );

		byte[] retData = structureHandle2.getData( );
		assertEquals( 4, retData.length );
		assertEquals( 1, retData[0] );
		assertEquals( 2, retData[1] );
		assertEquals( 3, retData[2] );
		assertEquals( 4, retData[3] );

		propHandle = designHandle
				.getPropertyHandle( ReportDesign.CONFIG_VARS_PROP );

		ConfigVariable structure3 = StructureFactory.createConfigVar( );
		structure3.setName( "myvar" ); //$NON-NLS-1$
		propHandle.addItem( structure3 );

		iter = propHandle.iterator( );
		ConfigVariableHandle structureHandle3 = (ConfigVariableHandle) iter
				.next( );

		structureHandle3.setName( "new name" ); //$NON-NLS-1$
		structureHandle3.setValue( "new value" ); //$NON-NLS-1$

		assertEquals( "new name", structureHandle3.getName( ) ); //$NON-NLS-1$
		assertEquals( "new value", structureHandle3.getValue( ) ); //$NON-NLS-1$
	}

	/**
	 * Tests a Hide structure handle.
	 * 
	 * @throws SemanticException
	 */

	public void testHideHandle( ) throws SemanticException
	{
		createDesign( );

		ElementFactory factory = new ElementFactory( design );
		LabelHandle handle = factory.newLabel( "label 1" ); //$NON-NLS-1$
		PropertyHandle propHandle = handle
				.getPropertyHandle( ReportItem.VISIBILITY_PROP );

		HideRule structure = StructureFactory.createHideRule( );
		propHandle.addItem( structure );

		Iterator iter = propHandle.iterator( );
		HideRuleHandle structureHandle = (HideRuleHandle) iter.next( );

		structureHandle.setExpression( "new expression" ); //$NON-NLS-1$
		structureHandle.setFormat( DesignChoiceConstants.FORMAT_TYPE_PDF );

		assertEquals( DesignChoiceConstants.FORMAT_TYPE_PDF, structureHandle
				.getFormat( ) );
		assertEquals( "new expression", structureHandle.getExpression( ) ); //$NON-NLS-1$

		structureHandle.setFormat( "user_defined_format" ); //$NON-NLS-1$
		assertEquals( "user_defined_format", structureHandle.getFormat( ) ); //$NON-NLS-1$
	}

	/**
	 * Tests drop().
	 * 
	 * @throws Exception
	 */

	public void testDrop( ) throws Exception
	{
		createDesign( );

		PropertyHandle propHandle = designHandle
				.getPropertyHandle( ReportDesign.LIBRARIES_PROP );

		// test the property list case
		// add three structures

		IncludedLibrary structure1 = StructureFactory.createIncludeLibrary( );
		structure1.setFileName( "a.xml" ); //$NON-NLS-1$
		structure1.setNamespace( "a" ); //$NON-NLS-1$
		StructureHandle sHandle1 = propHandle.addItem( structure1 );

		IncludedLibrary structure2 = StructureFactory.createIncludeLibrary( );
		structure2.setFileName( "b.xml" ); //$NON-NLS-1$
		structure2.setNamespace( "b" ); //$NON-NLS-1$
		StructureHandle sHandle2 = propHandle.addItem( structure2 );

		IncludedLibrary structure3 = StructureFactory.createIncludeLibrary( );
		structure3.setFileName( "c.xml" ); //$NON-NLS-1$
		structure3.setNamespace( "c" ); //$NON-NLS-1$
		StructureHandle sHandle3 = propHandle.addItem( structure3 );

		// now drop structures

		sHandle2.drop( );
		List value = propHandle.getListValue( );
		assertEquals( 2, value.size( ) );
		assertEquals( sHandle1.getStructure( ), value.get( 0 ) );
		assertNull( sHandle2.getStructure( ) );
		assertEquals( sHandle3.getStructure( ), value.get( 1 ) );
		designHandle.close( );

		// test member is a list case

		openDesign( "ActionHandleTest.xml" ); //$NON-NLS-1$
		ImageHandle imageHandle = (ImageHandle) designHandle
				.findElement( "Image3" ); //$NON-NLS-1$
		ActionHandle actionHandle = imageHandle.getActionHandle( );

		MemberHandle memberHandle = actionHandle.getParamBindings( );
		assertEquals( 2, memberHandle.getListValue( ).size( ) );
		sHandle1 = memberHandle.getAt( 0 );
		sHandle2 = memberHandle.getAt( 1 );
		sHandle1.drop( );
		value = memberHandle.getListValue( );
		assertEquals( 1, value.size( ) );
		assertEquals( sHandle2.getStructure( ), value.get( 0 ) );

		// problems in Structure list contains another structure lists.

		createDesign( );

		TabularCubeHandle cube = designHandle.getElementFactory( )
				.newTabularCube( "cube1" ); //$NON-NLS-1$
		designHandle.getCubes( ).add( cube );

		DimensionConditionHandle condition = cube
				.addDimensionCondition( StructureFactory
						.createCubeJoinCondition( ) );

		DimensionJoinCondition tmpJoin = StructureFactory
				.createDimensionJoinCondition( );
		tmpJoin.setCubeKey( "cube1 key" ); //$NON-NLS-1$
		tmpJoin.setHierarchyKey( "hierarchy 1 key" ); //$NON-NLS-1$

		DimensionJoinConditionHandle joinCondition1 = condition
				.addJoinCondition( tmpJoin );

		condition = cube.addDimensionCondition( StructureFactory
				.createCubeJoinCondition( ) );

		tmpJoin = StructureFactory.createDimensionJoinCondition( );
		tmpJoin.setCubeKey( "cube1 key" ); //$NON-NLS-1$
		tmpJoin.setHierarchyKey( "hierarchy 2 key" ); //$NON-NLS-1$

		DimensionJoinConditionHandle joinCondition2 = condition
				.addJoinCondition( tmpJoin );

		joinCondition2.drop( );
		condition.drop( );

		condition = (DimensionConditionHandle) cube.joinConditionsIterator( )
				.next( );

		joinCondition1.drop( );
		condition.drop( );
	}

	/**
	 * Test getExternalizedValue method.
	 * 
	 * @throws Exception
	 */

	public void testGetExternalizedText( ) throws Exception
	{
		openDesign( "StructureHandleTest.xml" );//$NON-NLS-1$

		TableHandle tableHandle = (TableHandle) designHandle.getBody( ).get( 0 );
		PropertyHandle propHandle = tableHandle
				.getPropertyHandle( IStyleModel.MAP_RULES_PROP );

		ModuleOption option = new ModuleOption( );
		option.setLocale( new ULocale( "en" ) ); //$NON-NLS-1$
		design.setOptions( option );

		MapRuleHandle structHandle = (MapRuleHandle) propHandle.get( 0 );
		String value = structHandle.getExternalizedValue(
				MapRule.DISPLAY_ID_MEMBER, MapRule.DISPLAY_MEMBER );
		assertEquals( "en", value ); //$NON-NLS-1$

		option.setLocale( new ULocale( "en_US" ) );//$NON-NLS-1$

		structHandle = (MapRuleHandle) propHandle.get( 0 );
		value = structHandle.getExternalizedValue( MapRule.DISPLAY_ID_MEMBER,
				MapRule.DISPLAY_MEMBER );
		assertEquals( "en_US", value ); //$NON-NLS-1$
	}

	/**
	 * When parsing the library file, the member reference for the filter is
	 * created. And the structure is cached.
	 * <p>
	 * If the user wants to update the filter member values later, the cached
	 * structure needs to be updated. Otherwise, Model will throw a running time
	 * exception indicating that the member reference is floating.
	 * 
	 * @throws Exception
	 */

	public void testUpdateCachedStructureInMemberRef( ) throws Exception
	{
		openDesign( "StructureHandleTest_1.xml" );//$NON-NLS-1$

		TableHandle tableHandle = (TableHandle) designHandle
				.findElement( "table1" ); //$NON-NLS-1$
		Iterator filters = tableHandle.filtersIterator( );
		FilterConditionHandle filter = (FilterConditionHandle) filters.next( );

		filter.setExpr( "new design expr" ); //$NON-NLS-1$
		PropertyHandle propHandle = tableHandle
				.getPropertyHandle( TableHandle.FILTER_PROP );
		assertTrue( propHandle.isLocal( ) );

		assertEquals( "new design expr", filter.getExpr( ) ); //$NON-NLS-1$

	}

	/**
	 * When copies a design element, its structure values are also copied. This
	 * requires a non-null structure context. It is established in
	 * DesignElement.copy().
	 * 
	 * @throws Exception
	 */

	public void testContextWhenCopingElements( ) throws Exception
	{
		createDesign( );

		TableHandle tableHandle = designHandle.getElementFactory( )
				.newTableItem( null );
		HideRule rule = StructureFactory.createHideRule( );
		rule.setExpression( "value1" ); //$NON-NLS-1$
		rule.setFormat( DesignChoiceConstants.FORMAT_TYPE_ALL );

		tableHandle.getPropertyHandle( TableHandle.VISIBILITY_PROP ).addItem(
				rule );

		TableHandle newTable = (TableHandle) tableHandle.copy( ).getHandle(
				design );
		newTable.getPropertyHandle( TableHandle.VISIBILITY_PROP )
				.removeItem( 0 );

	}

	/**
	 * When <code>EmbeddedImage</code> has reference to library embedded image,
	 * the method hasExtends in <code>EmbeddedImageHandle</code> return true,
	 * else return false.
	 * 
	 * @throws Exception
	 */
	public void testEmbeddedImageExtendsFromLib( ) throws Exception
	{
		openDesign( "EmbeddedImageWithExtendsTest.xml", ULocale.ENGLISH ); //$NON-NLS-1$

		PropertyHandle images = designHandle
				.getPropertyHandle( ReportDesign.IMAGES_PROP );

		EmbeddedImageHandle image = (EmbeddedImageHandle) images.getAt( 0 );

		assertTrue( image.isLibReference( ) );

		EmbeddedImageHandle image1 = (EmbeddedImageHandle) images.getAt( 1 );
		assertFalse( image1.isLibReference( ) );

		image = (EmbeddedImageHandle) images.getAt( 2 );

		assertFalse( image.isLibReference( ) );

	}

}