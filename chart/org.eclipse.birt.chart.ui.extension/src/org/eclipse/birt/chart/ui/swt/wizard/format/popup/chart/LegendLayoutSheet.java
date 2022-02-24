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

import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Direction;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.Stretch;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.util.ChartDefaultValueUtil;
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.AbstractChartInsets;
import org.eclipse.birt.chart.ui.swt.AbstractChartNumberEditor;
import org.eclipse.birt.chart.ui.swt.ChartCombo;
import org.eclipse.birt.chart.ui.swt.ChartSpinner;
import org.eclipse.birt.chart.ui.swt.composites.FillChooserComposite;
import org.eclipse.birt.chart.ui.swt.composites.LineAttributesComposite;
import org.eclipse.birt.chart.ui.swt.composites.TextEditorComposite;
import org.eclipse.birt.chart.ui.swt.fieldassist.TextNumberEditorAssistField;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.AbstractPopupSheet;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIExtensionUtil;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.chart.util.NameSet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;

/**
 * Legend - Layout
 */

public class LegendLayoutSheet extends AbstractPopupSheet implements Listener, ModifyListener, SelectionListener {

	protected ChartCombo cmbAnchor;

	private ChartCombo cmbStretch;

	protected LineAttributesComposite outlineLegend;

	private AbstractChartInsets icLegend;

	protected ChartCombo cmbOrientation;

	protected ChartCombo cmbPosition;

	protected FillChooserComposite fccBackground;

	private ChartCombo cmbDirection;

	protected AbstractChartNumberEditor txtWrapping;

	private ChartSpinner spnMaxPercent;

	private ChartSpinner spnTitlePercent;

	public LegendLayoutSheet(String title, ChartWizardContext context) {
		super(title, context, false);
	}

	@Override
	protected void bindHelp(Composite parent) {
		ChartUIUtil.bindHelp(parent, ChartHelpContextIds.POPUP_LEGEND_LAYOUT);
	}

	protected Composite getComponent(Composite parent) {
		Composite cmpContent = new Composite(parent, SWT.NONE);
		cmpContent.setLayout(new GridLayout());

		Group grpLegendArea = new Group(cmpContent, SWT.NONE);
		{
			grpLegendArea.setLayout(new GridLayout(2, false));
			grpLegendArea.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			grpLegendArea.setText(Messages.getString("MoreOptionsChartLegendSheet.Label.LegendArea")); //$NON-NLS-1$
		}

		Composite cmpLegLeft = new Composite(grpLegendArea, SWT.NONE);
		{
			GridLayout gl = new GridLayout(3, false);
			gl.marginHeight = 0;
			gl.marginWidth = 0;
			gl.marginRight = 4;
			gl.horizontalSpacing = 8;
			cmpLegLeft.setLayout(gl);
			cmpLegLeft.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING));

			getComponentLegendLeftArea(cmpLegLeft);
		}

		Composite cmpLegRight = new Composite(grpLegendArea, SWT.NONE);
		{
			GridLayout gl = new GridLayout();
			gl.marginHeight = 0;
			gl.marginWidth = 0;
			gl.verticalSpacing = 10;
			cmpLegRight.setLayout(gl);
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.verticalAlignment = SWT.BEGINNING;
			cmpLegRight.setLayoutData(gd);

			getComponentLegendRightArea(cmpLegRight);
		}

		populateLists();

		return cmpContent;
	}

	private ChartSpinner createSpinner(Composite cmp, String sCaption, double dValue, boolean bEnableUI, Legend legend,
			String property) {
		new Label(cmp, SWT.NONE).setText(sCaption);

		ChartSpinner spn = getContext().getUIFactory().createChartSpinner(cmp, SWT.BORDER, legend, property, bEnableUI);
		{
			setSpinnerValue(spn.getWidget(), dValue);
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = 2;
			spn.setLayoutData(gd);
		}
		return spn;
	}

	protected void setSpinnerValue(Spinner spn, double dValue) {
		int spnValue = (int) (dValue * 100);
		spn.setValues(spnValue, 1, 100, 0, 1, 10);
	}

	protected void getComponentLegendLeftArea(Composite cmpLegLeft) {
		Label lblOrientation = new Label(cmpLegLeft, SWT.NONE);
		lblOrientation.setText(Messages.getString("BlockAttributeComposite.Lbl.Orientation")); //$NON-NLS-1$

		cmbOrientation = getContext().getUIFactory().createChartCombo(cmpLegLeft, SWT.DROP_DOWN | SWT.READ_ONLY,
				getBlockForProcessing(), "orientation", //$NON-NLS-1$
				ChartDefaultValueUtil.getDefaultLegend(getChart()).getOrientation().getName());
		GridData gdCMBOrientation = new GridData(GridData.FILL_HORIZONTAL);
		gdCMBOrientation.horizontalSpan = 2;
		cmbOrientation.setLayoutData(gdCMBOrientation);
		cmbOrientation.addSelectionListener(this);

		Label lblPosition = new Label(cmpLegLeft, SWT.NONE);
		lblPosition.setText(Messages.getString("BlockAttributeComposite.Lbl.Position")); //$NON-NLS-1$

		cmbPosition = getContext().getUIFactory().createChartCombo(cmpLegLeft, SWT.DROP_DOWN | SWT.READ_ONLY,
				getBlockForProcessing(), "position", //$NON-NLS-1$
				ChartDefaultValueUtil.getDefaultLegend(getChart()).getPosition().getName());
		GridData gdCMBPosition = new GridData(GridData.FILL_HORIZONTAL);
		gdCMBPosition.horizontalSpan = 2;
		cmbPosition.setLayoutData(gdCMBPosition);
		cmbPosition.addSelectionListener(this);

		Label lblAnchor = new Label(cmpLegLeft, SWT.NONE);
		lblAnchor.setText(Messages.getString("BlockAttributeComposite.Lbl.Anchor")); //$NON-NLS-1$

		cmbAnchor = getContext().getUIFactory().createChartCombo(cmpLegLeft, SWT.DROP_DOWN | SWT.READ_ONLY,
				getBlockForProcessing(), "anchor", //$NON-NLS-1$
				ChartDefaultValueUtil.getDefaultLegend(getChart()).getAnchor().getName());
		GridData gdCBAnchor = new GridData(GridData.FILL_HORIZONTAL);
		gdCBAnchor.horizontalSpan = 2;
		cmbAnchor.setLayoutData(gdCBAnchor);
		cmbAnchor.addSelectionListener(this);

		Label lblStretch = new Label(cmpLegLeft, SWT.NONE);
		lblStretch.setText(Messages.getString("BlockAttributeComposite.Lbl.Stretch")); //$NON-NLS-1$

		cmbStretch = getContext().getUIFactory().createChartCombo(cmpLegLeft, SWT.DROP_DOWN | SWT.READ_ONLY,
				getBlockForProcessing(), "stretch", //$NON-NLS-1$
				ChartDefaultValueUtil.getDefaultLegend(getChart()).getStretch().getName());
		GridData gdCBStretch = new GridData(GridData.FILL_HORIZONTAL);
		gdCBStretch.horizontalSpan = 2;
		cmbStretch.setLayoutData(gdCBStretch);
		cmbStretch.addSelectionListener(this);

		Label lblBackground = new Label(cmpLegLeft, SWT.NONE);
		lblBackground.setText(Messages.getString("Shared.mne.Background_K")); //$NON-NLS-1$

		int fillOptions = FillChooserComposite.ENABLE_GRADIENT | FillChooserComposite.ENABLE_IMAGE
				| FillChooserComposite.ENABLE_TRANSPARENT | FillChooserComposite.ENABLE_TRANSPARENT_SLIDER;
		fillOptions |= getContext().getUIFactory().supportAutoUI() ? FillChooserComposite.ENABLE_AUTO : fillOptions;
		fccBackground = new FillChooserComposite(cmpLegLeft, SWT.NONE, fillOptions, getContext(),
				getBlockForProcessing().getBackground());
		GridData gdFCCBackground = new GridData(GridData.FILL_HORIZONTAL);
		gdFCCBackground.horizontalSpan = 2;
		fccBackground.setLayoutData(gdFCCBackground);
		fccBackground.addListener(this);
		fccBackground.setTextIndent(0);

		Label lblDirection = new Label(cmpLegLeft, SWT.NONE);
		lblDirection.setText(Messages.getString("BlockAttributeComposite.Lbl.Direction")); //$NON-NLS-1$

		cmbDirection = getContext().getUIFactory().createChartCombo(cmpLegLeft, SWT.DROP_DOWN | SWT.READ_ONLY,
				getBlockForProcessing(), "direction", //$NON-NLS-1$
				ChartDefaultValueUtil.getDefaultLegend(getChart()).getDirection().getName());
		GridData gdCMBDirection = new GridData(GridData.FILL_HORIZONTAL);
		gdCMBDirection.horizontalSpan = 2;
		cmbDirection.setLayoutData(gdCMBDirection);
		cmbDirection.addSelectionListener(this);

		Label lblWrapping = new Label(cmpLegLeft, SWT.NONE);
		lblWrapping.setText(Messages.getString("LegendLayoutSheet.Label.WrappingWidth")); //$NON-NLS-1$

		txtWrapping = getContext().getUIFactory().createChartNumberEditor(cmpLegLeft, SWT.BORDER | SWT.SINGLE, null,
				getBlockForProcessing(), "wrappingSize");//$NON-NLS-1$
		new TextNumberEditorAssistField(txtWrapping.getTextControl(), null);
		{
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = 2;
			txtWrapping.setLayoutData(gd);
			txtWrapping.setValue(getBlockForProcessing().getWrappingSize());
			txtWrapping.addModifyListener(this);
		}

		spnMaxPercent = createSpinner(cmpLegLeft, Messages.getString("LegendLayoutSheet.Label.MaxPercent"), //$NON-NLS-1$
				getBlockForProcessing().getMaxPercent(), true, getBlockForProcessing(), "maxPercent"); //$NON-NLS-1$
		spnMaxPercent.setRatio(100d);

		spnTitlePercent = createSpinner(cmpLegLeft, Messages.getString("LegendLayoutSheet.Label.TitlePercent"), //$NON-NLS-1$
				getBlockForProcessing().getTitlePercent(), true, getBlockForProcessing(), "titlePercent"); //$NON-NLS-1$
		spnTitlePercent.setRatio(100d);
	}

	protected void getComponentLegendRightArea(Composite cmpLegRight) {
		Group grpOutline = new Group(cmpLegRight, SWT.NONE);
		{
			GridData gdGRPOutline = new GridData(GridData.FILL_HORIZONTAL);
			grpOutline.setLayoutData(gdGRPOutline);
			grpOutline.setLayout(new FillLayout());
			grpOutline.setText(Messages.getString("MoreOptionsChartLegendSheet.Label.Outline")); //$NON-NLS-1$
		}

		boolean bEnableUI = getBlockForProcessing().isVisible();
		int lineOptions = LineAttributesComposite.ENABLE_VISIBILITY | LineAttributesComposite.ENABLE_STYLES
				| LineAttributesComposite.ENABLE_WIDTH | LineAttributesComposite.ENABLE_COLOR;
		lineOptions |= getContext().getUIFactory().supportAutoUI() ? LineAttributesComposite.ENABLE_AUTO_COLOR
				: lineOptions;
		outlineLegend = new LineAttributesComposite(grpOutline, SWT.NONE, lineOptions, getContext(),
				getBlockForProcessing().getOutline(), ChartDefaultValueUtil.getDefaultLegend(getChart()).getOutline());
		{
			outlineLegend.addListener(this);
			outlineLegend.setAttributesEnabled(bEnableUI);
		}

		icLegend = getContext().getUIFactory().createChartInsetsComposite(cmpLegRight, SWT.NONE, 2,
				getBlockForProcessing().getInsets(), getChart().getUnits(), getContext().getUIServiceProvider(),
				getContext(), ChartDefaultValueUtil.getDefaultPlot(getChart()).getInsets());
		{
			GridData gdICBlock = new GridData(GridData.FILL_HORIZONTAL);
			icLegend.setLayoutData(gdICBlock);
			icLegend.setEnabled(bEnableUI);
		}
	}

	protected void populateLists() {
		// Set the block Stretch property
		NameSet ns = LiteralHelper.stretchSet;
		cmbStretch.setItems(ns.getDisplayNames());
		cmbStretch.setItemData(ns.getNames());
		cmbStretch.setSelection(getBlockForProcessing().getStretch().getName());

		// Set Legend Orientation property
		ns = LiteralHelper.orientationSet;
		cmbOrientation.setItems(ns.getDisplayNames());
		cmbOrientation.setItemData(ns.getNames());
		cmbOrientation.setSelection(getBlockForProcessing().getOrientation().getName());

		// Set Legend Direction property
		ns = LiteralHelper.directionSet;
		cmbDirection.setItems(ns.getDisplayNames());
		cmbDirection.setItemData(ns.getNames());
		cmbDirection.setSelection(getBlockForProcessing().getDirection().getName());

		// Set Legend Position property
		ns = LiteralHelper.notOutPositionSet;
		cmbPosition.setItems(ns.getDisplayNames());
		cmbPosition.setItemData(ns.getNames());
		cmbPosition.setSelection(getBlockForProcessing().getPosition().getName());

		// Set block Anchor property
		getAnchorSet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events
	 * .ModifyEvent)
	 */
	public void modifyText(ModifyEvent e) {
		if (e.widget.equals(txtWrapping)) {
			if (!TextEditorComposite.TEXT_RESET_MODEL.equals(e.data)) {
				setWrappingSizeAttr();
			}
		}
	}

	protected void setWrappingSizeAttr() {
		getBlockForProcessing().setWrappingSize(txtWrapping.getValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.
	 * Event)
	 */
	public void handleEvent(Event event) {
		if (event.widget.equals(fccBackground)) {
			getBlockForProcessing().setBackground((Fill) event.data);
		} else if (event.widget.equals(outlineLegend)) {
			boolean isUnset = (event.detail == ChartUIExtensionUtil.PROPERTY_UNSET);
			switch (event.type) {
			case LineAttributesComposite.STYLE_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getBlockForProcessing().getOutline(), "style", //$NON-NLS-1$
						event.data, isUnset);
				break;
			case LineAttributesComposite.WIDTH_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getBlockForProcessing().getOutline(), "thickness", //$NON-NLS-1$
						((Integer) event.data).intValue(), isUnset);
				break;
			case LineAttributesComposite.COLOR_CHANGED_EVENT:
				getBlockForProcessing().getOutline().setColor((ColorDefinition) event.data);
				break;
			case LineAttributesComposite.VISIBILITY_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getBlockForProcessing().getOutline(), "visible", //$NON-NLS-1$
						((Boolean) event.data).booleanValue(), isUnset);
				break;
			}
		} else if (event.widget.equals(icLegend)) {
			getBlockForProcessing().setInsets((Insets) event.data);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse
	 * .swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected(SelectionEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt
	 * .events.SelectionEvent)
	 */
	public void widgetSelected(SelectionEvent e) {
		Object oSource = e.getSource();
		Legend legend = getBlockForProcessing();
		if (oSource.equals(cmbAnchor)) {
			String selectedAnchor = cmbAnchor.getSelectedItemData();
			if (selectedAnchor != null) {
				legend.setAnchor(Anchor.getByName(selectedAnchor));
			}
		} else if (oSource.equals(cmbStretch)) {
			String selectedStretch = cmbStretch.getSelectedItemData();
			if (selectedStretch != null) {
				legend.setStretch(Stretch.getByName(selectedStretch));
			}
		} else if (oSource.equals(cmbOrientation)) {
			String selectedOrientation = cmbOrientation.getSelectedItemData();
			if (selectedOrientation != null) {
				legend.setOrientation(Orientation.getByName(selectedOrientation));
			}
		} else if (oSource.equals(cmbDirection)) {
			String selectedDirection = cmbDirection.getSelectedItemData();
			if (selectedDirection != null) {
				legend.setDirection(Direction.getByName(selectedDirection));
			}
		} else if (oSource.equals(cmbPosition)) {
			String selectedPosition = cmbPosition.getSelectedItemData();
			if (selectedPosition != null) {
				legend.setPosition(Position.getByName(selectedPosition));
			}
			getAnchorSet();
		}
	}

	protected Legend getBlockForProcessing() {
		return getChart().getLegend();
	}

	protected void getAnchorSet() {
		String positionValue = getBlockForProcessing().getPosition().getLiteral();
		NameSet ns;
		if (positionValue.equals(Position.LEFT_LITERAL.getLiteral())
				|| positionValue.equals(Position.RIGHT_LITERAL.getLiteral())) {
			ns = LiteralHelper.verticalAnchorSet;
		} else if (positionValue.equals(Position.ABOVE_LITERAL.getLiteral())
				|| positionValue.equals(Position.BELOW_LITERAL.getLiteral())) {
			ns = LiteralHelper.horizontalAnchorSet;
		} else {
			ns = LiteralHelper.anchorSet;
		}

		cmbAnchor.setItems(ns.getDisplayNames());
		cmbAnchor.setItemData(ns.getNames());
		cmbAnchor.setSelection(getBlockForProcessing().getAnchor().getName());
	}
}
