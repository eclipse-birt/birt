/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.api.filterExtension;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.api.filterExtension.interfaces.IFilterExprDefinition;

/**
 * OdaFilterExprHelper
 */

public class OdaFilterExprHelper
{

	/**
	 * Returns the list of IFilterExprDefinition. If under commercial BIRT, the
	 * list contains both of ODA extension provider registered filter
	 * definitions, and BIRT predefined filter definitions. If under OS BIRT,
	 * the list will only contain the IFilterExprDefinition instance which
	 * represent the BIRT predefined ones.
	 * 
	 *@param odaDatasetExtensionId
	 *            oda datasource extension id.
	 *@param odaDataSourceExtensionId
	 *            oda dataset extension id.
	 * @return List of IFilterExprDefinition instance.
	 */
	public static List<IFilterExprDefinition> getMappedFilterExprDefinitions(
			String dataSetExtId, String dataSourceExtId )
	{

		Object delegateObject = ExternalDelegateUtil.getExternalFilterHelper( );
		if ( delegateObject != null )
		{
			Method method = ExternalDelegateUtil.getMethod(
					"getMappedFilterExprDefinitions",
					delegateObject.getClass( ), new Class[]{String.class,
							String.class} );
			if ( method != null )
			{
				return (List<IFilterExprDefinition>) ExternalDelegateUtil
						.invokeMethod( method, delegateObject, new Object[]{
								dataSetExtId, dataSourceExtId} );
			}
		}

		return Collections.emptyList( );
	}

	/**
	 * Return the IFilterExprDefinition instance based on the passed in BIRT
	 * predefined Filter expression name. For commercial BIRT, the returned
	 * IFilterExprDefinition will provide the information that mapped to a
	 * corresponding ODA extension Filter if there is one. For OS BIRT, the
	 * returned IFilterExprDefinition will not have any map information to the
	 * ODA extension filters.
	 * 
	 * @param birtFilterExprId
	 *            the BIRT predefined fitler expression id.
	 * @param datasetExtId
	 *            ODA dataset extension id. Null if is for OS BIRT.
	 * @param datasourceExtId
	 *            ODA datasource extension id. Null if is for OS BIRT.
	 * @return Instance of IFilterExprDefinition. IFilterExprDefinition instance
	 *         based on the passed in filter expression id.
	 */
	public static IFilterExprDefinition getFilterExpressionDefn(
			String birtFilterExprId, String datasetExtId, String datasourceExtId )
	{
		Object delegateObject = ExternalDelegateUtil.getExternalFilterHelper( );
		if ( delegateObject != null )
		{
			Method method = ExternalDelegateUtil.getMethod(
					"getFilterExpressionDefn", delegateObject.getClass( ),
					new Class[]{String.class, String.class, String.class} );
			if ( method != null )
			{
				return (IFilterExprDefinition) ExternalDelegateUtil
						.invokeMethod( method, delegateObject,
								new Object[]{birtFilterExprId, datasetExtId,
										datasourceExtId} );
			}
		}

		return null;
	}

	/**
	 * Indicates if support the ODA extension filter expressions.
	 * 
	 * @return true if support, false if not.
	 */
	public static boolean supportOdaExtensionFilters( )
	{
		Object delegateObject = ExternalDelegateUtil.getExternalFilterHelper( );
		if ( delegateObject != null )
		{
			Method method = ExternalDelegateUtil.getMethod(
					"supportOdaExtensionFilters", delegateObject.getClass( ),
					null );
			if ( method != null )
			{
				return ( (Boolean) ExternalDelegateUtil.invokeMethod( method,
						delegateObject, null ) ).booleanValue( );
			}
		}

		return false;
	}
}
