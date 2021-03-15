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
package org.eclipse.birt.report.designer.data.ui.dataset;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.designer.data.ui.util.Utility;
import org.eclipse.birt.report.designer.internal.ui.processor.ElementProcessorFactory;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.ParameterDialog;
import org.eclipse.birt.report.model.adapter.oda.ReportParameterAdapter;
import org.eclipse.birt.report.model.api.OdaDataSetParameterHandle;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

/**
 * Parameter combo cell editor, it enable user to select the scalarParameter
 * list from the combo list and it also allow user to create or edit a
 * scalarParameter.
 */
public class ParameterComboCellEditor extends DialogCellEditor
{

	/**
	 * The ComboBox to keep the system defined and customer defined Expression.
	 */
	private CCombo comboBox;

	/**
	 * The list of items to present in the combo box.
	 */
	private String[] items;

	/**
	 * The zero-based index of the selected item.
	 */
	private int selection;

	/**
	 * Default ComboBoxCellEditor style
	 */
	protected static final int defaultStyle = SWT.NONE;

	/**
	 * The composite to keep the combo box and button together
	 */
	private Composite composite;

	private Button btnPopup;
	
	private Listener listener;
	
	private Object obj;
	
	/**
	 * If the selection is zero, that means no scalarParmaeter selected, we
	 * should set the mode to CREATE_MODE to allow user to create a new
	 * scalarParameter. Or user could choose an exsiting report parameter to
	 * modify its attributes in EDIT_MODE
	 */
	private final static int CREATE_MODE = 0;
	private final static int EDIT_MODE = 1;
	private static String UNLINKED_REPORT_PARAM = Messages.getString( "DataSetParametersPage.reportParam.None" ); //$NON-NLS-1$
	private static Logger logger = Logger.getLogger( ParameterComboCellEditor.class.getName( ) );

	/**
	 * Creates a new dialog cell editor whose parent is given. The combo box
	 * lists is <code>null</code> initially.
	 * 
	 * @param parent
	 *            the parent control
	 */
	public ParameterComboCellEditor( Composite parent )
	{
		super( parent );
		setStyle( defaultStyle );
	}

	/**
	 * Creates a new dialog cell editor whose parent is given. The combo box
	 * lists is initialized with the given items.
	 * 
	 * @param parent
	 *            the parent control
	 * @param items
	 *            the combo box list to be initialized
	 */
	public ParameterComboCellEditor( Composite parent, String[] items )
	{
		this( parent, items, defaultStyle );
	}

	/**
	 * Creates a new dialog cell editor whose parent and style are given. The
	 * combo box lists is initialized with the given items.
	 * 
	 * @param parent
	 *            the parent control
	 * @param items
	 *            the combo box list to be initialized
	 * @param style
	 *            the style of this editor
	 */
	public ParameterComboCellEditor( Composite parent, String[] items,
			int style )
	{
		super( parent, style );
		setItems( items );
	}

	/**
	 * Returns the list of choices for the combo box
	 * 
	 * @return the list of choices for the combo box
	 */
	public String[] getItems( )
	{
		return items;
	}

	/**
	 * Sets the list of choices for the combo box
	 * 
	 * @param items
	 *            the list of choices for the combo box
	 */
	public void setItems( String[] items )
	{
		Assert.isNotNull( items );
		this.items = items;
		populateComboBoxItems( );
	}

	/**
	 * Updates the list of choices for the combo box for the current control.
	 */
	private void populateComboBoxItems( )
	{
		if ( comboBox != null && items != null )
		{
			comboBox.removeAll( );

			for ( int i = 0; i < items.length; i++ )
				comboBox.add( items[i], i );

			setValueValid( true );
			selection = 0;
		}
	}

	/*
	 * (non-Javadoc) Method declared on DialogCellEditor.
	 */
	protected Control createContents( Composite cell )
	{
		Color bg = cell.getBackground( );
		composite = new Composite( cell, getStyle( ) );
		composite.setBackground( bg );

		composite.setLayout( new FillLayout( ) );

		comboBox = new CCombo( composite, getStyle( ) );
		comboBox.setBackground( bg );
		comboBox.setFont( cell.getFont( ) );
		comboBox.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent event )
			{
				Object newValue = comboBox.getText( );
				if ( newValue != null )
				{
					boolean newValidState = isCorrect( newValue );
					if ( newValidState )
					{
						markDirty( );
						doSetValue( newValue );
					}
					else
					{
						// try to insert the current value into the error
						// message.
						setErrorMessage( MessageFormat.format( getErrorMessage( ),
								new Object[]{
									newValue.toString( )
								} ) );
					}
					fireApplyEditorValue( );
				}
			}

			public void widgetDefaultSelected( SelectionEvent e )
			{
				Object newValue = comboBox.getText( );
				if ( newValue != null )
				{
					boolean newValidState = isCorrect( newValue );
					if ( newValidState )
					{
						markDirty( );
						doSetValue( newValue );
					}
					else
					{
						// try to insert the current value into the error
						// message.
						setErrorMessage( MessageFormat.format( getErrorMessage( ),
								new Object[]{
									newValue.toString( )
								} ) );
					}
					fireApplyEditorValue( );
				}
			}
		} );
		comboBox.addFocusListener( new FocusAdapter( ) {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.FocusAdapter#focusLost(org.eclipse.swt.events.FocusEvent)
			 */
			public void focusLost( FocusEvent e )
			{
				if ( btnPopup != null
						&& !btnPopup.isFocusControl( )
						&& Display.getCurrent( ).getCursorControl( ) != btnPopup )
				{
					applyEditorValueAndDeactivate( );
					super.focusLost( e );

				}
			}

		} );
		return composite;
	}

	/**
	 * Apply the currently selected value and de-actiavate the cell editor.
	 */
	void applyEditorValueAndDeactivate( )
	{
		// must set the selection before getting value
		selection = comboBox.getSelectionIndex( );
		Object newValue = doGetValue( );

		markDirty( );
		boolean isValid = isCorrect( newValue );
		setValueValid( isValid );
		if ( !isValid )
		{
			// try to insert the current value into the error message.
			setErrorMessage( MessageFormat.format( getErrorMessage( ),
					new Object[]{
						items[selection]
					} ) );
		}
		fireApplyEditorValue( );
		deactivate( );
	}

	/*
	 * (non-Javadoc) Method declared on DialogCellEditor.
	 */
	protected Object openDialogBox( Control cellEditorWindow )
	{
		ParameterDialog dialog = null;
		ParameterHandle handle = Utility.getScalarParameter( this.comboBox.getText( ) );

		int mode = CREATE_MODE;
		if ( handle == null )
		{
			handle = (ScalarParameterHandle) ElementProcessorFactory.createProcessor( "ScalarParameter" ) //$NON-NLS-1$
					.createElement( null );
			dialog = new ParameterDialog( cellEditorWindow.getShell( ),
					Messages.getString( "ParameterGroupNodeProvider.Dialogue.ParameterNew" ) ); //$NON-NLS-1$
			if ( obj != null && obj instanceof OdaDataSetParameterHandle )
			{
				ReportParameterAdapter adapter = new ReportParameterAdapter( );
				try
				{
					adapter.updateLinkedReportParameter( (ScalarParameterHandle) handle,
							(OdaDataSetParameterHandle) obj );
				}
				catch ( SemanticException e )
				{
				}
			}
			mode = this.CREATE_MODE;
		}
		else
		{
			dialog = new ParameterDialog( cellEditorWindow.getShell( ),
					Messages.getString( "ParameterNodeProvider.dial.title.editScalar" ) ); //$NON-NLS-1$
			mode = this.EDIT_MODE;
		}

		handle.addListener( this.listener );
		dialog.setInput( handle );
		if ( dialog.open( ) == Dialog.OK )
		{
			if ( dialog.getResult( ) instanceof ParameterHandle )
			{
				ParameterHandle paramerHandle = (ParameterHandle) dialog.getResult( );
				if ( mode == this.CREATE_MODE )
				{
					SlotHandle parameterSlotHandle = Utility.getReportModuleHandle( )
							.getParameters( );
					try
					{
						parameterSlotHandle.add( paramerHandle );
						comboBox.add( paramerHandle.getQualifiedName( ) );
					}
					catch ( ContentException e )
					{
						logger.log( Level.FINE, e.getMessage( ), e );
					}
					catch ( NameException e )
					{
						logger.log( Level.FINE, e.getMessage( ), e );
					}
				}

				return paramerHandle.getQualifiedName( );
			}
		}
		setFocus( );
		handle.removeListener( this.listener );
		return null;
	}

	/*
	 * (non-Javadoc) Method declared on DialogCellEditor.
	 */
	protected void updateContents( Object value )
	{
		if ( comboBox == null )
			return;

		String text = "";//$NON-NLS-1$
		if ( value != null && !value.toString( ).trim( ).equals( "" ) ) //$NON-NLS-1$
		{
			text = value.toString( );
		}
		else
		{
			text = UNLINKED_REPORT_PARAM;
		}
		comboBox.setText( text );
	}

	public void setEnable( boolean flag )
	{
		comboBox.setEnabled( flag );
	}

	public void setInput( Object obj )
	{
		this.obj = obj;
	}
	
	/**
	 * 
	 * @param listener
	 */
	public void addScalarParmeterLister( Listener listener )
	{
		this.listener = listener; 
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.CellEditor#doSetFocus()
	 */
	protected void doSetFocus( )
	{
		comboBox.setFocus( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.CellEditor#doGetValue()
	 */
	protected Object doGetValue( )
	{
		int selection = comboBox.getSelectionIndex( );
		if ( selection == -1 )
		{
			return comboBox.getText( );
		}
		return comboBox.getItem( selection );
	}
}
