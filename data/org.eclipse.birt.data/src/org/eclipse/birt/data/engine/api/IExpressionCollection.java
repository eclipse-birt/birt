/*******************************************************************************
 * Copyright (c) 2004,2007 Actuate Corporation.
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
package org.eclipse.birt.data.engine.api;

import java.util.Collection;

/**
 * Describes an Array Collection of IBaseExpression, it will return an result
 * Array.
 *
 */
public interface IExpressionCollection extends IBaseExpression {

	/**
	 * Gets the expression collection.
	 */
	Collection<IBaseExpression> getExpressions();
}
