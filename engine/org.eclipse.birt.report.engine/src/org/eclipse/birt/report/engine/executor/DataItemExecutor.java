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

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.impl.DataContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.ir.DataItemDesign;
import org.eclipse.birt.report.engine.ir.IReportItemVisitor;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.script.internal.DataItemScriptExecutor;

/**
 * <code>DataItemExecutor</code> is a concrete subclass of
 * <code>QueryItemExecutor</code> that manipulates data items from database
 * columns, expressions and so on.
 * <p>
 * Data item executor calculates expressions in data item design, generate a
 * data content instance, evaluate styles, bookmark, action property and pass
 * this instance to emitter.
 * 
 * @version $Revision: 1.22 $ $Date: 2005/12/02 11:57:05 $
 */
public class DataItemExecutor extends QueryItemExecutor
{

	/**
	 * construct a data item executor by giving execution context and report
	 * executor visitor
	 * 
	 * @param context
	 *            the executor context
	 * @param itemEmitter
	 *            the emitter
	 */
	public DataItemExecutor( ExecutionContext context,
			IReportItemVisitor visitor )
	{
		super( context, visitor );
	}

	/**
	 * execute the data item.
	 * 
	 * <li> create the data content object
	 * <li> push it to the stack
	 * <li> open the data set, seek to the first record
	 * <li> intialize the content object
	 * <li> process the style, visiblitly, action and bookmark
	 * <li> evaluate the expression, and map it to a predefined value.
	 * <li> call the onCreate if necessary
	 * <li> pass it to emitter
	 * <li> close the data set if any
	 * <li> pop the stack.
	 * 
	 * @see org.eclipse.birt.report.engine.excutor.ReportItemLoader#execute()
	 */
	public void execute( ReportItemDesign item, IContentEmitter emitter )
	{
		DataItemDesign dataItem = (DataItemDesign) item;
		IDataContent dataObj = report.createDataContent( );
		assert ( dataObj instanceof DataContent );
		IContent parent = context.getContent( );
		context.pushContent( dataObj );

		openResultSet( item );
		accessQuery( item, emitter );

		initializeContent( parent, item, dataObj );

		processAction( item, dataObj );
		processBookmark( item, dataObj );
		processStyle( item, dataObj );
		processVisibility( item, dataObj );

		Object value = context.evaluate( dataItem.getValue( ) );
		dataObj.setValue( value );

		// get the mapping value
		processMappingValue( dataItem, dataObj );

		if ( context.isInFactory( ) )
		{
			DataItemScriptExecutor.handleOnCreate( (DataContent) dataObj,
					context );
		}

		openTOCEntry( dataObj );

		// pass the text content instance to emitter
		if ( emitter != null )
		{
			emitter.startData( dataObj );
		}

		closeTOCEntry( );
		closeResultSet( );
		context.popContent( );
	}
}