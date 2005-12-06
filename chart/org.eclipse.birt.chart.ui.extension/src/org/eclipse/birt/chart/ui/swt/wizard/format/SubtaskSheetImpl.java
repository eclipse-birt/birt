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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.ui.swt.SheetPlaceHolder;
import org.eclipse.birt.chart.ui.swt.interfaces.ITaskPopupSheet;
import org.eclipse.birt.chart.ui.swt.wizard.ChartAdapter;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.birt.core.ui.frameworks.taskwizard.CompoundTask;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.ISubtaskSheet;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.ITask;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

/**
 * @author Actuate Corporation
 * 
 */
public class SubtaskSheetImpl implements ISubtaskSheet, DisposeListener
{

	private transient String sNodePath = ""; //$NON-NLS-1$

	private transient int subtaskIndex = 0;

	protected transient Composite cmpContent = null;

	private transient ChartWizardContext context = null;

	private transient WizardBase wizard;

	protected transient Shell popupShell;

	protected transient ITaskPopupSheet popupSheet;

	private transient ITask parentTask;

	private static boolean POPUP_ATTACHING = false;

	private transient List buttonRegistry = new ArrayList( 4 );

	public SubtaskSheetImpl( )
	{
		super( );
	}

	/**
	 * 
	 */
	public SubtaskSheetImpl( String sNodePath, String sIdentifier )
	{
		// if ( sNodePath != null )
		// {
		// this.sNodePath = sNodePath;
		// }
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
		detachPopup( );
		cmpContent.dispose( );
		buttonRegistry.clear( );
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
	 * Detaches the popup dialogue if the name is same with the widget
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
			getWizard( ).detachPopup( popupShell );
			popupShell = null;
			return true;
		}
		return false;
	}

	/**
	 * Forces the popup dialogue detached.
	 * 
	 * @return detach result
	 */
	protected boolean detachPopup( )
	{
		if ( popupShell != null && !popupShell.isDisposed( ) )
		{
			getWizard( ).detachPopup( popupShell );
			popupShell = null;
			return true;
		}
		return false;
	}

	protected Shell createPopupShell( )
	{
		POPUP_ATTACHING = true;
		Shell shell = getWizard( ).createPopupContainer( );
		shell.addDisposeListener( this );
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
		for ( int i = 0; i < buttonRegistry.size( ); i++ )
		{
			( (Button) buttonRegistry.get( i ) ).setSelection( isSelected );
		}
	}

	private boolean isButtonSelected( )
	{
		for ( int i = 0; i < buttonRegistry.size( ); i++ )
		{
			if ( ( (Button) buttonRegistry.get( i ) ).getSelection( ) )
			{
				return true;
			}
		}
		return false;
	}

	public void widgetDisposed( DisposeEvent e )
	{
		if ( e.widget.equals( popupShell ) )
		{
			if ( !POPUP_ATTACHING )
			{
				selectAllButtons( false );
			}
		}
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
	 * @param text
	 *            text
	 * @return button control
	 */
	protected Button createToggleButton( Composite parent, String text )
	{
		Button button = new Button( parent, SWT.TOGGLE );
		button.setText( text );
		if ( text.length( ) <= 15 )
		{
			GridData gd = new GridData( );
			gd.widthHint = 100;
			button.setLayoutData( gd );
		}
		buttonRegistry.add( button );
		return button;
	}

	public void setParentTask( ITask parentTask )
	{
		this.parentTask = parentTask;
	}

	protected ITask getParentTask( )
	{
		return parentTask;
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
}