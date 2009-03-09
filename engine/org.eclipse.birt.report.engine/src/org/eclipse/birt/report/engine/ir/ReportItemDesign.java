/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.ir;

import org.eclipse.birt.core.script.ScriptExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;

/**
 * Report Item
 * 
 */
abstract public class ReportItemDesign extends StyledElementDesign
{
	/**
	 * x position
	 */
	protected Expression<DimensionType> x;
	/**
	 * y position
	 */
	protected Expression<DimensionType> y;
	/**
	 * width
	 */
	protected Expression<DimensionType> width;
	/**
	 * height
	 */
	protected Expression<DimensionType> height;

	/**
	 * book-mark associated with this element.
	 */
	protected Expression<String> bookmark;
	/**
	 * TOC expression
	 */
	protected Expression<Object> toc;
	
	/**
	 * scripted called while on created
	 */
	protected ScriptExpression onCreate;
	
	/**
	 * script called while on render
	 */
	protected ScriptExpression onRender;
	
	/**
	 * script called while on render
	 */
	protected ScriptExpression onPageBreak;	

	/**
	 * Visibility property.
	 */
	protected VisibilityDesign visibility;
	
	/**
	 * Action associated with this DataItem.
	 */
	protected ActionDesign action;	
	
	/**
	 * query used to create the data set.
	 */
	transient protected IDataQueryDefinition[] queries;
	/**
	 * execution state associated with this design
	 */
	transient protected Object executionState;
	
	/**
	 * if the item use cached result or not.
	 */
	protected boolean useCachedResult = false;
	
	/**
	 * @return Returns the height.
	 */
	public Expression<DimensionType> getHeight( )
	{
		return height;
	}

	/**
	 * @param height
	 *            The height to set.
	 */
	public void setHeight( Expression<DimensionType> height )
	{
		this.height = height;
	}

	/**
	 * @return Returns the width.
	 */
	public Expression<DimensionType> getWidth( )
	{
		return width;
	}

	/**
	 * @param width
	 *            The width to set.
	 */
	public void setWidth( Expression<DimensionType> width )
	{
		this.width = width;
	}

	/**
	 * @return Returns the x.
	 */
	public Expression<DimensionType> getX( )
	{
		return x;
	}

	/**
	 * @param x
	 *            The x to set.
	 */
	public void setX( Expression<DimensionType> x )
	{
		this.x = x;
	}

	/**
	 * @return Returns the y.
	 */
	public Expression<DimensionType> getY( )
	{
		return y;
	}

	/**
	 * @param y
	 *            The y to set.
	 */
	public void setY( Expression<DimensionType> y )
	{
		this.y = y;
	}

	/**
	 * accept a visitor. see visit pattern.
	 * 
	 * @param visitor
	 */
	abstract public Object accept( IReportItemVisitor visitor , Object value);

	
	public Expression<Object> getTOC( )
	{
		return toc;
	}
	
	public void setTOC( Expression<Object> expr )
	{
		this.toc = expr;
	}
	/**
	 * @return Returns the boo-kmark.
	 */
	public Expression<String> getBookmark( )
	{
		return bookmark;
	}
	/**
	 * @param bookmark The book-mark to set.
	 */
	public void setBookmark( Expression<String> bookmark )
	{
		this.bookmark = bookmark;
	}
	
	/**
	 * @return Returns the queries.
	 */
	public IDataQueryDefinition[] getQueries( )
	{
		return queries;
	}
	/**
	 * @param query The queries to set.
	 */
	public void setQueries( IDataQueryDefinition[] queries )
	{
		this.queries = queries;
	}
	
	/**
	 * @return Returns the query.
	 */
	public IDataQueryDefinition getQuery( )
	{
		if ( queries != null && queries.length > 0 )
		{
			return queries[0];
		}
		return null;
	}
	/**
	 * @param query The query to set.
	 */
	public void setQueries( IBaseQueryDefinition query )
	{
		this.queries = new IBaseQueryDefinition[]{ query };
	}
	
	
	/**
	 * @return Returns the onCreate.
	 */
	public ScriptExpression getOnCreate( )
	{
		return onCreate;
	}
	/**
	 * @param onCreate The onCreate to set.
	 */
	public void setOnCreate( ScriptExpression expr )
	{
		onCreate = expr;
	}
	/**
	 * @return Returns the onRender.
	 */
	public ScriptExpression getOnRender( )
	{
		return onRender;
	}
	/**
	 * @param onPageBreak The onPageBreak to set.
	 */
	public void setOnPageBreak( ScriptExpression expr )
	{
		onPageBreak = expr;
	}
	/**
	 * @return Returns the onPageBreak.
	 */
	public ScriptExpression getOnPageBreak( )
	{
		return onPageBreak;
	}
	/**
	 * @param onRender The onRender to set.
	 */
	public void setOnRender( ScriptExpression expr )
	{
		onRender = expr;
	}
	/**
	 * @return Returns the visibility.
	 */
	public VisibilityDesign getVisibility( )
	{
		return visibility;
	}
	/**
	 * @param visibility The visibility to set.
	 */
	public void setVisibility( VisibilityDesign visibility )
	{
		this.visibility = visibility;
	}
	
	/**
	 * @return Returns the action.
	 */
	public ActionDesign getAction( )
	{
		return action;
	}

	/**
	 * @param action
	 *            The action to set.
	 */
	public void setAction( ActionDesign action )
	{
		this.action = action;
	}

	public void setExecutionState(Object state)
	{
		executionState = state;
	}
	
	public Object getExecutionState()
	{
		return executionState;
	}
	

	public void setUseCachedResult( boolean useCachedResult )
	{
		this.useCachedResult = useCachedResult;
	}
	
	public boolean useCachedResult( )
	{
		return useCachedResult;
	}
}
