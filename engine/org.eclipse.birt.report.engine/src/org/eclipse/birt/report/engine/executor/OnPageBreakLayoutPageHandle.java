
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.executor;

import java.util.List;

import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.content.impl.ListGroupContent;
import org.eclipse.birt.report.engine.content.impl.TableGroupContent;
import org.eclipse.birt.report.engine.data.IResultSet;
import org.eclipse.birt.report.engine.ir.GridItemDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;
import org.eclipse.birt.report.engine.layout.ILayoutPageHandler;
import org.eclipse.birt.report.engine.script.internal.CellScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.DataItemScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.DynamicTextScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.GridScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.ImageScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.LabelScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.ListGroupScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.ListScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.RowScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.TableGroupScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.TableScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.TextItemScriptExecutor;

/**
 * 
 */

public class OnPageBreakLayoutPageHandle implements ILayoutPageHandler
{
	
	ExecutionContext executionContext;
	
	public OnPageBreakLayoutPageHandle( ExecutionContext executionContext )
	{
		this.executionContext = executionContext;
	}

	public void onPage( long page, Object context )
	{
		if ( executionContext == null )
		{
			return;
		}

		List openningContents = executionContext.getAllOnPageBreaks( );
		IResultSet currentRset = executionContext.getResultSet( );
		for ( int i = 0; i < openningContents.size( ); i++ )
		{
			Object[] pageBreakEvent = (Object[]) openningContents.get( i );
			IResultSet rset = (IResultSet) pageBreakEvent[0];
			executionContext.setResultSet( rset );
			IContent content = (IContent) pageBreakEvent[1];
			ReportItemDesign design = (ReportItemDesign) content
					.getGenerateBy( );
			int contentType = content.getContentType( );
			switch ( contentType )
			{
				case IContent.CELL_CONTENT :
					CellScriptExecutor.handleOnPageBreak(
							(ICellContent) content, executionContext );
					break;
				case IContent.DATA_CONTENT :
					DataItemScriptExecutor.handleOnPageBreak(
							(IDataContent) content, executionContext );
					break;
				case IContent.FOREIGN_CONTENT :
					DynamicTextScriptExecutor.handleOnPageBreak(
							(IForeignContent) content, executionContext );
					break;
				case IContent.IMAGE_CONTENT :
					ImageScriptExecutor.handleOnPageBreak(
							(IImageContent) content, executionContext );
					break;
				case IContent.LABEL_CONTENT :
					LabelScriptExecutor.handleOnPageBreak(
							(ILabelContent) content, executionContext );
					break;
				case IContent.ROW_CONTENT :
					RowScriptExecutor.handleOnPageBreak( (IRowContent) content,
							executionContext );
					break;
				case IContent.LIST_CONTENT :
					ListScriptExecutor.handleOnPageBreak(
							(IListContent) content, executionContext );
					break;
				case IContent.TABLE_CONTENT :
					if ( design instanceof TableItemDesign )
					{
						TableScriptExecutor.handleOnPageBreak(
								(ITableContent) content, executionContext );
					}
					else if ( design instanceof GridItemDesign )
					{
						GridScriptExecutor.handleOnPageBreak(
								(ITableContent) content, executionContext );
					}
					break;
				case IContent.TABLE_GROUP_CONTENT :
					TableGroupScriptExecutor.handleOnPageBreak(
							(TableGroupContent) content, executionContext );
					break;
				case IContent.LIST_GROUP_CONTENT :
					ListGroupScriptExecutor.handleOnPageBreak(
							(ListGroupContent) content, executionContext );
					break;
				case IContent.TEXT_CONTENT :
					TextItemScriptExecutor.handleOnPageBreak(
							(ITextContent) content, executionContext );
					break;
			}
		}
		executionContext.setResultSet( currentRset );
	}

}
