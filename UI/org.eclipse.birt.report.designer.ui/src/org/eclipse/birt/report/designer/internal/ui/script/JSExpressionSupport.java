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

package org.eclipse.birt.report.designer.internal.ui.script;

import org.eclipse.birt.report.designer.internal.ui.expressions.AbstractExpressionSupport;
import org.eclipse.birt.report.designer.internal.ui.expressions.IExpressionBuilder;
import org.eclipse.birt.report.designer.internal.ui.expressions.IExpressionConverter;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

/**
 * JSExpressionSupport
 */
public class JSExpressionSupport extends AbstractExpressionSupport {

	public IExpressionBuilder createBuilder(Shell shl, Object expression) {
		String expr = expression == null ? null : expression.toString();

		return new JSExpressionBuilder(shl, expr);
	}

	public IExpressionBuilder createBuilder(Shell shl, Object expression, boolean showLeafOnlyInThirdColumn) {
		String expr = expression == null ? null : expression.toString();
		JSExpressionBuilder builder = new JSExpressionBuilder(shl, expr);
		builder.setShowLeafOnlyInThirdColumn(showLeafOnlyInThirdColumn);
		return builder;
	}

	public Image getImage() {
		return ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_ENABLE_EXPRESSION_JAVASCRIPT);
	}

	public String getDisplayName() {
		return Messages.getString("ExpressionButtonProvider.Javascript"); //$NON-NLS-1$
	}

	public String getName() {
		return ExpressionType.JAVASCRIPT;
	}

	/**
	 * The stateless singleton renderer instance
	 */
	private static final JSExpressionConverter JS_EXPR_RENDERER = new JSExpressionConverter();

	public IExpressionConverter getConverter() {
		return JS_EXPR_RENDERER;
	}

}
