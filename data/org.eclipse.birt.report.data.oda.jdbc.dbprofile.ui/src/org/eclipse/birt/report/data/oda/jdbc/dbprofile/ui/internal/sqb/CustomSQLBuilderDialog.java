/*
 *************************************************************************
 * Copyright (c) 2008, 2009 Actuate Corporation.
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
import org.eclipse.datatools.sqltools.sqlbuilder.sqlbuilderdialog.SQLBuilderDialog;
import org.eclipse.datatools.sqltools.sqlbuilder.views.source.SQLSourceViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * Extends the SQB dialog that hosts the SQLBuilder and the ResultsView in a dialog
 * to use a SQLBuilderStorageEditorInput as the SQLBuilder input.
 */
public class CustomSQLBuilderDialog extends SQLBuilderDialog 
    implements IContentChangeListener
{
    private static final String DIRTY_STATUS_MARK = "*"; //$NON-NLS-1$
	private SQLBuilderDesignState m_savedSQBState;

	public CustomSQLBuilderDialog( Shell parentShell ) 
	{
	    super( parentShell );
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.datatools.sqltools.sqlbuilder.sqlbuilderdialog.SQLBuilderDialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
    public Control createDialogArea( Composite parent ) 
    {
        setParentShell( parent.getShell() );
        
        String origDialogTitle = parent.getShell().getText();
        Control dialogArea = super.createDialogArea( parent );
        parent.getShell().setText( origDialogTitle );   // restore the original title overriden by SQB
        
        getSQLBuilder().addContentChangeListener( this );
        return dialogArea;
    }
    
    /*
     * (non-Javadoc)
     * @see org.eclipse.datatools.sqltools.sqlbuilder.sqlbuilderdialog.SQLBuilderDialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
     */
	protected void createButtonsForButtonBar( Composite parent ) 
	{
	    // override base class dialog to not create additional buttons
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.datatools.sqltools.sqlbuilder.sqlbuilderdialog.SQLBuilderDialog#buttonPressed(int)
	 */
	protected void buttonPressed( int buttonId ) 
	{
        // override base class dialog to no-op since no additional buttons were created
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.report.data.oda.jdbc.dbprofile.ui.internal.sqb.SQLBuilderDialog#setInput(org.eclipse.datatools.sqltools.sqlbuilder.input.ISQLBuilderEditorInput)
	 */
	public boolean setInput( ISQLBuilderEditorInput editorInput )
	{
	    try
        {
            return super.setInput( editorInput );
        }
        catch( RuntimeException ex )
        {
            // TODO logging
            ex.printStackTrace();
            return false;
        }
	}

	SQLBuilderDesignState getSavedSQBState()
    {
        return m_savedSQBState;
    }

    SQLBuilderDesignState saveSQBState( String sqbInputName )
	{
        m_savedSQBState = getSQLBuilderState( sqbInputName );
        return m_savedSQBState;
	}
	
    private SQLBuilderDesignState getSQLBuilderState( String sqbInputName )
    {
        SQLBuilder sqlBuilder = getSQLBuilder();
        if( sqlBuilder == null )
            return null;    // no SQLBuilder to get state from
        
        // Create a SQBDesignerState from the SQLBuilder in this dialog area
        return new SQLBuilderDesignState( sqbInputName, sqlBuilder );
    }
    
    QueryStatement getSQLQueryStatement() 
    {
        refreshSQLQueryStatement();
        return getSQLBuilder().getDomainModel().getSQLStatement();
    }

    private void refreshSQLQueryStatement()
    {
        if( ! isDirty() )
            return;     // no need to refresh
        
        // interim workaround for Bugzilla 229229;
        // force re-parse to get an updated QueryStatement
        SQLBuilder sqlBuilder = getSQLBuilder();
        SQLSourceViewer sourceViewer = sqlBuilder.getSourceViewer();
        boolean currentTextModified = sourceViewer.isTextChanged();
        
        sourceViewer.setTextChanged( true );
        sourceViewer.setParseRequired( true );
        sqlBuilder.reparseIfRequired(); // generates a new QueryStatement 
        
        sourceViewer.setTextChanged( currentTextModified ); // restore original value
    }
    
    /**
     * Marks the dialog to have a changed state.
     * @param dirty
     */
    public void setDirty( boolean dirty )
    {
        getSQLBuilder().setDirty( dirty );
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.datatools.sqltools.sqlbuilder.sqlbuilderdialog.SQLBuilderDialog#isDirty()
     */
    public boolean isDirty() 
    {
        return getSQLBuilder().isDirty();
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
        Shell dialogShell = getParentShell();
        if( dialogShell == null || dialogShell.getText() == null ) 
            return;
        
        String currentTitle = dialogShell.getText();
        if( isDirty() ) 
        {
            if( ! currentTitle.startsWith( DIRTY_STATUS_MARK ) ) 
            {
                dialogShell.setText( DIRTY_STATUS_MARK + currentTitle );
            }
        } 
        else if( currentTitle.startsWith( DIRTY_STATUS_MARK ) ) 
        {
            dialogShell.setText( currentTitle.substring(1) );
        }
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.datatools.sqltools.sqlbuilder.sqlbuilderdialog.SQLBuilderDialog#close()
     */
    public boolean close()
    {
        getSQLBuilder().removeContentChangeListener( this );
        return super.close();
    }

}
