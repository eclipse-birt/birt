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

package org.eclipse.birt.data.engine.impl.aggregation;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.expression.AggregateExpression;

/**
 * Interface to define an aggregate registry to be provided by the compiler's
 * consumer. The compiler calls the registry to register each aggregate function
 * that it encounters in the compiled expression.
 */
public interface AggregateRegistry {

	/**
	 * Registers an aggregate function that appears in a compiled expression.
	 * Returns an integer handle for the registered aggregate function
	 */
	int register(AggregateExpression aggregationExpr) throws DataException;

}
