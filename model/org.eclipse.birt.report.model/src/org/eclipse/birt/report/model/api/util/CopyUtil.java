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

package org.eclipse.birt.report.model.api.util;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.core.IDesignElement;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.util.ContentExceptionFactory;
import org.eclipse.birt.report.model.util.LevelContentIterator;
import org.eclipse.birt.report.model.util.copy.ContextCopyPastePolicy;

/**
 * The utility class for copy/paste. It is for the UI usage. Other uses should
 * use <code>DesignElementHandle.copy()</code>.
 * 
 */

public class CopyUtil
{

	/**
	 * Returns the copy of the current element.
	 * 
	 * @param source
	 *            the given element
	 * 
	 * @return the copy of the given element
	 */

	public static IElementCopy copy( DesignElementHandle source )
	{
		return ContextCopyPastePolicy.getInstance( ).createCopy(
				source.getElement( ), source.getModule( ) );
	}

	/**
	 * Pastes a report item to the slot. The item must be newly created and not
	 * yet added to the design.
	 * 
	 * @param copy
	 *            the copy from the return value of <code>copy</code>
	 * @param container
	 *            the target container
	 * @param slotID
	 *            the id of the target slot
	 * @return a list containing all errors for the pasted element
	 * @throws SemanticException
	 *             if the element is not allowed in the slot
	 */

	public static List paste( IElementCopy copy, DesignElementHandle container,
			int slotID ) throws SemanticException
	{
		ContainerContext context = new ContainerContext(
				container.getElement( ), slotID );
		Module root = container.getModule( );
		if ( !canPaste( copy, container, slotID ) )
			throw ContentExceptionFactory
					.createContentException(
							context,
							ContentException.DESIGN_EXCEPTION_CONTENT_NOT_ALLOWED_PASTED );

		IDesignElement chosen = ContextCopyPastePolicy.getInstance( )
				.preWorkForPaste( context, copy, root );

		DesignElementHandle target = chosen.getHandle( root );
		container.getModuleHandle( ).rename( container, target );

		if ( chosen == null )
			return Collections.EMPTY_LIST;

		container.getSlot( slotID ).add( target );
		return checkPostPasteErrors( target.getElement( ), root );
	}

	/**
	 * Pastes a report item to the slot. The item must be newly created and not
	 * yet added to the design.
	 * 
	 * @param copy
	 *            the copy from the return value of <code>copy</code>
	 * @param container
	 *            the target container
	 * @param slotID
	 *            the id of the target slot
	 * @param newPos
	 *            the target position
	 * @return a list containing all errors for the pasted element
	 * @throws SemanticException
	 *             if the element is not allowed in the slot
	 */

	public static List paste( IElementCopy copy, DesignElementHandle container,
			int slotID, int newPos ) throws SemanticException
	{
		ContainerContext context = new ContainerContext(
				container.getElement( ), slotID );

		Module root = container.getModule( );

		if ( !canPaste( copy, container, slotID ) )
			throw ContentExceptionFactory
					.createContentException(
							context,
							ContentException.DESIGN_EXCEPTION_CONTENT_NOT_ALLOWED_PASTED );

		IDesignElement chosen = ContextCopyPastePolicy.getInstance( )
				.preWorkForPaste( context, copy, root );

		if ( chosen == null )
			return Collections.EMPTY_LIST;

		DesignElementHandle target = chosen.getHandle( root );
		container.getModuleHandle( ).rename( container, target );

		container.getSlot( slotID ).add( target, newPos );
		return checkPostPasteErrors( target.getElement( ), root );
	}

	/**
	 * Pastes a report item to the slot. The item must be newly created and not
	 * yet added to the design.
	 * 
	 * @param copy
	 *            the copy from the return value of <code>copy</code>
	 * @param container
	 *            the target container
	 * @param propName
	 *            the property name of the target container
	 * 
	 * @return a list containing all errors for the pasted element
	 * @throws SemanticException
	 *             if the element is not allowed in the slot
	 */

	public static List paste( IElementCopy copy, DesignElementHandle container,
			String propName ) throws SemanticException
	{
		ContainerContext context = new ContainerContext(
				container.getElement( ), propName );

		Module root = container.getModule( );

		if ( !canPaste( copy, container, propName ) )
			throw ContentExceptionFactory
					.createContentException(
							context,
							ContentException.DESIGN_EXCEPTION_CONTENT_NOT_ALLOWED_PASTED );

		IDesignElement chosen = ContextCopyPastePolicy.getInstance( )
				.preWorkForPaste( context, copy, root );

		if ( chosen == null )
			return Collections.EMPTY_LIST;

		DesignElementHandle target = chosen.getHandle( root );
		container.getModuleHandle( ).rename( container, target );

		container.add( propName, target );
		return checkPostPasteErrors( target.getElement( ), root );
	}

	/**
	 * Pastes a report item to the slot. The item must be newly created and not
	 * yet added to the design.
	 * 
	 * @param copy
	 *            the copy from the return value of <code>copy</code>
	 * @param container
	 *            the target container
	 * @param propName
	 *            the property name of the target container
	 * @param newPos
	 *            the target position
	 * 
	 * @return a list containing all errors for the pasted element
	 * @throws SemanticException
	 *             if the element is not allowed in the slot
	 */

	public static List paste( IElementCopy copy, DesignElementHandle container,
			String propName, int newPos ) throws SemanticException
	{
		ContainerContext context = new ContainerContext(
				container.getElement( ), propName );

		Module root = container.getModule( );

		if ( !canPaste( copy, container, propName ) )
			throw ContentExceptionFactory
					.createContentException(
							context,
							ContentException.DESIGN_EXCEPTION_CONTENT_NOT_ALLOWED_PASTED );

		IDesignElement chosen = ContextCopyPastePolicy.getInstance( )
				.preWorkForPaste( context, copy, root );

		DesignElementHandle target = chosen.getHandle( root );
		container.getModuleHandle( ).rename( container, target );

		if ( chosen == null )
			return Collections.EMPTY_LIST;

		container.add( propName, target, newPos );
		return checkPostPasteErrors( target.getElement( ), root );
	}

	/**
	 * /** Checks whether the given copy can be pasted into the given slot of
	 * the specified element.
	 * 
	 * @param copy
	 *            the copied instance
	 * @param container
	 *            the target element
	 * @param slotID
	 *            the target slot id
	 * @return <code>true</code> is the copy is good for pasting. Otherwise
	 *         <code>false</code>.
	 */

	public static boolean canPaste( IElementCopy copy,
			DesignElementHandle container, int slotID )
	{
		if ( slotID >= container.getDefn( ).getSlotCount( ) )
			return false;

		return ContextCopyPastePolicy.getInstance( ).isValidCopy( copy );

	}

	/**
	 * /** Checks whether the given copy can be pasted into the given slot of
	 * the specified element.
	 * 
	 * @param copy
	 *            the copied instance
	 * @param container
	 *            the target element
	 * @param propName
	 *            the target property name
	 * @return <code>true</code> is the copy is good for pasting. Otherwise
	 *         <code>false</code>.
	 */

	public static boolean canPaste( IElementCopy copy,
			DesignElementHandle container, String propName )
	{
		IPropertyDefn propDefn = container.getPropertyDefn( propName );
		if ( propDefn == null )
			return false;

		if ( propDefn.getTypeCode( ) != IPropertyType.ELEMENT_TYPE )
			return false;

		return ContextCopyPastePolicy.getInstance( ).isValidCopy( copy );
	}

	/**
	 * Checks the element after the paste action.
	 * 
	 * @param content
	 *            the pasted element
	 * 
	 * @return a list containing parsing errors. Each element in the list is
	 *         <code>ErrorDetail</code>.
	 */

	private static List checkPostPasteErrors( DesignElement content, Module root )
	{
		revisePropertyNameSpace( root, content, content.getDefn( ).getProperty(
				IDesignElementModel.EXTENDS_PROP ) );

		reviseNameSpace( root, content );

		List exceptionList = content.validateWithContents( root );
		List errorDetailList = ErrorDetail.convertExceptionList( exceptionList );

		return errorDetailList;
	}

	/**
	 * Uses the new name space of the current module for reference property
	 * values of the given element. This method checks the <code>content</code>
	 * and nested elements in it.
	 * 
	 * @param module
	 *            the module that <code>content</code> attaches.
	 * @param content
	 *            the element to revise
	 * @param nameSpace
	 *            the new name space
	 */

	private static void reviseNameSpace( Module module, DesignElement content )
	{
		Iterator propNames = content.propertyWithLocalValueIterator( );
		IElementDefn defn = content.getDefn( );

		while ( propNames.hasNext( ) )
		{
			String propName = (String) propNames.next( );

			ElementPropertyDefn propDefn = (ElementPropertyDefn) defn
					.getProperty( propName );
			revisePropertyNameSpace( module, content, propDefn );
		}

		Iterator iter = new LevelContentIterator( module, content, 1 );
		while ( iter.hasNext( ) )
		{
			DesignElement item = (DesignElement) iter.next( );
			reviseNameSpace( module, item );
		}
	}

	/**
	 * Uses the new name space of the current module for reference property
	 * values of the given element. This method checks the <code>content</code>
	 * and nested elements in it.
	 * 
	 * @param module
	 *            the module that <code>content</code> attaches.
	 * @param content
	 *            the element to revise
	 * @param propDefn
	 *            the property definition
	 * @param nameSpace
	 *            the new name space
	 */

	private static void revisePropertyNameSpace( Module module,
			DesignElement content, IElementPropertyDefn propDefn )
	{
		if ( propDefn == null || content == null )
			return;

		if ( propDefn.getTypeCode( ) != IPropertyType.ELEMENT_REF_TYPE
				&& propDefn.getTypeCode( ) != IPropertyType.EXTENDS_TYPE )
			return;

		content.getLocalProperty( module, (ElementPropertyDefn) propDefn );
	}
}
