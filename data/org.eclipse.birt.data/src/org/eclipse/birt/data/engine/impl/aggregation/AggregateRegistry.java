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