/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.data.oda.jdbc.ui.editors;

import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import org.eclipse.birt.report.data.oda.jdbc.ui.JdbcPlugin;
import org.eclipse.birt.report.data.oda.jdbc.ui.preference.externaleditor.ExternalEditorPreferenceManager;
import org.eclipse.birt.report.data.oda.jdbc.ui.preference.externaleditor.IExternalEditorPreference;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.Utility;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.ui.dialogs.properties.AbstractPropertyPage;
import org.eclipse.birt.report.designer.ui.editors.sql.SQLPartitionScanner;
import org.eclipse.birt.report.model.activity.SemanticException;
import org.eclipse.birt.report.model.api.ExtendedDataSetHandle;
import org.eclipse.birt.report.model.api.ExtendedDataSourceHandle;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.rules.DefaultPartitioner;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.LineNumberRulerColumn;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

/**
 * TODO: Please document
 * 
 * @version $Revision: #2 $ $Date: 2005/02/05 $
 */

public class SQLDataSetEditorPage extends AbstractPropertyPage implements SelectionListener
{
	private transient Document doc = null;
	private Hashtable htActions = new Hashtable( );
    private transient IExternalEditorPreference preference = null;

	/**
	 * @param pageName
	 */
	public SQLDataSetEditorPage( )
	{
		super( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.IPropertyPage#createPageControl(org.eclipse.swt.widgets.Composite)
	 */
	public Control createPageControl( Composite parent )
	{
        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        composite.setLayout(layout);
        
		CompositeRuler ruler = new CompositeRuler( );
		LineNumberRulerColumn lineNumbers = new LineNumberRulerColumn( );
		ruler.addDecorator( 0, lineNumbers );
		SourceViewer viewer = new SourceViewer( composite, ruler, SWT.H_SCROLL
				| SWT.V_SCROLL );
		viewer.configure( new JdbcSQLSourceViewerConfiguration( ( (ExtendedDataSetHandle) getContainer( ).getModel( ) ) ) );
		doc = new Document( ( (ExtendedDataSetHandle) getContainer( ).getModel( ) ).getQueryText( ) );
		DefaultPartitioner partitioner = new DefaultPartitioner( new SQLPartitionScanner( ),
				new String[]{
						SQLPartitionScanner.SINGLE_LINE_COMMENT1,
						SQLPartitionScanner.SINGLE_LINE_COMMENT2,
						SQLPartitionScanner.MULTI_LINE_COMMENT,
						IDocument.DEFAULT_CONTENT_TYPE
				} );
		partitioner.connect( doc );
		doc.setDocumentPartitioner( partitioner );
		viewer.setDocument( doc );
		viewer.getTextWidget( ).setFont( JFaceResources.getTextFont( ) );
		attachMenus( viewer );
        
        GridData data = new GridData(GridData.FILL_BOTH);
        viewer.getControl().setLayoutData(data);
        
        if(isExternalEditorConfigured())
        {
            Button btnExternalEditor = new Button(composite, SWT.NONE);
            btnExternalEditor.setText("Edit with external editor");
            btnExternalEditor.addSelectionListener(this);
        }

		return composite;
	}
    
    private final boolean isExternalEditorConfigured()
    {
        ExtendedDataSourceHandle handle = (ExtendedDataSourceHandle) ((ExtendedDataSetHandle) getContainer( ).getModel( )).getDataSource();
        String editorName = Utility.getUserProperty(handle, ExternalEditorPreferenceManager.PROPERTY_NAME_PREFIX + "externaleditortype");
        preference = ExternalEditorPreferenceManager.getInstance().getEditor(editorName); 
        return (preference != null && preference.canBeLaunched((ExtendedDataSetHandle) getContainer( ).getModel( )));
    }

	private final void attachMenus( SourceViewer viewer )
	{
		StyledText widget = viewer.getTextWidget( );
		MenuManager manager = new MenuManager( );
		Separator separator = new Separator( "undo" );//$NON-NLS-1$
		manager.add( separator );
		separator = new Separator( "copy" );//$NON-NLS-1$
		manager.add( separator );
		separator = new Separator( "select" );//$NON-NLS-1$
		manager.add( separator );
		manager.appendToGroup( "undo", getAction( "undo", viewer, JdbcPlugin.getResourceString( "sqleditor.action.undo" ), ITextOperationTarget.UNDO ) );//$NON-NLS-1$
		manager.appendToGroup( "undo", getAction( "redo", viewer, JdbcPlugin.getResourceString( "sqleditor.action.redo" ), ITextOperationTarget.REDO ) );//$NON-NLS-1$
		manager.appendToGroup( "copy", getAction( "cut", viewer, JdbcPlugin.getResourceString( "sqleditor.action.cut" ), ITextOperationTarget.CUT ) );//$NON-NLS-1$
		manager.appendToGroup( "copy", getAction( "copy", viewer, JdbcPlugin.getResourceString( "sqleditor.action.copy" ), ITextOperationTarget.COPY ) );//$NON-NLS-1$
		manager.appendToGroup( "copy", getAction( "paste", viewer, JdbcPlugin.getResourceString( "sqleditor.action.paste" ), ITextOperationTarget.PASTE ) );//$NON-NLS-1$
		manager.appendToGroup( "select", getAction( "selectall", viewer, JdbcPlugin.getResourceString( "sqleditor.action.selectAll" ), ITextOperationTarget.SELECT_ALL ) );//$NON-NLS-1$
		Menu menu = manager.createContextMenu( widget );

		manager.addMenuListener( new IMenuListener( ) {

			public void menuAboutToShow( IMenuManager manager )
			{
				Enumeration elements = htActions.elements( );
				while ( elements.hasMoreElements( ) )
				{
					SQLEditorAction action = (SQLEditorAction) elements.nextElement( );
					action.update( );
				}
			}
		} );
		widget.setMenu( menu );
	}

	private final SQLEditorAction getAction( String id, SourceViewer viewer,
			String name, int operation )
	{
		SQLEditorAction action = (SQLEditorAction) htActions.get( id );
		if ( action == null )
		{
			action = new SQLEditorAction( viewer, name, operation );
			htActions.put( id, action );
		}
		return action;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.IPropertyPage#pageActivated()
	 */
	public void pageActivated( )
	{
		getContainer( ).setMessage( JdbcPlugin.getResourceString( "dataset.editor.page.query" ), IMessageProvider.NONE );//$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.IPropertyPage#getName()
	 */
	public String getName( )
	{
		return JdbcPlugin.getResourceString( "dataset.editor.page.query" );//$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.IPropertyPage#canLeave()
	 */
	public boolean canLeave( )
	{
		try
		{
			( (ExtendedDataSetHandle) getContainer( ).getModel( ) ).setQueryText( doc.get( ) );
		}
		catch ( SemanticException e )
		{
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.IPropertyPage#performOk()
	 */
	public boolean performOk( )
	{
		try
		{
			( (ExtendedDataSetHandle) getContainer( ).getModel( ) ).setQueryText( doc.get( ) );
		}
		catch ( SemanticException e )
		{
			return false;
		}
		return true;
	}
    
    /* (non-Javadoc)
     * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetSelected(SelectionEvent e)
    {
        if(preference != null)
        {
            try
            {
                ( (ExtendedDataSetHandle) getContainer( ).getModel( ) ).setQueryText( doc.get( ) );
                String command = preference.getPreparedCommandLine((ExtendedDataSetHandle) getContainer( ).getModel( ));
                Process process = Runtime.getRuntime().exec(command);
                process.waitFor();
                FileInputStream fis = new FileInputStream(preference.getTemporaryFile());
                StringBuffer stringBuffer = new StringBuffer();
                byte[] buf = new byte[10000];
                int n = -1;
                while((n = fis.read(buf)) != -1)
                {
                    stringBuffer.append(new String(buf, 0, n));
                }
                doc.set(stringBuffer.toString());
                preference.getTemporaryFile().delete();
                
            }
            catch (Exception e1)
            {
                ExceptionHandler.handle(e1);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetDefaultSelected(SelectionEvent e)
    {
    }

}

class SQLEditorAction extends Action
{

	private int operationCode = -1;
	private SourceViewer viewer = null;

	public SQLEditorAction( SourceViewer viewer, String text, int operationCode )
	{
		super( text );
		this.operationCode = operationCode;
		this.viewer = viewer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run( )
	{
		viewer.doOperation( operationCode );
	}

	public void update( )
	{
		setEnabled( viewer.canDoOperation( operationCode ) );
	}
}
