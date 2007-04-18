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

package org.eclipse.birt.report.engine.script.internal.element;

import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.element.IDataItem;
import org.eclipse.birt.report.engine.api.script.element.IDataSet;
import org.eclipse.birt.report.engine.api.script.element.IDataSource;
import org.eclipse.birt.report.engine.api.script.element.IDynamicText;
import org.eclipse.birt.report.engine.api.script.element.IGrid;
import org.eclipse.birt.report.engine.api.script.element.IImage;
import org.eclipse.birt.report.engine.api.script.element.ILabel;
import org.eclipse.birt.report.engine.api.script.element.IList;
import org.eclipse.birt.report.engine.api.script.element.IReportDesign;
import org.eclipse.birt.report.engine.api.script.element.IReportElement;
import org.eclipse.birt.report.engine.api.script.element.ITable;
import org.eclipse.birt.report.engine.api.script.element.ITextItem;
import org.eclipse.birt.report.engine.script.internal.ElementUtil;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

public class ReportDesign extends DesignElement implements IReportDesign
{

	public ReportDesign(
			org.eclipse.birt.report.model.api.simpleapi.IReportDesign reportElementImpl )
	{
		super( null );
		designElementImpl = reportElementImpl;
	}

	public ReportDesign( ReportDesignHandle report )
	{
		super( report );
	}

	public IDataSet getDataSet( String name )
	{
		return new DataSet(
				( (org.eclipse.birt.report.model.api.simpleapi.IReportDesign) designElementImpl )
						.getDataSet( name ) );
	}

	public IDataSource getDataSource( String name )
	{
		return new DataSource(
				( (org.eclipse.birt.report.model.api.simpleapi.IReportDesign) designElementImpl )
						.getDataSource( name ) );
	}

	public IReportElement getReportElement( String name )
	{
		org.eclipse.birt.report.model.api.simpleapi.IReportElement tmpElement = ( (org.eclipse.birt.report.model.api.simpleapi.IReportDesign) designElementImpl )
				.getReportElement( name );

		return (IReportElement) ElementUtil.getElement( tmpElement );

	}

	public IDataItem getDataItem( String name )
	{
		return new DataItem(
				( (org.eclipse.birt.report.model.api.simpleapi.IReportDesign) designElementImpl )
						.getDataItem( name ) );
	}

	public IGrid getGrid( String name )
	{
		return new Grid(
				( (org.eclipse.birt.report.model.api.simpleapi.IReportDesign) designElementImpl )
						.getGrid( name ) );
	}

	public IImage getImage( String name )
	{
		return new Image(
				( (org.eclipse.birt.report.model.api.simpleapi.IReportDesign) designElementImpl )
						.getImage( name ) );
	}

	public ILabel getLabel( String name )
	{
		return new Label(
				( (org.eclipse.birt.report.model.api.simpleapi.IReportDesign) designElementImpl )
						.getLabel( name ) );
	}

	public IList getList( String name )
	{
		return new List(
				( (org.eclipse.birt.report.model.api.simpleapi.IReportDesign) designElementImpl )
						.getList( name ) );
	}

	public ITable getTable( String name )
	{
		return new Table(
				( (org.eclipse.birt.report.model.api.simpleapi.IReportDesign) designElementImpl )
						.getTable( name ) );
	}

	public IDynamicText getDynamicText( String name )
	{
		return new DynamicText(
				( (org.eclipse.birt.report.model.api.simpleapi.IReportDesign) designElementImpl )
						.getDynamicText( name ) );
	}

	public ITextItem getTextItem( String name )
	{
		return new TextItem(
				( (org.eclipse.birt.report.model.api.simpleapi.IReportDesign) designElementImpl )
						.getTextItem( name ) );
	}

	public void setDisplayNameKey( String displayNameKey )
			throws ScriptException
	{
		try
		{
			( (org.eclipse.birt.report.model.api.simpleapi.IReportDesign) designElementImpl )
					.setDisplayNameKey( displayNameKey );
		}
		catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	public String getDisplayNameKey( )
	{
		return ( (org.eclipse.birt.report.model.api.simpleapi.IReportDesign) designElementImpl )
				.getDisplayNameKey( );
	}

	public void setDisplayName( String displayName ) throws ScriptException
	{
		try
		{
			( (org.eclipse.birt.report.model.api.simpleapi.IReportDesign) designElementImpl )
					.setDisplayName( displayName );
		}
		catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	public String getDisplayName( )
	{
		return ( (org.eclipse.birt.report.model.api.simpleapi.IReportDesign) designElementImpl )
				.getDisplayName( );
	}
}
