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

package org.eclipse.birt.data.engine.script;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.DataSetRuntime;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * Underlying implementation of the Javascript "rows" object. The ROM scripts
 * use this JS object to access the array of row object
 */
public class JSRows extends ScriptableObject {
	/**
	 * Array of nested data sets; element[0] is data set for outermost query; last
	 * element is current query's data set
	 */
	private DataSetRuntime[] dataSets;

	private static Logger logger = Logger.getLogger(JSRows.class.getName());
	private static final long serialVersionUID = -6381733586388272803L;

	/*
	 * return the Class Name
	 * 
	 * @see org.mozilla.javascript.Scriptable#getClassName()
	 */
	public String getClassName() {
		return "DataRows";
	}

	/**
	 * Construct the rows object from the outer query result and the row object of
	 * the current result.
	 * 
	 * @param outerResults  the outer query result
	 * @param currentRowObj the row object of the cuurent result
	 * @throws DataException
	 */
	public JSRows(DataSetRuntime[] dataSets) throws DataException {
		logger.entering(JSRows.class.getName(), "JSRows");
		this.dataSets = dataSets;
	}

	/**
	 * Gets an indexed Row Object
	 */
	public Object get(int index, Scriptable start) {
		logger.entering(JSColumnDefn.class.getName(), "get", Integer.valueOf(index));
		if (index >= 0 && index < dataSets.length) {
			return dataSets[index].getJSRowObject();
		} else {
			logger.exiting(JSColumnDefn.class.getName(), "get", null);
			return null;
		}
	}

	/**
	 * Checks if an row Object exists
	 */
	public boolean has(int index, Scriptable start) {
		if (logger.isLoggable(Level.FINER))
			logger.entering(JSColumnDefn.class.getName(), "has", Integer.valueOf(index));
		return (index >= 0 && dataSets.length > index) ? true : false;
	}

}