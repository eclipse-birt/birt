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
import org.eclipse.birt.report.model.api.simpleapi.ISortElement;

/**
 * ILevel
 */
public interface ILevel {

	String getName();

	String getDimensionName();

	List<IFilterConditionElement> getFilterConditions();

	void removeAllFilterConditions() throws SemanticException;

	void addFilterCondition(IFilterConditionElement filter) throws SemanticException;

	void removeFilterCondition(IFilterConditionElement filter) throws SemanticException;

	List<ISortElement> getSortConditions();

	void removeAllSortConditions() throws SemanticException;

	void addSortCondition(ISortElement sort) throws SemanticException;

	void removeSortCondition(ISortElement sort) throws SemanticException;

	String getPageBreakBefore();

	String getPageBreakAfter();

	String getPageBreakInside();

	int getPageBreakInterval();

	void setPageBreakBefore(String value) throws SemanticException;

	void setPageBreakAfter(String value) throws SemanticException;

	void setPageBreakInside(String value) throws SemanticException;

	void setPageBreakInterval(int value) throws SemanticException;
}
