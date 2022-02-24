/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard.format.chart;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.Angle3D;
import org.eclipse.birt.chart.model.attribute.AngleType;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Interactivity;
import org.eclipse.birt.chart.model.attribute.impl.InteractivityImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.util.ChartDefaultValueUtil;
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.AbstractChartNumberEditor;
import org.eclipse.birt.chart.ui.swt.ChartCheckbox;
import org.eclipse.birt.chart.ui.swt.ChartPreviewPainterBase;
import org.eclipse.birt.chart.ui.swt.ChartSpinner;
import org.eclipse.birt.chart.ui.swt.composites.FillChooserComposite;
import org.eclipse.birt.chart.ui.swt.composites.LocalizedNumberEditorComposite;
import org.eclipse.birt.chart.ui.swt.composites.TextEditorComposite;
import org.eclipse.birt.chart.ui.swt.composites.TriggerDataComposite;
import org.eclipse.birt.chart.ui.swt.fieldassist.TextNumberEditorAssistField;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.ITaskPopupSheet;
import org.eclipse.birt.chart.ui.swt.wizard.ChartAdapter;
import org.eclipse.birt.chart.ui.swt.wizard.format.SubtaskSheetImpl;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.InteractivitySheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart.BlockPropertiesSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart.CustomPropertiesSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart.GeneralPropertiesChartSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart.VisibilitySheet;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.TriggerSupportMatrix;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import com.ibm.icu.text.DecimalFormatSymbols;

/**
 * Chart Area subtask
 * 
 */
public class ChartSheetImpl extends SubtaskSheetImpl implements SelectionListener, Listener {

	protected FillChooserComposite cmbBackground;

	protected FillChooserComposite fccWall;

	protected FillChooserComposite fccFloor;

	private Combo cmbStyle;

	private Button btnEnablePreview;

	private Button btnResetValue;

	private ChartCheckbox btnEnable;

	private AxisRotationChooser xChooser;

	private AxisRotationChooser yChooser;

	private AxisRotationChooser zChooser;

	protected ChartSpinner spnCorverage;

	private ChartCheckbox btnStudyLayout;

	protected static final int DEFAULT_COVERAGE = 50;

	public void createControl(Composite parent) {
		ChartUIUtil.bindHelp(parent, ChartHelpContextIds.SUBTASK_CHART);
		Chart defChart = ChartDefaultValueUtil.getDefaultValueChart(getChart());
		init();
		Composite cmpBasic = createContentComposite(parent);
		createAdditionalComposite(cmpBasic);
		createChartBackgroundComposite(cmpBasic);
		createChartWallNFloorComposite(cmpBasic);
		createStyleNPreviewComposite(cmpBasic);
		createAngleChooserComposite(cmpContent);
		createCoverageComposite(cmpBasic);
		createInteractivityComposite(defChart, cmpBasic);
		createStudyLayoutComposite(defChart, cmpBasic);
		populateLists();
		createButtonGroup(cmpContent);
	}

	protected Composite createContentComposite(Composite parent) {
		cmpContent = new Composite(parent, SWT.NONE);
		{
			GridLayout glContent = new GridLayout(3, true);
			cmpContent.setLayout(glContent);
			GridData gd = new GridData(GridData.FILL_BOTH);
			cmpContent.setLayoutData(gd);
		}

		Composite cmpBasic = new Composite(cmpContent, SWT.NONE);
		{
			cmpBasic.setLayout(new GridLayout(3, false));
			GridData gd = new GridData(GridData.FILL_BOTH);
			gd.horizontalSpan = 2;
			cmpBasic.setLayoutData(gd);
		}
		return cmpBasic;
	}

	protected void createChartBackgroundComposite(Composite cmpBasic) {
		Label lblBackground = new Label(cmpBasic, SWT.NONE);
		lblBackground.setText(Messages.getString("ChartSheetImpl.Label.Background")); //$NON-NLS-1$

		cmbBackground = new FillChooserComposite(cmpBasic, SWT.NONE, getBackgroundFillStyles(), getContext(),
				getChart().getBlock().getBackground());
		{
			GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
			cmbBackground.setLayoutData(gridData);
			cmbBackground.addListener(this);
		}

		new Label(cmpBasic, SWT.NONE);
	}

	protected void createChartWallNFloorComposite(Composite cmpBasic) {
		if (hasWallAndFloor()) {
			Label lblWall = new Label(cmpBasic, SWT.NONE);
			lblWall.setLayoutData(new GridData());
			lblWall.setText(Messages.getString("AttributeSheetImpl.Lbl.ChartWall")); //$NON-NLS-1$

			fccWall = new FillChooserComposite(cmpBasic, SWT.NONE, getContext(),
					((ChartWithAxes) getChart()).getWallFill(), true, true, true, true);
			GridData gdFCCWall = new GridData(GridData.FILL_HORIZONTAL);
			fccWall.setLayoutData(gdFCCWall);
			fccWall.addListener(this);

			new Label(cmpBasic, SWT.NONE);

			Label lblFloor = new Label(cmpBasic, SWT.NONE);
			lblFloor.setLayoutData(new GridData());
			lblFloor.setText(Messages.getString("AttributeSheetImpl.Lbl.ChartFloor")); //$NON-NLS-1$

			fccFloor = new FillChooserComposite(cmpBasic, SWT.NONE, getContext(),
					((ChartWithAxes) getChart()).getFloorFill(), true, true, true, true);
			GridData gdFCCFloor = new GridData(GridData.FILL_HORIZONTAL);
			fccFloor.setLayoutData(gdFCCFloor);
			fccFloor.addListener(this);

			new Label(cmpBasic, SWT.NONE);
		}
	}

	protected void createInteractivityComposite(Chart defChart, Composite cmpBasic) {
		btnEnable = getContext().getUIFactory().createChartCheckbox(cmpBasic, SWT.NONE,
				defChart.getInteractivity().isEnable());
		{
			GridData gridData = new GridData();
			gridData.horizontalSpan = 3;
			btnEnable.setLayoutData(gridData);
			btnEnable.setText(Messages.getString("ChartSheetImpl.Label.InteractivityEnable")); //$NON-NLS-1$
			btnEnable.setSelectionState(!getChart().getInteractivity().isSetEnable() ? ChartCheckbox.STATE_GRAYED
					: (getChart().getInteractivity().isEnable() ? ChartCheckbox.STATE_SELECTED
							: ChartCheckbox.STATE_UNSELECTED));
			btnEnable.addSelectionListener(this);
		}
	}

	protected void createStudyLayoutComposite(Chart defChart, Composite cmpBasic) {
		// #170985
		if (enableStudyLayout()) {
			btnStudyLayout = getContext().getUIFactory().createChartCheckbox(cmpBasic, SWT.NONE,
					((ChartWithAxes) defChart).isStudyLayout());
			GridData gridData = new GridData();
			gridData.horizontalSpan = 3;
			btnStudyLayout.setLayoutData(gridData);
			btnStudyLayout.setText(Messages.getString("ChartSheetImpl.Button.EnableStudyLayout")); //$NON-NLS-1$
			btnStudyLayout
					.setSelectionState(((ChartWithAxes) getChart()).isSetStudyLayout()
							? (((ChartWithAxes) getChart()).isStudyLayout() ? ChartCheckbox.STATE_SELECTED
									: ChartCheckbox.STATE_UNSELECTED)
							: ChartCheckbox.STATE_GRAYED);
			btnStudyLayout.addSelectionListener(this);
		}
	}

	protected void createCoverageComposite(Composite cmpBasic) {
		if (!(getChart() instanceof ChartWithoutAxes)) {
			return;
		}

		ChartWithoutAxes cwa = (ChartWithoutAxes) getChart();

		new Label(cmpBasic, SWT.NONE).setText(Messages.getString("ChartSheetImpl.Label.Coverage")); //$NON-NLS-1$

		Composite cmpCoverage = new Composite(cmpBasic, SWT.NONE);
		{
			cmpCoverage.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			GridLayout layout = new GridLayout(2, false);
			layout.verticalSpacing = 0;
			layout.marginHeight = 0;
			layout.marginWidth = 0;
			cmpCoverage.setLayout(layout);
		}

		spnCorverage = getContext().getUIFactory().createChartSpinner(cmpCoverage, SWT.BORDER, cwa, "coverage", //$NON-NLS-1$
				true, null, "%"); //$NON-NLS-1$
		spnCorverage.setRatio(100);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		spnCorverage.setLayoutData(gd);
		int spnValue = (int) (cwa.getCoverage() * 100);
		if (!cwa.isSetCoverage()) {
			spnValue = DEFAULT_COVERAGE;
		}
		spnCorverage.getWidget().setValues(spnValue, 1, 100, 0, 1, 10);
	}

	protected void createAngleChooserComposite(Composite cmpContent) {
		Composite cmp3D = new Composite(cmpContent, SWT.NONE);
		{
			cmp3D.setLayout(new GridLayout());
			cmp3D.setLayoutData(new GridData(GridData.FILL_BOTH));
		}

		if (!((getChart() instanceof ChartWithAxes) && is3DEnabled())) {
			return;
		}

		Group cmpRotation = new Group(cmp3D, SWT.NONE);
		{
			GridLayout gl = new GridLayout();
			gl.marginTop = 0;
			gl.verticalSpacing = 0;
			cmpRotation.setLayout(gl);
			cmpRotation.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			cmpRotation.setText(Messages.getString("ChartLegendImpl.Group.Rotation")); //$NON-NLS-1$
		}

		xChooser = new AxisRotationChooser(ChartUIUtil.getAxisXForProcessing((ChartWithAxes) getChart()), AngleType.X);
		xChooser.placeComponents(cmpRotation);

		yChooser = new AxisRotationChooser(ChartUIUtil.getAxisYForProcessing((ChartWithAxes) getChart(), 0),
				AngleType.Y);
		yChooser.placeComponents(cmpRotation);

		zChooser = new AxisRotationChooser(ChartUIUtil.getAxisZForProcessing((ChartWithAxes) getChart()), AngleType.Z);
		zChooser.placeComponents(cmpRotation);

		btnResetValue = new Button(cmpRotation, SWT.PUSH);
		{
			btnResetValue.setText(Messages.getString("ChartSheetImpl.Label.ResetValue")); //$NON-NLS-1$
			btnResetValue.setSelection(ChartPreviewPainterBase.isProcessorEnabled());
			btnResetValue.addSelectionListener(this);
		}
	}

	protected boolean enableStudyLayout() {
		return ChartUtil.hasMultipleYAxes(getChart());
	}

	protected void createStyleNPreviewComposite(Composite cmpBasic) {
		new Label(cmpBasic, SWT.NONE).setText(Messages.getString("ChartSheetImpl.Label.Style")); //$NON-NLS-1$

		cmbStyle = new Combo(cmpBasic, SWT.DROP_DOWN | SWT.READ_ONLY);
		{
			GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
			cmbStyle.setLayoutData(gridData);
			cmbStyle.addSelectionListener(this);
		}

		btnEnablePreview = new Button(cmpBasic, SWT.CHECK);
		{
			btnEnablePreview.setText(Messages.getString("ChartSheetImpl.Label.EnableInPreview")); //$NON-NLS-1$
			btnEnablePreview.setSelection(ChartPreviewPainterBase.isProcessorEnabled());
			btnEnablePreview.addSelectionListener(this);
		}
	}

	protected boolean is3DEnabled() {
		return ChartUIUtil.is3DType(getChart());
	}

	protected void createAdditionalComposite(Composite cmpBasic) {

	}

	protected void init() {
		// Make it compatible with old model
		if (getChart().getInteractivity() == null) {
			Interactivity interactivity = InteractivityImpl.create();
			interactivity.eAdapters().addAll(getChart().eAdapters());
			getChart().setInteractivity(interactivity);
		}
	}

	protected void populateLists() {
		if (cmbStyle == null) {
			return;
		}
		// POPULATE STYLE COMBO WITH AVAILABLE REPORT STYLES
		IDataServiceProvider idsp = getContext().getDataServiceProvider();
		if (idsp != null) {
			String[] allStyleNames = idsp.getAllStyles();
			String[] displayNames = idsp.getAllStyleDisplayNames();

			// Add None option to remove style
			String[] selection = new String[displayNames.length + 1];
			System.arraycopy(displayNames, 0, selection, 1, displayNames.length);
			selection[0] = Messages.getString("ChartSheetImpl.Label.None"); //$NON-NLS-1$
			cmbStyle.setItems(selection);
			cmbStyle.setData(allStyleNames);

			String sStyle = idsp.getCurrentStyle();
			int idx = getStyleIndex(sStyle);
			cmbStyle.select(idx + 1);
		}
	}

	private int getStyleIndex(String style) {
		String[] allStyleNames = (String[]) cmbStyle.getData();

		if (style != null && allStyleNames != null) {
			for (int i = 0; i < allStyleNames.length; i++) {
				if (style.equals(allStyleNames[i])) {
					return i;
				}
			}
		}

		return -1;
	}

	protected void createButtonGroup(Composite parent) {
		Composite cmp = new Composite(parent, SWT.NONE);
		{
			cmp.setLayout(new GridLayout(5, false));
			GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
			gridData.horizontalSpan = 3;
			gridData.grabExcessVerticalSpace = true;
			gridData.verticalAlignment = SWT.END;
			cmp.setLayoutData(gridData);
		}

		ITaskPopupSheet popup;

		popup = new BlockPropertiesSheet(Messages.getString("ChartSheetImpl.Text.Outline"), //$NON-NLS-1$
				getContext());
		Button btnBlockProp = createToggleButton(cmp, BUTTON_OUTLINE,
				Messages.getString("ChartSheetImpl.Text.Outline&"), //$NON-NLS-1$
				popup);
		btnBlockProp.addSelectionListener(this);

		// Chart Visibility
		popup = new VisibilitySheet(Messages.getString("ChartSheetImpl.Group.EmptyMessage"), //$NON-NLS-1$
				getContext());
		Button btnVisibilityProp = createToggleButton(cmp, BUTTON_VISIBILITY,
				Messages.getString("ChartSheetImpl.Group.EmptyMessage&"), //$NON-NLS-1$
				popup);
		btnVisibilityProp.addSelectionListener(this);

		popup = new GeneralPropertiesChartSheet(Messages.getString("ChartSheetImpl.Text.GeneralProperties"), //$NON-NLS-1$
				getContext());
		Button btnGeneralProp = createToggleButton(cmp, BUTTON_GERNERAL,
				Messages.getString("ChartSheetImpl.Text.GeneralProperties&"), //$NON-NLS-1$
				popup);
		btnGeneralProp.addSelectionListener(this);

		popup = new CustomPropertiesSheet(Messages.getString("ChartSheetImpl.Text.CustomProperties"), //$NON-NLS-1$
				getContext());
		Button btnCustomProp = createToggleButton(cmp, BUTTON_CUSTOM,
				Messages.getString("ChartSheetImpl.Text.CustomProperties&"), //$NON-NLS-1$
				popup);
		btnCustomProp.addSelectionListener(this);

		// Interactivity
		if (getContext().isInteractivityEnabled()) {
			popup = new InteractivitySheet(Messages.getString("ChartSheetImpl.Label.Interactivity"), //$NON-NLS-1$
					getContext(), getChart().getBlock().getTriggers(), getChart().getBlock(),
					TriggerSupportMatrix.TYPE_CHARTAREA, TriggerDataComposite.ENABLE_SHOW_TOOLTIP_VALUE);
			Button btnInteractivity = createToggleButton(cmp, BUTTON_INTERACTIVITY,
					Messages.getString("SeriesYSheetImpl.Label.Interactivity&"), //$NON-NLS-1$
					popup, getChart().getInteractivity().isEnable());
			btnInteractivity.addSelectionListener(this);
		}
	}

	protected int getBackgroundFillStyles() {
		return FillChooserComposite.ENABLE_AUTO | FillChooserComposite.ENABLE_GRADIENT
				| FillChooserComposite.ENABLE_IMAGE | FillChooserComposite.ENABLE_TRANSPARENT
				| FillChooserComposite.ENABLE_TRANSPARENT_SLIDER;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.
	 * Event)
	 */
	public void handleEvent(Event event) {
		if (event.widget.equals(cmbBackground)) {
			getChart().getBlock().setBackground((Fill) event.data);
		} else if (event.widget.equals(fccWall)) {
			if (hasWallAndFloor()) {
				((ChartWithAxes) getChart()).setWallFill((Fill) event.data);
			}
		} else if (event.widget.equals(fccFloor)) {
			if (hasWallAndFloor()) {
				((ChartWithAxes) getChart()).setFloorFill((Fill) event.data);
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

		if (e.widget.equals(cmbStyle)) {
			String[] allStyleNames = (String[]) cmbStyle.getData();
			String sStyle = null;
			int idx = cmbStyle.getSelectionIndex();
			if (idx > 0) {
				sStyle = allStyleNames[idx - 1];
			}
			getContext().getDataServiceProvider().setStyle(sStyle);
			if (btnEnablePreview.getSelection()) {
				refreshPreview();
			}
		} else if (e.widget.equals(btnEnablePreview)) {
			ChartPreviewPainterBase.enableProcessor(btnEnablePreview.getSelection());
			refreshPreview();
		} else if (e.widget.equals(btnEnable)) {
			int state = btnEnable.getSelectionState();
			ChartElementUtil.setEObjectAttribute(getChart().getInteractivity(), "enable", //$NON-NLS-1$
					state == ChartCheckbox.STATE_SELECTED, state == ChartCheckbox.STATE_GRAYED);
			setToggleButtonEnabled(BUTTON_INTERACTIVITY, state == ChartCheckbox.STATE_SELECTED);

			if (getToggleButtonSelection(BUTTON_INTERACTIVITY)) {
				detachPopup();
			}
		} else if (e.widget.equals(btnResetValue)) {
			if (!xChooser.isAutoAngle()) {
				setAxisAngle(AngleType.X, -20);
				xChooser.txtRotation.setValue(-20);
			}
			if (!yChooser.isAutoAngle()) {
				setAxisAngle(AngleType.Y, 45);
				yChooser.txtRotation.setValue(45);
			}
			if (!zChooser.isAutoAngle()) {
				setAxisAngle(AngleType.Z, 0);
				zChooser.txtRotation.setValue(0);
			}
		} else if (e.widget == btnStudyLayout) {
			if (getChart() instanceof ChartWithAxes) {
				ChartWithAxes cwa = (ChartWithAxes) getChart();
				if (btnStudyLayout.getSelectionState() == ChartCheckbox.STATE_GRAYED) {
					cwa.unsetStudyLayout();
				} else {
					cwa.setStudyLayout(btnStudyLayout.getSelectionState() == ChartCheckbox.STATE_SELECTED);
				}
			}
		}

	}

	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * Refreshes the preview by model modification. Used by non-model change.
	 * 
	 */
	private void refreshPreview() {
		// Populate a model changed event to refresh the preview canvas.
		boolean currentValue = getChart().getTitle().isVisible();
		ChartAdapter.ignoreNotifications(true);
		getChart().getTitle().setVisible(!currentValue);
		ChartAdapter.ignoreNotifications(false);
		getChart().getTitle().setVisible(currentValue);
	}

	protected boolean hasWallAndFloor() {
		return (getChart() instanceof ChartWithAxes)
				&& (getChart().getDimension().getValue() != ChartDimension.TWO_DIMENSIONAL);
	}

	private class AxisRotationChooser implements SelectionListener, ModifyListener {

		private Button btnAntiRotation;

		private Button btnRotation;

		private AbstractChartNumberEditor txtRotation;

		private int angleType;

		public AxisRotationChooser(Axis axis, int angleType) {
			this.angleType = angleType;
		}

		public void placeComponents(Composite parent) {
			Composite context = new Composite(parent, SWT.NONE);
			{
				GridLayout gl = new GridLayout(4, false);
				gl.horizontalSpacing = 8;
				context.setLayout(gl);
				context.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			}

			btnAntiRotation = new Button(context, SWT.PUSH);
			{
				GridData gd = new GridData();
				ChartUIUtil.setChartImageButtonSizeByPlatform(gd);
				btnAntiRotation.setLayoutData(gd);
				btnAntiRotation.setImage(UIHelper.getImage(getImagePath(angleType, true)));
				btnAntiRotation.addSelectionListener(this);
			}

			btnRotation = new Button(context, SWT.PUSH);
			{
				GridData gd = new GridData();
				gd.widthHint = 20;
				gd.heightHint = 20;
				btnRotation.setLayoutData(gd);
				btnRotation.setImage(UIHelper.getImage(getImagePath(angleType, false)));
				btnRotation.addSelectionListener(this);
			}

			txtRotation = getContext().getUIFactory().createChartNumberEditor(context, SWT.BORDER | SWT.SINGLE, null,
					getAngle3D(), getAxisAngleProperty(angleType));
			new RotationEditorAssistField((LocalizedNumberEditorComposite) txtRotation, null);
			{
				GridData gd = new GridData(GridData.FILL_HORIZONTAL);
				gd.horizontalSpan = 2;
				txtRotation.setLayoutData(gd);
				txtRotation.setValue(getAxisAngle(angleType));
				txtRotation.addModifyListener(this);
			}

			updateUIState(txtRotation.getTextControl().isEnabled());
		}

		private boolean isAutoAngle() {
			return !ChartElementUtil.isSetEObjectAttribute(getAngle3D(), getAxisAngleProperty(angleType));
		}

		private void updateUIState(boolean enabled) {
			btnAntiRotation.setEnabled(enabled);
			btnRotation.setEnabled(enabled);
		}

		public void widgetSelected(SelectionEvent e) {
			if (e.widget.equals(btnAntiRotation)) {
				setAxisAngle(angleType, (int) getAxisAngle(angleType) - 10);
				txtRotation.setValue(getAxisAngle(angleType));
			} else if (e.widget.equals(btnRotation)) {
				setAxisAngle(angleType, (int) getAxisAngle(angleType) + 10);
				txtRotation.setValue(getAxisAngle(angleType));
			}
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.
		 * events.ModifyEvent)
		 */
		public void modifyText(ModifyEvent e) {
			if (e.widget.equals(txtRotation)) {
				if (!TextEditorComposite.TEXT_RESET_MODEL.equals(e.data)) {
					setAxisAngle(angleType, (int) txtRotation.getValue());
				}
				updateUIState(txtRotation.getTextControl().isEnabled());
			}
		}

		private String getImagePath(int angleType, boolean bAntiRotation) {
			String basePath = "icons/obj16/"; //$NON-NLS-1$
			String filename = null;
			switch (angleType) {
			case AngleType.X:
				filename = bAntiRotation ? "x_rotation.gif" : "x_anti_rotation.gif"; //$NON-NLS-1$ //$NON-NLS-2$
				break;
			case AngleType.Y:
				filename = bAntiRotation ? "y_anti_rotation.gif" : "y_rotation.gif"; //$NON-NLS-1$ //$NON-NLS-2$
				break;
			case AngleType.Z:
				filename = bAntiRotation ? "z_rotation.gif" : "z_anti_rotation.gif"; //$NON-NLS-1$ //$NON-NLS-2$
				break;
			}
			return basePath + filename;
		}

		private double getAxisAngle(int angleType) {
			switch (angleType) {
			case AngleType.X:
				return getAngle3D().getXAngle();
			case AngleType.Y:
				return getAngle3D().getYAngle();
			case AngleType.Z:
				return getAngle3D().getZAngle();
			default:
				return 0;
			}
		}

		private String getAxisAngleProperty(int angleType) {
			switch (angleType) {
			case AngleType.X:
				return "xAngle"; //$NON-NLS-1$
			case AngleType.Y:
				return "yAngle"; //$NON-NLS-1$
			case AngleType.Z:
				return "zAngle"; //$NON-NLS-1$
			}
			return "xAngle"; //$NON-NLS-1$
		}
	}

	/**
	 * RotationEditorAssistField
	 */
	static class RotationEditorAssistField extends TextNumberEditorAssistField {

		/**
		 * Constructor of the class.
		 * 
		 * @param numberEditor
		 * @param composite
		 */
		public RotationEditorAssistField(LocalizedNumberEditorComposite numberEditor, Composite composite) {
			super(numberEditor.getTextControl(), composite);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.chart.ui.swt.fieldassist.SmartField#isValid()
		 */
		@Override
		public boolean isValid() {
			String contents = getContents();
			if (contents == null || "".equals(contents.trim())) //$NON-NLS-1$
			{
				return true;
			}

			char groupingSeparator = DecimalFormatSymbols.getInstance().getGroupingSeparator();
			int length = contents.length();
			for (int i = 0; i < length;) {
				char ch = contents.charAt(i++);
				if (!Character.isDigit(ch) && ch != '.' && ch != '-' && ch != '+' && ch != groupingSeparator) {
					return false;
				}
			}

			return true;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @seeorg.eclipse.birt.chart.ui.swt.fieldassist.
		 * LocalizedNumberEditorAssistField#quickFix()
		 */
		public void quickFix() {
			String contents = getContents();
			StringBuffer numbersOnly = new StringBuffer();
			char groupingSeparator = DecimalFormatSymbols.getInstance().getGroupingSeparator();
			int length = contents.length();
			for (int i = 0; i < length;) {
				char ch = contents.charAt(i++);
				if (Character.isDigit(ch) || ch == '.' || ch == '-' || ch == '+' || ch == groupingSeparator) {
					numbersOnly.append(ch);
				}
			}
			setContents(numbersOnly.toString());
		}
	}

	private void setAxisAngle(int angleType, int angleDegree) {
		Angle3D angle3D = getAngle3D();
		angle3D.setType(AngleType.NONE_LITERAL);
		((ChartWithAxes) getChart()).getRotation().getAngles().clear();
		((ChartWithAxes) getChart()).getRotation().getAngles().add(angle3D);

		switch (angleType) {
		case AngleType.X:
			angle3D.setXAngle(angleDegree);
			break;
		case AngleType.Y:
			angle3D.setYAngle(angleDegree);
			break;
		case AngleType.Z:
			angle3D.setZAngle(angleDegree);
			break;
		}
	}

	private Angle3D getAngle3D() {
		return ((ChartWithAxes) getChart()).getRotation().getAngles().get(0);
	}

	@Override
	public void dispose() {
		super.dispose();
		ChartPreviewPainterBase.enableProcessor(true);
	}

}
