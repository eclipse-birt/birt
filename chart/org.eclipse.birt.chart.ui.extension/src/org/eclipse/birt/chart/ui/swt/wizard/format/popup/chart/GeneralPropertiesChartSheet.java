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

package org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.UnitsOfMeasurement;
import org.eclipse.birt.chart.model.attribute.impl.TextImpl;
import org.eclipse.birt.chart.model.util.ChartDefaultValueUtil;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.AbstractChartNumberEditor;
import org.eclipse.birt.chart.ui.swt.ChartCombo;
import org.eclipse.birt.chart.ui.swt.ChartSpinner;
import org.eclipse.birt.chart.ui.swt.composites.ExternalizedTextEditorComposite;
import org.eclipse.birt.chart.ui.swt.composites.TextEditorComposite;
import org.eclipse.birt.chart.ui.swt.fieldassist.TextNumberEditorAssistField;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartSubType;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.AbstractPopupSheet;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.chart.util.NameSet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * GeneralPropertiesChartSheet
 */

public class GeneralPropertiesChartSheet extends AbstractPopupSheet
		implements Listener, ModifyListener, SelectionListener {

	private Composite cmpContent;

	private ExternalizedTextEditorComposite txtDescription;

	private Text txtType;

	private Text txtSubType;

	private ChartSpinner txtUnitSpacing;

	private ChartCombo cmbUnits;

	private Label lblSeriesThickness;

	private AbstractChartNumberEditor txtSeriesThickness;

	private ChartSpinner iscColumnCount;

	private String sOldUnits = ""; //$NON-NLS-1$

	public GeneralPropertiesChartSheet(String title, ChartWizardContext context) {
		super(title, context, false);
	}

	@Override
	protected void bindHelp(Composite parent) {
		ChartUIUtil.bindHelp(parent, ChartHelpContextIds.POPUP_CHART_GENERAL);
	}

	@Override
	protected Composite getComponent(Composite parent) {
		cmpContent = new Composite(parent, SWT.NONE);
		{
			GridLayout glContent = new GridLayout();
			glContent.horizontalSpacing = 5;
			glContent.verticalSpacing = 5;
			glContent.marginHeight = 7;
			glContent.marginWidth = 7;
			cmpContent.setLayout(glContent);
		}

		// Layout for General composite
		GridLayout glGeneral = new GridLayout();
		glGeneral.numColumns = 3;
		glGeneral.horizontalSpacing = 5;
		glGeneral.verticalSpacing = 5;
		glGeneral.marginHeight = 7;
		glGeneral.marginWidth = 7;

		createDescriptionArea(cmpContent);

		Group grpGeneral = new Group(cmpContent, SWT.NONE);
		GridData gdGRPGeneral = new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL);
		grpGeneral.setLayoutData(gdGRPGeneral);
		grpGeneral.setLayout(glGeneral);
		grpGeneral.setText(Messages.getString("AttributeSheetImpl.Lbl.ChartProperties")); //$NON-NLS-1$

		Label lblType = new Label(grpGeneral, SWT.NONE);
		GridData gdLBLType = new GridData();
		gdLBLType.horizontalIndent = 1;
		lblType.setLayoutData(gdLBLType);
		lblType.setText(Messages.getString("AttributeSheetImpl.Lbl.Type")); //$NON-NLS-1$

		txtType = new Text(grpGeneral, SWT.BORDER | SWT.READ_ONLY);
		GridData gdTXTType = new GridData(GridData.FILL_HORIZONTAL);
		gdTXTType.horizontalSpan = 2;
		txtType.setLayoutData(gdTXTType);
		txtType.setText(getContext().getChartType().getDisplayName());

		Label lblSubType = new Label(grpGeneral, SWT.NONE);
		GridData gdLBLSubType = new GridData();
		gdLBLSubType.horizontalIndent = 1;
		lblSubType.setLayoutData(gdLBLSubType);
		lblSubType.setText(Messages.getString("AttributeSheetImpl.Lbl.Subtype")); //$NON-NLS-1$

		txtSubType = new Text(grpGeneral, SWT.BORDER | SWT.READ_ONLY);
		GridData gdTXTSubType = new GridData(GridData.FILL_HORIZONTAL);
		gdTXTSubType.horizontalSpan = 2;
		txtSubType.setLayoutData(gdTXTSubType);
		txtSubType.setText("");//$NON-NLS-1$

		Orientation orientation = Orientation.VERTICAL_LITERAL;
		if (getChart() instanceof ChartWithAxes) {
			orientation = ((ChartWithAxes) getChart()).getOrientation();
		}
		Vector<IChartSubType> vSubType = (Vector<IChartSubType>) getContext().getChartType()
				.getChartSubtypes(getChart().getDimension().getName(), orientation);
		Iterator<IChartSubType> iter = vSubType.iterator();
		while (iter.hasNext()) {
			IChartSubType cSubType = iter.next();
			if (cSubType.getName().equals(getChart().getSubType())) {
				txtSubType.setText(cSubType.getDisplayName());
				break;
			}
		}

		createMisc(grpGeneral);

		return cmpContent;
	}

	protected void createMisc(Group grpGeneral) {
		if (getChart().getDimension().getValue() == ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH) {
			Label lblUnit = new Label(grpGeneral, SWT.NONE);
			GridData gdLBLUnit = new GridData();
			gdLBLUnit.horizontalIndent = 1;
			lblUnit.setLayoutData(gdLBLUnit);
			lblUnit.setText(Messages.getString("AttributeSheetImpl.Lbl.Units")); //$NON-NLS-1$

			cmbUnits = getContext().getUIFactory().createChartCombo(grpGeneral, SWT.DROP_DOWN | SWT.READ_ONLY,
					getChart(), "units", //$NON-NLS-1$
					ChartDefaultValueUtil.getDefaultValueChart(getChart()).getUnits());
			GridData gdCMBUnits = new GridData(GridData.FILL_HORIZONTAL);
			gdCMBUnits.horizontalSpan = 2;
			cmbUnits.setLayoutData(gdCMBUnits);
			cmbUnits.addSelectionListener(this);

			lblSeriesThickness = new Label(grpGeneral, SWT.NONE);
			GridData gdLBLSeriesThickness = new GridData();
			gdLBLSeriesThickness.horizontalIndent = 1;
			lblSeriesThickness.setLayoutData(gdLBLSeriesThickness);

			txtSeriesThickness = getContext().getUIFactory().createChartNumberEditor(grpGeneral,
					SWT.BORDER | SWT.SINGLE, null, getChart(), "seriesThickness");//$NON-NLS-1$
			new TextNumberEditorAssistField(txtSeriesThickness.getTextControl(), null);

			GridData gdTXTSeriesThickness = new GridData(GridData.FILL_HORIZONTAL);
			gdTXTSeriesThickness.horizontalSpan = 2;
			txtSeriesThickness.setLayoutData(gdTXTSeriesThickness);
			double dblPoints = getChart().getSeriesThickness();
			double dblCurrent = getContext().getUIServiceProvider().getConvertedValue(dblPoints, "Points", getUnits()); //$NON-NLS-1$
			txtSeriesThickness.setValue(dblCurrent);
			txtSeriesThickness.addModifyListener(this);

			populateLists();
		}

		if (getChart() instanceof ChartWithoutAxes) {
			Label lblColumnCount = new Label(grpGeneral, SWT.NONE);
			GridData gdLBLColumnCount = new GridData();
			gdLBLColumnCount.horizontalIndent = 1;
			lblColumnCount.setLayoutData(gdLBLColumnCount);
			lblColumnCount.setText(Messages.getString("AttributeSheetImpl.Lbl.ColumnCount")); //$NON-NLS-1$

			iscColumnCount = getContext().getUIFactory().createChartSpinner(grpGeneral, SWT.BORDER, getChart(),
					"gridColumnCount", //$NON-NLS-1$
					true);
			GridData gdISCColumnCount = new GridData(GridData.FILL_HORIZONTAL);
			gdISCColumnCount.horizontalSpan = 2;
			iscColumnCount.setLayoutData(gdISCColumnCount);
			iscColumnCount.getWidget().setMinimum(0);
			iscColumnCount.getWidget().setMaximum(5);
			iscColumnCount.getWidget().setSelection(getChart().getGridColumnCount());
		}

		else if (getChart() instanceof ChartWithAxes) {
			Label lblUnitSpacing = new Label(grpGeneral, SWT.NONE);
			GridData gdUnitSpacing = new GridData();
			gdUnitSpacing.horizontalIndent = 1;
			lblUnitSpacing.setLayoutData(gdUnitSpacing);
			lblUnitSpacing.setText(Messages.getString("AttributeSheetImpl.Lbl.UnitSpacing")); //$NON-NLS-1$

			txtUnitSpacing = getContext().getUIFactory().createChartSpinner(grpGeneral, SWT.BORDER, getChart(),
					"unitSpacing", //$NON-NLS-1$
					true);
			GridData gdTXTUnitSpacing = new GridData(GridData.FILL_HORIZONTAL);
			gdTXTUnitSpacing.horizontalSpan = 2;
			txtUnitSpacing.setLayoutData(gdTXTUnitSpacing);
			txtUnitSpacing.getWidget().setMinimum(0);
			txtUnitSpacing.getWidget().setMaximum(100);
			txtUnitSpacing.getWidget().setIncrement(1);
			double unitSpacing = ((ChartWithAxes) getChart()).getUnitSpacing();
			txtUnitSpacing.getWidget().setSelection((int) unitSpacing);
		}
	}

	private void createDescriptionArea(Composite parent) {
		Composite cmpDesp = new Composite(parent, SWT.NONE);
		{
			cmpDesp.setLayout(new GridLayout(2, false));
			GridData griddata = new GridData();
			griddata.horizontalAlignment = SWT.FILL;
			griddata.widthHint = 300;
			cmpDesp.setLayoutData(griddata);
		}

		List<String> keys = null;
		if (getContext().getUIServiceProvider() != null) {
			keys = getContext().getUIServiceProvider().getRegisteredKeys();
		}

		Label lblDescription = new Label(cmpDesp, SWT.NONE);
		GridData gdLBLDescription = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		gdLBLDescription.horizontalIndent = 2;
		gdLBLDescription.grabExcessHorizontalSpace = false;
		lblDescription.setLayoutData(gdLBLDescription);
		lblDescription.setText(Messages.getString("GeneralSheetImpl.Lbl.Description")); //$NON-NLS-1$

		String sDescription = ""; //$NON-NLS-1$
		if (getChart().getDescription() != null) {
			sDescription = getChart().getDescription().getValue();
		}
		txtDescription = new ExternalizedTextEditorComposite(cmpDesp, SWT.BORDER | SWT.MULTI | SWT.WRAP, 65, -1, keys,
				getContext().getUIServiceProvider(), sDescription);
		GridData gdTXTDescription = new GridData(GridData.FILL_HORIZONTAL);
		gdTXTDescription.heightHint = 65;
		txtDescription.setLayoutData(gdTXTDescription);
		txtDescription.addListener(this);
	}

	private void populateLists() {
		NameSet ns = LiteralHelper.unitsOfMeasurementSet;
		cmbUnits.setItems(ns.getDisplayNames());
		cmbUnits.setItemData(ns.getNames());

		cmbUnits.setSelection(getUnits());
		this.sOldUnits = cmbUnits.getSelectedItemData();
		if (this.sOldUnits == null) {
			this.sOldUnits = ChartDefaultValueUtil.getDefaultUnits(getChart());
		}
		lblSeriesThickness.setText(new MessageFormat(Messages.getString("GeneralSheetImpl.Lbl.SeriesWidth")) //$NON-NLS-1$
				.format(new Object[] { LiteralHelper.unitsOfMeasurementSet.getDisplayNameByName(getUnits()) }));
	}

	private String getUnits() {
		String units = getChart().getUnits();
		if (units == null) {
			units = ChartDefaultValueUtil.getDefaultUnits(getChart());
		}
		return units;
	}

	private double recalculateUnitDependentValues(double value) {
		return getContext().getUIServiceProvider().getConvertedValue(value, sOldUnits,
				cmbUnits.getSelectedItemData() == null ? ChartDefaultValueUtil.getDefaultUnits(getChart())
						: cmbUnits.getSelectedItemData());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events
	 * .ModifyEvent)
	 */
	@Override
	public void modifyText(ModifyEvent e) {
		if (e.widget.equals(txtSeriesThickness)) {
			if (!TextEditorComposite.TEXT_RESET_MODEL.equals(e.data)) {
				updateToSeriesThickness();
			}
		}

	}

	protected void updateToSeriesThickness() {
		double dblCurrent = txtSeriesThickness.getValue();
		String selectUnits = cmbUnits.getSelectedItemData();
		double dblPoints = getContext().getUIServiceProvider().getConvertedValue(dblCurrent,
				selectUnits == null ? ChartDefaultValueUtil.getDefaultUnits(getChart()) : selectUnits,
				UnitsOfMeasurement.POINTS_LITERAL.getName());
		getChart().setSeriesThickness(dblPoints);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.
	 * Event)
	 */
	@Override
	public void handleEvent(Event event) {
		if (event.widget.equals(txtDescription)) {
			if (getChart().getDescription() != null) {
				getChart().getDescription().setValue(txtDescription.getText());
			} else {
				org.eclipse.birt.chart.model.attribute.Text description = TextImpl.create(txtDescription.getText());
				getChart().setDescription(description);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse
	 * .swt.events.SelectionEvent)
	 */
	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt
	 * .events.SelectionEvent)
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {
		if (e.getSource().equals(cmbUnits)) {
			String selectedUnits = cmbUnits.getSelectedItemData();
			if (selectedUnits != null) {
				getChart().setUnits(selectedUnits);
				txtSeriesThickness.setValue(recalculateUnitDependentValues(txtSeriesThickness.getValue()));
			}

			// Update the Units for the Insets in Title properties
			lblSeriesThickness.setText(
					new MessageFormat(Messages.getString("GeneralSheetImpl.Lbl.SeriesWidth")).format(new Object[] { //$NON-NLS-1$
							LiteralHelper.unitsOfMeasurementSet.getDisplayNameByName(getUnits()) }));
			sOldUnits = getUnits();
		}
	}

}
