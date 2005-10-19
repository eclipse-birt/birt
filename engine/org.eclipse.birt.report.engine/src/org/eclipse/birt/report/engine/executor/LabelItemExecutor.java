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
 * @version $Revision: 1.9 $ $Date: 2005/05/11 08:18:32 $
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
		LabelItemDesign labelItem = (LabelItemDesign) item;
		IReportItemEmitter textEmitter = emitter.getEmitter( "text" ); //$NON-NLS-1$
		if ( textEmitter == null )
		{
			return;
		}
		TextItemContent textObj = (TextItemContent) ContentFactory
				.createTextContent( labelItem, context.getContentObject( ) );

		context.enterScope( textObj );

		textObj.setHelpText( getLocalizedString( labelItem.getHelpTextKey( ),
				labelItem.getHelpText( ) ) );
		textObj.setValue( getLocalizedString( labelItem.getTextKey( ),
				labelItem.getText( ) ) );
		setStyles( textObj, item );
		setVisibility( item, textObj );

		processAction( labelItem.getAction( ), textObj );
		String bookmarkStr = evalBookmark( item );
		if ( bookmarkStr != null )
			textObj.setBookmarkValue( bookmarkStr );

		Object value = textObj.getValue( );
		value = getMapVal( value, item );
		if ( value == null )
		{
			textObj.setValue( "" ); //$NON-NLS-1$
		}
		else
		{
			textObj.setValue( value.toString( ) );
		}

		context.evaluate( labelItem.getOnCreate( ) );
		context.exitScope( );

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