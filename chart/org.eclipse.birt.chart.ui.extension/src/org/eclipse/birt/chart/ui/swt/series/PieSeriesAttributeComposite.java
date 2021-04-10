/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt.series;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.LeaderLineStyle;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.birt.chart.model.util.DefaultValueProvider;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.plugin.ChartUIExtensionPlugin;
import org.eclipse.birt.chart.ui.swt.AbstractChartNumberEditor;
import org.eclipse.birt.chart.ui.swt.ChartCheckbox;
import org.eclipse.birt.chart.ui.swt.ChartCombo;
import org.eclipse.birt.chart.ui.swt.ChartSlider;
import org.eclipse.birt.chart.ui.swt.ChartSpinner;
import org.eclipse.birt.chart.ui.swt.composites.FillChooserComposite;
import org.eclipse.birt.chart.ui.swt.composites.LineAttributesComposite;
import org.eclipse.birt.chart.ui.swt.composites.TextEditorComposite;
import org.eclipse.birt.chart.ui.swt.fieldassist.TextNumberEditorAssistField;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIExtensionUtil;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.chart.util.NameSet;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * UI composite for Pie series attributes
 * 
 */
public class PieSeriesAttributeComposite extends Composite implements Listener, SelectionListener, ModifyListener {

	private final static String[] MINMUM_SLICE_ITEMS = new String[] {
			Messages.getString("PieBottomAreaComponent.Label.Percentage"), //$NON-NLS-1$
			Messages.getString("PieSeriesAttributeComposite.InnerRadiusType.Points") //$NON-NLS-1$
	};

	private Group grpLeaderLine = null;

	private FillChooserComposite fccSliceOutline = null;

	private ChartCombo cmbLeaderLine = null;

	private ChartSpinner iscLeaderLength = null;

	private LineAttributesComposite liacLeaderLine = null;

	private PieSeries series = null;

	private PieSeries defSeries = DefaultValueProvider.defPieSeries();

	private static final int MAX_LEADER_LENGTH = 200;

	private ChartWizardContext context;

	private TextEditorComposite txtExplode;
	private Button btnBuilder;
	private ChartSpinner iscExplosion;

	private ChartSlider sRatio;
	private ChartSlider sRotation;

	private ChartCheckbox btnDirection;

	private AbstractChartNumberEditor txtInnerRadius;

	private ChartCombo cmbInnerRadiusPercent;

	private Label lblInnerRadiusPercent;

	private final static String TOOLTIP_EXPLODE_SLICE_WHEN = Messages
			.getString("PieBottomAreaComponent.Label.TheExplosionCondition"); //$NON-NLS-1$
	private final static String TOOLTIP_EXPLOSION_DISTANCE = Messages
			.getString("PieBottomAreaComponent.Label.TheAmplitudeOfTheExplosion"); //$NON-NLS-1$
	private final static String TOOLTIP_RATIO = Messages.getString("PieBottomAreaComponent.Label.TheRatioOfTheChart"); //$NON-NLS-1$
	private final static String TOOLTIP_ROTATION = Messages
			.getString("PiesBottomAreaComponent.Label.TheRotationOfTheChart"); //$NON-NLS-1$

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.ui.extension/swt.series"); //$NON-NLS-1$

	/**
	 * @param parent
	 * @param style
	 */
	public PieSeriesAttributeComposite(Composite parent, int style, Series series, ChartWizardContext context) {
		super(parent, style);
		this.context = context;
		if (!(series instanceof PieSeries)) {
			try {
				throw new ChartException(ChartUIExtensionPlugin.ID, ChartException.VALIDATION,
						"PieSeriesAttributeComposite.Exception.IllegalArgument", //$NON-NLS-1$
						new Object[] { series.getClass().getName() }, Messages.getResourceBundle());
			} catch (ChartException e) {
				logger.log(e);
				e.printStackTrace();
			}
		}
		this.series = (PieSeries) series;
		init();
		placeComponents();
		ChartUIUtil.bindHelp(parent, ChartHelpContextIds.SUBTASK_YSERIES_PIE);
	}

	private void init() {
		this.setSize(getParent().getClientArea().width, getParent().getClientArea().height);
	}

	private void placeComponents() {
		{
			// Layout for content composite
			GridLayout glContent = new GridLayout();
			glContent.numColumns = 2;
			glContent.marginHeight = 2;
			glContent.marginWidth = 4;
			// Main content composite
			this.setLayout(glContent);
		}

		// LeaderLine group
		grpLeaderLine = new Group(this, SWT.NONE);
		{
			GridData gdGRPLeaderLine = new GridData(GridData.FILL_BOTH);
			grpLeaderLine.setLayoutData(gdGRPLeaderLine);
			// Layout for content composite
			GridLayout glLeaderLine = new GridLayout();
			glLeaderLine.numColumns = 2;
			glLeaderLine.marginHeight = 0;
			glLeaderLine.marginWidth = 2;
			glLeaderLine.verticalSpacing = 0;
			grpLeaderLine.setLayout(glLeaderLine);
			grpLeaderLine.setText(Messages.getString("PieSeriesAttributeComposite.Lbl.LeaderLine")); //$NON-NLS-1$
		}

		// LeaderLine Attributes composite
		liacLeaderLine = new LineAttributesComposite(grpLeaderLine, SWT.NONE, getLeaderLineAttributesStyle(), context,
				series.getLeaderLineAttributes(), defSeries.getLeaderLineAttributes());
		GridData gdLIACLeaderLine = new GridData(GridData.FILL_HORIZONTAL);
		gdLIACLeaderLine.horizontalSpan = 2;
		liacLeaderLine.setLayoutData(gdLIACLeaderLine);
		liacLeaderLine.addListener(this);

		Composite cmpStyle = new Composite(grpLeaderLine, SWT.NONE);
		{
			GridLayout gl = new GridLayout(2, false);
			gl.marginHeight = 0;
			gl.marginBottom = 0;
			cmpStyle.setLayout(gl);
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = 2;
			cmpStyle.setLayoutData(gd);
		}

		createLeaderLineStyle(cmpStyle);

		// Leader Line Size composite
		Label lblLeaderSize = new Label(cmpStyle, SWT.NONE);
		GridData gdLBLLeaderSize = new GridData();
		lblLeaderSize.setLayoutData(gdLBLLeaderSize);
		lblLeaderSize.setText(Messages.getString("PieSeriesAttributeComposite.Lbl.LeaderLineLength")); //$NON-NLS-1$

		Composite comp = new Composite(cmpStyle, SWT.NONE);
		GridLayout gl = new GridLayout(2, false);
		gl.marginWidth = 0;
		gl.marginHeight = 0;
		comp.setLayout(gl);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		comp.setLayoutData(gd);

		iscLeaderLength = context.getUIFactory().createChartSpinner(comp, SWT.BORDER, series, "leaderLineLength", //$NON-NLS-1$
				true);
		GridData gdISCLeaderLength = new GridData(GridData.FILL_HORIZONTAL);
		gdISCLeaderLength.horizontalSpan = 2;
		iscLeaderLength.setLayoutData(gdISCLeaderLength);
		iscLeaderLength.getWidget().setMinimum(0);
		iscLeaderLength.getWidget().setMaximum(MAX_LEADER_LENGTH);
		iscLeaderLength.getWidget().setSelection((int) series.getLeaderLineLength());
		iscLeaderLength.addScreenReaderAccessibility(lblLeaderSize.getText());

		Composite cmpRight = new Composite(this, SWT.NONE);
		{
			cmpRight.setLayout(new GridLayout(3, false));
			cmpRight.setLayoutData(new GridData(GridData.FILL_BOTH));
		}

		Label lblInnerRadius = new Label(cmpRight, SWT.NONE);
		lblInnerRadius.setText(Messages.getString("PieSeriesAttributeComposite.Button.InnerRadius")); //$NON-NLS-1$

		comp = new Composite(cmpRight, SWT.NONE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		comp.setLayoutData(gd);

		gl = new GridLayout(3, false);
		gl.marginLeft = 0;
		gl.marginTop = 0;
		gl.marginRight = 0;
		gl.marginBottom = 0;
		gl.marginWidth = 0;
		gl.marginHeight = 0;
		comp.setLayout(gl);

		cmbInnerRadiusPercent = context.getUIFactory().createChartCombo(comp, SWT.DROP_DOWN | SWT.READ_ONLY, series,
				"innerRadiusPercent", //$NON-NLS-1$
				Messages.getString("PieBottomAreaComponent.Label.Percentage"));//$NON-NLS-1$
		{
			cmbInnerRadiusPercent.setItems(MINMUM_SLICE_ITEMS);
			cmbInnerRadiusPercent.setItemData(MINMUM_SLICE_ITEMS);
			cmbInnerRadiusPercent.setSelection(
					series.isInnerRadiusPercent() ? Messages.getString("PieBottomAreaComponent.Label.Percentage")//$NON-NLS-1$
							: Messages.getString("PieSeriesAttributeComposite.InnerRadiusType.Points")); //$NON-NLS-1$
			cmbInnerRadiusPercent.addSelectionListener(this);
		}

		txtInnerRadius = context.getUIFactory().createChartNumberEditor(comp, SWT.BORDER, "%", //$NON-NLS-1$
				series, "innerRadius");//$NON-NLS-1$
		new TextNumberEditorAssistField(txtInnerRadius.getTextControl(), null);
		{
			GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
			gridData.horizontalSpan = 2;
			txtInnerRadius.setLayoutData(gridData);
			txtInnerRadius.setValue(series.getInnerRadius());
			txtInnerRadius.addModifyListener(this);
		}

		lblInnerRadiusPercent = txtInnerRadius.getUnitLabel();

		lblInnerRadiusPercent.setVisible(series.isInnerRadiusPercent());
		createRatio(cmpRight);
		createRotation(cmpRight);
		createDirection(cmpRight);
		createSeriesDetail(cmpRight);

		updateInnerRadiusStates();
	}

	protected int getLeaderLineAttributesStyle() {
		return LineAttributesComposite.ENABLE_VISIBILITY | LineAttributesComposite.ENABLE_WIDTH
				| LineAttributesComposite.ENABLE_COLOR | LineAttributesComposite.ENABLE_STYLES
				| LineAttributesComposite.ENABLE_AUTO_COLOR;
	}

	protected void createLeaderLineStyle(Composite cmpStyle) {
		// Leader Line Style composite
		Label lblLeaderStyle = new Label(cmpStyle, SWT.NONE);
		GridData gdLBLLeaderStyle = new GridData();
		lblLeaderStyle.setLayoutData(gdLBLLeaderStyle);
		lblLeaderStyle.setText(Messages.getString("PieSeriesAttributeComposite.Lbl.LeaderLineStyle")); //$NON-NLS-1$

		cmbLeaderLine = context.getUIFactory().createChartCombo(cmpStyle, SWT.DROP_DOWN | SWT.READ_ONLY, series,
				"leaderLineStyle", //$NON-NLS-1$
				defSeries.getLeaderLineStyle().getName());
		GridData gdCMBLeaderLine = new GridData(GridData.FILL_HORIZONTAL);
		cmbLeaderLine.setLayoutData(gdCMBLeaderLine);
		cmbLeaderLine.addSelectionListener(this);

		NameSet ns = LiteralHelper.leaderLineStyleSet;
		cmbLeaderLine.setItems(ns.getDisplayNames());
		cmbLeaderLine.setItemData(ns.getNames());
		cmbLeaderLine.setSelection(series.getLeaderLineStyle().getName());
	}

	protected void createRatio(Composite cmpRight) {
		Label lblRatio = new Label(cmpRight, SWT.NONE);
		{
			lblRatio.setText(Messages.getString("PieBottomAreaComponent.Label.Ratio")); //$NON-NLS-1$
			lblRatio.setToolTipText(TOOLTIP_RATIO);
		}

		sRatio = context.getUIFactory().createChartSlider(cmpRight, SWT.HORIZONTAL, series, "ratio"); //$NON-NLS-1$
		{
			GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
			gridData.horizontalSpan = 2;
			sRatio.setLayoutData(gridData);
			sRatio.setValues((int) (series.getRatio() * 10), 1, 101, 1, 1, 10, 10);
			sRatio.setToolTipText(String.valueOf(series.getRatio()));
			sRatio.setEnabled(true);
			sRatio.addSelectionListener(this);
		}
	}

	protected void createRotation(Composite cmpRight) {
		Label lblRotation = new Label(cmpRight, SWT.NONE);
		{
			lblRotation.setText(Messages.getString("PieBottomAreaComponent.Label.Rotation")); //$NON-NLS-1$
			lblRotation.setToolTipText(TOOLTIP_ROTATION);
		}

		sRotation = context.getUIFactory().createChartSlider(cmpRight, SWT.HORIZONTAL, series, "rotation"); //$NON-NLS-1$
		{
			GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
			gridData.horizontalSpan = 2;
			sRotation.setLayoutData(gridData);
			sRotation.setValues((int) (series.getRotation()), 0, 360, 1, 1, 10, 1);
			sRotation.setToolTipText(String.valueOf(series.getRotation()));
			sRotation.setEnabled(true);
			sRotation.addSelectionListener(this);
		}
	}

	protected void createDirection(Composite cmpRight) {
		btnDirection = context.getUIFactory().createChartCheckbox(cmpRight, SWT.NONE, defSeries.isClockwise());
		{
			GridData gd = new GridData();
			gd.horizontalSpan = 3;
			btnDirection.setLayoutData(gd);
			btnDirection.setText(Messages.getString("PieSeriesAttributeComposite.Button.Direction")); //$NON-NLS-1$
			btnDirection.setToolTipText(Messages.getString("PieSeriesAttributeComposite.Button.Direction.ToolTipText")); //$NON-NLS-1$
			btnDirection.setSelectionState(series.isSetClockwise()
					? (series.isClockwise() ? ChartCheckbox.STATE_SELECTED : ChartCheckbox.STATE_UNSELECTED)
					: ChartCheckbox.STATE_GRAYED);
			btnDirection.addListener(SWT.Selection, this);
		}
	}

	private void updateInnerRadiusStates() {
		lblInnerRadiusPercent.setVisible(series.isInnerRadiusPercent());
	}

	private void createSeriesDetail(Composite cmpRight) {
		Group grpSlice = new Group(cmpRight, SWT.NONE);
		{
			GridLayout gridLayout = new GridLayout(3, false);
			gridLayout.marginWidth = 0;
			GridData gd = new GridData(GridData.FILL_BOTH);
			gd.horizontalSpan = 3;
			grpSlice.setLayoutData(gd);
			grpSlice.setLayout(gridLayout);
			grpSlice.setText(Messages.getString("PieSeriesAttributeComposite.Grp.Slice"));//$NON-NLS-1$
		}

		Label lblExpSliWhen = new Label(grpSlice, SWT.NONE);
		{
			lblExpSliWhen.setText(Messages.getString("PieBottomAreaComponent.Label.ExplodeSliceWhen")); //$NON-NLS-1$
			lblExpSliWhen.setToolTipText(TOOLTIP_EXPLODE_SLICE_WHEN);
		}

		txtExplode = new TextEditorComposite(grpSlice, SWT.BORDER | SWT.SINGLE);
		{
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			txtExplode.setLayoutData(gd);
			if (series.getExplosionExpression() != null) {
				txtExplode.setText(series.getExplosionExpression());
			}
			txtExplode.setToolTipText(Messages.getString("PieBaseSeriesComponent.Tooltip.EnterBooleanExpression")); //$NON-NLS-1$
			txtExplode.addListener(this);
		}

		btnBuilder = new Button(grpSlice, SWT.PUSH);
		{
			GridData gdBTNBuilder = new GridData();
			// TED 69967, the button size in mac is not as large as in windows,
			// so the fixed image size can't display in mac, need use auto computing
			if (!Platform.OS_MACOSX.equals(Platform.getOS())) {
				gdBTNBuilder.heightHint = 20;
				gdBTNBuilder.widthHint = 20;
			}
			btnBuilder.setLayoutData(gdBTNBuilder);
			btnBuilder.setImage(UIHelper.getImage("icons/obj16/expressionbuilder.gif")); //$NON-NLS-1$
			btnBuilder.addSelectionListener(this);
			btnBuilder.setToolTipText(Messages.getString("DataDefinitionComposite.Tooltip.InvokeExpressionBuilder")); //$NON-NLS-1$
			btnBuilder.getImage().setBackground(btnBuilder.getBackground());
			if (context.getUIServiceProvider() == null) {
				btnBuilder.setEnabled(false);
			}
		}

		Label lblExpDistance = new Label(grpSlice, SWT.NONE);
		{
			lblExpDistance.setText(Messages.getString("PieBottomAreaComponent.Label.ByDistance")); //$NON-NLS-1$
			lblExpDistance.setToolTipText(TOOLTIP_EXPLOSION_DISTANCE);
		}

		iscExplosion = context.getUIFactory().createChartSpinner(grpSlice, SWT.BORDER, series, "explosion", //$NON-NLS-1$
				true);
		{
			GridData gdISCExplosion = new GridData(GridData.FILL_HORIZONTAL);
			gdISCExplosion.horizontalSpan = 2;
			iscExplosion.setLayoutData(gdISCExplosion);
			iscExplosion.getWidget().setMinimum(0);
			iscExplosion.getWidget().setMaximum(100);
			iscExplosion.getWidget().setSelection(series.getExplosion());
		}

		// Slice outline color composite
		Label lblSliceOutline = new Label(grpSlice, SWT.NONE);
		GridData gdLBLSliceOutline = new GridData();
		lblSliceOutline.setLayoutData(gdLBLSliceOutline);
		lblSliceOutline.setText(Messages.getString("PieSeriesAttributeComposite.Lbl.SliceOutline")); //$NON-NLS-1$

		int fillStyles = FillChooserComposite.ENABLE_TRANSPARENT | FillChooserComposite.ENABLE_TRANSPARENT_SLIDER
				| FillChooserComposite.DISABLE_PATTERN_FILL;
		fillStyles |= context.getUIFactory().supportAutoUI() ? FillChooserComposite.ENABLE_AUTO : fillStyles;
		fccSliceOutline = new FillChooserComposite(grpSlice, SWT.NONE, fillStyles, context, series.getSliceOutline());
		GridData gdFCCSliceOutline = new GridData(GridData.FILL_HORIZONTAL);
		gdFCCSliceOutline.horizontalSpan = 2;
		fccSliceOutline.setLayoutData(gdFCCSliceOutline);
		fccSliceOutline.addListener(this);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.
	 * Event)
	 */
	public void handleEvent(Event event) {
		boolean isUnset = (event.detail == ChartUIExtensionUtil.PROPERTY_UNSET);
		if (event.widget.equals(fccSliceOutline)) {
			series.setSliceOutline((ColorDefinition) event.data);
		} else if (event.widget.equals(liacLeaderLine)) {
			switch (event.type) {
			case LineAttributesComposite.VISIBILITY_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(series.getLeaderLineAttributes(), "visible", //$NON-NLS-1$
						((Boolean) event.data).booleanValue(), isUnset);
				break;
			case LineAttributesComposite.STYLE_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(series.getLeaderLineAttributes(), "style", //$NON-NLS-1$
						event.data, isUnset);
				break;
			case LineAttributesComposite.WIDTH_CHANGED_EVENT:
				ChartElementUtil.setEObjectAttribute(series.getLeaderLineAttributes(), "thickness", //$NON-NLS-1$
						((Integer) event.data).intValue(), isUnset);
				break;
			case LineAttributesComposite.COLOR_CHANGED_EVENT:
				series.getLeaderLineAttributes().setColor((ColorDefinition) event.data);
				break;
			}
		} else if (event.widget.equals(txtExplode)) {
			series.setExplosionExpression(txtExplode.getText());
		} else if (event.widget == btnDirection) {
			ChartElementUtil.setEObjectAttribute(series, "clockwise", //$NON-NLS-1$
					btnDirection.getSelectionState() == ChartCheckbox.STATE_SELECTED,
					btnDirection.getSelectionState() == ChartCheckbox.STATE_GRAYED);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt
	 * .events.SelectionEvent)
	 */
	public void widgetSelected(SelectionEvent e) {
		if (e.getSource().equals(cmbLeaderLine)) {
			String selectedLineStyle = cmbLeaderLine.getSelectedItemData();
			if (selectedLineStyle != null) {
				ChartElementUtil.setEObjectAttribute(series, "leaderLineStyle", //$NON-NLS-1$
						LeaderLineStyle.getByName(selectedLineStyle), false);
			}
		} else if (e.widget.equals(btnBuilder)) {
			try {
				String sExpr = context.getUIServiceProvider().invoke(
						IUIServiceProvider.COMMAND_EXPRESSION_CHART_DATAPOINTS, txtExplode.getText(),
						context.getExtendedItem(), Messages.getString("PieBaseSeriesComponent.Text.SpecifyExplodeSlice") //$NON-NLS-1$
				);
				txtExplode.setText(sExpr);
				txtExplode.setToolTipText(sExpr);
				series.setExplosionExpression(sExpr);
			} catch (ChartException e1) {
				WizardBase.displayException(e1);
			}
		} else if (e.widget.equals(sRatio)) {
			series.setRatio(((double) sRatio.getSelection()) / 10);
			if (series.isSetRatio()) {
				sRatio.setToolTipText(String.valueOf(series.getRatio()));
			} else {
				sRatio.setToolTipText(String.valueOf(defSeries.getRatio()));
			}
		} else if (e.widget.equals(sRotation)) {
			series.setRotation(sRotation.getSelection());
			if (series.isSetRotation()) {
				sRotation.setToolTipText(String.valueOf(series.getRotation()));
			} else {
				sRotation.setToolTipText(String.valueOf(defSeries.getRotation()));
			}
		} else if (e.widget == cmbInnerRadiusPercent) {
			String selectedRadius = cmbInnerRadiusPercent.getSelectedItemData();
			if (selectedRadius != null) {
				series.setInnerRadiusPercent(
						selectedRadius.equals(Messages.getString("PieBottomAreaComponent.Label.Percentage"))); //$NON-NLS-1$
			}
			updateInnerRadiusStates();
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

	public void modifyText(ModifyEvent event) {
		if (event.widget == txtInnerRadius) {
			if (!TextEditorComposite.TEXT_RESET_MODEL.equals(event.data)) {
				series.setInnerRadius(txtInnerRadius.getValue());
			}
		}
	}
}
