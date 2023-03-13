/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.expressions;

import org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider;

/**
 * This interface provides context specific expression provider support
 *
 * @since 2.3.0
 */
public interface IContextExpressionProvider {

	/**
	 * Returns the expression provider according to given context name
	 *
	 * @param contextName
	 * @return
	 */
	IExpressionProvider getExpressionProvider(String contextName);

	/**
	 * Returns the expression filters which will be applied to the given context
	 *
	 * @param contextName
	 * @return
	 *
	 * @since 2.5
	 */
	ExpressionFilter getExpressionFilter(String contextName);
}
