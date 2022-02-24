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
 * $Id: ModelValidator.java,v 1.3 2009/03/05 08:07:37 ywang1 Exp $
 */

package org.eclipse.birt.chart.model.util;

import java.util.Map;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.DialChart;
import org.eclipse.birt.chart.model.ModelPackage;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.EObjectValidator;
import org.eclipse.emf.ecore.xml.type.util.XMLTypeValidator;

/**
 * <!-- begin-user-doc --> The <b>Validator</b> for the model. <!-- end-user-doc
 * -->
 *
 * @see org.eclipse.birt.chart.model.ModelPackage
 * @generated
 */
public class ModelValidator extends EObjectValidator {

	/**
	 * The cached model package <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static final ModelValidator INSTANCE = new ModelValidator();

	/**
	 * A constant for the {@link org.eclipse.emf.common.util.Diagnostic#getSource()
	 * source} of diagnostic {@link org.eclipse.emf.common.util.Diagnostic#getCode()
	 * codes} from this package. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see org.eclipse.emf.common.util.Diagnostic#getSource()
	 * @see org.eclipse.emf.common.util.Diagnostic#getCode()
	 * @generated
	 */
	public static final String DIAGNOSTIC_SOURCE = "org.eclipse.birt.chart.model"; //$NON-NLS-1$

	/**
	 * A constant with a fixed name that can be used as the base value for
	 * additional hand written constants. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @generated
	 */
	private static final int GENERATED_DIAGNOSTIC_CODE_COUNT = 0;

	/**
	 * A constant with a fixed name that can be used as the base value for
	 * additional hand written constants in a derived class. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected static final int DIAGNOSTIC_CODE_COUNT = GENERATED_DIAGNOSTIC_CODE_COUNT;

	/**
	 * The cached base package validator. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @generated
	 */
	protected XMLTypeValidator xmlTypeValidator;

	/**
	 * Creates an instance of the switch. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @generated
	 */
	public ModelValidator() {
		super();
		xmlTypeValidator = XMLTypeValidator.INSTANCE;
	}

	/**
	 * Returns the package of this validator switch. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	protected EPackage getEPackage() {
		return ModelPackage.eINSTANCE;
	}

	/**
	 * Calls <code>validateXXX</code> for the corresponding classifier of the model.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	protected boolean validate(int classifierID, Object value, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		switch (classifierID) {
		case ModelPackage.CHART:
			return validateChart((Chart) value, diagnostics, context);
		case ModelPackage.CHART_WITH_AXES:
			return validateChartWithAxes((ChartWithAxes) value, diagnostics, context);
		case ModelPackage.CHART_WITHOUT_AXES:
			return validateChartWithoutAxes((ChartWithoutAxes) value, diagnostics, context);
		case ModelPackage.DIAL_CHART:
			return validateDialChart((DialChart) value, diagnostics, context);
		case ModelPackage.COVERAGE_TYPE:
			return validateCoverageType((Double) value, diagnostics, context);
		case ModelPackage.COVERAGE_TYPE_OBJECT:
			return validateCoverageTypeObject((Double) value, diagnostics, context);
		default:
			return true;
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public boolean validateChart(Chart chart, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) chart, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public boolean validateChartWithAxes(ChartWithAxes chartWithAxes, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) chartWithAxes, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public boolean validateChartWithoutAxes(ChartWithoutAxes chartWithoutAxes, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) chartWithoutAxes, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public boolean validateDialChart(DialChart dialChart, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint((EObject) dialChart, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public boolean validateCoverageType(double coverageType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = validateCoverageType_Min(coverageType, diagnostics, context);
		if (result || diagnostics != null) {
			result &= validateCoverageType_Max(coverageType, diagnostics, context);
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @see #validateCoverageType_Min
	 */
	public static final double COVERAGE_TYPE__MIN__VALUE = 0.0;

	/**
	 * Validates the Min constraint of '<em>Coverage Type</em>'. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public boolean validateCoverageType_Min(double coverageType, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		boolean result = coverageType >= COVERAGE_TYPE__MIN__VALUE;
		if (!result && diagnostics != null) {
			reportMinViolation(ModelPackage.Literals.COVERAGE_TYPE, coverageType, COVERAGE_TYPE__MIN__VALUE, true,
					diagnostics, context);
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @see #validateCoverageType_Max
	 */
	public static final double COVERAGE_TYPE__MAX__VALUE = 1.0;

	/**
	 * Validates the Max constraint of '<em>Coverage Type</em>'. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public boolean validateCoverageType_Max(double coverageType, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		boolean result = coverageType <= COVERAGE_TYPE__MAX__VALUE;
		if (!result && diagnostics != null) {
			reportMaxViolation(ModelPackage.Literals.COVERAGE_TYPE, coverageType, COVERAGE_TYPE__MAX__VALUE, true,
					diagnostics, context);
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public boolean validateCoverageTypeObject(Double coverageTypeObject, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		boolean result = validateCoverageType_Min(coverageTypeObject, diagnostics, context);
		if (result || diagnostics != null) {
			result &= validateCoverageType_Max(coverageTypeObject, diagnostics, context);
		}
		return result;
	}

	/**
	 * Returns the resource locator that will be used to fetch messages for this
	 * validator's diagnostics. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		// TODO
		// Specialize this to return a resource locator for messages specific to this
		// validator.
		// Ensure that you remove @generated or mark it @generated NOT
		return super.getResourceLocator();
	}

} // ModelValidator
