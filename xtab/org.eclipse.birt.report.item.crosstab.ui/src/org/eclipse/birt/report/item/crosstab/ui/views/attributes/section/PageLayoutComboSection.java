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

package org.eclipse.birt.report.item.crosstab.ui.views.attributes.section;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SimpleComboSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.SimpleComboPropertyDescriptor;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.widget.PageLayoutComboPropertyDescriptor;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Administrator
 *
 */
public class PageLayoutComboSection extends SimpleComboSection {

	ContainerSection containerSection;

	public PageLayoutComboSection(String labelText, Composite parent, boolean isFormStyle) {
		super(labelText, parent, isFormStyle);
	}

	public PageLayoutComboSection(String labelText, ContainerSection section, boolean isFormStyle) {
		super(labelText, null, isFormStyle);
		this.containerSection = section;
	}

	@Override
	public void createSection() {
		if (parent == null && containerSection != null) {
			parent = containerSection.getContainerComposite();
		}
		super.createSection();
	}

	@Override
	protected SimpleComboPropertyDescriptor getSimpleComboControl(Composite parent) {
		if (simpleCombo == null) {
			simpleCombo = new PageLayoutComboPropertyDescriptor(true);
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
