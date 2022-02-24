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

import java.util.List;

import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.model.component.CurveFitting;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.CurveFittingImpl;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.util.ChartDefaultValueUtil;
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.AbstractChartInsets;
import org.eclipse.birt.chart.ui.swt.ChartCheckbox;
import org.eclipse.birt.chart.ui.swt.ChartCombo;
import org.eclipse.birt.chart.ui.swt.composites.ExternalizedTextEditorComposite;
import org.eclipse.birt.chart.ui.swt.composites.FillChooserComposite;
import org.eclipse.birt.chart.ui.swt.composites.FontDefinitionComposite;
import org.eclipse.birt.chart.ui.swt.composites.LineAttributesComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.AbstractPopupSheet;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIExtensionUtil;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.chart.util.NameSet;
import org.eclipse.swt.SWT;
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

/**
 * 
 */

public class SeriesTrendlineSheet extends AbstractPopupSheet implements Listener, SelectionListener {

	private transient SeriesDefinition seriesDefn = null;
	private transient Series series = null;
	private transient Composite cmpContent = null;
	private transient LineAttributesComposite trendLineText;
	private transient LineAttributesComposite outlineText;
	private transient ChartCombo cmbAnchor;
	// private transient Button btnTriggers;
	private transient ExternalizedTextEditorComposite txtValue;
	private ChartCheckbox btnLabelVisible;
	// private transient Label lblPosition;
	// private transient Combo cmbPosition;
	private transient Label lblFont;
	private transient FontDefinitionComposite fdcFont;
	private transient Label lblFill;
	private transient FillChooserComposite fccBackground;
	private transient Label lblShadow;
	private transient FillChooserComposite fccShadow;
	private transient AbstractChartInsets icLabel;
	private transient Label lblValue;
	private transient Label lblAnchor;
	private transient ChartWizardContext context;
	private Series defSeries;

	/**
	 * @param title
	 * @param context
	 * @param seriesDefn
	 * 
	 * @deprecated since 3.7
	 */
	public SeriesTrendlineSheet(String title, ChartWizardContext context, SeriesDefinition seriesDefn) {
		super(title, context, false);
		this.seriesDefn = seriesDefn;
		this.context = context;
	}

	/**
	 * @param title
	 * @param context
	 * @param series
	 */
	public SeriesTrendlineSheet(String title, ChartWizardContext context, Series series) {
		super(title, context, false);
		this.series = series;
		this.context = context;
		this.defSeries = ChartDefaultValueUtil.getDefaultSeries(this.series);
	}

	protected Composite getComponent(Composite parent) {
		ChartUIUtil.bindHelp(parent, ChartHelpContextIds.POPUP_SERIES_CURVE_FITTING);

		cmpContent = new Composite(parent, SWT.NONE);
		{
			GridLayout glMain = new GridLayout();
			glMain.numColumns = 2;
			cmpContent.setLayout(glMain);
		}

		Composite cmpLeft = new Composite(cmpContent, SWT.NONE);
		{
			GridLayout gl = new GridLayout();
			gl.numColumns = 2;
			cmpLeft.setLayout(gl);
			GridData gd = new GridData(GridData.FILL_VERTICAL);
			cmpLeft.setLayoutData(gd);
		}

		lblValue = new Label(cmpLeft, SWT.NONE);
		{
			GridData gd = new GridData();
			lblValue.setLayoutData(gd);
			lblValue.setText(Messages.getString("SeriesTrendlineSheet.Label.Label&")); //$NON-NLS-1$
		}

		List<String> keys = null;
		if (getContext().getUIServiceProvider() != null) {
			keys = getContext().getUIServiceProvider().getRegisteredKeys();
		}

		txtValue = new ExternalizedTextEditorComposite(cmpLeft, SWT.BORDER | SWT.SINGLE, -1, -1, keys,
				getContext().getUIServiceProvider(), getTrendlineText());
		{
			GridData gd = new GridData();
			gd.widthHint = 125;
			txtValue.setLayoutData(gd);
			txtValue.addListener(this);
		}

		lblAnchor = new Label(cmpLeft, SWT.NONE);
		GridData gdLBLAnchor = new GridData();
		lblAnchor.setLayoutData(gdLBLAnchor);
		lblAnchor.setText(Messages.getString("BlockAttributeComposite.Lbl.Anchor")); //$NON-NLS-1$

		cmbAnchor = context.getUIFactory().createChartCombo(cmpLeft, SWT.DROP_DOWN | SWT.READ_ONLY, getTrendline(),
				"labelAnchor", //$NON-NLS-1$
				ChartUIUtil.getFlippedAnchor(defSeries.getCurveFitting().getLabelAnchor(), isFlippedAxes()).getName());
		GridData gdCBAnchor = new GridData(GridData.FILL_HORIZONTAL);
		cmbAnchor.setLayoutData(gdCBAnchor);
		cmbAnchor.addSelectionListener(this);
		cmbAnchor.getWidget().setVisibleItemCount(30);

		// btnTriggers = new Button( cmpLeft, SWT.PUSH );
		// GridData gdBTNTriggers = new GridData( );
		// gdBTNTriggers.horizontalSpan = 2;
		// btnTriggers.setLayoutData( gdBTNTriggers );
		// btnTriggers.setText( Messages.getString( "Shared.Lbl.Triggers" ) );
		// //$NON-NLS-1$
		// btnTriggers.addSelectionListener( this );

		Composite cmpRight = new Composite(cmpContent, SWT.NONE);
		{
			cmpRight.setLayout(new FillLayout());
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			cmpRight.setLayoutData(gd);
		}
		int lineStyles = LineAttributesComposite.ENABLE_STYLES | LineAttributesComposite.ENABLE_WIDTH
				| LineAttributesComposite.ENABLE_COLOR;
		lineStyles |= getContext().getUIFactory().supportAutoUI() ? LineAttributesComposite.ENABLE_AUTO_COLOR
				: lineStyles;

		trendLineText = new LineAttributesComposite(cmpRight, SWT.NONE, lineStyles, getContext(),
				getTrendline().getLineAttributes(), getDefaultTrendline().getLineAttributes());
		trendLineText.addListener(this);

		Group cmpLabel = new Group(cmpContent, SWT.NONE);
		{
			GridLayout gl = new GridLayout(2, false);
			cmpLabel.setLayout(gl);
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = 2;
			cmpLabel.setLayoutData(gd);
			cmpLabel.setText(Messages.getString("SeriesTrendlineSheet.Label.Label")); //$NON-NLS-1$
		}

		Composite cmpLabelInner = new Composite(cmpLabel, SWT.NONE);
		{
			GridLayout gl = new GridLayout(2, false);
			cmpLabelInner.setLayout(gl);
			GridData gd = new GridData(GridData.FILL_BOTH);
			gd.verticalAlignment = SWT.BEGINNING;
			cmpLabelInner.setLayoutData(gd);
		}

		btnLabelVisible = getContext().getUIFactory().createChartCheckbox(cmpLabelInner, SWT.NONE,
				getDefaultTrendline().getLabel().isVisible());
		GridData gdCBVisible = new GridData(GridData.FILL_HORIZONTAL);
		gdCBVisible.horizontalSpan = 2;
		btnLabelVisible.setLayoutData(gdCBVisible);
		btnLabelVisible.setText(Messages.getString("SeriesTrendlineSheet.SeriesTrendlineSheet.Lbl.IsVisible")); //$NON-NLS-1$
		btnLabelVisible.setSelectionState(
				getTrendline().getLabel().isSetVisible()
						? (getTrendline().getLabel().isVisible() ? ChartCheckbox.STATE_SELECTED
								: ChartCheckbox.STATE_UNSELECTED)
						: ChartCheckbox.STATE_GRAYED);
		btnLabelVisible.addSelectionListener(this);

		// lblPosition = new Label( cmpLabelInner, SWT.NONE );
		// GridData gdLBLPosition = new GridData( );
		// lblPosition.setLayoutData( gdLBLPosition );
		// lblPosition.setText( Messages.getString(
		// "LabelAttributesComposite.Lbl.Position" ) ); //$NON-NLS-1$
		//
		// cmbPosition = new Combo( cmpLabelInner, SWT.DROP_DOWN | SWT.READ_ONLY
		// );
		// GridData gdCMBPosition = new GridData( GridData.FILL_BOTH );
		// cmbPosition.setLayoutData( gdCMBPosition );
		// cmbPosition.addSelectionListener( this );

		lblFont = new Label(cmpLabelInner, SWT.NONE);
		GridData gdLFont = new GridData();
		lblFont.setLayoutData(gdLFont);
		lblFont.setText(Messages.getString("LabelAttributesComposite.Lbl.Font")); //$NON-NLS-1$

		fdcFont = new FontDefinitionComposite(cmpLabelInner, SWT.NONE, getContext(),
				getTrendline().getLabel().getCaption().getFont(), getTrendline().getLabel().getCaption().getColor(),
				false);
		GridData gdFDCFont = new GridData(GridData.FILL_BOTH);
		// gdFDCFont.heightHint = fdcFont.getPreferredSize( ).y;
		gdFDCFont.widthHint = fdcFont.getPreferredSize().x;
		gdFDCFont.grabExcessVerticalSpace = false;
		fdcFont.setLayoutData(gdFDCFont);
		fdcFont.addListener(this);

		lblFill = new Label(cmpLabelInner, SWT.NONE);
		GridData gdLFill = new GridData();
		lblFill.setLayoutData(gdLFill);
		lblFill.setText(Messages.getString("LabelAttributesComposite.Lbl.Background")); //$NON-NLS-1$

		int fillStyles = FillChooserComposite.ENABLE_TRANSPARENT | FillChooserComposite.ENABLE_TRANSPARENT_SLIDER
				| FillChooserComposite.DISABLE_PATTERN_FILL;
		fillStyles |= getContext().getUIFactory().supportAutoUI() ? FillChooserComposite.ENABLE_AUTO : fillStyles;
		fccBackground = new FillChooserComposite(cmpLabelInner, SWT.NONE, fillStyles, getContext(),
				getTrendline().getLabel().getBackground());
		GridData gdFCCBackground = new GridData(GridData.FILL_BOTH);
		fccBackground.setLayoutData(gdFCCBackground);
		fccBackground.addListener(this);

		lblShadow = new Label(cmpLabelInner, SWT.NONE);
		GridData gdLBLShadow = new GridData();
		lblShadow.setLayoutData(gdLBLShadow);
		lblShadow.setText(Messages.getString("LabelAttributesComposite.Lbl.Shadow")); //$NON-NLS-1$

		fccShadow = new FillChooserComposite(cmpLabelInner, SWT.NONE, fillStyles, getContext(),
				getTrendline().getLabel().getShadowColor());
		GridData gdFCCShadow = new GridData(GridData.FILL_BOTH);
		fccShadow.setLayoutData(gdFCCShadow);
		fccShadow.addListener(this);

		Group grpOutline = new Group(cmpLabel, SWT.NONE);
		grpOutline.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		grpOutline.setLayout(new FillLayout());
		grpOutline.setText(Messages.getString("SeriesTrendlineSheet.Label.Outline")); //$NON-NLS-1$

		lineStyles = LineAttributesComposite.ENABLE_VISIBILITY | LineAttributesComposite.ENABLE_STYLES
				| LineAttributesComposite.ENABLE_WIDTH | LineAttributesComposite.ENABLE_COLOR;
		lineStyles |= getContext().getUIFactory().supportAutoUI() ? LineAttributesComposite.ENABLE_AUTO_COLOR
				: lineStyles;
		outlineText = new LineAttributesComposite(grpOutline, SWT.NONE, lineStyles, getContext(),
				getTrendline().getLabel().getOutline(), getDefaultTrendline().getLabel().getOutline());
		outlineText.addListener(this);

		icLabel = getContext().getUIFactory().createChartInsetsComposite(cmpLabel, SWT.NONE, 1, getInsets(),
				getChart().getUnits(), getContext().getUIServiceProvider(), getContext(),
				defSeries.getLabel().getInsets());
		GridData gdICBlock = new GridData(GridData.FILL_HORIZONTAL);
		gdICBlock.horizontalSpan = 2;
		icLabel.setLayoutData(gdICBlock);

		populateLists();
		setState(getContext().getUIFactory().canEnableUI(btnLabelVisible));
		return cmpContent;
	}

	protected Insets getInsets() {
		Insets insets = getTrendline().getLabel().getInsets();
		if (insets == null) {
			insets = ChartUIExtensionUtil.createCurveFitting(getContext()).getLabel().getInsets();
			getTrendline().getLabel().setInsets(insets);
			insets.eAdapters().addAll(getTrendline().getLabel().eAdapters());
		}
		return insets;
	}

	protected String getTrendlineText() {
		if (getTrendline().getLabel().getCaption() == null) {
			Text caption = ChartUIExtensionUtil.createCurveFitting(getContext()).getLabel().getCaption();
			getTrendline().getLabel().setCaption(caption);
			caption.eAdapters().addAll(getTrendline().getLabel().eAdapters());
		}

		if (getTrendline().getLabel().getCaption().getValue() == null) {
			return "";//$NON-NLS-1$
		}
		return getTrendline().getLabel().getCaption().getValue();
	}

	private CurveFitting getTrendline() {
		if (series != null) {
			return series.getCurveFitting();
		}
		return seriesDefn.getDesignTimeSeries().getCurveFitting();
	}

	private CurveFitting getDefaultTrendline() {
		if (defSeries.getCurveFitting() == null) {
			return CurveFittingImpl.create();
		}
		return defSeries.getCurveFitting();
	}

	private void setState(boolean bEnableUI) {
		lblShadow.setEnabled(bEnableUI);
		fccShadow.setEnabled(bEnableUI);
		fccBackground.setEnabled(bEnableUI);
		lblFill.setEnabled(bEnableUI);
		fdcFont.setEnabled(bEnableUI);
		lblFont.setEnabled(bEnableUI);
		// lblPosition.setEnabled( bEnableUI );
		// cmbPosition.setEnabled( bEnableUI );
		outlineText.setAttributesEnabled(bEnableUI);
		icLabel.setEnabled(bEnableUI);
		lblValue.setEnabled(bEnableUI);
		txtValue.setEnabled(bEnableUI);
		lblAnchor.setEnabled(bEnableUI);
		cmbAnchor.setEnabled(bEnableUI);
	}

	private void populateLists() {
		// Set block Anchor property
		NameSet nameSet = LiteralHelper.anchorSet;
		cmbAnchor.setItems(nameSet.getDisplayNames());
		cmbAnchor.setItemData(nameSet.getNames());
		cmbAnchor
				.setSelection(ChartUIUtil.getFlippedAnchor(getTrendline().getLabelAnchor(), isFlippedAxes()).getName());

		// Set Legend Position property
		// nameSet = LiteralHelper.fullPositionSet;
		// cmbPosition.setItems( nameSet.getDisplayNames( ) );
		// cmbPosition.select( nameSet.getSafeNameIndex( getTrendline(
		// ).getPosition( )
		// .getName( ) ) );

	}

	public void handleEvent(Event event) {
		boolean isUnset = (event.detail == ChartUIExtensionUtil.PROPERTY_UNSET);
		if (event.widget.equals(txtValue)) {
			String text = txtValue.getText();
			if (text == null || text.trim().length() == 0) {
				getTrendline().getLabel().getCaption().setValue(null);
			} else {
				getTrendline().getLabel().getCaption().setValue(txtValue.getText());
			}
		} else if (event.widget.equals(icLabel)) {
			getTrendline().getLabel().setInsets((Insets) event.data);
		} else if (event.widget.equals(fdcFont)) {
			getTrendline().getLabel().getCaption().setFont((FontDefinition) ((Object[]) event.data)[0]);
			getTrendline().getLabel().getCaption().setColor((ColorDefinition) ((Object[]) event.data)[1]);
		} else if (event.widget.equals(fccBackground)) {
			getTrendline().getLabel().setBackground((Fill) event.data);
		} else if (event.widget.equals(fccShadow)) {
			getTrendline().getLabel().setShadowColor((ColorDefinition) event.data);
		} else if (event.widget.equals(trendLineText)) {
			switch (event.type) {
			case LineAttributesComposite.STYLE_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getTrendline().getLineAttributes(), "style", //$NON-NLS-1$
						event.data, isUnset);
				break;
			case LineAttributesComposite.WIDTH_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getTrendline().getLineAttributes(), "thickness", //$NON-NLS-1$
						((Integer) event.data).intValue(), isUnset);

				break;
			case LineAttributesComposite.COLOR_CHANGED_EVENT:
				getTrendline().getLineAttributes().setColor((ColorDefinition) event.data);
				break;
			case LineAttributesComposite.VISIBILITY_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getTrendline().getLineAttributes(), "visible", //$NON-NLS-1$
						((Boolean) event.data).booleanValue(), isUnset);
				break;
			}
		} else if (event.widget.equals(outlineText)) {
			switch (event.type) {
			case LineAttributesComposite.STYLE_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getTrendline().getLabel().getOutline(), "style", //$NON-NLS-1$
						event.data, isUnset);
				break;
			case LineAttributesComposite.WIDTH_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getTrendline().getLabel().getOutline(), "thickness", //$NON-NLS-1$
						((Integer) event.data).intValue(), isUnset);
				break;
			case LineAttributesComposite.COLOR_CHANGED_EVENT:
				getTrendline().getLabel().getOutline().setColor((ColorDefinition) event.data);
				break;
			case LineAttributesComposite.VISIBILITY_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(getTrendline().getLabel().getOutline(), "visible", //$NON-NLS-1$
						((Boolean) event.data).booleanValue(), isUnset);
				break;
			}
		}
	}

	public void widgetSelected(SelectionEvent e) {
		if (e.widget.equals(cmbAnchor)) {
			String itemData = cmbAnchor.getSelectedItemData();
			if (itemData != null) {
				getTrendline()
						.setLabelAnchor(ChartUIUtil.getFlippedAnchor(Anchor.getByName(itemData), isFlippedAxes()));
			}
		}
		// else if ( e.widget.equals( btnTriggers ) )
		// {
		// String sTitle = sTitle = Messages.getString(
		// "BlockAttributeComposite.Title.LegendBlock" ); //$NON-NLS-1$
		// new TriggerEditorDialog( cmpContent.getShell( ),
		// getTrendline( ).getLabel().getTriggers( ),
		// sTitle );
		// }
		else if (e.widget == btnLabelVisible) {
			if (btnLabelVisible.getSelectionState() == ChartCheckbox.STATE_GRAYED) {
				getTrendline().getLabel().unsetVisible();
			} else {
				getTrendline().getLabel()
						.setVisible(btnLabelVisible.getSelectionState() == ChartCheckbox.STATE_SELECTED);
			}
			setState(getContext().getUIFactory().canEnableUI(btnLabelVisible));
		}
		// else if ( e.widget.equals( cmbPosition ) )
		// {
		//
		// }
	}

	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub

	}

	private boolean isFlippedAxes() {
		return ((ChartWithAxes) context.getModel()).getOrientation().equals(Orientation.HORIZONTAL_LITERAL);
	}

}
