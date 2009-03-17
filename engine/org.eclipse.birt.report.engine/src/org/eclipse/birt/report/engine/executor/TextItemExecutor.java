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
import java.util.Map.Entry;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.impl.ForeignContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.ir.Expression;
import org.eclipse.birt.report.engine.ir.TextItemDesign;

/**
 * <code>DataItemExecutor</code> is a concrete subclass of
 * <code>StyledItemExecutor</code> that manipulates label/text items.
 * 
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
	public TextItemExecutor( ExecutorManager manager )
	{
		super( manager, ExecutorManager.TEXTITEM );
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
		TextItemDesign textDesign = (TextItemDesign) getDesign( );
		String textType = evaluate( textDesign.getTextType( ) );
		String text = evaluate( textDesign.getText( ) );
		String contentType = ForeignContent.getTextRawType( textType, text );

		if ( IForeignContent.HTML_TYPE.equals( contentType ) )
		{
			return executeHtmlText( );
		}
		else
		{
			return executePlainText( );
		}
	}

	public void close( ) throws BirtException
	{
		finishTOCEntry( );
		closeQuery( );
		super.close( );
	}

	/**
	 * execute the html text.
	 * 
	 * @param design
	 * @param emitter
	 */
	protected IContent executeHtmlText( )
	{
		TextItemDesign textDesign = (TextItemDesign) getDesign( );
		IForeignContent textContent = report.createForeignContent( );
		setContent( textContent );

		executeQuery( );
		// accessQuery( );

		initializeContent( textDesign, textContent );

		processAction( textDesign, textContent );
		processBookmark( textDesign, textContent );
		processStyle( textDesign, textContent );
		processVisibility( textDesign, textContent );

		Expression<String> textExpression = textDesign.getText( );
		String text = evaluate( textExpression );
		String textType = evaluate( textDesign.getTextType( ) );

		
		HashMap<String, String> exprs = null;
		if ( textDesign.hasExpression( ) )
		{
			if ( textExpression.isExpression( ) )
			{
				exprs = TextItemDesign.extractExpression( text, textType );
			}
			else
			{
				exprs = textDesign.getExpressions( );
			}
		}
		if ( exprs != null && !exprs.isEmpty( ) )
		{
			HashMap<String, Object> results = new HashMap<String, Object>( );
			Iterator<Entry<String, String>> iter = exprs.entrySet( ).iterator( );
			while ( iter.hasNext( ) )
			{
				Entry<String, String> entry = (Entry<String, String>) iter
						.next( );
				String expr = (String) entry.getValue( );
				try
				{
					Object value = evaluate( expr );
					results.put( entry.getKey( ), value );
				}
				catch ( BirtException ex )
				{
					context.addException( ex );
				}
			}
			Object[] value = new Object[2];
			value[0] = null;
			value[1] = results;
			textContent.setRawValue( value );
		}
		else
		{
			textContent.setRawValue( new Object[]{null, null} );
		}
		textContent.setRawType( IForeignContent.TEMPLATE_TYPE );

		if ( context.isInFactory( ) )
		{
			handleOnCreate( textContent );
		}

		startTOCEntry( content );

		return textContent;
	}

	/**
	 * execute the plain text.
	 * 
	 * @param design
	 * @param emitter
	 */
	protected IContent executePlainText( )
	{
		TextItemDesign textDesign = (TextItemDesign) getDesign( );

		ILabelContent textContent = report.createLabelContent( );
		setContent( textContent );

		executeQuery( );
		// accessQuery( design, emitter );

		initializeContent( textDesign, textContent );
		textContent.setLabelText( evaluate( textDesign.getText( ) ) );
		textContent.setLabelKey( evaluate( textDesign.getTextKey( ) ) );

		processAction( textDesign, textContent );
		processBookmark( textDesign, textContent );
		processStyle( textDesign, textContent );
		processVisibility( textDesign, textContent );

		if ( context.isInFactory( ) )
		{
			handleOnCreate( textContent );
		}

		startTOCEntry( content );

		return textContent;
	}
}