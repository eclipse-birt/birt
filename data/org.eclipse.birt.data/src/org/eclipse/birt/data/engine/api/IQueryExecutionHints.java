/*
 *************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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

import java.util.List;

/**
 * This class provides hints info for a Data Engine Query to execution.
 * 
 *
 */
public interface IQueryExecutionHints
{
	/**
	 * Indicate whether should we do sort before grouping.
	 * 
	 * @return
	 */
	public boolean doSortBeforeGrouping();
	
	/**
     * Return a list of IGroupInstanceInfo instances that is needed in target
     * ResultSet.
     * 
     * @return
     */
    public List<IGroupInstanceInfo> getTargetGroupInstances( );


}
