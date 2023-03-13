
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
 *
 */

public interface IAggrDefnManager {
	IAggrInfo getAggrDefn(String name) throws DataException;

	IAggrInfo getAggrDefn(int index) throws DataException;

	int getAggrDefnIndex(String name) throws DataException;

	int getAggrCount();
}
