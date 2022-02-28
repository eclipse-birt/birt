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

package org.eclipse.birt.report.designer.internal.ui.util;

import org.eclipse.birt.report.designer.internal.ui.dialogs.expression.ExpressionButton;
import org.eclipse.birt.report.designer.internal.ui.dialogs.expression.IExpressionHelper;
import org.eclipse.birt.report.designer.internal.ui.expressions.ExpressionContextFactoryImpl;
import org.eclipse.birt.report.designer.internal.ui.expressions.IExpressionContextFactory;
import org.eclipse.birt.report.designer.internal.ui.expressions.IExpressionConverter;
import org.eclipse.birt.report.designer.internal.ui.expressions.IExpressionSupport;
import org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionHandle;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 *
 */

public class ExpressionButtonUtil {

	public static class ExpressionHelper implements IExpressionHelper {

		protected Control control;
		protected Listener listener;
		private IExpressionProvider provider;
		protected ExpressionButton button;
		private Object contextObject;

		@Override
		public String getExpression() {
			if (control.isDisposed()) {
				return ""; //$NON-NLS-1$
			}
			if (control instanceof Text) {
				return ((Text) control).getText();
			} else if (control instanceof Combo) {
				return ((Combo) control).getText();
			} else if (control instanceof CCombo) {
				return ((CCombo) control).getText();
			}
			return ""; //$NON-NLS-1$
		}

		@Override
		public void notifyExpressionChangeEvent(String oldExpression, String newExpression) {
			if (listener != null) {
				Event event = new Event();
				event.widget = button.getControl();
				event.data = new String[] { oldExpression, newExpression };
				event.detail = SWT.Modify;
				listener.handleEvent(event);
			}
			control.setFocus();
		}

		@Override
		public void setExpression(String expression) {
			if (control.isDisposed()) {
				return;
			}
			if (control instanceof Text) {
				((Text) control).setText(DEUtil.resolveNull(expression));
			} else if (control instanceof Combo) {
				((Combo) control).setText(DEUtil.resolveNull(expression));
			} else if (control instanceof CCombo) {
				((CCombo) control).setText(DEUtil.resolveNull(expression));
			}
		}

		@Override
		public String getExpressionType() {
			if (!control.isDisposed()) {
				return (String) control.getData(EXPR_TYPE);
			} else {
				return UIUtil.getDefaultScriptType();
			}
		}

		@Override
		public void setExpressionType(String exprType) {
			if (!control.isDisposed()) {
				control.setData(EXPR_TYPE, exprType);
			}
		}

		public void setProvider(IExpressionProvider provider) {
			this.provider = provider;
		}

		public IExpressionProvider getProvider() {
			return provider;
		}

		protected void setListener(Listener listener) {
			this.listener = listener;
		}

		private void setControl(Control control) {
			this.control = control;
		}

		protected void setExpressionButton(ExpressionButton button) {
			this.button = button;
		}

		@Override
		public Object getContextObject() {
			return contextObject;
		}

		public void setContextObject(Object contextObject) {
			this.contextObject = contextObject;
		}

		@Override
		public IExpressionContextFactory getExpressionContextFactory() {
			return new ExpressionContextFactoryImpl(contextObject, provider);
		}
	}

	public static final String EXPR_BUTTON = "exprButton";//$NON-NLS-1$
	public static final String EXPR_TYPE = "exprType";//$NON-NLS-1$

	public static ExpressionButton createExpressionButton(Composite parent, Control control,
			IExpressionProvider provider, Object contextObject) {
		return createExpressionButton(parent, control, provider, contextObject, null, false, SWT.PUSH,
				new ExpressionHelper());
	}

	public static ExpressionButton createExpressionButton(Composite parent, Control control,
			IExpressionProvider provider, Object contextObject, boolean showOnlyLeafInThirdColumn) {
		return createExpressionButton(parent, control, provider, contextObject, null, false, SWT.PUSH,
				new ExpressionHelper(), showOnlyLeafInThirdColumn);
	}

	public static ExpressionButton createExpressionButton(Composite parent, Control control,
			IExpressionProvider provider, Object contextObject, ExpressionHelper helper) {
		return createExpressionButton(parent, control, provider, contextObject, null, false, SWT.PUSH, helper);
	}

	public static ExpressionButton createExpressionButton(Composite parent, final Control control,
			final IExpressionProvider provider, Object contextObject, Listener listener) {
		return createExpressionButton(parent, control, provider, contextObject, listener, false, SWT.PUSH,
				new ExpressionHelper());
	}

	public static ExpressionButton createExpressionButton(Composite parent, Control control,
			IExpressionProvider provider, Object contextObject, int style) {
		return createExpressionButton(parent, control, provider, contextObject, null, false, style,
				new ExpressionHelper());
	}

	public static ExpressionButton createExpressionButton(Composite parent, Control control,
			IExpressionProvider provider, Object contextObject, boolean allowConstant, int style) {
		return createExpressionButton(parent, control, provider, contextObject, null, allowConstant, style,
				new ExpressionHelper());
	}

	public static ExpressionButton createExpressionButton(Composite parent, final Control control,
			final IExpressionProvider provider, Object contextObject, final Listener listener, boolean allowConstant,
			int style) {

		return createExpressionButton(parent, control, provider, contextObject, listener, allowConstant, style,
				new ExpressionHelper());
	}

	public static ExpressionButton createExpressionButton(Composite parent, final Control control,
			final IExpressionProvider provider, Object contextObject, final Listener listener, boolean allowConstant,
			int style, ExpressionHelper helper) {

		final ExpressionButton button = UIUtil.createExpressionButton(parent, style, allowConstant);
		helper.setProvider(provider);
		helper.setListener(listener);
		helper.setControl(control);
		helper.setExpressionButton(button);
		helper.setContextObject(contextObject);
		button.setExpressionHelper(helper);

		control.setData(EXPR_BUTTON, button);
		control.setData(ExpressionButtonUtil.EXPR_TYPE, helper.getExpressionType());
		button.refresh();

		return button;
	}

	public static ExpressionButton createExpressionButton(Composite parent, final Control control,
			final IExpressionProvider provider, Object contextObject, final Listener listener, boolean allowConstant,
			int style, ExpressionHelper helper, boolean showOnlyLeafInThirdColumn) {

		final ExpressionButton button = UIUtil.createExpressionButton(parent, style, allowConstant,
				showOnlyLeafInThirdColumn);
		helper.setProvider(provider);
		helper.setListener(listener);
		helper.setControl(control);
		helper.setExpressionButton(button);
		helper.setContextObject(contextObject);
		button.setExpressionHelper(helper);

		control.setData(EXPR_BUTTON, button);
		control.setData(ExpressionButtonUtil.EXPR_TYPE, helper.getExpressionType());
		button.refresh();

		return button;
	}

	public static boolean isSupportJavaScript(ExpressionButton button) {
		return button.isSupportType(ExpressionType.JAVASCRIPT);
	}

	public static void initExpressionButtonControl(Control control, Object element, String property) {
		if (element instanceof DesignElementHandle) {
			ExpressionHandle value = ((DesignElementHandle) element).getExpressionProperty(property);
			initExpressionButtonControl(control, value);
		} else if (element instanceof StructureHandle) {
			ExpressionHandle value = ((StructureHandle) element).getExpressionProperty(property);
			initExpressionButtonControl(control, value);
		} else if (element instanceof Structure) {
			Expression value = ((Structure) element).getExpressionProperty(property);
			initExpressionButtonControl(control, value);
		}
	}

	public static void saveExpressionButtonControl(Control control, Object element, String property)
			throws SemanticException {
		ExpressionButton button = getExpressionButton(control);
		if (button != null && button.getExpressionHelper() != null) {
			Expression expression = new Expression(button.getExpressionHelper().getExpression(),
					button.getExpressionHelper().getExpressionType());

			if (element instanceof DesignElementHandle) {
				((DesignElementHandle) element).setExpressionProperty(property, expression);
			} else if (element instanceof StructureHandle) {
				((StructureHandle) element).setExpressionProperty(property, expression);
			} else if (element instanceof Structure) {
				((Structure) element).setExpressionProperty(property, expression);
			}
		}
	}

	public static void initExpressionButtonControl(Control control, ExpressionHandle value) {

		ExpressionButton button = getExpressionButton(control);
		if (button != null && button.getExpressionHelper() != null) {
			button.getExpressionHelper()
					.setExpressionType(value == null || value.getType() == null ? UIUtil.getDefaultScriptType()
							: (String) value.getType());
			String stringValue = value == null || value.getExpression() == null ? "" : (String) value.getExpression(); //$NON-NLS-1$
			button.getExpressionHelper().setExpression(stringValue);
			button.refresh();
		}
	}

	public static void initExpressionButtonControl(Control control, Expression value) {
		ExpressionButton button = getExpressionButton(control);
		if (button != null && button.getExpressionHelper() != null) {
			button.getExpressionHelper()
					.setExpressionType(value == null || value.getType() == null ? UIUtil.getDefaultScriptType()
							: (String) value.getType());
			String stringValue = value == null || value.getExpression() == null ? "" : (String) value.getExpression(); //$NON-NLS-1$
			button.getExpressionHelper().setExpression(stringValue);
			button.refresh();
		}
	}

	public static Expression getExpression(Control control) {
		ExpressionButton button = getExpressionButton(control);
		if (button != null && button.getExpressionHelper() != null) {
			String expression = button.getExpressionHelper().getExpression();
			String type = button.getExpressionHelper().getExpressionType();
			return new Expression(expression, type);
		}
		return null;
	}

	public static ExpressionButton getExpressionButton(Control control) {
		Object button = control.getData(ExpressionButtonUtil.EXPR_BUTTON);
		if (button instanceof ExpressionButton) {
			return ((ExpressionButton) button);
		}
		return null;
	}

	public static IExpressionConverter getCurrentExpressionConverter(Control control) {
		return getCurrentExpressionConverter(control, true);
	}

	public static IExpressionConverter getCurrentExpressionConverter(Control control, boolean refreshButtonType) {
		if (control == null) {
			return null;
		} else if (getExpressionButton(control) == null) {
			return null;
		} else {
			IExpressionSupport support = ExpressionButtonUtil.getExpressionButton(control)
					.getCurrentExpressionSupport();
			if (support != null && support.getConverter() != null) {
				return support.getConverter();
			} else if (refreshButtonType) {
				support = ExpressionButtonUtil.getExpressionButton(control)
						.getExpressionSupport(ExpressionType.JAVASCRIPT);
				if (support != null && support.getConverter() != null) {
					control.setData(ExpressionButtonUtil.EXPR_TYPE, ExpressionType.JAVASCRIPT);
					getExpressionButton(control).refresh();
					return support.getConverter();
				}
			}
		}
		return null;
	}

}
