/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.impl.query;

public class CubeQueryExecutorHints
{
	private boolean executeNestedAggregation = true;
	private boolean needSaveToDoc = true;
	private boolean executeDrillOperation = true;
	
	public boolean canExecuteCubeOperation( )
	{
		return this.executeNestedAggregation;
	}

	public void executeCubeOperation( boolean canExecuteCubeOperation )
	{
		this.executeNestedAggregation = canExecuteCubeOperation;
	}
	
	public boolean canExecuteDrillOperation( )
	{
		return this.executeDrillOperation;
	}

	public void executeDrillOperation( boolean canExecuteDrillOperation )
	{
		this.executeDrillOperation = canExecuteDrillOperation;
	}

	public void needSaveToDoc( boolean needSaveToDoc )
	{
		this.needSaveToDoc  = needSaveToDoc;
	}	

	public boolean saveToDoc( )
	{
		return this.needSaveToDoc;
	}
}
