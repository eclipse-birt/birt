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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.Section;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

/**
 * @author Administrator
 * 
 */
public class ContainerSection extends Section {

	// CheckPropertyDescriptor columnCheck;

	public Group group;

	public ContainerSection(String labelText, Composite parent, boolean isFormStyle) {
		super(labelText, parent, isFormStyle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.attributes.section.Section
	 * #createSection()
	 */
	public void createSection() {
		getGroupSection(parent);
	}

	public Composite getContainerComposite() {
		return group;
	}

	private Group getGroupSection(Composite parent) {
		if (group == null) {
			group = new Group(parent, SWT.NONE);
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = 3;
			group.setLayout(WidgetUtil.createGridLayout(3));
			group.setText(getLabelText());
			group.setLayoutData(gd);

		}
		return group;
	}

	public void layout() {
		GridData gd = (GridData) group.getLayoutData();
		if (getLayoutNum() > 0)
			gd.horizontalSpan = getLayoutNum() - placeholder;
		else
			gd.horizontalSpan = ((GridLayout) parent.getLayout()).numColumns - placeholder;
		if (width > -1) {
			gd.widthHint = width;
			gd.grabExcessHorizontalSpace = false;
		} else
			gd.grabExcessHorizontalSpace = true;
	}

	private int width = -1;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void load() {
		// if(group!=null && !group.isDisposed( ))group.load( );
	}

	IDescriptorProvider provider;

	public IDescriptorProvider getProvider() {
		return provider;
	}

	public void setProvider(IDescriptorProvider provider) {
	}

	public void setInput(Object input) {

	}

	public void setHidden(boolean isHidden) {
		if (group != null)
			WidgetUtil.setExcludeGridData(group, isHidden);

	}

	public void setVisible(boolean isVisable) {
		if (group != null)
			group.setVisible(isVisable);

	}

}
