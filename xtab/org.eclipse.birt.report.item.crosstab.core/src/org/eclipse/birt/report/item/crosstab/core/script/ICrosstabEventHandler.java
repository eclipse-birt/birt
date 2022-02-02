/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

package org.eclipse.birt.report.item.crosstab.core.script;

import org.eclipse.birt.report.engine.api.script.IReportContext;

/**
 * ICrosstabEventHandler
 */
public interface ICrosstabEventHandler {

	void onPrepareCrosstab(ICrosstab crosstab, IReportContext reportContext);

	void onPrepareCell(ICrosstabCell cell, IReportContext reportContext);

	void onCreateCrosstab(ICrosstabInstance crosstab, IReportContext reportContext);

	void onCreateCell(ICrosstabCellInstance cell, IReportContext reportContext);

	void onRenderCrosstab(ICrosstabInstance crosstab, IReportContext reportContext);

	void onRenderCell(ICrosstabCellInstance cell, IReportContext reportContext);

	// void onCrosstabPageBreak( ICrosstabInstance crosstab,
	// IReportContext reportContext );
	//
	// void onCellPageBreak( ICrosstabCellInstance cell,
	// IReportContext reportContext );
}
