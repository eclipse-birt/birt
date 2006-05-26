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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.views.data.DataSetItemModel;
import org.eclipse.birt.report.designer.internal.ui.dialogs.ExpressionFilter;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.metadata.IArgumentInfo;
import org.eclipse.birt.report.model.api.metadata.IArgumentInfoList;
import org.eclipse.birt.report.model.api.metadata.IClassInfo;
import org.eclipse.birt.report.model.api.metadata.ILocalizableInfo;
import org.eclipse.birt.report.model.api.metadata.IMemberInfo;
import org.eclipse.birt.report.model.api.metadata.IMethodInfo;
import org.eclipse.swt.graphics.Image;

/**
 * The default implementation of IExpressionProvider
 */

public class ExpressionProvider implements IExpressionProvider
{

	/** Arithmetic operators and their descriptions */
	protected static final Operator[] OPERATORS_ASSIGNMENT = new Operator[]{
			new Operator( "=", Messages.getString( "ExpressionProvider.Operator.Assign" ) ), //$NON-NLS-1$ //$NON-NLS-2$
			new Operator( "+=", Messages.getString( "ExpressionProvider.Operator.AddTo" ) ), //$NON-NLS-1$ //$NON-NLS-2$
			new Operator( "-=", Messages.getString( "ExpressionProvider.Operator.SubFrom" ) ), //$NON-NLS-1$ //$NON-NLS-2$
			new Operator( "*=", Messages.getString( "ExpressionProvider.Operator.MultTo" ) ), //$NON-NLS-1$ //$NON-NLS-2$
			new Operator( "/=", Messages.getString( "ExpressionProvider.Operator.DividingFrom" ) ), //$NON-NLS-1$ //$NON-NLS-2$
	};

	/** Comparison operators and their descriptions */
	protected static Operator[] OPERATORS_COMPARISON = new Operator[]{
			new Operator( "==", Messages.getString( "ExpressionProvider.Operator.Equals" ) ), //$NON-NLS-1$ //$NON-NLS-2$
			new Operator( "<", Messages.getString( "ExpressionProvider.Operator.Less" ) ), //$NON-NLS-1$ //$NON-NLS-2$
			new Operator( "<=", Messages.getString( "ExpressionProvider.Operator.LessEqual" ) ), //$NON-NLS-1$ //$NON-NLS-2$ 
			new Operator( "!=", Messages.getString( "ExpressionProvider.Operator.NotEqual" ) ), //$NON-NLS-1$ //$NON-NLS-2$ 
			new Operator( ">", Messages.getString( "ExpressionProvider.Operator.Greater" ) ), //$NON-NLS-1$ //$NON-NLS-2$
			new Operator( ">=", Messages.getString( "ExpressionProvider.Operator.GreaterEquals" ) ), //$NON-NLS-1$ //$NON-NLS-2$

	};

	/** Computational operators and their descriptions */
	protected static final Operator[] OPERATORS_COMPUTATIONAL = new Operator[]{
			new Operator( "+", Messages.getString( "ExpressionProvider.Operator.Add" ) ), //$NON-NLS-1$ //$NON-NLS-2$
			new Operator( "-", Messages.getString( "ExpressionProvider.Operator.Sub" ) ), //$NON-NLS-1$ //$NON-NLS-2$
			new Operator( "*", Messages.getString( "ExpressionProvider.Operator.Mult" ) ), //$NON-NLS-1$ //$NON-NLS-2$
			new Operator( "/", Messages.getString( "ExpressionProvider.Operator.Divides" ) ), //$NON-NLS-1$ //$NON-NLS-2$ 
			new Operator( "++ ", Messages.getString( "ExpressionProvider.Operator.Inc" ) ), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 
			new Operator( "-- ", Messages.getString( "ExpressionProvider.Operator.Dec" ) ), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	};

	/** Logical operators and their descriptions */
	protected static final Operator[] OPERATORS_LOGICAL = new Operator[]{
			new Operator( "&&", Messages.getString( "ExpressionProvider.Operator.And" ) ), //$NON-NLS-1$ //$NON-NLS-2$ 
			new Operator( "||", Messages.getString( "ExpressionProvider.Operator.Or" ) ), //$NON-NLS-1$ //$NON-NLS-2$ 

	};

	/** Default operators on the operator bar */
	protected static final Operator[] OPERATORS_ON_BAR = new Operator[]{
			OPERATORS_COMPUTATIONAL[0],
			OPERATORS_COMPUTATIONAL[1],
			OPERATORS_COMPUTATIONAL[2],
			OPERATORS_COMPUTATIONAL[3],
			OPERATOR_SEPARATOR,
			new Operator( "!", Messages.getString( "ExpressionProvider.Operator.Not" ) ),//$NON-NLS-1$
			new Operator( "=", Messages.getString( "ExpressionProvider.Operator.Equals" ) ),//$NON-NLS-1$
			OPERATORS_COMPARISON[1],
			OPERATORS_COMPARISON[4],
			OPERATOR_SEPARATOR,
			new Operator( "&", Messages.getString( "ExpressionProvider.Operator.BitAnd" ) ),//$NON-NLS-1$
			new Operator( "|", Messages.getString( "ExpressionProvider.Operator.BitOr" ) ),//$NON-NLS-1$
			new Operator( "(", Messages.getString( "ExpressionProvider.Operator.LeftBracket" ) ),//$NON-NLS-1$
			new Operator( ")", Messages.getString( "ExpressionProvider.Operator.RightBracket" ) ),//$NON-NLS-1$
	};

	protected static final String DISPLAY_TEXT_ASSIGNMENT = Messages.getString( "ExpressionProvider.Operators.Assignment" ); //$NON-NLS-1$	
	protected static final String DISPLAY_TEXT_COMPARISON = Messages.getString( "ExpressionProvider.Operators.Comparison" ); //$NON-NLS-1$
	protected static final String DISPLAY_TEXT_COMPUTATIONAL = Messages.getString( "ExpressionProvider.Operators.Computational" ); //$NON-NLS-1$
	protected static final String DISPLAY_TEXT_LOGICAL = Messages.getString( "ExpressionProvider.Operators.Logical" ); //$NON-NLS-1$

	protected static final Image IMAGE_OPERATOR = getIconImage( IReportGraphicConstants.ICON_EXPRESSION_OPERATOR );
	protected static final Image IMAGE_COLUMN = getIconImage( IReportGraphicConstants.ICON_DATA_COLUMN );
	protected static final Image IMAGE_METHOD = getIconImage( IReportGraphicConstants.ICON_EXPRESSION_METHOD );
	protected static final Image IMAGE_STATIC_METHOD = getIconImage( IReportGraphicConstants.ICON_EXPRESSION_STATIC_METHOD );
	protected static final Image IMAGE_MEMBER = getIconImage( IReportGraphicConstants.ICON_EXPRESSION_MEMBER );
	protected static final Image IMAGE_STATIC_MEMBER = getIconImage( IReportGraphicConstants.ICON_EXPRESSION_STATIC_MEMBER );

	public static final String OPERATORS = Messages.getString( "ExpressionProvider.Category.Operators" ); //$NON-NLS-1$
	public static final String COLUMN_BINDINGS = Messages.getString( "ExpressionProvider.Category.ColumnBinding" ); //$NON-NLS-1$
	// public static final String DATASETS = Messages.getString(
	// "ExpressionProvider.Category.DataSets" ); //$NON-NLS-1$
	public static final String PARAMETERS = Messages.getString( "ExpressionProvider.Category.Parameters" ); //$NON-NLS-1$
	public static final String NATIVE_OBJECTS = Messages.getString( "ExpressionProvider.Category.NativeObjects" );//$NON-NLS-1$
	public static final String BIRT_OBJECTS = Messages.getString( "ExpressionProvider.Category.BirtObjects" );//$NON-NLS-1$

	protected static final String ALL = Messages.getString( "ExpressionProvider.Label.All" ); //$NON-NLS-1$

	private static final String TOOLTIP_BINDING_PREFIX = Messages.getString( "ExpressionProvider.Tooltip.ColumnBinding" ); //$NON-NLS-1$

	protected DesignElementHandle elementHandle;

	private List filterList;

	private boolean includeSelf;

	/**
	 * Create a new expression provider with the current module
	 */
	public ExpressionProvider( )
	{
		this( null );
	}

	/**
	 * Create a new expression provider with the given element
	 * 
	 * @param handle
	 *            the handle of the element
	 */
	public ExpressionProvider( DesignElementHandle handle )
	{
		this( handle, true );
	}

	/**
	 * Create a new expression provider with the given element
	 * 
	 * @param handle
	 *            the handle of the element
	 */
	public ExpressionProvider( DesignElementHandle handle, boolean includeSelf )
	{
		if ( handle == null )
		{
			elementHandle = SessionHandleAdapter.getInstance( )
					.getReportDesignHandle( );
		}
		else
		{
			elementHandle = handle;
		}
		this.includeSelf = includeSelf;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider#getOperators()
	 */
	public Operator[] getOperators( )
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
		Object[] category = getCategoryList( ).toArray( );
		if ( filterList != null && !filterList.isEmpty( ) )
		{
			for ( Iterator iter = filterList.iterator( ); iter.hasNext( ); )
			{
				Object obj = iter.next( );
				if ( obj instanceof ExpressionFilter )
				{
					category = ( (ExpressionFilter) obj ).filter( ExpressionFilter.CATEGORY,
							category );
				}
			}
		}
		return category;
	}

	protected List getCategoryList( )
	{
		ArrayList categoryList = new ArrayList( 5 );
		if ( !DEUtil.getAllColumnBindingList( elementHandle ).isEmpty( ) )
		{
			categoryList.add( COLUMN_BINDINGS );
		}
		if ( elementHandle.getModuleHandle( ).getParameters( ).getCount( ) != 0 )
		{
			categoryList.add( PARAMETERS );
		}
		categoryList.add( NATIVE_OBJECTS );
		categoryList.add( BIRT_OBJECTS );
		categoryList.add( OPERATORS );
		return categoryList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider#getChildren(java.lang.Object)
	 */
	public Object[] getChildren( Object parent )
	{
		Object[] children = getChildrenList( parent ).toArray( );
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

	protected List getChildrenList( Object parent )
	{
		ArrayList childrenList = new ArrayList( );
		if ( parent instanceof Object[] )
		{
			Object[] array = (Object[]) parent;
			if ( array instanceof Operator[] )
			{
				return Arrays.asList( array );
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
				for ( Iterator iter = elementHandle.getModuleHandle( )
						.getParameters( )
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
				for ( Iterator iter = elementHandle.getModuleHandle( )
						.getParameters( )
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
				if ( COLUMN_BINDINGS.equals( parent ) )
				{
					List bindingList = DEUtil.getAllColumnBindingList( elementHandle,
							includeSelf );
					// The list is from top to bottom,reverse it
					Collections.reverse( bindingList );
					for ( Iterator iter = bindingList.iterator( ); iter.hasNext( ); )
					{
						ComputedColumnHandle handle = (ComputedColumnHandle) iter.next( );
						if ( !childrenList.contains( handle.getElementHandle( ) ) )
						{
							childrenList.add( handle.getElementHandle( ) );
						}
					}
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
			for ( Iterator iter = DEUtil.getMembers( classInfo ).iterator( ); iter.hasNext( ); )
			{
				childrenList.add( new ILocalizableInfo[]{
						classInfo, (IMemberInfo) iter.next( )
				} );
			}
			for ( Iterator iter = DEUtil.getMethods( classInfo ).iterator( ); iter.hasNext( ); )
			{
				childrenList.add( new ILocalizableInfo[]{
						classInfo, (IMethodInfo) iter.next( )
				} );
			}
		}
		else if ( parent instanceof ParameterGroupHandle )
		{
			childrenList.addAll( ( (ParameterGroupHandle) parent ).getParameters( )
					.getContents( ) );
		}
		else if ( parent instanceof ReportItemHandle
				|| parent instanceof GroupHandle )
		{
			Iterator iter;
			if ( parent instanceof ReportItemHandle )
			{
				iter = ( (ReportItemHandle) parent ).columnBindingsIterator( );
			}
			else
			{
				iter = ( (GroupHandle) parent ).columnBindingsIterator( );
			}
			while ( iter.hasNext( ) )
			{
				childrenList.add( iter.next( ) );
			}
		}
		return childrenList;
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
					boolean isFirst = true;

					Iterator argumentListIter = method.argumentListIterator( );
					IArgumentInfoList arguments = (IArgumentInfoList) argumentListIter.next( );
					for ( Iterator iter = arguments.argumentsIterator( ); iter.hasNext( ); )
					{
						IArgumentInfo argInfo = (IArgumentInfo) iter.next( );
						if ( !isFirst )
						{
							displayText.append( ", " ); //$NON-NLS-1$
						}
						isFirst = false;

						displayText.append( argInfo.getDisplayName( ) );
					}
					displayText.append( ")" ); //$NON-NLS-1$
					if ( !argumentListIter.hasNext( ) )
					{
						displayText.append( " : " ); //$NON-NLS-1$
						String returnType = method.getReturnType( );
						if ( returnType == null )
						{
							returnType = "void"; //$NON-NLS-1$
						}
						displayText.append( returnType );
					}

				}
				else if ( info instanceof IMemberInfo )
				{
					displayText.append( " : " ); //$NON-NLS-1$
					displayText.append( ( (IMemberInfo) info ).getDataType( ) );
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
			return DEUtil.getDisplayLabel( element, false );
		}
		else if ( element instanceof ComputedColumnHandle )
		{
			return ( (ComputedColumnHandle) element ).getName( );
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
		else if ( element instanceof ComputedColumnHandle )
		{
			return TOOLTIP_BINDING_PREFIX
					+ ( (ComputedColumnHandle) element ).getExpression( ); //$NON-NLS-1$
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
		else if ( element instanceof ComputedColumnHandle
				|| element instanceof ResultSetColumnHandle
				|| element instanceof DataSetItemModel )
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
		else if ( element instanceof ParameterHandle )
		{
			return DEUtil.getExpression( element );
		}
		else if ( element instanceof ComputedColumnHandle )
		{
			return DEUtil.getBindingexpression( elementHandle,
					(ComputedColumnHandle) element );
		}
		return null;
	}

	private static Image getIconImage( String id )
	{
		return ReportPlatformUIImages.getImage( id );
	}

	protected List getClassList( boolean isNative )
	{
		List list = DEUtil.getClasses( );
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