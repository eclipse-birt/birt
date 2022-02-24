/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.chart.ui.swt.wizard.format.popup.series;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.util.ChartDefaultValueUtil;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.PaletteEditorComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.AbstractPopupSheet;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

/**
 * 
 */

public class SeriesPaletteSheet extends AbstractPopupSheet implements SelectionListener {

	private SeriesDefinition cSeriesDefn = null;

	private SeriesDefinition[] vSeriesDefns = null;

	private ChartWizardContext context = null;

	private boolean isGroupedSeries = false;

	private StackLayout slPalette = null;

	private Group grpPalette = null;

	private PaletteEditorComposite cmpPE = null;

	private Composite cmpMPE = null;

	private TabFolder tf = null;

	private final int iFillChooserStyle;

	private Button btnAutoPals;

	private Composite cmpContent;

	/**
	 * 
	 * @param title
	 * @param context
	 * @param cSeriesDefn
	 * @param vSeriesDefns
	 * @param isGroupedSeries
	 * @param iFillChooserStyle style to decide what fill types should display in
	 *                          fill chooser
	 */
	public SeriesPaletteSheet(String title, ChartWizardContext context, SeriesDefinition cSeriesDefn,
			SeriesDefinition[] vSeriesDefns, boolean isGroupedSeries, int iFillChooserStyle) {
		super(title, context, true);
		this.context = context;
		this.cSeriesDefn = cSeriesDefn;
		this.vSeriesDefns = vSeriesDefns;
		this.isGroupedSeries = isGroupedSeries;
		this.iFillChooserStyle = iFillChooserStyle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.ui.swt.interfaces.ISheet#getComponent(org.eclipse.swt.
	 * widgets.Composite)
	 */
	public Composite getComponent(Composite parent) {
		ChartUIUtil.bindHelp(parent, ChartHelpContextIds.POPUP_SERIES_PALETTE);
		// Sheet content composite
		cmpContent = new Composite(parent, SWT.NONE);
		{
			// Layout for the content composite
			GridLayout glContent = new GridLayout();
			glContent.marginHeight = 7;
			glContent.marginWidth = 7;
			cmpContent.setLayout(glContent);
		}

		btnAutoPals = new Button(cmpContent, SWT.CHECK);
		btnAutoPals.setText(Messages.getString("SeriesPaletteSheet.Label.Auto")); //$NON-NLS-1$
		btnAutoPals.addSelectionListener(this);
		btnAutoPals.setVisible(context.getUIFactory().supportAutoUI());

		// Palete composite
		createPaletteUI(cmpContent);
		updateUIStatus();
		cmpContent.pack();
		return cmpContent;
	}

	protected void updateSeriesPalette() {
		// Add series palettes, user can specify/modify color palette for series
		// defintions.
		ChartDefaultValueUtil.updateSeriesPalettes(getChart(), getChart().eAdapters());
	}

	private void createPaletteUI(Composite cmpContent) {
		boolean isAutoPalette = ChartDefaultValueUtil.isAutoSeriesPalette(getChart());
		slPalette = new StackLayout();

		grpPalette = new Group(cmpContent, SWT.NONE);
		GridData gdGRPPalette = new GridData(GridData.FILL_BOTH);
		gdGRPPalette.heightHint = 300;

		grpPalette.setLayoutData(gdGRPPalette);
		grpPalette.setLayout(slPalette);
		grpPalette.setText(Messages.getString("BaseSeriesAttributeSheetImpl.Lbl.Palette")); //$NON-NLS-1$
		/*
		 * To let group palettee show out, otherwise the patette will disapper after
		 * modifying the value of 'auto' checkbox.
		 */
		if (cmpContent.isVisible()) {
			grpPalette.getShell().pack();
		}

		/*
		 * If Auto selected, show default series palette with action disabled TED -
		 * 47366
		 */
		Chart chart = getChart();
		if (isAutoPalette) {
			chart = getChart().copyInstance();
			ChartDefaultValueUtil.updateSeriesPalettes(chart, chart.eAdapters());
			vSeriesDefns = ChartUtil.getValueSeriesDefinitions(chart);
			cSeriesDefn = ChartUtil.getCategorySeriesDefinition(chart);
		}

		cmpPE = new PaletteEditorComposite(grpPalette, getContext(), cSeriesDefn.getSeriesPalette(), vSeriesDefns,
				iFillChooserStyle);
		cmpPE.setEnabled(!isAutoPalette);

		cmpMPE = new Composite(grpPalette, SWT.NONE);
		{
			GridLayout gl = new GridLayout();
			gl.marginLeft = 0;
			gl.marginRight = 0;
			cmpMPE.setLayoutData(new GridData(GridData.FILL_BOTH));
			cmpMPE.setLayout(gl);
		}

		tf = new TabFolder(cmpMPE, SWT.NONE);
		{
			tf.setLayoutData(new GridData(GridData.FILL_BOTH));
		}

		if (isGroupedSeries && isColoredByValue()) {
			for (int i = 0; i < vSeriesDefns.length; i++) {
				TabItem ti = new TabItem(tf, SWT.NONE);
				ti.setText(Messages.getString("SeriesPaletteSheet.Tab.Series") + (i + 1)); //$NON-NLS-1$
				PaletteEditorComposite pec = new PaletteEditorComposite(tf, getContext(),
						vSeriesDefns[i].getSeriesPalette(), null, iFillChooserStyle);
				pec.setEnabled(!isAutoPalette);
				ti.setControl(pec);
			}
			tf.setSelection(0);
			slPalette.topControl = cmpMPE;
		} else {
			if (isMultiAxes() && isColoredByValue()) {

				for (int i = 0; i < ChartUIUtil.getOrthogonalAxisNumber(chart); i++) {
					SeriesDefinition[] seriesDefns = ChartUIUtil.getOrthogonalSeriesDefinitions(chart, i)
							.toArray(new SeriesDefinition[] {});
					TabItem ti = new TabItem(tf, SWT.NONE);
					ti.setText(Messages.getString("SeriesPaletteSheet.Tab.Axis") + (i + 1)); //$NON-NLS-1$
					PaletteEditorComposite pec = new PaletteEditorComposite(tf, getContext(),
							seriesDefns[0].getSeriesPalette(), seriesDefns, iFillChooserStyle);
					pec.setEnabled(!isAutoPalette);
					ti.setControl(pec);
				}
				tf.setSelection(0);
				slPalette.topControl = cmpMPE;
			} else {
				slPalette.topControl = cmpPE;
			}
		}
	}

	private void updateUIStatus() {
		if (context.getUIFactory().supportAutoUI() && ChartDefaultValueUtil.isAutoSeriesPalette(getChart())) {
			// It means there isn't specified color palette, it is Auto mode.
			btnAutoPals.setSelection(true);
			grpPalette.setEnabled(false);
		} else {
			btnAutoPals.setSelection(false);
			grpPalette.setEnabled(true);
		}
	}

	public void setGroupedPalette(boolean isGroupedSeries) {
		this.isGroupedSeries = isGroupedSeries;
	}

	public void setCategorySeries(SeriesDefinition sd) {
		this.cSeriesDefn = sd;
	}

	private boolean isColoredByValue() {
		return context.getModel().getLegend().getItemType().getValue() == LegendItemType.SERIES;
	}

	private boolean isMultiAxes() {
		return ChartUIUtil.getOrthogonalAxisNumber(context.getModel()) > 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.
	 * swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected(SelectionEvent arg0) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.
	 * events.SelectionEvent)
	 */
	public void widgetSelected(SelectionEvent e) {
		if (btnAutoPals == e.widget) {
			if (btnAutoPals.getSelection()) {
				grpPalette.setEnabled(false);
				grpPalette.setVisible(false);

				// Disable series palettes, removed all palettes from series definitions of
				// chart.
				ChartDefaultValueUtil.removeSerlesPalettes(getChart());
			} else {
				grpPalette.setEnabled(true);
				grpPalette.setVisible(true);

				updateSeriesPalette();
			}

			refreshPaletteUI();
		}
	}

	private void refreshPaletteUI() {
		vSeriesDefns = ChartUtil.getValueSeriesDefinitions(getChart());
		cSeriesDefn = ChartUtil.getCategorySeriesDefinition(getChart());
		if (grpPalette != null && !grpPalette.isDisposed()) {
			grpPalette.dispose();
		}
		createPaletteUI(cmpContent);
		updateUIStatus();
		cmpContent.getShell().layout();
		cmpContent.getShell().pack();
	}

}
