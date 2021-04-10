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

package org.eclipse.birt.report.item.crosstab.core;

/**
 * Interface to define some constants for MeasureViewHandle.
 */

public interface IMeasureViewConstants {

	/**
	 * Name of the property that refers a OLAP measure element.
	 */
	String MEASURE_PROP = "measure"; //$NON-NLS-1$

	/**
	 * Name of the property that holds single CrosstabCell.
	 */
	String DETAIL_PROP = "detail"; //$NON-NLS-1$

	/**
	 * Name of the property that contains list of AggregationCell.
	 */
	String AGGREGATIONS_PROP = "aggregations"; //$NON-NLS-1$

	/**
	 * Name of the property that holds single CrosstabCell to show the header for
	 * this measure.
	 */
	String HEADER_PROP = "header"; //$NON-NLS-1$

	/**
	 * Name of the property that defines some filter conditions.
	 */
	String FILTER_PROP = "filter"; //$NON-NLS-1$
}
