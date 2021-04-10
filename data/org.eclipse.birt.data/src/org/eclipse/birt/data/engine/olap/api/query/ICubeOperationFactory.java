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
