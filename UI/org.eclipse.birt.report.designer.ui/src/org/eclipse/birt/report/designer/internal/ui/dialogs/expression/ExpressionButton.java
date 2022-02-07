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

import java.util.Arrays;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.expressions.IExpressionBuilder;
import org.eclipse.birt.report.designer.internal.ui.expressions.IExpressionSupport;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.MenuButton;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Widget;

/**
 * ExpressionButton
 */
public class ExpressionButton {

	private MenuButton button;

	private IExpressionHelper helper;

	private IExpressionButtonProvider provider;

	private Menu menu;

	private SelectionAdapter listener = new SelectionAdapter() {

		public void widgetSelected(SelectionEvent e) {
			Widget widget = e.widget;
			if (widget instanceof MenuItem) {
				String exprType = (String) widget.getData();
				provider.handleSelectionEvent(exprType);
				refresh();
			} else if (widget instanceof MenuButton) {
				provider.handleSelectionEvent(getExpressionType());
			}
		}

	};

	public ExpressionButton(Composite parent, int style, boolean allowConstant) {
		button = new MenuButton(parent, style);
		button.addSelectionListener(listener);

		menu = new Menu(parent.getShell(), SWT.POP_UP);
		button.setDropDownMenu(menu);

		setExpressionButtonProvider(new ExpressionButtonProvider(allowConstant));
		refresh();
	}

	public void setEnabled(boolean enable) {
		button.setEnabled(enable);
	}

	public boolean isEnabled() {
		return button.isEnabled();
	}

	public MenuButton getControl() {
		return button;
	}

	protected void setExpressionType(String exprType) {
		if (helper != null && !exprType.equals(helper.getExpressionType()))
			helper.setExpressionType(exprType);
	}

	protected String getExpressionType() {
		String type = null;
		if (helper != null) {
			type = helper.getExpressionType();
			if (type == null) {
				type = UIUtil.getDefaultScriptType();
				helper.setExpressionType(type);
			}
			if (provider != null) {
				List types = Arrays.asList(provider.getExpressionTypes());
				if (!types.contains(type) && types.size() > 0) {
					type = types.get(0).toString();
					helper.setExpressionType(type);
				}
			}
		}
		return type;
	}

	protected String getExpression() {
		if (helper != null) {
			return helper.getExpression();
		}
		return ""; //$NON-NLS-1$
	}

	protected void setExpression(String expression) {
		if (expression != null && helper != null)
			helper.setExpression(expression);
	}

	protected int openExpressionBuilder(IExpressionBuilder builder, String expressionType) {
		builder.setExpression(getExpression());

		if (helper != null) {
			builder.setExpressionContext(
					helper.getExpressionContextFactory().getContext(expressionType, helper.getContextObject()));
		}

		if (builder.open() == Window.OK) {
			setExpressionType(expressionType);

			if (helper != null) {
				Object result = builder.getExpression();
				String newExpression = result == null ? null : result.toString();
				helper.setExpression(newExpression);
			}

			return Window.OK;
		}
		return Window.CANCEL;
	}

	public void notifyExpressionChangeEvent(String oldExpression, String newExpression) {
		if (helper != null)
			helper.notifyExpressionChangeEvent(oldExpression, newExpression);
	}

	public void setExpressionHelper(IExpressionHelper helper) {
		this.helper = helper;
	}

	public IExpressionHelper getExpressionHelper() {
		return helper;
	}

	public void refresh() {
		if (!button.isDisposed()) {
			button.setImage(provider.getImage(getExpressionType()));
			button.setToolTipText(provider.getTooltipText(getExpressionType()));
		}
	}

	public void setExpressionButtonProvider(IExpressionButtonProvider provider) {
		if (provider != null && provider != this.provider) {
			this.provider = provider;

			provider.setInput(this);

			for (int i = 0; i < menu.getItemCount(); i++) {
				menu.getItem(i).dispose();
				i--;
			}

			String[] types = this.provider.getExpressionTypes();
			for (int i = 0; i < types.length; i++) {
				MenuItem item = new MenuItem(menu, SWT.PUSH);
				item.setText(this.provider.getText(types[i]));
				item.setData(types[i]);
				item.setImage(this.provider.getImage(types[i]));
				item.addSelectionListener(listener);
			}

			if (menu.getItemCount() <= 1) {
				button.setDropDownMenu(null);
			}

			refresh();
		}
	}

	public IExpressionButtonProvider getExpressionButtonProvider() {
		return provider;
	}

	public boolean isSupportType(String expressionType) {
		if (provider != null) {
			String[] types = this.provider.getExpressionTypes();
			return Arrays.asList(types).contains(expressionType);
		}
		return false;
	}

	public IExpressionSupport getExpressionSupport(String exprType) {
		if (provider != null) {
			return provider.getExpressionSupport(exprType);
		}
		return null;
	}

	public IExpressionSupport getCurrentExpressionSupport() {
		if (provider != null) {
			return provider.getExpressionSupport(getExpressionType());
		}
		return null;
	}
}
