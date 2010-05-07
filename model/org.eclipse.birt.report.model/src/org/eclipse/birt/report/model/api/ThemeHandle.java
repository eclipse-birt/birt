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
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.model.api.elements.structures.IncludedCssStyleSheet;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.css.CssStyleSheet;
import org.eclipse.birt.report.model.css.CssStyleSheetHandleAdapter;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.elements.interfaces.IThemeModel;

/**
 * Represents a theme in the library. Each theme contains some number of styles.
 * 
 * 
 * @see org.eclipse.birt.report.model.elements.Theme
 */

public class ThemeHandle extends AbstractThemeHandle implements IThemeModel
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
	 * Gets all css styles sheet
	 * 
	 * @return each item is <code>CssStyleSheetHandle</code>
	 */

	public List getAllCssStyleSheets( )
	{
		Theme theme = (Theme) getElement( );
		List allStyles = new ArrayList( );
		List csses = theme.getCsses( );
		for ( int i = 0; csses != null && i < csses.size( ); ++i )
		{
			CssStyleSheet sheet = (CssStyleSheet) csses.get( i );
			allStyles.add( sheet.handle( getModule( ) ) );
		}
		return allStyles;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.AbstractThemeHandle#addCss(org.eclipse
	 * .birt.report.model.api.css.CssStyleSheetHandle)
	 */

	public void addCss( CssStyleSheetHandle sheetHandle )
			throws SemanticException
	{
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(
				module, getElement( ) );
		adapter.addCss( sheetHandle );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.AbstractThemeHandle#addCss(org.eclipse
	 * .birt.report.model.api.elements.structures.IncludedCssStyleSheet)
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.AbstractThemeHandle#addCss(java.lang
	 * .String)
	 */
	public void addCss( String fileName ) throws SemanticException
	{
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(
				module, getElement( ) );
		adapter.addCss( fileName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.AbstractThemeHandle#dropCss(org.eclipse
	 * .birt.report.model.api.css.CssStyleSheetHandle)
	 */

	public void dropCss( CssStyleSheetHandle sheetHandle )
			throws SemanticException
	{
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(
				module, getElement( ) );
		adapter.dropCss( sheetHandle );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.AbstractThemeHandle#canDropCssStyleSheet
	 * (org.eclipse.birt.report.model.api.css.CssStyleSheetHandle)
	 */
	public boolean canDropCssStyleSheet( CssStyleSheetHandle sheetHandle )
	{
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(
				module, getElement( ) );
		return adapter.canDropCssStyleSheet( sheetHandle );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.AbstractThemeHandle#canAddCssStyleSheet
	 * (org.eclipse.birt.report.model.api.css.CssStyleSheetHandle)
	 */

	public boolean canAddCssStyleSheet( CssStyleSheetHandle sheetHandle )
	{
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(
				module, getElement( ) );
		return adapter.canAddCssStyleSheet( sheetHandle );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.AbstractThemeHandle#canAddCssStyleSheet
	 * (java.lang.String)
	 */
	public boolean canAddCssStyleSheet( String fileName )
	{
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(
				module, getElement( ) );
		return adapter.canAddCssStyleSheet( fileName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.AbstractThemeHandle#reloadCss(org.eclipse
	 * .birt.report.model.api.css.CssStyleSheetHandle)
	 */

	public void reloadCss( CssStyleSheetHandle sheetHandle )
			throws SemanticException
	{
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(
				module, getElement( ) );
		adapter.reloadCss( sheetHandle );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.AbstractThemeHandle#
	 * findCssStyleSheetHandleByName(java.lang.String)
	 */
	public CssStyleSheetHandle findCssStyleSheetHandleByName( String fileName )
	{
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(
				module, getElement( ) );
		return adapter.findCssStyleSheetHandleByFileName( fileName );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.AbstractThemeHandle#
	 * findIncludedCssStyleSheetHandleByName(java.lang.String)
	 */
	public IncludedCssStyleSheetHandle findIncludedCssStyleSheetHandleByName(
			String fileName )
	{
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(
				module, getElement( ) );
		return adapter.findIncludedCssStyleSheetHandleByFileName( fileName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.AbstractThemeHandle#renameCss(org.eclipse
	 * .birt.report.model.api.IncludedCssStyleSheetHandle, java.lang.String)
	 */
	public void renameCss( IncludedCssStyleSheetHandle handle,
			String newFileName ) throws SemanticException
	{

		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(
				module, getElement( ) );
		adapter.renameCss( handle, newFileName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.AbstractThemeHandle#canRenameCss(org
	 * .eclipse.birt.report.model.api.IncludedCssStyleSheetHandle,
	 * java.lang.String)
	 */
	public boolean canRenameCss( IncludedCssStyleSheetHandle handle,
			String newFileName ) throws SemanticException
	{
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(
				module, getElement( ) );
		return adapter.canRenameCss( handle, newFileName );
	}
}
