/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.data.ui.dataset;

import java.util.Iterator;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.report.data.adapter.api.DataAdapterUtil;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.engine.api.impl.DatasetPreviewTask;
import org.eclipse.birt.report.engine.api.impl.ReportEngine;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSetParameterHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;

public class OutParameterPreviewTask extends DatasetPreviewTask
{
	public static final String PREFIX_OUTPUTPARAMETER = "outputParams"; //$NON-NLS-1$
	
	protected OutParameterPreviewTask( ReportEngine engine )
	{
		super( engine );
	}

	protected QueryDefinition constructQuery( DataSetHandle dataset, DataRequestSession session )
			throws BirtException
	{
		QueryDefinition query = super.constructQuery( dataset, session );

		PropertyHandle propertyHandle = dataset.getPropertyHandle( DataSetHandle.PARAMETERS_PROP );
		int paramsSize = propertyHandle.getListValue( ).size( );
		Iterator paramIter = propertyHandle.iterator( );
		for ( int n = 1; n <= paramsSize; n++ )
		{
			DataSetParameterHandle paramDefn = (DataSetParameterHandle) paramIter.next( );
			// get output parameters alone
			if ( !paramDefn.isOutput( ) )
				continue;

			String bindingName = paramDefn.getName( );
			IBinding binding = new Binding( bindingName );
			binding.setExpression( new ScriptExpression( PREFIX_OUTPUTPARAMETER
					+ "[\"" + paramDefn.getName( ) + "\"]" ) ); //$NON-NLS-1$ //$NON-NLS-2$
			binding.setDataType( DataAdapterUtil.adaptModelDataType( paramDefn.getDataType( ) ) );
			query.addBinding( binding );
		}
		return query;
	}

}
