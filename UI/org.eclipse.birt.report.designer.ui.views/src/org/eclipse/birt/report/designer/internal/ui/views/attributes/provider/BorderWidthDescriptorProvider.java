
package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;

public class BorderWidthDescriptorProvider extends StyleComboProvider
{

	private static final String LABEL_WIDTH = Messages.getString( "BordersPage.Label.Width" ); //$NON-NLS-1$

	public BorderWidthDescriptorProvider( )
	{
		super( );
	}

	public String getDisplayName( )
	{
		return LABEL_WIDTH;
	}

	public Object load( )
	{
		String value = getLocalStringValue( StyleHandle.BORDER_LEFT_WIDTH_PROP );
		if ( !"".equals( value ) )
		{
			this.indexText = value;
			return value;
		}

		value = getLocalStringValue( StyleHandle.BORDER_RIGHT_WIDTH_PROP );
		if ( !"".equals( value ) )
		{
			this.indexText = value;
			return value;
		}

		value = getLocalStringValue( StyleHandle.BORDER_TOP_WIDTH_PROP );
		if ( !"".equals( value ) )
		{
			this.indexText = value;
			return value;
		}

		value = getLocalStringValue( StyleHandle.BORDER_BOTTOM_WIDTH_PROP );
		if ( !"".equals( value ) )
		{
			this.indexText = value;
			return value;
		}
		return indexText;
	}

	public void save( Object value ) throws SemanticException
	{
		this.indexText = value == null ? "" : value;
		if ( ( (Boolean) styleMap.get( StyleHandle.BORDER_TOP_STYLE_PROP ) ).booleanValue( ) == true )
		{
			save( StyleHandle.BORDER_TOP_WIDTH_PROP, value );
		}
		else
			save( StyleHandle.BORDER_TOP_WIDTH_PROP, null );

		if ( ( (Boolean) styleMap.get( StyleHandle.BORDER_BOTTOM_STYLE_PROP ) ).booleanValue( ) == true )
		{
			save( StyleHandle.BORDER_BOTTOM_WIDTH_PROP, value );
		}
		else
			save( StyleHandle.BORDER_BOTTOM_WIDTH_PROP, null );

		if ( ( (Boolean) styleMap.get( StyleHandle.BORDER_LEFT_STYLE_PROP ) ).booleanValue( ) == true )
		{
			save( StyleHandle.BORDER_LEFT_WIDTH_PROP, value );
		}
		else
			save( StyleHandle.BORDER_LEFT_WIDTH_PROP, null );

		if ( ( (Boolean) styleMap.get( StyleHandle.BORDER_RIGHT_STYLE_PROP ) ).booleanValue( ) == true )
		{
			save( StyleHandle.BORDER_RIGHT_WIDTH_PROP, value );
		}
		else
			save( StyleHandle.BORDER_RIGHT_WIDTH_PROP, null );
	}

	private String[] getWidths( IChoiceSet choiceSet )
	{

		IChoice[] choices = choiceSet.getChoices( );

		String[] strChoices = new String[choices.length];

		for ( int i = 0; i < choices.length; i++ )
		{
			strChoices[i] = choices[i].getName( );
		}
		return strChoices;
	}

	public Object[] getItems( )
	{
		return getWidths( ChoiceSetFactory.getElementChoiceSet( ReportDesignConstants.STYLE_ELEMENT,
				StyleHandle.BORDER_TOP_WIDTH_PROP ) );
	}

	public void handleModifyEvent( )
	{
		try
		{
			if ( indexText != null )
				save( indexText );
		}
		catch ( Exception e )
		{
		}
	}
}
