/*******************************************************************************
 * Copyright (c) 2006, 2007, 2008 Actuate Corporation.
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

package org.eclipse.birt.chart.ui.swt.composites;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.SortOption;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.SeriesGrouping;
import org.eclipse.birt.chart.model.data.impl.QueryImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesGroupingImpl;
import org.eclipse.birt.chart.model.impl.ChartModelHelper;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.IExpressionButton;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIConstants;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.util.ChartExpressionUtil.ExpressionCodec;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import com.ibm.icu.text.Collator;
import com.ibm.icu.util.ULocale;

/**
 * The dialog is responsible to set grouping and sort condition.
 */

public class GroupSortingDialog extends TrayDialog implements Listener {

	protected static final String UNSORTED_OPTION = Messages.getString("BaseSeriesDataSheetImpl.Choice.Unsorted"); //$NON-NLS-1$

	protected static final String AUTO = Messages.getString("GroupSortingDialog.Sort.Locale.Auto"); //$NON-NLS-1$

	/** The default sort strength items. */
	public static final Map<String, Integer> STRENGTH_MAP;

	private static final int ASCII_SORT_STRENGTH = -1;

	static {
		STRENGTH_MAP = new HashMap<String, Integer>();
		STRENGTH_MAP.put(Messages.getString("GroupSortingDialog.Sort.Strength.ASCII"), //$NON-NLS-1$
				Integer.valueOf(ASCII_SORT_STRENGTH));
		STRENGTH_MAP.put(Messages.getString("GroupSortingDialog.Sort.Strength.PRIMARY"), //$NON-NLS-1$
				Integer.valueOf(Collator.PRIMARY));
		STRENGTH_MAP.put(Messages.getString("GroupSortingDialog.Sort.Strength.SECONDARY"), //$NON-NLS-1$
				Integer.valueOf(Collator.SECONDARY));
		STRENGTH_MAP.put(Messages.getString("GroupSortingDialog.Sort.Strength.TERTIARY"), //$NON-NLS-1$
				Integer.valueOf(Collator.TERTIARY));
		STRENGTH_MAP.put(Messages.getString("GroupSortingDialog.Sort.Strength.QUATENARY"), //$NON-NLS-1$
				Integer.valueOf(Collator.QUATERNARY));
		STRENGTH_MAP.put(Messages.getString("GroupSortingDialog.Sort.Strength.IDENTICAL"), //$NON-NLS-1$
				Integer.valueOf(Collator.IDENTICAL));
	}

	protected ChartWizardContext wizardContext;

	private SeriesDefinition sd;

	protected Group cmpSortArea;

	protected Label lblSorting;

	protected Label lblSortExpr;

	protected Combo cmbSorting;

	protected Combo cmbSortExpr;

	protected Label lblSortLocale;

	protected Combo cmbSortLocale;

	protected Label lblSortStrength;

	protected Combo cmbSortStrength;

	protected IExpressionButton btnSortExprBuilder;

	/** The field indicates if the aggregation composite should be enabled. */
	protected boolean fEnableAggregation = true;

	protected SeriesGroupingComposite fGroupingComposite;

	protected final ExpressionCodec exprCodec = ChartModelHelper.instance().createExpressionCodec();

	public GroupSortingDialog(Shell shell, ChartWizardContext wizardContext, SeriesDefinition sd) {
		super(shell);
		setHelpAvailable(false);
		setShellStyle(getShellStyle() | SWT.RESIZE);
		this.wizardContext = wizardContext;
		this.sd = sd;
	}

	public GroupSortingDialog(Shell shell, ChartWizardContext wizardContext, SeriesDefinition sd,
			boolean disableAggregation) {
		super(shell);
		setHelpAvailable(false);
		setShellStyle(getShellStyle() | SWT.RESIZE);
		this.wizardContext = wizardContext;
		this.sd = sd;
		this.fEnableAggregation = disableAggregation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createContents(org.eclipse.swt.widgets.
	 * Composite)
	 */
	protected Control createContents(Composite parent) {
		Control c = super.createContents(parent);
		// Pack shell for dynamic creating aggregate parameters widgets.
		c.pack();
		return c;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.
	 * Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		ChartUIUtil.bindHelp(parent, ChartHelpContextIds.DIALOG_GROUP_AND_SORT);
		getShell().setText(Messages.getString("GroupSortingDialog.Label.GroupAndSorting")); //$NON-NLS-1$

		Composite cmpContent = new Composite(parent, SWT.NONE);
		{
			GridLayout glContent = new GridLayout(2, false);
			cmpContent.setLayout(glContent);
			GridData gd = new GridData(GridData.FILL_BOTH);
			cmpContent.setLayoutData(gd);
		}

		Composite cmpBasic = new Composite(cmpContent, SWT.NONE);
		{
			cmpBasic.setLayout(new GridLayout(2, false));
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			cmpBasic.setLayoutData(gd);
		}

		createSortArea(cmpBasic);

		if (ChartUIUtil.isGroupingSupported(wizardContext)) {
			createGroupArea(cmpBasic);
		}

		initSortKey();

		populateLists();

		return cmpContent;
	}

	/**
	 * Create composite of group area.
	 * 
	 * @param cmpBasic
	 */
	protected void createGroupArea(Composite cmpBasic) {
		Composite cmpGrouping = new Composite(cmpBasic, SWT.NONE);
		GridData gdCMPGrouping = new GridData(GridData.FILL_HORIZONTAL);
		gdCMPGrouping.horizontalSpan = 2;
		cmpGrouping.setLayoutData(gdCMPGrouping);
		cmpGrouping.setLayout(new FillLayout());
		fGroupingComposite = createSeriesGroupingComposite(cmpGrouping);
	}

	/**
	 * Create runtime instance of <code>SeriesGroupingComposite</code>.
	 * 
	 * @param parent
	 * @since 2.3
	 */
	protected SeriesGroupingComposite createSeriesGroupingComposite(Composite parent) {
		SeriesGrouping grouping = getSeriesDefinitionForProcessing().getGrouping();
		if (grouping == null) {
			grouping = SeriesGroupingImpl.create();
			getSeriesDefinitionForProcessing().setGrouping(grouping);
		}

		return new SeriesGroupingComposite(parent, SWT.NONE, grouping, fEnableAggregation, wizardContext, null);
	}

	protected void updateSortKey() {
		String sExpr = btnSortExprBuilder.getExpression();
		btnSortExprBuilder.setExpression(sExpr);
		getSeriesDefinitionForProcessing().getSortKey().setDefinition(sExpr);
	}

	/**
	 * Create composite of sort area.
	 * 
	 * @param cmpBasic
	 */
	public void createSortArea(Composite parent) {
		cmpSortArea = new Group(parent, SWT.NONE);
		{
			cmpSortArea.setText(Messages.getString("GroupSortingDialog.Composite.Group.Sorting")); //$NON-NLS-1$
			cmpSortArea.setLayout(new GridLayout(3, false));
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = 2;
			gd.widthHint = 300;
			cmpSortArea.setLayoutData(gd);
		}
		lblSorting = new Label(cmpSortArea, SWT.NONE);
		lblSorting.setText(Messages.getString("BaseSeriesDataSheetImpl.Lbl.DataSorting")); //$NON-NLS-1$

		cmbSorting = new Combo(cmpSortArea, SWT.DROP_DOWN | SWT.READ_ONLY);
		GridData gdCMBSorting = new GridData(GridData.FILL_HORIZONTAL);
		cmbSorting.setLayoutData(gdCMBSorting);
		cmbSorting.addListener(SWT.Selection, this);

		new Label(cmpSortArea, SWT.NONE);

		// Add sort column selection composites.
		lblSortExpr = new Label(cmpSortArea, SWT.NONE);
		lblSortExpr.setText(Messages.getString("BaseGroupSortingDialog.Label.SortOn")); //$NON-NLS-1$

		cmbSortExpr = new Combo(cmpSortArea, SWT.DROP_DOWN);
		GridData gdCMBSortExpr = new GridData(GridData.FILL_HORIZONTAL);
		cmbSortExpr.setLayoutData(gdCMBSortExpr);
		cmbSortExpr.addListener(SWT.Selection, this);
		cmbSortExpr.addFocusListener(new FocusAdapter() {

			public void focusLost(FocusEvent e) {
				updateSortKey();
			}
		});

		try {
			btnSortExprBuilder = (IExpressionButton) wizardContext.getUIServiceProvider().invoke(
					IUIServiceProvider.Command.EXPRESS_BUTTON_CREATE, cmpSortArea, cmbSortExpr,
					wizardContext.getExtendedItem(), IUIServiceProvider.COMMAND_EXPRESSION_DATA_BINDINGS,
					new Listener() {

						public void handleEvent(Event event) {
							if (event.data instanceof String[]) {
								handleBuilderAction((String[]) event.data);
							}

						}
					});

			Query query = getSeriesDefinitionForProcessing().getSortKey();
			if (query != null) {
				btnSortExprBuilder.setExpression(query.getDefinition());
			}
		} catch (ChartException e) {
			WizardBase.displayException(e);
		}

		lblSortLocale = new Label(cmpSortArea, SWT.NONE);
		lblSortLocale.setText(Messages.getString("GroupSortingDialog.Composite.Label.SortLocale")); //$NON-NLS-1$
		cmbSortLocale = new Combo(cmpSortArea, SWT.READ_ONLY | SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		cmbSortLocale.setLayoutData(gd);
		cmbSortLocale.setVisibleItemCount(30);
		cmbSortLocale.addListener(SWT.Selection, this);

		new Label(cmpSortArea, SWT.NONE);
		lblSortStrength = new Label(cmpSortArea, SWT.NONE);
		lblSortStrength.setText(Messages.getString("GroupSortingDialog.Composite.Label.SortStrength")); //$NON-NLS-1$
		cmbSortStrength = new Combo(cmpSortArea, SWT.READ_ONLY | SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		cmbSortStrength.setLayoutData(gd);
		cmbSortStrength.setVisibleItemCount(30);
		cmbSortStrength.addListener(SWT.Selection, this);

		if (isInheritColumnsGroups()) {
			disableSorting();
		}

	}

	/**
	 * Handle builder dialog action.
	 */
	protected void handleBuilderAction(String[] data) {
		if (data.length != 4 || data[1].equals(data[0])) {
			return;
		}
		updateSortKey();
	}

	protected SeriesDefinition getSeriesDefinitionForProcessing() {
		return sd;
	}

	/**
	 * All combo lists
	 */
	protected void populateLists() {
		populateSortList();
		populateSortKeyList();
	}

	/**
	 * Populate sort direction list.
	 */
	protected void populateSortList() {
		// populate sorting combo
		cmbSorting.add(UNSORTED_OPTION);

		String[] nss = LiteralHelper.sortOptionSet.getDisplayNames();
		for (int i = 0; i < nss.length; i++) {
			cmbSorting.add(nss[i]);
		}

		// Select value
		if (!getSeriesDefinitionForProcessing().isSetSorting()) {
			cmbSorting.select(0);
		} else {
			// plus one for the first is unsorted option.
			cmbSorting.select(
					LiteralHelper.sortOptionSet.getNameIndex(getSeriesDefinitionForProcessing().getSorting().getName())
							+ 1);
		}

		diableSortKeySelectionStateBySortDirection();

		// populate sort locale combo.
		List<String> localeNames = new ArrayList<String>();
		localeNames.add(AUTO);
		localeNames.addAll(ChartUIUtil.LOCALE_TABLE.keySet());
		cmbSortLocale.setItems(localeNames.toArray(new String[] {}));
		if (getSeriesDefinitionForProcessing().getSortLocale() == null) {
			cmbSortLocale.select(0);
		} else {
			String locale = null;
			for (Map.Entry<String, ULocale> entry : ChartUIUtil.LOCALE_TABLE.entrySet()) {
				if (getSeriesDefinitionForProcessing().getSortLocale().equals(entry.getValue().getName())) {
					locale = entry.getKey();
					break;
				}
			}
			if (locale != null) {
				int index = cmbSortLocale.indexOf(locale);
				cmbSortLocale.select(index < 0 ? 0 : index);
			}
		}

		// Populate sort strength combo.
		List<String> strengthNames = new ArrayList<String>(STRENGTH_MAP.keySet());
		Collections.sort(strengthNames, new Comparator<String>() {

			public int compare(String o1, String o2) {
				return STRENGTH_MAP.get(o1) - STRENGTH_MAP.get(o2);
			}
		});
		cmbSortStrength.setItems(strengthNames.toArray(new String[] {}));
		if (!getSeriesDefinitionForProcessing().isSetSortStrength()) {
			// If sort strength is not set, it should be the default value of
			// <code>com.ibm.icu.text.Collator</code>.
			cmbSortStrength.select(Collator.TERTIARY + 1);
		} else {
			String strength = null;
			for (Map.Entry<String, Integer> entry : STRENGTH_MAP.entrySet()) {
				if (getSeriesDefinitionForProcessing().getSortStrength() == entry.getValue().intValue()) {
					strength = entry.getKey();
				}
			}
			if (strength != null) {
				int index = cmbSortStrength.indexOf(strength);
				cmbSortStrength.select(index < 0 ? 0 : index);
			}
		}
	}

	private Object[] getPredefinedQuery(Set<String> exprSet) {
		if (!onlyCategoryExprAsCategorySortKey()) {
			Object[] queries = wizardContext.getPredefinedQuery(ChartUIConstants.QUERY_VALUE);
			Object[] predefinedQuery = new Object[queries.length + exprSet.size()];
			int i = 0;
			for (Object obj : queries) {
				predefinedQuery[i++] = obj;
			}
			for (String expr : exprSet) {
				predefinedQuery[i++] = new String[] { expr, "expression" }; //$NON-NLS-1$
			}
			return predefinedQuery;
		}

		return exprSet.toArray(new String[exprSet.size()]);
	}

	protected void populateSortKeyList() {
		initSortKey();
		updateSortState();

		if (cmbSorting.getText().equals(UNSORTED_OPTION)) {
			getSeriesDefinitionForProcessing().unsetSorting();
			cmbSortExpr.removeAll();
		} else {
			Set<String> exprSet = getSortKeySet();

			String sortExpr = this.getSeriesDefinitionForProcessing().getSortKey().getDefinition();

			btnSortExprBuilder.setPredefinedQuery(getPredefinedQuery(exprSet));
			btnSortExprBuilder.setExpression(sortExpr);

			if (sortExpr != null && !"".equals(sortExpr)) //$NON-NLS-1$
			{
				exprSet.add(sortExpr);
				btnSortExprBuilder.setExpression(sortExpr);
			} else if (!exprSet.isEmpty()) {
				cmbSortExpr.select(0);
				Event event = new Event();
				event.type = SWT.Selection;
				event.widget = cmbSortExpr;
				cmbSortExpr.notifyListeners(SWT.Selection, event);
			}
		}

		setSortKeyInModel();
	}

	protected void updateSortState() {
		updateSortKeySelectionState();

		boolean sortEnabled = isSortEnabled();
		lblSortLocale.setEnabled(sortEnabled);
		cmbSortLocale.setEnabled(sortEnabled);
		lblSortStrength.setEnabled(sortEnabled);
		cmbSortStrength.setEnabled(sortEnabled);
	}

	protected boolean isSortEnabled() {
		return !UNSORTED_OPTION.equals(cmbSorting.getText());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent(Event event) {
		if (event.type == SWT.Selection) {
			if (event.widget == cmbSorting) {

				diableSortKeySelectionStateBySortDirection();

				if (cmbSorting.getText().equals(UNSORTED_OPTION)) {
					getSeriesDefinitionForProcessing().eUnset(DataPackage.eINSTANCE.getSeriesDefinition_Sorting());
					getSeriesDefinitionForProcessing().getSortKey().setDefinition(null);
				} else {
					getSeriesDefinitionForProcessing().setSorting(SortOption
							.getByName(LiteralHelper.sortOptionSet.getNameByDisplayName(cmbSorting.getText())));
				}

				populateSortKeyList();
			} else if (event.widget == cmbSortLocale) {
				ULocale locale = ChartUIUtil.LOCALE_TABLE.get(cmbSortLocale.getText());
				if (locale == null) {
					getSeriesDefinitionForProcessing().setSortLocale(null);
				} else {
					getSeriesDefinitionForProcessing().setSortLocale(locale.getName());
				}
			} else if (event.widget == cmbSortStrength) {
				Integer sValue = STRENGTH_MAP.get(cmbSortStrength.getText());
				if (sValue == null) {
					getSeriesDefinitionForProcessing().setSortStrength(ASCII_SORT_STRENGTH);
				} else {
					getSeriesDefinitionForProcessing().setSortStrength(sValue.intValue());
				}
			}
		}
	}

	/**
	 * Set state of SortKey components.
	 * 
	 * @param enabled
	 * @since BIRT 2.3
	 */
	protected void setSortKeySelectionState(boolean enabled) {
		lblSortExpr.setEnabled(enabled);
		cmbSortExpr.setEnabled(enabled);
		if (btnSortExprBuilder != null) {
			btnSortExprBuilder.setEnabled(enabled);
		}
	}

	protected void updateSortKeySelectionState() {
		setSortKeySelectionState(!UNSORTED_OPTION.equals(cmbSorting.getText()));
	}

	/**
	 * Disable SortKey selection state by check sort direction.
	 * 
	 * @since BIRT 2.3
	 */
	protected void diableSortKeySelectionStateBySortDirection() {
		if (cmbSorting.getText().equals(UNSORTED_OPTION)) {
			setSortKeySelectionState(false);
		}
	}

	/**
	 * Initialize SortKey object of chart model if it doesn't exist.
	 * 
	 * @since BIRT 2.3
	 */
	protected void initSortKey() {
		if (getSeriesDefinitionForProcessing().getSortKey() == null) {
			getSeriesDefinitionForProcessing().setSortKey(QueryImpl.create((String) null));
		}
	}

	/**
	 * Check if Y grouping is enabled and current is using cube, only category
	 * expression is allowed as category sort key.
	 * 
	 * @return
	 * @since BIRT 2.3
	 */
	protected boolean onlyCategoryExprAsCategorySortKey() {
		return ChartUIUtil.hasLimitOnCategorySortKey(wizardContext);
	}

	protected boolean isInheritColumnsGroups() {
		int stateInfo = wizardContext.getDataServiceProvider().getState();
		return (stateInfo & IDataServiceProvider.HAS_DATA_SET) == 0 && (stateInfo & IDataServiceProvider.HAS_CUBE) == 0
				&& (stateInfo & IDataServiceProvider.INHERIT_DATA_SET) != 0
				&& (stateInfo & IDataServiceProvider.INHERIT_COLUMNS_GROUPS) != 0;
	}

	protected void disableSorting() {
		lblSorting.setEnabled(false);
		cmbSorting.setEnabled(false);
		lblSortExpr.setEnabled(false);
		cmbSortExpr.setEnabled(false);
		lblSortLocale.setEnabled(false);
		cmbSortLocale.setEnabled(false);
		lblSortStrength.setEnabled(false);
		cmbSortStrength.setEnabled(false);
		if (btnSortExprBuilder != null) {
			btnSortExprBuilder.setEnabled(false);
		}
	}

	/**
	 * check if Y grouping is set.
	 * 
	 * @return
	 * @since BIRT 2.3
	 */
	protected boolean isYGroupingEnabled() {
		return ChartUtil.isSpecifiedYOptionalExpression(wizardContext.getModel());
	}

	/**
	 * Get expressions of base series.
	 * 
	 * @return
	 * @since BIRT 2.3
	 */
	protected Set<String> getBaseSeriesExpression() {
		Set<String> exprList = new LinkedHashSet<String>();
		Chart chart = wizardContext.getModel();
		if (chart instanceof ChartWithAxes) {
			// Add the expression of base series.
			final Axis axPrimaryBase = ((ChartWithAxes) chart).getPrimaryBaseAxes()[0];
			EList<SeriesDefinition> elSD = axPrimaryBase.getSeriesDefinitions();
			if (elSD != null && elSD.size() >= 1) {
				SeriesDefinition baseSD = elSD.get(0);
				final Series seBase = baseSD.getDesignTimeSeries();
				EList<Query> elBaseSeries = seBase.getDataDefinition();
				if (elBaseSeries != null && elBaseSeries.size() >= 1) {
					String baseSeriesExpression = elBaseSeries.get(0).getDefinition();
					exprList.add(baseSeriesExpression);
				}
			}
		} else {
			EList<SeriesDefinition> lstSDs = ((ChartWithoutAxes) chart).getSeriesDefinitions();
			for (int i = 0; i < lstSDs.size(); i++) {
				// Add base expression.
				SeriesDefinition sd = lstSDs.get(i);
				Series series = sd.getDesignTimeSeries();
				for (Query qSeries : series.getDataDefinition()) {
					if (qSeries != null && qSeries.getDefinition() != null) {
						exprList.add(qSeries.getDefinition());
					}
				}
			}
		}
		return exprList;
	}

	/**
	 * Get the expressions of value series.
	 * 
	 * @return
	 * @since BIRT 2.3
	 */
	protected Set<String> getValueSeriesExpressions() {
		Set<String> exprList = new LinkedHashSet<String>();
		Chart chart = wizardContext.getModel();
		if (chart instanceof ChartWithAxes) {
			ChartWithAxes cwa = (ChartWithAxes) chart;
			final Axis axPrimaryBase = cwa.getPrimaryBaseAxes()[0];

			// Add expressions of value series.
			for (Axis axOrthogonal : cwa.getOrthogonalAxes(axPrimaryBase, true)) {
				for (SeriesDefinition orthoSD : axOrthogonal.getSeriesDefinitions()) {
					Series seOrthogonal = orthoSD.getDesignTimeSeries();
					for (Query qOrthogonalSeries : seOrthogonal.getDataDefinition()) {
						if (qOrthogonalSeries == null || qOrthogonalSeries.getDefinition() == null
								|| qOrthogonalSeries.getDefinition().length() == 0) {
							continue;
						}
						exprList.add(qOrthogonalSeries.getDefinition());
					}
				}
			}
		} else {
			ChartWithoutAxes cwoa = (ChartWithoutAxes) chart;

			for (SeriesDefinition sd : cwoa.getSeriesDefinitions()) {
				// Add value series expressions.
				for (SeriesDefinition orthSD : sd.getSeriesDefinitions()) {
					Series orthSeries = orthSD.getDesignTimeSeries();

					for (Query qSeries : orthSeries.getDataDefinition()) {
						if (qSeries == null || qSeries.getDefinition() == null
								|| qSeries.getDefinition().length() == 0) {
							continue;
						}
						exprList.add(qSeries.getDefinition());
					}
				}
			}
		}

		return exprList;
	}

	protected Set<String> getSortKeySet() {
		return Collections.emptySet();
	}

	/**
	 * Set SortKey attribute by UI value.
	 */
	protected void setSortKeyInModel() {
		String sortKey = btnSortExprBuilder.getExpression();
		if ("".equals(sortKey)) //$NON-NLS-1$
		{
			sortKey = null;
		}

		getSeriesDefinitionForProcessing().getSortKey().setDefinition(sortKey);
	}

	@Override
	protected void okPressed() {
		if (!UNSORTED_OPTION.equals(cmbSorting.getText()) && cmbSortExpr.getText().isEmpty()) {
			MessageDialog.openWarning(null, Messages.getString("GroupSortingDialog.Sort.SortOn.Warning.Title"), //$NON-NLS-1$
					Messages.getString("GroupSortingDialog.Sort.SortOn.Warning.Message")); //$NON-NLS-1$
		} else {
			super.okPressed();
		}
	}
}
