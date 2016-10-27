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

package org.eclipse.birt.data.oda.mongodb.ui.impl;

import org.eclipse.birt.data.oda.mongodb.ui.i18n.Messages;
import org.eclipse.birt.data.oda.mongodb.ui.util.IHelpConstants;
import org.eclipse.birt.data.oda.mongodb.ui.util.UIHelper;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.birt.data.oda.mongodb.internal.impl.QueryModel;

public class MDBQueryExpressionBuilder extends MongoDBExpressionBuilder
{

	public MDBQueryExpressionBuilder( Shell parent )
	{
		super( parent );
	}

	protected void initDialogTitle( )
	{
		setTitle( Messages.getString( "MongoDBExpressionBuilder.QueryExpression.DialogTitle" ) ); //$NON-NLS-1$
	}

	protected void createDialogHelper( Composite composite )
	{
		UIHelper.setSystemHelp( composite,
				IHelpConstants.CONTEXT_ID_DIALOG_MONGODB_DATASET_QUERY_EXPRESSION );
	}

	protected void doValidateExpressionSyntax( ) throws OdaException
	{
		QueryModel.validateQuerySyntax( expression );
	}

}
