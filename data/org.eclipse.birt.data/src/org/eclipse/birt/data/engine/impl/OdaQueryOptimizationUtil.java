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

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IOdaDataSetDesign;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.datatools.connectivity.oda.spec.QuerySpecification;
import org.eclipse.datatools.connectivity.oda.spec.ValidationContext;

/**
 * 
 */

public class OdaQueryOptimizationUtil {

	public static QuerySpecification optimizeExecution(String dataSourceId, ValidationContext validationContext,
			IOdaDataSetDesign dataSetDesign, IQueryDefinition query, DataEngineSession session, Map appContext,
			IQueryContextVisitor contextVisitor) throws DataException {
		return null;
	}

	public static Set<String> populateDirectDataSetColumnReferenceBindings(List<IBinding> candidateBinding,
			List<String> dataSetColumnName) {
		return null;
	}

}
