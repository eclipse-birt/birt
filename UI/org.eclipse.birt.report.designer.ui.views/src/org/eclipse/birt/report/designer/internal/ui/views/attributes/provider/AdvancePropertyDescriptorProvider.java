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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.views.property.GroupPropertyHandleWrapper;
import org.eclipse.birt.report.designer.core.model.views.property.PropertySheetRootElement;
import org.eclipse.birt.report.designer.internal.ui.views.memento.Memento;
import org.eclipse.birt.report.designer.internal.ui.views.memento.MementoElement;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.GroupElementHandle;
import org.eclipse.birt.report.model.api.GroupPropertyHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

/**
 * 
 * 
 */
public class AdvancePropertyDescriptorProvider implements IDescriptorProvider
{

	public String getDisplayName( )
	{
		// TODO Auto-generated method stub
		return "Advance";
	}

	public Object load( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void save( Object value ) throws SemanticException
	{
		// TODO Auto-generated method stub

	}

	private Object input;

	public void setInput( Object input )
	{
		this.input = input;

	}

	public boolean isEnable( )
	{
		if ( input == null
				|| !DEUtil.getGroupElementHandle( DEUtil.getInputElements( input ) )
						.isSameType( ) )
		{
			return false;
		}
		return true;
	}

	public String getTitleDisplayName( )
	{
		String displayName = null;
		DesignElementHandle element = (DesignElementHandle) DEUtil.getInputFirstElement( input );
		if ( element != null )
		{
			displayName = getElementType( );
		}

		if ( !isEnable( ) || displayName == null || "".equals( displayName ) )//$NON-NLS-1$ 
		{
			displayName = Messages.getString( "ReportPropertySheetPage.Root.Default.Title" );
		}
		return displayName;
	}

	AdvancePropertyContentProvider contentProvider = new AdvancePropertyContentProvider( );
	AdvancePropertyLabelProvider labelProvider = new AdvancePropertyLabelProvider( );

	public AdvancePropertyContentProvider getContentProvier( )
	{
		return contentProvider;
	}

	public AdvancePropertyLabelProvider getLabelProvier( )
	{
		return labelProvider;
	}

	public boolean addNode( Memento element, MementoElement[] nodePath )
	{
		if ( nodePath != null && nodePath.length > 0 )
		{
			MementoElement memento = element.getMementoElement( );
			if ( !memento.equals( nodePath[0] ) )
				return false;
			for ( int i = 1; i < nodePath.length; i++ )
			{
				MementoElement child = getChild( memento, nodePath[i] );
				if ( child != null )
					memento = child;
				else
				{
					memento.addChild( nodePath[i] );
					return true;
				}
			}
			return true;
		}
		return false;
	}

	public boolean removeNode( Memento element, MementoElement[] nodePath )
	{
		if ( nodePath != null && nodePath.length > 0 )
		{
			MementoElement memento = element.getMementoElement( );
			if ( !memento.equals( nodePath[0] ) )
				return false;
			for ( int i = 1; i < nodePath.length; i++ )
			{
				MementoElement child = getChild( memento, nodePath[i] );
				if ( child != null )
					memento = child;
				else
					return false;
			}
			memento.getParent( ).removeChild( memento );
			return true;
		}
		return false;
	}

	private MementoElement getChild( MementoElement parent, MementoElement key )
	{
		MementoElement[] children = parent.getChildren( );
		for ( int i = 0; i < children.length; i++ )
		{
			if ( children[i].equals( key ) )
				return children[i];
		}
		return null;
	};

	public MementoElement[] getNodePath( MementoElement node )
	{
		LinkedList pathList = new LinkedList( );
		MementoElement memento = node;
		pathList.add( node );// add root
		while ( memento.getChildren( ).length > 0 )
		{
			pathList.add( memento.getChild( 0 ) );
			memento = (MementoElement) memento.getChild( 0 );
		}
		MementoElement[] paths = new MementoElement[pathList.size( )];
		pathList.toArray( paths );
		return paths;
	}

	public String getElementType( )
	{
		String displayName = ( (DesignElementHandle) DEUtil.getInputFirstElement( input ) ).getDefn( )
				.getDisplayName( );

		if ( displayName == null || "".equals( displayName ) )//$NON-NLS-1$ 
		{
			displayName = ( (DesignElementHandle) DEUtil.getInputFirstElement( input ) ).getDefn( )
					.getName( );
		}

		return displayName;
	}
}

class AdvancePropertyLabelProvider implements ITableLabelProvider
{

	private static final String PASSWORD_REPLACEMENT = "********";//$NON-NLS-1$ 

	public Image getColumnImage( Object element, int columnIndex )
	{
		return null;
	}

	public String getColumnText( Object element, int columnIndex )
	{
		if ( columnIndex == 0 )
		{
			if ( element instanceof List )
			{
				GroupPropertyHandle property = (GroupPropertyHandle) ( (GroupPropertyHandleWrapper) ( ( (List) element ).get( 0 ) ) ).getModel( );
				return property.getPropertyDefn( ).getGroupName( );
			}

			if ( element instanceof PropertySheetRootElement )
			{
				return ( (PropertySheetRootElement) element ).getDisplayName( );
			}
			GroupPropertyHandle property = ( (GroupPropertyHandleWrapper) element ).getModel( );
			return property.getPropertyDefn( ).getDisplayName( );
		}
		else if ( columnIndex == 1
				&& element instanceof GroupPropertyHandleWrapper )
		{
			GroupPropertyHandle propertyHandle = (GroupPropertyHandle) ( (GroupPropertyHandleWrapper) element ).getModel( );
			String value = null;
			if ( propertyHandle != null )
			{
				if ( propertyHandle.getStringValue( ) != null )
				{
					if ( propertyHandle.getPropertyDefn( ).isEncryptable( ) )
					{
						value = PASSWORD_REPLACEMENT;
					}
					else
					{
						value = propertyHandle.getStringValue( );
					}
				}
			}
			if ( value == null )
				value = ""; //$NON-NLS-1$ 
			return value;
		}
		else
			return ""; //$NON-NLS-1$ 
	}

	public void addListener( ILabelProviderListener listener )
	{
	}

	public void dispose( )
	{
	}

	public boolean isLabelProperty( Object element, String property )
	{
		return false;
	}

	public void removeListener( ILabelProviderListener listener )
	{
	}
}

class AdvancePropertyContentProvider implements ITreeContentProvider
{

	private static final String ROOT_DEFAUL_TITLE = Messages.getString( "ReportPropertySheetPage.Root.Default.Title" ); //$NON-NLS-1$

	public Object[] getChildren( Object parentElement )
	{
		if ( parentElement instanceof List )
		{
			return ( (List) parentElement ).toArray( );
		}
		if ( parentElement instanceof PropertySheetRootElement )
		{
			ArrayList items = new ArrayList( );
			HashMap map = new HashMap( );
			GroupElementHandle handle = (GroupElementHandle) ( (PropertySheetRootElement) parentElement ).getModel( );

			for ( Iterator it = handle.visiblePropertyIterator( ); it.hasNext( ); )
			{
				GroupPropertyHandle property = (GroupPropertyHandle) it.next( );
				IElementPropertyDefn defn = property.getPropertyDefn( );
				if ( defn.getGroupNameKey( ) == null )
					items.add( new GroupPropertyHandleWrapper( property ) );
				else
				{
					List group = (List) map.get( defn.getGroupNameKey( ) );
					if ( group == null )
					{
						group = new ArrayList( );
						items.add( group );
						map.put( defn.getGroupNameKey( ), group );
					}
					group.add( new GroupPropertyHandleWrapper( property ) );
				}
			}
			return items.toArray( );
		}
		return null;
	}

	public Object getParent( Object element )
	{
		return null;
	}

	public boolean hasChildren( Object element )
	{
		return ( ( element instanceof List && ( (List) element ).size( ) > 0 ) || element instanceof PropertySheetRootElement );
	}

	PropertySheetRootElement[] roots = new PropertySheetRootElement[1];
	public Object[] getElements( Object input )
	{
		GroupElementHandle inputElement = DEUtil.getGroupElementHandle( DEUtil.getInputElements( input ) );
		if ( inputElement instanceof GroupElementHandle )
		{

			PropertySheetRootElement root = new PropertySheetRootElement( inputElement );

			String displayName = null;
			Object element = ( (GroupElementHandle) inputElement ).getElements( )
					.get( 0 );

			if ( element instanceof DesignElementHandle )
			{
				displayName = ( (DesignElementHandle) element ).getDefn( )
						.getDisplayName( );

				if ( displayName == null || "".equals( displayName ) )//$NON-NLS-1$ 
				{
					displayName = ( (DesignElementHandle) element ).getDefn( )
							.getName( );
				}
			}

			if ( displayName == null || "".equals( displayName ) )//$NON-NLS-1$ 
			{
				displayName = ROOT_DEFAUL_TITLE;
			}
			root.setDisplayName( displayName );
			
			roots[0] = root;
		}
		return roots;
	}

	public void dispose( )
	{
	}

	public void inputChanged( Viewer viewer, Object oldInput, Object newInput )
	{
	}
}
