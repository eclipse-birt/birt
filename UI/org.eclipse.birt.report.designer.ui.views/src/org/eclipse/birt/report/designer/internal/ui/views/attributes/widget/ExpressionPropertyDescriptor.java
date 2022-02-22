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
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionButtonUtil;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionButtonUtil.ExpressionHelper;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.ExpressionPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * Property Descriptor for value expression property.
 */

public class ExpressionPropertyDescriptor extends PropertyDescriptor {

	protected Text text;

	private Composite containerPane;

	private Expression deValue;

	private String newValue;

	private ExpressionButton exprButton;

	private boolean multi = true;

	/**
	 * The constructor.
	 */
	public ExpressionPropertyDescriptor(boolean formStyle) {
		setFormStyle(formStyle);
	}

	public Text getTextControl() {
		return text;
	}

	@Override
	public void setInput(Object handle) {
		this.input = handle;
		getDescriptorProvider().setInput(input);
	}

	public boolean isMulti() {
		return multi;
	}

	public void setMulti(boolean multi) {
		this.multi = multi;
	}

	/**
	 * After selection changed, re-sets UI data.
	 */
	@Override
	public void load() {
		ExpressionHelper helper = (ExpressionHelper) exprButton.getExpressionHelper();
		helper.setContextObject(DEUtil.getInputFirstElement(this.getInput()));
		if (getDescriptorProvider() instanceof ExpressionPropertyDescriptorProvider) {
			helper.setProvider(
					((ExpressionPropertyDescriptorProvider) getDescriptorProvider()).getExpressionProvider());
		}

		Object value = getDescriptorProvider().load();
		if (value == null || value instanceof Expression) {
			deValue = (Expression) value;

			String stringValue = deValue == null || deValue.getExpression() == null ? "" //$NON-NLS-1$
					: (String) deValue.getExpression();
			text.setText(stringValue);

			text.setData(ExpressionButtonUtil.EXPR_TYPE,
					deValue == null || deValue.getType() == null ? UIUtil.getDefaultScriptType()
							: (String) deValue.getType());

			Object button = text.getData(ExpressionButtonUtil.EXPR_BUTTON);
			if (button instanceof ExpressionButton) {
				((ExpressionButton) button).refresh();
			}

			if (getDescriptorProvider() instanceof ExpressionPropertyDescriptorProvider) {
				boolean readOnly = ((ExpressionPropertyDescriptorProvider) getDescriptorProvider()).isReadOnly();
				boolean enable = ((ExpressionPropertyDescriptorProvider) getDescriptorProvider()).isEnable();
				text.setEnabled(enable && (!readOnly));

				if (button instanceof ExpressionButton) {
					((ExpressionButton) button).refresh();
					((ExpressionButton) button).setEnabled(enable && (!readOnly));
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.
	 * PropertyDescriptor#getControl()
	 */
	@Override
	public Control getControl() {
		return containerPane;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.designer.ui.extensions.IPropertyDescriptor#
	 * createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public Control createControl(Composite parent) {
		containerPane = FormWidgetFactory.getInstance().createComposite(parent);

		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		if (!multi) {
			layout.marginWidth = layout.marginHeight = 2;
		}
		containerPane.setLayout(layout);
		if (multi) {
			if (isFormStyle()) {
				text = FormWidgetFactory.getInstance().createText(containerPane, "", //$NON-NLS-1$
						SWT.MULTI | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL);
			} else {
				text = new Text(containerPane, SWT.MULTI | SWT.WRAP | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
			}
			text.setLayoutData(new GridData(GridData.FILL_BOTH));
		} else {
			if (isFormStyle()) {
				text = FormWidgetFactory.getInstance().createText(containerPane, "", SWT.MULTI); //$NON-NLS-1$
			} else {
				text = new Text(containerPane, SWT.MULTI | SWT.BORDER);
			}
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.heightHint = text.computeSize(SWT.DEFAULT, SWT.DEFAULT).y
					- (isFormStyle() ? 0 : (text.getBorderWidth() * 2));
			text.setLayoutData(gd);
		}
		// text.addSelectionListener( new SelectionAdapter( ) {
		//
		// public void widgetDefaultSelected( SelectionEvent e )
		// {
		// handleSelectEvent( );
		// }
		// } );
		text.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {

			}

			@Override
			public void focusLost(FocusEvent e) {
				handleFocusLostEvent();
			}
		});

		if (getDescriptorProvider() instanceof ExpressionPropertyDescriptorProvider) {
			Listener listener = new Listener() {

				@Override
				public void handleEvent(Event event) {
					if (event.data instanceof String[]) {
						newValue = ((String[]) event.data)[0];
					}
					processAction();
				}

			};

			exprButton = ExpressionButtonUtil.createExpressionButton(containerPane, text, null, null, listener, false,
					isFormStyle() ? SWT.FLAT : SWT.PUSH, new ExpressionHelper());
		}

		return containerPane;
	}

	protected void handleSelectEvent() {
		newValue = text.getText();
		processAction();
	}

	protected void handleFocusLostEvent() {
		newValue = text.getText();
		processAction();
	}

	/**
	 * Processes the save action.
	 */
	private void processAction() {
		String value = newValue;
		if (value != null && value.length() == 0) {
			value = null;
		}

		try {
			if (value == null && deValue != null) {
				save(value);
			} else if (text.getText().trim().length() == 0) {
				save(null);
			} else {
				Expression expression = new Expression(text.getText().trim(),
						(String) text.getData(ExpressionButtonUtil.EXPR_TYPE));
				save(expression);
			}
		} catch (SemanticException e1) {
			text.setText(UIUtil.convertToGUIString(deValue == null ? null : deValue.getStringExpression()));
			ExceptionHandler.handle(e1);
		}

	}

	public void setText(String text) {
		this.text.setText(text);
	}

	@Override
	public void save(Object obj) throws SemanticException {
		getDescriptorProvider().save(obj);

	}

	public void setHidden(boolean isHidden) {
		WidgetUtil.setExcludeGridData(containerPane, isHidden);
	}

	public void setVisible(boolean isVisible) {
		containerPane.setVisible(isVisible);
	}

}
