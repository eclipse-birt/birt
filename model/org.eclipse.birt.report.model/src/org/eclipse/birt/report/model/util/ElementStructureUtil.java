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

package org.eclipse.birt.report.model.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.core.StyledElement;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.ReferenceValue;

/**
 * The utility class to duplicate the layout structure from one element to
 * another. The two input element must have extends relationship. Otherwise,
 * methods have no effect.
 */

public class ElementStructureUtil
{

	/**
	 * The data structure to store the property name/value pair.
	 */

	private static class Property
	{

		private String name;
		private Object value;

		Property( String name, Object value )
		{
			this.name = name;
			this.value = value;
		}

		String getName( )
		{
			return name;
		}

		Object getValue( )
		{
			return value;
		}

	}

	/**
	 * Updates the structure from one exntend parent element to its one child.
	 * Local properties will all be cleared.Please note that the containment
	 * relationship is kept while property values are not copied.
	 * <p>
	 * The two element should be the same type.
	 * 
	 * @param child
	 *            the child element
	 * @param parent
	 *            the extends parent element
	 * @return <code>true</code> if the refresh action is successful.
	 *         <code>false</code> othersize.
	 * 
	 */

	public static boolean updateStructureFromParent( DesignElement child,
			DesignElement parent )
	{
		if ( child == null || parent == null )
			return false;

		if ( child.getExtendsElement( ) != parent )
			return false;

		Map overriddenValues = collectPropertyValues( child );
		boolean retValue = duplicateStructure( parent, child );
		distributeValues( child, overriddenValues );
		return retValue;
	}

	/**
	 * Scatters overridden values to virtual elements in the given design
	 * element.
	 * 
	 * @param element
	 *            the design element
	 * @param overriddenValues
	 *            a map containing overridden values of virtual element. The key
	 *            is the base id of virtual element. The value is a list
	 *            containing property name/value pair.
	 */

	public static void distributeValues( DesignElement element,
			Map overriddenValues )
	{
		ContentIterator contentIterator = new ContentIterator( element );

		while ( contentIterator.hasNext( ) )
		{
			DesignElement content = (DesignElement) contentIterator.next( );
			Long baseId = new Long( content.getID( ) );

			List values = (List) overriddenValues.get( baseId );
			if ( values == null || values.isEmpty( ) )
				continue;

			for ( int i = 0; i < values.size( ); i++ )
			{
				Property prop = (Property) values.get( i );

				// TODO if the name is set, add it to name space.

				// the intrinsic style property must use setStyle().

				if ( StyledElement.STYLE_PROP.equals( prop.getName( ) ) )
					( (StyledElement) content ).setStyleName( (String) prop
							.getValue( ) );
				else
					content.setProperty( prop.getName( ), prop.getValue( ) );
			}
		}

	}

	/**
	 * Gathers local values of virtual elements in the given design element.
	 * 
	 * @param element
	 *            the design element
	 * @return a map containing overridden values of virtual element. The key is
	 *         the base id of virtual element. The value is a list containing
	 *         property name/value pair.
	 */

	public static Map collectPropertyValues( DesignElement element )
	{
		Map map = new HashMap( );

		Module root = element.getRoot( );

		ContentIterator contentIterator = new ContentIterator( element );

		while ( contentIterator.hasNext( ) )
		{
			DesignElement content = (DesignElement) contentIterator.next( );
			Long baseId = new Long( content.getBaseId( ) );

			List values = (List) map.get( baseId );
			if ( values == null )
			{
				values = new ArrayList( );
				map.put( baseId, values );
			}

			List propDefns = null;
			if ( content instanceof ExtendedItem )
				propDefns = ( (ExtendedItem) content ).getExtDefn( )
						.getProperties( );
			else
				propDefns = content.getPropertyDefns( );

			for ( int i = 0; i < propDefns.size( ); i++ )
			{
				PropertyDefn propDefn = (PropertyDefn) propDefns.get( i );
				if ( DesignElement.NAME_PROP.equalsIgnoreCase( propDefn
						.getName( ) )
						|| DesignElement.EXTENDS_PROP
								.equalsIgnoreCase( propDefn.getName( ) ) )
					continue;

				if ( content instanceof ExtendedItem
						&& ExtendedItem.EXTENSION_NAME_PROP
								.equalsIgnoreCase( propDefn.getName( ) ) )
					continue;

				String propName = propDefn.getName( );
				Object propValue = content.getLocalProperty( root, propName );
				if ( propValue == null )
					continue;

				if ( StyledElement.STYLE_PROP.equals( propName ) )
				{
					ReferenceValue refValue = (ReferenceValue) propValue;
					propValue = refValue.getName( );
				}

				values.add( new Property( propName, propValue ) );
			}
		}

		return map;
	}

	/**
	 * Duplicates the structure from one element to another element. Local
	 * properties will all be cleared.Please note that the containment
	 * relationship is kept while property values are not copied.
	 * <p>
	 * The two element should be the same type.
	 * 
	 * @param source
	 *            source element
	 * @param target
	 *            target element
	 * @return <code>true</code> if the refresh action is successful.
	 *         <code>false</code> othersize.
	 * 
	 */

	private static boolean duplicateStructure( DesignElement source,
			DesignElement target )
	{
		assert source != null;
		assert target != null;
		assert source.getDefn( ) == target.getDefn( );

		if ( source.getDefn( ).getSlotCount( ) == 0 )
			return true;

		DesignElement cloned = null;
		try
		{
			cloned = (DesignElement) source.clone( );
		}
		catch ( CloneNotSupportedException e )
		{
			assert false;
			return false;
		}

		// Parent element and the cloned one must have the same structures.
		// Clear all children's properties and set base id reference.

		Iterator sourceIter = new ContentIterator( source );
		Iterator clonedIter = new ContentIterator( cloned );
		while ( clonedIter.hasNext( ) )
		{
			DesignElement virtualParent = (DesignElement) sourceIter.next( );
			DesignElement virtualChild = (DesignElement) clonedIter.next( );

			String name = virtualChild.getName( );
			virtualChild.clearAllProperties( );

			if ( name != null )
				virtualChild.setName( name );

			virtualChild.setBaseId( virtualParent.getID( ) );
		}

		// Copies top level slots from cloned element to the target element.

		for ( int i = 0; i < source.getDefn( ).getSlotCount( ); i++ )
		{
			ContainerSlot sourceSlot = cloned.getSlot( i );
			ContainerSlot targetSlot = target.getSlot( i );

			// clear the slot contents of the this element.

			int count = targetSlot.getCount( );
			while ( --count >= 0 )
			{
				targetSlot.remove( count );
			}

			for ( int j = 0; j < sourceSlot.getCount( ); j++ )
			{
				DesignElement content = sourceSlot.getContent( j );

				// setup the containment relationship

				targetSlot.add( content );
				content.setContainer( target, i );
			}
		}

		return true;
	}

	/**
	 * Clears directly and indirectly included element for the given element.
	 * Uses this method precausiously.
	 * 
	 * @param element
	 *            the design element
	 */

	public static void clearStructure( DesignElement element )
	{
		if ( element == null )
			return;

		if ( element.getDefn( ).getSlotCount( ) == 0 )
			return;

		for ( int i = 0; i < element.getDefn( ).getSlotCount( ); i++ )
		{
			ContainerSlot sourceSlot = element.getSlot( i );
			sourceSlot.clear( );
		}
	}

	/**
	 * Updates layout structures of content elements in the given element. If
	 * one element can find its extends parent, use parent's layout structure.
	 * Otherwise, its layout strcuture is cleared.
	 * 
	 * @param element
	 *            the element
	 * @param module
	 *            the module
	 * 
	 */

	public static void updateContentStructures( DesignElement element,
			Module module )
	{
		ContentIterator contentIter = new ContentIterator( element );
		while ( contentIter.hasNext( ) )
		{
			DesignElement content = (DesignElement) contentIter.next( );
			ElementDefn metaData = (ElementDefn) content.getDefn( );
			if ( !metaData.canExtend( ) )
				continue;

			// try to resolve extends

			content.getLocalProperty( module, DesignElement.EXTENDS_PROP );
			if ( content.getExtendsElement( ) != null )
				content.refreshStructureFromParent( module );
			else
				ElementStructureUtil.clearStructure( content );
		}
	}

	/**
	 * Add the virtual elements name into the module namespace.
	 * 
	 * @param element
	 *            the element contains virtual elements inside.
	 * @param module
	 *            the module
	 */

	public static void addTheVirualElementsToNamesapce( DesignElement element,
			Module module )
	{
		Iterator contentIter = new ContentIterator( element );

		while ( contentIter.hasNext( ) )
		{
			DesignElement virtualElement = (DesignElement) contentIter.next( );

			if ( virtualElement.getName( ) == null )
				continue;

			module.makeUniqueName( virtualElement );

			ElementDefn contentDefn = (ElementDefn) virtualElement.getDefn( );
			int id = contentDefn.getNameSpaceID( );

			NameSpace ns = module.getNameSpace( id );
			ns.insert( virtualElement );

		}
	}
}
