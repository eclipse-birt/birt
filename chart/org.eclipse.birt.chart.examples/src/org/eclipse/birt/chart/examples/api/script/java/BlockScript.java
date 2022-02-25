/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.chart.examples.api.script.java;

import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.script.ChartEventHandlerAdapter;
import org.eclipse.birt.chart.script.IChartScriptContext;

public class BlockScript extends ChartEventHandlerAdapter {

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.script.IChartItemScriptHandler#beforeDrawBlock(org.
	 * eclipse.birt.chart.model.layout.Block,
	 * org.eclipse.birt.chart.script.IChartScriptContext)
	 */
	@Override
	public void beforeDrawBlock(Block block, IChartScriptContext icsc) {
		if (block.isLegend()) {
			block.getOutline().setVisible(true);
			block.getOutline().getColor().set(21, 244, 231);
		} else if (block.isPlot()) {
			block.getOutline().setVisible(true);
			block.getOutline().getColor().set(244, 21, 231);
		} else if (block.isTitle()) {
			block.getOutline().setVisible(true);
			block.setBackground(ColorDefinitionImpl.CREAM());
			block.getOutline().getColor().set(0, 0, 0);
		}
	}

}
