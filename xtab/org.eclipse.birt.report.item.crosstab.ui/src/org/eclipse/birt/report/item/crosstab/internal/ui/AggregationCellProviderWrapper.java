/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
/**
 *
 */

package org.eclipse.birt.report.item.crosstab.internal.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.ComputedMeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.ui.extension.IAggregationCellViewProvider;
import org.eclipse.birt.report.item.crosstab.ui.extension.SwitchCellInfo;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;

import com.ibm.icu.text.Collator;

/**
 * @author Administrator
 *
 */
public class AggregationCellProviderWrapper {
	private ProviderComparator providerComparator = new ProviderComparator(false);
	ExtendedItemHandle handle;
	CrosstabReportItemHandle crosstab;
	private IAggregationCellViewProvider[] providers;
	private List<AggregationCellHandle> filterCellList = new ArrayList<>();
	private List<SwitchCellInfo> switchList = new ArrayList<>();

	/**
	 *
	 * @param handle
	 */
	public AggregationCellProviderWrapper(ExtendedItemHandle handle) {

		IReportItem reportItem = null;
		try {
			reportItem = handle.getReportItem();
		} catch (ExtendedElementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assert (reportItem instanceof CrosstabReportItemHandle);
		this.crosstab = (CrosstabReportItemHandle) reportItem;
		this.handle = handle;
		inilitializeProviders();
	}

	public AggregationCellProviderWrapper(CrosstabReportItemHandle crosstab) {
		this((ExtendedItemHandle) crosstab.getModelHandle());
	}

	private void inilitializeProviders() {
		Object obj = ElementAdapterManager.getAdapters(handle, IAggregationCellViewProvider.class);

		if (obj instanceof Object[]) {
			Object arrays[] = (Object[]) obj;
			// arrays = setDefaultOrder((Object[])arrays);
			Arrays.sort(arrays, providerComparator);
			providers = new IAggregationCellViewProvider[arrays.length + 1];
			providers[0] = null;
			for (int i = 0; i < arrays.length; i++) {
				IAggregationCellViewProvider tmp = (IAggregationCellViewProvider) arrays[i];
				providers[i + 1] = tmp;
			}
		}

	}

	static class ProviderComparator implements Comparator {
		private boolean ascending = true;

		public ProviderComparator(boolean ascending) {
			this.ascending = ascending;
		}

		public ProviderComparator() {
			this(true);
		}

		@Override
		public int compare(Object arg0, Object arg1) {
			// TODO Auto-generated method stub
			assert (arg0 instanceof IAggregationCellViewProvider);
			assert (arg1 instanceof IAggregationCellViewProvider);

			String name0 = ((IAggregationCellViewProvider) arg0).getViewDisplayName();
			String name1 = ((IAggregationCellViewProvider) arg1).getViewDisplayName();

			if (name0 == null) {
				name0 = "";//$NON-NLS-1$
			}
			if (name1 == null) {
				name1 = "";//$NON-NLS-1$
			}

			if (ascending) {
				return Collator.getInstance().compare(name0, name1);
			} else {
				return Collator.getInstance().compare(name1, name0);
			}
		}
	}

	public IAggregationCellViewProvider[] getAllProviders() {
		return providers;
	}

	public boolean switchView(String expectedView, AggregationCellHandle cell) {
		boolean ret = false;

		IAggregationCellViewProvider provider = getMatchProvider(cell);
		if (provider != null) {
			// if current view is the same view with the expected one, then don't restore
			if (!provider.getViewName().equals(expectedView)) {
				provider.restoreView(cell);
			}
		}

		provider = getProvider(expectedView);
		if (provider == null) {
			return ret;
		}
		ret = true;

		provider.switchView(cell);
		filterCellList.add(cell);
		return ret;
	}

	public void restoreViews(SwitchCellInfo info) {
		AggregationCellHandle cell = info.getAggregationCell();
		String expectedView = info.getExpectedView();
		if (expectedView == null || expectedView.length() == 0) {
			return;
		}

		IAggregationCellViewProvider provider = getMatchProvider(cell);
		if (provider != null) {
			provider.restoreView(cell);
		}
	}

	public boolean switchView(SwitchCellInfo info) {
		boolean ret = false;

		AggregationCellHandle cell = info.getAggregationCell();
		String expectedView = info.getExpectedView();
		if (expectedView == null || expectedView.length() == 0) {
			return false;
		}

		IAggregationCellViewProvider provider = getMatchProvider(cell);
		if (provider != null) {
			// if current view is the same view with the expected one, then do nothing
			if (provider.getViewName().equals(expectedView)) {
				return false;
			} else {
				provider.restoreView(cell);
			}
		}

		provider = getProvider(expectedView);
		if (provider == null) {
			return ret;
		}
		ret = true;

		provider.switchView(info);
		filterCellList.add(cell);

		return ret;
	}

	public IAggregationCellViewProvider getProvider(String viewName) {
		IAggregationCellViewProvider retProvider = null;
		if (viewName == null || providers == null || providers.length <= 0) {
			return null;
		}
		for (int i = 0; i < providers.length; i++) {
			if (providers[i] == null) {
				continue;
			}
			if (providers[i].getViewName().equals(viewName)) {
				retProvider = providers[i];
				break;
			}
		}
		return retProvider;
	}

	public IAggregationCellViewProvider getMatchProvider(AggregationCellHandle cell) {
		IAggregationCellViewProvider retProvider = null;
		if (providers == null || providers.length <= 0) {
			return null;
		}
		for (int i = 0; i < providers.length; i++) {
			if (providers[i] == null) {
				continue;
			}
			if (providers[i].matchView(cell)) {
				retProvider = providers[i];
				break;
			}
		}
		return retProvider;
	}

	public void updateAggregationCell(AggregationCellHandle cell) {
		IAggregationCellViewProvider provider = getMatchProvider(cell);
		if (provider != null) {
			provider.updateView(cell);
		}

	}

	public void updateAggregationCell(AggregationCellHandle cell, int type) {
		IAggregationCellViewProvider provider = getMatchProvider(cell);
		if (provider != null) {
			provider.updateView(cell, type);
		}

	}

	public void addSwitchInfo(SwitchCellInfo info) {
		switchList.add(info);
	}

	public void updateAllAggregationCells() {
		int measureCount = crosstab.getMeasureCount();
		for (int i = 0; i < measureCount; i++) {
			MeasureViewHandle measure = crosstab.getMeasure(i);
			if (measure == null || measure instanceof ComputedMeasureViewHandle) {
				continue;
			}
			AggregationCellHandle cell = measure.getCell();
			if (filterCellList.indexOf(cell) < 0) {
				updateAggregationCell(cell);
			}

			for (int j = 0; j < measure.getAggregationCount(); j++) {
				cell = measure.getAggregationCell(j);
				if (filterCellList.indexOf(cell) >= 0) {
					continue;
				}
				updateAggregationCell(cell);
			}
		}

		filterCellList.clear();
	}

	public void updateAllAggregationCells(int types) {
		int measureCount = crosstab.getMeasureCount();
		for (int i = 0; i < measureCount; i++) {
			MeasureViewHandle measure = crosstab.getMeasure(i);
			if (measure == null || measure instanceof ComputedMeasureViewHandle) {
				continue;
			}
			AggregationCellHandle cell = measure.getCell();
			if (filterCellList.indexOf(cell) < 0) {
				updateAggregationCell(cell, types);
			}

			for (int j = 0; j < measure.getAggregationCount(); j++) {
				cell = measure.getAggregationCell(j);
				if (filterCellList.indexOf(cell) >= 0) {
					continue;
				}
				updateAggregationCell(cell, types);
			}
		}

		filterCellList.clear();
	}

	public void switchViews() {
		for (int i = 0; i < switchList.size(); i++) {
			SwitchCellInfo info = switchList.get(i);
//			AggregationCellHandle cell = info.getAggregationCell( );
//			String expectedView = info.getExpectedView( );
			switchView(info);
		}
		switchList.clear();
	}

	public String getViewDisplayName(String viewName) {
		if (viewName == null || viewName.length() == 0) {
			return "";
		}

		for (int i = 0; i < providers.length; i++) {
			if (providers[i] == null) {
				continue;
			}
			String cmpName = providers[i].getViewName();
			if (cmpName != null && cmpName.equals(viewName)) {
				return providers[i].getViewDisplayName();
			}
		}
		return "";
	}
}
