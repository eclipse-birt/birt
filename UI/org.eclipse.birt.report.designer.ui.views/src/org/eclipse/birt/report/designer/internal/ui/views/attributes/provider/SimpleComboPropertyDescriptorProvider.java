/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.IncludedCssStyleSheetHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.model.api.css.StyleSheetException;
import org.eclipse.birt.report.model.metadata.PredefinedStyle;

public class SimpleComboPropertyDescriptorProvider extends
		PropertyDescriptorProvider
{

	public SimpleComboPropertyDescriptorProvider( String property,
			String element )
	{
		super( property, element );
	}

	public String[] getItems( )
	{
		String[] items = null;

		if ( ReportItemHandle.DATA_SET_PROP.equals( getProperty( ) ) )
			items = ChoiceSetFactory.getDataSets( );
		else if ( StyleHandle.MASTER_PAGE_PROP.equals( getProperty( ) ) )
			items = ChoiceSetFactory.getMasterPages( );
		else if ( ReportItemHandle.STYLE_PROP.equals( getProperty( ) ) )
		{
			items = getModifiedStyles( );
		}
		else if ( ReportDesignHandle.THEME_PROP.equals( getProperty( ) ) )
			items = ChoiceSetFactory.getThemes( );
		return items;
	}

	private String[] getAllStyles( )
	{
		String items[] = ChoiceSetFactory.getStyles( );
		List preStyles = DesignEngine.getMetaDataDictionary( )
				.getPredefinedStyles( );
		List<String> preStyleNames = new ArrayList<String>( );

		for ( int i = 0; i < preStyles.size( ); i++ )
		{
			preStyleNames.add( ( (PredefinedStyle) preStyles.get( i ) ).getName( ) );
		}

		List<String> sytleNames = new ArrayList<String>( );
		for ( int i = 0; i < items.length; i++ )
		{
			if ( preStyleNames.indexOf( items[i] ) == -1 )
			{
				sytleNames.add( items[i] );
			}
		}
		String[] styleNamesArray = sytleNames.toArray( new String[]{} );
		return styleNamesArray;
	}

	private String[] getModifiedStyles( )
	{
		String[] styleNamesArray = getAllStyles( );
		List<String> sytleNames = new ArrayList<String>( );
		sytleNames.addAll( Arrays.asList( styleNamesArray ) );
		ModuleHandle module = SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( );
		List<CssStyleSheetHandle> cssList = new ArrayList<CssStyleSheetHandle>( );
		if ( module instanceof ReportDesignHandle )
		{
			ReportDesignHandle reportDesign = (ReportDesignHandle) module;
			cssList.addAll( reportDesign.getAllCssStyleSheets( ) );
			for ( int i = 0; i < cssList.size( ); i++ )
			{
				CssStyleSheetHandle css = cssList.get( i );
				IncludedCssStyleSheetHandle inCss = reportDesign.findIncludedCssStyleSheetHandleByFileName( css.getFileName( ) );
				if ( inCss.getExternalCssURI( ) != null
						&& inCss.getExternalCssURI( ).length( ) > 0 )
				{
					String fileName = inCss.getExternalCssURI( );
					CssStyleSheetHandle uriCss = null;
					try
					{
						uriCss = SessionHandleAdapter.getInstance( )
								.getReportDesignHandle( )
								.openCssStyleSheet( fileName );
						if ( uriCss == null )
						{
							continue;
						}
					}
					catch ( StyleSheetException e )
					{
						// TODO Auto-generated catch block
						continue;
					}

					for ( Iterator iter = css.getStyleIterator( ); iter.hasNext( ); )
					{
						SharedStyleHandle styleHandle = (SharedStyleHandle) iter.next( );
						int index = sytleNames.indexOf( styleHandle.getName( ) );
						if ( index >= 0
								&& uriCss.findStyle( styleHandle.getName( ) ) != null )
						{
							styleNamesArray[index] = styleHandle.getName( )
									+ " "
									+ Messages.getString( "CssStyleSheetNodeProvider.Tooltip.URI" );
						}
					}
				}
			}
		}
		else if ( module instanceof LibraryHandle )
		{
			LibraryHandle libary = (LibraryHandle) module;
			ThemeHandle theme = libary.getTheme( );
			cssList.addAll( theme.getAllCssStyleSheets( ) );
			for ( int i = 0; i < cssList.size( ); i++ )
			{
				CssStyleSheetHandle css = cssList.get( i );
				IncludedCssStyleSheetHandle inCss = theme.findIncludedCssStyleSheetHandleByName( css.getFileName( ) );
				if ( inCss.getExternalCssURI( ) != null
						&& inCss.getExternalCssURI( ).length( ) > 0 )
				{
					for ( Iterator iter = css.getStyleIterator( ); iter.hasNext( ); )
					{
						SharedStyleHandle styleHandle = (SharedStyleHandle) iter.next( );
						int index = sytleNames.indexOf( styleHandle.getName( ) );
						String tooltipURI = Messages.getString( "CssStyleSheetNodeProvider.Tooltip.URI" );
						if ( index >= 0 )
						{
							styleNamesArray[index] = styleHandle.getName( )
									+ " "
									+ tooltipURI;
						}
					}
				}
			}
		}

		return styleNamesArray;

	}

	public boolean isSpecialProperty( )
	{
		return ReportItemHandle.STYLE_PROP.equals( getProperty( ) )
				|| ReportDesignHandle.THEME_PROP.equals( getProperty( ) )
				|| StyleHandle.MASTER_PAGE_PROP.equals( getProperty( ) );

	}

	public void save( Object value ) throws SemanticException
	{
		if ( ReportItemHandle.STYLE_PROP.equals( getProperty( ) ) )
		{
			String[] styleNamesArray = getAllStyles( );
			String[] modifiedArray = getModifiedStyles( );
			int index = Arrays.asList( modifiedArray ).indexOf( value );
			if ( index >= 0 )
			{
				value = styleNamesArray[index];
			}
			else
			{
				value = null;
			}
		}

		super.save( value );

	}

}
