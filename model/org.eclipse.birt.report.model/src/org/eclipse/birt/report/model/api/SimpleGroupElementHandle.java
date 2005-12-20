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
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

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
	 * @see org.eclipse.birt.report.model.api.GroupElementHandle#getModuleHandle()
	 */

	public ModuleHandle getModuleHandle( )
	{
		return (ModuleHandle) module.getHandle( module );
	}

	/**
	 * Finds common properties from two set of property definitions. Two
	 * property definitions are considered identical if they are same instances
	 * or they are equal.
	 * 
	 * @param list1
	 *            storing a list of <code>PropertyDefn</code>
	 * @param list2
	 *            storing a list of <code>PropertyDefn</code>
	 * @return A set containing all the common properties from the two lists.
	 */

	private List findInCommon( List list1, List list2 )
	{
		List retList = new ArrayList( );

		for ( Iterator iter = list1.iterator( ); iter.hasNext( ); )
		{
			PropertyDefn propDefn = (PropertyDefn) iter.next( );
			if ( list2.contains( propDefn ) )
				retList.add( propDefn );
		}

		return retList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.GroupElementHandle#getCommonProperties()
	 */

	public List getCommonProperties( )
	{
		List commonProps = Collections.EMPTY_LIST;

		for ( int i = 0; i < elements.size( ); i++ )
		{
			Object item = elements.get( i );

			if ( !( item instanceof DesignElementHandle ) )
				return Collections.EMPTY_LIST;

			List elemProps = ( (DesignElementHandle) item ).getElement( )
					.getPropertyDefns( );

			if ( i == 0 )
				commonProps = elemProps;
			else
				commonProps = findInCommon( commonProps, elemProps );

			if ( commonProps.isEmpty( ) )
				return Collections.EMPTY_LIST;
		}

		return commonProps;
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
	 * @see org.eclipse.birt.report.model.api.GroupElementHandle#visiblePropertyIterator()
	 */

	public Iterator visiblePropertyIterator( )
	{
		List list = getCommonProperties( );
		final List visibleList = new ArrayList( );

		for ( int i = 0; i < list.size( ); i++ )
		{
			IElementPropertyDefn propDefn = (IElementPropertyDefn) list.get( i );
			if ( isPropertyVisible( propDefn.getName( ) ) )
				visibleList.add( propDefn );
		}

		return new GroupPropertyIterator( visibleList );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.GroupElementHandle#isPropertyVisible(java.lang.String)
	 */

	protected boolean isPropertyVisible( String propName )
	{
		boolean isVisible = true;

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
			{
				isVisible = false;
				break;
			}
		}

		return isVisible;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.GroupElementHandle#clearLocalProperties()
	 */

	public void clearLocalProperties( ) throws SemanticException
	{
		ActivityStack stack = module.getActivityStack( );
		stack.startTrans( );

		try
		{
			Iterator iter = propertyIterator( );
			while ( iter.hasNext( ) )
			{
				GroupPropertyHandle propHandle = (GroupPropertyHandle) iter
						.next( );

				String propName = propHandle.getPropertyDefn( ).getName( );
				if ( DesignElement.EXTENDS_PROP.equals( propName ) )
				{
					// ignore extends property.
					continue;
				}

				propHandle.clearValue( );
			}
		}
		catch ( SemanticException e )
		{
			stack.rollback( );
			throw e;
		}

		stack.commit( );

	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.api.GroupElementHandle#isExtendedElements()
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

			if ( ( (DesignElementHandle) next ).getExtends( ) == null )
				return false;
		}

		// Each element has a parent.

		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.api.GroupElementHandle#isPropertyReadOnly(java.lang.String)
	 */
	
	protected boolean isPropertyReadOnly( String propName )
	{
		boolean isReadOnly = false;

		List elements = getElements( );
		for ( int i = 0; i < elements.size( ); i++ )
		{
			PropertyHandle propertyHandle = ( (DesignElementHandle) elements
					.get( i ) ).getPropertyHandle( propName );

			// if the property is not defined, then it is read-only; if it
			// exsits and set to read-only in ROM, then it is read-only too.

			if ( propertyHandle != null && propertyHandle.isReadOnly( )
					|| propertyHandle == null )
			{
				isReadOnly = true;
				break;
			}
		}

		return isReadOnly;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.api.GroupElementHandle#getPropertyHandle(java.lang.String)
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

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.api.GroupElementHandle#isInGroup(org.eclipse.birt.report.model.api.DesignElementHandle)
	 */
	
	protected boolean isInGroup( DesignElementHandle element )
	{
		return elements.contains( element );
	}
}
