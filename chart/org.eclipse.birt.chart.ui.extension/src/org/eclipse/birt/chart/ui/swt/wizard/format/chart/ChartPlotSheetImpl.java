/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard.format.chart;

import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.util.DefaultValueProvider;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.ChartCheckbox;
import org.eclipse.birt.chart.ui.swt.composites.FillChooserComposite;
import org.eclipse.birt.chart.ui.swt.interfaces.ITaskPopupSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.SubtaskSheetImpl;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart.PlotClientAreaSheet;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * Sheet for plot settings
 * 
 */
public class ChartPlotSheetImpl extends SubtaskSheetImpl implements Listener, SelectionListener {

	private ChartCheckbox btnIncludingVisible;

	protected ChartCheckbox btnWithinVisible;

	private FillChooserComposite cmbBlockColor;

	protected FillChooserComposite cmbClientAreaColor;

	protected Label lblVisibleWithin;

	public void createControl(Composite parent) {
		ChartUIUtil.bindHelp(parent, ChartHelpContextIds.SUBTASK_PLOT);

		cmpContent = new Composite(parent, SWT.NONE);
		{
			GridLayout glContent = new GridLayout(2, false);
			cmpContent.setLayout(glContent);
			GridData gd = new GridData(GridData.FILL_BOTH);
			cmpContent.setLayoutData(gd);
		}

		Composite cmpBasic = new Composite(cmpContent, SWT.NONE);
		{
			cmpBasic.setLayout(new GridLayout(2, false));
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			cmpBasic.setLayoutData(gd);
		}

		int fillStyles = FillChooserComposite.ENABLE_GRADIENT | FillChooserComposite.ENABLE_IMAGE
				| FillChooserComposite.ENABLE_TRANSPARENT | FillChooserComposite.ENABLE_TRANSPARENT_SLIDER;
		fillStyles |= getContext().getUIFactory().supportAutoUI() ? FillChooserComposite.ENABLE_AUTO : fillStyles;
		createControlForAreaIncludingAxes(cmpBasic, fillStyles);
		createControlForAreaWithinAxes(cmpBasic, fillStyles);

		createButtonGroup(cmpContent);
	}

	protected void createControlForAreaIncludingAxes(Composite cmpBasic, int fillStyles) {
		Label lblIncludingAxes = new Label(cmpBasic, SWT.NONE);
		{
			GridData gd = new GridData();
			gd.horizontalSpan = 2;
			lblIncludingAxes.setLayoutData(gd);
			lblIncludingAxes.setFont(JFaceResources.getBannerFont());
			lblIncludingAxes.setText(getChart() instanceof ChartWithAxes
					? Messages.getString("ChartPlotSheetImpl.Label.AreaIncludingAxes") //$NON-NLS-1$
					: Messages.getString("ChartPlotSheetImpl.Label.PlotArea")); //$NON-NLS-1$
		}

		new Label(cmpBasic, SWT.NONE).setText(Messages.getString("ChartPlotSheetImpl.Label.Background")); //$NON-NLS-1$

		cmbBlockColor = new FillChooserComposite(cmpBasic, SWT.DROP_DOWN | SWT.READ_ONLY, fillStyles, getContext(),
				getChart().getPlot().getBackground());
		{
			GridData gd = new GridData();
			gd.widthHint = 200;
			cmbBlockColor.setLayoutData(gd);
			cmbBlockColor.addListener(this);
		}

		new Label(cmpBasic, SWT.NONE).setText(Messages.getString("ChartPlotSheetImpl.Label.Outline")); //$NON-NLS-1$

		btnIncludingVisible = getContext().getUIFactory().createChartCheckbox(cmpBasic, SWT.NONE,
				DefaultValueProvider.defChartWithAxes().getPlot().getOutline().isVisible());
		btnIncludingVisible.setText(Messages.getString("ChartPlotSheetImpl.Label.Visible")); //$NON-NLS-1$
		btnIncludingVisible
				.setSelectionState(getChart().getPlot().getOutline().isSetVisible()
						? (getChart().getPlot().getOutline().isVisible() ? ChartCheckbox.STATE_SELECTED
								: ChartCheckbox.STATE_UNSELECTED)
						: ChartCheckbox.STATE_GRAYED);
		btnIncludingVisible.addSelectionListener(this);
	}

	protected void createControlForAreaWithinAxes(Composite cmpBasic, int fillStyles) {
		Label lblWithinAxes = new Label(cmpBasic, SWT.NONE);
		{
			GridData gd = new GridData();
			gd.horizontalSpan = 2;
			lblWithinAxes.setLayoutData(gd);
			lblWithinAxes.setFont(JFaceResources.getBannerFont());
			lblWithinAxes.setText(
					getChart() instanceof ChartWithAxes ? Messages.getString("ChartPlotSheetImpl.Label.AreaWithinAxes") //$NON-NLS-1$
							: Messages.getString("ChartPlotSheetImpl.Label.ClientArea")); //$NON-NLS-1$
		}

		// WithinAxes area is not supported in 3D
		if (enableAreaWithinAxesUI()) {
			new Label(cmpBasic, SWT.NONE).setText(Messages.getString("ChartPlotSheetImpl.Label.Background2")); //$NON-NLS-1$

			cmbClientAreaColor = new FillChooserComposite(cmpBasic, SWT.DROP_DOWN | SWT.READ_ONLY, fillStyles,
					getContext(), getChart().getPlot().getClientArea().getBackground());
			{
				GridData gridData = new GridData();
				gridData.widthHint = 200;
				cmbClientAreaColor.setLayoutData(gridData);
				cmbClientAreaColor.addListener(this);
			}
		}

		// Following settings only work in some criteria
		boolean is3DWallFloorSet = ChartUIUtil.is3DWallFloorSet(getChart());
		lblVisibleWithin = new Label(cmpBasic, SWT.NONE);
		{
			lblVisibleWithin.setText(Messages.getString("ChartPlotSheetImpl.Label.Outline")); //$NON-NLS-1$
			lblVisibleWithin.setEnabled(is3DWallFloorSet);
		}

		btnWithinVisible = getContext().getUIFactory().createChartCheckbox(cmpBasic, SWT.NONE,
				DefaultValueProvider.defChartWithAxes().getPlot().getClientArea().getOutline().isVisible());
		btnWithinVisible.setText(Messages.getString("ChartPlotSheetImpl.Label.Visible2")); //$NON-NLS-1$
		btnWithinVisible.setSelectionState(getChart().getPlot().getClientArea().getOutline().isSetVisible()
				? (getChart().getPlot().getClientArea().getOutline().isVisible() ? ChartCheckbox.STATE_SELECTED
						: ChartCheckbox.STATE_UNSELECTED)
				: ChartCheckbox.STATE_GRAYED);
		btnWithinVisible.setEnabled(is3DWallFloorSet);
		if (!btnWithinVisible.getEnabled()) {
			// Hide for 3D
			btnWithinVisible.setSelectionState(ChartCheckbox.STATE_UNSELECTED);
		}
		btnWithinVisible.addSelectionListener(this);
	}

	protected boolean enableAreaWithinAxesUI() {
		return !ChartUIUtil.is3DType(getChart());
	}

	protected void createButtonGroup(Composite parent) {
		Composite cmp = new Composite(parent, SWT.NONE);
		{
			cmp.setLayout(new GridLayout());
			GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
			gridData.horizontalSpan = 2;
			gridData.grabExcessVerticalSpace = true;
			gridData.verticalAlignment = SWT.END;
			cmp.setLayoutData(gridData);
		}

		ITaskPopupSheet popup = new PlotClientAreaSheet(Messages.getString("ChartPlotSheetImpl.Label.AreaFormat"), //$NON-NLS-1$
				getContext());
		Button btnArea = createToggleButton(cmp, BUTTON_AREA_FORMAT,
				Messages.getString("ChartPlotSheetImpl.Label.AreaFormat&"), popup); //$NON-NLS-1$
		btnArea.addSelectionListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.
	 * Event)
	 */
	public void handleEvent(Event event) {
		if (event.widget.equals(cmbBlockColor)) {
			if (event.type == FillChooserComposite.FILL_CHANGED_EVENT) {
				getChart().getPlot().setBackground((Fill) event.data);
			}
		} else if (event.widget.equals(cmbClientAreaColor)) {
			if (event.type == FillChooserComposite.FILL_CHANGED_EVENT) {
				getChart().getPlot().getClientArea().setBackground((Fill) event.data);
			}
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

		if (e.widget.equals(btnIncludingVisible)) {
			if (btnIncludingVisible.getSelectionState() == ChartCheckbox.STATE_GRAYED) {
				getChart().getPlot().getOutline().unsetVisible();
			} else {
				getChart().getPlot().getOutline()
						.setVisible(((ChartCheckbox) e.widget).getSelectionState() == ChartCheckbox.STATE_SELECTED);
			}
			refreshPopupSheet();
		} else if (e.widget.equals(btnWithinVisible)) {
			if (btnWithinVisible.getSelectionState() == ChartCheckbox.STATE_GRAYED) {
				getChart().getPlot().getClientArea().getOutline().unsetVisible();
			} else {
				getChart().getPlot().getClientArea().getOutline()
						.setVisible(((ChartCheckbox) e.widget).getSelectionState() == ChartCheckbox.STATE_SELECTED);
			}
			refreshPopupSheet();
		}

	}

	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
	}

}
