/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.data.ui.property;

import org.eclipse.jface.dialogs.ControlAnimator;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.ImageAndMessageArea;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceColors;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.Policy;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public abstract class AbstractTitlePropertyDialog extends
		AbstractPropertyDialog
{

	public AbstractTitlePropertyDialog( Shell parentShell, Object model )
	{
		super( parentShell, model );
	}

	protected Control createDialogArea( Composite parent )
	{
		createDialogTitleArea( parent );
		return super.createDialogArea( parent );
	}

	private RGB titleAreaRGB;
	Color titleAreaColor;
	private Label titleLabel;
	private Label titleImageLabel;
	private Image titleImage;
	public static final String DLG_IMG_TITLE_BANNER = "dialog_title_banner_image"; //$NON-NLS-1$
	 public static final String DLG_IMG_TITLE_ERROR = DLG_IMG_MESSAGE_ERROR;
	private Label messageImageLabel;
	private Label messageLabel;
	private int messageLabelHeight;
	private ImageAndMessageArea messageArea;
	private Label leftFillerLabel;
	private String errorMessage;
	private boolean showingError = false;
	private boolean showingWarning = false;
	private String message = ""; //$NON-NLS-1$
	private Image messageImage;
	private String warningMessage;
	private ControlAnimator animator;

	private static final int H_GAP_IMAGE = 5;
	static
	{
		ImageRegistry reg = JFaceResources.getImageRegistry( );
		if ( reg.get( DLG_IMG_TITLE_BANNER ) == null )
			reg.put( DLG_IMG_TITLE_BANNER,
					ImageDescriptor.createFromFile( TitleAreaDialog.class,
							"images/title_banner.gif" ) );//$NON-NLS-1$
	}

	private void createDialogTitleArea( Composite parent )
	{
		Composite contents = new Composite( parent, SWT.NONE );
		contents.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		FormLayout layout = new FormLayout( );
		contents.setLayout( layout );

		titleArea = new Composite( contents, SWT.NONE );
		initializeDialogUnits( titleArea );

		FormData titleAreaData = new FormData( );
		titleAreaData.top = new FormAttachment( 0, 0 );
		titleAreaData.left = new FormAttachment( 0, 0 );
		titleAreaData.right = new FormAttachment( 100, 0 );
		titleArea.setLayoutData( titleAreaData );

		layout = new FormLayout( );
		titleArea.setLayout( layout );

		// add a dispose listener
		titleArea.addDisposeListener( new DisposeListener( ) {

			public void widgetDisposed( DisposeEvent e )
			{
				if ( titleAreaColor != null )
				{
					titleAreaColor.dispose( );
				}
			}
		} );
		// Determine the background color of the title bar
		Display display = titleArea.getDisplay( );
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
		int verticalSpacing = convertVerticalDLUsToPixels( IDialogConstants.VERTICAL_SPACING );
		int horizontalSpacing = convertHorizontalDLUsToPixels( IDialogConstants.HORIZONTAL_SPACING );
		titleArea.setBackground( background );
		// Dialog image @ right
		titleImageLabel = new Label( titleArea, SWT.CENTER );
		titleImageLabel.setBackground( background );
		if ( titleImage == null || titleImage.isDisposed( ) )
		{
			titleImageLabel.setImage( JFaceResources.getImage( DLG_IMG_TITLE_BANNER ) );
		}
		else
		{
			titleImageLabel.setImage( titleImage );
		}
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
		titleLabel = new Label( titleArea, SWT.LEFT );
		JFaceColors.setColors( titleLabel, foreground, background );
		titleLabel.setFont( JFaceResources.getBannerFont( ) );
		titleLabel.setText( " " );//$NON-NLS-1$
		FormData titleData = new FormData( );
		titleData.top = new FormAttachment( 0, verticalSpacing );
		titleData.right = new FormAttachment( titleImageLabel );
		titleData.left = new FormAttachment( 0, horizontalSpacing );
		titleLabel.setLayoutData( titleData );
		messageImageLabel = new Label( titleArea, SWT.CENTER );
		messageImageLabel.setBackground( background );
		messageLabel = new Label( titleArea, SWT.WRAP | SWT.READ_ONLY );
		JFaceColors.setColors( messageLabel, foreground, background );
		messageLabel.setText( " \n " ); // two lines//$NON-NLS-1$
		messageLabel.setFont( JFaceResources.getDialogFont( ) );
		messageLabelHeight = messageLabel.computeSize( SWT.DEFAULT, SWT.DEFAULT ).y;
		leftFillerLabel = new Label( titleArea, SWT.CENTER );
		leftFillerLabel.setBackground( background );
		setLayoutsForNormalMessage( verticalSpacing, horizontalSpacing );
		determineTitleImageLargest( );

		Label titleBarSeparator = new Label( parent, SWT.HORIZONTAL
				| SWT.SEPARATOR );
		titleBarSeparator.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
	}

	public void setErrorMessage( String newErrorMessage )
	{
		// Any change?
		if ( errorMessage == null ? newErrorMessage == null
				: errorMessage.equals( newErrorMessage ) )
		{
			return;
		}
		errorMessage = newErrorMessage;

		// Clear or set error message.
		if ( errorMessage == null )
		{
			if ( messageArea != null && !showingWarning )
			{
				setMessageAreaVisible( false );
			}
			if ( showingError )
			{
				// we were previously showing an error
				showingError = false;
			}
			// show the message
			// avoid calling setMessage in case it is overridden to call
			// setErrorMessage,
			// which would result in a recursive infinite loop
			if ( message == null )
			{
				// setMessage does this conversion....
				message = ""; //$NON-NLS-1$
			}
			updateMessage( message );
			messageImageLabel.setImage( messageImage );
			setImageLabelVisible( messageImage != null );

			if ( showingWarning )
				setWarningMessage( warningMessage );

		}
		else
		{
			if ( !showingError )
			{
				// we were not previously showing an error
				showingError = true;
			}
			if ( showingWarning )
				setWarningMessage( null );

			if ( messageArea == null )
			{
				// create a message area to display the error
				messageArea = new ImageAndMessageArea( titleArea, SWT.WRAP );
				messageArea.setBackground( messageLabel.getBackground( ) );

				animator = Policy.getAnimatorFactory( )
						.createAnimator( messageArea );
			}
			// show the error
			messageArea.setToolTipText( errorMessage );
			messageArea.setText( errorMessage );
			messageArea.setImage( JFaceResources.getImage( DLG_IMG_TITLE_ERROR ) );
			setMessageAreaVisible( true );
		}
		int verticalSpacing = convertVerticalDLUsToPixels( IDialogConstants.VERTICAL_SPACING );
		int horizontalSpacing = convertHorizontalDLUsToPixels( IDialogConstants.HORIZONTAL_SPACING );
		setLayoutsForNormalMessage( verticalSpacing, horizontalSpacing );
	}

	private void updateMessage( String newMessage )
	{
		messageLabel.setText( newMessage );
	}

	private void setMessageAreaVisible( boolean visible )
	{
		messageArea.moveAbove( null );

		// assumes that bottom of the message area should match
		// the bottom of te parent composite.
		int bottom = titleArea.getBounds( ).y + titleArea.getBounds( ).height;

		// Only set bounds if the message area is CLOSED (i.e. not visible)
		// and out of place. The bounds are dependent on whether a message
		// image is being shown.
		Rectangle msgLabelBounds = messageLabel.getBounds( );
		if ( !messageArea.isVisible( ) && messageArea.getBounds( ).y != bottom )
		{
			messageArea.setBounds( ( messageImageLabel == null ) ? msgLabelBounds.x
					: messageImageLabel.getBounds( ).x,
					bottom,
					( messageImageLabel == null ) ? msgLabelBounds.width
							: msgLabelBounds.width
									+ messageImageLabel.getBounds( ).width,
					messageArea.computeSize( SWT.DEFAULT, SWT.DEFAULT ).y );
		}
		animator.setVisible( visible );
		setMessageLayoutData( );
	}

    private void setMessageLayoutData() {
    	if(messageArea == null) 
    		return;
        FormData messageAreaData = new FormData();
        messageAreaData.right = new FormAttachment(titleImageLabel);
        messageAreaData.left = new FormAttachment(leftFillerLabel);
        messageAreaData.bottom = new FormAttachment(100,0);
        messageArea.setLayoutData(messageAreaData);
    }
    
    private void setImageLabelVisible(boolean visible) {
        messageImageLabel.setVisible(visible);
        leftFillerLabel.setVisible(visible);
    }
    
	public void setTitleMessage( String message )
	{
		if ( messageLabel != null )
			messageLabel.setText( message );
	}

	public void setTitleTitle( String title )
	{
		if ( titleLabel != null )
			titleLabel.setText( title );
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

	private boolean titleImageLargest = true;
	private Composite titleArea;

	private void setLayoutsForNormalMessage( int verticalSpacing,
			int horizontalSpacing )
	{
		FormData messageLabelData = new FormData( );
		messageLabelData.top = new FormAttachment( titleLabel, verticalSpacing );
		messageLabelData.right = new FormAttachment( titleImageLabel );
		messageLabelData.left = new FormAttachment( messageImageLabel,
				horizontalSpacing );
		messageLabelData.height = messageLabelHeight;
		if ( titleImageLargest )
		{
			messageLabelData.bottom = new FormAttachment( titleImageLabel,
					0,
					SWT.BOTTOM );
		}
		messageLabel.setLayoutData( messageLabelData );
		FormData imageLabelData = new FormData( );
		imageLabelData.top = new FormAttachment( titleLabel, verticalSpacing );
		imageLabelData.left = new FormAttachment( leftFillerLabel );
		imageLabelData.right = new FormAttachment( messageLabel );
		messageImageLabel.setLayoutData( imageLabelData );

		FormData data = new FormData( );
		data.top = new FormAttachment( titleLabel, 0, SWT.TOP );
		data.left = new FormAttachment( 0, H_GAP_IMAGE );
		data.bottom = new FormAttachment( messageLabel, 0, SWT.BOTTOM );
		leftFillerLabel.setLayoutData( data );

	}

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

	private void setWarningMessage(String newMessage) {
        // Any change?
        if (warningMessage == null ? newMessage == null : warningMessage
                .equals(newMessage)) {
			return;
		}
        warningMessage = newMessage;
         
        //Clear or set warning message.
        if (warningMessage == null) {
        	if(messageArea != null && !showingError)
           		setMessageAreaVisible(false);
        	
            if (showingWarning)
                showingWarning = false;

         } else {
            if (!showingWarning)
                showingWarning = true;

            warningMessage = newMessage;
            if(messageArea == null){
            	// create a message area to display the error
            	messageArea = new ImageAndMessageArea(titleArea, SWT.WRAP);
            	messageArea.setBackground(messageLabel.getBackground());
       		
           		animator = Policy.getAnimatorFactory().createAnimator(messageArea);
            }
            // show the error
            messageArea.setToolTipText(warningMessage);
            messageArea.setText(warningMessage);
            messageArea.setImage(JFaceResources.getImage(DLG_IMG_MESSAGE_WARNING));
            setMessageAreaVisible(true);
         }
        int verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
        int horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
        setLayoutsForNormalMessage(verticalSpacing, horizontalSpacing);
    } 
}
