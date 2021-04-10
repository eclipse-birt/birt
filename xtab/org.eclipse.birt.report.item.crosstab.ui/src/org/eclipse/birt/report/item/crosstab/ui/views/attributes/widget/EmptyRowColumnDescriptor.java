/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.ui.views.attributes.widget;

import java.util.Arrays;

import org.eclipse.birt.report.designer.internal.ui.swt.custom.FormWidgetFactory;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.views.attributes.IPropertyDescriptor;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider.EmptyRowColumnProvider;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;

public class EmptyRowColumnDescriptor implements IPropertyDescriptor {

	private boolean formStyle;
	private List list;
	private Button button;

	public boolean isFormStyle() {
		return formStyle;
	}

	public void setFormStyle(boolean formStyle) {
		this.formStyle = formStyle;
	}

	public EmptyRowColumnDescriptor(boolean formStyle) {
		setFormStyle(formStyle);
	}

	public Control createControl(Composite parent) {
		composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginWidth = layout.marginHeight = 2;
		layout.horizontalSpacing = 10;
		layout.numColumns = 2;
		composite.setLayout(layout);
		button = FormWidgetFactory.getInstance().createButton(composite, SWT.CHECK, isFormStyle());
		int buttonWidth = button.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
		button.setText(provider.getDisplayName());
		button.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				if (button.getSelection()) {
					initList();
					if (list.getItemCount() > 0) {
						button.setEnabled(true);
						list.setEnabled(true);
						if (list.getSelectionCount() == 0) {
							list.setSelection(0);
						}
						handleListSelectEvent();
					} else {
						button.setEnabled(false);
						button.setSelection(false);
						list.setEnabled(false);
					}
				} else {
					list.setEnabled(false);
					try {
						save(null);
					} catch (SemanticException e1) {
						ExceptionUtil.handle(e1);
					}
				}
			}

		});
		if (isFormStyle()) {
			list = FormWidgetFactory.getInstance().createList(composite, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
		} else
			list = new List(parent, SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);

		list.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				handleListSelectEvent();
			}

		});

		GridData gd = new GridData();
		gd.verticalAlignment = GridData.VERTICAL_ALIGN_FILL;
		int width = provider.getMaxLengthOfDisplayName(button);
		gd.widthHint = width + buttonWidth;
		button.setLayoutData(gd);

		gd = new GridData();
		gd.heightHint = 80;
		gd.widthHint = 180;
		list.setLayoutData(gd);

		return composite;
	}

	protected void handleListSelectEvent() {
		try {
			save(list.getSelection()[0]);
		} catch (SemanticException e) {
			ExceptionUtil.handle(e);
		}
	}

	public Control getControl() {
		return composite;
	}

	public void load() {
		initList();
		if (list.getItemCount() == 0) {
			button.setEnabled(false);
			button.setSelection(false);
			list.setEnabled(false);
			return;
		} else
			button.setEnabled(true);
		Object value = provider.load();
		if (value == null) {
			button.setSelection(false);
			list.setEnabled(false);
		} else {
			button.setSelection(true);
			list.setEnabled(true);

			if (list.getSelectionCount() > 0 && list.getSelection()[0].equals(((LevelHandle) value).getName()))
				return;
			list.setSelection(new String[] { ((LevelHandle) value).getName() });
		}
	}

	private void initList() {
		String[] names = new String[provider.getViewLevels().size()];
		java.util.List levels = provider.getViewLevels();
		for (int i = 0; i < levels.size(); i++) {
			LevelHandle level = (LevelHandle) levels.get(i);
			names[i] = level.getName();
		}
		if (!Arrays.equals(names, list.getItems())) {
			list.removeAll();
			list.setItems(names);
		}
	}

	public void save(Object obj) throws SemanticException {
		provider.save(obj);
	}

	public void setInput(Object input) {
		provider.setInput(input);
	}

	EmptyRowColumnProvider provider;
	private Composite composite;

	public void setDescriptorProvider(EmptyRowColumnProvider provider) {
		this.provider = provider;
	}

	public IDescriptorProvider getDescriptorProvider() {
		return provider;
	}

	public void reset() {
		if (provider != null && provider.canReset()) {
			try {
				provider.reset();
			} catch (SemanticException e) {
				ExceptionUtil.handle(e);
			}
		}
	}
}
