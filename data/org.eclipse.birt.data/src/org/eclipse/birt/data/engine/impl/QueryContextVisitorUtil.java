
/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
