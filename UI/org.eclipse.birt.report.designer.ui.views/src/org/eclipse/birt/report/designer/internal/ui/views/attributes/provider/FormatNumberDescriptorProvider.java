
package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.FormatChangeEvent;
import org.eclipse.birt.report.designer.internal.ui.dialogs.IFormatChangeListener;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.NumberFormatValue;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;

import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.Currency;
import com.ibm.icu.util.ULocale;

public class FormatNumberDescriptorProvider implements IDescriptorProvider
{

	public String getDisplayName( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void save( Object value ) throws SemanticException
	{
		String[] result = (String[]) value;
		if ( result.length == 2 )
		{
			SessionHandleAdapter.getInstance( )
					.getCommandStack( )
					.startTrans( Messages.getString( "FormatNumberAttributePage.Trans.SetNumberFormat" ) );

			for ( Iterator iter = DEUtil.getInputElements( input ).iterator( ); iter.hasNext( ); )
			{
				DesignElementHandle element = (DesignElementHandle) iter.next( );
				try
				{
					if ( result[0] == null && result[1] == null )
					{
						element.setProperty( IStyleModel.NUMBER_FORMAT_PROP,
								null );
					}
					else
					{
						element.getPrivateStyle( )
								.setNumberFormatCategory( result[0] );
						element.getPrivateStyle( ).setNumberFormat( result[1] );
					}
				}
				catch ( SemanticException e )
				{
					SessionHandleAdapter.getInstance( )
							.getCommandStack( )
							.rollbackAll( );
					ExceptionHandler.handle( e );
				}
			}
			SessionHandleAdapter.getInstance( ).getCommandStack( ).commit( );
		}
	}

	private Object input;

	public void setInput( Object input )
	{
		this.input = input;

	}

	public int getIndexOfCategory( String name )
	{
		if ( initChoiceArray( ) != null )
		{
			for ( int i = 0; i < choiceArray.length; i++ )
			{
				if ( choiceArray[i][1].equals( name ) )
				{
					return i;
				}
			}
		}
		return 0;
	}

	/**
	 * Gets the corresponding category for given display name.
	 */

	public String getCategory4DisplayName( String displayName )
	{
		if ( initChoiceArray( ) != null )
		{
			for ( int i = 0; i < choiceArray.length; i++ )
			{
				if ( choiceArray[i][0].equals( displayName ) )
				{
					return choiceArray[i][1];
				}
			}
		}
		return displayName;
	}

	/**
	 * Gets the corresponding internal display name given the category.
	 * 
	 * @param category
	 * @return
	 */

	public String getDisplayName4Category( String category )
	{
		return ChoiceSetFactory.getStructDisplayName( NumberFormatValue.FORMAT_VALUE_STRUCT,
				NumberFormatValue.CATEGORY_MEMBER,
				category );
	}

	public String getPatternForCategory( String category )
	{
		String pattern = null;
		if ( DesignChoiceConstants.NUMBER_FORMAT_TYPE_CURRENCY.equals( category ) )
		{
			pattern = "\u00A4###,##0.00"; //$NON-NLS-1$
			Currency currency = Currency.getInstance( ULocale.getDefault( ) );
			if ( currency != null )
			{
				String symbol = currency.getSymbol( );
				NumberFormat formater = NumberFormat.getCurrencyInstance( );
				String result = formater.format( 1 );
				if ( result.endsWith( symbol ) )
				{
					pattern = "###,##0.00\u00A4";//$NON-NLS-1$
				}
			}
		}
		else if ( DesignChoiceConstants.NUMBER_FORMAT_TYPE_FIXED.equals( category ) )
		{
			pattern = "#0.00"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.NUMBER_FORMAT_TYPE_PERCENT.equals( category ) )
		{
			pattern = "0.00%"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.NUMBER_FORMAT_TYPE_SCIENTIFIC.equals( category ) )
		{
			pattern = "0.00E00"; //$NON-NLS-1$
		}
		else
		{
			pattern = ""; //$NON-NLS-1$
		}
		return pattern;
	}

	public void fireFormatChanged( String newCategory, String newPattern )
	{
		if ( listeners.isEmpty( ) )
		{
			return;
		}
		FormatChangeEvent event = new FormatChangeEvent( this,
				StyleHandle.NUMBER_FORMAT_PROP,
				newCategory,
				newPattern );
		for ( Iterator iter = listeners.iterator( ); iter.hasNext( ); )
		{
			Object listener = iter.next( );
			if ( listener instanceof IFormatChangeListener )
			{
				( (IFormatChangeListener) listener ).formatChange( event );
			}
		}
	}

	private java.util.List listeners = new ArrayList( );

	public void addFormatChangeListener( IFormatChangeListener listener )
	{
		if ( !listeners.contains( listener ) )
		{
			listeners.add( listener );
		}
	}

	public String getPattern( String displayName )
	{
		String category = ChoiceSetFactory.getStructPropValue( NumberFormatValue.FORMAT_VALUE_STRUCT,
				NumberFormatValue.CATEGORY_MEMBER,
				displayName );

		return getPatternForCategory( category );

	}

	public String NUMBER_FORMAT_TYPE_UNFORMATTED = DesignChoiceConstants.NUMBER_FORMAT_TYPE_UNFORMATTED;

	public String NUMBER_FORMAT_TYPE_GENERAL_NUMBER = DesignChoiceConstants.NUMBER_FORMAT_TYPE_GENERAL_NUMBER;

	public String NUMBER_FORMAT_TYPE_CURRENCY = DesignChoiceConstants.NUMBER_FORMAT_TYPE_CURRENCY;

	public String NUMBER_FORMAT_TYPE_FIXED = DesignChoiceConstants.NUMBER_FORMAT_TYPE_FIXED;

	public String NUMBER_FORMAT_TYPE_PERCENT = DesignChoiceConstants.NUMBER_FORMAT_TYPE_PERCENT;

	public String NUMBER_FORMAT_TYPE_SCIENTIFIC = DesignChoiceConstants.NUMBER_FORMAT_TYPE_SCIENTIFIC;

	public String NUMBER_FORMAT_TYPE_CUSTOM = DesignChoiceConstants.NUMBER_FORMAT_TYPE_CUSTOM;

	public String[][] initChoiceArray( )
	{
		if ( choiceArray == null )
		{
			IChoiceSet set = ChoiceSetFactory.getStructChoiceSet( NumberFormatValue.FORMAT_VALUE_STRUCT,
					NumberFormatValue.CATEGORY_MEMBER );
			IChoice[] choices = set.getChoices( );
			if ( choices.length > 0 )
			{
				// excludes "standard".
				choiceArray = new String[choices.length - 1][2];
				for ( int i = 0, j = 0; i < choices.length; i++ )
				{
					if ( !choices[i].getName( )
							.equals( DesignChoiceConstants.NUMBER_FORMAT_TYPE_STANDARD ) )
					{
						choiceArray[j][0] = choices[i].getDisplayName( );
						choiceArray[j][1] = choices[i].getName( );
						j++;
					}
				}
			}
			else
			{
				choiceArray = new String[0][0];
			}
		}
		return choiceArray;
	}

	private static String[][] choiceArray = null;
	private static String[] formatTypes = null;

	public String[] getFormatTypes( )
	{
		if ( initChoiceArray( ) != null )
		{
			formatTypes = new String[choiceArray.length];
			for ( int i = 0; i < choiceArray.length; i++ )
			{
				formatTypes[i] = choiceArray[i][0];
			}
		}
		else
		{
			formatTypes = new String[0];
		}
		return formatTypes;
	}

	public boolean isBlank( String fmtStr )
	{
		return StringUtil.isBlank( fmtStr );
	}

	public Object load( )
	{
		if ( DEUtil.getInputElements( input ).isEmpty( ) )
		{
			return null;
		}
		String baseCategory = ( (DesignElementHandle) DEUtil.getInputFirstElement( input ) ).getPrivateStyle( )
				.getNumberFormatCategory( );
		String basePattern = ( (DesignElementHandle) DEUtil.getInputFirstElement( input ) ).getPrivateStyle( )
				.getNumberFormat( );

		for ( Iterator iter = DEUtil.getInputElements( input ).iterator( ); iter.hasNext( ); )
		{
			DesignElementHandle handle = (DesignElementHandle) iter.next( );
			String category = handle.getPrivateStyle( )
					.getNumberFormatCategory( );
			String pattern = handle.getPrivateStyle( ).getNumberFormat( );

			if ( ( ( baseCategory == null && category == null ) || ( baseCategory != null && baseCategory.equals( category ) ) )
					&& ( ( basePattern == null && pattern == null ) || ( basePattern != null && basePattern.equals( pattern ) ) ) )
			{
				continue;
			}
			return null;
		}
		return new String[]{
				baseCategory, basePattern
		};
	}
	

}
