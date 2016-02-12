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
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;

/**
 * A dialog helper to create binding dialog
 */
public interface IBindingDialogHelper
{

	/**
	 * Set the binding holder
	 * 
	 * @param bindingHolder
	 */
	void setBindingHolder( ReportItemHandle bindingHolder );

	/**
	 * Set the binding content.
	 * 
	 * @param binding
	 */
	void setBinding( ComputedColumnHandle binding );

	/**
	 * Set the DataColumnBindingDialog which use this helper.
	 * 
	 * @param dialog
	 */
	void setDialog( DataColumnBindingDialog dialog );

	/**
	 * Set if the Binding is a aggregation.
	 * 
	 * @param isAggregate
	 */
	void setAggregate( boolean isAggregate );
	
	/**
	 * Set if the Binding is a aggregation.
	 * 
	 * @param isAggregate
	 */
	void setMeasure( boolean isAggregate );
	
	/**Set if the Binding is a time period.
	 * @param timePeriod
	 */
	void setTimePeriod(boolean timePeriod);

	/**
	 * Set the ExpressionProvider for ExpressionBuilder open in the
	 * BindingDialog.
	 * 
	 * @param expressionProvider
	 */
	void setExpressionProvider( ExpressionProvider expressionProvider );

	/**
	 * Create the content of the dialog
	 * 
	 * @param parent
	 */
	void createContent( Composite parent );

	/**
	 * Check the input is valid
	 */
	void validate( );

	/**
	 * Invoke after create UI.
	 */
	void initDialog( );

	/**
	 * return this helper's content is differ to a binding
	 * 
	 * @param binding
	 * @return
	 */
	boolean differs( ComputedColumnHandle binding );

	/**
	 * create a new data binding use this helper's content
	 * 
	 * @param name
	 *            if the name is null, helper will determine the name.
	 * @return
	 */
	ComputedColumnHandle newBinding( ReportItemHandle bindingHolder, String name )
			throws SemanticException;

	/**
	 * edit a existing data binding use this helper's content
	 * 
	 * @param binding
	 * @return
	 * @throws SemanticException
	 */
	ComputedColumnHandle editBinding( ComputedColumnHandle binding )
			throws SemanticException;

	/**
	 * set the container DesignElementHandle of the created data item
	 * 
	 * @param container
	 */
	void setContainer( Object container );

	/**
	 * Return that this helper can process binding editing even there are
	 * warnings.
	 * 
	 * @return
	 */
	boolean canProcessWithWarning( );

	/**
	 * Return that this helper can process aggregation.
	 * 
	 * @return
	 */
	boolean canProcessAggregation( );

	
	void setEditModal( boolean isEditModal );

}
