/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.HelpEvent;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * Base class for most dialog in BIRT BaseDialog extends JFace Dialog to
 */

public abstract class BaseDialog extends Dialog
{

	/**
	 * The title of the dialog
	 */
	private String title;

	private String okLabel = IDialogConstants.OK_LABEL;

	/**
	 * The help button
	 */
	protected Button helpButton;

	private boolean helpAvailable;

	/**
	 * The OK button
	 */
	protected Button okButton;

	protected Object result;

	/**
	 * 
	 * Creates a dialog under the eclipse platform window with the given title
	 * and a help button. This constructor is equivalent to calling
	 * <code>BaseDialog( Shell parentShell, String title, true )</code>.
	 * 
	 * @param title
	 *            the title of the dialog
	 */

	protected BaseDialog( Shell parentShell, String title )
	{
		this( parentShell, title, true );
	}

	/**
	 * 
	 * Creates a dialog under the parent shell with the given title and a help
	 * button. This constructor is equivalent to calling
	 * <code>BaseDialog( Shell parentShell, String title, true )</code>.
	 * 
	 * @param title
	 *            the title of the dialog
	 */

	protected BaseDialog( String title )
	{
		this( PlatformUI.getWorkbench( ).getDisplay( ).getActiveShell( ),
				title,
				true );
	}

	/**
	 * Creates a dialog under the parent shell with the given title
	 * 
	 * @param parentShell
	 *            the parent shell
	 * @param title
	 *            the title of the dialog
	 * @param needHelp
	 *            to specify if needs a help button
	 */

	protected BaseDialog( Shell parentShell, String title, boolean needHelp )
	{
		super( parentShell );
		this.title = title;
		helpAvailable = needHelp;
	}

	/**
	 * Creates a dialog under the platform shell with the given title
	 * 
	 * @param title
	 *            the title of the dialog
	 * @param needHelp
	 *            to specify if needs a help button
	 */

	protected BaseDialog( String title, boolean needHelp )
	{
		this( PlatformUI.getWorkbench( ).getDisplay( ).getActiveShell( ),
				title,
				needHelp );
	}

	/**
	 * Opens this window, creating it first if it has not yet been created.
	 * <p>(<code>BaseDialog</code>) overrides this method to initialize the
	 * dialog after create it. If initializtion failed, the dialog will be
	 * treated as cancel button is pressed
	 * </p>
	 * 
	 * @return the return code
	 * 
	 * @see #create()
	 */
	public int open( )
	{
		if ( getShell( ) == null )
		{
			// create the window
			create( );
		}
		if ( initDialog( ) )
		{
			return super.open( );
		}

		return Dialog.CANCEL;
	}

	/**
	 * Creates and returns the contents of this dialog's button bar.
	 * <p>
	 * The <code>BaseDialog</code> implementation of this framework method
	 * prevents the buttons from aligning with the same direction in order to
	 * make Help button split with other buttons.
	 * </p>
	 * 
	 * @param parent
	 *            the parent composite to contain the button bar
	 * @return the button bar control
	 */
	protected Control createButtonBar( Composite parent )
	{
		Composite composite = (Composite) super.createButtonBar( parent );
		( (GridLayout) composite.getLayout( ) ).makeColumnsEqualWidth = false;
		composite.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_FILL ) );

		return composite;
	}

	/**
	 * Adds buttons to this dialog's button bar.
	 * <p>
	 * The <code>BaseDialog</code> overrides this framework to add Help Button
	 * to the button bar.
	 * </p>
	 * 
	 * @param parent
	 *            the button bar composite
	 */
	protected void createButtonsForButtonBar( Composite parent )
	{
		if ( helpAvailable )
		{
			helpButton = createButton( parent,
					IDialogConstants.HELP_ID,
					IDialogConstants.HELP_LABEL,
					false );
			helpButton.addHelpListener( new HelpListener( ) {

				public void helpRequested( HelpEvent e )
				{
					helpPressed( );
				}
			} );
		}
		// create OK and Cancel buttons by default
		okButton = createButton( parent, IDialogConstants.OK_ID, okLabel, true );
		createButton( parent,
				IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL,
				false );
	}

	/**
	 * Initialize the dialog after all controls have been created.The default
	 * implement of this framework method does nothing.Subclassed may override
	 * it.
	 * 
	 * @return Returns true if the dialog is initialized correctly, or false if
	 *         failed
	 */
	protected boolean initDialog( )
	{//Do nothing
		return true;
	}

	/**
	 * Sets the layout data of the button to a GridData with appropriate heights
	 * and widths.
	 * <p>
	 * The <code>BaseDialog</code> override the method in order to make Help
	 * button split with other buttons.
	 * 
	 * @param button
	 *            the button to be set layout data to
	 */
	protected void setButtonLayoutData( Button button )
	{
		GridData gridData;
		if ( button.getText( ).equals( IDialogConstants.HELP_LABEL ) )
		{
			gridData = new GridData( GridData.HORIZONTAL_ALIGN_BEGINNING
					| GridData.VERTICAL_ALIGN_CENTER );
			gridData.grabExcessHorizontalSpace = true;
		}
		else
		{
			gridData = new GridData( GridData.HORIZONTAL_ALIGN_END
					| GridData.VERTICAL_ALIGN_CENTER );
			if ( button.getText( ).equals( okLabel ) && !isHelpAvaiable( ) )
			{
				gridData.grabExcessHorizontalSpace = true;
			}
		}
		gridData.heightHint = convertVerticalDLUsToPixels( IDialogConstants.BUTTON_HEIGHT );
		int widthHint = convertHorizontalDLUsToPixels( IDialogConstants.BUTTON_WIDTH );
		gridData.widthHint = Math.max( widthHint,
				button.computeSize( SWT.DEFAULT, SWT.DEFAULT, true ).x );
		button.setLayoutData( gridData );
	}

	/**
	 * Notifies that this dialog's button with the given id has been pressed.
	 * <p>
	 * The <code>BaseDialog</code> overrides this framework method to call
	 * <code>helpPressed</code> if the help button is the pressed.
	 * </p>
	 * 
	 * @param buttonId
	 *            the id of the button that was pressed (see
	 *            <code>IDialogConstants.*_ID</code> constants)
	 */
	protected void buttonPressed( int buttonId )
	{
		if ( buttonId == IDialogConstants.HELP_ID )
		{
			helpPressed( );
		}
		else
		{
			super.buttonPressed( buttonId );
		}
	}

	/**
	 * Configures the given shell in preparation for opening this window in it.
	 * <p>
	 * The <code>BaseDialog</code> overrides this framework method sets in
	 * order to set the title of the dialog.
	 * </p>
	 * 
	 * @param shell
	 *            the shell
	 */
	protected void configureShell( Shell shell )
	{
		super.configureShell( shell );
		if ( title != null )
		{
			shell.setText( title );
		}
	}

	/**
	 * Notifies that the help button of this dialog has been pressed.
	 * <p>
	 * The <code>BaseDialog</code> default implementation of this framework
	 * method does nothing. Subclasses may override if desired.
	 * </p>
	 */
	protected void helpPressed( )
	{//Do nothing
	}

	/**
	 * Sets the title of the dialog
	 */

	public void setTitle( String newTitle )
	{
		title = newTitle;
		if ( getShell( ) != null )
		{
			getShell( ).setText( newTitle );
		}
	}

	/**
	 * Gets the title of the dialog
	 * 
	 * @return Returns the title.
	 */
	public String getTitle( )
	{
		return title;
	}

	/**
	 * Gets the Ok button
	 * 
	 * @return Returns the OK button
	 */
	protected Button getOkButton( )
	{
		return getButton( IDialogConstants.OK_ID );
	}

	/**
	 * Sets the text for OK button.
	 * 
	 * @param label
	 */
	public void setOKLabel( String label )
	{
		okLabel = label;
	}

	/**
	 * Returns if help button available.
	 * 
	 * @return true if help button available, else false.
	 */
	public boolean isHelpAvaiable( )
	{
		return helpAvailable;
	}

	/**
	 * Get the dialog result.
	 * 
	 * @return the dialog result.
	 */
	public Object getResult( )
	{
		return result;
	}

	/**
	 * Sets the dialog result.
	 * 
	 * @param value
	 */
	final protected void setResult( Object value )
	{
		result = value;
	}

}