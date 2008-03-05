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

import org.eclipse.birt.report.model.api.util.IElementCopy;
import org.eclipse.birt.report.model.core.DesignElement;

/**
 * 
 */

class ContextCopiedElement implements IElementCopy
{

	private final DesignElement copy;
	private final DesignElement localizedCopy;

	private final long id;
	private final String rootLocation;
	private final String xpath;

	/**
	 * Default constructor.
	 * 
	 * @param element
	 *            the element
	 * @param localizedElement
	 *            the localized element
	 * @param id
	 *            the element id
	 * @param xpath
	 *            the xpath of the element
	 * @param rootLocation
	 *            the location of the corresponding module
	 * 
	 */

	ContextCopiedElement( DesignElement element,
			DesignElement localizedElement, long id, String xpath,
			String rootLocation )
	{
		this.copy = element;
		this.localizedCopy = localizedElement;
		this.id = id;
		this.rootLocation = rootLocation;
		this.xpath = xpath;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */

	public Object clone( ) throws CloneNotSupportedException
	{
		DesignElement newCopy = (DesignElement) copy.doClone( DummyCopyPolicy
				.getInstance( ) );

		DesignElement newLocalized = (DesignElement) localizedCopy
				.doClone( DummyCopyPolicy.getInstance( ) );

		ContextCopiedElement retValue = new ContextCopiedElement( newCopy,
				newLocalized, id, xpath, rootLocation );

		return retValue;
	}

	/**
	 * Returns the copied element.
	 * 
	 * @return the copied
	 */

	DesignElement getCopy( )
	{
		return copy;
	}

	/**
	 * Returns the corresponding element id.
	 * 
	 * @return the id
	 */

	long getId( )
	{
		return id;
	}

	/**
	 * Returns the location of the corresponding module.
	 * 
	 * @return the rootLocation
	 */

	String getRootLocation( )
	{
		return rootLocation;
	}

	/**
	 * Returns the localized element of which the extends value is null.
	 * 
	 * @return the localized element 
	 */

	DesignElement getLocalizedCopy( )
	{
		return localizedCopy;
	}
}
