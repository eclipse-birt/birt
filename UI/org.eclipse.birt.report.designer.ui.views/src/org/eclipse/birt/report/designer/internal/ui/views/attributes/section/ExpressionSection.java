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
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.ExpressionPropertyDescriptor;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class ExpressionSection extends Section {

	public ExpressionSection(String labelText, Composite parent, boolean isFormStyle) {
		super(labelText, parent, isFormStyle);
		// TODO Auto-generated constructor stub
	}

	protected ExpressionPropertyDescriptor expression;

	@Override
	public void createSection() {
		getLabelControl(parent);
		getExpressionControl(parent);
		getGridPlaceholder(parent);

	}

	@Override
	protected Label getLabelControl(Composite parent) {
		Label label = super.getLabelControl(parent);
		if (multi) {
			GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_BEGINNING);
			gd.horizontalSpan = 2;
			label.setLayoutData(gd);
		}
		return label;
	}

	protected ExpressionPropertyDescriptor getExpressionControl(Composite parent) {
		if (expression == null) {
			expression = DescriptorToolkit.createExpressionPropertyDescriptor(true);
			expression.setMulti(multi);
			if (getProvider() != null) {
				expression.setDescriptorProvider(getProvider());
			}
			expression.createControl(parent);
			expression.getControl().setLayoutData(new GridData());
			expression.getControl().addDisposeListener(new DisposeListener() {

				@Override
				public void widgetDisposed(DisposeEvent event) {
					expression = null;
				}
			});
			// if ( buttonText != null )
			// expression.setButtonText( buttonText );
		} else {
			checkParent(expression.getControl(), parent);
		}
		return expression;
	}

	public ExpressionPropertyDescriptor getExpressionControl() {
		return expression;
	}

	@Override
	public void layout() {
		GridData gd = (GridData) expression.getControl().getLayoutData();
		if (getLayoutNum() > 1 + placeholder) {
			gd.horizontalSpan = getLayoutNum() - 1 - placeholder;
		} else if (((GridLayout) parent.getLayout()).numColumns > -1 - placeholder) {
			gd.horizontalSpan = ((GridLayout) parent.getLayout()).numColumns - placeholder;
		}
		// gd.horizontalAlignment = SWT.FILL;
		if (width > -1) {
			gd.widthHint = width;
			gd.grabExcessHorizontalSpace = false;
		} else {
			gd.grabExcessHorizontalSpace = fillColor;
		}
		if (multi) {
			gd.grabExcessVerticalSpace = true;
			gd.verticalAlignment = GridData.FILL;
		}
	}

	@Override
	public void load() {
		if (expression != null && !expression.getControl().isDisposed()) {
			expression.load();
		}

	}

	IDescriptorProvider provider;

	public IDescriptorProvider getProvider() {
		return provider;
	}

	public void setProvider(IDescriptorProvider provider) {
		this.provider = provider;
		if (expression != null) {
			expression.setDescriptorProvider(provider);
		}
	}

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
		expression.setInput(input);
	}

	boolean fillColor = false;

	public boolean isFillColor() {
		return fillColor;
	}

	public void setFillColor(boolean fillColor) {
		this.fillColor = fillColor;
	}

	boolean multi = true;

	public boolean isMulti() {
		return multi;
	}

	public void setMulti(boolean multi) {
		this.multi = multi;
	}

	@Override
	public void setHidden(boolean isHidden) {
		if (displayLabel != null) {
			WidgetUtil.setExcludeGridData(displayLabel, isHidden);
		}
		if (expression != null) {
			expression.setHidden(isHidden);
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
		if (expression != null) {
			expression.setVisible(isVisible);
		}
		if (placeholderLabel != null) {
			placeholderLabel.setVisible(isVisible);
		}
	}
}
