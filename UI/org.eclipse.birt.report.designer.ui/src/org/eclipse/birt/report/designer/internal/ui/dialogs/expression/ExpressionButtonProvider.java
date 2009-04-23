/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.dialogs.expression;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.swt.graphics.Image;

/**
 * 
 */

public class ExpressionButtonProvider implements IExpressionButtonProvider
{

	private static final String CONSTANT = Messages.getString("ExpressionButtonProvider.Constant"); //$NON-NLS-1$
	private static final String JAVA_SCRIPT = Messages.getString("ExpressionButtonProvider.Javascript"); //$NON-NLS-1$
	private ExpressionButton input;

	public void setInput( ExpressionButton input )
	{
		this.input = input;
	}

	public String[] getExpressionTypes( )
	{
		return new String[]{
				ExpressionType.CONSTANT, ExpressionType.JAVASCRIPT
		};
	}

	public Image getImage( String exprType )
	{
		String imageName = null;
		if ( ExpressionType.CONSTANT.equals( exprType ) )
		{
			if ( input.isEnabled( ) )
			{
				imageName = IReportGraphicConstants.ICON_ENABLE_EXPRESSION_CONSTANT;
			}
			else
			{
				imageName = IReportGraphicConstants.ICON_DISABLE_EXPRESSION_CONSTANT;
			}
		}
		else
		{
			if ( input.isEnabled( ) )
			{
				imageName = IReportGraphicConstants.ICON_ENABLE_EXPRESSION_JAVASCRIPT;
			}
			else
			{
				imageName = IReportGraphicConstants.ICON_DISABLE_EXPRESSION_JAVASCRIPT;
			}
		}
		return ReportPlatformUIImages.getImage( imageName );
	}

	public String getText( String exprType )
	{
		if ( ExpressionType.CONSTANT.equals( exprType ) )
			return CONSTANT;
		else if ( ExpressionType.JAVASCRIPT.equals( exprType ) )
			return JAVA_SCRIPT;
		else 
			return ""; //$NON-NLS-1$
	}

	public String getTooltipText( String exprType )
	{
		if ( ExpressionType.CONSTANT.equals( exprType ) )
			return CONSTANT;
		else if ( ExpressionType.JAVASCRIPT.equals( exprType ) )
			return JAVA_SCRIPT;
		else
			return ""; //$NON-NLS-1$
	}

	public void handleSelectionEvent( String exprType )
	{
		if ( ExpressionType.JAVASCRIPT.equals( exprType ) )
		{
			input.openExpressionBuilder( );
		}
	}

}
