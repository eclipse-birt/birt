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

import java.util.List;

import org.eclipse.birt.report.engine.ir.Report;

/**
 * The object represents the report content as a whole.
 * 
 * @version $Revision: 1.5 $ $Date: 2005/05/20 03:39:29 $
 */
public interface IReportContent
{

	public Report getDesign( );
	
	public List getErrors();

	public IStyle findStyle( String styleClass );

	public IHyperlinkAction createActionContent( );

	public IStyle createStyle( );

	public ICellContent createCellContent( );

	public IContainerContent createContainerContent( );

	public IPageContent createPageContent( );

	public ITableContent createTableContent( );

	public ITableBandContent createTableBandContent( );

	public IRowContent createRowContent( );

	public ITextContent createTextContent( );
	public ITextContent createTextContent(IContent conent);

	public IForeignContent createForeignContent( );

	public IImageContent createImageContent( );
	public IImageContent createImageContent(IContent conent);

	public ILabelContent createLabelContent( );
	public ILabelContent createLabelContent(IContent conent);

	public IDataContent createDataContent( );
	
	public IDataContent createDataContent(IContent conent);

}