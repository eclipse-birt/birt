/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.model.api.simpleapi;

import java.util.Iterator;

import org.eclipse.birt.report.model.api.ExpressionType;

/**
 *
 */

public interface IExpressionType {

	/**
	 *
	 */

	String CONSTANT = ExpressionType.CONSTANT;

	/**
	 *
	 */

	String JAVASCRIPT = ExpressionType.JAVASCRIPT;

	/**
	 * Gets possible types for the expression.
	 *
	 * @return the iterator
	 */

	Iterator<String> iterator();
}
