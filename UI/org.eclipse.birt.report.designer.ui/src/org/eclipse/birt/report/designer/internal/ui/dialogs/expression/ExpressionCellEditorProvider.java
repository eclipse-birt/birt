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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.internal.ui.expressions.ExpressionSupportManager;
import org.eclipse.birt.report.designer.internal.ui.expressions.IExpressionBuilder;
import org.eclipse.birt.report.designer.internal.ui.expressions.IExpressionSupport;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.swt.graphics.Image;

/**
 * ExpressionButtonProvider
 */
public class ExpressionCellEditorProvider implements IExpressionCellEditorProvider {

	private static final String CONSTANT = Messages.getString("ExpressionButtonProvider.Constant"); //$NON-NLS-1$

	private ExpressionCellEditor input;

	private Map<String, IExpressionSupport> supports = new HashMap<String, IExpressionSupport>();
	private String[] supportedTypes;

	public ExpressionCellEditorProvider(boolean allowConstant) {
		List<String> types = new ArrayList<String>();

		if (allowConstant) {
			types.add(ExpressionType.CONSTANT);
		}

		IExpressionSupport[] exts = ExpressionSupportManager.getExpressionSupports();

		if (exts != null) {
			for (IExpressionSupport ex : exts) {
				types.add(ex.getName());
				supports.put(ex.getName(), ex);
			}
		}

		supportedTypes = types.toArray(new String[types.size()]);
	}

	public void setInput(ExpressionCellEditor input) {
		this.input = input;
	}

	public String[] getExpressionTypes() {
		return supportedTypes;
	}

	public Image getImage(String exprType) {
		if (ExpressionType.CONSTANT.equals(exprType)) {
			return ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_ENABLE_EXPRESSION_CONSTANT);
		} else {
			IExpressionSupport spt = supports.get(exprType);

			if (spt != null) {
				return spt.getImage();
			}
		}
		return null;
	}

	public String getText(String exprType) {
		if (ExpressionType.CONSTANT.equals(exprType)) {
			return CONSTANT;
		}

		IExpressionSupport spt = supports.get(exprType);

		if (spt != null) {
			return spt.getDisplayName();
		}

		return ""; //$NON-NLS-1$
	}

	public String getTooltipText(String exprType) {
		return getText(exprType);
	}

	public void handleSelectionEvent(String exprType) {
		IExpressionSupport spt = supports.get(exprType);
		String sOldExpr = input.getExpression();

		if (spt != null) {
			IExpressionBuilder builder = spt.createBuilder(input.getControl().getShell(), null);

			if (builder != null) {
				input.openExpressionBuilder(builder, exprType);
			}
		}
		if (ExpressionType.CONSTANT.equals(exprType)) {
			input.openConstantEditor(exprType);
		}
		input.notifyExpressionChangeEvent(sOldExpr, input.getExpression());
	}

	public IExpressionSupport getExpressionSupport(String exprType) {
		return supports.get(exprType);
	}

}
