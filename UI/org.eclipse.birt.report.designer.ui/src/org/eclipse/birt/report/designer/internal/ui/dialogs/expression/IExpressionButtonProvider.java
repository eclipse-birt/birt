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
import org.eclipse.swt.graphics.Image;

/**
 * IExpressionButtonProvider
 */
public interface IExpressionButtonProvider {

	public void setInput(ExpressionButton input);

	public String[] getExpressionTypes();

	public Image getImage(String exprType);

	public String getText(String exprType);

	public String getTooltipText(String exprType);

	public void handleSelectionEvent(String exprType);

	public IExpressionSupport getExpressionSupport(String exprType);
}
