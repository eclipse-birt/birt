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

import org.eclipse.birt.data.engine.core.DataException;

/**
 * The interface defines the behavior of class that is used to fetch a row fromt
 * data set.
 */
public interface IDataSetPopulator {
	/**
	 * Return the IResultObject instance of a data set.
	 *
	 * @return
	 * @throws DataException
	 */
	IResultObject next() throws DataException;
}
