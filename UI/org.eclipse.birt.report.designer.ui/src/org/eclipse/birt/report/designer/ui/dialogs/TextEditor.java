/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.ui.dialogs;

import java.text.Bidi;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.regex.Pattern;

import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BidiSegmentEvent;
import org.eclipse.swt.custom.BidiSegmentListener;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.ST;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.ui.texteditor.TextEditorAction;

/**
 * Dialog used to edit a text item
 */

public class TextEditor extends BaseDialog
{

	private static final String ACTION_TEXT_EDIT_DYNAMIC_TEXT = Messages.getString( "TextEditDialog.action.text.editDynamicText" ); //$NON-NLS-1$

	private static final String ACTION_TEXT_FORMAT_DATE_TIME = Messages.getString( "TextEditDialog.action.text.formatDateTime" ); //$NON-NLS-1$

	private static final String ACTION_TEXT_FORMAT_STRING = Messages.getString( "TextEditDialog.action.text.formatString" ); //$NON-NLS-1$

	private static final String ACTION_TEXT_FORMAT_NUMBER = Messages.getString( "TextEditDialog.action.text.formatNumber" ); //$NON-NLS-1$

	private static final String TOOL_TIP_TAG_FONT = Messages.getString( "TextEditDialog.toolTip.tag.font" ); //$NON-NLS-1$

	private static final String TOOL_TIP_TAG_U = Messages.getString( "TextEditDialog.toolTip.tag.u" ); //$NON-NLS-1$

	private static final String TOOL_TIP_TAG_I = Messages.getString( "TextEditDialog.toolTip.tag.i" ); //$NON-NLS-1$

	private static final String TOOL_TIP_TAG_B = Messages.getString( "TextEditDialog.toolTip.tag.b" ); //$NON-NLS-1$

	private static final String TOOL_TIP_VALUE_OF = Messages.getString( "TextEditDialog.toolTip.valueOf" ); //$NON-NLS-1$

	private static final String TOOL_TIP_TAG_DD = Messages.getString( "TextEditDialog.toolTip.tag.dd" ); //$NON-NLS-1$

	private static final String TOOL_TIP_TAG_DT = Messages.getString( "TextEditDialog.toolTip.tag.dt" ); //$NON-NLS-1$

	private static final String TOOL_TIP_TAG_LI = Messages.getString( "TextEditDialog.toolTip.tag.li" ); //$NON-NLS-1$

	private static final String TOOL_TIP_TAG_UL = Messages.getString( "TextEditDialog.toolTip.tag.ul" ); //$NON-NLS-1$

	private static final String TOOL_TIP_TAG_DL = Messages.getString( "TextEditDialog.toolTip.tag.dl" ); //$NON-NLS-1$

	private static final String TOOL_TIP_TAG_BIRT_IMAGE = Messages.getString( "TextEditDialog.toolTip.tag.birtImage" ); //$NON-NLS-1$

	private static final String TOOL_TIP_TAG_DEL = Messages.getString( "TextEditDialog.toolTip.tag.del" ); //$NON-NLS-1$

	private static final String TOOL_TIP_TAG_INS = Messages.getString( "TextEditDialog.toolTip.tag.ins" ); //$NON-NLS-1$

	private static final String TOOL_TIP_TAG_A = Messages.getString( "TextEditDialog.toolTip.tag.a" ); //$NON-NLS-1$

	private static final String TOOL_TIP_TAG_IMG = Messages.getString( "TextEditDialog.toolTip.tag.img" ); //$NON-NLS-1$

	private static final String TOOL_TIP_TAG_GENERIC_STYLE = Messages.getString( "TextEditDialog.toolTip.tag.genericStyleContainer" ); //$NON-NLS-1$

	private static final String TOOL_TIP_TAG_P = Messages.getString( "TextEditDialog.toolTip.tag.p" ); //$NON-NLS-1$

	private static final String TOOL_TIP_TAG_BR = Messages.getString( "TextEditDialog.toolTip.tag.br" ); //$NON-NLS-1$

	private static final String TOOL_TIP_TAG_CENTER = Messages.getString( "TextEditDialog.toolTip.tag.center" ); //$NON-NLS-1$

	private static final String TOOL_TIP_TAG_TT = Messages.getString( "TextEditDialog.toolTip.tag.tt" ); //$NON-NLS-1$

	private static final String TOOL_TIP_TAG_CODE = Messages.getString( "TextEditDialog.toolTip.tag.code" ); //$NON-NLS-1$

	private static final String TOOL_TIP_TAG_PRE = Messages.getString( "TextEditDialog.toolTip.tag.pre" ); //$NON-NLS-1$

	private static final String TOOL_TIP_TAG_STRONG = Messages.getString( "TextEditDialog.toolTip.tag.strong" ); //$NON-NLS-1$

	private static final String TOOL_TIP_TAG_EM = Messages.getString( "TextEditDialog.toolTip.tag.em" ); //$NON-NLS-1$

	private static final String TOOL_TIP_TAG_SUP = Messages.getString( "TextEditDialog.toolTip.tag.sup" ); //$NON-NLS-1$

	private static final String TOOL_TIP_TAG_SUB = Messages.getString( "TextEditDialog.toolTip.tag.sub" ); //$NON-NLS-1$

	private static final String TOOL_TIP_TAG_H6 = Messages.getString( "TextEditDialog.toolTip.tag.h6" ); //$NON-NLS-1$

	private static final String TOOL_TIP_TAG_H5 = Messages.getString( "TextEditDialog.toolTip.tag.h5" ); //$NON-NLS-1$

	private static final String TOOL_TIP_TAG_H4 = Messages.getString( "TextEditDialog.toolTip.tag.h4" ); //$NON-NLS-1$

	private static final String TOOL_TIP_TAG_H3 = Messages.getString( "TextEditDialog.toolTip.tag.h3" ); //$NON-NLS-1$

	private static final String TOOL_TIP_TAG_H2 = Messages.getString( "TextEditDialog.toolTip.tag.h2" ); //$NON-NLS-1$

	private static final String TOOL_TIP_TAG_H1 = Messages.getString( "TextEditDialog.toolTip.tag.h1" ); //$NON-NLS-1$

	private static final String TOOL_TIP_TEXT_REDO = Messages.getString( "TextEditDialog.toolTipText.redo" ); //$NON-NLS-1$

	private static final String TOOL_TIP_TEXT_UNDO = Messages.getString( "TextEditDialog.toolTipText.undo" ); //$NON-NLS-1$

	private static final String TOOL_TIP_TEXT_DELETE = Messages.getString( "TextEditDialog.toolTipText.delete" ); //$NON-NLS-1$

	private static final String TOOL_TIP_TEXT_PASTE = Messages.getString( "TextEditDialog.toolTipText.paste" ); //$NON-NLS-1$

	private static final String TOOL_TIP_TEXT_CUT = Messages.getString( "TextEditDialog.toolTipText.cut" ); //$NON-NLS-1$

	private static final String TOOL_TIP_TEXT_COPY = Messages.getString( "TextEditDialog.toolTipText.copy" ); //$NON-NLS-1$

	public static final String DLG_TITLE_NEW = "New Text Item";

	public static final String DLG_TITLE_EDIT = "Edit Text Item";

	private static final int FORMAT_CHOICE_INDEX_FORMATTING = 0;

	private static final int FORMAT_CHOICE_INDEX_LAYOUT = 1;

	private static final int FORMAT_CHOICE_INDEX_CONTENT = 2;

	private static final int FORMAT_CHOICE_INDEX_LISTS = 3;

	private static final int FORMAT_CHOICE_INDEX_DYNAMIC_TEXT = 4;

	private TextItemHandle handle;

	private String oldValue = ""; //$NON-NLS-1$

	private StyledText textEditor;

	private SourceViewer textViewer;

	private CCombo textTypeChoicer, formatChoicer;

	private ToolBar formatTagsBar, commonTagsBar;

	private ToolItem tagItem;

	/**
	 * Constructor
	 * 
	 * Creates a new text editor under the given parent shell with the given
	 * title to edit the given text
	 * 
	 * @param parentShell
	 *            the parent shell contains this pop-up dialog
	 * @param title
	 *            the title of dialog
	 * @param handle
	 *            the handle of the text element
	 */
	public TextEditor( Shell parentShell, String title, TextItemHandle handle )
	{

		super( parentShell, title );
		this.handle = handle;
		if ( handle.getContent( ) != null )
		{
			oldValue = handle.getContent( );
		}
	}

	/**
	 * Creates and returns the contents of the upper part of this dialog (above
	 * the button bar).
	 * <p>
	 * The <code>TextEditorDialog</code> overrides this framework method to
	 * create and return a new <code>Composite</code> with an empty tab
	 * folder.
	 * </p>
	 * 
	 * @param parent
	 *            the parent composite to contain the dialog area
	 * @return the dialog area control
	 */
	protected Control createDialogArea( Composite parent )
	{
		Composite composite = (Composite) super.createDialogArea( parent );

		GridLayout layout = (GridLayout) composite.getLayout( );
		layout.numColumns = 3;
		layout.makeColumnsEqualWidth = false;

		// create the top tool bar.
		createToolBar( composite );

		// create the horizontal separator.
		Label label = new Label( composite, SWT.SEPARATOR | SWT.HORIZONTAL );
		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 3;
		label.setLayoutData( gd );

		// create the html format bar.
		createFormatBar( composite );

		// create the text edit area.
		createTextArea( composite );

		return composite;
	}

	/**
	 * Creates the toolBar and toolItems, type choicer.
	 * 
	 * @param composite
	 *            composite to contain toolBar
	 */
	private void createToolBar( Composite composite )
	{
		ToolBar toolBar = new ToolBar( composite, SWT.FLAT );
		toolBar.setLayoutData( new GridData( ) );

		ToolItem copy = new ToolItem( toolBar, SWT.NONE );
		copy.setImage( ReportPlatformUIImages.getImage( ISharedImages.IMG_TOOL_COPY ) );
		copy.setToolTipText( TOOL_TIP_TEXT_COPY );
		copy.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				textEditor.copy( );
			}
		} );

		ToolItem cut = new ToolItem( toolBar, SWT.NONE );
		cut.setImage( ReportPlatformUIImages.getImage( ISharedImages.IMG_TOOL_CUT ) );
		cut.setToolTipText( TOOL_TIP_TEXT_CUT );
		cut.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				textEditor.cut( );
			}
		} );

		ToolItem paste = new ToolItem( toolBar, SWT.NONE );
		paste.setImage( ReportPlatformUIImages.getImage( ISharedImages.IMG_TOOL_PASTE ) );
		paste.setToolTipText( TOOL_TIP_TEXT_PASTE );
		paste.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				textEditor.paste( );
			}
		} );

		ToolItem delete = new ToolItem( toolBar, SWT.NONE );
		delete.setImage( ReportPlatformUIImages.getImage( ISharedImages.IMG_TOOL_DELETE ) );
		delete.setToolTipText( TOOL_TIP_TEXT_DELETE );
		delete.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				if ( textEditor.getSelectionCount( ) > 0 )
				{
					textEditor.insert( "" ); //$NON-NLS-1$
				}
			}
		} );

		ToolItem undo = new ToolItem( toolBar, SWT.NONE );
		undo.setImage( ReportPlatformUIImages.getImage( ISharedImages.IMG_TOOL_UNDO ) );
		undo.setToolTipText( TOOL_TIP_TEXT_UNDO );
		undo.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				textViewer.doOperation( ITextOperationTarget.UNDO );
			}
		} );

		ToolItem redo = new ToolItem( toolBar, SWT.NONE );
		redo.setImage( ReportPlatformUIImages.getImage( ISharedImages.IMG_TOOL_REDO ) );
		redo.setToolTipText( TOOL_TIP_TEXT_REDO );
		redo.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				textViewer.doOperation( ITextOperationTarget.REDO );
			}
		} );

		// vertical separator between toolItems and combo
		new ToolItem( toolBar, SWT.SEPARATOR );

		textTypeChoicer = new CCombo( composite, SWT.FLAT | SWT.READ_ONLY );
		GridData data = new GridData( GridData.HORIZONTAL_ALIGN_BEGINNING );
		data.widthHint = textTypeChoicer.computeSize( SWT.DEFAULT, SWT.DEFAULT ).x + 100;
		textTypeChoicer.setLayoutData( data );
		textTypeChoicer.setBackground( PlatformUI.getWorkbench( )
				.getDisplay( )
				.getSystemColor( SWT.COLOR_LIST_BACKGROUND ) );
		textTypeChoicer.setItems( new String[]{
				Messages.getString( "TextEditDialog.choice.plainText" ), Messages.getString( "TextEditDialog.choice.HTML" ) //$NON-NLS-1$ //$NON-NLS-2$
		} );

		textTypeChoicer.select( handle.getContentType( )
				.equals( DesignChoiceConstants.TEXT_CONTENT_TYPE_HTML ) ? 1 : 0 );

		textTypeChoicer.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				final int index = textTypeChoicer.getSelectionIndex( );
				formatChoicer.setEnabled( index == 1 );
				formatTagsBar.setEnabled( index == 1 );
				commonTagsBar.setEnabled( index == 1 );

				// set the enablement of all html tags when the text type is
				// changed.
				ToolItem[] toolItems = formatTagsBar.getItems( );
				ToolItem[] commonTags = commonTagsBar.getItems( );

				for ( int i = 1; i < toolItems.length; i++ )
				{
					toolItems[i].setEnabled( formatTagsBar.isEnabled( ) );
				}
				for ( int i = 1; i < commonTags.length; i++ )
				{
					commonTags[i].setEnabled( commonTagsBar.isEnabled( ) );
				}

				textEditor.setFocus( );
			}
		} );
		// create common tags on the right of the text type choicer.
		commonTagsBar = new ToolBar( composite, SWT.FLAT );
		commonTagsBar.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_BEGINNING ) );
		commonTagsBar.setEnabled( textTypeChoicer.getSelectionIndex( ) == 1 );
		createCommonTags( commonTagsBar );

	}

	/**
	 * Creates formatChoicer and formatBar
	 * 
	 * @param composite
	 *            composite to contain formatBar
	 */
	private void createFormatBar( Composite composite )
	{
		formatChoicer = new CCombo( composite, SWT.READ_ONLY | SWT.FLAT );
		GridData gdata = new GridData( GridData.FILL_HORIZONTAL
				| GridData.HORIZONTAL_ALIGN_BEGINNING );
		gdata.horizontalIndent = 5;
		formatChoicer.setLayoutData( gdata );
		formatChoicer.setBackground( PlatformUI.getWorkbench( )
				.getDisplay( )
				.getSystemColor( SWT.COLOR_LIST_BACKGROUND ) );

		formatChoicer.setItems( new String[]{
				Messages.getString( "TextEditDialog.formatChoice.formatting" ), Messages.getString( "TextEditDialog.formatChoice.layout" ), Messages.getString( "TextEditDialog.formatChoice.content" ), Messages.getString( "TextEditDialog.formatChoice.lists" ), Messages.getString( "TextEditDialog.formatChoice.dynamicText" ) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

		} );

		formatChoicer.select( 0 );
		formatChoicer.setEnabled( textTypeChoicer.getSelectionIndex( ) == 1 );

		formatTagsBar = new ToolBar( composite, SWT.FLAT );
		GridData data = new GridData( GridData.FILL_BOTH );
		data.horizontalSpan = 2;
		formatTagsBar.setLayoutData( data );

		// vertical separator between combo and tooItems
		new ToolItem( formatTagsBar, SWT.SEPARATOR );

		formatTagsBar.setEnabled( textTypeChoicer.getSelectionIndex( ) == 1 );

		// create initial format tags.
		createFormatTags( 0, formatTagsBar );

		// initiate the enablement of tool items right after they were created.
		ToolItem[] toolItems = formatTagsBar.getItems( );
		for ( int i = 1; i < toolItems.length; i++ )
		{
			toolItems[i].setEnabled( formatTagsBar.isEnabled( ) );
		}

		formatChoicer.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				final int index = formatChoicer.getSelectionIndex( );
				ToolItem[] toolItems = formatTagsBar.getItems( );
				// avoid to dispose the separator( toolItems[ 0 ] )
				for ( int i = 1; i < toolItems.length; i++ )
				{
					toolItems[i].dispose( );
				}
				// create html format tags according to the index of the format
				// choice selected.
				createFormatTags( index, formatTagsBar );
				textEditor.setFocus( );
			}
		} );
	}

	/**
	 * Creates the text area for edit operation.
	 * 
	 * @param parent
	 *            The composite of the text area.
	 */
	private void createTextArea( Composite parent )
	{
		IVerticalRuler ruler = null;
		textViewer = new SourceViewer( parent, ruler, SWT.WRAP
				| SWT.MULTI
				| SWT.BORDER
				| SWT.H_SCROLL
				| SWT.V_SCROLL
				| SWT.FULL_SELECTION | SWT.LEFT_TO_RIGHT );
		textViewer.setDocument( new Document( ) );
		textEditor = textViewer.getTextWidget( );
		{
			GridData gd = new GridData( GridData.FILL_BOTH );
			gd.horizontalSpan = 3;
			gd.widthHint = 600;
			gd.heightHint = 300;
			textEditor.setLayoutData( gd );
			textEditor.setText( oldValue );
			textEditor.setFocus( );
			textEditor.setFont( JFaceResources.getTextFont( ) );
		}

		textEditor.addKeyListener( new KeyListener( ) {

			public void keyPressed( KeyEvent e )
			{
				if ( isUndoKeyPress( e ) )
				{
					textViewer.doOperation( ITextOperationTarget.UNDO );
				}
				else if ( isRedoKeyPress( e ) )
				{
					textViewer.doOperation( ITextOperationTarget.REDO );
				}
			}

			public void keyReleased( KeyEvent e )
			{
				// do nothing
			}

			private boolean isUndoKeyPress( KeyEvent e )
			{
				return ( ( e.stateMask & SWT.CONTROL ) > 0 )
						&& ( ( e.keyCode == 'z' ) || ( e.keyCode == 'Z' ) );
			}

			private boolean isRedoKeyPress( KeyEvent e )
			{
				return ( ( e.stateMask & SWT.CONTROL ) > 0 )
						&& ( ( e.keyCode == 'y' ) || ( e.keyCode == 'Y' ) );
			}

		} );
		textViewer.getTextWidget( )
				.addBidiSegmentListener( new BidiSegmentListener( ) {

					public void lineGetSegments( BidiSegmentEvent event )
					{
						event.segments = UIUtil.getExpressionBidiSegments( event.lineText );
					}
				} );

		textViewer.configure( new SourceViewerConfiguration( ) );
		textEditor.invokeAction( ST.TEXT_END );

		// create actions for context menu and short cut keys
		ResourceBundle bundle = ResourceBundle.getBundle( "org.eclipse.birt.report.designer.nls.messages" );//$NON-NLS-1$
		final TextEditorAction undoAction = new EBTextAction( bundle,
				"TextAreaContextMenu.Undo.",//$NON-NLS-1$
				textViewer,
				ITextOperationTarget.UNDO );
		undoAction.setAccelerator( SWT.CTRL | 'Z' );

		final TextEditorAction redoAction = new EBTextAction( bundle,
				"TextAreaContextMenu.Redo.",//$NON-NLS-1$
				textViewer,
				ITextOperationTarget.REDO );
		redoAction.setAccelerator( SWT.CTRL | 'Y' );

		final TextEditorAction cutAction = new EBTextAction( bundle,
				"TextAreaContextMenu.Cut.",//$NON-NLS-1$
				textViewer,
				ITextOperationTarget.CUT );
		cutAction.setAccelerator( SWT.CTRL | 'X' );

		final TextEditorAction copyAction = new EBTextAction( bundle,
				"TextAreaContextMenu.Copy.",//$NON-NLS-1$
				textViewer,
				ITextOperationTarget.COPY );
		copyAction.setAccelerator( SWT.CTRL | 'C' );

		final TextEditorAction pasteAction = new EBTextAction( bundle,
				"TextAreaContextMenu.Paste.",//$NON-NLS-1$
				textViewer,
				ITextOperationTarget.PASTE );
		pasteAction.setAccelerator( SWT.CTRL | 'V' );

		final TextEditorAction selectAllAction = new EBTextAction( bundle,
				"TextAreaContextMenu.SelectAll.",//$NON-NLS-1$
				textViewer,
				ITextOperationTarget.SELECT_ALL );
		selectAllAction.setAccelerator( SWT.CTRL | 'A' );

		// Create context menu
		MenuManager menuMgr = new MenuManager( "#EB Context" );//$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown( true );
		menuMgr.addMenuListener( new IMenuListener( ) {

			public void menuAboutToShow( IMenuManager menuManager )
			{
				menuManager.add( new Separator( ITextEditorActionConstants.GROUP_UNDO ) );
				menuManager.add( new Separator( ITextEditorActionConstants.GROUP_COPY ) );
				menuManager.add( new Separator( ITextEditorActionConstants.GROUP_EDIT ) );
				menuManager.add( new Separator( ITextEditorActionConstants.GROUP_REST ) );

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

				IAction action = new Action( ACTION_TEXT_FORMAT_NUMBER ) {

					public boolean isEnabled( )
					{
						return formatChoicer.getSelectionIndex( ) == FORMAT_CHOICE_INDEX_DYNAMIC_TEXT;
					}

					public void run( )
					{
						insertFormat( FormatBuilder.NUMBER );
					}
				};

				menuManager.appendToGroup( ITextEditorActionConstants.GROUP_REST,
						action );

				action = new Action( ACTION_TEXT_FORMAT_STRING ) {

					public boolean isEnabled( )
					{
						return formatChoicer.getSelectionIndex( ) == FORMAT_CHOICE_INDEX_DYNAMIC_TEXT;
					}

					public void run( )
					{
						insertFormat( FormatBuilder.STRING );
					}
				};

				menuManager.appendToGroup( ITextEditorActionConstants.GROUP_REST,
						action );

				action = new Action( ACTION_TEXT_FORMAT_DATE_TIME ) {

					public boolean isEnabled( )
					{
						return formatChoicer.getSelectionIndex( ) == FORMAT_CHOICE_INDEX_DYNAMIC_TEXT;
					}

					public void run( )
					{
						insertFormat( FormatBuilder.DATETIME );
					}
				};

				menuManager.appendToGroup( ITextEditorActionConstants.GROUP_REST,
						action );

				action = new Action( ACTION_TEXT_EDIT_DYNAMIC_TEXT ) {

					public boolean isEnabled( )
					{
						return formatChoicer.getSelectionIndex( ) == FORMAT_CHOICE_INDEX_DYNAMIC_TEXT;
					}

					public void run( )
					{
						ExpressionBuilder eb = new ExpressionBuilder( textEditor.getSelectionText( ) ); //$NON-NLS-1$

						eb.setExpressionProvier( new ExpressionProvider( handle ) );

						if ( eb.open( ) == OK )
						{
							if ( !eb.getResult( ).equals( "" ) ) //$NON-NLS-1$
							{
								textEditor.insert( eb.getResult( ) );
							}
						}
					}
				};

				menuManager.appendToGroup( ITextEditorActionConstants.GROUP_REST,
						action );

			}
		} );
		textEditor.setMenu( menuMgr.createContextMenu( textEditor ) );
	}

	// inner class definition for create text editor actions.
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
	 * Creates common html tags uesd frequently.
	 */
	private void createCommonTags( ToolBar toolBar )
	{
		// vertical separator between combo and tooItems
		new ToolItem( toolBar, SWT.SEPARATOR );

		HTMLTag tag = new HTMLTag( "<B>", true ); //$NON-NLS-1$
		tag.setToolTipText( TOOL_TIP_TAG_B );
		createToolItemWithHTMLTag( toolBar, tag );

		tag = new HTMLTag( "<I>", true ); //$NON-NLS-1$
		tag.setToolTipText( TOOL_TIP_TAG_I ); //$NON-NLS-1$
		createToolItemWithHTMLTag( toolBar, tag );

		tag = new HTMLTag( "<U>", true ); //$NON-NLS-1$
		tag.setToolTipText( TOOL_TIP_TAG_U );
		createToolItemWithHTMLTag( toolBar, tag );

		tag = new HTMLTag( "<FONT>", true ); //$NON-NLS-1$
		tag.setToolTipText( TOOL_TIP_TAG_FONT );
		tag.addAttribute( "size" ); //$NON-NLS-1$
		tag.addAttribute( "color" ); //$NON-NLS-1$
		tag.addAttribute( "face" ); //$NON-NLS-1$
		createToolItemWithHTMLTag( toolBar, tag );

		// set initial enablement of common tags right after they were created.
		ToolItem[] toolItems = commonTagsBar.getItems( );
		for ( int i = 1; i < toolItems.length; i++ )
		{
			toolItems[i].setEnabled( commonTagsBar.isEnabled( ) );
		}
	}

	/**
	 * Creates format tags according to the selection of the formatChoicer.
	 * 
	 * @param index
	 *            index of the formatChoicer that is selected
	 * @param toolBar
	 *            the toolbar that contains these tags
	 */
	private void createFormatTags( int index, ToolBar toolBar )
	{
		HTMLTag tag;
		switch ( index )
		{
			// Creates tags of formatting.
			// case 0 :
			case FORMAT_CHOICE_INDEX_FORMATTING :
			{
				tag = new HTMLTag( "<H1>", true ); //$NON-NLS-1$
				tag.setToolTipText( TOOL_TIP_TAG_H1 );
				createToolItemWithHTMLTag( toolBar, tag );

				tag = new HTMLTag( "<H2>", true ); //$NON-NLS-1$
				tag.setToolTipText( TOOL_TIP_TAG_H2 );
				createToolItemWithHTMLTag( toolBar, tag );

				tag = new HTMLTag( "<H3>", true ); //$NON-NLS-1$
				tag.setToolTipText( TOOL_TIP_TAG_H3 );
				createToolItemWithHTMLTag( toolBar, tag );

				tag = new HTMLTag( "<H4>", true ); //$NON-NLS-1$
				tag.setToolTipText( TOOL_TIP_TAG_H4 );
				createToolItemWithHTMLTag( toolBar, tag );

				tag = new HTMLTag( "<H5>", true ); //$NON-NLS-1$
				tag.setToolTipText( TOOL_TIP_TAG_H5 );
				createToolItemWithHTMLTag( toolBar, tag );

				tag = new HTMLTag( "<H6>", true ); //$NON-NLS-1$
				tag.setToolTipText( TOOL_TIP_TAG_H6 );
				createToolItemWithHTMLTag( toolBar, tag );

				tag = new HTMLTag( "<SUB>", true ); //$NON-NLS-1$
				tag.setToolTipText( TOOL_TIP_TAG_SUB );
				createToolItemWithHTMLTag( toolBar, tag );

				tag = new HTMLTag( "<SUP>", true ); //$NON-NLS-1$
				tag.setToolTipText( TOOL_TIP_TAG_SUP );
				createToolItemWithHTMLTag( toolBar, tag );

				tag = new HTMLTag( "<EM>", true ); //$NON-NLS-1$
				tag.setToolTipText( TOOL_TIP_TAG_EM );
				createToolItemWithHTMLTag( toolBar, tag );

				tag = new HTMLTag( "<STRONG>", true ); //$NON-NLS-1$
				tag.setToolTipText( TOOL_TIP_TAG_STRONG );
				createToolItemWithHTMLTag( toolBar, tag );

				tag = new HTMLTag( "<PRE>", true ); //$NON-NLS-1$
				tag.setToolTipText( TOOL_TIP_TAG_PRE );
				createToolItemWithHTMLTag( toolBar, tag );

				tag = new HTMLTag( "<CODE>", true ); //$NON-NLS-1$
				tag.setToolTipText( TOOL_TIP_TAG_CODE );
				createToolItemWithHTMLTag( toolBar, tag );

				tag = new HTMLTag( "<TT>", true ); //$NON-NLS-1$
				tag.setToolTipText( TOOL_TIP_TAG_TT );
				createToolItemWithHTMLTag( toolBar, tag );
				break;
			}
				// Creates tags of layout.
			case FORMAT_CHOICE_INDEX_LAYOUT :
				// case 1 :
			{
				tag = new HTMLTag( "<CENTER>", true ); //$NON-NLS-1$
				tag.setToolTipText( TOOL_TIP_TAG_CENTER );
				createToolItemWithHTMLTag( toolBar, tag );

				tag = new HTMLTag( "<BR>", false ); //$NON-NLS-1$
				tag.setToolTipText( TOOL_TIP_TAG_BR );
				createToolItemWithHTMLTag( toolBar, tag );

				tag = new HTMLTag( "<P>", true ); //$NON-NLS-1$
				tag.setToolTipText( TOOL_TIP_TAG_P );
				createToolItemWithHTMLTag( toolBar, tag );

				tag = new HTMLTag( "<DIV>", true ); //$NON-NLS-1$
				tag.setToolTipText( TOOL_TIP_TAG_GENERIC_STYLE );
				createToolItemWithHTMLTag( toolBar, tag );

				tag = new HTMLTag( "<SPAN>", true ); //$NON-NLS-1$
				tag.setToolTipText( TOOL_TIP_TAG_GENERIC_STYLE );
				createToolItemWithHTMLTag( toolBar, tag );
				break;
			}
				// Creates tags of content.
				// case 2 :
			case FORMAT_CHOICE_INDEX_CONTENT :
			{
				tag = new HTMLTag( "<IMG>", false ); //$NON-NLS-1$
				tag.setToolTipText( TOOL_TIP_TAG_IMG );
				tag.addAttribute( "src" ); //$NON-NLS-1$
				tag.addAttribute( "alt" ); //$NON-NLS-1$

				createToolItemWithHTMLTag( toolBar, tag );

				tag = new HTMLTag( "<A>", true ); //$NON-NLS-1$
				tag.setToolTipText( TOOL_TIP_TAG_A );
				tag.addAttribute( "name" ); //$NON-NLS-1$
				tag.addAttribute( "href" ); //$NON-NLS-1$
				createToolItemWithHTMLTag( toolBar, tag );

				tag = new HTMLTag( "<INS>", true ); //$NON-NLS-1$
				tag.setToolTipText( TOOL_TIP_TAG_INS );
				tag.addAttribute( "cite" ); //$NON-NLS-1$
				createToolItemWithHTMLTag( toolBar, tag );

				tag = new HTMLTag( "<DEL>", true ); //$NON-NLS-1$
				tag.setToolTipText( TOOL_TIP_TAG_DEL );
				tag.addAttribute( "cite" ); //$NON-NLS-1$
				createToolItemWithHTMLTag( toolBar, tag );

				tag = new HTMLTag( "<IMAGE>", true ); //$NON-NLS-1$
				tag.setToolTipText( TOOL_TIP_TAG_BIRT_IMAGE );
				tag.addAttribute( "name" ); //$NON-NLS-1$
				tag.addAttribute( "type" ); //$NON-NLS-1$
				createToolItemWithHTMLTag( toolBar, tag );
				break;
			}
				// Creates tags of list.
			case FORMAT_CHOICE_INDEX_LISTS :
				// case 3 :
			{
				tag = new HTMLTag( "<DL>", true ); //$NON-NLS-1$
				tag.setToolTipText( TOOL_TIP_TAG_DL );
				createToolItemWithHTMLTag( toolBar, tag );

				tag = new HTMLTag( "<UL>", true ); //$NON-NLS-1$
				tag.setToolTipText( TOOL_TIP_TAG_UL );
				createToolItemWithHTMLTag( toolBar, tag );

				tag = new HTMLTag( "<LI>", true ); //$NON-NLS-1$
				tag.setToolTipText( TOOL_TIP_TAG_LI );
				createToolItemWithHTMLTag( toolBar, tag );

				tag = new HTMLTag( "<DT>", true ); //$NON-NLS-1$
				tag.setToolTipText( TOOL_TIP_TAG_DT );
				createToolItemWithHTMLTag( toolBar, tag );

				tag = new HTMLTag( "<DD>", true ); //$NON-NLS-1$
				tag.setToolTipText( TOOL_TIP_TAG_DD );
				createToolItemWithHTMLTag( toolBar, tag );
				break;
			}
				// Creates tags for Dynamic Text.
				// case 4 :
			case FORMAT_CHOICE_INDEX_DYNAMIC_TEXT :
			{
				final ToolItem value = new ToolItem( toolBar, SWT.NONE );
				value.setText( "<VALUE-OF>" ); //$NON-NLS-1$
				value.setToolTipText( TOOL_TIP_VALUE_OF );
				value.addSelectionListener( new SelectionAdapter( ) {

					public void widgetSelected( SelectionEvent e )
					{
						ExpressionBuilder expressionBuilder = new ExpressionBuilder( "" ); //$NON-NLS-1$

						expressionBuilder.setExpressionProvier( new ExpressionProvider( handle ) );

						if ( expressionBuilder.open( ) == OK )
						{
							if ( !expressionBuilder.getResult( ).equals( "" ) ) //$NON-NLS-1$
							{
								Point point = textEditor.getSelection( );
								int start = point.x < point.y ? point.x
										: point.y;
								String result = value.getText( )
										+ expressionBuilder.getResult( )
										+ value.getText( ).replaceFirst( "<", //$NON-NLS-1$
												"</" ); //$NON-NLS-1$
								textEditor.insert( result ); //$NON-NLS-1$
								textEditor.setSelection( start
										+ result.length( ) );
							}
						}
					}
				} );

				break;
			}
		}
	}

	private void insertFormat( int style )
	{
		FormatBuilder dialog = new FormatBuilder( style );
		if ( dialog.open( ) == OK
				&& ( (String[]) dialog.getResult( ) )[1] != null )
		{
			String result = " format=\"" //$NON-NLS-1$
					+ ( (String[]) dialog.getResult( ) )[1]
					+ "\""; //$NON-NLS-1$
			textEditor.insert( result );
		}
	}

	/**
	 * A inner class to store a html tag information.
	 */
	private static class HTMLTag
	{

		// tag 's display name.
		private String name;
		// is the tag to display in pair.
		private boolean isPair;
		// the tool tip text of the display tag.
		private String toolTip;
		// attributes list the tag takes, if any.
		private List attributes = new ArrayList( );

		public HTMLTag( String name, boolean isPair )
		{
			this.name = name;
			this.isPair = isPair;
		}

		public void setToolTipText( String text )
		{
			toolTip = text;
		}

		public void addAttribute( String obj )
		{
			getAttributes( ).add( obj );
		}

		/**
		 * @return Returns the name.
		 */
		public String getName( )
		{
			return name;
		}

		/**
		 * @return Returns the toolTip.
		 */
		public String getToolTip( )
		{
			return toolTip;
		}

		/**
		 * @return Returns the isPair.
		 */
		public boolean isPair( )
		{
			return isPair;
		}

		/**
		 * @return Returns the attributes.
		 */
		public List getAttributes( )
		{
			return attributes;
		}
	}

	/**
	 * Creates tool item given a HTMLTag to display information in the tool bar.
	 * 
	 * @param parent
	 *            Container tool bar.
	 * @param tag
	 *            The given HTMLTag contain information of the tag to display.
	 */
	protected void createToolItemWithHTMLTag( ToolBar parent, final HTMLTag tag )
	{
		tagItem = new ToolItem( parent, SWT.NONE );
		tagItem.setText( tag.getName( ) );
		tagItem.setToolTipText( tag.getToolTip( ) );
		tagItem.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				String frontTag = tag.getName( );
				String backTag = tag.getName( ).replaceFirst( "<", "</" ); //$NON-NLS-1$ //$NON-NLS-2$				

				// if tag.attributes is not empty, then adds its attributes
				// following the front tag.
				if ( !tag.getAttributes( ).isEmpty( ) )
				{
					String text = " "; //$NON-NLS-1$
					for ( Iterator iter = tag.getAttributes( ).iterator( ); iter.hasNext( ); )
					{
						text = text + (String) iter.next( ) + "=\"\" "; //$NON-NLS-1$
					}
					frontTag = tag.getName( ).replaceFirst( ">", text + ">" ); //$NON-NLS-1$ //$NON-NLS-2$					
				}

				// start: the start offset of selected text.
				Point point = textEditor.getSelection( );
				int start = point.x < point.y ? point.x : point.y;
				String selectedText = textEditor.getSelectionText( );

				if ( tag.isPair( ) )
				{
					String text;
					if ( selectedText.length( ) == 0 )
					{
						text = frontTag
								+ textEditor.getLineDelimiter( )
								+ textEditor.getLineDelimiter( )
								+ backTag;
					}
					else
					{
						text = frontTag + selectedText + backTag;
					}
					textEditor.insert( text );
				}
				else
				{
					textEditor.insert( frontTag + selectedText );
				}

				// if the tag takes some attributes, then set the
				// cursor to the first attribute.
				if ( !tag.getAttributes( ).isEmpty( ) )
				{
					textEditor.setCaretOffset( start
							+ tag.getName( ).length( )
							+ ( (String) tag.getAttributes( ).get( 0 ) ).length( )
							+ 2 );
				}
				else
				{
					int offset = start + frontTag.length( );
					if ( tag.isPair( ) && selectedText.length( ) == 0 )
					{
						// a new line break is needed, set cursor to the new
						// line.
						offset = textEditor.getOffsetAtLine( textEditor.getLineAtOffset( start ) + 1 );
					}
					textEditor.setCaretOffset( offset );
				}
			}
		} );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	protected void okPressed( )
	{
		String result = textEditor.getText( );
		try
		{
			if ( textTypeChoicer.getSelectionIndex( ) == 1 )
			{
				handle.setContentType( DesignChoiceConstants.TEXT_CONTENT_TYPE_HTML );
			}
			else
			{
				handle.setContentType( DesignChoiceConstants.TEXT_CONTENT_TYPE_PLAIN );
			}
			handle.setContent( result.length( ) > 0 ? result : null );
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
		}

		setResult( handle );
		super.okPressed( );
	}

	private int[] getBidiLineSegments( String lineText )
	{
		int[] seg = null;
		if ( lineText != null
				&& lineText.length( ) > 0
				&& !new Bidi( lineText, Bidi.DIRECTION_LEFT_TO_RIGHT ).isLeftToRight( ) )
		{
			List list = new ArrayList( );

			// Punctuations will be regarded as delimiter so that different
			// splits could be rendered separately.
			Object[] splits = lineText.split( "\\p{Punct}" );

			// !=, <> etc. leading to "" will be filtered to meet the rule that
			// segments must not have duplicates.
			for ( int i = 0; i < splits.length; i++ )
			{
				if ( !splits[i].equals( "" ) )
					list.add( splits[i] );
			}
			splits = list.toArray( );

			// first segment must be 0
			// last segment does not necessarily equal to line length
			seg = new int[splits.length + 1];
			for ( int i = 0; i < splits.length; i++ )
			{
				seg[i + 1] = lineText.indexOf( (String) splits[i], seg[i] )
						+ ( (String) splits[i] ).length( );
			}
		}

		return seg;
	}

}