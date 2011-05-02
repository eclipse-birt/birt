/*
 *************************************************************************
 * Copyright (c) 2004, 2011 Actuate Corporation.
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

package org.eclipse.birt.data.engine.executor.transform;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.aggregation.ProgressiveAggregationHelper;
import org.eclipse.birt.data.engine.impl.document.stream.StreamManager;
import org.eclipse.birt.data.engine.odi.IResultObject;


public interface IGroupCalculator
{
	public void registerPreviousResultObject( IResultObject previous );
	public void registerCurrentResultObject( IResultObject current );
	public void registerNextResultObject( IResultObject next );
	public void next( int rowId ) throws DataException;
	public int getStartingGroup( ) throws DataException;
	public int getEndingGroup( ) throws DataException;
	public void close( ) throws DataException;
	public void doSave( StreamManager manager ) throws DataException;
	public void setAggrHelper( ProgressiveAggregationHelper aggrHelper ) throws DataException;
	public boolean isAggrAtIndexAvailable( String aggrName, int currentIndex ) throws DataException;
}
