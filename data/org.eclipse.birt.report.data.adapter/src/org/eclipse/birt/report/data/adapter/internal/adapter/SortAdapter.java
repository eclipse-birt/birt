/*
 *************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
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

import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;
import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.data.adapter.api.IModelAdapter;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionHandle;
import org.eclipse.birt.report.model.api.SortKeyHandle;
import org.eclipse.birt.report.model.api.elements.structures.SortKey;

/**
 * Definition of a sort condition, which comprises of a sort key expression and
 * a sort direction based on that key
 */
public class SortAdapter extends SortDefinition {
	/**
	 * Creates a new sort based on the provided key and direction Direction contains
	 * a String value defined in Model
	 * 
	 * @throws AdapterException
	 */
	public SortAdapter(IModelAdapter adapter, Expression expr, String direction) throws AdapterException {
		this.setExpression(adapter.adaptExpression(expr));
		this.setSortDirection(sortDirectionFromModel(direction));
	}

	/**
	 * Creates a new sort based on model sort key definition
	 * 
	 * @throws AdapterException
	 */
	public SortAdapter(IModelAdapter adapter, SortKeyHandle keyHandle) throws AdapterException {
		ExpressionHandle handle = keyHandle.getExpressionProperty(SortKey.KEY_MEMBER);
		if (handle == null)
			return;
		IScriptExpression expr = adapter.adaptExpression((Expression) handle.getValue());
		this.setExpression(expr);
		this.setSortDirection(sortDirectionFromModel(keyHandle.getDirection()));
		this.setSortStrength(keyHandle.getStrength());
		if (keyHandle.getLocale() != null)
			this.setSortLocale(keyHandle.getLocale());
	}

	/**
	 * Converts a model sort direction string to equivalent enumeration constant
	 */
	public static int sortDirectionFromModel(String modelDirectionStr) {
		if ("asc".equals(modelDirectionStr)) //$NON-NLS-1$
			return IGroupDefinition.SORT_ASC;
		if ("desc".equals(modelDirectionStr)) //$NON-NLS-1$
			return IGroupDefinition.SORT_DESC;

		return IGroupDefinition.SORT_ASC;
	}
}
