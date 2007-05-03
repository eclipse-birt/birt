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

import org.eclipse.birt.data.engine.api.querydefn.ComputedColumn;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;

/**
 * Adapts a Model computed column
 */
public class ComputedColumnAdapter extends ComputedColumn
{
	public ComputedColumnAdapter ( ComputedColumnHandle modelCmptdColumn )
	{
		super( modelCmptdColumn.getName( ),
				modelCmptdColumn.getExpression( ),
				org.eclipse.birt.report.data.adapter.api.DataAdapterUtil.adaptModelDataType( modelCmptdColumn.getDataType( ) ));
	}

}
