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

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.querydefn.ScriptDataSourceDesign;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.model.api.ScriptDataSourceHandle;

public class ScriptDataSourceAdapter extends ScriptDataSourceDesign
{
	/**
	 * Creates adaptor based on Model DataSourceHandle.
	 * @param source model handle
	 */
	public ScriptDataSourceAdapter( ScriptDataSourceHandle source, DataSessionContext context )
		throws BirtException
	{
		super( source.getQualifiedName( ) );

		// TODO: event handler!!!!
		
		// Adapt base class properties
		DataAdapterUtil.adaptBaseDataSource(source, this );
		// Adapt script data source elements
		setOpenScript( source.getOpen( ) );
		setCloseScript( source.getClose( ) );
	}
}
