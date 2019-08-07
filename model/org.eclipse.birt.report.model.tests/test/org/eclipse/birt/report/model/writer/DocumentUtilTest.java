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

package org.eclipse.birt.report.model.writer;

import java.io.ByteArrayOutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.FilterConditionElementHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.ReportItemThemeHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.model.api.util.DocumentUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests the document related serialization.
 */

public class DocumentUtilTest extends BaseTestCase
{

	/**
	 * Design file name, which tests the element property value localization.
	 */

	private static final String DESIGN_WITH_ELEMENT_EXTENDS = "DocumentUtilTest.xml"; //$NON-NLS-1$

	/**
	 * Design file name, which tests the lib reference with library structures.
	 */

	private static final String DESIGN_WITH_STRUCTURE_REFERENCE = "DocumentUtilTest_1.xml"; //$NON-NLS-1$

	/**
	 * Design file name, which tests indirect element reference of library
	 * elements by inheritance.
	 */

	private static final String DESIGN_WITH_INDIRECT_REFERENCE = "DocumentUtilTest_2.xml"; //$NON-NLS-1$

	/**
	 * Design file name. Tests the shared style's name must not become element's
	 * name.
	 */

	private static final String DESIGN_WITH_SHARED_STYLE = "DocumentUtilTest_3.xml"; //$NON-NLS-1$

	/**
	 * Design file name.
	 */

	private static final String DESIGN_WITH_EXTERNAL_SELECTORS = "DocumentUtilTest_4.xml"; //$NON-NLS-1$

	/**
	 * Design file name.
	 */

	private static final String DESIGN_WITH_CUBE_EXTENDS = "DocumentUtilTest_5.xml"; //$NON-NLS-1$

	/**
	 * Design file name.
	 */

	private static final String DESIGN_WITH_TESTING_TABLE_EXTENDS = "DocumentUtilTest_6.xml"; //$NON-NLS-1$

	/**
	 * Design file name.
	 */

	private static final String DESIGN_WITH_FILTER_ELEMENT = "DocumentUtilTest_7.xml"; //$NON-NLS-1$

	/**
	 * Design file name.
	 */

	private static final String DESIGN_WITH_CSS_STYLES = "DocumentUtilTest_8.xml"; //$NON-NLS-1$

	/**
	 * Tests the element property value localization.
	 * 
	 * @throws Exception
	 */

	public void testSerializeWithElementExtends( ) throws Exception
	{
		openDesign( DESIGN_WITH_ELEMENT_EXTENDS );
		assertNotNull( designHandle );

		serializeDocument( );
		assertTrue( compareFile( "DocumentUtilTest_golden.xml" ) ); //$NON-NLS-1$ 
	}

	/**
	 * Tests the lib reference of embedded images.
	 * 
	 * @throws Exception
	 */

	public void testSerializeWithLibReference( ) throws Exception
	{
		openDesign( DESIGN_WITH_STRUCTURE_REFERENCE );
		assertNotNull( designHandle );

		serializeDocument( );
		assertTrue( compareFile( "DocumentUtilTest_golden_1.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * Tests the indirect element references by inheritances during the
	 * serialization.
	 * 
	 * @throws Exception
	 */

	public void testSerializeWithIndirectRef( ) throws Exception
	{
		openDesign( DESIGN_WITH_INDIRECT_REFERENCE );
		assertNotNull( designHandle );

		serializeDocument( );

		assertTrue( compareFile( "DocumentUtilTest_golden_2.xml" ) ); //$NON-NLS-1$ 
	}

	/**
	 * Tests the serlization of template elements.
	 * 
	 * @throws Exception
	 */

	public void testTemplate( ) throws Exception
	{
		openDesign( "TemplateElementParserTest.xml" ); //$NON-NLS-1$
		assertNotNull( designHandle );

		serializeDocument( );
		assertTrue( compareFile( "DocumentUtilTest_golden_3.xml" ) ); //$NON-NLS-1$ 
	}

	/**
	 * when serialize report design, the embedded image from library should be
	 * copied locally.
	 * 
	 * @throws Exception
	 */
	public void testSerializeWithEmbeddedImage( ) throws Exception
	{
		String string = "TestSerializeEmbeddeImage.xml"; //$NON-NLS-1$
		openDesign( string );
		assertNotNull( designHandle );
		serializeDocument( );
		assertTrue( compareFile( "DocumentUtilTest_golden_4.xml" ) ); //$NON-NLS-1$ 

	}

	/**
	 * when there is a external resource file sets for this report, all report
	 * properties use the external string value should be saved into the report
	 * file after serialization. And the reource key should be set to null.
	 * 
	 * @throws Exception
	 * 
	 */
	public void testSerializeExternalString( ) throws Exception
	{
		openDesign( "DocumnetUtilTest_ExternalResource.xml" ); //$NON-NLS-1$
		assertNotNull( designHandle );
		serializeDocument( );

		assertTrue( compareFile( "DocumentUtilTest_golden_5.xml" ) ); //$NON-NLS-1$ 

	}

	/**
	 * For extended item, the extension name must be set first. So that, other
	 * properties can be set properly.
	 * 
	 * @throws Exception
	 */

	public void testExtendedItem( ) throws Exception
	{
		openDesign( "DocumentUtilTest_ExtendedItem.xml" ); //$NON-NLS-1$

		assertNotNull( designHandle );
		serializeDocument( );
		ExtendedItemHandle matrix1 = (ExtendedItemHandle) designHandle
				.findElement( "matrix1" ); //$NON-NLS-1$
		assertNotNull( ( (ExtendedItem) matrix1.getElement( ) ).getExtDefn( ) );

		assertTrue( compareFile( "DocumentUtilTest_golden_6.xml" ) ); //$NON-NLS-1$

	}

	/**
	 * Test the group name is changed or not based on bugzilla 199537.
	 * 
	 * @throws Exception
	 */

	public void testGroup( ) throws Exception
	{
		openDesign( "DocumentUtilTest_Group.xml" ); //$NON-NLS-1$
		serializeDocument( );

		TableHandle tableHandle = (TableHandle) designHandle.getBody( ).get( 0 );
		GroupHandle groupHandle = (GroupHandle) tableHandle.getGroups( )
				.get( 0 );
		assertEquals( "Year", groupHandle.getName( ) ); //$NON-NLS-1$
		groupHandle = (GroupHandle) tableHandle.getGroups( ).get( 1 );
		assertEquals( "Month", groupHandle.getName( ) ); //$NON-NLS-1$
	}

	/**
	 * Tests
	 * 
	 * <ul>
	 * <li>serialize user property or not based on bugzilla 199751.
	 * <li>for extension element, the user property value may be lost.
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testUserProperty( ) throws Exception
	{
		openDesign( "DocumentUtilTest_UserProperty.xml" ); //$NON-NLS-1$
		serializeDocument( );

		// user property is in report design directly.
		assertEquals( "1.0", designHandle.getProperty( "version" ) );//$NON-NLS-1$//$NON-NLS-2$

		// user property is set in label.
		LabelHandle labelHandle = (LabelHandle) designHandle.getBody( ).get( 0 );
		assertEquals( "label 1.0", labelHandle.getProperty( "version" ) );//$NON-NLS-1$//$NON-NLS-2$

		// user property is set in label in library.
		LabelHandle labelHandle1 = (LabelHandle) designHandle.getBody( )
				.get( 1 );
		assertEquals( "2.0", labelHandle1.getProperty( "version" ) );//$NON-NLS-1$//$NON-NLS-2$

		openDesign( "DocumentUtilTest_UserProperty1.xml" ); //$NON-NLS-1$

		serializeDocument( );

		assertTrue( compareFile( "DocumentUtilTest_UserProperty1_golden.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * If two non-named element refers the same shared style, their names should
	 * not be style names.
	 * 
	 * @throws Exception
	 */

	public void testSerializeWithSharedStyle( ) throws Exception
	{
		openDesign( DESIGN_WITH_SHARED_STYLE );
		assertNotNull( designHandle );

		serializeDocument( );

		assertTrue( compareFile( "DocumentUtilTest_shared_style_golden.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * For cubes that contains elements in property values. Should no exception.
	 * 
	 * @throws Exception
	 */

	public void testSerializeWithElementProps( ) throws Exception
	{
		openDesign( "DocumentUtilTest_ElementProps.xml" ); //$NON-NLS-1$
		assertNotNull( designHandle );

		serializeDocument( );
		assertTrue( compareFile( "DocumentUtilTest_ElementProps_golden.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * Tests a design file that is serialized by the document util.
	 * 
	 * @throws Exception
	 */
	public void testParserForSerializer( ) throws Exception
	{
		openDesign( "DocumentUtilTest_parser.xml" ); //$NON-NLS-1$
		assertNotNull( designHandle );
	}

	/**
	 * If two non-named element refers the same shared style, their names should
	 * not be style names.
	 * 
	 * @throws Exception
	 */

	public void testSerializeExternalSelectors( ) throws Exception
	{
		openDesign( DESIGN_WITH_EXTERNAL_SELECTORS );
		assertNotNull( designHandle );

		serializeDocument( );

		// make sure that selectors in the library are in the namespace of the
		// design

		DesignElement localizedStyle = design.getNameHelper( ).getNameSpace(
				Module.STYLE_NAME_SPACE ).getElement( "table-footer" ); //$NON-NLS-1$
		assertNotNull( localizedStyle );

		assertTrue( compareFile( "DocumentUtilTest_external_selectors_golden.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * @throws Exception
	 */

	public void testCubeAndCrosstable( ) throws Exception
	{
		openDesign( DESIGN_WITH_CUBE_EXTENDS );

		serializeDocument( );

		assertTrue( compareFile( "DocumentUtilTest_extends_cube_golden.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * In the getPropertyFromElement() call of the
	 * ReportDesignSerializer.localizePropertyValues(), the module should be
	 * element.getRoot() instead of targetDesign. Otherwise, some elements
	 * cannot be resolved. It could caused preview failure.
	 * 
	 * @throws Exception
	 */

	public void testCubeAndCrosstable1( ) throws Exception
	{
		openDesign( DESIGN_WITH_TESTING_TABLE_EXTENDS );

		serializeDocument( );

		assertTrue( compareFile( "DocumentUtilTest_extends_testingtable_golden.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * For content elements, they should be serialized.
	 * 
	 * @throws Exception
	 */

	public void testFilterElement( ) throws Exception
	{
		openDesign( DESIGN_WITH_FILTER_ELEMENT );

		serializeDocument( );

		assertTrue( compareFile( "DocumentUtilTest_filter_element_golden.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * For css styles, they must be visited and saved in the serialized design.
	 * 
	 * @throws Exception
	 */

	public void testLocalCssStyles( ) throws Exception
	{
		openDesign( DESIGN_WITH_CSS_STYLES );

		serializeDocument( );

		List sheets = designHandle.getAllCssStyleSheets( );
		assertEquals( 1, sheets.size( ) );

		CssStyleSheetHandle sheet = (CssStyleSheetHandle) sheets.get( 0 );
		Iterator styles = sheet.getStyleIterator( );

		int count = 0;
		while ( styles.hasNext( ) )
		{
			styles.next( );
			count++;
		}
		assertEquals( 5, count );

		assertNotNull( designHandle.getSystemId( ) );
		assertNotNull( designHandle.getFileName( ) );
	}

	/**
	 * Tests a structure refers an element and this referred element is a
	 * container element(Hierarchy element). Its children name is not global
	 * unique.
	 * 
	 * @throws Exception
	 */

	public void testSerializeWithElementRefer( ) throws Exception
	{
		openDesign( "DocumentUtilTest_9.xml" ); //$NON-NLS-1$
		serializeDocument( );
		assertTrue( compareFile( "DocumentUtilTest_golden_7.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * Tests property bindings defined in the library, and the data set uses the
	 * binding. After the serialization, the output document should have the
	 * property binding.
	 * 
	 * @throws Exception
	 */

	public void testSerializeExternalPropertyBindings( ) throws Exception
	{
		openDesign( "DocumentUtilTest_PropBindings.xml" ); //$NON-NLS-1$
		serializeDocument( );

		assertTrue( compareFile( "DocumentUtilTest_propBindings_golden.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * Tests the serialization when the design defines local translation table
	 * and included libraries.
	 * 
	 * @throws Exception
	 */
	public void testLocalTranslationTable( ) throws Exception
	{
		openDesign( "DocumentUtilTest_10.xml" ); //$NON-NLS-1$
		serializeDocument( );

		assertTrue( compareFile( "DocumentUtilTest_golden_8.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * Tests the serialization when the scriptLibs of library is flatten to the
	 * report file and the report file does not contain the scriptLibs.
	 * 
	 * @throws Exception
	 */
	public void testFlattenReportWithoutScriptLib( ) throws Exception
	{
		openDesign( "DocumentUtilTest_11.xml" ); //$NON-NLS-1$
		serializeDocument( );

		assertTrue( compareFile( "DocumentUtilTest_golden_11.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * Tests the serialization when the scriptLibs of library is flatten to the
	 * report file and the report file contain the scriptLibs.
	 * 
	 * @throws Exception
	 */
	public void testFlattenReportWithScriptLib( ) throws Exception
	{
		openDesign( "DocumentUtilTest_12.xml" ); //$NON-NLS-1$
		serializeDocument( );

		assertTrue( compareFile( "DocumentUtilTest_golden_12.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * when there are some external resource files set for the report and
	 * libraries, the external resource file should be saved into the report
	 * file after serialization.
	 * 
	 * @throws Exception
	 */
	public void testFlattenReportWithIncludedResource( ) throws Exception
	{

		// flatten the external resource files to the report before version
		// 3.2.16
		openDesign( "DocumentUtilTest_13.xml" ); //$NON-NLS-1$
		serializeDocument( );

		assertTrue( compareFile( "DocumentUtilTest_golden_13.xml" ) ); //$NON-NLS-1$

		// flatten the external resource files to the report which is version
		// 3.2.16
		openDesign( "DocumentUtilTest_14.xml" ); //$NON-NLS-1$
		serializeDocument( );

		assertTrue( compareFile( "DocumentUtilTest_golden_14.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * Tests the container information of the filter condition element in the
	 * extended item is set correctly, when this element is copied from the
	 * library to the design report.
	 * 
	 * @throws Exception
	 */
	public void testFlattenReport( ) throws Exception
	{
		openDesign( "DocumentUtilTest_15.xml" ); //$NON-NLS-1$

		serializeDocument( );

		DesignElementHandle handle = designHandle.getElementByID( 19 );
		List list = (List) handle.getProperty( "filter" );//$NON-NLS-1$
		FilterConditionElementHandle elementHandle = (FilterConditionElementHandle) list
				.get( 0 );
		assertTrue( elementHandle.getElement( ).getContainer( ) != null );

		assertTrue( compareFile( "DocumentUtilTest_golden_15.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * Tests the function to rename dimension/level names in the binding
	 * expression and aggregation on lists.
	 * 
	 * @throws Exception
	 */

	public void testUpdateOLAPElementNames( ) throws Exception
	{
		openDesign( "DocumentUtilTest_OLAPBindings.xml" ); //$NON-NLS-1$

		serializeDocument( );

		assertTrue( compareFile( "DocumentUtilTest_OLAPBindings_golden.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * Tests the function to rename dimension/level names in the binding
	 * expression and aggregation on lists.
	 * 
	 * @throws Exception
	 */

	public void testExternalHostElements( ) throws Exception
	{
		openDesign( "DocumentUtilTest_hostElements.xml" ); //$NON-NLS-1$

		serializeDocument( );

		assertTrue( compareFile( "DocumentUtilTest_hostElements_golden.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * Tests Flatten default pageBreakInterval property before new version.
	 * 
	 * @throws Exception
	 */
	public void testFlattenPageBreakInterval( ) throws Exception
	{
		// flatten table within single report
		openDesign( "DocumentUtilTest_PageBreakInterval.xml" ); //$NON-NLS-1$

		serializeDocument( );

		assertTrue( compareFile( "DocumentUtilTest_PageBreakInterval_golden.xml" ) ); //$NON-NLS-1$

		// flatten table with library

		openDesign( "DocumentUtilTest_PageBreakInterval_1.xml" ); //$NON-NLS-1$

		serializeDocument( );

		assertTrue( compareFile( "DocumentUtilTest_PageBreakInterval_golden_1.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * Tests variable elements with the library.
	 * 
	 * @throws Exception
	 */

	public void testVariableElements( ) throws Exception
	{
		// flatten variable elements with the library

		openDesign( "DocumentUtilTest_16.xml" ); //$NON-NLS-1$

		serializeDocument( );

		assertTrue( compareFile( "DocumentUtilTest_golden_16.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * Tests to flatten a report with expressions contain new line.
	 * 
	 * @throws Exception
	 */
	public void testExpressionWithNewLine( )  throws Exception
	{
		openDesign( "DocumentUtilTest_17.xml" ); //$NON-NLS-1$

		os = new ByteArrayOutputStream( );
		DocumentUtil.serialize( designHandle, os );
		
		assertTrue( compareFile( "DocumentUtilTest_golden_17.xml" ) ); //$NON-NLS-1$
	}
	
	public void testSameReportItemTheme( ) throws Exception
	{
		openDesign( "reportItemThemeFromLib.xml" ); //$NON-NLS-1$
		ReportDesignHandle newDesign = DocumentUtil.serialize( designHandle,
				new ByteArrayOutputStream( ) );
		List<DesignElementHandle> themes = newDesign.getSlot(
				IReportDesignModel.THEMES_SLOT ).getContents( );
		HashSet<String> themeName = new HashSet<String>( );
		for (DesignElementHandle theme : themes)
		{
			if ( theme instanceof ReportItemThemeHandle )
			{
				ReportItemThemeHandle reportItemTheme = (ReportItemThemeHandle) theme;
				String name = reportItemTheme.getName( );
				assertFalse( themeName.contains( name ) );
				themeName.add( name );
			}
		}

		List<DesignElementHandle> items = newDesign.getSlot(
				IReportDesignModel.BODY_SLOT ).getContents( );
		themeName.clear( );
		for (DesignElementHandle item : items)
		{
			ReportItemHandle reportItem = (ReportItemHandle) item;
			ReportItemThemeHandle theme = reportItem.getTheme( );
			if ( theme != null )
			{
				assertFalse( themeName.contains( theme.getName( ) ) );
				themeName.add( theme.getName( ) );
			}
		}

	}
}
