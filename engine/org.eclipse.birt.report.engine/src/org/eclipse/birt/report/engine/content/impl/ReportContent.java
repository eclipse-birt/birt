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
 * Report is the root element of the design.
 * 
 * @version $Revision: 1.7 $ $Date: 2005/11/14 10:55:58 $
 */
public class ReportContent implements IReportContent
{

	private CSSEngine cssEngine;
	private Report report;
	ArrayList errors = new ArrayList( );

	/**
	 * default constructor.
	 */
	public ReportContent( Report report )
	{
		cssEngine = BIRTCSSEngine.getInstance( );
		this.report = report;
	}

	public ReportContent( )
	{
		cssEngine = BIRTCSSEngine.getInstance( );
	}

	public Report getDesign( )
	{
		return report;
	}

	public IStyle findStyle( String styleClass )
	{
		if ( report != null )
		{
			return (IStyle) report.findStyle( styleClass );
		}
		return null;
	}

	public CSSEngine getCSSEngine( )
	{
		return this.cssEngine;
	}

	/**
	 * Creates the Action Content
	 * 
	 * @param hyperlink
	 *            the hyper link string
	 * @param target
	 *            the target frame
	 * @return the action content instance
	 */
	public IHyperlinkAction createActionContent( )
	{
		return new ActionContent( );
	}

	public IStyle createStyle( )
	{
		return new StyleDeclaration( cssEngine );
	}

	/**
	 * Creates the Cell Content
	 * 
	 * @param design
	 *            the Cell Design
	 * @param parentNode
	 *            the parent content object
	 * @return the Cell Content instance
	 */
	public ICellContent createCellContent( )
	{
		return new CellContent( this );
	}

	/**
	 * Creates the Container Content
	 * 
	 * @param design
	 *            the report item design
	 * @param parent
	 *            the parent content object
	 * @return the container content instance
	 */
	public IContainerContent createContainerContent( )
	{
		return new ContainerContent( this );
	}

	/**
	 * Creates the Page setup content object
	 * 
	 * @param design
	 *            the page setup design
	 * @return the instance
	 */
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

	/**
	 * Creates the Row Content object
	 * 
	 * @param design
	 *            the RowDesign
	 * @param parent
	 *            the parent content object
	 * @return the instance
	 */

	public IRowContent createRowContent( )
	{
		return new RowContent( this );
	}

	public ITableContent createTableContent( )
	{
		return new TableContent( this );
	}

	/**
	 * Creates the Text content object
	 * 
	 * @param design
	 *            the TextItemDesign
	 * @param parent
	 *            the parent content object
	 * 
	 * @return the instance
	 */
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

	/**
	 * Creates the extended item content object
	 * 
	 * @return the instance
	 */
	public IForeignContent createForeignContent( )
	{
		return new ForeignContent( this );
	}

	public IForeignContent createForeignContent( IContent content )
	{
		return new ForeignContent( content );
	}

	/**
	 * Creates the image content object
	 * 
	 * @param design
	 *            the image design, or extened item design
	 * @param parent
	 *            the parent content object
	 * @return the instance
	 */
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
}