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

package org.eclipse.birt.report.designer.internal.ui.views.dialogs.provider;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.ui.dialogs.MapRuleBuilder;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.MapRuleHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.MapRule;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;

/**
 * Helper class for map rule handle operation.
 */

public class MapHandleProvider
{

	private static final MapRuleHandle[] EMPTY = new MapRuleHandle[0];

	protected DesignElementHandle elementHandle;
	public static int EXPRESSION_TYPE_ROW = 0;
	public static int EXPRESSION_TYPE_DATA = 1;
	private int expressionType;
	
	public MapHandleProvider()
	{		
		super();
		this.expressionType = EXPRESSION_TYPE_ROW;
	}
	
	public MapHandleProvider(int expressionType)
	{		
		super();
		this.expressionType = expressionType;
	}
	
	public int getExpressionType()
	{
		return expressionType;
	}
	
	public void setExpressionType(int expressionType)
	{
		this.expressionType = expressionType;
	}
	
	public URL getResourceURL( )
	{
		return SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( )
				.findResource( getBaseName( ), IResourceLocator.MESSAGE_FILE );
	}
	
	public String getBaseName( )
	{
		return SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( )
				.getIncludeResource( );
	}
	
	/**
	 * Returns the current design element handle.
	 * 
	 * @return
	 */
	public DesignElementHandle getDesignElementHandle( )
	{
		return elementHandle;
	}

	/**
	 * Returns the column text for each highlight item.
	 * 
	 * @param element
	 *            highlight rule handle element.
	 * @param columnIndex
	 * @return
	 */
	public String getColumnText( Object element, int columnIndex )
	{
		MapRuleHandle handle = (MapRuleHandle) element;

		switch ( columnIndex )
		{
			case 0 :
				String pv = handle.getDisplay( );

				return pv == null ? "" : pv; //$NON-NLS-1$

			case 1 :
				//				String exp = resolveNull( getTestExpression( ) )
				String exp = resolveNull( handle.getTestExpression( ) )
						+ " " //$NON-NLS-1$
						+ MapRuleBuilder.getNameForOperator( handle.getOperator( ) );

				int vv = MapRuleBuilder.determineValueVisible( handle.getOperator( ) );

				if ( vv == 1 )
				{
					exp += " " + resolveNull( handle.getValue1( ) ); //$NON-NLS-1$
				}
				else if ( vv == 2 )
				{
					exp += " " //$NON-NLS-1$
							+ resolveNull( handle.getValue1( ) ) + " , " //$NON-NLS-1$
							+ resolveNull( handle.getValue2( ) );
				}

				return exp;

			default :
				return ""; //$NON-NLS-1$
		}
	}

	/**
	 * Returns the test expression property value for current map rules
	 * property.
	 * 
	 * @return
	 */
	//	public String getTestExpression( )
	//	{
	//		PropertyHandle mapRules = elementHandle.getPropertyHandle(
	// StyleHandle.MAP_TEST_EXPR_PROP );
	//
	//		return mapRules.getStringValue( );
	//
	//	}
	private String resolveNull( String src )
	{
		if ( src == null )
		{
			return ""; //$NON-NLS-1$
		}

		return src;
	}

	/**
	 * Sets the test expression property value for current map rules property.
	 * 
	 * @param exp
	 *            test expression value.
	 * @throws SemanticException
	 */
	//	public void setTestExpression( String exp ) throws SemanticException
	//	{
	//		PropertyHandle mapRules = elementHandle.getPropertyHandle(
	// StyleHandle.MAP_TEST_EXPR_PROP );
	//
	//		mapRules.setStringValue( exp );
	//	}
	/**
	 * Swaps the two neighbour-items, no edge check.
	 * 
	 * @param pos
	 *            item position.
	 * @param direction
	 *            negative - UP or LEFT, positive or zero - BOTTOM or RIGHT
	 * @return
	 * @throws PropertyValueException
	 */
	public boolean doSwapItem( int pos, int direction )
			throws PropertyValueException
	{
		PropertyHandle phandle = elementHandle.getPropertyHandle( StyleHandle.MAP_RULES_PROP );

		if ( direction < 0 )
		{
			phandle.moveItem( pos, pos - 1 );
		}
		else
		{
			/**
			 * Original code: phandle.moveItem( pos, pos + 1 );
			 * 
			 * Changes due to model api changes. since property handle now
			 * treats moving from 0-0, 0-1 as the same.
			 */
			phandle.moveItem( pos, pos + 1 );
		}

		return true;
	}

	/**
	 * Deletes specified map rule item from current map rules property.
	 * 
	 * @param pos
	 *            item position.
	 * @return
	 * @throws PropertyValueException
	 */
	public boolean doDeleteItem( int pos ) throws PropertyValueException
	{
		PropertyHandle phandle = elementHandle.getPropertyHandle( StyleHandle.MAP_RULES_PROP );

		phandle.removeItem( pos );

		return true;
	}

	/**
	 * Adds new map rule item to current map rules property.
	 * 
	 * @param rule
	 *            new map rule item.
	 * @param pos
	 *            current map rule items count.
	 * @return new created map rule handle.
	 */
	public MapRuleHandle doAddItem( MapRule rule, int pos )
	{
		PropertyHandle phandle = elementHandle.getPropertyHandle( StyleHandle.MAP_RULES_PROP );

		try
		{
			phandle.addItem( rule );
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
		}

		StructureHandle handle = rule.getHandle( phandle, pos );

		return (MapRuleHandle) handle;
	}

	/**
	 * Returns all map rule items from current DesignElement.
	 * 
	 * @param inputElement
	 *            design element handle.
	 * @return
	 */
	public Object[] getElements( Object inputElement )
	{
		if ( inputElement instanceof List )
		{
			if ( ( (List) inputElement ).size( ) > 0 )
			{
				inputElement = ( (List) inputElement ).get( 0 );
			}
			else
			{
				inputElement = null;
			}
		}

		if ( inputElement instanceof DesignElementHandle )
		{
			elementHandle = (DesignElementHandle) inputElement;

			PropertyHandle mapRules = elementHandle.getPropertyHandle( StyleHandle.MAP_RULES_PROP );

			ArrayList list = new ArrayList( );

			for ( Iterator itr = mapRules.iterator( ); itr.hasNext( ); )
			{
				Object o = itr.next( );

				list.add( o );
			}

			return (MapRuleHandle[]) list.toArray( new MapRuleHandle[0] );
		}

		return EMPTY;
	}
}