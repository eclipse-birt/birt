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
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.core.IReportElementConstants;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.views.data.DataSetItemModel;
import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSetParameterHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.GraphicMasterPageHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.GroupElementFactory;
import org.eclipse.birt.report.model.api.GroupElementHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IAccessControl;
import org.eclipse.birt.report.model.api.core.IDesignElement;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.metadata.IArgumentInfo;
import org.eclipse.birt.report.model.api.metadata.IArgumentInfoList;
import org.eclipse.birt.report.model.api.metadata.IClassInfo;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IMethodInfo;
import org.eclipse.birt.report.model.api.metadata.ISlotDefn;
import org.eclipse.birt.report.model.api.util.ColorUtil;
import org.eclipse.birt.report.model.api.util.DimensionUtil;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.api.util.URIUtil;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.Assert;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;

/**
 * This class integrated some methods that will be used in GUI. It provides the
 * information that GUI will use and is called widely. *
 */
public class DEUtil
{

	/**
	 * Property name for element labelContent.
	 */
	public static final String ELEMENT_LABELCONTENT_PROPERTY = "labelContent"; //$NON-NLS-1$

	private static HashMap propertiesMap = new HashMap( );

	private static ArrayList notSupportList = new ArrayList( );

	static
	{
		propertiesMap
				.put( LabelHandle.TEXT_PROP, ELEMENT_LABELCONTENT_PROPERTY );
		propertiesMap.put( TextItemHandle.CONTENT_PROP,
				ELEMENT_LABELCONTENT_PROPERTY );

		// do not support following element in release 2
		notSupportList.add( DesignEngine.getMetaDataDictionary( ).getElement(
				ReportDesignConstants.LINE_ITEM ) );
		notSupportList.add( DesignEngine.getMetaDataDictionary( ).getElement(
				ReportDesignConstants.TEMPLATE_REPORT_ITEM ) );
		notSupportList.add( DesignEngine.getMetaDataDictionary( ).getElement(
				ReportDesignConstants.FREE_FORM_ITEM ) );
		notSupportList.add( DesignEngine.getMetaDataDictionary( ).getElement(
				ReportDesignConstants.GRAPHIC_MASTER_PAGE_ELEMENT ) );

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
		ISlotDefn slotDefn = parent.getDefn( ).getSlot( slotId );
		if ( slotDefn != null )
		{
			list.addAll( slotDefn.getContentExtendedElements( ) );
			list.removeAll( notSupportList );
		}

		// Append to validate the type according to the context
		List availableList = new ArrayList( );
		for ( int i = 0; i < list.size( ); i++ )
		{
			if ( parent.canContain( slotId, ( (IElementDefn) list.get( i ) )
					.getName( ) ) )
			{
				availableList.add( list.get( i ) );
			}
		}
		return availableList;
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
			slotID = GraphicMasterPageHandle.CONTENT_SLOT;
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
		return slotHandle.findPosn( child );
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
		return obj.toString( ).substring(
				obj.toString( ).lastIndexOf( "." ) + 1 ); //$NON-NLS-1$
	}

	/**
	 * Gets the definition for the element with the specified name
	 * 
	 * @param elementName
	 *            the name of the element
	 * 
	 * @return Returns the definition, or null if the element is not defined.
	 */
	public static IElementDefn getElementDefn( String elementName )
	{
		IElementDefn defn = DesignEngine.getMetaDataDictionary( ).getElement(
				elementName );
		if ( defn == null )
		{
			defn = DesignEngine.getMetaDataDictionary( ).getExtension(
					elementName );
		}
		return defn;
	}

	/**
	 * Get display label of report element
	 * 
	 * @param obj
	 */
	public static String getDisplayLabel( Object obj )
	{
		return getDisplayLabel( obj, true );
	}

	/**
	 * Get display label of report element
	 * 
	 * @param obj
	 */
	public static String getDisplayLabel( Object obj, boolean includeElementName )
	{
		if ( obj instanceof DesignElementHandle )
		{
			DesignElementHandle handle = (DesignElementHandle) obj;
			String elementName = handle.getDefn( ).getDisplayName( );
			String displayName;
			if ( handle.getQualifiedName( ) != null
					&& !handle.getQualifiedName( ).equals( handle.getName( ) ) )
			{
				displayName = handle.getQualifiedName( );
			}
			else
			{
				displayName = handle
						.getDisplayLabel( DesignElementHandle.USER_LABEL );
			}
			if ( !StringUtil.isBlank( displayName ) )
			{
				if ( includeElementName )
				{
					return elementName + " - " + displayName; //$NON-NLS-1$
				}
				return displayName;
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
		SlotHandle slotHandle = SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( ).getMasterPages( );

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
			slotID = GraphicMasterPageHandle.CONTENT_SLOT;
		}
		else if ( parent instanceof ParameterGroupHandle )
		{
			slotID = ParameterGroupHandle.PARAMETERS_SLOT;
		}
		else if ( parent instanceof ReportDesignHandle )
		{
			slotID = ReportDesignHandle.BODY_SLOT;
		}
		else if ( parent instanceof LibraryHandle )
		{
			slotID = ModuleHandle.COMPONENT_SLOT;
		}
		else if ( parent instanceof CellHandle )
		{
			slotID = CellHandle.CONTENT_SLOT;
		}
		else if ( parent instanceof RowHandle )
		{
			slotID = RowHandle.CONTENT_SLOT;
		}
		else if ( parent instanceof GridHandle )
		{
			slotID = GridHandle.ROW_SLOT;
		}
		else if ( parent instanceof ThemeHandle )
		{
			slotID = ThemeHandle.STYLES_SLOT;
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

		int slotID = ( (DesignElementHandle) parent )
				.findContentSlot( (DesignElementHandle) child );

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
			SlotHandle slotHandle = parent.getSlot( DEUtil
					.getDefaultSlotID( parent ) );
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

		// Default value is DesignChoiceConstants.UNITS_IN
		if ( "".equalsIgnoreCase( units ) ) //$NON-NLS-1$
		{
			px = measure;
		}

		if ( fontSize == 0 )
		{
			Font defaultFont = JFaceResources.getDefaultFont( );
			FontData[] fontData = defaultFont.getFontData( );
			fontSize = fontData[0].getHeight( );
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
		// added by gao if unit is "", set the unit is Design default unit
		else if ( "".equals( units ) )//$NON-NLS-1$ 
		{
			units = SessionHandleAdapter.getInstance( ).getReportDesignHandle( )
					.getDefaultUnits( );
			px = DimensionUtil.convertTo( measure, units,
					DesignChoiceConstants.UNITS_IN ).getMeasure( );
		}
		else if ( units == null )//$NON-NLS-1$
		{
			px = 0.0;
		}
		else
		{
			px = DimensionUtil.convertTo( measure, units,
					DesignChoiceConstants.UNITS_IN ).getMeasure( );
		}

		return MetricUtility.inchToPixel( px );
	}

	/**
	 * Transform other units to target unit.
	 * 
	 * @param handle
	 * @param targetUnit
	 * @return
	 */
	public static double convertToValue( DimensionHandle handle,
			String targetUnit )
	{
		double retValue = 0.0;

		if ( handle.isSet( ) )
		{
			retValue = DimensionUtil.convertTo( handle.getMeasure( ),
					handle.getUnits( ), targetUnit ).getMeasure( );
		}
		return retValue;
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

		return new RGB( ( rgbValue >> 16 ) & 0xff, ( rgbValue >> 8 ) & 0xff,
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

		return ( ( rgb.red & 0xff ) << 16 ) | ( ( rgb.green & 0xff ) << 8 )
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
				DesignElementHandle dataSet = ( (ReportItemHandle) handle )
						.getDataSet( );
				if ( dataSet != null && !dataSetList.contains( dataSet ) )
				{
					dataSetList.add( dataSet );
				}
			}
			for ( Iterator itor = getDataSetList( handle.getContainer( ) )
					.iterator( ); itor.hasNext( ); )
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
	 * Gets the list of data sets which can be used for the specified element
	 * 
	 * @param handle
	 *            the handle of the element
	 * @return Returns the list of data sets which can be used for this element
	 *         excluding itself
	 */
	public static List getDataSetListExcludeSelf( DesignElementHandle handle )
	{
		List dataSetList = new ArrayList( );
		if ( handle instanceof ReportElementHandle )
		{
			for ( Iterator itor = getDataSetList( handle.getContainer( ) )
					.iterator( ); itor.hasNext( ); )
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
		IElementDefn elementDefn = DesignEngine.getMetaDataDictionary( )
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
					+ "[\"" + escape( ( (ParameterHandle) model ).getQualifiedName( ) ) + "\"]"; //$NON-NLS-1$ //$NON-NLS-2$
		}
		if ( model instanceof DataSetItemModel )
		{
			String colName = ( (DataSetItemModel) model ).getAlias( );

			if ( colName == null || colName.trim( ).length( ) == 0 )
			{
				colName = ( (DataSetItemModel) model ).getName( );
			}
			return getColumnExpression( colName );
		}
		if ( model instanceof ComputedColumnHandle )
		{
			return getColumnExpression( ( (ComputedColumnHandle) model )
					.getName( ) );
		}
		if ( model instanceof ResultSetColumnHandle )
		{
			return getResultSetColumnExpression( ( (ResultSetColumnHandle) model )
					.getColumnName( ) );
		}
		if ( model instanceof DataSetParameterHandle )
		{
			return IReportElementConstants.STOREDPROCUDURE_OUTPUT_PREFIX
					+ "[\"" + escape( ( (DataSetParameterHandle) model ).getName( ) ) + "\"]"; //$NON-NLS-1$ //$NON-NLS-2$
		}
		return null;
	}

	/**
	 * Returns the integer font size for string value.
	 * 
	 * @param fontSize
	 * @return
	 */
	public static int getFontSize( String fontSize )
	{
		if ( DesignChoiceConstants.FONT_SIZE_LARGER.equals( fontSize ) )
		{
			fontSize = DesignChoiceConstants.FONT_SIZE_LARGE;
		}
		else if ( DesignChoiceConstants.FONT_SIZE_SMALLER.equals( fontSize ) )
		{
			fontSize = DesignChoiceConstants.FONT_SIZE_SMALL;
		}
		else if ( fontSize == null )
		{
			fontSize = DesignChoiceConstants.FONT_SIZE_MEDIUM;
		}

		String rt = (String) DesignerConstants.fontMap.get( fontSize );

		if ( rt != null )
		{
			return Integer.parseInt( rt );
		}

		String[] sp = DEUtil.splitString( fontSize );

		if ( sp[0] != null && DEUtil.isValidNumber( sp[0] ) )
		{
			return (int) CSSUtil.convertToPoint( new DimensionValue( Double
					.parseDouble( sp[0] ), sp[1] ) );
		}

		return 10;// as medium size.
	}

	/**
	 * Get the handle's font size 's string value. if the font size is relative,
	 * calculate the actual size according to its parent.
	 * 
	 * @param handle
	 *            The style handle to work with the style properties of this
	 *            element.
	 * @return The font size string value.
	 */
	public static String getFontSize( DesignElementHandle handle )
	{
		if ( !( handle instanceof ReportItemHandle ) )
		{
			if ( handle instanceof ModuleHandle )
			{
				return DesignChoiceConstants.FONT_SIZE_MEDIUM;
			}
			if ( handle instanceof GroupHandle )
			{
				handle = handle.getContainer( );
			}
		}

		Object fontSizeValue = getModelFontSize( handle );

		if ( fontSizeValue instanceof DimensionValue )
		{
			return ( (DimensionValue) fontSizeValue ).toString( );
		}
		else if ( fontSizeValue instanceof String )
		{
			String fontSize = (String) fontSizeValue;
			if ( fontSize.equals( DesignChoiceConstants.FONT_SIZE_LARGER ) )
			{
				return getLargerFontSize( handle.getContainer( ) );
			}
			else if ( fontSize.equals( DesignChoiceConstants.FONT_SIZE_SMALLER ) )
			{
				return getSmallerFontSize( handle.getContainer( ) );
			}
			else
			{
				return fontSize;
			}
		}
		else
		{
			return DesignChoiceConstants.FONT_SIZE_MEDIUM;
		}
	}

	private static String getLargerFontSize( DesignElementHandle handle )
	{
		if ( !( handle instanceof ReportItemHandle ) )
		{
			if ( handle instanceof ModuleHandle )
			{
				for ( int i = 0; i < DesignerConstants.fontSizes.length - 1; i++ )
				{
					if ( DesignChoiceConstants.FONT_SIZE_MEDIUM
							.equals( DesignerConstants.fontSizes[i][0] ) )
					{
						return DesignerConstants.fontSizes[i + 1][0];
					}
				}
			}
			if ( handle instanceof GroupHandle )
			{
				handle = handle.getContainer( );
			}
		}

		Object fontSizeValue = getModelFontSize( handle );

		if ( fontSizeValue instanceof DimensionValue )
		{
			int parentSize = getFontSizeIntValue( handle.getContainer( ) );
			int size = (int) CSSUtil.convertToPoint( fontSizeValue, parentSize ) + 1;

			DimensionValue dm = new DimensionValue( size, "pt" ); //$NON-NLS-1$
			return dm.toString( );
		}
		else if ( fontSizeValue instanceof String )
		{
			String fontSize = (String) fontSizeValue;
			if ( fontSize.equals( DesignChoiceConstants.FONT_SIZE_LARGER ) )
			{
				return getLargerFontSize( handle.getContainer( ) );
			}
			else if ( fontSize.equals( DesignChoiceConstants.FONT_SIZE_SMALLER ) )
			{
				return getSmallerFontSize( handle.getContainer( ) );
			}
			else
			{
				for ( int i = 0; i < DesignerConstants.fontSizes.length - 1; i++ )
				{
					if ( fontSize.equals( DesignerConstants.fontSizes[i][0] ) )
					{
						return DesignerConstants.fontSizes[i + 1][0];
					}
				}
				return DesignerConstants.fontSizes[DesignerConstants.fontSizes.length - 1][0];
			}
		}
		else
		{
			return DesignChoiceConstants.FONT_SIZE_MEDIUM;
		}

	}

	private static String getSmallerFontSize( DesignElementHandle handle )
	{
		if ( !( handle instanceof ReportItemHandle ) )
		{
			if ( handle instanceof ModuleHandle )
			{
				for ( int i = DesignerConstants.fontSizes.length - 1; i > 0; i-- )
				{
					if ( DesignChoiceConstants.FONT_SIZE_MEDIUM
							.equals( DesignerConstants.fontSizes[i][0] ) )
					{
						return DesignerConstants.fontSizes[i - 1][0];
					}
				}
			}
			if ( handle instanceof GroupHandle )
			{
				handle = handle.getContainer( );
			}
		}

		Object fontSizeValue = getModelFontSize( handle );

		if ( fontSizeValue instanceof DimensionValue )
		{
			int parentSize = getFontSizeIntValue( handle.getContainer( ) );
			int size = (int) CSSUtil.convertToPoint( fontSizeValue, parentSize ) - 1;

			size = ( size < 1 ) ? 1 : size;

			DimensionValue dm = new DimensionValue( size, "pt" ); //$NON-NLS-1$
			return dm.toString( );
		}
		else if ( fontSizeValue instanceof String )
		{
			String fontSize = (String) fontSizeValue;
			if ( fontSize.equals( DesignChoiceConstants.FONT_SIZE_LARGER ) )
			{
				return getLargerFontSize( handle.getContainer( ) );
			}
			else if ( fontSize.equals( DesignChoiceConstants.FONT_SIZE_SMALLER ) )
			{
				return getSmallerFontSize( handle.getContainer( ) );
			}
			else
			{
				for ( int i = DesignerConstants.fontSizes.length - 1; i > 0; i-- )
				{
					if ( fontSize.equals( DesignerConstants.fontSizes[i][0] ) )
					{
						return DesignerConstants.fontSizes[i - 1][0];
					}
				}
				return DesignerConstants.fontSizes[0][0];
			}
		}
		else
		{
			return DesignChoiceConstants.FONT_SIZE_MEDIUM;
		}

	}

	/**
	 * Get the handle's font size int value. if the font size is relative,
	 * calculate the actual size according to its parent.
	 * 
	 * @param handle
	 *            The style handle to work with the style properties of this
	 *            element.
	 * @return The font size int value
	 */
	public static int getFontSizeIntValue( DesignElementHandle handle )
	{
		if ( !( handle instanceof ReportItemHandle ) )
		{
			if ( handle instanceof ModuleHandle )
			{
				// return 10.
				String size = (String) DesignerConstants.fontMap
						.get( DesignChoiceConstants.FONT_SIZE_MEDIUM );
				return Integer.parseInt( size );
			}
			if ( handle instanceof GroupHandle )
			{
				handle = handle.getContainer( );
			}
		}

		Object fontSizeValue = getModelFontSize( handle );

		if ( fontSizeValue instanceof DimensionValue )
		{
			// use parent's font size as the base size for converting sizeValue
			// to a int value.
			int size = getFontSizeIntValue( handle.getContainer( ) );
			return (int) CSSUtil.convertToPoint( fontSizeValue, size );
		}
		else if ( fontSizeValue instanceof String )
		{
			String fontSize = (String) fontSizeValue;

			if ( fontSize.equals( DesignChoiceConstants.FONT_SIZE_LARGER ) )
			{
				return getLargerFontSizeIntValue( handle.getContainer( ) );
			}
			else if ( fontSize.equals( DesignChoiceConstants.FONT_SIZE_SMALLER ) )
			{
				return getSmallerFontSizeIntValue( handle.getContainer( ) );
			}
			else
			{
				String size = (String) DesignerConstants.fontMap.get( fontSize );
				return Integer.parseInt( size );
			}
		}
		else
		{
			// return 10.
			String size = (String) DesignerConstants.fontMap
					.get( DesignChoiceConstants.FONT_SIZE_MEDIUM );
			return Integer.parseInt( size );
		}
	}

	private static int getLargerFontSizeIntValue( DesignElementHandle handle )
	{
		if ( !( handle instanceof ReportItemHandle ) )
		{
			if ( handle instanceof ModuleHandle )
			{
				// return 10.
				String size = (String) DesignerConstants.fontMap
						.get( DesignChoiceConstants.FONT_SIZE_MEDIUM );
				return Integer.parseInt( size ) + 1;
			}
			if ( handle instanceof GroupHandle )
			{
				handle = handle.getContainer( );
			}
		}

		Object fontSizeValue = getModelFontSize( handle );

		if ( fontSizeValue instanceof DimensionValue )
		{
			int size = getFontSizeIntValue( handle.getContainer( ) );
			return (int) CSSUtil.convertToPoint( fontSizeValue, size ) + 1;
		}
		else if ( fontSizeValue instanceof String )
		{
			String fontSize = (String) fontSizeValue;
			if ( fontSize.equals( DesignChoiceConstants.FONT_SIZE_LARGER ) )
			{
				return getLargerFontSizeIntValue( handle.getContainer( ) );
			}
			else if ( fontSize.equals( DesignChoiceConstants.FONT_SIZE_SMALLER ) )
			{
				return getSmallerFontSizeIntValue( handle.getContainer( ) );
			}
			else
			{
				for ( int i = 0; i < DesignerConstants.fontSizes.length - 1; i++ )
				{
					if ( fontSize.equals( DesignerConstants.fontSizes[i][0] ) )
					{
						return Integer
								.parseInt( DesignerConstants.fontSizes[i + 1][1] );
					}
				}
				return Integer
						.parseInt( DesignerConstants.fontSizes[DesignerConstants.fontSizes.length - 1][1] );
			}
		}
		else
		{
			// return 10 + 1.
			String size = (String) DesignerConstants.fontMap
					.get( DesignChoiceConstants.FONT_SIZE_MEDIUM );
			return Integer.parseInt( size ) + 1;
		}
	}

	private static int getSmallerFontSizeIntValue( DesignElementHandle handle )
	{
		if ( !( handle instanceof ReportItemHandle ) )
		{
			if ( handle instanceof ModuleHandle )
			{
				// return 10.
				String size = (String) DesignerConstants.fontMap
						.get( DesignChoiceConstants.FONT_SIZE_MEDIUM );
				return Integer.parseInt( size ) - 1;
			}
			if ( handle instanceof GroupHandle )
			{
				handle = handle.getContainer( );
			}
		}

		Object fontSizeValue = getModelFontSize( handle );

		if ( fontSizeValue instanceof DimensionValue )
		{
			int gParentFontSize = getFontSizeIntValue( handle.getContainer( ) );
			int size = (int) CSSUtil.convertToPoint( fontSizeValue,
					gParentFontSize ) - 1;
			if ( size < 1 )
			{
				return 1;
			}
			return size;

		}
		else if ( fontSizeValue instanceof String )
		{
			String fontSize = (String) fontSizeValue;
			if ( fontSize.equals( DesignChoiceConstants.FONT_SIZE_LARGER ) )
			{
				return getLargerFontSizeIntValue( handle.getContainer( ) );
			}
			else if ( fontSize.equals( DesignChoiceConstants.FONT_SIZE_SMALLER ) )
			{
				return getSmallerFontSizeIntValue( handle.getContainer( ) );
			}
			else
			{
				for ( int i = DesignerConstants.fontSizes.length - 1; i > 0; i-- )
				{
					if ( fontSize.equals( DesignerConstants.fontSizes[i][0] ) )
					{
						return Integer
								.parseInt( DesignerConstants.fontSizes[i - 1][1] );
					}
				}
				return Integer.parseInt( DesignerConstants.fontSizes[0][1] );
			}
		}
		else
		{
			// return 10 -1.
			String size = (String) DesignerConstants.fontMap
					.get( DesignChoiceConstants.FONT_SIZE_MEDIUM );
			return Integer.parseInt( size ) - 1;
		}
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
		return getSystemFontNames( null );
	}

	/**
	 * Returns all font names for current system. NOTES: Java 1.4 only support
	 * true type fonts.
	 * 
	 * @param comparator
	 *            Sort comparator.
	 * @return font names.
	 */
	public static String[] getSystemFontNames( Comparator comparator )
	{
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment( );

		String[] fontNames = ge.getAvailableFontFamilyNames( );

		if ( comparator != null )
		{
			Arrays.sort( fontNames, comparator );
		}
		return fontNames;
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
		ModuleHandle designHandle = SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( );
		GroupElementHandle handle = GroupElementFactory.newGroupElement(
				designHandle, modelList );
		return handle;
	}

	/**
	 * Escapes \ and " following standard of Javascript
	 * 
	 * @param str
	 * @return new string after escape special character
	 */
	public static String escape( String str )
	{
		String[][] chars = {{"\\\\", "\"", "\'", //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		}, {"\\\\\\\\", "\\\\\"", "\\\\\'", //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		}};
		String result = str;
		for ( int i = 0; i < chars[0].length; i++ )
		{
			result = result.replaceAll( chars[0][i], chars[1][i] );
		}
		return result;
	}

	/**
	 * Gets decimal string given the number of zeros.
	 */
	public static String getDecmalStr( int decPlaces )
	{

		String defaultDecs = "0000000000"; //$NON-NLS-1$
		String decStr = ""; //$NON-NLS-1$
		if ( decPlaces > 0 && decPlaces < 10 )
		{
			decStr = defaultDecs.substring( 0, decPlaces );
		}
		else if ( decPlaces >= 10 )
		{
			if ( decPlaces > 100 )
			{
				decPlaces = 100;
			}
			int quotient = decPlaces / 10;
			int remainder = decPlaces % 10;

			StringBuffer s = new StringBuffer( 100 );
			for ( int i = 0; i < quotient; i++ )
			{
				s.append( defaultDecs );
			}
			s.append( defaultDecs.substring( 0, remainder ) );

			decStr = s.toString( );
		}
		return decStr;
	}

	/**
	 * @param transferSource
	 * @return true if parameter is parameter group.
	 */
	public static boolean isParameterGroup( Object transferSource )
	{
		return ( transferSource instanceof IDesignElement && ( (IDesignElement) transferSource )
				.getDefn( ).getName( ).equals(
						ReportDesignConstants.PARAMETER_GROUP_ELEMENT ) );
	}

	/**
	 * @return Alphabetically sortted styles list.
	 */
	public static Iterator getStyles( )
	{
		return getStyles( new AlphabeticallyComparator( ) );
	}

	/**
	 * @param comparator
	 * @return return styles list sortted with given comparator.
	 */
	public static Iterator getStyles( Comparator comparator )
	{
		List styles = null;
		if ( SessionHandleAdapter.getInstance( ).getReportDesignHandle( ) instanceof ReportDesignHandle )
		{
			styles = SessionHandleAdapter.getInstance( )
					.getReportDesignHandle( ).getAllStyles( );
		}
		else if ( SessionHandleAdapter.getInstance( ).getReportDesignHandle( ) instanceof LibraryHandle )
		{
			styles = new ArrayList( );
			ThemeHandle theme = ( (LibraryHandle) SessionHandleAdapter
					.getInstance( ).getReportDesignHandle( ) ).getTheme( );

			if (theme != null)
			{
				styles.addAll(theme.getStyles().getContents());
			}
		}

		Object[] stylesArray = ( styles == null ? new Object[0] : styles
				.toArray( ) );

		if ( comparator != null )
		{
			Arrays.sort( stylesArray, comparator );
		}
		return Arrays.asList( stylesArray ).iterator( );
	}

	/**
	 * Checks if two strings have same value.
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean isSameString( String str1, String str2 )
	{
		if ( str1 == null && str2 == null )
		{
			return true;
		}
		if ( str1 != null && str1.equals( str2 ) )
		{
			return true;
		}
		return false;
	}

	/**
	 * Create a row expression base on a binding column name.
	 * 
	 * @param columnName
	 *            the column name
	 * @return the expression, or null if the column name is blank.
	 */
	public static String getColumnExpression( String columnName )
	{
		Assert.isNotNull( columnName );
		if ( StringUtil.isBlank( columnName ) )
		{
			return null;
		}
		return IReportElementConstants.BINDING_COLUMN_PREFIX
				+ "[\"" + DEUtil.escape( columnName ) + "\"]";//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Create a row expression base on a result set column name.
	 * 
	 * @param columnName
	 *            the column name
	 * @return the expression, or null if the column name is blank.
	 */
	public static String getResultSetColumnExpression( String columnName )
	{
		Assert.isNotNull( columnName );
		if ( StringUtil.isBlank( columnName ) )
		{
			return null;
		}
		return IReportElementConstants.RESULTSET_COLUMN_PREFIX
				+ "[\"" + DEUtil.escape( columnName ) + "\"]";//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Relativizes the path against this base path.
	 * 
	 * @param basePath
	 *            the base path
	 * @param path
	 *            the path to convert
	 * @return The relative path based on the base path if it is possible, or
	 *         the original path
	 * 
	 */
	public static String getRelativedPath( String basePath, String path )
	{
		File baseFile = new File( basePath );
		if ( baseFile.isFile( ) )
		{
			baseFile = baseFile.getParentFile( );
		}
		return URIUtil.getRelativePath( baseFile.getAbsolutePath( ), path );
	}

	/**
	 * Returns the handle of the action of the given element.
	 * 
	 * @param element
	 *            the element handle
	 * @return the handle of the action, or null if the element is not a proper
	 *         type
	 */
	public static ActionHandle getActionHandle( ReportItemHandle element )
	{
		ActionHandle actionHandle = null;
		if ( element instanceof LabelHandle )
		{
			actionHandle = ( (LabelHandle) element ).getActionHandle( );
		}
		else if ( element instanceof DataItemHandle )
		{
			actionHandle = ( (DataItemHandle) element ).getActionHandle( );
		}
		else if ( element instanceof ImageHandle )
		{
			actionHandle = ( (ImageHandle) element ).getActionHandle( );
		}
		return actionHandle;
	}

	/**
	 * Sets the handle of the action of the given element.
	 * 
	 * @param element
	 *            the element handle to set
	 * @param action
	 *            the action
	 * 
	 * @return the handle of the action, or null if the element is not a proper
	 *         type
	 * 
	 * @throws SemanticException
	 */
	public static ActionHandle setAction( ReportItemHandle element,
			Action action ) throws SemanticException
	{
		ActionHandle actionHandle = null;
		if ( element instanceof LabelHandle )
		{
			actionHandle = ( (LabelHandle) element ).setAction( action );
		}
		else if ( element instanceof DataItemHandle )
		{
			actionHandle = ( (DataItemHandle) element ).setAction( action );
		}
		else if ( element instanceof ImageHandle )
		{
			actionHandle = ( (ImageHandle) element ).setAction( action );
		}
		return actionHandle;
	}

	private static Object getModelFontSize( DesignElementHandle handle )
	{
		// Fix 118374
		// ReportTemplateElement is the only exception that is a DesignElemtn
		// but doesn't have Style attached.
		StyleHandle styleHandle = handle.getPrivateStyle( );
		Object fontSizeValue = null;
		if ( styleHandle != null )
		{
			fontSizeValue = handle.getPrivateStyle( ).getFontSize( ).getValue( );
		}
		return fontSizeValue;
	}

	/**
	 * @deprecated
	 */
	public static List getDataSets( )
	{
		return SessionHandleAdapter.getInstance( ).getReportDesignHandle( )
				.getVisibleDataSets( );
	}

	public static Iterator getThemes( )
	{
		return getThemes( new AlphabeticallyComparator( ) );
	}

	private static Iterator getThemes( AlphabeticallyComparator comparator )
	{
		List themes = SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( ).getVisibleThemes(
						IAccessControl.DIRECTLY_INCLUDED_LEVEL );

		Object[] themesArray = themes.toArray( );

		if ( comparator != null )
		{
			Arrays.sort( themesArray, comparator );
		}
		return Arrays.asList( themesArray ).iterator( );
	}

	/**
	 * Generates GroupElementHandle for given model list.
	 * 
	 * @param modelList
	 * @return
	 */
	public static GroupElementHandle getGroupElementHandle( List modelList )
	{
		if ( SessionHandleAdapter.getInstance( ).getReportDesignHandle( ) == null )
		{
			return GroupElementFactory.newGroupElement( SessionHandleAdapter
					.getInstance( ).getReportDesignHandle( ),
					Collections.EMPTY_LIST );
		}
		return GroupElementFactory.newGroupElement( SessionHandleAdapter
				.getInstance( ).getReportDesignHandle( ), modelList );

	}

	/**
	 * Checks if the library is included.
	 * 
	 * @param handle
	 * @return
	 */
	public static boolean isIncluded( LibraryHandle handle )
	{
		return handle.getNamespace( ) != null;
	}

	public static List getClasses( )
	{

		return getClasses( new AlphabeticallyComparator( ) );
	}

	public static List getClasses( Comparator comp )
	{
		List classes = DesignEngine.getMetaDataDictionary( ).getClasses( );
		Collections.sort( classes, comp );

		return classes;
	}

	public static List getMethods( IClassInfo classInfo )
	{
		return getMethods( classInfo, new AlphabeticallyComparator( ) );
	}

	public static List getMethods( IClassInfo classInfo, Comparator comp )
	{
		List methods = classInfo.getMethods( );
		Collections.sort( methods, comp );

		return methods;
	}

	public static List getMembers( IClassInfo classInfo )
	{
		return getMembers( classInfo, new AlphabeticallyComparator( ) );
	}

	public static List getMembers( IClassInfo classInfo, Comparator comp )
	{
		List members = classInfo.getMembers( );
		Collections.sort( members, comp );

		return members;

	}

	/**
	 * 
	 * Return DesignElementHandle avaliable method's argument type name.
	 * 
	 * @param handle
	 *            DesignElementHandle.
	 * @param methodName
	 * @param argIdex
	 *            starts from 0.
	 * @return
	 */
	public static String getMethodArgumentType( DesignElementHandle handle,
			String methodName, int argIndex )
	{
		if ( handle instanceof DataSetHandle )
			return ReportDesignConstants.DATA_SET_ELEMENT; //$NON-NLS-1$
		if ( handle instanceof DataSourceHandle )
			return ReportDesignConstants.DATA_SOURCE_ELEMENT; //$NON-NLS-1$

		List methods = handle.getDefn( ).getLocalMethods( );
		for ( Iterator iter = methods.iterator( ); iter.hasNext( ); )
		{
			IMethodInfo method = ( (ElementPropertyDefn) iter.next( ) )
					.getMethodInfo( );
			if ( method.getName( ).equals( methodName ) )
			{
				Iterator argumentListIterator = method.argumentListIterator( );
				if ( argumentListIterator.hasNext( ) )
				{
					IArgumentInfoList argumentInfoList = (IArgumentInfoList) argumentListIterator
							.next( );
					int i = 0;
					for ( Iterator iterator = argumentInfoList
							.argumentsIterator( ); iterator.hasNext( ); i++ )
					{
						Object arg = iterator.next( );
						if ( argIndex == i )
						{
							return ( (IArgumentInfo) arg ).getType( );
						}
					}

				}
				return null;
			}
		}
		return null;
	}

	/**
	 * Get a DesignElementHandle's method's all arguments.
	 * 
	 * @param handle
	 * @param methodName
	 * @return Arguments map, key is argument name, value is argument type.
	 */
	public static Map getDesignElementMethodArguments(
			DesignElementHandle handle, String methodName )
	{
		List methods = handle.getDefn( ).getMethods( );
		// if ( handle instanceof DataSetHandle )
		// {
		// methods = DesignEngine.getMetaDataDictionary( )
		// .getElement( ReportDesignConstants.DATA_SET_ELEMENT ) //$NON-NLS-1$
		// .getMethods( );
		// }
		// else if ( handle instanceof DataSourceHandle )
		// {
		// methods = DesignEngine.getMetaDataDictionary( )
		// .getElement( ReportDesignConstants.DATA_SOURCE_ELEMENT )
		// //$NON-NLS-1$
		// .getMethods( );
		// }
		// else
		// {
		// methods = handle.getDefn( ).getLocalMethods( );
		// }
		Map argMap = new LinkedHashMap( methods.size( ) * 2 );

		for ( Iterator iter = methods.iterator( ); iter.hasNext( ); )
		{
			IMethodInfo method = ( (ElementPropertyDefn) iter.next( ) )
					.getMethodInfo( );
			if ( method.getName( ).equals( methodName ) )
			{
				Iterator argumentListIterator = method.argumentListIterator( );
				if ( argumentListIterator.hasNext( ) )
				{
					IArgumentInfoList argumentInfoList = (IArgumentInfoList) argumentListIterator
							.next( );
					int i = 0;
					for ( Iterator iterator = argumentInfoList
							.argumentsIterator( ); iterator.hasNext( ); i++ )
					{
						IArgumentInfo arg = (IArgumentInfo) iterator.next( );
						argMap.put( arg.getName( ), arg.getType( ) );
					}
				}
			}
		}
		return argMap;
	}

	/**
	 * @deprecated
	 */
	public static List getDataSources( )
	{
		return SessionHandleAdapter.getInstance( ).getReportDesignHandle( )
				.getVisibleDataSources( );
	}

	/**
	 * Returns all available column bindings for the given element
	 * 
	 * @param handle
	 *            the handle of the element
	 * @return the list of all column bindings available.The list order is from
	 *         the top to the given element
	 */
	public static List getAllColumnBindingList( DesignElementHandle handle )
	{
		return getAllColumnBindingList( handle, true );
	}

	/**
	 * Returns all available column bindings for the given element.
	 * 
	 * @param handle
	 *            the handle of the element
	 * @param includeSelf
	 *            true if includes the element itself, or false only includes
	 *            bindings in the containers
	 * @return the list of all column bindings available.The list order is from
	 *         the top to the given element
	 */

	public static List getAllColumnBindingList( DesignElementHandle handle,
			boolean includeSelf )
	{
		List bindingList = new ArrayList( );
		if ( handle instanceof ReportElementHandle )
		{
			Iterator iterator = getBindingColumnIterator( handle );
			while ( iterator.hasNext( ) )
			{
				bindingList.add( iterator.next( ) );
			}
			if ( handle instanceof ListingHandle )
			{
				SlotHandle groupSlotHandle = ( (ListingHandle) handle )
						.getGroups( );
				for ( Iterator iter = groupSlotHandle.iterator( ); iter
						.hasNext( ); )
				{
					GroupHandle group = (GroupHandle) iter.next( );
					for ( Iterator columnIter = group.columnBindingsIterator( ); columnIter
							.hasNext( ); )
					{
						bindingList.add( columnIter.next( ) );
					}
				}
			}
			bindingList.addAll( 0, getAllColumnBindingList( handle
					.getContainer( ), true ) );
		}
		return bindingList;
	}

	/**
	 * Returns all visible column bindings in the holder scope for the given
	 * element
	 * 
	 * @param handle
	 *            the handle of the element
	 * @return the list of all visible column bindings.The list order is from
	 *         the top to the given element
	 */
	public static List getVisiableColumnBindingsList( DesignElementHandle handle )
	{
		return getVisiableColumnBindingsList( handle, true );
	}

	/**
	 * Returns all visible column bindings for the given element
	 * 
	 * @param handle
	 *            the handle of the element
	 * @return the list of all visible column bindings.The list order is from
	 *         the top to the given element
	 */
	public static List getVisiableColumnBindingsList(
			DesignElementHandle handle, boolean includeSelf )
	{
		List bindingList = new ArrayList( );
		if ( includeSelf )
		{
			Iterator iterator = getBindingColumnIterator( handle );
			while ( iterator.hasNext( ) )
			{
				bindingList.add( iterator.next( ) );
			}
		}
		ReportItemHandle holder = getBindingHolder( handle );
		if ( holder != null )
		{
			for ( DesignElementHandle elementHandle = handle.getContainer( ); elementHandle != holder
					.getContainer( ); elementHandle = elementHandle
					.getContainer( ) )
			{
				List subBindingList = new ArrayList( );
				Iterator iterator = getBindingColumnIterator( elementHandle );
				while ( iterator.hasNext( ) )
				{
					subBindingList.add( iterator.next( ) );
				}
				bindingList.addAll( 0, subBindingList );
			}
		}
		return bindingList;
	}

	/**
	 * Returns the element handle which can save binding columns the given
	 * element
	 * 
	 * @param handle
	 *            the handle of the element which needs binding columns
	 * @return the holder for the element,or itself if no holder available
	 */
	public static ReportItemHandle getBindingHolder( DesignElementHandle handle )
	{
		if ( handle instanceof ReportElementHandle )
		{
			if ( handle instanceof ListingHandle )
			{
				return (ReportItemHandle) handle;
			}
			if ( handle instanceof ReportItemHandle )
			{
				if ( ( (ReportItemHandle) handle ).getDataSet( ) != null
						|| ( (ReportItemHandle) handle )
								.columnBindingsIterator( ).hasNext( ) )
				{
					return (ReportItemHandle) handle;
				}
			}
			ReportItemHandle result = getBindingHolder( handle.getContainer( ) );
			if ( result == null && handle instanceof ReportItemHandle
					&& !( handle instanceof GridHandle ) )
			{
				result = (ReportItemHandle) handle;
			}
			return result;
		}
		return null;
	}

	/**
	 * Return the first DataSetHandle for a report item.
	 * 
	 * If the report item has no DataSetHandle, search for it's container if
	 * container is not a ListingHandle.
	 * 
	 * @param handle
	 *            the ReportItemHandle
	 * @return Available DataSetHandle
	 */
	public static DataSetHandle getFirstDataSet( DesignElementHandle handle )
	{
		DataSetHandle dataSetHandle = null;
		if ( handle instanceof ReportItemHandle )
		{
			dataSetHandle = ( (ReportItemHandle) handle ).getDataSet( );
		}
		if ( dataSetHandle == null )
		{
			for ( DesignElementHandle elementHandle = handle; elementHandle != null; elementHandle = elementHandle
					.getContainer( ) )
			{
				if ( elementHandle instanceof ListingHandle
						&& ( dataSetHandle = ( (ListingHandle) elementHandle )
								.getDataSet( ) ) != null )
				{
					return dataSetHandle;
				}
			}
		}
		return dataSetHandle;
	}

	/**
	 * Get a container that is a ListingHandle that can hold dataset.
	 * 
	 * @param container
	 * @return
	 */
	public static ListingHandle getListingContainer(
			DesignElementHandle container )
	{
		for ( DesignElementHandle elementHandle = container; elementHandle != null; elementHandle = elementHandle
				.getContainer( ) )
		{
			if ( elementHandle instanceof ListingHandle )
			{
				return (ListingHandle) elementHandle;
			}
		}
		return null;
	}

	/**
	 * Add a binding column on the given element
	 * 
	 * @param handle
	 *            the handle of the elementIt should be a ReportItemHandle or a
	 *            GroupHandle
	 * @param column
	 *            the column to add
	 * @param inForce
	 *            true to add the column with duplicated expression,or false not
	 *            to do
	 * 
	 * @return the handle of the binding column,or null if failed
	 */
	public static ComputedColumnHandle addColumn( DesignElementHandle handle,
			ComputedColumn column, boolean inForce ) throws SemanticException
	{
		Assert.isLegal( handle instanceof ReportItemHandle
				|| handle instanceof GroupHandle );
		if ( handle instanceof GroupHandle )
		{
			return ( (GroupHandle) handle ).addColumnBinding( column, inForce );
		}
		return ( (ReportItemHandle) handle ).addColumnBinding( column, inForce );
	}

	/**
	 * Returns the binding column iterator of the given element
	 * 
	 * @param handle
	 *            the handle of the element. It should be a ReportItemHandle or
	 *            a GroupHandle,or an empty iterator will be returned.
	 * @return the iterator of binding columns
	 */
	public static Iterator getBindingColumnIterator( DesignElementHandle handle )
	{
		if ( handle instanceof GroupHandle )
		{
			return ( (GroupHandle) handle ).columnBindingsIterator( );
		}
		else if ( handle instanceof ReportItemHandle )
		{
			return ( (ReportItemHandle) handle ).columnBindingsIterator( );
		}
		return Collections.EMPTY_LIST.iterator( );
	}

	/**
	 * Return the expression for the given binding column based on the given
	 * element
	 * 
	 * @param baseElement
	 *            the base element
	 * @param column
	 *            the binding column
	 * @return the expression for the column
	 */
	public static String getBindingexpression( DesignElementHandle baseElement,
			ComputedColumnHandle column )
	{
		String exp = IReportElementConstants.BINDING_COLUMN_PREFIX;
		for ( int i = 0; i < getBindingLevel( column.getElementHandle( ),
				baseElement ); i++ )
		{
			exp += IReportElementConstants.OUTER_BINDING_COLUMN_PREFIX;
		}
		exp += "[\"" + DEUtil.escape( column.getName( ) ) + "\"]";
		return exp;
	}

	/**
	 * Returns the level between the holder and the given handle
	 * 
	 * @param holder
	 *            the handle of the holder
	 * @param baseElement
	 *            the handle of the base element
	 * 
	 * @return the level between the holder and the base element, or -1 if the
	 *         element is not a children of the holder
	 */
	public static int getBindingLevel( DesignElementHandle holder,
			DesignElementHandle baseElement )
	{
		int level = 0;
		for ( DesignElementHandle elementHandle = baseElement; elementHandle
				.getContainer( ) != null; elementHandle = getBindingHolder(
				elementHandle ).getContainer( ), level++ )
		{
			DesignElementHandle bindingHolder = getBindingHolder( elementHandle );
			if ( bindingHolder == holder )
			{
				return level;
			}
			if ( holder instanceof GroupHandle
					&& bindingHolder == holder.getContainer( ) )
			{
				return level;
			}
		}
		return -1;
	}

	/**
	 * Check if the given element is linked from a library or not.
	 * 
	 * @param handle
	 *            the handle of the element to check
	 * @return true if it is linked from a library ,or false if else;
	 * 
	 */
	public static boolean isLinked( DesignElementHandle handle )
	{
		if ( handle.getExtends( ) != null )
		{
			return handle.getExtends( ).getRoot( ) instanceof LibraryHandle;
		}
		return false;
	}

	/**
	 * Return the group list of the given element.
	 * 
	 * @param handle
	 *            the handle of the element.
	 * @return the group list of the given element.
	 */
	public static List getGroups( DesignElementHandle handle )
	{
		List groupList = new ArrayList( );
		if ( handle instanceof ListingHandle )
		{
			SlotHandle groupSlotHandle = ( (ListingHandle) handle ).getGroups( );
			for ( Iterator iter = groupSlotHandle.iterator( ); iter.hasNext( ); )
			{
				GroupHandle group = (GroupHandle) iter.next( );
				groupList.add( group );
			}
			return groupList;
		}

		DesignElementHandle result = handle.getContainer( );
		if ( result != null )
		{
			if ( result instanceof GroupHandle )
			{
				groupList.add( (GroupHandle) result );
				return groupList;
			}
			return getGroups( result );
		}

		return groupList;
	}

	/**
	 * The group container is ListingHandler, list, table, and so on.
	 */
	public static final String TYPE_GROUP_LISTING = "listing";

	/**
	 * The group container is GroupHandler
	 */
	public static final String TYPE_GROUP_GROUP = "group";

	/**
	 * other container, has none group.
	 */
	public static final String TYPE_GROUP_NONE = "none";

	/**
	 * Return the group container type of the given element handle.
	 * 
	 * @param handle
	 *            the handle of the element.
	 * @return the group container type of the given element.
	 */
	public static String getGroupControlType( DesignElementHandle handle )
	{
		if ( handle instanceof ListingHandle )
		{
			return TYPE_GROUP_LISTING;
		}

		DesignElementHandle result = handle.getContainer( );
		if ( result != null )
		{
			if ( result instanceof GroupHandle )
			{
				return TYPE_GROUP_GROUP;
			}
			return getGroupControlType( result );
		}
		return TYPE_GROUP_NONE;
	}
	
	/**
	 * Get the string enclosed with quote.
	 * 
	 * @param string
	 * @return
	 */
	public static String AddQuote( String string )
	{
		if ( string != null
				&& ( !( string.startsWith( "\"" ) && string.endsWith( "\"" ) ) ) ) //$NON-NLS-1$//$NON-NLS-2$
		{
			return "\"" + string + "\""; //$NON-NLS-1$//$NON-NLS-2$
		}
		return string;
	}

	/**
	 * Remove the quote if the string enclosed width quote .
	 * 
	 * @param string
	 * @return
	 */
	public static String RemoveQuote( String string )
	{
		if ( string != null
				&& string.length( ) >= 2
				&& string.startsWith( "\"" ) //$NON-NLS-1$
				&& string.endsWith( "\"" ) ) //$NON-NLS-1$
		{
			return string.substring( 1, string.length( ) - 1 );
		}
		return string;
	}
}