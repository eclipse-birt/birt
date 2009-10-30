
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
package org.eclipse.birt.data.engine.olap.impl.query;

import org.eclipse.birt.data.engine.olap.api.query.IMeasureDefinition;
import org.eclipse.birt.data.engine.olap.api.query.NamedObject;

/**
 * 
 */

public class MeasureDefinition extends NamedObject implements IMeasureDefinition
{
	private String aggrFunction;
	
	/**
	 * 
	 * @param name
	 */
	public MeasureDefinition( String name )
	{
		super( name );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.api.query.IMeasureDefinition#setAggrFunction(java.lang.String)
	 */
	public void setAggrFunction( String name )
	{
		this.aggrFunction = name;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.api.query.IMeasureDefinition#getAggrFunction()
	 */
	public String getAggrFunction( )
	{
		return this.aggrFunction;
	}

}
