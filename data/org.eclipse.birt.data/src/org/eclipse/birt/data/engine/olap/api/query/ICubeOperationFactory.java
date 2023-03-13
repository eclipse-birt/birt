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

package org.eclipse.birt.data.engine.olap.api.query;

import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * Cube Operation Factory
 */
public interface ICubeOperationFactory {

	/**
	 * @param nestAggregations: added nest aggregations
	 * @return
	 * @throws DataException
	 */
	ICubeOperation createAddingNestAggregationsOperation(IBinding[] nestAggregations) throws DataException;
}
