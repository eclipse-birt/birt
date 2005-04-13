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

package org.eclipse.birt.report.model.extension;

import java.util.Locale;

import org.eclipse.birt.report.model.api.extension.IMessages;
import org.eclipse.birt.report.model.i18n.ThreadResources;

/**
 * Represents the default implementation for <code>IMessages</code>. This
 * implementation takes the instance of <code>ThreadResources</code>.
 */

public class DefaultMessages implements IMessages
{

	private ThreadResources resources;

	/**
	 * Constructor with thread resources, which specified by the class loader
	 * and base name of resource bundle.
	 * 
	 * @param resources
	 *            thread resources instance
	 */

	public DefaultMessages( ThreadResources resources )
	{
		this.resources = resources;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.IMessages#getMessage(java.lang.String,
	 *      java.util.Locale)
	 */
	
	public String getMessage( String key, Locale locale )
	{
		return resources.getMessage( key );
	}

}
