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
import org.eclipse.datatools.sqltools.sqlbuilder.SQLBuilder;
import org.eclipse.datatools.sqltools.sqlbuilder.input.ISQLBuilderEditorInput;
import org.eclipse.datatools.sqltools.sqlbuilder.input.SQLBuilderStorageEditorInput;
import org.eclipse.datatools.sqltools.sqlbuilder.util.SQLBuilderEditorInputUtil;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.XMLMemento;

/**
 * Extends the sample SQLBuilder in a dialog.
 * @see {@link SQLBuilderDialog}
 */
public class CustomSQLBuilderDialog extends SQLBuilderDialog 
{
	private String m_savedSQBState;

	public CustomSQLBuilderDialog( Shell parentShell ) 
	{
	    super( parentShell );
	}
	
	SQLBuilder getSQLBuilder()
	{
	    return super._sqlBuilder;
	}

	public Control createContents(Composite parent) 
	{
	    return super.createContents( parent );
	}

	/**
	 * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
	 */
	protected void createButtonsForButtonBar(Composite parent) 
	{
	    // override sample dialog to not create additional buttons
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

	public void setDirty( boolean dirty )
	{
	    getSQLBuilder().setDirty( dirty );
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
        SQLBuilder sqlBuilder = getSQLBuilder();
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
        storageEditorInput.setConnectionInfo( sqlBuilder
                .getConnectionInfo() );
        // Set the SQLBuilderStorageEditorInput's OmitSchemaInfo
        storageEditorInput.setOmitSchemaInfo( sqlBuilder
                .getOmitSchemaInfo() );
        // Set the SQLBuilderStorageEditorInput's InputUsageOptions
        storageEditorInput.setInputUsageOptions( sqlBuilder
                .getEditorInputUsageOptions() );
        // Set the SQLBuilderStorageEditorInput's WindowStateInfo
        storageEditorInput.setWindowStateInfo( sqlBuilder
                .getWindowStateInfo() );

        // Save the state of the SQLBuilderStorageEditorInput to a XMLMemento
        XMLMemento memento = 
            SQLBuilderEditorInputUtil.saveSQLBuilderStorageEditorInput( storageEditorInput );
        // Write out memento to a string 
        String sqbState = SQLBuilderEditorInputUtil.writeXMLMementoToString( memento );
        return sqbState;
    }

    String getSQL( )
    {
        return getSQLBuilder().getSQL( );
    }
    
    QueryStatement getSQLQueryStatement() 
    {
        return getSQLBuilder().getDomainModel().getSQLStatement();
    }

}
