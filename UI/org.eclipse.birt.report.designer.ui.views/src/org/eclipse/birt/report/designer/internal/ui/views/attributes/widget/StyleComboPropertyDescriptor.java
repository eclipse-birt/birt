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

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.FormWidgetFactory;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.IComboProvider;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.StyleCombo;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * ComboPropertyDescriptor manages Combo choice control.
 */
public class StyleComboPropertyDescriptor extends PropertyDescriptor {

	protected StyleCombo combo;

	protected IChoiceSet choiceSet;

	protected String oldValue;

	public StyleComboPropertyDescriptor(boolean formStyle) {
		setFormStyle(formStyle);
	}

	@Override
	public void setInput(Object handle) {
		this.input = handle;
		getDescriptorProvider().setInput(input);
	}

	private int style = SWT.BORDER | SWT.READ_ONLY;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.ui.attributes.widget.PropertyDescriptor#
	 * resetUIData()
	 */
	void refresh(String value) {
		combo.setSelectedItem(value);
	}

	private String text;

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.
	 * PropertyDescriptor#getControl()
	 */
	@Override
	public Control getControl() {
		return combo;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.extensions.IPropertyDescriptor#
	 * createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public Control createControl(Composite parent) {
		assert (getDescriptorProvider() instanceof IComboProvider);
		if (isFormStyle()) {
			combo = FormWidgetFactory.getInstance().createStyleCombo(parent, (IComboProvider) getDescriptorProvider());
		} else {
			combo = new StyleCombo(parent, style, (IComboProvider) getDescriptorProvider());
		}
		combo.setItems(((IComboProvider) getDescriptorProvider()).getItems());
		if (text != null) {
			combo.setSelectedItem(text);
		}
		combo.addControlListener(new ControlListener() {

			@Override
			public void controlMoved(ControlEvent e) {
				combo.clearSelection();
			}

			@Override
			public void controlResized(ControlEvent e) {
				combo.clearSelection();
			}
		});
		combo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				handleComboSelectEvent();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				handleComboSelectEvent();
			}
		});
		return combo;
	}

	/**
	 * Processes the save action.
	 */
	protected void handleComboSelectEvent() {
		CommandStack stack = getActionStack();
		stack.startTrans("");//$NON-NLS-1$
		try {
			save((String) combo.getSelectedItem());
		} catch (SemanticException e1) {
			stack.rollback();
			WidgetUtil.processError(combo.getShell(), e1);
		}
		stack.commit();
	}

	private CommandStack getActionStack() {
		return SessionHandleAdapter.getInstance().getCommandStack();
	}

	@Override
	public void setDescriptorProvider(IDescriptorProvider provider) {
		super.setDescriptorProvider(provider);
	}

	@Override
	public void save(Object value) throws SemanticException {
		descriptorProvider.save(value);
	}

	public void setHidden(boolean isHidden) {
		WidgetUtil.setExcludeGridData(combo, isHidden);
	}

	public void setVisible(boolean isVisible) {
		combo.setVisible(isVisible);
	}

	@Override
	public void load() {
		oldValue = getDescriptorProvider().load().toString();
		refresh(oldValue);
	}

	public void addStyle(int style) {
		this.style |= style;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		if (combo != null) {
			combo.setSelectedItem(text);
		}
	}
}
