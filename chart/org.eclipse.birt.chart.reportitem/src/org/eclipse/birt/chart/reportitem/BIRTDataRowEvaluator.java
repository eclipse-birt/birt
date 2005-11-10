package org.eclipse.birt.chart.reportitem;

/***********************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.report.engine.extension.IRowSet;

public class BIRTDataRowEvaluator implements IDataRowExpressionEvaluator
{

	private IRowSet set;

	private HashMap map;

	public BIRTDataRowEvaluator( IRowSet set, IBaseQueryDefinition definition )
	{
		this.set = set;
		this.map = new HashMap();
		for( Iterator iter = definition.getRowExpressions().iterator(); iter.hasNext();  )
		{
			IScriptExpression exp = (IScriptExpression)iter.next();
			map.put(exp.getText(), exp);
			
		}
			
	}
	public Object evaluate( String expression )
	{
		return set.evaluate((IBaseExpression)map.get(expression));
	}

	public boolean next( )
	{
		return set.next();
	}

	public void close( )
	{
		set.close();
	}
	public void first( )
	{
		// not needed here		
	}

}
