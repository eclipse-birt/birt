/*
 *************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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
package org.eclipse.birt.data.engine.script;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.DataSetRuntime;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.eclipse.birt.data.engine.odi.IResultObjectEvent;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 * Class to implement an event sink for dataSet.onFetch event
 */
public class OnFetchScriptHelper implements IResultObjectEvent
{
	// scope which is used to evaluate computed column value
	private Scriptable scope;

	// row object which will bind with row script object
	private JSRowObject rowObject;
	
	private String script;
	
	public OnFetchScriptHelper( DataSetRuntime dataSet )
	{
		this.scope = dataSet.getScriptable();
		this.rowObject = (JSRowObject) dataSet.getJSRowObject();
		this.script = dataSet.getOnFetchScript();
	}
	
	/**
	 * @see org.eclipse.birt.data.engine.odi.IResultObjectEvent#process(org.eclipse.birt.data.engine.odi.IResultObject)
	 */
	public boolean process(IResultObject resultObject, int rowIndex) throws DataException
	{
		// bind new object to row script object
		rowObject.setRowObject( resultObject, true );
		rowObject.setCurrentRowIndex( rowIndex );

		// now assign the computed value to each of its projected computed
		// columns
		Context cx = Context.enter( );
		try
		{
			ScriptEvalUtil.evaluateJSAsMethod( cx, scope, script, "dataSet.onFetch", 0 );
		}
		finally
		{
			Context.exit( );
		}
		return true;
	}
}
