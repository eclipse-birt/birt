
/*******************************************************************************
 * Copyright (c) 2004, 2012 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.storage;

import it.uniroma3.mat.extendedset.intset.IntSet;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.document.viewing.IDataSetResultSet;

/**
 * 
 */

public interface IDataSetReader
{
	public IDataSetResultSet load( IntSet targetRows ) throws DataException;
	
	public void close( ) throws DataException;
}
