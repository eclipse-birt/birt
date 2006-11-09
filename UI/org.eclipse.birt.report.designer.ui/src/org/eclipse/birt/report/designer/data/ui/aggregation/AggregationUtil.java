/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.data.ui.aggregation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.designer.util.AlphabeticallyComparator;
import org.eclipse.birt.report.model.api.metadata.IClassInfo;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

/**
 * A utility class involving user-defined aggregation
 */
public class AggregationUtil
{

	private static final String AGGREGATION_CATEGORY = "total";//$NON-NLS-1$
	private static final String AGGREGATION_EXT_POINT = "org.eclipse.birt.data.aggregation";//$NON-NLS-1$
	private static final String BLANK = " ";//$NON-NLS-1$
	private static final String DEFUALT_RETURN_TYPE = "number";//$NON-NLS-1$
	private static final String AGGREGATION_ATTR_NAME = "name";//$NON-NLS-1$
	private static final String UIINFO_ATTR_TIP = "tip";//$NON-NLS-1$
	private static final String UIINFO_ATTR_TEXTDATA = "textData";//$NON-NLS-1$
	private static final String UIINFO_ATTR_PARAMTERMETAINFO = "parameterMetaInfo";//$NON-NLS-1$
	private static final String REGULAR_EXPR_DELIMITER_COMMA = "[,]";//$NON-NLS-1$

	/**
	 * Provide all user-defined aggregation methods
	 * 
	 * @param classInfo
	 * @return
	 */
	public static List getMethods( IClassInfo classInfo )
	{
		if ( !classInfo.getName( ).equalsIgnoreCase( ( AGGREGATION_CATEGORY ) ) )
			return Collections.EMPTY_LIST;

		List methodList = new ArrayList( );
		IConfigurationElement[] aggregations = ( (IConfigurationElement[]) Platform.getExtensionRegistry( )
				.getConfigurationElementsFor( AGGREGATION_EXT_POINT ) )[0].getChildren( );

		for ( int i = 0; i < aggregations.length; i++ )
		{
			IConfigurationElement[] uiInfos = ( aggregations[i].getChildren( ) );
			for ( int j = 0; j < uiInfos.length; j++ )
			{
				MethodInfo methodInfo = new MethodInfo( false );
				methodInfo.setName( aggregations[i].getAttribute( AGGREGATION_ATTR_NAME ) );
				methodInfo.setDisplayNameKey( uiInfos[j].getAttribute( UIINFO_ATTR_TEXTDATA ) );
				methodInfo.setToolTipKey( uiInfos[j].getAttribute( UIINFO_ATTR_TIP ) );
				methodInfo.addArgumentList( loadArgumentList( uiInfos[j].getAttribute( UIINFO_ATTR_PARAMTERMETAINFO ) ) );
				methodInfo.setStatic( true );
				methodInfo.setReturnType( DEFUALT_RETURN_TYPE );

				methodList.add( methodInfo );
			}

			Collections.sort( methodList, new AlphabeticallyComparator( ) );
		}

		return methodList;
	}

	private static ArgumentInfoList loadArgumentList( String metaInfo )
	{
		ArgumentInfoList argList = new ArgumentInfoList( );

		if ( metaInfo == null )
			return argList;

		String[] args = metaInfo.split( REGULAR_EXPR_DELIMITER_COMMA );
		if ( !isValid( args ) )
			return argList;

		for ( int i = 0; i < args.length; i++ )
		{
			ArgumentInfo arg = new ArgumentInfo( );
			String type = args[i].substring( 0, args[i].indexOf( BLANK ) );
			String name = args[i].substring( args[i].indexOf( BLANK ) + 1 );

			arg.setDisplayNameKey( name );
			arg.setName( name );
			arg.setType( type );

			argList.addArgument( arg );
		}

		return argList;
	}

	private static boolean isValid( String[] args )
	{
		for ( int i = 0; i < args.length; i++ )
		{
			args[i] = args[i].trim( );
			if ( args[i].indexOf( BLANK ) == -1 )
				return false;
		}

		return true;
	}

}
