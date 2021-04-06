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

package org.eclipse.birt.report.item.crosstab.core.util;

import java.util.Map;

import org.eclipse.birt.report.item.crosstab.core.de.internal.ICrosstabModelListener;

/**
 * Listener interface to monitor crosstab model updates
 */
public interface ICrosstabUpdateListener extends ICrosstabModelListener {

	int MEASURE_HEADER = 1;
	int MEASURE_DETAIL = 2;
	int MEASURE_AGGREGATION = 3;

	String EXTRA_FUNCTION_HINT = "function.hint"; //$NON-NLS-1$

	/**
	 * Sets the context for crosstab model update. Note this context may change and
	 * be set frequently, so do not cache it across event calls.
	 */
	void setContext(ICrosstabUpdateContext context);

	/**
	 * Called after certain crosstab model has been created.
	 */
	void onCreated(int type, Object model, Map<String, Object> extras);

	/**
	 * Called whenever the given crosstab model need be validated.
	 */
	void onValidate(int type, Object model, Map<String, Object> extras);
}
