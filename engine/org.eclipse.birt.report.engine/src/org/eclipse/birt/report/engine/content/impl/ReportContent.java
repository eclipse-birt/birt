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

import org.eclipse.birt.report.engine.api.ITOCTree;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.api.TOCNode;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IListBandContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.IListGroupContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITableGroupContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.css.dom.StyleDeclaration;
import org.eclipse.birt.report.engine.css.engine.BIRTCSSEngine;
import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.toc.ITreeNode;
import org.eclipse.birt.report.engine.toc.TOCView;

import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

/**
 * Report content is the result of report generation.
 * 
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
	private ITreeNode tocTree;
	
	private IContent root;
	
	private long totalPage;

	protected String acl;
	
	private IReportContext reportContext;

	private ExecutionContext executionContext;
	
	/**
	 * default constructor.
	 */
	public ReportContent( Report report )
	{
		cssEngine = report.getCSSEngine( );
		this.report = report;
		this.root = createContainerContent();
		this.root.setStyleClass( report.getRootStyleName( ) );
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

	public IContent getRoot()
	{
		return this.root;
	}
	
	public IPageContent getPageContent(long pageNumber)
	{
		return null;
	}
	
	
	/**
	 * @return the pageNumber
	 */
	public long getTotalPage( )
	{
		return totalPage;
	}

	
	/**
	 * @param pageNumber the pageNumber to set
	 */
	public void setTotalPage( long totalPage )
	{
		this.totalPage = totalPage;
	}

	public IContent getContent(InstanceID id)
	{
		return null;
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

	public IRowContent createRowContent( )
	{
		return new RowContent( this );
	}

	public IListContent createListContent( )
	{
		return new ListContent( this );
	}

	public IListGroupContent createListGroupContent( )
	{
		return new ListGroupContent( this );
	}
	
	public IListBandContent createListBandContent( )
	{
		return new ListBandContent( this );
	}
	
	public ITableContent createTableContent( )
	{
		return new TableContent( this );
	}
	
	public ITableGroupContent createTableGroupContent( )
	{
		return new TableGroupContent( this );
	}

	public ITableBandContent createTableBandContent( )
	{
		return new TableBandContent( this );
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
	
	public IAutoTextContent createAutoTextContent( )
	{
		return new AutoTextContent( this );
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
	
	public IImageContent createObjectContent( )
	{
		return new ObjectContent( this );
	}

	public List getErrors( )
	{
		return errors;
	}

	public ITOCTree getTOCTree( String format, ULocale locale )
	{
		if ( tocTree == null )
		{
			return null;
		}
		return new TOCView( tocTree, report.getReportDesign( ), locale,
				TimeZone.getDefault( ) );
	}
	
	public void setTOCTree( ITreeNode tocTree )
	{
		this.tocTree = tocTree;
	}

	public TOCNode getTOC( )
	{
		return getTOCTree( "viewer", ULocale.getDefault( ) ).getRoot( );
	}
	
	public String getACL( )
	{
		return acl;
	}

	public void setACL( String acl )
	{
		this.acl = acl;
	}
	
	public IReportContext getReportContext( )
	{
		return reportContext;
	}
	
	public void setReportContext( IReportContext context )
	{
		this.reportContext = context;
	}

	public ExecutionContext getExecutionContext( )
	{
		return executionContext;
	}

	public void setExecutionContext( ExecutionContext executionContext )
	{
		this.executionContext = executionContext;
	}
}