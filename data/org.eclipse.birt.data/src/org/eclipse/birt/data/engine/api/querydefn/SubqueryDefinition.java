/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */ 
package org.eclipse.birt.data.engine.api.querydefn;

import org.eclipse.birt.data.engine.api.ISubqueryDefinition;


/**
 * Default implementation of the {@link org.eclipse.birt.data.engine.api.ISubqueryDefinition} interface.
 */

public class SubqueryDefinition extends BaseQueryDefinition implements ISubqueryDefinition
{
	private String name;
	private boolean onGroup;

	/**
	 * Constructs a SubqueryDefn. A name must be provided that uniquely
	 * identifies the subquery within the report query that contains it.
	 * 
	 * @param name
	 */
	public SubqueryDefinition( String name )
	{
		super( null );
		this.name = name;
		this.onGroup = true;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.api.ISubqueryDefinition#getName()
	 */
	public String getName() 
	{
		return name;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.api.ISubqueryDefinition#onGroup()
	 */
	public boolean applyOnGroup( )
	{
		return this.onGroup;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.api.ISubqueryDefinition#onGroup()
	 */
	public void setApplyOnGroup( boolean onGroup )
	{
		this.onGroup = onGroup;
	}
	
}
