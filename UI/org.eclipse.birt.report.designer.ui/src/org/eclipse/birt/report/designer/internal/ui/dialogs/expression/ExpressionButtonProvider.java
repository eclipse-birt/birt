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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.internal.ui.expressions.ExpressionSupportManager;
import org.eclipse.birt.report.designer.internal.ui.expressions.IExpressionBuilder;
import org.eclipse.birt.report.designer.internal.ui.expressions.IExpressionSupport;
import org.eclipse.birt.report.designer.internal.ui.script.JSExpressionSupport;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;

/**
 * ExpressionButtonProvider
 */
public class ExpressionButtonProvider implements IExpressionButtonProvider {

	private static final String CONSTANT = Messages.getString("ExpressionButtonProvider.Constant"); //$NON-NLS-1$

	private ExpressionButton input;

	private Map<String, IExpressionSupport> supports = new HashMap<>();
	private String[] supportedTypes;
	private boolean showLeafOnlyInThirdColumn = false;

	public ExpressionButtonProvider(boolean allowConstant) {
		List<String> types = new ArrayList<>();

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

	@Override
	public void setInput(ExpressionButton input) {
		this.input = input;
	}

	@Override
	public String[] getExpressionTypes() {
		return supportedTypes;
	}

	@Override
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

	@Override
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

	@Override
	public String getTooltipText(String exprType) {
		return getText(exprType);
	}

	@Override
	public void handleSelectionEvent(String exprType) {
		IExpressionSupport spt = supports.get(exprType);
		String sOldExpr = input.getExpression();

		if (spt != null) {
			IExpressionBuilder builder = null;
			if (spt instanceof JSExpressionSupport) {
				builder = ((JSExpressionSupport) spt).createBuilder(input.getControl().getShell(), null,
						showLeafOnlyInThirdColumn);
			} else {
				builder = spt.createBuilder(input.getControl().getShell(), null);
			}
			if (builder != null) {
				if (Window.OK == input.openExpressionBuilder(builder, exprType)) {
					input.notifyExpressionChangeEvent(sOldExpr, input.getExpression());
				}
			} else {
				input.setExpressionType(exprType);
				input.notifyExpressionChangeEvent(sOldExpr, input.getExpression());
			}
		} else {
			input.setExpressionType(exprType);
			input.notifyExpressionChangeEvent(sOldExpr, input.getExpression());
		}

	}

	@Override
	public IExpressionSupport getExpressionSupport(String exprType) {
		return supports.get(exprType);
	}

	public void setShowLeafOnlyInThirdColumn(boolean leafOnly) {
		showLeafOnlyInThirdColumn = leafOnly;
	}

}
