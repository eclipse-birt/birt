/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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
	protected String hyperlink;

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
	 * Constructor for hyperlink action type
	 * 
	 * @param actionString
	 *            the action string
	 * @param target
	 *            the target window
	 */
	public ActionContent( String hyperlink, String target )
	{
		this.type = IHyperlinkAction.ACTION_HYPERLINK;
		this.hyperlink = hyperlink;
		this.target = target;
	}

	/**
	 * Constructor for bookmark action type
	 * 
	 * @param bookmark
	 *            the bookmark value.
	 */
	public ActionContent( String bookmark )
	{
		this.type = IHyperlinkAction.ACTION_BOOKMARK;
		this.bookmark = bookmark;
	}

	/**
	 * Constructor for drill-through action type
	 * 
	 * @param bookmark
	 *            the bookmark string
	 * @param reportName
	 *            the report name navigated
	 * @param parameterBindings
	 *            the parameters of the report navigated
	 * @param searchCriteria
	 *            the search criteria
	 * @param target
	 *            the target window
	 */
	public ActionContent( String bookmark, String reportName,
			Map parameterBindings, Map searchCriteria, String target )
	{
		this.bookmark = bookmark;
		this.reportName = reportName;
		this.parameterBindings = parameterBindings;
		this.searchCriteria = searchCriteria;
		this.target = target;
		this.type = IHyperlinkAction.ACTION_DRILLTHROUGH;
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
	 * @see org.eclipse.birt.report.engine.api.IHyperlinkAction#getReportName()
	 */
	public String getReportName( )
	{
		assert type == IHyperlinkAction.ACTION_DRILLTHROUGH;
		return reportName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IHyperlinkAction#getParameterbindings()
	 */
	public Map getParameterBindings( )
	{
		assert type == IHyperlinkAction.ACTION_DRILLTHROUGH;
		return parameterBindings;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IHyperlinkAction#getSearchCriteria()
	 */
	public Map getSearchCriteria( )
	{
		assert type == IHyperlinkAction.ACTION_DRILLTHROUGH;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IHyperlinkAction#getHyperlink()
	 */
	public String getHyperlink( )
	{
		return hyperlink;
	}
}