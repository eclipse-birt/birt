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

package org.eclipse.birt.chart.ui.swt.wizard.format.series;

import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.util.ChartDefaultValueUtil;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.ChartCheckbox;
import org.eclipse.birt.chart.ui.swt.ChartCombo;
import org.eclipse.birt.chart.ui.swt.composites.ExternalizedTextEditorComposite;
import org.eclipse.birt.chart.ui.swt.composites.FillChooserComposite;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartType;
import org.eclipse.birt.chart.ui.swt.interfaces.ITaskPopupSheet;
import org.eclipse.birt.chart.ui.swt.type.BubbleChart;
import org.eclipse.birt.chart.ui.swt.type.PieChart;
import org.eclipse.birt.chart.ui.swt.wizard.ChartAdapter;
import org.eclipse.birt.chart.ui.swt.wizard.ChartUIExtensionsImpl;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizard;
import org.eclipse.birt.chart.ui.swt.wizard.format.SubtaskSheetImpl;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.series.SeriesPaletteSheet;
import org.eclipse.birt.chart.ui.util.ChartCacheManager;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIExtensionUtil;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.chart.util.NameSet;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TreeItem;

/**
 * "Series" subtask. Attention: the series layout order must be consistent with
 * series items in the navigator tree.
 * 
 */
public class SeriesSheetImpl extends SubtaskSheetImpl implements SelectionListener

{

	private static Hashtable<String, Series> htSeriesNames = null;
	protected Collection<IChartType> cTypes = null;

	protected ChartCombo cmbColorBy;

	private ITaskPopupSheet popup = null;

	protected int columnDetailNumber;
	protected static final int HORIZONTAL_SPACING = 5;

	protected Composite cmpList = null;

	public SeriesSheetImpl() {
		super();
		columnDetailNumber = 7;
	}

	public void createControl(Composite parent) {
		ChartUIUtil.bindHelp(parent, ChartHelpContextIds.SUBTASK_SERIES);
		final int COLUMN_CONTENT = 4;

		cTypes = ChartUIExtensionsImpl.instance().getUIChartTypeExtensions(getContext().getIdentifier());

		cmpContent = new Composite(parent, SWT.NONE);
		{
			GridLayout glContent = new GridLayout(COLUMN_CONTENT, false);
			glContent.horizontalSpacing = HORIZONTAL_SPACING;
			cmpContent.setLayout(glContent);
			GridData gd = new GridData(GridData.FILL_BOTH);
			cmpContent.setLayoutData(gd);
		}

		new Label(cmpContent, SWT.NONE).setText(Messages.getString("ChartSheetImpl.Label.ColorBy")); //$NON-NLS-1$

		cmbColorBy = getContext().getUIFactory().createChartCombo(cmpContent, SWT.DROP_DOWN | SWT.READ_ONLY,
				getChart().getLegend(), "itemType", //$NON-NLS-1$
				ChartDefaultValueUtil.getDefaultLegend(getChart()).getItemType().getName());
		{
			GridData gridData = new GridData();
			gridData.horizontalSpan = COLUMN_CONTENT - 1;
			cmbColorBy.setLayoutData(gridData);
			NameSet ns = LiteralHelper.legendItemTypeSet;
			cmbColorBy.setItems(ns.getDisplayNames());
			cmbColorBy.setItemData(ns.getNames());
			cmbColorBy.setSelection(getChart().getLegend().getItemType().getName());
			cmbColorBy.addSelectionListener(this);
		}

		ScrolledComposite cmpScroll = new ScrolledComposite(cmpContent, SWT.V_SCROLL | SWT.H_SCROLL);
		{
			GridData gd = new GridData(GridData.FILL_BOTH);
			gd.horizontalSpan = COLUMN_CONTENT;
			cmpScroll.setLayoutData(gd);

			cmpScroll.setExpandVertical(true);
			cmpScroll.setExpandHorizontal(true);
		}

		createSeriesOptions(cmpScroll);

		createButtonGroup(cmpContent);
	}

	protected int getColorByComboDefaultIndex() {
		return getChart().getLegend().isSetItemType()
				? (LiteralHelper.legendItemTypeSet.getSafeNameIndex(getChart().getLegend().getItemType().getName()) + 1)
				: 0;
	}

	protected void createSeriesOptions(ScrolledComposite cmpScroll) {
		if (cmpList == null || cmpList.isDisposed()) {
			cmpList = new Composite(cmpScroll, SWT.NONE);

			GridLayout glContent = new GridLayout(columnDetailNumber, false);
			glContent.horizontalSpacing = HORIZONTAL_SPACING;
			cmpList.setLayout(glContent);
			cmpList.setLayoutData(new GridData(GridData.FILL_BOTH));
			cmpScroll.setContent(cmpList);
		} else {
			Control[] children = cmpList.getChildren();
			for (int i = 0; i < children.length; i++) {
				children[i].dispose();
			}
		}

		createSeriesOptionsLabels();

		int treeIndex = 0;

		if (needCategorySeries()) {
			// Pie chart needs Category Series.
			List<SeriesDefinition> seriesDefns = ChartUIUtil.getBaseSeriesDefinitions(getChart());
			for (int i = 0; i < seriesDefns.size(); i++) {
				createSeriesOptionChoser(seriesDefns.get(i),
						Messages.getString("SeriesSheetImpl.Label.CategoryBaseSeries"), //$NON-NLS-1$
						i, treeIndex++, false).placeComponents(cmpList);
			}
		}

		List<SeriesDefinition> allSeriesDefns = ChartUIUtil.getAllOrthogonalSeriesDefinitions(getChart());

		String text = getChart() instanceof ChartWithAxes ? Messages.getString("SeriesSheetImpl.Label.ValueYSeries") //$NON-NLS-1$
				: Messages.getString("SeriesSheetImpl.Label.ValueOrthogonalSeries"); //$NON-NLS-1$
		boolean canStack;
		int seriesIndex = 0;
		for (int i = 0; i < ChartUIUtil.getOrthogonalAxisNumber(getChart()); i++) {
			canStack = true;
			List<SeriesDefinition> seriesDefns = ChartUIUtil.getOrthogonalSeriesDefinitions(getChart(), i);
			for (int j = 0; j < seriesDefns.size(); j++) {
				if (!seriesDefns.get(j).getDesignTimeSeries().canBeStacked()) {
					canStack = false;
					break;
				}
			}
			for (int j = 0; j < seriesDefns.size(); j++) {
				createSeriesOptionChoser(seriesDefns.get(j),
						(allSeriesDefns.size() == 1 ? text : (text + " - " + (seriesIndex + 1))), seriesIndex++, //$NON-NLS-1$
						treeIndex++, canStack, i).placeComponents(cmpList);
			}
		}

	}

	protected void createSeriesOptionsLabels() {
		Label lblSeries = new Label(cmpList, SWT.WRAP);
		{
			GridData gd = new GridData();
			gd.horizontalAlignment = SWT.CENTER;
			lblSeries.setLayoutData(gd);
			lblSeries.setFont(JFaceResources.getBannerFont());
			lblSeries.setText(Messages.getString("SeriesSheetImpl.Label.Series")); //$NON-NLS-1$
		}

		Label lblTitle = new Label(cmpList, SWT.WRAP);
		{
			GridData gd = new GridData();
			gd.horizontalAlignment = SWT.CENTER;
			lblTitle.setLayoutData(gd);
			lblTitle.setFont(JFaceResources.getBannerFont());
			lblTitle.setText(Messages.getString("SeriesSheetImpl.Label.Title")); //$NON-NLS-1$
		}

		Label lblType = new Label(cmpList, SWT.WRAP);
		{
			GridData gd = new GridData();
			gd.horizontalAlignment = SWT.CENTER;
			lblType.setLayoutData(gd);
			lblType.setFont(JFaceResources.getBannerFont());
			lblType.setText(Messages.getString("SeriesSheetImpl.Label.Type")); //$NON-NLS-1$
		}

		Label lblZOrder = new Label(cmpList, SWT.WRAP);
		{
			GridData gd = new GridData();
			gd.horizontalAlignment = SWT.CENTER;
			lblZOrder.setLayoutData(gd);
			lblZOrder.setFont(JFaceResources.getBannerFont());
			lblZOrder.setText(Messages.getString("SeriesSheetImpl.Label.ZOrder")); //$NON-NLS-1$
		}

		Label lblVisible = new Label(cmpList, SWT.WRAP);
		{
			GridData gd = new GridData();
			gd.horizontalAlignment = SWT.CENTER;
			lblVisible.setLayoutData(gd);
			lblVisible.setFont(JFaceResources.getBannerFont());
			lblVisible.setText(Messages.getString("SeriesSheetImpl.Label.Visible")); //$NON-NLS-1$
		}

		Label lblStack = new Label(cmpList, SWT.WRAP);
		{
			GridData gd = new GridData();
			gd.horizontalAlignment = SWT.CENTER;
			lblStack.setLayoutData(gd);
			lblStack.setFont(JFaceResources.getBannerFont());
			lblStack.setText(Messages.getString("SeriesSheetImpl.Label.Stacked")); //$NON-NLS-1$
		}

		Label lblTranslucent = new Label(cmpList, SWT.WRAP);
		{
			GridData gd = new GridData();
			gd.horizontalAlignment = SWT.CENTER;
			lblTranslucent.setLayoutData(gd);
			lblTranslucent.setFont(JFaceResources.getBannerFont());
			lblTranslucent.setText(Messages.getString("SeriesSheetImpl.Label.Translucent")); //$NON-NLS-1$
		}
	}

	protected SeriesOptionChoser createSeriesOptionChoser(SeriesDefinition seriesDefn, String seriesName,
			int iSeriesDefinitionIndex, int treeIndex, boolean canStack, int axisIndex) {
		return new SeriesOptionChoser(seriesDefn, seriesName, iSeriesDefinitionIndex, treeIndex, canStack, axisIndex);
	}

	protected SeriesOptionChoser createSeriesOptionChoser(SeriesDefinition seriesDefn, String seriesName,
			int iSeriesDefinitionIndex, int treeIndex, boolean canStack) {
		return new SeriesOptionChoser(seriesDefn, seriesName, iSeriesDefinitionIndex, treeIndex, canStack);
	}

	protected int getSeriesFillStyles() {
		return FillChooserComposite.ENABLE_TRANSPARENT | FillChooserComposite.ENABLE_TRANSPARENT_SLIDER
				| FillChooserComposite.ENABLE_GRADIENT | FillChooserComposite.ENABLE_IMAGE
				| FillChooserComposite.ENABLE_POSITIVE_NEGATIVE;
	}

	protected void createButtonGroup(Composite parent) {
		Composite cmp = new Composite(parent, SWT.NONE);
		{
			cmp.setLayout(new GridLayout(6, false));
			GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
			gridData.horizontalSpan = 2;
			gridData.grabExcessVerticalSpace = true;
			gridData.verticalAlignment = SWT.END;
			cmp.setLayoutData(gridData);
		}

		popup = new SeriesPaletteSheet(Messages.getString("SeriesSheetImpl.Label.SeriesPalette"), //$NON-NLS-1$
				getContext(), getCategorySeriesDefinition(), getValueSeriesDefinitions(), isGroupedSeries(),
				getSeriesFillStyles());

		Button btnSeriesPals = createToggleButton(cmp, BUTTON_PALETTE,
				Messages.getString("SeriesSheetImpl.Label.SeriesPalette&"), //$NON-NLS-1$
				popup);
		btnSeriesPals.addSelectionListener(this);
	}

	private SeriesDefinition getCategorySeriesDefinition() {
		return ChartUtil.getCategorySeriesDefinition(getChart());
	}

	private SeriesDefinition[] getValueSeriesDefinitions() {
		return ChartUtil.getValueSeriesDefinitions(getChart());
	}

	public class SeriesOptionChoser implements SelectionListener, Listener {

		protected SeriesDefinition seriesDefn;
		private String seriesName;

		private Link linkSeries;
		private ExternalizedTextEditorComposite txtTitle;
		private Combo cmbTypes;
		private Spinner spnZOrder;
		private ChartCheckbox btnVisible;
		private ChartCheckbox btnStack;
		private ChartCheckbox btnTranslucent;

		private boolean canStack;

		private int iSeriesDefinitionIndex = 0;
		private int axisIndex = 0;
		// Index of tree item in the navigator tree
		private int treeIndex = 0;

		private boolean bStackedPercent;

		protected Series defSeries;

		public SeriesOptionChoser(SeriesDefinition seriesDefn, String seriesName, int iSeriesDefinitionIndex,
				int treeIndex, boolean canStack) {
			this.seriesDefn = seriesDefn;
			this.seriesName = seriesName;
			this.iSeriesDefinitionIndex = iSeriesDefinitionIndex;
			this.treeIndex = treeIndex;
			this.canStack = canStack;
			this.bStackedPercent = isStackedPercent(seriesDefn);
		}

		public SeriesOptionChoser(SeriesDefinition seriesDefn, String seriesName, int iSeriesDefinitionIndex,
				int treeIndex, boolean canStack, int axisIndex) {
			this.seriesDefn = seriesDefn;
			this.seriesName = seriesName;
			this.iSeriesDefinitionIndex = iSeriesDefinitionIndex;
			this.treeIndex = treeIndex;
			this.canStack = canStack;
			this.axisIndex = axisIndex;
			this.bStackedPercent = isStackedPercent(seriesDefn);
		}

		private boolean isStackedPercent(SeriesDefinition seriesDefn) {
			if (seriesDefn.eContainer() instanceof Axis) {
				return ((Axis) seriesDefn.eContainer()).isPercent();
			}
			return false;
		}

		public void placeComponents(Composite parent) {
			Series series = seriesDefn.getDesignTimeSeries();
			defSeries = ChartDefaultValueUtil.getDefaultSeries(series);

			linkSeries = new Link(parent, SWT.NONE);
			{
				GridData gd = new GridData(GridData.FILL_HORIZONTAL);
				linkSeries.setLayoutData(gd);
				linkSeries.setText("<a>" + seriesName + "</a>"); //$NON-NLS-1$//$NON-NLS-2$
				linkSeries.addSelectionListener(this);
			}

			List<String> keys = null;
			if (getContext().getUIServiceProvider() != null) {
				keys = getContext().getUIServiceProvider().getRegisteredKeys();
			}

			txtTitle = new ExternalizedTextEditorComposite(parent, SWT.BORDER | SWT.SINGLE, -1, -1, keys,
					getContext().getUIServiceProvider(), getSeriesIdentifierText(series));
			{
				GridData gd = new GridData(GridData.FILL_HORIZONTAL);
				txtTitle.setLayoutData(gd);
				txtTitle.addListener(this);
				txtTitle.addScreenReaderAccessbility(Messages.getString("SeriesSheetImpl.Label.Title")); //$NON-NLS-1$
			}

			cmbTypes = new Combo(parent, SWT.DROP_DOWN | SWT.READ_ONLY);
			{
				GridData gd = new GridData(GridData.FILL_HORIZONTAL);
				cmbTypes.setLayoutData(gd);
				cmbTypes.addSelectionListener(this);
				// Disable the conversion of the first series
				if (iSeriesDefinitionIndex == 0) {
					cmbTypes.setEnabled(false);
				}
				ChartUIUtil.addScreenReaderAccessbility(cmbTypes, Messages.getString("SeriesSheetImpl.Label.Type")); //$NON-NLS-1$
			}

			if (!series.getClass().isAssignableFrom(SeriesImpl.class)) {
				initZOrderUI(parent);
				initVisibleUI(parent, series);
				initStackUI(parent, series);
				initTranslucentUI(parent, series);

				setTypeComboState();
				setStackedBoxState();
			} else {
				// Occupy a blank area
				Label dummy = new Label(parent, SWT.CHECK);
				GridData gd = new GridData();
				gd.horizontalSpan = 4;
				dummy.setLayoutData(gd);
			}

			populateLists(seriesDefn.getDesignTimeSeries());
		}

		protected String getSeriesIdentifierText(Series series) {
			if (series.getSeriesIdentifier() == null) {
				return "";//$NON-NLS-1$
			}
			return series.getSeriesIdentifier().toString();
		}

		protected void initTranslucentUI(Composite parent, Series series) {
			btnTranslucent = getContext().getUIFactory().createChartCheckbox(parent, SWT.NONE,
					defSeries.isTranslucent());
			{
				GridData gd = new GridData();
				gd.horizontalAlignment = SWT.CENTER;
				btnTranslucent.setLayoutData(gd);
				btnTranslucent.setSelectionState(series.isSetTranslucent()
						? (series.isTranslucent() ? ChartCheckbox.STATE_SELECTED : ChartCheckbox.STATE_UNSELECTED)
						: ChartCheckbox.STATE_GRAYED);
				btnTranslucent.addSelectionListener(this);
				btnTranslucent.addScreenReaderAccessiblity(Messages.getString("SeriesSheetImpl.Label.Translucent")); //$NON-NLS-1$
			}
		}

		protected void initVisibleUI(Composite parent, Series series) {
			btnVisible = getContext().getUIFactory().createChartCheckbox(parent, SWT.NONE, defSeries.isVisible());
			{
				GridData gd = new GridData();
				gd.horizontalAlignment = SWT.CENTER;
				btnVisible.setLayoutData(gd);
				btnVisible.setSelectionState(series.isSetVisible()
						? (series.isVisible() ? ChartCheckbox.STATE_SELECTED : ChartCheckbox.STATE_UNSELECTED)
						: ChartCheckbox.STATE_GRAYED);
				btnVisible.addSelectionListener(this);
				btnVisible.addScreenReaderAccessiblity(Messages.getString("SeriesSheetImpl.Label.Visible")); //$NON-NLS-1$
			}
		}

		protected void initStackUI(Composite parent, Series series) {
			boolean defSelected = false;
			if (defSeries.isSetStacked() && defSeries.isStacked() && !canStack) {
				defSelected = false;
			} else {
				defSelected = defSeries.isStacked();
			}
			btnStack = getContext().getUIFactory().createChartCheckbox(parent, SWT.NONE, defSelected);
			{
				GridData gd = new GridData();
				gd.horizontalAlignment = SWT.CENTER;
				btnStack.setLayoutData(gd);
				btnStack.setEnabled(canStack && series.canBeStacked()
						&& getChart().getDimension().getValue() != ChartDimension.THREE_DIMENSIONAL
						&& !bStackedPercent);
				int state = 0;
				if (series.isSetStacked() && series.isStacked() && !canStack) {
					state = ChartCheckbox.STATE_UNSELECTED;
					series.setStacked(false);
				} else {
					state = series.isSetStacked()
							? (series.isStacked() ? ChartCheckbox.STATE_SELECTED : ChartCheckbox.STATE_UNSELECTED)
							: ChartCheckbox.STATE_GRAYED;
				}
				btnStack.setSelectionState(state);
				btnStack.addSelectionListener(this);
				btnStack.addScreenReaderAccessiblity(Messages.getString("SeriesSheetImpl.Label.Stacked"));//$NON-NLS-1$
			}
		}

		protected void initZOrderUI(Composite parent) {
			spnZOrder = new Spinner(parent, SWT.BORDER);
			{
				GridData gd = new GridData();
				gd.horizontalAlignment = SWT.CENTER;
				spnZOrder.setLayoutData(gd);
				spnZOrder.setMinimum(0);
				spnZOrder.setMaximum(10);
				if (getChart() instanceof ChartWithAxes && !(getContext().getChartType() instanceof BubbleChart)
						&& getChart().getDimension() == ChartDimension.TWO_DIMENSIONAL_LITERAL) {
					// Bubble chart has special z order
					spnZOrder.setSelection(seriesDefn.getZOrder());
					spnZOrder.addSelectionListener(this);
				} else {
					spnZOrder.setEnabled(false);
				}
				ChartUIUtil.addSpinnerScreenReaderAccessbility(spnZOrder,
						Messages.getString("SeriesSheetImpl.Label.ZOrder")); //$NON-NLS-1$
			}
		}

		public void widgetSelected(SelectionEvent e) {
			Series series = seriesDefn.getDesignTimeSeries();

			if (e.getSource().equals(cmbTypes)) {
				if (getCurrentChartType().canCombine()) {

					// Get a new series of the selected type by using as
					// much
					// information as possible from the existing series
					String typeName = cmbTypes.getText();
					convertSeriesType(series, typeName);
				}
			} else if (e.widget == btnVisible) {
				if (btnVisible.getSelectionState() == ChartCheckbox.STATE_GRAYED) {
					series.unsetVisible();
				} else {
					series.setVisible(btnVisible.getSelectionState() == 1);
				}
			} else if (e.widget == btnStack) {
				if (btnStack.getSelectionState() == ChartCheckbox.STATE_GRAYED) {
					series.unsetStacked();
				} else {
					series.setStacked(btnStack.getSelectionState() == ChartCheckbox.STATE_SELECTED);
				}

				// Default label position is inside if Stacked checkbox is
				// selected.
				if (series instanceof BarSeries && (series.isSetStacked() && series.isStacked())) {
					series.setLabelPosition(Position.INSIDE_LITERAL);
				}

				setTypeComboState();
			} else if (e.widget == btnTranslucent) {
				if (btnTranslucent.getSelectionState() == ChartCheckbox.STATE_GRAYED) {
					series.unsetTranslucent();
				} else {
					series.setTranslucent(btnTranslucent.getSelectionState() == ChartCheckbox.STATE_SELECTED);
				}
			} else if (e.getSource().equals(linkSeries)) {
				switchTo(treeIndex);
			} else if (e.getSource().equals(spnZOrder)) {
				seriesDefn.setZOrder(spnZOrder.getSelection());
			}
		}

		/**
		 * Convert current type of series to other.
		 * 
		 * @param series   specified series.
		 * @param typeName other type of series.
		 */
		private void convertSeriesType(Series series, String typeName) {
			Series newSeries = getNewSeries(typeName, series);
			ChartAdapter.beginIgnoreNotifications();
			SeriesDefinition[] seriesDefns = ChartUIUtil.getOrthogonalSeriesDefinitions(getChart(), axisIndex)
					.toArray(new SeriesDefinition[] {});
			if (!newSeries.canBeStacked()) {
				for (int i = 0; i < seriesDefns.length; i++) {
					if ((seriesDefns[i]).getDesignTimeSeries().isStacked()) {
						(seriesDefns[i]).getDesignTimeSeries().setStacked(false);
					}
				}
			}
			ChartAdapter.endIgnoreNotifications();

			newSeries.eAdapters().addAll(seriesDefn.eAdapters());
			seriesDefn.getSeries().set(0, newSeries);

			createSeriesOptions((ScrolledComposite) cmpList.getParent());

			cmpList.layout();
		}

		private Series getNewSeries(String sSeriesName, final Series oldSeries) {
			try {
				// Cache old series
				ChartCacheManager.getInstance().cacheSeries(iSeriesDefinitionIndex, oldSeries);
				// Find new series
				Series series = ChartCacheManager.getInstance()
						.findSeries((htSeriesNames.get(sSeriesName)).getDisplayName(), iSeriesDefinitionIndex);
				if (series == null) {
					series = htSeriesNames.get(sSeriesName);
					ChartAdapter.beginIgnoreNotifications();
					ChartUIUtil.copyGeneralSeriesAttributes(oldSeries, series);
					ChartAdapter.endIgnoreNotifications();
				}
				ChartWizard.removeException(ChartWizard.SeriesShImpl_ID);
				return series;
			} catch (Exception e) {
				ChartWizard.showException(ChartWizard.SeriesShImpl_ID, e.getLocalizedMessage());
			}
			return null;
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub

		}

		public void handleEvent(Event event) {
			if (event.widget.equals(txtTitle)) {
				String text = txtTitle.getText();
				if (text == null || text.trim().length() == 0) {
					seriesDefn.getDesignTimeSeries().setSeriesIdentifier(null);
				} else {
					seriesDefn.getDesignTimeSeries().setSeriesIdentifier(txtTitle.getText());
				}
			}
		}

		private void populateLists(Series series) {
			if (htSeriesNames == null) {
				htSeriesNames = new Hashtable<String, Series>(20);
			}

			// Populate Series Types List
			ChartUIExtensionUtil.populateSeriesTypesList(htSeriesNames, cmbTypes, getContext(), cTypes, series);
		}

		private void switchTo(int index) {
			TreeItem currentItem = getParentTask().getNavigatorTree().getSelection()[0];
			TreeItem[] children = currentItem.getItems();
			if (index < children.length) {
				// Switch to specified subtask
				getParentTask().switchToTreeItem(children[index]);
			}
		}

		/**
		 * Set enabled/disabled state of series type combo.
		 */
		public void setTypeComboState() {
			if (btnStack == null) {
				return;
			}

			ChartDimension cd = getChart().getDimension();
			if (cd == ChartDimension.TWO_DIMENSIONAL_LITERAL
					|| cd == ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL) {
				if (btnStack.getSelectionState() == ChartCheckbox.STATE_SELECTED) {
					cmbTypes.setEnabled(false);
				} else {
					List<SeriesDefinition> seriesDefns;
					if (getContext().isMoreAxesSupported() || ChartUIUtil.getOrthogonalAxisNumber(getChart()) > 2) {
						seriesDefns = ChartUIUtil.getOrthogonalSeriesDefinitions(getChart(), 0);
					} else {
						seriesDefns = ChartUIUtil.getOrthogonalSeriesDefinitions(getChart(), axisIndex);
					}
					Series s = seriesDefns.get(0).getDesignTimeSeries();
					if (s != seriesDefn.getDesignTimeSeries()) {
						cmbTypes.setEnabled(true);
					} else {
						cmbTypes.setEnabled(false);
					}
				}
			}
		}

		/**
		 * Set state of stacked CheckBox by type of series.
		 */
		private void setStackedBoxState() {
			if (btnStack == null) {
				return;
			}

			ChartDimension cd = getChart().getDimension();
			if ((cd == ChartDimension.TWO_DIMENSIONAL_LITERAL
					|| cd == ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL)) {
				List<SeriesDefinition> seriesDefns = ChartUIUtil.getOrthogonalSeriesDefinitions(getChart(), axisIndex);
				Series s = seriesDefns.get(0).getDesignTimeSeries();
				if (s.getDisplayName().equals(seriesDefn.getDesignTimeSeries().getDisplayName())) {
					if (canStack && seriesDefn.getDesignTimeSeries().canBeStacked() && !bStackedPercent) {
						btnStack.setEnabled(true);
					}
				} else {
					btnStack.setEnabled(false);
					cmbTypes.setEnabled(true);
				}
			}
		}
	}

	public void widgetSelected(SelectionEvent e) {
		// Detach popup dialog if there's selected popup button.
		if (detachPopup(e.widget)) {
			return;
		} else if (isRegistered(e.widget)) {
			attachPopup(((Button) e.widget).getData().toString());
		} else if (e.widget.equals(cmbColorBy)) {
			String selectedItemType = cmbColorBy.getSelectedItemData();
			if (selectedItemType != null) {
				getChart().getLegend().setItemType(LegendItemType.getByName(selectedItemType));
				if ((getChart().getLegend().getItemType().getValue() == LegendItemType.CATEGORIES) && isGroupedSeries()
						&& !ChartDefaultValueUtil.isAutoSeriesPalette(getChart())) {
					ChartAdapter.beginIgnoreNotifications();

					// Update color palette of base series
					SeriesDefinition[] osds = getValueSeriesDefinitions();
					SeriesDefinition bsd = getCategorySeriesDefinition();
					bsd.getSeriesPalette().shift(0);
					for (int i = 0; i < osds.length; i++) {
						bsd.getSeriesPalette().getEntries().set(i,
								osds[i].getSeriesPalette().getEntries().get(0).copyInstance());
					}
					((SeriesPaletteSheet) popup).setCategorySeries(bsd);

					ChartAdapter.endIgnoreNotifications();
				}
			}
			((SeriesPaletteSheet) popup).setGroupedPalette(isGroupedSeries());
			refreshPopupSheet();
		}
	}

	public void widgetDefaultSelected(SelectionEvent e) {
		// Do nothing.
	}

	private boolean isGroupedSeries() {
		return (!getValueSeriesDefinitions()[0].getQuery().getDefinition().trim().equals("")); //$NON-NLS-1$ );
	}

	protected IChartType getCurrentChartType() {
		for (IChartType ct : cTypes) {
			if (ct.getName().equals(getChart().getType())) {
				return ct;
			}
		}
		return null;
	}

	protected boolean needCategorySeries() {
		return getContext().getChartType() instanceof PieChart;
	}
}
