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

package org.eclipse.birt.report.model.api;

import java.util.Collections;
import java.util.Iterator;

import org.eclipse.birt.report.model.activity.SemanticException;
import org.eclipse.birt.report.model.core.MemberRef;
import org.eclipse.birt.report.model.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.elements.structures.Action;
import org.eclipse.birt.report.model.elements.structures.ParamBinding;
import org.eclipse.birt.report.model.elements.structures.SearchKey;

/**
 * Represents an "action" (hyperlink) attached to an element. Obtain an instance
 * of this class by calling the <code>getActionHandle</code> method on the
 * handle of an element that defines an action.
 * <p>
 * The link type of an Action can be only one of hyperlink, bookmark Link or
 * drill-through.
 * <ul>
 * <li>The hyperlink property returns a standard web-style link with "http:" or
 * "mailto:" prefix.
 * <li>The bookmark link simply identifies a bookmark identified within this
 * report.
 * <li>The drill-though link runs and/or views another report. A drill-through
 * action can include parameters (used when the hyperlink is used to run a
 * report), search keys (an optional list of search criteria) and a bookmark
 * destination within the target report.
 * </ul>
 * 
 * 
 * @see DataItemHandle#getActionHandle()
 * @see ImageHandle#getActionHandle()
 * @see LabelHandle#getActionHandle()
 * 
 * @see org.eclipse.birt.report.model.elements.structures.Action
 */

public class ActionHandle extends StructureHandle
{

	/**
	 * Construct an handle to deal with the action structure.
	 * 
	 * @param element
	 *            the element that defined the action.
	 * @param ref
	 *            reference to the action property.
	 */

	public ActionHandle( DesignElementHandle element, MemberRef ref )
	{
		super( element, ref );
	}

	/**
	 * Gets the hyperlink if the link type is
	 * <code>ACTION_LINK_TYPE_HYPERLINK</code>. Otherwise, return null.
	 * 
	 * @return the link expression in a string
	 */

	public String getHyperlink( )
	{
		if ( DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK
				.equalsIgnoreCase( getLinkType( ) ) )
			return getStringProperty( Action.HYPERLINK_MEMBER );

		return null;
	}

	/**
	 * Gets the target window of the action.
	 * 
	 * @return the window name
	 */

	public String getTargetWindow( )
	{
		if ( DesignChoiceConstants.DRILL_THROUGH_LINK_TYPE_BOOKMARK_LINK
				.equalsIgnoreCase( getLinkType( ) ) )
			return null;

		return getStringProperty( Action.TARGET_WINDOW_MEMBER );
	}

	/**
	 * Gets the link type of the action. The link type are defined in
	 * DesignChoiceConstants and can be one of the following:
	 * <p>
	 * <ul>
	 * <li><code>ACTION_LINK_TYPE_NONE</code>
	 * <li><code>ACTION_LINK_TYPE_HYPERLINK</code>
	 * <li><code>ACTION_LINK_TYPE_DRILLTHROUGH</code>
	 * <li><code>ACTION_LINK_TYPE_BOOKMARK_LINK</code>
	 * </ul>
	 * 
	 * @return the string value of the link type
	 * 
	 * @see org.eclipse.birt.report.model.elements.DesignChoiceConstants
	 */

	public String getLinkType( )
	{
		return getStringProperty( Action.LINK_TYPE_MEMBER );
	}

	/**
	 * Get a handle to deal with the parameter binding list member if the link
	 * type is <code>ACTION_LINK_TYPE_DRILLTHROUGH</code>. Otherwise, return
	 * null.
	 * 
	 * @return a handle to deal with the parameter binding list member
	 */

	public MemberHandle getParamBindings( )
	{
		if ( DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH
				.equalsIgnoreCase( getLinkType( ) ) )
			return getMember( Action.DRILLTHROUGH_PARAM_BINDINGS_MEMBER );

		return null;
	}

	/**
	 * Add a new parameter binding to the action.
	 * 
	 * @param paramBinding
	 *            a new parameter binding to be added.
	 * @throws SemanticException
	 *             if the parameter binding is not valid
	 */

	public void addParamBinding( ParamBinding paramBinding )
			throws SemanticException
	{
		MemberHandle memberHandle = getMember( Action.DRILLTHROUGH_PARAM_BINDINGS_MEMBER );
		elementHandle.design.getActivityStack( ).startTrans( );

		try
		{
			memberHandle.addItem( paramBinding );
			setProperty( Action.LINK_TYPE_MEMBER,
					DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH );
		}
		catch ( SemanticException e )
		{
			elementHandle.design.getActivityStack( ).rollback( );
			throw e;
		}

		elementHandle.design.getActivityStack( ).commit( );
	}

	/**
	 * Get a handle to deal with the search key list member if the link type is
	 * <code>ACTION_LINK_TYPE_DRILLTHROUGH</code> and the drill-through type
	 * is <code>DRILL_THROUGH_LINK_TYPE_BOOKMARK_LINK</code>. Otherwise,
	 * return null.
	 * 
	 * @return a handle to deal with the search key list member
	 */

	public MemberHandle getSearch( )
	{
		if ( DesignChoiceConstants.DRILL_THROUGH_LINK_TYPE_SEARCH
				.equalsIgnoreCase( getDrillThroughType( ) ) )
			return getMember( Action.DRILLTHROUGH_SEARCH_MEMBER );

		return null;
	}

	/**
	 * Add a new search key to the action.
	 * 
	 * @param key
	 *            a new search key to be added.
	 * @throws SemanticException
	 *             if the value is not valid.
	 */

	public void addSearch( SearchKey key ) throws SemanticException
	{
		MemberHandle memberHandle = getMember( Action.DRILLTHROUGH_SEARCH_MEMBER );

		elementHandle.design.getActivityStack( ).startTrans( );
		try
		{
			memberHandle.addItem( key );
			setProperty( Action.LINK_TYPE_MEMBER,
					DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH );
			setProperty( Action.DRILLTHROUGH_TYPE_MEMBER,
					DesignChoiceConstants.DRILL_THROUGH_LINK_TYPE_SEARCH );
		}
		catch ( SemanticException e )
		{
			elementHandle.design.getActivityStack( ).rollback( );
			throw e;
		}
		elementHandle.design.getActivityStack( ).commit( );
	}

	/**
	 * Sets the target window of the action.
	 * 
	 * @param window
	 *            the target window name
	 * @throws SemanticException
	 *             if this property is locked.
	 */

	public void setTargetWindow( String window ) throws SemanticException
	{
		setProperty( Action.TARGET_WINDOW_MEMBER, window );
	}

	/**
	 * Sets the hyperlink of this action. The link type will be changed to
	 * <code>ACTION_LINK_TYPE_HYPERLINK</code>.
	 * 
	 * @param hyperlink
	 *            the hyperlink to set
	 * @throws SemanticException
	 *             if the property is locked.
	 * @see #getHyperlink()
	 */

	public void setHyperlink( String hyperlink ) throws SemanticException
	{
		try
		{
			elementHandle.design.getActivityStack( ).startTrans( );

			setProperty( Action.HYPERLINK_MEMBER, hyperlink );
			setProperty( Action.LINK_TYPE_MEMBER,
					DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK );
		}
		catch ( SemanticException e )
		{
			elementHandle.design.getActivityStack( ).rollback( );
			throw e;
		}

		elementHandle.design.getActivityStack( ).commit( );
	}

	/**
	 * Gets the name of the target report document if the link type is
	 * <code>ACTION_LINK_TYPE_DRILLTHROUGH</code>. Otherwise, return null.
	 * 
	 * @return the name of the target report document
	 * @see #setDrillThroughReportName(String)
	 */

	public String getDrillThroughReportName( )
	{
		return getStringProperty( Action.DRILLTHROUGH_REPORT_NAME_MEMBER );
	}

	/**
	 * Sets target report name for a drill-though link. The link type willl be
	 * changed to <code>ACTION_LINK_TYPE_DRILLTHROUGH</code>. The report name
	 * can include relative or absolute names. If the suffix is omitted, it is
	 * computed on the server by looking for a matching report. BIRT reports are
	 * searched in the following order: 1) a BIRT report document or 2) a BIRT
	 * report design.
	 * 
	 * @param reportName
	 *            the name of the target report
	 * @throws SemanticException
	 *             if the property is locked.
	 * @see #getDrillThroughReportName()
	 */

	public void setDrillThroughReportName( String reportName )
			throws SemanticException
	{
		try
		{
			elementHandle.design.getActivityStack( ).startTrans( );

			setProperty( Action.DRILLTHROUGH_REPORT_NAME_MEMBER, reportName );
			setProperty( Action.LINK_TYPE_MEMBER,
					DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH );
		}
		catch ( SemanticException e )
		{
			elementHandle.design.getActivityStack( ).rollback( );
			throw e;
		}
		elementHandle.design.getActivityStack( ).commit( );
	}

	/**
	 * Gets the bookmark link if the link type is
	 * <code>ACTION_LINK_TYPE_BOOKMARK_LINK</code>. Otherwise, return null.
	 * 
	 * @return the bookmark link
	 */

	public String getBookmarkLink( )
	{
		if ( DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK
				.equalsIgnoreCase( getLinkType( ) ) )
			return getStringProperty( Action.BOOKMARK_LINK_MEMBER );

		return null;
	}

	/**
	 * Sets the bookmark link of this action. The link type will be changed to
	 * <code>ACTION_LINK_TYPE_BOOKMARK_LINK</code>.
	 * 
	 * @param expr
	 *            the expression value.
	 * @throws SemanticException
	 *             if the property is locked.
	 * @see #getBookmarkLink()
	 */

	public void setBookmarkLink( String expr ) throws SemanticException
	{
		try
		{
			elementHandle.design.getActivityStack( ).startTrans( );

			setProperty( Action.BOOKMARK_LINK_MEMBER, expr );
			setProperty( Action.LINK_TYPE_MEMBER,
					DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK );

		}
		catch ( SemanticException e )
		{
			elementHandle.design.getActivityStack( ).rollback( );
			throw e;
		}
		elementHandle.design.getActivityStack( ).commit( );
	}

	/**
	 * Gets the drill-through bookmark if the link type is
	 * <code>ACTION_LINK_TYPE_DRILLTHROUGH</code> and the drill-through type
	 * is <code>DRILL_THROUGH_LINK_TYPE_BOOKMARK_LINK</code>.
	 * 
	 * @return drill-through bookmark link
	 */

	public String getDrillThroughBookmarkLink( )
	{
		if ( DesignChoiceConstants.DRILL_THROUGH_LINK_TYPE_BOOKMARK_LINK
				.equalsIgnoreCase( getDrillThroughType( ) ) )
			return getStringProperty( Action.DRILLTHROUGH_BOOKMARK_LINK_MEMBER );

		return null;
	}

	/**
	 * Sets the drill-through bookmark. The link type will be changed to
	 * <code>ACTION_LINK_TYPE_DRILLTHROUGH</code>, and drill-through type
	 * will be changed to <code>DRILL_THROUGH_LINK_TYPE_BOOKMARK_LINK</code>.
	 * 
	 * @param bookmark
	 *            the bookmark to set.
	 * @throws SemanticException
	 *             if the property is locked.
	 * @see #getBookmarkLink()
	 */

	public void setDrillThroughBookmarkLink( String bookmark )
			throws SemanticException
	{
		try
		{
			elementHandle.design.getActivityStack( ).startTrans( );

			setProperty( Action.DRILLTHROUGH_BOOKMARK_LINK_MEMBER, bookmark );
			setProperty( Action.LINK_TYPE_MEMBER,
					DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH );
			setProperty( Action.DRILLTHROUGH_TYPE_MEMBER,
					DesignChoiceConstants.DRILL_THROUGH_LINK_TYPE_BOOKMARK_LINK );
		}
		catch ( SemanticException e )
		{
			elementHandle.design.getActivityStack( ).rollback( );
			throw e;
		}
		elementHandle.design.getActivityStack( ).commit( );
	}

	/**
	 * Gets the drill-through link type. The drillthrough link type is defined
	 * in DesignChoiceConstants and can be one of the following:
	 * <p>
	 * <ul>
	 * <li><code>DRILL_THROUGH_LINK_TYPE_NONE</code>
	 * <li><code>DRILL_THROUGH_LINK_TYPE_BOOKMARK_LINK</code>
	 * <li><code>DRILL_THROUGH_LINK_TYPE_SEARCH</code>
	 * </ul>
	 * 
	 * @return the string value of the drillthrough type
	 * 
	 * @see org.eclipse.birt.report.model.elements.DesignChoiceConstants
	 */

	public String getDrillThroughType( )
	{
		return getStringProperty( Action.DRILLTHROUGH_TYPE_MEMBER );
	}

	/**
	 * Gets the parameter binding list of a drill-through action if the link
	 * type is <code>ACTION_LINK_TYPE_DRILLTHROUGH</code>. Each one is the
	 * instance of <code>ParameBindingHandle</code>
	 * <p>
	 * Action binds a data value in the report to a report parameter defined in
	 * the target report.
	 * <p>
	 * Note that this is a parameter binding, not a parameter definition. The
	 * report makes no attempt to check that the parameters listed here are
	 * accurate in name or type for the target report. Also, it is legal to bind
	 * the same parameter multiple times; the meaning depends on the semantics
	 * of the target report.
	 * 
	 * @return the iterator over parameters of a drill-through action.
	 *  
	 */

	public Iterator paramBindingsIterator( )
	{
		if ( !DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH
				.equalsIgnoreCase( getLinkType( ) ) )
			return Collections.EMPTY_LIST.iterator( );

		MemberHandle memberHandle = getMember( Action.DRILLTHROUGH_PARAM_BINDINGS_MEMBER );
		return memberHandle.iterator( );

	}

	/**
	 * Gets the search key list for a drill-through action if the link type is
	 * <code>ACTION_LINK_TYPE_DRILLTHROUGH</code> and the drill through type
	 * is <code>DRILL_THROUGH_LINK_TYPE_SEARCH</code>. Each one is the
	 * instance of <code>SearchKeyHandle</code>
	 * <p>
	 * The search key list identifies search criteria in the target report and
	 * is used for drill-though links. The search is assumed to be quality. That
	 * is, identify a column defined in the target report and a data value
	 * defined in this report. The link will then search for this value.
	 * 
	 * @return the iterator over search keys of a drill-through action.
	 *  
	 */

	public Iterator searchIterator( )
	{
		if ( !DesignChoiceConstants.DRILL_THROUGH_LINK_TYPE_SEARCH
				.equalsIgnoreCase( getDrillThroughType( ) ) )
			return Collections.EMPTY_LIST.iterator( );;

		MemberHandle memberHandle = getMember( Action.DRILLTHROUGH_SEARCH_MEMBER );
		return memberHandle.iterator( );
	}

}