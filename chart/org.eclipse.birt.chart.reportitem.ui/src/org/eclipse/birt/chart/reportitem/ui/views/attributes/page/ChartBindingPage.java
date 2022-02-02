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

package org.eclipse.birt.chart.reportitem.ui.views.attributes.page;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.reportitem.ChartReportItemUtil;
import org.eclipse.birt.chart.reportitem.api.ChartCubeUtil;
import org.eclipse.birt.chart.reportitem.api.ChartItemUtil;
import org.eclipse.birt.chart.reportitem.api.ChartReportItemHelper;
import org.eclipse.birt.chart.reportitem.ui.ChartXTabUIUtil;
import org.eclipse.birt.chart.reportitem.ui.views.attributes.provider.ChartBindingGroupDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.BindingPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.PageSectionId;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.AggregateOnBindingsFormHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.BindingGroupDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.BindingGroupSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SortingFormSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.AggregateOnBindingsFormDescriptor;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.olap.CubeHandle;

public class ChartBindingPage extends BindingPage {

	protected BindingGroupDescriptorProvider createBindingGroupDescriptorProvider() {
		return new ChartBindingGroupDescriptorProvider();
	}

	protected void applyCustomSections() {
		BindingGroupDescriptorProvider bindingProvider = createBindingGroupDescriptorProvider();
		bindingProvider.setRefrenceSection(((BindingGroupSection) getSection(PageSectionId.BINDING_GROUP)));
		((BindingGroupSection) getSection(PageSectionId.BINDING_GROUP)).setProvider(bindingProvider);
		AggregateOnBindingsFormHandleProvider dataSetFormProvider = createDataSetFormProvider();
		((SortingFormSection) getSection(PageSectionId.BINDING_DATASET_FORM))
				.setCustomForm(new AggregateOnBindingsFormDescriptor(true) {

					@Override
					public void setInput(Object object) {
						super.setInput(object);
						// always enable refresh button
						btnRefresh.setEnabled(true);
					}
				});
		((SortingFormSection) getSection(PageSectionId.BINDING_DATASET_FORM)).setProvider(dataSetFormProvider);
		if (((BindingGroupSection) getSection(PageSectionId.BINDING_GROUP)).getProvider() != null) {
			IDescriptorProvider dataSetProvider = ((BindingGroupSection) getSection(PageSectionId.BINDING_GROUP))
					.getProvider();
			if (dataSetProvider instanceof BindingGroupDescriptorProvider)
				((BindingGroupDescriptorProvider) dataSetProvider).setDependedProvider(dataSetFormProvider);
		}
	}

	/**
	 * Create different dataset provider for common and sharing case.
	 * 
	 * @return
	 * @since 2.3
	 */
	protected AggregateOnBindingsFormHandleProvider createDataSetFormProvider() {
		return new AggregateOnBindingsFormHandleProvider() {

			@Override
			public boolean isEditable() {
				if (input == null) {
					return super.isEditable();
				}

				final ReportItemHandle rih;
				if (input instanceof List) {
					rih = (ExtendedItemHandle) ((List) input).get(0);
				} else {
					rih = (ExtendedItemHandle) input;
				}

				// Multi-view case
				// Don't allow to edit bindings in chart property page when chart is in
				// multi-views, so return false.
				if (ChartReportItemUtil.isChildOfMultiViewsHandle(rih)) {
					return false;
				}

				// Sharing, Cube, Inheriting and x-chart cases.
				boolean isSharing = false;
				if (ChartItemUtil.getReportItemReference(rih) != null) {
					isSharing = true;
				}
				boolean useCube = (ChartReportItemHelper.instance().getBindingCubeHandle(rih) != null);
				return !isSharing && (!useCube) && !ChartItemUtil.isChartInheritGroups(rih)
						&& !ChartCubeUtil.isAxisChart(rih) && !ChartCubeUtil.isPlotChart(rih);
			}

			@Override
			public void generateAllBindingColumns() {
				// for cube binding refresh
				super.generateAllBindingColumns();
				if (getBindingObject() != null) {
					CubeHandle cube = null;
					if (getBindingObject() instanceof ExtendedItemHandle) {
						cube = ((ExtendedItemHandle) getBindingObject()).getCube();
					}
					if (cube != null) {
						try {
							ExtendedItemHandle inputElement = (ExtendedItemHandle) getBindingObject();
							inputElement.getColumnBindings().clearValue();

							List<ComputedColumn> columnList = ChartXTabUIUtil.generateComputedColumns(inputElement,
									cube);

							if (columnList.size() > 0) {
								for (Iterator<ComputedColumn> iter = columnList.iterator(); iter.hasNext();) {
									DEUtil.addColumn(inputElement, iter.next(), false);
								}
							}
						} catch (SemanticException e) {
							// do nothing
						}

					}
				}

			}

		};
	}
}
