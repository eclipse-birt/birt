/*
 *************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
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
 *  
 *************************************************************************
 */

package org.eclipse.birt.report.data.adapter.internal.adapter;

import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;
import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.data.adapter.api.IModelAdapter;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.SortHintHandle;

public class SortHintAdapter extends SortDefinition {

	public SortHintAdapter(IModelAdapter adapter, SortHintHandle sortHint) throws AdapterException {
		IScriptExpression expr = adapter.adaptExpression(sortHint.getColumnName(), ExpressionType.JAVASCRIPT);
		this.setExpression(expr);
		this.setSortDirection(SortAdapter.sortDirectionFromModel(sortHint.getDirection()));
	}
}
