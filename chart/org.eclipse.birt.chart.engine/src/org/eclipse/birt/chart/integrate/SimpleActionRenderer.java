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

package org.eclipse.birt.chart.integrate;

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.event.StructureType;
import org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.TooltipValue;
import org.eclipse.birt.chart.model.attribute.URLValue;
import org.eclipse.birt.chart.model.data.Action;
import org.eclipse.birt.chart.render.ActionRendererAdapter;
import org.eclipse.birt.chart.util.ChartUtil;

/**
 * Simple implementation for IActionRenderer
 */

public class SimpleActionRenderer extends ActionRendererAdapter {

	private final IDataRowExpressionEvaluator evaluator;

	public SimpleActionRenderer(IDataRowExpressionEvaluator evaluator) {
		this.evaluator = evaluator;
	}

	@Override
	public void processAction(Action action, StructureSource source, RunTimeContext rtc) {
		if (ActionType.URL_REDIRECT_LITERAL.equals(action.getType())) {
			URLValue uv = (URLValue) action.getValue();

			String sa = uv.getBaseUrl();
			SimpleActionHandle handle = SimpleActionUtil.deserializeAction(sa);
			String uri = handle.getURI();
			if (StructureType.SERIES_DATA_POINT.equals(source.getType()) && evaluator != null) {
				final DataPointHints dph = (DataPointHints) source.getSource();
				uri = ChartUtil.stringValue(dph.getUserValue(uri));
			}
			uv.setBaseUrl(uri);
			uv.setTarget(handle.getTargetWindow());
		} else if (ActionType.SHOW_TOOLTIP_LITERAL.equals(action.getType())) {
			TooltipValue tv = (TooltipValue) action.getValue();

			if (StructureType.SERIES_DATA_POINT.equals(source.getType())) {
				final DataPointHints dph = (DataPointHints) source.getSource();
				tv.setText(ChartUtil.stringValue(dph.getUserValue(tv.getText())));
			}
		}
	}

}
