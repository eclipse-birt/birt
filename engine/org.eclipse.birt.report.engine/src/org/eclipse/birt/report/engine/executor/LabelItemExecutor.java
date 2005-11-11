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
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.ir.IReportItemVisitor;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;

/**
 * the labelItem excutor
 * 
 * @version $Revision: 1.9 $ $Date: 2005/11/08 09:57:06 $
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
			IReportItemVisitor visitor )
	{
		super( context, visitor );
	}

	/**
	 * execute a label and output an label item content.
	 * The execution process is:
	 * <li> create an label
	 * <li> push it into the stack
	 * <li> intialize the content
	 * <li> process the action, bookmark, style ,visibility.
	 * <li> execute the onCreate if necessary
	 * <li> call emitter to start the label
	 * <li> popup the label.
	 * @see org.eclipse.birt.report.engine.executor.ReportItemExcutor#execute()
	 */
	public void execute( ReportItemDesign item, IContentEmitter emitter )
	{
		ILabelContent labelObj = report.createLabelContent( );
		IContent parent = context.getContent(); 
		
		context.pushContent( labelObj );

		initializeContent(parent, item, labelObj);

		processAction( item, labelObj );
		processBookmark( item, labelObj );
		processStyle( item, labelObj );
		processVisibility( item, labelObj );

		if (context.isInFactory())
		{
			context.execute( item.getOnCreate( ) );
		}

		if ( emitter != null )
		{
			emitter.startLabel( labelObj );
		}
		context.popContent( );
	}
}