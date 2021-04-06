/*******************************************************************************
 * Copyright (c) 2007, 2008 Actuate Corporation.
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

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.SeriesGrouping;
import org.eclipse.birt.chart.model.data.impl.SeriesGroupingImpl;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

/**
 * The dialog is used to set Y grouping and sorting attributes.
 * 
 * @since BIRT 2.3
 */
public class YOptionalGroupSortingDialog extends GroupSortingDialog {

	/**
	 * Constructor of the class.
	 * 
	 * @param shell
	 * @param wizardContext
	 * @param sd
	 * @param disableAggregation
	 */
	public YOptionalGroupSortingDialog(Shell shell, ChartWizardContext wizardContext, SeriesDefinition sd,
			boolean disableAggregation) {
		super(shell, wizardContext, sd, disableAggregation);
	}

	@Override
	protected Set<String> getSortKeySet() {
		Set<String> exprSet = new LinkedHashSet<String>();
		exprSet.addAll(getYGroupingExpressions());
		exprSet.addAll(getValueSeriesExpressions());
		return exprSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.ui.swt.composites.GroupSortingDialog#createSortArea(
	 * org.eclipse.swt.widgets.Composite)
	 */
	public void createSortArea(Composite parent) {
		super.createSortArea(parent);
		if (!isYGroupingEnabled()) {
			cmpSortArea.setEnabled(false);
			lblSorting.setEnabled(false);
			cmbSorting.setEnabled(false);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.composites.GroupSortingDialog#
	 * createSeriesGroupingComposite(org.eclipse.swt.widgets.Composite)
	 */
	protected SeriesGroupingComposite createSeriesGroupingComposite(Composite parent) {
		SeriesGrouping grouping = getSeriesDefinitionForProcessing().getQuery().getGrouping();
		if (grouping == null) {
			grouping = SeriesGroupingImpl.create();
			getSeriesDefinitionForProcessing().getQuery().setGrouping(grouping);
		}

		SeriesGroupingComposite sgc = new YSeriesGroupingComposite(parent, SWT.NONE, grouping, fEnableAggregation,
				wizardContext, null);
		sgc.setGroupingButtionEnabled(false);
		return sgc;
	}

	/**
	 * 
	 */
	class YSeriesGroupingComposite extends SeriesGroupingComposite {

		public YSeriesGroupingComposite(Composite parent, int style, SeriesGrouping grouping, boolean aggEnabled,
				ChartWizardContext context, String title) {
			super(parent, style, grouping, aggEnabled, context, title);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.chart.ui.swt.composites.SeriesGroupingComposite#
		 * setGroupingButtonStatus()
		 */
		protected void setGroupingButtonSelection() {
			Query query = getSeriesDefinitionForProcessing().getQuery();
			if (query != null && query.getDefinition() != null && !"".equals(query.getDefinition())) //$NON-NLS-1$
			{
				btnEnabled.setSelection(true);
			} else {
				btnEnabled.setSelection(false);
			}
		}
	}

	/**
	 * Get the Y Grouping expression.
	 * 
	 * @return
	 */
	protected Set<String> getYGroupingExpressions() {
		Set<String> exprSet = new LinkedHashSet<String>();
		Chart chart = wizardContext.getModel();
		if (chart instanceof ChartWithAxes) {
			ChartWithAxes cwa = (ChartWithAxes) chart;
			final Axis axPrimaryBase = cwa.getPrimaryBaseAxes()[0];

			// Add expressions of value series.
			for (Axis axOrthogonal : cwa.getOrthogonalAxes(axPrimaryBase, true)) {
				for (SeriesDefinition orthoSD : axOrthogonal.getSeriesDefinitions()) {
					if (orthoSD.getQuery() != null && orthoSD.getQuery().getDefinition() != null) {
						exprSet.add(orthoSD.getQuery().getDefinition());
					}
				}
			}
		} else {
			ChartWithoutAxes cwoa = (ChartWithoutAxes) chart;
			for (SeriesDefinition sd : cwoa.getSeriesDefinitions()) {
				// Add value series expressions.
				for (SeriesDefinition orthSD : sd.getSeriesDefinitions()) {
					if (orthSD.getQuery() != null && orthSD.getQuery().getDefinition() != null) {
						exprSet.add(orthSD.getQuery().getDefinition());
					}
				}
			}
		}

		return exprSet;
	}
}
