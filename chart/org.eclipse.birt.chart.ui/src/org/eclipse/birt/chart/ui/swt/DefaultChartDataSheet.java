/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

package org.eclipse.birt.chart.ui.swt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartDataSheet;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataCustomizeUI;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.ITask;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.IWizardContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 *
 */

public class DefaultChartDataSheet implements IChartDataSheet {

	private List<Listener> listeners = new ArrayList<>(2);
	private Chart cm;
	protected ChartWizardContext context;

	@Override
	public void addListener(Listener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	@Override
	public Composite createActionButtons(Composite parent) {
		return new Composite(parent, SWT.NONE);
	}

	@Override
	public Composite createDataDragSource(Composite parent) {
		return new Composite(parent, SWT.NONE);
	}

	@Override
	public Composite createDataSelector(Composite parent) {
		return new Composite(parent, SWT.NONE);
	}

	@Override
	public void removeListener(Listener listener) {
		listeners.remove(listener);
	}

	@Override
	public void notifyListeners(Event event) {
		for (Iterator<Listener> iterator = listeners.iterator(); iterator.hasNext();) {
			iterator.next().handleEvent(event);
		}
	}

	@Override
	public void dispose() {
		listeners.clear();
	}

	@Override
	public void setChartModel(Chart cm) {
		this.cm = cm;

	}

	protected Chart getChartModel() {
		return this.cm;
	}

	@Override
	public void setContext(IWizardContext context) {
		assert context instanceof ChartWizardContext;
		this.context = (ChartWizardContext) context;
	}

	protected ChartWizardContext getContext() {
		return this.context;
	}

	@Override
	public ISelectDataCustomizeUI createCustomizeUI(ITask task) {
		return null;
	}

	public List<String> getAllValueDefinitions() {
		return new ArrayList<>(2);
	}

}
