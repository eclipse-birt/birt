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

package org.eclipse.birt.report.item.crosstab.core;

/**
 * Interface to define some constants for Aggregation Cell.
 */

public interface IAggregationCellConstants extends ICrosstabCellConstants {

	/**
	 * Name of the property that refers the level view with this aggregation applied
	 * on.
	 */
	String AGGREGATION_ON_ROW_PROP = "aggregationOnRow"; //$NON-NLS-1$

	/**
	 * Name of the property that refers the level view with this aggregation applied
	 * on.
	 */
	String AGGREGATION_ON_COLUMN_PROP = "aggregationOnColumn"; //$NON-NLS-1$

	/**
	 * Name of the property that refers the target row level for this cell to span
	 * over.
	 *
	 * @since 2.3
	 */
	String SPAN_OVER_ON_ROW_PROP = "spanOverOnRow"; //$NON-NLS-1$

	/**
	 * Name of the property that refers the target column level for this cell to
	 * span over.
	 *
	 * @since 2.3
	 */
	String SPAN_OVER_ON_COLUMN_PROP = "spanOverOnColumn"; //$NON-NLS-1$
}
