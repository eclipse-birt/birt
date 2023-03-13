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

import org.eclipse.birt.report.designer.internal.ui.expressions.IExpressionSupport;
import org.eclipse.swt.graphics.Image;

/**
 * IExpressionButtonProvider
 */
public interface IExpressionButtonProvider {

	void setInput(ExpressionButton input);

	String[] getExpressionTypes();

	Image getImage(String exprType);

	String getText(String exprType);

	String getTooltipText(String exprType);

	void handleSelectionEvent(String exprType);

	IExpressionSupport getExpressionSupport(String exprType);
}
