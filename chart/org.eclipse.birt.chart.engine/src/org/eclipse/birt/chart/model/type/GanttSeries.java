/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/
/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */

package org.eclipse.birt.chart.model.type;

import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Series;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Gantt
 * Series</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc --> This is a Series type that holds data for Gantt
 * Charts. <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.type.GanttSeries#getStartMarker
 * <em>Start Marker</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.GanttSeries#getStartMarkerPosition
 * <em>Start Marker Position</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.GanttSeries#getEndMarker <em>End
 * Marker</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.GanttSeries#getEndMarkerPosition
 * <em>End Marker Position</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.GanttSeries#getConnectionLine
 * <em>Connection Line</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.GanttSeries#getOutline
 * <em>Outline</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.GanttSeries#getOutlineFill
 * <em>Outline Fill</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.GanttSeries#isUseDecorationLabelValue
 * <em>Use Decoration Label Value</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.GanttSeries#getDecorationLabel
 * <em>Decoration Label</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.GanttSeries#getDecorationLabelPosition
 * <em>Decoration Label Position</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.GanttSeries#isPaletteLineColor
 * <em>Palette Line Color</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.type.TypePackage#getGanttSeries()
 * @model extendedMetaData="name='GanttSeries' kind='elementOnly'"
 * @generated
 */
public interface GanttSeries extends Series {

	/**
	 * Returns the value of the '<em><b>Start Marker</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc
	 * -->
	 * 
	 * Specifies the marker to be used for displaying the start data point on the
	 * line in the chart.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Start Marker</em>' containment reference.
	 * @see #setStartMarker(Marker)
	 * @see org.eclipse.birt.chart.model.type.TypePackage#getGanttSeries_StartMarker()
	 * @model containment="true" required="true" extendedMetaData="kind='element'
	 *        name='StartMarker'"
	 * @generated
	 */
	Marker getStartMarker();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.type.GanttSeries#getStartMarker
	 * <em>Start Marker</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Start Marker</em>' containment
	 *              reference.
	 * @see #getStartMarker()
	 * @generated
	 */
	void setStartMarker(Marker value);

	/**
	 * Returns the value of the '<em><b>Start Marker Position</b></em>' attribute.
	 * The default value is <code>"Above"</code>. The literals are from the
	 * enumeration {@link org.eclipse.birt.chart.model.attribute.Position}. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> Specifies
	 * the position of the start marker relative to the connection line start point.
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Start Marker Position</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.Position
	 * @see #isSetStartMarkerPosition()
	 * @see #unsetStartMarkerPosition()
	 * @see #setStartMarkerPosition(Position)
	 * @see org.eclipse.birt.chart.model.type.TypePackage#getGanttSeries_StartMarkerPosition()
	 * @model default="Above" unsettable="true" required="true"
	 *        extendedMetaData="kind='element' name='StartMarkerPosition'"
	 * @generated
	 */
	Position getStartMarkerPosition();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.type.GanttSeries#getStartMarkerPosition
	 * <em>Start Marker Position</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Start Marker Position</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.Position
	 * @see #isSetStartMarkerPosition()
	 * @see #unsetStartMarkerPosition()
	 * @see #getStartMarkerPosition()
	 * @generated
	 */
	void setStartMarkerPosition(Position value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.type.GanttSeries#getStartMarkerPosition
	 * <em>Start Marker Position</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isSetStartMarkerPosition()
	 * @see #getStartMarkerPosition()
	 * @see #setStartMarkerPosition(Position)
	 * @generated
	 */
	void unsetStartMarkerPosition();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.type.GanttSeries#getStartMarkerPosition
	 * <em>Start Marker Position</em>}' attribute is set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Start Marker Position</em>' attribute
	 *         is set.
	 * @see #unsetStartMarkerPosition()
	 * @see #getStartMarkerPosition()
	 * @see #setStartMarkerPosition(Position)
	 * @generated
	 */
	boolean isSetStartMarkerPosition();

	/**
	 * Returns the value of the '<em><b>End Marker</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Specifies the marker to be used for displaying the end data point on the line
	 * in the chart. <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>End Marker</em>' containment reference.
	 * @see #setEndMarker(Marker)
	 * @see org.eclipse.birt.chart.model.type.TypePackage#getGanttSeries_EndMarker()
	 * @model containment="true" required="true" extendedMetaData="kind='element'
	 *        name='EndMarker'"
	 * @generated
	 */
	Marker getEndMarker();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.type.GanttSeries#getEndMarker <em>End
	 * Marker</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>End Marker</em>' containment
	 *              reference.
	 * @see #getEndMarker()
	 * @generated
	 */
	void setEndMarker(Marker value);

	/**
	 * Returns the value of the '<em><b>End Marker Position</b></em>' attribute. The
	 * default value is <code>"Above"</code>. The literals are from the enumeration
	 * {@link org.eclipse.birt.chart.model.attribute.Position}. <!-- begin-user-doc
	 * --> <!-- end-user-doc --> <!-- begin-model-doc --> Specifies the position of
	 * the end marker relative to the connection line end point. <!-- end-model-doc
	 * -->
	 * 
	 * @return the value of the '<em>End Marker Position</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.Position
	 * @see #isSetEndMarkerPosition()
	 * @see #unsetEndMarkerPosition()
	 * @see #setEndMarkerPosition(Position)
	 * @see org.eclipse.birt.chart.model.type.TypePackage#getGanttSeries_EndMarkerPosition()
	 * @model default="Above" unsettable="true" required="true"
	 *        extendedMetaData="kind='element' name='EndMarkerPosition'"
	 * @generated
	 */
	Position getEndMarkerPosition();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.type.GanttSeries#getEndMarkerPosition
	 * <em>End Marker Position</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>End Marker Position</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.Position
	 * @see #isSetEndMarkerPosition()
	 * @see #unsetEndMarkerPosition()
	 * @see #getEndMarkerPosition()
	 * @generated
	 */
	void setEndMarkerPosition(Position value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.type.GanttSeries#getEndMarkerPosition
	 * <em>End Marker Position</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isSetEndMarkerPosition()
	 * @see #getEndMarkerPosition()
	 * @see #setEndMarkerPosition(Position)
	 * @generated
	 */
	void unsetEndMarkerPosition();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.type.GanttSeries#getEndMarkerPosition
	 * <em>End Marker Position</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>End Marker Position</em>' attribute is
	 *         set.
	 * @see #unsetEndMarkerPosition()
	 * @see #getEndMarkerPosition()
	 * @see #setEndMarkerPosition(Position)
	 * @generated
	 */
	boolean isSetEndMarkerPosition();

	/**
	 * Returns the value of the '<em><b>Connection Line</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc
	 * -->
	 * 
	 * Specifies the attributes for the line used to represent this series. <!--
	 * end-model-doc -->
	 * 
	 * @return the value of the '<em>Connection Line</em>' containment reference.
	 * @see #setConnectionLine(LineAttributes)
	 * @see org.eclipse.birt.chart.model.type.TypePackage#getGanttSeries_ConnectionLine()
	 * @model containment="true" required="true" extendedMetaData="kind='element'
	 *        name='ConnectionLine'"
	 * @generated
	 */
	LineAttributes getConnectionLine();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.type.GanttSeries#getConnectionLine
	 * <em>Connection Line</em>}' containment reference. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Connection Line</em>' containment
	 *              reference.
	 * @see #getConnectionLine()
	 * @generated
	 */
	void setConnectionLine(LineAttributes value);

	/**
	 * Returns the value of the '<em><b>Outline</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Specifies the attributes for the outline used to represent this series. <!--
	 * end-model-doc -->
	 * 
	 * @return the value of the '<em>Outline</em>' containment reference.
	 * @see #setOutline(LineAttributes)
	 * @see org.eclipse.birt.chart.model.type.TypePackage#getGanttSeries_Outline()
	 * @model containment="true" required="true" extendedMetaData="kind='element'
	 *        name='Outline'"
	 * @generated
	 */
	LineAttributes getOutline();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.type.GanttSeries#getOutline
	 * <em>Outline</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Outline</em>' containment reference.
	 * @see #getOutline()
	 * @generated
	 */
	void setOutline(LineAttributes value);

	/**
	 * Returns the value of the '<em><b>Outline Fill</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc
	 * --> Defines the extra fill to be used with outline.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Outline Fill</em>' containment reference.
	 * @see #setOutlineFill(Fill)
	 * @see org.eclipse.birt.chart.model.type.TypePackage#getGanttSeries_OutlineFill()
	 * @model containment="true" extendedMetaData="kind='element'
	 *        name='OutlineFill'"
	 * @generated
	 */
	Fill getOutlineFill();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.type.GanttSeries#getOutlineFill
	 * <em>Outline Fill</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Outline Fill</em>' containment
	 *              reference.
	 * @see #getOutlineFill()
	 * @generated
	 */
	void setOutlineFill(Fill value);

	/**
	 * Returns the value of the '<em><b>Use Decoration Label Value</b></em>'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc
	 * --> Specifies if use decoration value as the datapoint label value <!--
	 * end-model-doc -->
	 * 
	 * @return the value of the '<em>Use Decoration Label Value</em>' attribute.
	 * @see #isSetUseDecorationLabelValue()
	 * @see #unsetUseDecorationLabelValue()
	 * @see #setUseDecorationLabelValue(boolean)
	 * @see org.eclipse.birt.chart.model.type.TypePackage#getGanttSeries_UseDecorationLabelValue()
	 * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        required="true" extendedMetaData="kind='element'
	 *        name='UseDecorationLabelValue'"
	 * @generated
	 */
	boolean isUseDecorationLabelValue();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.type.GanttSeries#isUseDecorationLabelValue
	 * <em>Use Decoration Label Value</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Use Decoration Label Value</em>'
	 *              attribute.
	 * @see #isSetUseDecorationLabelValue()
	 * @see #unsetUseDecorationLabelValue()
	 * @see #isUseDecorationLabelValue()
	 * @generated
	 */
	void setUseDecorationLabelValue(boolean value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.type.GanttSeries#isUseDecorationLabelValue
	 * <em>Use Decoration Label Value</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isSetUseDecorationLabelValue()
	 * @see #isUseDecorationLabelValue()
	 * @see #setUseDecorationLabelValue(boolean)
	 * @generated
	 */
	void unsetUseDecorationLabelValue();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.type.GanttSeries#isUseDecorationLabelValue
	 * <em>Use Decoration Label Value</em>}' attribute is set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Use Decoration Label Value</em>'
	 *         attribute is set.
	 * @see #unsetUseDecorationLabelValue()
	 * @see #isUseDecorationLabelValue()
	 * @see #setUseDecorationLabelValue(boolean)
	 * @generated
	 */
	boolean isSetUseDecorationLabelValue();

	/**
	 * Returns the value of the '<em><b>Decoration Label</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc
	 * -->
	 * 
	 * Holds the properties for the decoration label, which could be used to
	 * decorate the primary base Axis.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Decoration Label</em>' containment reference.
	 * @see #setDecorationLabel(Label)
	 * @see org.eclipse.birt.chart.model.type.TypePackage#getGanttSeries_DecorationLabel()
	 * @model containment="true" required="true" extendedMetaData="kind='element'
	 *        name='DecorationLabel'"
	 * @generated
	 */
	Label getDecorationLabel();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.type.GanttSeries#getDecorationLabel
	 * <em>Decoration Label</em>}' containment reference. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Decoration Label</em>' containment
	 *              reference.
	 * @see #getDecorationLabel()
	 * @generated
	 */
	void setDecorationLabel(Label value);

	/**
	 * Returns the value of the '<em><b>Decoration Label Position</b></em>'
	 * attribute. The default value is <code>"Above"</code>. The literals are from
	 * the enumeration {@link org.eclipse.birt.chart.model.attribute.Position}. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Holds the position property for the decoration label.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Decoration Label Position</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.Position
	 * @see #isSetDecorationLabelPosition()
	 * @see #unsetDecorationLabelPosition()
	 * @see #setDecorationLabelPosition(Position)
	 * @see org.eclipse.birt.chart.model.type.TypePackage#getGanttSeries_DecorationLabelPosition()
	 * @model default="Above" unsettable="true" required="true"
	 *        extendedMetaData="kind='element' name='DecorationLabelPosition'"
	 * @generated
	 */
	Position getDecorationLabelPosition();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.type.GanttSeries#getDecorationLabelPosition
	 * <em>Decoration Label Position</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Decoration Label Position</em>'
	 *              attribute.
	 * @see org.eclipse.birt.chart.model.attribute.Position
	 * @see #isSetDecorationLabelPosition()
	 * @see #unsetDecorationLabelPosition()
	 * @see #getDecorationLabelPosition()
	 * @generated
	 */
	void setDecorationLabelPosition(Position value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.type.GanttSeries#getDecorationLabelPosition
	 * <em>Decoration Label Position</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isSetDecorationLabelPosition()
	 * @see #getDecorationLabelPosition()
	 * @see #setDecorationLabelPosition(Position)
	 * @generated
	 */
	void unsetDecorationLabelPosition();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.type.GanttSeries#getDecorationLabelPosition
	 * <em>Decoration Label Position</em>}' attribute is set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Decoration Label Position</em>'
	 *         attribute is set.
	 * @see #unsetDecorationLabelPosition()
	 * @see #getDecorationLabelPosition()
	 * @see #setDecorationLabelPosition(Position)
	 * @generated
	 */
	boolean isSetDecorationLabelPosition();

	/**
	 * Returns the value of the '<em><b>Palette Line Color</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> Indicates
	 * if use the series palette color to draw the line instead of the color in
	 * ConnectionLine <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Palette Line Color</em>' attribute.
	 * @see #isSetPaletteLineColor()
	 * @see #unsetPaletteLineColor()
	 * @see #setPaletteLineColor(boolean)
	 * @see org.eclipse.birt.chart.model.type.TypePackage#getGanttSeries_PaletteLineColor()
	 * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        required="true" extendedMetaData="kind='element'
	 *        name='PaletteLineColor'"
	 * @generated
	 */
	boolean isPaletteLineColor();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.type.GanttSeries#isPaletteLineColor
	 * <em>Palette Line Color</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Palette Line Color</em>' attribute.
	 * @see #isSetPaletteLineColor()
	 * @see #unsetPaletteLineColor()
	 * @see #isPaletteLineColor()
	 * @generated
	 */
	void setPaletteLineColor(boolean value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.type.GanttSeries#isPaletteLineColor
	 * <em>Palette Line Color</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isSetPaletteLineColor()
	 * @see #isPaletteLineColor()
	 * @see #setPaletteLineColor(boolean)
	 * @generated
	 */
	void unsetPaletteLineColor();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.type.GanttSeries#isPaletteLineColor
	 * <em>Palette Line Color</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Palette Line Color</em>' attribute is
	 *         set.
	 * @see #unsetPaletteLineColor()
	 * @see #isPaletteLineColor()
	 * @see #setPaletteLineColor(boolean)
	 * @generated
	 */
	boolean isSetPaletteLineColor();

	/**
	 * @generated
	 */
	GanttSeries copyInstance();

} // GanttSeries
