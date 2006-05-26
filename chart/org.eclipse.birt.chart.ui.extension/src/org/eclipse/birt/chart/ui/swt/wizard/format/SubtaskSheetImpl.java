/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard.format;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.ui.swt.SheetPlaceHolder;
import org.eclipse.birt.chart.ui.swt.interfaces.ITaskPopupSheet;
import org.eclipse.birt.chart.ui.swt.wizard.ChartAdapter;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizard;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.TreeCompoundTask;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.birt.core.ui.frameworks.taskwizard.CompoundTask;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.ISubtaskSheet;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.ITask;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

/**
 * @author Actuate Corporation
 * 
 */
public class SubtaskSheetImpl implements ISubtaskSheet, ShellListener
{

	private transient String sNodePath = ""; //$NON-NLS-1$

	private transient int subtaskIndex = 0;

	protected transient Composite cmpContent = null;

	private transient ChartWizardContext context = null;

	private transient WizardBase wizard;

	private transient Shell popupShell;

	private transient ITaskPopupSheet popupSheet;

	private transient ITask parentTask;

	private static boolean POPUP_ATTACHING = false;

	private transient Map popupButtonRegistry = new HashMap( 5 );

	private transient Map popupSheetRegistry = new HashMap( 5 );

	private transient Map lastPopupRegistry = new HashMap( 3 );

	public SubtaskSheetImpl( )
	{
		super( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.ISheet#getComponent(org.eclipse.swt.widgets.Composite)
	 */
	public void getComponent( Composite parent )
	{
		cmpContent = new SheetPlaceHolder( parent, SWT.NONE, "" ); //$NON-NLS-1$
	}

	public Object onHide( )
	{
		// No need to clear popup selection because it's closed automatically
		ChartWizard.POPUP_CLOSING_BY_USER = false;
		detachPopup( );
		ChartWizard.POPUP_CLOSING_BY_USER = true;

		cmpContent.dispose( );
		popupButtonRegistry.clear( );
		popupSheetRegistry.clear( );
		return getContext( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.ISheet#setIgnoreNotifications(boolean)
	 */
	public void setIgnoreNotifications( boolean bIgnoreNotifications )
	{
		ChartAdapter.ignoreNotifications( bIgnoreNotifications );
	}

	public void onShow( Object context, Object container )
	{
		this.context = (ChartWizardContext) context;
		this.wizard = (WizardBase) container;
	}

	protected Chart getChart( )
	{
		return context.getModel( );
	}

	protected ChartWizardContext getContext( )
	{
		return context;
	}

	protected WizardBase getWizard( )
	{
		return wizard;
	}

	protected void setWizard( WizardBase wizard )
	{
		this.wizard = wizard;
	}

	/**
	 * Detaches the popup dialogue if the name is same with the widget. Called
	 * when clicking buttons manually.
	 * 
	 * @param widget
	 *            the button widget
	 * @return detach result
	 */
	protected boolean detachPopup( Widget widget )
	{
		if ( widget instanceof Button
				&& popupShell != null && !popupShell.isDisposed( )
				&& !isButtonSelected( ) )
		{
			getWizard( ).detachPopup( );
			popupShell = null;

			// Clear selection if user unselected the button.
			setCurrentPopupSelection( null );
			getParentTask( ).setPopupSelection( null );
			return true;
		}
		return false;
	}

	public boolean detachPopup( )
	{
		if ( popupShell != null && !popupShell.isDisposed( ) )
		{
			getWizard( ).detachPopup( );
			popupShell = null;
			return true;
		}
		return false;
	}

	protected Shell createPopupShell( )
	{
		POPUP_ATTACHING = true;
		Shell shell = getWizard( ).createPopupContainer( );
		shell.addShellListener( this );
		shell.setImage( UIHelper.getImage( "icons/obj16/chartbuilder.gif" ) ); //$NON-NLS-1$
		POPUP_ATTACHING = false;
		return shell;
	}

	/**
	 * Selects all registered buttons
	 * 
	 * @param isSelected
	 *            selection status
	 */
	final protected void selectAllButtons( boolean isSelected )
	{
		Iterator buttons = popupButtonRegistry.values( ).iterator( );
		while ( buttons.hasNext( ) )
		{
			( (Button) buttons.next( ) ).setSelection( isSelected );
		}
	}

	private boolean isButtonSelected( )
	{
		Iterator buttons = popupButtonRegistry.values( ).iterator( );
		while ( buttons.hasNext( ) )
		{
			if ( ( (Button) buttons.next( ) ).getSelection( ) )
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks whether the specified widget is registered in current subtask
	 */
	protected boolean isRegistered( Widget widget )
	{
		Iterator buttons = popupButtonRegistry.values( ).iterator( );
		while ( buttons.hasNext( ) )
		{
			if ( buttons.next( ).equals( widget ) )
			{
				return true;
			}
		}
		return false;
	}

	protected void refreshPopupSheet( )
	{
		if ( popupShell != null && !popupShell.isDisposed( ) )
		{
			popupSheet.refreshComponent( popupShell );
		}
	}

	public void setIndex( int index )
	{
		subtaskIndex = index;
	}

	protected int getIndex( )
	{
		return subtaskIndex;
	}

	/**
	 * Creates a toggle button to popup and registers it.
	 * 
	 * @param parent
	 *            control parent
	 * @param popupName
	 *            button text and registry key
	 * @param popupSheet
	 *            popup sheet
	 * @return button control
	 */
	protected Button createToggleButton( Composite parent, String popupName,
			ITaskPopupSheet popupSheet )
	{
		Button button = new Button( parent, SWT.TOGGLE );
		button.setText( popupName );

		// Use GC to calculate the button width
		GC gc = new GC( parent );
		int width = Math.max( 80, gc.textExtent( popupName ).x + 5 );
		gc.dispose( );

		GridData gd = new GridData( );
		gd.widthHint = width;
		button.setLayoutData( gd );

		popupButtonRegistry.put( popupName, button );
		popupSheetRegistry.put( popupName, popupSheet );

		return button;
	}

	public void setParentTask( ITask parentTask )
	{
		assert parentTask instanceof TreeCompoundTask;
		this.parentTask = parentTask;
	}

	protected TreeCompoundTask getParentTask( )
	{
		return (TreeCompoundTask) parentTask;
	}

	protected void switchTo( String subtaskPath )
	{
		if ( parentTask instanceof CompoundTask )
		{
			( (CompoundTask) parentTask ).switchTo( subtaskPath );
		}
	}

	public void setNodePath( String nodePath )
	{
		this.sNodePath = nodePath;
	}

	protected String getNodePath( )
	{
		return sNodePath;
	}

	public void shellActivated( ShellEvent e )
	{
		// TODO Auto-generated method stub

	}

	public void shellClosed( ShellEvent e )
	{
		Control focusControl = Display.getDefault( ).getFocusControl( );
		if ( focusControl instanceof Text )
		{
			// Focus saving the text by focus out
			focusControl.notifyListeners( SWT.FocusOut, null );
		}

		if ( e.widget.equals( popupShell ) )
		{
			if ( !POPUP_ATTACHING )
			{
				selectAllButtons( false );
			}

			if ( ChartWizard.POPUP_CLOSING_BY_USER )
			{
				// Clear selection if user closed the popup.
				setCurrentPopupSelection( null );
				getParentTask( ).setPopupSelection( null );
			}
		}
	}

	public void shellDeactivated( ShellEvent e )
	{
		// TODO Auto-generated method stub

	}

	public void shellDeiconified( ShellEvent e )
	{
		// TODO Auto-generated method stub

	}

	public void shellIconified( ShellEvent e )
	{
		// TODO Auto-generated method stub

	}

	public boolean attachPopup( String popupName )
	{
		// If general selection is null or not existent, to open subtask
		// selection.
		boolean affectTaskSelection = true;
		if ( popupName == null || !popupSheetRegistry.containsKey( popupName ) )
		{
			popupName = getCurrentPopupSelection( );
			// Keep task selection since user doesn't change it
			affectTaskSelection = false;
		}

		// If subtask selection is null, do nothing.
		if ( popupName == null )
		{
			return false;
		}

		detachPopup( );

		if ( popupSheetRegistry.containsKey( popupName ) )
		{
			// Select the button
			selectAllButtons( false );
			( (Button) popupButtonRegistry.get( popupName ) ).setSelection( true );

			// Store last popup selection
			setCurrentPopupSelection( popupName );
			if ( affectTaskSelection )
			{
				getParentTask( ).setPopupSelection( popupName );
			}

			// Open the popup
			popupShell = createPopupShell( );
			popupSheet = (ITaskPopupSheet) popupSheetRegistry.get( popupName );
			popupSheet.getUI( popupShell );

			getWizard( ).attachPopup( popupSheet.getTitle( ), -1, -1 );

			return true;
		}
		return false;
	}

	private String getCurrentPopupSelection( )
	{
		return (String) lastPopupRegistry.get( getContext( ).getWizardID( ) );
	}

	private void setCurrentPopupSelection( String lastPopup )
	{
		lastPopupRegistry.put( getContext( ).getWizardID( ), lastPopup );
	}
}