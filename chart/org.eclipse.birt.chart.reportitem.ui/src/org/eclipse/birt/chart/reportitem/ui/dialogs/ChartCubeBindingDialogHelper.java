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

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.impl.ChartModelHelper;
import org.eclipse.birt.chart.reportitem.api.ChartCubeUtil;
import org.eclipse.birt.chart.reportitem.api.ChartItemUtil;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.util.ChartExpressionUtil.ExpressionCodec;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.aggregation.AggregationManager;
import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.data.engine.api.aggregation.IParameterDefn;
import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.data.adapter.api.DataAdapterUtil;
import org.eclipse.birt.report.designer.data.ui.util.DataUtil;
import org.eclipse.birt.report.designer.internal.ui.dialogs.AbstractBindingDialogHelper;
import org.eclipse.birt.report.designer.internal.ui.dialogs.ResourceEditDialog;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionButtonUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.BindingExpressionProvider;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.AggregationArgumentHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionHandle;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.AggregationArgument;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * This is for a chart using a cube data source
 * 
 */

public class ChartCubeBindingDialogHelper extends AbstractBindingDialogHelper {

	protected static final String NAME = Messages.getString("BindingDialogHelper.text.Name"); //$NON-NLS-1$
	protected static final String DATA_TYPE = Messages.getString("BindingDialogHelper.text.DataType"); //$NON-NLS-1$
	protected static final String FUNCTION = Messages.getString("BindingDialogHelper.text.Function"); //$NON-NLS-1$
	protected static final String DATA_FIELD = Messages.getString("BindingDialogHelper.text.DataField"); //$NON-NLS-1$
	protected static final String FILTER_CONDITION = Messages.getString("BindingDialogHelper.text.Filter"); //$NON-NLS-1$
	protected static final String AGGREGATE_ON = Messages.getString("BindingDialogHelper.text.AggOn"); //$NON-NLS-1$
	protected static final String EXPRESSION = Messages.getString("BindingDialogHelper.text.Expression"); //$NON-NLS-1$
	protected static final String ALL = Messages.getString("CrosstabBindingDialogHelper.AggOn.All"); //$NON-NLS-1$
	protected static final String DISPLAY_NAME = Messages.getString("BindingDialogHelper.text.displayName"); //$NON-NLS-1$
	protected static final String DISPLAY_NAME_ID = Messages.getString("BindingDialogHelper.text.displayNameID"); //$NON-NLS-1$
	protected static final String DEFAULT_ITEM_NAME = Messages.getString("BindingDialogHelper.bindingName.dataitem"); //$NON-NLS-1$
	protected static final String DEFAULT_AGGREGATION_NAME = Messages
			.getString("BindingDialogHelper.bindingName.aggregation"); //$NON-NLS-1$

	protected static final IChoiceSet DATA_TYPE_CHOICE_SET = DEUtil.getMetaDataDictionary()
			.getStructure(ComputedColumn.COMPUTED_COLUMN_STRUCT).getMember(ComputedColumn.DATA_TYPE_MEMBER)
			.getAllowedChoices();
	protected static final IChoice[] DATA_TYPE_CHOICES = DATA_TYPE_CHOICE_SET.getChoices(null);
	protected String[] dataTypes = ChoiceSetFactory.getDisplayNamefromChoiceSet(DATA_TYPE_CHOICE_SET);

	private Text txtName, txtFilter, txtExpression;
	private Combo cmbType, cmbFunction, cmbAggOn;
	private Composite paramsComposite;
	private Button btnDisplayNameID;
	private Map<String, Control> paramsMap = new HashMap<String, Control>();

	private Composite composite;
	private Text txtDisplayName, txtDisplayNameID;
	private ComputedColumn newBinding;
	private CLabel messageLine;
	private Label lbName, lbDisplayNameID;
	private Object container;
	protected final ExpressionCodec exprCodec = ChartModelHelper.instance().createExpressionCodec();

	public void createContent(Composite parent) {
		composite = parent;

		((GridLayout) composite.getLayout()).numColumns = 3;

		lbName = new Label(composite, SWT.NONE);
		lbName.setText(NAME);

		txtName = new Text(composite, SWT.BORDER);

		GridData gd = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		gd.horizontalSpan = 2;
		gd.widthHint = 200;
		txtName.setLayoutData(gd);

		txtName.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				validate();
			}

		});

		lbDisplayNameID = new Label(composite, SWT.NONE);
		lbDisplayNameID.setText(DISPLAY_NAME_ID);
		lbDisplayNameID.addTraverseListener(new TraverseListener() {

			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_MNEMONIC && e.doit) {
					e.detail = SWT.TRAVERSE_NONE;
					openKeySelectionDialog();
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

		new Label(composite, SWT.NONE).setText(DISPLAY_NAME);
		txtDisplayName = new Text(composite, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		txtDisplayName.setLayoutData(gd);

		new Label(composite, SWT.NONE).setText(DATA_TYPE);
		cmbType = new Combo(composite, SWT.BORDER | SWT.READ_ONLY);
		cmbType.setLayoutData(gd);

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
			txtDisplayNameID.setText(result[0]);
			txtDisplayName.setText(result[1]);
		}
	}

	public void initDialog() {
		txtDisplayName.setFocus();
		if (isAggregate()) {
			initFunction();
			initFilter();
			initAggOn();
		}

		if (getBinding() == null)// create
		{
			setTypeSelect(dataTypes[0]);
			this.newBinding = StructureFactory.newComputedColumn(getBindingHolder(),
					isAggregate() ? DEFAULT_AGGREGATION_NAME : DEFAULT_ITEM_NAME);
			setName(this.newBinding.getName());
		} else {
			setName(getBinding().getName());
			setDisplayName(getBinding().getDisplayName());
			setDisplayNameID(getBinding().getDisplayNameID());
			if (getBinding().getDataType() != null) {
				if (DATA_TYPE_CHOICE_SET.findChoice(getBinding().getDataType()) != null)
					setTypeSelect(DATA_TYPE_CHOICE_SET.findChoice(getBinding().getDataType()).getDisplayName());
				else
					// the old type 'any'
					cmbType.setText(""); //$NON-NLS-1$
			}

			if (txtExpression != null) {
				ExpressionButtonUtil.initExpressionButtonControl(txtExpression, getBinding(),
						ComputedColumn.EXPRESSION_MEMBER);
			}
		}

		if (this.getBinding() != null) {
			this.txtName.setEnabled(false);
		}

		validate();
	}

	private void initAggOn() {
		ReportItemHandle handle = getBindingHolder();
		String[] aggOns = getAggOns(handle);
		cmbAggOn.setItems(aggOns);

		StringBuffer aggstr = new StringBuffer();
		if (getBinding() != null) {
			List aggOnList = getBinding().getAggregateOnList();
			int i = 0;
			for (Iterator iterator = aggOnList.iterator(); iterator.hasNext();) {
				if (i > 0)
					aggstr.append(","); //$NON-NLS-1$
				String name = (String) iterator.next();
				aggstr.append(name);
				i++;
			}
		}

		for (int j = 0; j < aggOns.length; j++) {
			if (aggOns[j].equals(aggstr.toString())) {
				cmbAggOn.select(j);
				return;
			}
		}
		cmbAggOn.select(0);
	}

	private String[] getAggOns(ReportItemHandle handle) {
		String catExpr = null, yopExpr = null;
		List<String> aggOnList = new ArrayList<String>();
		aggOnList.add(ALL);

		Chart chart;
		if (container instanceof ChartWizardContext) {
			chart = ((ChartWizardContext) container).getModel();
		} else {
			chart = ChartItemUtil.getChartFromHandle((ExtendedItemHandle) handle);
		}

		SeriesDefinition category = ChartUIUtil.getBaseSeriesDefinitions(chart).get(0);
		String catName = exprCodec.getCubeBindingName(ChartUIUtil.getDataQuery(category, 0).getDefinition(), true);
		if (catName != null) {
			for (Iterator<ComputedColumnHandle> bindings = handle.getColumnBindings().iterator(); bindings.hasNext();) {
				ComputedColumnHandle bindingHandle = bindings.next();
				if (bindingHandle.getName().equalsIgnoreCase(catName)) {
					ChartItemUtil.loadExpression(exprCodec, bindingHandle);
					String[] cat = exprCodec.getLevelNames();
					catExpr = cat[0] + "/" + cat[1]; //$NON-NLS-1$
					aggOnList.add(catExpr);
				}
			}
		}

		SeriesDefinition yopgrouping = ChartUIUtil.getOrthogonalSeriesDefinitions(chart, 0).get(0);
		String yopName = exprCodec.getCubeBindingName(yopgrouping.getQuery().getDefinition(), true);
		if (yopName != null) {
			for (Iterator<ComputedColumnHandle> bindings = handle.getColumnBindings().iterator(); bindings.hasNext();) {
				ComputedColumnHandle bindingHandle = bindings.next();
				if (bindingHandle.getName().equalsIgnoreCase(yopName)) {
					ChartItemUtil.loadExpression(exprCodec, bindingHandle);
					String[] yop = exprCodec.getLevelNames();
					yopExpr = yop[0] + "/" + yop[1]; //$NON-NLS-1$
					aggOnList.add(yopExpr);
				}
			}
		}

		if (catName != null && yopName != null) {
			aggOnList.add(yopExpr + "," + yopExpr); //$NON-NLS-1$
		}

		return aggOnList.toArray(new String[aggOnList.size()]);
	}

	private void initFilter() {
		ExpressionButtonUtil.initExpressionButtonControl(txtFilter, binding, ComputedColumn.FILTER_MEMBER);
	}

	private void initFunction() {
		cmbFunction.setItems(getFunctionDisplayNames());
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
		for (Iterator iterator = binding.argumentsIterator(); iterator.hasNext();) {
			AggregationArgumentHandle arg = (AggregationArgumentHandle) iterator.next();
			if (paramsMap.containsKey(arg.getName())) {
				if (arg.getValue() != null) {
					Control control = paramsMap.get(arg.getName());
					if (ExpressionButtonUtil.getExpressionButton(control) != null) {
						ExpressionButtonUtil.initExpressionButtonControl(control, arg,
								AggregationArgument.VALUE_MEMBER);
					} else {
						ExpressionHandle expr = arg.getExpressionProperty(AggregationArgument.VALUE_MEMBER);
						if (expr != null && expr.getStringExpression() != null) {
							((Combo) control).setText(expr.getStringExpression());
						}
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
		return displayNames;
	}

	private IAggrFunction getFunctionByDisplayName(String displayName) {
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

	private String getFunctionDisplayName(String function) {
		try {
			return DataUtil.getAggregationManager().getAggregation(function).getDisplayName();
		} catch (BirtException e) {
			ExceptionHandler.handle(e);
			return null;
		}
	}

	private IAggrFunction[] getFunctions() {
		try {
			List aggrInfoList = DataUtil.getAggregationManager().getAggregations(AggregationManager.AGGR_XTAB);
			return (IAggrFunction[]) aggrInfoList.toArray(new IAggrFunction[0]);
		} catch (BirtException e) {
			ExceptionHandler.handle(e);
			return new IAggrFunction[0];
		}
	}

	private String getDataTypeDisplayName(String dataType) {
		for (int i = 0; i < DATA_TYPE_CHOICES.length; i++) {
			if (dataType.equals(DATA_TYPE_CHOICES[i].getName())) {
				return DATA_TYPE_CHOICES[i].getDisplayName();
			}
		}

		return ""; //$NON-NLS-1$
	}

	/**
	 * fill the cmbDataField with binding holder's bindings
	 */
	private void initDataFields(Combo cmbDataField) {
		String[] items = getMesures();
		cmbDataField.setItems(items);
		if (binding != null && binding.getExpression() != null) {
			for (int i = 0; i < items.length; i++) {
				if (items[i].equals(binding.getExpression())) {
					cmbDataField.select(i);
				}
			}
		}
	}

	private String[] getMesures() {
		List<MeasureHandle> mesureList = ChartCubeUtil.getAllMeasures(getBindingHolder().getCube());
		String[] mesures = new String[mesureList.size() + 1];
		mesures[0] = ""; //$NON-NLS-1$
		for (int i = 1; i < mesures.length; i++) {
			mesures[i] = DEUtil.getExpression(mesureList.get(i - 1));
		}
		return mesures;
	}

	private void setName(String name) {
		if (name != null && txtName != null)
			txtName.setText(name);
	}

	private void setDisplayNameID(String displayNameID) {
		if (displayNameID != null && txtDisplayNameID != null)
			txtDisplayNameID.setText(displayNameID);
	}

	private void setDisplayName(String displayName) {
		if (displayName != null && txtDisplayName != null)
			txtDisplayName.setText(displayName);
	}

	private void setTypeSelect(String typeSelect) {
		if (dataTypes != null && cmbType != null) {
			cmbType.setItems(dataTypes);
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

	private void createAggregateSection(Composite composite) {

		new Label(composite, SWT.NONE).setText(FUNCTION);
		cmbFunction = new Combo(composite, SWT.BORDER | SWT.READ_ONLY);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		cmbFunction.setLayoutData(gd);

		// WidgetUtil.createGridPlaceholder( composite, 1, false );

		cmbFunction.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				handleFunctionSelectEvent();
				validate();
			}
		});

		paramsComposite = new Composite(composite, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		gridData.horizontalSpan = 3;
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

		new Label(composite, SWT.NONE).setText(FILTER_CONDITION);
		txtFilter = new Text(composite, SWT.BORDER);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		txtFilter.setLayoutData(gridData);

		createExpressionButton(composite, txtFilter);

		Label lblAggOn = new Label(composite, SWT.NONE);
		lblAggOn.setText(AGGREGATE_ON);
		gridData = new GridData();
		gridData.verticalAlignment = GridData.BEGINNING;
		lblAggOn.setLayoutData(gridData);

		cmbAggOn = new Combo(composite, SWT.BORDER | SWT.READ_ONLY);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		cmbAggOn.setLayoutData(gridData);

	}

	private void createCommonSection(Composite composite) {
		new Label(composite, SWT.NONE).setText(EXPRESSION);
		txtExpression = new Text(composite, SWT.BORDER);
		txtExpression.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		createExpressionButton(composite, txtExpression);
		txtExpression.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				validate();
			}

		});
	}

	private void createMessageSection(Composite composite) {
		messageLine = new CLabel(composite, SWT.NONE);
		GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);
		layoutData.horizontalSpan = 3;
		messageLine.setLayoutData(layoutData);
	}

	protected void handleFunctionSelectEvent() {
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

				for (IParameterDefn param : params) {
					Label lblParam = new Label(paramsComposite, SWT.NONE);
					lblParam.setText(param.getDisplayName() + ":"); //$NON-NLS-1$
					GridData gd = new GridData();
					gd.widthHint = lblParam.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
					if (gd.widthHint < width)
						gd.widthHint = width;
					lblParam.setLayoutData(gd);

					if (param.isDataField()) {
						final Combo cmbDataField = new Combo(paramsComposite, SWT.BORDER);
						gd = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
						gd.horizontalSpan = 2;
						cmbDataField.setLayoutData(gd);

						cmbDataField.addModifyListener(new ModifyListener() {

							public void modifyText(ModifyEvent e) {
								validate();
							}
						});

						initDataFields(cmbDataField);

						paramsMap.put(param.getName(), cmbDataField);
					} else {
						Text txtParam = new Text(paramsComposite, SWT.BORDER);
						txtParam.addModifyListener(new ModifyListener() {

							public void modifyText(ModifyEvent e) {
								validate();
							}
						});
						GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
						gridData.horizontalIndent = 0;
						txtParam.setLayoutData(gridData);
						createExpressionButton(paramsComposite, txtParam);
						paramsMap.put(param.getName(), txtParam);
					}
				}
			} else {
				((GridData) paramsComposite.getLayoutData()).heightHint = 0;
				// ( (GridData) argsComposite.getLayoutData( ) ).exclude = true;
			}

			// this.cmbDataField.setEnabled( function.needDataField( ) );
			try {
				cmbType.setText(getDataTypeDisplayName(DataAdapterUtil.adapterToModelDataType(
						DataUtil.getAggregationManager().getAggregation(function.getName()).getDataType())));
			} catch (BirtException e) {
				ExceptionHandler.handle(e);
			}
		} else {
			((GridData) paramsComposite.getLayoutData()).heightHint = 0;
			// ( (GridData) argsComposite.getLayoutData( ) ).exclude = true;
			// new Label( argsComposite, SWT.NONE ).setText( "no args" );
		}
		paramsComposite.layout();
		composite.layout();
		setContentSize(composite);
	}

	private void createExpressionButton(final Composite parent, final Text text) {
		if (expressionProvider == null) {
			expressionProvider = new BindingExpressionProvider(bindingHolder, binding);
		}

		ExpressionButtonUtil.createExpressionButton(parent, text, expressionProvider, bindingHolder);
	}

	public void validate() {
		if (txtName != null && (txtName.getText() == null || txtName.getText().trim().equals(""))) //$NON-NLS-1$
		{
			dialog.setCanFinish(false);
		} else if (txtExpression != null
				&& (txtExpression.getText() == null || txtExpression.getText().trim().equals(""))) //$NON-NLS-1$
		{
			dialog.setCanFinish(false);
		} else {
			if (this.binding == null)// create bindnig, we should check if
			// the binding name already exists.
			{
				for (Iterator iterator = this.bindingHolder.getColumnBindings().iterator(); iterator.hasNext();) {
					ComputedColumnHandle computedColumn = (ComputedColumnHandle) iterator.next();
					if (computedColumn.getName().equals(txtName.getText())) {
						dialog.setCanFinish(false);
						this.messageLine.setText(Messages.getFormattedString("BindingDialogHelper.error.nameduplicate", //$NON-NLS-1$
								new Object[] { txtName.getText() }));
						this.messageLine.setImage(
								PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_ERROR_TSK));
						return;
					}
				}
			}
			dialog.setCanFinish(true);
			this.messageLine.setText(""); //$NON-NLS-1$
			this.messageLine.setImage(null);

			if (txtExpression != null && (txtExpression.getText() == null || txtExpression.getText().trim().equals(""))) //$NON-NLS-1$
			{
				dialog.setCanFinish(false);
				return;
			}
			if (isAggregate()) {
				try {
					IAggrFunction aggregation = DataUtil.getAggregationManager()
							.getAggregation(getFunctionByDisplayName(cmbFunction.getText()).getName());

					if (aggregation.getParameterDefn().length > 0) {
						IParameterDefn[] parameters = aggregation.getParameterDefn();
						for (IParameterDefn param : parameters) {
							if (!param.isOptional()) {
								String paramValue = getControlValue(paramsMap.get(param.getName()));
								if (paramValue == null || paramValue.trim().equals("")) //$NON-NLS-1$
								{
									dialog.setCanFinish(false);
									return;
								}
							}
						}
					}
				} catch (BirtException e) {
					// TODO show error message in message panel
				}
			}
			dialog.setCanFinish(true);
		}
	}

	public boolean differs(ComputedColumnHandle binding) {
		if (isAggregate()) {
			if (!strEquals(binding.getName(), txtName.getText()))
				return true;
			if (!strEquals(binding.getDisplayName(), txtDisplayName.getText()))
				return true;
			if (!strEquals(binding.getDisplayNameID(), txtDisplayNameID.getText()))
				return true;
			if (!strEquals(binding.getDataType(), getDataType()))
				return true;
			if (!strEquals(binding.getAggregateFunction(), getFunctionByDisplayName(cmbFunction.getText()).getName()))
				return true;
			if (!exprEquals((Expression) binding.getExpressionProperty(ComputedColumn.FILTER_MEMBER).getValue(),
					ExpressionButtonUtil.getExpression(txtFilter)))
				return true;
			if (!strEquals(cmbAggOn.getText(), binding.getAggregateOn()))
				return true;

			for (Iterator iterator = binding.argumentsIterator(); iterator.hasNext();) {
				AggregationArgumentHandle handle = (AggregationArgumentHandle) iterator.next();
				if (paramsMap.containsKey(handle.getName())) {
					if (!exprEquals(
							(Expression) handle.getExpressionProperty(AggregationArgument.VALUE_MEMBER).getValue(),
							ExpressionButtonUtil.getExpression(paramsMap.get(handle.getName())))) {
						return true;
					}
				} else {
					return true;
				}
			}

		} else {
			if (!strEquals(txtName.getText(), binding.getName()))
				return true;
			if (!strEquals(txtDisplayName.getText(), binding.getDisplayName()))
				return true;
			if (!strEquals(txtDisplayNameID.getText(), binding.getDisplayNameID()))
				return true;
			if (!strEquals(getDataType(), binding.getDataType()))
				return true;
			if (!exprEquals(ExpressionButtonUtil.getExpression(txtExpression),
					(Expression) binding.getExpressionProperty(ComputedColumn.EXPRESSION_MEMBER).getValue()))
				return true;
		}
		return false;
	}

	private boolean exprEquals(Expression left, Expression right) {
		if (left == null && right == null) {
			return true;
		} else if (left == null && right != null) {
			return false;
		} else if (left != null && right == null) {
			return false;
		} else if (left.getStringExpression() == null && right.getStringExpression() == null)
			return true;
		else if (strEquals(left.getStringExpression(), right.getStringExpression())
				&& strEquals(left.getType(), right.getType()))
			return true;
		return false;
	}

	private String getControlValue(Control control) {
		if (control instanceof Text) {
			return ((Text) control).getText();
		} else if (control instanceof Combo) {
			return ((Combo) control).getText();
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

	private String getDataType() {
		for (int i = 0; i < DATA_TYPE_CHOICES.length; i++) {
			if (DATA_TYPE_CHOICES[i].getDisplayName().equals(cmbType.getText())) {
				return DATA_TYPE_CHOICES[i].getName();
			}
		}
		return ""; //$NON-NLS-1$
	}

	public ComputedColumnHandle editBinding(ComputedColumnHandle binding) throws SemanticException {
		if (isAggregate()) {
			binding.setDisplayName(txtDisplayName.getText());
			binding.setDisplayNameID(txtDisplayNameID.getText());
			for (int i = 0; i < DATA_TYPE_CHOICES.length; i++) {
				if (DATA_TYPE_CHOICES[i].getDisplayName().equals(cmbType.getText())) {
					binding.setDataType(DATA_TYPE_CHOICES[i].getName());
					break;
				}
			}

			binding.setAggregateFunction(getFunctionByDisplayName(cmbFunction.getText()).getName());

			ExpressionButtonUtil.saveExpressionButtonControl(txtFilter, binding, ComputedColumn.FILTER_MEMBER);

			binding.clearAggregateOnList();
			String aggStr = cmbAggOn.getText();
			StringTokenizer token = new StringTokenizer(aggStr, ","); //$NON-NLS-1$

			while (token.hasMoreTokens()) {
				String agg = token.nextToken();
				if (!agg.equals(ALL))
					binding.addAggregateOn(agg);
			}

			binding.clearArgumentList();
			binding.setExpression(null);

			for (Iterator<String> iterator = paramsMap.keySet().iterator(); iterator.hasNext();) {
				String arg = iterator.next();
				String value = getControlValue(paramsMap.get(arg));
				if (value != null) {
					AggregationArgument argHandle = StructureFactory.createAggregationArgument();
					argHandle.setName(arg);
					if (ExpressionButtonUtil.getExpressionButton(paramsMap.get(arg)) != null) {
						ExpressionButtonUtil.saveExpressionButtonControl(paramsMap.get(arg), argHandle,
								AggregationArgument.VALUE_MEMBER);
					} else {
						Expression expression = new Expression(value, ExpressionType.JAVASCRIPT);
						argHandle.setExpressionProperty(AggregationArgument.VALUE_MEMBER, expression);
					}
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

			if (ExpressionButtonUtil.getExpressionButton(txtExpression) != null) {
				ExpressionButtonUtil.saveExpressionButtonControl(txtExpression, binding,
						ComputedColumn.EXPRESSION_MEMBER);
			} else {
				Expression expression = new Expression(getControlValue(txtExpression), ExpressionType.JAVASCRIPT);
				binding.setExpressionProperty(AggregationArgument.VALUE_MEMBER, expression);
			}
		}
		return binding;
	}

	public ComputedColumnHandle newBinding(ReportItemHandle bindingHolder, String name) throws SemanticException {
		ComputedColumn column = StructureFactory.newComputedColumn(bindingHolder,
				name == null ? txtName.getText() : name);
		ComputedColumnHandle binding = DEUtil.addColumn(bindingHolder, column, true);
		return editBinding(binding);
	}

	public void setContainer(Object container) {
		this.container = container;
	}

	public boolean canProcessAggregation() {
		return true;
	}

	private URL[] getAvailableResourceUrls() {
		List<URL> urls = new ArrayList<URL>();
		String[] baseNames = getBaseNames();
		if (baseNames == null)
			return urls.toArray(new URL[0]);
		else {
			for (int i = 0; i < baseNames.length; i++) {
				URL url = getModuleHandle().findResource(baseNames[i], IResourceLocator.MESSAGE_FILE);
				if (url != null)
					urls.add(url);
			}
			return urls.toArray(new URL[0]);
		}
	}

	private String[] getBaseNames() {
		List<String> resources = getModuleHandle().getIncludeResources();
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
				urls[i] = getModuleHandle().findResource(baseNames[i], IResourceLocator.MESSAGE_FILE);
			}
			return urls;
		}
	}

	protected ModuleHandle getModuleHandle() {
		return bindingHolder.getModuleHandle();
	}

}
