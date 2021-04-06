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

package org.eclipse.birt.chart.model.type;

import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Line
 * Series</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc --> This is a Series type that, during design time,
 * holds the query data for Line charts, and during run time, holds the value
 * for each data point in the line. When rendered, a line connects each data
 * point. <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.type.LineSeries#getMarkers
 * <em>Markers</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.LineSeries#getMarker
 * <em>Marker</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.LineSeries#getLineAttributes
 * <em>Line Attributes</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.LineSeries#isPaletteLineColor
 * <em>Palette Line Color</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.LineSeries#isCurve
 * <em>Curve</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.LineSeries#getShadowColor
 * <em>Shadow Color</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.LineSeries#isConnectMissingValue
 * <em>Connect Missing Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.type.TypePackage#getLineSeries()
 * @model extendedMetaData="name='LineSeries' kind='elementOnly'"
 * @generated
 */
public interface LineSeries extends Series {

	/**
	 * Returns the value of the '<em><b>Markers</b></em>' containment reference
	 * list. The list contents are of type
	 * {@link org.eclipse.birt.chart.model.attribute.Marker}. <!-- begin-user-doc
	 * --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Specifies the marker to be used for displaying the data point on the line in
	 * the chart.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Markers</em>' containment reference list.
	 * @see org.eclipse.birt.chart.model.type.TypePackage#getLineSeries_Markers()
	 * @model type="org.eclipse.birt.chart.model.attribute.Marker"
	 *        containment="true" resolveProxies="false"
	 *        extendedMetaData="kind='element' name='Markers'"
	 * @generated
	 */
	EList<Marker> getMarkers();

	/**
	 * Returns the value of the '<em><b>Marker</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> <!--
	 * end-model-doc -->
	 * 
	 * @return the value of the '<em>Marker</em>' containment reference.
	 * @see #setMarker(Marker)
	 * @see org.eclipse.birt.chart.model.type.TypePackage#getLineSeries_Marker()
	 * @model containment="true" resolveProxies="false"
	 *        extendedMetaData="kind='element' name='Marker'"
	 * @deprecated Use Markers property instead. This is kept just to maintain the
	 *             compatibility with old models.
	 */
	Marker getMarker();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.type.LineSeries#getMarker
	 * <em>Marker</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Marker</em>' containment reference.
	 * @see #getMarker()
	 * @deprecated Use Markers property instead. This is kept just to maintain the
	 *             compatibility with old models.
	 */
	void setMarker(Marker value);

	/**
	 * Returns the value of the '<em><b>Line Attributes</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc
	 * -->
	 * 
	 * Specifies the attributes for the line used to represent this series.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Line Attributes</em>' containment reference.
	 * @see #setLineAttributes(LineAttributes)
	 * @see org.eclipse.birt.chart.model.type.TypePackage#getLineSeries_LineAttributes()
	 * @model containment="true" resolveProxies="false"
	 * @generated
	 */
	LineAttributes getLineAttributes();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.type.LineSeries#getLineAttributes
	 * <em>Line Attributes</em>}' containment reference. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Line Attributes</em>' containment
	 *              reference.
	 * @see #getLineAttributes()
	 * @generated
	 */
	void setLineAttributes(LineAttributes value);

	/**
	 * Returns the value of the '<em><b>Palette Line Color</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> Indicates
	 * if use the series palette color to draw the line instead of the color in
	 * LineAttributes <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Palette Line Color</em>' attribute.
	 * @see #isSetPaletteLineColor()
	 * @see #unsetPaletteLineColor()
	 * @see #setPaletteLineColor(boolean)
	 * @see org.eclipse.birt.chart.model.type.TypePackage#getLineSeries_PaletteLineColor()
	 * @model unique="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Boolean" required="true"
	 *        extendedMetaData="kind='element' name='PaletteLineColor'"
	 * @generated
	 */
	boolean isPaletteLineColor();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.type.LineSeries#isPaletteLineColor
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
	 * '{@link org.eclipse.birt.chart.model.type.LineSeries#isPaletteLineColor
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
	 * '{@link org.eclipse.birt.chart.model.type.LineSeries#isPaletteLineColor
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
	 * Returns the value of the '<em><b>Curve</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Indicates whether the line segments joining data points in the series are to
	 * be drawn as curves or as straight lines.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Curve</em>' attribute.
	 * @see #isSetCurve()
	 * @see #unsetCurve()
	 * @see #setCurve(boolean)
	 * @see org.eclipse.birt.chart.model.type.TypePackage#getLineSeries_Curve()
	 * @model unique="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Boolean" required="true"
	 *        extendedMetaData="kind='element' name='Curve'"
	 * @generated
	 */
	boolean isCurve();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.type.LineSeries#isCurve <em>Curve</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Curve</em>' attribute.
	 * @see #isSetCurve()
	 * @see #unsetCurve()
	 * @see #isCurve()
	 * @generated
	 */
	void setCurve(boolean value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.type.LineSeries#isCurve <em>Curve</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetCurve()
	 * @see #isCurve()
	 * @see #setCurve(boolean)
	 * @generated
	 */
	void unsetCurve();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.type.LineSeries#isCurve <em>Curve</em>}'
	 * attribute is set. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Curve</em>' attribute is set.
	 * @see #unsetCurve()
	 * @see #isCurve()
	 * @see #setCurve(boolean)
	 * @generated
	 */
	boolean isSetCurve();

	/**
	 * Returns the value of the '<em><b>Shadow Color</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc
	 * -->
	 * 
	 * Specifies the color to be used for the shadow.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Shadow Color</em>' containment reference.
	 * @see #setShadowColor(ColorDefinition)
	 * @see org.eclipse.birt.chart.model.type.TypePackage#getLineSeries_ShadowColor()
	 * @model containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	ColorDefinition getShadowColor();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.type.LineSeries#getShadowColor
	 * <em>Shadow Color</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Shadow Color</em>' containment
	 *              reference.
	 * @see #getShadowColor()
	 * @generated
	 */
	void setShadowColor(ColorDefinition value);

	/**
	 * Returns the value of the '<em><b>Connect Missing Value</b></em>' attribute.
	 * The default value is <code>"true"</code>. <!-- begin-user-doc --> <!--
	 * end-user-doc --> <!-- begin-model-doc --> Specifies if connect the missing
	 * values in the line or just skip drawing. <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Connect Missing Value</em>' attribute.
	 * @see #isSetConnectMissingValue()
	 * @see #unsetConnectMissingValue()
	 * @see #setConnectMissingValue(boolean)
	 * @see org.eclipse.birt.chart.model.type.TypePackage#getLineSeries_ConnectMissingValue()
	 * @model default="true" unique="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        extendedMetaData="kind='element' name='ConnectMissingValue'"
	 * @generated
	 */
	boolean isConnectMissingValue();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.type.LineSeries#isConnectMissingValue
	 * <em>Connect Missing Value</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Connect Missing Value</em>' attribute.
	 * @see #isSetConnectMissingValue()
	 * @see #unsetConnectMissingValue()
	 * @see #isConnectMissingValue()
	 * @generated
	 */
	void setConnectMissingValue(boolean value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.type.LineSeries#isConnectMissingValue
	 * <em>Connect Missing Value</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isSetConnectMissingValue()
	 * @see #isConnectMissingValue()
	 * @see #setConnectMissingValue(boolean)
	 * @generated
	 */
	void unsetConnectMissingValue();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.type.LineSeries#isConnectMissingValue
	 * <em>Connect Missing Value</em>}' attribute is set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Connect Missing Value</em>' attribute
	 *         is set.
	 * @see #unsetConnectMissingValue()
	 * @see #isConnectMissingValue()
	 * @see #setConnectMissingValue(boolean)
	 * @generated
	 */
	boolean isSetConnectMissingValue();

	/**
	 * @generated
	 */
	LineSeries copyInstance();

} // LineSeries
