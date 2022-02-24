/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.attributes.section;

import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.DescriptorToolkit;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.MarignPropertyDescriptor;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class MarignSection extends Section {

	protected MarignPropertyDescriptor marign;

	public MarignSection(String labelText, Composite parent, boolean isFormStyle) {
		super(labelText, parent, isFormStyle);
		// TODO Auto-generated constructor stub
	}

	public void createSection() {
		getLabelControl(parent);
		getMarignControl(parent);
		getGridPlaceholder(parent);
	}

	public void layout() {
		GridData gd = (GridData) marign.getControl().getLayoutData();
		if (getLayoutNum() > 0)
			gd.horizontalSpan = getLayoutNum() - 1 - placeholder;
		else
			gd.horizontalSpan = ((GridLayout) parent.getLayout()).numColumns - 1 - placeholder;
		if (width > -1) {
			gd.widthHint = width;
			gd.grabExcessHorizontalSpace = false;
		} else
			gd.grabExcessHorizontalSpace = fillMarign;
	}

	public MarignPropertyDescriptor getMarignControl() {
		return marign;
	}

	protected MarignPropertyDescriptor getMarignControl(Composite parent) {
		if (marign == null) {
			marign = DescriptorToolkit.createMarignPropertyDescriptor(isFormStyle);
			if (getProvider() != null)
				marign.setDescriptorProvider(getProvider());
			marign.createControl(parent);
			marign.getControl().setLayoutData(new GridData());
			marign.getControl().addDisposeListener(new DisposeListener() {

				public void widgetDisposed(DisposeEvent event) {
					marign = null;
				}
			});

			setAccessible(marign.getControl());
		} else {
			checkParent(marign.getControl(), parent);
		}
		return marign;
	}

	private void setAccessible(final Control control) {
		if (control instanceof Composite) {
			Composite parent = (Composite) control;
			if (parent != null && parent.getTabList() != null) {
				Control[] children = parent.getTabList();
				for (int i = 0; i < children.length; i++) {
					setAccessible(children[i]);
				}
			}
		} else {
			control.getAccessible().addAccessibleListener(new AccessibleAdapter() {

				public void getName(AccessibleEvent e) {
					Label lbl = getLabelControl();
					if (lbl != null) {
						if (control instanceof Text) {
							e.result = UIUtil.stripMnemonic(getLabelControl().getText()) + ((Text) control).getText();
						}
					}
				}

			});
		}
	}

	IDescriptorProvider provider;

	public IDescriptorProvider getProvider() {
		return provider;
	}

	public void setProvider(IDescriptorProvider provider) {
		this.provider = provider;
		if (marign != null)
			marign.setDescriptorProvider(provider);
	}

	private int width = -1;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setInput(Object input) {
		assert (input != null);
		marign.setInput(input);
	}

	private boolean fillMarign = false;

	public boolean isFillMarign() {
		return fillMarign;
	}

	public void setFillMarign(boolean fillMarign) {
		this.fillMarign = fillMarign;
	}

	public void load() {
		if (marign != null && !marign.getControl().isDisposed())
			marign.load();
	}

	public void reset() {
		if (marign != null && !marign.getControl().isDisposed()) {
			marign.reset();
		}
	}

	public void setHidden(boolean isHidden) {
		if (displayLabel != null)
			WidgetUtil.setExcludeGridData(displayLabel, isHidden);
		if (marign != null)
			marign.setHidden(isHidden);
		if (placeholderLabel != null)
			WidgetUtil.setExcludeGridData(placeholderLabel, isHidden);
	}

	public void setVisible(boolean isVisible) {
		if (displayLabel != null)
			displayLabel.setVisible(isVisible);
		if (marign != null)
			marign.setVisible(isVisible);
		if (placeholderLabel != null)
			placeholderLabel.setVisible(isVisible);
	}

}
