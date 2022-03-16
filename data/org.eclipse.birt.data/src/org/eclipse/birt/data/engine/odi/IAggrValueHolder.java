
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
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
	Set<String> getAggrNames() throws DataException;

	Object getAggrValue(String aggrName) throws DataException;

	List getAggrValues(String aggrName) throws DataException;

	IAggrInfo getAggrInfo(String aggrName) throws DataException;
}
