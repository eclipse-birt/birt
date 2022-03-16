/***********************************************************************
 * Copyright (c) 2004-2011 Actuate Corporation.
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

package org.eclipse.birt.chart.ui.swt.composites;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.ActionValue;
import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.Cursor;
import org.eclipse.birt.chart.model.attribute.CursorType;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.MultiURLValues;
import org.eclipse.birt.chart.model.attribute.ScriptValue;
import org.eclipse.birt.chart.model.attribute.SeriesValue;
import org.eclipse.birt.chart.model.attribute.TooltipValue;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.attribute.URLValue;
import org.eclipse.birt.chart.model.attribute.impl.MultiURLValuesImpl;
import org.eclipse.birt.chart.model.attribute.impl.TextImpl;
import org.eclipse.birt.chart.model.attribute.impl.TooltipValueImpl;
import org.eclipse.birt.chart.model.component.impl.LabelImpl;
import org.eclipse.birt.chart.model.data.Action;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.model.data.impl.ActionImpl;
import org.eclipse.birt.chart.model.data.impl.TriggerImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.interfaces.IAssistField;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.IExpressionButton;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.chart.ui.swt.wizard.ChartAdapter;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.ChartUIUtil.EAttributeAccessor;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.chart.util.TriggerSupportMatrix;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.birt.core.ui.swt.custom.TextCombo;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
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
import org.eclipse.swt.widgets.Text;

/**
 * Interactivity UI composite
 *
 */
public class TriggerDataComposite extends Composite implements SelectionListener {

	public static final int ENABLE_URL_PARAMETERS = 1;

	public static final int DISABLE_CATEGORY_SERIES = 1 << 1;

	public static final int DISABLE_VALUE_SERIES = 1 << 2;

	public static final int DISABLE_VALUE_SERIES_NAME = 1 << 3;

	public static final int ENABLE_SHOW_TOOLTIP_VALUE = 1 << 4;

	public static final int ENABLE_TOOLTIP_FORMATTER = 1 << 5;

	private Group grpValue = null;

	private Composite cmpURL = null;

	private MultipleHyperlinksComposite multiHyperlinksComposite = null;

	// private Text txtBaseURL = null;

	// private Text txtTarget = null;

	private Group grpParameters = null;

	private Text txtBaseParm = null;

	private Text txtValueParm = null;

	private Text txtSeriesParm = null;

	private Composite cmpCallback = null;

	private Composite cmpDefault = null;

	private Composite cmpScript = null;

	private Text txtScript = null;

	private IExpressionButton btnScriptExpBuilder = null;

	private Composite cmpTooltip = null;

	// private Spinner iscDelay = null;

	private Text txtTooltipText = null;

	private Button btnFormat = null;

	private IExpressionButton btnTooltipExpBuilder = null;

	private Composite cmpVisiblity = null;

	private Composite cmpHighlight = null;

	private Composite cmpDPVisibility = null;

	// private Text txtSeriesDefinition = null;

	private StackLayout slValues = null;

	private TextCombo cmbTriggerType = null;

	private Combo cmbActionType = null;

	private ChartWizardContext wizardContext;

	private Button btnBaseURL;

	private Button btnAdvanced;

	private String sBaseURL = ""; //$NON-NLS-1$

	private boolean bAdvanced = false;

	private EList<Trigger> triggersList;

	private EObject cursorContainer;

	private Map<String, Trigger> triggersMap;

	private String lastTriggerType;

	private FormatSpecifier formatSpecifier = null;

	// Indicates whether the trigger will be saved when UI is disposed
	private boolean needSaveWhenDisposing = false;

	private final static int INDEX_1_URL_REDIRECT = 1;
	private final static int INDEX_2_TOOLTIP = 2;
	private final static int INDEX_3_TOOGLE_VISABILITY = 3;
	private final static int INDEX_4_SCRIPT = 4;
	private final static int INDEX_5_HIGHLIGHT = 5;
	private final static int INDEX_6_CALLBACK = 6;
	private final static int INDEX_7_TOOGLE_DATAPOINT = 7;

	private TriggerSupportMatrix triggerMatrix;

	private int optionalStyle;

	private Combo cmbCursorType = null;

	private Button btnCursorImage;

	private final TriggerCondition[] conditionFilter;
	private final CursorType[] cursorFilter;

	/**
	 * Interactivity UI constructor
	 *
	 * @param parent             composite parent
	 * @param style              composite style
	 * @param triggers           trigger model
	 * @param cursorContainer    cursor model container
	 * @param wizardContext      wizard context
	 * @param iInteractivityType interactivity type. See
	 *                           {@link TriggerSupportMatrix}
	 * @param optionalStyle      optional UI settings
	 */
	public TriggerDataComposite(Composite parent, int style, EList<Trigger> triggers, EObject cursorContainer,
			ChartWizardContext wizardContext, int iInteractivityType, int optionalStyle) {
		super(parent, style);
		this.wizardContext = wizardContext;
		this.optionalStyle = optionalStyle;
		this.triggersList = triggers;
		this.cursorContainer = cursorContainer;
		this.triggerMatrix = wizardContext.getUIFactory().createSupportMatrix(wizardContext.getOutputFormat(),
				iInteractivityType);
		this.conditionFilter = triggerMatrix.getConditionFilters();
		this.cursorFilter = triggerMatrix.getCursorFilters();
		init();

		placeComponents();

		addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				if (needSaveWhenDisposing) {
					// Only save when it's needed
					updateTrigger(cmbTriggerType.getText());
				}
			}
		});
	}

	protected TriggerSupportMatrix createSupportMatrix(int iInteractivityType) {
		return new TriggerSupportMatrix(wizardContext.getOutputFormat(), iInteractivityType);
	}

	private void init() {
		this.setSize(getParent().getClientArea().width, getParent().getClientArea().height);

		triggersMap = new HashMap<>();
		for (int i = 0; i < triggersList.size(); i++) {
			Trigger trigger = triggersList.get(i);
			triggersMap.put(LiteralHelper.triggerConditionSet.getDisplayNameByName(trigger.getCondition().getName()),
					trigger);
		}
	}

	private void addFormatButtonListener() {
		if (btnFormat != null) {
			btnFormat.addListener(SWT.Selection, new Listener() {

				@Override
				public void handleEvent(Event event) {
					FormatSpecifierDialog editor = new FormatSpecifierDialog(btnFormat.getShell(), formatSpecifier,
							AxisType.values(), ""); //$NON-NLS-1$
					if (editor.open() == Window.OK) {
						formatSpecifier = editor.getFormatSpecifier();
						updateTrigger(cmbTriggerType.getText());
					}

				}
			});
		}
	}

	private void placeComponents() {
		// Layout for the content composite
		GridLayout glCMPTrigger = new GridLayout();
		glCMPTrigger.numColumns = 3;
		glCMPTrigger.horizontalSpacing = 16;
		glCMPTrigger.verticalSpacing = 5;

		// Layout for the Action Details group
		slValues = new StackLayout();

		// Layout for url value composite
		GridLayout glURL = new GridLayout();
		glURL.marginWidth = 2;
		glURL.marginHeight = 6;
		glURL.horizontalSpacing = 6;
		glURL.numColumns = 3;

		// Layout for script value composite
		GridLayout glParameter = new GridLayout();
		glParameter.marginWidth = 2;
		glParameter.marginHeight = 6;
		glParameter.horizontalSpacing = 6;
		glParameter.numColumns = 3;

		// Main content composite
		this.setLayout(glCMPTrigger);

		Label lblTriggerEvent = new Label(this, SWT.NONE);
		GridData gdLBLTriggerEvent = new GridData();
		gdLBLTriggerEvent.horizontalIndent = 4;
		lblTriggerEvent.setLayoutData(gdLBLTriggerEvent);
		lblTriggerEvent.setText(Messages.getString("TriggerDataComposite.Lbl.Event")); //$NON-NLS-1$

		cmbTriggerType = new TextCombo(this, SWT.NONE);
		GridData gdCMBTriggerType = new GridData(GridData.FILL_HORIZONTAL);
		gdCMBTriggerType.horizontalSpan = 2;
		cmbTriggerType.setLayoutData(gdCMBTriggerType);
		cmbTriggerType.addListener(TextCombo.SELECTION_EVENT, new Listener() {

			@Override
			public void handleEvent(Event event) {
				updateTrigger(lastTriggerType);
				updateActionTypeItems();
				Trigger trigger = triggersMap.get(cmbTriggerType.getText());
				// Only display supported trigger
				if (trigger != null && triggerMatrix.check(trigger)) {
					cmbActionType.setText(getActionText(trigger));
				} else {
					cmbActionType.select(0);
				}
				updateUI(trigger);
				switchUI();
				lastTriggerType = cmbTriggerType.getText();
			}
		});
		ChartUIUtil.addScreenReaderAccessbility(cmbTriggerType, lblTriggerEvent.getText());

		Label lblActionType = new Label(this, SWT.NONE);
		GridData gdLBLActionType = new GridData();
		gdLBLActionType.horizontalIndent = 4;
		lblActionType.setLayoutData(gdLBLActionType);
		lblActionType.setText(Messages.getString("TriggerDataComposite.Lbl.Action")); //$NON-NLS-1$

		cmbActionType = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
		GridData gdCMBActionType = new GridData(GridData.FILL_HORIZONTAL);
		gdCMBActionType.horizontalSpan = 2;
		cmbActionType.setLayoutData(gdCMBActionType);
		cmbActionType.addSelectionListener(this);
		cmbActionType.setVisibleItemCount(10);

		Label lblCursorType = new Label(this, SWT.NONE);
		GridData gdLBLCursorType = new GridData();
		gdLBLCursorType.horizontalIndent = 4;
		lblCursorType.setLayoutData(gdLBLCursorType);
		lblCursorType.setText(Messages.getString("TriggerDataComposite.Lbl.Cursor")); //$NON-NLS-1$

		cmbCursorType = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
		GridData gdCMBCursorType = new GridData(GridData.FILL_HORIZONTAL);
		cmbCursorType.setLayoutData(gdCMBCursorType);
		cmbCursorType.addSelectionListener(this);
		cmbCursorType.setVisibleItemCount(30);

		btnCursorImage = new Button(this, SWT.NONE);
		btnCursorImage.setText(Messages.getString("TriggerDataComposite.Lbl.Image")); //$NON-NLS-1$
		GridData gdBTNCursorImage = new GridData();
		btnCursorImage.setLayoutData(gdBTNCursorImage);
		btnCursorImage.addSelectionListener(this);
		btnCursorImage.setEnabled(false);

		grpValue = new Group(this, SWT.NONE);
		GridData gdGRPValue = new GridData(GridData.FILL_BOTH);
		gdGRPValue.horizontalSpan = 3;
		grpValue.setLayoutData(gdGRPValue);
		grpValue.setText(Messages.getString("TriggerDataComposite.Lbl.ActionDetails")); //$NON-NLS-1$
		grpValue.setLayout(slValues);

		// Composite for default value
		cmpDefault = new Composite(grpValue, SWT.NONE);

		// Composite for callback value
		cmpCallback = new Composite(grpValue, SWT.NONE);
		cmpCallback.setLayout(new GridLayout());

		addDescriptionLabel(cmpCallback, 1, Messages.getString("TriggerDataComposite.Label.CallbackDescription")); //$NON-NLS-1$

		// Composite for highlight value
		cmpHighlight = new Composite(grpValue, SWT.NONE);
		cmpHighlight.setLayout(new GridLayout());

		addDescriptionLabel(cmpHighlight, 1, Messages.getString("TriggerDataComposite.Label.HighlightDescription")); //$NON-NLS-1$

		// Composite for Toogle Visibility value
		cmpVisiblity = new Composite(grpValue, SWT.NONE);
		cmpVisiblity.setLayout(new GridLayout());

		addDescriptionLabel(cmpVisiblity, 1, Messages.getString("TriggerDataComposite.Label.VisiblityDescription")); //$NON-NLS-1$

		// Composite for Toogle DataPoint Visibility value
		cmpDPVisibility = new Composite(grpValue, SWT.NONE);
		cmpDPVisibility.setLayout(new GridLayout());

		addDescriptionLabel(cmpDPVisibility, 1,
				Messages.getString("TriggerDataComposite.Label.DPVisibilityDescription")); //$NON-NLS-1$

		// Composite for script value
		cmpScript = new Composite(grpValue, SWT.NONE);
		cmpScript.setLayout(new GridLayout(2, false));

		Label lblScript = new Label(cmpScript, SWT.NONE);
		{
			GridData gdLBLScript = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
			lblScript.setLayoutData(gdLBLScript);
			lblScript.setText(Messages.getString("TriggerDataComposite.Lbl.Script")); //$NON-NLS-1$
		}
		txtScript = new Text(cmpScript, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		{
			GridData gd = new GridData(GridData.FILL_BOTH);
			gd.verticalSpan = 2;
			txtScript.setLayoutData(gd);
			txtScript.setToolTipText(Messages.getString("TriggerDataComposite.Tooltip.InputScript")); //$NON-NLS-1$
		}

		try {
			btnScriptExpBuilder = (IExpressionButton) wizardContext.getUIServiceProvider().invoke(
					IUIServiceProvider.Command.EXPRESS_BUTTON_CREATE, cmpScript, txtScript,
					wizardContext.getExtendedItem(), getExpressionBuilderScriptCommand(), null);
		} catch (ChartException e) {
			WizardBase.displayException(e);
		}

		// Composite for tooltip value
		cmpTooltip = new Composite(grpValue, SWT.NONE);
		cmpTooltip.setLayout(new GridLayout(3, false));

		// Deprecate delay attribute: #132289
		// Label lblDelay = new Label( cmpTooltip, SWT.NONE );
		// GridData gdLBLDelay = new GridData( );
		// lblDelay.setLayoutData( gdLBLDelay );
		// lblDelay.setText( Messages.getString(
		// "TriggerDataComposite.Lbl.TooltipDelay" ) ); //$NON-NLS-1$
		//
		// iscDelay = new Spinner( cmpTooltip, SWT.BORDER );
		// GridData gdISCDelay = new GridData( GridData.FILL_HORIZONTAL );
		// gdISCDelay.horizontalSpan = 2;
		// iscDelay.setLayoutData( gdISCDelay );
		// iscDelay.setMaximum( 5000 );
		// iscDelay.setMinimum( 100 );
		// iscDelay.setIncrement( 100 );
		// iscDelay.setSelection( 200 );

		Label lblText = new Label(cmpTooltip, SWT.NONE);
		lblText.setText(Messages.getString("TriggerDataComposite.Lbl.TooltipText")); //$NON-NLS-1$

		if (((optionalStyle & ENABLE_SHOW_TOOLTIP_VALUE) == ENABLE_SHOW_TOOLTIP_VALUE)) {
			GridData lblGd = new GridData();
			lblGd.horizontalSpan = 3;
			lblText.setLayoutData(lblGd);

			txtTooltipText = new Text(cmpTooltip, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
			GridData gdTXTTooltipText = new GridData(GridData.FILL_BOTH);
			gdTXTTooltipText.horizontalSpan = 3;
			txtTooltipText.setLayoutData(gdTXTTooltipText);

			// Create the dummy instance to keep consistency
			btnTooltipExpBuilder = new IExpressionButton() {

				@Override
				public void setExpression(String expr) {
					txtTooltipText.setText(expr);
				}

				@Override
				public void setEnabled(boolean bEnabled) {
					txtTooltipText.setEnabled(bEnabled);
				}

				@Override
				public boolean isEnabled() {
					return txtTooltipText.isEnabled();
				}

				@Override
				public String getExpression() {
					return txtTooltipText.getText();
				}

				@Override
				public String getDisplayExpression() {
					return getExpression();
				}

				@Override
				public void addListener(Listener listener) {
					// not implemented
				}

				@Override
				public void setAccessor(EAttributeAccessor<String> accessor) {
					// not implemented
				}

				@Override
				public String getExpressionType() {
					return null;
				}

				@Override
				public boolean isCube() {
					return false;
				}

				@Override
				public void setBindingName(String bindingName, boolean bNotifyEvents) {
					// not implemented
				}

				@Override
				public void setExpression(String expr, boolean bNotifyEvents) {
					// not implemented
				}

				@Override
				public void setAssitField(IAssistField assistField) {
					// not implemented
				}

				@Override
				public void setPredefinedQuery(Object[] predefinedQuery) {
					// not implemented
				}
			};
		} else {
			txtTooltipText = new Text(cmpTooltip, SWT.BORDER | SWT.SINGLE);
			txtTooltipText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

			try {
				btnTooltipExpBuilder = (IExpressionButton) wizardContext.getUIServiceProvider().invoke(
						IUIServiceProvider.Command.EXPRESS_BUTTON_CREATE, cmpTooltip, txtTooltipText,
						wizardContext.getExtendedItem(), getExpressionBuilderTooltipCommand(), null);
			} catch (ChartException e) {
				WizardBase.displayException(e);
			}

			addDescriptionLabel(cmpTooltip, 3,
					Messages.getString("TriggerDataComposite.Label.TooltipUsingDataLabelOfSeries")); //$NON-NLS-1$
		}
		txtTooltipText.addListener(SWT.FocusOut, new Listener() {

			@Override
			public void handleEvent(Event event) {
				updateTrigger(cmbTriggerType.getText());

			}
		});

		if ((optionalStyle & ENABLE_TOOLTIP_FORMATTER) == ENABLE_TOOLTIP_FORMATTER) {
			btnFormat = new Button(cmpTooltip, SWT.PUSH);
			btnFormat.setText(Messages.getString("TriggerDataComposite.Btn.Format")); //$NON-NLS-1$
		}

		// Composite for url value
		createURLComposite(glURL, glParameter);

		multiHyperlinksComposite = new MultipleHyperlinksComposite(grpValue, SWT.NONE, wizardContext, triggerMatrix,
				optionalStyle);

		populateLists();
	}

	/**
	 * @param glURL
	 * @param glParameter
	 */
	private void createURLComposite(GridLayout glURL, GridLayout glParameter) {
		final boolean bEnableURLParameters = ((optionalStyle & ENABLE_URL_PARAMETERS) == ENABLE_URL_PARAMETERS);

		cmpURL = new Composite(grpValue, SWT.NONE);
		cmpURL.setLayout(glURL);

		Label lblBaseURL = new Label(cmpURL, SWT.NONE);
		GridData gdLBLBaseURL = new GridData();
		gdLBLBaseURL.horizontalIndent = 2;
		lblBaseURL.setLayoutData(gdLBLBaseURL);
		lblBaseURL.setText(Messages.getString("TriggerDataComposite.Lbl.BaseURL")); //$NON-NLS-1$

		btnBaseURL = new Button(cmpURL, SWT.NONE);
		{
			GridData gd = new GridData();
			btnBaseURL.setLayoutData(gd);
			btnBaseURL.setText(Messages.getString("TriggerDataComposite.Text.EditBaseURL")); //$NON-NLS-1$
			btnBaseURL.setToolTipText(Messages.getString("TriggerDataComposite.Tooltip.InvokeURLBuilder")); //$NON-NLS-1$
			btnBaseURL.addSelectionListener(this);
			btnBaseURL.setEnabled(wizardContext.getUIServiceProvider().isInvokingSupported());
			// Bugzilla#193463 URI has been supported in standalone mode
			// btnBaseURL.setVisible( wizardContext.getUIServiceProvider( )
			// .isEclipseModeSupported( ) );
		}

		Label lblDefine = new Label(cmpURL, SWT.WRAP);
		{
			GridData gd = new GridData();
			gd.horizontalIndent = 2;
			gd.horizontalSpan = 3;
			gd.widthHint = 200;
			lblDefine.setLayoutData(gd);
			lblDefine.setText(Messages.getString("TriggerDataComposite.Label.Description")); //$NON-NLS-1$
		}

		btnAdvanced = new Button(cmpURL, SWT.NONE);
		{
			GridData gd = new GridData();
			gd.horizontalSpan = 2;
			btnAdvanced.setLayoutData(gd);
			btnAdvanced.setText(getAdvancedButtonText(bAdvanced));
			btnAdvanced.setToolTipText(Messages.getString("TriggerDataComposite.Tooltip.Advanced")); //$NON-NLS-1$
			btnAdvanced.addSelectionListener(this);
			btnAdvanced.setEnabled(bEnableURLParameters);
		}

		// Label lblTarget = new Label( cmpURL, SWT.NONE );
		// GridData gdLBLTarget = new GridData( );
		// gdLBLTarget.horizontalIndent = 2;
		// lblTarget.setLayoutData( gdLBLTarget );
		// lblTarget.setText( Messages.getString(
		// "TriggerDataComposite.Lbl.Target" ) ); //$NON-NLS-1$
		//
		// txtTarget = new Text( cmpURL, SWT.BORDER );
		// GridData gdTXTTarget = new GridData( GridData.FILL_HORIZONTAL );
		// gdTXTTarget.horizontalSpan = 2;
		// txtTarget.setLayoutData( gdTXTTarget );

		grpParameters = new Group(cmpURL, SWT.NONE);
		GridData gdGRPParameters = new GridData(GridData.FILL_HORIZONTAL);
		gdGRPParameters.horizontalSpan = 3;
		grpParameters.setLayoutData(gdGRPParameters);
		grpParameters.setLayout(glParameter);
		grpParameters.setText(Messages.getString("TriggerDataComposite.Lbl.ParameterNames")); //$NON-NLS-1$
		grpParameters.setVisible(bAdvanced);

		StyledText stParameters = new StyledText(grpParameters, SWT.WRAP | SWT.READ_ONLY);
		{
			GridData gd = new GridData();
			gd.horizontalIndent = 2;
			gd.horizontalSpan = 3;
			gd.widthHint = 200;
			stParameters.setLayoutData(gd);
			stParameters.setText(Messages.getString("TriggerDataComposite.Label.OptionalURLParameters")); //$NON-NLS-1$
			StyleRange[] sr = { new StyleRange(0, 4, this.getForeground(), this.getBackground(), SWT.ITALIC),
					new StyleRange(4, stParameters.getText().length() - 4, this.getForeground(), this.getBackground(),
							SWT.NORMAL) };
			stParameters.setStyleRanges(sr);
			stParameters.setBackground(this.getBackground());
		}

		Label lblBaseParm = new Label(grpParameters, SWT.NONE);
		{
			GridData gdLBLBaseParm = new GridData();
			gdLBLBaseParm.horizontalIndent = 2;
			lblBaseParm.setLayoutData(gdLBLBaseParm);
			lblBaseParm.setText(Messages.getString("TriggerDataComposite.Lbl.CategorySeries")); //$NON-NLS-1$
			lblBaseParm.setToolTipText(Messages.getString("TriggerDataComposite.Tooltip.ParameterCategory")); //$NON-NLS-1$
		}

		txtBaseParm = new Text(grpParameters, SWT.BORDER);
		GridData gdTXTBaseParm = new GridData(GridData.FILL_HORIZONTAL);
		gdTXTBaseParm.horizontalSpan = 2;
		txtBaseParm.setLayoutData(gdTXTBaseParm);
		txtBaseParm.setToolTipText(Messages.getString("TriggerDataComposite.Tooltip.ParameterCategory")); //$NON-NLS-1$
		txtBaseParm.setEnabled(
				bEnableURLParameters && ((optionalStyle & DISABLE_CATEGORY_SERIES) != DISABLE_CATEGORY_SERIES));
		Label lblValueParm = new Label(grpParameters, SWT.NONE);
		{
			GridData gdLBLValueParm = new GridData();
			gdLBLValueParm.horizontalIndent = 2;
			lblValueParm.setLayoutData(gdLBLValueParm);
			lblValueParm.setText(Messages.getString("TriggerDataComposite.Lbl.ValueSeries")); //$NON-NLS-1$
			lblValueParm.setToolTipText(Messages.getString("TriggerDataComposite.Tooltip.ParameterValue")); //$NON-NLS-1$
		}

		txtValueParm = new Text(grpParameters, SWT.BORDER);
		GridData gdTXTValueParm = new GridData(GridData.FILL_HORIZONTAL);
		gdTXTValueParm.horizontalSpan = 2;
		txtValueParm.setLayoutData(gdTXTValueParm);
		txtValueParm.setToolTipText(Messages.getString("TriggerDataComposite.Tooltip.ParameterValue")); //$NON-NLS-1$
		txtValueParm
				.setEnabled(bEnableURLParameters && ((optionalStyle & DISABLE_VALUE_SERIES) != DISABLE_VALUE_SERIES));

		Label lblSeriesParm = new Label(grpParameters, SWT.NONE);
		{
			GridData gdLBLSeriesParm = new GridData();
			gdLBLSeriesParm.horizontalIndent = 2;
			lblSeriesParm.setLayoutData(gdLBLSeriesParm);
			lblSeriesParm.setText(Messages.getString("TriggerDataComposite.Lbl.ValueSeriesName")); //$NON-NLS-1$
			lblSeriesParm.setToolTipText(Messages.getString("TriggerDataComposite.Tooltip.ParameterSeries")); //$NON-NLS-1$
		}

		txtSeriesParm = new Text(grpParameters, SWT.BORDER);
		GridData gdTXTSeriesParm = new GridData(GridData.FILL_HORIZONTAL);
		gdTXTSeriesParm.horizontalSpan = 2;
		txtSeriesParm.setLayoutData(gdTXTSeriesParm);
		txtSeriesParm.setToolTipText(Messages.getString("TriggerDataComposite.Tooltip.ParameterSeries")); //$NON-NLS-1$
		txtSeriesParm.setEnabled(
				bEnableURLParameters && ((optionalStyle & DISABLE_VALUE_SERIES_NAME) != DISABLE_VALUE_SERIES_NAME));
	}

	private void populateLists() {
		final String[] triggerTypes;
		if (conditionFilter == null) {
			// All
			triggerTypes = LiteralHelper.triggerConditionSet.getDisplayNames();
		} else {
			// Filtered
			triggerTypes = new String[conditionFilter.length];
			for (int i = 0; i < conditionFilter.length; i++) {
				triggerTypes[i] = LiteralHelper.triggerConditionSet.getDisplayNameByName(conditionFilter[i].getName());
			}
		}
		cmbTriggerType.setItems(triggerTypes);
		if (cmbTriggerType.getItemCount() > 0) {
			cmbTriggerType.select(0);
		}

		Trigger firstTrigger = null;
		for (int i = 0; i < triggerTypes.length; i++) {
			if (triggersMap.containsKey(triggerTypes[i])) {
				Trigger trigger = triggersMap.get(triggerTypes[i]);
				// Only display supported trigger
				if (triggerMatrix.check(trigger)) {
					cmbTriggerType.markSelection(triggerTypes[i]);
					if (firstTrigger == null) {
						firstTrigger = trigger;
						// Select first trigger
						cmbTriggerType.setText(triggerTypes[i]);
					}
				}
			}
		}

		// Initializes the value of last trigger type
		this.lastTriggerType = cmbTriggerType.getText();

		// Updates ActionType combo according to Trigger condition
		updateActionTypeItems();

		if (firstTrigger != null) {
			cmbActionType.setText(
					LiteralHelper.actionTypeSet.getDisplayNameByName(firstTrigger.getAction().getType().getName()));
			updateUI(firstTrigger);
		} else {
			cmbActionType.select(0);
			slValues.topControl = cmpDefault;
		}

		if (btnFormat != null) {
			if (firstTrigger != null && firstTrigger.getAction() != null
					&& firstTrigger.getAction().getType() == ActionType.SHOW_TOOLTIP_LITERAL) {
				TooltipValue tv = (TooltipValue) firstTrigger.getAction().getValue();
				formatSpecifier = tv.getFormatSpecifier();
			}
			addFormatButtonListener();
		}

		// Initializes the cursor type list.
		updateCursorTypeItems();
		updateImageButtonState();
		updateCursorArea();
	}

	/**
	 * Updates Combo items after TriggerCondition change
	 */
	private void updateActionTypeItems() {
		TriggerCondition condition = TriggerCondition
				.getByName(LiteralHelper.triggerConditionSet.getNameByDisplayName(cmbTriggerType.getText()));
		if (condition != null) {
			cmbActionType.setItems(this.triggerMatrix.getSupportedActionsDisplayName(condition));

			// Add extra item for NONE
			// #234902
			cmbActionType.add(Messages.getString("TriggerDataComposite.Lbl.None." + condition.getName()), 0); //$NON-NLS-1$
		}
	}

	private void updateCursorTypeItems() {
		final String[] cursorDisplayNames;
		if (cursorFilter == null) {
			// All
			cursorDisplayNames = LiteralHelper.cursorSet.getDisplayNames();
		} else {
			// Filtered
			cursorDisplayNames = new String[cursorFilter.length];
			for (int i = 0; i < cursorFilter.length; i++) {
				cursorDisplayNames[i] = LiteralHelper.cursorSet.getDisplayNameByName(cursorFilter[i].getName());
			}
		}
		cmbCursorType.setItems(cursorDisplayNames);
		if (cmbCursorType.getItemCount() > 0) {
			Cursor c = getMouseCursor();
			if (c != null && c.getType() != null) {
				cmbCursorType.setText(LiteralHelper.cursorSet.getDisplayNameByName(c.getType().getName()));
				if (cmbCursorType.getSelectionIndex() < 0) {
					cmbCursorType.select(0);
					setMouseCursor(CursorType
							.getByName(LiteralHelper.cursorSet.getNameByDisplayName(cmbCursorType.getText())));
				}
			} else if (cmbCursorType.getSelectionIndex() < 0) {
				cmbCursorType.select(0);
			}
		}
	}

	/**
	 * Provides a mapper method to switch trigger Combo text to fixed index
	 * constants.
	 *
	 * @return fixed index defined as constants
	 */
	private int getTriggerIndex() {
		// Order by usage frequency
		if (cmbActionType.getText()
				.equals(LiteralHelper.actionTypeSet.getDisplayNameByName(ActionType.SHOW_TOOLTIP_LITERAL.getName()))) {
			return INDEX_2_TOOLTIP;
		}
		if (cmbActionType.getText()
				.equals(LiteralHelper.actionTypeSet.getDisplayNameByName(ActionType.URL_REDIRECT_LITERAL.getName()))) {
			return INDEX_1_URL_REDIRECT;
		}
		if (cmbActionType.getText()
				.equals(LiteralHelper.actionTypeSet.getDisplayNameByName(ActionType.INVOKE_SCRIPT_LITERAL.getName()))) {
			return INDEX_4_SCRIPT;
		}
		if (cmbActionType.getText()
				.equals(LiteralHelper.actionTypeSet.getDisplayNameByName(ActionType.HIGHLIGHT_LITERAL.getName()))) {
			return INDEX_5_HIGHLIGHT;
		}
		if (cmbActionType.getText().equals(
				LiteralHelper.actionTypeSet.getDisplayNameByName(ActionType.TOGGLE_VISIBILITY_LITERAL.getName()))) {
			return INDEX_3_TOOGLE_VISABILITY;
		}
		if (cmbActionType.getText().equals(LiteralHelper.actionTypeSet
				.getDisplayNameByName(ActionType.TOGGLE_DATA_POINT_VISIBILITY_LITERAL.getName()))) {
			return INDEX_7_TOOGLE_DATAPOINT;
		}
		if (cmbActionType.getText()
				.equals(LiteralHelper.actionTypeSet.getDisplayNameByName(ActionType.CALL_BACK_LITERAL.getName()))) {
			return INDEX_6_CALLBACK;
		}
		return 0;
	}

	private Label addDescriptionLabel(Composite parent, int horizontalSpan, String description) {
		Label label = new Label(parent, SWT.WRAP);
		{
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.widthHint = 200;
			gd.horizontalSpan = horizontalSpan;
			label.setLayoutData(gd);
			label.setText(description);
		}
		return label;
	}

	/**
	 * Marks UI will save the trigger when closing
	 *
	 */
	public void markSaveWhenClosing() {
		this.needSaveWhenDisposing = true;
	}

	public void setTrigger(Trigger trigger) {
		if (trigger == null) {
			clear();
			return;
		}
		cmbTriggerType
				.setText(LiteralHelper.triggerConditionSet.getDisplayNameByName(trigger.getCondition().getName()));
		updateActionTypeItems();

		if (triggerMatrix.check(trigger)) {
			cmbActionType
					.setText(LiteralHelper.actionTypeSet.getDisplayNameByName(trigger.getAction().getType().getName()));
		} else {
			cmbActionType.select(0);
		}
		updateUI(trigger);
	}

	private void initUI() {
		final String BLANK_STRING = ""; //$NON-NLS-1$
		// case INDEX_1_URL_REDIRECT :
		txtBaseParm.setText(BLANK_STRING);
		txtValueParm.setText(BLANK_STRING);
		txtSeriesParm.setText(BLANK_STRING);
		// case INDEX_2_TOOLTIP :
		txtTooltipText.setText(BLANK_STRING);
		// case INDEX_4_SCRIPT :
		txtScript.setText(BLANK_STRING);
		formatSpecifier = null;
	}

	/**
	 * Update UI values according to specified trigger model values.
	 *
	 * @param trigger
	 */
	private void updateUI(Trigger trigger) {
		if (trigger == null) {
			initUI();
			return;
		}

		switch (getTriggerIndex()) {
		case INDEX_1_URL_REDIRECT:
			this.slValues.topControl = multiHyperlinksComposite;
			ActionValue value = trigger.getAction().getValue();
			if (value instanceof MultiURLValues) {
				MultiURLValues urlValues = (MultiURLValues) trigger.getAction().getValue();
				multiHyperlinksComposite.populateUIValues(urlValues);
			} else if (value instanceof URLValue) {
				ChartAdapter.beginIgnoreNotifications();

				MultiURLValues muv = MultiURLValuesImpl.create();
				URLValue uv = (URLValue) value;
				org.eclipse.birt.chart.model.component.Label l = LabelImpl.create();
				l.setCaption(TextImpl.create(Messages.getString("TriggerDataComposite.TemporaryName.Hyperlink"))); //$NON-NLS-1$
				uv.setLabel(l);
				muv.getURLValues().add(uv);
				muv.setTooltip(uv.getTooltip());
				muv.eAdapters().addAll(value.eAdapters());
				trigger.getAction().setValue(muv);
				multiHyperlinksComposite.populateUIValues(muv);

				ChartAdapter.endIgnoreNotifications();
			} else {
				// Null case
				ChartAdapter.beginIgnoreNotifications();
				MultiURLValues urlValues = MultiURLValuesImpl.create();
				trigger.getAction().setValue(urlValues);
				urlValues.eAdapters().addAll(trigger.eAdapters());
				multiHyperlinksComposite.populateUIValues(urlValues);
				ChartAdapter.endIgnoreNotifications();

//					this.slValues.topControl = cmpURL;
//					URLValue urlValue = (URLValue) trigger.getAction( )
//							.getValue( );
//					sBaseURL = urlValue.getBaseUrl( );
//					// txtBaseURL.setText( sBaseURL );
//					// txtTarget.setText( ( urlValue.getTarget( ).length( ) > 0
//					// )
//					// ? urlValue.getTarget( ) : "" ); //$NON-NLS-1$
//					txtBaseParm.setText( ( urlValue.getBaseParameterName( )
//							.length( ) > 0 ) ? urlValue.getBaseParameterName( )
//							: "" ); //$NON-NLS-1$
//					txtValueParm.setText( ( urlValue.getValueParameterName( )
//							.length( ) > 0 ) ? urlValue.getValueParameterName( )
//							: "" ); //$NON-NLS-1$
//					txtSeriesParm.setText( ( urlValue.getSeriesParameterName( )
//							.length( ) > 0 ) ? urlValue.getSeriesParameterName( )
//							: "" ); //$NON-NLS-1$
			}
			break;
		case INDEX_2_TOOLTIP:
			this.slValues.topControl = cmpTooltip;
			TooltipValue tooltipValue = (TooltipValue) trigger.getAction().getValue();
			// iscDelay.setSelection( tooltipValue.getDelay( ) );
			btnTooltipExpBuilder.setExpression(tooltipValue.getText());
			formatSpecifier = tooltipValue.getFormatSpecifier();
			break;
		case INDEX_3_TOOGLE_VISABILITY:
			this.slValues.topControl = cmpVisiblity;
			break;
		case INDEX_4_SCRIPT:
			this.slValues.topControl = cmpScript;
			ScriptValue scriptValue = (ScriptValue) trigger.getAction().getValue();
			btnScriptExpBuilder.setExpression(scriptValue.getScript());
			break;
		case INDEX_5_HIGHLIGHT:
			this.slValues.topControl = cmpHighlight;
			// SeriesValue highlightSeriesValue = (SeriesValue)
			// trigger.getAction( )
			// .getValue( );
			// txtSeriesDefinition.setText( ( highlightSeriesValue.getName(
			// )
			// .length( ) > 0 ) ? highlightSeriesValue.getName( ) : "" );
			// //$NON-NLS-1$
			break;
		case INDEX_6_CALLBACK:
			this.slValues.topControl = cmpCallback;
			break;
		case INDEX_7_TOOGLE_DATAPOINT:
			this.slValues.topControl = cmpDPVisibility;
			break;
		default:
			this.slValues.topControl = cmpDefault;
			break;
		}
		grpValue.layout();
	}

	/**
	 * Returns the trigger instance according to current UI values.
	 *
	 * @return trigger from UI values
	 */
	public Trigger getTrigger() {
		if (cmbActionType.getSelectionIndex() == 0) {
			return null;
		}
		ActionValue value = null;
		switch (getTriggerIndex()) {
		case INDEX_1_URL_REDIRECT:
			// Must copy here to avoid chain set
			MultiURLValues muv = multiHyperlinksComposite.getURLValues();
			value = (muv != null) ? muv.copyInstance() : muv;
			break;
		case INDEX_2_TOOLTIP:
			value = TooltipValueImpl.create(200, ""); //$NON-NLS-1$
			((TooltipValue) value).setText(btnTooltipExpBuilder.getExpression());
			((TooltipValue) value).setFormatSpecifier(formatSpecifier);
			break;
		case INDEX_3_TOOGLE_VISABILITY:
			value = AttributeFactory.eINSTANCE.createSeriesValue();
			((SeriesValue) value).setName(""); //$NON-NLS-1$
			break;
		case INDEX_4_SCRIPT:
			value = AttributeFactory.eINSTANCE.createScriptValue();
			((ScriptValue) value).setScript(btnScriptExpBuilder.getExpression());
			break;
		case INDEX_5_HIGHLIGHT:
			value = AttributeFactory.eINSTANCE.createSeriesValue();
			((SeriesValue) value).setName(""); //$NON-NLS-1$
			break;
		case INDEX_7_TOOGLE_DATAPOINT:
			value = AttributeFactory.eINSTANCE.createSeriesValue();
			((SeriesValue) value).setName(""); //$NON-NLS-1$
		default:
			break;
		}
		Action action = ActionImpl.create(
				ActionType.getByName(LiteralHelper.actionTypeSet.getNameByDisplayName(cmbActionType.getText())), value);
		return TriggerImpl.create(
				TriggerCondition.getByName(LiteralHelper.triggerConditionSet
						.getNameByDisplayName(lastTriggerType == null ? cmbTriggerType.getText() : lastTriggerType)),
				action);
	}

	public void clear() {
		if (cmbTriggerType.getItemCount() > 0) {
			cmbTriggerType.select(0);
		}
		if (cmbActionType.getItemCount() > 0) {
			cmbActionType.select(0);
		}
		if (cmbCursorType.getItemCount() > 0) {
			cmbCursorType.select(0);
		}
		switchUI();
	}

	/**
	 * Update UI components and values for according to selected trigger type.
	 */
	private void switchUI() {
		switch (getTriggerIndex()) {
		case INDEX_1_URL_REDIRECT:
			Trigger trigger = triggersMap.get(cmbTriggerType.getText());
			if (trigger == null || !(trigger.getAction().getValue() instanceof MultiURLValues)) {
				multiHyperlinksComposite.populateUIValues(MultiURLValuesImpl.create());
			} else {
				multiHyperlinksComposite.populateUIValues((MultiURLValues) trigger.getAction().getValue());
			}
			this.slValues.topControl = multiHyperlinksComposite;
			break;
		case INDEX_2_TOOLTIP:
			this.slValues.topControl = cmpTooltip;
			break;
		case INDEX_3_TOOGLE_VISABILITY:
			this.slValues.topControl = cmpVisiblity;
			break;
		case INDEX_4_SCRIPT:
			this.slValues.topControl = cmpScript;
			break;
		case INDEX_5_HIGHLIGHT:
			this.slValues.topControl = cmpHighlight;
			break;
		case INDEX_6_CALLBACK:
			this.slValues.topControl = cmpCallback;
			break;
		case INDEX_7_TOOGLE_DATAPOINT:
			this.slValues.topControl = cmpDPVisibility;
			break;
		default:
			this.slValues.topControl = cmpDefault;
			break;
		}
		grpValue.layout();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt
	 * .events.SelectionEvent)
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {
		if (e.getSource().equals(cmbActionType)) {
			String triggerType = cmbTriggerType.getText();
			if (cmbActionType.getSelectionIndex() == 0) {
				cmbTriggerType.unmarkSelection(triggerType);
				Object trigger = triggersMap.get(triggerType);
				if (trigger != null) {
					triggersMap.remove(triggerType);
					triggersList.remove(trigger);
				}
			} else {
				cmbTriggerType.markSelection(triggerType);
				// #48981 update the trigger when trigger isn't null.
				Object trigger = triggersMap.get(triggerType);
				if (trigger == null) {
					updateTrigger(triggerType);
				}
			}
			cmbTriggerType.setText(triggerType);
			switchUI();
		} else if (e.getSource().equals(btnBaseURL)) {
			try {
				if (wizardContext != null) {
					sBaseURL = wizardContext.getUIServiceProvider().invoke(getHyperlinkBuilderCommand(), sBaseURL,
							wizardContext.getExtendedItem(), null);
				}
			} catch (ChartException ex) {
				WizardBase.displayException(ex);
			}
		} else if (e.getSource().equals(btnAdvanced)) {
			bAdvanced = !bAdvanced;
			btnAdvanced.setText(getAdvancedButtonText(bAdvanced));
			grpParameters.setVisible(bAdvanced);
			this.slValues.topControl = cmpURL;
			grpValue.layout(true, true);
		} else if (e.getSource() == cmbCursorType) {
			setMouseCursor(CursorType.getByName(LiteralHelper.cursorSet.getNameByDisplayName(cmbCursorType.getText())));
			updateImageButtonState();
		} else if (e.getSource() == btnCursorImage) {
			Cursor c = getMouseCursor();

			CursorImageDialog idlg = new CursorImageDialog(this.getShell(), c);
			idlg.open();
		}

		updateCursorArea();
	}

	private void updateImageButtonState() {
		Cursor c = getMouseCursor();
		if (c != null && c.getType() == CursorType.CUSTOM) {
			btnCursorImage.setEnabled(true);
			return;
		}
		btnCursorImage.setEnabled(false);
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

	private String getActionText(Trigger trigger) {
		return LiteralHelper.actionTypeSet.getDisplayNameByName(trigger.getAction().getType().getName());
	}

	/**
	 * Get modified trigger object instead of old.
	 *
	 * @param triggerType
	 */
	private void updateTrigger(String triggerType) {
		if (triggerType == null || triggerType.length() == 0) {
			return;
		}

		if (cmbActionType.getSelectionIndex() <= 0) {
			cmbTriggerType.unmarkSelection(triggerType);
			return;
		}
		cmbTriggerType.markSelection(triggerType);

		Trigger oldTrigger = triggersMap.get(triggerType);
		Trigger newTrigger = getTrigger();
		if (oldTrigger != null) {
			int index = triggersList.indexOf(oldTrigger);
			if (index >= 0) {
				triggersList.set(index, newTrigger);
			}
		} else {
			triggersList.add(newTrigger);
		}
		triggersMap.put(triggerType, newTrigger);

		updateCursorArea();
	}

	private String getAdvancedButtonText(boolean bAdvanced) {
		if (bAdvanced) {
			return Messages.getString("TriggerDataComposite.Text.OpenAdvanced"); //$NON-NLS-1$
		}
		return Messages.getString("TriggerDataComposite.Text.Advanced"); //$NON-NLS-1$

	}

	private int getExpressionBuilderScriptCommand() {
		int type = this.triggerMatrix.getType();
		if ((type & TriggerSupportMatrix.TYPE_DATAPOINT) == TriggerSupportMatrix.TYPE_DATAPOINT) {
			return IUIServiceProvider.COMMAND_EXPRESSION_SCRIPT_DATAPOINTS;
		}
		return IUIServiceProvider.COMMAND_EXPRESSION_TRIGGERS_SIMPLE;
	}

	private int getExpressionBuilderTooltipCommand() {
		// Bugzilla#202386: Tooltips never support chart variables.
		int type = this.triggerMatrix.getType();
		if ((type & TriggerSupportMatrix.TYPE_DATAPOINT) == TriggerSupportMatrix.TYPE_DATAPOINT) {
			boolean useCube = wizardContext.getDataServiceProvider().checkState(IDataServiceProvider.HAS_CUBE)
					|| wizardContext.getDataServiceProvider().checkState(IDataServiceProvider.SHARE_CROSSTAB_QUERY);
			if (useCube) {
				return IUIServiceProvider.COMMAND_CUBE_EXPRESSION_TOOLTIPS_DATAPOINTS;
			}
			return IUIServiceProvider.COMMAND_EXPRESSION_TOOLTIPS_DATAPOINTS;
		}
		return IUIServiceProvider.COMMAND_EXPRESSION_TRIGGERS_SIMPLE;
	}

	private int getHyperlinkBuilderCommand() {
		int type = this.triggerMatrix.getType();
		if ((type & TriggerSupportMatrix.TYPE_DATAPOINT) == TriggerSupportMatrix.TYPE_DATAPOINT) {
			boolean useCube = wizardContext.getDataServiceProvider().checkState(IDataServiceProvider.HAS_CUBE)
					|| wizardContext.getDataServiceProvider().checkState(IDataServiceProvider.SHARE_CROSSTAB_QUERY);
			if (useCube) {
				// Remove column bindings in data cube case
				return IUIServiceProvider.COMMAND_HYPERLINK_DATAPOINTS_SIMPLE;
			}
			return IUIServiceProvider.COMMAND_HYPERLINK_DATAPOINTS;
		}
		if ((type & TriggerSupportMatrix.TYPE_LEGEND) == TriggerSupportMatrix.TYPE_LEGEND) {
			return IUIServiceProvider.COMMAND_HYPERLINK_LEGEND;
		}
		return IUIServiceProvider.COMMAND_HYPERLINK;
	}

	private Cursor getMouseCursor() {
		EStructuralFeature esf = cursorContainer.eClass().getEStructuralFeature("cursor"); //$NON-NLS-1$
		return (Cursor) cursorContainer.eGet(esf);
	}

	private void setMouseCursor(CursorType type) {
		Cursor c = getMouseCursor();
		if (c == null) {
			EStructuralFeature esf = cursorContainer.eClass().getEStructuralFeature("cursor"); //$NON-NLS-1$
			c = AttributeFactory.eINSTANCE.createCursor();
			cursorContainer.eSet(esf, c);
			c.eAdapters().addAll(cursorContainer.eAdapters());
		}

		c.setType(type);
	}

	private void updateCursorArea() {
		boolean enableCursor = (this.triggersList.size() > 0) || (cmbActionType.getSelectionIndex() > 0);
		cmbCursorType.setEnabled(enableCursor);
		if (!enableCursor) {
			cmbCursorType.select(0);
			setMouseCursor(null);
		}
		btnCursorImage.setEnabled(btnCursorImage.isEnabled() && enableCursor);
	}
}
