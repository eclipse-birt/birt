/*
 *************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
