/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.engine.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IParameterDefnBase;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IScalarParameterDefn;
import org.eclipse.birt.report.engine.api.ReportEngine;
import org.eclipse.birt.report.engine.executor.ExecutionContext;

/**
 * Defines an engine task that handles parameter definition retrieval
 */
public class GetParameterDefinitionTask extends EngineTask implements IGetParameterDefinitionTask
{
	// Stores default values for all parameters
	protected HashMap 			defaultValues = new HashMap();
	
	// stores all parameter definitions. Each task clones the parameter definition information
	// so that Engine IR (repor runnable) can keep a task-independent of the parameter definitions.
	protected Collection 		params = null;
	
	// Exeution context
	protected ExecutionContext 	context;

	/**
	 * @param engine reference to the report engine
	 * @param runnable the runnable report design
	 */
	public GetParameterDefinitionTask(ReportEngine engine, IReportRunnable runnable)
	{
		super(engine, runnable);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IGetParameterDefinitionTask#getParameterDefns(boolean)
	 */
	public Collection getParameterDefns(boolean includeParameterGroups)
	{
		Collection original = ((ReportRunnable)runnable).getParameterDefns(includeParameterGroups);
		Iterator iter = original.iterator();
		
		// Clone parameter definitions, fill in locale and report dsign information
		params = new ArrayList();
		
		while (iter.hasNext())
		{
			ParameterDefnBase pBase = (ParameterDefnBase) iter.next();
			try
			{
				params.add(pBase.clone());
			}
			catch (CloneNotSupportedException e)
			{
				log.log(Level.SEVERE, e.getMessage(), e);
			}
		}
		
		if (params != null)
		{
			iter = params.iterator();
			while (iter.hasNext())
			{
				IParameterDefnBase pBase = (IParameterDefnBase) iter.next();
				if (pBase instanceof ScalarParameterDefn) 
				{
					((ScalarParameterDefn)pBase).setReportDesign(runnable.getDesignHandle().getDesign());
					((ScalarParameterDefn)pBase).setLocale(locale);
					((ScalarParameterDefn)pBase).evaluateSelectionList();
				}
				else if (pBase instanceof ParameterGroupDefn)
				{
					Iterator iter2 = ((ParameterGroupDefn) pBase).getContents().iterator();
					while (iter2.hasNext())
					{
						IParameterDefnBase p = (IParameterDefnBase) iter2.next();
						if (p instanceof ScalarParameterDefn) 
						{
							((ScalarParameterDefn)p).setReportDesign(runnable.getDesignHandle().getDesign());
							((ScalarParameterDefn)p).setLocale(locale);	
							((ScalarParameterDefn)p).evaluateSelectionList();
						}
					}
				}
			}
		}
		return params;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IGetParameterDefinitionTask#evaluateDefaults()
	 */
	public void evaluateDefaults() throws EngineException
	{
		//evaluate the default value
		if (params == null)
			params = getParameterDefns(false);
		
		if (params != null && params.size() > 0)
		{
			defaultValues.putAll(evaluateDefaults(params));
		}
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IGetParameterDefinitionTask#setValue(java.lang.String,
	 *      java.lang.Object)
	 */
	public void setValue(String name, Object value)
	{
		defaultValues.put(name, value);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IGetParameterDefinitionTask#getParameterValues()
	 */
	public HashMap getParameterValues()
	{
		return defaultValues;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IGetParameterDefinitionTask#getDefaultValue(org.eclipse.birt.report.engine.api2.IParameterDefnBase)
	 */
	public Object getDefaultValue(IParameterDefnBase param)
	{
		// For now, only supports scalar parameters
		if (param instanceof ScalarParameterDefn)
			return defaultValues.get(param.getName());
		return null;
	}
}

