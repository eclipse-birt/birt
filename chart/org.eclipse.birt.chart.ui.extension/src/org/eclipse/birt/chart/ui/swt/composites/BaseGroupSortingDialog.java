/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.composites;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

/**
 * The dialog is used to set grouping and sort condition of base series.
 */
public class BaseGroupSortingDialog extends GroupSortingDialog {
	public BaseGroupSortingDialog(Shell shell, ChartWizardContext wizardContext, SeriesDefinition sd) {
		super(shell, wizardContext, sd);
	}

	public void createSortArea(Composite parent) {
		super.createSortArea(parent);

		if (onlyCategoryExprAsCategorySortKey()) {
			setSortKeySelectionState(false);
		}
	}

	@Override
	protected Set<String> getSortKeySet() {
		Set<String> exprSet = new LinkedHashSet<String>();

		if (onlyCategoryExprAsCategorySortKey()) {
			exprSet.add((String) getBaseSeriesExpression().toArray()[0]);
		} else {
			exprSet.addAll(getBaseSeriesExpression());
			exprSet.addAll(getValueSeriesExpressions());
		}

		return exprSet;
	}

	protected void updateSortKeySelectionState() {
		setSortKeySelectionState(isSortEnabled() && !onlyCategoryExprAsCategorySortKey());
	}

}
