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

package org.eclipse.birt.report.designer.ui.dialogs;

import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.BaseTransform;
import org.eclipse.birt.data.engine.api.querydefn.ComputedColumn;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.OdaDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.internal.ui.util.DataSetManager;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * This dialog takes an expression and a data set and shows a list of unique
 * values for selection from the data set. It allows both multiple and single
 * selection. The default is single selection.
 * 
 * @version $Revision: 1.5 $ $Date: 2005/03/23 03:10:03 $
 */
public class SelectValueDialog extends BaseDialog
{

	private DataSetHandle dataSetHandle = null;
	private String expression = null;
	private boolean multipleSelection = false;

	private List selectValueList = null;

	/**
	 * @param parentShell
	 * @param title
	 */
	public SelectValueDialog( Shell parentShell, String title )
	{
		super( parentShell, title, false );
	}

	/**
	 * @return Returns the dataSetHandle.
	 */
	public DataSetHandle getDataSetHandle( )
	{
		return dataSetHandle;
	}

	/**
	 * @param dataSetHandle
	 *            The dataSetHandle to set.
	 */
	public void setDataSetHandle( DataSetHandle dataSetHandle )
	{
		this.dataSetHandle = dataSetHandle;
	}

	/**
	 * @return Returns the expression.
	 */
	public String getExpression( )
	{
		return expression;
	}

	/**
	 * @param expression
	 *            The expression to set.
	 */
	public void setExpression( String expression )
	{
		this.expression = expression;
	}

	/**
	 * @return Returns the multipleSelection.
	 */
	public boolean isMultipleSelection( )
	{
		return multipleSelection;
	}

	/**
	 * @param multipleSelection
	 *            The multipleSelection to set.
	 */
	public void setMultipleSelection( boolean multipleSelection )
	{
		this.multipleSelection = multipleSelection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea( Composite parent )
	{
		Composite composite = new Composite( parent, SWT.NONE );
		GridLayout layout = new GridLayout( );
		composite.setLayout( layout );
		Label label = new Label( composite, SWT.NONE );
		label.setText( Messages.getString( "SelectValueDialog.selectValue" ) ); //$NON-NLS-1$

		selectValueList = new List( composite,
				isMultipleSelection( ) ? SWT.MULTI : SWT.SINGLE
						| SWT.V_SCROLL
						| SWT.H_SCROLL );
		GridData data = new GridData( GridData.FILL_BOTH );
		data.heightHint = 250;
		data.widthHint = 300;
		selectValueList.setLayoutData( data );
		selectValueList.add( Messages.getString( "SelectValueDialog.retrieving" ) ); //$NON-NLS-1$
		selectValueList.addMouseListener( new MouseAdapter( ) {

			public void mouseDoubleClick( MouseEvent e )
			{
				if ( selectValueList.getSelectionCount( ) > 0 )
				{
					okPressed( );
				}
			}
		} );

		PlatformUI.getWorkbench( ).getDisplay( ).asyncExec( new Runnable( ) {

			public void run( )
			{
				populateList( );
			}

		} );

		return composite;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	protected void okPressed( )
	{
		setResult( selectValueList.getSelection( ) );
		super.okPressed( );
	}

	public String getSelectedValue( )
	{
		String[] result = (String[]) getResult( );
		return ( result != null && result.length > 0 ) ? result[0] : null;
	}

	public String[] getSelectedValues( )
	{
		String[] result = (String[]) getResult( );
		return ( result != null && result.length > 0 ) ? result : null;
	}

	private void populateList( )
	{
		try
		{
			//Execute the query and populate this list
			BaseQueryDefinition query = (BaseQueryDefinition) DataSetManager.getCurrentInstance( )
					.getPreparedQuery( getDataSetHandle( ), true, false)
					.getReportQueryDefn( );
			ScriptExpression expression = new ScriptExpression( getExpression( ) );
			GroupDefinition defn = new GroupDefinition( );
			defn.setKeyExpression( getExpression( ) );
			query.setUsesDetails( false );
			query.addGroup( defn );
			query.addExpression( expression, BaseTransform.BEFORE_FIRST_ROW );

			IPreparedQuery preparedQuery = DataSetManager.getCurrentInstance( )
					.getEngine( )
					.prepare( (IQueryDefinition) query );
			IQueryResults results = preparedQuery.execute( null );
			selectValueList.removeAll( );
			if ( results != null )
			{
				IResultIterator iter = results.getResultIterator( );
				if ( iter != null )
				{
					while ( iter.next( ) )
					{
						selectValueList.add( iter.getString( expression ) );
					}
				}

				results.close( );
			}
		}
		catch ( Exception e )
		{
			ExceptionHandler.handle( e );
		}
	}

	private void populateList1( )
	{
		try
		{
			//Execute the query and populate this list
			OdaDataSetDesign design = (OdaDataSetDesign) DataSetManager.getCurrentInstance( )
					.getDataSetDesign( getDataSetHandle( ), true, false );
			design.addComputedColumn( new ComputedColumn( "selectValue",
					getExpression( ) ) );
			QueryDefinition query = DataSetManager.getCurrentInstance( )
					.getQueryDefinition( design );
			ScriptExpression expression = new ScriptExpression( "row[\"selectValue\"]" );

			GroupDefinition defn = new GroupDefinition( );
			defn.setKeyExpression( expression.getText( ) );
			query.setUsesDetails( false );
			query.addGroup( defn );
			query.addExpression( expression, BaseTransform.BEFORE_FIRST_ROW );

			IPreparedQuery preparedQuery = DataSetManager.getCurrentInstance( )
					.getPreparedQuery( query );
			IQueryResults results = preparedQuery.execute( null );
			selectValueList.removeAll( );
			if ( results != null )
			{
				IResultIterator iter = results.getResultIterator( );
				if ( iter != null )
				{
					while ( iter.next( ) )
					{
						selectValueList.add( iter.getString( expression ) );
					}
				}

				results.close( );
			}
		}
		catch ( Exception e )
		{
			e.printStackTrace( );
			ExceptionHandler.handle( e );
		}
	}
}