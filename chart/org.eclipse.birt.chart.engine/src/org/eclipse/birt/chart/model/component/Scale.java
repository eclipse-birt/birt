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

package org.eclipse.birt.chart.model.component;

import org.eclipse.birt.chart.model.attribute.ScaleUnitType;
import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Scale</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * 			This type defines the scale associated with an axis.
 * 			
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.birt.chart.model.component.Scale#getMin <em>Min</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.component.Scale#getMax <em>Max</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.component.Scale#getStep <em>Step</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.component.Scale#getUnit <em>Unit</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.component.Scale#getMinorGridsPerUnit <em>Minor Grids Per Unit</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.component.Scale#getStepNumber <em>Step Number</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.component.Scale#isShowOutside <em>Show Outside</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getScale()
 * @model extendedMetaData="name='Scale' kind='elementOnly'"
 * @generated
 */
public interface Scale extends EObject
{

	/**
	 * Returns the value of the '<em><b>Min</b></em>' containment reference. <!-- begin-user-doc --> Gets the
	 * minimum value that will appear on the axis. <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Minimum value that should appear on the axis.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Min</em>' containment reference.
	 * @see #setMin(DataElement)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getScale_Min()
	 * @model containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	DataElement getMin( );

	/**
	 * Sets the value of the '{@link org.eclipse.birt.chart.model.component.Scale#getMin <em>Min</em>}' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min</em>' containment reference.
	 * @see #getMin()
	 * @generated
	 */
	void setMin( DataElement value );

	/**
	 * Returns the value of the '<em><b>Max</b></em>' containment reference. <!-- begin-user-doc --> Gets the
	 * maximum value that will appear on the axis. <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Maximum value that should appear on the axis.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Max</em>' containment reference.
	 * @see #setMax(DataElement)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getScale_Max()
	 * @model containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	DataElement getMax( );

	/**
	 * Sets the value of the '{@link org.eclipse.birt.chart.model.component.Scale#getMax <em>Max</em>}' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max</em>' containment reference.
	 * @see #getMax()
	 * @generated
	 */
	void setMax( DataElement value );

	/**
	 * Returns the value of the '<em><b>Step</b></em>' attribute. <!-- begin-user-doc --> Gets the stepping in the
	 * values shown on the axis. <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Stepping in the values shown on the axis.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Step</em>' attribute.
	 * @see #isSetStep()
	 * @see #unsetStep()
	 * @see #setStep(double)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getScale_Step()
	 * @model unique="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Double" required="true"
	 * @generated
	 */
	double getStep( );

	/**
	 * Sets the value of the '{@link org.eclipse.birt.chart.model.component.Scale#getStep <em>Step</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Step</em>' attribute.
	 * @see #isSetStep()
	 * @see #unsetStep()
	 * @see #getStep()
	 * @generated
	 */
	void setStep( double value );

	/**
	 * Unsets the value of the '{@link org.eclipse.birt.chart.model.component.Scale#getStep <em>Step</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #isSetStep()
	 * @see #getStep()
	 * @see #setStep(double)
	 * @generated
	 */
	void unsetStep( );

	/**
	 * Returns whether the value of the '{@link org.eclipse.birt.chart.model.component.Scale#getStep <em>Step</em>}' attribute is set.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return whether the value of the '<em>Step</em>' attribute is set.
	 * @see #unsetStep()
	 * @see #getStep()
	 * @see #setStep(double)
	 * @generated
	 */
	boolean isSetStep( );

	/**
	 * Returns the value of the '<em><b>Unit</b></em>' attribute. The default value is <code>"Seconds"</code>.
	 * The literals are from the enumeration {@link org.eclipse.birt.chart.model.attribute.ScaleUnitType}. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Stepping in the values shown on the axis.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Unit</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.ScaleUnitType
	 * @see #isSetUnit()
	 * @see #unsetUnit()
	 * @see #setUnit(ScaleUnitType)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getScale_Unit()
	 * @model default="Seconds" unique="false" unsettable="true" required="true"
	 * @generated
	 */
	ScaleUnitType getUnit( );

	/**
	 * Sets the value of the '{@link org.eclipse.birt.chart.model.component.Scale#getUnit <em>Unit</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Unit</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.ScaleUnitType
	 * @see #isSetUnit()
	 * @see #unsetUnit()
	 * @see #getUnit()
	 * @generated
	 */
	void setUnit( ScaleUnitType value );

	/**
	 * Unsets the value of the '{@link org.eclipse.birt.chart.model.component.Scale#getUnit <em>Unit</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #isSetUnit()
	 * @see #getUnit()
	 * @see #setUnit(ScaleUnitType)
	 * @generated
	 */
	void unsetUnit( );

	/**
	 * Returns whether the value of the '{@link org.eclipse.birt.chart.model.component.Scale#getUnit <em>Unit</em>}' attribute is set.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return whether the value of the '<em>Unit</em>' attribute is set.
	 * @see #unsetUnit()
	 * @see #getUnit()
	 * @see #setUnit(ScaleUnitType)
	 * @generated
	 */
	boolean isSetUnit( );

	/**
	 * Returns the value of the '<em><b>Minor Grids Per Unit</b></em>' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Specifies the number of minor grids per unit of the scale.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Minor Grids Per Unit</em>' attribute.
	 * @see #isSetMinorGridsPerUnit()
	 * @see #unsetMinorGridsPerUnit()
	 * @see #setMinorGridsPerUnit(int)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getScale_MinorGridsPerUnit()
	 * @model unique="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Int" required="true"
	 * @generated
	 */
	int getMinorGridsPerUnit( );

	/**
	 * Sets the value of the '{@link org.eclipse.birt.chart.model.component.Scale#getMinorGridsPerUnit <em>Minor Grids Per Unit</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Minor Grids Per Unit</em>' attribute.
	 * @see #isSetMinorGridsPerUnit()
	 * @see #unsetMinorGridsPerUnit()
	 * @see #getMinorGridsPerUnit()
	 * @generated
	 */
	void setMinorGridsPerUnit( int value );

	/**
	 * Unsets the value of the '{@link org.eclipse.birt.chart.model.component.Scale#getMinorGridsPerUnit <em>Minor Grids Per Unit</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #isSetMinorGridsPerUnit()
	 * @see #getMinorGridsPerUnit()
	 * @see #setMinorGridsPerUnit(int)
	 * @generated
	 */
	void unsetMinorGridsPerUnit( );

	/**
	 * Returns whether the value of the '{@link org.eclipse.birt.chart.model.component.Scale#getMinorGridsPerUnit <em>Minor Grids Per Unit</em>}' attribute is set.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return whether the value of the '<em>Minor Grids Per Unit</em>' attribute is set.
	 * @see #unsetMinorGridsPerUnit()
	 * @see #getMinorGridsPerUnit()
	 * @see #setMinorGridsPerUnit(int)
	 * @generated
	 */
	boolean isSetMinorGridsPerUnit( );

	/**
	 * Returns the value of the '<em><b>Step Number</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Number of steps in the values shown on the axis.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Step Number</em>' attribute.
	 * @see #isSetStepNumber()
	 * @see #unsetStepNumber()
	 * @see #setStepNumber(int)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getScale_StepNumber()
	 * @model unique="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Int" required="true"
	 *        extendedMetaData="kind='element' name='StepNumber'"
	 * @generated
	 */
	int getStepNumber( );

	/**
	 * Sets the value of the '{@link org.eclipse.birt.chart.model.component.Scale#getStepNumber <em>Step Number</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Step Number</em>' attribute.
	 * @see #isSetStepNumber()
	 * @see #unsetStepNumber()
	 * @see #getStepNumber()
	 * @generated
	 */
	void setStepNumber( int value );

	/**
	 * Unsets the value of the '{@link org.eclipse.birt.chart.model.component.Scale#getStepNumber <em>Step Number</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetStepNumber()
	 * @see #getStepNumber()
	 * @see #setStepNumber(int)
	 * @generated
	 */
	void unsetStepNumber( );

	/**
	 * Returns whether the value of the '{@link org.eclipse.birt.chart.model.component.Scale#getStepNumber <em>Step Number</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Step Number</em>' attribute is set.
	 * @see #unsetStepNumber()
	 * @see #getStepNumber()
	 * @see #setStepNumber(int)
	 * @generated
	 */
	boolean isSetStepNumber( );

	/**
	 * Returns the value of the '<em><b>Show Outside</b></em>' attribute.
	 * The default value is <code>"true"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Indicates if it shows values outside Axis range.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Show Outside</em>' attribute.
	 * @see #isSetShowOutside()
	 * @see #unsetShowOutside()
	 * @see #setShowOutside(boolean)
	 * @see org.eclipse.birt.chart.model.component.ComponentPackage#getScale_ShowOutside()
	 * @model default="true" unique="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean" required="true"
	 *        extendedMetaData="kind='element' name='ShowOutside'"
	 * @generated
	 */
	boolean isShowOutside( );

	/**
	 * Sets the value of the '{@link org.eclipse.birt.chart.model.component.Scale#isShowOutside <em>Show Outside</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Show Outside</em>' attribute.
	 * @see #isSetShowOutside()
	 * @see #unsetShowOutside()
	 * @see #isShowOutside()
	 * @generated
	 */
	void setShowOutside( boolean value );

	/**
	 * Unsets the value of the '{@link org.eclipse.birt.chart.model.component.Scale#isShowOutside <em>Show Outside</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetShowOutside()
	 * @see #isShowOutside()
	 * @see #setShowOutside(boolean)
	 * @generated
	 */
	void unsetShowOutside( );

	/**
	 * Returns whether the value of the '{@link org.eclipse.birt.chart.model.component.Scale#isShowOutside <em>Show Outside</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Show Outside</em>' attribute is set.
	 * @see #unsetShowOutside()
	 * @see #isShowOutside()
	 * @see #setShowOutside(boolean)
	 * @generated
	 */
	boolean isSetShowOutside( );

} // Scale
