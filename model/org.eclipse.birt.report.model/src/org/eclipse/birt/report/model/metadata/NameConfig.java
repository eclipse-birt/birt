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

package org.eclipse.birt.report.model.metadata;

import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;

/**
 * Configuration information for the name management of the element.
 */

public class NameConfig
{

	/**
	 * Indicates the name space in which instances of this element reside.
	 */

	protected String nameSpaceID = MetaDataConstants.NO_NAME_SPACE;

	/**
	 * Definition of the element where the name resides.
	 */
	protected IElementDefn holder = null;

	/**
	 * Name option: one of following defined in MetaDataConstants:
	 * {@link MetaDataConstants#NO_NAME NO_NAME},
	 * {@link MetaDataConstants#OPTIONAL_NAME OPTIONAL_NAME}, or
	 * {@link MetaDataConstants#REQUIRED_NAME REQUIRED_NAME}.
	 */

	protected int nameOption = MetaDataConstants.OPTIONAL_NAME;

	protected IElementPropertyDefn targetProperty = null;

	protected String targetPropertyName = null;

	protected String configString = null;

	/**
		 * 
		 */

	NameConfig( )
	{

	}

	/**
	 * The ID of the name space defined by the name container.
	 * 
	 * @return the name space ID
	 */
	public String getNameSpaceID( )
	{
		return nameSpaceID;
	}

	/**
	 * The definition of the name container. Generally, <code>Module</code> is
	 * the default name container. However, for some special case,
	 * <code>Dimension</code>> can also be the container.
	 * 
	 * @return definition of the name container
	 */
	public IElementDefn getNameContainer( )
	{
		return holder;
	}

	public IElementPropertyDefn getNameProperty( )
	{
		return targetProperty;
	}
}
