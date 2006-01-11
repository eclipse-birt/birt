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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

import org.eclipse.birt.core.util.IOUtil;
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
	protected int type = -1;

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

	protected String format;

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
	public ActionContent( )
	{
	}

	public void setHyperlink( String hyperlink, String target )
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
	public void setBookmark( String bookmark )
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
	public void setDrillThrough( String bookmark, String reportName,
			Map parameterBindings, Map searchCriteria, String target,
			String format )
	{
		this.bookmark = bookmark;
		this.reportName = reportName;
		this.parameterBindings = parameterBindings;
		this.searchCriteria = searchCriteria;
		this.target = target;
		this.type = IHyperlinkAction.ACTION_DRILLTHROUGH;
		this.format = format;
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

	/**
	 * object document version
	 */
	static final protected int VERSION = 0;

	final static int FIELD_NONE = -1;
	final static int FIELD_TYPE = 0;
	final static int FIELD_BOOKMARK = 1;
	final static int FIELD_HYPERLINK = 2;
	final static int FIELD_REPORTNAME = 3;
	final static int FIELD_PARAMETERBINDINGS = 4;
	final static int FIELD_SEARCHCRITERIA = 5;
	final static int FIELD_TARGET = 6;
	final static int FIELD_FORMAT = 7;

	protected void writeFields( DataOutputStream out ) throws IOException
	{
		if ( type != -1 )
		{
			IOUtil.writeInt( out, FIELD_TYPE );
			IOUtil.writeInt( out, type );
		}
		if ( bookmark != null )
		{
			IOUtil.writeInt( out, FIELD_BOOKMARK );
			IOUtil.writeString( out, bookmark );
		}
		if ( hyperlink != null )
		{
			IOUtil.writeInt( out, FIELD_HYPERLINK );
			IOUtil.writeString( out, hyperlink );
		}
		if ( reportName != null )
		{
			IOUtil.writeInt( out, FIELD_REPORTNAME );
			IOUtil.writeString( out, reportName );
		}
		if ( parameterBindings != null )
		{
			IOUtil.writeInt( out, FIELD_PARAMETERBINDINGS );
			IOUtil.writeMap( out, parameterBindings );
		}
		if ( searchCriteria != null )
		{
			IOUtil.writeInt( out, FIELD_SEARCHCRITERIA );
			IOUtil.writeMap( out, searchCriteria );
		}
		if ( target != null )
		{
			IOUtil.writeInt( out, FIELD_TARGET );
			IOUtil.writeString( out, target );
		}
		if ( format != null )
		{
			IOUtil.writeInt( out, FIELD_FORMAT );
			IOUtil.writeString( out, format );
		}

	}

	protected void readField( int version, int filedId, DataInputStream in )
			throws IOException
	{
		switch ( filedId )
		{
			case FIELD_TYPE :
				type = IOUtil.readInt( in );
				break;
			case FIELD_BOOKMARK :
				bookmark = IOUtil.readString( in );
				break;
			case FIELD_HYPERLINK :
				hyperlink = IOUtil.readString( in );
				break;
			case FIELD_REPORTNAME :
				reportName = IOUtil.readString( in );
				break;
			case FIELD_PARAMETERBINDINGS :
				parameterBindings = IOUtil.readMap( in );
				break;
			case FIELD_SEARCHCRITERIA :
				searchCriteria = IOUtil.readMap( in );
				break;
			case FIELD_TARGET :
				target = IOUtil.readString( in );
				break;
			case FIELD_FORMAT :
				format = IOUtil.readString( in );
				break;
		}
	}

	public void readObject( DataInputStream in ) throws IOException
	{
		int version = IOUtil.readInt( in );
		int filedId = IOUtil.readInt( in );
		while ( filedId != FIELD_NONE )
		{
			readField( version, filedId, in );
			filedId = IOUtil.readInt( in );
		}
	}

	public void writeObject( DataOutputStream out ) throws IOException
	{
		IOUtil.writeInt( out, VERSION );
		writeFields( out );
		IOUtil.writeInt( out, FIELD_NONE );
	}

	public String getFormat( )
	{
		return format;
	}

}