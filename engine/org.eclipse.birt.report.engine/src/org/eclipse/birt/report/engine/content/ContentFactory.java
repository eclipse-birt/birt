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

package org.eclipse.birt.report.engine.content;

import java.util.Map;

import org.eclipse.birt.report.engine.content.impl.ActionContent;
import org.eclipse.birt.report.engine.content.impl.CellContent;
import org.eclipse.birt.report.engine.content.impl.ColumnContent;
import org.eclipse.birt.report.engine.content.impl.ContainerContent;
import org.eclipse.birt.report.engine.content.impl.ExtendedItemContent;
import org.eclipse.birt.report.engine.content.impl.ImageItemContent;
import org.eclipse.birt.report.engine.content.impl.MasterPageContent;
import org.eclipse.birt.report.engine.content.impl.PageSequenceContent;
import org.eclipse.birt.report.engine.content.impl.PageSetupContent;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.engine.content.impl.RowContent;
import org.eclipse.birt.report.engine.content.impl.TableBandContent;
import org.eclipse.birt.report.engine.content.impl.TableContent;
import org.eclipse.birt.report.engine.content.impl.TextItemContent;
import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.ColumnDesign;
import org.eclipse.birt.report.engine.ir.DataItemDesign;
import org.eclipse.birt.report.engine.ir.ExtendedItemDesign;
import org.eclipse.birt.report.engine.ir.GridItemDesign;
import org.eclipse.birt.report.engine.ir.LabelItemDesign;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;
import org.eclipse.birt.report.engine.ir.MultiLineItemDesign;
import org.eclipse.birt.report.engine.ir.PageSequenceDesign;
import org.eclipse.birt.report.engine.ir.PageSetupDesign;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.RowDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;
import org.eclipse.birt.report.engine.ir.TextItemDesign;

/**
 * Creates the content objects.
 * <p>
 * In any case, the user gets the two different content object for any two
 * calls.
 * 
 * @version $Revision: 1.6 $ $Date: 2005/04/08 05:21:07 $
 */
public class ContentFactory
{

	/**
	 * Creates the Action Content
	 * 
	 * @param hyperlink
	 *            the hyper link string
	 * @param target
	 *            the target frame
	 * @return the action content instance
	 */
	public static IHyperlinkAction createActionContent( String hyperlink,
			String target )
	{
		return new ActionContent( hyperlink, target );
	}

	/**
	 * Creates the Action Content
	 * 
	 * @param bookmark
	 *            the book-mark string
	 * @return the action content instance
	 */
	public static IHyperlinkAction createActionContent( String bookmark )
	{
		return new ActionContent( bookmark );
	}

	/**
	 * Creates the Action Content
	 * 
	 * @param bookmark
	 *            the bookmark string
	 * @param reportName
	 *            the report name navigated
	 * @param parameterBindings
	 *            the parameters of the report navigated
	 * @param searchCriteria
	 *            the search criteria
	 */
	public static IHyperlinkAction createActionContent( String bookmark,
			String reportName, Map parameterBindings, Map searchCriteria,
			String target )
	{
		return new ActionContent( bookmark, reportName, parameterBindings,
				searchCriteria, target );
	}

	/**
	 * Creates the Cell Content
	 * 
	 * @param design
	 *            the Cell Design
	 * @param parent the parent content object
	 * @return the Cell Content instance
	 */
	public static ICellContent createCellContent( CellDesign design, IReportElementContent parent )
	{
		return new CellContent( design, parent );
	}

	/**
	 * Creates the column content
	 * 
	 * @param design
	 *            the column design
	 * @param parent the parent content object
	 * @return the column content instance
	 */
	public static IColumnContent createColumnContent( ColumnDesign design, IReportElementContent parent )
	{
		return new ColumnContent( design, parent );
	}

	/**
	 * Creates the Container content
	 * 
	 * @return the container content instance
	 */
	public static IContainerContent createContainerContent( )
	{
		return new ContainerContent( );
	}

	/**
	 * Creates the Container Content
	 * 
	 * @param design
	 *            the report item design
	 * @param parent the parent content object
	 * @return the container content instance
	 */
	public static IContainerContent createContainerContent(
			ReportItemDesign design, IReportElementContent parent )
	{
		return new ContainerContent( design, parent );
	}

	/**
	 * Creates the extended item content object
	 * 
	 * @return the instance
	 */
	public static IExtendedItemContent createExtendedItemContent(
			ExtendedItemDesign design, IReportElementContent parent )
	{
		return new ExtendedItemContent( design, parent );
	}

	/**
	 * Creates the image content object
	 * 
	 * @param design
	 *            the image design, or extened item design
	 * @param parent the parent content object
	 * @return the instance
	 */
	public static IImageItemContent createImageContent( ReportItemDesign design, IReportElementContent parent )
	{
		return new ImageItemContent( design, parent );
	}

	/**
	 * Creates the Master page Content object
	 * 
	 * @param design
	 *            the master page design
	 * @return
	 */
	public static IMasterPageContent createMasterPageContent(
			MasterPageDesign design )
	{
		return new MasterPageContent( design );
	}

	/**
	 * Creates the page sequence content
	 * 
	 * @param pageSetup
	 *            the page setup content object
	 * @param design
	 *            the page sequence design
	 * @return the instance
	 */
	public static IPageSequenceContent createPageSequenceContent(
			IPageSetupContent pageSetup, PageSequenceDesign design )
	{
		return new PageSequenceContent( pageSetup, design );
	}

	/**
	 * Creates the Page setup content object
	 * 
	 * @param design
	 *            the page setup design
	 * @return the instance
	 */
	public static IPageSetupContent createPageSetupContent(
			PageSetupDesign design )
	{
		return new PageSetupContent( design );
	}

	/**
	 * Creates the Row Content object
	 * 
	 * @param design
	 *            the RowDesign
	 * @param parent the parent content object
	 * @return the instance
	 */
	public static IRowContent createRowContent( RowDesign design, IReportElementContent parent )
	{
		return new RowContent( design, parent );
	}

	/**
	 * Creates the table band content object
	 * 
	 * @param type
	 *            the type of the band
	 * @return the instance
	 */
	public static ITableBandContent createTableBandContent( int type )
	{
		return new TableBandContent( type );
	}

	/**
	 * Creates the table content
	 * 
	 * @param design
	 *            the table design
	 * @param parent the parent content object
	 * @return the instance
	 */
	public static ITableContent createTableContent( TableItemDesign design, IReportElementContent parent )
	{
		return new TableContent( design, parent );
	}

	/**
	 * Creates the table content object
	 * 
	 * @param design
	 *            the grid design
	 * @param parent the parent content object
	 * @return the instance
	 */
	public static ITableContent createTableContent( GridItemDesign design, IReportElementContent parent )
	{
		return new TableContent( design, parent );
	}

	/**
	 * Creates the Text content object
	 * 
	 * @param design
	 *            the TextItemDesign
	 * @param parent the parent content object
	 * 
	 * @return the instance
	 */
	public static ITextContent createTextContent( TextItemDesign design, IReportElementContent parent )
	{
		return new TextItemContent( design, parent );
	}

	/**
	 * Creates the Text Content object
	 * 
	 * @param design
	 *            the DataItemDesign
	 * @param parent the parent content object
	 * @return the instance
	 */
	public static ITextContent createTextContent( DataItemDesign design, IReportElementContent parent )
	{
		return new TextItemContent( design, parent );
	}

	/**
	 * Creates the Text Content object
	 * 
	 * @param design
	 *            the LabelItemDesign
	 * @param parent the parent content object
	 * @return the instance
	 */
	public static ITextContent createTextContent( LabelItemDesign design, IReportElementContent parent )
	{
		return new TextItemContent( design, parent );
	}

	/**
	 * Creates the Text content object
	 * 
	 * @param design
	 *            the MultiLineItemDesign
	 * @param parent the parent content object
	 * @return the instance
	 */
	public static ITextContent createTextContent( MultiLineItemDesign design, IReportElementContent parent )
	{
		return new TextItemContent( design,  parent );
	}
	
	/**
	 * Creates the Report content object
	 * @param design the Report
	 * @param parent the parent content object
	 * @return the instance
	 */
	public static IReportContent createReportContent( Report design, IReportElementContent parent )
	{
		return new ReportContent( design, parent );
	}
}