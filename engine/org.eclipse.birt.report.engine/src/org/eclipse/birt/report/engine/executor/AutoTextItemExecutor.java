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
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.ir.AutoTextItemDesign;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

/**
 * the AutoTextItem excutor
 * 
 * @version $Revision: 1.6 $ $Date: 2006/08/25 03:24:02 $
 */
public class AutoTextItemExecutor extends StyledItemExecutor
{

	/**
	 * constructor
	 * 
	 * @param manager
	 *            the excutor manager which create this executor
	 */
	public AutoTextItemExecutor( ExecutorManager manager )
	{
		super( manager );
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
	 * @see org.eclipse.birt.report.engine.executor.ReportItemExcutor#execute(IContentEmitter)
	 */
	public IContent execute( )
	{
		AutoTextItemDesign textDesign = (AutoTextItemDesign)getDesign();
		IAutoTextContent textContent = report.createAutoTextContent( );
		setContent(textContent);

		restoreResultSet( );
		
		initializeContent( textDesign, textContent );
		processStyle( design, content );
		processVisibility( design, content );
		
		String type = ((AutoTextItemDesign)design).getType();
		if (DesignChoiceConstants.AUTO_TEXT_PAGE_NUMBER.equalsIgnoreCase(type))
		{
			textContent.setType(IAutoTextContent.PAGE_NUMBER);
			// If we can get the current page No., set it.
			textContent.setText(String.valueOf(context.getPageNumber()));
		}
		else if (DesignChoiceConstants.AUTO_TEXT_TOTAL_PAGE.equalsIgnoreCase(type))
		{
			textContent.setType(IAutoTextContent.TOTAL_PAGE);
			long totalPage = context.getTotalPage();
			if (totalPage <= 0)
			{
				textContent.setText("---");
			}
			else
			{
				textContent.setText(String.valueOf(context.getTotalPage()));
			}
		}
		
		if ( context.isInFactory( ) )
		{
			handleOnCreate( textContent );
		}

		if ( emitter != null )
		{
			emitter.startAutoText( textContent );
		}
		return textContent;
	}
	
	public void close( )
	{
		manager.releaseExecutor( ExecutorManager.AUTOTEXTITEM, this );
	}
}