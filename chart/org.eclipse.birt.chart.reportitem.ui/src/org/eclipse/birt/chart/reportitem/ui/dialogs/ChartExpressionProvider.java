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

package org.eclipse.birt.chart.reportitem.ui.dialogs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.eclipse.birt.chart.script.ScriptHandler;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.designer.ui.dialogs.Operator;
import org.eclipse.birt.report.model.api.metadata.IClassInfo;
import org.eclipse.birt.report.model.api.metadata.ILocalizableInfo;
import org.eclipse.birt.report.model.api.metadata.IMemberInfo;
import org.eclipse.birt.report.model.api.metadata.IMethodInfo;
import org.eclipse.swt.graphics.Image;

/**
 * Provide a specific expression builder to explode pie slices.
 */

public class ChartExpressionProvider extends ExpressionProvider
{

	public static final String CHART_VARIABLES = org.eclipse.birt.chart.reportitem.ui.i18n.Messages.getString( "ChartExpressionProvider.Category.ChartVariables" );//$NON-NLS-1$

	private static final String DATA_POINTS = org.eclipse.birt.chart.reportitem.ui.i18n.Messages.getString( "ChartExpressionProvider.ChartVariables.DataPoints" );//$NON-NLS-1$

	public ChartExpressionProvider( )
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider#getCategory()
	 */
	public Object[] getCategory( )
	{
		ArrayList categoryList = new ArrayList( 3 );
		categoryList.add( NATIVE_OBJECTS );
		categoryList.add( CHART_VARIABLES );
		categoryList.add( OPERATORS );

		return categoryList.toArray( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider#getChildren(java.lang.Object)
	 */
	public Object[] getChildren( Object parent )
	{
		ArrayList childrenList = new ArrayList( );
		if ( parent instanceof Object[] )
		{
			Object[] array = (Object[]) parent;
			if ( array instanceof Operator[] )
			{
				return array;
			}
			for ( int i = 0; i < array.length; i++ )
			{
				Object[] children = getChildren( array[i] );
				childrenList.addAll( Arrays.asList( children ) );
			}
		}
		else if ( parent instanceof String )
		{
			if ( DATA_POINTS.equals( parent ) )
			{
				childrenList.add( ScriptHandler.BASE_VALUE );
				childrenList.add( ScriptHandler.ORTHOGONAL_VALUE );
				childrenList.add( ScriptHandler.SERIES_VALUE );
			}
			else
			{
				if ( CHART_VARIABLES.equals( parent ) )
				{
					childrenList.add( DATA_POINTS );
				}
				else if ( NATIVE_OBJECTS.equals( parent ) )
				{
					childrenList.addAll( getClassList( true ) );
				}
				else if ( OPERATORS.equals( parent ) )
				{
					childrenList.add( OPERATORS_ASSIGNMENT );
					childrenList.add( OPERATORS_COMPARISON );
					childrenList.add( OPERATORS_COMPUTATIONAL );
					childrenList.add( OPERATORS_LOGICAL );
					childrenList.add( 0, childrenList.toArray( ) );
				}
			}
		}
		else if ( parent instanceof IClassInfo )
		{
			IClassInfo classInfo = (IClassInfo) parent;
			for ( Iterator iter = classInfo.getMembers( ).iterator( ); iter.hasNext( ); )
			{
				childrenList.add( new ILocalizableInfo[]{
						classInfo, (IMemberInfo) iter.next( )
				} );
			}
			for ( Iterator iter = classInfo.getMethods( ).iterator( ); iter.hasNext( ); )
			{
				childrenList.add( new ILocalizableInfo[]{
						classInfo, (IMethodInfo) iter.next( )
				} );
			}
		}
		return childrenList.toArray( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider#getDisplayText(java.lang.Object)
	 */
	public String getDisplayText( Object element )
	{
		if ( element instanceof Object[] )
		{
			if ( element instanceof Operator[] )
			{
				if ( element == OPERATORS_ASSIGNMENT )
				{
					return DISPLAY_TEXT_ASSIGNMENT;
				}
				else if ( element == OPERATORS_COMPARISON )
				{
					return DISPLAY_TEXT_COMPARISON;
				}
				else if ( element == OPERATORS_COMPUTATIONAL )
				{
					return DISPLAY_TEXT_COMPUTATIONAL;
				}
				else if ( element == OPERATORS_LOGICAL )
				{
					return DISPLAY_TEXT_LOGICAL;
				}
			}
			else if ( element instanceof ILocalizableInfo[] )
			{
				// including class info,method info and member info
				ILocalizableInfo info = ( (ILocalizableInfo[]) element )[1];
				StringBuffer displayText = new StringBuffer( info.getName( ) );
				if ( info instanceof IMethodInfo )
				{
					IMethodInfo method = (IMethodInfo) info;
					displayText.append( "(" ); //$NON-NLS-1$
					displayText.append( ") " ); //$NON-NLS-1$
					displayText.append( method.getReturnType( ) );
				}
				return displayText.toString( );
			}
			return ALL;
		}
		else if ( element instanceof String )
		{
			if ( element.equals( ScriptHandler.BASE_VALUE ) )
			{
				return org.eclipse.birt.chart.reportitem.ui.i18n.Messages.getString( "ChartExpressionProvider.DataPoints.BaseValue" );//$NON-NLS-1$;
			}
			else if ( element.equals( ScriptHandler.ORTHOGONAL_VALUE ) )
			{
				return org.eclipse.birt.chart.reportitem.ui.i18n.Messages.getString( "ChartExpressionProvider.DataPoints.OrthogonalValue" );//$NON-NLS-1$
			}
			else if ( element.equals( ScriptHandler.SERIES_VALUE ) )
			{
				return org.eclipse.birt.chart.reportitem.ui.i18n.Messages.getString( "ChartExpressionProvider.DataPoints.SeriesValue" );//$NON-NLS-1$
			}
			return (String) element;
		}
		else if ( element instanceof Operator )
		{
			return ( (Operator) element ).symbol;
		}
		return element.toString( );
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider#getImage(java.lang.Object)
	 */
	public Image getImage( Object element )
	{
		if ( element instanceof Operator )
		{
			return IMAGE_OPERATOR;
		}
		else if ( element instanceof ILocalizableInfo[] )
		{
			ILocalizableInfo info = ( (ILocalizableInfo[]) element )[1];
			if ( info instanceof IMethodInfo )
			{
				if ( ( (IMethodInfo) info ).isStatic( ) )
				{
					return IMAGE_STATIC_METHOD;
				}
				return IMAGE_METHOD;
			}
			if ( info instanceof IMemberInfo )
			{
				if ( ( (IMemberInfo) info ).isStatic( ) )
				{
					return IMAGE_STATIC_MEMBER;
				}
				return IMAGE_MEMBER;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider#getInsertText(java.lang.Object)
	 */
	public String getInsertText( Object element )
	{
		if ( element instanceof Operator )
		{
			return ( (Operator) element ).insertString;
		}
		else if ( element instanceof ILocalizableInfo[] )
		{
			IClassInfo classInfo = (IClassInfo) ( (ILocalizableInfo[]) element )[0];
			ILocalizableInfo info = ( (ILocalizableInfo[]) element )[1];
			StringBuffer insertText = new StringBuffer( );
			if ( info instanceof IMemberInfo )
			{
				IMemberInfo memberInfo = (IMemberInfo) info;
				if ( memberInfo.isStatic( ) )
				{
					insertText.append( classInfo.getName( ) + "." ); //$NON-NLS-1$
				}
				insertText.append( memberInfo.getName( ) );
			}
			else if ( info instanceof IMethodInfo )
			{
				IMethodInfo methodInfo = (IMethodInfo) info;
				if ( methodInfo.isStatic( ) )
				{
					insertText.append( classInfo.getName( ) + "." ); //$NON-NLS-1$
				}
				insertText.append( methodInfo.getName( ) );
				insertText.append( "()" ); //$NON-NLS-1$
			}
			return insertText.toString( );
		}
		else if ( element instanceof String )
		{
			return (String) element;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider#getTooltipText(java.lang.Object)
	 */
	public String getTooltipText( Object element )
	{
		if ( element instanceof Operator )
		{
			return ( (Operator) element ).tooltip;
		}
		else if ( element instanceof ILocalizableInfo[] )
		{
			return ( (ILocalizableInfo[]) element )[1].getToolTip( );
		}
		return getDisplayText( element );
	}
}
