
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.odi;

import java.util.List;
import java.util.Set;

import org.eclipse.birt.data.engine.core.DataException;

/**
 * 
 */

public interface IAggrValueHolder {
	public Set<String> getAggrNames() throws DataException;

	public Object getAggrValue(String aggrName) throws DataException;

	public List getAggrValues(String aggrName) throws DataException;

	public IAggrInfo getAggrInfo(String aggrName) throws DataException;
}
