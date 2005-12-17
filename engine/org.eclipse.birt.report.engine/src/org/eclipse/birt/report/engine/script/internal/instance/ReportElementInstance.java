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

import java.util.Map;

import org.eclipse.birt.report.engine.api.script.instance.IReportElementInstance;
import org.eclipse.birt.report.engine.api.script.instance.IScriptStyle;
import org.eclipse.birt.report.engine.content.impl.AbstractContent;
import org.eclipse.birt.report.engine.ir.ReportElementDesign;
import org.eclipse.birt.report.engine.script.internal.ElementUtil;

public class ReportElementInstance implements IReportElementInstance
{

	protected AbstractContent content;

	public ReportElementInstance( AbstractContent content )
	{
		this.content = content;
	}

	protected ReportElementInstance( )
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

	public IReportElementInstance getParent( )
	{
		return ElementUtil.getInstance( content.getParent( ) );
	}

}
