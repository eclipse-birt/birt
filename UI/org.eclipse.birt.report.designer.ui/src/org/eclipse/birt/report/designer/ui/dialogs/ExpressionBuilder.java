/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.dialogs;

import java.util.List;
import java.util.ResourceBundle;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.internal.ui.dialogs.ExpressionTreeSupport;
import org.eclipse.birt.report.designer.internal.ui.editors.js.JSDocumentProvider;
import org.eclipse.birt.report.designer.internal.ui.editors.js.JSEditorInput;
import org.eclipse.birt.report.designer.internal.ui.editors.js.JSSourceViewerConfiguration;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.FontManager;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandManager;
import org.eclipse.ui.keys.KeySequence;
import org.eclipse.ui.keys.KeyStroke;
import org.eclipse.ui.keys.ModifierKey;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.texteditor.TextEditorAction;

/**
 * Expression Builder
 * 
 * The builder is used to build an expression.
 */
public class ExpressionBuilder extends BaseDialog
{

	private static final String LABEL_TEXT_EXPRESSION = Messages.getString( "ExpressionBuilder.Label.Expression" ); //$NON-NLS-1$
	private static final String LABEL_TEXT_AVAILABLE_OBJECTS = Messages.getString( "ExpressionBuilder.Label.AvailableObjects" ); //$NON-NLS-1$
	private static final String LABEL_TEXT_DESCRIPTION = Messages.getString( "ExpressionBuilder.Label.instruction" ); //$NON-NLS-1$
	private static final String LABEL_TEXT_HEADER = Messages.getString( "ExpressionBuilder.Label.title" ); //$NON-NLS-1$
	private static final String LABEL_TEXT_SELECTION = Messages.getString( "ExpressionBuilder.Label.Selection" ); //$NON-NLS-1$
	
	/** file name of the state file */
	// Layout constant values
	private static final int SASH_WEIGHT_LEFT = 33;

	private static final int SASH_WEIGHT_RIGHT = 100 - SASH_WEIGHT_LEFT;

	/** The expression text area */
	protected SourceViewer expressionViewer;

	/** The expression value to return */
	protected String inputExpression = ""; //$NON-NLS-1$
	
	private Label lblText;
	private Label lblTooltip;
	
	final static String EXPRESSIONBUILDERDIALOG_SHELLNAME = Messages.getString( "ExpressionBuidler.Dialog.Title" ); //$NON-NLS-1$

	private List dataSetList = null;

	/**
	 * Add extension support: mouse down to show selection status
	 */
	private ExpressionTreeSupport treeCommon = new ExpressionTreeSupport( ) {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.designer.internal.ui.dialogs.ExpressionTreeSupport#addMouseListener()
		 */
		public void addMouseListener( )
		{
			getTree( ).addMouseListener( new MouseAdapter( ) {

				/*
				 * (non-Javadoc)
				 * 
				 * @see org.eclipse.swt.events.MouseAdapter#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
				 */
				public void mouseDoubleClick( MouseEvent e )
				{
					TreeItem[] selection = getTreeSelection( );
					if ( selection == null || selection.length <= 0 )
						return;
					TreeItem item = selection[0];
					if ( item != null )
					{
						Object obj = item.getData( ITEM_DATA_KEY_TEXT );
						Boolean isEnabled = (Boolean) item.getData( ITEM_DATA_KEY_ENABLED );
						if ( obj != null && isEnabled.booleanValue( ) )
						{
							String text = (String) obj;
							insertText( text );
						}
					}
				}

				/*
				 * (non-Javadoc)
				 * 
				 * @see org.eclipse.swt.events.MouseAdapter#mouseDown(org.eclipse.swt.events.MouseEvent)
				 */
				public void mouseDown( MouseEvent e )
				{
					TreeItem[] selection = getTreeSelection( );
					if ( selection == null || selection.length <= 0 )
						return;
					TreeItem item = selection[0];
					if ( item != null )
					{
						lblText.setText( LABEL_TEXT_SELECTION + item.getText( ) );
						String tooltip = (String) item.getData( ITEM_DATA_KEY_TOOLTIP );
						if ( tooltip == null )
						{
							tooltip = ""; //$NON-NLS-1$
						}
						lblTooltip.setText( tooltip );
					}
				}
			} );
		}
	};

	class EBTextAction extends TextEditorAction
	{

		SourceViewer sourceViewer;

		int operationCode;

		public EBTextAction( ResourceBundle bundle, String prefix,
				SourceViewer sourceViewer, int operationCode )
		{
			super( bundle, prefix, null );
			this.sourceViewer = sourceViewer;
			this.operationCode = operationCode;
			update( );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ui.texteditor.IUpdate#update()
		 */
		public void update( )
		{
			if ( sourceViewer != null )
				setEnabled( sourceViewer.canDoOperation( operationCode ) );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.action.IAction#run()
		 */
		public void run( )
		{
			sourceViewer.doOperation( operationCode );
		}
	}

	/**
	 * Constructor, creates a new expression builder
	 * 
	 * @param parentShell
	 */
	public ExpressionBuilder( Shell parentShell )
	{
		super( parentShell, EXPRESSIONBUILDERDIALOG_SHELLNAME );
		setShellStyle( getShellStyle( ) | SWT.RESIZE );
	}

	/**
	 * Constructor, creates a new expression builder
	 * 
	 * @param parentShell
	 * @param initValue
	 *            the value displayed in the viewer
	 */
	public ExpressionBuilder( Shell parentShell, String initValue )
	{
		this( parentShell );

		if ( initValue != null )
		{
			inputExpression = initValue;
		}
	}

	/**
	 * Constructor, creates a new expression builder
	 * 
	 * @param initValue
	 *            the value displayed in the viewer
	 */
	public ExpressionBuilder( String initValue )
	{
		this( PlatformUI.getWorkbench( ).getDisplay( ).getActiveShell( ),
				initValue );

	}

	/**
	 * Creates and returns the contents of the upper part of this dialog (above
	 * the button bar).
	 * 
	 * @param parent
	 *            the parent composite to contain the dialog area
	 * @return the dialog area control
	 */
	protected Control createDialogArea( Composite parent )
	{
		Composite topLevel = (Composite) super.createDialogArea( parent );

		if ( dataSetList == null )
		{
			dataSetList = SessionHandleAdapter.getInstance( )
					.getReportDesignHandle( )
					.getDataSets( )
					.getContents( );
		}

		createTopArea( topLevel );
		createExpressionArea( topLevel );
		createStatusArea( topLevel );
		
		return topLevel;
	}
	
	private void createExpressionArea( Composite composite )
	{
		//	create sash form
		SashForm sashForm = new SashForm( composite, SWT.HORIZONTAL );
		GridData data = new GridData( GridData.FILL_BOTH );
		data.widthHint = 600;
		data.heightHint = 400;
		sashForm.setLayoutData( data );

		Composite c = new Composite( sashForm, SWT.NONE );
		GridLayout layout = new GridLayout( );
		layout.marginWidth = 0;
		c.setLayout( layout );
		c.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		Label label = new Label( c, SWT.NONE );
		label.setText( LABEL_TEXT_AVAILABLE_OBJECTS );
		// Create left tree
		createLeftTree( c );

		c = new Composite( sashForm, SWT.NONE );
		layout = new GridLayout( );
		layout.marginWidth = 0;
		c.setLayout( layout );
		c.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		label = new Label( c, SWT.NONE );
		label.setText( LABEL_TEXT_EXPRESSION );
		// Expression Text Area:
		createExpressionViewer( c );
		setFocus( );
		
		sashForm.setWeights( new int[]{
				SASH_WEIGHT_LEFT, SASH_WEIGHT_RIGHT
		} );
	}
	
	private void createTopArea( Composite composite )
	{
		Composite c = new Composite( composite, SWT.NONE );
		c.setBackground( ColorConstants.white );
		c.setLayout( new GridLayout( ) );

		Label title = new Label( c, SWT.NONE );
		title.setText( LABEL_TEXT_HEADER );
		title.setFont( FontManager.getFont( title.getFont( ).toString( ),
				10,
				SWT.BOLD ) );
		title.setBackground( ColorConstants.white );
		title.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		title = new Label( c, SWT.NONE );
		title.setText( LABEL_TEXT_DESCRIPTION );
		title.setFont( FontManager.getFont( title.getFont( ).toString( ),
				9,
				SWT.NORMAL ) );
		title.setBackground( ColorConstants.white );
		title.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		Label bar = new Label( composite, SWT.HORIZONTAL | SWT.SEPARATOR );
		bar.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
	}

	private void createStatusArea( Composite composite )
	{
		lblText = new Label( composite, SWT.HORIZONTAL );
		lblText.setText( LABEL_TEXT_SELECTION );
		lblText.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		lblTooltip = new Label( composite, SWT.HORIZONTAL );
		lblTooltip.setText( " " ); //$NON-NLS-1$
		lblTooltip.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		// horizontal bar
		Label bar = new Label( composite, SWT.HORIZONTAL | SWT.SEPARATOR );
		GridData data = new GridData( );
		data.horizontalAlignment = GridData.FILL;
		bar.setLayoutData( data );
	}

	/**
	 * Set focus on source viewer. 
	 */
	private void setFocus( )
	{
		expressionViewer.getControl().setFocus();
	}

	/**
	 * create the left tree
	 * 
	 * @param parent
	 *            the parent composite
	 */
	private void createLeftTree( Composite parent )
	{
		Tree tree = new Tree( parent, SWT.BORDER );
		tree.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		treeCommon.setTree( tree );
		treeCommon.createDefaultExpressionTree( dataSetList );

		// Add tool tips
		tree.setToolTipText( "" ); //$NON-NLS-1$
		treeCommon.addMouseTrackListener( );
		treeCommon.addMouseListener( );
		treeCommon.addDragSupportToTree( );
	}

	/**
	 * Create the expression text area
	 * 
	 * @param parent
	 */
	private void createExpressionViewer( Composite parent )
	{
		IVerticalRuler ruler = null;
		expressionViewer = new SourceViewer( parent, ruler, SWT.WRAP
				| SWT.MULTI
				| SWT.BORDER
				| SWT.H_SCROLL
				| SWT.V_SCROLL
				| SWT.FULL_SELECTION );
		expressionViewer.getTextWidget().setLayoutData( new GridData( GridData.FILL_BOTH ) );
		treeCommon.setExpressionViewer( expressionViewer );
		treeCommon.addDropSupportToViewer( );

		expressionViewer.configure( new JSSourceViewerConfiguration( ) );
		JSEditorInput editorInput = new JSEditorInput( inputExpression );
		JSDocumentProvider documentProvider = new JSDocumentProvider( );
		try
		{
			documentProvider.connect( editorInput );
		}
		catch ( CoreException e )
		{
			ExceptionHandler.handle( e );
		}

		final StyledText text = expressionViewer.getTextWidget( );
		IDocument document = documentProvider.getDocument( editorInput );
		expressionViewer.setDocument( document );
		text.setFont( JFaceResources.getTextFont( ) );

		//create actions for context menu and short cut keys
		ResourceBundle bundle = ResourceBundle.getBundle( "org.eclipse.ui.texteditor.EditorMessages" );//$NON-NLS-1$
		final TextEditorAction undoAction = new EBTextAction( bundle,
				"Editor.Undo.",//$NON-NLS-1$
				expressionViewer,
				ITextOperationTarget.UNDO );
		final TextEditorAction redoAction = new EBTextAction( bundle,
				"Editor.Redo.",//$NON-NLS-1$
				expressionViewer,
				ITextOperationTarget.REDO );
		final TextEditorAction cutAction = new EBTextAction( bundle,
				"Editor.Cut.",//$NON-NLS-1$
				expressionViewer,
				ITextOperationTarget.CUT );
		final TextEditorAction copyAction = new EBTextAction( bundle,
				"Editor.Copy.",//$NON-NLS-1$
				expressionViewer,
				ITextOperationTarget.COPY );
		final TextEditorAction pasteAction = new EBTextAction( bundle,
				"Editor.Paste.",//$NON-NLS-1$
				expressionViewer,
				ITextOperationTarget.PASTE );
		final TextEditorAction selectAllAction = new EBTextAction( bundle,
				"Editor.SelectAll.",//$NON-NLS-1$
				expressionViewer,
				ITextOperationTarget.SELECT_ALL );

		//Create context menu
		MenuManager menuMgr = new MenuManager( "#EB Context" );//$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown( true );
		menuMgr.addMenuListener( new IMenuListener( ) {

			public void menuAboutToShow( IMenuManager menuManager )
			{
				menuManager.add( new Separator( ITextEditorActionConstants.GROUP_UNDO ) );
				menuManager.add( new Separator( ITextEditorActionConstants.GROUP_COPY ) );
				menuManager.add( new Separator( ITextEditorActionConstants.GROUP_EDIT ) );
				undoAction.update( );
				redoAction.update( );
				copyAction.update( );
				cutAction.update( );
				pasteAction.update( );
				selectAllAction.update( );
				
				menuManager.appendToGroup( ITextEditorActionConstants.GROUP_UNDO,
						undoAction );
				menuManager.appendToGroup( ITextEditorActionConstants.GROUP_UNDO,
						redoAction );
				menuManager.appendToGroup( ITextEditorActionConstants.GROUP_COPY,
						cutAction );
				menuManager.appendToGroup( ITextEditorActionConstants.GROUP_COPY,
						copyAction );
				menuManager.appendToGroup( ITextEditorActionConstants.GROUP_COPY,
						pasteAction );
				menuManager.appendToGroup( ITextEditorActionConstants.GROUP_EDIT,
						selectAllAction );
			}
		} );
		text.setMenu( menuMgr.createContextMenu( text ) );

		//Create short cut keys for undo and redo, they can be configured in
		// preferences
		text.addKeyListener( new KeyAdapter( ) {

			public void keyPressed( KeyEvent e )
			{
				KeySequence keySeq = getKeySequenceFromKeyEvent( e );
				if ( keySeq == null )
				{
					return;
				}
				ICommandManager cmdMgr = PlatformUI.getWorkbench( )
						.getCommandSupport( )
						.getCommandManager( );
				if ( ITextEditorActionDefinitionIds.UNDO.equals( cmdMgr.getPerfectMatch( keySeq ) ) )
				{
					undoAction.update( );
					if ( undoAction.isEnabled( ) )
						undoAction.run( );
				}
				else if ( ITextEditorActionDefinitionIds.REDO.equals( cmdMgr.getPerfectMatch( keySeq ) ) )
				{
					redoAction.update( );
					if ( redoAction.isEnabled( ) )
						redoAction.run( );
				}
			}
		} );
	}

	private KeySequence getKeySequenceFromKeyEvent( KeyEvent e )
	{
		String keyString = Character.toString( (char) ( e.keyCode ^ e.stateMask ) )
				.toUpperCase( );
		if ( ( e.stateMask & SWT.CTRL ) != 0 )
			keyString = ModifierKey.CTRL.toString( )
					+ KeyStroke.KEY_DELIMITER
					+ keyString;
		if ( ( e.stateMask & SWT.ALT ) != 0 )
			keyString = ModifierKey.ALT.toString( )
					+ KeyStroke.KEY_DELIMITER
					+ keyString;
		if ( ( e.stateMask & SWT.SHIFT ) != 0 )
			keyString = ModifierKey.SHIFT.toString( )
					+ KeyStroke.KEY_DELIMITER
					+ keyString;
		if ( ( e.stateMask & SWT.COMMAND ) != 0 )
			keyString = ModifierKey.COMMAND.toString( )
					+ KeyStroke.KEY_DELIMITER
					+ keyString;
		try
		{
			return KeySequence.getInstance( KeyStroke.getInstance( keyString ) );
		}
		catch ( Exception ex )
		{
			return null;
		}
	}

	/**
	 * Close the window.
	 */
	protected void okPressed( )
	{
		setResult( expressionViewer.getDocument( ).get( ) );
		super.okPressed( );
	}

	/**
	 * Sets the usable dataset list for the builder.
	 * 
	 * @param dataSetList
	 */
	public void setDataSetList( List dataSetList )
	{
		this.dataSetList = dataSetList;
	}

}