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
package org.eclipse.birt.data.engine.api;

/**
 * Describes a data row filter defined in a data set or a report query. A filter is defined
 * as an expression that returns a Boolean type. The expression normally
 * operates on the "row" Javascript object to apply conditions based on column values of a data row.
 */
public interface IFilterDefinition
{
	/**
	 * Gets the Boolean expression used to define this filter.
	 */
	IBaseExpression getExpression();
}
