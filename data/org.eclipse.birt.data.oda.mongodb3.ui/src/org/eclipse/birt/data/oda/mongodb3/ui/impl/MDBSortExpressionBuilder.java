/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.oda.mongodb3.ui.impl;

import org.eclipse.birt.data.oda.mongodb3.ui.i18n.Messages;
import org.eclipse.birt.data.oda.mongodb3.ui.util.IHelpConstants;
import org.eclipse.birt.data.oda.mongodb3.ui.util.UIHelper;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.birt.data.oda.mongodb3.internal.impl.QueryModel;

public class MDBSortExpressionBuilder extends MongoDBExpressionBuilder
{

	public MDBSortExpressionBuilder( Shell parent )
	{
		super( parent );
	}

	protected void initDialogTitle( )
	{
		setTitle( Messages.getString( "MongoDBExpressionBuilder.SortExpression.DialogTitle" ) ); //$NON-NLS-1$
	}

	protected void createDialogHelper( Composite composite )
	{
		UIHelper.setSystemHelp( composite,
				IHelpConstants.CONTEXT_ID_DIALOG_MONGODB_DATASET_SORT_EXPRESSION );
	}

	protected void doValidateExpressionSyntax( ) throws OdaException
	{
		QueryModel.validateSortExprSyntax( expression );
	}

	protected void validateStatus( )
	{
		updateStatus( getOKStatus( ) );

	}

}
