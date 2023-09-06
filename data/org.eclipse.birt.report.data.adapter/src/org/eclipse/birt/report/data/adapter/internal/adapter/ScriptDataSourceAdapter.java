/*
 *************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
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
 *
 *************************************************************************
 */
package org.eclipse.birt.report.data.adapter.internal.adapter;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.querydefn.ScriptDataSourceDesign;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.model.api.ScriptDataSourceHandle;

/**
 * Adapter class of scripted data source
 *
 * @since 3.3
 *
 */
public class ScriptDataSourceAdapter extends ScriptDataSourceDesign {

	/**
	 * Creates adaptor based on Model DataSourceHandle.
	 *
	 * @param source  model handle
	 * @param context data session context
	 * @throws BirtException
	 */
	public ScriptDataSourceAdapter(ScriptDataSourceHandle source, DataSessionContext context) throws BirtException {
		super(source.getQualifiedName());

		// TODO: event handler!!!!

		// Adapt base class properties
		DataAdapterUtil.adaptBaseDataSource(source, this);

		// Adapt script data source elements
		setOpenScript(source.getOpen());
		setCloseScript(source.getClose());
	}

}
