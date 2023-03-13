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
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.PreviewPropertyDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class PreviewSection extends Section {

	public PreviewSection(String labelText, Composite parent, boolean isFormStyle) {
		super(labelText, parent, isFormStyle);
		// TODO Auto-generated constructor stub
	}

	boolean isTabbed = false;

	public PreviewSection(String labelText, Composite parent, boolean isFormStyle, boolean isTabbed) {
		super(labelText, parent, isFormStyle);
		this.isTabbed = isTabbed;
	}

	private boolean showLabel = false;

	public void showDisplayLabel(boolean show) {
		this.showLabel = show;
	}

	@Override
	public void createSection() {
		if (isTabbed) {
			getTitleControl(parent);
		} else if (showLabel) {
			getLabelControl(parent);
		}
		getPreviewControl(parent);
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

	@Override
	public void layout() {
		GridData gd = (GridData) preview.getControl().getLayoutData();
		if (getLayoutNum() > 0) {
			gd.horizontalSpan = getLayoutNum() - placeholder;
		} else {
			gd.horizontalSpan = ((GridLayout) parent.getLayout()).numColumns - placeholder;
		}
		gd.horizontalAlignment = SWT.FILL;
		if (width > -1) {
			gd.widthHint = width;
			gd.grabExcessHorizontalSpace = false;
		} else {
			gd.grabExcessHorizontalSpace = fillForm;
		}

		if (height > -1) {
			if (height > preview.getControl().computeSize(SWT.DEFAULT, SWT.DEFAULT).y) {
				gd.heightHint = height;
			} else {
				gd.heightHint = preview.getControl().computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
			}
			gd.grabExcessVerticalSpace = false;
		} else {
			gd.grabExcessVerticalSpace = fillForm;
		}

		gd.grabExcessVerticalSpace = true;
		gd.verticalAlignment = GridData.FILL;

		if (displayLabel != null) {
			gd = (GridData) displayLabel.getLayoutData();
			gd.horizontalSpan = ((GridLayout) parent.getLayout()).numColumns;
			gd.grabExcessHorizontalSpace = true;
			gd.horizontalAlignment = SWT.FILL;
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
		if (preview != null && !preview.getControl().isDisposed()) {
			preview.load();
		}

	}

	IDescriptorProvider provider;

	public IDescriptorProvider getProvider() {
		return provider;
	}

	public void setProvider(IDescriptorProvider provider) {
		this.provider = provider;
		if (preview != null) {
			preview.setDescriptorProvider(provider);
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
		preview.setInput(input);
	}

	boolean fillForm = false;

	public boolean isFillForm() {
		return fillForm;
	}

	public void setFillPreview(boolean fillForm) {
		this.fillForm = fillForm;
	}

	@Override
	public void setHidden(boolean isHidden) {
		if (displayLabel != null) {
			WidgetUtil.setExcludeGridData(displayLabel, isHidden);
		}
		if (title != null) {
			WidgetUtil.setExcludeGridData(title, isHidden);
		}
		if (preview != null) {
			preview.setHidden(isHidden);
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
		if (preview != null) {
			preview.setVisible(isVisible);
		}
		if (placeholderLabel != null) {
			placeholderLabel.setVisible(isVisible);
		}
	}

	protected PreviewPropertyDescriptor preview;

	public PreviewPropertyDescriptor getPreviewControl() {
		return preview;
	}

	protected PreviewPropertyDescriptor getPreviewControl(Composite parent) {
		if (preview == null) {
			if (customPreview == null) {
				return null;
			} else {
				preview = customPreview;
			}
			preview.setDescriptorProvider(provider);
			preview.createControl(parent);
			preview.getControl().addDisposeListener(new DisposeListener() {

				@Override
				public void widgetDisposed(DisposeEvent event) {
					preview = null;
				}
			});
		} else {
			checkParent(preview.getControl(), parent);
		}
		return preview;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	PreviewPropertyDescriptor customPreview;

	public void setPreview(PreviewPropertyDescriptor preview) {
		this.customPreview = preview;
	}

}
