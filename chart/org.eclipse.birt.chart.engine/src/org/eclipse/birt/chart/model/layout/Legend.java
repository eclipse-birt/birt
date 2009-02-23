/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.model.layout;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Direction;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.Text;

import org.eclipse.birt.chart.model.component.Label;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Legend</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * 				This type defines a legend in the chart, including the legend text, size and position.  
 * 				The legend items usually describe either the series or categories, depeding on the chart type.
 * 			
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.birt.chart.model.layout.Legend#getHorizontalSpacing <em>Horizontal Spacing</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.layout.Legend#getVerticalSpacing <em>Vertical Spacing</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.layout.Legend#getClientArea <em>Client Area</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.layout.Legend#getText <em>Text</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.layout.Legend#getOrientation <em>Orientation</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.layout.Legend#getDirection <em>Direction</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.layout.Legend#getSeparator <em>Separator</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.layout.Legend#getPosition <em>Position</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.layout.Legend#getItemType <em>Item Type</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.layout.Legend#getTitle <em>Title</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.layout.Legend#getTitlePosition <em>Title Position</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.layout.Legend#isShowValue <em>Show Value</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.layout.Legend#isShowPercent <em>Show Percent</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.layout.Legend#isShowTotal <em>Show Total</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.layout.Legend#getWrappingSize <em>Wrapping Size</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.layout.Legend#getMaxPercent <em>Max Percent</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.layout.Legend#getTitlePercent <em>Title Percent</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getLegend()
 * @model extendedMetaData="name='Legend' kind='elementOnly'"
 * @generated
 */
public interface Legend extends Block
{

	/**
	 * Returns the value of the '<em><b>Horizontal Spacing</b></em>' attribute. <!-- begin-user-doc --> Gets the
	 * horizontal spacing between entries in the legend. <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * The horizontal spacing between elements in the legend.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Horizontal Spacing</em>' attribute.
	 * @see #isSetHorizontalSpacing()
	 * @see #unsetHorizontalSpacing()
	 * @see #setHorizontalSpacing(int)
	 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getLegend_HorizontalSpacing()
	 * @model unique="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Int" required="true"
	 * @generated
	 */
	int getHorizontalSpacing( );

	/**
	 * Sets the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#getHorizontalSpacing <em>Horizontal Spacing</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Horizontal Spacing</em>' attribute.
	 * @see #isSetHorizontalSpacing()
	 * @see #unsetHorizontalSpacing()
	 * @see #getHorizontalSpacing()
	 * @generated
	 */
	void setHorizontalSpacing( int value );

	/**
	 * Unsets the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#getHorizontalSpacing <em>Horizontal Spacing</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #isSetHorizontalSpacing()
	 * @see #getHorizontalSpacing()
	 * @see #setHorizontalSpacing(int)
	 * @generated
	 */
	void unsetHorizontalSpacing( );

	/**
	 * Returns whether the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#getHorizontalSpacing <em>Horizontal Spacing</em>}' attribute is set.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return whether the value of the '<em>Horizontal Spacing</em>' attribute is set.
	 * @see #unsetHorizontalSpacing()
	 * @see #getHorizontalSpacing()
	 * @see #setHorizontalSpacing(int)
	 * @generated
	 */
	boolean isSetHorizontalSpacing( );

	/**
	 * Returns the value of the '<em><b>Vertical Spacing</b></em>' attribute. <!-- begin-user-doc --> Gets the
	 * vertical spacing between entries in the legend. <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * The vertical spacing between elements in the legend.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Vertical Spacing</em>' attribute.
	 * @see #isSetVerticalSpacing()
	 * @see #unsetVerticalSpacing()
	 * @see #setVerticalSpacing(int)
	 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getLegend_VerticalSpacing()
	 * @model unique="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Int" required="true"
	 * @generated
	 */
	int getVerticalSpacing( );

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.birt.chart.model.layout.Legend#getVerticalSpacing <em>Vertical Spacing</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Vertical Spacing</em>' attribute.
	 * @see #isSetVerticalSpacing()
	 * @see #unsetVerticalSpacing()
	 * @see #getVerticalSpacing()
	 * @generated
	 */
	void setVerticalSpacing( int value );

	/**
	 * Unsets the value of the '
	 * {@link org.eclipse.birt.chart.model.layout.Legend#getVerticalSpacing <em>Vertical Spacing</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetVerticalSpacing()
	 * @see #getVerticalSpacing()
	 * @see #setVerticalSpacing(int)
	 * @generated
	 */
	void unsetVerticalSpacing( );

	/**
	 * Returns whether the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#getVerticalSpacing <em>Vertical Spacing</em>}' attribute is set.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return whether the value of the '<em>Vertical Spacing</em>' attribute is set.
	 * @see #unsetVerticalSpacing()
	 * @see #getVerticalSpacing()
	 * @see #setVerticalSpacing(int)
	 * @generated
	 */
	boolean isSetVerticalSpacing( );

	/**
	 * Returns the value of the '<em><b>Client Area</b></em>' containment reference. <!-- begin-user-doc --> Gets
	 * the client area of the legend where the legend entries will be displayed. <!-- end-user-doc --> <!--
	 * begin-model-doc -->
	 * 
	 * The area of the legend block within which the legend items are displayed.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Client Area</em>' containment reference.
	 * @see #setClientArea(ClientArea)
	 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getLegend_ClientArea()
	 * @model containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	ClientArea getClientArea( );

	/**
	 * Sets the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#getClientArea <em>Client Area</em>}' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Client Area</em>' containment reference.
	 * @see #getClientArea()
	 * @generated
	 */
	void setClientArea( ClientArea value );

	/**
	 * Returns the value of the '<em><b>Text</b></em>' containment reference. <!-- begin-user-doc --> Gets the text
	 * instance that holds the formatting information for entries in the legend. <!-- end-user-doc --> <!--
	 * begin-model-doc -->
	 * 
	 * The actual text content of the block.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Text</em>' containment reference.
	 * @see #setText(Text)
	 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getLegend_Text()
	 * @model containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	Text getText( );

	/**
	 * Sets the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#getText <em>Text</em>}' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Text</em>' containment reference.
	 * @see #getText()
	 * @generated
	 */
	void setText( Text value );

	/**
	 * Returns the value of the '<em><b>Orientation</b></em>' attribute. The default value is
	 * <code>"Horizontal"</code>. The literals are from the enumeration
	 * {@link org.eclipse.birt.chart.model.attribute.Orientation}. <!-- begin-user-doc --> Gets the orientation to be
	 * used for entries in the legend. This determines the way the entries are arranged in the legend. <!-- end-user-doc
	 * --> <!-- begin-model-doc -->
	 * 
	 * The orientation of elements in the legend.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Orientation</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.Orientation
	 * @see #isSetOrientation()
	 * @see #unsetOrientation()
	 * @see #setOrientation(Orientation)
	 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getLegend_Orientation()
	 * @model default="Horizontal" unique="false" unsettable="true" required="true"
	 * @generated
	 */
	Orientation getOrientation( );

	/**
	 * Sets the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#getOrientation <em>Orientation</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Orientation</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.Orientation
	 * @see #isSetOrientation()
	 * @see #unsetOrientation()
	 * @see #getOrientation()
	 * @generated
	 */
	void setOrientation( Orientation value );

	/**
	 * Unsets the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#getOrientation <em>Orientation</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #isSetOrientation()
	 * @see #getOrientation()
	 * @see #setOrientation(Orientation)
	 * @generated
	 */
	void unsetOrientation( );

	/**
	 * Returns whether the value of the '
	 * {@link org.eclipse.birt.chart.model.layout.Legend#getOrientation <em>Orientation</em>}' attribute is set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Orientation</em>' attribute is set.
	 * @see #unsetOrientation()
	 * @see #getOrientation()
	 * @see #setOrientation(Orientation)
	 * @generated
	 */
	boolean isSetOrientation( );

	/**
	 * Returns the value of the '<em><b>Direction</b></em>' attribute. The default value is
	 * <code>"Left_Right"</code>. The literals are from the enumeration
	 * {@link org.eclipse.birt.chart.model.attribute.Direction}. <!-- begin-user-doc --> Gets the direction to be used
	 * for entries in the legend. This determines the flow of the entries in the legend. <!-- end-user-doc --> <!--
	 * begin-model-doc -->
	 * 
	 * The direction in which the entries are added to the legend.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Direction</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.Direction
	 * @see #isSetDirection()
	 * @see #unsetDirection()
	 * @see #setDirection(Direction)
	 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getLegend_Direction()
	 * @model default="Left_Right" unique="false" unsettable="true" required="true"
	 * @generated
	 */
	Direction getDirection( );

	/**
	 * Sets the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#getDirection <em>Direction</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Direction</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.Direction
	 * @see #isSetDirection()
	 * @see #unsetDirection()
	 * @see #getDirection()
	 * @generated
	 */
	void setDirection( Direction value );

	/**
	 * Unsets the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#getDirection <em>Direction</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #isSetDirection()
	 * @see #getDirection()
	 * @see #setDirection(Direction)
	 * @generated
	 */
	void unsetDirection( );

	/**
	 * Returns whether the value of the '
	 * {@link org.eclipse.birt.chart.model.layout.Legend#getDirection <em>Direction</em>}' attribute is set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Direction</em>' attribute is set.
	 * @see #unsetDirection()
	 * @see #getDirection()
	 * @see #setDirection(Direction)
	 * @generated
	 */
	boolean isSetDirection( );

	/**
	 * Returns the value of the '<em><b>Separator</b></em>' containment reference. <!-- begin-user-doc --> Gets the
	 * separator attributes. The separator is used in the legend to separate entries that are produced by different
	 * series keys. <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * The line attributes to be used for the line separating groups of entries in the legend.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Separator</em>' containment reference.
	 * @see #setSeparator(LineAttributes)
	 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getLegend_Separator()
	 * @model containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	LineAttributes getSeparator( );

	/**
	 * Sets the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#getSeparator <em>Separator</em>}' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Separator</em>' containment reference.
	 * @see #getSeparator()
	 * @generated
	 */
	void setSeparator( LineAttributes value );

	/**
	 * Returns the value of the '<em><b>Position</b></em>' attribute. The default value is <code>"Above"</code>.
	 * The literals are from the enumeration {@link org.eclipse.birt.chart.model.attribute.Position}. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * The position of the legend.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Position</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.Position
	 * @see #isSetPosition()
	 * @see #unsetPosition()
	 * @see #setPosition(Position)
	 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getLegend_Position()
	 * @model default="Above" unique="false" unsettable="true" required="true"
	 * @generated
	 */
	Position getPosition( );

	/**
	 * Sets the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#getPosition <em>Position</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Position</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.Position
	 * @see #isSetPosition()
	 * @see #unsetPosition()
	 * @see #getPosition()
	 * @generated
	 */
	void setPosition( Position value );

	/**
	 * Unsets the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#getPosition <em>Position</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #isSetPosition()
	 * @see #getPosition()
	 * @see #setPosition(Position)
	 * @generated
	 */
	void unsetPosition( );

	/**
	 * Returns whether the value of the '
	 * {@link org.eclipse.birt.chart.model.layout.Legend#getPosition <em>Position</em>}' attribute is set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Position</em>' attribute is set.
	 * @see #unsetPosition()
	 * @see #getPosition()
	 * @see #setPosition(Position)
	 * @generated
	 */
	boolean isSetPosition( );

	/**
	 * Returns the value of the '<em><b>Item Type</b></em>' attribute. The default value is <code>"Series"</code>.
	 * The literals are from the enumeration {@link org.eclipse.birt.chart.model.attribute.LegendItemType}. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * The form rendering of series should take (by Series or by Categories).
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Item Type</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.LegendItemType
	 * @see #isSetItemType()
	 * @see #unsetItemType()
	 * @see #setItemType(LegendItemType)
	 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getLegend_ItemType()
	 * @model default="Series" unique="false" unsettable="true" required="true"
	 * @generated
	 */
	LegendItemType getItemType( );

	/**
	 * Sets the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#getItemType <em>Item Type</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Item Type</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.LegendItemType
	 * @see #isSetItemType()
	 * @see #unsetItemType()
	 * @see #getItemType()
	 * @generated
	 */
	void setItemType( LegendItemType value );

	/**
	 * Unsets the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#getItemType <em>Item Type</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #isSetItemType()
	 * @see #getItemType()
	 * @see #setItemType(LegendItemType)
	 * @generated
	 */
	void unsetItemType( );

	/**
	 * Returns whether the value of the '
	 * {@link org.eclipse.birt.chart.model.layout.Legend#getItemType <em>Item Type</em>}' attribute is set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Item Type</em>' attribute is set.
	 * @see #unsetItemType()
	 * @see #getItemType()
	 * @see #setItemType(LegendItemType)
	 * @generated
	 */
	boolean isSetItemType( );

	/**
	 * Returns the value of the '<em><b>Title</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 								A label instance to hold attributes for
	 * 								legend title.
	 * 							
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Title</em>' containment reference.
	 * @see #setTitle(Label)
	 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getLegend_Title()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='Title'"
	 * @generated
	 */
	Label getTitle( );

	/**
	 * Sets the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#getTitle <em>Title</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Title</em>' containment reference.
	 * @see #getTitle()
	 * @generated
	 */
	void setTitle( Label value );

	/**
	 * Returns the value of the '<em><b>Title Position</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.birt.chart.model.attribute.Position}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 								Specifies where the title for the legend
	 * 								should be displayed.
	 * 							
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Title Position</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.Position
	 * @see #isSetTitlePosition()
	 * @see #unsetTitlePosition()
	 * @see #setTitlePosition(Position)
	 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getLegend_TitlePosition()
	 * @model unsettable="true"
	 *        extendedMetaData="kind='element' name='TitlePosition'"
	 * @generated
	 */
	Position getTitlePosition( );

	/**
	 * Sets the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#getTitlePosition <em>Title Position</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Title Position</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.Position
	 * @see #isSetTitlePosition()
	 * @see #unsetTitlePosition()
	 * @see #getTitlePosition()
	 * @generated
	 */
	void setTitlePosition( Position value );

	/**
	 * Unsets the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#getTitlePosition <em>Title Position</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetTitlePosition()
	 * @see #getTitlePosition()
	 * @see #setTitlePosition(Position)
	 * @generated
	 */
	void unsetTitlePosition( );

	/**
	 * Returns whether the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#getTitlePosition <em>Title Position</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Title Position</em>' attribute is set.
	 * @see #unsetTitlePosition()
	 * @see #getTitlePosition()
	 * @see #setTitlePosition(Position)
	 * @generated
	 */
	boolean isSetTitlePosition( );

	/**
	 * Returns the value of the '<em><b>Show Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 								Specifies if show legend item value.
	 * 							
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Show Value</em>' attribute.
	 * @see #isSetShowValue()
	 * @see #unsetShowValue()
	 * @see #setShowValue(boolean)
	 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getLegend_ShowValue()
	 * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        extendedMetaData="kind='element' name='ShowValue'"
	 * @generated
	 */
	boolean isShowValue( );

	/**
	 * Sets the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#isShowValue <em>Show Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Show Value</em>' attribute.
	 * @see #isSetShowValue()
	 * @see #unsetShowValue()
	 * @see #isShowValue()
	 * @generated
	 */
	void setShowValue( boolean value );

	/**
	 * Unsets the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#isShowValue <em>Show Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetShowValue()
	 * @see #isShowValue()
	 * @see #setShowValue(boolean)
	 * @generated
	 */
	void unsetShowValue( );

	/**
	 * Returns whether the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#isShowValue <em>Show Value</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Show Value</em>' attribute is set.
	 * @see #unsetShowValue()
	 * @see #isShowValue()
	 * @see #setShowValue(boolean)
	 * @generated
	 */
	boolean isSetShowValue( );

	/**
	 * Returns the value of the '<em><b>Show Percent</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 								Specifies if show legend item value as
	 * 								percentile.
	 * 							
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Show Percent</em>' attribute.
	 * @see #isSetShowPercent()
	 * @see #unsetShowPercent()
	 * @see #setShowPercent(boolean)
	 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getLegend_ShowPercent()
	 * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        extendedMetaData="kind='element' name='ShowPercent'"
	 * @generated
	 */
	boolean isShowPercent( );

	/**
	 * Sets the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#isShowPercent <em>Show Percent</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Show Percent</em>' attribute.
	 * @see #isSetShowPercent()
	 * @see #unsetShowPercent()
	 * @see #isShowPercent()
	 * @generated
	 */
	void setShowPercent( boolean value );

	/**
	 * Unsets the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#isShowPercent <em>Show Percent</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetShowPercent()
	 * @see #isShowPercent()
	 * @see #setShowPercent(boolean)
	 * @generated
	 */
	void unsetShowPercent( );

	/**
	 * Returns whether the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#isShowPercent <em>Show Percent</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Show Percent</em>' attribute is set.
	 * @see #unsetShowPercent()
	 * @see #isShowPercent()
	 * @see #setShowPercent(boolean)
	 * @generated
	 */
	boolean isSetShowPercent( );

	/**
	 * Returns the value of the '<em><b>Show Total</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 								Specifies if show legend item value
	 * 								total.
	 * 							
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Show Total</em>' attribute.
	 * @see #isSetShowTotal()
	 * @see #unsetShowTotal()
	 * @see #setShowTotal(boolean)
	 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getLegend_ShowTotal()
	 * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        extendedMetaData="kind='element' name='ShowTotal'"
	 * @generated
	 */
	boolean isShowTotal( );

	/**
	 * Sets the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#isShowTotal <em>Show Total</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Show Total</em>' attribute.
	 * @see #isSetShowTotal()
	 * @see #unsetShowTotal()
	 * @see #isShowTotal()
	 * @generated
	 */
	void setShowTotal( boolean value );

	/**
	 * Unsets the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#isShowTotal <em>Show Total</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetShowTotal()
	 * @see #isShowTotal()
	 * @see #setShowTotal(boolean)
	 * @generated
	 */
	void unsetShowTotal( );

	/**
	 * Returns whether the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#isShowTotal <em>Show Total</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Show Total</em>' attribute is set.
	 * @see #unsetShowTotal()
	 * @see #isShowTotal()
	 * @see #setShowTotal(boolean)
	 * @generated
	 */
	boolean isSetShowTotal( );

	/**
	 * Returns the value of the '<em><b>Wrapping Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 								Specifies the maximum string size for
	 * 								each legend item. If the string size is
	 * 								greater than this, it will be wrapped.
	 * 								Zero means no wrap.
	 * 							
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Wrapping Size</em>' attribute.
	 * @see #isSetWrappingSize()
	 * @see #unsetWrappingSize()
	 * @see #setWrappingSize(double)
	 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getLegend_WrappingSize()
	 * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Double"
	 *        extendedMetaData="kind='element' name='WrappingSize'"
	 * @generated
	 */
	double getWrappingSize( );

	/**
	 * Sets the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#getWrappingSize <em>Wrapping Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Wrapping Size</em>' attribute.
	 * @see #isSetWrappingSize()
	 * @see #unsetWrappingSize()
	 * @see #getWrappingSize()
	 * @generated
	 */
	void setWrappingSize( double value );

	/**
	 * Unsets the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#getWrappingSize <em>Wrapping Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetWrappingSize()
	 * @see #getWrappingSize()
	 * @see #setWrappingSize(double)
	 * @generated
	 */
	void unsetWrappingSize( );

	/**
	 * Returns whether the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#getWrappingSize <em>Wrapping Size</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Wrapping Size</em>' attribute is set.
	 * @see #unsetWrappingSize()
	 * @see #getWrappingSize()
	 * @see #setWrappingSize(double)
	 * @generated
	 */
	boolean isSetWrappingSize( );

	/**
	 * Returns the value of the '<em><b>Max Percent</b></em>' attribute.
	 * The default value is <code>"0.33333333"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 								The maximal percent of space which the
	 * 								legend can take from the chart block.
	 * 							
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Max Percent</em>' attribute.
	 * @see #isSetMaxPercent()
	 * @see #unsetMaxPercent()
	 * @see #setMaxPercent(double)
	 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getLegend_MaxPercent()
	 * @model default="0.33333333" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Double"
	 *        extendedMetaData="kind='element' name='MaxPercent'"
	 * @generated
	 */
	double getMaxPercent( );

	/**
	 * Sets the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#getMaxPercent <em>Max Percent</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Percent</em>' attribute.
	 * @see #isSetMaxPercent()
	 * @see #unsetMaxPercent()
	 * @see #getMaxPercent()
	 * @generated
	 */
	void setMaxPercent( double value );

	/**
	 * Unsets the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#getMaxPercent <em>Max Percent</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetMaxPercent()
	 * @see #getMaxPercent()
	 * @see #setMaxPercent(double)
	 * @generated
	 */
	void unsetMaxPercent( );

	/**
	 * Returns whether the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#getMaxPercent <em>Max Percent</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Max Percent</em>' attribute is set.
	 * @see #unsetMaxPercent()
	 * @see #getMaxPercent()
	 * @see #setMaxPercent(double)
	 * @generated
	 */
	boolean isSetMaxPercent( );

	/**
	 * Returns the value of the '<em><b>Title Percent</b></em>' attribute.
	 * The default value is <code>"0.6"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 								The maximal percent of space which the
	 * 								legend title can take from the legend
	 * 								block. By default, it's 0.6.
	 * 							
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Title Percent</em>' attribute.
	 * @see #isSetTitlePercent()
	 * @see #unsetTitlePercent()
	 * @see #setTitlePercent(double)
	 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getLegend_TitlePercent()
	 * @model default="0.6" unsettable="true" dataType="org.eclipse.birt.chart.model.layout.TitlePercentType" required="true"
	 *        extendedMetaData="kind='element' name='TitlePercent'"
	 * @generated
	 */
	double getTitlePercent( );

	/**
	 * Sets the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#getTitlePercent <em>Title Percent</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Title Percent</em>' attribute.
	 * @see #isSetTitlePercent()
	 * @see #unsetTitlePercent()
	 * @see #getTitlePercent()
	 * @generated
	 */
	void setTitlePercent( double value );

	/**
	 * Unsets the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#getTitlePercent <em>Title Percent</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetTitlePercent()
	 * @see #getTitlePercent()
	 * @see #setTitlePercent(double)
	 * @generated
	 */
	void unsetTitlePercent( );

	/**
	 * Returns whether the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#getTitlePercent <em>Title Percent</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Title Percent</em>' attribute is set.
	 * @see #unsetTitlePercent()
	 * @see #getTitlePercent()
	 * @see #setTitlePercent(double)
	 * @generated
	 */
	boolean isSetTitlePercent( );

	/**
	 * Update the legend's block relationship in the hierarchy w.r.t. the chart model
	 * 
	 * @param cm
	 */
	void updateLayout( Chart cm );
} // Legend
