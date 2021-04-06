/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.dialogs.expression;

import org.eclipse.birt.report.designer.internal.ui.expressions.IExpressionSupport;

/**
 * IExpressionButtonProvider
 */
public interface IExpressionCellEditorProvider {
	public void setInput(ExpressionCellEditor input);

	public String[] getExpressionTypes();

	public void handleSelectionEvent(String exprType);

	public IExpressionSupport getExpressionSupport(String exprType);

	public String getText(String exprType);

	public String getTooltipText(String exprType);
}
