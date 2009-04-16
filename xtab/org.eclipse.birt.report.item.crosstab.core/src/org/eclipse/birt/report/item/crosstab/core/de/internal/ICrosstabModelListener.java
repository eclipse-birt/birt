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

/**
 * ICrosstabModelListener
 */
public interface ICrosstabModelListener
{

	int MEASURE_HEADER = 1;

	/**
	 * Called after certain crosstab model has been created.
	 * 
	 * @param type
	 * @param model
	 */
	void onCreated( int type, Object model );
}
