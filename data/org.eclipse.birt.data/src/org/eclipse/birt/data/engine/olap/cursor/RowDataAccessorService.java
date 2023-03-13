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
package org.eclipse.birt.data.engine.olap.cursor;

import org.eclipse.birt.data.engine.olap.driver.DimensionAxis;
import org.eclipse.birt.data.engine.olap.query.view.BirtEdgeView;

/**
 * This class provide the available information when populating edgeInfo.
 *
 */
public class RowDataAccessorService {
	private DimensionAxis[] dimAxis;
	private BirtEdgeView view;

	/**
	 *
	 * @param rs
	 * @param isPage
	 * @param dimAxis
	 * @param mirrorStartPosition
	 */
	public RowDataAccessorService(DimensionAxis[] dimAxis, BirtEdgeView view) {
		this.dimAxis = dimAxis;
		this.view = view;
	}

	public DimensionAxis[] getDimensionAxis() {
		return this.dimAxis;
	}

	public int getPagePosition() {
		return this.view.getPageEndingIndex();
	}
}
