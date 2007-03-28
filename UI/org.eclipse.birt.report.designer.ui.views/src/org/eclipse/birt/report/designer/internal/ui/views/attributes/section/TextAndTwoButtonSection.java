
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
//		getFirstButtonControl( parent );
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
		
		gd = (GridData) secondButton.getLayoutData( );
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




	protected Button secondButton;

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

//	private String firstButtonText;
	
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
		if(textField!=null && !textField.getControl( ).isDisposed( ))textField.load( );
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
		if ( placeholderLabel != null )
			WidgetUtil.setExcludeGridData( placeholderLabel, isHidden );
	}

	public void setVisible( boolean isVisible )
	{
		if ( displayLabel != null )
			displayLabel.setVisible( isVisible );
		if ( textField != null )
			textField.setVisible( isVisible );
		if ( placeholderLabel != null )
			placeholderLabel.setVisible( isVisible );
	}

	
	private String secondButtonTooltipText;

	public void setSecondButtonTooltipText( String string )
	{
		this.secondButtonTooltipText = string;
		if ( secondButton != null )
			secondButton.setText( secondButtonTooltipText );

	}


	public void setSecondButtonText( String buttonText )
	{
		this.secondButtonText = buttonText;
		if ( secondButton != null )
			secondButton.setText( buttonText );
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
