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

package org.eclipse.birt.report.designer.internal.ui.editors.script;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.util.mediator.IColleague;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.views.data.DataViewPage;
import org.eclipse.birt.report.designer.internal.ui.views.data.DataViewTreeViewerPage;
import org.eclipse.birt.report.designer.internal.ui.views.outline.DesignerOutlinePage;
import org.eclipse.birt.report.designer.internal.ui.views.property.ReportPropertySheetPage;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.ProviderFactory;
import org.eclipse.birt.report.designer.ui.views.attributes.AttributeViewPage;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.ScriptDataSourceHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.views.palette.PalettePage;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.ITextListener;
import org.eclipse.jface.text.TextEvent;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.forms.editor.IFormPage;
import org.eclipse.ui.part.MultiPageEditorSite;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.texteditor.StatusTextEditor;
import org.eclipse.ui.texteditor.TextOperationAction;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;

/**
 * Main class of javaScript editor
 * 
 */

public class JSEditor extends StatusTextEditor implements
		ISelectionChangedListener,
		IColleague
{

	private static final String NO_EXPRESSION = Messages.getString( "JSEditor.Display.NoExpression" ); //$NON-NLS-1$

	private IEditorPart editingDomainEditor;

	/**
	 * Check if text setting by selection changed.
	 * <p>
	 * Do not mark dirty when text setting by selection changed.
	 * </p>
	 */
	private boolean settingText = false;

	JSEditorInput editorInput = new JSEditorInput( "" ); //$NON-NLS-1$

	Combo cmbExpList = null;

	public ComboViewer cmbExprListViewer;

	IPropertyDefn cmbItemLastSelected = null;

	boolean editorUIEnabled = true;

	private ActionRegistry actionRegistry = null;

	private Button butReset;

	private Label ano;

	private final HashMap selectionMap = new HashMap( );

	private boolean isModified;

	private JSSyntaxContext context = new JSSyntaxContext( );

	private Object editObject;

	/**
	 * Palette page
	 */
	public TreeViewPalettePage palettePage = new TreeViewPalettePage( );

	/**
	 * JSEditor - constructor
	 */
	public JSEditor( IEditorPart parent )
	{
		super( );
		this.editingDomainEditor = parent;
		setSourceViewerConfiguration( new JSSourceViewerConfiguration( context ) );
		// try
		// {
		// context.setVariable( IReportGraphicConstants.REPORT_KEY_WORD,
		// IReportContext.class ); //$NON-NLS-1$
		// }
		// catch ( ClassNotFoundException e )
		// {
		// }
		setDocumentProvider( new JSDocumentProvider( ) );
		setSite( parent.getEditorSite( ) );
	}

	/**
	 * @see AbstractTextEditor#doSave( IProgressMonitor )
	 */
	public void doSave( IProgressMonitor monitor )
	{
	}

	public boolean isDirty( )
	{
		return isCodeModified( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.editors.text.TextEditor#isSaveAsAllowed()
	 */
	public boolean isSaveAsAllowed( )
	{
		return true;
	}

	/**
	 * disposes all color objects
	 */
	public void dispose( )
	{
		// colorManager.dispose( );

		// remove the mediator listener
		// SessionHandleAdapter.getInstance( )
		// .getMediator( )
		// .removeColleague( this );
		selectionMap.clear( );
		editingDomainEditor = null;
		super.dispose( );
		// ( (ReportMultiPageEditorSite) getSite( ) ).dispose( );
		( (MultiPageEditorSite) getSite( ) ).dispose( );
	}

	public void createPartControl( Composite parent )
	{
		Composite child = this.initEditorLayout( parent );

		setInput( editorInput );

		cmbExprListViewer = new ComboViewer( cmbExpList );
		JSExpListProvider provider = new JSExpListProvider( );
		cmbExprListViewer.setContentProvider( provider );
		cmbExprListViewer.setLabelProvider( provider );

		// Initialize the model for the document.
		Object model = getModel( );
		if ( model != null )
		{
			cmbExpList.setVisible( true );
			setComboViewerInput( model );
		}
		else
		{
			setComboViewerInput( Messages.getString( "JSEditor.Input.trial" ) ); //$NON-NLS-1$
		}
		cmbExprListViewer.addSelectionChangedListener( palettePage.getSupport( ) );
		cmbExprListViewer.addSelectionChangedListener( new ISelectionChangedListener( ) {

			/**
			 * selectionChanged( event) - This listener implementation is
			 * invoked when an item in the combo box is selected, - It saves the
			 * current editor contents. - Updates the editor content with the
			 * expression corresponding to the selected method name or
			 * expression. name.
			 */
			public void selectionChanged( SelectionChangedEvent event )
			{
				ISelection selection = event.getSelection( );
				if ( selection != null )
				{
					settingText = true;
					Object[] sel = ( (IStructuredSelection) selection ).toArray( );
					if ( sel.length == 1 )
					{
						if ( sel[0] instanceof IPropertyDefn )
						{
							// Save the current expression into the DE
							// using DE
							// API
							DesignElementHandle desHandle = (DesignElementHandle) cmbExprListViewer.getInput( );
							saveModelIfNeeds( );

							// Update the editor to display the
							// expression
							// corresponding to the selected
							// combo item ( method name/ expression name
							// )
							IPropertyDefn elePropDefn = (IPropertyDefn) sel[0];
							cmbItemLastSelected = elePropDefn;
							setEditorText( desHandle.getStringProperty( elePropDefn.getName( ) ) );
							setIsModified( false );
							selectionMap.put( getModel( ), selection );

							String method = cmbItemLastSelected.getName( );
							Map argMap = DEUtil.getDesignElementMethodArguments( desHandle,
									method );
							context.clear( );
							for ( Iterator iter = argMap.entrySet( ).iterator( ); iter.hasNext( ); )
							{
								Map.Entry element = (Map.Entry) iter.next( );
								String name = (String) element.getKey( );
								String type = (String) element.getValue( );
								// try
								// {
								// Class typeClass = Class.forName( type
								// );
								// if ( typeClass ==
								// IReportElement.class
								// || typeClass ==
								// IReportElementInstance.class
								// )
								// {
								// context.setVariable( name, typeClass
								// );
								// }
								// else
								// {
								// context.removeVariable( name );
								// }
								// }
								// catch ( Exception e )
								// {
								// // class not found, may by engine
								// defined
								// objects.
								// context.setVariable( name, type );
								// }
								context.setVariable( name, type );
							}

						}
					}
					settingText = false;
				}
			}

		} );

		super.createPartControl( child );

		getViewer( ).addTextListener( new ITextListener( ) {

			public void textChanged( TextEvent event )
			{
				if ( !settingText && !isModified && event.getOffset( ) != 0 )
				{
					markDirty( );
				}
			}
		} );

		this.getSourceViewer( )
				.getTextWidget( )
				.addModifyListener( new ModifyListener( ) {

					public void modifyText( ModifyEvent e )
					{
						markDirty( );
					}
				} );

		// suport the mediator
		SessionHandleAdapter.getInstance( ).getMediator( ).addColleague( this );

		disableEditor( );
	}

	/**
	 * Get current edit element, not report design model.
	 * 
	 * @return
	 */
	public Object getModel( )
	{
		// return cmbExprListViewer.getInput( );
		return editObject;
	}

	protected void createActions( )
	{
		super.createActions( );
		IAction contentAssistAction = new TextOperationAction( Messages.getReportResourceBundle( ),
				"ContentAssistProposal_", this, ISourceViewer.CONTENTASSIST_PROPOSALS, true );//$NON-NLS-1$
		contentAssistAction.setActionDefinitionId( ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS );
		setAction( "ContentAssistProposal", contentAssistAction );//$NON-NLS-1$
		// TODO: rewirte those actions
		// Add page actions
		// Action action = LayoutPageAction.getInstance( );
		// getActionRegistry( ).registerAction( action );
		//		
		// action = NormalPageAction.getInstance( );
		// getActionRegistry( ).registerAction( action );
		//
		// action = MasterPageAction.getInstance( );
		// getActionRegistry( ).registerAction( action );
		//
		// action = PreviewPageAction.getInstance( );
		// getActionRegistry( ).registerAction( action );
		//
		// action = CodePageAction.getInstance( );
		// getActionRegistry( ).registerAction( action );
	}

	public void setAction( String actionID, IAction action )
	{
		super.setAction( actionID, action );
		if ( action.getId( ) == null )
		{
			action.setId( actionID );
		}
		getActionRegistry( ).registerAction( action );
	}

	private void updateAnnotationLabel( Object handle )
	{
		String name = ProviderFactory.createProvider( handle )
				.getNodeDisplayName( handle );

		if ( name == null )
		{
			ano.setText( "" ); //$NON-NLS-1$
		}
		else
		{
			ano.setText( name );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter( Class adapter )
	{
		if ( adapter == ActionRegistry.class )
		{
			return this.getActionRegistry( );
		}
		else if ( adapter == PalettePage.class )
		{
			if ( cmbExprListViewer != null )
			{
				cmbExprListViewer.addSelectionChangedListener( palettePage.getSupport( ) );
			}
			return palettePage;
		}

		if ( adapter == IContentOutlinePage.class )
		{

			// ( (NonGEFSynchronizerWithMutiPageEditor)
			// getSelectionSynchronizer( ) ).add( (NonGEFSynchronizer)
			// outlinePage.getAdapter( NonGEFSynchronizer.class ) );

			// Add JS Editor as a selection listener to Outline view selections.
			// outlinePage.addSelectionChangedListener( jsEditor );
			DesignerOutlinePage outlinePage = new DesignerOutlinePage( SessionHandleAdapter.getInstance( )
					.getReportDesignHandle( ) );

			return outlinePage;

		}

		// return the property sheet page
		if ( adapter == IPropertySheetPage.class )
		{
			ReportPropertySheetPage sheetPage = new ReportPropertySheetPage( );
			return sheetPage;
		}

		if ( adapter == DataViewPage.class )
		{
			DataViewTreeViewerPage page = new DataViewTreeViewerPage( SessionHandleAdapter.getInstance( )
					.getReportDesignHandle( ) );
			return page;
		}

		if ( adapter == AttributeViewPage.class )
		{
			AttributeViewPage page = new AttributeViewPage( );
			return page;
		}

		return super.getAdapter( adapter );
	}

	/**
	 * 
	 * initEditorLayout - initialize the UI components of the editor
	 * 
	 */
	private Composite initEditorLayout( Composite parent )
	{
		// Create the editor parent composite.
		Composite mainPane = new Composite( parent, SWT.NONE );
		GridLayout layout = new GridLayout( );
		layout.verticalSpacing = 0;
		mainPane.setLayout( layout );

		final Composite barPane = new Composite( mainPane, SWT.NONE );
		layout = new GridLayout( 4, false );
		barPane.setLayout( layout );
		GridData gdata = new GridData( GridData.FILL_HORIZONTAL );
		barPane.setLayoutData( gdata );

		// Create the combo box
		cmbExpList = new Combo( barPane, SWT.READ_ONLY );
		GridData layoutData = new GridData( GridData.FILL_HORIZONTAL );
		cmbExpList.setLayoutData( layoutData );

		// Creates Reset button
		butReset = new Button( barPane, SWT.PUSH );
		butReset.setText( Messages.getString( "JSEditor.Button.Reset" ) ); //$NON-NLS-1$
		layoutData = new GridData( );
		layoutData.horizontalIndent = 6;
		butReset.setLayoutData( layoutData );
		butReset.addSelectionListener( new SelectionListener( ) {

			public void widgetSelected( SelectionEvent e )
			{
				setEditorText( "" ); //$NON-NLS-1$
				markDirty( );
			}

			public void widgetDefaultSelected( SelectionEvent e )
			{
				widgetSelected( e );
			}
		} );

		Label column = new Label( barPane, SWT.SEPARATOR | SWT.VERTICAL );
		layoutData = new GridData( );
		layoutData.heightHint = 20;
		layoutData.horizontalIndent = 10;
		column.setLayoutData( layoutData );

		ano = new Label( barPane, 0 );
		layoutData = new GridData( GridData.FILL_HORIZONTAL
				| GridData.VERTICAL_ALIGN_CENTER );
		ano.setLayoutData( layoutData );

		final Composite sep = new Composite( mainPane, 0 );
		layoutData = new GridData( GridData.FILL_HORIZONTAL );
		layoutData.heightHint = 1;
		sep.setLayoutData( layoutData );
		sep.addPaintListener( new PaintListener( ) {

			public void paintControl( PaintEvent e )
			{
				GC gc = e.gc;
				Rectangle rect = sep.getBounds( );
				gc.setForeground( ColorConstants.darkGray );
				gc.drawLine( 0, 0, rect.width, 0 );
			}
		} );

		// Create the code editor pane.
		Composite jsEditorContainer = new Composite( mainPane, SWT.NONE );
		gdata = new GridData( GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL );
		jsEditorContainer.setLayoutData( gdata );
		jsEditorContainer.setLayout( new FillLayout( ) );

		return jsEditorContainer;
	}

	public void selectionChanged( SelectionChangedEvent event )
	{

	}

	/*
	 * SelectionChanged. - Selection listener implementation for changes in
	 * other views Selection of elements in other views, triggers this event. -
	 * The code editor view is updated to show the methods corresponding to the
	 * selected element.
	 */
	public void handleSelectionChanged( SelectionChangedEvent event )
	{
		ISelection selection = event.getSelection( );
		handleSelectionChanged( selection );
	}

	public void handleSelectionChanged( ISelection selection )
	{

		if ( getSourceViewer( ) == null
				|| !getSourceViewer( ).getTextWidget( ).isVisible( ) )
		{
			return;
		}
		if ( editorUIEnabled == true )
		{
			// save the previous editor content.
			// saveModelIfNeeds( );
			saveModel( );
		}

		if ( selection != null )
		{
			settingText = true;
			Object[] sel = ( (IStructuredSelection) selection ).toArray( );
			if ( sel.length == 1 && sel[0] instanceof DesignElementHandle )
			{
				// set the combo viewer input to the the selected element.
				editObject = sel[0];
				palettePage.getSupport( ).setCurrentEditObject( editObject );
				if ( editObject instanceof ExtendedItemHandle )
				{
					disableEditor( );
					cmbExpList.removeAll( );
					cmbItemLastSelected = null;
					getSourceViewer( ).getTextWidget( ).setEnabled( true );
					setEditorText( ( (ExtendedItemHandle) editObject ).getExternalScript( ) );
					context.setVariable( "this", "org.eclipse.birt.report.model.api.ExtendedItemHandle" ); //$NON-NLS-1$ //$NON-NLS-2$
				}
				else
				{
					setComboViewerInput( editObject );

					// clear the latest selected item.
					cmbItemLastSelected = null;
					setEditorText( "" ); //$NON-NLS-1$

					// enable/disable editor based on the items in the
					// expression list.
					if ( cmbExpList.getItemCount( ) > 0 )
					{
						enableEditor( );
						// Selects the first item in the expression list.
						selectItemInComboExpList( (ISelection) selectionMap.get( getModel( ) ) );
					}
					else
					{
						disableEditor( );
					}
				}
				checkDirty( );
				palettePage.getSupport( ).updateParametersTree( );
			}
			else
			{
				disableEditor( );
				cmbExpList.removeAll( );
				cmbItemLastSelected = null;
				palettePage.getSupport( ).setCurrentEditObject( null );
			}
			if ( sel.length > 0 )
			{
				updateAnnotationLabel( sel[0] );
			}
			settingText = false;
		}
	}

	private void checkDirty( )
	{

		// ( (AbstractMultiPageLayoutEditor) editingDomainEditor ).checkDirty(
		// );

	}

	private void selectItemInComboExpList( ISelection selection )
	{
		ISelection sel = selection;
		if ( sel.isEmpty( ) && cmbExpList.getItemCount( ) > 0 )
		{
			IPropertyDefn propDefn = (IPropertyDefn) cmbExprListViewer.getElementAt( 0 );
			if ( propDefn != null )
			{
				sel = new StructuredSelection( propDefn );
			}
		}
		cmbExprListViewer.setSelection( sel );
		return;
	}

	// /**
	// * selectItemInComboExpList - selects the specified input item in the
	// * expList - if input is null selects first item.
	// */
	// private void selectItemInComboExpList( IPropertyDefn propDefn )
	// {
	// if ( propDefn == null )
	// {
	// if ( cmbExpList.getItemCount( ) > 0 )
	// propDefn = (IPropertyDefn) this.cmbExprListViewer
	// .getElementAt( 0 );
	// }
	//
	// if ( propDefn != null )
	// selectItemInComboExpList( new StructuredSelection( propDefn ) );
	//
	// }

	/**
	 * setEditorText - sets the editor content.
	 * 
	 * @param text
	 */
	private void setEditorText( String text )
	{
		if ( text == null )
			text = ""; //$NON-NLS-1$
		( (JSDocumentProvider) this.getDocumentProvider( ) ).getDocument( editorInput )
				.set( text );

	}

	/**
	 * getEditorText() - gets the editor content.
	 * 
	 */
	private String getEditorText( )
	{
		return this.getDocumentProvider( ).getDocument( editorInput ).get( );
	}

	/**
	 * saveEditorContentsDE - saves the current editor contents to ROM using DE
	 * API
	 * 
	 * @param desHdl
	 * @return true if updated else false.
	 */
	private boolean saveEditorContentsDE( DesignElementHandle desHdl )
	{
		if ( desHdl != null && getEditorText( ) != null )
		{
			try
			{

				if ( desHdl instanceof ExtendedItemHandle )
				{
					( (ExtendedItemHandle) desHdl ).setExternalScript( getEditorText( ) );
				}
				else if ( cmbItemLastSelected != null )
				{

					desHdl.setStringProperty( cmbItemLastSelected.getName( ),
							getEditorText( ) );

					selectionMap.put( getModel( ),
							cmbExprListViewer.getSelection( ) );
				}

			}
			catch ( SemanticException e )
			{
				ExceptionHandler.handle( e );
				return false;
			}
		}
		return true;
	}

	/**
	 * Saves input code to model
	 * 
	 */
	public void saveModel( )
	{
		if ( isCodeModified( ) && editObject instanceof DesignElementHandle )
		{
			saveEditorContentsDE( (DesignElementHandle) editObject );
		}
		setIsModified( false );
	}

	/**
	 * @param b
	 */
	public void setIsModified( boolean b )
	{
		isModified = b;
	}

	private boolean isCodeModified( )
	{
		return isModified;
	}

	public void saveModelIfNeeds( )
	{
		if ( checkEditorActive( ) )
		{
			if ( isCodeModified( ) && editObject instanceof DesignElementHandle )
			{
				saveEditorContentsDE( (DesignElementHandle) editObject );
			}
		}
	}

	protected void markDirty( )
	{
		this.isModified = true;
		( (IFormPage) editingDomainEditor ).getEditor( )
				.editorDirtyStateChanged( );
		firePropertyChange( PROP_DIRTY );
	}

	protected boolean checkEditorActive( )
	{
		return true;
	}

	/**
	 * Enables the editor UI components
	 */
	private void enableEditor( )
	{
		if ( editorUIEnabled == false )
		{
			getSourceViewer( ).getTextWidget( ).setEnabled( true );
			cmbExpList.setEnabled( true );
			butReset.setEnabled( true );
			editorUIEnabled = true;
		}
		settingText = true;
		setEditorText( "" ); //$NON-NLS-1$
		settingText = false;
	}

	/**
	 * Disables the editor UI components
	 */
	private void disableEditor( )
	{
		if ( editorUIEnabled == true )
		{
			getSourceViewer( ).getTextWidget( ).setEnabled( false );
			cmbExpList.setEnabled( false );
			butReset.setEnabled( false );
			editorUIEnabled = false;
		}
		settingText = true;
		setEditorText( NO_EXPRESSION );
		settingText = false;
	}

	/**
	 * @return Returns the actionRegisty.
	 */
	public ActionRegistry getActionRegistry( )
	{
		if ( actionRegistry == null )
			actionRegistry = new ActionRegistry( );
		return actionRegistry;
	}

	/**
	 * Gets source viewer in the editor
	 * 
	 * @return source viewer
	 */
	public SourceViewer getViewer( )
	{
		return (SourceViewer) getSourceViewer( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.core.util.mediator.IColleague#performRequest(org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest)
	 */
	public void performRequest( ReportRequest request )
	{
		if ( ReportRequest.SELECTION.equals( request.getType( ) ) )
		{
			handleSelectionChange( request.getSelectionModelList( ) );
		}
		if ( ReportRequest.CREATE_ELEMENT.equals( request.getType( ) )
				&& request.getSelectionModelList( ).get( 0 ) instanceof ScriptDataSourceHandle )
		{
			handleSelectionChange( request.getSelectionModelList( ) );
		}
	}

	private void setComboViewerInput( Object model )
	{
		cmbExprListViewer.setInput( model );

		Object oldSelection = selectionMap.get( model );

		if ( oldSelection == null )
		{
			selectItemInComboExpList( new StructuredSelection( ) );
		}
		else
		{
			selectItemInComboExpList( (ISelection) oldSelection );
		}
		return;
	}

	private void setComboViewerInput( String message )
	{
		cmbExprListViewer.setInput( message );
		return;
	}

	/**
	 * Reset the selection forcely.
	 * 
	 * @param list
	 */
	public void handleSelectionChange( List list )
	{
		SelectionChangedEvent event = new SelectionChangedEvent( getSelectionProvider( ),
				new StructuredSelection( list ) );
		handleSelectionChanged( event );
	}

}

/**
 * class JSExpListProvider - Is the content and label provider for the
 * expression list
 * 
 */

class JSExpListProvider implements IStructuredContentProvider, ILabelProvider
{

	private static final String NO_TEXT = Messages.getString( "JSEditor.Text.NoText" ); //$NON-NLS-1$;

	public Object[] getElements( Object inputElement )
	{
		if ( inputElement instanceof DesignElementHandle )
		{
			DesignElementHandle eleHandle = (DesignElementHandle) inputElement;
			if ( eleHandle.getDefn( ) != null )
			{
				// Add methods only
				return eleHandle.getDefn( ).getMethods( ).toArray( );
			}
		}
		return new Object[]{};
	}

	public void dispose( )
	{

	}

	public void inputChanged( Viewer viewer, Object oldInput, Object newInput )
	{
		viewer.refresh( );

	}

	public String getText( Object element )
	{
		if ( element instanceof IPropertyDefn )
		{
			IPropertyDefn eleDef = (IPropertyDefn) element;
			return eleDef.getName( );
		}
		return NO_TEXT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
	 */
	public Image getImage( Object element )
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 */
	public void addListener( ILabelProviderListener listener )
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object,
	 *      java.lang.String)
	 */
	public boolean isLabelProperty( Object element, String property )
	{
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 */
	public void removeListener( ILabelProviderListener listener )
	{

	}
}