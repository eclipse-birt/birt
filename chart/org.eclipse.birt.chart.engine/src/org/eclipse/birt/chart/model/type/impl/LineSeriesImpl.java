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

import java.util.Collection;

import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.MarkerType;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.attribute.impl.MarkerImpl;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.ScatterSeries;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.model.type.TypeFactory;
import org.eclipse.birt.chart.model.type.TypePackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.ibm.icu.util.StringTokenizer;
import com.ibm.icu.util.ULocale;

/**
 * <!-- begin-user-doc --> An implementation of the model object ' <em><b>Line
 * Series</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.LineSeriesImpl#getMarkers
 * <em>Markers</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.LineSeriesImpl#getMarker
 * <em>Marker</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.LineSeriesImpl#getLineAttributes
 * <em>Line Attributes</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.LineSeriesImpl#isPaletteLineColor
 * <em>Palette Line Color</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.LineSeriesImpl#isCurve
 * <em>Curve</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.LineSeriesImpl#getShadowColor
 * <em>Shadow Color</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.LineSeriesImpl#isConnectMissingValue
 * <em>Connect Missing Value</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LineSeriesImpl extends SeriesImpl implements LineSeries {

	/**
	 * The cached value of the '{@link #getMarkers() <em>Markers</em>}' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getMarkers()
	 * @generated
	 * @ordered
	 */
	protected EList<Marker> markers;

	/**
	 * The cached value of the '{@link #getMarker() <em>Marker</em>}' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getMarker()
	 * @generated
	 * @ordered
	 */
	protected Marker marker;

	/**
	 * The cached value of the '{@link #getLineAttributes() <em>Line
	 * Attributes</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #getLineAttributes()
	 * @generated
	 * @ordered
	 */
	protected LineAttributes lineAttributes;

	/**
	 * The default value of the '{@link #isPaletteLineColor() <em>Palette Line
	 * Color</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isPaletteLineColor()
	 * @generated
	 * @ordered
	 */
	protected static final boolean PALETTE_LINE_COLOR_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isPaletteLineColor() <em>Palette Line
	 * Color</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isPaletteLineColor()
	 * @generated
	 * @ordered
	 */
	protected boolean paletteLineColor = PALETTE_LINE_COLOR_EDEFAULT;

	/**
	 * This is true if the Palette Line Color attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean paletteLineColorESet;

	/**
	 * The default value of the '{@link #isCurve() <em>Curve</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isCurve()
	 * @generated
	 * @ordered
	 */
	protected static final boolean CURVE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isCurve() <em>Curve</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isCurve()
	 * @generated
	 * @ordered
	 */
	protected boolean curve = CURVE_EDEFAULT;

	/**
	 * This is true if the Curve attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean curveESet;

	/**
	 * The cached value of the '{@link #getShadowColor() <em>Shadow Color</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getShadowColor()
	 * @generated
	 * @ordered
	 */
	protected ColorDefinition shadowColor;

	/**
	 * The default value of the '{@link #isConnectMissingValue() <em>Connect Missing
	 * Value</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isConnectMissingValue()
	 * @generated
	 * @ordered
	 */
	protected static final boolean CONNECT_MISSING_VALUE_EDEFAULT = true;

	/**
	 * The cached value of the '{@link #isConnectMissingValue() <em>Connect Missing
	 * Value</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isConnectMissingValue()
	 * @generated
	 * @ordered
	 */
	protected boolean connectMissingValue = CONNECT_MISSING_VALUE_EDEFAULT;

	/**
	 * This is true if the Connect Missing Value attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean connectMissingValueESet;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected LineSeriesImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TypePackage.Literals.LINE_SERIES;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EList<Marker> getMarkers() {
		if (markers == null) {
			markers = new EObjectContainmentEList<>(Marker.class, this, TypePackage.LINE_SERIES__MARKERS);
		}
		return markers;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Marker getMarker() {
		return marker;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetMarker(Marker newMarker, NotificationChain msgs) {
		Marker oldMarker = marker;
		marker = newMarker;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					TypePackage.LINE_SERIES__MARKER, oldMarker, newMarker);
			if (msgs == null) {
				msgs = notification;
			} else {
				msgs.add(notification);
			}
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setMarker(Marker newMarker) {
		if (newMarker != marker) {
			NotificationChain msgs = null;
			if (marker != null) {
				msgs = ((InternalEObject) marker).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - TypePackage.LINE_SERIES__MARKER, null, msgs);
			}
			if (newMarker != null) {
				msgs = ((InternalEObject) newMarker).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - TypePackage.LINE_SERIES__MARKER, null, msgs);
			}
			msgs = basicSetMarker(newMarker, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.LINE_SERIES__MARKER, newMarker,
					newMarker));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public LineAttributes getLineAttributes() {
		return lineAttributes;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetLineAttributes(LineAttributes newLineAttributes, NotificationChain msgs) {
		LineAttributes oldLineAttributes = lineAttributes;
		lineAttributes = newLineAttributes;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					TypePackage.LINE_SERIES__LINE_ATTRIBUTES, oldLineAttributes, newLineAttributes);
			if (msgs == null) {
				msgs = notification;
			} else {
				msgs.add(notification);
			}
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setLineAttributes(LineAttributes newLineAttributes) {
		if (newLineAttributes != lineAttributes) {
			NotificationChain msgs = null;
			if (lineAttributes != null) {
				msgs = ((InternalEObject) lineAttributes).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - TypePackage.LINE_SERIES__LINE_ATTRIBUTES, null, msgs);
			}
			if (newLineAttributes != null) {
				msgs = ((InternalEObject) newLineAttributes).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - TypePackage.LINE_SERIES__LINE_ATTRIBUTES, null, msgs);
			}
			msgs = basicSetLineAttributes(newLineAttributes, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.LINE_SERIES__LINE_ATTRIBUTES,
					newLineAttributes, newLineAttributes));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isPaletteLineColor() {
		return paletteLineColor;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setPaletteLineColor(boolean newPaletteLineColor) {
		boolean oldPaletteLineColor = paletteLineColor;
		paletteLineColor = newPaletteLineColor;
		boolean oldPaletteLineColorESet = paletteLineColorESet;
		paletteLineColorESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.LINE_SERIES__PALETTE_LINE_COLOR,
					oldPaletteLineColor, paletteLineColor, !oldPaletteLineColorESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetPaletteLineColor() {
		boolean oldPaletteLineColor = paletteLineColor;
		boolean oldPaletteLineColorESet = paletteLineColorESet;
		paletteLineColor = PALETTE_LINE_COLOR_EDEFAULT;
		paletteLineColorESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, TypePackage.LINE_SERIES__PALETTE_LINE_COLOR,
					oldPaletteLineColor, PALETTE_LINE_COLOR_EDEFAULT, oldPaletteLineColorESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetPaletteLineColor() {
		return paletteLineColorESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isCurve() {
		return curve;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setCurve(boolean newCurve) {
		boolean oldCurve = curve;
		curve = newCurve;
		boolean oldCurveESet = curveESet;
		curveESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.LINE_SERIES__CURVE, oldCurve, curve,
					!oldCurveESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetCurve() {
		boolean oldCurve = curve;
		boolean oldCurveESet = curveESet;
		curve = CURVE_EDEFAULT;
		curveESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, TypePackage.LINE_SERIES__CURVE, oldCurve,
					CURVE_EDEFAULT, oldCurveESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetCurve() {
		return curveESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public ColorDefinition getShadowColor() {
		return shadowColor;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetShadowColor(ColorDefinition newShadowColor, NotificationChain msgs) {
		ColorDefinition oldShadowColor = shadowColor;
		shadowColor = newShadowColor;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					TypePackage.LINE_SERIES__SHADOW_COLOR, oldShadowColor, newShadowColor);
			if (msgs == null) {
				msgs = notification;
			} else {
				msgs.add(notification);
			}
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setShadowColor(ColorDefinition newShadowColor) {
		if (newShadowColor != shadowColor) {
			NotificationChain msgs = null;
			if (shadowColor != null) {
				msgs = ((InternalEObject) shadowColor).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - TypePackage.LINE_SERIES__SHADOW_COLOR, null, msgs);
			}
			if (newShadowColor != null) {
				msgs = ((InternalEObject) newShadowColor).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - TypePackage.LINE_SERIES__SHADOW_COLOR, null, msgs);
			}
			msgs = basicSetShadowColor(newShadowColor, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.LINE_SERIES__SHADOW_COLOR, newShadowColor,
					newShadowColor));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isConnectMissingValue() {
		return connectMissingValue;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setConnectMissingValue(boolean newConnectMissingValue) {
		boolean oldConnectMissingValue = connectMissingValue;
		connectMissingValue = newConnectMissingValue;
		boolean oldConnectMissingValueESet = connectMissingValueESet;
		connectMissingValueESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.LINE_SERIES__CONNECT_MISSING_VALUE,
					oldConnectMissingValue, connectMissingValue, !oldConnectMissingValueESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetConnectMissingValue() {
		boolean oldConnectMissingValue = connectMissingValue;
		boolean oldConnectMissingValueESet = connectMissingValueESet;
		connectMissingValue = CONNECT_MISSING_VALUE_EDEFAULT;
		connectMissingValueESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, TypePackage.LINE_SERIES__CONNECT_MISSING_VALUE,
					oldConnectMissingValue, CONNECT_MISSING_VALUE_EDEFAULT, oldConnectMissingValueESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetConnectMissingValue() {
		return connectMissingValueESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case TypePackage.LINE_SERIES__MARKERS:
			return ((InternalEList<?>) getMarkers()).basicRemove(otherEnd, msgs);
		case TypePackage.LINE_SERIES__MARKER:
			return basicSetMarker(null, msgs);
		case TypePackage.LINE_SERIES__LINE_ATTRIBUTES:
			return basicSetLineAttributes(null, msgs);
		case TypePackage.LINE_SERIES__SHADOW_COLOR:
			return basicSetShadowColor(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case TypePackage.LINE_SERIES__MARKERS:
			return getMarkers();
		case TypePackage.LINE_SERIES__MARKER:
			return getMarker();
		case TypePackage.LINE_SERIES__LINE_ATTRIBUTES:
			return getLineAttributes();
		case TypePackage.LINE_SERIES__PALETTE_LINE_COLOR:
			return isPaletteLineColor();
		case TypePackage.LINE_SERIES__CURVE:
			return isCurve();
		case TypePackage.LINE_SERIES__SHADOW_COLOR:
			return getShadowColor();
		case TypePackage.LINE_SERIES__CONNECT_MISSING_VALUE:
			return isConnectMissingValue();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case TypePackage.LINE_SERIES__MARKERS:
			getMarkers().clear();
			getMarkers().addAll((Collection<? extends Marker>) newValue);
			return;
		case TypePackage.LINE_SERIES__MARKER:
			setMarker((Marker) newValue);
			return;
		case TypePackage.LINE_SERIES__LINE_ATTRIBUTES:
			setLineAttributes((LineAttributes) newValue);
			return;
		case TypePackage.LINE_SERIES__PALETTE_LINE_COLOR:
			setPaletteLineColor((Boolean) newValue);
			return;
		case TypePackage.LINE_SERIES__CURVE:
			setCurve((Boolean) newValue);
			return;
		case TypePackage.LINE_SERIES__SHADOW_COLOR:
			setShadowColor((ColorDefinition) newValue);
			return;
		case TypePackage.LINE_SERIES__CONNECT_MISSING_VALUE:
			setConnectMissingValue((Boolean) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case TypePackage.LINE_SERIES__MARKERS:
			getMarkers().clear();
			return;
		case TypePackage.LINE_SERIES__MARKER:
			setMarker((Marker) null);
			return;
		case TypePackage.LINE_SERIES__LINE_ATTRIBUTES:
			setLineAttributes((LineAttributes) null);
			return;
		case TypePackage.LINE_SERIES__PALETTE_LINE_COLOR:
			unsetPaletteLineColor();
			return;
		case TypePackage.LINE_SERIES__CURVE:
			unsetCurve();
			return;
		case TypePackage.LINE_SERIES__SHADOW_COLOR:
			setShadowColor((ColorDefinition) null);
			return;
		case TypePackage.LINE_SERIES__CONNECT_MISSING_VALUE:
			unsetConnectMissingValue();
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case TypePackage.LINE_SERIES__MARKERS:
			return markers != null && !markers.isEmpty();
		case TypePackage.LINE_SERIES__MARKER:
			return marker != null;
		case TypePackage.LINE_SERIES__LINE_ATTRIBUTES:
			return lineAttributes != null;
		case TypePackage.LINE_SERIES__PALETTE_LINE_COLOR:
			return isSetPaletteLineColor();
		case TypePackage.LINE_SERIES__CURVE:
			return isSetCurve();
		case TypePackage.LINE_SERIES__SHADOW_COLOR:
			return shadowColor != null;
		case TypePackage.LINE_SERIES__CONNECT_MISSING_VALUE:
			return isSetConnectMissingValue();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) {
			return super.toString();
		}

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (paletteLineColor: "); //$NON-NLS-1$
		if (paletteLineColorESet) {
			result.append(paletteLineColor);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(", curve: "); //$NON-NLS-1$
		if (curveESet) {
			result.append(curve);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(", connectMissingValue: "); //$NON-NLS-1$
		if (connectMissingValueESet) {
			result.append(connectMissingValue);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(')');
		return result.toString();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.model.component.Series#canParticipateInCombination()
	 */
	@Override
	public boolean canParticipateInCombination() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.model.component.Series#translateFrom(org.eclipse.birt.
	 * chart.model.component.Series, int, org.eclipse.birt.chart.model.Chart)
	 */
	@Override
	public void translateFrom(Series series, int iSeriesDefinitionIndex, Chart chart) {
		if (series instanceof ScatterSeries && ((ScatterSeries) series).getMarkers().size() > 0) {
			getMarkers().addAll(((ScatterSeries) series).getMarkers());
		}

		// Copy generic series properties
		this.setLabel(series.getLabel());
		if (series.isSetLabelPosition()) {
			if (series.getLabelPosition().equals(Position.INSIDE_LITERAL)
					|| series.getLabelPosition().equals(Position.OUTSIDE_LITERAL)) {
				this.setLabelPosition(Position.ABOVE_LITERAL);
			} else {
				this.setLabelPosition(series.getLabelPosition());
			}
		}

		if (series.isSetVisible()) {
			this.setVisible(series.isVisible());
		}
		if (series.isSetStacked()) {
			this.setStacked(series.isStacked());
		}
		if (series.eIsSet(ComponentPackage.eINSTANCE.getSeries_Triggers())) {
			this.getTriggers().addAll(series.getTriggers());
		}
		if (series.eIsSet(ComponentPackage.eINSTANCE.getSeries_DataPoint())) {
			this.setDataPoint(series.getDataPoint());
		}
		if (series.eIsSet(ComponentPackage.eINSTANCE.getSeries_DataDefinition())) {
			this.getDataDefinition().add(series.getDataDefinition().get(0));
		}

		// Copy series specific properties
		if (series instanceof StockSeries) {
			this.getLineAttributes().setColor(((StockSeries) series).getLineAttributes().getColor());
		}

		// Update the base axis to type text if it isn't already
		if (!(chart instanceof ChartWithAxes)) {
			throw new IllegalArgumentException(Messages.getString("error.invalid.argument.for.lineSeries", //$NON-NLS-1$
					new Object[] { chart.getClass().getName() }, ULocale.getDefault()));
		}

		// Update the sampledata in the model
		chart.setSampleData(getConvertedSampleData(chart.getSampleData(), iSeriesDefinitionIndex));
	}

	private SampleData getConvertedSampleData(SampleData currentSampleData, int iSeriesDefinitionIndex) {
		// Do NOT convert the base sample data since the base axis is not being
		// changed

		// Convert orthogonal sample data
		EList<OrthogonalSampleData> osdList = currentSampleData.getOrthogonalSampleData();
		for (int i = 0; i < osdList.size(); i++) {
			if (i == iSeriesDefinitionIndex) {
				OrthogonalSampleData osd = osdList.get(i);
				osd.setDataSetRepresentation(
						getConvertedOrthogonalSampleDataRepresentation(osd.getDataSetRepresentation()));
				currentSampleData.getOrthogonalSampleData().set(i, osd);
			}
		}
		return currentSampleData;
	}

	private String getConvertedOrthogonalSampleDataRepresentation(String sOldRepresentation) {
		StringTokenizer strtok = new StringTokenizer(sOldRepresentation, ","); //$NON-NLS-1$
		StringBuilder sbNewRepresentation = new StringBuilder(""); //$NON-NLS-1$
		while (strtok.hasMoreTokens()) {
			String sElement = strtok.nextToken().trim();
			if (sElement.startsWith("H")) //$NON-NLS-1$ // Orthogonal sample data is for a
			// stock chart (Orthogonal sample
			// data CANNOT
			// be text
			{
				StringTokenizer strStockTokenizer = new StringTokenizer(sElement);
				sbNewRepresentation.append(strStockTokenizer.nextToken().trim().substring(1));
			} else {
				sbNewRepresentation.append(sElement);
			}
			sbNewRepresentation.append(","); //$NON-NLS-1$
		}
		return sbNewRepresentation.toString().substring(0, sbNewRepresentation.length() - 1);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.model.component.Series#canShareAxisUnit()
	 */
	@Override
	public final boolean canShareAxisUnit() {
		return true;
	}

	/**
	 * A convenience method to create an initialized 'Series' instance
	 *
	 * @return line series instance with setting 'isSet' flag.
	 */
	public static Series create() // SUBCLASSED BY ScatterSeriesImpl
	{
		final LineSeries ls = TypeFactory.eINSTANCE.createLineSeries();
		((LineSeriesImpl) ls).initialize();
		return ls;
	}

	/**
	 * Initializes all member variables within this object recursively
	 *
	 * Note: Manually written
	 */
	@Override
	protected void initialize() // SUBCLASSED BY ScatterSeriesImpl
	{
		super.initialize();

		final LineAttributes lia = AttributeFactory.eINSTANCE.createLineAttributes();
		((LineAttributesImpl) lia).set(ColorDefinitionImpl.BLACK(), LineStyle.SOLID_LITERAL, 1);
		lia.setVisible(true);
		setLineAttributes(lia);
		setLabelPosition(Position.ABOVE_LITERAL);

		final Marker m = AttributeFactory.eINSTANCE.createMarker();
		m.setType(MarkerType.BOX_LITERAL);
		m.setSize(4);
		m.setVisible(true);
		LineAttributes la = AttributeFactory.eINSTANCE.createLineAttributes();
		la.setVisible(true);
		m.setOutline(la);
		getMarkers().add(m);
		setPaletteLineColor(true);
	}

	/**
	 * A convenience method to create an initialized 'Series' instance
	 *
	 * @return line series instance without setting 'isSet' flag.
	 */
	public static Series createDefault() // SUBCLASSED BY ScatterSeriesImpl
	{
		final LineSeries ls = TypeFactory.eINSTANCE.createLineSeries();
		((LineSeriesImpl) ls).initDefault();
		return ls;
	}

	/**
	 * Initializes all member variables within this object recursively
	 *
	 * Note: Manually written
	 */
	@Override
	protected void initDefault() // SUBCLASSED BY ScatterSeriesImpl
	{
		super.initDefault();

		final LineAttributes lia = LineAttributesImpl.createDefault(null, LineStyle.SOLID_LITERAL, 1, true);
		setLineAttributes(lia);
		labelPosition = Position.ABOVE_LITERAL;

		final Marker m = MarkerImpl.createDefault(MarkerType.BOX_LITERAL, 4, true);
		LineAttributes la = LineAttributesImpl.createDefault(true);
		m.setOutline(la);
		getMarkers().add(m);

		paletteLineColor = true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.model.component.Series#canBeStacked()
	 */
	@Override
	public boolean canBeStacked() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.model.component.Series#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		return Messages.getString("LineSeriesImpl.displayName"); //$NON-NLS-1$
	}

	/**
	 * @generated
	 */
	@Override
	public LineSeries copyInstance() {
		LineSeriesImpl dest = new LineSeriesImpl();
		dest.set(this);
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set(LineSeries src) {

		super.set(src);

		// children

		if (src.getMarkers() != null) {
			EList<Marker> list = getMarkers();
			for (Marker element : src.getMarkers()) {
				list.add(element.copyInstance());
			}
		}

		if (src.getMarker() != null) {
			setMarker(src.getMarker().copyInstance());
		}

		if (src.getLineAttributes() != null) {
			setLineAttributes(src.getLineAttributes().copyInstance());
		}

		if (src.getShadowColor() != null) {
			setShadowColor(src.getShadowColor().copyInstance());
		}

		// attributes

		paletteLineColor = src.isPaletteLineColor();

		paletteLineColorESet = src.isSetPaletteLineColor();

		curve = src.isCurve();

		curveESet = src.isSetCurve();

		connectMissingValue = src.isConnectMissingValue();

		connectMissingValueESet = src.isSetConnectMissingValue();

	}

} // LineSeriesImpl
