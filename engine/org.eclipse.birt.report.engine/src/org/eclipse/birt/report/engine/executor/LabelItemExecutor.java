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

import org.eclipse.birt.report.engine.content.ContentFactory;
import org.eclipse.birt.report.engine.content.impl.TextItemContent;
import org.eclipse.birt.report.engine.emitter.IReportEmitter;
import org.eclipse.birt.report.engine.emitter.IReportItemEmitter;
import org.eclipse.birt.report.engine.ir.LabelItemDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;

/**
 * the labelItem excutor
 * 
 * @version $Revision: 1.5 $ $Date: 2005/03/15 03:29:36 $
 */
public class LabelItemExecutor extends StyledItemExecutor
{

	/**
	 * constructor
	 * 
	 * @param context
	 *            the excutor context
	 * @param visitor
	 *            the report executor visitor
	 */
	public LabelItemExecutor( ExecutionContext context,
			ReportExecutorVisitor visitor )
	{
		super( context, visitor );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.executor.ReportItemExcutor#execute()
	 */
	public void execute( ReportItemDesign item, IReportEmitter emitter )
	{
		LabelItemDesign labelItem = ( LabelItemDesign ) item;
		TextItemContent textObj =(TextItemContent) ContentFactory
				.createTextContent( labelItem, context.getContentObject( ) );
		textObj
				.setHelpText(
						getLocalizedString( labelItem.getHelpTextKey( ), labelItem.getHelpText( ) ) );
		textObj.setValue( getLocalizedString( labelItem.getTextKey( ), labelItem.getText( ) ) );
		setStyles( textObj, item );
		setVisibility( item, textObj );
		IReportItemEmitter textEmitter = emitter.getEmitter( "text" ); //$NON-NLS-1$
		if ( textEmitter == null )
		{
			return;
		}

		processAction( labelItem.getAction( ), textObj );
		String bookmarkStr = evalBookmark( item );
		if ( bookmarkStr != null )
			textObj.setBookmarkValue( bookmarkStr );

		textObj.setValue( getMapVal( textObj.getValue( ), item ).toString( ) );
		textEmitter.start( textObj );
		textEmitter.end( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.executor.ReportItemExecutor#reset()
	 */
	public void reset( )
	{
		// TODO Auto-generated method stub

	}

}