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

package org.eclipse.birt.chart.reportitem;

import org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator;
import org.eclipse.birt.chart.render.IActionRenderer;
import org.eclipse.birt.report.engine.api.IHTMLActionHandler;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.extension.IReportItemPresentation;
import org.eclipse.birt.report.engine.extension.IReportItemPresentationInfo;
import org.eclipse.birt.report.model.api.DesignElementHandle;

/**
 * Factory class used to create instances.
 */

public interface IChartReportItemFactory
{

	IActionRenderer createActionRenderer( DesignElementHandle eih,
			IHTMLActionHandler handler, IDataRowExpressionEvaluator evaluator,
			IReportContext context );

	IReportItemPresentation createReportItemPresentation(
			IReportItemPresentationInfo info );
}
