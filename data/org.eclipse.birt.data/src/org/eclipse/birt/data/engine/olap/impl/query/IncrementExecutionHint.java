
/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.impl.query;

import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.ISortDefinition;

/**
 * Hint for cube query execution which is based on the execution result of
 * another cube query definition, i.e. an array of {@code IAggregationResultSet}
 */

public class IncrementExecutionHint {
	// increment sorts
	private ISortDefinition[] sorts = new ISortDefinition[0];

	// increment bindings;
	private IBinding[] bindings = new IBinding[0];

	// increment filters;
	private IFilterDefinition[] filters = new IFilterDefinition[0];

	public ISortDefinition[] getSorts() {
		return sorts;
	}

	public void setSorts(ISortDefinition[] sorts) {
		if (sorts == null) {
			this.sorts = new ISortDefinition[0];
			return;
		}
		this.sorts = sorts;
	}

	public IBinding[] getBindings() {
		return bindings;
	}

	public void setBindings(IBinding[] bindings) {
		if (bindings == null) {
			this.bindings = new IBinding[0];
			return;
		}
		this.bindings = bindings;
	}

	public IFilterDefinition[] getFilters() {
		return filters;
	}

	public void setFilters(IFilterDefinition[] filters) {
		if (filters == null) {
			this.filters = new IFilterDefinition[0];
			return;
		}
		this.filters = filters;
	}

	public boolean isNoIncrement() {
		return sorts.length == 0 && bindings.length == 0 && filters.length == 0;
	}
}
