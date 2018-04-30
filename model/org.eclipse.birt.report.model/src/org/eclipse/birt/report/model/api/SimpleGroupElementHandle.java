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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.PropertyBinding;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.interfaces.ICellModel;
import org.eclipse.birt.report.model.elements.interfaces.IDerivedExtendableElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IExtendedItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IOdaExtendableElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.CommandLabelFactory;

/**
 * Implements a simple group element handle, which has a non-empty selection
 * element list and root module.
 */

public class SimpleGroupElementHandle extends GroupElementHandle
{

	/**
	 * The module that provides overall information, especially the command
	 * stack.
	 */

	protected final Module module;

	/**
	 * List of handles to design elements.
	 */

	protected List elements = null;

	/**
	 * Constructs a handle to deal with a list of report elements. The contents
	 * of the given list should be design element handles.
	 * 
	 * @param moduleHandle
	 *            the handle of module
	 * @param elements
	 *            a list of handles of design elements. If a item is not
	 *            <code>DesignElementHandle</code>, it is ignored.
	 * @see DesignElementHandle
	 */

	public SimpleGroupElementHandle( ModuleHandle moduleHandle, List elements )
	{
		assert moduleHandle != null;
		module = moduleHandle.getModule( );
		assert elements != null;

		this.elements = elements;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.GroupElementHandle#getElements()
	 */

	public List getElements( )
	{
		return elements;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.GroupElementHandle#getModule()
	 */

	public Module getModule( )
	{
		return module;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.GroupElementHandle#getModuleHandle()
	 */

	public ModuleHandle getModuleHandle( )
	{
		return (ModuleHandle) module.getHandle( module );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.GroupElementHandle#getCommonProperties
	 * ()
	 */

	public List getCommonProperties( )
	{
		if ( elements.size( ) == 1 )
			return Collections
					.unmodifiableList( ( (DesignElementHandle) elements.get( 0 ) )
							.getElement( ).getPropertyDefns( ) );

		List minProps = getMinPropDefns( );
		List commonProps = new ArrayList( minProps );

		Iterator iter = minProps.iterator( );
		while ( iter.hasNext( ) )
		{
			PropertyDefn propDefn = (PropertyDefn) iter.next( );
			for ( int i = 0; i < elements.size( ); i++ )
			{
				if ( ( (DesignElementHandle) elements.get( i ) ).getElement( )
						.getPropertyDefn( propDefn.getName( ) ) == null )
				{
					commonProps.remove( propDefn );
					break;
				}
			}
		}

		return Collections.unmodifiableList( commonProps );
	}

	/**
	 * Returns the property definition list that has the minimum size.
	 * 
	 * @return the property definition list that has the minimum size.
	 */

	private List getMinPropDefns( )
	{
		int min = Integer.MAX_VALUE;
		List rtnPropDefns = Collections.EMPTY_LIST;

		for ( int j = 0; j < elements.size( ); j++ )
		{
			Object item = elements.get( j );
			if ( !( item instanceof DesignElementHandle ) )
				return Collections.EMPTY_LIST;

			List propDefns = ( (DesignElementHandle) item ).getElement( )
					.getPropertyDefns( );

			if ( propDefns.size( ) < min )
			{
				min = propDefns.size( );
				rtnPropDefns = propDefns;
			}
		}

		return rtnPropDefns;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.GroupElementHandle#isSameType()
	 */

	public boolean isSameType( )
	{
		if ( elements.size( ) == 0 )
			return false;

		IElementDefn baseDefn = null;

		for ( int i = 0; i < elements.size( ); i++ )
		{
			Object item = elements.get( i );
			if ( !( item instanceof DesignElementHandle ) )
				return false;

			IElementDefn elemDefn = ( (DesignElementHandle) item ).getDefn( );

			if ( baseDefn == null )
				baseDefn = elemDefn;

			if ( elemDefn != baseDefn )
				return false;
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.GroupElementHandle#visiblePropertyIterator
	 * ()
	 */

	public Iterator visiblePropertyIterator( )
	{
		List list = getCommonProperties( );
		final List visibleList = new ArrayList( );

		for ( int i = 0; i < list.size( ); i++ )
		{
			IElementPropertyDefn propDefn = (IElementPropertyDefn) list.get( i );
			if ( isPropertyVisible( propDefn.getName( ) ) )
			{
				visibleList.add( propDefn );
			}
		}

		return new GroupPropertyIterator( visibleList );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.GroupElementHandle#isPropertyVisible
	 * (java.lang.String)
	 */

	protected boolean isPropertyVisible( String propName )
	{
		List elements = getElements( );
		for ( int i = 0; i < elements.size( ); i++ )
		{
			PropertyHandle propertyHandle = ( (DesignElementHandle) elements
					.get( i ) ).getPropertyHandle( propName );

			// if the property is not defined, then it is invisible; if the
			// property exsits and set to invisible in ROM, then it is invisible
			// too.

			if ( propertyHandle != null && !propertyHandle.isVisible( )
					|| propertyHandle == null )
				return false;
		}

		// if the group is in master page, property toc, bookmark, pagebreak
		// should be set invisible.

		return !needHide( propName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.GroupElementHandle#clearLocalProperties
	 * ()
	 */

	public void clearLocalProperties( ) throws SemanticException
	{
		ActivityStack stack = module.getActivityStack( );
		stack.startTrans( CommandLabelFactory
				.getCommandLabel( MessageConstants.CLEAR_PROPERTIES_MESSAGE ) );

		try
		{
			Iterator iter = propertyIterator( );
			while ( iter.hasNext( ) )
			{
				GroupPropertyHandle propHandle = (GroupPropertyHandle) iter
						.next( );
				DesignElementHandle elementHandle = null;
				if ( elements != null && !elements.isEmpty( ) )
					elementHandle = (DesignElementHandle) elements.get( 0 );

				String propName = propHandle.getPropertyDefn( ).getName( );

				if ( IDesignElementModel.EXTENDS_PROP.equals( propName )
						|| IDesignElementModel.NAME_PROP.equals( propName )
						|| ( IExtendedItemModel.EXTENSION_NAME_PROP
								.equals( propName ) && elementHandle instanceof IExtendedItemModel )
						|| propHandle.isExtensionModelProperty( )
						|| ( elementHandle instanceof IDerivedExtendableElementModel && IDerivedExtendableElementModel.EXTENSION_ID_PROP
								.equals( propName ) )
						|| ( elementHandle instanceof IOdaExtendableElementModel && IOdaExtendableElementModel.EXTENSION_ID_PROP
								.equals( propName ) )
						|| propHandle.getPropertyDefn( ).getTypeCode( ) == IPropertyType.ELEMENT_TYPE )
				{
					// ignore name, extends, extension id property.
					continue;
				}
				Object localValue = propHandle.getLocalValue( );

				if ( localValue != null )
				{
					propHandle.clearValue( );
				}

			}
			clearPropertyBindings();
		}
		catch ( SemanticException e )
		{
			stack.rollback( );
			throw e;
		}

		stack.commit( );

	}
	
	//Clears property bindings for all selected items which are of DesignElementHandle type
	private void clearPropertyBindings( ) throws SemanticException
	{
		List<PropertyBinding> propertyBindings;

		for ( Object element : elements )
		{
			if ( element instanceof DesignElementHandle )
			{

				propertyBindings = ( (DesignElementHandle) element )
						.getPropertyBindings( );

				for ( PropertyBinding propertyBinding : propertyBindings )
				{
					( (DesignElementHandle) element ).setPropertyBinding(
							propertyBinding.getName( ), (Expression) null );

				}
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.GroupElementHandle#clearLocalPropertiesIncludeSubElement
	 * ()
	 */
	public void clearLocalPropertiesIncludeSubElement( ) throws SemanticException
	{
		ActivityStack stack = module.getActivityStack( );
		stack.startTrans( CommandLabelFactory.getCommandLabel( MessageConstants.CLEAR_PROPERTIES_MESSAGE ) );
		try
		{
			Iterator iter = propertyIterator( );
			while ( iter.hasNext( ) )
			{
				GroupPropertyHandle propHandle = (GroupPropertyHandle) iter.next( );
				DesignElementHandle elementHandle = null;
				if ( elements != null && !elements.isEmpty( ) )
				{
					elementHandle = (DesignElementHandle) elements.get( 0 );
				}
				String propName = propHandle.getPropertyDefn( ).getName( );
				Iterator iterator = elementHandle.getPropertyIterator( );
				if ( IDesignElementModel.EXTENDS_PROP.equals( propName )
						|| IDesignElementModel.NAME_PROP.equals( propName )
						|| ( IExtendedItemModel.EXTENSION_NAME_PROP.equals( propName ) && elementHandle instanceof IExtendedItemModel )
						|| propHandle.isExtensionModelProperty( )
						|| ( elementHandle instanceof IDerivedExtendableElementModel && IDerivedExtendableElementModel.EXTENSION_ID_PROP.equals( propName ) )
						|| ( elementHandle instanceof IOdaExtendableElementModel && IOdaExtendableElementModel.EXTENSION_ID_PROP.equals( propName ) ) )
				{
					// ignore name, extends, extension id property.
					continue;
				}
				if ( propHandle.getPropertyDefn( ).getTypeCode( ) == IPropertyType.ELEMENT_TYPE )
				{
					Object object = propHandle.getValue( );
					if ( object != null )
					{
						if ( object instanceof DesignElementHandle )
						{
							DesignElementHandle designElementHandle = (DesignElementHandle) object;
							clearLocalPropertiesIncludeSubElement( designElementHandle );
						}
						else if ( object instanceof List && ( (List) object ).size( ) > 0 )
						{
							for ( int i = 0; i < ( (List) object ).size( ); i++ )
							{
								DesignElementHandle designElementHandle = (DesignElementHandle) ( (List) object ).get( i );
								clearLocalPropertiesIncludeSubElement( designElementHandle );
							}
						}
					}
					continue;
				}
				Object localValue = propHandle.getLocalValue( );

				if ( localValue != null )
				{
					propHandle.clearValue( );
				}

			}
		}
		catch ( SemanticException e )
		{
			stack.rollback( );
			throw e;
		}
		stack.commit( );
	}	
	
	/** 
	 * Clear local values of elementHandle include sub element.
	 * @param elementHandle
	 * @throws SemanticException
	 */
	private void clearLocalPropertiesIncludeSubElement( DesignElementHandle elementHandle ) throws SemanticException
	{
		if ( !hasLocalPropertiesIncludeSubElement( elementHandle ) )
		{
			return;
		}
		Iterator iterator = elementHandle.getPropertyIterator( );
		while ( iterator.hasNext( ) )
		{
			PropertyHandle propertyHandler = (PropertyHandle) iterator.next( );
			String propertyName = propertyHandler.getPropertyDefn( ).getName( );
			if ( IDesignElementModel.EXTENDS_PROP.equals( propertyName )
					|| IDesignElementModel.NAME_PROP.equals( propertyName )
					|| ( IExtendedItemModel.EXTENSION_NAME_PROP.equals( propertyName ) && elementHandle instanceof IExtendedItemModel )
					|| ( elementHandle instanceof ExtendedItemHandle && ( (ExtendedItem) elementHandle.getElement( ) ).isExtensionModelProperty( propertyName ) )
					|| ( elementHandle instanceof IDerivedExtendableElementModel && IDerivedExtendableElementModel.EXTENSION_ID_PROP.equals( propertyName ) )
					|| ( elementHandle instanceof IOdaExtendableElementModel && IOdaExtendableElementModel.EXTENSION_ID_PROP.equals( propertyName ) ) )
			{
				continue;
			}
			if ( propertyHandler.getPropertyDefn( ).getTypeCode( ) == IPropertyType.ELEMENT_TYPE )
			{
				List list = propertyHandler.getContents( );
				if ( list != null && list.size( ) > 0 )
				{
					for ( int i = 0; i < list.size( ); i++ )
					{
						DesignElementHandle handle = (DesignElementHandle) list.get( i );
						clearLocalPropertiesIncludeSubElement( handle );
					}
				}
			}
			else
			{
				Object localValue = elementHandle.getElement( ).getLocalProperty( elementHandle.getModule( ), propertyName );
				if ( localValue != null )
				{
					elementHandle.setProperty( propertyName, null );
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.GroupElementHandle#isExtendedElements()
	 */

	public boolean isExtendedElements( )
	{
		if ( elements.isEmpty( ) )
			return false;

		for ( Iterator iter = elements.iterator( ); iter.hasNext( ); )
		{
			Object next = iter.next( );
			if ( !( next instanceof DesignElementHandle ) )
				return false;

			DesignElementHandle designHandle = (DesignElementHandle) next;

			if ( designHandle.getExtends( ) == null )
				return false;

		}

		// Each element has a parent.

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.api.GroupElementHandle#
	 * hasVirtualExtendsElements()
	 */

	protected boolean allExtendedElements( )
	{

		if ( elements.isEmpty( ) )
			return false;

		for ( Iterator iter = elements.iterator( ); iter.hasNext( ); )
		{
			Object next = iter.next( );
			if ( !( next instanceof DesignElementHandle ) )
				return false;

			DesignElementHandle elementHandle = (DesignElementHandle) next;

			// Design without extends element and virtual extends element

			if ( ( elementHandle.getExtends( ) == null )
					&& ( elementHandle.getElement( ).getBaseId( ) <= 0 ) )
				return false;

		}

		// Each element has a parent.

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.GroupElementHandle#isPropertyReadOnly
	 * (java.lang.String)
	 */

	protected boolean isPropertyReadOnly( String propName )
	{
		for ( int i = 0; i < elements.size( ); i++ )
		{
			PropertyHandle propertyHandle = ( (DesignElementHandle) elements
					.get( i ) ).getPropertyHandle( propName );

			// if the property is not defined, then it is read-only; if it
			// exsits and set to read-only in ROM, then it is read-only too.

			if ( propertyHandle != null && propertyHandle.isReadOnly( )
					|| propertyHandle == null )
				return true;
		}

		// if the group is in master page, property toc, bookmark, pagebreak
		// should be set readonly.

		return needHide( propName );
	}

	/**
	 * Returns if the property need to be hiden under some cases.
	 * 
	 * @param propName
	 *            the property name to check
	 * 
	 * @return true if the property need to be hiden under some cases, false
	 *         otherwise.
	 */

	private boolean needHide( String propName )
	{
		if ( !( IReportItemModel.BOOKMARK_PROP.equals( propName )
				|| IReportItemModel.TOC_PROP.equals( propName )
				|| IStyleModel.PAGE_BREAK_AFTER_PROP.equals( propName )
				|| IStyleModel.PAGE_BREAK_BEFORE_PROP.equals( propName )
				|| IStyleModel.PAGE_BREAK_INSIDE_PROP.equals( propName ) || ICellModel.DROP_PROP
				.equals( propName ) ) )
			return false;

		for ( int i = 0; i < elements.size( ); i++ )
		{
			DesignElementHandle current = ( (DesignElementHandle) elements
					.get( i ) );
			DesignElementHandle container = current.getContainer( );

			// hide "drop" property for all cells except cells in group
			// element
			if ( ICellModel.DROP_PROP.equals( propName ) )
			{
				if ( current instanceof CellHandle )
				{
					if ( container == null )
					{
						continue;
					}
					if ( !( container.getContainer( ) instanceof GroupHandle ) )
						return true;
				}
			}
			else
			{
				while ( container != null )
				{
					if ( container instanceof MasterPageHandle )
						return true;
					container = container.getContainer( );
				}
			}
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.GroupElementHandle#getPropertyHandle
	 * (java.lang.String)
	 */

	public GroupPropertyHandle getPropertyHandle( String propName )
	{
		List commProps = getCommonProperties( );
		for ( int i = 0; i < commProps.size( ); i++ )
		{
			ElementPropertyDefn propDefn = (ElementPropertyDefn) commProps
					.get( i );
			if ( propDefn.getName( ).equalsIgnoreCase( propName ) )
			{
				return new GroupPropertyHandle( this, propDefn );
			}
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.GroupElementHandle#isInGroup(org.eclipse
	 * .birt.report.model.api.DesignElementHandle)
	 */

	protected boolean isInGroup( DesignElementHandle element )
	{
		return elements.contains( element );
	}
}
