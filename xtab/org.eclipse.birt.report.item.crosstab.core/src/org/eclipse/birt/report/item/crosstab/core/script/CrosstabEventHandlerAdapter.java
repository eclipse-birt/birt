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
 * CrosstabEventHandlerAdapter
 */
public class CrosstabEventHandlerAdapter implements ICrosstabEventHandler {

	@Override
	public void onPrepareCrosstab(ICrosstab crosstab, IReportContext reportContext) {
	}

	@Override
	public void onPrepareCell(ICrosstabCell cell, IReportContext reportContext) {
	}

	@Override
	public void onCreateCrosstab(ICrosstabInstance crosstab, IReportContext reportContext) {
	}

	@Override
	public void onCreateCell(ICrosstabCellInstance cell, IReportContext reportContext) {
	}

	@Override
	public void onRenderCrosstab(ICrosstabInstance crosstab, IReportContext reportContext) {
	}

	@Override
	public void onRenderCell(ICrosstabCellInstance cell, IReportContext reportContext) {
	}

	// public void onCrosstabPageBreak( ICrosstabInstance crosstab,
	// IReportContext reportContext )
	// {
	// }
	//
	// public void onCellPageBreak( ICrosstabCellInstance cell,
	// IReportContext reportContext )
	// {
	// }

}
