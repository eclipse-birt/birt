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
