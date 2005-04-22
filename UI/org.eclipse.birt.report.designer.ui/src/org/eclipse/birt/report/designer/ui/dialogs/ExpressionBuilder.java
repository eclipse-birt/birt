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
import org.eclipse.core.runtime.CoreException;
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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
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

	/** file name of the state file */
	// Layout constant values
	private static final int SASH_WEIGHT_LEFT = 30;

	private static final int SASH_WEIGHT_RIGHT = 100 - SASH_WEIGHT_LEFT;

	/** The Left tree */
	protected Tree tree = null;

	/** The expression text area */
	protected SourceViewer expressionViewer;

	/** The expression value to return */
	protected String inputExpression = ""; //$NON-NLS-1$

	/** The sash form */
	protected SashForm sashForm;

	/** The operator buttons group */
	protected Composite opButtonsGroup;

	/** Columns, expressions and parameters available for the left tree */
	protected List choiceOfValues;

	final static String EXPRESSIONBUILDERDIALOG_SHELLNAME = Messages.getString( "ExpressionBuidler.Dialog.Title" ); //$NON-NLS-1$

	private List dataSetList = null;

	private String text;

	private ExpressionTreeSupport treeCommon = new ExpressionTreeSupport( );

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
		//create sash form
		sashForm = new SashForm( topLevel, SWT.HORIZONTAL );
		GridData data = new GridData( GridData.FILL_BOTH );
		data.widthHint = 600;
		data.heightHint = 300;
		sashForm.setLayoutData( data );

		// Create left tree
		createLeftTree( sashForm );

		// Expression Text Area:
		createExpressionViewer( sashForm );
		
		setFocus();

		// Set weight for the tree and text area.
		sashForm.setWeights( new int[]{
				SASH_WEIGHT_LEFT, SASH_WEIGHT_RIGHT
		} );

		// horizontal bar
		Label bar = new Label( parent, SWT.HORIZONTAL | SWT.SEPARATOR );
		data = new GridData( );
		data.horizontalAlignment = GridData.FILL;
		bar.setLayoutData( data );
		return topLevel;
	}

	/**
	 * Set focus on source viewer. 
	 */
	private void setFocus( )
	{
		expressionViewer.getControl().setFocus();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.dialogs.BaseDialog#initDialog()
	 */
	protected boolean initDialog( )
	{
		if ( text != null && expressionViewer != null )
		{
			expressionViewer.getTextWidget( ).setText( text );
		}

		return super.initDialog( );
	}

	/**
	 * create the left tree
	 * 
	 * @param parent
	 *            the parent composite
	 */
	private void createLeftTree( Composite parent )
	{
		tree = new Tree( parent, SWT.BORDER );
		treeCommon.setTree( tree );
		treeCommon.createOperatorsTree( );
		treeCommon.createNativeObjectsTree( );
		treeCommon.createBirtObjectsTree( );
		treeCommon.createDataSetsTree( dataSetList );
		treeCommon.createParamtersTree( );

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
		treeCommon.setExpressionViewer( expressionViewer );

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

		treeCommon.addDropSupportToViewer( );
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

	/**
	 * Sets the initial text.
	 * 
	 * @param text
	 */
	public void setText( String text )
	{
		this.text = text;
	}

}