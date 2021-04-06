/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard.format.series;

import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.type.DialSeries;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.LineAttributesComposite;
import org.eclipse.birt.chart.ui.swt.composites.NeedleComposite;
import org.eclipse.birt.chart.ui.swt.composites.NeedleComposite.NeedleAttributesContext;
import org.eclipse.birt.chart.ui.swt.composites.TriggerDataComposite;
import org.eclipse.birt.chart.ui.swt.interfaces.ITaskPopupSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.SubtaskSheetImpl;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.InteractivitySheet;
import org.eclipse.birt.chart.util.TriggerSupportMatrix;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Actuate Corporation
 * 
 */
public class NeedleSheetImpl extends SubtaskSheetImpl implements SelectionListener {

	public void createControl(Composite parent) {
		cmpContent = new Composite(parent, SWT.NONE);
		{
			GridLayout glContent = new GridLayout(2, true);
			cmpContent.setLayout(glContent);
			GridData gd = new GridData(GridData.FILL_BOTH);
			cmpContent.setLayoutData(gd);
		}

		createNeedleComponent(cmpContent);

		createButtonGroup(cmpContent);
	}

	protected void createNeedleComponent(Composite cmpContent) {
		NeedleComposite cmpNeedle = new NeedleComposite(cmpContent, getContext(),
				(DialSeries) getSeriesDefinitionForProcessing().getDesignTimeSeries(), getNeedleAttributesContext());

		cmpNeedle.setLayoutData(new GridData(GridData.FILL_BOTH));
	}

	protected NeedleAttributesContext getNeedleAttributesContext() {
		NeedleAttributesContext needleAttrContext = new NeedleAttributesContext();

		needleAttrContext.lineOptionalStyles = LineAttributesComposite.ENABLE_WIDTH
				| LineAttributesComposite.ENABLE_STYLES;

		needleAttrContext.bEnableHeadStyle = true;

		return needleAttrContext;
	}

	private void createButtonGroup(Composite parent) {
		Composite cmp = new Composite(parent, SWT.NONE);
		{
			cmp.setLayout(new GridLayout(6, false));
			GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
			gridData.horizontalSpan = 2;
			gridData.grabExcessVerticalSpace = true;
			gridData.verticalAlignment = SWT.END;
			cmp.setLayoutData(gridData);
		}

		// Interactivity
		if (getContext().isInteractivityEnabled()) {
			ITaskPopupSheet popup = new InteractivitySheet(Messages.getString("SeriesYSheetImpl.Label.Interactivity"), //$NON-NLS-1$
					getContext(), getSeriesDefinitionForProcessing().getDesignTimeSeries().getTriggers(),
					getSeriesDefinitionForProcessing().getDesignTimeSeries(), TriggerSupportMatrix.TYPE_DATAPOINT,
					TriggerDataComposite.ENABLE_URL_PARAMETERS | TriggerDataComposite.ENABLE_TOOLTIP_FORMATTER);
			Button btnInteractivity = createToggleButton(cmp, BUTTON_INTERACTIVITY,
					Messages.getString("SeriesYSheetImpl.Label.Interactivity&"), //$NON-NLS-1$
					popup);
			btnInteractivity.addSelectionListener(this);
			btnInteractivity.setEnabled(getChart().getInteractivity().isEnable());
		}
	}

	public void widgetSelected(SelectionEvent e) {
		// Detach popup dialog if there's selected popup button.
		if (detachPopup(e.widget)) {
			return;
		}

		if (isRegistered(e.widget)) {
			attachPopup(((Button) e.widget).getData().toString());
		}
	}

	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
	}

	protected SeriesDefinition getSeriesDefinitionForProcessing() {
		SeriesDefinition sd = null;
		if (getChart() instanceof ChartWithAxes) {
			int iAxis = getParentAxisIndex(getIndex());
			int iAxisSeries = getSeriesIndexWithinAxis(getIndex());
			sd = ((ChartWithAxes) getChart()).getAxes().get(0).getAssociatedAxes().get(iAxis).getSeriesDefinitions()
					.get(iAxisSeries);
		} else if (getChart() instanceof ChartWithoutAxes) {
			sd = ((ChartWithoutAxes) getChart()).getSeriesDefinitions().get(0).getSeriesDefinitions().get(getIndex());
		}
		return sd;
	}

	private int getParentAxisIndex(int iSeriesDefinitionIndex) {
		int iTmp = 0;
		int iAxisCount = ((ChartWithAxes) getChart()).getAxes().get(0).getAssociatedAxes().size();
		for (int i = 0; i < iAxisCount; i++) {
			iTmp += ((ChartWithAxes) getChart()).getAxes().get(0).getAssociatedAxes().get(i).getSeriesDefinitions()
					.size();
			if (iTmp - 1 >= iSeriesDefinitionIndex) {
				return i;
			}
		}
		return 0;
	}

	private int getSeriesIndexWithinAxis(int iSeriesDefinitionIndex) {
		int iTotalDefinitions = 0;
		int iAxisCount = ((ChartWithAxes) getChart()).getAxes().get(0).getAssociatedAxes().size();
		for (int i = 0; i < iAxisCount; i++) {
			int iOldTotal = iTotalDefinitions;
			iTotalDefinitions += ((ChartWithAxes) getChart()).getAxes().get(0).getAssociatedAxes().get(i)
					.getSeriesDefinitions().size();
			if (iTotalDefinitions - 1 >= iSeriesDefinitionIndex) {
				return iSeriesDefinitionIndex - iOldTotal;
			}
		}
		return iSeriesDefinitionIndex;
	}
}