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
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.command.ThemeException;
import org.eclipse.birt.report.model.api.core.IAccessControl;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.metadata.ColorPropertyType;
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
			assertEquals( "Theme3", e.getTheme( ) ); //$NON-NLS-1$
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

		assertEquals( 1, listener.counter );

		libraryHandle.getCommandStack( ).undo( );

		assertEquals( ColorPropertyType.BLACK, text1
				.getProperty( Style.COLOR_PROP ) );

		assertEquals( 2, listener.counter );
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

		// theme is added.

		libraryHandle.getCommandStack( ).undo( );

		assertEquals( ColorPropertyType.RED, label1
				.getProperty( Style.COLOR_PROP ) );

		// drop the theme

		libraryHandle.getCommandStack( ).redo( );

		assertEquals( ColorPropertyType.BLACK, label1
				.getProperty( Style.COLOR_PROP ) );

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
		assertEquals( 1, listener.counter );

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
		assertTrue( compareTextFile( "DesignWithThemeInLibrary_golden.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * @throws Exception
	 */

	public void testCompatibleLibraryWithStyles( ) throws Exception
	{
		openLibrary( "CompatibleLibraryTheme.xml" ); //$NON-NLS-1$

		saveLibrary( );
		assertTrue( compareTextFile( "CompatibleLibraryTheme_golden.xml" ) ); //$NON-NLS-1$

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
		assertTrue( compareTextFile( "CompatibleLibraryThemeEmpty_golden.xml" ) ); //$NON-NLS-1$

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
		assertTrue( compareTextFile( "CompatibleLibraryThemeWithoutStyles_golden.xml" ) ); //$NON-NLS-1$

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

		Theme themeCloned = (Theme) theme1.copy( );
		ThemeHandle clonedTheme1 = themeCloned.handle( libraryHandle
				.getModule( ) );

		StyleHandle style1 = libraryHandle.getElementFactory( ).newStyle(
				"style1" ); //$NON-NLS-1$
		theme1.getStyles( ).add( style1 );

		assertEquals( 1, theme1.getStyles( ).getCount( ) );
		assertEquals( 0, clonedTheme1.getStyles( ).getCount( ) );

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

		assertEquals( 2, library.getNameSpace( Module.THEME_NAME_SPACE )
				.getCount( ) );
		assertEquals( 0, library.getNameSpace( Module.STYLE_NAME_SPACE )
				.getCount( ) );

		theme.getStyles( ).add( libFactory.newStyle( "style2" ) ); //$NON-NLS-1$
		assertEquals( 0, library.getNameSpace( Module.STYLE_NAME_SPACE )
				.getCount( ) );
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
	 * <li>1. includes one librarie with theme, assert getDisplayLabel()
	 * returns qualified name of the item. No exception expected.
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
	 * <li>For the design, directly included libraries have no themes.
	 * <code>getAllThemes()</code> return 0.
	 * <li>For the library, directly included libraries have 3 themes.
	 * <code>getAllThemes()</code> return 3.
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
		assertEquals( 3, libraryHandle.getVisibleThemes(
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

	class MyListener implements Listener
	{

		int counter = 0;

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.core.Listener#notify(org.eclipse.birt.report.model.core.DesignElement,
		 *      org.eclipse.birt.report.model.activity.NotificationEvent)
		 */
		public void elementChanged( DesignElementHandle focus,
				NotificationEvent ev )
		{
			if ( ev.getEventType( ) == NotificationEvent.STYLE_EVENT
					|| ev.getEventType( ) == NotificationEvent.THEME_EVENT )
				counter++;
		}

	}
}