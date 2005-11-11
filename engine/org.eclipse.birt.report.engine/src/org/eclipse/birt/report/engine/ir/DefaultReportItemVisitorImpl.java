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
 * Default implementation for IReportItemVisitor interface
 * 
 * @version $Revision: 1.3 $ $Date: 2005/10/27 02:16:33 $
 */
public class DefaultReportItemVisitorImpl implements IReportItemVisitor
{

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.ir.IReportItemVisitor#visitFreeFormItem(org.eclipse.birt.report.engine.ir.FreeFormItemDesign)
	 */
	public void visitFreeFormItem( FreeFormItemDesign container, Object value )
	{
		visitReportItem(container, value);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.ir.IReportItemVisitor#visitListItem(org.eclipse.birt.report.engine.ir.ListItemDesign)
	 */
	public void visitListItem( ListItemDesign list , Object value)
	{
		visitReportItem(list, value);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.ir.IReportItemVisitor#visitTextItem(org.eclipse.birt.report.engine.ir.TextItemDesign)
	 */
	public void visitTextItem( TextItemDesign text , Object value)
	{
		visitReportItem(text, value);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.ir.IReportItemVisitor#visitLabelItem(org.eclipse.birt.report.engine.ir.LabelItemDesign)
	 */
	public void visitLabelItem( LabelItemDesign label , Object value)
	{
		visitReportItem(label, value);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.ir.IReportItemVisitor#visitDataItem(org.eclipse.birt.report.engine.ir.DataItemDesign)
	 */
	public void visitDataItem( DataItemDesign data , Object value)
	{
		visitReportItem(data, value);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.ir.IReportItemVisitor#visitMultiLineItem(org.eclipse.birt.report.engine.ir.MultiLineItemDesign)
	 */
	public void visitMultiLineItem( MultiLineItemDesign multiLine , Object value)
	{
		visitReportItem(multiLine, value);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.ir.IReportItemVisitor#visitGridItem(org.eclipse.birt.report.engine.ir.GridItemDesign)
	 */
	public void visitGridItem( GridItemDesign grid , Object value)
	{
		visitReportItem(grid, value);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.ir.IReportItemVisitor#visitTableItem(org.eclipse.birt.report.engine.ir.TableItemDesign)
	 */
	public void visitTableItem( TableItemDesign table , Object value)
	{
		visitReportItem(table, value);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.ir.IReportItemVisitor#visitImageItem(org.eclipse.birt.report.engine.ir.ImageItemDesign)
	 */
	public void visitImageItem( ImageItemDesign image , Object value)
	{
		visitReportItem(image, value);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.ir.IReportItemVisitor#visitExtendedItem(org.eclipse.birt.report.engine.ir.ExtendedItemDesign)
	 */
	public void visitExtendedItem( ExtendedItemDesign item , Object value)
	{
		visitReportItem(item, value);
	}
	
	/**
	 * @param item the report item
	 */
	public void visitReportItem(ReportItemDesign item, Object value)
	{
	}
	
	public void visitRow(RowDesign row, Object value)
	{
		visitReportItem(row, value);
	}
	
	public void visitCell(CellDesign cell, Object value)
	{
		visitReportItem(cell, value);
	}
}
