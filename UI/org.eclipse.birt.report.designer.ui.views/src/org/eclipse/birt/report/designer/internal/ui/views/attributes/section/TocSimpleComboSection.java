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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.section;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.DescriptorToolkit;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.SimpleComboPropertyDescriptor;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Administrator
 *
 */
public class TocSimpleComboSection extends SimpleComboSection {

	public TocSimpleComboSection(String labelText, Composite parent, boolean isFormStyle) {
		super(labelText, parent, isFormStyle);
	}

	@Override
	protected SimpleComboPropertyDescriptor getSimpleComboControl(Composite parent) {
		if (simpleCombo == null) {
			simpleCombo = DescriptorToolkit.createTocSimpleComboPropertyDescriptor(true);
			if (getProvider() != null) {
				simpleCombo.setDescriptorProvider(getProvider());
			}
			simpleCombo.createControl(parent);
			simpleCombo.getControl().setLayoutData(new GridData());
			simpleCombo.getControl().addDisposeListener(new DisposeListener() {

				@Override
				public void widgetDisposed(DisposeEvent event) {
					simpleCombo = null;
				}
			});
		} else {
			checkParent(simpleCombo.getControl(), parent);
		}
		return simpleCombo;
	}

}
