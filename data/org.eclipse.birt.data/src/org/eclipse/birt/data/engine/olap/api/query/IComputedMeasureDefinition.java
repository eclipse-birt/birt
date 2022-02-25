
/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * This interface defines the API for computed measure.
 */

public interface IComputedMeasureDefinition extends IMeasureDefinition {
	/**
	 * Return the expression of computed measure.
	 *
	 * @return
	 * @throws DataException
	 */
	IBaseExpression getExpression() throws DataException;

}
