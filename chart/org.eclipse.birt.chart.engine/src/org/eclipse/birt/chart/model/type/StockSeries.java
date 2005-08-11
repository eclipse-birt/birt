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

import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.component.Series;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Stock Series</b></em>'. <!-- end-user-doc
 * -->
 * 
 * <!-- begin-model-doc -->
 * 
 * This is a Series type that holds data for Stock Charts.
 * 
 * <!-- end-model-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.type.StockSeries#getFill <em>Fill</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.StockSeries#getLineAttributes <em>Line Attributes</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.birt.chart.model.type.TypePackage#getStockSeries()
 * @model
 * @generated
 */
public interface StockSeries extends Series{

    /**
     * Returns the value of the '<em><b>Fill</b></em>' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc --> <!-- begin-model-doc -->
     * 
     * Defines the fill to be used for the Candle.
     * 
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Fill</em>' containment reference.
     * @see #setFill(Fill)
     * @see org.eclipse.birt.chart.model.type.TypePackage#getStockSeries_Fill()
     * @model containment="true" resolveProxies="false"
     * @generated
     */
    Fill getFill();

	/**
	 * Sets the value of the '{@link org.eclipse.birt.chart.model.type.StockSeries#getFill <em>Fill</em>}' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fill</em>' containment reference.
	 * @see #getFill()
	 * @generated
	 */
    void setFill(Fill value);

    /**
     * Returns the value of the '<em><b>Line Attributes</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc --> <!-- begin-model-doc -->
     * 
     * Specifies the style to be used to display the lines for this series.
     * 
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Line Attributes</em>' containment reference.
     * @see #setLineAttributes(LineAttributes)
     * @see org.eclipse.birt.chart.model.type.TypePackage#getStockSeries_LineAttributes()
     * @model containment="true" resolveProxies="false"
     * @generated
     */
    LineAttributes getLineAttributes();

	/**
	 * Sets the value of the '{@link org.eclipse.birt.chart.model.type.StockSeries#getLineAttributes <em>Line Attributes</em>}' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Line Attributes</em>' containment reference.
	 * @see #getLineAttributes()
	 * @generated
	 */
    void setLineAttributes(LineAttributes value);

} // StockSeries
