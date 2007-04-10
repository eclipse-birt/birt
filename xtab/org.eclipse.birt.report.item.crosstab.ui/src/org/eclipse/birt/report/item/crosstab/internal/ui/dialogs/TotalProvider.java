package org.eclipse.birt.report.item.crosstab.internal.ui.dialogs;

import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.jface.viewers.LabelProvider;


public class TotalProvider extends LabelProvider
{
	public String[] getFunctionDisplayNames( )
	{
		IChoice[] choices = getFunctions( );
		if ( choices == null )
			return new String[0];

		String[] displayNames = new String[choices.length];
		for ( int i = 0; i < choices.length; i++ )
		{
			displayNames[i] = choices[i].getDisplayName( );
		}
		return displayNames;

	}

	public String[] getFunctionNames( )
	{
		IChoice[] choices = getFunctions( );
		if ( choices == null )
			return new String[0];

		String[] displayNames = new String[choices.length];
		for ( int i = 0; i < choices.length; i++ )
		{
			displayNames[i] = choices[i].getName( );
		}
		return displayNames;
	}

	public String getFunctionDisplayName( String name )
	{
		return ChoiceSetFactory.getDisplayNameFromChoiceSet( name,
				DEUtil.getMetaDataDictionary( )
						.getChoiceSet( DesignChoiceConstants.CHOICE_MEASURE_FUNCTION ) );
	}

	private IChoice[] getFunctions( )
	{
		return DEUtil.getMetaDataDictionary( )
				.getChoiceSet( DesignChoiceConstants.CHOICE_MEASURE_FUNCTION )
				.getChoices( );
	}
}
