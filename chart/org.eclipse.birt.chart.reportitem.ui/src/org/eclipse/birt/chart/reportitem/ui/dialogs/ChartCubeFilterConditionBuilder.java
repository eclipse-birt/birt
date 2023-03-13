/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.chart.reportitem.ui.dialogs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.impl.ChartModelHelper;
import org.eclipse.birt.chart.reportitem.ChartCubeQueryHelper;
import org.eclipse.birt.chart.reportitem.ChartReportItemImpl;
import org.eclipse.birt.chart.reportitem.api.ChartCubeUtil;
import org.eclipse.birt.chart.reportitem.api.ChartItemUtil;
import org.eclipse.birt.chart.reportitem.api.ChartReportItemConstants;
import org.eclipse.birt.chart.reportitem.ui.ChartExpressionButtonUtil;
import org.eclipse.birt.chart.reportitem.ui.ChartReportItemUIUtil;
import org.eclipse.birt.chart.reportitem.ui.views.attributes.provider.ChartCubeFilterExpressionProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.IExpressionButton;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.util.ChartExpressionUtil.ExpressionCodec;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.olap.api.query.IBaseCubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.data.adapter.api.IModelAdapter;
import org.eclipse.birt.report.data.adapter.api.IModelAdapter.ExpressionLocation;
import org.eclipse.birt.report.designer.data.ui.util.DataSetProvider;
import org.eclipse.birt.report.designer.data.ui.util.DummyEngineTask;
import org.eclipse.birt.report.designer.internal.ui.data.DataService;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionButtonUtil;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.BaseTitleAreaDialog;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionBuilder;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.designer.ui.dialogs.FilterConditionBuilder;
import org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider;
import org.eclipse.birt.report.designer.ui.dialogs.SelectValueDialog;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.AlphabeticallyComparator;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.impl.ReportEngine;
import org.eclipse.birt.report.engine.api.impl.ReportEngineFactory;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.FilterConditionElementHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.elements.interfaces.IFilterConditionElementModel;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

/**
 * The dialog maintains the filters against chart using cube set case.
 *
 * @since 2.3
 */
public class ChartCubeFilterConditionBuilder extends BaseTitleAreaDialog {

	protected static final Logger logger = Logger.getLogger(FilterConditionBuilder.class.getName());

	protected ChartWizardContext context = null;

	private static String CHOICE_SELECT_VALUE = Messages.getString("ExpressionValueCellEditor.selectValueAction");//$NON-NLS-1$

	protected final String NULL_STRING = null;
	protected Composite dummy1, dummy2;
	protected Label label1, label2;

	protected List<String> valueList = new ArrayList<>();

	protected List selValueList = new ArrayList();

	/**
	 * Usable operators for building map rule conditions.
	 */
	protected static final String[][] OPERATOR;

	private ParamBindingHandle[] bindingParams = null;

	protected ReportElementHandle currentItem = null;

	protected static final String[] EMPTY_ARRAY = {};

	protected List<String> columnList, measureList = new ArrayList<>();

	protected int valueVisible;

	protected Table table;
	protected TableViewer tableViewer;

	/**
	 * Constant, represents empty String array.
	 */
	protected static final String[] EMPTY = {};

	private Map<String, String> fExprMap = new LinkedHashMap<>();

	protected String title, message;

	protected IChoiceSet choiceSet;

	protected IExpressionButton expButton;

	protected DataRequestSession session = null;

	protected final ExpressionCodec exprCodec = ChartModelHelper.instance().createExpressionCodec();

	/**
	 * @param title
	 */
	public ChartCubeFilterConditionBuilder(String title, String message) {
		this(UIUtil.getDefaultShell(), title, message);
	}

	/**
	 * @param parentShell
	 * @param title
	 */
	public ChartCubeFilterConditionBuilder(Shell parentShell, String title, String message) {
		super(parentShell);
		this.title = title;
		this.message = message;
	}

	private void setContext(ChartWizardContext context) {
		this.context = context;
	}

	protected synchronized DataRequestSession getDteSession() {
		if (session == null) {
			try {
				session = DataRequestSession
						.newSession(new DataSessionContext(DataSessionContext.MODE_DIRECT_PRESENTATION));
			} catch (BirtException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
		}

		return session;
	}

	protected synchronized void shutDownDteSession() {
		if (session != null) {
			session.shutdown();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.dialogs.FilterConditionBuilder#
	 * setColumnList(org.eclipse.birt.report.model.api.DesignElementHandle)
	 */
	protected void setColumnList(DesignElementHandle handle) {
		if (handle instanceof ExtendedItemHandle) {
			try {
				fExprMap = getValidFilterExpressions((ExtendedItemHandle) handle);
				columnList = new ArrayList<>(fExprMap.keySet());
				return;
			} catch (ExtendedElementException e) {
				e.printStackTrace();
			}
		}

		columnList = new ArrayList<>();
	}

	protected String adaptExpr(ExpressionCodec exprCodec) {
		Expression expr = new Expression(exprCodec.getExpression(), exprCodec.getType());
		IModelAdapter adapter = getDteSession().getModelAdaptor();
		return adapter.adaptExpression(expr, ExpressionLocation.CUBE).getText();
	}

	protected Map<String, String> getValidFilterExpressions(ExtendedItemHandle handle) throws ExtendedElementException {
		Map<String, String> exprMap = new LinkedHashMap<>();
		measureList = new ArrayList<>();

		for (LevelHandle lh : ChartCubeUtil.getAllLevels(handle.getCube())) {
			exprCodec.setBindingName(ChartCubeUtil.createLevelBindingName(lh), true);
			String expr = adaptExpr(exprCodec);
			exprMap.put(exprCodec.getBindingName(), expr);
		}
		for (MeasureHandle mh : ChartCubeUtil.getAllMeasures(handle.getCube())) {
			exprCodec.setBindingName(ChartCubeUtil.createMeasureBindingName(mh), true);
			String expr = adaptExpr(exprCodec);
			exprMap.put(exprCodec.getBindingName(), expr);
			measureList.add(exprCodec.getBindingName());
		}

		return exprMap;
	}

	protected String[] getDataSetColumns() {
		if (columnList.isEmpty()) {
			return EMPTY;
		}
		String[] values = new String[columnList.size()];
		for (int i = 0; i < columnList.size(); i++) {
			values[i] = columnList.get(i);
		}
		return values;
	}

	protected Object getResultSetColumn(String name) {
		if (columnList.isEmpty()) {
			return null;
		}
		for (int i = 0; i < columnList.size(); i++) {
			if (columnList.get(i).equals(name)) {
				return columnList.get(i);
			}
		}
		return null;
	}

	protected String getColumnName(Object obj) {
		return (String) obj;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		try {
			if (inputHandle == null) {
				FilterConditionElementHandle filter = DesignElementFactory.getInstance(currentItem.getModuleHandle())
						.newFilterConditionElement();
				filter.setProperty(IFilterConditionElementModel.OPERATOR_PROP,
						DEUtil.resolveNull(getValueForOperator(operator.getText())));

				// filter.setExpr( expButton.getExpression( ) );
				Expression expression = new Expression(expButton.getDisplayExpression(), expButton.getExpressionType());
				filter.setExpressionProperty(FilterCondition.EXPR_MEMBER, expression);

				if (valueVisible == 3) {
					filter.setValue1(valueList);
					filter.setValue2(""); //$NON-NLS-1$
				} else {
					assert (!expressionValue1.isDisposed());
					assert (!expressionValue2.isDisposed());
					if (expressionValue1.getVisible()) {
						List valueList = new ArrayList();
						valueList.add(ExpressionButtonUtil.getExpression(expressionValue1));
						filter.setValue1(valueList);
					} else {
						filter.setValue1(NULL_STRING);
					}

					if (expressionValue2.getVisible()) {
						ExpressionButtonUtil.saveExpressionButtonControl(expressionValue2, filter,
								FilterCondition.VALUE2_MEMBER);
					} else {
						filter.setValue2(NULL_STRING);
					}
				}

				PropertyHandle propertyHandle = designHandle
						.getPropertyHandle(ChartReportItemConstants.PROPERTY_CUBE_FILTER);
				propertyHandle.add(filter);
			} else {
				inputHandle.setOperator(DEUtil.resolveNull(getValueForOperator(operator.getText())));
				if (valueVisible == 3) {
					inputHandle.setValue1(valueList);
					inputHandle.setValue2(NULL_STRING);
				} else {
					assert (!expressionValue1.isDisposed());
					assert (!expressionValue2.isDisposed());
					if (expressionValue1.getVisible()) {
						List valueList = new ArrayList();
						valueList.add(ExpressionButtonUtil.getExpression(expressionValue1));
						inputHandle.setValue1(valueList);
					} else {
						inputHandle.setValue1(NULL_STRING);
					}

					if (expressionValue2.getVisible()) {
						ExpressionButtonUtil.saveExpressionButtonControl(expressionValue2, inputHandle,
								FilterCondition.VALUE2_MEMBER);
					} else {
						inputHandle.setValue2(NULL_STRING);
					}
				}
				// inputHandle.setExpr( expButton.getExpression( ) );
				Expression expression = new Expression(expButton.getDisplayExpression(), expButton.getExpressionType());
				inputHandle.setExpressionProperty(FilterCondition.EXPR_MEMBER, expression);
			}
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}

		super.okPressed();
	}

	// private String getDisplayExpression( String expression )
	// {
	// for ( Iterator<Entry<String, String>> iter = fExprMap.entrySet( )
	// .iterator( ); iter.hasNext( ); )
	// {
	// Entry<String, String> entry = iter.next( );
	// if ( expression == null && entry.getValue( ) == null )
	// {
	// return DEUtil.resolveNull( expression );
	// }
	// else if ( expression != null &&
	// expression.equals( entry.getValue( ) ) )
	// {
	// return entry.getKey( );
	// }
	// }
	// return DEUtil.resolveNull( expression );
	// }

	public void setReportElement(ReportElementHandle reportItem) {
		currentItem = reportItem;
	}

	/**
	 *
	 */
	public void setBindingParams(ParamBindingHandle[] params) {
		this.bindingParams = params;
	}

	static {
		IChoiceSet chset = ChoiceSetFactory.getStructChoiceSet(FilterCondition.FILTER_COND_STRUCT,
				FilterCondition.OPERATOR_MEMBER);
		IChoice[] chs = chset.getChoices(new AlphabeticallyComparator());
		OPERATOR = new String[chs.length][2];

		for (int i = 0; i < chs.length; i++) {
			OPERATOR[i][0] = chs[i].getDisplayName();
			OPERATOR[i][1] = chs[i].getName();
		}
	}

	/**
	 * Returns the operator value by its display name.
	 *
	 * @param name
	 */
	public static String getValueForOperator(String name) {
		for (int i = 0; i < OPERATOR.length; i++) {
			if (OPERATOR[i][0].equals(name)) {
				return OPERATOR[i][1];
			}
		}

		return null;
	}

	/**
	 * Returns how many value fields this operator needs.
	 *
	 * @param operatorValue
	 */
	public static int determineValueVisible(String operatorValue) {
		if (DesignChoiceConstants.FILTER_OPERATOR_ANY.equals(operatorValue)
				|| DesignChoiceConstants.FILTER_OPERATOR_FALSE.equals(operatorValue)
				|| DesignChoiceConstants.FILTER_OPERATOR_TRUE.equals(operatorValue)
				|| DesignChoiceConstants.FILTER_OPERATOR_NULL.equals(operatorValue)
				|| DesignChoiceConstants.FILTER_OPERATOR_NOT_NULL.equals(operatorValue)) {
			return 0;
		} else if (DesignChoiceConstants.FILTER_OPERATOR_LT.equals(operatorValue)
				|| DesignChoiceConstants.FILTER_OPERATOR_LE.equals(operatorValue)
				|| DesignChoiceConstants.FILTER_OPERATOR_EQ.equals(operatorValue)
				|| DesignChoiceConstants.FILTER_OPERATOR_NE.equals(operatorValue)
				|| DesignChoiceConstants.FILTER_OPERATOR_GE.equals(operatorValue)
				|| DesignChoiceConstants.FILTER_OPERATOR_GT.equals(operatorValue)
				|| DesignChoiceConstants.FILTER_OPERATOR_LIKE.equals(operatorValue)) {
			return 1;
		} else if (DesignChoiceConstants.FILTER_OPERATOR_BETWEEN.equals(operatorValue)
				|| DesignChoiceConstants.FILTER_OPERATOR_NOT_BETWEEN.equals(operatorValue)) {
			return 2;
		} else if (DesignChoiceConstants.FILTER_OPERATOR_IN.equals(operatorValue)
				|| DesignChoiceConstants.FILTER_OPERATOR_NOT_IN.equals(operatorValue)) {
			return 3;
		}

		return 1;
	}

	/**
	 * Returns the operator display name by its value.
	 *
	 * @param value
	 */
	public static String getNameForOperator(String value) {
		for (int i = 0; i < OPERATOR.length; i++) {
			if (OPERATOR[i][1].equals(value)) {
				return OPERATOR[i][0];
			}
		}

		return ""; //$NON-NLS-1$
	}

	/**
	 * Returns the index for given operator value in the operator list.
	 *
	 * @param value
	 */
	protected static int getIndexForOperatorValue(String value) {
		for (int i = 0; i < OPERATOR.length; i++) {
			if (OPERATOR[i][1].equals(value)) {
				return i;
			}
		}

		return 0;
	}

	protected FilterConditionElementHandle inputHandle;

	protected Combo expression, operator;

	protected Button addBtn, editBtn, delBtn, delAllBtn;

	protected CCombo expressionValue1, expressionValue2, addExpressionValue;

	protected Composite valueListComposite;

	protected Label andLable;

	protected DesignElementHandle designHandle;

	protected static final String VALUE_OF_THIS_DATA_ITEM = Messages
			.getString("FilterConditionBuilder.choice.ValueOfThisDataItem"); //$NON-NLS-1$

	private String fCurrentExpr = ""; //$NON-NLS-1$
	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.dialogs.Dialog#createContents(org.eclipse.swt.widgets.
	 * Composite)
	 */

	@Override
	protected Control createDialogArea(Composite parent) {
		UIUtil.bindHelp(parent, IHelpContextIds.INSERT_EDIT_FILTER_CONDITION_DIALOG_ID);

		Composite area = (Composite) super.createDialogArea(parent);
		Composite contents = new Composite(area, SWT.NONE);
		contents.setLayoutData(new GridData(GridData.FILL_BOTH));
		contents.setLayout(new GridLayout());

		this.setTitle(title);
		this.setMessage(message);
		getShell().setText(title);

		applyDialogFont(contents);
		initializeDialogUnits(area);

		createFilterConditionContent(contents);

		return area;
	}

	protected void createFilterConditionContent(Composite innerParent) {

		Composite anotherParent = new Composite(innerParent, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		anotherParent.setLayoutData(gd);
		GridLayout glayout = new GridLayout(4, false);
		anotherParent.setLayout(glayout);

		Label lb = new Label(anotherParent, SWT.NONE);
		lb.setText(Messages.getString("FilterConditionBuilder.text.Condition")); //$NON-NLS-1$

		Label lb2 = new Label(anotherParent, SWT.NONE);
		lb2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		new Label(anotherParent, SWT.NONE);

		Composite condition = new Composite(innerParent, SWT.NONE);
		gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 180;
		condition.setLayoutData(gd);
		glayout = new GridLayout(5, false);
		condition.setLayout(glayout);

		expression = new Combo(condition, SWT.NONE);
		GridData gdata = new GridData();
		gdata.widthHint = 100;
		expression.setLayoutData(gdata);
		expression.addListener(SWT.Selection, comboModifyListener);
		// expression.setItems( getDataSetColumns( ) );
		if (expression.getItemCount() == 0) {
			expression.add(DEUtil.resolveNull(null));
		}
		expression.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (expression.getText().equals(VALUE_OF_THIS_DATA_ITEM) && designHandle instanceof DataItemHandle) {
					expression
							.setText(DEUtil.getColumnExpression(((DataItemHandle) designHandle).getResultSetColumn()));
				}
				updateButtons();
			}
		});

		// Create expression button.
		ExpressionProvider ep = new ChartExpressionProvider(designHandle, context,
				ChartReportItemUIUtil.getExpressionBuilderStyle(IUIServiceProvider.COMMAND_EXPRESSION_DATA_BINDINGS));
		expressionProvider = ep;

		expButton = ChartExpressionButtonUtil.createExpressionButton(condition, expression,
				(ExtendedItemHandle) designHandle, ep);

		expButton.addListener(new Listener() {

			@Override
			public void handleEvent(Event event) {
				if (event.data instanceof String[]) {
					updateButtons();
					if (!expButton.getExpression().equals(fCurrentExpr)) {
						needRefreshList = true;
						fCurrentExpr = expButton.getExpression();
					}
				}
			}
		});
		expButton.setPredefinedQuery(getDataSetColumns());

		operator = new Combo(condition, SWT.READ_ONLY);
		for (int i = 0; i < OPERATOR.length; i++) {
			operator.add(OPERATOR[i][0]);
		}
		operator.setVisibleItemCount(30);
		operator.addSelectionListener(operatorSelectionListener);

		if (operator.getItemCount() > 0 && operator.getSelectionIndex() == -1) {
			operator.select(getIndexForOperatorValue("eq")); //$NON-NLS-1$
		}

		create2ValueComposite(condition);

		if (inputHandle != null) {
			syncViewProperties();
		}

		lb = new Label(innerParent, SWT.SEPARATOR | SWT.HORIZONTAL);
		lb.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

	}

	protected Listener expValueVerifyListener = new Listener() {

		@Override
		public void handleEvent(Event event) {
			CCombo thisCombo = (CCombo) event.widget;
			String text = event.text;
			if (text != null && thisCombo.indexOf(text) >= 0) {
				event.doit = false;
			} else {
				event.doit = true;
			}
		}
	};

	private Listener expValueSelectionListener = new Listener() {

		@Override
		public void handleEvent(Event event) {
			CCombo thisCombo = (CCombo) event.widget;
			int selectionIndex = thisCombo.getSelectionIndex();
			if (selectionIndex < 0) {
				return;
			}
			String value = thisCombo.getItem(selectionIndex);

			boolean isAddClick = false;
			if (tableViewer != null && (addBtn != null && (!addBtn.isDisposed()))) {
				isAddClick = true;
			}

			String express = expButton.getExpression();
			if (express != null) {
				express = express.trim();
			}
			exprCodec.decode(express);
			String bindingName = exprCodec.getBindingName();
			// if ( bindingName == null )
			// {
			// String regx = "\\Qdata[\"\\E.*\\Q\"]\\E"; //$NON-NLS-1$
			// if ( express != null && express.matches( regx ))
			// {
			// bindingName = express;
			// }
			// }

			boolean returnValue = false;
			if (value != null) {
				String newValues[] = new String[1];
				if (CHOICE_SELECT_VALUE.equals(value)) {
					if (bindingName != null) {
						if (designHandle instanceof ReportItemHandle
								&& ((ReportItemHandle) designHandle).getCube() != null) {
							List selectValueList = getSelectValueList();
							if (selectValueList == null || selectValueList.size() == 0) {
								MessageDialog.openInformation(null, Messages.getString("SelectValueDialog.selectValue"), //$NON-NLS-1$
										Messages.getString(org.eclipse.birt.chart.reportitem.ui.i18n.Messages.getString(
												"ChartCubeFilterConditionBuilder.SelectValueDialog.messages.info.selectVauleUnavailable"))); //$NON-NLS-1$

							} else {
								SelectValueDialog dialog = new SelectValueDialog(
										PlatformUI.getWorkbench().getDisplay().getActiveShell(),
										Messages.getString("ExpressionValueCellEditor.title")); //$NON-NLS-1$
								if (isAddClick) {
									dialog.setMultipleSelection(true);
								}
								dialog.setSelectedValueList(selectValueList);
								if (bindingParams != null) {
									dialog.setBindingParams(bindingParams);
								}
								if (dialog.open() == IDialogConstants.OK_ID) {
									returnValue = true;
									newValues = dialog.getSelectedExprValues();
								}
							}
						} else {
							MessageDialog.openInformation(null, Messages.getString("SelectValueDialog.selectValue"), //$NON-NLS-1$
									Messages.getString(org.eclipse.birt.chart.reportitem.ui.i18n.Messages.getString(
											"ChartCubeFilterConditionBuilder.SelectValueDialog.messages.info.selectVauleUnavailable"))); //$NON-NLS-1$
						}
					} else {
						MessageDialog.openInformation(null, Messages.getString("SelectValueDialog.selectValue"), //$NON-NLS-1$
								org.eclipse.birt.chart.reportitem.ui.i18n.Messages.getString(
										"ChartCubeFilterConditionBuilder.SelectValueDialog.messages.info.illegalVauleExpr")); //$NON-NLS-1$
					}
				}

				if (returnValue) {
					if (addExpressionValue == thisCombo) {
						thisCombo.setText(""); //$NON-NLS-1$
						addBtn.setEnabled(false);
					} else if (newValues.length == 1) {
						thisCombo.setText(DEUtil.resolveNull(newValues[0]));
					}

					if (isAddClick) {

						boolean change = false;
						for (int i = 0; i < newValues.length; i++) {
							if (valueList.indexOf(DEUtil.resolveNull(newValues[i])) < 0) {
								valueList.add(DEUtil.resolveNull(newValues[i]));
								change = true;
							}
						}
						if (change) {
							tableViewer.refresh();
							updateButtons();
							addExpressionValue.setFocus();
						}

					}

				}
			}
		}

	};

	private int create2ValueComposite(Composite condition) {

		if (expressionValue1 != null && !expressionValue1.isDisposed()) {
			return 0;
		}

		if (valueListComposite != null && !valueListComposite.isDisposed()) {
			valueListComposite.dispose();
			valueListComposite = null;
		}

		GridData expgd = new GridData();
		expgd.widthHint = 100;

		expressionValue1 = createExpressionValue(condition);
		expressionValue1.setLayoutData(expgd);

		dummy1 = createDummy(condition, 3);

		andLable = new Label(condition, SWT.NONE);
		andLable.setText(Messages.getString("FilterConditionBuilder.text.AND")); //$NON-NLS-1$
		andLable.setEnabled(false);
		andLable.setVisible(false);

		dummy2 = createDummy(condition, 3);

		expressionValue2 = createExpressionValue(condition);
		expressionValue2.setLayoutData(expgd);

		expressionValue2.setVisible(false);
		ExpressionButtonUtil.getExpressionButton(expressionValue2).getControl().setVisible(false);

		condition.getParent().layout(true, true);
		return 1;
	}

	private CCombo createExpressionValue(Composite parent) {
		final CCombo expressionValue = new CCombo(parent, SWT.BORDER);
		expressionValue.add(CHOICE_SELECT_VALUE);
		expressionValue.addListener(SWT.Verify, expValueVerifyListener);
		expressionValue.addListener(SWT.Selection, expValueSelectionListener);
		Listener listener = new Listener() {

			@Override
			public void handleEvent(Event event) {
				updateButtons();
			}

		};
		expressionValue.addListener(SWT.Modify, listener);
		expressionValue.addListener(SWT.MouseDown, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				if (isMeasureSelected()) {
					if (expressionValue.getItemCount() > 0) {
						expressionValue.remove(0);
					}
					expressionValue.setVisibleItemCount(0);
				} else {
					if (expressionValue.getItemCount() == 0) {
						expressionValue.add(CHOICE_SELECT_VALUE);
					}
					expressionValue.setVisibleItemCount(1);
				}
			}
		});

		IExpressionButton ceb = ChartExpressionButtonUtil.createExpressionButton(parent, expressionValue,
				(ExtendedItemHandle) designHandle, expressionProvider);
		ceb.addListener(listener);

		return expressionValue;
	}

	private int createValueListComposite(Composite parent) {

		if (valueListComposite != null && !valueListComposite.isDisposed()) {
			return 0;
		}

		if (expressionValue1 != null && !expressionValue1.isDisposed()) {
			ExpressionButtonUtil.getExpressionButton(expressionValue1).getControl().dispose();
			expressionValue1.dispose();
			expressionValue1 = null;

			dummy1.dispose();
			dummy1 = null;

			ExpressionButtonUtil.getExpressionButton(expressionValue2).getControl().dispose();
			expressionValue2.dispose();
			expressionValue2 = null;

			dummy2.dispose();
			dummy2 = null;

			andLable.dispose();
			andLable = null;
		}

		valueListComposite = new Composite(parent, SWT.NONE);
		GridData gdata = new GridData(GridData.FILL_HORIZONTAL);
		gdata.horizontalSpan = 4;
		valueListComposite.setLayoutData(gdata);
		GridLayout layout = new GridLayout();
		layout.numColumns = 4;
		valueListComposite.setLayout(layout);

		Group group = new Group(valueListComposite, SWT.NONE);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.heightHint = 118;
		data.horizontalSpan = 3;
		data.horizontalIndent = 0;
		data.horizontalAlignment = SWT.BEGINNING;
		data.grabExcessHorizontalSpace = true;
		group.setLayoutData(data);
		layout = new GridLayout();
		layout.numColumns = 5;
		group.setLayout(layout);

		new Label(group, SWT.NONE).setText(Messages.getString("FilterConditionBuilder.label.value")); //$NON-NLS-1$

		GridData expgd = new GridData();
		expgd.widthHint = 100;

		addExpressionValue = createExpressionValue(group);
		addExpressionValue.setLayoutData(expgd);

		addBtn = new Button(group, SWT.PUSH);
		addBtn.setText(Messages.getString("FilterConditionBuilder.button.add")); //$NON-NLS-1$
		addBtn.setToolTipText(Messages.getString("FilterConditionBuilder.button.add.tooltip")); //$NON-NLS-1$
		setButtonLayoutData(addBtn);
		addBtn.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				String value = addExpressionValue.getText().trim();
				if (valueList.indexOf(value) < 0) {
					valueList.add(value);
					tableViewer.refresh();
					updateButtons();
					addExpressionValue.setFocus();
					addExpressionValue.setText(""); //$NON-NLS-1$
				} else {
					addBtn.setEnabled(false);
				}

			}
		});

		new Label(group, SWT.NONE);

		int tableStyle = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION;
		table = new Table(group, tableStyle);
		data = new GridData(GridData.FILL_VERTICAL);
		data.horizontalSpan = 4;
		table.setLayoutData(data);

		table.setHeaderVisible(false);
		table.setLinesVisible(true);
		TableColumn column;
		int i;
		String[] columNames = { Messages.getString("FilterConditionBuilder.list.item1"), //$NON-NLS-1$
		};
		int[] columLength = { 288 };
		for (i = 0; i < columNames.length; i++) {
			column = new TableColumn(table, SWT.NONE, i);
			column.setText(columNames[i]);
			column.setWidth(columLength[i]);
		}
		table.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				checkEditDelButtonStatus();
			}
		});

		table.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.DEL) {
					int index = table.getSelectionIndex();
					if (index > -1) {
						valueList.remove(index);
						tableViewer.refresh();
						if (valueList.size() > 0) {
							if (valueList.size() <= index) {
								index = index - 1;
							}
							table.select(index);
						}
						updateButtons();
					} else {
						delBtn.setEnabled(false);
					}
				}

			}

			@Override
			public void keyReleased(KeyEvent e) {

			}

		});
		table.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
				if (selection.getFirstElement() != null && selection.getFirstElement() instanceof String) {
					String initValue = (String) selection.getFirstElement();

					ExpressionBuilder expressionBuilder = new ExpressionBuilder(getShell(), initValue);

					if (designHandle != null) {
						if (expressionProvider == null) {
							expressionBuilder.setExpressionProvider(new ExpressionProvider(designHandle));
						} else {
							expressionBuilder.setExpressionProvider(expressionProvider);
						}
					}

					if (expressionBuilder.open() == OK) {
						String result = DEUtil.resolveNull(expressionBuilder.getResult());
						int index = table.getSelectionIndex();
						valueList.remove(index);
						valueList.add(index, result);
						tableViewer.refresh();
						table.select(index);
					}
					updateButtons();
				} else {
					editBtn.setEnabled(false);
				}
			}
		});

		tableViewer = new TableViewer(table);
		tableViewer.setUseHashlookup(true);
		tableViewer.setColumnProperties(columNames);
		tableViewer.setLabelProvider(tableLableProvier);
		tableViewer.setContentProvider(tableContentProvider);

		Composite rightPart = new Composite(valueListComposite, SWT.NONE);
		data = new GridData(GridData.FILL_BOTH | GridData.VERTICAL_ALIGN_END);
		rightPart.setLayoutData(data);
		layout = new GridLayout();
		layout.makeColumnsEqualWidth = true;
		rightPart.setLayout(layout);

		editBtn = new Button(rightPart, SWT.PUSH);
		editBtn.setText(Messages.getString("FilterConditionBuilder.button.edit")); //$NON-NLS-1$
		editBtn.setToolTipText(Messages.getString("FilterConditionBuilder.button.edit.tooltip")); //$NON-NLS-1$
		setButtonLayoutData(editBtn);
		editBtn.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
				if (selection.getFirstElement() != null && selection.getFirstElement() instanceof String) {
					String initValue = (String) selection.getFirstElement();

					ExpressionBuilder expressionBuilder = new ExpressionBuilder(getShell(), initValue);

					if (designHandle != null) {
						if (expressionProvider == null) {
							expressionBuilder.setExpressionProvier(new ExpressionProvider(designHandle));
						} else {
							expressionBuilder.setExpressionProvier(expressionProvider);
						}
					}

					if (expressionBuilder.open() == OK) {
						String result = DEUtil.resolveNull(expressionBuilder.getResult());
						int index = table.getSelectionIndex();
						valueList.remove(index);
						valueList.add(index, result);
						tableViewer.refresh();
						table.select(index);
					}
					updateButtons();
				} else {
					editBtn.setEnabled(false);
				}
			}

		});

		delBtn = new Button(rightPart, SWT.PUSH);
		delBtn.setText(Messages.getString("FilterConditionBuilder.button.delete")); //$NON-NLS-1$
		delBtn.setToolTipText(Messages.getString("FilterConditionBuilder.button.delete.tooltip")); //$NON-NLS-1$
		setButtonLayoutData(delBtn);
		delBtn.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = table.getSelectionIndex();
				if (index > -1) {
					valueList.remove(index);
					tableViewer.refresh();
					if (valueList.size() > 0) {
						if (valueList.size() <= index) {
							index = index - 1;
						}
						table.select(index);
					}
					updateButtons();
				} else {
					delBtn.setEnabled(false);
				}
			}

		});

		delAllBtn = new Button(rightPart, SWT.PUSH);
		delAllBtn.setText(Messages.getString("FilterConditionBuilder.button.deleteall")); //$NON-NLS-1$
		delAllBtn.setToolTipText(Messages.getString("FilterConditionBuilder.button.deleteall.tooltip")); //$NON-NLS-1$
		setButtonLayoutData(delAllBtn);
		delAllBtn.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				int count = valueList.size();
				if (count > 0) {
					valueList.clear();
					tableViewer.refresh();
					updateButtons();
				} else {
					delAllBtn.setEnabled(false);
				}
			}

		});

		addExpressionValue.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				checkAddButtonStatus();
				updateButtons();
			}
		});

		parent.getParent().layout(true, true);
		return 1;

	}

	protected ITableLabelProvider tableLableProvier = new ITableLabelProvider() {

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			if (columnIndex == 0) {
				return (String) element;
			}
			return ""; //$NON-NLS-1$
		}

		@Override
		public void addListener(ILabelProviderListener listener) {

		}

		@Override
		public void dispose() {

		}

		@Override
		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener listener) {

		}
	};

	protected IStructuredContentProvider tableContentProvider = new IStructuredContentProvider() {

		@Override
		public void dispose() {

		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}

		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement == null) {
				return new Object[0];
			} else if (inputElement instanceof List) {
				return ((List) inputElement).toArray();
			}
			return null;
		}
	};

	protected SelectionListener operatorSelectionListener = new SelectionListener() {

		@Override
		public void widgetSelected(SelectionEvent e) {
			String value = getValueForOperator(operator.getText());

			valueVisible = determineValueVisible(value);

			if (valueVisible == 3) {
				int ret = createValueListComposite(operator.getParent());
				if (ret != 0) {
					if (inputHandle != null) {
						valueList = new ArrayList(inputHandle.getValue1List());
					}

					tableViewer.setInput(valueList);
				}
			} else {
				int ret = create2ValueComposite(operator.getParent());
				if (ret != 0 && inputHandle != null) {
					expressionValue1.setText(DEUtil.resolveNull(inputHandle.getValue1()));
					expressionValue2.setText(DEUtil.resolveNull(inputHandle.getValue2()));
				}

			}

			if (valueVisible == 0) {
				expressionValue1.setVisible(false);
				ExpressionButtonUtil.getExpressionButton(expressionValue1).getControl().setVisible(false);
				expressionValue2.setVisible(false);
				ExpressionButtonUtil.getExpressionButton(expressionValue2).getControl().setVisible(false);
				andLable.setVisible(false);
			} else if (valueVisible == 1) {
				expressionValue1.setVisible(true);
				ExpressionButtonUtil.getExpressionButton(expressionValue1).getControl().setVisible(true);
				expressionValue2.setVisible(false);
				ExpressionButtonUtil.getExpressionButton(expressionValue2).getControl().setVisible(false);
				andLable.setVisible(false);
			} else if (valueVisible == 2) {
				expressionValue1.setVisible(true);
				ExpressionButtonUtil.getExpressionButton(expressionValue1).getControl().setVisible(true);
				expressionValue2.setVisible(true);
				ExpressionButtonUtil.getExpressionButton(expressionValue2).getControl().setVisible(true);
				andLable.setVisible(true);
				andLable.setEnabled(true);
			}
			updateButtons();
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {

		}
	};

	protected Listener comboModifyListener = new Listener() {

		@Override
		public void handleEvent(Event e) {
			Assert.isLegal(e.widget instanceof Combo);
			Combo combo = (Combo) e.widget;
			String newValue = combo.getText();
			String value = DEUtil.getExpression(getResultSetColumn(newValue));
			if (value != null) {
				newValue = value;
			}
			combo.setText(newValue);
			updateButtons();
		}
	};

	protected Composite createDummy(Composite parent, int colSpan) {
		Composite dummy = new Composite(parent, SWT.NONE);
		GridData gdata = new GridData();
		gdata.widthHint = 22;
		gdata.horizontalSpan = colSpan;
		gdata.heightHint = 10;
		dummy.setLayoutData(gdata);

		return dummy;
	}

	/*
	 * Update handle for the Map Rule builder
	 */
	public void updateHandle(FilterConditionElementHandle handle, int handleCount) {
		this.inputHandle = handle;
	}

	/*
	 * Set design handle for the Map Rule builder
	 */
	private void setDesignHandle(DesignElementHandle handle) {
		this.designHandle = handle;
		setColumnList(this.designHandle);
	}

	public void setDesignHandle(DesignElementHandle handle, ChartWizardContext context) {
		setContext(context);
		setDesignHandle(handle);

	}

	protected IExpressionProvider expressionProvider;

	public void setDesignHandle(DesignElementHandle handle, IExpressionProvider provider) {
		setDesignHandle(handle);
		this.expressionProvider = provider;
		setColumnList(this.designHandle);
	}

	/*
	 * Return the hanle of Map Rule builder
	 */
	public FilterConditionElementHandle getInputHandle() {
		return inputHandle;
	}

	/**
	 * Refreshes the OK button state.
	 *
	 */
	@Override
	protected void updateButtons() {
		enableInput(isExpressionOK());
		if (getButton(IDialogConstants.OK_ID) != null) {
			getButton(IDialogConstants.OK_ID).setEnabled(isConditionOK());
		}

	}

	protected void enableInput(boolean val) {
		operator.setEnabled(val);
		if (valueVisible != 3) {
			if (expressionValue1 != null) {
				expressionValue1.setEnabled(val);
				ExpressionButtonUtil.getExpressionButton(expressionValue1).getControl().setEnabled(val);
			}

			if (expressionValue2 != null) {
				expressionValue2.setEnabled(val);
				ExpressionButtonUtil.getExpressionButton(expressionValue2).getControl().setEnabled(val);
			}

			if (andLable != null) {
				andLable.setEnabled(val);
			}

		} else {
			setControlEnable(valueListComposite, val);
			if (val) {
				checkAddButtonStatus();
				checkEditDelButtonStatus();
			} // or set all the children control to false
		}

	}

	protected void setControlEnable(Control control, boolean bool) {
		if (control == null || control.isDisposed()) {
			return;
		}
		control.setEnabled(bool);
		Composite tmp = null;
		if (control instanceof Composite) {
			tmp = (Composite) control;
		}
		if (tmp != null && tmp.getChildren() != null) {
			for (int i = 0; i < tmp.getChildren().length; i++) {
				setControlEnable(tmp.getChildren()[i], bool);
			}
		}
	}

	/**
	 * Gets if the expression field is not empty.
	 */
	protected boolean isExpressionOK() {
		if ((expression == null) || expression.getText() == null || expression.getText().length() == 0) {
			return false;
		}

		return true;
	}

	/**
	 * Gets if the condition is available.
	 */
	protected boolean isConditionOK() {
		if ((expression == null) || !isExpressionOK()) {
			return false;
		}

		return checkValues();
	}

	/**
	 * Gets if the values of the condition is(are) available.
	 */
	protected boolean checkValues() {
		if (valueVisible == 3) {
			if (valueList.size() <= 0) {
				return false;
			} else {
				return true;
			}
		} else if (expressionValue1 != null && expressionValue2 != null) {
			assert (!expressionValue1.isDisposed());
			assert (!expressionValue2.isDisposed());

			if (expressionValue1.getVisible()) {
				if (expressionValue1.getText() == null || expressionValue1.getText().trim().length() == 0) {
					return false;
				}
			}

			if (expressionValue2.getVisible()) {
				if (expressionValue2.getText() == null || expressionValue2.getText().trim().length() == 0) {
					return false;
				}
			}
		}

		return true;
	}

	protected void checkAddButtonStatus() {
		if (addExpressionValue != null && !addExpressionValue.isDisposed()) {
			String value = addExpressionValue.getText();
			if (value == null || value.length() == 0 || value.trim().length() == 0) {
				addBtn.setEnabled(false);
				return;
			}
			if (value != null) {
				value = value.trim();
			}
			if (valueList.indexOf(value) < 0) {
				addBtn.setEnabled(true);
			} else {
				addBtn.setEnabled(false);
			}
		}
	}

	protected void checkEditDelButtonStatus() {
		if (tableViewer == null || table.isDisposed()) {
			return;
		}
		boolean enabled = (tableViewer.getSelection() == null) ? false : true;
		if (enabled && tableViewer.getSelection() instanceof StructuredSelection) {
			StructuredSelection selection = (StructuredSelection) tableViewer.getSelection();
			if (selection.toList().size() <= 0) {
				enabled = false;
			}
		}
		editBtn.setEnabled(enabled);
		delBtn.setEnabled(enabled);

		enabled = table.getItemCount() > 0 ? true : false;
		delAllBtn.setEnabled(enabled);

	}

	/**
	 * SYNC the control value according to the handle.
	 */
	protected void syncViewProperties() {
		ChartItemUtil.loadExpression(exprCodec, inputHandle);

		expButton.setExpression(exprCodec.encode());
		operator.select(getIndexForOperatorValue(inputHandle.getOperator()));
		valueVisible = determineValueVisible(inputHandle.getOperator());

		if (valueVisible == 3) {
			createValueListComposite(operator.getParent());
			valueList = new ArrayList(inputHandle.getValue1List());
			tableViewer.setInput(valueList);
		} else {
			create2ValueComposite(operator.getParent());
			if (inputHandle != null) {
				if (inputHandle.getValue1ExpressionList().getListValue() != null
						&& inputHandle.getValue1ExpressionList().getListValue().size() > 0) {
					ExpressionButtonUtil.initExpressionButtonControl(expressionValue1,
							inputHandle.getValue1ExpressionList().getListValue().get(0));
				}
				ExpressionButtonUtil.initExpressionButtonControl(expressionValue2, inputHandle,
						FilterCondition.VALUE2_MEMBER);
			}
		}

		if (valueVisible == 0) {
			expressionValue1.setVisible(false);
			ExpressionButtonUtil.getExpressionButton(expressionValue1).getControl().setVisible(false);
			expressionValue2.setVisible(false);
			ExpressionButtonUtil.getExpressionButton(expressionValue2).getControl().setVisible(false);
			andLable.setVisible(false);
		} else if (valueVisible == 1) {
			expressionValue1.setVisible(true);
			ExpressionButtonUtil.getExpressionButton(expressionValue1).getControl().setVisible(true);
			expressionValue2.setVisible(false);
			ExpressionButtonUtil.getExpressionButton(expressionValue2).getControl().setVisible(false);

			andLable.setVisible(false);
		} else if (valueVisible == 2) {
			expressionValue1.setVisible(true);
			ExpressionButtonUtil.getExpressionButton(expressionValue1).getControl().setVisible(true);
			expressionValue2.setVisible(true);

			ExpressionButtonUtil.getExpressionButton(expressionValue2).getControl().setVisible(true);
			andLable.setVisible(true);
			andLable.setEnabled(true);
		} else if (valueVisible == 3) {
			if (expression.getText().length() == 0) {
				valueListComposite.setEnabled(false);
			} else {
				valueListComposite.setEnabled(true);
			}
		}

	}

	protected void editValue(Control control) {
		String initValue = null;
		if (control instanceof Text) {
			initValue = ((Text) control).getText();
		} else if (control instanceof Combo) {
			initValue = ((Combo) control).getText();
		} else if (control instanceof CCombo) {
			initValue = ((CCombo) control).getText();
		}
		ExpressionBuilder expressionBuilder = new ExpressionBuilder(getShell(), initValue);

		if (designHandle != null) {
			if (expressionProvider == null) {
				ExpressionProvider exprProvider = new ChartCubeFilterExpressionProvider(designHandle,
						fExprMap.values().toArray(new String[] {}));
				expressionBuilder.setExpressionProvider(exprProvider);
			} else {
				expressionBuilder.setExpressionProvider(expressionProvider);
			}
		}

		if (expressionBuilder.open() == OK) {
			String result = DEUtil.resolveNull(expressionBuilder.getResult());
			if (control instanceof Text) {
				((Text) control).setText(result);
			} else if (control instanceof Combo) {
				((Combo) control).setText(result);
			} else if (control instanceof CCombo) {
				((CCombo) control).setText(result);
			}
		}
		updateButtons();
	}

	/**
	 * Sets the model input.
	 *
	 * @param input
	 */
	public void setInput(Object inputHandle) {
		if (inputHandle instanceof FilterConditionElementHandle) {
			this.inputHandle = (FilterConditionElementHandle) inputHandle;
		} else {
			this.inputHandle = null;
		}

	}

	private transient boolean needRefreshList = true;

	/**
	 * @return
	 */
	private List getSelectValueList() {
		if (!needRefreshList) {
			return selValueList;
		}
		CubeHandle cube = null;
		if (designHandle instanceof ExtendedItemHandle) {
			cube = ((ExtendedItemHandle) designHandle).getCube();
		}
		if (cube == null || expression.getText().length() == 0) {
			return new ArrayList();
		}
		Iterator iter = null;

		// get cubeQueryDefn
		IBaseCubeQueryDefinition cubeQueryDefn = null;
		try {
			ModuleHandle moduleHandle = designHandle.getModuleHandle();

			EngineConfig config = new EngineConfig();

			config.setProperty(EngineConstants.APPCONTEXT_CLASSLOADER_KEY, DataSetProvider
					.getCustomScriptClassLoader(Thread.currentThread().getContextClassLoader(), moduleHandle));
			ReportEngine engine = (ReportEngine) new ReportEngineFactory().createReportEngine(config);

			DummyEngineTask engineTask = new DummyEngineTask(engine,
					ChartItemUtil.openReportDesign(engine, moduleHandle), moduleHandle);
			session = engineTask.getDataSession();

			engineTask.run();

			DataService.getInstance().registerSession(cube, session);

			IReportItem item = ((ExtendedItemHandle) designHandle).getReportItem();
			Chart cm = getChartModel(item);
			ChartCubeQueryHelper ccqh = new ChartCubeQueryHelper((ExtendedItemHandle) designHandle, cm,
					session.getModelAdaptor());
			// The equivalent expressions mean the expression is not used by
			// chart model, it needs to add the binding into cube query.
			String expr = expButton.getExpression();
			if (expr != null && expr.equals(expButton.getExpression())) {
				cubeQueryDefn = ccqh.createCubeQuery(null, new String[] { expr });
			} else {
				cubeQueryDefn = ccqh.createCubeQuery(null);
			}
			String bindingName = exprCodec.getBindingName(expr);
			exprCodec.setBindingName(bindingName, true, ExpressionType.JAVASCRIPT);

			iter = session.getCubeQueryUtil().getMemberValueIterator(cube, exprCodec.getExpression(),
					(ICubeQueryDefinition) cubeQueryDefn);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		selValueList = new ArrayList();
		while (iter != null && iter.hasNext()) {
			Object obj = iter.next();
			if (obj != null) {
				if (selValueList.indexOf(obj) < 0) {
					selValueList.add(obj);
				}

			}

		}

		needRefreshList = false;
		return selValueList;
	}

	/**
	 * Gets chart model from UI context under chart builder case or from report item
	 * under property editor case.
	 *
	 * @param item
	 * @return
	 */
	protected Chart getChartModel(IReportItem item) {
		Chart cm;
		if (context != null && context.getModel() != null) {
			cm = context.getModel();
		} else {
			cm = (Chart) ((ChartReportItemImpl) item).getProperty(ChartReportItemConstants.PROPERTY_CHART);
		}
		return cm;
	}

	@Override
	public int open() {
		if (getShell() == null) {
			// create the window
			create();
		}
		updateButtons();
		return super.open();
	}

	protected void updateMessage(String s, int type) {
		super.setMessage(s, type);
	}

	@Override
	public boolean close() {
		shutDownDteSession();
		return super.close();
	}

	private boolean isMeasureSelected() {
		exprCodec.setExpression(expression.getText());
		exprCodec.setType(expButton.getExpressionType());
		String bindingName = exprCodec.getBindingName();
		return measureList.contains(bindingName);
	}
}
