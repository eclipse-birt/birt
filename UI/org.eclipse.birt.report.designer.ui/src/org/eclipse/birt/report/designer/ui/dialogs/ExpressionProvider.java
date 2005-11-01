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

package org.eclipse.birt.report.designer.ui.dialogs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.views.data.DataSetItemModel;
import org.eclipse.birt.report.designer.internal.ui.dialogs.ExpressionFilter;
import org.eclipse.birt.report.designer.internal.ui.util.DataSetManager;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.metadata.IClassInfo;
import org.eclipse.birt.report.model.api.metadata.ILocalizableInfo;
import org.eclipse.birt.report.model.api.metadata.IMemberInfo;
import org.eclipse.birt.report.model.api.metadata.IMethodInfo;
import org.eclipse.jface.util.Assert;
import org.eclipse.swt.graphics.Image;

/**
 * The default implementation of IExpressionProvider
 */

public class ExpressionProvider implements IExpressionProvider
{

	private static class Operator
	{

		/**
		 * The tooltip of the operator
		 */
		public String tooltip;
		/**
		 * The symbol of the operator
		 */
		public String symbol;

		/**
		 * The text to insert into the source viewer
		 */
		public String insertText;

		Operator( String symbol, String tooltipKey )
		{
			this.symbol = symbol;
			insertText = symbol;
			this.tooltip = Messages.getString( tooltipKey );
		}

		Operator( String symbol, String tooltipKey, String insertText )
		{
			this( symbol, tooltipKey );
			this.insertText = insertText;
		}
	}

	private static final String[] OPERATORS_ON_BAR = new String[]{
			"+", //$NON-NLS-1$
			"-", //$NON-NLS-1$
			"*", //$NON-NLS-1$
			"/", //$NON-NLS-1$
			OPERATOR_SEPARATOR, "!", //$NON-NLS-1$
			"=", //$NON-NLS-1$
			"<", //$NON-NLS-1$
			">", //$NON-NLS-1$
			OPERATOR_SEPARATOR, "&", //$NON-NLS-1$
			"|", //$NON-NLS-1$
			"(", //$NON-NLS-1$
			")" //$NON-NLS-1$
	};

	/** Arithmetic operators and their descriptions */
	private static final Operator[] OPERATORS_ASSIGNMENT = new Operator[]{
			new Operator( "=", "ExpressionProvider.Operator.Assign" ), //$NON-NLS-1$ //$NON-NLS-2$
			new Operator( "+=", "ExpressionProvider.Operator.AddTo" ), //$NON-NLS-1$ //$NON-NLS-2$
			new Operator( "-=", "ExpressionProvider.Operator.SubFrom" ), //$NON-NLS-1$ //$NON-NLS-2$
			new Operator( "*=", "ExpressionProvider.Operator.MultTo" ), //$NON-NLS-1$ //$NON-NLS-2$
			new Operator( "/=", "ExpressionProvider.Operator.DividingFrom" ), //$NON-NLS-1$ //$NON-NLS-2$
	};

	/** Comparison operators and their descriptions */
	private static Operator[] OPERATORS_COMPARISON = new Operator[]{
			new Operator( "==", "ExpressionProvider.Operator.Equals" ), //$NON-NLS-1$ //$NON-NLS-2$
			new Operator( "<", "ExpressionProvider.Operator.Less" ), //$NON-NLS-1$ //$NON-NLS-2$
			new Operator( "<=", "ExpressionProvider.Operator.LessEqual" ), //$NON-NLS-1$ //$NON-NLS-2$ 
			new Operator( "!=", "ExpressionProvider.Operator.NotEqual" ), //$NON-NLS-1$ //$NON-NLS-2$ 
			new Operator( ">", "ExpressionProvider.Operator.Greater" ), //$NON-NLS-1$ //$NON-NLS-2$
			new Operator( ">=", "ExpressionProvider.Operator.GreaterEquals" ), //$NON-NLS-1$ //$NON-NLS-2$

	};

	/** Computational operators and their descriptions */
	private static final Operator[] OPERATORS_COMPUTATIONAL = new Operator[]{
			new Operator( "+", "ExpressionProvider.Operator.Add" ), //$NON-NLS-1$ //$NON-NLS-2$
			new Operator( "-", "ExpressionProvider.Operator.Sub" ), //$NON-NLS-1$ //$NON-NLS-2$
			new Operator( "*", "ExpressionProvider.Operator.Mult" ), //$NON-NLS-1$ //$NON-NLS-2$
			new Operator( "/", "ExpressionProvider.Operator.Dvides" ), //$NON-NLS-1$ //$NON-NLS-2$ 
			new Operator( "++X ", "ExpressionProvider.Operator.Inc", "++@" ), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 
			new Operator( "X++ ", "ExpressionProvider.Operator.ReturnInc", "@++" ), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			new Operator( "--X ", "ExpressionProvider.Operator.Dec", "--@" ), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			new Operator( "X-- ", "ExpressionProvider.Operator.ReturnDec", "@--" ), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	};

	/** Logical operators and their descriptions */
	private static final Operator[] OPERATORS_LOGICAL = new Operator[]{
			new Operator( "&&", "ExpressionProvider.Operator.And" ), //$NON-NLS-1$ //$NON-NLS-2$ 
			new Operator( "||", "ExpressionProvider.Operator.Or" ) //$NON-NLS-1$ //$NON-NLS-2$ 

	};

	private static final String DISPLAY_TEXT_ASSIGNMENT = Messages.getString( "ExpressionProvider.Operators.Assignment" ); //$NON-NLS-1$	
	private static final String DISPLAY_TEXT_COMPARISON = Messages.getString( "ExpressionProvider.Operators.Comparison" ); //$NON-NLS-1$
	private static final String DISPLAY_TEXT_COMPUTATIONAL = Messages.getString( "ExpressionProvider.Operators.Computational" ); //$NON-NLS-1$
	private static final String DISPLAY_TEXT_LOGICAL = Messages.getString( "ExpressionProvider.Operators.Logical" ); //$NON-NLS-1$

	private static final Image IMAGE_OPERATOR = getIconImage( IReportGraphicConstants.ICON_EXPRESSION_OPERATOR );
	private static final Image IMAGE_COLUMN = getIconImage( IReportGraphicConstants.ICON_DATA_COLUMN );
	private static final Image IMAGE_METHOD = getIconImage( IReportGraphicConstants.ICON_EXPRESSION_METHOD );
	private static final Image IMAGE_STATIC_METHOD = getIconImage( IReportGraphicConstants.ICON_EXPRESSION_STATIC_METHOD );
	private static final Image IMAGE_MEMBER = getIconImage( IReportGraphicConstants.ICON_EXPRESSION_MEMBER );
	private static final Image IMAGE_STATIC_MEMBER = getIconImage( IReportGraphicConstants.ICON_EXPRESSION_STATIC_MEMBER );

	public static final String OPERATORS = Messages.getString( "ExpressionProvider.Category.Operators" ); //$NON-NLS-1$ 
	public static final String DATASETS = Messages.getString( "ExpressionProvider.Category.DataSets" ); //$NON-NLS-1$
	public static final String PARAMETERS = Messages.getString( "ExpressionProvider.Category.Parameters" ); //$NON-NLS-1$
	public static final String NATIVE_OBJECTS = Messages.getString( "ExpressionProvider.Category.NativeObjects" );//$NON-NLS-1$
	public static final String BIRT_OBJECTS = Messages.getString( "ExpressionProvider.Category.BirtObjects" );//$NON-NLS-1$

	private static final String ALL = Messages.getString( "ExpressionProvider.Label.All" ); //$NON-NLS-1$

	private List dataSetList;
	private ModuleHandle moduleHandle;

	private List filterList;

	/**
	 * Create a new expression provider with the given module
	 * 
	 * @param moduleHandle
	 *            the handle of the module
	 */
	public ExpressionProvider( ModuleHandle moduleHandle )
	{
		Assert.isNotNull( moduleHandle );
		this.moduleHandle = moduleHandle;
	}

	/**
	 * Create a new expression provider with the given module and dataset list
	 * 
	 * @param moduleHandle
	 *            the handle of the module
	 * @param dataSetList
	 *            the list of the data set
	 */
	public ExpressionProvider( ModuleHandle moduleHandle, List dataSetList )
	{
		this( moduleHandle );
		this.dataSetList = dataSetList;
	}

	/**
	 * Create a new expression provider with the current module
	 * 
	 * @param moduleHandle
	 *            the handle of the module
	 * @param dataSetList
	 *            the list of the data set
	 */
	public ExpressionProvider( )
	{
		this( SessionHandleAdapter.getInstance( ).getReportDesignHandle( ) );
	}

	/**
	 * Create a new expression provider with the current module and the given
	 * dataset list
	 * 
	 * @param moduleHandle
	 *            the handle of the module
	 * @param dataSetList
	 *            the list of the data set
	 */
	public ExpressionProvider( List dataSetList )
	{
		this( SessionHandleAdapter.getInstance( ).getReportDesignHandle( ),
				dataSetList );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider#getOperators()
	 */
	public String[] getOperators( )
	{
		return OPERATORS_ON_BAR;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider#getCategory()
	 */
	public Object[] getCategory( )
	{
		ArrayList categoryList = new ArrayList( 5 );
		if ( dataSetList != null && !dataSetList.isEmpty( ) )
		{
			categoryList.add( DATASETS );
		}
		if ( !moduleHandle.getAllParameters( ).isEmpty( ) )
		{
			categoryList.add( PARAMETERS );
		}
		categoryList.add( NATIVE_OBJECTS );
		categoryList.add( BIRT_OBJECTS );
		categoryList.add( OPERATORS );
		Object[] category = categoryList.toArray( );
		if ( filterList != null && !filterList.isEmpty( ) )
		{
			for ( Iterator iter = filterList.iterator( ); iter.hasNext( ); )
			{
				Object obj = iter.next( );
				if ( obj instanceof ExpressionFilter )
				{
					category = ( (ExpressionFilter) obj ).filter( null,
							category );
				}
			}
		}
		return category;
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
			if ( PARAMETERS.equals( parent ) )
			{
				childrenList.add( ALL );
				for ( Iterator iter = moduleHandle.getAllParameters( )
						.iterator( ); iter.hasNext( ); )
				{
					Object obj = iter.next( );
					if ( obj instanceof ParameterGroupHandle )
					{
						childrenList.add( obj );
					}
				}
			}
			else if ( ALL.equals( parent ) )
			{
				for ( Iterator iter = moduleHandle.getAllParameters( )
						.iterator( ); iter.hasNext( ); )
				{
					Object obj = iter.next( );
					if ( obj instanceof ParameterHandle )
					{
						childrenList.add( obj );
					}
				}
			}
			else
			{
				if ( DATASETS.equals( parent ) )
				{
					childrenList.addAll( dataSetList );
					childrenList.add( 0, childrenList.toArray( ) );
				}
				else if ( BIRT_OBJECTS.equals( parent ) )
				{
					childrenList.addAll( getClassList( false ) );
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
		else if ( parent instanceof DataSetHandle )
		{
			DataSetItemModel[] models = DataSetManager.getCurrentInstance( )
					.getColumns( ( (DataSetHandle) parent ), false );
			childrenList.addAll( Arrays.asList( models ) );
		}
		else if ( parent instanceof ParameterGroupHandle )
		{
			childrenList.addAll( ( (ParameterGroupHandle) parent ).getParameters( )
					.getContents( ) );
		}
		Object[] children = childrenList.toArray( );
		if ( filterList != null && !filterList.isEmpty( ) )
		{
			for ( Iterator iter = filterList.iterator( ); iter.hasNext( ); )
			{
				Object obj = iter.next( );
				if ( obj instanceof ExpressionFilter )
				{
					children = ( (ExpressionFilter) obj ).filter( parent,
							children );
				}
			}
		}
		return children;
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
					// boolean isFirst = true;
					// IArgumentInfoList arguments = (IArgumentInfoList)
					// method.argumentListIterator( )
					// .next( );
					// for ( Iterator iter = arguments.argumentsIterator( );
					// iter.hasNext( ); )
					// {
					// IArgumentInfo argInfo = (IArgumentInfo) iter.next( );
					// if ( !isFirst )
					// {
					// displayText.append( ", " );
					// }
					// isFirst = false;
					// if ( IArgumentInfo.OPTIONAL_ARGUMENT_NAME.equals(
					// argInfo.getName( ) ) )
					// {
					// displayText.append( argInfo.getDisplayName( ) );
					// }
					// else
					// {
					// displayText.append( argInfo.getType( )
					// + " "
					// + argInfo.getDisplayName( ) );
					// }
					// }
					displayText.append( ") " ); //$NON-NLS-1$
					displayText.append( method.getReturnType( ) );
				}
				return displayText.toString( );
			}
			return ALL;
		}
		else if ( element instanceof String )
		{
			return (String) element;
		}
		else if ( element instanceof Operator )
		{
			return ( (Operator) element ).symbol;
		}
		else if ( element instanceof DesignElementHandle )
		{
			return ( (DesignElementHandle) element ).getName( );
		}
		else if ( element instanceof DataSetItemModel )
		{
			return ( (DataSetItemModel) element ).getName( );
		}
		return element.toString( );
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
		else if ( element instanceof DataSetItemModel )
		{
			return IMAGE_COLUMN;
		}
		else if ( element instanceof DesignElementHandle )
		{
			return ReportPlatformUIImages.getImage( element );
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
			return ( (Operator) element ).insertText;
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
		else if ( element instanceof DataSetItemModel
				|| element instanceof ParameterHandle )
		{
			return DEUtil.getExpression( element );
		}
		return null;
	}

	private static Image getIconImage( String id )
	{
		return ReportPlatformUIImages.getImage( id );
	}

	private List getClassList( boolean isNative )
	{
		List list = DesignEngine.getMetaDataDictionary( ).getClasses( );
		ArrayList resultList = new ArrayList( );
		for ( Iterator iter = list.iterator( ); iter.hasNext( ); )
		{
			IClassInfo classInfo = (IClassInfo) iter.next( );
			if ( classInfo.isNative( ) == isNative )
			{
				resultList.add( classInfo );
			}
		}
		return resultList;
	}

	/**
	 * Adds a filter for this expression builder.
	 * 
	 * @param filter
	 *            the filter to add
	 */
	public void addFilter( ExpressionFilter filter )
	{
		if ( filterList == null )
		{
			filterList = new ArrayList( );
		}

		if ( !filterList.contains( filter ) )
		{
			filterList.add( filter );
		}
	}

	/**
	 * Adds a filter list for this expression builder.
	 * 
	 * @param list
	 *            the list of the filter to add
	 */
	public void addFilterList( List list )
	{
		// allows for null.
		if ( list == null )
		{
			return;
		}
		for ( Iterator iter = list.iterator( ); iter.hasNext( ); )
		{
			Object obj = iter.next( );
			if ( obj instanceof ExpressionFilter )
			{
				addFilter( (ExpressionFilter) obj );
			}
		}
	}

	/**
	 * Removes a filter from the filter list.
	 * 
	 * @param filter
	 *            the filter to remove
	 */
	public void removeFilter( ExpressionFilter filter )
	{
		if ( filterList == null )
		{
			return;
		}
		filterList.remove( filter );
	}

	/**
	 * Clears the filter list.
	 */
	public void clearFilters( )
	{
		if ( filterList == null )
		{
			return;
		}
		filterList.clear( );
	}

}