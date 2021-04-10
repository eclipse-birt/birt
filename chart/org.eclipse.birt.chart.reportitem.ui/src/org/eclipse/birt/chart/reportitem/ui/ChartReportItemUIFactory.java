/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.reportitem.ui;

import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.attribute.impl.TooltipValueImpl;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.Action;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.model.data.impl.ActionImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.TriggerImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;
import org.eclipse.birt.chart.reportitem.ChartReportItemImpl;
import org.eclipse.birt.chart.reportitem.api.ChartReportItemConstants;
import org.eclipse.birt.chart.reportitem.ui.views.attributes.ChartPageGenerator;
import org.eclipse.birt.chart.ui.swt.ChartUIFactory;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartDataSheet;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.IImageServiceProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.AbstractFilterHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.FilterHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.ISectionHelper;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.extensions.IMenuBuilder;
import org.eclipse.birt.report.designer.ui.preferences.IStatusChangeListener;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.core.resources.IProject;

/**
 * 
 */

public class ChartReportItemUIFactory extends ChartUIFactory {
	protected static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.reportitem/trace"); //$NON-NLS-1$

	private static ChartReportItemUIFactory instance = new ChartReportItemUIFactory();

	protected ReportDataServiceProvider reportDataProvider = null;

	protected ChartReportItemUIFactory() {

	}

	public static ChartReportItemUIFactory instance() {
		return instance;
	}

	public static void initInstance(ChartReportItemUIFactory newInstance) {
		instance = newInstance;
	}

	public IChartDataSheet createDataSheet(ExtendedItemHandle handle, ReportDataServiceProvider dataProvider) {
		return new StandardChartDataSheet(handle, dataProvider);
	}

	public ChartWizardContext createWizardContext(Chart cm, IUIServiceProvider uiProvider,
			IImageServiceProvider imageProvider, IDataServiceProvider dataProvider, IChartDataSheet dataSheet) {
		return new ChartWizardContext(cm, uiProvider, imageProvider, dataProvider, dataSheet, this);
	}

	public DteAdapter createDteAdapter() {
		return new DteAdapter();
	}

	/**
	 * Creates instance of <code>ChartReportItemBuilderImpl</code>.
	 * 
	 * @param taskId
	 * @return instance of <code>ChartReportItemBuilderImpl</code>.
	 */
	public ChartReportItemBuilderImpl createReportItemBuilder(String taskId) {
		return new ChartReportItemBuilderImpl(taskId);
	}

	/**
	 * Creates instance of <code>IMenuBuilder</code>.
	 * 
	 * @return instance of <code>IMenuBuilder</code>.
	 */
	public IMenuBuilder createMenuBuilder() {
		return new ChartMenuBuilder();
	}

	/**
	 * Updates chart page section helper according to context.
	 * 
	 * @param sectionHelper
	 * @return chart page section helper according to context.
	 */
	public ISectionHelper updateChartPageSectionHelper(ISectionHelper sectionHelper) {
		return sectionHelper;
	}

	public ReportDataServiceProvider createReportDataServiceProvider(ExtendedItemHandle extendedHandle) {
		return new ReportDataServiceProvider(extendedHandle);
	}

	/**
	 * Creates Figure used for edit part in layout editor.
	 * 
	 * @param crii report item
	 * @return figure instance
	 * @since 3.7
	 */
	public DesignerRepresentation createFigure(ChartReportItemImpl crii) {
		return new DesignerRepresentation(crii);
	}

	/**
	 * Create chart preference page
	 * 
	 * @param context
	 * @param project
	 * @return ChartConfigurationBlock
	 */
	public ChartConfigurationBlock createChartConfigurationBlock(IStatusChangeListener context, IProject project) {
		return new ChartConfigurationBlock(context, project);
	}

	public AbstractFilterHandleProvider getFilterProvider(Object handle,
			ReportDataServiceProvider dataServiceProvider) {
		AbstractFilterHandleProvider baseProvider = AbstractFilterHandleProvider.class
				.cast(ElementAdapterManager.getAdapter(new ChartPageGenerator(), AbstractFilterHandleProvider.class));
		if (baseProvider == null) {
			baseProvider = new FilterHandleProvider();
		}
		return baseProvider;
	}

	public ExtendedItemHandle createChartViewHandle(DesignElementHandle host) throws ExtendedElementException {
		// Create chart
		ChartWithAxes cm = ChartWithAxesImpl.createDefault();
		cm.setType("Bar Chart");//$NON-NLS-1$
		cm.setSubType("Side-by-side");//$NON-NLS-1$

		// Add base series
		SeriesDefinition sdBase = SeriesDefinitionImpl.createDefault();
		Series series = SeriesImpl.createDefault();
		sdBase.getSeries().add(series);
		cm.getBaseAxes()[0].getSeriesDefinitions().add(sdBase);

		// Add orthogonal series
		SeriesDefinition sdOrth = SeriesDefinitionImpl.createDefault();
		series = BarSeriesImpl.createDefault();
		Action a = ActionImpl.create(ActionType.SHOW_TOOLTIP_LITERAL, TooltipValueImpl.create(200, "")); //$NON-NLS-1$
		Trigger e = TriggerImpl.create(TriggerCondition.ONMOUSEOVER_LITERAL, a);
		series.getTriggers().add(e);
		sdOrth.getSeries().add(series);
		cm.getOrthogonalAxes(cm.getBaseAxes()[0], true)[0].getSeriesDefinitions().add(sdOrth);
		ChartUIUtil.setSeriesName(cm);

		// Add sample data
		SampleData sampleData = DataFactory.eINSTANCE.createSampleData();
		sampleData.getBaseSampleData().clear();
		sampleData.getOrthogonalSampleData().clear();
		// Create Base Sample Data
		BaseSampleData sampleDataBase = DataFactory.eINSTANCE.createBaseSampleData();
		sampleDataBase.setDataSetRepresentation("A, B, C"); //$NON-NLS-1$
		sampleData.getBaseSampleData().add(sampleDataBase);
		// Create Orthogonal Sample Data (with simulation count of 2)
		OrthogonalSampleData sampleDataOrth = DataFactory.eINSTANCE.createOrthogonalSampleData();
		sampleDataOrth.setDataSetRepresentation("5,4,12"); //$NON-NLS-1$
		sampleDataOrth.setSeriesDefinitionIndex(0);
		sampleData.getOrthogonalSampleData().add(sampleDataOrth);
		cm.setSampleData(sampleData);

		// Create a new item handle.
		String name = ReportPlugin.getDefault().getCustomName(ChartReportItemConstants.CHART_EXTENSION_NAME);
		ExtendedItemHandle itemHandle = host.getElementFactory().newExtendedItem(name,
				ChartReportItemConstants.CHART_EXTENSION_NAME);

		itemHandle.getReportItem().setProperty(ChartReportItemConstants.PROPERTY_CHART, cm);

		return itemHandle;
	}

}
