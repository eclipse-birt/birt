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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.model.api.elements.structures.IncludedCssStyleSheet;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.css.CssStyleSheet;
import org.eclipse.birt.report.model.css.CssStyleSheetHandleAdapter;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.elements.interfaces.IThemeModel;

/**
 * Represents a theme in the library. Each theme contains some number of styles.
 * 
 * 
 * @see org.eclipse.birt.report.model.elements.Theme
 */

public class ThemeHandle extends ReportElementHandle implements IThemeModel
{

	/**
	 * Constructs the handle for a theme with the given design and element. The
	 * application generally does not create handles directly. Instead, it uses
	 * one of the navigation methods available on other element handles.
	 * 
	 * @param module
	 *            the module
	 * @param element
	 *            the model representation of the element
	 */

	public ThemeHandle( Module module, DesignElement element )
	{
		super( module, element );
	}

	/**
	 * Returns the styles slot of row. Through SlotHandle, each style can be
	 * obtained.
	 * 
	 * @return the handle to the style slot
	 * 
	 * @see SlotHandle
	 */

	public SlotHandle getStyles( )
	{
		return getSlot( IThemeModel.STYLES_SLOT );
	}

	/**
	 * Gets all styles in theme,include css file.
	 * 
	 * @return all styles.each item is <code>StyleHandle</code>
	 */

	public List<DesignElementHandle> getAllStyles( )
	{
		Theme theme = (Theme) getElement( );
		List<DesignElementHandle> styles = new ArrayList<DesignElementHandle>( );
		List<StyleElement> styleList = theme.getAllStyles( );
		Iterator<StyleElement> iter = styleList.iterator( );
		while ( iter.hasNext( ) )
		{
			StyleElement style = iter.next( );
			styles.add( style.getHandle( module ) );
		}
		return styles;
	}

	/**
	 * Gets all css styles sheet
	 * 
	 * @return each item is <code>CssStyleSheetHandle</code>
	 */

	public List<CssStyleSheetHandle> getAllCssStyleSheets( )
	{
		Theme theme = (Theme) getElement( );
		List<CssStyleSheetHandle> allStyles = new ArrayList<CssStyleSheetHandle>( );
		List<CssStyleSheet> csses = theme.getCsses( );
		for ( int i = 0; csses != null && i < csses.size( ); ++i )
		{
			CssStyleSheet sheet = csses.get( i );
			allStyles.add( sheet.handle( getModule( ) ) );
		}
		return allStyles;
	}

	/**
	 * Returns the style with the given name.
	 * 
	 * @param name
	 *            the style name
	 * @return the corresponding style
	 */

	public StyleHandle findStyle( String name )
	{
		Theme theme = (Theme) getElement( );
		StyleElement style = theme.findStyle( name );
		if ( style != null )
			return (StyleHandle) style.getHandle( module );

		return null;
	}

	/**
	 * Makes the unique style name in the given theme. The return name is based
	 * on <code>name</code>.
	 * 
	 * @param name
	 *            the style name
	 * @return the new unique style name
	 */

	String makeUniqueStyleName( String name )
	{
		assert this != null;

		SlotHandle styles = getStyles( );
		Set<String> set = new HashSet<String>( );
		for ( int i = 0; i < styles.getCount( ); i++ )
		{
			StyleHandle style = (StyleHandle) styles.get( i );
			set.add( style.getName( ) );
		}

		// Should different from css file name

		PropertyHandle propHandle = getPropertyHandle( IThemeModel.CSSES_PROP );
		Iterator iterator = propHandle.iterator( );
		while ( iterator.hasNext( ) )
		{
			IncludedCssStyleSheetHandle handle = (IncludedCssStyleSheetHandle) iterator
					.next( );
			set.add( handle.getFileName( ) );
		}

		// Add a numeric suffix that makes the name unique.

		int index = 0;
		String baseName = name;
		while ( set.contains( name ) )
		{
			name = baseName + ++index;
		}
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.DesignElementHandle#getDisplayLabel
	 * (int)
	 */

	public String getDisplayLabel( int level )
	{

		String displayLabel = super.getDisplayLabel( level );

		Module rootModule = getModule( );
		if ( rootModule instanceof Library )
			displayLabel = StringUtil.buildQualifiedReference(
					( (Library) rootModule ).getNamespace( ), displayLabel );

		return displayLabel;

	}

	/**
	 * Returns the iterator over all included css style sheets. Each one is the
	 * instance of <code>IncludedCssStyleSheetHandle</code>
	 * 
	 * @return the iterator over all included css style sheets.
	 */

	public Iterator includeCssesIterator( )
	{
		PropertyHandle propHandle = getPropertyHandle( IThemeModel.CSSES_PROP );
		assert propHandle != null;
		return propHandle.iterator( );
	}

	/**
	 * Includes one css with the given css file name. The new css will be
	 * appended to the css list.
	 * 
	 * @param sheetHandle
	 *            css style sheet handle
	 * @throws SemanticException
	 *             if error is encountered when handling
	 *             <code>CssStyleSheet</code> structure list.
	 */

	public void addCss( CssStyleSheetHandle sheetHandle )
			throws SemanticException
	{
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(
				module, getElement( ) );
		adapter.addCss( sheetHandle );
	}

	/**
	 * Includes one CSS structure with the given IncludedCssStyleSheet. The new
	 * css will be appended to the CSS list.
	 * 
	 * @param cssStruct
	 *            the CSS structure
	 * @throws SemanticException
	 *             if error is encountered when handling
	 *             <code>CssStyleSheet</code> structure list.
	 */

	public void addCss( IncludedCssStyleSheet cssStruct )
			throws SemanticException
	{
		if ( cssStruct == null )
			return;

		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(
				module, getElement( ) );
		adapter.addCss( cssStruct );
	}

	/**
	 * Includes one css with the given css file name. The new css will be
	 * appended to the css list.
	 * 
	 * @param fileName
	 *            css file name
	 * @throws SemanticException
	 *             if error is encountered when handling
	 *             <code>CssStyleSheet</code> structure list.
	 */

	public void addCss( String fileName ) throws SemanticException
	{
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(
				module, getElement( ) );
		adapter.addCss( fileName );
	}

	/**
	 * Drops the given css style sheet of this design file.
	 * 
	 * @param sheetHandle
	 *            the css to drop
	 * @throws SemanticException
	 *             if error is encountered when handling
	 *             <code>CssStyleSheet</code> structure list. Or it maybe
	 *             because that the given css is not found in the design. Or
	 *             that the css has descedents in the current module
	 */

	public void dropCss( CssStyleSheetHandle sheetHandle )
			throws SemanticException
	{
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(
				module, getElement( ) );
		adapter.dropCss( sheetHandle );
	}

	/**
	 * Check style sheet can be droped or not.
	 * 
	 * @param sheetHandle
	 * @return <code>true</code> can be dropped.else return <code>false</code>
	 */

	public boolean canDropCssStyleSheet( CssStyleSheetHandle sheetHandle )
	{
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(
				module, getElement( ) );
		return adapter.canDropCssStyleSheet( sheetHandle );
	}

	/**
	 * Check style sheet can be added or not.
	 * 
	 * @param sheetHandle
	 * @return <code>true</code> can be added.else return <code>false</code>
	 */

	public boolean canAddCssStyleSheet( CssStyleSheetHandle sheetHandle )
	{
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(
				module, getElement( ) );
		return adapter.canAddCssStyleSheet( sheetHandle );
	}

	/**
	 * Check style sheet can be added or not.
	 * 
	 * @param fileName
	 * @return <code>true</code> can be added.else return <code>false</code>
	 */

	public boolean canAddCssStyleSheet( String fileName )
	{
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(
				module, getElement( ) );
		return adapter.canAddCssStyleSheet( fileName );
	}

	/**
	 * Reloads the css with the given css file path. If the css already is
	 * included directly, reload it. If the css is not included, exception will
	 * be thrown.
	 * 
	 * @param sheetHandle
	 *            css style sheet handle
	 * @throws SemanticException
	 *             if error is encountered when handling
	 *             <code>CssStyleSheet</code> structure list. Or it maybe
	 *             because that the given css is not found in the design. Or
	 *             that the css has descedents in the current module
	 */

	public void reloadCss( CssStyleSheetHandle sheetHandle )
			throws SemanticException
	{
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(
				module, getElement( ) );
		adapter.reloadCss( sheetHandle );
	}

	/**
	 * Gets <code>CssStyleSheetHandle</code> by file name.
	 * 
	 * @param fileName
	 *            the file name.
	 * 
	 * @return the cssStyleSheet handle.
	 */
	public CssStyleSheetHandle findCssStyleSheetHandleByName( String fileName )
	{
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(
				module, getElement( ) );
		return adapter.findCssStyleSheetHandleByFileName( fileName );

	}

	/**
	 * Gets <code>IncludedCssStyleSheetHandle</code> by file name.
	 * 
	 * @param fileName
	 *            the file name
	 * @return the includedCssStyleSheet handle.
	 */
	public IncludedCssStyleSheetHandle findIncludedCssStyleSheetHandleByName(
			String fileName )
	{
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(
				module, getElement( ) );
		return adapter.findIncludedCssStyleSheetHandleByFileName( fileName );
	}

	/**
	 * Renames both <code>IncludedCssStyleSheet</code> and
	 * <code>CSSStyleSheet<code> to newFileName.
	 * 
	 * @param handle
	 *            the includedCssStyleSheetHandle
	 * @param newFileName
	 *            the new file name
	 */
	public void renameCss( IncludedCssStyleSheetHandle handle,
			String newFileName ) throws SemanticException
	{

		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(
				module, getElement( ) );
		adapter.renameCss( handle, newFileName );
	}

	/**
	 * Checks included style sheet can be renamed or not.
	 * 
	 * @param handle
	 *            the included css style sheet handle.
	 * @param newFileName
	 *            the new file name.
	 * @return <code>true</code> can be renamed.else return <code>false</code>
	 */
	public boolean canRenameCss( IncludedCssStyleSheetHandle handle,
			String newFileName ) throws SemanticException
	{
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(
				module, getElement( ) );
		return adapter.canRenameCss( handle, newFileName );
	}
}
