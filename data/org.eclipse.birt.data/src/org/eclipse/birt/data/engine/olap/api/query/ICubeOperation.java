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
