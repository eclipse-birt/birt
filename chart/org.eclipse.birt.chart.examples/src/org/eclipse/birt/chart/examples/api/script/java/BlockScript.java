/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.examples.api.script.java;

import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
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
