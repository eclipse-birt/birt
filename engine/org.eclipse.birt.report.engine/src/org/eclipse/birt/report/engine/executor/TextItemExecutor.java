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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.impl.ForeignContent;
import org.eclipse.birt.report.engine.content.impl.LabelContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.ir.Expression;
import org.eclipse.birt.report.engine.ir.IReportItemVisitor;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.TextItemDesign;
import org.eclipse.birt.report.engine.script.TextItemScriptExecutor;

/**
 * <code>DataItemExecutor</code> is a concrete subclass of
 * <code>StyledItemExecutor</code> that manipulates label/text items.
 * 
 * @version $Revision: 1.26 $ $Date: 2005/11/21 06:49:18 $
 */
public class TextItemExecutor extends QueryItemExecutor
{

	/**
	 * constructor
	 * 
	 * @param context
	 *            the executor context
	 * @param visitor
	 *            the report executor visitor
	 */
	public TextItemExecutor( ExecutionContext context,
			IReportItemVisitor visitor )
	{
		super( context, visitor );
	}

	/**
	 * Text item create an foreign object. The process is:
	 * <li> create the foreign object
	 * <li> push it into the context
	 * <li> execute the query and seek to the first record if any.
	 * <li> process the style, visiblity, bookmark, actions
	 * <li> evaluate the expressions in the text.
	 * <li> call onCreate if needed.
	 * <li> pass it to emitter
	 * <li> close the query
	 * <li> pop up.
	 * 
	 * @see org.eclipse.birt.report.engine.executor.ReportItemExcutor#execute()
	 */
	public void execute( ReportItemDesign item, IContentEmitter emitter )
	{
		TextItemDesign textItem = (TextItemDesign) item;
		String contentType = ForeignContent.getTextRawType( textItem
				.getTextType( ), textItem.getText( ) );

		if ( IForeignContent.HTML_TYPE.equals( contentType ) )
		{
			executeHtmlText( textItem, emitter );
		}
		else
		{
			executePlainText( textItem, emitter );
		}
	}

	/**
	 * execute the html text.
	 * 
	 * @param design
	 * @param emitter
	 */
	protected void executeHtmlText( TextItemDesign design,
			IContentEmitter emitter )
	{
		IForeignContent content = report.createForeignContent( );
		assert ( content instanceof ForeignContent );
		IContent parent = context.getContent( );
		context.pushContent( content );

		openResultSet( design );
		accessQuery( design, emitter );

		initializeContent( parent, design, content );

		processAction( design, content );
		processBookmark( design, content );
		processStyle( design, content );
		processVisibility( design, content );

		HashMap exprs = design.getExpressions( );
		if ( exprs != null && !exprs.isEmpty( ) )
		{
			HashMap results = new HashMap( );
			Iterator iter = exprs.entrySet( ).iterator( );
			while ( iter.hasNext( ) )
			{
				Map.Entry entry = (Map.Entry) iter.next( );
				Expression expr = (Expression) entry.getValue( );
				Object value = context.evaluate( expr );
				results.put( entry.getKey( ), value );
			}
			content.setRawValue( results );
		}
		content.setRawType( IForeignContent.TEMPLATE_TYPE );

		if ( context.isInFactory( ) )
		{
			TextItemScriptExecutor.handleOnCreate( (ForeignContent) content,
					context );
		}

		openTOCEntry( content );
		if ( emitter != null )
		{
			emitter.startForeign( content );
		}
		closeTOCEntry( );

		closeResultSet( );
		context.popContent( );
	}

	/**
	 * execute the plain text.
	 * 
	 * @param design
	 * @param emitter
	 */
	protected void executePlainText( TextItemDesign design,
			IContentEmitter emitter )
	{
		ILabelContent content = report.createLabelContent( );
		assert ( content instanceof LabelContent );
		IContent parent = context.getContent( );
		context.pushContent( content );

		openResultSet( design );
		accessQuery( design, emitter );

		initializeContent( parent, design, content );
		content.setLabelText( design.getText( ) );
		content.setLabelKey( design.getTextKey( ) );

		processAction( design, content );
		processBookmark( design, content );
		processStyle( design, content );
		processVisibility( design, content );

		if ( context.isInFactory( ) )
		{
			TextItemScriptExecutor.handleOnCreate( (LabelContent) content,
					context );
		}

		openTOCEntry( content );
		if ( emitter != null )
		{
			emitter.startLabel( content );
		}
		closeTOCEntry( );

		closeResultSet( );
		context.popContent( );
	}
}
