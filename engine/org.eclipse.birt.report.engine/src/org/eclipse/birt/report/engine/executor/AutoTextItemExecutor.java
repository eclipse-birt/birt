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

import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.impl.AutoTextContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.ir.AutoTextItemDesign;
import org.eclipse.birt.report.engine.ir.IReportItemVisitor;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

/**
 * the AutoTextItem excutor
 * 
 * @version $Revision: 1.18 $ $Date: 2006/04/11 08:17:43 $
 */
public class AutoTextItemExecutor extends StyledItemExecutor
{

	/**
	 * constructor
	 * 
	 * @param context
	 *            the excutor context
	 * @param visitor
	 *            the report executor visitor
	 */
	public AutoTextItemExecutor( ExecutionContext context,
			IReportItemVisitor visitor )
	{
		super( context, visitor );
	}

	/**
	 * execute a AutoText and output an AutoText item content. The execution process
	 * is:
	 * <li> create an AutoText
	 * <li> push it into the stack
	 * <li> intialize the content
	 * <li> process the action, bookmark, style ,visibility.
	 * <li> execute the onCreate if necessary
	 * <li> call emitter to start the AutoText
	 * <li> popup the AutoText.
	 * 
	 * @see org.eclipse.birt.report.engine.executor.ReportItemExcutor#execute()
	 */
	public void execute( ReportItemDesign item, IContentEmitter emitter )
	{
		IAutoTextContent autoTextObj = report.createAutoTextContent( );
		assert ( autoTextObj instanceof AutoTextContent );
		IContent parent = context.getContent( );

		context.pushContent( autoTextObj );
		
		initializeContent( parent, item, autoTextObj );

		processStyle( item, autoTextObj );
		processVisibility( item, autoTextObj );
		
		assert ( item instanceof AutoTextItemDesign );
		String type = ((AutoTextItemDesign)item).getType();
		if (DesignChoiceConstants.AUTO_TEXT_PAGE_NUMBER.equalsIgnoreCase(type))
		{
			autoTextObj.setType(IAutoTextContent.PAGE_NUMBER);
			// If we can get the current page No., set it.
			autoTextObj.setText(String.valueOf(context.getPageNumber()));
		}
		else if (DesignChoiceConstants.AUTO_TEXT_TOTAL_PAGE.equalsIgnoreCase(type))
		{
			autoTextObj.setType(IAutoTextContent.TOTAL_PAGE);
			long totalPage = context.getTotalPage();
			if (totalPage <= 0)
			{
				autoTextObj.setText("---");
			}
			else
			{
				autoTextObj.setText(String.valueOf(context.getTotalPage()));
			}
			
		}
		
		if ( emitter != null )
		{
			emitter.startAutoText( autoTextObj );
		}
		context.popContent( );
	}
}