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

import org.eclipse.birt.report.designer.internal.ui.swt.custom.FormWidgetFactory;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.TabbedPropertyTitle;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.AdvancePropertyDescriptor;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.DescriptorToolkit;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class AdvancePropertySection extends Section {

	public AdvancePropertySection(String labelText, Composite parent, boolean isControlStyle) {
		super(labelText, parent, isControlStyle);
		// TODO Auto-generated constructor stub
	}

	boolean isTabbed = false;

	public AdvancePropertySection(String labelText, Composite parent, boolean isControlStyle, boolean isTabbed) {
		super(labelText, parent, isControlStyle);
		this.isTabbed = isTabbed;
	}

	private boolean showLabel = false;

	public void showDisplayLabel(boolean show) {
		this.showLabel = show;
	}

	protected AdvancePropertyDescriptor descriptor;

	@Override
	public void createSection() {
		if (isTabbed) {
			getTitleControl(parent);
		} else if (showLabel) {
			getLabelControl(parent);
		}
		getControl(parent);
		getGridPlaceholder(parent);

	}

	protected TabbedPropertyTitle title;

	public TabbedPropertyTitle getTitleControl() {
		return title;
	}

	protected TabbedPropertyTitle getTitleControl(Composite parent) {
		if (title == null) {
			title = new TabbedPropertyTitle(parent, FormWidgetFactory.getInstance());
			title.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			title.setFont(parent.getFont());
			title.setLayoutData(new GridData());
			String text = getLabelText();
			if (text != null) {
				title.setTitle(text, null);
			}
			title.addDisposeListener(new DisposeListener() {

				@Override
				public void widgetDisposed(DisposeEvent event) {
					title = null;
				}
			});
		} else {
			checkParent(title, parent);
		}
		return title;
	}

	public AdvancePropertyDescriptor getControl() {
		return descriptor;
	}

	protected AdvancePropertyDescriptor getControl(Composite parent) {
		if (descriptor == null) {
			descriptor = DescriptorToolkit.createAdvancePropertyDescriptor(true);
			if (getProvider() != null) {
				descriptor.setDescriptorProvider(getProvider());
			}
			descriptor.createControl(parent);
			descriptor.getControl().setLayoutData(new GridData());
			descriptor.getControl().addDisposeListener(new DisposeListener() {

				@Override
				public void widgetDisposed(DisposeEvent event) {
					descriptor = null;
				}
			});
		} else {
			checkParent(descriptor.getControl(), parent);
		}
		return descriptor;
	}

	int displayLabelStyle = SWT.VERTICAL;

	public void setDisplayLabelStyle(int style) {
		displayLabelStyle = style;
	}

	@Override
	public void layout() {
		GridData gd = (GridData) descriptor.getControl().getLayoutData();
		if (getLayoutNum() > 0) {
			gd.horizontalSpan = getLayoutNum() - placeholder;
		} else {
			gd.horizontalSpan = ((GridLayout) parent.getLayout()).numColumns - placeholder;
		}
		if (displayLabel != null && (displayLabelStyle & SWT.HORIZONTAL) != 0) {
			gd.horizontalSpan = gd.horizontalSpan - 1;
		}

		gd.horizontalAlignment = SWT.FILL;
		if (width > -1) {
			gd.widthHint = width;
			gd.grabExcessHorizontalSpace = false;
		} else {
			gd.grabExcessHorizontalSpace = fillControl;
		}

		if (height > -1) {
			gd.heightHint = height;
			gd.grabExcessVerticalSpace = false;
		} else {
			gd.grabExcessVerticalSpace = fillControl;
		}

		gd.grabExcessVerticalSpace = true;
		gd.verticalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalIndent = GridData.FILL;

		if (displayLabel != null) {
			if ((displayLabelStyle & SWT.VERTICAL) != 0) {
				gd = (GridData) displayLabel.getLayoutData();
				gd.horizontalSpan = ((GridLayout) parent.getLayout()).numColumns;
				gd.grabExcessHorizontalSpace = true;
				gd.horizontalAlignment = SWT.FILL;
			} else {
				gd = (GridData) displayLabel.getLayoutData();
				gd.verticalAlignment = SWT.BEGINNING;
			}
		}

		if (title != null) {
			gd = (GridData) title.getLayoutData();
			gd.horizontalSpan = ((GridLayout) parent.getLayout()).numColumns;
			gd.grabExcessHorizontalSpace = true;
			gd.horizontalAlignment = SWT.FILL;
		}

	}

	@Override
	public void load() {
		if (descriptor != null && !descriptor.getControl().isDisposed()) {
			descriptor.load();
		}

	}

	IDescriptorProvider provider;

	public IDescriptorProvider getProvider() {
		return provider;
	}

	public void setProvider(IDescriptorProvider provider) {
		this.provider = provider;
		if (descriptor != null) {
			descriptor.setDescriptorProvider(provider);
		}
	}

	private int height = -1;
	private int width = -1;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	@Override
	public void setInput(Object input) {
		assert (input != null);
		descriptor.setInput(input);
	}

	boolean fillControl = false;

	public boolean isFillControl() {
		return fillControl;
	}

	public void setFillControl(boolean fillControl) {
		this.fillControl = fillControl;
	}

	@Override
	public void setHidden(boolean isHidden) {
		if (displayLabel != null) {
			WidgetUtil.setExcludeGridData(displayLabel, isHidden);
		}
		if (title != null) {
			WidgetUtil.setExcludeGridData(title, isHidden);
		}
		if (descriptor != null) {
			descriptor.setHidden(isHidden);
		}
		if (placeholderLabel != null) {
			WidgetUtil.setExcludeGridData(placeholderLabel, isHidden);
		}
	}

	@Override
	public void setVisible(boolean isVisible) {
		if (displayLabel != null) {
			displayLabel.setVisible(isVisible);
		}
		if (title != null) {
			title.setVisible(isVisible);
		}
		if (descriptor != null) {
			descriptor.setVisible(isVisible);
		}
		if (placeholderLabel != null) {
			placeholderLabel.setVisible(isVisible);
		}
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}
