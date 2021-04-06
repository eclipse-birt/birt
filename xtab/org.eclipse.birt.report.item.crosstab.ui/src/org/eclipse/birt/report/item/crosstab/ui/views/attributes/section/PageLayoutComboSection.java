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

	public void createSection() {
		if (parent == null && containerSection != null) {
			parent = containerSection.getContainerComposite();
		}
		super.createSection();
	}

	protected SimpleComboPropertyDescriptor getSimpleComboControl(Composite parent) {
		if (simpleCombo == null) {
			simpleCombo = new PageLayoutComboPropertyDescriptor(true);
			if (getProvider() != null)
				simpleCombo.setDescriptorProvider(getProvider());
			simpleCombo.createControl(parent);
			simpleCombo.getControl().setLayoutData(new GridData());
			simpleCombo.getControl().addDisposeListener(new DisposeListener() {

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
