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

package org.eclipse.birt.report.designer.internal.ui.extension;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.extensions.IReportItemUI;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.Assert;

/**
 * The ExtensionPoinyManager is utility class to retrieve IExtendedElementUI
 * extensions by model extension ID, or full list.It caches the information to
 * avoid reading the extensions each time.
 */

public class ExtensionPointManager
{

	private Map pointsMap = new HashMap( );

	private static ExtensionPointManager instance = null;

	private ExtensionPointManager( )
	{
	}

	public static ExtensionPointManager getInstance( )
	{
		if ( instance == null )
		{
			instance = new ExtensionPointManager( );
		}
		return instance;
	}

	/**
	 * Gets the list of all the extended element point.
	 * 
	 * @return Returns the list of all the extended element point.
	 */
	public List getExtendedElementPoints( )
	{
		List list = new ArrayList( );
		for ( Iterator itor = getExtensionElements( ).iterator( ); itor.hasNext( ); )
		{
			IConfigurationElement element = (IConfigurationElement) itor.next( );
			String extensionName = getExtensionName( element );
			ExtendedElementUIPoint point = null;
			if ( extensionName != null )
			{
				if ( pointsMap.containsKey( extensionName ) )
				{
					point = (ExtendedElementUIPoint) pointsMap.get( extensionName );
				}
				else
				{
					point = loadElement( element );
					if ( point != null )
					{
						pointsMap.put( extensionName, point );
					}
				}
			}
			if ( point != null )
			{
				list.add( point );
			}
		}
		return list;
	}

	/**
	 * Gets the extended element point with the specified extension name.
	 * 
	 * @param extensionName
	 *            the extension name of the extended element
	 * 
	 * @return Returns the extended element point, or null if any problem exists
	 */
	public ExtendedElementUIPoint getExtendedElementPoint( String extensionName )
	{
		Assert.isLegal( extensionName != null );
		if ( pointsMap.containsKey( extensionName ) )
		{
			return (ExtendedElementUIPoint) pointsMap.get( extensionName );
		}
		ExtendedElementUIPoint newPoint = createExtendedElementPoint( extensionName );
		if ( newPoint != null )
		{
			pointsMap.put( extensionName, newPoint );
		}
		return newPoint;
	}

	private String getExtensionName( IConfigurationElement element )
	{
		return element.getAttribute( IExtensionConstants.EXTENSION_NAME );
	}

	private List getExtensionElements( )
	{
		IExtensionRegistry registry = Platform.getExtensionRegistry( );
		if ( registry == null )
		{//extension registry cannot be resolved
			return Collections.EMPTY_LIST;
		}
		IExtensionPoint extensionPoint = registry.getExtensionPoint( IExtensionConstants.EXTENSIONPOINT_ID );
		if ( extensionPoint == null )
		{//extension point cannot be resolved
			return Collections.EMPTY_LIST;
		}
		return Arrays.asList( extensionPoint.getConfigurationElements( ) );
	}

	private ExtendedElementUIPoint createExtendedElementPoint(
			String extensionName )
	{
		Assert.isLegal( extensionName != null );
		List list = getExtensionElements( );
		for ( Iterator itor = list.iterator( ); itor.hasNext( ); )
		{
			IConfigurationElement element = (IConfigurationElement) itor.next( );
			String name = element.getAttribute( IExtensionConstants.EXTENSION_NAME );
			if ( extensionName.equals( name ) )
			{
				return loadElement( element );
			}
		}
		return null;
	}

	private ExtendedElementUIPoint loadElement( IConfigurationElement element )
	{
		String extensionName = getExtensionName( element );
		if ( MetaDataDictionary.getInstance( ).getExtension( extensionName ) == null )
		{//Non-defined element
			return null;
		}
		ExtendedElementUIPoint newPoint = new ExtendedElementUIPoint( extensionName );
		String className = element.getAttribute( IExtensionConstants.CLASS );
		if ( className == null )
		{//class is required
			return null;
		}
		try
		{
			Object ui = element.createExecutableExtension( IExtensionConstants.CLASS );
			if ( ui instanceof IReportItemUI )
			{
				newPoint.setReportItemUI( (IReportItemUI) ui );
			}
			else
			{//wrong type
				return null;
			}

		}
		catch ( Exception e )
		{//class cannot be loaded
			return null;
		}

		IConfigurationElement[] elements = element.getChildren( );
		if ( elements != null )
		{
			for ( int i = 0; i < elements.length; i++ )
			{
				loadAttributes( newPoint, elements[i] );
			}
		}
		return newPoint;
	}

	private void loadAttributes( ExtendedElementUIPoint newPoint,
			IConfigurationElement element )
	{
		String elementName = element.getName( );
		if ( IExtensionConstants.PALETTE.equals( elementName ) )
		{
			loadIconAttribute( newPoint,
					element,
					IExtensionConstants.PALETTE_ICON,
					false );
			loadStringAttribute( newPoint,
					element,
					IExtensionConstants.PALETTE_CATEGORY );
			loadStringAttribute( newPoint,
					element,
					IExtensionConstants.PALETTE_CATEGORY_DISPLAYNAME );
		}
		else if ( IExtensionConstants.EDITOR.equals( elementName ) )
		{
			loadBooleanAttribute( newPoint,
					element,
					IExtensionConstants.EDITOR_SHOW_IN_DESIGNER );
			loadBooleanAttribute( newPoint,
					element,
					IExtensionConstants.EDITOR_SHOW_IN_MASTERPAGE );
			loadBooleanAttribute( newPoint,
					element,
					IExtensionConstants.EDITOR_CAN_RESIZE );
		}
		else if ( IExtensionConstants.OUTLINE.equals( elementName ) )
		{
			loadIconAttribute( newPoint,
					element,
					IExtensionConstants.OUTLINE_ICON,
					true );
		}
	}

	private ImageDescriptor getImageDescriptor( IConfigurationElement element )
	{
		Assert.isLegal( element != null );
		IExtension extension = element.getDeclaringExtension( );
		String iconPath = element.getAttribute( IExtensionConstants.ICON );
		if ( iconPath == null )
		{
			return null;
		}
		URL path = Platform.getBundle( extension.getNamespace( ) )
				.getEntry( "/" ); //$NON-NLS-1$
		try
		{
			return ImageDescriptor.createFromURL( new URL( path, iconPath ) );
		}
		catch ( MalformedURLException e )
		{
		}
		return null;
	}

	private void loadStringAttribute( ExtendedElementUIPoint newPoint,
			IConfigurationElement element, String attributeName )
	{
		String value = element.getAttribute( attributeName );
		if ( value != null )
		{
			newPoint.setAttribute( attributeName, value );
		}

	}

	private void loadBooleanAttribute( ExtendedElementUIPoint newPoint,
			IConfigurationElement element, String attributeName )
	{
		String value = element.getAttribute( attributeName );
		if ( value != null )
		{
			newPoint.setAttribute( attributeName, Boolean.valueOf( value ) );
		}
	}

	private void loadIconAttribute( ExtendedElementUIPoint newPoint,
			IConfigurationElement element, String attributeName, boolean shared )
	{
		ImageDescriptor imageDescriptor = getImageDescriptor( element );
		if ( imageDescriptor != null )
		{
			if ( shared )
			{
				String symbolName = ReportPlatformUIImages.getIconSymbolName( newPoint.getExtensionName( ),
						attributeName );
				ReportPlatformUIImages.declareImage( symbolName,
						imageDescriptor );
			}
			newPoint.setAttribute( attributeName, imageDescriptor );
		}
	}

}