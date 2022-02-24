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

package org.eclipse.birt.report.designer.ui.dialogs;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.data.ui.dataset.DataSetViewData;
import org.eclipse.birt.report.designer.data.ui.util.SelectValueFetcher;
import org.eclipse.birt.report.designer.internal.ui.dialogs.ResourceEditDialog;
import org.eclipse.birt.report.designer.internal.ui.dialogs.expression.ExpressionEditor;
import org.eclipse.birt.report.designer.internal.ui.expressions.IExpressionConverter;
import org.eclipse.birt.report.designer.internal.ui.extension.IUseCubeQueryList;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionButtonUtil;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionUtility;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.expressions.ExpressionFilter;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.MapHandleProvider;
import org.eclipse.birt.report.designer.util.AlphabeticallyComparator;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.MapRuleHandle;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.elements.structures.MapRule;
import org.eclipse.birt.report.model.api.elements.structures.StyleRule;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
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
 * Dialog for adding or editing map rule.
 */
public class MapRuleBuilder extends BaseTitleAreaDialog {

	public static final String DLG_TITLE_NEW = Messages.getString("MapRuleBuilder.DialogTitle.New"); //$NON-NLS-1$
	public static final String DLG_TITLE_EDIT = Messages.getString("MapRuleBuilder.DialogTitle.Edit"); //$NON-NLS-1$

	protected static final Logger logger = Logger.getLogger(MapRuleBuilder.class.getName());

	protected static final String[] EMPTY_ARRAY = new String[] {};

	private static final String NULL_STRING = null;

	protected ExpressionProvider expressionProvider;

	protected String bindingName = null;

	protected ReportElementHandle currentItem = null;

	protected List<Control> compositeList = new ArrayList<Control>();

	protected List<Expression> valueList = new ArrayList<Expression>();

	private MapRuleHandle handle;

	private MapHandleProvider provider;

	private int handleCount;

	protected Combo expression, operator;

	private Text display;

	// private ExpressionValue value1, value2;
	protected Composite valueListComposite;
	protected Combo addExpressionValue;
	protected Button addBtn, editBtn, delBtn, delAllBtn;
	protected Table table;
	protected TableViewer tableViewer;

	protected int valueVisible;

	private Combo expressionValue1, expressionValue2;

	private Label andLable;

	protected Composite dummy1, dummy2;

	private Text resourceKeytext;

	private Button btnBrowse;

	private Button btnReset;

	protected DesignElementHandle designHandle;

	protected static final String VALUE_OF_THIS_DATA_ITEM = Messages
			.getString("HighlightRuleBuilderDialog.choice.ValueOfThisDataItem"); //$NON-NLS-1$

	private static final String CHOICE_SELECT_VALUE = Messages.getString("ExpressionValueCellEditor.selectValueAction"); //$NON-NLS-1$

	private ParamBindingHandle[] bindingParams = null;

	protected List<ComputedColumnHandle> columnList;

	/**
	 * Usable operators for building map rule conditions.
	 */
	private static final String[][] OPERATOR;

	static {
		IChoiceSet chset = ChoiceSetFactory.getStructChoiceSet(MapRule.STRUCTURE_NAME, MapRule.OPERATOR_MEMBER);
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
		if (DesignChoiceConstants.MAP_OPERATOR_ANY.equals(operatorValue)
				|| DesignChoiceConstants.MAP_OPERATOR_FALSE.equals(operatorValue)
				|| DesignChoiceConstants.MAP_OPERATOR_TRUE.equals(operatorValue)
				|| DesignChoiceConstants.MAP_OPERATOR_NULL.equals(operatorValue)
				|| DesignChoiceConstants.MAP_OPERATOR_NOT_NULL.equals(operatorValue)) {
			return 0;
		} else if (DesignChoiceConstants.MAP_OPERATOR_LT.equals(operatorValue)
				|| DesignChoiceConstants.MAP_OPERATOR_LE.equals(operatorValue)
				|| DesignChoiceConstants.MAP_OPERATOR_EQ.equals(operatorValue)
				|| DesignChoiceConstants.MAP_OPERATOR_NE.equals(operatorValue)
				|| DesignChoiceConstants.MAP_OPERATOR_GE.equals(operatorValue)
				|| DesignChoiceConstants.MAP_OPERATOR_GT.equals(operatorValue)
				|| DesignChoiceConstants.MAP_OPERATOR_LIKE.equals(operatorValue)) {
			return 1;
		} else if (DesignChoiceConstants.MAP_OPERATOR_BETWEEN.equals(operatorValue)
				|| DesignChoiceConstants.MAP_OPERATOR_NOT_BETWEEN.equals(operatorValue)) {
			return 2;
		} else if (DesignChoiceConstants.MAP_OPERATOR_IN.equals(operatorValue)
				|| DesignChoiceConstants.MAP_OPERATOR_NOT_IN.equals(operatorValue)) {
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
	static int getIndexForOperatorValue(String value) {
		for (int i = 0; i < OPERATOR.length; i++) {
			if (OPERATOR[i][1].equals(value)) {
				return i;
			}
		}

		return 0;
	}

	/**
	 * Default constructor.
	 * 
	 * @param parentShell Parent Shell
	 * @param title       Window Title
	 */
	public MapRuleBuilder(Shell parentShell, String title, MapHandleProvider provider) {
		super(parentShell);
		this.title = title;
		this.provider = provider;
	}

	private String[] getDataSetColumns() {
		if (columnList.isEmpty()) {
			return EMPTY_ARRAY;
		}
		String[] values = new String[columnList.size()];
		for (int i = 0; i < columnList.size(); i++) {
			values[i] = columnList.get(i).getName();
		}
		return values;
	}

	protected Object getResultSetColumn(String name) {
		if (columnList.isEmpty()) {
			return null;
		}
		for (int i = 0; i < columnList.size(); i++) {
			ComputedColumnHandle column = columnList.get(i);
			if (column.getName().equals(name)) {
				return column;
			}
		}
		return null;
	}

	protected SelectionListener expSelListener = new SelectionAdapter() {

		public void widgetSelected(SelectionEvent e) {
			IExpressionConverter converter = ExpressionButtonUtil.getCurrentExpressionConverter(expression);
			if (converter != null) {
				String newValue = expression.getText();
				String value = null;
				if (expression.getText().equals(VALUE_OF_THIS_DATA_ITEM) && designHandle instanceof DataItemHandle) {
					if (designHandle.getContainer() instanceof ExtendedItemHandle) {
						value = ExpressionUtility
								.getDataExpression(((DataItemHandle) designHandle).getResultSetColumn(), converter);
					} else {
						value = ExpressionUtility
								.getColumnExpression(((DataItemHandle) designHandle).getResultSetColumn(), converter);
					}
				} else {
					value = ExpressionUtility.getExpression(getResultSetColumn(newValue), converter);
				}
				if (value != null)
					newValue = value;
				expression.setText(newValue);
			}
			updateButtons();
		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.ui.dialogs.BaseTitleAreaDialog#
	 * createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents(Composite parent) {
		Composite composite = (Composite) super.createContents(parent);

		setTitle(Messages.getString("MapRuleBuilderDialog.text.Title")); //$NON-NLS-1$

		UIUtil.bindHelp(parent, IHelpContextIds.INSERT_EDIT_MAP_RULE_DIALOG_ID);

		if (handle != null) {
			syncViewProperties();
		}

		updateButtons();

		return composite;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.TitleAreaDialog#createDialogArea(org.eclipse
	 * .swt.widgets.Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);

		Composite contents = new Composite(composite, SWT.NONE);
		contents.setLayout(new GridLayout());
		contents.setLayoutData(new GridData(GridData.FILL_BOTH));

		Label lb = new Label(contents, SWT.NONE);
		lb.setText(Messages.getString("MapRuleBuilderDialog.text.Condition")); //$NON-NLS-1$

		condition = new Composite(contents, SWT.NONE);
		GridData gdata = new GridData(GridData.FILL_HORIZONTAL);
		// gdata.heightHint = 180;
		condition.setLayoutData(gdata);
		GridLayout glayout = GridLayoutFactory.createFrom(new GridLayout()).numColumns(5).equalWidth(false).create();
		condition.setLayout(glayout);

		expression = new Combo(condition, SWT.NONE);
		gdata = new GridData();
		gdata.widthHint = 120;
		expression.setLayoutData(gdata);
		expression.setVisibleItemCount(30);
		expression.setItems(getDataSetColumns());
		fillExpression(expression);
		expression.addSelectionListener(expSelListener);
		expression.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				updateButtons();
			}
		});

		Listener listener = new Listener() {

			public void handleEvent(Event event) {
				updateButtons();
			}

		};

		ExpressionButtonUtil.createExpressionButton(condition, expression, getExpressionProvider(), designHandle,
				listener);

		operator = new Combo(condition, SWT.READ_ONLY);
		operator.setVisibleItemCount(30);
		for (int i = 0; i < OPERATOR.length; i++) {
			operator.add(OPERATOR[i][0]);
		}

		create2ValueComposite(condition);

		operator.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				operatorChange();
			}
		});

		lb = new Label(contents, SWT.NONE);
		lb.setText(Messages.getString("MapRuleBuilderDialog.text.Display")); //$NON-NLS-1$

		Composite format = new Composite(contents, SWT.NONE);
		format.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		glayout = new GridLayout();
		format.setLayout(glayout);

		display = new Text(format, SWT.BORDER);
		gdata = new GridData();
		gdata.widthHint = 300;
		display.setLayoutData(gdata);

		createResourceKeyArea(contents);

		lb = new Label(contents, SWT.SEPARATOR | SWT.HORIZONTAL);
		lb.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		return composite;
	}

	protected ExpressionProvider getExpressionProvider() {
		if (expressionProvider == null) {
			expressionProvider = new ExpressionProvider(designHandle);
			expressionProvider.addFilter(new ExpressionFilter() {

				public boolean select(Object parentElement, Object element) {
					if (ExpressionFilter.CATEGORY.equals(parentElement)
							&& ExpressionProvider.CURRENT_CUBE.equals(element)) {
						return false;
					}
					return true;
				}

			});
		}
		return expressionProvider;
	}

	private int create2ValueComposite(Composite condition) {
		if (expressionValue1 != null && !expressionValue1.isDisposed()) {
			return 0;
		}
		if (valueListComposite != null && !valueListComposite.isDisposed()) {
			valueListComposite.dispose();
			valueListComposite = null;
		}

		GridData expgd = new GridData(GridData.FILL_HORIZONTAL);
		expgd.widthHint = 120;

		expressionValue1 = createExpressionValue(condition);
		expressionValue1.add(CHOICE_SELECT_VALUE);
		expressionValue1.setLayoutData(expgd);

		dummy1 = createDummy(condition, 3);

		andLable = new Label(condition, SWT.NONE);
		andLable.setText(Messages.getString("MapRuleBuilder.text.AND")); //$NON-NLS-1$
		andLable.setEnabled(false);

		dummy2 = createDummy(condition, 3);

		expressionValue2 = createExpressionValue(condition);
		expressionValue2.add(CHOICE_SELECT_VALUE);
		expressionValue2.setLayoutData(expgd);

		if (operator.getItemCount() > 0 && operator.getSelectionIndex() == -1) {
			operator.setText(getNameForOperator(DesignChoiceConstants.MAP_OPERATOR_EQ));
			operatorChange();
		}
		condition.getParent().layout(true, true);
		if (getButtonBar() != null)
			layout();
		return 1;
	}

	private void layout() {
		GridData gd = (GridData) condition.getLayoutData();
		Point size = condition.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		if (gd.widthHint < size.x)
			gd.widthHint = size.x;
		if (gd.heightHint < size.y)
			gd.heightHint = size.y;
		condition.setLayoutData(gd);
		condition.getShell().layout();
		if (getButtonBar() != null)
			condition.getShell().pack();
	}

	private Combo createExpressionValue(Composite parent) {
		final Combo expressionValue = new Combo(parent, SWT.BORDER);
		expressionValue.addVerifyListener(new VerifyListener() {

			public void verifyText(VerifyEvent e) {
				String selection = e.text;
				if (expressionValue.indexOf(selection) == -1) {
					e.doit = true;
					return;
				}

				if (selection.equals(CHOICE_SELECT_VALUE)) {
					e.doit = false;
				} else {
					e.doit = true;
				}
			}
		});
		expressionValue.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				if (expressionValue.getSelectionIndex() == -1)
					return;
				String selection = expressionValue.getItem(expressionValue.getSelectionIndex());
				if (selection.equals(CHOICE_SELECT_VALUE)) {
					String value = getSelectionValue(expressionValue);
					if (value != null)
						expressionValue.setText(value);
				}
			}
		});
		expressionValue.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				updateButtons();
			}
		});

		createComplexExpressionButton(parent, expressionValue);

		return expressionValue;
	}

	private Combo createMultiExpressionValue(Composite parent) {
		final Combo expressionValue = new Combo(parent, SWT.BORDER);
		expressionValue.addVerifyListener(new VerifyListener() {

			public void verifyText(VerifyEvent e) {
				String selection = e.text;
				if (expressionValue.indexOf(selection) == -1) {
					e.doit = true;
					return;
				}

				if (selection.equals(CHOICE_SELECT_VALUE)) {
					e.doit = false;
				} else {
					e.doit = true;
				}
			}
		});
		expressionValue.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				if (expressionValue.getSelectionIndex() == -1)
					return;
				String selection = expressionValue.getItem(expressionValue.getSelectionIndex());
				if (selection.equals(CHOICE_SELECT_VALUE)) {
					selectMultiValues(expressionValue);
				}
			}
		});
		expressionValue.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				updateButtons();
			}
		});

		createComplexExpressionButton(parent, expressionValue);

		return expressionValue;
	}

	private void createComplexExpressionButton(Composite parent, final Combo combo) {
		Listener listener = new Listener() {

			public void handleEvent(Event event) {
				updateButtons();
			}

		};
		ExpressionButtonUtil.createExpressionButton(parent, combo, getExpressionProvider(), designHandle, listener);
	}

	protected String getColumnName(Object obj) {
		if (obj instanceof DataSetViewData) {
			return ((DataSetViewData) obj).getName();
		} else if (obj instanceof ComputedColumnHandle) {
			return ((ComputedColumnHandle) obj).getName();
		} else if (obj instanceof ResultSetColumnHandle) {
			return ((ResultSetColumnHandle) obj).getColumnName();
		} else
			return ""; //$NON-NLS-1$
	}

	protected String getSelectionValue(Combo combo) {
		String retValue = null;

		bindingName = getExpressionBindingName();

		if (bindingName == null && expression.getText().trim().length() > 0)
			bindingName = expression.getText().trim();

		if (bindingName != null) {
			try {
				List selectValueList = getSelectValueList();

				if (selectValueList == null || selectValueList.size() == 0) {
					MessageDialog.openInformation(null, Messages.getString("SelectValueDialog.selectValue"), //$NON-NLS-1$
							Messages.getString("SelectValueDialog.messages.info.selectVauleUnavailable")); //$NON-NLS-1$

				} else {
					SelectValueDialog dialog = new SelectValueDialog(
							PlatformUI.getWorkbench().getDisplay().getActiveShell(),
							Messages.getString("ExpressionValueCellEditor.title")); //$NON-NLS-1$
					dialog.setSelectedValueList(selectValueList);
					if (bindingParams != null) {
						dialog.setBindingParams(bindingParams);
					}

					if (dialog.open() == IDialogConstants.OK_ID) {
						IExpressionConverter converter = ExpressionButtonUtil.getCurrentExpressionConverter(combo);
						retValue = dialog.getSelectedExprValue(converter);
					}
				}

			} catch (Exception ex) {
				MessageDialog.openError(null, Messages.getString("SelectValueDialog.selectValue"), //$NON-NLS-1$
						Messages.getString("SelectValueDialog.messages.error.selectVauleUnavailable") //$NON-NLS-1$
								+ "\n" //$NON-NLS-1$
								+ ex.getMessage());
			}

		} else {
			MessageDialog.openInformation(null, Messages.getString("SelectValueDialog.selectValue"), //$NON-NLS-1$
					Messages.getString("SelectValueDialog.messages.info.selectVauleUnavailable")); //$NON-NLS-1$
		}

		return retValue;
	}

	private String getExpressionBindingName() {
		for (Iterator iter = columnList.iterator(); iter.hasNext();) {
			String columnName = getColumnName(iter.next());

			if (designHandle instanceof DataItemHandle) {
				if (designHandle.getContainer() instanceof ExtendedItemHandle) {
					if (ExpressionUtility
							.getDataExpression(columnName,
									ExpressionUtility.getExpressionConverter(
											ExpressionButtonUtil.getExpression(expression).getType()))
							.equals(expression.getText())) {
						return columnName;
					}
				} else {
					if (ExpressionUtility
							.getColumnExpression(columnName,
									ExpressionUtility.getExpressionConverter(
											ExpressionButtonUtil.getExpression(expression).getType()))
							.equals(expression.getText())) {
						return columnName;
					}
				}

			} else {
				Expression expr = ExpressionButtonUtil.getExpression(expression);
				if (expr != null) {
					String exprType = expr.getType();
					IExpressionConverter converter = ExpressionUtility.getExpressionConverter(exprType);
					String tempExpression = ExpressionUtility.getColumnExpression(columnName, converter);
					if (DEUtil.isBindingCube(designHandle)) {
						tempExpression = ExpressionUtility.getDataExpression(columnName, converter);
					}
					if (expression.getText().equals(tempExpression)) {
						return columnName;
					}
				}
			}
		}

		return null;
	}

	protected void delTableValue() {
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

	protected void editTableValue() {
		IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
		if (selection.getFirstElement() != null && selection.getFirstElement() instanceof Expression) {
			Expression initValue = (Expression) selection.getFirstElement();

			ExpressionEditor editor = new ExpressionEditor(Messages.getString("ExpressionEditor.Title")); //$NON-NLS-1$
			editor.setExpression(initValue);
			editor.setInput(designHandle, getExpressionProvider(), false);

			if (editor.open() == OK) {
				Expression result = editor.getExpression();
				if (result == null || result.getStringExpression() == null
						|| result.getStringExpression().length() == 0) {
					MessageDialog.openInformation(getShell(), Messages.getString("MapRuleBuilder.MsgDlg.Title"), //$NON-NLS-1$
							Messages.getString("MapRuleBuilder.MsgDlg.Msg")); //$NON-NLS-1$
					return;
				}
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
		GridData gdata = new GridData(GridData.FILL_BOTH);
		gdata.horizontalSpan = 4;
		valueListComposite.setLayoutData(gdata);
		GridLayout layout = new GridLayout();
		layout.numColumns = 4;
		valueListComposite.setLayout(layout);

		Group group = new Group(valueListComposite, SWT.NONE);
		GridData data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 3;
		data.horizontalIndent = 0;
		data.grabExcessHorizontalSpace = true;
		group.setLayoutData(data);
		layout = new GridLayout();
		layout.numColumns = 5;
		group.setLayout(layout);

		new Label(group, SWT.NONE).setText(Messages.getString("MapRuleBuilder.label.value")); //$NON-NLS-1$

		GridData expgd = new GridData();
		expgd.widthHint = 100;

		addExpressionValue = createMultiExpressionValue(group);
		addExpressionValue.setLayoutData(expgd);
		addExpressionValue.add(CHOICE_SELECT_VALUE);

		addBtn = new Button(group, SWT.PUSH);
		addBtn.setText(Messages.getString("MapRuleBuilder.button.add")); //$NON-NLS-1$
		addBtn.setToolTipText(Messages.getString("MapRuleBuilder.button.add.tooltip")); //$NON-NLS-1$
		setButtonLayoutData(addBtn);

		addBtn.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				Expression value = ExpressionButtonUtil.getExpression(addExpressionValue);
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
		data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 5;
		data.heightHint = 75;
		table.setLayoutData(data);

		table.setHeaderVisible(false);
		table.setLinesVisible(true);
		TableColumn column;
		int i;
		String[] columNames = new String[] { Messages.getString("MapRuleBuilder.list.item1"), //$NON-NLS-1$
		};
		int[] columLength = new int[] { 288 };
		for (i = 0; i < columNames.length; i++) {
			column = new TableColumn(table, SWT.NONE, i);
			column.setText(columNames[i]);
			column.setWidth(columLength[i]);
		}
		table.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				checkEditDelButtonStatus();
			}
		});

		table.addKeyListener(new KeyAdapter() {

			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.DEL) {
					delTableValue();
				}

			}
		});
		table.addMouseListener(new MouseAdapter() {

			public void mouseDoubleClick(MouseEvent e) {
				editTableValue();
			}
		});

		tableViewer = new TableViewer(table);
		tableViewer.setUseHashlookup(true);
		tableViewer.setColumnProperties(columNames);
		tableViewer.setLabelProvider(tableLableProvier);
		tableViewer.setContentProvider(tableContentProvider);

		Composite rightPart = new Composite(valueListComposite, SWT.NONE);
		data = new GridData(GridData.HORIZONTAL_ALIGN_END);
		rightPart.setLayoutData(data);
		layout = new GridLayout();
		layout.makeColumnsEqualWidth = true;
		rightPart.setLayout(layout);

		editBtn = new Button(rightPart, SWT.PUSH);
		editBtn.setText(Messages.getString("MapRuleBuilder.button.edit")); //$NON-NLS-1$
		editBtn.setToolTipText(Messages.getString("MapRuleBuilder.button.edit.tooltip")); //$NON-NLS-1$
		setButtonLayoutData(editBtn);
		GridData gd = (GridData) editBtn.getLayoutData();
		gd.grabExcessVerticalSpace = true;
		gd.verticalAlignment = SWT.END;
		editBtn.setLayoutData(gd);
		editBtn.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				editTableValue();
			}

		});

		delBtn = new Button(rightPart, SWT.PUSH);
		delBtn.setText(Messages.getString("MapRuleBuilder.button.delete")); //$NON-NLS-1$
		delBtn.setToolTipText(Messages.getString("MapRuleBuilder.button.delete.tooltip")); //$NON-NLS-1$
		setButtonLayoutData(delBtn);
		delBtn.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				delTableValue();
			}

		});

		delAllBtn = new Button(rightPart, SWT.PUSH);
		delAllBtn.setText(Messages.getString("MapRuleBuilder.button.deleteall")); //$NON-NLS-1$
		delAllBtn.setToolTipText(Messages.getString("MapRuleBuilder.button.deleteall.tooltip")); //$NON-NLS-1$
		setButtonLayoutData(delAllBtn);
		gd = (GridData) delAllBtn.getLayoutData();
		gd.grabExcessVerticalSpace = true;
		gd.verticalAlignment = SWT.BEGINNING;
		delAllBtn.setLayoutData(gd);
		delAllBtn.addSelectionListener(new SelectionAdapter() {

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

			public void modifyText(ModifyEvent e) {
				checkAddButtonStatus();
				updateButtons();
			}
		});

		parent.getParent().layout(true, true);
		if (getButtonBar() != null)
			layout();
		return 1;

	}

	protected ITableLabelProvider tableLableProvier = new ITableLabelProvider() {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			if (columnIndex == 0) {
				if (element instanceof Expression) {
					return ((Expression) element).getStringExpression();
				}
				return element.toString();
			}
			return ""; //$NON-NLS-1$
		}

		public void addListener(ILabelProviderListener listener) {
		}

		public void dispose() {
		}

		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		public void removeListener(ILabelProviderListener listener) {
		}
	};

	protected IStructuredContentProvider tableContentProvider = new IStructuredContentProvider() {

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		public Object[] getElements(Object inputElement) {
			if (inputElement == null) {
				return new Object[0];
			} else if (inputElement instanceof List) {
				return ((List) inputElement).toArray();
			}
			return null;
		}
	};

	protected void checkAddButtonStatus() {
		if (addExpressionValue != null) {
			String value = addExpressionValue.getText();
			if (value == null || value.length() == 0 || value.trim().length() == 0) {
				addBtn.setEnabled(false);
				return;
			}
			// if ( value != null )
			// {
			// value = value.trim( );
			// }
			if (valueList.indexOf(ExpressionButtonUtil.getExpression(addExpressionValue)) < 0) {
				addBtn.setEnabled(true);
			} else {
				addBtn.setEnabled(false);
			}
		}
	}

	protected void checkEditDelButtonStatus() {
		if (tableViewer == null) {
			return;
		}
		boolean enabled = (tableViewer.getSelection() == null) ? false : true;
		if (enabled == true && tableViewer.getSelection() instanceof StructuredSelection) {
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

	private Composite condition;

	protected List getSelectValueList() throws BirtException {
		List selectValueList = new ArrayList();
		ReportItemHandle reportItem = DEUtil.getBindingHolder(currentItem);
		// if used extend to get value,then no fetch again
		boolean hasFetched = false;
		if (bindingName != null && reportItem != null) {
			if (reportItem instanceof ExtendedItemHandle) {
				Object obj = ElementAdapterManager.getAdapters(reportItem, IUseCubeQueryList.class);

				if (obj instanceof Object[]) {
					Object arrays[] = (Object[]) obj;
					if (arrays.length == 1 && arrays[0] != null) {
						List valueList = ((IUseCubeQueryList) arrays[0]).getQueryList(
								ExpressionUtility.getDataExpression(bindingName,
										ExpressionUtility.getExpressionConverter(ExpressionType.JAVASCRIPT)),
								(ExtendedItemHandle) reportItem);
						selectValueList.addAll(valueList);
						hasFetched = true;
					}
				}

			}

			if (!hasFetched && selectValueList.size() == 0) {
				selectValueList = SelectValueFetcher.getSelectValueFromBinding(
						ExpressionButtonUtil.getExpression(expression), reportItem.getDataSet(),
						DEUtil.getVisiableColumnBindingsList(designHandle).iterator(),
						DEUtil.getGroups(designHandle).iterator(), false);
			}

		} else {
			ExceptionHandler.openErrorMessageBox(Messages.getString("SelectValueDialog.errorRetrievinglist"), //$NON-NLS-1$
					Messages.getString("SelectValueDialog.noExpressionSet")); //$NON-NLS-1$
		}
		return selectValueList;
	}

	private Composite createResourceKeyArea(Composite parent) {
		Composite resourceKeyArea = new Composite(parent, SWT.NONE);
		resourceKeyArea.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout glayout = new GridLayout(4, false);
		resourceKeyArea.setLayout(glayout);

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		resourceKeyArea.setLayoutData(gd);

		Label lb = new Label(resourceKeyArea, SWT.NONE);
		lb.setText(Messages.getString("MapRuleBuilder.Button.ResourceKey")); //$NON-NLS-1$
		resourceKeytext = new Text(resourceKeyArea, SWT.BORDER | SWT.READ_ONLY);
		resourceKeytext.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		btnBrowse = new Button(resourceKeyArea, SWT.PUSH);
		btnBrowse.setLayoutData(new GridData());
		btnBrowse.setText(Messages.getString("MapRuleBuilder.Button.Browse")); //$NON-NLS-1$
		btnBrowse.setToolTipText(Messages.getString("MapRuleBuilder.Button.Browse.Tooltip")); //$NON-NLS-1$
		btnBrowse.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				handleBrowserSelectedEvent();
			}
		});

		btnReset = new Button(resourceKeyArea, SWT.PUSH);
		btnReset.setLayoutData(new GridData());
		btnReset.setText(Messages.getString("MapRuleBuilder.Button.Reset")); //$NON-NLS-1$

		btnReset.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				handleSelectedEvent(null);
			};

		});
		checkResourceKey();

		Label noteLabel = new Label(parent, SWT.NONE | SWT.WRAP);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 4;
		gd.widthHint = 350;
		noteLabel.setLayoutData(gd);
		noteLabel.setText(Messages.getString("I18nPage.text.Note")); //$NON-NLS-1$
		return resourceKeyArea;
	}

	protected void handleBrowserSelectedEvent() {
		ResourceEditDialog dlg = new ResourceEditDialog(btnBrowse.getShell(),
				Messages.getString("ResourceKeyDescriptor.title.SelectKey")); //$NON-NLS-1$

		dlg.setResourceURLs(getResourceURLs());

		if (dlg.open() == Window.OK) {
			handleSelectedEvent((String) dlg.getResult());
		}
	}

	private void handleSelectedEvent(String newValue) {
		if ("".equals(newValue))//$NON-NLS-1$
		{
			newValue = null;
		}

		resourceKeytext.setText(DEUtil.resolveNull(newValue));

	}

	private Composite createDummy(Composite parent, int colSpan) {
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
	public void updateHandle(MapRuleHandle handle, int handleCount) {
		this.handle = handle;
		this.handleCount = handleCount;
	}

	/*
	 * Set design handle for the Map Rule builder
	 */
	public void setDesignHandle(DesignElementHandle handle) {
		this.designHandle = handle;
		initializeProviderType();
		inilializeColumnList(handle);
		initializeParamterBinding(handle);
	}

	public void setReportElement(ReportElementHandle reportItem) {
		currentItem = reportItem;
	}

	protected void inilializeColumnList(DesignElementHandle handle) {
		columnList = DEUtil.getVisiableColumnBindingsList(handle);
	}

	private void initializeParamterBinding(DesignElementHandle handle) {
		if (handle instanceof ReportItemHandle) {
			ReportItemHandle inputHandle = (ReportItemHandle) handle;
			List list = new ArrayList();
			for (Iterator iterator = inputHandle.paramBindingsIterator(); iterator.hasNext();) {
				ParamBindingHandle paramBindingHandle = (ParamBindingHandle) iterator.next();
				list.add(paramBindingHandle);
			}
			bindingParams = new ParamBindingHandle[list.size()];
			list.toArray(bindingParams);
		}
	}

	public MapRuleHandle getHandle() {
		return handle;
	}

	private void fillExpression(Combo control) {
		if ((designHandle instanceof DataItemHandle)
				&& (((DataItemHandle) designHandle).getResultSetColumn() != null)) {
			control.add(VALUE_OF_THIS_DATA_ITEM, 0);
		}

		if (control.getItemCount() == 0) {
			control.add(DEUtil.resolveNull(null));
			control.select(control.getItemCount() - 1);
		}

	}

	/**
	 * Refreshes the OK button state.
	 * 
	 */
	protected void updateButtons() {
		enableInput(isExpressionOK());
		if (getButton(IDialogConstants.OK_ID) != null) {
			getButton(IDialogConstants.OK_ID).setEnabled(isConditionOK());
		}

	}

	private void enableInput(boolean val) {
		operator.setEnabled(val);
		if (valueVisible != 3) {
			if (expressionValue1 != null && (!expressionValue1.isDisposed())) {
				expressionValue1.setEnabled(val);
				ExpressionButtonUtil.getExpressionButton(expressionValue1).setEnabled(expressionValue1.isEnabled());
			}
			if (expressionValue2 != null && (!expressionValue2.isDisposed())) {
				expressionValue2.setEnabled(val);
				ExpressionButtonUtil.getExpressionButton(expressionValue2).setEnabled(expressionValue2.isEnabled());
			}
			if (andLable != null && (!andLable.isDisposed())) {
				andLable.setEnabled(val);
			}
		} else {
			setControlEnable(valueListComposite, val);
			if (val) {
				checkAddButtonStatus();
				checkEditDelButtonStatus();
			}
		}
		if (display != null && (!display.isDisposed())) {
			display.setEnabled(val);
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
	private boolean isExpressionOK() {
		if (expression == null) {
			return false;
		}

		if (expression.getText() == null || expression.getText().length() == 0) {
			return false;
		}

		return true;
	}

	/**
	 * Gets if the condition is available.
	 */
	private boolean isConditionOK() {
		if (expression == null) {
			return false;
		}

		if (!isExpressionOK()) {
			return false;
		}

		return checkValues();
	}

	/**
	 * Gets if the values of the condition is(are) available.
	 */
	private boolean checkValues() {
		if (valueVisible == 3) {
			if (valueList.size() <= 0) {
				return false;
			} else {
				return true;
			}
		} else {
			if (expressionValue1.getVisible()) {
				if (expressionValue1.getText() == null || expressionValue1.getText().length() == 0) {
					return false;
				}
			}

			if (expressionValue2.getVisible()) {
				if (expressionValue2.getText() == null || expressionValue2.getText().length() == 0) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * SYNC the control value according to the handle.
	 */
	private void syncViewProperties() {
		ExpressionButtonUtil.initExpressionButtonControl(expression, handle, StyleRule.TEST_EXPR_MEMBER);

		operator.select(getIndexForOperatorValue(handle.getOperator()));

		String value = getValueForOperator(operator.getText());
		valueVisible = determineValueVisible(value);

		if (valueVisible == 3) {
			createValueListComposite(operator.getParent());
			if (handle != null) {
				valueList = new ArrayList();
				if (handle.getValue1ExpressionList().getListValue() != null
						&& handle.getValue1ExpressionList().getListValue().size() > 0)
					valueList.addAll(handle.getValue1ExpressionList().getListValue());
			}

			tableViewer.setInput(valueList);
		} else {
			create2ValueComposite(operator.getParent());
			if (handle != null) {
				if (handle.getValue1ExpressionList().getListValue() != null
						&& handle.getValue1ExpressionList().getListValue().size() > 0)
					ExpressionButtonUtil.initExpressionButtonControl(expressionValue1,
							handle.getValue1ExpressionList().getListValue().get(0));
				ExpressionButtonUtil.initExpressionButtonControl(expressionValue2, handle, StyleRule.VALUE2_MEMBER);
			}

		}

		valueVisible = determineValueVisible(handle.getOperator());

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
			;
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

		display.setText(DEUtil.resolveNull(handle.getDisplay()));
		resourceKeytext.setText(DEUtil.resolveNull(handle.getDisplayKey()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	protected void okPressed() {
		try {
			valueVisible = determineValueVisible(DEUtil.resolveNull(getValueForOperator(operator.getText())));

			if (handle == null) {
				MapRule rule = StructureFactory.createMapRule();

				rule.setProperty(MapRule.OPERATOR_MEMBER, DEUtil.resolveNull(getValueForOperator(operator.getText())));

				if (valueVisible == 3) {
					rule.setValue1(valueList);
					rule.setValue2(""); //$NON-NLS-1$
				} else {
					assert (!expressionValue1.isDisposed());
					assert (!expressionValue2.isDisposed());
					if (expressionValue1.getVisible()) {
						List valueList = new ArrayList();
						valueList.add(ExpressionButtonUtil.getExpression(expressionValue1));
						rule.setValue1(valueList);
					} else {
						rule.setValue1(NULL_STRING);
					}

					if (expressionValue2.getVisible()) {
						ExpressionButtonUtil.saveExpressionButtonControl(expressionValue2, rule, MapRule.VALUE2_MEMBER);
					} else {
						rule.setValue2(NULL_STRING);
					}
				}

				rule.setDisplay(DEUtil.resolveNull(display.getText()));
				rule.setDisplayKey(DEUtil.resolveNull(resourceKeytext.getText()));

				ExpressionButtonUtil.saveExpressionButtonControl(expression, rule, StyleRule.TEST_EXPR_MEMBER);

				handle = provider.doAddItem(rule, handleCount);
			} else {
				handle.setOperator(DEUtil.resolveNull(getValueForOperator(operator.getText())));

				if (valueVisible != 3) {
					assert (!expressionValue1.isDisposed());
					assert (!expressionValue2.isDisposed());
					if (expressionValue1.getVisible()) {
						List valueList = new ArrayList();
						valueList.add(ExpressionButtonUtil.getExpression(expressionValue1));
						handle.setValue1(valueList);
					} else {
						handle.setValue1(NULL_STRING);
					}

					if (expressionValue2.getVisible()) {
						ExpressionButtonUtil.saveExpressionButtonControl(expressionValue2, handle,
								MapRule.VALUE2_MEMBER);
					} else {
						handle.setValue2(NULL_STRING);
					}
				} else {
					handle.setValue1(valueList);
					handle.setValue2(""); //$NON-NLS-1$
				}

				handle.setDisplay(DEUtil.resolveNull(display.getText()));
				handle.setDisplayKey(DEUtil.resolveNull(resourceKeytext.getText()));

				ExpressionButtonUtil.saveExpressionButtonControl(expression, handle, StyleRule.TEST_EXPR_MEMBER);

			}
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}

		super.okPressed();
	}

	private URL getResourceURL() {
		return SessionHandleAdapter.getInstance().getReportDesignHandle().findResource(getBaseName(),
				IResourceLocator.MESSAGE_FILE);
	}

	private String getBaseName() {
		return SessionHandleAdapter.getInstance().getReportDesignHandle().getIncludeResource();
	}

	private void checkResourceKey() {
		checkResourceKey(null);
	}

	private void checkResourceKey(MapRuleHandle handle) {
		resourceKeytext.setEnabled(true);
		btnBrowse.setEnabled(true);
		btnReset.setEnabled(true);

		if (getAvailableResourceUrls() == null || getAvailableResourceUrls().length < 1) {
			btnBrowse.setEnabled(false);
		}
		if (handle != null) {
			resourceKeytext.setText(DEUtil.resolveNull(handle.getDisplayKey()));
		}
	}

	private void initializeProviderType() {
		if (designHandle instanceof DataItemHandle) {
			DataItemHandle dataItem = (DataItemHandle) designHandle;
			if (dataItem.getContainer() instanceof ExtendedItemHandle) {
				provider.setExpressionType(MapHandleProvider.EXPRESSION_TYPE_DATA);
			} else {
				provider.setExpressionType(MapHandleProvider.EXPRESSION_TYPE_ROW);
			}
		}
	}

	protected void selectMultiValues(Combo combo) {
		String[] retValue = null;

		bindingName = getExpressionBindingName();

		if (bindingName == null && expression.getText().trim().length() > 0)
			bindingName = expression.getText().trim();

		if (bindingName != null) {
			try {
				List selectValueList = getSelectValueList();
				SelectValueDialog dialog = new SelectValueDialog(
						PlatformUI.getWorkbench().getDisplay().getActiveShell(),
						Messages.getString("ExpressionValueCellEditor.title")); //$NON-NLS-1$

				dialog.setMultipleSelection(true);

				dialog.setSelectedValueList(selectValueList);
				if (bindingParams != null) {
					dialog.setBindingParams(bindingParams);
				}
				if (dialog.open() == IDialogConstants.OK_ID) {
					retValue = dialog.getSelectedExprValues(ExpressionButtonUtil.getCurrentExpressionConverter(combo));
				}
			} catch (Exception ex) {
				MessageDialog.openError(null, Messages.getString("SelectValueDialog.selectValue"), //$NON-NLS-1$
						Messages.getString("SelectValueDialog.messages.error.selectVauleUnavailable") //$NON-NLS-1$
								+ "\n" //$NON-NLS-1$
								+ ex.getMessage());
			}
		} else {
			MessageDialog.openInformation(null, Messages.getString("SelectValueDialog.selectValue"), //$NON-NLS-1$
					Messages.getString("SelectValueDialog.messages.info.selectVauleUnavailable")); //$NON-NLS-1$
		}

		if (retValue != null) {
			addBtn.setEnabled(false);

			if (retValue.length == 1) {
				combo.setText(DEUtil.resolveNull(retValue[0]));
			} else if (retValue.length > 1) {
				combo.setText(""); //$NON-NLS-1$
			}

			boolean change = false;
			for (int i = 0; i < retValue.length; i++) {
				Expression expression = new Expression(retValue[i],
						ExpressionButtonUtil.getExpression(combo).getType());
				if (valueList.indexOf(expression) < 0) {
					valueList.add(expression);
					change = true;
				}
			}
			if (change) {
				tableViewer.refresh();
				updateButtons();
				combo.setFocus();
			}
		}
	}

	protected void operatorChange() {
		if (operator.getSelectionIndex() == -1)
			return;
		valueVisible = determineValueVisible(DEUtil.resolveNull(getValueForOperator(operator.getText())));

		if (valueVisible == 3) {
			int ret = createValueListComposite(operator.getParent());
			if (ret != 0) {
				valueList = new ArrayList();
				if (handle != null) {
					if (handle.getValue1ExpressionList().getListValue() != null
							&& handle.getValue1ExpressionList().getListValue().size() > 0)
						valueList.addAll(handle.getValue1ExpressionList().getListValue());
				}
				tableViewer.setInput(valueList);
			}
		} else {
			int ret = create2ValueComposite(operator.getParent());
			if (ret != 0 && handle != null) {
				if (handle.getValue1ExpressionList().getListValue() != null
						&& handle.getValue1ExpressionList().getListValue().size() > 0)
					ExpressionButtonUtil.initExpressionButtonControl(expressionValue1,
							handle.getValue1ExpressionList().getListValue().get(0));
				ExpressionButtonUtil.initExpressionButtonControl(expressionValue2, handle,
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
		}
		updateButtons();
	}

	public int open() {
		if (getShell() == null) {
			// create the window
			create();
		}
		updateButtons();
		return super.open();
	}

	protected void constrainShellSize() {
		// limit the shell size to the display size
		Rectangle bounds = getShell().getBounds();
		Point location = getInitialLocation(new Point(bounds.width, bounds.height));
		bounds = new Rectangle(location.x, location.y, bounds.width, bounds.height);
		Rectangle constrained = getConstrainedShellBounds(bounds);
		getShell().setBounds(constrained);
	}

	public void setExpressionProvider(ExpressionProvider expressionProvider) {
		this.expressionProvider = expressionProvider;
	}

	private URL[] getAvailableResourceUrls() {
		List<URL> urls = new ArrayList<URL>();
		String[] baseNames = getBaseNames();
		if (baseNames == null)
			return urls.toArray(new URL[0]);
		else {
			for (int i = 0; i < baseNames.length; i++) {
				URL url = SessionHandleAdapter.getInstance().getReportDesignHandle().findResource(baseNames[i],
						IResourceLocator.MESSAGE_FILE);
				if (url != null)
					urls.add(url);
			}
			return urls.toArray(new URL[0]);
		}
	}

	private String[] getBaseNames() {
		List<String> resources = SessionHandleAdapter.getInstance().getReportDesignHandle().getIncludeResources();
		if (resources == null)
			return null;
		else
			return resources.toArray(new String[0]);
	}

	private URL[] getResourceURLs() {
		String[] baseNames = getBaseNames();
		if (baseNames == null)
			return null;
		else {
			URL[] urls = new URL[baseNames.length];
			for (int i = 0; i < baseNames.length; i++) {
				urls[i] = SessionHandleAdapter.getInstance().getReportDesignHandle().findResource(baseNames[i],
						IResourceLocator.MESSAGE_FILE);
			}
			return urls;
		}
	}
}
