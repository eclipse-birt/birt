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
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.ExpressionComposite;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.OutputPropertyDescriptor;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

public class OutputSection extends Section {

	public OutputSection(Composite parent, boolean isFormStyle) {
		super(" ", parent, isFormStyle); //$NON-NLS-1$
	}

	protected OutputPropertyDescriptor output;

	public void createSection() {
		getOutputControl(parent);
		getGridPlaceholder(parent);
	}

	public OutputPropertyDescriptor getOutputControl() {
		return output;
	}

	protected OutputPropertyDescriptor getOutputControl(Composite parent) {
		if (output == null) {
			output = new OutputPropertyDescriptor(true);
			output.setDescriptorProvider(provider);
			output.createControl(parent);
			output.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
			output.getControl().addDisposeListener(new DisposeListener() {

				public void widgetDisposed(DisposeEvent event) {
					output = null;
				}
			});

			setAccessible(output.getControl());

		} else {
			checkParent(output.getControl(), parent);
		}
		return output;
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
					if (control instanceof Text && control.getParent() instanceof ExpressionComposite) {
						e.result = UIUtil.stripMnemonic(Messages.getString("VisibilityPage.Label.Expression")) //$NON-NLS-1$
								+ ((Text) control).getText();
					}
				}
			});
		}
	}

	public void layout() {
		GridData gd = (GridData) output.getControl().getLayoutData();
		if (getLayoutNum() > 0)
			gd.horizontalSpan = getLayoutNum() - placeholder;
		else
			gd.horizontalSpan = ((GridLayout) parent.getLayout()).numColumns - placeholder;
		if (height > -1) {
			gd.heightHint = height;
			gd.grabExcessVerticalSpace = false;
			if (displayLabel != null) {
				gd = (GridData) displayLabel.getLayoutData();
				gd.verticalAlignment = GridData.VERTICAL_ALIGN_FILL;
			}
		}
	}

	private int height = -1;

	public void setHeight(int height) {
		this.height = height;
	}

	public void load() {
		if (output != null && !output.getControl().isDisposed())
			output.load();
	}

	IDescriptorProvider provider;

	public IDescriptorProvider getProvider() {
		return provider;
	}

	public void setProvider(IDescriptorProvider provider) {
		this.provider = provider;
		if (output != null)
			output.setDescriptorProvider(provider);
	}

	public void setInput(Object input) {
		assert (input != null);
		output.setInput(input);
	}

	public void setHidden(boolean isHidden) {
		if (output != null)
			WidgetUtil.setExcludeGridData(output.getControl(), isHidden);

	}

	public void setVisible(boolean isVisable) {
		if (output != null)
			output.getControl().setVisible(isVisable);

	}

	public void reset() {
		if (output != null && !output.getControl().isDisposed()) {
			output.reset();
		}
	}

}
