
/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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
package org.eclipse.birt.data.engine.impl;

import java.util.Map;

import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;

/**
 *
 */

public class QueryContextVisitorUtil {
	public static IQueryContextVisitor createQueryContextVisitor(IQueryDefinition query, Map appContext) {
		return null;
	}

	public static Object getVisitorQuery(IQueryContextVisitor visitor) {
		return null;
	}

	public static void populateEffectiveQueryText(IQueryContextVisitor visitor, String effectiveQueryText)
			throws DataException {

	}

	public static void populateDataSet(IQueryContextVisitor visitor, IBaseDataSetDesign dataSet, Map appContext)
			throws DataException {

	}

	public static void populateOriginalQueryText(IQueryContextVisitor visitor, String effectiveQueryText)
			throws DataException {

	}
}
