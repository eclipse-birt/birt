/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.core.script;

import org.eclipse.birt.report.model.api.simpleapi.IReportElement;

/**
 * ICrosstabCell
 */
public interface ICrosstabCell extends IReportElement {

	String TYPE_HEADER = "header"; //$NON-NLS-1$

	String TYPE_AGGREGATION = "aggregation"; //$NON-NLS-1$

	long getCellID();

	String getCellType();
}
