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

package org.eclipse.birt.report.designer.ui.actions;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.ExpressionBuilder;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.views.IRequestConstants;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.elements.ReportDesignConstants;
import org.eclipse.gef.Request;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Action class for insert an expression item. Actually we insert a Data element
 * and fill its expression property.
 */

public class InsertExpressionMenuAction extends BaseInsertMenuAction
{

	/**
	 * ID for insert Expression action.
	 */
	public static final String ID = "Insert Expression"; //$NON-NLS-1$

	/**
	 * Display text for insert Expression action.
	 */
	public static final String DISPLAY_TEXT = Messages.getString( "InsertExpressionMenuAction.text.Expression" ); //$NON-NLS-1$

	/**
	 * The constructor.
	 * 
	 * @param part
	 */
	public InsertExpressionMenuAction( IWorkbenchPart part )
	{
		super( part, ReportDesignConstants.DATA_ITEM );

		setId( ID );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.designer.ui.actions.BaseInsertMenuAction#run()
	 */
	public void run( )
	{
		ExpressionBuilder expressionBuilder = new ExpressionBuilder( new Shell( ),
				"" ); //$NON-NLS-1$

		expressionBuilder.setDataSetList( DEUtil.getDataSetList( slotHandle == null
				? null : slotHandle.getElementHandle( ) ) );

		if ( expressionBuilder.open( ) == Window.OK )
		{
			CommandStack stack = SessionHandleAdapter.getInstance( )
					.getActivityStack( );
			stack.startTrans( STACK_MSG_INSERT_ELEMENT );

			try
			{
				Request req = insertElement( );
				Object obj = req.getExtendedData( )
						.get( IRequestConstants.REQUEST_KEY_RESULT );

				if ( obj instanceof DataItemHandle )
				{
					( (DataItemHandle) obj ).setValueExpr( (String) expressionBuilder.getResult( ) );
				}

				stack.commit( );

				selectElement( obj, false );
			}
			catch ( Exception e )
			{
				stack.rollbackAll( );
				ExceptionHandler.handle( e );
			}
		}
	}
}