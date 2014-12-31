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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.designer.internal.ui.dialogs.BackgroundPreferencePage;
import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseStylePreferencePage;
import org.eclipse.birt.report.designer.internal.ui.dialogs.BlockPreferencePage;
import org.eclipse.birt.report.designer.internal.ui.dialogs.BorderPreferencePage;
import org.eclipse.birt.report.designer.internal.ui.dialogs.BoxPreferencePage;
import org.eclipse.birt.report.designer.internal.ui.dialogs.CommentsPreferencePage;
import org.eclipse.birt.report.designer.internal.ui.dialogs.FontPreferencePage;
import org.eclipse.birt.report.designer.internal.ui.dialogs.FormatDateTimePreferencePage;
import org.eclipse.birt.report.designer.internal.ui.dialogs.FormatNumberPreferencePage;
import org.eclipse.birt.report.designer.internal.ui.dialogs.FormatStringPreferencePage;
import org.eclipse.birt.report.designer.internal.ui.dialogs.GeneralPreferencePage;
import org.eclipse.birt.report.designer.internal.ui.dialogs.HighlightsPreferencePage;
import org.eclipse.birt.report.designer.internal.ui.dialogs.MapPreferencePage;
import org.eclipse.birt.report.designer.internal.ui.dialogs.PageBreakPreferencePage;
import org.eclipse.birt.report.designer.internal.ui.dialogs.SizePreferencePage;
import org.eclipse.birt.report.designer.internal.ui.dialogs.StylePreferenceNode;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.birt.report.model.api.AbstractThemeHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.preference.PreferenceContentProvider;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceColors;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

/**
 * Presents style builder dialog.
 */

public class StyleBuilder extends PreferenceDialog
{

	protected Logger logger = Logger.getLogger( StyleBuilder.class.getName( ) );

	public static final String DLG_TITLE_NEW = Messages.getString( "SytleBuilder.DialogTitle.New" ); //$NON-NLS-1$
	public static final String DLG_TITLE_EDIT = Messages.getString( "SytleBuilder.DialogTitle.Edit" ); //$NON-NLS-1$

	protected String title;

	/**
	 * Constructor
	 * 
	 * @param parentShell
	 * @param handle
	 */
	public StyleBuilder( Shell parentShell, ReportElementHandle handle,
			String title )
	{
		this( parentShell, handle, null, title );
	}

	/**
	 * Constructor
	 * 
	 * @param parentShell
	 * @param handle
	 */
	public StyleBuilder( Shell parentShell, ReportElementHandle handle,
			AbstractThemeHandle theme, String title )
	{
		super( parentShell, createPreferenceManager( handle, theme ) );
		setHelpAvailable( false );
		IPreferenceNode[] nodes = this.getPreferenceManager( )
				.getRootSubNodes( );
		for ( int i = 0; i < nodes.length; i++ )
		{
			( (BaseStylePreferencePage) nodes[i].getPage( ) ).setBuilder( this );
		}
		this.title = title;

	}

	protected TreeViewer createTreeViewer( Composite parent )
	{
		final Tree tree = new Tree( parent, SWT.FULL_SELECTION
				| SWT.SINGLE
				| SWT.HIDE_SELECTION
				| SWT.H_SCROLL
				| SWT.V_SCROLL );

		// configure the widget
		// tree.setLinesVisible( false );
		tree.setHeaderVisible( false );
		columns = new TreeColumn[2];
		columns[0] = new TreeColumn( tree, SWT.LEFT );
		columns[0].setWidth( 0 );
		columns[1] = new TreeColumn( tree, SWT.LEFT );
		columns[1].setWidth( getLastRightWidth( ) );
		tree.addControlListener( new ControlAdapter( ) {

			public void controlResized( ControlEvent e )
			{
				TreeItem[] items = viewer.getTree( ).getItems( );

				if ( items != null )
				{

					String[] itemContents = new String[items.length];
					for ( int i = 0; i < items.length; i++ )
					{
						itemContents[i] = items[i].getText( 1 );
					}

					int maxString = UIUtil.getMaxStringWidth( itemContents,
							tree );

					maxString += ( 16 + 16 );

					columns[1].setWidth( maxString > getLastRightWidth( ) ? maxString
							: getLastRightWidth( ) );
				}
			}

		} );
		viewer = new TreeViewer( tree );
		addListeners( viewer );
		viewer.setLabelProvider( new PreferenceTreeLabelProvider( ) );
		viewer.setContentProvider( new PreferenceContentProvider( ) );
		return viewer;
	}

	public void refreshPagesStatus( )
	{
		if ( viewer != null )
		{
			viewer.refresh( );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.window.Window#open()
	 */
	public int open( )
	{
		setSelectedNode( "General" ); //$NON-NLS-1$
		return super.open( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets
	 * .Shell)
	 */
	protected void configureShell( Shell newShell )
	{
		super.configureShell( newShell );
		newShell.setText( title );
	}

	private static PreferenceManager createPreferenceManager(
			ReportElementHandle handle, AbstractThemeHandle theme )
	{
		PreferenceManager preferenceManager = new PreferenceManager( '/' );

		// Get the pages from the registry
		List<IPreferenceNode> pageContributions = new ArrayList<IPreferenceNode>( );

		// adds preference pages into page contributions.
		pageContributions.add( new StylePreferenceNode( "General", //$NON-NLS-1$
				new GeneralPreferencePage( handle, theme ) ) );
		pageContributions.add( new StylePreferenceNode( "Font", //$NON-NLS-1$
				new FontPreferencePage( handle ) ) );
		pageContributions.add( new StylePreferenceNode( "Size", //$NON-NLS-1$
				new SizePreferencePage( handle ) ) );
		pageContributions.add( new StylePreferenceNode( "Background", //$NON-NLS-1$
				new BackgroundPreferencePage( handle ) ) );
		pageContributions.add( new StylePreferenceNode( "Block", //$NON-NLS-1$
				new BlockPreferencePage( handle ) ) );
		pageContributions.add( new StylePreferenceNode( "Box", //$NON-NLS-1$
				new BoxPreferencePage( handle ) ) );
		pageContributions.add( new StylePreferenceNode( "Border", //$NON-NLS-1$
				new BorderPreferencePage( handle ) ) );
		pageContributions.add( new StylePreferenceNode( "Number Format", //$NON-NLS-1$
				new FormatNumberPreferencePage( handle ) ) );
		pageContributions.add( new StylePreferenceNode( "DateTime Format", //$NON-NLS-1$
				new FormatDateTimePreferencePage( handle ) ) );
		pageContributions.add( new StylePreferenceNode( "String Format", //$NON-NLS-1$
				new FormatStringPreferencePage( handle ) ) );
		pageContributions.add( new StylePreferenceNode( "PageBreak", //$NON-NLS-1$
				new PageBreakPreferencePage( handle ) ) );
		pageContributions.add( new StylePreferenceNode( "Map", //$NON-NLS-1$
				new MapPreferencePage( handle ) ) );
		pageContributions.add( new StylePreferenceNode( "Highlights", //$NON-NLS-1$
				new HighlightsPreferencePage( handle ) ) );
		pageContributions.add( new StylePreferenceNode( "Comments", //$NON-NLS-1$
				new CommentsPreferencePage( handle ) ) );

		// Add the contributions to the manager
		Iterator<IPreferenceNode> it = pageContributions.iterator( );
		while ( it.hasNext( ) )
		{
			IPreferenceNode node = it.next( );
			preferenceManager.addToRoot( node );
		}
		return preferenceManager;
	}

	private void saveAll( final boolean closeDialog )
	{
		SafeRunner.run( new SafeRunnable( ) {

			private boolean errorOccurred;

			private boolean invalid;

			public void run( )
			{
				errorOccurred = false;
				invalid = false;
				try
				{
					// Notify all the pages and give them a chance to abort
					Iterator nodes = getPreferenceManager( ).getElements( PreferenceManager.PRE_ORDER )
							.iterator( );
					while ( nodes.hasNext( ) )
					{
						IPreferenceNode node = (IPreferenceNode) nodes.next( );
						IPreferencePage page = node.getPage( );
						if ( page != null )
						{
							if ( !page.performOk( ) )
							{
								invalid = true;
								return;
							}
						}
					}
				}
				catch ( Exception e )
				{
					handleException( e );
				}
				finally
				{
					// Give subclasses the choice to save the state of the
					// preference pages.
					if ( !errorOccurred )
					{
						handleSave( );
					}

					// Need to restore state
					if ( !invalid && closeDialog )
					{
						close( );
					}

				}
			}

			public void handleException( Throwable e )
			{
				errorOccurred = true;
				if ( Platform.isRunning( ) )
				{
					String bundle = Platform.PI_RUNTIME;
					Platform.getLog( Platform.getBundle( bundle ) )
							.log( new Status( IStatus.ERROR,
									bundle,
									0,
									e.toString( ),
									e ) );
				}
				else
				{
					logger.log( Level.SEVERE, e.getMessage( ), e );
				}

				setSelectedNodePreference( null );
				String message = ""; //$NON-NLS-1$
				MessageDialog.openError( getShell( ), "", message ); //$NON-NLS-1$

			}
		} );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferenceDialog#okPressed()
	 */
	protected void okPressed( )
	{
		saveAll( true );
	}

	private RGB titleAreaRGB;
	Color titleAreaColor;
	private Label titleLabel;
	private Label titleImageLabel;
	private Image titleImage;
	private Image titleAreaImage;
	public static final String DLG_IMG_TITLE_BANNER = "dialog_title_banner_image"; //$NON-NLS-1$
	public static final String DLG_IMG_TITLE_ERROR = DLG_IMG_MESSAGE_ERROR;
	private Label messageImageLabel;
	private Text messageLabel;
	private int messageLabelHeight;
	private Label leftFillerLabel, bottomFillerLabel;
	private String errorMessage;
	private boolean showingError = false;
	private String message = ""; //$NON-NLS-1$
	private Image messageImage;

	private static final int H_GAP_IMAGE = 5;
	static
	{
		ImageRegistry reg = JFaceResources.getImageRegistry( );
		if ( reg.get( DLG_IMG_TITLE_BANNER ) == null )
			reg.put( DLG_IMG_TITLE_BANNER,
					ImageDescriptor.createFromFile( TitleAreaDialog.class,
							"images/title_banner.gif" ) );//$NON-NLS-1$
	}

	protected Control createDialogArea( Composite parent )
	{
		createDialogTitleArea( parent );

		setTitleTitle( Messages.getString( "StyleBuilder.Title" ) ); //$NON-NLS-1$

		updateMessage( "" ); //$NON-NLS-1$

		return super.createDialogArea( parent );
	}

	private void createDialogTitleArea( Composite parent )
	{

		Composite contents = new Composite( parent, SWT.NONE );
		contents.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		FormLayout layout = new FormLayout( );
		contents.setLayout( layout );

		dialogTitleArea = new Composite( contents, SWT.NONE );
		initializeDialogUnits( dialogTitleArea );

		FormData titleAreaData = new FormData( );
		titleAreaData.top = new FormAttachment( 0, 0 );
		titleAreaData.left = new FormAttachment( 0, 0 );
		titleAreaData.right = new FormAttachment( 100, 0 );
		dialogTitleArea.setLayoutData( titleAreaData );

		layout = new FormLayout( );
		dialogTitleArea.setLayout( layout );

		// add a dispose listener
		dialogTitleArea.addDisposeListener( new DisposeListener( ) {

			public void widgetDisposed( DisposeEvent e )
			{
				if ( titleAreaColor != null )
				{
					titleAreaColor.dispose( );
				}
			}
		} );
		// Determine the background color of the title bar
		Display display = dialogTitleArea.getDisplay( );
		Color background;
		Color foreground;
		if ( titleAreaRGB != null )
		{
			titleAreaColor = new Color( display, titleAreaRGB );
			background = titleAreaColor;
			foreground = null;
		}
		else
		{
			background = JFaceColors.getBannerBackground( display );
			foreground = JFaceColors.getBannerForeground( display );
		}

		dialogTitleArea.setBackground( background );
		int verticalSpacing = convertVerticalDLUsToPixels( IDialogConstants.VERTICAL_SPACING );
		int horizontalSpacing = convertHorizontalDLUsToPixels( IDialogConstants.HORIZONTAL_SPACING );
		// Dialog image @ right
		titleImageLabel = new Label( dialogTitleArea, SWT.CENTER );
		titleImageLabel.setBackground( background );
		if ( titleAreaImage == null )
			titleImageLabel.setImage( JFaceResources.getImage( DLG_IMG_TITLE_BANNER ) );
		else
			titleImageLabel.setImage( titleAreaImage );

		FormData imageData = new FormData( );
		imageData.top = new FormAttachment( 0, 0 );
		// Note: do not use horizontalSpacing on the right as that would be a
		// regression from
		// the R2.x style where there was no margin on the right and images are
		// flush to the right
		// hand side. see reopened comments in 41172
		imageData.right = new FormAttachment( 100, 0 ); // horizontalSpacing
		titleImageLabel.setLayoutData( imageData );
		// Title label @ top, left
		titleLabel = new Label( dialogTitleArea, SWT.LEFT );
		JFaceColors.setColors( titleLabel, foreground, background );
		titleLabel.setFont( JFaceResources.getBannerFont( ) );
		titleLabel.setText( " " );//$NON-NLS-1$
		FormData titleData = new FormData( );
		titleData.top = new FormAttachment( 0, verticalSpacing );
		titleData.right = new FormAttachment( titleImageLabel );
		titleData.left = new FormAttachment( 0, horizontalSpacing );
		titleLabel.setLayoutData( titleData );
		// Message image @ bottom, left
		messageImageLabel = new Label( dialogTitleArea, SWT.CENTER );
		messageImageLabel.setBackground( background );
		// Message label @ bottom, center
		messageLabel = new Text( dialogTitleArea, SWT.WRAP | SWT.READ_ONLY );
		JFaceColors.setColors( messageLabel, foreground, background );
		messageLabel.setText( " \n " ); // two lines//$NON-NLS-1$
		messageLabel.setFont( JFaceResources.getDialogFont( ) );
		messageLabelHeight = messageLabel.computeSize( SWT.DEFAULT, SWT.DEFAULT ).y;
		// Filler labels
		leftFillerLabel = new Label( dialogTitleArea, SWT.CENTER );
		leftFillerLabel.setBackground( background );
		bottomFillerLabel = new Label( dialogTitleArea, SWT.CENTER );
		bottomFillerLabel.setBackground( background );
		setLayoutsForNormalMessage( verticalSpacing, horizontalSpacing );
		determineTitleImageLargest( );

		Label titleBarSeparator = new Label( parent, SWT.HORIZONTAL
				| SWT.SEPARATOR );
		titleBarSeparator.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
	}

	private void determineTitleImageLargest( )
	{
		int titleY = titleImageLabel.computeSize( SWT.DEFAULT, SWT.DEFAULT ).y;
		int verticalSpacing = convertVerticalDLUsToPixels( IDialogConstants.VERTICAL_SPACING );
		int labelY = titleLabel.computeSize( SWT.DEFAULT, SWT.DEFAULT ).y;
		labelY += verticalSpacing;
		labelY += messageLabelHeight;
		labelY += verticalSpacing;
		titleImageLargest = titleY > labelY;
	}

	private void setLayoutsForNormalMessage( int verticalSpacing,
			int horizontalSpacing )
	{
		FormData messageImageData = new FormData( );
		messageImageData.top = new FormAttachment( titleLabel, verticalSpacing );
		messageImageData.left = new FormAttachment( 0, H_GAP_IMAGE );
		messageImageLabel.setLayoutData( messageImageData );
		FormData messageLabelData = new FormData( );
		messageLabelData.top = new FormAttachment( titleLabel, verticalSpacing );
		messageLabelData.right = new FormAttachment( titleImageLabel );
		messageLabelData.left = new FormAttachment( messageImageLabel,
				horizontalSpacing );
		messageLabelData.height = messageLabelHeight;
		if ( titleImageLargest )
			messageLabelData.bottom = new FormAttachment( titleImageLabel,
					0,
					SWT.BOTTOM );
		messageLabel.setLayoutData( messageLabelData );
		FormData fillerData = new FormData( );
		fillerData.left = new FormAttachment( 0, horizontalSpacing );
		fillerData.top = new FormAttachment( messageImageLabel, 0 );
		fillerData.bottom = new FormAttachment( messageLabel, 0, SWT.BOTTOM );
		bottomFillerLabel.setLayoutData( fillerData );
		FormData data = new FormData( );
		data.top = new FormAttachment( messageImageLabel, 0, SWT.TOP );
		data.left = new FormAttachment( 0, 0 );
		data.bottom = new FormAttachment( messageImageLabel, 0, SWT.BOTTOM );
		data.right = new FormAttachment( messageImageLabel, 0 );
		leftFillerLabel.setLayoutData( data );
	}

	/**
	 * Display the given error message. The currently displayed message is saved
	 * and will be redisplayed when the error message is set to
	 * <code>null</code>.
	 * 
	 * @param newErrorMessage
	 *            the newErrorMessage to display or <code>null</code>
	 */
	public void setErrorMessage( String newErrorMessage )
	{
		// Any change?
		if ( errorMessage == null ? newErrorMessage == null
				: errorMessage.equals( newErrorMessage ) )
			return;
		errorMessage = newErrorMessage;

		// Clear or set error message.
		if ( errorMessage == null )
		{
			if ( showingError )
			{
				// we were previously showing an error
				showingError = false;
			}
			// show the message
			// avoid calling setMessage in case it is overridden to call
			// setErrorMessage,
			// which would result in a recursive infinite loop
			if ( message == null ) // this should probably never happen since
				// setMessage does this conversion....
				message = ""; //$NON-NLS-1$
			updateMessage( message );
			messageImageLabel.setImage( messageImage );
			setImageLabelVisible( messageImage != null );
		}
		else
		{
			// Add in a space for layout purposes but do not
			// change the instance variable
			String displayedErrorMessage = " " + errorMessage; //$NON-NLS-1$
			updateMessage( displayedErrorMessage );
			if ( !showingError )
			{
				// we were not previously showing an error
				showingError = true;
				messageImageLabel.setImage( JFaceResources.getImage( DLG_IMG_TITLE_ERROR ) );
				setImageLabelVisible( true );
			}
		}
		layoutForNewMessage( );
	}

	private void setImageLabelVisible( boolean visible )
	{
		messageImageLabel.setVisible( visible );
		bottomFillerLabel.setVisible( visible );
		leftFillerLabel.setVisible( visible );
	}

	/**
	 * Re-layout the labels for the new message.
	 */
	private void layoutForNewMessage( )
	{
		int verticalSpacing = convertVerticalDLUsToPixels( IDialogConstants.VERTICAL_SPACING );
		int horizontalSpacing = convertHorizontalDLUsToPixels( IDialogConstants.HORIZONTAL_SPACING );
		// If there are no images then layout as normal
		if ( errorMessage == null && messageImage == null )
		{
			setImageLabelVisible( false );
			setLayoutsForNormalMessage( verticalSpacing, horizontalSpacing );
		}
		else
		{
			messageImageLabel.setVisible( true );
			bottomFillerLabel.setVisible( true );
			leftFillerLabel.setVisible( true );
			/**
			 * Note that we do not use horizontalSpacing here as when the
			 * background of the messages changes there will be gaps between the
			 * icon label and the message that are the background color of the
			 * shell. We add a leading space elsewhere to compendate for this.
			 */
			FormData data = new FormData( );
			data.left = new FormAttachment( 0, H_GAP_IMAGE );
			data.top = new FormAttachment( titleLabel, verticalSpacing );
			messageImageLabel.setLayoutData( data );
			data = new FormData( );
			data.top = new FormAttachment( messageImageLabel, 0 );
			data.left = new FormAttachment( 0, 0 );
			data.bottom = new FormAttachment( messageLabel, 0, SWT.BOTTOM );
			data.right = new FormAttachment( messageImageLabel, 0, SWT.RIGHT );
			bottomFillerLabel.setLayoutData( data );
			data = new FormData( );
			data.top = new FormAttachment( messageImageLabel, 0, SWT.TOP );
			data.left = new FormAttachment( 0, 0 );
			data.bottom = new FormAttachment( messageImageLabel, 0, SWT.BOTTOM );
			data.right = new FormAttachment( messageImageLabel, 0 );
			leftFillerLabel.setLayoutData( data );
			FormData messageLabelData = new FormData( );
			messageLabelData.top = new FormAttachment( titleLabel,
					verticalSpacing );
			messageLabelData.right = new FormAttachment( titleImageLabel );
			messageLabelData.left = new FormAttachment( messageImageLabel, 0 );
			messageLabelData.height = messageLabelHeight;
			if ( titleImageLargest )
				messageLabelData.bottom = new FormAttachment( titleImageLabel,
						0,
						SWT.BOTTOM );
			messageLabel.setLayoutData( messageLabelData );
		}
		// Do not layout before the dialog area has been created
		// to avoid incomplete calculations.
		if ( dialogArea != null )
		{
			dialogTitleArea.getParent( ).layout( true );
			dialogTitleArea.layout( true );
		}
	}

	public void setTitleMessage( String newMessage, int newType )
	{
		Image newImage = null;
		if ( newMessage != null )
		{
			switch ( newType )
			{
				case IMessageProvider.NONE :
					break;
				case IMessageProvider.INFORMATION :
					newImage = JFaceResources.getImage( DLG_IMG_MESSAGE_INFO );
					break;
				case IMessageProvider.WARNING :
					newImage = JFaceResources.getImage( DLG_IMG_MESSAGE_WARNING );
					break;
				case IMessageProvider.ERROR :
					newImage = JFaceResources.getImage( DLG_IMG_MESSAGE_ERROR );
					break;
			}
		}
		showTitleMessage( newMessage, newImage );
	}

	/**
	 * Show the new message and image.
	 * 
	 * @param newMessage
	 * @param newImage
	 */
	private void showTitleMessage( String newMessage, Image newImage )
	{
		// Any change?
		if ( message.equals( newMessage ) && messageImage == newImage )
		{
			return;
		}
		message = newMessage;
		if ( message == null )
			message = "";//$NON-NLS-1$
		// Message string to be shown - if there is an image then add in
		// a space to the message for layout purposes
		String shownMessage = ( newImage == null ) ? message : " " + message; //$NON-NLS-1$  
		messageImage = newImage;
		if ( !showingError )
		{
			// we are not showing an error
			updateMessage( shownMessage );
			messageImageLabel.setImage( messageImage );
			setImageLabelVisible( messageImage != null );
		}
		layoutForNewMessage( );
	}

	private void updateMessage( String newMessage )
	{
		if ( newMessage != null && newMessage.length( ) > 0 )
			messageLabel.setText( newMessage );
		else if ( DLG_TITLE_EDIT.equals( title ) )
			setTitleMessage( Messages.getString( "StyleBuilder.Edit.Info" ) ); //$NON-NLS-1$
		else if ( DLG_TITLE_NEW.equals( title ) )
			setTitleMessage( Messages.getString( "StyleBuilder.New.Info" ) ); //$NON-NLS-1$
	}

	public void setTitleMessage( String message )
	{
		if ( messageLabel != null ){
			messageLabel.setText( message );
			layoutForNewMessage( );
		}
	}

	public void setTitleTitle( String title )
	{
		if ( titleLabel != null )
		{
			titleLabel.setText( title );
			layoutForNewMessage( );
		}
	}

	private boolean titleImageLargest = true;

	private TreeViewer viewer;

	private Composite dialogTitleArea;

	private TreeColumn[] columns;

	public boolean isTitleImageLargest( )
	{
		return titleImageLargest;
	}

	public void setTitleImageLargest( boolean titleImageLargest )
	{
		this.titleImageLargest = titleImageLargest;
	}

	public Image getTitleImage( )
	{
		return titleImage;
	}

	public void setTitleImage( Image titleImage )
	{
		this.titleImage = titleImage;
	}

	private static class PreferenceTreeLabelProvider implements
			ITableLabelProvider
	{

		public Image getColumnImage( Object element, int columnIndex )
		{
			if ( columnIndex == 1 )
				return ( (IPreferenceNode) element ).getLabelImage( );
			else
				return null;
		}

		public String getColumnText( Object element, int columnIndex )
		{
			if ( columnIndex == 1 )
				return ( (IPreferenceNode) element ).getLabelText( );
			else
				return ""; //$NON-NLS-1$
		}

		public void addListener( ILabelProviderListener listener )
		{

		}

		public void dispose( )
		{

		}

		public boolean isLabelProperty( Object element, String property )
		{
			return false;
		}

		public void removeListener( ILabelProviderListener listener )
		{

		}

	}

}