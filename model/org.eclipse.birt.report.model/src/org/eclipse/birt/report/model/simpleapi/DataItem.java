/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.simpleapi;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.simpleapi.IAction;
import org.eclipse.birt.report.model.api.simpleapi.IDataItem;
import org.eclipse.birt.report.model.elements.interfaces.IDataItemModel;
import org.eclipse.birt.report.model.elements.interfaces.ILabelModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;

public class DataItem extends ReportItem implements IDataItem
{

	public DataItem( DataItemHandle data )
	{
		super( data );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IDataItem#getValueExpr
	 * ()
	 */

	public String getHelpText( )
	{
		return ( (DataItemHandle) handle ).getHelpText( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IDataItem#setHelpText
	 * (java.lang.String)
	 */

	public void setHelpText( String value ) throws SemanticException
	{
		setProperty( IDataItemModel.HELP_TEXT_PROP, value );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IDataItem#getHelpTextKey
	 * ()
	 */

	public String getHelpTextKey( )
	{
		return ( (DataItemHandle) handle ).getHelpTextKey( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IDataItem#setHelpTextKey
	 * (java.lang.String)
	 */

	public void setHelpTextKey( String value ) throws SemanticException
	{
		setProperty( IDataItemModel.HELP_TEXT_KEY_PROP, value );
	}

	public IAction getAction( )
	{
		return new ActionImpl( ( (DataItemHandle) handle ).getActionHandle( ),
				(DataItemHandle) handle );
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.api.simpleapi.IDataItem#addAction(org.eclipse.birt.report.model.api.simpleapi.IAction)
	 */
	public void addAction( IAction action ) throws SemanticException
	{
		if ( action == null )
			return;

		ActivityStack cmdStack = handle.getModule( ).getActivityStack( );
		cmdStack.startNonUndoableTrans( null );
		try
		{
			handle.setProperty( IDataItemModel.ACTION_PROP, action.getStructure( ));
		}
		catch ( SemanticException e )
		{
			cmdStack.rollback( );
			throw e;
		}

		cmdStack.commit( );
	}
}
