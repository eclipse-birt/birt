/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.data.oda.jdbc.ui.util;

import java.io.Serializable;

/**
 * TODO: Please document
 * 
 * @version $Revision: #2 $ $Date: 2005/02/05 $
 */

public class Column implements Serializable
{

	private String name = null;

	/**
	 *  
	 */
	public Column( )
	{
		super( );
	}

	/**
	 * @return Returns the name.
	 */
	public String getName( )
	{
		return name;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName( String name )
	{
		this.name = name;
	}
}