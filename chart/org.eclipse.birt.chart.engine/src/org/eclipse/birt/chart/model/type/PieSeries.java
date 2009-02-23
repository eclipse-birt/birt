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
import org.eclipse.birt.chart.model.attribute.LeaderLineStyle;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Series;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Pie Series</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * 			This is a Series type that, during design time, holds the query data for Pie charts, and during run time, 
 * 			holds the value for each data point in the pie that represents the series.  When rendered, each series is 
 * 			drawn as a complete pie, and each data point in that series defines the size of the pie's slices.  A pie chart
 * 			with multiple series will draw multiple pies.
 * 			
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.birt.chart.model.type.PieSeries#getExplosion <em>Explosion</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.type.PieSeries#getExplosionExpression <em>Explosion Expression</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.type.PieSeries#getTitle <em>Title</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.type.PieSeries#getTitlePosition <em>Title Position</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.type.PieSeries#getLeaderLineAttributes <em>Leader Line Attributes</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.type.PieSeries#getLeaderLineStyle <em>Leader Line Style</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.type.PieSeries#getLeaderLineLength <em>Leader Line Length</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.type.PieSeries#getSliceOutline <em>Slice Outline</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.type.PieSeries#getRatio <em>Ratio</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.type.PieSeries#getRotation <em>Rotation</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.type.TypePackage#getPieSeries()
 * @model extendedMetaData="name='PieSeries' kind='elementOnly'"
 * @generated
 */
public interface PieSeries extends Series
{

	/**
	 * Returns the value of the '<em><b>Explosion</b></em>' attribute. <!-- begin-user-doc --> Get the explosion
	 * value to be used for the chart. This defines the amount by which the slices are displaced from the center of the
	 * pie. <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Specifies the 'Explosion' value to be used while displaying the pie slices.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Explosion</em>' attribute.
	 * @see #isSetExplosion()
	 * @see #unsetExplosion()
	 * @see #setExplosion(int)
	 * @see org.eclipse.birt.chart.model.type.TypePackage#getPieSeries_Explosion()
	 * @model unique="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Int"
	 * @generated
	 */
	int getExplosion( );

	/**
	 * Sets the value of the '{@link org.eclipse.birt.chart.model.type.PieSeries#getExplosion <em>Explosion</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Explosion</em>' attribute.
	 * @see #isSetExplosion()
	 * @see #unsetExplosion()
	 * @see #getExplosion()
	 * @generated
	 */
	void setExplosion( int value );

	/**
	 * Unsets the value of the '{@link org.eclipse.birt.chart.model.type.PieSeries#getExplosion <em>Explosion</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #isSetExplosion()
	 * @see #getExplosion()
	 * @see #setExplosion(int)
	 * @generated
	 */
	void unsetExplosion( );

	/**
	 * Returns whether the value of the '
	 * {@link org.eclipse.birt.chart.model.type.PieSeries#getExplosion <em>Explosion</em>}' attribute is set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Explosion</em>' attribute is set.
	 * @see #unsetExplosion()
	 * @see #getExplosion()
	 * @see #setExplosion(int)
	 * @generated
	 */
	boolean isSetExplosion( );

	/**
	 * Returns the value of the '<em><b>Explosion Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Specifies an expression to determine if the explosion will be applied to each slice.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Explosion Expression</em>' attribute.
	 * @see #setExplosionExpression(String)
	 * @see org.eclipse.birt.chart.model.type.TypePackage#getPieSeries_ExplosionExpression()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='element' name='ExplosionExpression'"
	 * @generated
	 */
	String getExplosionExpression( );

	/**
	 * Sets the value of the '{@link org.eclipse.birt.chart.model.type.PieSeries#getExplosionExpression <em>Explosion Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Explosion Expression</em>' attribute.
	 * @see #getExplosionExpression()
	 * @generated
	 */
	void setExplosionExpression( String value );

	/**
	 * Returns the value of the '<em><b>Title</b></em>' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Holds the properties for a series title.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Title</em>' containment reference.
	 * @see #setTitle(Label)
	 * @see org.eclipse.birt.chart.model.type.TypePackage#getPieSeries_Title()
	 * @model containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	Label getTitle( );

	/**
	 * Sets the value of the '{@link org.eclipse.birt.chart.model.type.PieSeries#getTitle <em>Title</em>}' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Title</em>' containment reference.
	 * @see #getTitle()
	 * @generated
	 */
	void setTitle( Label value );

	/**
	 * Returns the value of the '<em><b>Title Position</b></em>' attribute. The default value is
	 * <code>"Above"</code>. The literals are from the enumeration
	 * {@link org.eclipse.birt.chart.model.attribute.Position}. <!-- begin-user-doc --> <!-- end-user-doc --> <!--
	 * begin-model-doc -->
	 * 
	 * Holds the position property for a series title.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Title Position</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.Position
	 * @see #isSetTitlePosition()
	 * @see #unsetTitlePosition()
	 * @see #setTitlePosition(Position)
	 * @see org.eclipse.birt.chart.model.type.TypePackage#getPieSeries_TitlePosition()
	 * @model default="Above" unique="false" unsettable="true" required="true"
	 * @generated
	 */
	Position getTitlePosition( );

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.birt.chart.model.type.PieSeries#getTitlePosition <em>Title Position</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Title Position</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.Position
	 * @see #isSetTitlePosition()
	 * @see #unsetTitlePosition()
	 * @see #getTitlePosition()
	 * @generated
	 */
	void setTitlePosition( Position value );

	/**
	 * Unsets the value of the '
	 * {@link org.eclipse.birt.chart.model.type.PieSeries#getTitlePosition <em>Title Position</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetTitlePosition()
	 * @see #getTitlePosition()
	 * @see #setTitlePosition(Position)
	 * @generated
	 */
	void unsetTitlePosition( );

	/**
	 * Returns whether the value of the '{@link org.eclipse.birt.chart.model.type.PieSeries#getTitlePosition <em>Title Position</em>}' attribute is set.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return whether the value of the '<em>Title Position</em>' attribute is set.
	 * @see #unsetTitlePosition()
	 * @see #getTitlePosition()
	 * @see #setTitlePosition(Position)
	 * @generated
	 */
	boolean isSetTitlePosition( );

	/**
	 * Returns the value of the '<em><b>Leader Line Attributes</b></em>' containment reference. <!-- begin-user-doc
	 * --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Holds the attributes for leader lines.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Leader Line Attributes</em>' containment reference.
	 * @see #setLeaderLineAttributes(LineAttributes)
	 * @see org.eclipse.birt.chart.model.type.TypePackage#getPieSeries_LeaderLineAttributes()
	 * @model containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	LineAttributes getLeaderLineAttributes( );

	/**
	 * Sets the value of the '{@link org.eclipse.birt.chart.model.type.PieSeries#getLeaderLineAttributes <em>Leader Line Attributes</em>}' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Leader Line Attributes</em>' containment reference.
	 * @see #getLeaderLineAttributes()
	 * @generated
	 */
	void setLeaderLineAttributes( LineAttributes value );

	/**
	 * Returns the value of the '<em><b>Leader Line Style</b></em>' attribute.
	 * The default value is <code>"Fixed_Length"</code>.
	 * The literals are from the enumeration {@link org.eclipse.birt.chart.model.attribute.LeaderLineStyle}.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 							Specifies how the leader lines are to be shown.
	 * 							
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Leader Line Style</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.LeaderLineStyle
	 * @see #isSetLeaderLineStyle()
	 * @see #unsetLeaderLineStyle()
	 * @see #setLeaderLineStyle(LeaderLineStyle)
	 * @see org.eclipse.birt.chart.model.type.TypePackage#getPieSeries_LeaderLineStyle()
	 * @model default="Fixed_Length" unsettable="true" required="true"
	 *        extendedMetaData="kind='element' name='LeaderLineStyle'"
	 * @generated
	 */
	LeaderLineStyle getLeaderLineStyle( );

	/**
	 * Sets the value of the '{@link org.eclipse.birt.chart.model.type.PieSeries#getLeaderLineStyle <em>Leader Line Style</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Leader Line Style</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.LeaderLineStyle
	 * @see #isSetLeaderLineStyle()
	 * @see #unsetLeaderLineStyle()
	 * @see #getLeaderLineStyle()
	 * @generated
	 */
	void setLeaderLineStyle( LeaderLineStyle value );

	/**
	 * Unsets the value of the '{@link org.eclipse.birt.chart.model.type.PieSeries#getLeaderLineStyle <em>Leader Line Style</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #isSetLeaderLineStyle()
	 * @see #getLeaderLineStyle()
	 * @see #setLeaderLineStyle(LeaderLineStyle)
	 * @generated
	 */
	void unsetLeaderLineStyle( );

	/**
	 * Returns whether the value of the '{@link org.eclipse.birt.chart.model.type.PieSeries#getLeaderLineStyle <em>Leader Line Style</em>}' attribute is set.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return whether the value of the '<em>Leader Line Style</em>' attribute is set.
	 * @see #unsetLeaderLineStyle()
	 * @see #getLeaderLineStyle()
	 * @see #setLeaderLineStyle(LeaderLineStyle)
	 * @generated
	 */
	boolean isSetLeaderLineStyle( );

	/**
	 * Returns the value of the '<em><b>Leader Line Length</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Length for the leader lines. Used only if style is 'FIXED_LENGTH'.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Leader Line Length</em>' attribute.
	 * @see #isSetLeaderLineLength()
	 * @see #unsetLeaderLineLength()
	 * @see #setLeaderLineLength(double)
	 * @see org.eclipse.birt.chart.model.type.TypePackage#getPieSeries_LeaderLineLength()
	 * @model unique="false" unsettable="true" dataType="org.eclipse.birt.chart.model.attribute.Percentage"
	 *        required="true"
	 * @generated
	 */
	double getLeaderLineLength( );

	/**
	 * Sets the value of the '{@link org.eclipse.birt.chart.model.type.PieSeries#getLeaderLineLength <em>Leader Line Length</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Leader Line Length</em>' attribute.
	 * @see #isSetLeaderLineLength()
	 * @see #unsetLeaderLineLength()
	 * @see #getLeaderLineLength()
	 * @generated
	 */
	void setLeaderLineLength( double value );

	/**
	 * Unsets the value of the '{@link org.eclipse.birt.chart.model.type.PieSeries#getLeaderLineLength <em>Leader Line Length</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #isSetLeaderLineLength()
	 * @see #getLeaderLineLength()
	 * @see #setLeaderLineLength(double)
	 * @generated
	 */
	void unsetLeaderLineLength( );

	/**
	 * Returns whether the value of the '{@link org.eclipse.birt.chart.model.type.PieSeries#getLeaderLineLength <em>Leader Line Length</em>}' attribute is set.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return whether the value of the '<em>Leader Line Length</em>' attribute is set.
	 * @see #unsetLeaderLineLength()
	 * @see #getLeaderLineLength()
	 * @see #setLeaderLineLength(double)
	 * @generated
	 */
	boolean isSetLeaderLineLength( );

	/**
	 * Returns the value of the '<em><b>Slice Outline</b></em>' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Defines the color to be used for the slice outline.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Slice Outline</em>' containment reference.
	 * @see #setSliceOutline(ColorDefinition)
	 * @see org.eclipse.birt.chart.model.type.TypePackage#getPieSeries_SliceOutline()
	 * @model containment="true" resolveProxies="false"
	 * @generated
	 */
	ColorDefinition getSliceOutline( );

	/**
	 * Sets the value of the '{@link org.eclipse.birt.chart.model.type.PieSeries#getSliceOutline <em>Slice Outline</em>}' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Slice Outline</em>' containment reference.
	 * @see #getSliceOutline()
	 * @generated
	 */
	void setSliceOutline( ColorDefinition value );

	/**
	 * Returns the value of the '<em><b>Ratio</b></em>' attribute.
	 * The default value is <code>"1"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Defines the ratio(height/width) of the oval, 1 means it's a circle, 0 means stretch automatically.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Ratio</em>' attribute.
	 * @see #isSetRatio()
	 * @see #unsetRatio()
	 * @see #setRatio(double)
	 * @see org.eclipse.birt.chart.model.type.TypePackage#getPieSeries_Ratio()
	 * @model default="1" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Double" required="true"
	 *        extendedMetaData="kind='element' name='Ratio'"
	 * @generated
	 */
	double getRatio( );

	/**
	 * Sets the value of the '{@link org.eclipse.birt.chart.model.type.PieSeries#getRatio <em>Ratio</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ratio</em>' attribute.
	 * @see #isSetRatio()
	 * @see #unsetRatio()
	 * @see #getRatio()
	 * @generated
	 */
	void setRatio( double value );

	/**
	 * Unsets the value of the '{@link org.eclipse.birt.chart.model.type.PieSeries#getRatio <em>Ratio</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetRatio()
	 * @see #getRatio()
	 * @see #setRatio(double)
	 * @generated
	 */
	void unsetRatio( );

	/**
	 * Returns whether the value of the '{@link org.eclipse.birt.chart.model.type.PieSeries#getRatio <em>Ratio</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Ratio</em>' attribute is set.
	 * @see #unsetRatio()
	 * @see #getRatio()
	 * @see #setRatio(double)
	 * @generated
	 */
	boolean isSetRatio( );

	/**
	 * Returns the value of the '<em><b>Rotation</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 								Defines the rotation of the pie, 0 means start drawing the first slice from the east.
	 * 							
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Rotation</em>' attribute.
	 * @see #isSetRotation()
	 * @see #unsetRotation()
	 * @see #setRotation(double)
	 * @see org.eclipse.birt.chart.model.type.TypePackage#getPieSeries_Rotation()
	 * @model default="0" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Double" required="true"
	 *        extendedMetaData="kind='element' name='Rotation'"
	 * @generated
	 */
	double getRotation( );

	/**
	 * Sets the value of the '{@link org.eclipse.birt.chart.model.type.PieSeries#getRotation <em>Rotation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Rotation</em>' attribute.
	 * @see #isSetRotation()
	 * @see #unsetRotation()
	 * @see #getRotation()
	 * @generated
	 */
	void setRotation( double value );

	/**
	 * Unsets the value of the '{@link org.eclipse.birt.chart.model.type.PieSeries#getRotation <em>Rotation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetRotation()
	 * @see #getRotation()
	 * @see #setRotation(double)
	 * @generated
	 */
	void unsetRotation( );

	/**
	 * Returns whether the value of the '{@link org.eclipse.birt.chart.model.type.PieSeries#getRotation <em>Rotation</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Rotation</em>' attribute is set.
	 * @see #unsetRotation()
	 * @see #getRotation()
	 * @see #setRotation(double)
	 * @generated
	 */
	boolean isSetRotation( );

} // PieSeries
