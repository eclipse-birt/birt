/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/
package org.eclipse.birt.report.designer.data.ui.dataset;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.designer.data.ui.util.DataUIConstants;
import org.eclipse.birt.report.designer.data.ui.util.DataUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPage;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.JointDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSetParameterHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.ScriptDataSetHandle;
import org.eclipse.birt.report.model.api.ScriptDataSourceHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;


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

	public static void populateApplicationContext( DataSetHandle handle,
			DataRequestSession session ) throws BirtException
	{
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

	public static boolean needUtilityPages( DataSetHandle ds )
	{
		return true;
	}

	public static Expression getParamValueExpression( DataSetHandle dataSet,
			OdaDataSetParameterHandle paramDefn ) throws BirtException
	{
		String linkedReportParam = ( (OdaDataSetParameterHandle) paramDefn ).getParamName( );
		if ( linkedReportParam != null )
		{
			ParameterHandle ph = dataSet.getModuleHandle( )
					.findParameter( linkedReportParam );
			if ( ph instanceof ScalarParameterHandle )
			{
				if ( ( (ScalarParameterHandle) ph ).getParamType( )
						.equals( DesignChoiceConstants.SCALAR_PARAM_TYPE_MULTI_VALUE ) )
				{
					throw new BirtException( Messages.getFormattedString( "dataset.editor.error.invalidLinkedParameter",
							new String[]{
								linkedReportParam
							} ),
							null );
				}
			}
		}
		return new Expression( DataUtil.getParamValue( dataSet, paramDefn ),
				ExpressionType.JAVASCRIPT );
	}
}
