/***********************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt.composites;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.chart.aggregate.IAggregateFunction;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.attribute.GroupingUnitType;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.SeriesGrouping;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.chart.ui.swt.type.StockChart;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.chart.util.NameSet;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * @author Actuate Corporation
 * 
 */
public class SeriesGroupingComposite extends Composite implements SelectionListener {

	private transient Group grpContent = null;

	protected transient Button btnEnabled = null;

	private transient Label lblType = null;

	private transient Combo cmbType = null;

	private transient Label lblUnit = null;

	private transient Combo cmbUnit = null;

	private transient Label lblInterval = null;

	private transient Text iscInterval = null;

	private transient Label lblAggregate = null;

	private transient Combo cmbAggregate = null;

	private final boolean fbAggEnabled;

	private Composite fCmpAggregate;

	private List<Text> fAggParamtersTextWidgets = new ArrayList<Text>();

	private Map<Button, Text> fExprBuilderWidgetsMap = new HashMap<Button, Text>();

	private Composite fAggParameterComposite;

	private ChartWizardContext fChartContext;

	private String fTitle = null;

	protected SeriesGrouping fGrouping;

	/**
	 * @param parent
	 * @param style
	 */
	public SeriesGroupingComposite(Composite parent, int style, SeriesGrouping grouping, ChartWizardContext context) {
		this(parent, style, grouping, true, context, null);
	}

	/**
	 * @param parent
	 * @param style
	 */
	public SeriesGroupingComposite(Composite parent, int style, SeriesGrouping grouping, boolean bAggEnabled,
			ChartWizardContext context, String title) {
		super(parent, style);
		fGrouping = grouping;
		this.fbAggEnabled = bAggEnabled;
		fChartContext = context;
		init();
		placeComponents();

		// Init data of UI and widgets status.
		initDataNWidgetsStatus();

		fTitle = (title == null || title.length() == 0)
				? Messages.getString("AggregateEditorComposite.AggregateParameterDefinition.Title") //$NON-NLS-1$
				: title;
	}

	private void init() {
		this.setSize(getParent().getClientArea().width, getParent().getClientArea().height);
	}

	private void placeComponents() {
		// Layout for content composite
		GridLayout glContent = new GridLayout();
		glContent.numColumns = 4;
		glContent.horizontalSpacing = 5;
		glContent.verticalSpacing = 5;
		glContent.marginWidth = 7;
		glContent.marginHeight = 7;

		this.setLayout(new FillLayout());

		// Content composite
		grpContent = new Group(this, SWT.NONE);
		grpContent.setLayout(glContent);
		grpContent.setText(Messages.getString("SeriesGroupingComposite.Lbl.Grouping")); //$NON-NLS-1$

		btnEnabled = new Button(grpContent, SWT.CHECK);
		GridData gdBTNEnabled = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gdBTNEnabled.horizontalSpan = 4;
		btnEnabled.setLayoutData(gdBTNEnabled);
		btnEnabled.setText(Messages.getString("SeriesGroupingComposite.Lbl.Enabled")); //$NON-NLS-1$
		btnEnabled.addSelectionListener(this);

		lblType = new Label(grpContent, SWT.NONE);
		GridData gdLBLType = new GridData();
		lblType.setLayoutData(gdLBLType);
		lblType.setText(Messages.getString("SeriesGroupingComposite.Lbl.Type")); //$NON-NLS-1$

		cmbType = new Combo(grpContent, SWT.DROP_DOWN | SWT.READ_ONLY);
		GridData gdCMBType = new GridData(GridData.FILL_HORIZONTAL);
		cmbType.setLayoutData(gdCMBType);
		cmbType.addSelectionListener(this);

		lblUnit = new Label(grpContent, SWT.NONE);
		GridData gdLBLUnit = new GridData();
		lblUnit.setLayoutData(gdLBLUnit);
		lblUnit.setText(Messages.getString("SeriesGroupingComposite.Lbl.Unit")); //$NON-NLS-1$

		cmbUnit = new Combo(grpContent, SWT.DROP_DOWN | SWT.READ_ONLY);
		GridData gdCMBUnit = new GridData(GridData.FILL_HORIZONTAL);
		cmbUnit.setLayoutData(gdCMBUnit);
		cmbUnit.addSelectionListener(this);

		lblInterval = new Label(grpContent, SWT.NONE);
		GridData gdLBLInterval = new GridData();
		lblInterval.setLayoutData(gdLBLInterval);
		lblInterval.setText(Messages.getString("SeriesGroupingComposite.Lbl.Interval")); //$NON-NLS-1$

		iscInterval = new Text(grpContent, SWT.BORDER);
		GridData gdISCInterval = new GridData(GridData.FILL_HORIZONTAL);
		iscInterval.setLayoutData(gdISCInterval);
		iscInterval.setToolTipText(Messages.getString("SeriesGroupingComposite.Tooltip.SelectIntervalForGrouping")); //$NON-NLS-1$
		iscInterval.addSelectionListener(this);
		iscInterval.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub

			}

			public void focusLost(FocusEvent e) {
				String text = iscInterval.getText();
				if (text == null || text.trim().length() == 0) {
					text = "0"; //$NON-NLS-1$
				}
				fGrouping.setGroupingInterval(Double.valueOf(text).doubleValue());
			}

		});
		iscInterval.addVerifyListener(new VerifyListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.VerifyListener#verifyText(org.eclipse.swt.events.
			 * VerifyEvent)
			 */
			public void verifyText(VerifyEvent e) {
				// Check if current format of text is correct, only allow "9999.99" format.
				String text = ((Text) e.getSource()).getText();
				if (e.text != null && e.text.length() > 0) {
					StringBuffer sb = new StringBuffer();
					sb.append(text.substring(0, e.start));
					sb.append(e.text);
					sb.append(text.substring(e.start));
					text = sb.toString();
				}
				if (text != null && text.length() > 0 && !text.matches("[0-9]*[.]?[0-9]*")) { //$NON-NLS-1$
					e.doit = false;
				}
			}

		});

		Label lblDummy = new Label(grpContent, SWT.NONE);
		GridData gdLBLDummy = new GridData(GridData.FILL_HORIZONTAL);
		gdLBLDummy.horizontalSpan = 2;
		lblDummy.setLayoutData(gdLBLDummy);

		// Layout for aggregate composite
		GridLayout glAggregate = new GridLayout();
		glAggregate.numColumns = 2;
		glAggregate.marginHeight = 0;
		glAggregate.marginWidth = 0;
		glAggregate.horizontalSpacing = 5;
		glAggregate.verticalSpacing = 5;

		if (fbAggEnabled) {
			fCmpAggregate = new Composite(grpContent, SWT.NONE);
			GridData gdCMPAggregate = new GridData(GridData.FILL_HORIZONTAL);
			gdCMPAggregate.horizontalSpan = 2;
			fCmpAggregate.setLayoutData(gdCMPAggregate);
			fCmpAggregate.setLayout(glAggregate);

			lblAggregate = new Label(fCmpAggregate, SWT.NONE);
			GridData gdLBLAggregate = new GridData();
			lblAggregate.setLayoutData(gdLBLAggregate);
			lblAggregate.setText(Messages.getString("SeriesGroupingComposite.Lbl.AggregateExpression")); //$NON-NLS-1$

			cmbAggregate = new Combo(fCmpAggregate, SWT.DROP_DOWN | SWT.READ_ONLY);
			cmbAggregate.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			cmbAggregate.addSelectionListener(this);
			cmbAggregate.setVisibleItemCount(30);

			// Use series queries size to check if UI is needed
			Series series = fChartContext.getChartType().getSeries();
			if (ChartUIUtil.getSeriesUIProvider(series).validationIndex(series).length > 1) {
				lblAggregate.setVisible(false);
				cmbAggregate.setVisible(false);
			}
		}

		if (fbAggEnabled) {
			fAggParameterComposite = new Composite(fCmpAggregate, SWT.NONE);
			GridData gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
			gridData.horizontalIndent = 0;
			gridData.horizontalSpan = 2;
			gridData.exclude = true;
			fAggParameterComposite.setLayoutData(gridData);
			GridLayout layout = new GridLayout();
			// layout.horizontalSpacing = layout.verticalSpacing = 0;
			layout.marginWidth = layout.marginHeight = 0;
			layout.numColumns = 3;
			fAggParameterComposite.setLayout(layout);
		}
	}

	/**
	 * Initialize widgets data and status.
	 * 
	 * @since 2.3
	 */
	private void initDataNWidgetsStatus() {
		setButtonsStatus();
		double iGroupInterval = 1;
		if (fGrouping != null) {
			iGroupInterval = fGrouping.getGroupingInterval();
		}
		if (iGroupInterval - (long) iGroupInterval == 0) {
			iscInterval.setText(String.valueOf((long) iGroupInterval));
		} else {
			iscInterval.setText(String.valueOf(iGroupInterval));
		}
		populateLists();
		if (fbAggEnabled) {
			// Display aggregate parameters.
			String aggFuncName = ((String[]) cmbAggregate.getData())[cmbAggregate.getSelectionIndex()];
			showAggregateParameters(aggFuncName);
		}
	}

	/**
	 * Set all buttons status.
	 * 
	 * @since 2.3
	 */
	protected void setButtonsStatus() {
		setGroupingButtonSelection();

		boolean bEnableUI = btnEnabled.getSelection();
		setIntervalButtonsStatus(bEnableUI);
	}

	/**
	 * Set all interval buttons status.
	 * 
	 * @param bEnableUI
	 * @since 2.3
	 */
	protected void setIntervalButtonsStatus(boolean bEnableUI) {
		this.lblType.setEnabled(bEnableUI);
		this.cmbType.setEnabled(bEnableUI);

		this.lblInterval.setEnabled(bEnableUI);
		this.iscInterval.setEnabled(bEnableUI);

		if (fbAggEnabled) {
			this.lblAggregate.setEnabled(bEnableUI);
			this.cmbAggregate.setEnabled(bEnableUI);
		}
	}

	/**
	 * Set selection status of grouping enabled button.
	 * 
	 * @since 2.3
	 */
	protected void setGroupingButtonSelection() {
		btnEnabled.setSelection(fGrouping.isEnabled());
	}

	/**
	 * Set the enabled status of grouping enabled button.
	 * 
	 * @param enabled
	 * @since 2.3
	 */
	protected void setGroupingButtionEnabled(boolean enabled) {
		btnEnabled.setEnabled(enabled);
	}

	/**
	 * Populate grouping property items by data type.
	 */
	private void populateLists() {
		cmbUnit.removeAll();

		boolean isGroupingEnableUI = btnEnabled.getSelection();

		// Populate the data type combo
		NameSet ns = LiteralHelper.dataTypeSet;
		cmbType.setItems(ns.getDisplayNames());

		if (isGroupingEnableUI && fGrouping != null) {
			cmbType.setText(ns.getDisplayNameByName(fGrouping.getGroupType().getName()));
		} else {
			cmbType.select(0);
		}

		// Populate grouping unit combo (applicable only if type is DateTime
		resetGroupingUnitsCombo(isGroupingEnableUI);

		// Populate grouping aggregate expression combo
		if (fbAggEnabled) {

			try {
				cmbAggregate.setItems(PluginSettings.instance().getRegisteredAggregateFunctionDisplayNames());
				cmbAggregate.setData(PluginSettings.instance().getRegisteredAggregateFunctions());
			} catch (ChartException e) {
				WizardBase.displayException(e);
			}

			if (isGroupingEnableUI && fGrouping.getAggregateExpression() != null) {
				int idx = getAggregateIndexByName(fGrouping.getAggregateExpression());
				if (cmbAggregate.getItemCount() > idx) {
					cmbAggregate.select(idx);
				}
			} else if (cmbAggregate.getItemCount() > 0) {
				cmbAggregate.select(0);
			}

			lblAggregate.setEnabled(isGroupingEnableUI);
			cmbAggregate.setEnabled(isGroupingEnableUI);
		}
	}

	/**
	 * Populate aggregate parameters widgets.
	 * 
	 * @since 2.3
	 */
	private void populateAggParameters() {
		if (fbAggEnabled) {
			EList<String> aggPars = fGrouping.getAggregateParameters();
			if (aggPars.size() > 0) {
				int size = aggPars.size() > fAggParamtersTextWidgets.size() ? fAggParamtersTextWidgets.size()
						: aggPars.size();
				for (int i = 0; i < size; i++) {
					String value = aggPars.get(i);
					if (value != null) {
						fAggParamtersTextWidgets.get(i).setText(value);
					}
				}
			}
		}
	}

	/**
	 * Reset grouping units items.
	 * 
	 * @param grouping
	 * @param isGroupingEnableUI
	 * @since BIRT 2.3
	 */
	private void resetGroupingUnitsCombo(boolean isGroupingEnableUI) {
		NameSet ns;
		ns = LiteralHelper.getGroupingUnitTypeSet(fGrouping.getGroupType());
		if (ns != null) {
			cmbUnit.setItems(ns.getDisplayNames());
			if (isGroupingEnableUI && fGrouping.getGroupType() != null
					&& (fGrouping.getGroupType() == DataType.DATE_TIME_LITERAL
							|| fGrouping.getGroupType() == DataType.TEXT_LITERAL)
					&& fGrouping.getGroupingUnit() != null) {
				String name = ChartUtil.getGroupingUnitName(fGrouping);
				if (name != null) {
					// When switch between grouping data types, the returned
					// display name might be null.
					String displayName = ns.getDisplayNameByName(name);
					if (displayName == null) {
						cmbUnit.select(0);
					} else {
						cmbUnit.setText(displayName);
					}
				} else {
					cmbUnit.select(0);
				}
			}
		} else {
			cmbUnit.removeAll();
		}

		lblUnit.setEnabled(
				isGroupingEnableUI && (isDateTimeGrouping(cmbType.getText()) || isTextGrouping(cmbType.getText())));
		cmbUnit.setEnabled(lblUnit.getEnabled());
	}

	/**
	 * Check if specified datat ype name is Text grouping type.
	 * 
	 * @param dataTypeName
	 * @return
	 * @since BIRT 2.3
	 */
	private boolean isTextGrouping(String dataTypeName) {
		return DataType.TEXT_LITERAL.getName().equals(LiteralHelper.dataTypeSet.getNameByDisplayName(dataTypeName));
	}

	/**
	 * Check if specified data type name is DateTime grouping type.
	 * 
	 * @param dataTypeName
	 * @return
	 * @since BIRT 2.3
	 */
	private boolean isDateTimeGrouping(String dataTypeName) {
		return DataType.DATE_TIME_LITERAL.getName()
				.equals(LiteralHelper.dataTypeSet.getNameByDisplayName(dataTypeName));
	}

	private int getAggregateIndexByName(String name) {
		if (fbAggEnabled) {
			String[] names = (String[]) cmbAggregate.getData();

			for (int i = 0; i < names.length; i++) {
				if (name.equals(names[i])) {
					return i;
				}
			}
		}
		return 0;
	}

//	private SeriesGrouping getGrouping( )
//	{
//		return fGrouping;
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.
	 * events.SelectionEvent)
	 */
	public void widgetSelected(SelectionEvent e) {
		Object oSource = e.getSource();
		if (oSource.equals(cmbType)) {
			fGrouping.setGroupType(
					DataType.getByName(LiteralHelper.dataTypeSet.getNameByDisplayName(cmbType.getText())));

			boolean bEnableUI = btnEnabled.getSelection();
			String selName = cmbType.getText();
			boolean bEnabled = isDateTimeGrouping(selName) || isTextGrouping(selName);
			resetGroupingUnitsCombo(bEnableUI);

			lblUnit.setEnabled(bEnableUI & bEnabled);
			cmbUnit.setEnabled(bEnableUI & bEnabled);
			lblInterval.setEnabled(bEnableUI);
			iscInterval.setEnabled(bEnableUI);

			if (fbAggEnabled) {
				lblAggregate.setEnabled(bEnableUI);
				cmbAggregate.setEnabled(bEnableUI);

				if (fChartContext.getChartType() instanceof StockChart
						&& fGrouping.getGroupType().getValue() == DataType.DATE_TIME) {
					ChartUIUtil.updateDefaultAggregations(fChartContext.getModel());
				}
			}
		} else if (oSource.equals(cmbUnit)) {
			fGrouping.setGroupingUnit(GroupingUnitType
					.getByName(LiteralHelper.groupingUnitTypeSet.getNameByDisplayName(cmbUnit.getText())));
		} else if (oSource.equals(cmbAggregate)) {
			int idx = cmbAggregate.getSelectionIndex();
			String aggExpr = null;
			if (idx >= 0) {
				String[] names = (String[]) cmbAggregate.getData();
				aggExpr = names[idx];
			}
			showAggregateParameters(aggExpr);
			getShell().pack();
			fGrouping.setAggregateExpression(aggExpr);
		} else if (oSource.equals(btnEnabled)) {
			fGrouping.setEnabled(btnEnabled.getSelection());

			// refresh UI
			setButtonsStatus();
			populateLists();
			populateAggParameters();

			String aggFuncName = null;
			try {
				aggFuncName = ((String[]) cmbAggregate.getData())[cmbAggregate.getSelectionIndex()];
			} catch (Exception e1) {
				;
			}

			showAggregateParameters(aggFuncName);
			getShell().pack();
		} else if (oSource.equals(iscInterval)) {
			fGrouping.setGroupingInterval(Double.valueOf(iscInterval.getText()).doubleValue());
		} else if (isAggParametersWidget(oSource)) {
			setAggParameter((Text) oSource);
		} else if (isBuilderBtnWidget(oSource)) {
			try {
				Text txtArg = fExprBuilderWidgetsMap.get(oSource);
				String sExpr = fChartContext.getUIServiceProvider().invoke(
						IUIServiceProvider.COMMAND_EXPRESSION_DATA_BINDINGS, txtArg.getText(),
						fChartContext.getExtendedItem(), fTitle);
				txtArg.setText(sExpr);
				setAggParameter(txtArg);
			} catch (ChartException e1) {
				WizardBase.displayException(e1);
			}
		}
	}

	private boolean isBuilderBtnWidget(Object source) {
		return fExprBuilderWidgetsMap.containsKey(source);
	}

	private void setAggParameter(Text oSource) {
		String text = oSource.getText();
		int index = fAggParamtersTextWidgets.indexOf(oSource);
		EList<String> parameters = fGrouping.getAggregateParameters();
		for (int i = parameters.size(); i < fAggParamtersTextWidgets.size(); i++) {
			parameters.add(null);
		}
		parameters.set(index, text);
	}

	private boolean isAggParametersWidget(Object source) {
		return fAggParamtersTextWidgets.contains(source);
	}

	/**
	 * @param aggFuncName The name of aggregate function, it allow null case.
	 */
	private void showAggregateParameters(String aggFuncName) {
		// Remove old parameters widgets.
		Control[] children = fAggParameterComposite.getChildren();
		for (int i = 0; i < children.length; i++) {
			children[i].dispose();
		}
		fAggParamtersTextWidgets.clear();
		fExprBuilderWidgetsMap.clear();

		IAggregateFunction aFunc = null;
		try {
			aFunc = PluginSettings.instance().getAggregateFunction(aggFuncName);
		} catch (ChartException e) {
			// Since the aggFuncName might be null, so we don't display the
			// exception to user, it is true.
		}

		String[] args = null;
		if (aFunc != null) {
			args = aFunc.getDisplayParameters();
		}

		if (aFunc != null && args != null && args.length > 0) {
			((GridData) fAggParameterComposite.getLayoutData()).exclude = false;
			((GridData) fAggParameterComposite.getLayoutData()).heightHint = SWT.DEFAULT;
			for (int i = 0; i < args.length; i++) {
				Label lblArg = new Label(fAggParameterComposite, SWT.NONE);
				lblArg.setText(args[i] + ":"); //$NON-NLS-1$
				GridData gd = new GridData();
				lblArg.setLayoutData(gd);

				Text txtArg = new Text(fAggParameterComposite, SWT.BORDER);
				GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
				gridData.horizontalIndent = 0;
				txtArg.setLayoutData(gridData);
				fAggParamtersTextWidgets.add(txtArg);

				txtArg.addSelectionListener(this);
				txtArg.addFocusListener(new FocusListener() {

					public void focusGained(FocusEvent e) {
						// TODO Auto-generated method stub

					}

					public void focusLost(FocusEvent e) {
						setAggParameter((Text) e.getSource());
					}
				});

				Button btnBuilder = new Button(fAggParameterComposite, SWT.PUSH);
				{
					fExprBuilderWidgetsMap.put(btnBuilder, txtArg);
					GridData gdBTNBuilder = new GridData();
					// TED 69967, the button size in mac is not as large as in windows,
					// so the fixed image size can't display in mac, need use auto computing
					if (!Platform.OS_MACOSX.equals(Platform.getOS())) {
						gdBTNBuilder.heightHint = 20;
						gdBTNBuilder.widthHint = 20;
					}
					btnBuilder.setLayoutData(gdBTNBuilder);
					btnBuilder.setImage(UIHelper.getImage("icons/obj16/expressionbuilder.gif")); //$NON-NLS-1$

					btnBuilder.setToolTipText(
							Messages.getString("DataDefinitionComposite.Tooltip.InvokeExpressionBuilder")); //$NON-NLS-1$
					btnBuilder.getImage().setBackground(btnBuilder.getBackground());
					btnBuilder.setEnabled(fChartContext.getUIServiceProvider().isInvokingSupported());
					btnBuilder.setVisible(fChartContext.getUIServiceProvider().isEclipseModeSupported());
					btnBuilder.addSelectionListener(this);
				}
			}
		} else {
			((GridData) fAggParameterComposite.getLayoutData()).heightHint = 0;
			// ( (GridData) argsComposite.getLayoutData( ) ).exclude = true;
		}

		fAggParameterComposite.layout();
		fCmpAggregate.layout();

		Composite c = fAggParameterComposite;
		while (c != getShell()) {
			c.layout();
			c = c.getParent();
		}
		populateAggParameters();
	}

	public String[] getAggParametersName(String aggExpr) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.
	 * swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected(SelectionEvent e) {
	}
}