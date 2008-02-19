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

package org.eclipse.birt.chart.reportitem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.event.StructureType;
import org.eclipse.birt.chart.factory.ActionEvaluatorAdapter;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.TooltipValue;
import org.eclipse.birt.chart.model.attribute.URLValue;
import org.eclipse.birt.chart.model.data.Action;
import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.SearchKeyHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

/**
 * A BIRT action evaluator implementation.
 */
public class BIRTActionEvaluator extends ActionEvaluatorAdapter
{

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.reportitem/trace" ); //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.factory.IActionEvaluator#getActionExpressions(org.eclipse.birt.chart.model.data.Action)
	 */
	public String[] getActionExpressions( Action action, StructureSource source )
	{
		if ( ActionType.URL_REDIRECT_LITERAL.equals( action.getType( ) ) )
		{
			URLValue uv = (URLValue) action.getValue( );

			String sa = uv.getBaseUrl( );

			try
			{
				ActionHandle handle = ModuleUtil.deserializeAction( sa );

				List expList = new ArrayList( );
				String exp;

				if ( DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK.equals( handle.getLinkType( ) ) )
				{
					exp = handle.getURI( );

					if ( !expList.contains( exp ) )
					{
						expList.add( exp );
					}
				}
				else if ( DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK.equals( handle.getLinkType( ) ) )
				{
					exp = handle.getTargetBookmark( );

					if ( !expList.contains( exp ) )
					{
						expList.add( exp );
					}
				}
				else if ( DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH.equals( handle.getLinkType( ) ) )
				{
					exp = handle.getTargetBookmark( );

					if ( exp != null && !expList.contains( exp ) )
					{
						expList.add( exp );
					}

					for ( Iterator itr = handle.getSearch( ).iterator( ); itr.hasNext( ); )
					{
						SearchKeyHandle skh = (SearchKeyHandle) itr.next( );
						exp = skh.getExpression( );

						if ( !expList.contains( exp ) )
						{
							expList.add( exp );
						}
					}

					for ( Iterator itr = handle.getParamBindings( ).iterator( ); itr.hasNext( ); )
					{
						ParamBindingHandle pbh = (ParamBindingHandle) itr.next( );
						exp = pbh.getExpression( );

						if ( !expList.contains( exp ) )
						{
							expList.add( exp );
						}
					}

				}

				if ( expList.size( ) > 0 )
				{
					return (String[]) expList.toArray( new String[expList.size( )] );
				}
			}
			catch ( DesignFileException e )
			{
				logger.log( e );
			}
		}
		else if ( ActionType.SHOW_TOOLTIP_LITERAL.equals( action.getType( ) ) )
		{
			if ( StructureType.SERIES.equals( source.getType( ) ) )
			{
				TooltipValue tv = (TooltipValue) action.getValue( );

				String exp = tv.getText( );
				if ( exp != null && exp.trim( ).length( ) > 0 )
				{
					return new String[]{
						exp
					};
				}
			}
		}

		return null;
	}
}
