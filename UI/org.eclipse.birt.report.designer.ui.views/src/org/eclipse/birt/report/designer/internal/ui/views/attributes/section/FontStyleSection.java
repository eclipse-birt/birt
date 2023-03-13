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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.DescriptorToolkit;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.FontStylePropertyDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class FontStyleSection extends Section {

	public FontStyleSection(Composite parent, boolean isFormStyle) {
		super(" ", parent, isFormStyle); //$NON-NLS-1$
	}

	private boolean showLabel = false;

	public FontStyleSection(Composite parent, boolean isFormStyle, boolean showLable) {
		super(" ", parent, isFormStyle); //$NON-NLS-1$
		this.showLabel = showLable;
	}

	public FontStyleSection(Composite parent, String title, boolean isFormStyle) {
		super(title, parent, isFormStyle);
	}

	protected FontStylePropertyDescriptor fontStyle;

	protected IDescriptorProvider[] providers;

	@Override
	public void createSection() {
		if (showLabel) {
			getLabelControl(parent);
		}
		getFontStyleControl(parent);
		getGridPlaceholder(parent);
	}

	public FontStylePropertyDescriptor getFontStyleControl() {
		return fontStyle;
	}

	protected FontStylePropertyDescriptor getFontStyleControl(Composite parent) {
		if (fontStyle == null) {
			fontStyle = DescriptorToolkit.createFontStylePropertyDescriptor(isFormStyle);
			if (getProviders() != null) {
				fontStyle.setProviders(getProviders());
			}
			fontStyle.createControl(parent);
			fontStyle.getControl().setLayoutData(new GridData());
			fontStyle.getControl().addDisposeListener(new DisposeListener() {

				@Override
				public void widgetDisposed(DisposeEvent event) {
					fontStyle = null;
				}
			});
		} else {
			checkParent(fontStyle.getControl(), parent);
		}
		return fontStyle;
	}

	@Override
	public void load() {
		if (fontStyle != null && !fontStyle.getControl().isDisposed()) {
			fontStyle.load();
		}
	}

	@Override
	public void reset() {
		if (fontStyle != null && !fontStyle.getControl().isDisposed()) {
			fontStyle.reset();
		}
	}

	@Override
	public void setInput(Object input) {
		assert (input != null);
		fontStyle.setInput(input);
	}

	private int width = -1;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	@Override
	public void setHidden(boolean isHidden) {
		if (displayLabel != null) {
			WidgetUtil.setExcludeGridData(displayLabel, isHidden);
		}
		if (displayLabel != null) {
			fontStyle.setHidden(isHidden);
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
		if (fontStyle != null) {
			fontStyle.setVisible(isVisible);
		}
		if (placeholderLabel != null) {
			placeholderLabel.setVisible(isVisible);
		}
	}

	public IDescriptorProvider[] getProviders() {
		return providers;
	}

	public void setProviders(IDescriptorProvider[] providers) {
		this.providers = providers;
	}

	@Override
	public void layout() {
		GridData gd = (GridData) fontStyle.getControl().getLayoutData();
		if (getLayoutNum() > 0) {
			gd.horizontalSpan = getLayoutNum() - 1 - placeholder;
		} else {
			gd.horizontalSpan = ((GridLayout) parent.getLayout()).numColumns - 1 - placeholder;
		}
		gd.horizontalAlignment = SWT.FILL;
		if (width > -1) {
			gd.widthHint = width;
			gd.grabExcessHorizontalSpace = false;
		} else {
			gd.grabExcessHorizontalSpace = fillFontStyle;
		}

		if (height > -1) {
			gd.heightHint = width;
			gd.grabExcessVerticalSpace = false;
			if (displayLabel != null) {
				gd = (GridData) displayLabel.getLayoutData();
				gd.verticalAlignment = GridData.VERTICAL_ALIGN_FILL;
			}
		} else {
			gd.grabExcessVerticalSpace = fillFontStyle;
			if (fillFontStyle) {
				gd.verticalAlignment = GridData.FILL;
				if (displayLabel != null) {
					gd = (GridData) displayLabel.getLayoutData();
					gd.verticalAlignment = GridData.VERTICAL_ALIGN_FILL;
				}
			}

		}

	}

	private int height = -1;

	private boolean fillFontStyle;

	public boolean isFillText() {
		return fillFontStyle;
	}

	public void setFillText(boolean fillFontStyle) {
		this.fillFontStyle = fillFontStyle;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
