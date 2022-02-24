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
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.BorderToggleDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.BorderPropertyDescriptor;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.DescriptorToolkit;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class BorderSection extends Section {

	public BorderSection(String labelText, Composite parent, boolean isFormStyle) {
		super(labelText, parent, isFormStyle);
		// TODO Auto-generated constructor stub
	}

	boolean isTabbed = false;

	public BorderSection(String labelText, Composite parent, boolean isFormStyle, boolean isTabbed) {
		super(labelText, parent, isFormStyle);
		this.isTabbed = isTabbed;
	}

	private boolean showLabel = false;

	public void showDisplayLabel(boolean show) {
		this.showLabel = show;
	}

	protected BorderPropertyDescriptor border;

	@Override
	public void createSection() {
		if (isTabbed) {
			getTitleControl(parent);
		} else if (showLabel) {
			getLabelControl(parent);
		}
		getBorderControl(parent);
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

	public BorderPropertyDescriptor getBorderControl() {
		return border;
	}

	protected BorderPropertyDescriptor getBorderControl(Composite parent) {
		if (border == null) {
			border = DescriptorToolkit.createBorderPropertyDescriptor(true);
			if (style != -1) {
				border.setStyle(style);
			}
			if (getStyleProvider() != null) {
				border.setStyleProvider(getStyleProvider());
			}
			if (getColorProvider() != null) {
				border.setColorProvider(getColorProvider());
			}
			if (getWidthProvider() != null) {
				border.setWidthProvider(getWidthProvider());
			}
			if (getToggleProviders() != null) {
				border.setToggleProviders(getToggleProviders());
			}
			border.createControl(parent);
			border.getControl().setLayoutData(new GridData());
			border.getControl().addDisposeListener(new DisposeListener() {

				@Override
				public void widgetDisposed(DisposeEvent event) {
					border = null;
				}
			});
		} else {
			checkParent(border.getControl(), parent);
		}
		return border;
	}

	int displayLabelStyle = SWT.VERTICAL;

	public void setDisplayLabelStyle(int style) {
		displayLabelStyle = style;
	}

	@Override
	public void layout() {
		GridData gd = (GridData) border.getControl().getLayoutData();
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
			gd.grabExcessHorizontalSpace = fillBorder;
		}

		if (height > -1) {
			gd.heightHint = height;
			gd.grabExcessVerticalSpace = false;
		} else {
			gd.grabExcessVerticalSpace = fillBorder;
		}

		gd.grabExcessVerticalSpace = true;
		gd.verticalAlignment = GridData.FILL;

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
		if (border != null && !border.getControl().isDisposed()) {
			border.load();
		}
	}

	@Override
	public void reset() {
		if (border != null && !border.getControl().isDisposed()) {
			border.reset();
		}
	}

	IDescriptorProvider styleProvider;

	public IDescriptorProvider getStyleProvider() {
		return styleProvider;
	}

	public void setStyleProvider(IDescriptorProvider provider) {
		this.styleProvider = provider;
		if (border != null) {
			border.setStyleProvider(provider);
		}
	}

	IDescriptorProvider colorProvider;

	public IDescriptorProvider getColorProvider() {
		return colorProvider;
	}

	public void setColorProvider(IDescriptorProvider provider) {
		this.colorProvider = provider;
		if (border != null) {
			border.setColorProvider(provider);
		}
	}

	IDescriptorProvider widthProvider;

	public IDescriptorProvider getWidthProvider() {
		return widthProvider;
	}

	public void setWidthProvider(IDescriptorProvider provider) {
		this.widthProvider = provider;
		if (border != null) {
			border.setWidthProvider(provider);
		}
	}

	BorderToggleDescriptorProvider[] toggleProviders;

	public BorderToggleDescriptorProvider[] getToggleProviders() {
		return toggleProviders;
	}

	public void setToggleProviders(BorderToggleDescriptorProvider[] toggleProviders) {
		this.toggleProviders = toggleProviders;
		if (border != null) {
			border.setToggleProviders(toggleProviders);
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
		border.setInput(input);
	}

	boolean fillBorder = false;

	public boolean isFillBorder() {
		return fillBorder;
	}

	public void setFillBorder(boolean fillBorder) {
		this.fillBorder = fillBorder;
	}

	@Override
	public void setHidden(boolean isHidden) {
		if (displayLabel != null) {
			WidgetUtil.setExcludeGridData(displayLabel, isHidden);
		}
		if (title != null) {
			WidgetUtil.setExcludeGridData(title, isHidden);
		}
		if (border != null) {
			border.setHidden(isHidden);
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
		if (border != null) {
			border.setVisible(isVisible);
		}
		if (placeholderLabel != null) {
			placeholderLabel.setVisible(isVisible);
		}
	}

	private int style = -1;

	public int getStyle() {
		return style;
	}

	public void setStyle(int style) {
		this.style = style;
		if (border != null) {
			border.setStyle(style);
		}
	}

	boolean withDialog = false;

	public void setButtonWithDialog(boolean withDialog) {
		this.withDialog = withDialog;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}
