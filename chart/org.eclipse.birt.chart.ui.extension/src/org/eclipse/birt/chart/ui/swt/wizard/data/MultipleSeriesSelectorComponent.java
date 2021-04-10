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

package org.eclipse.birt.chart.ui.swt.wizard.data;

import org.eclipse.birt.chart.model.DialChart;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.DefaultSelectDataComponent;
import org.eclipse.birt.chart.ui.swt.fieldassist.FieldAssistHelper;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataCustomizeUI;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartUIConstants;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

/**
 * 
 */

public class MultipleSeriesSelectorComponent extends DefaultSelectDataComponent {

	private EList<SeriesDefinition>[] seriesDefnsArray;

	private ChartWizardContext wizardContext = null;

	private String sTitle = null;

	private Group cmpLeft;

	private DataDefinitionSelector[] selectors;

	private ISelectDataCustomizeUI selectDataUI = null;

	private String areaTitle = null;

	public MultipleSeriesSelectorComponent(EList<SeriesDefinition>[] seriesDefnsArray, ChartWizardContext wizardContext,
			String sTitle, ISelectDataCustomizeUI selectDataUI) {
		super();
		this.seriesDefnsArray = seriesDefnsArray;
		this.wizardContext = wizardContext;
		this.sTitle = sTitle;
		this.selectDataUI = selectDataUI;
		this.areaTitle = ChartUIUtil.getChartType(wizardContext.getModel().getType()).getValueDefinitionName();
	}

	protected DataDefinitionSelector createDataDefinitionSelector(int axisIndex, EList<SeriesDefinition> seriesDefns,
			ChartWizardContext wizardContext, String sTitle, ISelectDataCustomizeUI selectDataUI) {
		return new DataDefinitionSelector(axisIndex, seriesDefns, wizardContext, sTitle, selectDataUI);
	}

	public Composite createArea(Composite parent) {
		Label topAngle = new Label(parent, SWT.NONE);
		{
			topAngle.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
			topAngle.setImage(UIHelper.getImage(ChartUIConstants.IMAGE_RA_TOPRIGHT));
		}

		cmpLeft = new Group(parent, SWT.NONE);
		{
			GridLayout gridLayout = new GridLayout();
			gridLayout.marginWidth = 0;
			gridLayout.marginHeight = 0;
			cmpLeft.setLayout(gridLayout);
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			// gd.minimumWidth = 150;
			cmpLeft.setLayoutData(gd);
			if (FieldAssistHelper.getInstance().isShowingRequiredFieldIndicator()) {
				cmpLeft.setText(areaTitle.concat("*")); //$NON-NLS-1$
			} else {
				cmpLeft.setText(areaTitle);
			}
		}

		if (seriesDefnsArray.length > 2) {
			wizardContext.setMoreAxesSupported(true);
		}
		if (wizardContext.isMoreAxesSupported()) {
			selectors = new DataDefinitionSelector[1];
			selectors[0] = new DataDefinitionSelector(wizardContext, sTitle, selectDataUI);
			if (wizardContext.getModel() instanceof DialChart) {
				selectors[0].setSelectionPrefix(Messages.getString("DialBottomAreaComponent.Label.Dial")); //$NON-NLS-1$
			}
			selectors[0].createArea(cmpLeft);
		} else {
			selectors = new DataDefinitionSelector[seriesDefnsArray.length];
			for (int i = 0; i < seriesDefnsArray.length; i++) {
				// Remove the title when only single series, i.e. axisIndex is
				// -1
				int axisIndex = seriesDefnsArray.length == 1 ? -1 : i;
				selectors[i] = createDataDefinitionSelector(axisIndex, seriesDefnsArray[i], wizardContext, sTitle,
						selectDataUI);
				if (wizardContext.getModel() instanceof DialChart) {
					selectors[i].setSelectionPrefix(Messages.getString("DialBottomAreaComponent.Label.Dial")); //$NON-NLS-1$
				}
				selectors[i].createArea(cmpLeft);
			}
		}

		Label bottomAngle = new Label(parent, SWT.NONE);
		{
			bottomAngle.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
			bottomAngle.setImage(UIHelper.getImage(ChartUIConstants.IMAGE_RA_BOTTOMRIGHT));
		}

		return cmpLeft;
	}

	public void selectArea(boolean selected, Object data) {
		for (int i = 0; i < selectors.length; i++) {
			selectors[i].selectArea(selected, data);
		}
	}

	public void dispose() {
		for (int i = 0; i < selectors.length; i++) {
			selectors[i].dispose();
		}
		super.dispose();
	}
}
