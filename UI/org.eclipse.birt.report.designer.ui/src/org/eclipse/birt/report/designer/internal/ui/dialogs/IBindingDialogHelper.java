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

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.swt.widgets.Composite;

/**
 * A dialog helper to create binding dialog 
 */

public interface IBindingDialogHelper
{

	void setDataItemContainer( Object itemContainer );
	
	void setBindingHolder( ReportItemHandle bindingHolder );

	void setBinding( ComputedColumnHandle binding );

	void setDialog( DataColumnBindingDialog dialog );

	void setAggregate( boolean isAggregate );

	void setExpressionProvider( ExpressionProvider expressionProvider );

	void createContent( Composite parent );

	void save( ) throws Exception;

	ComputedColumnHandle getBindingColumn( );

	void validate( );

	void initDialog( );

}
