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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.core.runtime.CoreException;
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

	private Map pointsMap = null;

	private static ExtensionPointManager instance = null;

	private ExtensionPointManager( )
	{
	}

	public static ExtensionPointManager getInstance( )
	{
		if ( instance == null )
		{
			synchronized ( ExtensionPointManager.class )
			{
				if ( instance == null )
				{
					instance = new ExtensionPointManager( );
				}
			}
		}
		return instance;
	}

	/**
	 * Gets the list of all the extended element points.
	 * 
	 * @return Returns the list of all the extended element point
	 *         (ExtendedElementUIPoint).
	 */
	public List getExtendedElementPoints( )
	{
		return Arrays.asList( getPointsMap( ).values( ).toArray( ) );
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
		return (ExtendedElementUIPoint) getPointsMap( ).get( extensionName );
	}

	/**
	 * 
	 */
	private Map getPointsMap( )
	{
		if ( pointsMap == null )
		{
			synchronized ( this )
			{
				if ( pointsMap == null )
				{
					pointsMap = new HashMap( );

					for ( Iterator iter = getExtensionElements( ).iterator( ); iter.hasNext( ); )
					{
						IExtension extension = (IExtension) iter.next( );

						ExtendedElementUIPoint point = createExtendedElementPoint( extension );
						if ( point != null )
							pointsMap.put( point.getExtensionName( ), point );
					}
				}
			}
		}
		return pointsMap;
	}

	private List getExtensionElements( )
	{
		IExtensionRegistry registry = Platform.getExtensionRegistry( );
		if ( registry == null )
		{// extension registry cannot be resolved
			return Collections.EMPTY_LIST;
		}
		IExtensionPoint extensionPoint = registry.getExtensionPoint( IExtensionConstants.EXTENSIONPOINT_ID );
		if ( extensionPoint == null )
		{// extension point cannot be resolved
			return Collections.EMPTY_LIST;
		}
		return Arrays.asList( extensionPoint.getExtensions( ) );
	}

	private ExtendedElementUIPoint createExtendedElementPoint(
			IExtension extension )
	{
		IConfigurationElement[] elements = extension.getConfigurationElements( );
		if ( elements != null && elements.length > 0 )
		{
			return loadElements( elements );
		}
		return null;
	}

	private ExtendedElementUIPoint loadElements(
			IConfigurationElement[] elements )
	{

		ExtendedElementUIPoint newPoint = new ExtendedElementUIPoint( );

		if ( elements != null )
		{
			try
			{
				for ( int i = 0; i < elements.length; i++ )
				{
					loadAttributes( newPoint, elements[i] );
				}
			}
			catch ( Exception e )
			{
				ExceptionHandler.handle( e );
				return null;
			}

		}
		if ( DesignEngine.getMetaDataDictionary( )
				.getExtension( newPoint.getExtensionName( ) ) == null )
		{
			// Non-defined element. Ignore
			return null;
		}
		if ( Policy.TRACING_EXTENSION_LOAD )
		{
			System.out.println( "GUI Extesion Manager >> Loads " //$NON-NLS-1$
					+ newPoint.getExtensionName( ) );
		}
		return newPoint;
	}

	private void loadAttributes( ExtendedElementUIPoint newPoint,
			IConfigurationElement element ) throws CoreException
	{
		String elementName = element.getName( );
		if ( IExtensionConstants.MODEL.equals( elementName ) )
		{
			String value = element.getAttribute( IExtensionConstants.EXTENSION_NAME );
			newPoint.setExtensionName( value );
		}
		else if ( IExtensionConstants.REPORT_ITEM_FIGURE_UI.equals( elementName )
				|| IExtensionConstants.REPORT_ITEM_IMAGE_UI.equals( elementName )
				|| IExtensionConstants.REPORT_ITEM_LABEL_UI.equals( elementName ) )
		{
			String className = element.getAttribute( IExtensionConstants.CLASS );
			if ( className != null )
			{
				Object ui = element.createExecutableExtension( IExtensionConstants.CLASS );
				newPoint.setReportItemUI( new ExtendedUIAdapter( ui ) );
			}
		}
		else if ( IExtensionConstants.BUILDER.equals( elementName ) )
		{
			loadClass( newPoint,
					element,
					IExtensionConstants.CLASS,
					IExtensionConstants.BUILDER );
		}
		else if ( IExtensionConstants.PROPERTYEDIT.equals( elementName ) )
		{
			loadClass( newPoint,
					element,
					IExtensionConstants.CLASS,
					IExtensionConstants.PROPERTYEDIT );
		}

		else if ( IExtensionConstants.PALETTE.equals( elementName ) )
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

	/**
	 * @param newPoint
	 *            the extension point instance
	 * @param element
	 *            the configuration element
	 * @param className
	 *            the name of the class attribute
	 */
	private void loadClass( ExtendedElementUIPoint newPoint,
			IConfigurationElement element, String className,
			String attributeName )
	{
		String value = element.getAttribute( className );
		if ( value != null )
		{
			try
			{
				newPoint.setClass( attributeName,
						element.createExecutableExtension( className ) );
			}
			catch ( CoreException e )
			{
			}
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