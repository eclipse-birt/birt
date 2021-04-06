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

import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.DefaultSelectDataComponent;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataComponent;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartUIConstants;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * Default data binding component for base series
 */

public class DefaultBaseSeriesComponent extends DefaultSelectDataComponent {

	private SeriesDefinition seriesDefn;

	private ChartWizardContext context = null;

	private String sTitle = null;

	private String labelText = Messages.getString("BarBottomAreaComponent.Label.CategoryXSeries"); //$NON-NLS-1$

	private String tooltipWhenBlank = null;

	private ISelectDataComponent comData;

	public DefaultBaseSeriesComponent(SeriesDefinition seriesDefn, ChartWizardContext context, String sTitle) {
		super();
		this.seriesDefn = seriesDefn;
		this.context = context;
		this.sTitle = sTitle;
	}

	public Composite createArea(Composite parent) {
		Composite cmpBottom = new Composite(parent, SWT.NONE);
		{
			GridLayout gridLayout = new GridLayout(3, false);
			gridLayout.marginWidth = 10;
			gridLayout.marginHeight = 0;
			cmpBottom.setLayout(gridLayout);
			GridData gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
			cmpBottom.setLayoutData(gridData);
		}

		Label leftAngle = new Label(cmpBottom, SWT.NONE);
		{
			GridData gridData = new GridData();
			leftAngle.setLayoutData(gridData);
			leftAngle.setImage(UIHelper.getImage(ChartUIConstants.IMAGE_RA_LEFTUP));
			leftAngle.getImage().setBackground(leftAngle.getBackground());
		}

		comData = new BaseDataDefinitionComponent(BaseDataDefinitionComponent.BUTTON_GROUP,
				ChartUIConstants.QUERY_CATEGORY, seriesDefn, ChartUIUtil.getDataQuery(seriesDefn, 0), context, sTitle);
		((BaseDataDefinitionComponent) comData).setDescription(labelText);
		if (tooltipWhenBlank != null) {
			((BaseDataDefinitionComponent) comData).setTooltipWhenBlank(tooltipWhenBlank);
		}
		comData.createArea(cmpBottom);
		((BaseDataDefinitionComponent) comData).bindAssociatedName(labelText);

		Label rightAngle = new Label(cmpBottom, SWT.NONE);
		rightAngle.setImage(UIHelper.getImage(ChartUIConstants.IMAGE_RA_RIGHTUP));
		rightAngle.getImage().setBackground(rightAngle.getBackground());

		return cmpBottom;
	}

	public void selectArea(boolean selected, Object data) {
		comData.selectArea(selected, data);
	}

	public void dispose() {
		comData.dispose();
		super.dispose();
	}

	public void setLabelText(String labelText) {
		this.labelText = labelText;
	}

	public void setTooltipWhenBlank(String tootipWhenBlank) {
		this.tooltipWhenBlank = tootipWhenBlank;
	}
}
