/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.birt.chart.factory.IActionEvaluator;
import org.eclipse.birt.chart.factory.IExternalizer;
import org.eclipse.birt.chart.factory.IResourceFinder;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.style.IStyleProcessor;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartDataSheet;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartType;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartUIFactory;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartWizardContext;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.IImageServiceProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.chart.ui.util.ChartUIConstants;

/**
 * ChartWizardContext
 */
public class ChartWizardContext implements IChartWizardContext<Chart> {

	protected Chart chartModel = null;
	private IChartType chartType = null;
	private Object extendedItem = null;
	private String sDefaultOutputFormat = "SVG"; //$NON-NLS-1$
	private String sOutputFormat = sDefaultOutputFormat;
	final protected IUIServiceProvider uiProvider;
	final protected IImageServiceProvider imageServiceProvider;
	final private IDataServiceProvider dataProvider;
	final private IChartDataSheet dataSheet;
	private IStyleProcessor processor;
	private boolean isMoreAxesSupported;
	private boolean isRtL;
	private boolean isTextRtL;
	private boolean isInheritColumnsOnly;
	private Map<String, Boolean> mSheetEnabled;
	private Map<String, Object[]> mQueries;
	private IResourceFinder resourceFinder = null;
	private IExternalizer externalizer = null;

	private Boolean isShowingDataPreview = null;
	private IActionEvaluator actionEvaluator;

	/**
	 * The thread is responsible to manage live preview.
	 */
	private Thread livePreviewThread = null;

	// Default implementation of UI factory
	private IChartUIFactory uiFactory;

	public ChartWizardContext(Chart chartModel, IUIServiceProvider uiProvider, IImageServiceProvider imageProvider,
			IDataServiceProvider dataProvider, IChartDataSheet dataSheet, IChartUIFactory uiFactory) {
		this.chartModel = chartModel;
		this.uiProvider = uiProvider;
		this.imageServiceProvider = imageProvider;
		this.dataProvider = dataProvider;
		this.dataSheet = dataSheet;
		if (this.dataSheet != null) {
			this.dataSheet.setContext(this);
		}
		this.uiFactory = uiFactory;
	}

	public ChartWizardContext(Chart chartModel, IUIServiceProvider uiProvider, IImageServiceProvider imageProvider,
			IDataServiceProvider dataProvider, IChartDataSheet dataSheet) {
		this(chartModel, uiProvider, imageProvider, dataProvider, dataSheet, null);
	}

	/**
	 * Sets live preview thread reference.
	 *
	 * @param thread
	 * @since 2.5.2
	 */
	public void setLivePreviewThread(Thread thread) {
		livePreviewThread = thread;
	}

	/**
	 * Returns reference of live preview thread.
	 *
	 * @return thread
	 * @since 2.5.2
	 */
	public Thread getLivePreviewThread() {
		return this.livePreviewThread;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.frameworks.taskwizard.interfaces.IWizardContext#getWizardID
	 * ()
	 */
	@Override
	public String getWizardID() {
		return getExtendedItem() == null ? ChartWizard.class.getName() : getExtendedItem().toString();
	}

	@Override
	public Chart getModel() {
		return chartModel;
	}

	public void setModel(Chart model) {
		this.chartModel = model;
	}

	@Override
	public Object getExtendedItem() {
		return extendedItem;
	}

	@Override
	public void setExtendedItem(Object extendedItem) {
		this.extendedItem = extendedItem;
	}

	public String getOutputFormat() {
		return sOutputFormat;
	}

	public void setOutputFormat(String format) {
		this.sOutputFormat = format;
	}

	public String getDefaultOutputFormat() {
		return sDefaultOutputFormat;
	}

	public void setDefaultOutputFormat(String sOutputFormat) {
		this.sDefaultOutputFormat = sOutputFormat;
		setOutputFormat(sOutputFormat);
	}

	@Override
	public IUIServiceProvider getUIServiceProvider() {
		return uiProvider;
	}

	@Override
	public IDataServiceProvider getDataServiceProvider() {
		return dataProvider;
	}

	public void setChartType(IChartType chartType) {
		this.chartType = chartType;
	}

	public IChartType getChartType() {
		if (chartType == null) {
			// If chart type is not set, fetch the value from the model
			LinkedHashMap<String, IChartType> htTypes = new LinkedHashMap<>();
			Collection<IChartType> cTypes = ChartUIExtensionsImpl.instance().getUIChartTypeExtensions(getIdentifier());
			Iterator<IChartType> iterTypes = cTypes.iterator();
			while (iterTypes.hasNext()) {
				IChartType type = iterTypes.next();
				htTypes.put(type.getName(), type);
			}
			chartType = htTypes.get(chartModel.getType());
		}
		return chartType;
	}

	/**
	 * Returns identifier for current context implementation. This is usually used
	 * to register extensions for chart types, series UI provider, and etc.
	 *
	 * @return identifier
	 * @since 3.7
	 */
	public String getIdentifier() {
		return getClass().getSimpleName();
	}

	/**
	 * @param processor The processor to set.
	 */
	@Override
	public void setProcessor(IStyleProcessor processor) {
		this.processor = processor;
	}

	/**
	 * @return Returns the processor.
	 */
	@Override
	public IStyleProcessor getProcessor() {
		return processor;
	}

	/**
	 * @param isMoreAxesSupported The isMoreAxesSupported to set.
	 */
	public void setMoreAxesSupported(boolean isMoreAxesSupported) {
		this.isMoreAxesSupported = isMoreAxesSupported;
	}

	/**
	 * @return Returns the isMoreAxesSupported.
	 */
	public boolean isMoreAxesSupported() {
		return isMoreAxesSupported;
	}

	/**
	 * Returns if chart direction is right to left.
	 *
	 * @return True: right to left. False: left to right
	 */
	public boolean isRtL() {
		return isRtL;
	}

	/**
	 * Sets the chart direction.
	 *
	 * @param isRtL True: right to left. False: left to right
	 */
	public void setRtL(boolean isRtL) {
		this.isRtL = isRtL;
	}

	/**
	 * Returns if text direction is right to left.
	 *
	 * @return True: right to left. False: left to right
	 */
	public boolean isTextRtL() {
		return isTextRtL;
	}

	/**
	 * Sets the text direction.
	 *
	 * @param isRtL True: right to left. False: left to right
	 */
	public void setTextRtL(boolean isRtL) {
		this.isTextRtL = isRtL;
	}

	/**
	 * Sets the UI enabled or not. The UI, including task, subtask or toggle button,
	 * is identified by the exclusive id.
	 *
	 * @param id       the exclusive id to identify the UI
	 * @param bEnabled the state to enable the UI
	 * @since 2.3
	 */
	public void setEnabled(String id, boolean bEnabled) {
		if (mSheetEnabled == null) {
			mSheetEnabled = new HashMap<>();
		}
		mSheetEnabled.put(id, bEnabled);
	}

	/**
	 * Returns if the UI is enabled or not.The UI, including task, subtask or toggle
	 * button, is identified by the exclusive id.
	 *
	 * @param id the exclusive id to identify the UI
	 * @return the UI enabled state
	 * @since 2.3
	 */
	@Override
	public boolean isEnabled(String id) {
		if (mSheetEnabled != null && mSheetEnabled.containsKey(id)) {
			return mSheetEnabled.get(id);
		}
		return true;
	}

	/**
	 * Adds predefined queries for later selection.
	 *
	 * @param queryType   query type. See {@link ChartUIConstants#QUERY_CATEGORY},
	 *                    {@link ChartUIConstants#QUERY_VALUE},
	 *                    {@link ChartUIConstants#QUERY_OPTIONAL}
	 * @param expressions expression array
	 * @since 2.3
	 */
	public void addPredefinedQuery(String queryType, Object[] expressions) {
		if (mQueries == null) {
			mQueries = new HashMap<>();
		}
		mQueries.put(queryType, expressions);
	}

	/**
	 * Returns the predefined queries
	 *
	 * @param queryType query type. See {@link ChartUIConstants#QUERY_CATEGORY},
	 *                  {@link ChartUIConstants#QUERY_VALUE},
	 *                  {@link ChartUIConstants#QUERY_OPTIONAL}
	 * @return expression array
	 * @since 2.3
	 */
	public Object[] getPredefinedQuery(String queryType) {
		if (mQueries != null && mQueries.containsKey(queryType)) {
			return mQueries.get(queryType);
		}
		return null;
	}

	@Override
	public IChartDataSheet getDataSheet() {
		return dataSheet;
	}

	/**
	 * @return Returns the resourceFinder.
	 */
	public IResourceFinder getResourceFinder() {
		return resourceFinder;
	}

	/**
	 * @param resourceFinder The resourceFinder to set.
	 */
	public void setResourceFinder(IResourceFinder resourceFinder) {
		this.resourceFinder = resourceFinder;
	}

	/**
	 * @return Returns the externalizer.
	 */
	public IExternalizer getExternalizer() {
		return externalizer;
	}

	/**
	 * @param externalizer The externalizer to set.
	 */
	public void setExternalizer(IExternalizer externalizer) {
		this.externalizer = externalizer;
	}

	/**
	 * @param isInheritColumnsOnly The isInheritColumnsOnly to set.
	 */
	public void setInheritColumnsOnly(boolean isInheritColumnsOnly) {
		this.isInheritColumnsOnly = isInheritColumnsOnly;
	}

	/**
	 * @return Returns the isInheritColumnsOnly.
	 */
	public boolean isInheritColumnsOnly() {
		return isInheritColumnsOnly;
	}

	public boolean isShowingDataPreview() {
		if (isShowingDataPreview == null) {
			return true;
		}
		return isShowingDataPreview.booleanValue();
	}

	public void setShowingDataPreview(Boolean isShowingDataPreview) {
		this.isShowingDataPreview = isShowingDataPreview;
	}

	public boolean isSetShowingDataPreview() {
		return this.isShowingDataPreview != null;
	}

	public void setActionEvaluator(IActionEvaluator birtActionEvaluator) {
		actionEvaluator = birtActionEvaluator;
	}

	public IActionEvaluator getActionEvaluator() {
		return actionEvaluator;
	}

	/**
	 * Returns current UI factory class. The default UI factory is
	 * <code>ChartUIFactoryBase</code>.
	 *
	 * @return UI factory
	 */
	public IChartUIFactory getUIFactory() {
		return uiFactory;
	}

	/**
	 * Sets the new UI factory.
	 *
	 * @param factory UI factory
	 */
	public void setUIFactory(IChartUIFactory factory) {
		this.uiFactory = factory;
	}

	/**
	 * Checks if interactivity is supported.
	 *
	 * @return true means interactivity is supported
	 */
	public boolean isInteractivityEnabled() {
		return true;
	}

	public IImageServiceProvider getImageServiceProvider() {
		return imageServiceProvider;
	}

}
