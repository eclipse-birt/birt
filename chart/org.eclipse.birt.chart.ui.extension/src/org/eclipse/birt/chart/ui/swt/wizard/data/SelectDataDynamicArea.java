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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataComponent;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataCustomizeUI;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.ITask;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 */

public class SelectDataDynamicArea implements ISelectDataCustomizeUI {

	private ITask task = null;

	protected List<ISelectDataComponent> subLeftAreas = new ArrayList<ISelectDataComponent>();
	protected List<ISelectDataComponent> subRightAreas = new ArrayList<ISelectDataComponent>();

	protected Composite cmpLeftArea = null;
	protected Composite cmpRightArea = null;
	protected Composite cmpBottomArea = null;

	protected ISelectDataComponent bottomArea;

	private int[] seriesIndex = new int[0];

	public SelectDataDynamicArea(ITask task) {
		this.task = task;
	}

	protected ChartWizardContext getContext() {
		return (ChartWizardContext) task.getContext();
	}

	protected Chart getChartModel() {
		return getContext().getModel();
	}

	public void selectLeftBindingArea(boolean selected, Object data) {
		for (int i = 0; i < subLeftAreas.size(); i++) {
			subLeftAreas.get(i).selectArea(selected, data);
		}
	}

	public void selectRightBindingArea(boolean selected, Object data) {
		for (int i = 0; i < subRightAreas.size(); i++) {
			subRightAreas.get(i).selectArea(selected, data);
		}
	}

	public void selectBottomBindingArea(boolean selected, Object data) {
		bottomArea.selectArea(selected, data);
	}

	public void dispose() {
		List<ISelectDataComponent> list = subLeftAreas;
		list.addAll(subRightAreas);
		for (int i = 0; i < list.size(); i++) {
			list.get(i).dispose();
		}
		if (bottomArea != null) {
			bottomArea.dispose();
		}
		list.clear();
	}

	public ISelectDataComponent getAreaComponent(int areaType, SeriesDefinition seriesdefinition,
			ChartWizardContext context, String sTitle) {
		return ChartUIUtil.getSeriesUIProvider(seriesdefinition.getDesignTimeSeries()).getSeriesDataComponent(areaType,
				seriesdefinition, context, sTitle);
	}

	public void refreshLeftBindingArea() {
		subLeftAreas.clear();
		Composite cmpContainer = cmpLeftArea.getParent();
		cmpLeftArea.dispose();
		createLeftBindingArea(cmpContainer);

		cmpContainer.layout();
	}

	public void refreshRightBindingArea() {
		subRightAreas.clear();
		Composite cmpContainer = cmpRightArea.getParent();
		cmpRightArea.dispose();
		createRightBindingArea(cmpContainer);

		cmpContainer.layout();
	}

	public void refreshBottomBindingArea() {
		Composite cmpContainer = cmpBottomArea.getParent();
		cmpBottomArea.dispose();
		createBottomBindingArea(cmpContainer);

		cmpContainer.layout();
	}

	protected MultipleSeriesSelectorComponent createMultipleSeriesSelectorComponent(
			EList<SeriesDefinition>[] seriesDefnsArray, ChartWizardContext wizardContext, String sTitle,
			ISelectDataCustomizeUI selectDataUI) {
		return new MultipleSeriesSelectorComponent(seriesDefnsArray, getContext(), "", //$NON-NLS-1$
				this);
	}

	@SuppressWarnings("unchecked")
	public void createLeftBindingArea(Composite parent) {
		cmpLeftArea = ChartUIUtil.createCompositeWrapper(parent);
		{
			GridData gd = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER);
			gd.minimumWidth = 100;
			cmpLeftArea.setLayoutData(gd);
		}

		if (getChartModel() instanceof ChartWithAxes) {
			int axisNum = ChartUIUtil.getOrthogonalAxisNumber(getChartModel());
			EList<SeriesDefinition>[] seriesDefnArray = new EList[axisNum];
			EList<Axis> axisList = getYAxisListForProcessing();
			if (axisList != null && !axisList.isEmpty()) {
				for (int i = 0; i < axisList.size(); i++) {
					seriesDefnArray[i] = axisList.get(i).getSeriesDefinitions();
				}
			}
			ISelectDataComponent component = createMultipleSeriesSelectorComponent(seriesDefnArray, getContext(), "", //$NON-NLS-1$
					this);
			subLeftAreas.add(component);
			component.createArea(cmpLeftArea);
		} else {
			MultipleSeriesSelectorComponent component = createMultipleSeriesSelectorComponent(
					new EList[] { getValueSeriesDefinitionForProcessing() }, getContext(), "", //$NON-NLS-1$
					this);

			component.createArea(cmpLeftArea);
			subLeftAreas.add(component);
		}
	}

	@SuppressWarnings("unchecked")
	public void createRightBindingArea(Composite parent) {
		cmpRightArea = ChartUIUtil.createCompositeWrapper(parent);
		cmpRightArea.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));

		if (getChartModel() instanceof ChartWithAxes) {
			int axisNum = ChartUIUtil.getOrthogonalAxisNumber(getChartModel());
			EList<SeriesDefinition>[] seriesDefnArray = new EList[axisNum];
			EList<Axis> axisList = getYAxisListForProcessing();
			if (axisList != null && !axisList.isEmpty()) {
				for (int i = 0; i < axisList.size(); i++) {
					seriesDefnArray[i] = axisList.get(i).getSeriesDefinitions();
				}
			}
			ISelectDataComponent component = new MultipleSeriesComponent(seriesDefnArray, getContext(),
					Messages.getString("AbstractSelectDataCustomizeUI.Label.SeriesGrouping"), this); //$NON-NLS-1$
			subRightAreas.add(component);
			component.createArea(cmpRightArea);
		} else {
			ISelectDataComponent component = new MultipleSeriesComponent(getValueSeriesDefinitionForProcessing(),
					getContext(), Messages.getString("AbstractSelectDataCustomizeUI.Label.SeriesGrouping"), this); //$NON-NLS-1$
			subRightAreas.add(component);
			component.createArea(cmpRightArea);
		}
	}

	private EList<Axis> getYAxisListForProcessing() {
		return ((ChartWithAxes) getChartModel()).getAxes().get(0).getAssociatedAxes();
	}

	public void createBottomBindingArea(Composite parent) {
		bottomArea = getContext().getChartType().getBaseUI(getChartModel(), this, getContext(), ""); //$NON-NLS-1$
		cmpBottomArea = bottomArea.createArea(parent);
	}

	private SeriesDefinition getBaseSeriesDefinitionForProcessing() {
		return ((ChartWithoutAxes) getChartModel()).getSeriesDefinitions().get(0);
	}

	private EList<SeriesDefinition> getValueSeriesDefinitionForProcessing() {
		return getBaseSeriesDefinitionForProcessing().getSeriesDefinitions();
	}

	public void layoutAll() {
		if (cmpBottomArea != null && !cmpBottomArea.isDisposed()) {
			cmpBottomArea.getParent().getParent().layout();
		}
	}

	public int[] getSeriesIndex() {
		return seriesIndex;
	}

	public void setSeriesIndex(int[] seriesIndex) {
		this.seriesIndex = seriesIndex;
	}

	public void init() {
		// Reset selected series index to 0
		seriesIndex = new int[ChartUIUtil.getOrthogonalAxisNumber(getChartModel())];
	}

	public void notifyChange(Notification notification) {
		// Do nothing
	}
}
