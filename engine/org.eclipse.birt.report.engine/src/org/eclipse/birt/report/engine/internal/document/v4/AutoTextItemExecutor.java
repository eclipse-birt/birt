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

package org.eclipse.birt.report.engine.internal.document.v4;

import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.ir.AutoTextItemDesign;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

/**
 * the AutoTextItem excutor
 * 
 */
public class AutoTextItemExecutor extends ReportItemExecutor
{

	/**
	 * constructor
	 * 
	 * @param manager
	 *            the excutor manager which create this executor
	 */
	public AutoTextItemExecutor( ExecutorManager manager )
	{
		super( manager, ExecutorManager.AUTOTEXTITEM );
	}

	protected IContent doCreateContent( )
	{
		return report.createAutoTextContent( );
	}

	public void doExecute( ) throws Exception
	{
		AutoTextItemDesign textDesign = (AutoTextItemDesign) design;
		IAutoTextContent textContent = (IAutoTextContent) content;

		String type = textDesign.getType( );
		if ( DesignChoiceConstants.AUTO_TEXT_PAGE_NUMBER
				.equalsIgnoreCase( type ) )
		{
			textContent.setType( IAutoTextContent.PAGE_NUMBER );
			// If we can get the current page No., set it.
			textContent.setText( String.valueOf( context.getPageNumber( ) ) );
		}
		else if ( DesignChoiceConstants.AUTO_TEXT_TOTAL_PAGE
				.equalsIgnoreCase( type ) )
		{
			textContent.setType( IAutoTextContent.TOTAL_PAGE );
			long totalPage = context.getTotalPage( );
			if ( totalPage <= 0 )
			{
				textContent.setText( "---" );
			}
			else
			{
				textContent.setText( String.valueOf( context.getTotalPage( ) ) );
			}
		}
	}
}
