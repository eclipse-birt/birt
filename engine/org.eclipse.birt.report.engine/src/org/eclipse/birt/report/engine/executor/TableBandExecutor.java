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

package org.eclipse.birt.report.engine.executor;

import org.eclipse.birt.report.engine.api.DataID;
import org.eclipse.birt.report.engine.api.DataSetID;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.ir.RowDesign;
import org.eclipse.birt.report.engine.ir.TableBandDesign;
import org.w3c.dom.css.CSSValue;

public class TableBandExecutor extends StyledItemExecutor
{

	protected TableBandExecutor( ExecutorManager manager )
	{
		super( manager );
	}
	
	protected DataID getDataID( )
	{
		//FIXME: overide the getDataId from the reportItemExecutor.
		IQueryResultSet curRset = getResultSet( );
		if ( curRset == null )
		{
			curRset = (IQueryResultSet) getParentResultSet( );
		}
		if ( curRset != null )
		{
			DataSetID dataSetID = curRset.getID( );
			long position = curRset.getRowIndex( );
			return new DataID( dataSetID, position );
		}		
		return null;
	}

	public IContent execute( )
	{
		// start table band
		TableBandDesign bandDesign = (TableBandDesign) getDesign( );
		ITableBandContent bandContent = report.createTableBandContent( );
		setContent(bandContent);

		restoreResultSet( );
		
		initializeContent( bandDesign, bandContent );
		int type = bandDesign.getBandType( );
		if((type == TableBandDesign.BAND_DETAIL || type == TableBandDesign.GROUP_HEADER )&& tableExecutor.needSoftBreakBefore( ))
		{
			IStyle style = content.getStyle( );
			if(style!=null)
			{
				CSSValue pageBreak = style.getProperty(IStyle.STYLE_PAGE_BREAK_BEFORE);
				if(pageBreak==null || IStyle.AUTO_VALUE.equals( pageBreak ))
				{
					style.setProperty( IStyle.STYLE_PAGE_BREAK_BEFORE, IStyle.SOFT_VALUE );
				}
			}
		}
		startTOCEntry( bandContent );
		if (emitter != null)
		{
			emitter.startTableBand( bandContent );
		}
		
		// prepare to execute the row in the band
		currentRow = 0;

		return content;
	}

	public void close( )
	{
		ITableBandContent bandContent = (ITableBandContent)getContent();
		if (emitter != null)
		{
			emitter.endTableBand( bandContent );
		}
		finishTOCEntry( );
		manager.releaseExecutor( ExecutorManager.TABLEBANDITEM, this );
	}

	int currentRow;

	public boolean hasNextChild( )
	{
		TableBandDesign bandDesign = (TableBandDesign) getDesign( );
		return currentRow < bandDesign.getRowCount( );
	}

	public IReportItemExecutor getNextChild( )
	{
		TableBandDesign bandDesign = (TableBandDesign) getDesign( );
		//TableItemExecutor tableExecutor = (TableItemExecutor) getParent( );

		if ( currentRow < bandDesign.getRowCount( ) )
		{
			RowDesign rowDesign = bandDesign.getRow( currentRow++ );
			RowExecutor rowExecutor = (RowExecutor) manager.createExecutor(
					this, rowDesign );
			rowExecutor.setRowId( tableExecutor.rowId++ );
			return rowExecutor;
		}
		return null;
	}

	TableItemExecutor tableExecutor;

	void setTableExecutor( TableItemExecutor tableExecutor )
	{
		this.tableExecutor = tableExecutor;
	}
}