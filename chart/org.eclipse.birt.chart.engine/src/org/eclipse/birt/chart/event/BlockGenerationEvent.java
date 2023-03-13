/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.event;

import org.eclipse.birt.chart.model.layout.Block;

/**
 * An event type for block generation.
 */
public class BlockGenerationEvent extends ChartEvent {

	private static final long serialVersionUID = 5869588499778117671L;

	/**
	 * The constructor.
	 */
	public BlockGenerationEvent(Object oSource) {
		super(oSource);
	}

	/**
	 * Updates the associated block object.
	 *
	 * @param bl
	 */
	public void updateBlock(Block bl) {
		source = bl;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.event.ChartEvent#reset()
	 */
	@Override
	public void reset() {
		source = null;
	}
}
