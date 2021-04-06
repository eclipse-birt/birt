/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.attributes.widget;

import org.eclipse.birt.report.designer.internal.ui.swt.custom.FormWidgetFactory;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.DualRadioButtonPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * This class is only for the Layout Preference Section.
 */
public class DualRadioButtonPropertyDescriptor extends PropertyDescriptor {

	private Composite composite;
	private Button radio1, radio2;
	private String oldValue;
	private String[] items;

	public DualRadioButtonPropertyDescriptor(boolean formStyle) {
		setFormStyle(formStyle);
	}

	public Control createControl(Composite parent) {
		if (isFormStyle()) {
			composite = FormWidgetFactory.getInstance().createComposite(parent);
		} else {
			composite = new Composite(parent, SWT.BORDER | SWT.READ_ONLY);
		}
		composite.setLayout(new GridLayout(2, false));

		if (getDescriptorProvider() != null) {
			items = ((DualRadioButtonPropertyDescriptorProvider) getDescriptorProvider()).getItems();

			radio1 = FormWidgetFactory.getInstance().createButton(composite, SWT.RADIO, isFormStyle());
			radio2 = FormWidgetFactory.getInstance().createButton(composite, SWT.RADIO, isFormStyle());

			// The first choice is always auto accordign to the provider.
			radio1.setData(items[0]);
			radio2.setData(items[1]);

			radio1.setImage(
					ReportPlatformUIImages.getImageDescriptor(IReportGraphicConstants.ICON_LAYOUT_AUTO).createImage());
			radio2.setImage(
					ReportPlatformUIImages.getImageDescriptor(IReportGraphicConstants.ICON_LAYOUT_FIXED).createImage());

			radio1.setText(Messages.getString("ApplyLayoutPreferenceAction.autoLayout")); //$NON-NLS-1$
			radio2.setText(Messages.getString("ApplyLayoutPreferenceAction.fixedLayout")); //$NON-NLS-1$

			radio1.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent e) {
				}

				public void widgetSelected(SelectionEvent e) {
					handleButtonSelectEvent(radio1);
				}
			});

			radio2.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent e) {
				}

				public void widgetSelected(SelectionEvent e) {
					handleButtonSelectEvent(radio2);
				}
			});
		}
		return composite;
	}

	private void handleButtonSelectEvent(Button btn) {
		try {
			save(btn.getData());
		} catch (SemanticException e) {
			WidgetUtil.processError(btn.getShell(), e);
		}
	}

	public void save(Object obj) throws SemanticException {
		getDescriptorProvider().save(obj);
	}

	public void load() {
		if (getDescriptorProvider() instanceof DualRadioButtonPropertyDescriptorProvider) {
			oldValue = ((DualRadioButtonPropertyDescriptorProvider) getDescriptorProvider()).load().toString();
			if (oldValue.equalsIgnoreCase((String) radio1.getData())) {
				radio1.setSelection(true);
				radio2.setSelection(false);

			} else {
				radio1.setSelection(false);
				radio2.setSelection(true);
			}
		}
	}

	public Control getControl() {
		return composite;
	}

	public void setInput(Object input) {
		getDescriptorProvider().setInput(input);
	}

	public void setHidden(boolean isHidden) {
		WidgetUtil.setExcludeGridData(composite, isHidden);
	}

	public void setVisible(boolean isVisible) {
		composite.setVisible(isVisible);
	}

}
