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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.core.script.JavascriptEvalUtil;
import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
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

import com.ibm.icu.util.ULocale;

/**
 * This dialog takes an expression and a data set and shows a list of unique
 * values for selection from the data set. It allows both multiple and single
 * selection. The default is single selection.
 * 
 * @version $Revision: 1.31 $ $Date: 2008/06/12 08:05:55 $
 */
public class SelectValueDialog extends BaseDialog
{

	private boolean multipleSelection = false;

	private List selectValueList = null;
	private int[] selectedIndices = null;
	private java.util.List modelValueList = new ArrayList( );
	private java.util.List viewerValueList = new ArrayList( );

	private ParamBindingHandle[] bindingParams = null;

	private final String nullValueDispaly = Messages.getString( "SelectValueDialog.SelectValue.NullValue" ); //$NON-NLS-1$

	/**
	 * @param parentShell
	 * @param title
	 */
	public SelectValueDialog( Shell parentShell, String title )
	{
		super( parentShell, title );
	}

	/**
	 * @return Returns the paramBindingHandles.
	 */
	public ParamBindingHandle[] getBindingParams( )
	{
		return bindingParams;
	}

	/**
	 * Set handles for binding parameters
	 */
	public void setBindingParams( ParamBindingHandle[] handles )
	{
		this.bindingParams = handles;
	}

	/**
	 * @param expression
	 *            The expression to set.
	 */
	public void setSelectedValueList( Collection valueList )
	{
		modelValueList.clear( );
		modelValueList.addAll( valueList );
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
		composite.setLayoutData( new GridData( GridData.FILL_BOTH) );
		Label label = new Label( composite, SWT.NONE );
		label.setText( Messages.getString( "SelectValueDialog.selectValue" ) ); //$NON-NLS-1$

		selectValueList = new List( composite,
				( isMultipleSelection( ) ? SWT.MULTI : SWT.SINGLE )
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
		selectedIndices = selectValueList.getSelectionIndices( );
		setResult( selectValueList.getSelection( ) );
		super.okPressed( );
	}

	/*
	 * Return the first selected value if selected result is not null
	 */
	public String getSelectedValue( )
	{
		String[] result = (String[]) getResult( );
		return ( result != null && result.length > 0 ) ? result[0] : null;
	}

	/*
	 * Return all the selected value if selected result is not null
	 */
	public String[] getSelectedValues( )
	{
		String[] result = (String[]) getResult( );
		return ( result != null && result.length > 0 ) ? result : null;
	}

	/**
	 * Return expression string value as expression required format. For example
	 * number type: Integer value 1 to String value "1" Boolean type: Boolean
	 * value true to String value "true" other types: String value "abc" to
	 * String value "\"abc\"" Date value "2000-10-10" to String value
	 * "\"2000-10-10\""
	 * 
	 * @return expression value
	 */
	public String getSelectedExprValue( )
	{
		String exprValue = null;
		if ( selectedIndices != null && selectedIndices.length > 0 )
		{
			int firstIndex = selectedIndices[0];
			Object modelValue = modelValueList.get( firstIndex );

			if ( modelValue == null )
			{
				return "null"; //$NON-NLS-1$
			}
			else
			{
				String viewerValue = (String) viewerValueList.get( firstIndex );
				if ( modelValue instanceof Boolean
						|| modelValue instanceof Integer
						|| modelValue instanceof Double )
				{
					exprValue = viewerValue;
				}
				else if ( modelValue instanceof BigDecimal )
				{
					exprValue = new String( "new java.math.BigDecimal(\""
							+ viewerValue
							+ "\")" );
				}
				else
				{
					exprValue = "\"" //$NON-NLS-1$
							+ JavascriptEvalUtil.transformToJsConstants( viewerValue )
							+ "\""; //$NON-NLS-1$
				}
			}
		}
		return exprValue;
	}

	/**
	 * Return expression string value as expression required format. For example
	 * number type: Integer value 1 to String value "1" Boolean type: Boolean
	 * value true to String value "true" other types: String value "abc" to
	 * String value "\"abc\"" Date value "2000-10-10" to String value
	 * "\"2000-10-10\""
	 * 
	 * @return expression value
	 */
	public String[] getSelectedExprValues( )
	{
		String[] exprValues = null;
		if ( selectedIndices != null && selectedIndices.length > 0 )
		{
			exprValues = new String[selectedIndices.length];
			for ( int i = 0; i < selectedIndices.length; i++ )
			{
				int firstIndex = selectedIndices[i];
				Object modelValue = modelValueList.get( firstIndex );

				if ( modelValue == null )
				{
					exprValues[i] = "null"; //$NON-NLS-1$
				}
				else
				{
					String viewerValue = (String) viewerValueList.get( firstIndex );
					if ( modelValue instanceof Boolean
							|| modelValue instanceof Integer
							|| modelValue instanceof Double )
					{
						exprValues[i] = viewerValue;
					}
					else if ( modelValue instanceof BigDecimal )
					{
						exprValues[i] = new String( "new java.math.BigDecimal(\""
								+ viewerValue
								+ "\")" );
					}
					else
					{
						exprValues[i] = "\"" //$NON-NLS-1$
								+ JavascriptEvalUtil.transformToJsConstants( viewerValue )
								+ "\""; //$NON-NLS-1$
					}
				}
			}
		}
		return exprValues;
	}

	/**
	 * populate all available value in selectValueList
	 */
	private void populateList( )
	{
		try
		{
			getOkButton( ).setEnabled( false );
			selectValueList.removeAll( );
			viewerValueList.clear( );
			if ( modelValueList != null )
			{
				Iterator iter = modelValueList.iterator( );
				DateFormatter formatter = new DateFormatter( ULocale.US );
				while ( iter.hasNext( ) )
				{
					Object candiateValue = iter.next( );
					if ( candiateValue != null )
					{
						Object displayCandiateValue;
						if ( candiateValue instanceof java.sql.Date )
						{
							formatter.applyPattern( "yyyy-MM-dd" ); //$NON-NLS-1$
							displayCandiateValue = formatter.format( (Date) candiateValue );
						}
						else if ( candiateValue instanceof java.sql.Time )
						{
							formatter.applyPattern( "HH:mm:ss.SSS" ); //$NON-NLS-1$
							displayCandiateValue = formatter.format( (Date) candiateValue );
						}
						else if ( candiateValue instanceof Date )
						{
							formatter.applyPattern( "yyyy-MM-dd HH:mm:ss.SSS" ); //$NON-NLS-1$
							displayCandiateValue = formatter.format( (Date) candiateValue );
						}
						else
							displayCandiateValue = candiateValue;
						viewerValueList.add( displayCandiateValue.toString( ) );
						selectValueList.add( displayCandiateValue.toString( ) );
					}
					else
					{
						viewerValueList.add( null );
						selectValueList.add( nullValueDispaly );
					}
				}
			}
			else
			{
				selectValueList.removeAll( );
				modelValueList.clear( );
				viewerValueList.clear( );
				ExceptionHandler.openErrorMessageBox( Messages.getString( "SelectValueDialog.errorRetrievinglist" ), Messages.getString( "SelectValueDialog.noExpressionSet" ) ); //$NON-NLS-1$ //$NON-NLS-2$
			}
			if ( selectValueList.getItemCount( ) > 0 )
			{
				selectValueList.select( 0 );
				getOkButton( ).setEnabled( true );
			}
		}
		catch ( Exception e )
		{
			ExceptionHandler.handle( e );
		}
	}
}