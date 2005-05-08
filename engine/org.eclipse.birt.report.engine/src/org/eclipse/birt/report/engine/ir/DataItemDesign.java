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

package org.eclipse.birt.report.engine.ir;

/**
 * DataItem.
 * 
 * reference to report design schema.
 * 
 * @version $Revision: 1.5 $ $Date: 2005/05/08 06:08:26 $
 */
public class DataItemDesign extends ReportItemDesign
{

	/**
	 * Data expression
	 */
	protected Expression value = new Expression( );

	/**
	 * Action associated with this DataItem.
	 */
	protected ActionDesign action;

	/**
	 * help text
	 */
	protected String helpText;
	/**
	 * help text resource key
	 */
	protected String helpTextKey;

	/**
	 * default constructor. create an empty expression.
	 */
	public DataItemDesign( )
	{
	}

	/**
	 * get the value expression
	 * 
	 * @return value expression
	 */
	public Expression getValue( )
	{
		return this.value;
	}

	/**
	 * set the value expression
	 * 
	 * @param value
	 *            value expression
	 */
	public void setValue( Expression value )
	{
		this.value = value;
	}

	public void accept( IReportItemVisitor visitor )
	{
		visitor.visitDataItem( this );
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

	/**
	 * set the help info.
	 * 
	 * @param key
	 *            resource key
	 * @param text
	 *            text content
	 */
	public void setHelpText( String key, String text )
	{
		this.helpTextKey = key;
		this.helpText = text;
	}

	/**
	 * get the help text property.
	 * 
	 * @return help text
	 */
	public String getHelpText( )
	{
		return this.helpText;
	}

	/**
	 * get the help text resource key property.
	 * 
	 * @return resource key of the help text
	 */
	public String getHelpTextKey( )
	{
		return this.helpTextKey;
	}
}
