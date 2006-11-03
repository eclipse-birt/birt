
package org.eclipse.birt.report.designer.internal.ui.views.attributes.section;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.DescriptorToolkit;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.FormWidgetFactory;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.TextPropertyDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class TextAndTwoButtonSection extends Section
{

	public TextAndTwoButtonSection( String labelText, Composite parent,
			boolean isFormStyle )
	{
		super( labelText, parent, isFormStyle );
	}

	private int width = -1;

	private boolean fillText = false;

	protected TextPropertyDescriptor textField;

	private List secondSelectList = new ArrayList( );

	public void createSection( )
	{
		if ( firstSelectList == null )
			firstSelectList = new ArrayList( );
		if ( secondSelectList == null )
			secondSelectList = new ArrayList( );
		getLabelControl( parent );
		getTextControl( parent );
		getFirstButtonControl( parent );
		getSecondButtonControl( parent );
		getGridPlaceholder( parent );
	}

	public void layout( )
	{
		GridData gd = (GridData) textField.getControl( ).getLayoutData( );
		if ( getLayoutNum( ) > 0 )
			gd.horizontalSpan = getLayoutNum( ) - 3 - placeholder;
		else
			gd.horizontalSpan = ( (GridLayout) parent.getLayout( ) ).numColumns
					- 3
					- placeholder;
		if ( width > -1 )
		{
			gd.widthHint = width;
			gd.grabExcessHorizontalSpace = false;
		}
		else
			gd.grabExcessHorizontalSpace = fillText;

		gd = (GridData) firstButton.getLayoutData( );

		if ( firstButtonWidth > -1 )
		{
			if ( !isComputeSize )
				gd.widthHint = Math.max( firstButton.computeSize( -1, -1 ).x,
						firstButtonWidth );
			else
				gd.widthHint = firstButton.computeSize( -1, -1 ).x;
		}
		
		if ( secondButtonWidth > -1 )
		{
			if ( !isComputeSize )
				gd.widthHint = Math.max( secondButton.computeSize( -1, -1 ).x,
						secondButtonWidth );
			else
				gd.widthHint = secondButton.computeSize( -1, -1 ).x;
		}
	}

	public TextPropertyDescriptor getTextControl( )
	{
		return textField;
	}

	protected TextPropertyDescriptor getTextControl( Composite parent )
	{
		if ( textField == null )
		{
			textField = DescriptorToolkit.createTextPropertyDescriptor( true );
			if ( getProvider( ) != null )
				textField.setDescriptorProvider( getProvider( ) );
			textField.createControl( parent );
			textField.getControl( ).setLayoutData( new GridData( ) );
			textField.getControl( ).addDisposeListener( new DisposeListener( ) {

				public void widgetDisposed( DisposeEvent event )
				{
					textField = null;
				}
			} );
		}
		else
		{
			checkParent( textField.getControl( ), parent );
		}
		return textField;
	}


	protected Button firstButton;

	protected Button secondButton;

	public Button getFirstButtonControl( )
	{
		return firstButton;
	}

	protected Button getFirstButtonControl( Composite parent )
	{
		if ( firstButton == null )
		{
			firstButton = FormWidgetFactory.getInstance( ).createButton( parent,
					SWT.PUSH,
					isFormStyle );
			firstButton.setFont( parent.getFont( ) );

			firstButton.setLayoutData( new GridData( ) );
			String text = getFirstButtonText( );
			if ( text != null )
			{
				firstButton.setText( text );
			}

			text = getFirstButtonTooltipText( );
			if ( text != null )
			{
				firstButton.setToolTipText( text );
			}

			firstButton.addDisposeListener( new DisposeListener( ) {

				public void widgetDisposed( DisposeEvent event )
				{
					firstButton = null;
				}
			} );

			if ( !firstSelectList.isEmpty( ) )
				firstButton.addSelectionListener( (SelectionListener) firstSelectList.get( 0 ) );
			else
			{
				SelectionListener listener = new SelectionAdapter( ) {

					public void widgetSelected( SelectionEvent e )
					{
						onClickFirstButton( );
					}
				};
				firstSelectList.add( listener );
			}

		}
		else
		{
			checkParent( firstButton, parent );
		}
		return firstButton;
	}

	protected Button getSecondButtonControl( Composite parent )
	{
		if ( secondButton == null )
		{
			secondButton = FormWidgetFactory.getInstance( ).createButton( parent,
					SWT.PUSH,
					isFormStyle );
			secondButton.setFont( parent.getFont( ) );

			secondButton.setLayoutData( new GridData( ) );
			String text = getSecondButtonText( );
			if ( text != null )
			{
				secondButton.setText( text );
			}

			text = getSecondButtonTooltipText( );
			if ( text != null )
			{
				secondButton.setToolTipText( text );
			}

			secondButton.addDisposeListener( new DisposeListener( ) {

				public void widgetDisposed( DisposeEvent event )
				{
					secondButton = null;
				}
			} );

			if ( !secondSelectList.isEmpty( ) )
				secondButton.addSelectionListener( (SelectionListener) secondSelectList.get( 0 ) );
			else
			{
				SelectionListener listener = new SelectionAdapter( ) {

					public void widgetSelected( SelectionEvent e )
					{
						onClickSecondButton( );
					}
				};
				secondSelectList.add( listener );
			}

		}
		else
		{
			checkParent( secondButton, parent );
		}
		return secondButton;
	}

	
	private String getSecondButtonTooltipText( )
	{
		return secondButtonTooltipText;
	}

	private String getSecondButtonText( )
	{
		return secondButtonText;
	}

	private String firstButtonText;
	
	private String secondButtonText;

	IDescriptorProvider provider;

	public IDescriptorProvider getProvider( )
	{
		return provider;
	}

	public void setProvider( IDescriptorProvider provider )
	{
		this.provider = provider;
		if ( textField != null )
			textField.setDescriptorProvider( provider );
	}

	protected List firstSelectList = new ArrayList( );

	/**
	 * if use this method , you couldn't use the onClickButton method.
	 */
	public void addFirstSelectionListener( SelectionListener listener )
	{
		if ( !firstSelectList.contains( listener ) )
		{
			if ( !firstSelectList.isEmpty( ) )
				removeFirstSelectionListener( (SelectionListener) firstSelectList.get( 0 ) );
			firstSelectList.add( listener );
			if ( firstButton != null )
				firstButton.addSelectionListener( listener );
		}
	}

	public void removeFirstSelectionListener( SelectionListener listener )
	{
		if ( firstSelectList.contains( listener ) )
		{
			firstSelectList.remove( listener );
			if ( firstButton != null )
				firstButton.removeSelectionListener( listener );
		}
	}
	
	/**
	 * if use this method , you couldn't use the onClickButton method.
	 */
	public void addSecondSelectionListener( SelectionListener listener )
	{
		if ( !secondSelectList.contains( listener ) )
		{
			if ( !secondSelectList.isEmpty( ) )
				removeSecondSelectionListener( (SelectionListener) secondSelectList.get( 0 ) );
			secondSelectList.add( listener );
			if ( secondButton != null )
				secondButton.addSelectionListener( listener );
		}
	}

	public void removeSecondSelectionListener( SelectionListener listener )
	{
		if ( secondSelectList.contains( listener ) )
		{
			secondSelectList.remove( listener );
			if ( secondButton != null )
				secondButton.removeSelectionListener( listener );
		}
	}

	protected void onClickFirstButton( )
	{
	};

	protected void onClickSecondButton( )
	{
	};

	
	public void forceFocus( )
	{
		textField.getControl( ).forceFocus( );
	}

	public void setInput( Object input )
	{
		textField.setInput( input );
	}

	public void load( )
	{
		textField.load( );
	}

	private int firstButtonWidth = 60;

	public void setFirstButtonWidth( int buttonWidth )
	{
		this.firstButtonWidth = buttonWidth;
		if ( firstButton != null )
		{
			GridData data = new GridData( );
			data.widthHint = Math.max( firstButton.computeSize( -1, -1 ).x,
					buttonWidth );;
			data.grabExcessHorizontalSpace = false;
			firstButton.setLayoutData( data );
		}
	}

	
	private int secondButtonWidth = 60;

	public void setSecondButtonWidth( int buttonWidth )
	{
		this.secondButtonWidth = buttonWidth;
		if ( secondButton != null )
		{
			GridData data = new GridData( );
			data.widthHint = Math.max( secondButton.computeSize( -1, -1 ).x,
					buttonWidth );;
			data.grabExcessHorizontalSpace = false;
			secondButton.setLayoutData( data );
		}
	}

	
	private boolean isComputeSize = false;

	public int getWidth( )
	{
		return width;
	}

	public void setWidth( int width )
	{
		this.width = width;
	}

	public int getFirstButtonWidth( )
	{
		return firstButtonWidth;
	}

	private String oldValue;

	public void setStringValue( String value )
	{
		if ( textField != null )
		{
			if ( value == null )
			{
				value = "";//$NON-NLS-1$
			}
			oldValue = textField.getText( );
			if ( !oldValue.equals( value ) )
			{
				textField.setText( value );
			}
		}
	}

	public boolean isFillText( )
	{
		return fillText;
	}

	public void setFillText( boolean fillText )
	{
		this.fillText = fillText;
	}

	public void setHidden( boolean isHidden )
	{
		if ( displayLabel != null )
			WidgetUtil.setExcludeGridData( displayLabel, isHidden );
		if ( textField != null )
			textField.setHidden( isHidden );
		if ( firstButton != null )
			WidgetUtil.setExcludeGridData( firstButton, isHidden );
		if ( placeholderLabel != null )
			WidgetUtil.setExcludeGridData( placeholderLabel, isHidden );
	}

	public void setVisible( boolean isVisible )
	{
		if ( displayLabel != null )
			displayLabel.setVisible( isVisible );
		if ( textField != null )
			textField.setVisible( isVisible );
		if ( firstButton != null )
			firstButton.setVisible( isVisible );
		if ( placeholderLabel != null )
			placeholderLabel.setVisible( isVisible );
	}

	private String firstButtonTooltipText;

	public void setFirstButtonTooltipText( String string )
	{
		this.firstButtonTooltipText = string;
		if ( firstButton != null )
			firstButton.setText( firstButtonTooltipText );

	}
	
	private String secondButtonTooltipText;

	public void setSecondButtonTooltipText( String string )
	{
		this.secondButtonTooltipText = string;
		if ( secondButton != null )
			secondButton.setText( secondButtonTooltipText );

	}

	public String getFirstButtonText( )
	{
		return firstButtonText;
	}

	public void setFristButtonText( String buttonText )
	{
		this.firstButtonText = buttonText;
		if ( firstButton != null )
			firstButton.setText( buttonText );
	}
	
	public void setSecondButtonText( String buttonText )
	{
		this.secondButtonText = buttonText;
		if ( secondButton != null )
			secondButton.setText( buttonText );
	}

	public String getFirstButtonTooltipText( )
	{
		return firstButtonTooltipText;
	}

	public boolean buttonIsComputeSize( )
	{
		return isComputeSize;
	}

	public void setButtonIsComputeSize( boolean isComputeSize )
	{
		this.isComputeSize = isComputeSize;
	}

}
