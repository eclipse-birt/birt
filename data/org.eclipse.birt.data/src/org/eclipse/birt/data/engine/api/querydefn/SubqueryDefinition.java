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
 * Default implementation of ISubQueryDefn
 * Defines a subquery: a supplemental use of rows returned by a data set
 * or a group. A subquery does not have its own data set, but rather it provides an alternate view
 * of data of an existing group or report query by applying additional transforms on top of such data.
 */

public class SubqueryDefinition extends BaseQueryDefinition implements ISubqueryDefinition
{
	protected String 	name;

	/**
	 * Constructs a SubqueryDefn. A name must be provided that uniquely identifies the subquery
	 * within the report query that contains it. 
	 * @param name
	 */
	public SubqueryDefinition( String name )
	{
		super( null );
		this.name = name;
	}
	
	/**
	 * Gets the name of the subquery
	 * @return Name of the subquery
	 */
	public String getName() 
	{
		return name;
	}
}
