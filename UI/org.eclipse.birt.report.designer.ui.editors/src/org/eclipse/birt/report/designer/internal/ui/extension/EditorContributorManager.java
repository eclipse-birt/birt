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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.util.Assert;

/**
 * The ExtensionPoinyManager is utility class to retrieve IExtendedElementUI
 * extensions by model extension ID, or full list.It caches the information to
 * avoid reading the extensions each time.
 */

public class EditorContributorManager implements IExtensionConstants
{

	public static class EditorContributor
	{

		public String targetEditorId;
		public List formPageList;

		public boolean merge( EditorContributor contributor )
		{
			Assert.isNotNull( targetEditorId );
			boolean merged = false;
			if ( targetEditorId.equals( contributor.targetEditorId ) )
			{
				for ( Iterator itor = contributor.formPageList.iterator( ); itor.hasNext( ); )
				{
					FormPageDef incomingPage = (FormPageDef) itor.next( );
					FormPageDef exsitPage = getPage( incomingPage.id );
					if ( exsitPage == null )
					{
						formPageList.add( incomingPage );
						merged = true;
					}
					else
					{
						int index = formPageList.indexOf( exsitPage );
						formPageList.remove( exsitPage );
						formPageList.add( index, incomingPage );
					}
				}
			}
			return merged;
		}

		public FormPageDef getPage( int index )
		{
			return (FormPageDef) formPageList.get( index );
		}

		public FormPageDef getPage( String id )
		{
			for ( Iterator itor = formPageList.iterator( ); itor.hasNext( ); )
			{
				FormPageDef page = (FormPageDef) itor.next( );
				if ( page.id.equals( id ) )
				{
					return page;
				}
			}
			return null;
		}
	}

	private HashMap editorContributorMap;

	private static EditorContributorManager instance = null;

	private EditorContributorManager( )
	{
	}

	public static EditorContributorManager getInstance( )
	{
		if ( instance == null )
		{
			synchronized ( ExtensionPointManager.class )
			{
				if ( instance == null )
				{
					instance = new EditorContributorManager( );
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
	public EditorContributor[] getEditorContributors( )
	{
		synchronized ( this )
		{
			if ( editorContributorMap == null )
			{
				createEditorContributorMap( );
				if ( editorContributorMap == null )
				{
					return null;
				}
			}
		}
		return (EditorContributor[]) editorContributorMap.values( )
				.toArray( new EditorContributor[editorContributorMap.size( )] );
	}

	public EditorContributor getEditorContributor( String targetEditorId )
	{
		Assert.isLegal( targetEditorId != null );
		synchronized ( this )
		{
			if ( editorContributorMap == null )
			{
				createEditorContributorMap( );
				if ( editorContributorMap == null )
				{
					return null;
				}
			}
		}

		return (EditorContributor) editorContributorMap.get( targetEditorId );
	}

	/**
	 * Gets the extended element point with the specified extension name.
	 * 
	 * @param extensionName
	 *            the extension name of the extended element
	 * 
	 * @return Returns the extended element point, or null if any problem exists
	 */
	public FormPageDef getFormPageDef( String targetEditorId, String pageId )
	{
		Assert.isLegal( targetEditorId != null );
		Assert.isLegal( pageId != null );
		int index = findFormPageIndex( targetEditorId, pageId );
		if ( index != -1 )
		{
			return getFormPageDef( targetEditorId, index );
		}
		return null;
	}

	public int findFormPageIndex( String targetEditorId, String pageId )
	{
		Assert.isLegal( targetEditorId != null );
		Assert.isLegal( pageId != null );
		EditorContributor editorContributor = getEditorContributor( targetEditorId );
		if ( editorContributor != null )
		{
			List formPageDefList = editorContributor.formPageList;
			if ( formPageDefList != null )
			{
				for ( int i = 0; i < formPageDefList.size( ); i++ )
				{
					FormPageDef formPageDef = (FormPageDef) formPageDefList.get( i );
					if ( formPageDef != null && formPageDef.equals( pageId ) )
					{
						return i;
					}
				}
			}
		}
		return -1;
	}

	/**
	 * Gets the extended element point with the specified extension name.
	 * 
	 * @param extensionName
	 *            the extension name of the extended element
	 * 
	 * @return Returns the extended element point, or null if any problem exists
	 */
	public FormPageDef getFormPageDef( String targetEditorId, int index )
	{
		Assert.isLegal( targetEditorId != null );
		Assert.isLegal( index >= 0 );
		EditorContributor editorContributor = getEditorContributor( targetEditorId );
		if ( editorContributor != null
				&& editorContributor.formPageList != null )
		{
			return (FormPageDef) editorContributor.formPageList.get( index );
		}
		return null;
	}

	private void createEditorContributorMap( )
	{
		synchronized ( this )
		{
			editorContributorMap = new HashMap( );
			for ( Iterator iter = getExtensionElements( EXTENSION_MULTIPAGE_EDITOR_CONTRIBUTOR ).iterator( ); iter.hasNext( ); )
			{
				IExtension extension = (IExtension) iter.next( );
				IConfigurationElement[] elements = extension.getConfigurationElements( );
				for ( int i = 0; i < elements.length; i++ )
				{
					EditorContributor editorContributor = createEditorContributor( elements[i] );
					if ( !editorContributorMap.containsKey( editorContributor.targetEditorId ) )
					{
						editorContributorMap.put( editorContributor.targetEditorId,
								editorContributor );
					}
					else
					{
						EditorContributor exsitContributor = (EditorContributor) editorContributorMap.get( editorContributor.targetEditorId );
						exsitContributor.merge( editorContributor );
					}
				}
			}

		}
	}

	private EditorContributor createEditorContributor(
			IConfigurationElement element )
	{
		EditorContributor editorContributor = new EditorContributor( );
		editorContributor.targetEditorId = loadStringAttribute( element,
				ATTRIBUTE_TARGET_EDITOR_ID );
		editorContributor.formPageList = createFormPageDefList( element );
		return editorContributor;
	}

	private List createFormPageDefList( IConfigurationElement element )
	{
		ArrayList formPageDefList = new ArrayList( );
		ArrayList keyList = new ArrayList( );
		IConfigurationElement[] elements = element.getChildren( );
		for ( int i = 0; i < elements.length; i++ )
		{
			FormPageDef formPageDef = new FormPageDef( elements[i] );
			if ( !keyList.contains( formPageDef.id ) )
			{
				formPageDefList.add( formPageDef );
				keyList.add( formPageDef.id );
			}
		}
		return formPageDefList;
	}

	private List getExtensionElements( String id )
	{
		IExtensionRegistry registry = Platform.getExtensionRegistry( );
		if ( registry == null )
		{// extension registry cannot be resolved
			return Collections.EMPTY_LIST;
		}
		IExtensionPoint extensionPoint = registry.getExtensionPoint( id );
		if ( extensionPoint == null )
		{// extension point cannot be resolved
			return Collections.EMPTY_LIST;
		}
		return Arrays.asList( extensionPoint.getExtensions( ) );
	}

	/**
	 * @param newPoint
	 *            the extension point instance
	 * @param element
	 *            the configuration element
	 * @param className
	 *            the name of the class attribute
	 */
	private Object loadClass( IConfigurationElement element,
			String attributeName )
	{
		Object clazz = null;
		try
		{

			clazz = element.createExecutableExtension( attributeName );
		}
		catch ( CoreException e )
		{
			ExceptionHandler.handle( e );
		}
		return clazz;
	}

	// private ImageDescriptor getImageDescriptor( IConfigurationElement element
	// )
	// {
	// return getImageDescriptor( element, ATTRIBUTE_ICON );
	// }
	//
	// private ImageDescriptor getImageDescriptor( IConfigurationElement
	// element,
	// String attributeName )
	// {
	// Assert.isLegal( element != null );
	// IExtension extension = element.getDeclaringExtension( );
	// String iconPath = element.getAttribute( attributeName );
	// if ( iconPath == null )
	// {
	// return null;
	// }
	// URL path = Platform.getBundle( extension.getNamespace( ) )
	// .getEntry( "/" ); //$NON-NLS-1$
	// try
	// {
	// return ImageDescriptor.createFromURL( new URL( path, iconPath ) );
	// }
	// catch ( MalformedURLException e )
	// {
	// }
	// return null;
	// }

	private String loadStringAttribute( IConfigurationElement element,
			String attributeName )
	{
		return element.getAttribute( attributeName );
	}

	private boolean loadBooleanAttribute( IConfigurationElement element,
			String attributeName )
	{
		String value = element.getAttribute( attributeName );
		if ( value != null )
		{
			return Boolean.valueOf( value ).booleanValue( );
		}
		return false;
	}

	// private ImageDescriptor loadIconAttribute( IConfigurationElement element,
	// String attributeName, String key )
	// {
	// ImageDescriptor imageDescriptor = getImageDescriptor( element );
	// if ( imageDescriptor != null && key != null )
	// {
	// ReportPlatformUIImages.declareImage( key, imageDescriptor );
	// }
	// return imageDescriptor;
	// }
}
