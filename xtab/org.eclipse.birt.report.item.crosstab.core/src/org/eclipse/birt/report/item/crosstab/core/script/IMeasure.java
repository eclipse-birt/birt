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

import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.simpleapi.IFilterConditionElement;

/**
 * IMeasure
 */
public interface IMeasure {

	String getName();

	String getFunctionName();

	String getMeasureExpression();

	List<IFilterConditionElement> getFilterConditions();

	void removeAllFilterConditions() throws SemanticException;

	void addFilterCondition(IFilterConditionElement filter) throws SemanticException;

	void removeFilterCondition(IFilterConditionElement filter) throws SemanticException;
}
