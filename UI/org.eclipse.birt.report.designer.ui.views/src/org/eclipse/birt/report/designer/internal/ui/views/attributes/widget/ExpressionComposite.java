/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.widget;

import org.eclipse.birt.report.designer.internal.ui.dialogs.expression.ExpressionButton;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.FormWidgetFactory;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionButtonUtil;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionButtonUtil.ExpressionHelper;
import org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.accessibility.AccessibleTextAdapter;
import org.eclipse.swt.accessibility.AccessibleTextEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * ExpressionComposite contains a Text and a Button control for presenting an
 * Expression builder UI.
 */
public class ExpressionComposite extends Composite {

	protected Text text;

	/**
	 * @param parent A widget which will be the parent of the new instance (cannot
	 *               be null)
	 * @param style  The style of widget to construct
	 */
	public ExpressionComposite(Composite parent, boolean isFormStyle) {
		super(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 2;
		layout.marginWidth = 2;
		layout.verticalSpacing = 2;
		layout.horizontalSpacing = 3;
		setLayout(layout);
		if (isFormStyle)
			text = FormWidgetFactory.getInstance().createText(this, "", //$NON-NLS-1$
					SWT.READ_ONLY | SWT.MULTI);
		else
			text = new Text(this, SWT.READ_ONLY | SWT.MULTI | SWT.BORDER);
		GridData data = new GridData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = GridData.FILL;
		data.heightHint = text.computeSize(SWT.DEFAULT, SWT.DEFAULT).y
				- (isFormStyle ? 0 : (text.getBorderWidth() * 2));
		text.setLayoutData(data);

		Listener listener = new Listener() {

			public void handleEvent(Event event) {
				notifyListeners(SWT.Modify, null);
			}

		};

		button = ExpressionButtonUtil.createExpressionButton(this, text, null, null, listener, false,
				isFormStyle ? SWT.FLAT : SWT.PUSH, new ExpressionHelper());

		initAccessible();
	}

	public void setInput(Object input) {
		ExpressionHelper helper = (ExpressionHelper) button.getExpressionHelper();
		helper.setContextObject(DEUtil.getInputFirstElement(input));
	}

	void initAccessible() {

		((ExpressionButton) text.getData(ExpressionButtonUtil.EXPR_BUTTON)).getControl().getAccessible()
				.addAccessibleListener(new AccessibleAdapter() {

					public void getHelp(AccessibleEvent e) {
						e.result = ((ExpressionButton) text.getData(ExpressionButtonUtil.EXPR_BUTTON)).getControl()
								.getToolTipText();
					}
				});

		text.getAccessible().addAccessibleControlListener(new AccessibleControlAdapter() {

			public void getRole(AccessibleControlEvent e) {
				e.detail = text.getEditable() ? ACC.ROLE_TEXT : ACC.ROLE_LABEL;
			}
		});

		getAccessible().addAccessibleTextListener(new AccessibleTextAdapter() {

			public void getCaretOffset(AccessibleTextEvent e) {
				e.offset = text.getCaretPosition();
			}

			public void getSelectionRange(AccessibleTextEvent e) {
				Point sel = text.getSelection();
				e.offset = sel.x;
				e.length = sel.y - sel.x;
			}
		});

		getAccessible().addAccessibleControlListener(new AccessibleControlAdapter() {

			public void getChildAtPoint(AccessibleControlEvent e) {
				Point testPoint = toControl(e.x, e.y);
				if (getBounds().contains(testPoint)) {
					e.childID = ACC.CHILDID_SELF;
				}
			}

			public void getLocation(AccessibleControlEvent e) {
				Rectangle location = getBounds();
				Point pt = toDisplay(location.x, location.y);
				e.x = pt.x;
				e.y = pt.y;
				e.width = location.width;
				e.height = location.height;
			}

			public void getChildCount(AccessibleControlEvent e) {
				e.detail = 0;
			}

			public void getRole(AccessibleControlEvent e) {
				e.detail = ACC.ROLE_TEXT;
			}

			public void getState(AccessibleControlEvent e) {
				e.detail = ACC.STATE_NORMAL;
			}

			public void getValue(AccessibleControlEvent e) {
				e.result = text.getText();
			}
		});
	}

	/**
	 * Sets value of the Expression.
	 * 
	 * @param string the String value.
	 */
	public void setExpression(Expression expression) {
		ExpressionButtonUtil.initExpressionButtonControl(text, expression);
	}

	/**
	 * Gets value of the Expression.
	 * 
	 * @return a String value.
	 */
	public Expression getExpression() {
		return ExpressionButtonUtil.getExpression(text);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Control#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabled) {
		text.setEnabled(enabled);
		((ExpressionButton) text.getData(ExpressionButtonUtil.EXPR_BUTTON)).setEnabled(enabled);
		super.setEnabled(enabled);
	}

	private ExpressionButton button;

	public void setExpressionProvider(IExpressionProvider provider) {
		ExpressionHelper helper = (ExpressionHelper) button.getExpressionHelper();
		helper.setProvider(provider);
	}
}
