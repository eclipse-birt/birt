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

import org.eclipse.birt.data.engine.core.DataException;
import org.mozilla.javascript.Scriptable;

/**
 * A prepared report query ready for execution. An instance of this class is compiled from the static
 * definition of an IReportQueryDefn object. 
 */
public interface IPreparedQuery
{
    /**
     * Returns the same IReportQueryDefn specified in prepare, without 
     * any changes.
     * A convenience method for the API consumer. 
     * @return	The IReportQueryDefn object used in prepare phase 
     * 			to produce this.  
     */
    public IReportQueryDefn getReportQueryDefn();

    /**
     * Executes the prepared execution plan.  This returns
     * a IQueryResult object at a state ready to return its 
     * current result iterator, or evaluate an aggregate expression.
     * <p>
     * The caller should create a separate Javascript scope, which uses the
     * data engine's shared scope as its prototype, and pass that scope as 
     * a parameter to this method. The Data Engine is responsible for setting
     * up necessary Javascript objects to facilitate evaluation of data related
     * expressions (e.g., those that uses the Javascript "row" object).
     * @return The QueryResults object opened and ready to return
     * 		the results of a report query. 
     * @param queryScope The Javascript scope for evaluating query's script expressions. 
     *      This is expected to be a top-level scope with the Data Engine's global scope
     *      at its top prototype chain. The factory should pass in the ElementState object
     *      that it maintains for the report item using this query.
     */
    public IQueryResults execute( Scriptable queryScope) 
    			throws DataException;

    /**
     * Executes the prepared execution plan as an inner query 
     * that appears within the scope of another report query. 
     * The outer query must have been prepared and executed, and 
     * its results given as a parameter to this method.
     * @param outerResults	QueryResults for the executed outer query
     * @param queryScope Javascript defined for this runtime instance of report query.
     * @return The QueryResults object opened and ready to return
     * 		the results of a report query. 
     */
    public IQueryResults execute( IQueryResults outerResults, Scriptable queryScope ) 
    			throws DataException;
}