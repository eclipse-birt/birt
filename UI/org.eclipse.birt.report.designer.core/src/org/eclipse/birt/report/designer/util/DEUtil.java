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

package org.eclipse.birt.report.designer.util;

import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.core.IReportElementConstants;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.ListBandProxy;
import org.eclipse.birt.report.designer.core.model.views.data.DataSetItemModel;
import org.eclipse.birt.report.designer.core.model.views.outline.ReportElementModel;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.GraphicMasterPageHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.GroupElementHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.ISlotDefn;
import org.eclipse.birt.report.model.api.util.ColorUtil;
import org.eclipse.birt.report.model.api.util.DimensionUtil;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.GraphicMasterPage;
import org.eclipse.birt.report.model.elements.GridItem;
import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.elements.ParameterGroup;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.elements.TextItem;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;

/**
 * This class integrated some methods that will be used in GUI. It provides the
 * information that GUI will use and is called widely. *
 */
public class DEUtil
{

	/** Target can't contain source */
	public static final int CONTAIN_NO = 0;

	/** Target can contain source */
	public static final int CONTAIN_THIS = 1;

	/** Target's parent can contain source */
	public static final int CONTAIN_PARENT = 2;

	/**
	 * Property name for element labelContent.
	 */
	public static final String ELEMENT_LABELCONTENT_PROPERTY = "labelContent"; //$NON-NLS-1$

	/**
	 * A default quick button height which if different in win32 from other OS.
	 */
	public static final int QUICK_BUTTON_HEIGHT = Platform.getOS( )
			.equals( Platform.OS_WIN32 ) ? 20 : 22;

	private static HashMap propertiesMap = new HashMap( );

	private static ArrayList notSupportList = new ArrayList( );

	static
	{
		propertiesMap.put( Label.TEXT_PROP, ELEMENT_LABELCONTENT_PROPERTY );
		propertiesMap.put( TextItem.CONTENT_PROP, ELEMENT_LABELCONTENT_PROPERTY );

		//do not support following element in release 1
		notSupportList.add( DesignEngine.getMetaDataDictionary( )
				.getElement( ReportDesignConstants.LINE_ITEM ) );
		notSupportList.add( DesignEngine.getMetaDataDictionary( )
				.getElement( ReportDesignConstants.FREE_FORM_ITEM ) );
		notSupportList.add( DesignEngine.getMetaDataDictionary( )
				.getElement( ReportDesignConstants.TEXT_DATA_ITEM ) );
		notSupportList.add( DesignEngine.getMetaDataDictionary( )
				.getElement( ReportDesignConstants.GRAPHIC_MASTER_PAGE_ELEMENT ) );

	}

	/**
	 * Gets the support list of the given parent element and the slotID.
	 * 
	 * @param parent
	 *            the parent element
	 * @param slotId
	 *            the slotID
	 * @return the element list that is supported with the given parent element
	 *         and in the given slotID
	 */
	public static List getElementSupportList( DesignElementHandle parent,
			int slotId )
	{
		List list = new ArrayList( );
		ISlotDefn slotDefn = parent.getElement( ).getDefn( ).getSlot( slotId );
		if ( slotDefn != null )
		{
			list.addAll( slotDefn.getContentElements( ) );
			list.removeAll( notSupportList );
		}
		return list;
	}

	/**
	 * Get the containable element type list of give slot handle
	 * 
	 * @param slotHandle
	 */
	public static List getElementSupportList( SlotHandle slotHandle )
	{
		return getElementSupportList( slotHandle.getElementHandle( ),
				slotHandle.getSlotID( ) );
	}

	/**
	 * Gets the support list of the given parent element. The slotID is decided
	 * by the parent element.
	 * 
	 * @param parent
	 *            the parent element
	 * @return the the support list of the element
	 */
	public static List getElementSupportList( DesignElementHandle parent )
	{
		int slotID = -1;
		if ( parent instanceof MasterPageHandle )
		{
			slotID = GraphicMasterPage.CONTENT_SLOT;
		}
		return getElementSupportList( parent, slotID );
	}

	/**
	 * Find the position of the element. If the element is null, the position is
	 * last
	 * 
	 * @param parent
	 * @param element
	 * @return position
	 */
	public static int findInsertPosition( DesignElementHandle parent,
			DesignElementHandle element, int slotID )
	{
		if ( element == null )
		{
			SlotHandle slotHandle = parent.getSlot( slotID );
			if ( slotHandle != null )
			{
				return slotHandle.getCount( );
			}
			return -1;
		}
		return DEUtil.findPos( parent, slotID, element );
	}

	/**
	 * Finds the position of the child element in the parent element with the
	 * given slotID
	 * 
	 * @param parent
	 *            the parent element
	 * @param slotID
	 *            the slotID
	 * @param child
	 *            the child element
	 * @return the position of the child element
	 */

	public static int findPos( DesignElementHandle parent, int slotID,
			DesignElementHandle child )
	{
		assert slotID >= 0;
		SlotHandle slotHandle = parent.getSlot( slotID );
		return slotHandle.findPosn( child.getElement( ) );
	}

	/**
	 * Gets the element name. The object is a long string that separated with
	 * the separator "."
	 * 
	 * @param obj
	 *            the object
	 * @return the name behind the last separator "."
	 */

	public static String getElementName( Object obj )
	{
		if ( obj instanceof Class )
		{
			obj = ( (Class) obj ).getName( );
		}
		return obj.toString( )
				.substring( obj.toString( ).lastIndexOf( "." ) + 1 ); //$NON-NLS-1$
	}

	/**
	 * Get display label of report element
	 * 
	 * @param obj
	 */
	public static String getDisplayLabel( Object obj )
	{
		if ( obj instanceof DesignElementHandle )
		{
			DesignElementHandle handle = (DesignElementHandle) obj;
			String elementName = handle.getDefn( ).getDisplayName( );
			String displayName = handle.getDisplayLabel( DesignElement.USER_LABEL );
			if ( !StringUtil.isBlank( displayName ) )
			{
				return elementName + " - " + displayName; //$NON-NLS-1$
			}
			return elementName;
		}
		return ""; //$NON-NLS-1$
	}

	/**
	 * Gets the master page count.
	 * 
	 * @return the count of master page
	 */
	public static int getMasterPageAccount( )
	{
		SlotHandle slotHandle = ( SessionHandleAdapter.getInstance( )
				.getReportDesign( ).handle( ) ).getMasterPages( );

		Iterator itor = slotHandle.iterator( );

		int account = 0;

		while ( itor.hasNext( ) )
		{
			account = account + 1;
			itor.next( );
		}
		return account;
	}

	/**
	 * Get default slot id of give container element
	 * 
	 * @param parent
	 * @return slot id, -1 if not found
	 */
	public static int getDefaultSlotID( Object parent )
	{
		int slotID = -1;
		if ( parent instanceof GraphicMasterPageHandle )
		{
			slotID = GraphicMasterPage.CONTENT_SLOT;
		}
		else if ( parent instanceof ParameterGroupHandle )
		{
			slotID = ParameterGroup.PARAMETERS_SLOT;
		}
		else if ( parent instanceof ReportDesignHandle )
		{
			slotID = ReportDesign.BODY_SLOT;
		}
		else if ( parent instanceof CellHandle )
		{
			slotID = Cell.CONTENT_SLOT;
		}
		else if ( parent instanceof RowHandle )
		{
			slotID = TableRow.CONTENT_SLOT;
		}
		else if ( parent instanceof GridHandle )
		{
			slotID = GridItem.ROW_SLOT;
		}
		return slotID;
	}

	/**
	 * Get the slot id of child
	 * 
	 * @param parent
	 * @param child
	 * @return slot ID
	 */

	public static int findSlotID( Object parent, Object child )
	{
		assert parent instanceof DesignElementHandle;
		assert child instanceof DesignElementHandle;

		int slotID = ( (DesignElementHandle) parent ).findContentSlot( (DesignElementHandle) child );

		return slotID;
	}

	/**
	 * Get the slot id of child If the slot id was not found, returns the
	 * default slot id
	 * 
	 * @param parent
	 * @param child
	 * @return slot id
	 */
	public static int getSlotID( Object parent, Object child )
	{
		assert parent instanceof DesignElementHandle;

		int slotID = -1;

		if ( child != null )
		{
			slotID = findSlotID( parent, child );
		}
		else
		{
			slotID = getDefaultSlotID( parent );
		}

		return slotID;
	}

	/**
	 * Find the position of the element. If the element is null, the position is
	 * last
	 * 
	 * @param parent
	 * @param element
	 * @return position
	 */
	public static int findInsertPosition( DesignElementHandle parent,
			DesignElementHandle element )
	{
		// if after is null, insert at last
		if ( element == null )
		{
			SlotHandle slotHandle = parent.getSlot( DEUtil.getDefaultSlotID( parent ) );
			if ( slotHandle != null )
			{
				return slotHandle.getCount( );
			}
			return -1;
		}
		return DEUtil.findPos( parent, element.getContainerSlotHandle( )
				.getSlotID( ), element );
	}

	/**
	 * Map GUI defined property key to DE defined property key
	 * 
	 * @param key
	 * @return DE defined property key
	 */
	public static String getGUIPropertyKey( String key )
	{
		if ( key != null )
		{
			return (String) propertiesMap.get( key );
		}
		return null;
	}

	/**
	 * Transform other units to pixel.
	 * 
	 * @param handle
	 *            DimensionHandle of model to keep the measure and units.
	 * @return The pixel value.
	 */
	public static double convertoToPixel( Object handle )
	{
		return convertToPixel( handle, 0 );
	}

	/**
	 * Transform other units to pixel.
	 * 
	 * @param object
	 *            model to keep the measure and units.
	 * @param fontSize
	 *            the parent font size.
	 * @return The pixel value.
	 */
	public static double convertToPixel( Object object, int fontSize )
	{
		double px = 0;
		double measure = 0;
		String units = ""; //$NON-NLS-1$

		if ( object instanceof DimensionValue )
		{
			DimensionValue dimension = (DimensionValue) object;
			measure = dimension.getMeasure( );
			units = dimension.getUnits( );
		}
		else if ( object instanceof DimensionHandle )
		{
			DimensionHandle dimension = (DimensionHandle) object;
			measure = dimension.getMeasure( );
			units = dimension.getUnits( );
		}

		if ( DesignChoiceConstants.UNITS_PX.equals( units ) )
		{
			return measure;
		}

		//Default value is DesignChoiceConstants.UNITS_IN
		if ( "".equalsIgnoreCase( units ) ) //$NON-NLS-1$
		{
			px = measure;
		}

		if ( fontSize == 0 )
		{
			Font defaultFont = JFaceResources.getDefaultFont( );
			FontData[] fontData = defaultFont.getFontData( );
			fontSize = fontData[0].height;
		}

		if ( DesignChoiceConstants.UNITS_EM.equals( units ) )
		{
			px = DimensionUtil.convertTo( measure * fontSize,
					DesignChoiceConstants.UNITS_PT,
					DesignChoiceConstants.UNITS_IN ).getMeasure( );
		}
		else if ( DesignChoiceConstants.UNITS_EX.equals( units ) )
		{
			px = DimensionUtil.convertTo( measure * fontSize / 3,
					DesignChoiceConstants.UNITS_PT,
					DesignChoiceConstants.UNITS_IN ).getMeasure( );
		}
		else if ( DesignChoiceConstants.UNITS_PERCENTAGE.equals( units ) )
		{
			px = DimensionUtil.convertTo( measure * fontSize / 100,
					DesignChoiceConstants.UNITS_PT,
					DesignChoiceConstants.UNITS_IN ).getMeasure( );
		}
		//added by gao if unit is "", set the unit is Design default unit
		else if ( "".equals( units ) )//$NON-NLS-1$ 
		{
			units = ( SessionHandleAdapter.getInstance( ).getReportDesign( ).handle( ) ).getDefaultUnits( );
			px = DimensionUtil.convertTo( measure,
					units,
					DesignChoiceConstants.UNITS_IN ).getMeasure( );
		}
		else
		{
			px = DimensionUtil.convertTo( measure,
					units,
					DesignChoiceConstants.UNITS_IN ).getMeasure( );
		}

		return MetricUtility.inchToPixel( px );
	}

	/**
	 * Checks if the value can be converted to a valid Integer.
	 * 
	 * @param val
	 * @return true if the value can be converted to a valid Integer, else
	 *         false.
	 */
	public static boolean isValidInteger( String val )
	{
		try
		{
			Integer.parseInt( val );
		}
		catch ( Exception e )
		{
			return false;
		}

		return true;
	}

	/**
	 * Checks if the value is a valid number, including any integer and float,
	 * double.
	 * 
	 * @param val
	 */
	public static boolean isValidNumber( String val )
	{
		try
		{
			Double.parseDouble( val );
		}
		catch ( Exception e )
		{
			return false;
		}

		return true;
	}

	/**
	 * Try to split the given value to String[2]. The result format is as
	 * follows: [number][other]. If either part can not be determined, it will
	 * leave null.
	 * 
	 * @param value
	 *            given string value
	 * @return [number][other]
	 */
	public static String[] splitString( String value )
	{
		String[] spt = new String[2];

		if ( value != null )
		{
			for ( int i = value.length( ); i > 0; i-- )
			{
				if ( isValidNumber( value.substring( 0, i ) ) )
				{
					spt[0] = value.substring( 0, i );
					spt[1] = value.substring( i, value.length( ) );

					break;
				}
			}

			if ( spt[0] == null && spt[1] == null )
			{
				spt[1] = value;
			}
		}

		return spt;
	}

	/**
	 * If given value if null, return an empty string, or return itself.
	 * 
	 * @param value
	 *            a String value.
	 * @return non-null value.
	 */
	public static String resolveNull( String value )
	{
		return value == null ? "" : value; //$NON-NLS-1$
	}

	/**
	 * Converts the RGB object value to a String, the String format is "r,g,b",
	 * no quotation marks.
	 * 
	 * @param rgb
	 *            RGB value.
	 * @return String value.
	 */
	public static String getRGBText( RGB rgb )
	{
		if ( rgb != null )
		{
			return rgb.red + "," + rgb.green + "," + rgb.blue; //$NON-NLS-1$ //$NON-NLS-2$
		}

		return ""; //$NON-NLS-1$
	}

	/**
	 * Converts the String value to an RGB object value, the String format is
	 * "r,g,b", no quotation marks.
	 * 
	 * @param val
	 *            String value.
	 * @return RGB value.
	 */
	public static RGB getRGBValue( String val )
	{
		if ( val != null )
		{
			if ( val.startsWith( "#" ) ) //$NON-NLS-1$
			{
				int rgb = ColorUtil.parseColor( val );

				if ( rgb != -1 )
				{
					return getRGBValue( rgb );
				}
			}
			else
			{
				String[] ss = val.split( "," ); //$NON-NLS-1$
				if ( ss.length == 3 )
				{
					try
					{
						int r = Integer.parseInt( ss[0] );
						int g = Integer.parseInt( ss[1] );
						int b = Integer.parseInt( ss[2] );

						return new RGB( r, g, b );
					}
					catch ( NumberFormatException e )
					{
						return null;
					}
				}
			}
		}

		return null;
	}

	/**
	 * Converts an Integer value to an RGB object value, the Integer format is
	 * 0xRRGGBB.
	 * 
	 * @param rgbValue
	 *            Integer value.
	 * @return RGB value.
	 */
	public static RGB getRGBValue( int rgbValue )
	{
		if ( rgbValue == -1 )
		{
			return null;
		}

		return new RGB( ( rgbValue >> 16 ) & 0xff,
				( rgbValue >> 8 ) & 0xff,
				rgbValue & 0xff );
	}

	/**
	 * Converts an RGB object value to an Integer value, the Integer format is
	 * 0xRRGGBB.
	 * 
	 * @param rgb
	 *            RGB value.
	 * @return Integer value.
	 */
	public static int getRGBInt( RGB rgb )
	{
		if ( rgb == null )
		{
			return -1;
		}

		return ( ( rgb.red & 0xff ) << 16 )
				| ( ( rgb.green & 0xff ) << 8 )
				| ( rgb.blue & 0xff );
	}

	/**
	 * Gets the list of data sets which can be used for the specified element
	 * 
	 * @param handle
	 *            the handle of the element
	 * @return Returns the list of data sets which can be used for this element
	 */
	public static List getDataSetList( DesignElementHandle handle )
	{
		List dataSetList = new ArrayList( );
		if ( handle instanceof ReportElementHandle )
		{
			if ( handle instanceof ReportItemHandle )
			{
				DesignElementHandle dataSet = ( (ReportItemHandle) handle ).getDataSet( );
				if ( dataSet != null && !dataSetList.contains( dataSet ) )
				{
					dataSetList.add( dataSet );
				}
			}
			for ( Iterator itor = getDataSetList( handle.getContainer( ) ).iterator( ); itor.hasNext( ); )
			{
				DesignElementHandle dataSet = (DesignElementHandle) itor.next( );
				if ( !dataSetList.contains( dataSet ) )
				{
					dataSetList.add( dataSet );
				}
			}
		}
		return dataSetList;
	}

	/**
	 * Get definition of model property.
	 * 
	 * @param elementName
	 * @param propertyName
	 */
	public static IElementPropertyDefn getPropertyDefn( String elementName,
			String propertyName )
	{
		IElementDefn elementDefn = DesignEngine.getMetaDataDictionary()
				.getElement( elementName );
		if ( elementDefn != null )
		{
			return elementDefn.getProperty( propertyName );
		}
		return null;
	}

	/**
	 * Gets the proper expression for the given model
	 * 
	 * @param model
	 *            the given model
	 * @return Returns the proper expression for the given model, or null if no
	 *         proper one exists
	 */
	public static String getExpression( Object model )
	{
		if ( model instanceof ParameterHandle )
		{
			return IReportElementConstants.PARAMETER_PREFIX
					+ "[\"" + ( (ParameterHandle) model ).getName( ) + "\"]"; //$NON-NLS-1$ //$NON-NLS-2$
		}
		if ( model instanceof DataSetItemModel )
		{
			return /*
				    * Roll back because engine hasn't support full path yet
				    * IReportElementConstants.DATA_SET_PREFIX + "[\"" + (
				    * (DataSetHandle) ( (DataSetItemModel) model ).getParent( )
				    * ).getName( ) + "\"]." +
				    */IReportElementConstants.DATA_COLUMN_PREFIX
					+ "[\"" + DEUtil.escape( ( (DataSetItemModel) model ).getName( ) ) + "\"]";//$NON-NLS-1$ //$NON-NLS-2$
		}
		return null;
	}

	/**
	 * Get the handle's font size. if the font size is relative, calculate the
	 * actual size according to its parent.
	 * 
	 * @param handle
	 *            The style handle to work with the style properties of this
	 *            element.
	 * @return The font size
	 */
	public static String getFontSize( DesignElementHandle handle )
	{
		if ( !( handle instanceof ReportItemHandle ) )
		{
			if ( handle instanceof ReportDesignHandle )
			{
				return DesignChoiceConstants.FONT_SIZE_MEDIUM;
			}
			if ( handle instanceof GroupHandle )
			{
				handle = handle.getContainer( );
			}
		}

		StyleHandle styleHandle = handle.getPrivateStyle( );
		String fontSize = (String) ( styleHandle.getFontSize( ).getValue( ) );

		if ( fontSize.equals( DesignChoiceConstants.FONT_SIZE_LARGER ) )
		{
			String parentFontSize = getFontSize( handle.getContainer( ) );
			for ( int i = 0; i < DesignerConstants.fontSizes.length - 1; i++ )
			{
				if ( parentFontSize.equals( DesignerConstants.fontSizes[i][0] ) )
				{
					return DesignerConstants.fontSizes[i + 1][0];
				}
			}
			return DesignerConstants.fontSizes[DesignerConstants.fontSizes.length - 1][0];
		}
		else if ( fontSize.equals( DesignChoiceConstants.FONT_SIZE_SMALLER ) )
		{
			String parentFontSize = getFontSize( handle.getContainer( ) );
			for ( int i = DesignerConstants.fontSizes.length - 1; i > 0; i-- )
			{
				if ( parentFontSize.equals( DesignerConstants.fontSizes[i][0] ) )
				{
					return DesignerConstants.fontSizes[i - 1][0];
				}
			}
			return DesignerConstants.fontSizes[0][0];
		}

		return fontSize;
	}

	/**
	 * Since "&" in menu text has special meaning, we must escape it before
	 * displaying.
	 * 
	 * @param src
	 *            Source text.
	 * @return Escaped text.
	 */
	public static String getEscapedMenuItemText( String src )
	{
		if ( src != null && src.indexOf( '&' ) != -1 )
		{
			src = src.replaceAll( "\\&", "&&" ); //$NON-NLS-1$ //$NON-NLS-2$
		}

		return src;
	}

	/**
	 * Returns all font names for current system.
	 * 
	 * NOTES: Java 1.4 only support true type fonts.
	 * 
	 * @return font names.
	 */
	public static String[] getSystemFontNames( )
	{
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment( );

		return ge.getAvailableFontFamilyNames( );
	}

	/**
	 * Validates target elements can contain transfer data
	 * 
	 * @param targetObj
	 *            target elements
	 * @param transferData
	 *            transfer data,single object or array are permitted
	 * @return if target elements can be dropped
	 */
	public static boolean handleValidateTargetCanContain( Object targetObj,
			Object transferData )
	{
		return handleValidateTargetCanContain( targetObj, transferData, true ) != CONTAIN_NO;
	}

	/**
	 * Validates target elements can contain transfer data.
	 * <p>
	 * If transfer data is single element, validate target's container also
	 * 
	 * @param targetObj
	 *            target elements
	 * @param transferData
	 *            transfer data,single object or array are permitted
	 * @param validateContainer
	 *            validate target's container can contain
	 * @return If target elements can't be dropped, return CONTAIN_NO.
	 *         <p>
	 *         If target elements can be dropped, return CONTAIN_THIS.
	 *         <p>
	 *         If target's container can be dropped, return CONTAIN_PARENT
	 */
	public static int handleValidateTargetCanContain( Object targetObj,
			Object transferData, boolean validateContainer )
	{
		if ( targetObj == null || transferData == null )
			return CONTAIN_NO;

		if ( transferData instanceof StructuredSelection )
		{
			return handleValidateTargetCanContain( targetObj,
					( (StructuredSelection) transferData ).toArray( ),
					validateContainer );
		}
		else if ( transferData instanceof Object[] )
		{
			Object[] array = (Object[]) transferData;
			if ( array.length == 1 )
			{
				return handleValidateTargetCanContain( targetObj,
						array[0],
						validateContainer );
			}
			int canContainAll = CONTAIN_NO;
			for ( int i = 0; i < array.length; i++ )
			{
				int canContain = handleValidateTargetCanContain( targetObj,
						array[i],
						validateContainer );
				if ( i == 0 )
				{
					canContainAll = canContain;
				}
				if ( canContain == CONTAIN_NO || canContain != canContainAll )
				{
					return CONTAIN_NO;
				}
			}
			return canContainAll;
		}
		else
		{
			//Gets handle to test if can contain
			if ( transferData instanceof DesignElementHandle )
			{
				return handleValidateTargetCanContainByContainer( targetObj,
						(DesignElementHandle) transferData,
						validateContainer,
						transferData );
			}
			else if ( transferData instanceof DesignElement )
			{
				DesignElementHandle childHandle = ( (DesignElement) transferData ).getHandle( SessionHandleAdapter.getInstance( )
						.getReportDesign( ) );
				return handleValidateTargetCanContainByContainer( targetObj,
						childHandle,
						validateContainer,
						transferData );
			}
			else if ( transferData instanceof SlotHandle )
			{
				SlotHandle slot = (SlotHandle) transferData;
				Object[] childHandles = slot.getContents( ).toArray( );
				return handleValidateTargetCanContainByContainer( targetObj,
						childHandles,
						validateContainer,
						transferData );
			}
			else
			{
				return CONTAIN_NO;
			}
		}
	}

	protected static int handleValidateTargetCanContainByContainer(
			Object targetObj, DesignElementHandle childHandle,
			boolean validateContainer, Object transferData )
	{
		if ( targetObj instanceof DesignElementHandle )
		{
			return handleValidateTargetCanContainElementHandle( (DesignElementHandle) targetObj,
					childHandle,
					validateContainer,
					transferData );
		}
		else if ( targetObj instanceof ReportElementModel )
		{
			ReportElementModel targetModel = (ReportElementModel) targetObj;
			return targetModel.getElementHandle( )
					.canContain( targetModel.getSlotId( ), childHandle ) ? CONTAIN_THIS
					: CONTAIN_NO;
		}
		else if ( targetObj instanceof SlotHandle )
		{
			SlotHandle targetHandle = (SlotHandle) targetObj;
			return targetHandle.getElementHandle( )
					.canContain( targetHandle.getSlotID( ), childHandle ) ? CONTAIN_THIS
					: CONTAIN_NO;
		}
		else if ( targetObj instanceof ListBandProxy )
		{
			ListBandProxy targetHandle = (ListBandProxy) targetObj;
			return targetHandle.getElemtHandle( )
					.canContain( targetHandle.getSlotId( ), childHandle ) ? CONTAIN_THIS
					: CONTAIN_NO;
		}
		else
		{
			return CONTAIN_NO;
		}
	}

	protected static int handleValidateTargetCanContainByContainer(
			Object targetObj, Object[] childHandles, boolean validateContainer,
			Object transferData )
	{
		if ( childHandles.length == 0 )
		{
			return CONTAIN_NO;
		}
		for ( int i = 0; i < childHandles.length; i++ )
		{
			if ( !( childHandles[i] instanceof DesignElementHandle )
					|| handleValidateTargetCanContainByContainer( targetObj,
							(DesignElementHandle) childHandles[i],
							validateContainer,
							transferData ) == CONTAIN_NO )
			{
				return CONTAIN_NO;
			}
		}
		return CONTAIN_THIS;
	}

	protected static int handleValidateTargetCanContainElementHandle(
			DesignElementHandle targetHandle, DesignElementHandle childHandle,
			boolean validateContainer, Object transferData )
	{
		if ( targetHandle.canContain( DEUtil.getDefaultSlotID( targetHandle ),
				childHandle ) )
		{
			return CONTAIN_THIS;
		}
		else if ( targetHandle instanceof ParameterGroupHandle
				&& childHandle instanceof ParameterGroupHandle )
		{
			return CONTAIN_THIS;
		}
		else if ( validateContainer )
		//Validates target's container
		{
			if ( targetHandle.getContainer( ) == null )
			{
				return CONTAIN_NO;
			}
			if ( !targetHandle.getContainer( )
					.getDefn( )
					.getSlot( targetHandle.getContainerSlotHandle( )
							.getSlotID( ) )
					.isMultipleCardinality( ) )
			{
				//If only can contain single
				return CONTAIN_NO;
			}
			if ( targetHandle.getClass( ).equals( transferData.getClass( ) ) )
			{
				//If class type is same
				return CONTAIN_PARENT;
			}
			return targetHandle.getContainer( )
					.canContain( targetHandle.getContainerSlotHandle( )
							.getSlotID( ),
							childHandle ) ? CONTAIN_PARENT : CONTAIN_NO;
		}
		return CONTAIN_NO;
	}

	/**
	 * Validates target can contain more elements
	 * 
	 * @param targetObj
	 *            target
	 * @param length
	 *            the length of elements in source.If do not add to target, set
	 *            zero
	 * @return whether target can contain more elements
	 */
	public static boolean handleValidateTargetCanContainMore( Object targetObj,
			int length )
	{
		if ( targetObj instanceof StructuredSelection )
		{
			return handleValidateTargetCanContainMore( ( (StructuredSelection) targetObj ).toArray( ),
					length );
		}
		else if ( targetObj instanceof Object[] )
		{
			Object[] array = (Object[]) targetObj;
			for ( int i = 0; i < array.length; i++ )
			{
				if ( !handleValidateTargetCanContainMore( array[i], length ) )
				{
					return false;
				}
			}
			return true;
		}
		else if ( targetObj instanceof SlotHandle )
		{
			return handleValidateTargetCanContainMore( new ReportElementModel( (SlotHandle) targetObj ),
					length );
		}
		else if ( targetObj instanceof ReportElementModel )
		{
			ReportElementModel model = (ReportElementModel) targetObj;
			int slotId = model.getSlotId( );
			return model.getElementHandle( )
					.getDefn( )
					.getSlot( slotId )
					.isMultipleCardinality( )
					|| model.getElementHandle( ).getSlot( slotId ).getCount( ) < 1
					&& length <= 1;
		}
		return true;
	}

	/**
	 * Validates target elements can contain specified type of transfer data
	 * 
	 * @param targetObj
	 *            target elements
	 * @param dragObjType
	 *            specified type of transfer data. Type should get from
	 *            <code>ReportDesignConstants</code>
	 * @see ReportDesignConstants
	 * @return if target elements can be dropped
	 */
	public static boolean handleValidateTargetCanContainType( Object targetObj,
			String dragObjType )
	{
		DesignElementHandle targetHandle = null;
		int slotId = 0;
		if ( targetObj instanceof DesignElementHandle )
		{
			targetHandle = (DesignElementHandle) targetObj;
			slotId = DEUtil.getDefaultSlotID( targetObj );
		}
		else if ( targetObj instanceof ReportElementModel )
		{
			targetHandle = ( (ReportElementModel) targetObj ).getElementHandle( );
			slotId = ( (ReportElementModel) targetObj ).getSlotId( );
		}
		else if ( targetObj instanceof SlotHandle )
		{
			targetHandle = ( (SlotHandle) targetObj ).getElementHandle( );
			slotId = ( (SlotHandle) targetObj ).getSlotID( );
		}
		else if ( targetObj instanceof ListBandProxy )
		{
			targetHandle = ( (ListBandProxy) targetObj ).getElemtHandle( );
			slotId = ( (ListBandProxy) targetObj ).getSlotId( );
		}
		else
			return false;

		return targetHandle.canContain( slotId, dragObjType );
	}

	/**
	 * Gets the tool used to process multil-selection.
	 * 
	 * @param modelList
	 *            DE model list.
	 * @return The tool used to process multil-selection.
	 */
	public static GroupElementHandle getMultiSelectionHandle( List modelList )
	{
		ReportDesign design = SessionHandleAdapter.getInstance( )
				.getReportDesign( );
		GroupElementHandle handle = new GroupElementHandle( design, modelList );
		return handle;
	}

	/**
	 * Escapte \ and " following standard of Javascript
	 * 
	 * @param str
	 * @return
	 */
	public static String escape( String str )
	{
		String[][] chars = {
				{
						"\\\\", "\""}, {"\\\\\\\\", "\\\\\""}}; //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$
		String result = str;
		for ( int i = 0; i < chars[0].length; i++ )
		{
			result = result.replaceAll( chars[0][i], chars[1][i] );
		}
		return result;
	}

}