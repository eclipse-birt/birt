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
 * 
 */
public interface ICrosstabViewConstants {

	/**
	 * Name of the property that holds the grand total header cell for this crosstab
	 * view.
	 */
	String GRAND_TOTAL_PROP = "grandTotal"; //$NON-NLS-1$

	/**
	 * Name of the property that contains list of DimensionView elements.
	 */
	String VIEWS_PROP = "views"; //$NON-NLS-1$

	/**
	 * Name of the property that defines a list of CrosstabMemberValue.
	 */
	String MEMBERS_PROP = "members"; //$NON-NLS-1$

	/**
	 * Name of the property that specifies if start from which level this view is
	 * mirrored.
	 */
	String MIRROR_STARTING_LEVEL_PROP = "mirrorStartingLevel"; //$NON-NLS-1$

	/**
	 * Name of the property that specifies the location for grand total header
	 */
	String GRAND_TOTAL_LOCATIION_PROP = "grandTotalLocation"; //$NON-NLS-1$
}
