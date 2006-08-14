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
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.ir.TemplateDesign;
import org.eclipse.birt.report.engine.ir.TextItemDesign;
import org.eclipse.birt.report.engine.script.internal.TextItemScriptExecutor;

public class TemplateExecutor extends TextItemExecutor
{

	/**
	 * constructor
	 * 
	 * @param context
	 *            the excutor context
	 * @param visitor
	 *            the report executor visitor
	 */
	public TemplateExecutor( ExecutorManager manager )
	{
		super( manager );
	}

	/**
	 * execute a template and output an text item content. The execution process
	 * is:
	 * <li> create an text
	 * <li> push it into the stack
	 * <li> intialize the content
	 * <li> call emitter to start the text
	 * <li> popup the text.
	 * 
	 * @see org.eclipse.birt.report.engine.executor.ReportItemExcutor#execute(IContentEmitter)
	 */
	public IContent execute( )
	{
		TemplateDesign templateDesign = (TemplateDesign) design;
		IForeignContent textContent = report.createForeignContent( );
		setContent(textContent);
		textContent.setRawType( IForeignContent.HTML_TYPE );		
		IStyle style = textContent.getStyle( );
		style.setProperty( IStyle.STYLE_BORDER_TOP_COLOR, IStyle.GRAY_VALUE );
		style.setProperty( IStyle.STYLE_BORDER_TOP_STYLE, IStyle.SOLID_VALUE );
		style.setProperty( IStyle.STYLE_BORDER_TOP_WIDTH, IStyle.THIN_VALUE );
		style.setProperty( IStyle.STYLE_BORDER_BOTTOM_COLOR, IStyle.GRAY_VALUE );
		style.setProperty( IStyle.STYLE_BORDER_BOTTOM_STYLE, IStyle.SOLID_VALUE );
		style.setProperty( IStyle.STYLE_BORDER_BOTTOM_WIDTH, IStyle.THIN_VALUE );
		style.setProperty( IStyle.STYLE_BORDER_LEFT_COLOR, IStyle.GRAY_VALUE );
		style.setProperty( IStyle.STYLE_BORDER_LEFT_STYLE, IStyle.SOLID_VALUE );
		style.setProperty( IStyle.STYLE_BORDER_LEFT_WIDTH, IStyle.THIN_VALUE );
		style.setProperty( IStyle.STYLE_BORDER_RIGHT_COLOR, IStyle.GRAY_VALUE );
		style.setProperty( IStyle.STYLE_BORDER_RIGHT_STYLE, IStyle.SOLID_VALUE );
		style.setProperty( IStyle.STYLE_BORDER_RIGHT_WIDTH, IStyle.THIN_VALUE );
		style.setProperty( IStyle.STYLE_TEXT_ALIGN, IStyle.CENTER_VALUE );
		style.setProperty( IStyle.STYLE_VERTICAL_ALIGN, IStyle.MIDDLE_VALUE );

		initializeContent( templateDesign, textContent );
		String promptText = templateDesign.getPromptText( );
		if ( promptText == null || promptText.trim( ).length( ) == 0 )
		{
			promptText = "<br/>";
		}
		textContent.setRawValue( promptText );
		textContent.setRawKey( templateDesign.getPromptTextKey( ) );

		processVisibility( templateDesign, textContent );
		
		if ( emitter != null )
		{
			emitter.startForeign( textContent );
		}
		
		return textContent;
	}
	

	public void close( )
	{
		context.unregisterOnPageBreak( content );
		manager.releaseExecutor( ExecutorManager.TEMPLATEITEM, this );
	}
}