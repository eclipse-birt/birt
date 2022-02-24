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

/**
 * Represents extra operations based on common <code>ICubeQueryDefinition</code>
 * execution result, i.e., <code>IAggregationResultSet[]</code> Currently, only
 * <code>AddNestAggregations</code> is provided
 */
public interface ICubeOperation {

	/**
	 * @return new bindings introduced by cube operation an empty array is returned
	 *         when no new binding is introduced
	 */
	IBinding[] getNewBindings();
}
