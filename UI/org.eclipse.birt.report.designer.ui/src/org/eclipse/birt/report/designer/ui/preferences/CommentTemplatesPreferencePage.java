
package org.eclipse.birt.report.designer.ui.preferences;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class CommentTemplatesPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage
{

	private final static String ENABLE_BUTTON = Messages.getString( "org.eclipse.birt.report.designer.ui.preference.commenttemplates.enablecomment" );

	private Text commentText;

	private Button enableButton;

	public CommentTemplatesPreferencePage( )
	{
		super( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#PreferencePage(java.lang.String)
	 */
	public CommentTemplatesPreferencePage( String title )
	{
		super( title );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#PreferencePage(java.lang.String,org.eclipse.jface.resource.ImageDescriptor)
	 */
	public CommentTemplatesPreferencePage( String title, ImageDescriptor image )
	{
		super( title, image );
	}

	protected Control createContents( Composite parent )
	{
		Composite mainComposite = new Composite( parent, SWT.NULL );

		GridData data = new GridData( GridData.FILL_HORIZONTAL
				| GridData.FILL_VERTICAL
				| GridData.VERTICAL_ALIGN_BEGINNING );
		data.grabExcessHorizontalSpace = true;
		mainComposite.setLayoutData( data );

		GridLayout layout = new GridLayout( );
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.numColumns = 1;
		mainComposite.setLayout( layout );

		enableButton = new Button( mainComposite, SWT.CHECK );
		enableButton.setText( ENABLE_BUTTON );
		enableButton.setSelection( ReportPlugin.getDefault( )
				.getEnableCommentPreference( ) );

		commentText = new Text( mainComposite, SWT.BORDER | SWT.MULTI | SWT.WRAP );
		data = new GridData( GridData.FILL_BOTH  );
		data.widthHint = 250;
		commentText.setLayoutData( data );
		commentText.setText( ReportPlugin.getDefault( ).getCommentPreference( ) );

		enableButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				handleChangeCommentText( );
			}
		} );
		handleChangeCommentText( );

		return mainComposite;
	}

	private void handleChangeCommentText( )
	{
		if ( enableButton.getSelection( ) )
		{
			commentText.setEditable( true );
			commentText.setEnabled( true );
		}
		else
		{
			commentText.setEnabled( false );
			commentText.setEditable( false );
		}
	}

	public void init( IWorkbench workbench )
	{
		setPreferenceStore( ReportPlugin.getDefault( ).getPreferenceStore( ) );
	}

	protected void performDefaults( )
	{
		commentText.setText( ReportPlugin.getDefault( )
				.getDefaultCommentPreference( ));
		enableButton.setSelection( ReportPlugin.getDefault( )
				.getDefaultEnabelCommentPreference( ) );
		handleChangeCommentText( );
	}

	public boolean performOk( )
	{
		ReportPlugin.getDefault( )
				.setCommentPreference( commentText.getText( ) );
		ReportPlugin.getDefault( )
				.setEnableCommentPreference( enableButton.getSelection( ) );
		return super.performOk( );
	}

}
