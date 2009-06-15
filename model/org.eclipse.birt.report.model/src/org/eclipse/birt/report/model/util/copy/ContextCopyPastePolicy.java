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
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.api.util.XPathUtil;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.DesignSession;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.strategy.CopyForPastePolicy;
import org.eclipse.birt.report.model.elements.strategy.DummyCopyPolicy;

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
		String xpath = XPathUtil.getXPath( source.getHandle( root ) );

		String extendsName = source.getExtendsName( );

		String libLocation = null;
		long extendsElementID = DesignElement.NO_ID;

		if ( !StringUtil.isBlank( extendsName ) )
		{
			String namespace = StringUtil.extractNamespace( extendsName );
			Library lib = root.getLibraryWithNamespace( namespace );
			if ( lib != null )
				libLocation = lib.getLocation( );

			DesignElement element = source.getExtendsElement( );
			if ( element != null )
				extendsElementID = element.getID( );
		}

		String location = null;
		if ( root != null && root.getSystemId( ) != null )
			location = root.getLocation( );

		DesignElement destination = null;

		if ( extendsElementID != DesignElement.NO_ID )
		{
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
				localized, xpath, location, libLocation, extendsElementID );

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

		DesignSession session = module.getSession( );
		Module copiedRoot = session.getOpenedModule( location );
		if ( copiedRoot == null )
			return copy.getLocalizedCopy( );

		String nameSpace = StringUtil.extractNamespace( copiedElement
				.getExtendsName( ) );

		// if the element is extends, element should be validated whether the
		// localize it or not
		if ( !StringUtil.isEmpty( nameSpace ) )
		{
			Library lib = module.getLibraryWithNamespace( nameSpace );
			if ( lib == null )
				return copy.getLocalizedCopy( );

			long extendsElementID = copy.getExtendsElementID( );
			if ( extendsElementID == DesignElement.NO_ID )
				return copy.getLocalizedCopy( );

			// gets the location of the library which contains the copied
			// extends.
			String libLocation = copy.getLibLocation( );
			if ( libLocation == null )
				return copy.getLocalizedCopy( );

			// validates the location of the library which contains the copied
			// extends is the same as the location of the library of the target
			// container
			if ( !libLocation.equals( lib.getLocation( ) ) )
				return copy.getLocalizedCopy( );

			Library copiedLib = copiedRoot.getLibraryWithNamespace( nameSpace );
			if ( copiedLib == null )
				return copy.getLocalizedCopy( );

			// validates the location of the newly open library is the same as
			// the location of the library which contains the extends element.

			if ( !libLocation.equals( copiedLib.getLocation( ) ) )
				return copy.getLocalizedCopy( );

			DesignElement libElement = lib.getElementByID( extendsElementID );
			if ( libElement == null )
				return copy.getLocalizedCopy( );

			DesignElement copyLibElement = copiedLib
					.getElementByID( extendsElementID );
			if ( libElement.getDefn( ) != copyLibElement.getDefn( ) )
				return copy.getLocalizedCopy( );
		}

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
	 * @param context
	 *            the context of container
	 * @param module
	 *            the module of the element to paste
	 * @param copy
	 *            the given copy
	 * 
	 * @return <code>true</code> is the copy is good for pasting. Otherwise
	 *         <code>false</code>.
	 */

	public boolean isValidCopy( ContainerContext context, Module module,
			IElementCopy copy )
	{
		if ( !( copy instanceof ContextCopiedElement ) )
			return false;

		DesignElement copied = ( (ContextCopiedElement) copy )
				.getLocalizedCopy( );

		if ( copied == null )
			return false;

		return context.canContain( module, copied );
	}

}
