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
 * Common interface for defining input and output parameters. A parameter has an ID (either by name or by 
 * 1-based index), and a data type
 */
public interface IParameterDefinition
{
    /**
     * Returns the parameter name. 
     * 
     * @return the name of the parameter. Null if parameter is identified by index.
     */
    public abstract String getName();

    /**
     * Returns the parameter position. Parameter positions start from 1.
     * 
     * @return the parameter position. -1 if parameter is identified by name.
     */
    public abstract int getPosition();

    /**
     * Returns the parameter data type. See the DataType class for return value constants.
     * 
     * @return the parameter data type
     */
    public abstract int getType();
}