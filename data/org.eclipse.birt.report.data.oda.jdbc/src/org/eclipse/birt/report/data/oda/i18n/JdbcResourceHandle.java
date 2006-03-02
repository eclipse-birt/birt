/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.data.oda.i18n;

import com.ibm.icu.util.ULocale;

import org.eclipse.birt.core.i18n.ResourceHandle;

/**
 * Implementation of ResourceHandle in oda-jdbc
 */

public class JdbcResourceHandle extends ResourceHandle
{

	/**
	 * Constructor.
	 * 
	 * @param locale
	 *            the user's locale.
	 *  
	 */
	public JdbcResourceHandle( ULocale locale )
	{
		super( locale );
	}
	
}
