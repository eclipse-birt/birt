/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.api.query;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.core.DataException;

public interface IDerivedMeasureDefinition extends IMeasureDefinition {
	/**
	 * Return the expression of the derived measure.
	 * 
	 * @return
	 * @throws DataException
	 */
	IBaseExpression getExpression() throws DataException;

}
