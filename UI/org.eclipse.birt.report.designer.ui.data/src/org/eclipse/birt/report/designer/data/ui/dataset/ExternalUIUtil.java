/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/
package org.eclipse.birt.report.designer.data.ui.dataset;

import org.eclipse.birt.report.designer.data.ui.util.DataUIConstants;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPage;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.JointDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.ScriptDataSetHandle;
import org.eclipse.birt.report.model.api.ScriptDataSourceHandle;


public class ExternalUIUtil
{

	public static void validateDataSetHandle( DataSetHandle ds )
	{
		if ( !( ds instanceof JointDataSetHandle ) )
		{
			if ( ds.getDataSource( ) == null )
			{
				throw new RuntimeException( Messages.getFormattedString( "dataset.editor.error.noDataSource", new String[]{ds.getQualifiedName( )} ) );//$NON-NLS-1$
			}
			if ( ( ds instanceof OdaDataSetHandle && !( ds.getDataSource( ) instanceof OdaDataSourceHandle ) ) )
			{
				throw new RuntimeException( Messages.getFormattedString( "dataset.editor.error.nonmatchedDataSource", //$NON-NLS-1$
						new String[]{
								ds.getQualifiedName( ),
								( (OdaDataSetHandle) ds ).getExtensionID( )
						} ) );
			}
			else if ( ds instanceof ScriptDataSetHandle
					&& !( ds.getDataSource( ) instanceof ScriptDataSourceHandle ) )
			{
				throw new RuntimeException( Messages.getFormattedString( "dataset.editor.error.nonmatchedDataSource", //$NON-NLS-1$
						new String[]{
								ds.getQualifiedName( ),
								DataUIConstants.DATA_SET_SCRIPT
						} ) );
			}
		}
	}
	
	public static boolean containsDataSource( DataSetHandle ds  )
	{
		if ( ds instanceof JointDataSetHandle )
		{
			return false;
		}
		return true;
	}
	
	public static String getDataSourceType( DataSetHandle ds )
	{
		return null;
	}
	
	public static String getDataSetType( DataSetHandle ds )
	{
		return null;
	}
	
	public static IPropertyPage[] getCommonPages( DataSetHandle ds )
	{
		return new IPropertyPage[0];
	}

	public static DataSetHandle getQulifiedDataSetHandle( DataSetHandle ds )
	{
		return ds;
	}

	public static boolean needUtilityPages( DataSetHandle ds )
	{
		return true;
	}
}
