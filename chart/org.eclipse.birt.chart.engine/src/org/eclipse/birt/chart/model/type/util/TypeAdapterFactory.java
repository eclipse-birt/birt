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

package org.eclipse.birt.chart.model.type.util;

import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.type.AreaSeries;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.BubbleSeries;
import org.eclipse.birt.chart.model.type.DialSeries;
import org.eclipse.birt.chart.model.type.DifferenceSeries;
import org.eclipse.birt.chart.model.type.GanttSeries;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.type.ScatterSeries;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.model.type.TypePackage;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> The <b>Adapter Factory </b> for the model. It
 * provides an adapter <code>createXXX</code> method for each class of the
 * model. <!-- end-user-doc -->
 *
 * @see org.eclipse.birt.chart.model.type.TypePackage
 * @generated
 */
public class TypeAdapterFactory extends AdapterFactoryImpl {

	/**
	 * The cached model package. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected static TypePackage modelPackage;

	/**
	 * Creates an instance of the adapter factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @generated
	 */
	public TypeAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = TypePackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object. <!--
	 * begin-user-doc --> This implementation returns <code>true</code> if the
	 * object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 *
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject) object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected TypeSwitch<Adapter> modelSwitch = new TypeSwitch<>() {

		@Override
		public Adapter caseAreaSeries(AreaSeries object) {
			return createAreaSeriesAdapter();
		}

		@Override
		public Adapter caseBarSeries(BarSeries object) {
			return createBarSeriesAdapter();
		}

		@Override
		public Adapter caseBubbleSeries(BubbleSeries object) {
			return createBubbleSeriesAdapter();
		}

		@Override
		public Adapter caseDialSeries(DialSeries object) {
			return createDialSeriesAdapter();
		}

		@Override
		public Adapter caseDifferenceSeries(DifferenceSeries object) {
			return createDifferenceSeriesAdapter();
		}

		@Override
		public Adapter caseGanttSeries(GanttSeries object) {
			return createGanttSeriesAdapter();
		}

		@Override
		public Adapter caseLineSeries(LineSeries object) {
			return createLineSeriesAdapter();
		}

		@Override
		public Adapter casePieSeries(PieSeries object) {
			return createPieSeriesAdapter();
		}

		@Override
		public Adapter caseScatterSeries(ScatterSeries object) {
			return createScatterSeriesAdapter();
		}

		@Override
		public Adapter caseStockSeries(StockSeries object) {
			return createStockSeriesAdapter();
		}

		@Override
		public Adapter caseSeries(Series object) {
			return createSeriesAdapter();
		}

		@Override
		public Adapter defaultCase(EObject object) {
			return createEObjectAdapter();
		}
	};

	/**
	 * Creates an adapter for the <code>target</code>. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject) target);
	}

	/**
	 * Creates a new adapter for an object of class
	 * '{@link org.eclipse.birt.chart.model.type.AreaSeries <em>Area Series</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we
	 * can easily ignore cases; it's useful to ignore a case when inheritance will
	 * catch all the cases anyway. <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.type.AreaSeries
	 * @generated
	 */
	public Adapter createAreaSeriesAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class
	 * '{@link org.eclipse.birt.chart.model.type.BarSeries <em>Bar Series</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we
	 * can easily ignore cases; it's useful to ignore a case when inheritance will
	 * catch all the cases anyway. <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.type.BarSeries
	 * @generated
	 */
	public Adapter createBarSeriesAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class
	 * '{@link org.eclipse.birt.chart.model.type.BubbleSeries <em>Bubble
	 * Series</em>}'. <!-- begin-user-doc --> This default implementation returns
	 * null so that we can easily ignore cases; it's useful to ignore a case when
	 * inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.type.BubbleSeries
	 * @generated
	 */
	public Adapter createBubbleSeriesAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class
	 * '{@link org.eclipse.birt.chart.model.type.DialSeries <em>Dial Series</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we
	 * can easily ignore cases; it's useful to ignore a case when inheritance will
	 * catch all the cases anyway. <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.type.DialSeries
	 * @generated
	 */
	public Adapter createDialSeriesAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class
	 * '{@link org.eclipse.birt.chart.model.type.DifferenceSeries <em>Difference
	 * Series</em>}'. <!-- begin-user-doc --> This default implementation returns
	 * null so that we can easily ignore cases; it's useful to ignore a case when
	 * inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.type.DifferenceSeries
	 * @generated
	 */
	public Adapter createDifferenceSeriesAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class
	 * '{@link org.eclipse.birt.chart.model.type.GanttSeries <em>Gantt
	 * Series</em>}'. <!-- begin-user-doc --> This default implementation returns
	 * null so that we can easily ignore cases; it's useful to ignore a case when
	 * inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.type.GanttSeries
	 * @generated
	 */
	public Adapter createGanttSeriesAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class
	 * '{@link org.eclipse.birt.chart.model.type.LineSeries <em>Line Series</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we
	 * can easily ignore cases; it's useful to ignore a case when inheritance will
	 * catch all the cases anyway. <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.type.LineSeries
	 * @generated
	 */
	public Adapter createLineSeriesAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class
	 * '{@link org.eclipse.birt.chart.model.type.PieSeries <em>Pie Series</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we
	 * can easily ignore cases; it's useful to ignore a case when inheritance will
	 * catch all the cases anyway. <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.type.PieSeries
	 * @generated
	 */
	public Adapter createPieSeriesAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class
	 * '{@link org.eclipse.birt.chart.model.type.ScatterSeries <em>Scatter
	 * Series</em>}'. <!-- begin-user-doc --> This default implementation returns
	 * null so that we can easily ignore cases; it's useful to ignore a case when
	 * inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.type.ScatterSeries
	 * @generated
	 */
	public Adapter createScatterSeriesAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class
	 * '{@link org.eclipse.birt.chart.model.type.StockSeries <em>Stock
	 * Series</em>}'. <!-- begin-user-doc --> This default implementation returns
	 * null so that we can easily ignore cases; it's useful to ignore a case when
	 * inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.type.StockSeries
	 * @generated
	 */
	public Adapter createStockSeriesAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class
	 * '{@link org.eclipse.birt.chart.model.component.Series <em>Series</em>}'. <!--
	 * begin-user-doc --> This default implementation returns null so that we can
	 * easily ignore cases; it's useful to ignore a case when inheritance will catch
	 * all the cases anyway. <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see org.eclipse.birt.chart.model.component.Series
	 * @generated
	 */
	public Adapter createSeriesAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case. <!-- begin-user-doc --> This
	 * default implementation returns null. <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} // TypeAdapterFactory
