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

package org.eclipse.birt.report.engine.content.impl;

import java.util.Map;

import org.eclipse.birt.report.engine.content.IHyperlinkAction;

/**
 * Implements the <code>IHyperlinkAction</code> interface for passing action
 * informaiton to emitters
 */
public class ActionContent implements IHyperlinkAction
{

	/**
	 * action type
	 */
	protected int type;

	/**
	 * bookmark string
	 */
	protected String bookmark;

	/**
	 * action string. See base interface
	 */
	protected String actionString;

	/**
	 * report name
	 */
	protected String reportName;

	/**
	 * parameters and their values for running drillthrough reports
	 */
	protected Map parameterBindings;

	/**
	 * search keys and their values for searching drillthrough reports
	 */
	protected Map searchCriteria;

	/**
	 * the name of a frame where a document is to be opened.
	 */
	protected String target = null;

	/**
	 * Constructor for hyperline action type
	 * 
	 * @param actionString
	 *            the action string
	 * @param target
	 *            the target window
	 */
	public ActionContent( String actionString, String target )
	{
		this.type = IHyperlinkAction.ACTION_HYPERLINK;
		this.actionString = actionString;
		this.target = target;
	}

	/**
	 * Constrictor for bookmark action type
	 * 
	 * @param bookmark
	 *            the bookmark value.
	 */
	public ActionContent( String bookmark )
	{
		this.type = IHyperlinkAction.ACTION_BOOKMARK;
		this.actionString = bookmark;
		this.bookmark = bookmark;
	}

	public ActionContent( String actionString, String bookmark,
			String reportName, Map parameterBindings, Map searchCriteria,
			String target )
	{
		this.actionString = actionString;
		this.bookmark = bookmark;
		this.reportName = reportName;
		this.parameterBindings = parameterBindings;
		this.searchCriteria = searchCriteria;
		this.type = IHyperlinkAction.ACTION_DRILLTHROUGH;
		this.target = target;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IHyperlinkAction#getType()
	 */
	public int getType( )
	{
		return type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IHyperlinkAction#getBookmark()
	 */
	public String getBookmark( )
	{
		return bookmark;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IHyperlinkAction#getActionString()
	 */
	public String getActionString( )
	{
		return actionString;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IHyperlinkAction#getReportName()
	 */
	public String getReportName( )
	{
		return reportName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IHyperlinkAction#getParameterbindings()
	 */
	public Map getParameterBindings( )
	{
		return parameterBindings;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IHyperlinkAction#getSearchCriteria()
	 */
	public Map getSearchCriteria( )
	{
		return searchCriteria;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IHyperlinkAction#getTargetWindow()
	 */
	public String getTargetWindow( )
	{
		return target;
	}

	/**
	 * @param actionString
	 *            The actionString to set.
	 */
	public void setActionString( String actionString )
	{
		this.actionString = actionString;
	}

	/**
	 * @param bookmark
	 *            The bookmark to set.
	 */
	public void setBookmark( String bookmark )
	{
		this.bookmark = bookmark;
	}

	/**
	 * @param parameterBindings
	 *            The parameterBindings to set.
	 */
	public void setParameterBindings( Map parameterBindings )
	{
		this.parameterBindings = parameterBindings;
	}

	/**
	 * @param reportName
	 *            The reportName to set.
	 */
	public void setReportName( String reportName )
	{
		this.reportName = reportName;
	}

	/**
	 * @param searchCriteria
	 *            The searchCriteria to set.
	 */
	public void setSearchCriteria( Map searchCriteria )
	{
		this.searchCriteria = searchCriteria;
	}

	/**
	 * @param type
	 *            The type to set.
	 */
	public void setType( int type )
	{
		this.type = type;
	}
}