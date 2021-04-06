/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.aggregation.AggregationManager;
import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.data.engine.api.aggregation.IParameterDefn;
import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.data.adapter.api.DataAdapterUtil;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.data.ui.dataset.DataSetUIUtil;
import org.eclipse.birt.report.designer.data.ui.util.DataUtil;
import org.eclipse.birt.report.designer.internal.ui.dialogs.expression.ExpressionButton;
import org.eclipse.birt.report.designer.internal.ui.extension.ExtendedDataModelUIAdapterHelper;
import org.eclipse.birt.report.designer.internal.ui.extension.IExtendedDataModelUIAdapter;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.CLabel;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionButtonUtil;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.util.WidgetUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.dialogs.BindingExpressionProvider;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.AlphabeticallyComparator;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.AggregationArgumentHandle;
import org.eclipse.birt.report.model.api.CachedMetaDataHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionHandle;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ListGroupHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.AggregationArgument;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.ResultSetColumn;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * 
 */

public class BindingDialogHelper extends AbstractBindingDialogHelper {

	protected static final String NAME = Messages.getString("BindingDialogHelper.text.Name"); //$NON-NLS-1$
	protected static final String DATA_TYPE = Messages.getString("BindingDialogHelper.text.DataType"); //$NON-NLS-1$
	protected static final String FUNCTION = Messages.getString("BindingDialogHelper.text.Function"); //$NON-NLS-1$
	protected static final String DATA_FIELD = Messages.getString("BindingDialogHelper.text.DataField"); //$NON-NLS-1$
	protected static final String FILTER_CONDITION = Messages.getString("BindingDialogHelper.text.Filter"); //$NON-NLS-1$
	protected static final String AGGREGATE_ON = Messages.getString("BindingDialogHelper.text.AggOn"); //$NON-NLS-1$
	protected static final String TABLE = Messages.getString("BindingDialogHelper.text.Table"); //$NON-NLS-1$
	protected static final String LIST = Messages.getString("BindingDialogHelper.text.List"); //$NON-NLS-1$
	protected static final String GRID = Messages.getString("BindingDialogHelper.text.Grid"); //$NON-NLS-1$
	protected static final String ALL = Messages.getString("CrosstabBindingDialogHelper.AggOn.All"); //$NON-NLS-1$
	protected static final String GROUP = Messages.getString("BindingDialogHelper.text.Group"); //$NON-NLS-1$
	protected static final String EXPRESSION = Messages.getString("BindingDialogHelper.text.Expression"); //$NON-NLS-1$
	protected static final String DISPLAY_NAME = Messages.getString("BindingDialogHelper.text.displayName"); //$NON-NLS-1$
	protected static final String ALLOW_EXPORT_LABEL = Messages.getString("BindingDialogHelper.text.allowExport"); //$NON-NLS-1$
	protected static final String ALLOW_EXPORT_BUTTON = Messages
			.getString("BindingDialogHelper.text.allowExport.button"); //$NON-NLS-1$
	protected static final String DISPLAY_NAME_ID = Messages.getString("BindingDialogHelper.text.displayNameID"); //$NON-NLS-1$

	protected static final String DEFAULT_ITEM_NAME = Messages.getString("BindingDialogHelper.bindingName.dataitem"); //$NON-NLS-1$
	protected static final String DEFAULT_AGGREGATION_NAME = Messages
			.getString("BindingDialogHelper.bindingName.aggregation"); //$NON-NLS-1$
	protected static final String NAME_LABEL = Messages.getString("BindingDialogHelper.error.text.Name"); //$NON-NLS-1$

	protected static final IChoiceSet DATA_TYPE_CHOICE_SET = DEUtil.getMetaDataDictionary()
			.getStructure(ComputedColumn.COMPUTED_COLUMN_STRUCT).getMember(ComputedColumn.DATA_TYPE_MEMBER)
			.getAllowedChoices();
	protected static final IChoice[] DATA_TYPE_CHOICES = DATA_TYPE_CHOICE_SET.getChoices(null);
	protected String[] dataTypes = ChoiceSetFactory.getDisplayNamefromChoiceSet(DATA_TYPE_CHOICE_SET);
	protected Button btnTable;

	protected Text txtName, txtFilter, txtExpression;
	protected Combo cmbType, cmbFunction, cmbGroup;
	protected Button btnGroup, btnDisplayNameID, btnRemoveDisplayNameID;
	protected Composite paramsComposite;

	protected Map<String, Control> paramsMap = new LinkedHashMap<String, Control>();
	protected Map<String, String[]> paramsValueMap = new HashMap<String, String[]>();

	protected Composite composite;
	protected Text txtDisplayName, txtDisplayNameID;
	private ComputedColumn newBinding;
	private CLabel messageLine;
	private Combo cmbName;
	private Label lbName, lbDisplayNameID;

	private boolean isCreate;
	protected boolean isRef;
	private Object container;

	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	public void createContent(Composite parent) {

		isCreate = getBinding() == null;
		isRef = getBindingHolder().getDataBindingType() == ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF;
		composite = parent;

		((GridLayout) composite.getLayout()).numColumns = 4;

		lbName = new Label(composite, SWT.NONE);
		lbName.setText(NAME);

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		gd.widthHint = 200;

		if (isRef) {
			cmbName = new Combo(composite, SWT.BORDER | SWT.READ_ONLY);
			cmbName.setLayoutData(gd);
			cmbName.setVisibleItemCount(30);
			cmbName.addSelectionListener(new SelectionAdapter() {

				public void widgetSelected(SelectionEvent e) {
					modifyDialogContent();

					String bindingName = cmbName.getItem(cmbName.getSelectionIndex());

					for (Iterator iterator = getBindingHolder().getDataBindingReference().getColumnBindings()
							.iterator(); iterator.hasNext();) {
						ComputedColumnHandle computedColumn = (ComputedColumnHandle) iterator.next();
						if (computedColumn.getName().equals(bindingName)) {
							setBinding(computedColumn);
							initDialog();
							return;
						}
					}
				}
			});
		} else {
			txtName = new Text(composite, SWT.BORDER);
			txtName.setLayoutData(gd);
			txtName.addModifyListener(new ModifyListener() {

				public void modifyText(ModifyEvent e) {
					modifyDialogContent();
					validate();
				}

			});
		}
		// WidgetUtil.createGridPlaceholder( composite, 1, false );

		lbDisplayNameID = new Label(composite, SWT.NONE);
		lbDisplayNameID.setText(DISPLAY_NAME_ID);
		lbDisplayNameID.addTraverseListener(new TraverseListener() {

			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_MNEMONIC && e.doit) {
					e.detail = SWT.TRAVERSE_NONE;
					if (btnDisplayNameID.isEnabled()) {
						openKeySelectionDialog();
					}
				}
			}
		});
		txtDisplayNameID = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
		txtDisplayNameID.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		btnDisplayNameID = new Button(composite, SWT.NONE);
		btnDisplayNameID
				.setEnabled(getAvailableResourceUrls() != null && getAvailableResourceUrls().length > 0 ? true : false);
		btnDisplayNameID.setText("..."); //$NON-NLS-1$
		btnDisplayNameID.setToolTipText(Messages.getString("ResourceKeyDescriptor.button.browse.tooltip")); //$NON-NLS-1$
		btnDisplayNameID.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent event) {
				openKeySelectionDialog();
			}
		});

		btnRemoveDisplayNameID = new Button(composite, SWT.NONE);
		btnRemoveDisplayNameID.setImage(ReportPlatformUIImages.getImage(ISharedImages.IMG_TOOL_DELETE));
		btnRemoveDisplayNameID.setToolTipText(Messages.getString("ResourceKeyDescriptor.button.reset.tooltip")); //$NON-NLS-1$
		btnRemoveDisplayNameID.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent event) {
				txtDisplayNameID.setText(EMPTY_STRING);
				txtDisplayName.setText(EMPTY_STRING);

				modifyDialogContent();

				updateRemoveBtnState();
			}
		});

		new Label(composite, SWT.NONE).setText(DISPLAY_NAME);
		txtDisplayName = new Text(composite, SWT.BORDER);
		txtDisplayName.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				modifyDialogContent();
			}

		});
		txtDisplayName.setLayoutData(gd);
		// WidgetUtil.createGridPlaceholder( composite, 1, false );

		new Label(composite, SWT.NONE).setText(DATA_TYPE);
		cmbType = new Combo(composite, SWT.BORDER | SWT.READ_ONLY);
		cmbType.setLayoutData(gd);
		cmbType.setVisibleItemCount(30);
		cmbType.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent arg0) {
				validate();
			}

			public void widgetSelected(SelectionEvent arg0) {
				modifyDialogContent();

				validate();
			}
		});

		Label allowExportLabel = new Label(composite, SWT.NONE);
		allowExportLabel.setText(ALLOW_EXPORT_LABEL);
		btnAllowExport = new Button(composite, SWT.CHECK);
		btnAllowExport.setText(ALLOW_EXPORT_BUTTON);
		btnAllowExport.setSelection(true);

		GridData gd1 = new GridData(GridData.FILL_HORIZONTAL);
		gd1.horizontalSpan = 3;
		gd1.widthHint = 200;
		gd1.heightHint = cmbType.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
		btnAllowExport.setLayoutData(gd1);

		btnAllowExport.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				modifyDialogContent();
			}
		});

		// WidgetUtil.setExcludeGridData( allowExportLabel, true );
		// WidgetUtil.setExcludeGridData( btnAllowExport, true );

		if (isAggregate()) {
			createAggregateSection(composite);
		} else {
			createCommonSection(composite);
		}

		createMessageSection(composite);

		gd = new GridData(GridData.FILL_BOTH);
		composite.setLayoutData(gd);
		setContentSize(composite);
	}

	private void openKeySelectionDialog() {
		ResourceEditDialog dlg = new ResourceEditDialog(composite.getShell(),
				Messages.getString("ResourceKeyDescriptor.title.SelectKey")); //$NON-NLS-1$

		dlg.setResourceURLs(getResourceURLs());

		if (dlg.open() == Window.OK) {
			String[] result = (String[]) dlg.getDetailResult();
			if (result != null && result.length > 1) {
				txtDisplayNameID.setText(DEUtil.resolveNull(result[0]));
				txtDisplayName.setText(DEUtil.resolveNull(result[1]));
				modifyDialogContent();
				updateRemoveBtnState();
			}
		}
	}

	private boolean hasInitDialog = false;

	public void initDialog() {
		cmbType.setItems(dataTypes);
		// txtDisplayName.setFocus( );
		// initiate function firstly then data type field.
		// Expression gets the comment.
		if (txtExpression != null && !txtExpression.isDisposed())// add
																	// if/else
																	// block to
																	// fix TED
																	// 52776:NPE
																	// thrown
		{
			txtExpression.setFocus();
		} else {
			txtDisplayName.setFocus();
		}
		if (isAggregate()) {
			initFunction();
			initFilter();
			initGroups();
		}

		if (isCreate)// create
		{
			if (isRef) {
				if (getBinding() == null) {
					for (Iterator iterator = getBindingHolder().getDataBindingReference().getColumnBindings()
							.iterator(); iterator.hasNext();) {
						ComputedColumnHandle computedColumn = (ComputedColumnHandle) iterator.next();
						if (isAggregate()) {
							if (computedColumn.getAggregateFunction() == null
									|| computedColumn.getAggregateFunction().equals("")) //$NON-NLS-1$
								continue;
						} else {
							if (computedColumn.getAggregateFunction() != null
									&& !computedColumn.getAggregateFunction().equals("")) //$NON-NLS-1$
								continue;
						}
						cmbName.add(computedColumn.getName());
					}
				} else {
					setDisplayName(getBinding().getDisplayName());
					setDisplayNameID(getBinding().getDisplayNameID());
					setAllowExport(getBinding().allowExport());
					for (int i = 0; i < DATA_TYPE_CHOICES.length; i++) {
						if (DATA_TYPE_CHOICES[i].getName().equals(getBinding().getDataType())) {
							setTypeSelect(DATA_TYPE_CHOICES[i].getDisplayName());
							break;
						}
					}
					setDataFieldExpression(getBinding());
				}
			} else {
				this.newBinding = StructureFactory.newComputedColumn(getBindingHolder(),
						isAggregate() ? DEFAULT_AGGREGATION_NAME : DEFAULT_ITEM_NAME);
				setName(this.newBinding.getName());
				setAllowExport(this.newBinding.allowExport());
				if (!isAggregate()) {
					setTypeSelect(getDataTypeDisplayName(DesignChoiceConstants.COLUMN_DATA_TYPE_STRING));
				}
			}
		} else {
			if (isRef) {
				int i = 0;
				for (Iterator iterator = getBindingHolder().getDataBindingReference().getColumnBindings()
						.iterator(); iterator.hasNext();) {
					ComputedColumnHandle computedColumn = (ComputedColumnHandle) iterator.next();
					if (isAggregate()) {
						if (computedColumn.getAggregateFunction() == null
								|| computedColumn.getAggregateFunction().equals("")) //$NON-NLS-1$
							continue;
					} else {
						if (computedColumn.getAggregateFunction() != null
								&& !computedColumn.getAggregateFunction().equals("")) //$NON-NLS-1$
							continue;
					}
					cmbName.add(computedColumn.getName());
					if (getBinding().getName().equals(computedColumn.getName()))
						cmbName.select(i);
					i++;
				}
				setDisplayName(getBinding().getDisplayName());
				setDisplayNameID(getBinding().getDisplayNameID());
				setAllowExport(getBinding().allowExport());
				for (i = 0; i < DATA_TYPE_CHOICES.length; i++) {
					if (DATA_TYPE_CHOICES[i].getName().equals(getBinding().getDataType())) {
						setTypeSelect(DATA_TYPE_CHOICES[i].getDisplayName());
						break;
					}
				}
				setDataFieldExpression(getBinding());
			} else {
				setName(getBinding().getName());
				setDisplayName(getBinding().getDisplayName());
				setDisplayNameID(getBinding().getDisplayNameID());
				setAllowExport(getBinding().allowExport());
				if (getBinding().getDataType() != null) {
					if (DATA_TYPE_CHOICE_SET.findChoice(getBinding().getDataType()) != null)
						setTypeSelect(DATA_TYPE_CHOICE_SET.findChoice(getBinding().getDataType()).getDisplayName());
					else
						// the old type 'any'
						cmbType.setText(""); //$NON-NLS-1$
				}
				setDataFieldExpression(getBinding());
			}
		}

		if (!isCreate) {
			if (isRef) {
				this.cmbName.setEnabled(true);
			} else {
				this.txtName.setEnabled(false);
			}
		}

		validate();

		hasInitDialog = true;
	}

	private void initExpressionButton(ExpressionHandle expressionHandle, Text text) {
		ExpressionButtonUtil.initExpressionButtonControl(text, expressionHandle);
	}

	private void initFilter() {
		if (binding != null) {
			ExpressionHandle expressionHandle = binding.getExpressionProperty(ComputedColumn.FILTER_MEMBER);
			initExpressionButton(expressionHandle, txtFilter);
		}
	}

	private void initFunction() {
		cmbFunction.setItems(getFunctionDisplayNames());
		// cmbFunction.add( NULL, 0 );
		if (binding == null) {
			cmbFunction.select(0);
			handleFunctionSelectEvent();
			return;
		}
		try {
			String functionString = getFunctionDisplayName(
					DataAdapterUtil.adaptModelAggregationType(binding.getAggregateFunction()));
			int itemIndex = getItemIndex(getFunctionDisplayNames(), functionString);
			cmbFunction.select(itemIndex);
			handleFunctionSelectEvent();
		} catch (AdapterException e) {
			ExceptionHandler.handle(e);
		}
		// List args = getFunctionArgs( functionString );
		// bindingColumn.argumentsIterator( )

		// FIXME backforward compatible with binding getExpression
		for (Iterator iterator = binding.argumentsIterator(); iterator.hasNext();) {
			AggregationArgumentHandle arg = (AggregationArgumentHandle) iterator.next();
			String argName = DataAdapterUtil.adaptArgumentName(arg.getName());
			if (paramsMap.containsKey(argName)) {
				if (arg.getValue() != null) {
					Control control = paramsMap.get(argName);
					if (control instanceof Text) {
						((Text) control).setText(arg.getValue());
					} else if (control instanceof Combo) {
						((Combo) control).setText(arg.getValue());
					}
				}
			}
		}
	}

	private String[] getFunctionDisplayNames() {
		IAggrFunction[] choices = getFunctions();
		if (choices == null)
			return new String[0];

		String[] displayNames = new String[choices.length];
		for (int i = 0; i < choices.length; i++) {
			displayNames[i] = choices[i].getDisplayName();
		}
		java.util.Arrays.sort(displayNames, new AlphabeticallyComparator());
		return displayNames;
	}

	protected IAggrFunction getFunctionByDisplayName(String displayName) {
		IAggrFunction[] choices = getFunctions();
		if (choices == null)
			return null;

		for (int i = 0; i < choices.length; i++) {
			if (choices[i].getDisplayName().equals(displayName)) {
				return choices[i];
			}
		}
		return null;
	}

	protected String getFunctionDisplayName(String function) {
		try {
			return DataUtil.getAggregationManager().getAggregation(function).getDisplayName();
		} catch (BirtException e) {
			ExceptionHandler.handle(e);
			return null;
		}
	}

	private IAggrFunction[] getFunctions() {
		try {
			List aggrInfoList = DataUtil.getAggregationManager().getAggregations(AggregationManager.AGGR_TABULAR);
			return (IAggrFunction[]) aggrInfoList.toArray(new IAggrFunction[0]);
		} catch (BirtException e) {
			ExceptionHandler.handle(e);
			return new IAggrFunction[0];
		}
	}

	protected void initTextField(Text txtParam, IParameterDefn param) {
		if (paramsValueMap.containsKey(param.getName())) {
			txtParam.setText(paramsValueMap.get(param.getName())[0]);
			txtParam.setData(ExpressionButtonUtil.EXPR_TYPE, paramsValueMap.get(param.getName())[1]);
			ExpressionButton button = (ExpressionButton) txtParam.getData(ExpressionButtonUtil.EXPR_BUTTON);
			if (button != null)
				button.refresh();
			return;
		}
		if (binding != null) {
			for (Iterator iterator = binding.argumentsIterator(); iterator.hasNext();) {
				AggregationArgumentHandle arg = (AggregationArgumentHandle) iterator.next();
				if (arg.getName().equals(param.getName())) {
					ExpressionButtonUtil.initExpressionButtonControl(txtParam, arg, AggregationArgument.VALUE_MEMBER);
					return;
				}
			}
		}
	}

	/**
	 * fill the cmbDataField with binding holder's bindings
	 * 
	 * @param param
	 */
	protected void initDataFields(Combo cmbDataField, IParameterDefn param) {
		cmbDataField.setItems(getColumnBindings());
		if (paramsValueMap.containsKey(param.getName())) {
			cmbDataField.setText(paramsValueMap.get(param.getName())[0]);
			cmbDataField.setData(ExpressionButtonUtil.EXPR_TYPE, paramsValueMap.get(param.getName())[1]);
			ExpressionButton button = (ExpressionButton) cmbDataField.getData(ExpressionButtonUtil.EXPR_BUTTON);
			if (button != null)
				button.refresh();
			return;
		}
		if (binding != null) {
			ExpressionHandle expressionHandle = null;
			for (Iterator iterator = binding.argumentsIterator(); iterator.hasNext();) {
				AggregationArgumentHandle arg = (AggregationArgumentHandle) iterator.next();
				if (arg.getName().equals(param.getName())) {
					ExpressionHandle value = arg.getExpressionProperty(AggregationArgument.VALUE_MEMBER);

					expressionHandle = value;

					break;
				}
			}

			if (expressionHandle == null)
				expressionHandle = binding.getExpressionProperty(ComputedColumn.EXPRESSION_MEMBER);

			ExpressionButtonUtil.initExpressionButtonControl(cmbDataField, expressionHandle);
		}

		Object button = cmbDataField.getData(ExpressionButtonUtil.EXPR_BUTTON);
		if (button instanceof ExpressionButton) {
			if (!((ExpressionButton) button).isSupportType(ExpressionType.JAVASCRIPT)) {
				cmbDataField.removeAll();
			}
		}
	}

	protected String[] getColumnBindings() {
		List elementsList = DEUtil.getVisiableColumnBindingsList(getBindingHolder());
		String[] bindings = new String[elementsList.size()];
		for (int i = 0; i < bindings.length; i++) {
			bindings[i] = ((ComputedColumnHandle) elementsList.get(i)).getName();
		}
		return bindings;
	}

	protected void initGroups() {
		String[] groups = getGroups();
		if (groups.length > 0) {
			cmbGroup.setItems(groups);
			if (binding != null && binding.getAggregateOn() != null) {
				btnGroup.setSelection(true);
				btnTable.setSelection(false);
				if (!isRef)
					cmbGroup.setEnabled(true);
				for (int i = 0; i < groups.length; i++) {
					if (groups[i].equals(binding.getAggregateOn())) {
						cmbGroup.select(i);
						return;
					}
				}
			} else {
				// BUG 201963
				if (this.container instanceof DesignElementHandle && ((DesignElementHandle) this.container)
						.getContainer().getContainer() instanceof TableGroupHandle) {
					TableGroupHandle groupHandle = (TableGroupHandle) ((DesignElementHandle) this.container)
							.getContainer().getContainer();
					for (int i = 0; i < groups.length; i++) {
						if (groups[i].equals(groupHandle.getName())) {
							cmbGroup.select(i);
						}
					}
					btnTable.setSelection(false);
					btnGroup.setSelection(true);
				} else if (this.container instanceof ListGroupHandle) {
					ListGroupHandle groupHandle = (ListGroupHandle) this.container;
					for (int i = 0; i < groups.length; i++) {
						if (groups[i].equals(groupHandle.getName())) {
							cmbGroup.select(i);
						}
					}
					btnTable.setSelection(false);
					btnGroup.setSelection(true);
				} else {
					btnTable.setSelection(true);
					btnGroup.setSelection(false);
					cmbGroup.select(0);
					cmbGroup.setEnabled(false);
				}
			}
		} else {
			btnGroup.setEnabled(false);
			cmbGroup.setEnabled(false);
			btnTable.setSelection(true);
		}
	}

	public String[] getGroups() {
		if (getBindingHolder() instanceof ListingHandle) {
			ListingHandle listingHandle = (ListingHandle) getBindingHolder();
			List groupNames = new ArrayList();
			for (int i = 0; i < listingHandle.getGroups().getCount(); i++) {
				String groupName = ((GroupHandle) listingHandle.getGroups().get(i)).getName();
				if (groupName != null)
					groupNames.add(groupName);
			}
			return (String[]) groupNames.toArray(new String[0]);
		} else {
			return super.getGroups();
		}
	}

	private void setDataFieldExpression(ComputedColumnHandle binding) {
		if (binding != null) {
			if (txtExpression != null && !txtExpression.isDisposed()) {
				ExpressionButtonUtil.initExpressionButtonControl(txtExpression, binding,
						ComputedColumn.EXPRESSION_MEMBER);
			}
		}
	}

	private void setName(String name) {
		if (name != null && txtName != null)
			txtName.setText(name);
	}

	private void setDisplayName(String displayName) {
		if (displayName != null && txtDisplayName != null)
			txtDisplayName.setText(displayName);
	}

	private void setDisplayNameID(String displayNameID) {
		if (displayNameID != null && txtDisplayNameID != null)
			txtDisplayNameID.setText(displayNameID);
	}

	private void setAllowExport(boolean allowExport) {
		if (btnAllowExport != null)
			btnAllowExport.setSelection(allowExport);
	}

	private void setTypeSelect(String typeSelect) {
		if (dataTypes != null && cmbType != null) {
			if (typeSelect != null)
				cmbType.select(getItemIndex(cmbType.getItems(), typeSelect));
			else
				cmbType.select(0);
		}
	}

	private int getItemIndex(String[] items, String item) {
		for (int i = 0; i < items.length; i++) {
			if (items[i].equals(item))
				return i;
		}
		return -1;
	}

	protected void createAggregateSection(Composite composite) {

		new Label(composite, SWT.NONE).setText(FUNCTION);
		cmbFunction = new Combo(composite, SWT.BORDER | SWT.READ_ONLY);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		cmbFunction.setLayoutData(gd);
		cmbFunction.setVisibleItemCount(30);
		// WidgetUtil.createGridPlaceholder( composite, 1, false );

		cmbFunction.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				modifyDialogContent();
				handleFunctionSelectEvent();
				validate();
			}
		});

		paramsComposite = new Composite(composite, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalIndent = 0;
		gridData.horizontalSpan = 4;
		gridData.exclude = true;
		paramsComposite.setLayoutData(gridData);
		GridLayout layout = new GridLayout();
		// layout.horizontalSpacing = layout.verticalSpacing = 0;
		layout.marginWidth = layout.marginHeight = 0;
		layout.numColumns = 3;
		Layout parentLayout = paramsComposite.getParent().getLayout();
		if (parentLayout instanceof GridLayout)
			layout.horizontalSpacing = ((GridLayout) parentLayout).horizontalSpacing;
		paramsComposite.setLayout(layout);

		createFilterCondition(composite, gd);

		final Label lblAggOn = new Label(composite, SWT.NONE);
		lblAggOn.setText(AGGREGATE_ON);
		gridData = new GridData();
		gridData.verticalAlignment = GridData.BEGINNING;
		lblAggOn.setLayoutData(gridData);

		Composite aggOnComposite = new Composite(composite, SWT.NONE);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 3;
		aggOnComposite.setLayoutData(gridData);

		layout = new GridLayout();
		layout.horizontalSpacing = layout.verticalSpacing = 0;
		layout.marginWidth = layout.marginHeight = 0;
		layout.numColumns = 2;
		aggOnComposite.setLayout(layout);

		btnTable = new Button(aggOnComposite, SWT.RADIO);
		if (getBindingHolder() instanceof TableHandle)
			btnTable.setText(TABLE);
		else if (getBindingHolder() instanceof ListHandle)
			btnTable.setText(LIST);
		else if (getBindingHolder() instanceof GridHandle)
			btnTable.setText(GRID);
		else
			btnTable.setText(ALL);

		btnTable.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				modifyDialogContent();
				cmbGroup.setEnabled(false);
			}
		});

		btnTable.getAccessible().addAccessibleListener(new AccessibleAdapter() {

			public void getName(AccessibleEvent e) {
				e.result = UIUtil.stripMnemonic(lblAggOn.getText()) + UIUtil.stripMnemonic(btnTable.getText());
			}
		});

		WidgetUtil.createGridPlaceholder(aggOnComposite, 1, false);

		btnGroup = new Button(aggOnComposite, SWT.RADIO);
		btnGroup.setText(GROUP);
		btnGroup.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				modifyDialogContent();
				cmbGroup.setEnabled(true);
			}
		});

		btnGroup.getAccessible().addAccessibleListener(new AccessibleAdapter() {

			public void getName(AccessibleEvent e) {
				e.result = UIUtil.stripMnemonic(lblAggOn.getText()) + UIUtil.stripMnemonic(btnGroup.getText());
			}
		});

		cmbGroup = new Combo(aggOnComposite, SWT.BORDER | SWT.READ_ONLY);
		cmbGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		cmbGroup.setVisibleItemCount(30);
		cmbGroup.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				modifyDialogContent();
			}
		});
		cmbFunction.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				modifyDialogContent();
			}
		});

		if (isRef) {
			txtDisplayName.setEnabled(false);
			txtDisplayNameID.setEnabled(false);
			btnDisplayNameID.setEnabled(false);
			cmbType.setEnabled(false);
			cmbFunction.setEnabled(false);
			// cmbDataField.setEnabled( false );
			txtFilter.setEnabled(false);
			paramsComposite.setEnabled(false);
			cmbGroup.setEnabled(false);
			btnTable.setEnabled(false);
			btnGroup.setEnabled(false);
		}
	}

	protected void createFilterCondition(Composite composite, GridData gd) {
		new Label(composite, SWT.NONE).setText(FILTER_CONDITION);
		txtFilter = new Text(composite, SWT.BORDER | SWT.MULTI);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = txtFilter.computeSize(SWT.DEFAULT, SWT.DEFAULT).y - txtFilter.getBorderWidth() * 2;
		gd.horizontalSpan = 2;
		txtFilter.setLayoutData(gd);

		txtFilter.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent arg0) {
				modifyDialogContent();
				validate();
			}
		});
		createExpressionButton(composite, txtFilter);
	}

	private void createCommonSection(Composite composite) {
		new Label(composite, SWT.NONE).setText(EXPRESSION);

		txtExpression = new Text(composite, SWT.BORDER | SWT.MULTI);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		gd.heightHint = txtExpression.computeSize(SWT.DEFAULT, SWT.DEFAULT).y - txtExpression.getBorderWidth() * 2;
		txtExpression.setLayoutData(gd);
		createExpressionButton(composite, txtExpression);
		txtExpression.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				modifyDialogContent();
				validate();
			}

		});
		if (isRef) {
			txtDisplayName.setEnabled(false);
			txtDisplayNameID.setEnabled(false);
			btnDisplayNameID.setEnabled(false);
			cmbType.setEnabled(false);
			txtExpression.setEnabled(false);
		}
	}

	private void createMessageSection(Composite composite) {
		messageLine = new CLabel(composite, SWT.LEFT);
		GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);
		layoutData.horizontalSpan = 4;
		messageLine.setLayoutData(layoutData);
	}

	public void setMessage(String message) {
		this.messageLine.setText(message);
		this.messageLine.setImage(null);
	}

	public void setErrorMessage(String message) {
		this.messageLine.setText(message);
		this.messageLine
				.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_ERROR_TSK));
	}

	private void verifyInput() {
		if (isRef) {
			if (cmbName.getText() == null || cmbName.getText().equals("")) //$NON-NLS-1$
			{
				dialog.setCanFinish(false);
			} else {
				dialogCanFinish();
			}
			return;
		}

		if (txtName != null && (txtName.getText() == null || txtName.getText().trim().equals(""))) //$NON-NLS-1$
		{
			setErrorMessage(Messages.getFormattedString("BindingDialogHelper.error.empty", //$NON-NLS-1$
					new Object[] { NAME_LABEL }));
			dialog.setCanFinish(false);
			return;
		}

		if (cmbType.getText() == null || cmbType.getText().equals("")) //$NON-NLS-1$
		{
			dialog.setCanFinish(false);
			return;
		}

		if (this.binding == null)// create bindnig, we should check if the
		// binding name already exists.
		{
			for (Iterator iterator = this.bindingHolder.getColumnBindings().iterator(); iterator.hasNext();) {
				ComputedColumnHandle computedColumn = (ComputedColumnHandle) iterator.next();
				if (computedColumn.getName().equals(txtName.getText())) {
					dialog.setCanFinish(false);
					setErrorMessage(Messages.getFormattedString("BindingDialogHelper.error.nameduplicate", //$NON-NLS-1$
							new Object[] { txtName.getText() }));
					return;
				}
			}
		}

		setMessage(""); //$NON-NLS-1$

		if (txtExpression != null && (txtExpression.getText() == null || txtExpression.getText().trim().equals(""))) //$NON-NLS-1$
		{
			// This is a special calse if the item is data item,and the
			// container is LibraryHandle,allow the empty expression
			if (!isAllowEmyptExpression()) {
				dialog.setCanFinish(false);
				return;
			}
		}

		// check non optional parameter is not empty
		if (isAggregate()) {
			try {
				IAggrFunction aggregation = DataUtil.getAggregationManager()
						.getAggregation(getFunctionByDisplayName(cmbFunction.getText()).getName());

				if (aggregation.getParameterDefn().length > 0) {
					IParameterDefn[] parameters = aggregation.getParameterDefn();
					for (IParameterDefn param : parameters) {
						if (!param.isOptional()) {
							Control control = paramsMap.get(param.getName());
							String paramValue = null;
							if (control instanceof Text) {
								paramValue = ((Text) control).getText();
							}
							if (control instanceof Combo) {
								paramValue = ((Combo) control).getText();
							}
							if (paramValue == null || paramValue.trim().equals("")) //$NON-NLS-1$
							{
								dialog.setCanFinish(false);
								setErrorMessage(Messages.getFormattedString("BindingDialogHelper.error.empty", //$NON-NLS-1$
										new String[] { param.getDisplayName().replaceAll("\\(&[a-zA-Z0-9]\\)", "")
												.replaceAll("&", "") }));
								return;
							}
						}
					}
				}
			} catch (BirtException e) {
				// TODO show error message in message panel
			}
		}
		dialogCanFinish();
	}

	private boolean isAllowEmyptExpression() {
		ReportItemHandle itemHandle = getBindingHolder();
		return itemHandle instanceof DataItemHandle && itemHandle.getDataSet() == null
				&& itemHandle.getContainer() instanceof LibraryHandle;
	}

	private void dialogCanFinish() {
		if (!isAllowEmyptExpression() && !hasModified && isEditModal())
			dialog.setCanFinish(false);
		else
			dialog.setCanFinish(true);
	}

	/**
	 * Create function parameters area. If parameter is data field type, create a
	 * combo box filled with binding holder's computed column.
	 */
	protected void handleFunctionSelectEvent() {
		if (isRef)
			return;
		Control[] children = paramsComposite.getChildren();
		for (int i = 0; i < children.length; i++) {
			children[i].dispose();
		}

		IAggrFunction function = getFunctionByDisplayName(cmbFunction.getText());
		if (function != null) {
			paramsMap.clear();
			IParameterDefn[] params = function.getParameterDefn();
			if (params.length > 0) {
				((GridData) paramsComposite.getLayoutData()).exclude = false;
				((GridData) paramsComposite.getLayoutData()).heightHint = SWT.DEFAULT;

				int width = 0;
				if (paramsComposite.getParent().getLayout() instanceof GridLayout) {
					Control[] controls = paramsComposite.getParent().getChildren();
					for (int i = 0; i < controls.length; i++) {
						if (controls[i] instanceof Label
								&& ((GridData) controls[i].getLayoutData()).horizontalSpan == 1) {
							int labelWidth = controls[i].getBounds().width - controls[i].getBorderWidth() * 2;
							if (labelWidth > width)
								width = labelWidth;
						}
					}
				}

				for (final IParameterDefn param : params) {
					Label lblParam = new Label(paramsComposite, SWT.NONE);
					lblParam.setText(param.getDisplayName() + Messages.getString("BindingDialogHelper.text.Colon")); //$NON-NLS-1$
					GridData gd = new GridData();
					gd.widthHint = lblParam.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
					if (gd.widthHint < width)
						gd.widthHint = width;
					lblParam.setLayoutData(gd);

					if (param.isDataField()) {
						final Combo cmbDataField = new Combo(paramsComposite, SWT.BORDER);
						cmbDataField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));
						cmbDataField.setVisibleItemCount(30);
						createExpressionButton(paramsComposite, cmbDataField);

						initDataFields(cmbDataField, param);

						cmbDataField.addModifyListener(new ModifyListener() {

							public void modifyText(ModifyEvent e) {
								modifyDialogContent();
								;
								validate();
								paramsValueMap.put(param.getName(), new String[] { cmbDataField.getText(),
										(String) cmbDataField.getData(ExpressionButtonUtil.EXPR_TYPE) });
							}
						});

						cmbDataField.addSelectionListener(new SelectionAdapter() {

							public void widgetSelected(SelectionEvent e) {
								String expr = getColumnBindingExpressionByName(cmbDataField);
								if (expr != null) {
									cmbDataField.setText(expr);
								}
								// cmbDataField.setData(
								// ExpressionButtonUtil.EXPR_TYPE,
								// ExpressionType.JAVASCRIPT );
								// ExpressionButton button = (ExpressionButton)
								// cmbDataField.getData(
								// ExpressionButtonUtil.EXPR_BUTTON );
								// if ( button != null )
								// button.refresh( );
							}
						});

						paramsMap.put(param.getName(), cmbDataField);
					} else {
						final Text txtParam = new Text(paramsComposite, SWT.BORDER | SWT.MULTI);
						txtParam.addModifyListener(new ModifyListener() {

							public void modifyText(ModifyEvent e) {
								modifyDialogContent();
								validate();
								paramsValueMap.put(param.getName(), new String[] { txtParam.getText(),
										(String) txtParam.getData(ExpressionButtonUtil.EXPR_TYPE) });
							}
						});
						GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
						gridData.heightHint = txtParam.computeSize(SWT.DEFAULT, SWT.DEFAULT).y
								- txtParam.getBorderWidth() * 2;
						gridData.horizontalIndent = 0;
						txtParam.setLayoutData(gridData);
						createExpressionButton(paramsComposite, txtParam);
						paramsMap.put(param.getName(), txtParam);
						initTextField(txtParam, param);

					}
				}
			} else {
				((GridData) paramsComposite.getLayoutData()).heightHint = 0;
				((GridData) paramsComposite.getLayoutData()).exclude = true;
			}

			try {
				cmbType.setText(getDataTypeDisplayName(DataAdapterUtil.adapterToModelDataType(
						DataUtil.getAggregationManager().getAggregation(function.getName()).getDataType())));
			} catch (BirtException e) {
				ExceptionHandler.handle(e);
			}
		} else {
			((GridData) paramsComposite.getLayoutData()).heightHint = 0;
			((GridData) paramsComposite.getLayoutData()).exclude = true;
			// new Label( argsComposite, SWT.NONE ).setText( "no args" );
		}
		paramsComposite.layout(true, true);
		paramsComposite.getParent().layout(true, true);
		setContentSize(composite);
	}

	protected void createExpressionButton(Composite parent, final Control control) {
		Listener listener = new Listener() {

			public void handleEvent(Event event) {
				modifyDialogContent();
				validate();
			}

		};

		if (expressionProvider == null) {
			IExtendedDataModelUIAdapter adapter = ExtendedDataModelUIAdapterHelper.getInstance().getAdapter();
			if (adapter != null && adapter.getBoundExtendedData(this.bindingHolder) != null) {
				expressionProvider = adapter.getBindingExpressionProvider(this.bindingHolder, this.binding);
			} else {
				expressionProvider = new BindingExpressionProvider(this.bindingHolder, this.binding);
			}
		}

		ExpressionButton button = ExpressionButtonUtil.createExpressionButton(parent, control, expressionProvider,
				this.bindingHolder, listener);
		if (isRef) {
			button.setEnabled(false);
		}
	}

	protected String getColumnBindingExpressionByName(Combo combo) {
		List elementsList = DEUtil.getVisiableColumnBindingsList(this.bindingHolder);
		for (Iterator iterator = elementsList.iterator(); iterator.hasNext();) {
			ComputedColumnHandle binding = (ComputedColumnHandle) iterator.next();
			if (binding.getName().equals(combo.getText()))
				return ExpressionButtonUtil.getCurrentExpressionConverter(combo).getBindingExpression(combo.getText());
		}
		return null;
	}

	public void validate() {
		verifyInput();
		updateRemoveBtnState();
	}

	public boolean differs(ComputedColumnHandle binding) {
		if (isAggregate()) {
			if (txtName != null && !strEquals(txtName.getText(), binding.getName()))
				return true;
			if (cmbName != null && !strEquals(cmbName.getText(), binding.getName()))
				return true;
			if (btnAllowExport.getSelection() != binding.allowExport())
				return true;
			if (!strEquals(binding.getDisplayName(), txtDisplayName.getText()))
				return true;
			if (!strEquals(binding.getDisplayNameID(), txtDisplayNameID.getText()))
				return true;
			if (!strEquals(binding.getDataType(), getDataType()))
				return true;
			try {
				if (!strEquals(DataAdapterUtil.adaptModelAggregationType(binding.getAggregateFunction()),
						getFunctionByDisplayName(cmbFunction.getText()).getName()))
					return true;
			} catch (AdapterException e) {
			}

			if (!expressionEquals(binding.getExpressionProperty(ComputedColumn.FILTER_MEMBER), txtFilter))
				return true;
			if (btnTable.getSelection() == (binding.getAggregateOn() != null))
				return true;
			if (!btnTable.getSelection() && !binding.getAggregateOn().equals(cmbGroup.getText()))
				return true;

			boolean hasArguments = false;

			for (Iterator iterator = binding.argumentsIterator(); iterator.hasNext();) {
				AggregationArgumentHandle handle = (AggregationArgumentHandle) iterator.next();
				if (paramsMap.containsKey(handle.getName())) {
					String[] paramValue = getControlValue(paramsMap.get(handle.getName()));
					if (!expressionEquals(handle.getExpressionProperty(AggregationArgument.VALUE_MEMBER), paramValue)) {
						return true;
					}
				} else {
					return true;
				}
				hasArguments = true;
			}

			if (!hasArguments && !paramsMap.isEmpty())
				return true;
		} else {
			if (txtName != null && !strEquals(txtName.getText(), binding.getName()))
				return true;
			if (cmbName != null && !strEquals(cmbName.getText(), binding.getName()))
				return true;
			if (!strEquals(txtDisplayName.getText(), binding.getDisplayName()))
				return true;
			if (!strEquals(txtDisplayNameID.getText(), binding.getDisplayNameID()))
				return true;
			if (btnAllowExport.getSelection() != binding.allowExport())
				return true;
			if (!strEquals(getDataType(), binding.getDataType()))
				return true;
			if (!expressionEquals(binding.getExpressionProperty(ComputedColumn.EXPRESSION_MEMBER), txtExpression))
				return true;
		}
		return false;
	}

	private boolean expressionEquals(ExpressionHandle expressionHandle, Text text) {
		if (expressionHandle == null) {
			if (text.getText().trim().length() == 0)
				return true;
		} else {
			if (strEquals(expressionHandle.getStringExpression(), text.getText())
					&& strEquals(expressionHandle.getType(), (String) text.getData(ExpressionButtonUtil.EXPR_TYPE)))
				return true;
		}
		return false;
	}

	private boolean expressionEquals(ExpressionHandle expressionHandle, String[] strs) {
		if (expressionHandle == null) {
			if (strs == null || strs[0].trim().length() == 0)
				return true;
		} else {
			if (strs != null && strEquals(expressionHandle.getStringExpression(), strs[0])
					&& strEquals(expressionHandle.getType(), strs[1]))
				return true;
		}
		return false;
	}

	protected String[] getControlValue(Control control) {
		if (control instanceof Text) {
			return new String[] { ((Text) control).getText(),
					(String) control.getData(ExpressionButtonUtil.EXPR_TYPE) };
		} else if (control instanceof Combo) {
			return new String[] { ((Combo) control).getText(),
					(String) control.getData(ExpressionButtonUtil.EXPR_TYPE) };
		}
		return null;
	}

	private boolean strEquals(String left, String right) {
		if (left == right)
			return true;
		if (left == null)
			return "".equals(right); //$NON-NLS-1$
		if (right == null)
			return "".equals(left); //$NON-NLS-1$
		return left.equals(right);
	}

	protected String getDataTypeDisplayName(String dataType) {
		for (int i = 0; i < DATA_TYPE_CHOICES.length; i++) {
			if (dataType.equals(DATA_TYPE_CHOICES[i].getName())) {
				return DATA_TYPE_CHOICES[i].getDisplayName();
			}
		}
		return ""; //$NON-NLS-1$
	}

	private String getDataType() {
		for (int i = 0; i < DATA_TYPE_CHOICES.length; i++) {
			if (DATA_TYPE_CHOICES[i].getDisplayName().equals(cmbType.getText())) {
				return DATA_TYPE_CHOICES[i].getName();
			}
		}
		return ""; //$NON-NLS-1$
	}

	public ComputedColumnHandle editBinding(ComputedColumnHandle binding) throws SemanticException {
		if (isRef)
			return getBindingColumn();
		if (isAggregate()) {
			binding.setDisplayName(txtDisplayName.getText());
			binding.setDisplayNameID(txtDisplayNameID.getText());
			for (int i = 0; i < DATA_TYPE_CHOICES.length; i++) {
				if (DATA_TYPE_CHOICES[i].getDisplayName().equals(cmbType.getText())) {
					binding.setDataType(DATA_TYPE_CHOICES[i].getName());
					break;
				}
			}
			binding.setAllowExport(btnAllowExport.getSelection());
			// binding.setExpression( cmbDataField.getText( ) );
			binding.setAggregateFunction(getFunctionByDisplayName(cmbFunction.getText()).getName());

			ExpressionButtonUtil.saveExpressionButtonControl(txtFilter, binding, ComputedColumn.FILTER_MEMBER);

			if (btnTable.getSelection()) {
				binding.setAggregateOn(null);
			} else {
				binding.setAggregateOn(cmbGroup.getText());
			}

			// remove expression created in old version.
			binding.setExpression(null);
			binding.clearArgumentList();

			for (Iterator iterator = paramsMap.keySet().iterator(); iterator.hasNext();) {
				String arg = (String) iterator.next();
				String[] value = getControlValue(paramsMap.get(arg));
				if (value != null) {
					AggregationArgument argHandle = StructureFactory.createAggregationArgument();
					argHandle.setName(arg);
					argHandle.setExpressionProperty(AggregationArgument.VALUE_MEMBER,
							new Expression(value[0], value[1]));
					binding.addArgument(argHandle);
				}
			}
		} else {
			for (int i = 0; i < DATA_TYPE_CHOICES.length; i++) {
				if (DATA_TYPE_CHOICES[i].getDisplayName().equals(cmbType.getText())) {
					binding.setDataType(DATA_TYPE_CHOICES[i].getName());
					break;
				}
			}
			binding.setDisplayName(txtDisplayName.getText());
			binding.setDisplayNameID(txtDisplayNameID.getText());
			binding.setAllowExport(btnAllowExport.getSelection());
			ExpressionButtonUtil.saveExpressionButtonControl(txtExpression, binding, ComputedColumn.EXPRESSION_MEMBER);
		}
		return binding;
	}

	public ComputedColumnHandle newBinding(ReportItemHandle bindingHolder, String name) throws SemanticException {
		if (isRef)
			return getBindingColumn();
		ComputedColumn column = StructureFactory.newComputedColumn(bindingHolder,
				name == null ? txtName.getText() : name);
		ComputedColumnHandle binding = DEUtil.addColumn(bindingHolder, column, true);
		return editBinding(binding);
	}

	public void setContainer(Object container) {
		this.container = container;
	}

	public boolean canProcessWithWarning() {

		if (!isAggregate()) {
			return true;
		}

		try {
			// check function type
			// if datatype in DTE is any, here will return '', for any is
			// deprecated.
			String type = getDataTypeDisplayName(DataAdapterUtil.adapterToModelDataType(DataUtil.getAggregationManager()
					.getAggregation(getFunctionByDisplayName(cmbFunction.getText()).getName()).getDataType()));
			if (!StringUtil.isEmpty(type) && !type.equals(cmbType.getText())) {
				if (!canProcessFunctionTypeError(cmbFunction.getText(), cmbType.getText(), type)) {
					return false;
				}
			}
			// check expression is vaid for parameter type
			// first get expression column or binding.
			IAggrFunction function = getFunctionByDisplayName(cmbFunction.getText());
			if (function != null) {
				DataSetHandle dataSetHandle = DEUtil.getFirstDataSet(this.bindingHolder);
				List<ResultSetColumn> columnList = null;
				if (dataSetHandle != null) {
					CachedMetaDataHandle meta = dataSetHandle.getCachedMetaDataHandle();
					if (meta == null) {
						DataSetUIUtil.updateColumnCache(dataSetHandle);
						meta = dataSetHandle.getCachedMetaDataHandle();
					}
					columnList = meta.getResultSet().getListValue();
				}

				List<ComputedColumnHandle> bindingList = DEUtil.getAllColumnBindingList(this.bindingHolder, true);

				loop: for (IParameterDefn param : function.getParameterDefn()) {
					if (param.isDataField()) {
						String[] expression = getControlValue(paramsMap.get(param.getName()));
						if (expression != null) {
							if (bindingList != null) {
								String bindingName = ExpressionUtil.getColumnBindingName(expression[0]);
								if (bindingName != null)
									for (ComputedColumnHandle bindingHandle : bindingList) {
										if (bindingHandle.getName().equals(bindingName)) {
											if (!param.supportDataType(
													DataAdapterUtil.adaptModelDataType(bindingHandle.getDataType()))) {
												if (!canProcessParamTypeError(expression[0], param.getDisplayName())) {
													return false;
												}
												continue loop;
											}
										}
									}
							}

							if (columnList != null) {
								String columnName = ExpressionUtil.getColumnName(expression[0]);
								if (columnName != null)
									for (ResultSetColumn column : columnList) {
										if (column.getColumnName().equals(columnName)) {
											if (!param.supportDataType(
													DataAdapterUtil.adaptModelDataType(column.getDataType()))) {
												if (!canProcessParamTypeError(expression[0], param.getDisplayName())) {
													return false;
												}
												continue loop;
											}
										}
									}
							}
						}
					}
				}
			}

		} catch (BirtException e) {
		}
		return true;
	}

	private boolean canProcessFunctionTypeError(String function, String type, String recommended) {
		MessageDialog dialog = new MessageDialog(UIUtil.getDefaultShell(), Messages.getString("Warning"), //$NON-NLS-1$
				null, Messages.getFormattedString("BindingDialogHelper.warning.function", //$NON-NLS-1$
						new String[] { recommended }),
				MessageDialog.WARNING,
				new String[] { Messages.getString(Messages.getString("BindingDialogHelper.warning.button.yes")), //$NON-NLS-1$
						Messages.getString(Messages.getString("BindingDialogHelper.warning.button.no")) //$NON-NLS-1$
				}, 0);
		return dialog.open() == 0;
	}

	private boolean canProcessParamTypeError(String expression, String parameter) {
		MessageDialog dialog = new MessageDialog(UIUtil.getDefaultShell(), Messages.getString("Warning"), //$NON-NLS-1$
				null, Messages.getFormattedString("BindingDialogHelper.warning.parameter", //$NON-NLS-1$
						new String[] { expression, parameter }),
				MessageDialog.WARNING, new String[] { Messages.getString("BindingDialogHelper.warning.button.yes"), //$NON-NLS-1$
						Messages.getString("BindingDialogHelper.warning.button.no") //$NON-NLS-1$
				}, 0);
		return dialog.open() == 0;
	}

	private String[] getBaseNames() {
		List<String> resources = SessionHandleAdapter.getInstance().getReportDesignHandle().getIncludeResources();
		if (resources == null)
			return null;
		else
			return resources.toArray(new String[0]);
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

	private void updateRemoveBtnState() {
		btnRemoveDisplayNameID.setEnabled(txtDisplayNameID.getText().equals(EMPTY_STRING) ? false : true);
	}

	private boolean isEditModal = false;

	public void setEditModal(boolean isEditModal) {
		this.isEditModal = isEditModal;
	}

	public boolean isEditModal() {
		return isEditModal;
	}

	public void modifyDialogContent() {
		if (hasInitDialog && isEditModal() && hasModified == false) {
			hasModified = true;
			validate();
		}
	}

	private boolean hasModified = false;
	protected Button btnAllowExport;
}
