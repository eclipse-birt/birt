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

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

/**
 * AbstractBindingDialogHelper
 */
public abstract class AbstractBindingDialogHelper implements IBindingDialogHelper {

	protected ReportItemHandle bindingHolder;
	protected ComputedColumnHandle binding;
	protected DataColumnBindingDialog dialog;
	private boolean isAggregate = false;
	private boolean isMeasure = false;
	private boolean isTimePeriod = false;

	protected ExpressionProvider expressionProvider;
	private Object itemContainer;

	private String[] groups = new String[0];

	public boolean isAggregate() {
		return isAggregate;
	}

	public void setAggregate(boolean isAggregate) {
		this.isAggregate = isAggregate;
	}

	public boolean isMeasure() {
		return isMeasure;
	}

	public void setMeasure(boolean isMeasure) {
		this.isMeasure = isMeasure;
	}

	public boolean isTimePeriod() {
		return isTimePeriod;
	}

	public void setTimePeriod(boolean timePeriod) {
		this.isTimePeriod = timePeriod;
	}

	public ReportItemHandle getBindingHolder() {
		return bindingHolder;
	}

	public void setBindingHolder(ReportItemHandle bindingHolder) {
		this.bindingHolder = bindingHolder;
	}

	public ComputedColumnHandle getBinding() {
		return binding;
	}

	public void setBinding(ComputedColumnHandle binding) {
		this.binding = binding;
		if (this.binding != null) {
			setAggregate(
					this.binding.getAggregateFunction() != null && !this.binding.getAggregateFunction().equals("")); //$NON-NLS-1$
			if (!isAggregate()) {
				setMeasure(this.binding.getAggregateOn() != null && !this.binding.getAggregateOn().equals("")); //$NON-NLS-1$
			}
			if (isMeasure() && !isAggregate()) {
				setAggregate(true);
			}
		}
		if (this.binding != null) {
			setTimePeriod(this.binding.getTimeDimension() != null && !this.binding.getTimeDimension().equals(""));//$NON-NLS-1$
		}
	}

	public ComputedColumnHandle getBindingColumn() {
		return this.binding;
	}

	public DataColumnBindingDialog getDialog() {
		return dialog;
	}

	public void setDialog(DataColumnBindingDialog dialog) {
		this.dialog = dialog;
	}

	public ExpressionProvider getExpressionProvider() {
		return expressionProvider;
	}

	public void setExpressionProvider(ExpressionProvider expressionProvider) {
		this.expressionProvider = expressionProvider;
	}

	public void setDataItemContainer(Object itemContainer) {
		this.itemContainer = itemContainer;
	}

	public Object getDataItemContainer() {
		return this.itemContainer;
	}

	public boolean canProcessWithWarning() {
		return true;
	}

	public boolean canProcessAggregation() {
		if (bindingHolder != null && bindingHolder instanceof ListingHandle)
			return true;
		return false;
	}

	protected void setContentSize(Composite composite) {
		Point size = composite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		composite.setSize(Math.max(size.x, 400), Math.max(size.y, isAggregate() ? 320 : 50));
	}

	public void setEditModal(boolean isEditModal) {

	}

	@Override
	public boolean canProcessMeasure() {
		return false;
	}

	@Override
	public void setGroups(String[] groups) {
		this.groups = groups;
	}

	@Override
	public String[] getGroups() {
		return this.groups;
	}
}
