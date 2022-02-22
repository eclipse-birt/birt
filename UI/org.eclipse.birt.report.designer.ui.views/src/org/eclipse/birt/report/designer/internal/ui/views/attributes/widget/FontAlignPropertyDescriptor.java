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

import org.eclipse.birt.report.designer.internal.ui.swt.custom.FormWidgetFactory;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.Accessible;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * TextAlignPropertyDescriptor manages TextAlign controls.
 */
public class FontAlignPropertyDescriptor extends PropertyDescriptor {

	protected Button right, left, justify, center;

	protected Composite container;

	protected SelectionListener listener;

	@Override
	public void setInput(Object handle) {
		this.input = handle;
		getDescriptorProvider().setInput(input);
	}

	public FontAlignPropertyDescriptor() {
		listener = new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (e.widget != right) {
					right.setSelection(false);
				}
				if (e.widget != left) {
					left.setSelection(false);
				}
				if (e.widget != center) {
					center.setSelection(false);
				}
				if (e.widget != justify) {
					justify.setSelection(false);
				}
				try {
					if (!right.getSelection() && !left.getSelection() && !center.getSelection()
							&& !justify.getSelection()) {
						save(null);
						return;
					}
					String value = (String) e.widget.getData();
					save(value);
				} catch (SemanticException e1) {
					WidgetUtil.processError(right.getShell(), e1);
				}

			}
		};
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.ui.attributes.widget.PropertyDescriptor#
	 * resetUIData()
	 */
	@Override
	public void load() {
		String value = getDescriptorProvider().load().toString();
		Button[] btns = { left, right, center, justify };

		boolean stateFlag = ((value == null) == btns[0].getEnabled());

		for (int i = 0; i < btns.length; i++) {
			if (btns[i].getData().equals(value)) {
				btns[i].setSelection(true);
			} else {
				btns[i].setSelection(false);
			}
			if (stateFlag) {
				btns[i].setEnabled(value != null);
			}
		}

		String[] values = { DesignChoiceConstants.TEXT_ALIGN_LEFT, DesignChoiceConstants.TEXT_ALIGN_RIGHT,
				DesignChoiceConstants.TEXT_ALIGN_CENTER, DesignChoiceConstants.TEXT_ALIGN_JUSTIFY, };
		for (int i = 0; i < values.length; i++) {
			String imageName = values[i];
			if (!btns[i].isEnabled()) {
				imageName += IReportGraphicConstants.DIS;
			}
			if (btns[i].getImage() == null) {
				btns[i].setImage(ReportPlatformUIImages.getImage(imageName));
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
		return container;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.extensions.IPropertyDescriptor#
	 * createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public Control createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		RowLayout layout = new RowLayout(SWT.HORIZONTAL);
		layout.marginBottom = 0;
		layout.marginHeight = 0;
		layout.marginLeft = 0;
		layout.marginRight = 0;
		layout.marginTop = 0;
		layout.marginWidth = 0;
		layout.spacing = 0;
		container.setLayout(layout);

		left = FormWidgetFactory.getInstance().createButton(container, SWT.TOGGLE, false);
		left.setToolTipText(Messages.getString("TextAlignPropertyDescriptor.0")); //$NON-NLS-1$
		center = FormWidgetFactory.getInstance().createButton(container, SWT.TOGGLE, false);
		center.setToolTipText(Messages.getString("TextAlignPropertyDescriptor.1")); //$NON-NLS-1$
		right = FormWidgetFactory.getInstance().createButton(container, SWT.TOGGLE, false);
		right.setToolTipText(Messages.getString("TextAlignPropertyDescriptor.2")); //$NON-NLS-1$
		justify = FormWidgetFactory.getInstance().createButton(container, SWT.TOGGLE, false);
		justify.setToolTipText(Messages.getString("TextAlignPropertyDescriptor.3")); //$NON-NLS-1$

		Button[] btns = { left, center, right, justify, };
		// The value is used to present active value and image key.
		String[] values = { DesignChoiceConstants.TEXT_ALIGN_LEFT, DesignChoiceConstants.TEXT_ALIGN_CENTER,
				DesignChoiceConstants.TEXT_ALIGN_RIGHT, DesignChoiceConstants.TEXT_ALIGN_JUSTIFY, };
		for (int i = 0; i < btns.length; i++) {
			btns[i].setData(values[i]);
			btns[i].addSelectionListener(listener);
			btns[i].getAccessible().addAccessibleListener(new AccessibleAdapter() {
				@Override
				public void getName(AccessibleEvent e) {
					Accessible accessible = (Accessible) e.getSource();
					Button item = (Button) accessible.getControl();
					if (item != null) {
						e.result = item.getToolTipText();
					}
				}
			});
		}
		return container;
	}

	@Override
	public void save(Object obj) throws SemanticException {
		getDescriptorProvider().save(obj);
	}
}
