/*
 *************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *  
 *************************************************************************
 */
package org.eclipse.birt.report.data.oda.jdbc.dbprofile.ui.internal.sqb;

import org.eclipse.datatools.modelbase.sql.query.QueryStatement;
import org.eclipse.datatools.sqltools.sqlbuilder.IContentChangeListener;
import org.eclipse.datatools.sqltools.sqlbuilder.SQLBuilder;
import org.eclipse.datatools.sqltools.sqlbuilder.input.ISQLBuilderEditorInput;
import org.eclipse.datatools.sqltools.sqlbuilder.input.SQLBuilderStorageEditorInput;
import org.eclipse.datatools.sqltools.sqlbuilder.util.SQLBuilderEditorInputUtil;
import org.eclipse.datatools.sqltools.sqlbuilder.sqlbuilderdialog.SQLBuilderDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.XMLMemento;

/**
 * Extends the SQB dialog that hosts the SQLBuilder and the ResultsView in a dialog
 * to use a SQLBuilderStorageEditorInput as the SQLBuilder input.
 */
public class CustomSQLBuilderDialog 
    implements IContentChangeListener
{
    private static final String DIRTY_STATUS_MARK = "*"; //$NON-NLS-1$
	private String m_savedSQBState;
	private SQLBuilderDialogWrapper m_dialog;
	public CustomSQLBuilderDialog( Shell parentShell ) 
	{
		m_dialog = new SQLBuilderDialogWrapper( parentShell );
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.datatools.sqltools.sqlbuilder.sqlbuilderdialog.SQLBuilderDialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
    public Control createDialogArea( Composite parent ) 
    {
        Control dialogArea = m_dialog.createDialogArea( parent );
        m_dialog.getSQLBuilder( ).addContentChangeListener( this );
        return dialogArea;
    }
  
  	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.report.data.oda.jdbc.dbprofile.ui.internal.sqb.SQLBuilderDialog#setInput(org.eclipse.datatools.sqltools.sqlbuilder.input.ISQLBuilderEditorInput)
	 */
	public boolean setInput( ISQLBuilderEditorInput editorInput )
	{
	    try
        {
            return m_dialog.setInput( editorInput );
        }
        catch( RuntimeException ex )
        {
            // TODO logging
            ex.printStackTrace();
            return false;
        }
	}

    String getSavedSQBState()
    {
        return m_savedSQBState;
    }

	String saveSQBState( String sqbInputName )
	{
        m_savedSQBState = getSQLBuilderState( sqbInputName );
        return m_savedSQBState;
	}
	
    private String getSQLBuilderState( String sqbInputName )
    {
        SQLBuilder sqlBuilder = m_dialog.getSQLBuilder( );
        if( sqlBuilder == null )
            return null;    // no SQLBuilder to get state from
        
        /*
         * Create a SQLBuilderStorageEditorInput and save the SQLStatement,
         * ConnectionInfo OmitSchemaInfo InputUsageOptions and
         * WindowStateInfo from the SQLBuilder in it
         */
        SQLBuilderStorageEditorInput storageEditorInput = new SQLBuilderStorageEditorInput(
                sqbInputName, sqlBuilder.getSQL() );
        // Set the SQLBuilderStorageEditorInput's connectionInfo
        storageEditorInput.setConnectionInfo( sqlBuilder.getConnectionInfo() );
        // Set the SQLBuilderStorageEditorInput's OmitSchemaInfo
        storageEditorInput.setOmitSchemaInfo( sqlBuilder.getOmitSchemaInfo() );
        // Set the SQLBuilderStorageEditorInput's InputUsageOptions
        storageEditorInput.setInputUsageOptions( sqlBuilder.getEditorInputUsageOptions() );
        // Set the SQLBuilderStorageEditorInput's WindowStateInfo
        storageEditorInput.setWindowStateInfo( sqlBuilder.getWindowStateInfo() );

        // Save the state of the SQLBuilderStorageEditorInput to a XMLMemento
        XMLMemento memento = 
            SQLBuilderEditorInputUtil.saveSQLBuilderStorageEditorInput( storageEditorInput );
        // Write out memento to a string 
        String sqbState = SQLBuilderEditorInputUtil.writeXMLMementoToString( memento );
        return sqbState;
    }
    
    QueryStatement getSQLQueryStatement() 
    {
        return m_dialog.getSQLBuilder( ).getDomainModel().getSQLStatement();
    }

    /**
     * Marks the dialog to have a changed state.
     * @param dirty
     */
    public void setDirty( boolean dirty )
    {
    	m_dialog.getSQLBuilder( ).setDirty( dirty );
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.datatools.sqltools.sqlbuilder.sqlbuilderdialog.SQLBuilderDialog#isDirty()
     */
    public boolean isDirty() 
    {
        return m_dialog.getSQLBuilder( ).isDirty();
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.datatools.sqltools.sqlbuilder.IContentChangeListener#notifyContentChange()
     */
    public void notifyContentChange() 
    {
        updateDirtyStatus();
    }

    private void updateDirtyStatus() 
    {
        // TODO - needs work to get the dialog shell
        Shell dialogShell = m_dialog.getShell();
        if( dialogShell == null || dialogShell.getText() == null ) 
            return;
        
        if( isDirty() ) 
        {
            if( ! dialogShell.getText().startsWith( DIRTY_STATUS_MARK ) ) 
            {
                dialogShell.setText( DIRTY_STATUS_MARK + dialogShell.getText() );
            }
        } 
        else if( dialogShell.getText().startsWith( DIRTY_STATUS_MARK ) ) 
        {
            dialogShell.setText( dialogShell.getText().substring(1) );
        }
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.datatools.sqltools.sqlbuilder.sqlbuilderdialog.SQLBuilderDialog#close()
     */
    public boolean close()
    {
    	m_dialog.getSQLBuilder( ).removeContentChangeListener( this );
        return m_dialog.close();
    }

    /**
     * This is a wrapper class for SQLBuilderDialog, which provide access to some internal 
     * data members. 
     *
     */
    private class SQLBuilderDialogWrapper extends SQLBuilderDialog
    {
    	/**
    	 * 
    	 * @param parentShell
    	 */
    	protected SQLBuilderDialogWrapper( Shell parentShell )
		{
			super( parentShell );
		}

    	/**
    	 * This method add the visibility of SQLBuilderDialog.getSQLBuilder();
    	 */
		public SQLBuilder getSQLBuilder()
    	{
    		return super.getSQLBuilder( );
    	}
    	
		/**
		 * This method add the visibility of SQLBuilderDialog.setInput();
		 */
    	public boolean setInput( ISQLBuilderEditorInput editorInput )
    	{
    		return super.setInput( editorInput );
    	}
    }
}
