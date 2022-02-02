/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.chart.reportitem;

import org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator;
import org.eclipse.birt.chart.factory.IGroupedDataRowExpressionEvaluator;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.Serializer;
import org.eclipse.birt.chart.render.IActionRenderer;
import org.eclipse.birt.chart.reportitem.api.IChartReportItem;
import org.eclipse.birt.report.data.adapter.api.IModelAdapter;
import org.eclipse.birt.report.engine.api.IHTMLActionHandler;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.extension.ICubeResultSet;
import org.eclipse.birt.report.engine.extension.IReportItemPresentation;
import org.eclipse.birt.report.engine.extension.IReportItemPresentationInfo;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;

/**
 * Factory class used to create instances.
 */

public interface IChartReportItemFactory {

	IActionRenderer createActionRenderer(DesignElementHandle eih, IHTMLActionHandler handler,
			IDataRowExpressionEvaluator evaluator, IReportContext context);

	IReportItemPresentation createReportItemPresentation(IReportItemPresentationInfo info);

	Serializer createSerializer(ExtendedItemHandle eih);

	ChartCubeQueryHelper createCubeQueryHelper(ExtendedItemHandle handle, Chart cm, IModelAdapter modelAdapter);

	IGroupedDataRowExpressionEvaluator createCubeEvaluator(Chart cm, ICubeResultSet set);

	IChartReportItem createChartReportItem(ExtendedItemHandle eih);

	ChartBaseQueryHelper createQueryHelper(ExtendedItemHandle handle, Chart cm, IModelAdapter modelAdapter);
}
