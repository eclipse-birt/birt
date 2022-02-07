/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.dialogs.expression;

import org.eclipse.birt.report.designer.internal.ui.expressions.IExpressionContextFactory;

/**
 * IExpressionHelper
 */
public interface IExpressionHelper {

	public String getExpression();

	public void setExpression(String expression);

	public String getExpressionType();

	public void setExpressionType(String exprType);

	public void notifyExpressionChangeEvent(String oldExpression, String newExpression);

	public IExpressionContextFactory getExpressionContextFactory();

	Object getContextObject();
}
