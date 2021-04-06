/*******************************************************************************
 * Copyright (c) 2004, 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.impl.index;

import org.eclipse.birt.data.engine.core.DataException;

public interface IDataSetIndex {
	public IOrderedIntSet getKeyIndex(Object key, int filterType) throws DataException;

	public boolean supportFilter(int filterType) throws DataException;

	public Object[] getAllKeyValues() throws DataException;

	public IOrderedIntSet getAllKeyRows() throws DataException;
}
