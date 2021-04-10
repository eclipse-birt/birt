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
import org.eclipse.core.runtime.IStatus;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.birt.data.oda.mongodb.internal.impl.QueryModel;
import org.eclipse.birt.data.oda.mongodb.internal.impl.QueryProperties.CommandOperationType;

public class MDBCommandExpressionBuilder extends MongoDBExpressionBuilder {

	private CommandOperationType opType;

	/**
	 * The only constructor which needs 2 parameters to build up the dialog. The
	 * CommandOperationType instance is needed.
	 * 
	 * @param parent
	 * @param opType
	 */
	public MDBCommandExpressionBuilder(Shell parent, CommandOperationType opType) {
		super(parent);
		this.opType = opType;
	}

	protected void initDialogTitle() {
		setTitle(Messages.getString("MongoDBExpressionBuilder.CommandExpression.DialogTitle")); //$NON-NLS-1$
	}

	protected void createDialogHelper(Composite composite) {
		UIHelper.setSystemHelp(composite, IHelpConstants.CONTEXT_ID_DIALOG_MONGODB_DATASET_COMMAND_EXPRESSION);
	}

	protected void doValidateExpressionSyntax() throws OdaException {
		QueryModel.validateCommandSyntax(opType, expression);
	}

	protected void validateStatus() {
		IStatus status = null;

		if (expression.trim().length() == 0) {
			status = getMiscStatus(IStatus.ERROR,
					Messages.getString("MongoDBExpressionBuilder.CommandExpression.error.EmptyExpression")); //$NON-NLS-1$
		} else {
			status = getOKStatus();
		}

		if (status != null) {
			updateStatus(status);
		}

	}

}
