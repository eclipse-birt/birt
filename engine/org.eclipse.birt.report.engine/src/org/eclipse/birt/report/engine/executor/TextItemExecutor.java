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
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.ir.TextItemDesign;
import org.eclipse.birt.report.engine.script.internal.TextItemScriptExecutor;

/**
 * <code>DataItemExecutor</code> is a concrete subclass of
 * <code>StyledItemExecutor</code> that manipulates label/text items.
 * 
 * @version $Revision: 1.35 $ $Date: 2006/06/22 08:38:23 $
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
	public TextItemExecutor( ExecutorManager manager)
	{
		super( manager );
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
	 * @see org.eclipse.birt.report.engine.executor.ReportItemExcutor#execute(IContentEmitter)
	 */
	public IContent execute( )
	{
		TextItemDesign textDesign = (TextItemDesign) getDesign();
		String contentType = ForeignContent.getTextRawType( textDesign
				.getTextType( ), textDesign.getText( ) );

		if ( IForeignContent.HTML_TYPE.equals( contentType ) )
		{
			return executeHtmlText( emitter );
		}
		else
		{
			return executePlainText(emitter );
		}
	}

	public void close( )
	{
		context.unregisterOnPageBreak( content );
		finishTOCEntry( );
		closeQuery( );
		manager.releaseExecutor( ExecutorManager.TEXTITEM, this );
	}
	
	/**
	 * execute the html text.
	 * 
	 * @param design
	 * @param emitter
	 */
	protected IContent executeHtmlText( IContentEmitter emitter )
	{
		TextItemDesign textDesign = (TextItemDesign) getDesign();
		IForeignContent textContent = report.createForeignContent( );
		setContent(textContent);

		executeQuery( );
		//accessQuery( );
		context.registerOnPageBreak( content );		
		
		initializeContent( textDesign, textContent );

		processAction( textDesign, textContent );
		processBookmark( textDesign, textContent );
		processStyle( textDesign, textContent );
		processVisibility( textDesign, textContent );

		HashMap exprs = textDesign.getExpressions( );
		if ( exprs != null && !exprs.isEmpty( ) )
		{
			HashMap results = new HashMap( );
			Iterator iter = exprs.entrySet( ).iterator( );
			while ( iter.hasNext( ) )
			{
				Map.Entry entry = (Map.Entry) iter.next( );
				String expr = ( String ) entry.getValue( );
				Object value = evaluate( expr );
				results.put( entry.getKey( ), value );
			}
			textContent.setRawValue( results );
		}
		textContent.setRawType( IForeignContent.TEMPLATE_TYPE );

		if ( context.isInFactory( ) )
		{
			TextItemScriptExecutor.handleOnCreate( textContent,
					context );
		}
		
		startTOCEntry( content );
		if ( emitter != null )
		{
			emitter.startForeign( textContent );
		}
		
		return textContent;
	}

	/**
	 * execute the plain text.
	 * 
	 * @param design
	 * @param emitter
	 */
	protected IContent executePlainText(IContentEmitter emitter)
	{
		TextItemDesign textDesign = (TextItemDesign) getDesign();
		
		ILabelContent textContent = report.createLabelContent( );
		setContent(textContent);

		executeQuery( );
		//accessQuery( design, emitter );
		context.registerOnPageBreak( content );		
		
		initializeContent( textDesign, textContent );
		textContent.setLabelText( textDesign.getText( ) );
		textContent.setLabelKey( textDesign.getTextKey( ) );

		processAction( textDesign, textContent );
		processBookmark( textDesign, textContent );
		processStyle( textDesign, textContent );
		processVisibility( textDesign, textContent );

		if ( context.isInFactory( ) )
		{
			TextItemScriptExecutor.handleOnCreate( textContent,
					context );
		}

		startTOCEntry( content );
		if ( emitter != null )
		{
			emitter.startLabel( textContent );
		}
		
		return textContent;
	}
}