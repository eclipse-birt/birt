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
 * Describes a computed column defined for a data set, or a report query.
 * A computed column has a name, and an JavaScript expression used to caculate value of the column.
 */
public interface IComputedColumn
{
    /**
     * Gets the name of the computed column
     */
    public abstract String getName();

    /**
     * Gets the expression of the computed column
     */
    public abstract IBaseExpression getExpression();

	/**
	 * Gets the data type of the computed column.
	 * @return Data type as an integer. 
	 */
	public abstract int getDataType();
}