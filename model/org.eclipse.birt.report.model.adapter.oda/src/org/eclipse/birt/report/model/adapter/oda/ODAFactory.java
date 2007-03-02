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

package org.eclipse.birt.report.model.adapter.oda;


/**
 * 
 *
 */

public class ODAFactory
{

	private static IODAFactory factory = null;

	/**
	 * @param inFactory
	 */
	
	public static synchronized void setODAFactory( IODAFactory inFactory )
	{
		factory = inFactory;
	}

	/**
	 * @return the factory
	 */
	
	public static IODAFactory getFactory( )
	{
		return factory;
	}

}
