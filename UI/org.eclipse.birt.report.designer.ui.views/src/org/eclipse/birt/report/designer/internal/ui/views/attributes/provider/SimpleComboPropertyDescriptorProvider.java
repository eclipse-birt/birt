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
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.GroupElementHandle;
import org.eclipse.birt.report.model.api.GroupPropertyHandle;
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
import org.eclipse.birt.report.model.api.metadata.IPredefinedStyle;

public class SimpleComboPropertyDescriptorProvider extends
		PropertyDescriptorProvider implements ISimpleComboDescriptorProvider
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
			preStyleNames.add( ( (IPredefinedStyle) preStyles.get( i ) ).getName( ) );
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

	boolean isEditable = false;

	private String[] getModifiedStyles( )
	{
		isEditable = false;
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
				IncludedCssStyleSheetHandle inCss = reportDesign.findIncludedCssStyleSheetHandleByProperties( css.getFileName( ),
						css.getExternalCssURI( ),
						css.isUseExternalCss( ) );
				if ( css.isUseExternalCss( )
						|| css.getExternalCssURI( ) != null )
				{
					isEditable = true;
				}
				if ( inCss != null
						&& inCss.getExternalCssURI( ) != null
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
						e.printStackTrace( );
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

			ThemeHandle theme = reportDesign.getTheme( );
			if ( theme != null )
			{
				cssList.addAll( theme.getAllCssStyleSheets( ) );
				for ( int i = 0; i < cssList.size( ); i++ )
				{
					CssStyleSheetHandle css = cssList.get( i );
					IncludedCssStyleSheetHandle inCss = theme.findIncludedCssStyleSheetHandleByProperties( css.getFileName( ),
							css.getExternalCssURI( ),
							css.isUseExternalCss( ) );
					if ( css.isUseExternalCss( )
							|| css.getExternalCssURI( ) != null )
					{
						isEditable = true;
						break;
					}
				}
			}
		}
		else if ( module instanceof LibraryHandle )
		{
			LibraryHandle libary = (LibraryHandle) module;
			ThemeHandle theme = libary.getTheme( );
			if ( theme != null )
			{
				cssList.addAll( theme.getAllCssStyleSheets( ) );
				for ( int i = 0; i < cssList.size( ); i++ )
				{
					CssStyleSheetHandle css = cssList.get( i );
					IncludedCssStyleSheetHandle inCss = theme.findIncludedCssStyleSheetHandleByProperties( css.getFileName( ),
							css.getExternalCssURI( ),
							css.isUseExternalCss( ) );
					if ( css.isUseExternalCss( )
							|| css.getExternalCssURI( ) != null )
					{
						isEditable = true;
					}
					if ( inCss != null
							&& inCss.getExternalCssURI( ) != null
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
				if ( !isEditable )
					value = null;
			}
		}

		super.save( value );

	}

	@Override
	public Object load( )
	{
		Object obj = super.load( );
		if ( ReportItemHandle.STYLE_PROP.equals( getProperty( ) ) )
		{
			String[] styleNamesArray = getAllStyles( );
			String[] modifiedArray = getModifiedStyles( );
			int index = Arrays.asList( styleNamesArray ).indexOf( obj );
			if ( index >= 0 )
			{
				obj = modifiedArray[index];
			}
		}
		return obj;
	}

	public boolean isEditable( )
	{
		return isEditable;
	}

	public boolean isReadOnly( )
	{
		GroupPropertyHandle propertyHandle = null;
		if ( input instanceof GroupElementHandle )
		{
			propertyHandle = ( (GroupElementHandle) input ).getPropertyHandle( property );

		}
		else if ( input instanceof List )
		{
			propertyHandle = DEUtil.getGroupElementHandle( (List) input )
					.getPropertyHandle( property );
		}
		if ( propertyHandle != null )
		{
			return propertyHandle.isReadOnly( );
		}
		return false;
	}
}
