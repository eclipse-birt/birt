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

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Legend</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * 			The Legend in a chart.
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
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.layout.LayoutPackage#getLegend()
 * @model 
 * @generated
 */
public interface Legend extends Block{

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
    int getHorizontalSpacing();

    /**
     * Sets the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#getHorizontalSpacing <em>Horizontal Spacing</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Horizontal Spacing</em>' attribute.
     * @see #isSetHorizontalSpacing()
     * @see #unsetHorizontalSpacing()
     * @see #getHorizontalSpacing()
     * @generated
     */
    void setHorizontalSpacing(int value);

    /**
     * Unsets the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#getHorizontalSpacing <em>Horizontal Spacing</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #isSetHorizontalSpacing()
     * @see #getHorizontalSpacing()
     * @see #setHorizontalSpacing(int)
     * @generated
     */
    void unsetHorizontalSpacing();

    /**
     * Returns whether the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#getHorizontalSpacing <em>Horizontal Spacing</em>}' attribute is set.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return whether the value of the '<em>Horizontal Spacing</em>' attribute is set.
     * @see #unsetHorizontalSpacing()
     * @see #getHorizontalSpacing()
     * @see #setHorizontalSpacing(int)
     * @generated
     */
    boolean isSetHorizontalSpacing();

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
    int getVerticalSpacing();

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
    void setVerticalSpacing(int value);

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
    void unsetVerticalSpacing();

    /**
     * Returns whether the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#getVerticalSpacing <em>Vertical Spacing</em>}' attribute is set.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @return whether the value of the '<em>Vertical Spacing</em>' attribute is set.
     * @see #unsetVerticalSpacing()
     * @see #getVerticalSpacing()
     * @see #setVerticalSpacing(int)
     * @generated
     */
    boolean isSetVerticalSpacing();

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
    ClientArea getClientArea();

    /**
     * Sets the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#getClientArea <em>Client Area</em>}' containment reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Client Area</em>' containment reference.
     * @see #getClientArea()
     * @generated
     */
    void setClientArea(ClientArea value);

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
    Text getText();

    /**
     * Sets the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#getText <em>Text</em>}' containment reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Text</em>' containment reference.
     * @see #getText()
     * @generated
     */
    void setText(Text value);

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
    Orientation getOrientation();

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
    void setOrientation(Orientation value);

    /**
     * Unsets the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#getOrientation <em>Orientation</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #isSetOrientation()
     * @see #getOrientation()
     * @see #setOrientation(Orientation)
     * @generated
     */
    void unsetOrientation();

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
    boolean isSetOrientation();

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
    Direction getDirection();

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
    void setDirection(Direction value);

    /**
     * Unsets the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#getDirection <em>Direction</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #isSetDirection()
     * @see #getDirection()
     * @see #setDirection(Direction)
     * @generated
     */
    void unsetDirection();

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
    boolean isSetDirection();

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
    LineAttributes getSeparator();

    /**
     * Sets the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#getSeparator <em>Separator</em>}' containment reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Separator</em>' containment reference.
     * @see #getSeparator()
     * @generated
     */
    void setSeparator(LineAttributes value);

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
    Position getPosition();

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
    void setPosition(Position value);

    /**
     * Unsets the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#getPosition <em>Position</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #isSetPosition()
     * @see #getPosition()
     * @see #setPosition(Position)
     * @generated
     */
    void unsetPosition();

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
    boolean isSetPosition();

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
    LegendItemType getItemType();

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
    void setItemType(LegendItemType value);

    /**
     * Unsets the value of the '{@link org.eclipse.birt.chart.model.layout.Legend#getItemType <em>Item Type</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #isSetItemType()
     * @see #getItemType()
     * @see #setItemType(LegendItemType)
     * @generated
     */
    void unsetItemType();

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
    boolean isSetItemType();

    /**
     * Update the legend's block relationship in the hierarchy w.r.t. the chart model
     * 
     * @param cm
     */
    void updateLayout(Chart cm);
} // Legend
