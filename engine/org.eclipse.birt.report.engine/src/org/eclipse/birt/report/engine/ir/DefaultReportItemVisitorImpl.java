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

package org.eclipse.birt.report.engine.ir;


/**
 * Default implementation for IReportItemVisitor interface
 * 
 * @version $Revision: 1.1 $ $Date: 2005/02/10 23:45:35 $
 */
public class DefaultReportItemVisitorImpl implements IReportItemVisitor
{

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.ir.IReportItemVisitor#visitFreeFormItem(org.eclipse.birt.report.engine.ir.FreeFormItemDesign)
	 */
	public void visitFreeFormItem( FreeFormItemDesign container )
	{
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.ir.IReportItemVisitor#visitListItem(org.eclipse.birt.report.engine.ir.ListItemDesign)
	 */
	public void visitListItem( ListItemDesign list )
	{
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.ir.IReportItemVisitor#visitTextItem(org.eclipse.birt.report.engine.ir.TextItemDesign)
	 */
	public void visitTextItem( TextItemDesign text )
	{
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.ir.IReportItemVisitor#visitLabelItem(org.eclipse.birt.report.engine.ir.LabelItemDesign)
	 */
	public void visitLabelItem( LabelItemDesign label )
	{
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.ir.IReportItemVisitor#visitDataItem(org.eclipse.birt.report.engine.ir.DataItemDesign)
	 */
	public void visitDataItem( DataItemDesign data )
	{
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.ir.IReportItemVisitor#visitMultiLineItem(org.eclipse.birt.report.engine.ir.MultiLineItemDesign)
	 */
	public void visitMultiLineItem( MultiLineItemDesign multiLine )
	{
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.ir.IReportItemVisitor#visitGridItem(org.eclipse.birt.report.engine.ir.GridItemDesign)
	 */
	public void visitGridItem( GridItemDesign grid )
	{
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.ir.IReportItemVisitor#visitTableItem(org.eclipse.birt.report.engine.ir.TableItemDesign)
	 */
	public void visitTableItem( TableItemDesign table )
	{
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.ir.IReportItemVisitor#visitImageItem(org.eclipse.birt.report.engine.ir.ImageItemDesign)
	 */
	public void visitImageItem( ImageItemDesign image )
	{
		visitReportItem(image);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.ir.IReportItemVisitor#visitExtendedItem(org.eclipse.birt.report.engine.ir.ExtendedItemDesign)
	 */
	public void visitExtendedItem( ExtendedItemDesign item )
	{
	}
	
	/**
	 * @param item the report item
	 */
	public void visitReportItem(ReportItemDesign item)
	{
		item.accept(this);
	}
}
