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

package org.eclipse.birt.chart.model.type.impl;

import org.eclipse.birt.chart.model.type.*;

import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.type.ScatterSeries;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.model.type.TypeFactory;
import org.eclipse.birt.chart.model.type.TypePackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.EFactoryImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory </b>. <!-- end-user-doc -->
 * @generated
 */
public class TypeFactoryImpl extends EFactoryImpl implements TypeFactory
{

    /**
     * Creates and instance of the factory.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public TypeFactoryImpl()
    {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EObject create(EClass eClass)
    {
        switch (eClass.getClassifierID())
        {
            case TypePackage.BAR_SERIES: return createBarSeries();
            case TypePackage.LINE_SERIES: return createLineSeries();
            case TypePackage.PIE_SERIES: return createPieSeries();
            case TypePackage.SCATTER_SERIES: return createScatterSeries();
            case TypePackage.STOCK_SERIES: return createStockSeries();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public BarSeries createBarSeries()
    {
        BarSeriesImpl barSeries = new BarSeriesImpl();
        return barSeries;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public LineSeries createLineSeries()
    {
        LineSeriesImpl lineSeries = new LineSeriesImpl();
        return lineSeries;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public PieSeries createPieSeries()
    {
        PieSeriesImpl pieSeries = new PieSeriesImpl();
        return pieSeries;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public ScatterSeries createScatterSeries()
    {
        ScatterSeriesImpl scatterSeries = new ScatterSeriesImpl();
        return scatterSeries;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public StockSeries createStockSeries()
    {
        StockSeriesImpl stockSeries = new StockSeriesImpl();
        return stockSeries;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public TypePackage getTypePackage()
    {
        return (TypePackage)getEPackage();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    public static TypePackage getPackage()
    {
        return TypePackage.eINSTANCE;
    }

} //TypeFactoryImpl
