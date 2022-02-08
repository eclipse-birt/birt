/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.designer.data.ui.dataset.DataSetViewData;
import org.eclipse.birt.report.designer.data.ui.util.DataSetProvider;
import org.eclipse.birt.report.designer.data.ui.util.SelectValueFetcher;
import org.eclipse.birt.report.designer.internal.ui.expressions.IExpressionConverter;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.MultiValueCombo;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.ValueCombo;
import org.eclipse.birt.report.designer.internal.ui.util.DataUtil;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionButtonUtil;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionUtility;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.expressions.ExpressionFilter;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.AlphabeticallyComparator;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularHierarchyHandle;
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
 * Dialog for adding or editing map rule.
 */

public class FilterConditionBuilder extends BaseTitleAreaDialog {

	private static String[] actions = new String[] { Messages.getString("ExpressionValueCellEditor.selectValueAction"), //$NON-NLS-1$
			Messages.getString("ExpressionValueCellEditor.buildExpressionAction"), //$NON-NLS-1$
	};

	public static final String DLG_MESSAGE_EDIT = Messages.getString("FilterConditionBuilder.DialogMessage.Edit"); //$NON-NLS-1$
	public static final String DLG_MESSAGE_NEW = Messages.getString("FilterConditionBuilder.DialogMessage.New"); //$NON-NLS-1$
	public static final String DLG_TITLE_EDIT = Messages.getString("FilterConditionBuilder.DialogTitle.Edit"); //$NON-NLS-1$
	public static final String DLG_TITLE_NEW = Messages.getString("FilterConditionBuilder.DialogTitle.New"); //$NON-NLS-1$

	/**
	 * Constant, represents empty String array.
	 */
	protected static final String[] EMPTY = new String[0];

	protected static final String[] EMPTY_ARRAY = new String[] {};

	protected static Logger logger = Logger.getLogger(FilterConditionBuilder.class.getName());
	/**
	 * Usable operators for building map rule conditions.
	 */
	protected static final String[][] OPERATOR;
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

	protected Button addBtn, editBtn, delBtn, delAllBtn;

	protected MultiValueCombo addExpressionValue;

	protected Label andLable;

	protected transient String bindingName;

	private ParamBindingHandle[] bindingParams = null;

	protected IChoiceSet choiceSet;

	protected List columnList;
	protected transient ReportElementHandle currentItem = null;

	protected DataSetHandle dataset;

	protected DataSetHandle dataSetHandle;

	protected DesignElementHandle designHandle;

	protected Composite dummy1, dummy2;

	protected Combo expression, operator;

	protected IExpressionProvider expressionProvider;
	protected ValueCombo expressionValue1, expressionValue2;

	private boolean isUsedForEditGroup = false;

	protected ValueCombo.ISelection expValueAction = new ValueCombo.ISelection() {

		public String doSelection(String input) {
			String retValue = null;

			ExpressionBuilder dialog = new ExpressionBuilder(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
					input);

			setProviderForExpressionBuilder(dialog);

			if (dialog.open() == IDialogConstants.OK_ID) {
				retValue = dialog.getResult();
			}
			return retValue;
		}
	};

	protected FilterConditionHandle filterCondition;

	protected Label label1, label2;

	protected MultiValueCombo.ISelection mAddExpValueAction = new MultiValueCombo.ISelection() {

		public void doAfterSelection(MultiValueCombo combo) {
			mAddSelValueAction.doAfterSelection(combo);
		}

		public String[] doSelection(String input) {
			String[] retValue = null;

			ExpressionBuilder dialog = new ExpressionBuilder(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
					input);

			setProviderForExpressionBuilder(dialog);

			if (dialog.open() == IDialogConstants.OK_ID) {
				if (dialog.getResult().length() != 0) {
					retValue = new String[] { dialog.getResult() };
				}

			}

			return retValue;
		}

	};

	protected MultiValueCombo.ISelection mAddSelValueAction = new MultiValueCombo.ISelection() {

		public void doAfterSelection(MultiValueCombo combo) {
			addBtn.setEnabled(false);

			if (addExpressionValue.getSelStrings().length == 1) {
				addExpressionValue.setText(DEUtil.resolveNull(addExpressionValue.getSelStrings()[0]));
			} else if (addExpressionValue.getSelStrings().length > 1) {
				addExpressionValue.setText(""); //$NON-NLS-1$
			}

			boolean change = false;
			for (int i = 0; i < addExpressionValue.getSelStrings().length; i++) {
				if (valueList.indexOf(DEUtil.resolveNull(addExpressionValue.getSelStrings()[i])) < 0) {
					valueList.add(DEUtil.resolveNull(addExpressionValue.getSelStrings()[i]));
					change = true;
				}
			}
			if (change) {
				tableViewer.refresh();
				updateButtons();
				addExpressionValue.setFocus();
			}
		}

		public String[] doSelection(String input) {

			String[] retValue = null;

			if (dataSetHandle != null || designHandle instanceof TabularCubeHandle
					|| designHandle instanceof TabularHierarchyHandle) {
				DataSetHandle dataSet;
				if (dataSetHandle != null) {
					dataSet = dataSetHandle;
				} else {
					if (designHandle instanceof TabularCubeHandle)
						dataSet = ((TabularCubeHandle) designHandle).getDataSet();
					else {
						dataSet = ((TabularHierarchyHandle) designHandle).getDataSet();
						if (dataSet == null && ((TabularHierarchyHandle) designHandle).getLevelCount() > 0) {
							dataSet = ((TabularCubeHandle) ((TabularHierarchyHandle) designHandle).getContainer()
									.getContainer()).getDataSet();
						}
					}
				}
				try {
					List selectValueList = dataSetHandle != null
							? SelectValueFetcher.getSelectValueList(ExpressionButtonUtil.getExpression(expression),
									dataSet, false)
							: SelectValueFetcher.getSelectValueFromBinding(
									ExpressionButtonUtil.getExpression(expression), dataSet,
									DEUtil.getVisiableColumnBindingsList(designHandle).iterator(),
									DEUtil.getGroups(designHandle).iterator(), true);
					SelectValueDialog dialog = new SelectValueDialog(
							PlatformUI.getWorkbench().getDisplay().getActiveShell(),
							Messages.getString("ExpressionValueCellEditor.title")); //$NON-NLS-1$
					dialog.setSelectedValueList(selectValueList);

					dialog.setMultipleSelection(true);

					if (dialog.open() == IDialogConstants.OK_ID) {
						retValue = dialog.getSelectedExprValues();

					}

				} catch (BirtException e1) {
					MessageDialog.openError(null, Messages.getString("SelectValueDialog.selectValue"), //$NON-NLS-1$
							Messages.getString("SelectValueDialog.messages.error.selectVauleUnavailable") //$NON-NLS-1$
									+ "\n" //$NON-NLS-1$
									+ e1.getMessage());
				}
			} else {
				for (Iterator iter = columnList.iterator(); iter.hasNext();) {
					Object obj = iter.next();
					String columnName = Messages.getString("ExpressionValueCellEditor.title"); //$NON-NLS-1$
					if (obj instanceof ComputedColumnHandle) {
						columnName = ((ComputedColumnHandle) (obj)).getName();
					} else if (obj instanceof ResultSetColumnHandle) {
						columnName = ((ResultSetColumnHandle) (obj)).getColumnName();
					}

					Expression expr = ExpressionButtonUtil.getExpression(expression);
					if (expr != null) {
						String exprType = expr.getType();
						IExpressionConverter converter = ExpressionUtility.getExpressionConverter(exprType);
						if (expression.getText().equals(ExpressionUtility.getColumnExpression(columnName, converter))) {
							bindingName = columnName;
							break;
						}
					}
				}

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
							retValue = dialog.getSelectedExprValues();
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
			}

			return retValue;
		}
	};

	protected final String NULL_STRING = null;

	protected SelectionListener operatorSelection = new SelectionAdapter() {

		public void widgetSelected(SelectionEvent e) {
			operatorChange();
		}
	};

	protected transient String[] popupItems = null;

	private transient boolean refreshItems = true;

	protected ValueCombo.ISelection selectValueAction = new ValueCombo.ISelection() {

		public String doSelection(String input) {
			String retValue = null;

			if (dataSetHandle != null || designHandle instanceof TabularCubeHandle
					|| designHandle instanceof TabularHierarchyHandle) {

				DataSetHandle dataSet = null;
				if (dataSetHandle != null) {
					dataSet = dataSetHandle;
				} else {
					if (designHandle instanceof TabularCubeHandle) {
						dataSet = ((TabularCubeHandle) designHandle).getDataSet();
					} else {
						dataSet = ((TabularHierarchyHandle) designHandle).getDataSet();
					}
				}
				try {
					List selectValueList = dataSetHandle != null
							? SelectValueFetcher.getSelectValueList(ExpressionButtonUtil.getExpression(expression),
									dataSet, false)
							: SelectValueFetcher.getSelectValueFromBinding(
									ExpressionButtonUtil.getExpression(expression), dataSet,
									DEUtil.getVisiableColumnBindingsList(designHandle).iterator(),
									DEUtil.getGroups(designHandle).iterator(), true);
					SelectValueDialog dialog = new SelectValueDialog(
							PlatformUI.getWorkbench().getDisplay().getActiveShell(),
							Messages.getString("ExpressionValueCellEditor.title")); //$NON-NLS-1$
					dialog.setSelectedValueList(selectValueList);
					if (dialog.open() == IDialogConstants.OK_ID) {

						retValue = dialog.getSelectedExprValue();
					}

				} catch (BirtException e1) {
					MessageDialog.openError(null, Messages.getString("SelectValueDialog.selectValue"), //$NON-NLS-1$
							Messages.getString("SelectValueDialog.messages.error.selectVauleUnavailable") //$NON-NLS-1$
									+ "\n" //$NON-NLS-1$
									+ e1.getMessage());
				}
			} else {
				for (Iterator iter = columnList.iterator(); iter.hasNext();) {
					String columnName = getColumnName(iter.next());
					Expression expr = ExpressionButtonUtil.getExpression(expression);
					if (expr != null) {
						String exprType = expr.getType();
						IExpressionConverter converter = ExpressionUtility.getExpressionConverter(exprType);
						if (expression.getText().equals(ExpressionUtility.getColumnExpression(columnName, converter))) {
							bindingName = columnName;
							break;
						}
					}
				}

				if (bindingName == null && expression.getText().trim().length() > 0)
					bindingName = expression.getText().trim();

				if (bindingName != null) {
					try {
						List selectValueList = getSelectValueList();
						SelectValueDialog dialog = new SelectValueDialog(
								PlatformUI.getWorkbench().getDisplay().getActiveShell(),
								Messages.getString("ExpressionValueCellEditor.title")); //$NON-NLS-1$
						dialog.setSelectedValueList(selectValueList);
						if (bindingParams != null) {
							dialog.setBindingParams(bindingParams);
						}

						if (dialog.open() == IDialogConstants.OK_ID) {
							retValue = dialog.getSelectedExprValue();
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
			}
			return retValue;
		}
	};

	protected List selValueList = new ArrayList();

	protected Table table;

	protected IStructuredContentProvider tableContentProvider = new IStructuredContentProvider() {

		public void dispose() {
		}

		public Object[] getElements(Object inputElement) {
			if (inputElement == null) {
				return new Object[0];
			} else if (inputElement instanceof List) {
				return ((List) inputElement).toArray();
			}
			return null;
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	};

	protected ITableLabelProvider tableLableProvier = new ITableLabelProvider() {

		public void addListener(ILabelProviderListener listener) {
		}

		public void dispose() {
		}

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

		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		public void removeListener(ILabelProviderListener listener) {
		}
	};

	protected TableViewer tableViewer;

	protected String title, message;

	protected List valueList = new ArrayList();

	protected Composite valueListComposite;

	protected int valueVisible;

	private Button updateAggrButton;

	private boolean showUpdateAggregationButton = true;

	/**
	 * @param parentShell
	 * @param title
	 */
	public FilterConditionBuilder(Shell parentShell, String title, String message) {
		super(parentShell);
		this.title = title;
		this.message = message;
	}

	/**
	 * @param title
	 */
	public FilterConditionBuilder(String title, String message) {
		this(UIUtil.getDefaultShell(), title, message);
	}

	protected void checkAddButtonStatus() {
		if (addExpressionValue != null) {
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
		} else {
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

		expressionValue1 = new ValueCombo(condition, SWT.NONE);
		expressionValue1.setLayoutData(expgd);
		expressionValue1.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				updateButtons();
			}
		});

		// expressionValue1.addListener( SWT.Verify, expValueVerifyListener );
		// expressionValue1.addListener( SWT.Selection,
		// expValueSelectionListener );
		refreshList();
		expressionValue1.setItems(popupItems);
		expressionValue1.addSelectionListener(0, selectValueAction);
		expressionValue1.addSelectionListener(1, expValueAction);

		dummy1 = createDummy(condition, 3);

		andLable = new Label(condition, SWT.NONE);
		andLable.setText(Messages.getString("FilterConditionBuilder.text.AND")); //$NON-NLS-1$
		andLable.setEnabled(false);
		// andLable.setVisible( false );

		dummy2 = createDummy(condition, 3);

		expressionValue2 = new ValueCombo(condition, SWT.NONE);
		expressionValue2.setLayoutData(expgd);
		expressionValue2.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				updateButtons();
			}
		});

		// expressionValue2.addListener( SWT.Verify, expValueVerifyListener );
		// expressionValue2.addListener( SWT.Selection,
		// expValueSelectionListener );
		expressionValue2.setItems(popupItems);

		expressionValue2.addSelectionListener(0, selectValueAction);
		expressionValue2.addSelectionListener(1, expValueAction);

		// expressionValue2.setVisible( false );

		if (operator.getItemCount() > 0 && operator.getSelectionIndex() == -1) {
			operator.select(0);
			operatorChange();
		}
		condition.getParent().layout(true, true);
		if (getButtonBar() != null)
			condition.getShell().pack();
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createContents(org.eclipse.swt.widgets
	 * .Composite)
	 */
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

	protected Composite createDummy(Composite parent, int colSpan) {
		Composite dummy = new Composite(parent, SWT.NONE);
		GridData gdata = new GridData();
		gdata.widthHint = 22;
		gdata.horizontalSpan = colSpan;
		gdata.heightHint = 10;
		dummy.setLayoutData(gdata);

		return dummy;
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
		gd.heightHint = 220;
		condition.setLayoutData(gd);
		glayout = new GridLayout(4, false);
		condition.setLayout(glayout);

		expression = new Combo(condition, SWT.NONE);
		GridData gdata = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gdata.widthHint = 100;
		gdata.minimumWidth = 100;
		expression.setLayoutData(gdata);
		expression.setVisibleItemCount(30);
		expression.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				IExpressionConverter converter = ExpressionButtonUtil.getCurrentExpressionConverter(expression);
				if (converter != null) {
					if (expression.getSelectionIndex() >= 0) {
						String newValue = expression.getItem(expression.getSelectionIndex());
						String value = ExpressionUtility.getFilterExpression(
								dataSetHandle != null ? dataSetHandle : designHandle, newValue, converter);
						if (value != null)
							newValue = value;
						expression.setText(newValue);
					}
				}
				updateButtons();
			}
		});
		expression.setItems(getDataSetColumns());
		if (expression.getItemCount() == 0) {
			expression.add(DEUtil.resolveNull(null));
		}
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

		ExpressionButtonUtil.createExpressionButton(condition, expression, getExpressionProvider(),
				dataSetHandle != null ? dataSetHandle : designHandle, listener);

		operator = new Combo(condition, SWT.READ_ONLY);
		operator.setVisibleItemCount(30);
		for (int i = 0; i < OPERATOR.length; i++) {
			operator.add(OPERATOR[i][0]);
		}
		operator.addSelectionListener(operatorSelection);

		create2ValueComposite(condition);

		if (filterCondition != null) {
			syncViewProperties();
		}

		if (showUpdateAggregationButton) {
			updateAggrButton = new Button(innerParent, SWT.CHECK);
			updateAggrButton.setText(Messages.getString("FilterConditionBuilder.Button.UpdateAggregation")); //$NON-NLS-1$
			gd = new GridData();
			gd.verticalIndent = 5;
			updateAggrButton.setLayoutData(gd);
			updateAggrButton.setSelection(true);
			if (filterCondition != null) {
				updateAggrButton.setSelection(filterCondition.updateAggregation());
			}
		}

		lb = new Label(innerParent, SWT.SEPARATOR | SWT.HORIZONTAL);
		lb.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}

	private int createValueListComposite(Composite parent) {

		if (valueListComposite != null && !valueListComposite.isDisposed()) {
			return 0;
		}

		if (expressionValue1 != null && !expressionValue1.isDisposed()) {
			expressionValue1.dispose();
			expressionValue1 = null;

			dummy1.dispose();
			dummy1 = null;

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
		layout.numColumns = 4;
		group.setLayout(layout);

		new Label(group, SWT.NONE).setText(Messages.getString("FilterConditionBuilder.label.value")); //$NON-NLS-1$

		GridData expgd = new GridData();
		expgd.widthHint = 100;

		addExpressionValue = new MultiValueCombo(group, SWT.NONE);
		addExpressionValue.setLayoutData(expgd);

		addBtn = new Button(group, SWT.PUSH);
		addBtn.setText(Messages.getString("FilterConditionBuilder.button.add")); //$NON-NLS-1$
		addBtn.setToolTipText(Messages.getString("FilterConditionBuilder.button.add.tooltip")); //$NON-NLS-1$
		setButtonLayoutData(addBtn);

		addBtn.addSelectionListener(new SelectionAdapter() {

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
		data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 4;
		table.setLayoutData(data);

		table.setHeaderVisible(false);
		table.setLinesVisible(true);
		TableColumn column;
		int i;
		String[] columNames = new String[] { Messages.getString("FilterConditionBuilder.list.item1"), //$NON-NLS-1$
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

		table.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.DEL) {
					delTableValue();
				}

			}

			public void keyReleased(KeyEvent e) {
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
		editBtn.setText(Messages.getString("FilterConditionBuilder.button.edit")); //$NON-NLS-1$
		editBtn.setToolTipText(Messages.getString("FilterConditionBuilder.button.edit.tooltip")); //$NON-NLS-1$
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
		delBtn.setText(Messages.getString("FilterConditionBuilder.button.delete")); //$NON-NLS-1$
		delBtn.setToolTipText(Messages.getString("FilterConditionBuilder.button.delete.tooltip")); //$NON-NLS-1$
		setButtonLayoutData(delBtn);
		delBtn.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				delTableValue();
			}

		});

		delAllBtn = new Button(rightPart, SWT.PUSH);
		delAllBtn.setText(Messages.getString("FilterConditionBuilder.button.deleteall")); //$NON-NLS-1$
		delAllBtn.setToolTipText(Messages.getString("FilterConditionBuilder.button.deleteall.tooltip")); //$NON-NLS-1$
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

		// addExpressionValue.addListener( SWT.Verify, expValueVerifyListener );
		// addExpressionValue.addListener( SWT.Selection,
		// expValueSelectionListener );

		refreshList();
		addExpressionValue.setItems(popupItems);
		addExpressionValue.addSelectionListener(0, mAddSelValueAction);
		addExpressionValue.addSelectionListener(1, mAddExpValueAction);

		parent.getParent().layout(true, true);
		if (getButtonBar() != null)
			parent.getShell().pack();
		return 1;

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
		if (selection.getFirstElement() != null && selection.getFirstElement() instanceof String) {
			String initValue = (String) selection.getFirstElement();

			ExpressionBuilder expressionBuilder = new ExpressionBuilder(getShell(), initValue);

			setProviderForExpressionBuilder(expressionBuilder);

			if (expressionBuilder.open() == OK) {
				String result = DEUtil.resolveNull(expressionBuilder.getResult());
				if (result.length() == 0) {
					MessageDialog.openInformation(getShell(), Messages.getString("MapRuleBuilderDialog.MsgDlg.Title"), //$NON-NLS-1$
							Messages.getString("MapRuleBuilderDialog.MsgDlg.Msg")); //$NON-NLS-1$
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

	protected void editValue(Control control) {
		String initValue = null;
		if (control instanceof Text) {
			initValue = ((Text) control).getText();
		} else if (control instanceof Combo) {
			initValue = ((Combo) control).getText();
		}
		ExpressionBuilder expressionBuilder = new ExpressionBuilder(getShell(), initValue);

		setProviderForExpressionBuilder(expressionBuilder);

		if (expressionBuilder.open() == OK) {
			String result = DEUtil.resolveNull(expressionBuilder.getResult());
			if (control instanceof Text) {
				((Text) control).setText(result);
			} else if (control instanceof Combo) {
				((Combo) control).setText(result);
			}
		}
		updateButtons();
	}

	protected void enableInput(boolean val) {
		operator.setEnabled(val);
		if (valueVisible != 3) {
			if (expressionValue1 != null)
				expressionValue1.setEnabled(val);
			if (expressionValue2 != null)
				expressionValue2.setEnabled(val);
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

	protected String[] getDataSetColumns() {
		if (columnList.isEmpty()) {
			return EMPTY;
		}
		String[] values = new String[columnList.size()];
		for (int i = 0; i < columnList.size(); i++) {
			values[i] = getColumnName(columnList.get(i));
		}
		return values;
	}

	protected DataSetHandle getDataSetFromHandle(DesignElementHandle handle) {
		DataSetHandle tempDataset = null;
		if (handle instanceof DataSetHandle) {
			tempDataset = (DataSetHandle) handle;
		} else if (handle instanceof TabularCubeHandle || handle instanceof TabularHierarchyHandle) {
			if (handle instanceof TabularCubeHandle)
				tempDataset = ((TabularCubeHandle) handle).getDataSet();
			else {
				tempDataset = ((TabularHierarchyHandle) handle).getDataSet();
				if (tempDataset == null && ((TabularHierarchyHandle) handle).getLevelCount() > 0) {
					tempDataset = ((TabularCubeHandle) ((TabularHierarchyHandle) handle).getContainer().getContainer())
							.getDataSet();
				}
			}
		} else {
			if (DEUtil.getBindingRoot(handle) != null)
				tempDataset = DEUtil.getBindingRoot(handle).getDataSet();
		}

		return tempDataset;
	}

	protected IExpressionProvider getExpressionProvider() {
		IExpressionProvider provider = null;
		if (dataSetHandle != null) {
			if (expressionProvider == null) {

				provider = new ExpressionProvider(dataSetHandle);
			} else {
				provider = expressionProvider;
			}
		} else if (designHandle != null) {
			if (expressionProvider == null) {
				if (designHandle instanceof TabularCubeHandle || designHandle instanceof TabularHierarchyHandle) {
					provider = new BindingExpressionProvider(designHandle, null);
				} else {
					provider = new ExpressionProvider(designHandle);
					((ExpressionProvider) provider).addFilter(new ExpressionFilter() {

						public boolean select(Object parentElement, Object element) {
							if (ExpressionFilter.CATEGORY.equals(parentElement)
									&& ExpressionProvider.DATASETS.equals(element)) {
								return false;
							}
							return true;
						}

					});
				}
			} else {
				provider = expressionProvider;
			}
		}
		return provider;
	}

	/*
	 * Return the hanle of Map Rule builder
	 */
	public FilterConditionHandle getInputHandle() {
		return filterCondition;
	}

	protected Object getResultSetColumn(String name) {
		if (columnList.isEmpty()) {
			return null;
		}
		for (int i = 0; i < columnList.size(); i++) {
			if (getColumnName(columnList.get(i)).equals(name)) {
				return columnList.get(i);
			}
		}
		return null;
	}

	protected List getSelectValueList() throws BirtException {
		List selectValueList = new ArrayList();
		ReportItemHandle reportItem = DEUtil.getBindingHolder(currentItem);
		if (bindingName != null && reportItem != null) {
			selectValueList = dataSetHandle != null
					? SelectValueFetcher.getSelectValueList(ExpressionButtonUtil.getExpression(expression),
							reportItem.getDataSet(), false)
					: SelectValueFetcher.getSelectValueFromBinding(ExpressionButtonUtil.getExpression(expression),
							reportItem.getDataSet(), DEUtil.getVisiableColumnBindingsList(designHandle).iterator(),
//							DEUtil.getGroups( designHandle )
//											.iterator( ),
							getGroupIterator(), true);
		} else {
			ExceptionHandler.openErrorMessageBox(Messages.getString("SelectValueDialog.errorRetrievinglist"), //$NON-NLS-1$
					Messages.getString("SelectValueDialog.noExpressionSet")); //$NON-NLS-1$
		}
		return selectValueList;
	}

	private Iterator<GroupHandle> getGroupIterator() {
		// if add a new group ,and at same time to add filter,then remove current added
		// group
		// because pass this new group to DTE,will cause Exception;
		// if edit a group ,then do nothing
		List<GroupHandle> groupList = UIUtil.getGroups(designHandle);
		if (groupList == null) {
			return null;
		}
		if (!isUsedForEditGroup) {
			groupList.remove(designHandle);
		}
		return groupList.iterator();
	}

	/**
	 * Gets if the condition is available.
	 */
	protected boolean isConditionOK() {
		if (expression == null) {
			return false;
		}

		if (!isExpressionOK()) {
			return false;
		}

		return checkValues();
	}

	/**
	 * Gets if the expression field is not empty.
	 */
	protected boolean isExpressionOK() {
		if (expression == null) {
			return false;
		}

		if (expression.getText() == null || expression.getText().length() == 0) {
			return false;
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	protected void okPressed() {
		try {
			if (filterCondition == null) {
				FilterCondition filter = StructureFactory.createFilterCond();

				filter.setProperty(FilterCondition.OPERATOR_MEMBER,
						DEUtil.resolveNull(getValueForOperator(operator.getText())));
				if (valueVisible == 3) {
					filter.setValue1(valueList);
					filter.setValue2(""); //$NON-NLS-1$
				} else {
					assert (!expressionValue1.isDisposed());
					assert (!expressionValue2.isDisposed());
					if (expressionValue1.getVisible()) {
						filter.setValue1(DEUtil.resolveNull(expressionValue1.getText()));
					} else {
						filter.setValue1(NULL_STRING);
					}

					if (expressionValue2.getVisible()) {
						filter.setValue2(DEUtil.resolveNull(expressionValue2.getText()));
					}
				}

				// set test expression for new map rule
				ExpressionButtonUtil.saveExpressionButtonControl(expression, filter, FilterCondition.EXPR_MEMBER);

				if (showUpdateAggregationButton && updateAggrButton != null)
					filter.setUpdateAggregation(updateAggrButton.getSelection());

				if (dataSetHandle != null) {
					PropertyHandle propertyHandle = dataSetHandle.getPropertyHandle(ListingHandle.FILTER_PROP);
					propertyHandle.addItem(filter);
				} else {
					PropertyHandle propertyHandle = designHandle.getPropertyHandle(ListingHandle.FILTER_PROP);
					propertyHandle.addItem(filter);
				}
			} else {
				filterCondition.setOperator(DEUtil.resolveNull(getValueForOperator(operator.getText())));
				if (valueVisible == 3) {
					filterCondition.setValue1(valueList);
					filterCondition.setValue2(NULL_STRING);
				} else {
					assert (!expressionValue1.isDisposed());
					assert (!expressionValue2.isDisposed());
					if (expressionValue1.getVisible()) {
						filterCondition.setValue1(DEUtil.resolveNull(expressionValue1.getText()));
					} else {
						filterCondition.setValue1(NULL_STRING);
					}

					if (expressionValue2.getVisible()) {
						filterCondition.setValue2(DEUtil.resolveNull(expressionValue2.getText()));
					} else {
						filterCondition.setValue2(NULL_STRING);
					}
				}
				ExpressionButtonUtil.saveExpressionButtonControl(expression, filterCondition,
						FilterCondition.EXPR_MEMBER);
				if (showUpdateAggregationButton && updateAggrButton != null)
					filterCondition.setUpdateAggregation(updateAggrButton.getSelection());
			}
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}

		super.okPressed();
	}

	public int open() {
		if (getShell() == null) {
			// create the window
			create();
		}
		updateButtons();
		return super.open();
	}

	public void showUpdateAggregationButton(boolean show) {
		showUpdateAggregationButton = show;
	}

	protected void operatorChange() {
		String value = getValueForOperator(operator.getText());

		valueVisible = determineValueVisible(value);

		if (valueVisible == 3) {
			int ret = createValueListComposite(operator.getParent());
			if (ret != 0) {
				if (filterCondition != null) {
					valueList = new ArrayList(filterCondition.getValue1List());
				}

				tableViewer.setInput(valueList);
			}
		} else {
			int ret = create2ValueComposite(operator.getParent());
			if (ret != 0 && filterCondition != null) {
				expressionValue1.setText(DEUtil.resolveNull(filterCondition.getValue1()));
				expressionValue2.setText(DEUtil.resolveNull(filterCondition.getValue2()));
			}

		}

		if (valueVisible == 0) {
			expressionValue1.setVisible(false);
			expressionValue2.setVisible(false);
			andLable.setVisible(false);
		} else if (valueVisible == 1) {
			expressionValue1.setVisible(true);
			expressionValue2.setVisible(false);
			andLable.setVisible(false);
		} else if (valueVisible == 2) {
			expressionValue1.setVisible(true);
			expressionValue2.setVisible(true);
			andLable.setVisible(true);
			andLable.setEnabled(true);
		}
		updateButtons();
	}

	private void refreshList() {
		if (refreshItems) {
			ArrayList finalItems = new ArrayList(10);
			for (int n = 0; n < actions.length; n++) {
				finalItems.add(actions[n]);
			}

			if (currentItem != null) {
				// addParamterItems( finalItems );
			}
			popupItems = (String[]) finalItems.toArray(EMPTY_ARRAY);
		}
		refreshItems = false;
	}

	/**
	 * @param bindingName The selectValueExpression to set.
	 */
	public void setBindingName(String bindingName) {
		this.bindingName = bindingName;
	}

	/**
	 * 
	 */
	public void setBindingParams(ParamBindingHandle[] params) {
		this.bindingParams = params;
	}

	protected void setColumnList(DesignElementHandle handle) {
		if (handle instanceof DataSetHandle) {
			try {
				columnList = Arrays
						.asList(DataSetProvider.getCurrentInstance().getColumns((DataSetHandle) handle, false));
			} catch (BirtException e) {
				// do nothing now
			}
		} else if (handle instanceof TabularCubeHandle || handle instanceof TabularHierarchyHandle) {
			try {
				if (dataset != null)
					columnList = DataUtil.getColumnList(dataset);
				else
					columnList = Collections.EMPTY_LIST;
			} catch (SemanticException e) {
				ExceptionHandler.handle(e);
			}
		} else {
			columnList = DEUtil.getVisiableColumnBindingsList(handle);
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

	/*
	 * If set dataset handle , then the priority of the dataset is first.
	 */
	public void setDataSetHandle(DataSetHandle handle) {
		this.dataSetHandle = handle;
		this.dataset = this.getDataSetFromHandle(handle);
		setColumnList(this.dataSetHandle);
	}

	public void setDataSetHandle(DataSetHandle handle, IExpressionProvider provider) {
		setDataSetHandle(handle);
		this.expressionProvider = provider;
	}

	/*
	 * Set design handle for the Map Rule builder
	 */
	public void setDesignHandle(DesignElementHandle handle) {
		this.designHandle = handle;
		this.dataset = this.getDataSetFromHandle(handle);
		setColumnList(this.designHandle);
	}

	public void setDesignHandle(DesignElementHandle handle, IExpressionProvider provider) {
		setDesignHandle(handle);
		this.expressionProvider = provider;
	}

	private void setExpression() {
		ExpressionButtonUtil.initExpressionButtonControl(expression, filterCondition, FilterCondition.EXPR_MEMBER);
	}

	/**
	 * Sets the model input.
	 * 
	 * @param sortKey
	 */
	public void setInput(Object inputHandle) {
		if (inputHandle instanceof FilterConditionHandle) {
			this.filterCondition = (FilterConditionHandle) inputHandle;
		} else {
			this.filterCondition = null;
		}

	}

	protected void setProviderForExpressionBuilder(ExpressionBuilder expressionBuilder) {
		if (dataSetHandle != null) {
			if (expressionProvider == null) {

				expressionBuilder.setExpressionProvier(new ExpressionProvider(dataSetHandle));
			} else {
				expressionBuilder.setExpressionProvier(expressionProvider);
			}
		} else if (designHandle != null) {
			if (expressionProvider == null) {
				if (designHandle instanceof TabularCubeHandle || designHandle instanceof TabularHierarchyHandle) {
					expressionBuilder.setExpressionProvier(new BindingExpressionProvider(designHandle, null));
				} else {
					expressionBuilder.setExpressionProvier(new ExpressionProvider(designHandle));
				}
			} else {
				expressionBuilder.setExpressionProvier(expressionProvider);
			}

		}
	}

	public void setReportElement(ReportElementHandle reportItem) {
		currentItem = reportItem;
	}

	/**
	 * SYNC the control value according to the handle.
	 */
	protected void syncViewProperties() {

		filterCondition.getProperty(FilterCondition.EXPR_MEMBER);

		setExpression();

		operator.select(getIndexForOperatorValue(filterCondition.getOperator()));
		valueVisible = determineValueVisible(filterCondition.getOperator());

		if (valueVisible == 3) {
			createValueListComposite(operator.getParent());
			valueList = new ArrayList(filterCondition.getValue1List());
			tableViewer.setInput(valueList);
		} else {
			create2ValueComposite(operator.getParent());
			expressionValue1.setText(DEUtil.resolveNull(filterCondition.getValue1()));
			expressionValue2.setText(DEUtil.resolveNull(filterCondition.getValue2()));
		}

		if (valueVisible == 0) {
			expressionValue1.setVisible(false);
			expressionValue2.setVisible(false);
			andLable.setVisible(false);
		} else if (valueVisible == 1) {
			expressionValue1.setVisible(true);
			expressionValue2.setVisible(false);

			andLable.setVisible(false);
		} else if (valueVisible == 2) {
			expressionValue1.setVisible(true);
			expressionValue2.setVisible(true);
			;
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

	/*
	 * Update handle for the Map Rule builder
	 */
	public void updateHandle(FilterConditionHandle handle, int handleCount) {
		this.filterCondition = handle;
	}

	public void setUsedForEditGroup(boolean isUsedForEditGroup) {
		this.isUsedForEditGroup = isUsedForEditGroup;
	}

}
