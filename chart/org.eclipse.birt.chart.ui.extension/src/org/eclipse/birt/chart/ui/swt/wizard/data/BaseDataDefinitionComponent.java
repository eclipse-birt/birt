/*******************************************************************************
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
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard.data;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.chart.aggregate.IAggregateFunction;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.attribute.GroupingUnitType;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.SeriesGrouping;
import org.eclipse.birt.chart.model.data.impl.DataFactoryImpl;
import org.eclipse.birt.chart.model.data.impl.QueryImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesGroupingImpl;
import org.eclipse.birt.chart.model.impl.ChartModelHelper;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.ColumnBindingInfo;
import org.eclipse.birt.chart.ui.swt.DataDefinitionTextManager;
import org.eclipse.birt.chart.ui.swt.DataTextDropListener;
import org.eclipse.birt.chart.ui.swt.DefaultSelectDataComponent;
import org.eclipse.birt.chart.ui.swt.IQueryExpressionManager;
import org.eclipse.birt.chart.ui.swt.SimpleTextTransfer;
import org.eclipse.birt.chart.ui.swt.composites.BaseGroupSortingDialog;
import org.eclipse.birt.chart.ui.swt.composites.GroupSortingDialog;
import org.eclipse.birt.chart.ui.swt.fieldassist.CComboAssistField;
import org.eclipse.birt.chart.ui.swt.fieldassist.FieldAssistHelper;
import org.eclipse.birt.chart.ui.swt.fieldassist.TextAssistField;
import org.eclipse.birt.chart.ui.swt.interfaces.IAssistField;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartDataSheet;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.IExpressionButton;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.chart.ui.swt.type.GanttChart;
import org.eclipse.birt.chart.ui.swt.wizard.ChartAdapter;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartUIConstants;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.birt.chart.util.ChartExpressionUtil.ExpressionCodec;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class BaseDataDefinitionComponent extends DefaultSelectDataComponent
		implements SelectionListener, IQueryExpressionManager {

	protected Composite cmpTop;

	private Label lblDesc;

	private CCombo cmbDefinition;

	protected Text txtDefinition = null;

	private IExpressionButton btnBuilder = null;

	protected Button btnGroup = null;

	protected Query query = null;

	protected SeriesDefinition seriesdefinition = null;

	protected ChartWizardContext context = null;

	private String description = ""; //$NON-NLS-1$

	private String tooltipWhenBlank = Messages.getString("BaseDataDefinitionComponent.Tooltip.InputValueExpression"); //$NON-NLS-1$

	private final String queryType;

	private int style = BUTTON_NONE;

	private AggregateEditorComposite fAggEditorComposite;

	/** Indicates no button */
	public static final int BUTTON_NONE = 0;

	/** Indicates button for group sorting will be created */
	public static final int BUTTON_GROUP = 1;

	/** Indicates button for aggregation will be created */
	public static final int BUTTON_AGGREGATION = 2;

	protected final ExpressionCodec exprCodec = ChartModelHelper.instance().createExpressionCodec();

	private final SharedBindingHelper sbHelper = new SharedBindingHelper();

	/**
	 *
	 * @param queryType
	 * @param seriesdefinition
	 * @param query
	 * @param context
	 * @param sTitle
	 */
	public BaseDataDefinitionComponent(String queryType, SeriesDefinition seriesdefinition, Query query,
			ChartWizardContext context, String sTitle) {
		this(BUTTON_NONE, queryType, seriesdefinition, query, context, sTitle);
	}

	/**
	 *
	 *
	 * @param style            Specify buttons by using '|'. See
	 *                         {@link #BUTTON_GROUP}, {@link #BUTTON_NONE},
	 *                         {@link #BUTTON_AGGREGATION}
	 * @param queryType        query type. See
	 *                         {@link ChartUIConstants#QUERY_CATEGORY},
	 *                         {@link ChartUIConstants#QUERY_VALUE},
	 *                         {@link ChartUIConstants#QUERY_OPTIONAL}
	 * @param seriesdefinition
	 * @param query
	 * @param context
	 * @param sTitle
	 */
	public BaseDataDefinitionComponent(int style, String queryType, SeriesDefinition seriesdefinition, Query query,
			ChartWizardContext context, String sTitle) {
		super();
		this.query = query;
		this.queryType = queryType;
		this.seriesdefinition = seriesdefinition;
		this.context = context;
		this.style = style;
	}

	@Override
	public Composite createArea(Composite parent) {
		int numColumns = 2;
		if (description != null && description.length() > 0) {
			numColumns++;
		}
		if ((style & BUTTON_AGGREGATION) == BUTTON_AGGREGATION) {
			numColumns++;
		}
		if ((style & BUTTON_GROUP) == BUTTON_GROUP) {
			numColumns++;
		}

		cmpTop = new Composite(parent, SWT.NONE);
		{
			GridLayout glContent = new GridLayout();
			glContent.numColumns = numColumns;
			glContent.marginHeight = 0;
			glContent.marginWidth = 0;
			glContent.horizontalSpacing = 2;
			cmpTop.setLayout(glContent);
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			cmpTop.setLayoutData(gd);
		}

		lblDesc = null;
		if (description != null && description.length() > 0) {
			lblDesc = new Label(cmpTop, SWT.NONE);
			updateLabel();
		}

		if ((style & BUTTON_AGGREGATION) == BUTTON_AGGREGATION) {
			createAggregationItem(cmpTop);
		}

		boolean isSharingChart = context.getDataServiceProvider().checkState(IDataServiceProvider.SHARE_CHART_QUERY);

		final Object[] predefinedQuery = context.getPredefinedQuery(queryType);
		sbHelper.reset(predefinedQuery);

		// always use combo box #66704
		boolean needComboField = true;

		IDataServiceProvider provider = context.getDataServiceProvider();
		// fix regression of #66704, keep the status.
		final boolean needComboStatus = (predefinedQuery != null && predefinedQuery.length > 0
				&& (provider.checkState(IDataServiceProvider.SHARE_QUERY)
						|| provider.checkState(IDataServiceProvider.HAS_CUBE)
						|| (provider.checkState(IDataServiceProvider.INHERIT_CUBE)
								&& !provider.checkState(IDataServiceProvider.PART_CHART))
						|| provider.checkState(IDataServiceProvider.INHERIT_COLUMNS_GROUPS)))
				&& !isSharingChart;
		boolean hasContentAssist = (!isSharingChart && predefinedQuery != null && predefinedQuery.length > 0);
		IAssistField assistField = null;
		if (needComboField) {
			// Create a composite to decorate combo field for the content assist function.
			Composite control = new Composite(cmpTop, SWT.NONE);
			GridData gd = new GridData(GridData.FILL_BOTH);
			gd.widthHint = 80;
			control.setLayoutData(gd);
			GridLayout gl = new GridLayout();
			FieldAssistHelper.getInstance().initDecorationMargin(gl);
			control.setLayout(gl);

			cmbDefinition = new CCombo(control,
					context.getDataServiceProvider().checkState(IDataServiceProvider.PART_CHART)
							? SWT.READ_ONLY | SWT.BORDER
							: SWT.BORDER);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.grabExcessHorizontalSpace = true;
			cmbDefinition.setLayoutData(gd);

			// Initialize content assist.
			if (hasContentAssist) {
				assistField = new CComboAssistField(cmbDefinition, null, null);
			}

			cmbDefinition.addListener(SWT.Selection, new Listener() {

				@Override
				public void handleEvent(Event event) {
					String oldQuery = query.getDefinition() == null ? "" : query.getDefinition(); //$NON-NLS-1$
					// Combo may be disposed, so cache the text first
					String text = btnBuilder.getExpression();

					// Do nothing for the same query
					if (!isTableSharedBinding() && !(isInXTabNonAggrCellAndInheritCube()) && text.equals(oldQuery)) {
						return;
					}

					// Set category/Y optional expression by value series
					// expression if it is crosstab sharing.
					if (!oldQuery.equals(text) && queryType == ChartUIConstants.QUERY_VALUE) {
						context.getDataServiceProvider().update(ChartUIConstants.QUERY_VALUE, text);
					}

					// Change direction once category query is changed in xtab
					// case
					if (context.getDataServiceProvider().checkState(IDataServiceProvider.PART_CHART)
							&& ChartUIConstants.QUERY_CATEGORY.equals(queryType)
							&& context.getModel() instanceof ChartWithAxes) {
						((ChartWithAxes) context.getModel()).setTransposed(cmbDefinition.getSelectionIndex() > 0);
					}

					if (needComboStatus && predefinedQuery.length == 0
							&& (getQuery().getDefinition() == null || getQuery().getDefinition().equals(""))) //$NON-NLS-1$
					{
						cmbDefinition.setEnabled(false);
						btnBuilder.setEnabled(false);
					}
				}
			});

		} else {
			Composite control = cmpTop;
			if (hasContentAssist) {
				// Create a composite to decorate text field for the content assist function.
				control = new Composite(cmpTop, SWT.NONE);
				GridData gd = new GridData(GridData.FILL_BOTH);
				gd.widthHint = 80;
				control.setLayoutData(gd);
				GridLayout gl = new GridLayout();
				FieldAssistHelper.getInstance().initDecorationMargin(gl);
				control.setLayout(gl);
			}

			txtDefinition = new Text(control, SWT.BORDER | SWT.SINGLE);
			GridData gdTXTDefinition = new GridData(GridData.FILL_HORIZONTAL);
			gdTXTDefinition.widthHint = 80;
			gdTXTDefinition.grabExcessHorizontalSpace = true;
			txtDefinition.setLayoutData(gdTXTDefinition);

			// Initialize content assist.
			if (hasContentAssist) {
				assistField = new TextAssistField(txtDefinition, null, null);
			}
		}

		try {
			btnBuilder = (IExpressionButton) context.getUIServiceProvider().invoke(
					IUIServiceProvider.Command.EXPRESS_BUTTON_CREATE, cmpTop, getInputControl(),
					context.getExtendedItem(), IUIServiceProvider.COMMAND_EXPRESSION_DATA_BINDINGS, new Listener() {

						@Override
						public void handleEvent(Event event) {
							onModifyExpression();
						}
					}, null, queryType);
		} catch (ChartException e) {
			WizardBase.displayException(e);
		}

		if (needComboStatus) {
			if ((predefinedQuery == null || predefinedQuery.length == 0)
					&& (getQuery().getDefinition() == null || getQuery().getDefinition().equals(""))) //$NON-NLS-1$
			{
				cmbDefinition.setEnabled(false);
				btnBuilder.setEnabled(false);
			}
		}

		btnBuilder.setPredefinedQuery(predefinedQuery);
		btnBuilder.setAssitField(assistField);

		if (query != null) {
			btnBuilder.setExpression(query.getDefinition());
		}

		// Listener for handling dropping of custom table header
		Control dropControl = getInputControl();
		DropTarget target = new DropTarget(dropControl, DND.DROP_COPY);
		Transfer[] types = { SimpleTextTransfer.getInstance() };
		target.setTransfer(types);
		// Add drop support
		target.addDropListener(new DataTextDropListener(dropControl, btnBuilder));
		// Add color manager
		DataDefinitionTextManager.getInstance().addDataDefinitionText(dropControl, this);

		if ((style & BUTTON_GROUP) == BUTTON_GROUP) {
			btnGroup = new Button(cmpTop, SWT.PUSH);
			GridData gdBTNGroup = new GridData();
			ChartUIUtil.setChartImageButtonSizeByPlatform(gdBTNGroup);
			btnGroup.setLayoutData(gdBTNGroup);
			btnGroup.setImage(UIHelper.getImage("icons/obj16/group.gif")); //$NON-NLS-1$
			btnGroup.addSelectionListener(this);
			btnGroup.setToolTipText(Messages.getString("BaseDataDefinitionComponent.Label.EditGroupSorting")); //$NON-NLS-1$
			ChartUIUtil.addScreenReaderAccessbility(btnGroup, btnGroup.getToolTipText());
		}

		// In shared binding, only support predefined query
		boolean isCubeNoMultiDimensions = (provider.checkState(IDataServiceProvider.HAS_CUBE)
				|| provider.checkState(IDataServiceProvider.SHARE_CROSSTAB_QUERY))
				&& !provider.checkState(IDataServiceProvider.MULTI_CUBE_DIMENSIONS);
		if (context.getDataServiceProvider().checkState(IDataServiceProvider.PART_CHART)
				|| context.getDataServiceProvider().checkState(IDataServiceProvider.SHARE_QUERY)) {
			// Sharing query with crosstab allows user to edit category and Y
			// optional expression, so here doesn't disable the text field if it
			// is SHARE_CROSSTAB_QUERY.
			if (!needComboStatus && cmbDefinition != null
					&& (!context.getDataServiceProvider().checkState(IDataServiceProvider.SHARE_CROSSTAB_QUERY)
							|| isSharingChart)) {
				// allow y-optional if contains definition
				if (!ChartUIConstants.QUERY_OPTIONAL.equals(queryType)
						|| !provider.checkState(IDataServiceProvider.SHARE_TABLE_QUERY)
						|| getQuery().getDefinition() == null || getQuery().getDefinition().trim().length() == 0) {
					cmbDefinition.setEnabled(false);
					btnBuilder.setEnabled(false);
				}
			}

			if (btnGroup != null) {
				btnGroup.setEnabled(false);
			}
		}

		// If current is 'Inherit columns & groups' and there is no group
		// defined in table, this case the Optional Y group UI should be
		// disabled.
		boolean disableOptionalY = context.getDataServiceProvider().checkState(
				IDataServiceProvider.INHERIT_COLUMNS_GROUPS) && ChartUIConstants.QUERY_OPTIONAL.equals(queryType)
				&& (predefinedQuery == null || predefinedQuery.length == 0);
		if (disableOptionalY) {
			getInputControl().setEnabled(false);
			btnBuilder.setEnabled(false);
			if (btnGroup != null) {
				btnGroup.setEnabled(false);
			}
		} else if (cmbDefinition != null && ChartUIConstants.QUERY_OPTIONAL.equals(queryType)
				&& isCubeNoMultiDimensions) {
			cmbDefinition.setEnabled(false);
			btnBuilder.setEnabled(false);
		}

		disableBtnGroup();

		setTooltipForInputControl();

		return cmpTop;
	}

	/*
	 * if:1.is cube 2. keep hierarchy is checked 3.current cmbDefinition is not top
	 * level then disable gourping and sorting dialog
	 * if(ChartUIUtil.isKeepCubeHierarchyAndIsNotTopLevel( wizardContext, query,
	 * keep_hierarchy ));
	 */
	protected void disableBtnGroup() {
		if (ChartUIConstants.QUERY_CATEGORY.equals(queryType) && query.getDefinition() != null
				&& !"".endsWith(query.getDefinition())) { //$NON-NLS-1$
			IDataServiceProvider dataServiceProvider = context.getDataServiceProvider();

			if (dataServiceProvider.checkState(IDataServiceProvider.IS_CUBE_AND_CATEGORY_NOT_TOP_LEVEL)) {
				btnGroup.setEnabled(false);
			}
		}
	}

	/**
	 * Check if current is using table shared binding.
	 *
	 * @return
	 * @since 2.3
	 */
	private boolean isTableSharedBinding() {
		return cmbDefinition != null && !cmbDefinition.isDisposed()
				&& (context.getDataServiceProvider().checkState(IDataServiceProvider.SHARE_QUERY)
						|| context.getDataServiceProvider().checkState(IDataServiceProvider.INHERIT_COLUMNS_GROUPS));
	}

	private boolean isInXTabNonAggrCellAndInheritCube() {
		IDataServiceProvider provider = context.getDataServiceProvider();
		int state = provider.getState();
		return (state & (IDataServiceProvider.HAS_DATA_SET | IDataServiceProvider.HAS_CUBE)) == 0
				&& (state & IDataServiceProvider.INHERIT_CUBE) != 0 && (state & IDataServiceProvider.SHARE_QUERY) == 0
				&& (state & IDataServiceProvider.PART_CHART) == 0;
	}

	@Override
	public void selectArea(boolean selected, Object data) {
		if (data instanceof Object[]) {
			Object[] array = (Object[]) data;
			seriesdefinition = (SeriesDefinition) array[0];
			query = (Query) array[1];
			updateText(query.getDefinition());
			DataDefinitionTextManager.getInstance().addDataDefinitionText(getInputControl(), this);
			setTooltipForInputControl();
			if (fAggEditorComposite != null) {
				fAggEditorComposite.setAggregation(query, seriesdefinition);
			}
		}
	}

	@Override
	public void dispose() {
		if (getInputControl() != null) {
			DataDefinitionTextManager.getInstance().removeDataDefinitionText(getInputControl());
		}
		super.dispose();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.
	 * events.SelectionEvent)
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {
		if (e.getSource().equals(btnGroup)) {
			handleGroupAction();
		}
	}

	/**
	 * Handle grouping/sorting action.
	 */
	protected void handleGroupAction() {
		SeriesDefinition sdBackup = seriesdefinition.copyInstance();
		GroupSortingDialog groupDialog = createGroupSortingDialog(sdBackup);

		if (groupDialog.open() == Window.OK) {
			if (!sdBackup.eIsSet(DataPackage.eINSTANCE.getSeriesDefinition_Sorting())) {
				seriesdefinition.eUnset(DataPackage.eINSTANCE.getSeriesDefinition_Sorting());
			} else {
				seriesdefinition.setSorting(sdBackup.getSorting());
			}

			seriesdefinition.setSortKey(sdBackup.getSortKey());
			seriesdefinition.getSortKey().eAdapters().addAll(seriesdefinition.eAdapters());

			seriesdefinition.setSortLocale(sdBackup.getSortLocale());

			if (sdBackup.isSetSortStrength()) {
				seriesdefinition.setSortStrength(sdBackup.getSortStrength());
			} else {
				seriesdefinition.unsetSortStrength();
			}

			seriesdefinition.setGrouping(sdBackup.getGrouping());
			seriesdefinition.getGrouping().eAdapters().addAll(seriesdefinition.eAdapters());
			ChartUIUtil.checkGroupType(context, context.getModel());
			ChartUIUtil.checkAggregateType(context);

			DataDefinitionTextManager.getInstance().updateTooltip();
		}
	}

	protected void onModifyExpression() {
		String newExpr = btnBuilder.getExpression();
		updateQuery(newExpr);
		setTooltipForInputControl();

		final Event e = new Event();
		e.widget = getInputControl();
		e.type = IChartDataSheet.EVENT_QUERY;
		e.detail = IChartDataSheet.DETAIL_UPDATE_COLOR_AND_TEXT;
		context.getDataSheet().notifyListeners(e);
	}

	/**
	 * Create instance of <code>GroupSortingDialog</code> for base series or Y
	 * series.
	 *
	 * @param sdBackup
	 * @return
	 */
	protected GroupSortingDialog createGroupSortingDialog(SeriesDefinition sdBackup) {
		return new BaseGroupSortingDialog(cmpTop.getShell(), context, sdBackup);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.
	 * swt.events.SelectionEvent)
	 */
	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
	}

	/**
	 * Set tooltip for input control.
	 */
	@Override
	public void setTooltipForInputControl() {
		Control control = getInputControl();
		if (control != null && !control.isDisposed()) {
			getInputControl().setToolTipText(getTooltipForDataText(ChartUIUtil.getText(control)));
		}
	}

	/**
	 * Sets the description in the left of data text box.
	 *
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	private String getTooltipForDataText(String queryText) {
		if (isTableSharedBinding()) {
			return cmbDefinition.getToolTipText();
		}
		if (queryText.trim().length() == 0) {
			return tooltipWhenBlank;
		}
		if (ChartUIConstants.QUERY_VALUE.equals(queryType)
				&& context.getDataServiceProvider().checkState(IDataServiceProvider.HAS_DATA_SET)) {
			SeriesDefinition baseSd = ChartUIUtil.getBaseSeriesDefinitions(context.getModel()).get(0);

			SeriesGrouping sg = null;
			boolean baseEnabled = baseSd.getGrouping() != null && baseSd.getGrouping().isEnabled();
			if (baseEnabled) {
				sg = baseSd.getGrouping();
				if (seriesdefinition.getGrouping() != null && seriesdefinition.getGrouping().isEnabled()) {
					sg = seriesdefinition.getGrouping();
				}
			}
			if (query.getGrouping() != null && query.getGrouping().isEnabled()) {
				sg = query.getGrouping();
			}

			if (sg != null) {
				StringBuilder sbuf = new StringBuilder();
				sbuf.append(sg.getAggregateExpression());
				sbuf.append("( "); //$NON-NLS-1$
				sbuf.append(queryText);
				IAggregateFunction aFunc = null;
				try {
					aFunc = PluginSettings.instance().getAggregateFunction(sg.getAggregateExpression());
				} catch (ChartException e) {
					// Since the aggFuncName might be null, so we don't display
					// the
					// exception to user, it is true.
				}

				if (!baseEnabled && aFunc != null && aFunc.getType() == IAggregateFunction.SUMMARY_AGGR) {
					return queryText;
				}

				int count = aFunc != null ? aFunc.getParametersCount() : sg.getAggregateParameters().size();

				for (int i = 0; i < sg.getAggregateParameters().size(); i++) {
					if (i < count) {
						sbuf.append(", "); //$NON-NLS-1$
						sbuf.append(sg.getAggregateParameters().get(i));
					}

				}
				sbuf.append(" )"); //$NON-NLS-1$
				return sbuf.toString();
			}

		}
		return queryText;
	}

	public void setTooltipWhenBlank(String tootipWhenBlank) {
		this.tooltipWhenBlank = tootipWhenBlank;
	}

	private void createAggregationItem(Composite composite) {
		SeriesDefinition baseSD = ChartUIUtil.getBaseSeriesDefinitions(context.getModel()).get(0);
		boolean enabled = ChartUIUtil.isGroupingSupported(context)
				&& (PluginSettings.instance().inEclipseEnv() || baseSD.getGrouping().isEnabled());
		if (query.getGrouping() == null) {
			// Set default aggregate function
			SeriesGrouping aggGrouping = SeriesGroupingImpl.create();
			if (seriesdefinition.getGrouping() != null) {
				aggGrouping.setAggregateExpression(seriesdefinition.getGrouping().getAggregateExpression());
			}
			query.setGrouping(aggGrouping);
		}
		fAggEditorComposite = new AggregateEditorComposite(composite, seriesdefinition, context, enabled, query);
	}

	private Control getInputControl() {
		if (txtDefinition != null) {
			return txtDefinition;
		}
		return cmbDefinition;
	}

	/**
	 * Update query by specified expression.
	 * <p>
	 * Under shared binding case, update grouping/aggregate attributes of chart
	 * model if the selected item is group/aggregate expression.
	 */
	@Override
	public void updateQuery(String expression) {
		if (getInputControl() instanceof CCombo) {
			Object checkResult = context.getDataServiceProvider().checkData(queryType, expression);
			if (checkResult instanceof Boolean) {
				if (!((Boolean) checkResult).booleanValue()) {
					// Can't select expressions of one dimension to set
					// on category series and Y optional at one time.

					// did not show the warning since its logic is different
					// from others
					// ChartWizard.showException( ChartWizard.BaseDataDefCom_ID,
					// Messages.getString(
					// "BaseDataDefinitionComponent.WarningMessage.ExpressionsForbidden" ) );
					// //$NON-NLS-1$
					// setUIText( getInputControl( ), oldQuery );
					return;
				}
			}
		}

		if (isTableSharedBinding()) {
			updateQueryForSharedBinding(expression);
		} else {
			setQueryExpression(expression);
		}

		enableAggEditor(expression);
	}

	/**
	 * Update query expression for sharing query with table.
	 *
	 * @param expression
	 */
	private void updateQueryForSharedBinding(String expression) {
		if (ChartUIConstants.QUERY_CATEGORY.equals(queryType) || ChartUIConstants.QUERY_OPTIONAL.equals(queryType)) {
			String grpName = sbHelper.findGroupName(expression);
			boolean isGroupExpr = grpName != null;

			if (ChartUIConstants.QUERY_CATEGORY.equals(queryType)) {
				ChartAdapter.beginIgnoreNotifications();
				seriesdefinition.getGrouping().setEnabled(isGroupExpr);
				query.setDefinition(null);
				ChartAdapter.endIgnoreNotifications();
			}

			if (isGroupExpr) {
				expression = sbHelper.translateToBindingName(expression, grpName);
			}
		} else if (ChartUIConstants.QUERY_VALUE.equals(queryType)) {
			String aggrName = sbHelper.findAggrName(expression);
			boolean isAggregationExpr = aggrName != null;
			String chartAggr = isAggregationExpr ? sbHelper.getChartAggr(aggrName) : null;
			ChartAdapter.beginIgnoreNotifications();
			if (query.getGrouping() != null) {
				query.getGrouping().setEnabled(isAggregationExpr);
				query.getGrouping().setAggregateExpression(chartAggr);
			}
			ChartAdapter.endIgnoreNotifications();

			if (isAggregationExpr) {
				expression = sbHelper.translateToBindingName(expression, aggrName);
			}
		}

		setQueryExpression(expression);
	}

	private void setQueryExpression(String expression) {
		if (ChartUIConstants.QUERY_VALUE.equals(queryType)) {
			if (!(context.getChartType() instanceof GanttChart)
					&& !context.getDataServiceProvider().checkState(IDataServiceProvider.SHARE_QUERY)
					&& context.getDataServiceProvider().checkState(IDataServiceProvider.HAS_DATA_SET)) {
				if (context.getDataServiceProvider().getDataType(expression) == DataType.DATE_TIME_LITERAL) {
					SeriesGrouping basegrouping = ChartUtil.getBaseSeriesDefinitions(context.getModel()).get(0)
							.getGrouping();
					if (basegrouping != null && basegrouping.isEnabled()
							&& !ChartUIUtil.isDataTimeSupportedAgg(basegrouping.getAggregateExpression())) {
						ChartAdapter.beginIgnoreNotifications();
						if (query.getGrouping() == null) {
							query.setGrouping(DataFactoryImpl.init().createSeriesGrouping());
						}
						SeriesGrouping group = query.getGrouping();
						group.setEnabled(true);
						if (!ChartUIUtil.isDataTimeSupportedAgg(group.getAggregateExpression())) {
							group.setAggregateExpression("First"); //$NON-NLS-1$
						}
						ChartAdapter.endIgnoreNotifications();
					}

				}
			}

		} else if (ChartUIConstants.QUERY_CATEGORY.equals(queryType)) {
			DataType type = context.getDataServiceProvider().getDataType(expression);
			ChartAdapter.beginIgnoreNotifications();
			if (seriesdefinition.getGrouping() == null) {
				seriesdefinition.setGrouping(DataFactoryImpl.init().createSeriesGrouping());
			}
			seriesdefinition.getGrouping().setGroupType(type);
			if (type == DataType.DATE_TIME_LITERAL) {
				seriesdefinition.getGrouping().setGroupingUnit(GroupingUnitType.YEARS_LITERAL);
			}

			// Update sort key according to special case.
			if (ChartUIUtil.hasLimitOnCategorySortKey(context) && ChartUtil.hasSorting(seriesdefinition)) {
				// Category sort key uses category expression instead.
				Query sortQuery = seriesdefinition.getSortKey();
				if (sortQuery == null) {
					sortQuery = QueryImpl.create(expression);
					sortQuery.eAdapters().addAll(seriesdefinition.eAdapters());
					seriesdefinition.setSortKey(sortQuery);
				} else {
					sortQuery.setDefinition(expression);
				}
			}
			ChartAdapter.endIgnoreNotifications();
		} else if (ChartUIConstants.QUERY_OPTIONAL.equals(queryType)) {
			ChartAdapter.beginIgnoreNotifications();
			if (expression == null || expression.trim().length() == 0) {
				seriesdefinition.eUnset(DataPackage.eINSTANCE.getSeriesDefinition_Sorting());
				if (seriesdefinition.getSortKey() != null) {
					seriesdefinition.getSortKey().setDefinition(null);
				}
			} else {
				if (seriesdefinition.getSortKey() != null && seriesdefinition.getSortKey().getDefinition() != null
						&& seriesdefinition.getSortKey().getDefinition().equals(query.getDefinition())) {
					seriesdefinition.getSortKey().setDefinition(expression);
				}
				DataType type = context.getDataServiceProvider().getDataType(expression);

				if (query.getGrouping() == null) {
					query.setGrouping(DataFactoryImpl.init().createSeriesGrouping());
				}
				query.getGrouping().setGroupType(type);
				if (type == DataType.DATE_TIME_LITERAL
						&& context.getDataServiceProvider().checkState(IDataServiceProvider.HAS_DATA_SET)) {
					query.getGrouping().setGroupingUnit(GroupingUnitType.YEARS_LITERAL);
				}
			}
			ChartAdapter.endIgnoreNotifications();
		}

		if (query != null) {
			query.setDefinition(expression);
		} else {
			query = QueryImpl.create(expression);
			query.eAdapters().addAll(seriesdefinition.eAdapters());
			// Since the data query must be non-null, it's created in
			// ChartUIUtil.getDataQuery(), assume current null is a grouping
			// query
			seriesdefinition.setQuery(query);
		}

	}

	@Override
	public Query getQuery() {
		if (query == null) {
			query = DataFactory.eINSTANCE.createQuery();
			query.eAdapters().addAll(seriesdefinition.eAdapters());
			ChartAdapter.beginIgnoreNotifications();
			seriesdefinition.setQuery(query);
			ChartAdapter.endIgnoreNotifications();
		}

		return query;
	}

	@Override
	public String getDisplayExpression() {
		String expr = btnBuilder.getExpression();
		return (expr == null) ? "" : expr; //$NON-NLS-1$
	}

	@Override
	public boolean isValidExpression(String expression) {
		if (cmbDefinition != null && cmbDefinition.getItems().length > 0) {
			return cmbDefinition.indexOf(expression) >= 0;
		}
		if (context.getDataServiceProvider().checkState(IDataServiceProvider.SHARE_QUERY)
				|| context.getDataServiceProvider().checkState(IDataServiceProvider.INHERIT_COLUMNS_GROUPS)
				|| context.getDataServiceProvider().checkState(IDataServiceProvider.HAS_CUBE)) {
			if (cmbDefinition == null) {
				return false;
			}
			return cmbDefinition.indexOf(expression) >= 0;
		}
		return true;
	}

	private boolean isGroupEnabled() {
		return seriesdefinition != null && seriesdefinition.getGrouping() != null
				&& seriesdefinition.getGrouping().isEnabled();
	}

	private boolean isAggregateEnabled() {
		return query != null && query.getGrouping() != null && query.getGrouping().isEnabled()
				&& query.getGrouping().getAggregateExpression() != null;
	}

	@Override
	public void updateText(String expression) {
		if (isTableSharedBinding()) {
			if (ChartUIConstants.QUERY_CATEGORY.equals(queryType) && isGroupEnabled()
					|| ChartUIConstants.QUERY_OPTIONAL.equals(queryType)
					|| ChartUIConstants.QUERY_VALUE.equals(queryType) && isAggregateEnabled()) {
				expression = sbHelper.translateFromBindingName(expression);
			}
		}

		if (btnBuilder != null) {
			btnBuilder.setExpression(expression);
		}

		enableAggEditor(expression);
	}

	private void enableAggEditor(String expression) {
		if (expression != null && fAggEditorComposite != null) {
			try {
				ExpressionCodec ec = ChartModelHelper.instance().createExpressionCodec();
				ec.decode(expression);
				expression = ec.convertJSExpression(false);

				boolean enabled = !this.context.getUIFactory().createUIHelper().useDataSetRow(context.getExtendedItem(),
						expression);
				fAggEditorComposite.setEnabled(enabled);
			} catch (BirtException e) {
				WizardBase.displayException(e);
			}

		}
	}

	public void updateLabel() {
		lblDesc.setText(description);
		lblDesc.setToolTipText(tooltipWhenBlank);
		boolean isRequiredField = (ChartUIConstants.QUERY_CATEGORY.equals(queryType));
		if (isRequiredField) {
			FieldAssistHelper.getInstance().addRequiredFieldIndicator(lblDesc);
		}
	}

	public String getExpressionType() {
		if (btnBuilder != null) {
			return btnBuilder.getExpressionType();
		}
		return null;
	}

	@Override
	public IExpressionButton getExpressionButton() {
		return btnBuilder;
	}

	private static class SharedBindingHelper {

		private final ExpressionCodec exprCodec = ChartModelHelper.instance().createExpressionCodec();

		/*
		 * map from group/aggr name to binding name
		 */
		private final Map<String, String> mapBindingName = new HashMap<>();

		/*
		 * map from group/aggr name to column binding info
		 */
		private final Map<String, ColumnBindingInfo> mapBinding = new HashMap<>();

		/**
		 * To reset the instance with predefinedQuery.
		 *
		 * @param predefinedQuery
		 */
		public void reset(Object[] predefinedQuery) {
			mapBindingName.clear();
			mapBinding.clear();

			if (predefinedQuery != null) {
				for (Object obj : predefinedQuery) {
					if (obj instanceof ColumnBindingInfo) {
						ColumnBindingInfo cbi = (ColumnBindingInfo) obj;

						switch (cbi.getColumnType()) {
						case ColumnBindingInfo.GROUP_COLUMN:
						case ColumnBindingInfo.AGGREGATE_COLUMN:
							String bindingName = exprCodec.getBindingName(cbi.getExpression());
							// Bugzilla 368070, T52858
							if (bindingName != null) {
								mapBindingName.put(cbi.getName(), bindingName);
								mapBinding.put(cbi.getName(), cbi);
							}
							break;
						}
					}
				}
			}
		}

		/**
		 * Finds the group name contained by the given expression
		 *
		 * @param expr the given expression
		 * @return the group name if found,null otherwise.
		 */
		public String findGroupName(String expr) {
			return findName(expr, ColumnBindingInfo.GROUP_COLUMN);
		}

		/**
		 * Finds the aggregation name contained by the given expression
		 *
		 * @param expr the given expression
		 * @return the aggregation name if found,null otherwise.
		 */
		public String findAggrName(String expr) {
			return findName(expr, ColumnBindingInfo.AGGREGATE_COLUMN);
		}

		private String findName(String expr, int columnType) {
			if (expr != null && expr.length() > 0) {
				for (Map.Entry<String, ColumnBindingInfo> entry : mapBinding.entrySet()) {
					if (entry.getValue().getColumnType() == columnType) {
						String name = entry.getKey();
						if (expr.contains(name)) {
							return name;
						}
					}
				}
			}
			return null;
		}

		public String getChartAggr(String aggrName) {
			ColumnBindingInfo cbi = mapBinding.get(aggrName);
			if (cbi != null) {
				return cbi.getChartAggExpression();
			}
			return null;
		}

		public String translateToBindingName(String expr, String name) {
			if (expr != null && expr.length() > 0) {
				String bindingName = mapBindingName.get(name);

				if (bindingName != null) {
					expr = expr.replaceAll(name, bindingName);
				}
			}

			return expr;
		}

		public String translateFromBindingName(String expr) {
			if (expr != null && expr.length() > 0) {
				for (Map.Entry<String, String> entry : mapBindingName.entrySet()) {
					if (expr.contains(entry.getValue())) {
						return expr.replaceAll(entry.getValue(), entry.getKey());
					}
				}
			}

			return expr;
		}

	}

	@Override
	public void bindAssociatedName(String name) {
		if (getInputControl() != null) {
			ChartUIUtil.addScreenReaderAccessbility(getInputControl(), name);
		}
	}
}
