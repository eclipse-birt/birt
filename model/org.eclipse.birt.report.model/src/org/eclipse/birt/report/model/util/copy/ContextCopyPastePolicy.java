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

package org.eclipse.birt.report.model.util.copy;

import org.eclipse.birt.report.model.api.core.IDesignElement;
import org.eclipse.birt.report.model.api.util.IElementCopy;
import org.eclipse.birt.report.model.api.util.XPathUtil;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.DesignSession;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.strategy.CopyForPastePolicy;

/**
 * This policy is a copy policy for pasting, which means, after copying, the
 * original object is deeply cloned, and the target object can be pasted to
 * every where.
 */

public class ContextCopyPastePolicy
{

	/**
	 * Private constructor.
	 */

	private ContextCopyPastePolicy( )
	{
	}

	private final static ContextCopyPastePolicy instance = new ContextCopyPastePolicy( );

	/**
	 * Returns the element with context information such as xpath, element id
	 * and the location of the design module.
	 * 
	 * @param source
	 *            the source element
	 * @param root
	 *            the module of the source element
	 * 
	 * @return the instance of <code>ContextCopiedElement</code> with context
	 *         information.
	 */

	public IElementCopy createCopy( DesignElement source, Module root )
	{
		long id = source.getID( );
		String xpath = XPathUtil.getXPath( source.getHandle( root ) );

		String location = null;
		if ( root != null && root.getSystemId( ) != null )
			location = root.getLocation( );

		DesignElement destination = null;
		try
		{
			destination = (DesignElement) source.doClone( DummyCopyPolicy
					.getInstance( ) );
		}
		catch ( CloneNotSupportedException e )
		{
			assert false;
			return null;
		}

		DesignElement localized = null;

		try
		{
			localized = (DesignElement) source.doClone( CopyForPastePolicy
					.getInstance( ) );
		}
		catch ( CloneNotSupportedException e )
		{
			localized = null;
			assert false;
		}

		ContextCopiedElement retValue = new ContextCopiedElement( destination,
				localized, id, xpath, location );

		return retValue;
	}

	/**
	 * Returns the instance of this class.
	 * 
	 * @return the instance of this class
	 */

	public static ContextCopyPastePolicy getInstance( )
	{
		return instance;
	}

	/**
	 * Checks whether the <code>content</code> can be pasted. And if
	 * localization is needed, localize property values to <code>content</code>.
	 * 
	 * @param context
	 *            the place where the content is to pasted
	 * @param content
	 *            the content
	 * @param module
	 *            the root of the context
	 * @return the element copy that should be added into the context
	 * 
	 */

	public IDesignElement preWorkForPaste( ContainerContext context,
			IElementCopy content, Module module )
	{

		ContextCopiedElement copy = null;

		try
		{
			copy = (ContextCopiedElement) ( (ContextCopiedElement) content )
					.clone( );
		}
		catch ( CloneNotSupportedException e )
		{
			assert false;
			return null;
		}

		String location = copy.getRootLocation( );
		if ( location == null )
			return copy.getLocalizedCopy( );

		DesignElement copiedElement = copy.getCopy( );

		long id = copy.getId( );

		DesignSession session = module.getSession( );
		Module copiedRoot = session.getOpenedModule( location );
		if ( copiedRoot == null )
			return copy.getLocalizedCopy( );

		DesignElement foundElement = copiedRoot.getElementByID( id );
		if ( foundElement == null )
			return copy.getLocalizedCopy( );

		if ( copiedElement.getDefn( ) != foundElement.getDefn( ) )
			return copy.getLocalizedCopy( );

		String originalExtendsName = foundElement.getExtendsName( );
		String copiedExtendsName = copiedElement.getExtendsName( );

		if ( originalExtendsName != null
				&& !originalExtendsName.equalsIgnoreCase( copiedExtendsName ) )
			return copy.getLocalizedCopy( );

		if ( copiedExtendsName != null
				&& !copiedExtendsName.equalsIgnoreCase( originalExtendsName ) )
			return copy.getLocalizedCopy( );

		return copy.getCopy( );
	}

	/**
	 * Checks whether the given copy is valid for pasting. Following cases are
	 * invalid:
	 * 
	 * <ul>
	 * <li>the instance is <code>null</code>.
	 * <li>the instance does not contain the localized copy.
	 * </ul>
	 * 
	 * @param copy
	 *            the given copy
	 * 
	 * @return <code>true</code> is the copy is good for pasting. Otherwise
	 *         <code>false</code>.
	 */

	public boolean isValidCopy( IElementCopy copy )
	{
		if ( !( copy instanceof ContextCopiedElement ) )
			return false;

		ContextCopiedElement copied = (ContextCopiedElement) copy;

		if ( copied.getLocalizedCopy( ) == null )
			return false;

		return true;
	}
}
