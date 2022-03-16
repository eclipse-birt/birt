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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextSection;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Administrator
 *
 */
public class InnerTextSection extends TextSection {
	public InnerTextSection(String labelText, Composite parent, boolean formStyle) {
		super(labelText, parent, formStyle);
		// TODO Auto-generated constructor stub
	}

	protected ContainerSection section;

	public InnerTextSection(String labelText, ContainerSection section, boolean isFormStyle) {
		super(labelText, null, isFormStyle);
		this.section = section;
	}

	@Override
	public void createSection() {
		if (parent == null && section != null) {
			parent = section.getContainerComposite();
		}
		super.createSection();
	}
}
