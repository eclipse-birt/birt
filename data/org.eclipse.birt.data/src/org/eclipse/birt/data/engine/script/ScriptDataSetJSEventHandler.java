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

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IScriptDataSetDesign;
import org.eclipse.birt.data.engine.api.script.IDataRow;
import org.eclipse.birt.data.engine.api.script.IDataSetInstance;
import org.eclipse.birt.data.engine.api.script.IScriptDataSetColumnMetaData;
import org.eclipse.birt.data.engine.api.script.IScriptDataSetEventHandler;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;

/**
 * This class handles script data set events by executing the Javascript
 * event code.
 * NOTE: functionality of this class will be moved to Engine. This class
 * is temporary 
 */

public class ScriptDataSetJSEventHandler extends DataSetJSEventHandler implements
		IScriptDataSetEventHandler
{
	public ScriptDataSetJSEventHandler( IScriptDataSetDesign design )
	{
		super(design);
	}
	protected IScriptDataSetDesign getScriptDataSetDesign()
	{
		return (IScriptDataSetDesign) getBaseDesign();
	}
	
	public void handleOpen(IDataSetInstance dataSet) throws BirtException
	{
		String script = getScriptDataSetDesign().getOpenScript();
		if ( script != null && script.length() > 0 )
		{
			getRunner( dataSet.getScriptScope() ).runScript(
					"open", script );
		}
	}

	public void handleClose(IDataSetInstance dataSet) throws BirtException
	{
		String script = getScriptDataSetDesign().getCloseScript();
		if ( script != null && script.length() > 0 )
		{
			getRunner( dataSet.getScriptScope() ).runScript(
					"close", script );
		}
	}

	public boolean handleFetch(IDataSetInstance dataSet, IDataRow row) throws BirtException
	{
		String script = getScriptDataSetDesign().getFetchScript();
		if ( script != null && script.length() > 0 )
		{
			Object result = getRunner( dataSet.getScriptScope() ).runScript(
					"fetch", script );

			if ( result instanceof Boolean )
				return ((Boolean) result).booleanValue();
			else
				throw new DataException( ResourceConstants.BAD_FETCH_RETURN_TYPE );
		}
		return false;
	}

	public IScriptDataSetColumnMetaData[] handleDescribe(IDataSetInstance dataSet) throws BirtException
	{
		// TODO: not implemented yet
		return null;
	}

}
