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
 * Describes the static design of any data source (connection)
 * to be used by the Data Engine.
 * Each sub-interface defines a specific type of data source. 
 */
public interface IBaseDataSourceDesign
{
    /** 
     * Gets the name of this data source.
     */
    public abstract String getName();

    /**
     * Gets the BeforeOpen script to be called just before opening the data
     * source (connection).
     * @return The BeforeOpen script.  Null if none is defined.
     */
    public abstract String getBeforeOpenScript();
    
    /**
     * Gets the AfterOpen script of the data source.
     * @return	The AfterOpen script.  Null if none is defined.
     */
    public abstract String getAfterOpenScript();

    /**
     * Gets the BeforeClose script to be called just before closing the
     * data source (connection).
     * @return The BeforeClose script.  Null if none is defined.
     */
    public abstract String getBeforeCloseScript();

    /**
     * Gets the AfterClose script of the data source.
     * @return	The AfterClose script.  Null if none is defined.
     */
    public abstract String getAfterCloseScript();

}