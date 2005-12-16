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

package org.eclipse.birt.report.engine.script.internal.instance;

import java.lang.String;
import java.util.Map;

import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.ir.ReportElementDesign;
import org.eclipse.birt.report.engine.api.script.instance.IReportItemInstance;
import org.eclipse.birt.report.engine.api.script.instance.IScriptStyle;
import org.eclipse.birt.report.engine.content.impl.AbstractContent;

/**
 * A class representing the runtime state of a report item
 */
public class ReportItemInstance implements IReportItemInstance
{

	protected AbstractContent content;

	public ReportItemInstance( AbstractContent content )
	{
		this.content = content;
	}

	protected void setContent( AbstractContent content )
	{
		this.content = content;
	}

	protected ReportItemInstance( )
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IReportInstance#getStyle()
	 */
	public IScriptStyle getStyle( )
	{
		return new StyleInstance( content.getStyle( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IReportInstance#getHyperlink()
	 */
	public String getHyperlink( )
	{
		return content.getHyperlinkAction( ).getHyperlink( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IReportInstance#getName()
	 */
	public String getName( )
	{
		return content.getName( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IReportInstance#setName(java.lang.String)
	 */
	public void setName( String name )
	{
		content.setName( name );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IReportInstance#getHelpText()
	 */
	public String getHelpText( )
	{
		return content.getHelpText( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IReportInstance#setHelpText(java.lang.String)
	 */
	public void setHelpText( String helpText )
	{
		content.setHelpText( helpText );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IReportInstance#getHorizontalPosition()
	 */
	public String getHorizontalPosition( )
	{
		return content.getX( ).toString( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IReportInstance#setHorizontalPosition(java.lang.String)
	 */
	public void setHorizontalPosition( String position )
	{
		content.setX( DimensionType.parserUnit( position ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IReportInstance#getVerticalPosition()
	 */
	public String getVerticalPosition( )
	{
		return content.getY( ).toString( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IReportInstance#setVerticalPosition(java.lang.String)
	 */
	public void setVerticalPosition( String position )
	{
		content.setY( DimensionType.parserUnit( position ) );
	}

	public Object getNamedExpressionValue( String name )
	{
		Object generatedBy = content.getGenerateBy( );
		if ( generatedBy instanceof ReportElementDesign )
		{
			ReportElementDesign design = ( ReportElementDesign ) generatedBy;
			Map m = design.getNamedExpressions( );
			return m.get( name );
		}
		// TODO Implement
		return null;
	}

	public void setNamedExpressionValue( String name, Object value )
	{
		// TODO Implement

	}

	public Object getUserProperty( String name )
	{
		// TODO Implement
		return null;
	}

	public void setUserProperty( String name, Object value )
	{
		// TODO Implement

	}

}
