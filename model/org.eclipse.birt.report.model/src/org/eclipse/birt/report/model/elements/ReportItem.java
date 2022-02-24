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

package org.eclipse.birt.report.model.elements;

import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.elements.interfaces.ISupportThemeElement;
import org.eclipse.birt.report.model.elements.strategy.ReportItemPropSearchStrategy;

/**
 * Base class for all report items. Represents anything that can be placed in a
 * layout container. Items have a size and position that are used in some of the
 * containers.
 * 
 */

public abstract class ReportItem extends ReportItemImpl implements IReportItemModel, ISupportThemeElement {

	/**
	 * Default constructor.
	 */

	public ReportItem() {
		this(null);
	}

	/**
	 * Constructs the report item with an optional name.
	 * 
	 * @param theName the optional name
	 */

	public ReportItem(String theName) {
		super(theName);
		cachedPropStrategy = ReportItemPropSearchStrategy.getInstance();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#validate(org.eclipse
	 * .birt.report.model.elements.ReportDesign)
	 */
}
