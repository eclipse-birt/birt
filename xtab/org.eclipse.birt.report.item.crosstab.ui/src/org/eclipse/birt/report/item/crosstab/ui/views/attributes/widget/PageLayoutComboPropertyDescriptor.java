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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.PropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.SimpleComboPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.SimpleComboPropertyDescriptor;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider.PageLayoutPropertyDescriptorProvider;

/**
 * @author Administrator
 *
 */
public class PageLayoutComboPropertyDescriptor extends SimpleComboPropertyDescriptor {

	public PageLayoutComboPropertyDescriptor(boolean formStyle) {
		super(formStyle);
	}

	protected void refresh(String value) {
		if (getDescriptorProvider() instanceof PageLayoutPropertyDescriptorProvider) {

			String[] items = ((PageLayoutPropertyDescriptorProvider) getDescriptorProvider()).getItems();
			combo.setItems(items);
			boolean stateFlag = ((value == null) == combo.getEnabled());
			if (stateFlag)
				combo.setEnabled(value != null);

			if (((PropertyDescriptorProvider) getDescriptorProvider()).isReadOnly()) {
				combo.setEnabled(false);
			}

			boolean isEditable = ((SimpleComboPropertyDescriptorProvider) getDescriptorProvider()).isEditable();
			setComboEditable(isEditable);

			int sindex = Arrays.asList(items).indexOf(oldValue);

			if (((SimpleComboPropertyDescriptorProvider) getDescriptorProvider()).isSpecialProperty() && sindex < 0) {
				if (value != null && value.length() > 0) {
					combo.setText(value);
					return;
				}

				if (combo.getItemCount() > 0) {
					combo.select(0);
					return;
				}
			}

			combo.select(sindex);

		} else {
			super.refresh(value);
		}
	}

}
