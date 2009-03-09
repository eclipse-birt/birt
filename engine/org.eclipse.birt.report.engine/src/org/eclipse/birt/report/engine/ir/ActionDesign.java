/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.ir;

/**
 * Action. Action include: hyperlink, drill through and bookmark.
 * 
 */
public class ActionDesign
{

	/**
	 * hyperlink action
	 */
	public final static int ACTION_HYPERLINK = 1;
	/**
	 * bookmark action
	 */
	public final static int ACTION_BOOKMARK = 2;
	/**
	 * drillthrough action
	 */
	public final static int ACTION_DRILLTHROUGH = 3;

	/**
	 * action type, one of the hyperlink, bookmark drillthrough.
	 */
	protected int actionType;
	
	protected Expression<String> tooltip;
	/**
	 * hyper link
	 */
	protected Expression<String> hyperlink;
	/**
	 * bookmark.
	 */
	protected Expression<String> bookmark;

	/**
	 * drill through
	 */
	protected DrillThroughActionDesign drillThrough;

	/**
	 * The name of a frame where a document is to be opened.
	 */
	protected Expression<String> target = null;
	
	/**
	 * The type of the target file.
	 */
	protected Expression<String> targetFileType = null;

	/**
	 * @return Returns the bookmark.
	 */
	public Expression<String> getBookmark( )
	{
		assert this.actionType == ACTION_BOOKMARK;
		return bookmark;
	}

	/**
	 * @param bookmark
	 *            The bookmark to set.
	 */
	public void setBookmark( Expression<String> bookmark )
	{
		this.actionType = ActionDesign.ACTION_BOOKMARK;
		this.bookmark = bookmark;
	}

	/**
	 * @return Returns the hyperlink.
	 */
	public Expression<String> getHyperlink( )
	{
		assert this.actionType == ACTION_HYPERLINK;
		return hyperlink;
	}

	/**
	 * @param hyperlink
	 *            The hyperlink to set.
	 */
	public void setHyperlink( Expression<String> hyperlink )
	{
		this.hyperlink = hyperlink;
		this.actionType = ActionDesign.ACTION_HYPERLINK;
	}

	/**
	 * @return Returns the drillThrough.
	 */
	public DrillThroughActionDesign getDrillThrough( )
	{
		assert this.actionType == ACTION_DRILLTHROUGH;
		return drillThrough;
	}

	/**
	 * @param drillThrough
	 *            The drillThrough to set.
	 */
	public void setDrillThrough( DrillThroughActionDesign drillThrough )
	{
		this.actionType = ACTION_DRILLTHROUGH;
		this.drillThrough = drillThrough;
	}

	/**
	 * @return Returns the type.
	 */
	public int getActionType( )
	{
		return actionType;
	}

	/**
	 * @return the target window.
	 */
	public Expression<String> getTargetWindow( )
	{
		return target;
	}

	/**
	 * @param target
	 *            The name of a frame where a document is to be opened.
	 */
	public void setTargetWindow( Expression<String> target )
	{
		this.target = target;
	}
	
	/**
	 * Sets target report file type for a drill-through action. The format type
	 * for action are defined in DesignChoiceConstants.
	 * 
	 * @param targetFileType
	 *            the type of the target report file.
	 */
	public void setTargetFileType( Expression<String> targetFileType )
	{
		this.targetFileType = targetFileType;
	}

	/**
	 * @return the type of the target report file.
	 */
	public Expression<String> getTargetFileType( )
	{
		return targetFileType;
	}
	
	public void setTooltip(Expression<String> tooltip)
	{
		this.tooltip = tooltip;
	}
	
	public Expression<String> getTooltip()
	{
		return tooltip;
	}
}