/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard.data;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.Palette;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.attribute.impl.TooltipValueImpl;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.Action;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.model.data.impl.ActionImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.TriggerImpl;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.ColorPalette;
import org.eclipse.birt.chart.ui.swt.DataDefinitionTextManager;
import org.eclipse.birt.chart.ui.swt.DefaultSelectDataComponent;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartDataSheet;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataComponent;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataCustomizeUI;
import org.eclipse.birt.chart.ui.swt.wizard.ChartAdapter;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartUIConstants;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.text.SimpleDateFormat;

/**
 * This UI component is made up of a combo selector for series selection, a
 * button for series deletion and a dynamic data components which is decided by
 * series type. An axis or a <code>ChartWithoutAxis</code> uses a selector
 * instance. Series adding is embedded in Combo selector.
 */

public class DataDefinitionSelector extends DefaultSelectDataComponent implements SelectionListener {

	private EList<SeriesDefinition> seriesDefns = null;

	private ChartWizardContext wizardContext = null;

	private String sTitle = null;

	private Composite cmpTop = null;

	private Composite cmpData = null;

	private ISelectDataComponent dataComponent = null;

	private Button btnAxisDelete;

	private Combo cmbAxisSelect;

	private Button btnSeriesDelete;

	private Combo cmbSeriesSelect;

	private int axisIndex;

	private String selectionName = Messages.getString("DataDefinitionSelector.Label.Series"); //$NON-NLS-1$

	private String description = ""; //$NON-NLS-1$

	private int areaType = ISelectDataCustomizeUI.ORTHOGONAL_SERIES;

	private ISelectDataCustomizeUI selectDataUI = null;

	/**
	 * 
	 * @param axisIndex     -1 means single axis; nonnegative number means the axis
	 *                      index
	 * @param seriesDefns
	 * @param wizardContext
	 * @param sTitle
	 * @param selectDataUI
	 */
	public DataDefinitionSelector(int axisIndex, EList<SeriesDefinition> seriesDefns, ChartWizardContext wizardContext,
			String sTitle, ISelectDataCustomizeUI selectDataUI) {
		this.seriesDefns = seriesDefns;
		this.wizardContext = wizardContext;
		this.sTitle = sTitle;
		this.axisIndex = axisIndex;
		this.selectDataUI = selectDataUI;
	}

	public DataDefinitionSelector(ChartWizardContext wizardContext, String sTitle,
			ISelectDataCustomizeUI selectDataUI) {
		this.wizardContext = wizardContext;
		this.sTitle = sTitle;
		this.axisIndex = -1;
		this.selectDataUI = selectDataUI;
	}

	public Composite createArea(Composite parent) {
		{
			if (axisIndex >= 0) {
				cmpTop = new Group(parent, SWT.NONE);
				((Group) cmpTop).setText(Messages.getString("DataDefinitionSelector.Label.YAxis") + (axisIndex + 1)); //$NON-NLS-1$
			} else {
				cmpTop = new Composite(parent, SWT.NONE);
			}

			GridLayout layout = new GridLayout();
			layout.numColumns = 2;
			layout.marginHeight = 0;
			layout.marginWidth = 0;
			layout.horizontalSpacing = 0;
			layout.verticalSpacing = 2;
			cmpTop.setLayout(layout);
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			cmpTop.setLayoutData(gd);
		}

		if (wizardContext.isMoreAxesSupported()) {
			cmbAxisSelect = new Combo(cmpTop, SWT.DROP_DOWN | SWT.READ_ONLY);
			{
				cmbAxisSelect.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
				cmbAxisSelect.addSelectionListener(this);
				refreshAxisCombo();
				cmbAxisSelect.select(0);
			}

			btnAxisDelete = new Button(cmpTop, SWT.NONE);
			{
				GridData gridData = new GridData();
				gridData.heightHint = 20;
				gridData.widthHint = 20;
				btnAxisDelete.setLayoutData(gridData);
				btnAxisDelete.setImage(UIHelper.getImage(ChartUIConstants.IMAGE_DELETE));
				btnAxisDelete.setToolTipText(Messages.getString("DataDefinitionSelector.Tooltip.RemoveAxis")); //$NON-NLS-1$
				btnAxisDelete.addSelectionListener(this);
				setAxisDeleteEnabled();
				ChartUIUtil.addScreenReaderAccessbility(btnAxisDelete,
						Messages.getString("DataDefinitionSelector.Label.DeleteAxis")); //$NON-NLS-1$
			}

			Label lblSeparator = new Label(cmpTop, SWT.SEPARATOR | SWT.HORIZONTAL);
			{
				GridData gd = new GridData(GridData.FILL_HORIZONTAL);
				gd.horizontalSpan = 2;
				lblSeparator.setLayoutData(gd);
			}

			axisIndex = cmbAxisSelect.getSelectionIndex();

			// Update series definition
			seriesDefns = ChartUIUtil.getOrthogonalSeriesDefinitions(getChart(), axisIndex);
		}

		cmbSeriesSelect = createSeriesSelectCombo(cmpTop, wizardContext);

		btnSeriesDelete = createSeriesDeleteButton(cmpTop, wizardContext);
		ChartUIUtil.addScreenReaderAccessbility(btnSeriesDelete,
				Messages.getString("DataDefinitionSelector.Label.DeleteSeries")); //$NON-NLS-1$
		setSeriesDeleteEnabled();

		updateDataDefinition();

		return cmpTop;
	}

	protected Combo createSeriesSelectCombo(Composite cmpTop, ChartWizardContext wizardContext) {
		Combo combo = new Combo(cmpTop, SWT.DROP_DOWN | SWT.READ_ONLY);
		{
			combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			combo.addSelectionListener(this);
			refreshSeriesCombo(combo);
			combo.select(0);
		}
		return combo;
	}

	protected Button createSeriesDeleteButton(Composite cmpTop, ChartWizardContext wizardContext) {
		Button button = new Button(cmpTop, SWT.NONE);
		{
			GridData gridData = new GridData();
			ChartUIUtil.setChartImageButtonSizeByPlatform(gridData);
			button.setLayoutData(gridData);
			button.setImage(UIHelper.getImage(ChartUIConstants.IMAGE_DELETE));
			button.setToolTipText(Messages.getString("DataDefinitionSelector.Tooltip.RemoveSeries")); //$NON-NLS-1$
			button.addSelectionListener(this);

		}
		return button;
	}

	private void updateDataDefinition() {
		ISelectDataComponent newComponent = getDataDefinitionComponent(getCurrentSeriesDefinition());
		if (dataComponent != null && dataComponent.getClass() == newComponent.getClass()) {
			// No change if new UI is same with the old
			return;
		}

		if (cmpData != null && !cmpData.isDisposed()) {
			cmpData.dispose();
		}

		dataComponent = newComponent;
		cmpData = dataComponent.createArea(cmpTop);
		if (cmpData != null) {
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = 2;
			cmpData.setLayoutData(gd);
		}
	}

	private SeriesDefinition getCurrentSeriesDefinition() {
		// TODO temp solution
		if (seriesDefns.isEmpty()) {
			addNewSeriesDefinition();
			refreshSeriesCombo();
			cmbSeriesSelect.select(0);
		}
		return seriesDefns.get(cmbSeriesSelect.getSelectionIndex());
	}

	private int getFirstIndexOfSameAxis() {
		if (axisIndex > 0) {
			return ChartUIUtil.getLastSeriesIndexWithinAxis(getChart(), axisIndex - 1) + 1;
		}
		return 0;
	}

	protected void addNewSeriesDefinition() {
		// Create a series definition without data definition
		SeriesDefinition sdTmp = SeriesDefinitionImpl.create();

		ChartAdapter.beginIgnoreNotifications();

		if (!seriesDefns.isEmpty()) {
			if (ChartElementUtil.isSetSeriesPalette(getChart())) {
				Palette pa = ((seriesDefns.get(0))).getSeriesPalette();
				for (int i = 0; i < pa.getEntries().size(); i++) {
					int index = i + seriesDefns.size();
					int paletteSize = pa.getEntries().size();
					while (index >= pa.getEntries().size()) {
						index -= paletteSize;
					}
					sdTmp.getSeriesPalette().getEntries().add(i, pa.getEntries().get(index).copyInstance());
				}
			}
			Series newSeries = seriesDefns.get(0).getDesignTimeSeries().copyInstance();
			newSeries.getTriggers().clear();

			// Add tooltips by default
			Action a = ActionImpl.create(ActionType.SHOW_TOOLTIP_LITERAL, TooltipValueImpl.create(200, "")); //$NON-NLS-1$
			Trigger e = TriggerImpl.create(TriggerCondition.ONMOUSEOVER_LITERAL, a);
			newSeries.getTriggers().add(e);

			sdTmp.getSeries().add(newSeries);

			// Add grouping query of the first series definition
			sdTmp.setQuery(seriesDefns.get(0).getQuery().copyInstance());
			cleanDataDefinition(sdTmp);
			// clean the possible series name
			sdTmp.getDesignTimeSeries().setSeriesIdentifier(""); //$NON-NLS-1$
			sdTmp.eAdapters().addAll(seriesDefns.get(0).eAdapters());

			int firstIndex = getFirstIndexOfSameAxis();
			EList<OrthogonalSampleData> list = getChart().getSampleData().getOrthogonalSampleData();

			// Create a new OrthogonalSampleData instance from the existing one
			OrthogonalSampleData sdOrthogonal = list.get(firstIndex).copyInstance();
			if (axisIndex == -1) {
				sdOrthogonal.setSeriesDefinitionIndex(seriesDefns.size());
			} else {
				sdOrthogonal
						.setSeriesDefinitionIndex(ChartUIUtil.getLastSeriesIndexWithinAxis(getChart(), axisIndex) + 1);
			}
			sdOrthogonal.setDataSetRepresentation(convertDataSetRepresentation(sdOrthogonal.getDataSetRepresentation(),
					sdOrthogonal.getSeriesDefinitionIndex()));
			sdOrthogonal.eAdapters().addAll(getChart().getSampleData().eAdapters());

			// Update the Sample Data.
			int sdIndex = sdOrthogonal.getSeriesDefinitionIndex();
			ArrayList<OrthogonalSampleData> al = new ArrayList<OrthogonalSampleData>();
			if (sdIndex >= list.size()) {
				list.add(sdOrthogonal);
			} else {
				for (int i = sdIndex; i < list.size(); i++) {
					al.add(list.get(i));
				}
				list.set(sdIndex, sdOrthogonal);
				for (int i = 1; i < al.size(); i++) {
					list.set(i + sdIndex, al.get(i - 1));
					list.get(i + sdIndex).setSeriesDefinitionIndex(i + sdIndex);
				}
				list.add(al.get(al.size() - 1));
				list.get(list.size() - 1).setSeriesDefinitionIndex(list.size() - 1);
			}
		} else {
			// TODO temp solution
			sdTmp.getSeries().add(BarSeriesImpl.create());
			OrthogonalSampleData sampleData = DataFactory.eINSTANCE.createOrthogonalSampleData();
			sampleData.setDataSetRepresentation(ChartUtil.getNewSampleData(AxisType.LINEAR_LITERAL, 0));
			sampleData.setSeriesDefinitionIndex(0);

			getChart().getSampleData().getOrthogonalSampleData().add(getFirstIndexOfSameAxis(), sampleData);
		}
		seriesDefns.add(sdTmp);
		ChartAdapter.endIgnoreNotifications();

		// Series name should be set after series is added
		ChartUIUtil.setSeriesName(wizardContext.getModel());
	}

	private String convertDataSetRepresentation(String dsRepresentation, int seriesDefinitionIndex) {
		if (dsRepresentation != null) {
			String[] strTok = ChartUtil.getStringTokens(dsRepresentation);
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < strTok.length; i++) {
				String strDataElement = strTok[i];
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy"); //$NON-NLS-1$
				NumberFormat nf = NumberFormat.getNumberInstance();

				try {
					Date dateElement = sdf.parse(strDataElement);
					dateElement.setTime(dateElement.getTime() + (dateElement.getTime() * seriesDefinitionIndex) / 10);
					sb.append(sdf.format(dateElement));
				} catch (ParseException e) {
					try {
						Number numberElement = nf.parse(strDataElement);
						sb.append(numberElement.doubleValue() * (seriesDefinitionIndex + 1));
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
				}
				if (i < strTok.length - 1) {
					sb.append(","); //$NON-NLS-1$
				}

			}
			return sb.toString();
		}
		return null;
	}

	private void cleanDataDefinition(SeriesDefinition sd) {
		EList<Query> dds = sd.getDesignTimeSeries().getDataDefinition();
		for (int i = 0; i < dds.size(); i++) {
			dds.get(i).setDefinition(""); //$NON-NLS-1$
			// disable the grouping aggregation
			if (dds.get(i).getGrouping() != null) {
				dds.get(i).getGrouping().setEnabled(false);
			}

		}
	}

//	/**
//	 * Updates series palette of series definition list without the series to be
//	 * moved
//	 * 
//	 * @param removedIndex
//	 *            the index of the series to be removed
//	 */
//	private void updateSeriesPalette( int removedIndex )
//	{
//		for ( int i = 0, j = 0; i < seriesDefns.size( ); i++ )
//		{
//			if ( i != removedIndex )
//			{
//				seriesDefns.get( i ).getSeriesPalette( )
//						.shift( -j++ );
//			}
//		}
//	}

	protected void removeSeriesDefinition() {
		boolean isNotificaionIgnored = ChartAdapter.isNotificationIgnored();
		ChartAdapter.ignoreNotifications(true);
		int firstIndex = getFirstIndexOfSameAxis();
		EList<OrthogonalSampleData> list = getChart().getSampleData().getOrthogonalSampleData();
		for (int i = 0; i < list.size(); i++) {
			// Check each entry if it is associated with the series
			// definition to be removed
			if (list.get(i).getSeriesDefinitionIndex() == (firstIndex + cmbSeriesSelect.getSelectionIndex())) {
				list.remove(i);
				break;
			}
		}
		// Reset index. If index is wrong, sample data can't display.
		ChartUIUtil.reorderOrthogonalSampleDataIndex(getChart());
//		updateSeriesPalette( cmbSeriesSelect.getSelectionIndex( ) );
		ChartAdapter.ignoreNotifications(isNotificaionIgnored);

		seriesDefns.remove(cmbSeriesSelect.getSelectionIndex());
	}

	public void widgetSelected(SelectionEvent e) {
		if (e.widget.equals(btnSeriesDelete)) {
			// Update color registry
			updateColorRegistry(cmbSeriesSelect.getSelectionIndex());

			// Remove sample data and series
			removeSeriesDefinition();

			int oldSelectedIndex = cmbSeriesSelect.getSelectionIndex();
			refreshSeriesCombo();
			// Selects the new item or last item
			if (oldSelectedIndex > cmbSeriesSelect.getItemCount() - 2) {
				oldSelectedIndex = cmbSeriesSelect.getItemCount() - 2;
			}
			cmbSeriesSelect.select(oldSelectedIndex);

			// Update data definition component and refresh query in it
			updateDataDefinition();
			refreshQuery();

			// Sets current series index and update bottom component if needed
			setSelectedSeriesIndex();

			// Reset the default series name
			ChartUIUtil.setSeriesName(wizardContext.getModel());

			// CHART ENGINE NOT SUPPORT MULTI-GROUPING, NO NEED TO REFRESH UI
			// selectDataUI.refreshRightBindingArea( );
			selectDataUI.layoutAll();

		} else if (e.widget.equals(cmbSeriesSelect)) {
			// Check if needing to add a new series
			if (cmbSeriesSelect.getSelectionIndex() == cmbSeriesSelect.getItemCount() - 1 && !isPartChart()) {
				addNewSeriesDefinition();

				refreshSeriesCombo();
				// Selects the new item
				cmbSeriesSelect.select(cmbSeriesSelect.getItemCount() - 2);
			}

			// Update data definition component and refresh query in it
			updateDataDefinition();
			refreshQuery();

			// Sets current series index and update bottom component if needed
			setSelectedSeriesIndex();

			// CHART ENGINE NOT SUPPORT MULTI-GROUPING, NO NEED TO REFRESH UI
			// selectDataUI.refreshRightBindingArea( );
			selectDataUI.layoutAll();
		} else if (e.widget.equals(cmbAxisSelect)) {
			// Check if needing to add a new series
			if (cmbAxisSelect.getSelectionIndex() == cmbAxisSelect.getItemCount() - 1) {
				// Update dimension if it doesn't support multiple axes
				String currentDimension = ChartUIUtil.getDimensionString(getChart().getDimension());
				boolean isDimensionSupported = wizardContext.getChartType().isDimensionSupported(currentDimension,
						wizardContext, cmbAxisSelect.getItemCount(), 0);
				if (!isDimensionSupported) {
					ChartAdapter.beginIgnoreNotifications();
					getChart().setDimension(
							ChartUIUtil.getDimensionType(wizardContext.getChartType().getDefaultDimension()));
					ChartAdapter.endIgnoreNotifications();
				}

				// Update model
				ChartUIUtil.addAxis((ChartWithAxes) getChart());

				// Update UI
				refreshAxisCombo();
				cmbAxisSelect.select(cmbAxisSelect.getItemCount() - 2);
			}

			axisIndex = cmbAxisSelect.getSelectionIndex();

			updateAllSeriesUnderAxis();
		} else if (e.widget.equals(btnAxisDelete)) {
			// Update color registry
			updateColorRegistry(-1);

			// Update model
			ChartUIUtil.removeAxis(getChart(), axisIndex);

			// Update UI
			refreshAxisCombo();
			// Selects the new item or last item
			if (axisIndex > cmbAxisSelect.getItemCount() - 2) {
				axisIndex = cmbAxisSelect.getItemCount() - 2;
			}
			cmbAxisSelect.select(axisIndex);

			// Reset the default series name
			ChartUIUtil.setSeriesName(wizardContext.getModel());

			updateAllSeriesUnderAxis();
		}
		setAxisDeleteEnabled();
		setSeriesDeleteEnabled();
		// series updated, check the aggregation
		ChartUIUtil.checkAggregateType(wizardContext);
	}

	private void updateAllSeriesUnderAxis() {
		// Update series definition
		seriesDefns = ChartUIUtil.getOrthogonalSeriesDefinitions(getChart(), axisIndex);
		setSeriesDeleteEnabled();
		refreshSeriesCombo();
		// Selects the new item or last item
		cmbSeriesSelect.select(0);

		// Update data definition component and refresh query in it
		updateDataDefinition();
		refreshQuery();

		// Sets current series index and update bottom component if needed
		setSelectedSeriesIndex();

		// CHART ENGINE NOT SUPPORT MULTI-GROUPING, NO NEED TO REFRESH UI
		// selectDataUI.refreshRightBindingArea( );
		selectDataUI.layoutAll();
	}

	/**
	 * Updates the color registry and refresh all background color of the text field
	 * 
	 * @param seriesIndex -1 means all series under selected axis
	 */
	private void updateColorRegistry(int seriesIndex) {
		List<Query> dataDefinitions = null;
		if (seriesIndex > -1) {
			dataDefinitions = seriesDefns.get(seriesIndex).getDesignTimeSeries().getDataDefinition();
		} else {
			List<SeriesDefinition> allSeriesDefns = ChartUIUtil.getAllOrthogonalSeriesDefinitions(getChart());
			dataDefinitions = new ArrayList<Query>();
			for (int i = 0; i < allSeriesDefns.size(); i++) {
				dataDefinitions.addAll(allSeriesDefns.get(i).getDesignTimeSeries().getDataDefinition());
			}
		}

		// Count each expression
		Map<String, Integer> queryMap = new HashMap<String, Integer>();
		for (int i = 0; i < dataDefinitions.size(); i++) {
			String expression = dataDefinitions.get(i).getDefinition();
			if (queryMap.containsKey(expression)) {
				int expCount = queryMap.get(expression).intValue();
				queryMap.put(expression, Integer.valueOf(expCount++));
			} else {
				queryMap.put(expression, Integer.valueOf(1));
			}
		}
		// If the expression count is the same to the count of all, delete this
		// color registry of the expression
		for (Iterator<Entry<String, Integer>> iterator = queryMap.entrySet().iterator(); iterator.hasNext();) {
			Entry<String, Integer> entry = iterator.next();
			String expression = entry.getKey();
			if (DataDefinitionTextManager.getInstance().getNumberOfSameDataDefinition(expression) == entry.getValue()
					.intValue()) {
				ColorPalette.getInstance().retrieveColor(expression);
			}
		}

		// refresh table color
		final Event e = new Event();
		e.data = DataDefinitionSelector.this;
		e.type = IChartDataSheet.EVENT_QUERY;
		e.detail = IChartDataSheet.DETAIL_UPDATE_COLOR_AND_TEXT;

		// Use async thread to update UI to prevent control disposed
		Display.getCurrent().asyncExec(new Runnable() {

			public void run() {
				wizardContext.getDataSheet().notifyListeners(e);
			}
		});
	}

	private void setSelectedSeriesIndex() {
		// Only standard type shows multiple series at the same time
		if (!wizardContext.isMoreAxesSupported()) {
			int axisNum = axisIndex < 0 ? 0 : axisIndex;
			int[] indexArray = selectDataUI.getSeriesIndex();
			indexArray[axisNum] = cmbSeriesSelect.getSelectionIndex();
			selectDataUI.setSeriesIndex(indexArray);
		}
	}

	private void setSeriesDeleteEnabled() {
		if (btnSeriesDelete != null) {
			btnSeriesDelete.setEnabled(seriesDefns.size() > 1 && cmbSeriesSelect.getSelectionIndex() > 0);
		}
	}

	private void setAxisDeleteEnabled() {
		if (btnAxisDelete != null) {
			btnAxisDelete.setEnabled(
					ChartUIUtil.getOrthogonalAxisNumber(getChart()) > 1 && cmbAxisSelect.getSelectionIndex() > 0);
		}
	}

	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub

	}

	private void refreshQuery() {
		Object[] data = new Object[2];
		data[0] = getCurrentSeriesDefinition();
		data[1] = ChartUIUtil.getDataQuery(getCurrentSeriesDefinition(), 0);
		dataComponent.selectArea(true, data);
	}

	private void refreshSeriesCombo() {
		refreshSeriesCombo(cmbSeriesSelect);
	}

	private void refreshSeriesCombo(Combo cmbSeriesSelect) {
		ArrayList<String> itemList = new ArrayList<String>();
		int seriesSize = seriesDefns.size();
		for (int i = 1; i <= seriesSize; i++) {
			itemList.add(selectionName + " " + i); //$NON-NLS-1$
		}
		if (!isPartChart()) {
			itemList.add(Messages.getString("DataDefinitionSelector.Text.NewSeries")); //$NON-NLS-1$
		}
		cmbSeriesSelect.removeAll();
		cmbSeriesSelect.setItems(itemList.toArray(new String[seriesSize]));
	}

	private boolean isPartChart() {
		return wizardContext.getDataServiceProvider().checkState(IDataServiceProvider.PART_CHART);
	}

	private void refreshAxisCombo() {
		ArrayList<String> itemList = new ArrayList<String>();
		int axisNum = ChartUIUtil.getOrthogonalAxisNumber(getChart());
		for (int i = 1; i <= axisNum; i++) {
			itemList.add(Messages.getString("DataDefinitionSelector.Label.Axis") + i); //$NON-NLS-1$
		}
		itemList.add(Messages.getString("DataDefinitionSelector.Text.NewAxis")); //$NON-NLS-1$
		cmbAxisSelect.removeAll();
		cmbAxisSelect.setItems(itemList.toArray(new String[axisNum]));
	}

	private ISelectDataComponent getDataDefinitionComponent(SeriesDefinition seriesDefn) {
		ISelectDataComponent sdc = selectDataUI.getAreaComponent(areaType, seriesDefn, wizardContext, sTitle);
		if (sdc instanceof BaseDataDefinitionComponent) {
			((BaseDataDefinitionComponent) sdc).setDescription(description);
		}
		return sdc;
	}

	public void selectArea(boolean selected, Object data) {
		dataComponent.selectArea(selected, data);
	}

	public void dispose() {
		if (dataComponent != null) {
			dataComponent.dispose();
		}
		super.dispose();
	}

	/**
	 * Sets the name prefix in the combo
	 * 
	 * @param selectionNamePrefix
	 */
	public void setSelectionPrefix(String selectionNamePrefix) {
		this.selectionName = selectionNamePrefix;
	}

	/**
	 * Sets the description in the left of data text box.
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	public void setAreaType(int areaType) {
		this.areaType = areaType;
	}

	private Chart getChart() {
		return wizardContext.getModel();
	}

}
