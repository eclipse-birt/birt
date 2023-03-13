/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.data.engine.impl;

import java.util.List;

import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.core.DataException;

public class FilterPrepareUtil {
	public static void prepareFilters(List<IFilterDefinition> filters, ScriptContext context) throws DataException {

	}

	public static void prepareFilters(IBaseQueryDefinition queryDefn, ScriptContext context) throws DataException {

	}

	public static boolean containsExternalFilter(List filterList, String dataSetExtId, String dataSourceExtId) {
		return false;
	}

}
