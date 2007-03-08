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

package org.eclipse.birt.report.designer.internal.ui.extension.experimental;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.ui.extensions.IExtensionConstants;
import org.eclipse.core.expressions.EvaluationContext;
import org.eclipse.core.expressions.EvaluationResult;
import org.eclipse.core.expressions.Expression;
import org.eclipse.core.expressions.ExpressionConverter;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.Assert;

/**
 * 
 */

public class EditpartExtensionManager
{

	private static Map extensionMap = new HashMap( );
	private static List palettes = new ArrayList( );

	static
	{
		IExtensionRegistry registry = Platform.getExtensionRegistry( );
		IExtensionPoint extensionPoint = registry.getExtensionPoint( "org.eclipse.birt.report.designer.ui.reportItemEditpart" );
		if ( extensionPoint != null )
		{
			IConfigurationElement[] elements = extensionPoint.getConfigurationElements( );
			for ( int i = 0; i < elements.length; i++ )
			{
				IConfigurationElement[] enablements = elements[i].getChildren( "enablement" );
				if ( enablements.length == 0 )
					continue;//log message
				try
				{
					extensionMap.put( ExpressionConverter.getDefault( )
							.perform( enablements[0] ), elements[i] );
				}
				catch ( CoreException e )
				{
					e.printStackTrace( );
				}
				IConfigurationElement[] paletteEntries = elements[i].getChildren( "paletteEntry" );
				if ( paletteEntries.length == 1 )
				{
					PaletteEntryExtension entry = new PaletteEntryExtension( );
					entry.setItemName( paletteEntries[0].getAttribute( "itemName" ) );
					//TODO label can't be empty
					entry.setLabel( paletteEntries[0].getAttribute( "label" ) );
					entry.setDescription( paletteEntries[0].getAttribute( "description" ) );
					entry.setIcon( getImageDescriptor( paletteEntries[0],
							paletteEntries[0].getAttribute( "icon" ) ) );
					entry.setIconLarge( getImageDescriptor( paletteEntries[0],
							paletteEntries[0].getAttribute( "iconLarge" ) ) );
					//TODO category can't be empty
					entry.setCategory( paletteEntries[0].getAttribute( "category" ) );
					//TODO command can't be empty
					entry.setCommand( paletteEntries[0].getAttribute( "createCommand" ) );
					palettes.add( entry );
				}
			}
		}
	}

	private static ImageDescriptor getImageDescriptor(
			IConfigurationElement extension, String iconPath )
	{
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

	public static EditPart createEditPart( EditPart context, Object model )
	{
		EvaluationContext econtext = new EvaluationContext( null, model );
		for ( Iterator iterator = extensionMap.keySet( ).iterator( ); iterator.hasNext( ); )
		{
			try
			{
				Expression expression = (Expression) iterator.next( );
				if ( expression.evaluate( econtext ) == EvaluationResult.TRUE )
				{
					EditPart editPart = (EditPart) ( (IConfigurationElement) extensionMap.get( expression ) ).createExecutableExtension( "type" );
					editPart.setModel( model );
					return editPart;
				}
			}
			catch ( CoreException e )
			{
				e.printStackTrace( );
			}
		}
		return null;
	}

	public static PaletteEntryExtension[] getPaletteEntries( )
	{
		return (PaletteEntryExtension[]) palettes.toArray( new PaletteEntryExtension[palettes.size( )] );
	}
}
