
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
package org.eclipse.birt.data.engine.impl.document.viewing;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odi.IDataSetPopulator;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * 
 */

public interface IDataSetResultSet extends IDataSetPopulator {
	public IResultObject getResultObject();

	public int getRowCount();

	public void skipTo(int index) throws DataException;

	public IResultClass getResultClass();

	public void close() throws DataException;
}
