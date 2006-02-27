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
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.ir.IReportItemVisitor;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.TemplateDesign;

public class TemplateExecutor extends ReportItemExecutor
{

	/**
	 * constructor
	 * 
	 * @param context
	 *            the excutor context
	 * @param visitor
	 *            the report executor visitor
	 */
	public TemplateExecutor( ExecutionContext context,
			IReportItemVisitor visitor )
	{
		super( context, visitor );
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
	 * @see org.eclipse.birt.report.engine.executor.ReportItemExcutor#execute()
	 */
	public void execute( ReportItemDesign item, IContentEmitter emitter )
	{
		TemplateDesign template = (TemplateDesign) item;
		ILabelContent textObj = report.createLabelContent( );
		IContent parent = context.getContent( );

		context.pushContent( textObj );

		IStyle style = textObj.getStyle( );
		style.setProperty( IStyle.STYLE_BORDER_TOP_COLOR, IStyle.BLACK_VALUE );
		style.setProperty( IStyle.STYLE_BORDER_TOP_STYLE, IStyle.SOLID_VALUE );
		style.setProperty( IStyle.STYLE_BORDER_TOP_WIDTH, IStyle.THIN_VALUE );
		style
				.setProperty( IStyle.STYLE_BORDER_BOTTOM_COLOR,
						IStyle.BLACK_VALUE );
		style
				.setProperty( IStyle.STYLE_BORDER_BOTTOM_STYLE,
						IStyle.SOLID_VALUE );
		style.setProperty( IStyle.STYLE_BORDER_BOTTOM_WIDTH, IStyle.THIN_VALUE );
		style.setProperty( IStyle.STYLE_BORDER_LEFT_COLOR, IStyle.BLACK_VALUE );
		style.setProperty( IStyle.STYLE_BORDER_LEFT_STYLE, IStyle.SOLID_VALUE );
		style.setProperty( IStyle.STYLE_BORDER_LEFT_WIDTH, IStyle.THIN_VALUE );
		style.setProperty( IStyle.STYLE_BORDER_RIGHT_COLOR, IStyle.BLACK_VALUE );
		style.setProperty( IStyle.STYLE_BORDER_RIGHT_STYLE, IStyle.SOLID_VALUE );
		style.setProperty( IStyle.STYLE_BORDER_RIGHT_WIDTH, IStyle.THIN_VALUE );

		initializeContent( parent, item, textObj );
		textObj.setLabelText( template.getPromptText( ) );
		textObj.setLabelKey( template.getPromptTextKey( ) );

		if ( emitter != null )
		{
			emitter.startLabel( textObj );
		}
		context.popContent( );
	}

}
