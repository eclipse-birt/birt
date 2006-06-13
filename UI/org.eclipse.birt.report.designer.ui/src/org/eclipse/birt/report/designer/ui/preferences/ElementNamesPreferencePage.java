/*******************************************************************************
 * Copyright (c) 2004,2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.preferences;

import java.util.Arrays;
import java.util.Vector;

import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog. This page is used to modify the default name for each
 * element. Thus, a name is given when an element is created.
 */

public class ElementNamesPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage
{

	// The default width of the column
	private static final int columnWidth = Integer.parseInt( Messages.getString( "designer.preview.preference.elementname.columnwidth" ) ); //$NON-NLS-1$

	// The names of column
	private static final String elementNames[] = {
			Messages.getString( "designer.preview.preference.elementname.defaultname" ), //$NON-NLS-1$
			Messages.getString( "designer.preview.preference.elementname.customname" ), //$NON-NLS-1$
			Messages.getString( "designer.preview.preference.elementname.description" ) //$NON-NLS-1$
	};

	private ItemContentList itemContentList;
	Table table;
	TableViewer tableViewer;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents( Composite parent )
	{

		UIUtil.bindHelp( parent,
				IHelpContextIds.PREFERENCE_BIRT_ELEMENT_NAMES_ID );

		Composite mainComposite = new Composite( parent, SWT.NONE );

		GridData data = new GridData( GridData.FILL_HORIZONTAL );
		data.grabExcessHorizontalSpace = true;
		mainComposite.setLayoutData( data );
		GridLayout layout = new GridLayout( );
		mainComposite.setLayout( layout );

		// Create Table
		createTable( mainComposite );

		// Create and setup Tableviewer for the table
		createTableViewer( );
		tableViewer.setContentProvider( new ContentProvider( ) );
		tableViewer.setLabelProvider( new ElementNameLabelProvider( ) );

		// The input for the table viewer is the instance of ItemContentList
		itemContentList = new ItemContentList( );
		tableViewer.setInput( itemContentList );

		return mainComposite;
	}

	/**
	 * Creates the table
	 * 
	 * @param parent
	 *            create a table from the parent
	 * 
	 */
	private void createTable( Composite parent )
	{

		int tableStyle = SWT.SINGLE
				| SWT.BORDER
				| SWT.H_SCROLL
				| SWT.V_SCROLL
				| SWT.FULL_SELECTION
				| SWT.HIDE_SELECTION;
		table = new Table( parent, tableStyle );
		GridData data = new GridData( GridData.FILL_BOTH );
		data.grabExcessHorizontalSpace = true;
		table.setLayoutData( data );

		table.setHeaderVisible( true );
		table.setLinesVisible( true );
		TableColumn column;
		int i;
		for ( i = 0; i < elementNames.length; i++ )
		{
			column = new TableColumn( table, SWT.NONE, i );
			column.setText( elementNames[i] );
			column.setWidth( columnWidth );
		}

	}

	/**
	 * create a tableview for the existed table
	 * 
	 */
	private void createTableViewer( )
	{
		tableViewer = new TableViewer( table );
		tableViewer.setUseHashlookup( true );
		tableViewer.setColumnProperties( elementNames );

		// Create the cell editors
		CellEditor[] editors = new CellEditor[elementNames.length];

		for ( int i = 0; i < elementNames.length; i++ )
		{
			TextCellEditor textEditor = new TextCellEditor( table );
			( (Text) textEditor.getControl( ) ).setTextLimit( 60 );
			if ( i == 1 )
			{
				// assure that the CUSTOM NAME column doesn't contain
				// ReportPlugin.PREFERENCE_DELIMITER
				( (Text) textEditor.getControl( ) ).addVerifyListener(

				new VerifyListener( ) {

					public void verifyText( VerifyEvent e )
					{
						e.doit = e.text.indexOf( ReportPlugin.PREFERENCE_DELIMITER ) < 0;
					}
				} );

			}
			editors[i] = textEditor;
		}

		// Assign the cell editors to the viewer
		tableViewer.setCellEditors( editors );
		// Set the cell modifier for the viewer
		tableViewer.setCellModifier( new ElementNamesCellModifier( this ) );
	}

	/**
	 * Get the list of elementNames
	 * 
	 */
	public java.util.List getElementNames( )
	{
		return Arrays.asList( elementNames );
	}

	/**
	 * get selected item
	 * 
	 * @return selection
	 */
	public ISelection getSelection( )
	{
		return tableViewer.getSelection( );
	}

	/**
	 * Return the ExampleTaskList
	 */
	public ItemContentList getContentList( )
	{
		return itemContentList;
	}

	/**
	 * InnerClass that acts as a proxy for the ItemContentList providing content
	 * for the Table. It implements the IItemListViewer interface since it must
	 * register changeListeners with the ItemContentList
	 */
	class ContentProvider implements
			IStructuredContentProvider,
			IItemListViewer
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
		 *      java.lang.Object, java.lang.Object)
		 */
		public void inputChanged( Viewer v, Object oldInput, Object newInput )
		{
			if ( newInput != null )
				( (ItemContentList) newInput ).addChangeListener( this );
			if ( oldInput != null )
				( (ItemContentList) oldInput ).removeChangeListener( this );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
		 */
		public void dispose( )
		{
			itemContentList.removeChangeListener( this );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
		 */
		public Object[] getElements( Object parent )
		{
			return itemContentList.getContents( ).toArray( );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see ITaskListViewer#addTask(ExampleTask)
		 */
		public void addContent( ItemContent content )
		{
			tableViewer.add( content );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see ITaskListViewer#removeTask(ExampleTask)
		 */
		public void removeContent( ItemContent content )
		{
			tableViewer.remove( content );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see ITaskListViewer#updateTask(ExampleTask)
		 */
		public void updateContent( ItemContent content )
		{
			tableViewer.update( content, null );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init( IWorkbench workbench )
	{
		// Initialize the preference store we wish to use
		setPreferenceStore( ReportPlugin.getDefault( ).getPreferenceStore( ) );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
	 */
	protected void performDefaults( )
	{
		ItemContent content;

		String[] defaultNames = ReportPlugin.getDefault( )
				.getDefaultDefaultNamePreference( );
		String[] customNames = ReportPlugin.getDefault( )
				.getDefaultCustomNamePreference( );
		String[] descriptions = ReportPlugin.getDefault( )
				.getDefaultDescriptionPreference( );

		int i;
		itemContentList.clearList( );
		for ( i = 0; i < ReportPlugin.getDefault( ).getCount( ); i++ )
		{
			content = new ItemContent( customNames[i] );
			content.setDefaultName( defaultNames[i] );
			content.setDescription( descriptions[i] );
			itemContentList.addContent( content );
		}
		tableViewer.refresh( );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferencePage#performOk()
	 */
	public boolean performOk( )
	{

		StringBuffer defaultNamePreference = new StringBuffer( );
		StringBuffer customNamePreference = new StringBuffer( );
		StringBuffer descriptionPreference = new StringBuffer( );
		Vector contents = itemContentList.getContents( );
		for ( int i = 0; i < contents.size( ); i++ )
		{
			ItemContent content = (ItemContent) contents.get( i );

			defaultNamePreference.append( content.getDefaultName( ) );
			defaultNamePreference.append( ReportPlugin.PREFERENCE_DELIMITER );

			customNamePreference.append( content.getCustomName( ) );
			customNamePreference.append( ReportPlugin.PREFERENCE_DELIMITER );

			descriptionPreference.append( content.getDescription( ) );
			descriptionPreference.append( ReportPlugin.PREFERENCE_DELIMITER );
		}

		ReportPlugin.getDefault( )
				.setDefaultNamePreference( defaultNamePreference.toString( ) );
		ReportPlugin.getDefault( )
				.setCustomNamePreference( customNamePreference.toString( ) );
		ReportPlugin.getDefault( )
				.setDescriptionPreference( descriptionPreference.toString( ) );

		return super.performOk( );
	}

}
