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

package org.eclipse.birt.report.engine.emitter;

import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IListBandContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.IListGroupContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITableGroupContent;
import org.eclipse.birt.report.engine.content.ITextContent;

/**
 * used to pass the content object to following process.
 * 
 */
public interface IContentEmitter
{

	String getOutputFormat( );

	void initialize( IEmitterServices service );

	void start( IReportContent report );

	void end( IReportContent report );

	/**
	 * start a page
	 * 
	 * @param page
	 */
	void startPage( IPageContent page );
	
	/**
	 * page end
	 * 
	 * @param page
	 */
	void endPage( IPageContent page );

	/**
	 * table started
	 * 
	 * @param table
	 */
	void startTable( ITableContent table );

	/**
	 * table end
	 */
	void endTable( ITableContent table );

	void startTableBand( ITableBandContent band );

	void endTableBand( ITableBandContent band );

	void startRow( IRowContent row );

	void endRow( IRowContent row );

	void startCell( ICellContent cell );

	void endCell( ICellContent cell );
	
	void startList( IListContent list );

	void endList( IListContent list );

	void startListBand( IListBandContent listBand );

	void endListBand( IListBandContent listBand );

	void startContainer( IContainerContent container );

	void endContainer( IContainerContent container );

	void startText( ITextContent text );

	void startData( IDataContent data );

	void startLabel( ILabelContent label );
	
	void startAutoText ( IAutoTextContent autoText );

	void startForeign( IForeignContent foreign );

	void startImage( IImageContent image );

	void startContent( IContent content );
	void endContent( IContent content);
	
	void startGroup( IGroupContent group );

	void endGroup( IGroupContent group );

	void startTableGroup( ITableGroupContent group );

	void endTableGroup( ITableGroupContent group );

	void startListGroup( IListGroupContent group );

	void endListGroup( IListGroupContent group );
}