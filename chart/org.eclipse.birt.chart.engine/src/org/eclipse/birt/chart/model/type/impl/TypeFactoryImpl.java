/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.model.type.impl;

import org.eclipse.birt.chart.model.type.*;

import org.eclipse.birt.chart.model.type.AreaSeries;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.type.ScatterSeries;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.model.type.TypeFactory;
import org.eclipse.birt.chart.model.type.TypePackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory </b>. <!--
 * end-user-doc -->
 * 
 * @generated
 */
public class TypeFactoryImpl extends EFactoryImpl implements TypeFactory {

	/**
	 * Creates the default factory implementation. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public static TypeFactory init() {
		try {
			TypeFactory theTypeFactory = (TypeFactory) EPackage.Registry.INSTANCE
					.getEFactory("http://www.birt.eclipse.org/ChartModelType"); //$NON-NLS-1$
			if (theTypeFactory != null) {
				return theTypeFactory;
			}
		} catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new TypeFactoryImpl();
	}

	/**
	 * Creates an instance of the factory. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 */
	public TypeFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
		case TypePackage.AREA_SERIES:
			return (EObject) createAreaSeries();
		case TypePackage.BAR_SERIES:
			return (EObject) createBarSeries();
		case TypePackage.BUBBLE_SERIES:
			return (EObject) createBubbleSeries();
		case TypePackage.DIAL_SERIES:
			return (EObject) createDialSeries();
		case TypePackage.DIFFERENCE_SERIES:
			return (EObject) createDifferenceSeries();
		case TypePackage.GANTT_SERIES:
			return (EObject) createGanttSeries();
		case TypePackage.LINE_SERIES:
			return (EObject) createLineSeries();
		case TypePackage.PIE_SERIES:
			return (EObject) createPieSeries();
		case TypePackage.SCATTER_SERIES:
			return (EObject) createScatterSeries();
		case TypePackage.STOCK_SERIES:
			return (EObject) createStockSeries();
		default:
			throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public AreaSeries createAreaSeries() {
		AreaSeriesImpl areaSeries = new AreaSeriesImpl();
		return areaSeries;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public BarSeries createBarSeries() {
		BarSeriesImpl barSeries = new BarSeriesImpl();
		return barSeries;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public BubbleSeries createBubbleSeries() {
		BubbleSeriesImpl bubbleSeries = new BubbleSeriesImpl();
		return bubbleSeries;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public DialSeries createDialSeries() {
		DialSeriesImpl dialSeries = new DialSeriesImpl();
		return dialSeries;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public DifferenceSeries createDifferenceSeries() {
		DifferenceSeriesImpl differenceSeries = new DifferenceSeriesImpl();
		return differenceSeries;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public GanttSeries createGanttSeries() {
		GanttSeriesImpl ganttSeries = new GanttSeriesImpl();
		return ganttSeries;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public LineSeries createLineSeries() {
		LineSeriesImpl lineSeries = new LineSeriesImpl();
		return lineSeries;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public PieSeries createPieSeries() {
		PieSeriesImpl pieSeries = new PieSeriesImpl();
		return pieSeries;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ScatterSeries createScatterSeries() {
		ScatterSeriesImpl scatterSeries = new ScatterSeriesImpl();
		return scatterSeries;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public StockSeries createStockSeries() {
		StockSeriesImpl stockSeries = new StockSeriesImpl();
		return stockSeries;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public TypePackage getTypePackage() {
		return (TypePackage) getEPackage();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static TypePackage getPackage() {
		return TypePackage.eINSTANCE;
	}

} // TypeFactoryImpl
