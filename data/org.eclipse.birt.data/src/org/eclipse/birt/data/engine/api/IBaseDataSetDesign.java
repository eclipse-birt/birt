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

import java.util.Collection;
import java.util.List;

/**
 * Describes the static design of any data set to be used by 
 * the Data Engine.
 * Each sub-interface defines a specific type of data set. 
 */
public interface IBaseDataSetDesign
{
    /**
     * Gets the name of the data set.
     * @return Name of data set.
     */
    public abstract String getName();

    /**
     * Returns the data source (connection) name for this data set. 
     * 
     * @return Name of the data source (connection) for this data set.
     */
    public abstract String getDataSourceName();

    /**
     * Returns a list of computed columns. Contains
     * IComputedColumn objects. Computed columns must be computed before
     * applying filters.
     * @return the computed columns.  An empty list if none is defined.
     */
    public abstract List getComputedColumns();

    /**
     * Returns a list of filters. The List contains IFilterDefn objects. The data set should discard any
     * row that does not satisfy all the filters.
     * @return the filters. An empty list if none is defined.
     */
    public abstract List getFilters();

    /**
     * Returns the input parameter definitions as a list
     * of IInputParamDefn objects. 
     * @return the input parameter definitions. 
     * 			An empty list if none is defined.
     */
    public abstract List getInputParameters();

    /**
     * Returns the output parameter definitions as a list
     * of IOutputParamDefn objects.
     * @return the output parameter definitions. 
     * 			An empty list if none is defined.
     */
    public abstract List getOutputParameters();

    /**
     * Returns the primary result set hints as a list of IColumnDefn
     * objects. 
     * @return the result set hints as a list of IColumnDefn objects.
     * 			An empty list if none is defined, which normally
     * 			means that the data set can provide the definition 
     * 			from the underlying data access provider.
     */
    public abstract List getResultSetHints();
	
	/**
	 * Returns the set of input parameter bindings as an unordered collection
	 * of IInputParamBinding objects.
	 * @return the input parameter bindings. 
	 * 			An empty collection if none is defined.
	 */
	public abstract Collection getInputParamBindings( );

    /**
     * Returns the BeforeOpen script to be called just before opening the data
     * set.
     * @return the BeforeOpen script. Null if none is defined.
     */
    public abstract String getBeforeOpenScript();

    /**
     * Returns the AfterOpen script to be called just after the data set is
     * opened, but before fetching each row.
     * @return the AfterOpen script.  Null if none is defined.
     */
    public abstract String getAfterOpenScript();

    /**
     * Returns the OnFetch script to be called just after the a row is read
     * from the data set. Called after setting computed columns and only for
     * rows that pass the filters. (Not called for rows that are filtered out
     * of the data set.)
     * @return the OnFetch script. Null if none is defined.
     */
    public abstract String getOnFetchScript();

    /**
     * Returns the BeforeClose script to be called just before closing the
     * data set.
     * @return the BeforeClose script.  Null if none is defined.
     */
    public abstract String getBeforeCloseScript();

    /**
     * Returns the AfterClose script to be called just after the data set is
     * closed.
     * @return the AfterClose script.  Null if none is defined.
     */
    public abstract String getAfterCloseScript();

}