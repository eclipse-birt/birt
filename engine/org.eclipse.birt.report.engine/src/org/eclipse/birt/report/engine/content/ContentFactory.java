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

import org.eclipse.birt.report.engine.api.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.impl.ActionContent;
import org.eclipse.birt.report.engine.content.impl.CellContent;
import org.eclipse.birt.report.engine.content.impl.ColumnContent;
import org.eclipse.birt.report.engine.content.impl.ColumnsContent;
import org.eclipse.birt.report.engine.content.impl.ContainerContent;
import org.eclipse.birt.report.engine.content.impl.ExtendedItemContent;
import org.eclipse.birt.report.engine.content.impl.ImageItemContent;
import org.eclipse.birt.report.engine.content.impl.MasterPageContent;
import org.eclipse.birt.report.engine.content.impl.PageSequenceContent;
import org.eclipse.birt.report.engine.content.impl.PageSetupContent;
import org.eclipse.birt.report.engine.content.impl.RowContent;
import org.eclipse.birt.report.engine.content.impl.TableBandContent;
import org.eclipse.birt.report.engine.content.impl.TableContent;
import org.eclipse.birt.report.engine.content.impl.TextItemContent;
import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.ColumnDesign;
import org.eclipse.birt.report.engine.ir.DataItemDesign;
import org.eclipse.birt.report.engine.ir.GridItemDesign;
import org.eclipse.birt.report.engine.ir.ImageItemDesign;
import org.eclipse.birt.report.engine.ir.LabelItemDesign;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;
import org.eclipse.birt.report.engine.ir.MultiLineItemDesign;
import org.eclipse.birt.report.engine.ir.PageSequenceDesign;
import org.eclipse.birt.report.engine.ir.PageSetupDesign;
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
 * @version $Revision$ $Date$
 */
public class ContentFactory
{

	/**
	 * Creates the Action Content
	 * 
	 * @param actionStr
	 * @return the action content
	 */
	public static IHyperlinkAction createActionContent( String actionStr )
	{
		return new ActionContent( actionStr );
	}

	/**
	 * Creates the Action Content
	 * 
	 * @param actionStr
	 *            the action string
	 * @param bookmark
	 *            the book-mark string
	 * @return the action content instance
	 */
	public static IHyperlinkAction createActionContent( String actionStr,
			String bookmark )
	{
		return new ActionContent( actionStr, bookmark );
	}

	/**
	 * Creates the Cell Content
	 * 
	 * @param design
	 *            the Cell Design
	 * @return the Cell Content instance
	 */
	public static ICellContent createCellContent( CellDesign design )
	{
		return new CellContent( design );
	}

	/**
	 * Creates the column content
	 * 
	 * @param design
	 *            the column design
	 * @return the column content instance
	 */
	public static IColumnContent createColumnContent( ColumnDesign design )
	{
		return new ColumnContent( design );
	}

	/**
	 * Creates the columns content
	 * 
	 * @return the columns content instance
	 */
	public static IColumnsContent createColumnsContent( )
	{
		return new ColumnsContent( );
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
	 * @return the container content instance
	 */
	public static IContainerContent createContainerContent(
			ReportItemDesign design )
	{
		return new ContainerContent( design );
	}

	/**
	 * Creates the extended item content object
	 * 
	 * @return the instance
	 */
	public static IExtendedItemContent createExtendedItemContent( )
	{
		return new ExtendedItemContent( );
	}

	/**
	 * Creates the image content object
	 * 
	 * @param design
	 *            the image design
	 * @return the instance
	 */
	public static IImageItemContent createImageContent( ImageItemDesign design )
	{
		return new ImageItemContent( design );
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
	 * @return the instance
	 */
	public static IRowContent createRowContent( RowDesign design )
	{
		return new RowContent( design );
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
	 * @return the instance
	 */
	public static ITableContent createTableContent( TableItemDesign design )
	{
		return new TableContent( design );
	}

	/**
	 * Creates the table content object
	 * 
	 * @param design
	 *            the grid design
	 * @return the instance
	 */
	public static ITableContent createTableContent( GridItemDesign design )
	{
		return new TableContent( design );
	}

	/**
	 * Creates the Text content object
	 * 
	 * @param design
	 *            the TextItemDesign
	 * 
	 * @return the instance
	 */
	public static ITextContent createTextContent( TextItemDesign design )
	{
		return new TextItemContent( design );
	}

	/**
	 * Creates the Text Content object
	 * 
	 * @param design
	 *            the DataItemDesign
	 * @return the instance
	 */
	public static ITextContent createTextContent( DataItemDesign design )
	{
		return new TextItemContent( design );
	}

	/**
	 * Creates the Text Content object
	 * 
	 * @param design
	 *            the LabelItemDesign
	 * @return the instance
	 */
	public static ITextContent createTextContent( LabelItemDesign design )
	{
		return new TextItemContent( design );
	}

	/**
	 * Creates the Text content object
	 * 
	 * @param design
	 *            the MultiLineItemDesign
	 * @return the instance
	 */
	public static ITextContent createTextContent( MultiLineItemDesign design )
	{
		return new TextItemContent( design );
	}
}