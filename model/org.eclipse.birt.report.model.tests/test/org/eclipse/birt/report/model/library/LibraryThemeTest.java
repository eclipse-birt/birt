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

package org.eclipse.birt.report.model.library;

import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.IncludedCssStyleSheetHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.CssException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.command.ThemeException;
import org.eclipse.birt.report.model.api.core.IAccessControl;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;
import org.eclipse.birt.report.model.api.elements.structures.IncludedCssStyleSheet;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyledElementModel;
import org.eclipse.birt.report.model.metadata.ColorPropertyType;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests theme functionality in the library and report design.
 */

public class LibraryThemeTest extends BaseTestCase
{

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp( ) throws Exception
	{
		super.setUp( );
	}

	/**
	 * Tests css style sheet in library theme.
	 * <tr>
	 * <td>add css file
	 * <td>check reference is unreslove or not
	 * </tr>
	 * <tr>
	 * <td>drop css file
	 * <td>check reference is unreslove or not
	 * </tr>
	 * 
	 * @throws Exception
	 */

	public void testCssStyleSheet( ) throws Exception
	{
		openLibrary( "BlankLibrary.xml" ); //$NON-NLS-1$

		ThemeHandle themeHandle = (ThemeHandle) libraryHandle.getThemes( ).get(
				0 );
		assertTrue( themeHandle.canAddCssStyleSheet( getResource(
				"input/base.css" ).getFile( ) ) );//$NON-NLS-1$
		assertTrue( themeHandle.canAddCssStyleSheet( "base.css" ) );//$NON-NLS-1$

		// test add css sheet

		CssStyleSheetHandle sheetHandle = libraryHandle
				.openCssStyleSheet( getResource( "input/base.css" ).getFile( ) );//$NON-NLS-1$
		assertNull( sheetHandle.getContainerHandle( ) );

		themeHandle.addCss( sheetHandle );

		assertFalse( themeHandle.canAddCssStyleSheet( sheetHandle ) );
		assertFalse( themeHandle.canAddCssStyleSheet( getResource(
				"input/base.css" ).getFile( ) ) );//$NON-NLS-1$

		List styles = themeHandle.getAllStyles( );
		assertEquals( 9, styles.size( ) );

		assertNotNull( sheetHandle.getContainerHandle( ) );

		try
		{
			themeHandle.addCss( sheetHandle );
			fail( );
		}
		catch ( CssException e )
		{
			assertEquals( CssException.DESIGN_EXCEPTION_DUPLICATE_CSS, e
					.getErrorCode( ) );
		}

		// label use it
		LabelHandle labelHandle = libraryHandle.getElementFactory( ).newLabel(
				"label" );//$NON-NLS-1$
		libraryHandle.getComponents( ).add( labelHandle );
		labelHandle.setStyle( (SharedStyleHandle) styles.get( 0 ) );

		// drop css
		assertTrue( themeHandle.canDropCssStyleSheet( sheetHandle ) );

		assertNotNull( labelHandle.getStyle( ) );

		// before drop , style is null

		themeHandle.dropCss( sheetHandle );
		assertNull( themeHandle.includeCssesIterator( ).next( ) );
		assertNull( labelHandle.getStyle( ) );

		assertNull( labelHandle.getElement( ).getStyle( ) );
		assertFalse( themeHandle.canDropCssStyleSheet( sheetHandle ) );
		assertNull( sheetHandle.getContainerHandle( ) );
		// add css file name
		themeHandle.addCss( "base.css" ); //$NON-NLS-1$
		styles = themeHandle.getAllStyles( );
		assertEquals( 9, styles.size( ) );

		CssStyleSheetHandle stylySheetHandle = themeHandle
				.findCssStyleSheetHandleByName( "base.css" ); //$NON-NLS-1$
		assertNotNull( stylySheetHandle );
		assertEquals( "base.css", stylySheetHandle.getFileName( ) );//$NON-NLS-1$

		IncludedCssStyleSheetHandle includedStylySheetHandle = themeHandle
				.findIncludedCssStyleSheetHandleByName( "base.css" );//$NON-NLS-1$
		assertNotNull( includedStylySheetHandle );
		assertEquals( "base.css", includedStylySheetHandle.getFileName( ) );//$NON-NLS-1$

		IncludedCssStyleSheet cssStruct = StructureFactory
				.createIncludedCssStyleSheet( );
		cssStruct.setFileName( "base1.css" ); //$NON-NLS-1$
		themeHandle.addCss( cssStruct );

		cssStruct = (IncludedCssStyleSheet) themeHandle.getListProperty(
				ReportDesignHandle.CSSES_PROP ).get( 1 );
		assertEquals( "base1.css", cssStruct.getFileName( ) ); //$NON-NLS-1$

	}

	/**
	 * Test change theme with css style
	 * 
	 * @throws Exception
	 */
	public void testChangeTheme( ) throws Exception
	{
		openDesign( "LibraryThemeTest_ChangeTheme.xml" ); //$NON-NLS-1$
		LabelHandle labelHandle = (LabelHandle) designHandle
				.findElement( "label" );//$NON-NLS-1$

		assertEquals( "left", labelHandle.getStyle( ).getTextAlign( ) );//$NON-NLS-1$

		LibraryHandle libHandle = designHandle
				.getLibrary( "LibraryThemeTest_TwoTheme" );;//$NON-NLS-1$
		ThemeHandle themeHandle = (ThemeHandle) libHandle.getThemes( ).get( 1 );
		assertEquals( "theme2", themeHandle.getName( ) );;//$NON-NLS-1$

		// change theme
		designHandle.setTheme( themeHandle );
		assertEquals( "center", labelHandle.getStyle( ).getTextAlign( ) );//$NON-NLS-1$
		StyleHandle styleHandle = themeHandle.findStyle( "code" ); //$NON-NLS-1$
		assertNotNull( styleHandle );
		List rules = labelHandle
				.getListProperty( StyleHandle.HIGHLIGHT_RULES_PROP );
		HighlightRule rule = (HighlightRule) rules.get( 0 );
		ElementRefValue value = (ElementRefValue) rule.getProperty( design,
				HighlightRule.STYLE_MEMBER );
		assertEquals( styleHandle.getElement( ), value.getElement( ) );
		rule = (HighlightRule) rules.get( 1 );
		value = (ElementRefValue) rule.getProperty( design,
				HighlightRule.STYLE_MEMBER );
		assertEquals( styleHandle.getElement( ), value.getElement( ) );
		rule = (HighlightRule) rules.get( 2 );
		value = (ElementRefValue) rule.getProperty( design,
				HighlightRule.STYLE_MEMBER );
		assertEquals( styleHandle.getElement( ), value.getElement( ) );

		assertEquals( 4, ( (StyleElement) styleHandle.getElement( ) )
				.getClientList( ).size( ) );

		// now change theme to null and then check the update back reference by
		// all the styles in the theme2: case for bug 283988
		designHandle.setTheme( null );

	}

	/**
	 * Tests how to resolve a style element. The path is: local custom style ->
	 * library custom style -> library selector -> selector in the module.
	 * 
	 * @throws Exception
	 */

	public void testGetProperty( ) throws Exception
	{
		openDesign( "DesignWithThemeInLibrary.xml" ); //$NON-NLS-1$

		LabelHandle label1 = (LabelHandle) designHandle.findElement( "label1" ); //$NON-NLS-1$
		LabelHandle label2 = (LabelHandle) designHandle.findElement( "label2" ); //$NON-NLS-1$
		LabelHandle label3 = (LabelHandle) designHandle.findElement( "label3" ); //$NON-NLS-1$

		ListHandle list1 = (ListHandle) designHandle.findElement( "list1" ); //$NON-NLS-1$

		assertEquals( ColorPropertyType.SILVER, label1
				.getProperty( Style.COLOR_PROP ) );

		assertEquals( ColorPropertyType.LIME, label2
				.getProperty( Style.COLOR_PROP ) );

		assertEquals( ColorPropertyType.MAROON, label3
				.getProperty( Style.COLOR_PROP ) );

		assertEquals( ColorPropertyType.GRAY, list1
				.getProperty( Style.COLOR_PROP ) );
	}

	/**
	 * Test cases:
	 * 
	 * 1. when the theme is set, the property value of style should be changed.
	 * 2. when the theme is set, the notification is sent to the module.
	 * 
	 * @throws Exception
	 */

	public void testSetTheme( ) throws Exception
	{
		openDesign( "DesignWithThemeInLibrary.xml" ); //$NON-NLS-1$

		LabelHandle label2 = (LabelHandle) designHandle.findElement( "label2" ); //$NON-NLS-1$
		ListHandle list1 = (ListHandle) designHandle.findElement( "list1" ); //$NON-NLS-1$

		assertEquals( ColorPropertyType.LIME, label2
				.getProperty( Style.COLOR_PROP ) );

		assertEquals( ColorPropertyType.GRAY, list1
				.getProperty( Style.COLOR_PROP ) );

		ThemeHandle theme2 = designHandle.findTheme( "Theme.theme2" ); //$NON-NLS-1$
		assertNotNull( theme2 );
		designHandle.setTheme( theme2 );

		assertEquals( ColorPropertyType.AQUA, label2
				.getProperty( Style.COLOR_PROP ) );

		assertEquals( ColorPropertyType.BLUE, list1
				.getProperty( Style.COLOR_PROP ) );

		designHandle.getCommandStack( ).undo( );

		assertEquals( ColorPropertyType.LIME, label2
				.getProperty( Style.COLOR_PROP ) );

		assertEquals( ColorPropertyType.GRAY, list1
				.getProperty( Style.COLOR_PROP ) );

		designHandle.getCommandStack( ).redo( );

		assertEquals( ColorPropertyType.AQUA, label2
				.getProperty( Style.COLOR_PROP ) );

		assertEquals( ColorPropertyType.BLUE, list1
				.getProperty( Style.COLOR_PROP ) );

		// test the notification to the module

		MyListener listener = new MyListener( );
		MyListener labelListener = new MyListener( );
		label2.addListener( labelListener );
		designHandle.addListener( listener );

		designHandle.setThemeName( "Theme.theme1" ); //$NON-NLS-1$
		assertEquals( 1, listener.counter );
		assertEquals( 1, labelListener.counter );

		designHandle.getCommandStack( ).undo( );
		assertEquals( 2, listener.counter );

		designHandle.getCommandStack( ).redo( );
		assertEquals( 3, listener.counter );

		// set the theme to a new one

		libraryHandle = designHandle.getLibrary( "Theme" ); //$NON-NLS-1$
		ThemeHandle theme = libraryHandle.getElementFactory( ).newTheme(
				"Theme3" ); //$NON-NLS-1$

		try
		{
			designHandle.setTheme( theme );
			fail( );
		}
		catch ( ThemeException e )
		{
			assertEquals( ThemeException.DESIGN_EXCEPTION_NOT_FOUND, e
					.getErrorCode( ) );
			assertEquals( "Theme.Theme3", e.getTheme( ) ); //$NON-NLS-1$
		}

		try
		{
			designHandle.setProperty( IModuleModel.THEME_PROP, theme );
			fail( );
		}
		catch ( ThemeException e )
		{
			assertEquals( ThemeException.DESIGN_EXCEPTION_NOT_FOUND, e
					.getErrorCode( ) );
			assertEquals( "Theme.Theme3", e.getTheme( ) ); //$NON-NLS-1$
		}

		try
		{
			designHandle.setProperty( IModuleModel.THEME_PROP, theme
					.getElement( ) );
			fail( );
		}
		catch ( ThemeException e )
		{
			assertEquals( ThemeException.DESIGN_EXCEPTION_NOT_FOUND, e
					.getErrorCode( ) );
			assertEquals( "Theme.Theme3", e.getTheme( ) ); //$NON-NLS-1$
		}

	}

	/**
	 * Test cases: 1. remove/add styles in the library and see effects to the
	 * library.
	 * 
	 * @throws Exception
	 */

	public void testAddRemoveStyleLocally( ) throws Exception
	{
		openLibrary( "LibraryTheme.xml" ); //$NON-NLS-1$

		ThemeHandle theme1 = libraryHandle.findTheme( "theme1" ); //$NON-NLS-1$
		assertNotNull( theme1 );

		StyleHandle myStyle1 = libraryHandle.getElementFactory( ).newStyle(
				"libStyle2" ); //$NON-NLS-1$
		myStyle1.getColor( ).setStringValue( ColorPropertyType.NAVY );
		MyListener listener = new MyListener( );

		LabelHandle label1 = (LabelHandle) libraryHandle.findElement( "label1" ); //$NON-NLS-1$		
		label1.addListener( listener );

		theme1.getStyles( ).add( myStyle1 );
		assertEquals( ColorPropertyType.NAVY, label1
				.getProperty( Style.COLOR_PROP ) );

		// no specified notifications for custom styles

		assertEquals( 0, listener.counter );

		TextItemHandle text1 = (TextItemHandle) libraryHandle
				.findElement( "text1" ); //$NON-NLS-1$

		StyleHandle textSelector = libraryHandle.getElementFactory( ).newStyle(
				"text" ); //$NON-NLS-1$
		textSelector.getColor( ).setStringValue( ColorPropertyType.NAVY );
		listener = new MyListener( );
		text1.addListener( listener );

		theme1.getStyles( ).add( textSelector );
		assertEquals( ColorPropertyType.NAVY, text1
				.getProperty( Style.COLOR_PROP ) );

		assertEquals( 2, listener.counter );

		libraryHandle.getCommandStack( ).undo( );

		assertEquals( ColorPropertyType.BLACK, text1
				.getProperty( Style.COLOR_PROP ) );

		assertEquals( 4, listener.counter );
	}

	/**
	 * Test cases:
	 * <p>
	 * 1. remove/add themes in the library and see effects to the library.
	 * 
	 * @throws Exception
	 */

	public void testAddRemoveThemeLocally( ) throws Exception
	{
		openLibrary( "LibraryTheme.xml" ); //$NON-NLS-1$

		assertNotNull( libraryHandle.getTheme( ) );

		StyleHandle libStyle1 = libraryHandle.findStyle( "libStyle1" ); //$NON-NLS-1$
		libStyle1.drop( );

		libraryHandle.getCommandStack( ).undo( );

		LabelHandle label3 = (LabelHandle) libraryHandle.findElement( "label3" ); //$NON-NLS-1$
		assertEquals( "lime", label3.getStringProperty( Style.COLOR_PROP ) ); //$NON-NLS-1$

		ThemeHandle theme1 = libraryHandle.findTheme( "theme1" ); //$NON-NLS-1$
		assertNotNull( theme1 );

		// do not consider notifications.

		TextItemHandle text1 = (TextItemHandle) libraryHandle
				.findElement( "text1" ); //$NON-NLS-1$

		LabelHandle label1 = (LabelHandle) libraryHandle.findElement( "label1" ); //$NON-NLS-1$		
		LabelHandle label2 = (LabelHandle) libraryHandle.findElement( "label2" ); //$NON-NLS-1$

		assertEquals( ColorPropertyType.BLACK, text1
				.getProperty( Style.COLOR_PROP ) );
		assertEquals( ColorPropertyType.RED, label1
				.getProperty( Style.COLOR_PROP ) );
		assertEquals( ColorPropertyType.RED, label2
				.getProperty( Style.COLOR_PROP ) );

		libraryHandle.getThemes( ).drop( theme1 );

		assertEquals( ColorPropertyType.BLACK, text1
				.getProperty( Style.COLOR_PROP ) );
		assertEquals( ColorPropertyType.BLACK, label1
				.getProperty( Style.COLOR_PROP ) );
		assertEquals( ColorPropertyType.BLACK, label2
				.getProperty( Style.COLOR_PROP ) );
		// unresolve the back-ref for all the styles in the dropped theme
		assertNotNull( label3.getProperty( IStyledElementModel.STYLE_PROP ) );

		// theme is added.
		libraryHandle.getCommandStack( ).undo( );
		assertEquals( ColorPropertyType.RED, label1
				.getProperty( Style.COLOR_PROP ) );

		// drop the theme
		libraryHandle.getCommandStack( ).redo( );
		assertEquals( ColorPropertyType.BLACK, label1
				.getProperty( Style.COLOR_PROP ) );

		// test dropAndClear
		libraryHandle.getCommandStack( ).undo( );
		libraryHandle.getThemes( ).dropAndClear( theme1 );
		assertNull( label3.getProperty( IStyledElementModel.STYLE_PROP ) );

	}

	/**
	 * @throws Exception
	 */

	public void testSetPropertyOnSeclector( ) throws Exception
	{
		openLibrary( "LibraryTheme.xml" ); //$NON-NLS-1$

		TextItemHandle text1 = (TextItemHandle) libraryHandle
				.findElement( "text1" ); //$NON-NLS-1$

		ThemeHandle theme3 = libraryHandle.findTheme( "theme3" ); //$NON-NLS-1$
		assertNotNull( theme3 );

		libraryHandle.setTheme( theme3 );

		MyListener listener = new MyListener( );
		text1.addListener( listener );

		StyleHandle textSelector = theme3.findStyle( "text" ); //$NON-NLS-1$
		assertNotNull( textSelector );

		textSelector.getColor( ).setStringValue( ColorPropertyType.BLUE );
		assertEquals( 2, listener.counter );

		assertEquals( ColorPropertyType.BLUE, text1
				.getProperty( Style.COLOR_PROP ) );
	}

	/**
	 * @throws Exception
	 */

	public void testThemeWithAddRemoveLibrary( ) throws Exception
	{
		openDesign( "DesignWithThemeInLibrary.xml" ); //$NON-NLS-1$

		LabelHandle label2 = (LabelHandle) designHandle.findElement( "label2" ); //$NON-NLS-1$
		ListHandle list1 = (ListHandle) designHandle.findElement( "list1" ); //$NON-NLS-1$

		assertEquals( ColorPropertyType.LIME, label2
				.getProperty( Style.COLOR_PROP ) );

		assertEquals( ColorPropertyType.GRAY, list1
				.getProperty( Style.COLOR_PROP ) );

		LibraryHandle libTheme = designHandle.getLibrary( "Theme" ); //$NON-NLS-1$
		designHandle.dropLibrary( libTheme );

		assertEquals( ColorPropertyType.MAROON, label2
				.getProperty( Style.COLOR_PROP ) );

		assertEquals( ColorPropertyType.BLACK, list1
				.getProperty( Style.COLOR_PROP ) );

		designHandle.getCommandStack( ).undo( );

		assertEquals( ColorPropertyType.LIME, label2
				.getProperty( Style.COLOR_PROP ) );

		assertEquals( ColorPropertyType.GRAY, list1
				.getProperty( Style.COLOR_PROP ) );

		designHandle.getCommandStack( ).redo( );

		assertEquals( ColorPropertyType.MAROON, label2
				.getProperty( Style.COLOR_PROP ) );

		assertEquals( ColorPropertyType.BLACK, list1
				.getProperty( Style.COLOR_PROP ) );

	}

	/**
	 * @throws Exception
	 */

	public void testWriter( ) throws Exception
	{
		openDesign( "DesignWithThemeInLibrary.xml" ); //$NON-NLS-1$

		assertEquals( "theme1", designHandle.getTheme( ).getName( ) ); //$NON-NLS-1$

		ThemeHandle theme2 = designHandle.findTheme( "Theme.theme2" ); //$NON-NLS-1$
		assertNotNull( theme2 );

		designHandle.setTheme( theme2 );

		save( );

		assertTrue( compareFile( "DesignWithThemeInLibrary_golden.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * @throws Exception
	 */

	public void testCompatibleLibraryWithStyles( ) throws Exception
	{
		openLibrary( "CompatibleLibraryTheme.xml" ); //$NON-NLS-1$

		saveLibrary( );
		assertTrue( compareFile( "CompatibleLibraryTheme_golden.xml" ) ); //$NON-NLS-1$

	}

	/**
	 * Test the meachanism to check the duplidate style name in one theme.
	 * 
	 * @throws Exception
	 */

	public void testCanContainStyle( ) throws Exception
	{
		createLibrary( );

		ThemeHandle theme = libraryHandle.getElementFactory( ).newTheme(
				"testTheme" ); //$NON-NLS-1$

		StyleHandle style1 = libraryHandle.getElementFactory( ).newStyle(
				"sytle1" ); //$NON-NLS-1$
		StyleHandle style2 = libraryHandle.getElementFactory( ).newStyle(
				"sytle1" ); //$NON-NLS-1$

		theme.getStyles( ).add( style1 );
		try
		{
			theme.getStyles( ).add( style2 );
			fail( );
		}
		catch ( NameException e )
		{
			assertEquals( NameException.DESIGN_EXCEPTION_DUPLICATE, e
					.getErrorCode( ) );
		}

		style2.setName( "style2" ); //$NON-NLS-1$
		theme.getStyles( ).add( style2 );
	}

	/**
	 * Tests the compatibility work when create the library. A default theme is
	 * created and the theme property of the library is set to be this default
	 * theme.
	 * 
	 * @throws Exception
	 * 
	 */

	public void testCreateLibrary( ) throws Exception
	{
		createLibrary( );

		assertEquals( 1, libraryHandle.getThemes( ).getCount( ) );

		saveLibrary( );
		assertTrue( compareFile( "CompatibleLibraryThemeEmpty_golden.xml" ) ); //$NON-NLS-1$

	}

	/**
	 * Tests the compatibility work to open a library without styles slot. A
	 * default theme is created and the theme property of the library is set to
	 * be this default theme.
	 * 
	 * @throws Exception
	 * 
	 */

	public void testCompatibleLibraryWithoutStyles( ) throws Exception
	{
		openLibrary( "CompatibleLibraryThemeWithoutStyles.xml" ); //$NON-NLS-1$

		saveLibrary( );
		assertTrue( compareFile( "CompatibleLibraryThemeWithoutStyles_golden.xml" ) ); //$NON-NLS-1$

	}

	/**
	 * Tests the clone method of theme element.
	 * 
	 * @throws Exception
	 * 
	 */

	public void testClone( ) throws Exception
	{
		createLibrary( );

		ThemeHandle theme1 = libraryHandle.getElementFactory( ).newTheme(
				"theme1" ); //$NON-NLS-1$

		libraryHandle.getThemes( ).add( theme1 );

		ThemeHandle themeCloned = (ThemeHandle) theme1.copy( ).getHandle(
				libraryHandle.getModule( ) );

		StyleHandle style1 = libraryHandle.getElementFactory( ).newStyle(
				"style1" ); //$NON-NLS-1$
		theme1.getStyles( ).add( style1 );

		assertEquals( 1, theme1.getStyles( ).getCount( ) );
		assertEquals( 0, themeCloned.getStyles( ).getCount( ) );

	}

	/**
	 * @throws Exception
	 */

	public void testAddStyleWithThemeInLibrary( ) throws Exception
	{
		createLibrary( );
		ElementFactory libFactory = libraryHandle.getElementFactory( );

		ThemeHandle theme = libFactory.newTheme( "them1" ); //$NON-NLS-1$
		theme.getStyles( ).add( libFactory.newStyle( "style1" ) ); //$NON-NLS-1$

		libraryHandle.getThemes( ).add( theme );
		Library library = (Library) libraryHandle.getModule( );

		assertEquals( 2, library.getNameHelper( ).getNameSpace(
				Module.THEME_NAME_SPACE ).getCount( ) );
		assertEquals( 0, library.getNameHelper( ).getNameSpace(
				Module.STYLE_NAME_SPACE ).getCount( ) );

		theme.getStyles( ).add( libFactory.newStyle( "style2" ) ); //$NON-NLS-1$
		assertEquals( 0, library.getNameHelper( ).getNameSpace(
				Module.STYLE_NAME_SPACE ).getCount( ) );
	}

	/**
	 * Test cases:
	 * 
	 * <ul>
	 * <li>1. includes two libraries with themes, set the design theme property
	 * with one of themes. No exception expected.
	 * 
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testCreateLibraryAndSetTheme( ) throws Exception
	{
		openDesign( "DesignWithThemesInTwoLibraries.xml" ); //$NON-NLS-1$

		designHandle.includeLibrary( "LibraryAIncludeTheme.xml", "libA" ); //$NON-NLS-1$ //$NON-NLS-2$
		designHandle.includeLibrary( "LibraryBIncludeTheme.xml", "libB" ); //$NON-NLS-1$ //$NON-NLS-2$

		designHandle.setThemeName( "libA.theme1" ); //$NON-NLS-1$

		TableHandle table = (TableHandle) designHandle.findElement( "mytable" ); //$NON-NLS-1$
		LabelHandle label = (LabelHandle) designHandle.findElement( "mylabel" ); //$NON-NLS-1$
		assertNotNull( table );
		assertNotNull( label );

		assertEquals( "#FF0000", table.getStringProperty( Style.COLOR_PROP ) ); //$NON-NLS-1$
		assertEquals( ColorPropertyType.BLACK, label
				.getStringProperty( Style.COLOR_PROP ) );
	}

	/**
	 * Test cases:
	 * 
	 * <ul>
	 * <li>1. includes one librarie with theme, assert getDisplayLabel() returns
	 * qualified name of the item. No exception expected.
	 * 
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testGetDisplayLabel( ) throws Exception
	{
		openDesign( "DesignWithThemesInTwoLibraries.xml" ); //$NON-NLS-1$

		designHandle.includeLibrary( "LibraryAIncludeTheme.xml", "libA" ); //$NON-NLS-1$ //$NON-NLS-2$

		assertEquals( "libA.theme1", ( (ThemeHandle) designHandle //$NON-NLS-1$
				.getVisibleThemes( IAccessControl.DIRECTLY_INCLUDED_LEVEL )
				.get( 1 ) ).getDisplayLabel( IDesignElementModel.FULL_LABEL ) );

		assertEquals( "libA.theme1", ( (ThemeHandle) designHandle //$NON-NLS-1$
				.getVisibleThemes( IAccessControl.DIRECTLY_INCLUDED_LEVEL )
				.get( 1 ) ).getDisplayLabel( ) );

		assertEquals( "libA.theme1", ( (ThemeHandle) designHandle //$NON-NLS-1$
				.getVisibleThemes( IAccessControl.DIRECTLY_INCLUDED_LEVEL )
				.get( 1 ) ).getDisplayLabel( IDesignElementModel.USER_LABEL ) );

	}

	/**
	 * Test cases:
	 * 
	 * <ul>
	 * <li>For the design, directly included libraries have no themes. <code>
	 * getAllThemes()</code>
	 * return 0.
	 * <li>For the library, directly included libraries have 2 themes.
	 * <code>getAllThemes()</code> return 2.
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testGetAllThemes( ) throws Exception
	{
		openDesign( "DesignWithoutLibrary.xml" ); //$NON-NLS-1$
		designHandle.includeLibrary( "LibraryIncludingTwoLibraries.xml", //$NON-NLS-1$
				"CompsiteLib" ); //$NON-NLS-1$

		List list = designHandle
				.getVisibleThemes( IAccessControl.DIRECTLY_INCLUDED_LEVEL );
		assertEquals( 0, list.size( ) );

		libraryHandle = designHandle.getLibrary( "CompsiteLib" ); //$NON-NLS-1$
		assertEquals( 2, libraryHandle.getVisibleThemes(
				IAccessControl.DIRECTLY_INCLUDED_LEVEL ).size( ) );
		assertEquals( 0, libraryHandle.getVisibleThemes(
				IAccessControl.NATIVE_LEVEL ).size( ) );
	}

	/**
	 * Test cases:
	 * 
	 * <ul>
	 * <li>Rename a style with duplicate name. The result is that NameException
	 * is thrown.
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testRenameStyleInTheme( ) throws Exception
	{
		createLibrary( );

		ElementFactory libFactory = libraryHandle.getElementFactory( );

		ThemeHandle theme = libFactory.newTheme( "them1" ); //$NON-NLS-1$

		libraryHandle.getThemes( ).add( theme );

		StyleHandle style = libFactory.newStyle( "style1" );//$NON-NLS-1$		
		theme.getStyles( ).add( style );

		style = libFactory.newStyle( "style2" );//$NON-NLS-1$		
		theme.getStyles( ).add( style );

		try
		{
			style.setName( "style1" ); //$NON-NLS-1$	
			fail( );
		}
		catch ( NameException e )
		{
			assertEquals( NameException.DESIGN_EXCEPTION_DUPLICATE, e
					.getErrorCode( ) );
		}

	}

	/**
	 * When remove library with css style which is used in report design, report
	 * design should receive event message. for bugzilla 192171.
	 * 
	 * @throws Exception
	 */
	public void testRemoveStyleInTheme( ) throws Exception
	{
		openDesign( "LibraryThemeTest.xml" );//$NON-NLS-1$		

		LibraryHandle libHandle = (LibraryHandle) designHandle.getLibraries( )
				.get( 0 );
		LabelHandle labelHandle = (LabelHandle) designHandle
				.getElementByID( 7l );

		assertNotNull( labelHandle.getStyle( ) );
		MyListener listener = new MyListener( );
		labelHandle.addListener( listener );

		designHandle.dropLibrary( libHandle );
		assertNull( labelHandle.getStyle( ) );

		assertEquals( 1, listener.getCounter( ) );
	}

	class MyListener implements Listener
	{

		int counter = 0;

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.report.model.core.Listener#notify(org.eclipse.birt
		 * .report.model.core.DesignElement,
		 * org.eclipse.birt.report.model.activity.NotificationEvent)
		 */
		public void elementChanged( DesignElementHandle focus,
				NotificationEvent ev )
		{
			if ( ev.getEventType( ) == NotificationEvent.STYLE_EVENT
					|| ev.getEventType( ) == NotificationEvent.THEME_EVENT )
				counter++;
		}

		/**
		 * Returns counter.
		 * 
		 * @return
		 */
		public int getCounter( )
		{
			return counter;
		}
	}

	/**
	 * Test cases:
	 * 
	 * Open an design file containing an invalid theme. A content exception is
	 * expected. Added for bugzilla 276218.
	 * 
	 * @throws Exception
	 */
	public void testAddNonExistingTheme( ) throws Exception
	{
		openDesign( "DesignWithInvalidTheme.xml" ); //$NON-NLS-1$		
		assertEquals( 1, design.getAllExceptions( ).size( ) );
		assertTrue( design.getAllExceptions( ).get( 0 ) instanceof ThemeException );
		ThemeException e = (ThemeException) design.getAllExceptions( ).get( 0 );
		assertEquals( ThemeException.DESIGN_EXCEPTION_NOT_FOUND, e
				.getErrorCode( ) );
	}
}
