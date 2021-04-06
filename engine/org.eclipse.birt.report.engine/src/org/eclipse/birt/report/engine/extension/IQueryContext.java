/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.extension;

import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.model.api.ReportElementHandle;

public interface IQueryContext {

	/**
	 * delegate to report engine to build query, the extened item may call this api
	 * to build children's query
	 */
	IDataQueryDefinition[] createQuery(IDataQueryDefinition parent, ReportElementHandle handle);

	/**
	 * return a data session
	 */
	DataRequestSession getDataRequestSession();
}
