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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.engine.api.TOCNode;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.css.dom.StyleDeclaration;
import org.eclipse.birt.report.engine.css.engine.BIRTCSSEngine;
import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.eclipse.birt.report.engine.ir.Report;

/**
 * Report content is the result of report generation.
 * 
 * @version $Revision: 1.10 $ $Date: 2005/12/02 11:57:06 $
 */
public class ReportContent implements IReportContent
{

	/**
	 * css engine used by this report.
	 */
	private CSSEngine cssEngine;
	/**
	 * report design used to create this report.
	 */
	private Report report;
	/**
	 * errors occured in the generation.
	 */
	private ArrayList errors = new ArrayList( );

	/**
	 * toc of this report
	 */
	private TOCNode tocRoot;

	/**
	 * default constructor.
	 */
	public ReportContent( Report report )
	{
		cssEngine = report.getCSSEngine( );
		this.report = report;
	}

	/**
	 * default constructor.
	 */
	public ReportContent( )
	{
		cssEngine = new BIRTCSSEngine( );
	}

	public Report getDesign( )
	{
		return report;
	}

	public IStyle findStyle( String styleClass )
	{
		return ( report == null ) ? null : report.findStyle( styleClass );
	}

	/**
	 * get the css engine used in the report.
	 * 
	 * @return css engine
	 */
	public CSSEngine getCSSEngine( )
	{
		return cssEngine;
	}

	public IHyperlinkAction createActionContent( )
	{
		return new ActionContent( );
	}

	public IStyle createStyle( )
	{
		return new StyleDeclaration( cssEngine );
	}

	public ICellContent createCellContent( )
	{
		return new CellContent( this );
	}

	public IContainerContent createContainerContent( )
	{
		return new ContainerContent( this );
	}

	public IPageContent createPageContent( )
	{
		return new PageContent( this );
	}

	public ITableBandContent createTableHeader( )
	{
		TableBandContent band = new TableBandContent( this );
		band.setType( ITableBandContent.BAND_HEADER );
		return band;
	}

	public ITableBandContent createTableBody( )
	{
		TableBandContent band = new TableBandContent( this );
		band.setType( ITableBandContent.BAND_BODY );
		return band;
	}

	public ITableBandContent createTableFooter( )
	{
		TableBandContent band = new TableBandContent( this );
		band.setType( ITableBandContent.BAND_FOOTER );
		return band;
	}

	public IRowContent createRowContent( )
	{
		return new RowContent( this );
	}

	public ITableContent createTableContent( )
	{
		return new TableContent( this );
	}

	public ITextContent createTextContent( )
	{
		return new TextContent( this );
	}

	public ITextContent createTextContent( IContent content )
	{
		return new TextContent( content );
	}

	public IDataContent createDataContent( )
	{
		return new DataContent( this );
	}

	public IDataContent createDataContent( IContent content )
	{
		return new DataContent( content );
	}

	public ILabelContent createLabelContent( )
	{
		return new LabelContent( this );
	}

	public ILabelContent createLabelContent( IContent content )
	{
		return new LabelContent( content );
	}

	public IForeignContent createForeignContent( )
	{
		return new ForeignContent( this );
	}

	public IForeignContent createForeignContent( IContent content )
	{
		return new ForeignContent( content );
	}

	public IImageContent createImageContent( )
	{
		return new ImageContent( this );
	}

	public IImageContent createImageContent( IContent content )
	{
		return new ImageContent( content );
	}

	public List getErrors( )
	{
		return errors;
	}

	public TOCNode getTOC( )
	{
		if ( tocRoot == null )
		{
			tocRoot = new TOCNode( );
		}
		return tocRoot;
	}

	public void setTOC( TOCNode root )
	{
		this.tocRoot = root;
	}
}