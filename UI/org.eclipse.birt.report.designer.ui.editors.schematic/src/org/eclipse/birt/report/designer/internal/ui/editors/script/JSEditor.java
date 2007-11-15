/*************************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.script;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.ui.swt.custom.TextCombo;
import org.eclipse.birt.core.ui.swt.custom.TextComboViewer;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.views.outline.ScriptElementNode;
import org.eclipse.birt.report.designer.core.util.mediator.IColleague;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.views.data.DataViewPage;
import org.eclipse.birt.report.designer.internal.ui.views.data.DataViewTreeViewerPage;
import org.eclipse.birt.report.designer.internal.ui.views.outline.DesignerOutlinePage;
import org.eclipse.birt.report.designer.internal.ui.views.property.ReportPropertySheetPage;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.views.ProviderFactory;
import org.eclipse.birt.report.designer.ui.views.attributes.AttributeViewPage;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.FontManager;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.ScriptDataSourceHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.metadata.IArgumentInfo;
import org.eclipse.birt.report.model.api.metadata.IArgumentInfoList;
import org.eclipse.birt.report.model.api.metadata.IClassInfo;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IMethodInfo;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.views.palette.PalettePage;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextListener;
import org.eclipse.jface.text.IUndoManager;
import org.eclipse.jface.text.TextEvent;
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
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.IFormPage;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.MultiPageEditorSite;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;

/**
 * Main class of javaScript editor
 * 
 */

public class JSEditor extends EditorPart implements IColleague
{

	protected static Logger logger = Logger.getLogger( JSEditor.class.getName( ) );

	private static final String NO_EXPRESSION = Messages.getString( "JSEditor.Display.NoExpression" ); //$NON-NLS-1$

	static final String VIEWER_CATEGORY_KEY = "Category"; //$NON-NLS-1$

	static final String VIEWER_CATEGORY_CONTEXT = "context"; //$NON-NLS-1$

	private IEditorPart editingDomainEditor;

	Combo cmbExpList = null;

	TextCombo cmbSubFunctions = null;

	ComboViewer cmbExprListViewer;

	TextComboViewer cmbSubFunctionsViewer;

	private IPropertyDefn cmbItemLastSelected = null;

	private boolean editorUIEnabled = true;

	private Button butReset;

	private Button butValidate;

	/** the icon for validator, default hide */
	private Label validateIcon = null;

	/** the tool bar pane */
	private Composite controller = null;

	private Label ano;

	private final HashMap selectionMap = new HashMap( );

	private boolean isModified;

	private Object editObject;

	/**
	 * Palette page
	 */
	public TreeViewPalettePage palettePage = new TreeViewPalettePage( );

	/** the script editor, dosen't include controller. */
	private final IScriptEditor scriptEditor = createScriptEditor( );

	/** the script validator */
	private ScriptValidator scriptValidator = null;

	/** the flag if the text listener is enabled. */
	private boolean isTextListenerEnable = true;

	/** the listener for text chaged. */
	private final ITextListener textListener = new ITextListener( ) {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.text.ITextListener#textChanged(org.eclipse.jface.text.TextEvent)
		 */
		public void textChanged( TextEvent event )
		{
			if ( isTextListenerEnable )
			{
				markDirty( );
			}
		}
	};

	/**
	 * JSEditor - constructor
	 */
	public JSEditor( IEditorPart parent )
	{
		super( );
		this.editingDomainEditor = parent;
		setSite( parent.getEditorSite( ) );
	}

	/**
	 * Creates script editor, dosen't include controller
	 * 
	 * @return a script editor
	 */
	protected IScriptEditor createScriptEditor( )
	{
		return new ScriptEditor( );
	}

	/**
	 * @see AbstractTextEditor#doSave( IProgressMonitor )
	 */
	public void doSave( IProgressMonitor monitor )
	{
		saveModel( );
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

	// Parameter names are constructed by taking the java class name
	// and make the first letter lowercase.
	// If there are more than 2 uppercase letters, it's shortened as the list of
	// those. For instance IChartScriptContext becomes icsc

	protected static String convertToParameterName( String fullName )
	{
		// strip the full qualified name
		fullName = fullName.substring( fullName.lastIndexOf( '.' ) + 1 );
		int upCase = 0;
		SortedMap caps = new TreeMap( );
		for ( int i = 0; i < fullName.length( ); i++ )
		{
			char character = fullName.charAt( i );
			if ( Character.isUpperCase( character ) )
			{
				upCase++;
				caps.put( new Integer( i ), new Integer( character ) );

			}
		}
		if ( upCase > 2 )
		{
			StringBuffer result = new StringBuffer( );
			for ( Iterator iter = caps.values( ).iterator( ); iter.hasNext( ); )
			{
				result.append( (char) ( (Integer) iter.next( ) ).intValue( ) );
			}
			return result.toString( ).toLowerCase( );
		}
		else
			return fullName.substring( 0, 1 ).toLowerCase( )
					+ fullName.substring( 1 );
	}

	private void updateScriptContext( DesignElementHandle handle, String method )
	{
		List args = DEUtil.getDesignElementMethodArgumentsInfo( handle, method );
		JSSyntaxContext context = scriptEditor.getContext( );

		context.clear( );

		for ( Iterator iter = args.iterator( ); iter.hasNext( ); )
		{
			IArgumentInfo element = (IArgumentInfo) iter.next( );
			String name = element.getName( );
			String type = element.getType( );

			// try load system class info first, if failed, then try extension
			// class info
			if ( !context.setVariable( name, type ) )
			{
				context.setVariable( name, element.getClassType( ) );
			}
		}

		if ( handle instanceof ExtendedItemHandle )
		{
			ExtendedItemHandle exHandle = (ExtendedItemHandle) handle;

			List mtds = exHandle.getMethods( method );

			// TODO implement better function-wise code assistant.

			if ( mtds != null && mtds.size( ) > 0 )
			{
				for ( int i = 0; i < mtds.size( ); i++ )
				{
					IMethodInfo mi = (IMethodInfo) mtds.get( i );

					for ( Iterator itr = mi.argumentListIterator( ); itr.hasNext( ); )
					{
						IArgumentInfoList ailist = (IArgumentInfoList) itr.next( );

						for ( Iterator argItr = ailist.argumentsIterator( ); argItr.hasNext( ); )
						{
							IArgumentInfo aiinfo = (IArgumentInfo) argItr.next( );

							IClassInfo ci = aiinfo.getClassType( );

							String name = convertToParameterName( ci.getName( ) );

							context.setVariable( name, ci );
						}
					}
				}
			}
		}
	}

	public void createPartControl( Composite parent )
	{
		Composite child = this.initEditorLayout( parent );

		// Script combo
		cmbExprListViewer = new ComboViewer( cmbExpList );
		JSExpListProvider provider = new JSExpListProvider( );
		cmbExprListViewer.setContentProvider( provider );
		cmbExprListViewer.setLabelProvider( provider );
		cmbExprListViewer.setData( VIEWER_CATEGORY_KEY, VIEWER_CATEGORY_CONTEXT );

		// SubFunctions combo
		JSSubFunctionListProvider subProvider = new JSSubFunctionListProvider( this );

		// also add subProvider as listener of expr viewer.
		cmbExprListViewer.addSelectionChangedListener( subProvider );

		cmbSubFunctionsViewer = new TextComboViewer( cmbSubFunctions );
		cmbSubFunctionsViewer.setContentProvider( subProvider );
		cmbSubFunctionsViewer.setLabelProvider( subProvider );
		cmbSubFunctionsViewer.addSelectionChangedListener( subProvider );

		// Initialize the model for the document.
		Object model = getModel( );
		if ( model != null )
		{
			cmbExpList.setVisible( true );
			cmbSubFunctions.setVisible( true );
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
					Object[] sel = ( (IStructuredSelection) selection ).toArray( );
					if ( sel.length == 1 )
					{
						if ( sel[0] instanceof IPropertyDefn )
						{

							// Save the current expression into the DE
							// using DE
							// API
							DesignElementHandle desHandle = (DesignElementHandle) cmbExprListViewer.getInput( );
							saveModel( );

							// Update the editor to display the
							// expression
							// corresponding to the selected
							// combo item ( method name/ expression name
							// )
							IPropertyDefn elePropDefn = (IPropertyDefn) sel[0];
							cmbItemLastSelected = elePropDefn;

							setEditorText( desHandle.getStringProperty( elePropDefn.getName( ) ) );
							selectionMap.put( getModel( ), selection );

							String method = cmbItemLastSelected.getName( );

							updateScriptContext( desHandle, method );
						}
					}
				}
			}

		} );

		scriptEditor.createPartControl( child );
		scriptValidator = new ScriptValidator( getViewer( ) );

		// suport the mediator
		SessionHandleAdapter.getInstance( ).getMediator( ).addColleague( this );

		disableEditor( );
		getViewer( ).addTextListener( textListener );
	}

	/**
	 * Sets the status of the text listener.
	 * 
	 * @param enabled
	 *            <code>true</code> if enable, <code>false</code> otherwise.
	 */
	private void setTextListenerEnable( boolean enabled )
	{
		isTextListenerEnable = enabled;
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
			return scriptEditor.getActionRegistry( );
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

		controller = createController( mainPane );

		// Create the code editor pane.
		Composite jsEditorContainer = new Composite( mainPane, SWT.NONE );
		GridData gdata = new GridData( GridData.FILL_HORIZONTAL
				| GridData.FILL_VERTICAL );
		jsEditorContainer.setLayoutData( gdata );
		jsEditorContainer.setLayout( new FillLayout( ) );

		return jsEditorContainer;
	}

	/**
	 * Creates tool bar pane.
	 * 
	 * @param parent
	 *            the parent of controller
	 * @return a tool bar pane
	 */
	protected Composite createController( Composite parent )
	{
		Composite barPane = new Composite( parent, SWT.NONE );
		GridLayout layout = new GridLayout( 8, false );
		GridData gdata = new GridData( GridData.FILL_HORIZONTAL );

		barPane.setLayout( layout );
		barPane.setLayoutData( gdata );

		initScriptLabel( barPane );
		initComboBoxes( barPane );

		// Creates Reset button
		butReset = new Button( barPane, SWT.PUSH );
		butReset.setText( Messages.getString( "JSEditor.Button.Reset" ) ); //$NON-NLS-1$
		GridData layoutData = new GridData( );
		layoutData.horizontalIndent = 6;
		butReset.setLayoutData( layoutData );
		butReset.addSelectionListener( new SelectionListener( ) {

			public void widgetSelected( SelectionEvent e )
			{
				IUndoManager undo = getViewer( ).getUndoManager( );

				// Allows to undo after reseting.
				try
				{
					getViewer( ).setUndoManager( null );
					setEditorText( "" ); //$NON-NLS-1$
				}
				finally
				{
					getViewer( ).setUndoManager( undo );
				}
				markDirty( );
				setFocus( );
			}

			public void widgetDefaultSelected( SelectionEvent e )
			{
				widgetSelected( e );
			}
		} );

		// Creates Validate button
		butValidate = new Button( barPane, SWT.PUSH );
		butValidate.setText( Messages.getString( "JSEditor.Button.Validate" ) ); //$NON-NLS-1$
		layoutData = new GridData( );
		layoutData.horizontalIndent = 6;
		butValidate.setLayoutData( layoutData );
		butValidate.addSelectionListener( new SelectionAdapter( ) {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			public void widgetSelected( SelectionEvent e )
			{
				doValidate( );
			}
		} );

		// Creates Validate icon, default empty.
		validateIcon = new Label( barPane, SWT.NULL );

		Label column = new Label( barPane, SWT.SEPARATOR | SWT.VERTICAL );
		layoutData = new GridData( );
		layoutData.heightHint = 20;
		layoutData.horizontalIndent = 10;
		column.setLayoutData( layoutData );

		ano = new Label( barPane, 0 );
		layoutData = new GridData( GridData.FILL_HORIZONTAL
				| GridData.VERTICAL_ALIGN_CENTER );
		ano.setLayoutData( layoutData );

		final Composite sep = new Composite( parent, 0 );
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

		return barPane;
	}

	/**
	 * Hides validate button & icon.
	 */
	protected void hideValidateButtonIcon( )
	{
		hideControl( butValidate );
		hideControl( validateIcon );
	}

	/**
	 * Hides a control from its parent composite.
	 * 
	 * @param control
	 *            the control to hide
	 */
	private void hideControl( Control control )
	{
		Object layoutData = control.getLayoutData( );

		if ( layoutData == null )
		{
			layoutData = new GridData( );
			control.setLayoutData( layoutData );
		}

		if ( layoutData instanceof GridData )
		{
			GridData gridData = (GridData) layoutData;

			gridData.exclude = true;
			control.setLayoutData( gridData );
			control.setVisible( false );
		}
	}

	private void initScriptLabel( Composite parent )
	{
		Label lblScript = new Label( parent, SWT.NONE );
		lblScript.setText( Messages.getString( "JSEditor.Label.Script" ) ); //$NON-NLS-1$
		final FontData fd = lblScript.getFont( ).getFontData( )[0];
		Font labelFont = FontManager.getFont( fd.getName( ),
				fd.getHeight( ),
				SWT.BOLD );
		lblScript.setFont( labelFont );
		GridData layoutData = new GridData( SWT.BEGINNING );
		lblScript.setLayoutData( layoutData );

	}

	private void initComboBoxes( Composite parent )
	{

		// Create the script combo box
		cmbExpList = new Combo( parent, SWT.READ_ONLY );
		GridData layoutData = new GridData( GridData.BEGINNING );
		layoutData.widthHint = 140;
		layoutData.heightHint = 21;
		cmbExpList.setLayoutData( layoutData );

		// Create the subfunction combo box
		cmbSubFunctions = new TextCombo( parent, SWT.NONE );// SWT.DROP_DOWN |
		// SWT.READ_ONLY );
		layoutData = new GridData( GridData.BEGINNING );
		layoutData.widthHint = 160;
		layoutData.heightHint = 21;
		cmbSubFunctions.setLayoutData( layoutData );
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

		if ( editorUIEnabled == true )
		{
			saveModel( );
		}

		if ( selection != null )
		{
			Object[] sel = ( (IStructuredSelection) selection ).toArray( );
			if ( sel.length == 1 )
			{
				editObject = sel[0];
				if ( sel[0] instanceof ScriptElementNode )
				{
					editObject = ( (ScriptElementNode) editObject ).getParent( );
				}
			}
			if ( editObject instanceof DesignElementHandle )
			{
				// set the combo viewer input to the the selected element.
				palettePage.getSupport( ).setCurrentEditObject( editObject );

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

				/*
				 * if ( editObject instanceof ExtendedItemHandle ) {
				 * setEditorText( ( (ExtendedItemHandle) editObject
				 * ).getExternalScript( ) ); context.setVariable( "this",
				 * "org.eclipse.birt.report.model.api.ExtendedItemHandle" );
				 * //$NON-NLS-1$ //$NON-NLS-2$ }
				 */
				checkDirty( );
				palettePage.getSupport( ).updateParametersTree( );
			}
			else
			{
				disableEditor( );
				cmbExpList.removeAll( );
				cmbSubFunctions.setItems( null );
				cmbItemLastSelected = null;
				palettePage.getSupport( ).setCurrentEditObject( null );
			}
			if ( sel.length > 0 )
			{
				updateAnnotationLabel( sel[0] );
			}
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
		if ( scriptEditor == null )
		{
			return;
		}

		try
		{
			// Disable text listener during setting script, so that the dirty
			// flag isn't changed by program.
			setTextListenerEnable( false );
			scriptEditor.setScript( text );
			if ( scriptValidator != null )
			{
				scriptValidator.init( );
				setValidateIcon( null, null );
			}
		}
		finally
		{
			setTextListenerEnable( true );
		}
	}

	/**
	 * getEditorText() - gets the editor content.
	 * 
	 */
	private String getEditorText( )
	{
		return scriptEditor.getScript( );
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
				if ( cmbItemLastSelected != null )
				{
					desHdl.setStringProperty( cmbItemLastSelected.getName( ),
							getEditorText( ) );

				}
				selectionMap.put( getModel( ), cmbExprListViewer.getSelection( ) );
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

		( (IFormPage) editingDomainEditor ).getEditor( )
				.editorDirtyStateChanged( );
		firePropertyChange( PROP_DIRTY );
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

	protected void markDirty( )
	{
		if ( !isModified )
		{
			setIsModified( true );
			( (IFormPage) editingDomainEditor ).getEditor( )
					.editorDirtyStateChanged( );

			firePropertyChange( PROP_DIRTY );
		}
	}

	/**
	 * Enables the editor UI components
	 */
	private void enableEditor( )
	{
		if ( editorUIEnabled == false )
		{
			getViewer( ).getTextWidget( ).setEnabled( true );
			cmbExpList.setEnabled( true );
			butReset.setEnabled( true );
			butValidate.setEnabled( true );
			editorUIEnabled = true;
		}
		setEditorText( "" ); //$NON-NLS-1$
	}

	/**
	 * Disables the editor UI components
	 */
	private void disableEditor( )
	{
		if ( editorUIEnabled == true )
		{
			getViewer( ).getTextWidget( ).setEnabled( false );
			cmbExpList.setEnabled( false );
			cmbSubFunctions.setEnabled( false );
			butReset.setEnabled( false );
			butValidate.setEnabled( false );
			editorUIEnabled = false;
		}
		setEditorText( NO_EXPRESSION );
	}

	/**
	 * Gets source viewer in the editor
	 * 
	 * @return source viewer
	 */
	public SourceViewer getViewer( )
	{
		return (SourceViewer) scriptEditor.getViewer( );
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

		cmbSubFunctionsViewer.setInput( model );
		int itemCount = cmbSubFunctions.getItemCount( );
		if ( itemCount > 0 )
		{
			cmbSubFunctions.select( 0 ); // select first element always
		}
		cmbSubFunctions.setEnabled( itemCount > 0 );
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
		if ( scriptEditor instanceof AbstractTextEditor )
		{
			SelectionChangedEvent event = new SelectionChangedEvent( ( (AbstractTextEditor) scriptEditor ).getSelectionProvider( ),
					new StructuredSelection( list ) );

			handleSelectionChanged( event );
		}
	}

	/**
	 * Returns the current script editor.
	 * 
	 * @return the current script editor.
	 */
	protected IScriptEditor getScriptEditor( )
	{
		return scriptEditor;
	}

	/**
	 * Validates the contents of this editor.
	 */
	public void doValidate( )
	{
		Image image = null;
		String message = null;

		if ( scriptValidator == null )
		{
			return;
		}

		try
		{
			scriptValidator.validate( );
			image = ReportPlatformUIImages.getImage( IReportGraphicConstants.ICON_SCRIPT_NOERROR );
			message = Messages.getString( "JSEditor.Validate.NoError" );
		}
		catch ( ParseException e )
		{
			image = ReportPlatformUIImages.getImage( IReportGraphicConstants.ICON_SCRIPT_ERROR );
			message = e.getLocalizedMessage( );
		}
		finally
		{
			setValidateIcon( image, message );
			setFocus( );
		}
	}

	/**
	 * Sets the validate icon with the specified image and tool tip text.
	 * 
	 * 
	 * @param image
	 *            the icon image
	 * @param tip
	 *            the tool tip text
	 */
	private void setValidateIcon( Image image, String tip )
	{
		if ( validateIcon != null )
		{
			validateIcon.setImage( image );
			validateIcon.setToolTipText( tip );
			if ( controller != null )
			{
				controller.layout( );
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	public void doSaveAs( )
	{
		scriptEditor.doSaveAs( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#init(org.eclipse.ui.IEditorSite,
	 *      org.eclipse.ui.IEditorInput)
	 */
	public void init( IEditorSite site, IEditorInput input )
			throws PartInitException
	{
		setSite( site );
		setInput( input );
		scriptEditor.init( site, input );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	public void setFocus( )
	{
		scriptEditor.setFocus( );
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
		if ( inputElement instanceof ExtendedItemHandle )
		{
			ExtendedItemHandle extHandle = (ExtendedItemHandle) inputElement;
			List methods = extHandle.getMethods( );
			List returnList = new ArrayList( );
			for ( Iterator iter = methods.iterator( ); iter.hasNext( ); )
			{
				IElementPropertyDefn method = (IElementPropertyDefn) iter.next( );
				if ( extHandle.getMethods( method.getName( ) ) != null )
				// TODO user visibility to filter context list instead
				// subfunction count.
				// if ( extHandle.getElement( )
				// .getDefn( )
				// .isPropertyVisible( method.getName( ) ) )
				{
					returnList.add( method );
				}
			}
			return returnList.toArray( );
		}
		else if ( inputElement instanceof DesignElementHandle )
		{
			DesignElementHandle eleHandle = (DesignElementHandle) inputElement;
			if ( eleHandle.getDefn( ) != null )
			{
				// Add methods only
				// return eleHandle.getDefn( ).getMethods( ).toArray( );
				return eleHandle.getMethods( ).toArray( );
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

class JSSubFunctionListProvider implements
		IStructuredContentProvider,
		ILabelProvider,
		ISelectionChangedListener
{

	protected static Logger logger = Logger.getLogger( JSSubFunctionListProvider.class.getName( ) );

	// private static final String NO_TEXT = Messages.getString(
	// "JSEditor.Text.NoText" ); //$NON-NLS-1$;
	private JSEditor editor;

	public JSSubFunctionListProvider( JSEditor editor )
	{
		this.editor = editor;
	}

	public Object[] getElements( Object inputElement )
	{
		List elements = new ArrayList( );

		if ( inputElement instanceof ExtendedItemHandle )
		{
			int selectedIndex = editor.cmbExpList.getSelectionIndex( );
			if ( selectedIndex >= 0 )
			{
				String scriptName = editor.cmbExpList.getItem( editor.cmbExpList.getSelectionIndex( ) );

				ExtendedItemHandle extHandle = (ExtendedItemHandle) inputElement;
				List methods = extHandle.getMethods( scriptName );

				if ( methods != null )
				{
					elements.add( 0,
							Messages.getString( "JSEditor.cmb.NewEventFunction" ) ); //$NON-NLS-1$
					elements.addAll( methods );
				}
			}
		}

		return elements.toArray( );
	}

	public void dispose( )
	{
	}

	public void inputChanged( Viewer viewer, Object oldInput, Object newInput )
	{
		if ( newInput != null )
			viewer.refresh( );

	}

	public Image getImage( Object element )
	{
		return null;
	}

	public String getText( Object element )
	{
		if ( element instanceof IMethodInfo )
		{
			IMethodInfo eleDef = (IMethodInfo) element;
			return "  " + eleDef.getName( );//$NON-NLS-1$
		}
		else if ( element instanceof String )
		{
			return (String) element;
		}
		return ""; //$NON-NLS-1$
	}

	public void addListener( ILabelProviderListener listener )
	{
	}

	public boolean isLabelProperty( Object element, String property )
	{
		return false;
	}

	public void removeListener( ILabelProviderListener listener )
	{

	}

	public void selectionChanged( SelectionChangedEvent event )
	{
		boolean isContextChange = false;

		if ( event.getSource( ) instanceof ComboViewer )
		{
			isContextChange = JSEditor.VIEWER_CATEGORY_CONTEXT.equals( ( (ComboViewer) event.getSource( ) ).getData( JSEditor.VIEWER_CATEGORY_KEY ) );
		}

		ISelection selection = event.getSelection( );
		if ( selection != null )
		{

			Object[] sel = ( (IStructuredSelection) selection ).toArray( );
			if ( sel.length == 1 )
			{
				if ( isContextChange )
				{
					editor.cmbSubFunctionsViewer.refresh( );
					int itemCount = editor.cmbSubFunctions.getItemCount( );
					if ( itemCount > 0 )
					{
						// select first element always
						editor.cmbSubFunctions.select( 0 );
					}
					editor.cmbSubFunctions.setEnabled( itemCount > 0 );
				}
				else
				{
					if ( sel[0] instanceof IMethodInfo )
					{
						IMethodInfo methodInfo = (IMethodInfo) sel[0];

						String signature = createSignature( methodInfo );

						try
						{
							IScriptEditor viewer = editor.getScriptEditor( );

							if ( viewer instanceof AbstractTextEditor )
							{
								AbstractTextEditor editor = (AbstractTextEditor) viewer;

								IDocument doc = ( editor.getDocumentProvider( ) ).getDocument( viewer.getEditorInput( ) );
								int length = doc.getLength( );

								doc.replace( length, 0, signature );
								editor.selectAndReveal( length + 1,
										signature.length( ) );
							}
						}
						catch ( BadLocationException e )
						{
							logger.log( Level.SEVERE, e.getMessage( ), e );
						}

						editor.cmbSubFunctions.select( 0 );
					}
				}
			}
		}
	}

	// create the signature to insert in the document:
	// function functionName(param1, param2){}
	private String createSignature( IMethodInfo info )
	{
		StringBuffer signature = new StringBuffer( );
		String javaDoc = info.getJavaDoc( );
		if ( javaDoc != null && javaDoc.length( ) > 0 )
		{
			signature.append( "\n" ); //$NON-NLS-1$
			signature.append( info.getJavaDoc( ) );
		}
		signature.append( "\nfunction " ); //$NON-NLS-1$
		signature.append( info.getName( ) );
		signature.append( '(' );
		Iterator iter = info.argumentListIterator( );
		if ( iter.hasNext( ) )
		{
			// only one iteraration, we ignore overload cases for now
			// need to do multiple iterations if overloaded methods should be
			// supported

			IArgumentInfoList argumentList = (IArgumentInfoList) iter.next( );
			for ( Iterator argumentIter = argumentList.argumentsIterator( ); argumentIter.hasNext( ); )
			{
				IArgumentInfo argument = (IArgumentInfo) argumentIter.next( );

				String type = argument.getType( );
				// convert string to parameter name
				signature.append( JSEditor.convertToParameterName( type ) );
				if ( argumentIter.hasNext( ) )
				{
					signature.append( ", " );//$NON-NLS-1$
				}
			}
		}
		signature.append( ")\n{\n}\n" ); //$NON-NLS-1$
		return signature.toString( );
	}

}
