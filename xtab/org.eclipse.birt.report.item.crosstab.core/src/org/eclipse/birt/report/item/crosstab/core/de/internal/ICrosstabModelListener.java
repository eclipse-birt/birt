/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.core.de.internal;

import org.eclipse.birt.report.item.crosstab.core.util.ICrosstabUpdateListener;

/**
 * ICrosstabModelListener
 * 
 * @deprecated use {@link ICrosstabUpdateListener} instead
 */
public interface ICrosstabModelListener {

	int MEASURE_HEADER = 1;
	int MEASURE_DETAIL = 2;

	/**
	 * Called after certain crosstab model has been created.
	 * 
	 * @param type
	 * @param model
	 */
	void onCreated(int type, Object model);

	/**
	 * Called whenever the given crosstab model need be validated.
	 * 
	 * @param type
	 * @param model
	 */
	void onValidate(int type, Object model);
}
