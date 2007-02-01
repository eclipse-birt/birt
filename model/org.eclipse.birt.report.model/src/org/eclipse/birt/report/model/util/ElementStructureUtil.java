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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.core.StyledElement;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.OdaDataSet;
import org.eclipse.birt.report.model.elements.OdaDataSource;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IExtendedItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyledElementModel;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
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
	 * @param childModule
	 *            the module where the child element resides
	 * 
	 * @param child
	 *            the child element
	 * @param parent
	 *            the extends parent element
	 * @return <code>true</code> if the refresh action is successful.
	 *         <code>false</code> othersize.
	 * 
	 */

	public static boolean updateStructureFromParent( Module childModule,
			DesignElement child, DesignElement parent )
	{
		if ( child == null || parent == null )
			return false;

		if ( child.getExtendsElement( ) != parent )
			return false;

		Map overriddenValues = collectPropertyValues( childModule, child );
		boolean retValue = duplicateStructure( parent, child, childModule );
		distributeValues( childModule, child, overriddenValues );
		return retValue;
	}

	/**
	 * Scatters overridden values to virtual elements in the given design
	 * element.
	 * 
	 * @param module
	 * 
	 * @param element
	 *            the design element
	 * @param overriddenValues
	 *            a map containing overridden values of virtual element. The key
	 *            is the base id of virtual element. The value is a list
	 *            containing property name/value pair.
	 */

	public static void distributeValues( Module module, DesignElement element,
			Map overriddenValues )
	{
		if ( element == null )
			return;

		ContentIterator contentIterator = new ContentIterator( module, element );

		while ( contentIterator.hasNext( ) )
		{
			DesignElement content = (DesignElement) contentIterator.next( );
			Long baseId = new Long( content.getID( ) );

			if ( overriddenValues == null || overriddenValues.isEmpty( ) )
				continue;

			List values = (List) overriddenValues.get( baseId );
			if ( values == null || values.isEmpty( ) )
				continue;

			for ( int i = 0; i < values.size( ); i++ )
			{
				Property prop = (Property) values.get( i );

				// TODO if the name is set, add it to name space.

				// the intrinsic style property must use setStyle().

				if ( IStyledElementModel.STYLE_PROP.equals( prop.getName( ) ) )
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
	 * @param module
	 * 
	 * @param element
	 *            the design element
	 * @return a map containing overridden values of virtual element. The key is
	 *         the base id of virtual element. The value is a list containing
	 *         property name/value pair.
	 */

	public static Map collectPropertyValues( Module module,
			DesignElement element )
	{
		if ( element == null )
			return Collections.EMPTY_MAP;

		Map map = new HashMap( );
		Module root = element.getRoot( );

		ContentIterator contentIterator = new ContentIterator( module, element );

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
				if ( IDesignElementModel.NAME_PROP.equalsIgnoreCase( propDefn
						.getName( ) )
						|| IDesignElementModel.EXTENDS_PROP
								.equalsIgnoreCase( propDefn.getName( ) ) )
					continue;

				if ( content instanceof ExtendedItem
						&& IExtendedItemModel.EXTENSION_NAME_PROP
								.equalsIgnoreCase( propDefn.getName( ) ) )
					continue;

				String propName = propDefn.getName( );
				Object propValue = content.getLocalProperty( root, propName );
				if ( propValue == null )
					continue;

				if ( IStyledElementModel.STYLE_PROP.equals( propName ) )
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
	 * @param targetModule
	 *            module where the target element resides
	 * @return <code>true</code> if the refresh action is successful.
	 *         <code>false</code> othersize.
	 * 
	 */

	public static boolean duplicateStructure( DesignElement source,
			DesignElement target, Module targetModule )
	{
		if ( source == null || target == null )
			throw new IllegalArgumentException( "Element can not be null." ); //$NON-NLS-1$

		ElementDefn defn = (ElementDefn) source.getDefn( );
		if ( defn != target.getDefn( ) )
			throw new IllegalArgumentException(
					"Two element are not the same type." ); //$NON-NLS-1$

		if ( !defn.isContainer( ) )
			return true;

		// Copies top level slots from cloned element to the target element.
		for ( int i = 0; i < defn.getSlotCount( ); i++ )
		{
			duplicateStructure( new ContainerContext( source, i ),
					new ContainerContext( target, i ), targetModule );
		}

		// copy top level properties
		List properties = defn.getContainmentProperties( );
		for ( int i = 0; i < properties.size( ); i++ )
		{
			PropertyDefn propDefn = (PropertyDefn) properties.get( i );
			duplicateStructure(
					new ContainerContext( source, propDefn.getName( ) ),
					new ContainerContext( target, propDefn.getName( ) ),
					targetModule );
		}

		return true;
	}

	/**
	 * Duplicates the structure from one element to another element. Local
	 * properties will all be cleared.Please note that the containment
	 * relationship is kept while property values are not copied.
	 * 
	 * @param sourceInfor
	 * @param targetInfor
	 */
	private static void duplicateStructure( ContainerContext sourceInfor,
			ContainerContext targetInfor, Module targetModule )
	{
		// ContainerSlot sourceSlot = source.getSlot( i );
		// ContainerSlot targetSlot = target.getSlot( i );

		// clear the slot contents of the this element.
		targetInfor.clearContents( );

		for ( int j = 0; j < sourceInfor.getContentCount( null ); j++ )
		{
			DesignElement sourceContent = sourceInfor.getContent( null, j );

			// create an element of the same type

			DesignElement targetContent = null;
			if ( sourceContent instanceof ExtendedItem )
			{
				ExtendedItem extendedItem = (ExtendedItem) sourceContent;
				targetContent = new ExtendedItem( sourceContent.getName( ) );
				targetContent
						.setProperty( IExtendedItemModel.EXTENSION_NAME_PROP,
								extendedItem.getProperty( extendedItem
										.getRoot( ),
										IExtendedItemModel.EXTENSION_NAME_PROP ) );
			}
			else if ( sourceContent instanceof OdaDataSet )
			{

			}
			else if ( sourceContent instanceof OdaDataSource )
			{

			}
			else
				targetContent = ModelUtil.newElement( sourceContent
						.getElementName( ), sourceContent.getName( ) );

			if ( targetContent != null )
			{
				// set up the element id and base id
				targetContent.setID( sourceContent.getID( ) );
				targetContent.setBaseId( sourceContent.getID( ) );

				// setup the containment relationship
				targetInfor.add( targetModule, targetContent );

				// recusively duplicates the slots of the content
				duplicateStructure( sourceContent, targetContent, targetModule );
			}
		}
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

		ElementDefn defn = (ElementDefn) element.getDefn( );

		if ( !defn.isContainer( ) )
			return;

		for ( int i = 0; i < defn.getSlotCount( ); i++ )
		{
			new ContainerContext( element, i ).clearContents( );
		}

		List properties = defn.getContainmentProperties( );
		for ( int i = 0; i < properties.size( ); i++ )
		{
			PropertyDefn propDefn = (PropertyDefn) properties.get( i );
			new ContainerContext( element, propDefn.getName( ) ).clearContents( );
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
		ContentIterator contentIter = new ContentIterator( module, element );
		while ( contentIter.hasNext( ) )
		{
			DesignElement content = (DesignElement) contentIter.next( );
			ElementDefn metaData = (ElementDefn) content.getDefn( );
			if ( !metaData.canExtend( ) )
				continue;

			// try to resolve extends

			if ( content.getExtendsElement( ) != null )
				refreshStructureFromParent( module, content );
			else
				clearStructure( content );
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
		Iterator contentIter = new ContentIterator( module, element );

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

	/**
	 * Copied the structure from the parent element to the element itself. Local
	 * properties of the contents will all be cleared.Please note that the
	 * containment relationship is kept while property values of the content
	 * elements are not copied.
	 * 
	 * @param module
	 *            the module
	 * @param element
	 *            the parent element
	 * 
	 * @return <code>true</code> if the refresh action is successful.
	 *         <code>false</code> othersize.
	 * 
	 */

	public static boolean refreshStructureFromParent( Module module,
			DesignElement element )
	{
		if ( element == null )
			throw new IllegalArgumentException(
					"Parent element can not be null." ); //$NON-NLS-1$
		DesignElement parent = element.getExtendsElement( );
		if ( parent == null )
			return false;

		// Copies top level slots from cloned element to the target element.

		boolean result = ElementStructureUtil.updateStructureFromParent(
				module, element, parent );
		if ( element instanceof TableItem )
		{
			( (TableItem) element ).refreshRenderModel( module );
		}
		return result;
	}

	/**
	 * Returns the id reference relationship between the parent element and the
	 * child element.
	 * <p>
	 * Notice: the element and its parent should have the same structure when
	 * calling this method. That is the child structure has already been
	 * refreshed from parent.
	 * 
	 * @param module
	 * 
	 * @param element
	 *            the element to setup the id reference
	 * @return a map to store the base id and the corresponding child element.
	 */

	public static Map getIdMap( Module module, DesignElement element )
	{
		assert element != null;

		// Parent and the child must have the same structures.

		DesignElement parent = element.getExtendsElement( );
		if ( parent == null )
			return Collections.EMPTY_MAP;

		Map idMap = new HashMap( );

		Iterator parentIter = new ContentIterator( module, parent );
		Iterator childIter = new ContentIterator( module, element );
		while ( childIter.hasNext( ) )
		{
			DesignElement virtualParent = (DesignElement) parentIter.next( );
			DesignElement virtualChild = (DesignElement) childIter.next( );

			assert virtualChild.getDefn( ).getName( ) == virtualChild.getDefn( )
					.getName( );
			assert virtualParent.getID( ) > 0;

			idMap.put( new Long( virtualParent.getID( ) ), virtualChild );
		}

		return idMap;
	}

	/**
	 * Break the relationship between the given element to its parent.Set all
	 * properties values of the given element on the element locally. The
	 * following properties will be set:
	 * <ul>
	 * <li>Properties set on element itself
	 * <li>Inherited from style or element's selector style
	 * <li>Inherited from parent
	 * </ul>
	 * 
	 * @param module
	 * 
	 * @param element
	 *            the element to be localized.
	 */

	public static void localizeElement( Module module, DesignElement element )
	{
		assert element != null;
		DesignElement parent = element.getExtendsElement( );
		if ( parent == null )
			return;

		duplicateProperties( parent, element );

		ContentIterator iter1 = new ContentIterator( module, parent );
		ContentIterator iter2 = new ContentIterator( module, element );

		while ( iter1.hasNext( ) )
		{
			DesignElement virtualParent = (DesignElement) iter1.next( );
			DesignElement virtualChild = (DesignElement) iter2.next( );

			duplicateProperties( virtualParent, virtualChild );
		}
	}

	/**
	 * Duplicates some properties in a design element when to export it.
	 * 
	 * @param from
	 *            the from element to get the property values
	 * @param to
	 *            the to element to duplicate the property values
	 */

	private static void duplicateProperties( DesignElement from,
			DesignElement to )
	{
		if ( from.getDefn( ).allowsUserProperties( ) )
		{
			Iterator iter = from.getUserProperties( ).iterator( );
			while ( iter.hasNext( ) )
			{
				UserPropertyDefn userPropDefn = (UserPropertyDefn) iter.next( );
				to.addUserPropertyDefn( userPropDefn );
			}
		}

		Iterator iter = from.getDefn( ).getProperties( ).iterator( );
		while ( iter.hasNext( ) )
		{
			ElementPropertyDefn propDefn = (ElementPropertyDefn) iter.next( );
			String propName = propDefn.getName( );

			// Style property and extends property will be removed.
			// The properties inherited from style or parent will be
			// flatten to new element.

			if ( IStyledElementModel.STYLE_PROP.equals( propName )
					|| IDesignElementModel.EXTENDS_PROP.equals( propName )
					|| IDesignElementModel.USER_PROPERTIES_PROP
							.equals( propName )
					|| IDesignElementModel.REF_TEMPLATE_PARAMETER_PROP
							.equals( propName ) )
				continue;

			Object localValue = to.getLocalProperty( from.getRoot( ), propDefn );
			Object parentValue = from.getStrategy( ).getPropertyFromElement(
					from.getRoot( ), from, propDefn );

			if ( localValue == null && parentValue != null )
			{
				Object valueToSet = ModelUtil.copyValue( propDefn, parentValue );
				to.setProperty( propDefn, valueToSet );
			}
		}
	}
}
