/***********************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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

package org.eclipse.birt.chart.examples.radar.model.type.impl;

import java.math.BigInteger;

import org.eclipse.birt.chart.examples.radar.i18n.Messages;
import org.eclipse.birt.chart.examples.radar.model.type.RadarSeries;
import org.eclipse.birt.chart.examples.radar.model.type.RadarTypePackage;
import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.MarkerType;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.attribute.impl.MarkerImpl;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.impl.LabelImpl;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.chart.util.NameSet;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object ' <em><b>Radar
 * Series</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>
 * {@link org.eclipse.birt.chart.examples.radar.model.type.impl.RadarSeriesImpl#getMarker
 * <em>Marker</em>}</li>
 * <li>
 * {@link org.eclipse.birt.chart.examples.radar.model.type.impl.RadarSeriesImpl#getLineAttributes
 * <em>Line Attributes</em>}</li>
 * <li>
 * {@link org.eclipse.birt.chart.examples.radar.model.type.impl.RadarSeriesImpl#isPaletteLineColor
 * <em>Palette Line Color</em>}</li>
 * <li>
 * {@link org.eclipse.birt.chart.examples.radar.model.type.impl.RadarSeriesImpl#isBackgroundOvalTransparent
 * <em>Background Oval Transparent</em>}</li>
 * <li>
 * {@link org.eclipse.birt.chart.examples.radar.model.type.impl.RadarSeriesImpl#getWebLineAttributes
 * <em>Web Line Attributes</em>}</li>
 * <li>
 * {@link org.eclipse.birt.chart.examples.radar.model.type.impl.RadarSeriesImpl#isShowWebLabels
 * <em>Show Web Labels</em>}</li>
 * <li>
 * {@link org.eclipse.birt.chart.examples.radar.model.type.impl.RadarSeriesImpl#isShowCatLabels
 * <em>Show Cat Labels</em>}</li>
 * <li>
 * {@link org.eclipse.birt.chart.examples.radar.model.type.impl.RadarSeriesImpl#isRadarAutoScale
 * <em>Radar Auto Scale</em>}</li>
 * <li>
 * {@link org.eclipse.birt.chart.examples.radar.model.type.impl.RadarSeriesImpl#getWebLabelMax
 * <em>Web Label Max</em>}</li>
 * <li>
 * {@link org.eclipse.birt.chart.examples.radar.model.type.impl.RadarSeriesImpl#getWebLabelMin
 * <em>Web Label Min</em>}</li>
 * <li>
 * {@link org.eclipse.birt.chart.examples.radar.model.type.impl.RadarSeriesImpl#getWebLabelUnit
 * <em>Web Label Unit</em>}</li>
 * <li>
 * {@link org.eclipse.birt.chart.examples.radar.model.type.impl.RadarSeriesImpl#isFillPolys
 * <em>Fill Polys</em>}</li>
 * <li>
 * {@link org.eclipse.birt.chart.examples.radar.model.type.impl.RadarSeriesImpl#isConnectEndpoints
 * <em>Connect Endpoints</em>}</li>
 * <li>
 * {@link org.eclipse.birt.chart.examples.radar.model.type.impl.RadarSeriesImpl#getWebLabel
 * <em>Web Label</em>}</li>
 * <li>
 * {@link org.eclipse.birt.chart.examples.radar.model.type.impl.RadarSeriesImpl#getCatLabel
 * <em>Cat Label</em>}</li>
 * <li>
 * {@link org.eclipse.birt.chart.examples.radar.model.type.impl.RadarSeriesImpl#getWebLabelFormatSpecifier
 * <em>Web Label Format Specifier</em>}</li>
 * <li>
 * {@link org.eclipse.birt.chart.examples.radar.model.type.impl.RadarSeriesImpl#getCatLabelFormatSpecifier
 * <em>Cat Label Format Specifier</em>}</li>
 * <li>
 * {@link org.eclipse.birt.chart.examples.radar.model.type.impl.RadarSeriesImpl#getPlotSteps
 * <em>Plot Steps</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RadarSeriesImpl extends SeriesImpl implements RadarSeries {

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
	 * The default value of the '{@link #isBackgroundOvalTransparent()
	 * <em>Background Oval Transparent</em>}' attribute. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #isBackgroundOvalTransparent()
	 * @generated
	 * @ordered
	 */
	protected static final boolean BACKGROUND_OVAL_TRANSPARENT_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isBackgroundOvalTransparent() <em>Background
	 * Oval Transparent</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @see #isBackgroundOvalTransparent()
	 * @generated
	 * @ordered
	 */
	protected boolean backgroundOvalTransparent = BACKGROUND_OVAL_TRANSPARENT_EDEFAULT;

	/**
	 * This is true if the Background Oval Transparent attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean backgroundOvalTransparentESet;

	/**
	 * The cached value of the '{@link #getWebLineAttributes() <em>Web Line
	 * Attributes</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #getWebLineAttributes()
	 * @generated
	 * @ordered
	 */
	protected LineAttributes webLineAttributes;

	/**
	 * The default value of the '{@link #isShowWebLabels() <em>Show Web
	 * Labels</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isShowWebLabels()
	 * @generated
	 * @ordered
	 */
	protected static final boolean SHOW_WEB_LABELS_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isShowWebLabels() <em>Show Web Labels</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isShowWebLabels()
	 * @generated
	 * @ordered
	 */
	protected boolean showWebLabels = SHOW_WEB_LABELS_EDEFAULT;

	/**
	 * This is true if the Show Web Labels attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean showWebLabelsESet;

	/**
	 * The default value of the '{@link #isShowCatLabels() <em>Show Cat
	 * Labels</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isShowCatLabels()
	 * @generated
	 * @ordered
	 */
	protected static final boolean SHOW_CAT_LABELS_EDEFAULT = true;

	/**
	 * The cached value of the '{@link #isShowCatLabels() <em>Show Cat Labels</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isShowCatLabels()
	 * @generated
	 * @ordered
	 */
	protected boolean showCatLabels = SHOW_CAT_LABELS_EDEFAULT;

	/**
	 * This is true if the Show Cat Labels attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean showCatLabelsESet;

	/**
	 * The default value of the '{@link #isRadarAutoScale() <em>Radar Auto
	 * Scale</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isRadarAutoScale()
	 * @generated
	 * @ordered
	 */
	protected static final boolean RADAR_AUTO_SCALE_EDEFAULT = true;

	/**
	 * The cached value of the '{@link #isRadarAutoScale() <em>Radar Auto
	 * Scale</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isRadarAutoScale()
	 * @generated
	 * @ordered
	 */
	protected boolean radarAutoScale = RADAR_AUTO_SCALE_EDEFAULT;

	/**
	 * This is true if the Radar Auto Scale attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean radarAutoScaleESet;

	/**
	 * The default value of the '{@link #getWebLabelMax() <em>Web Label Max</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getWebLabelMax()
	 * @generated
	 * @ordered
	 */
	protected static final double WEB_LABEL_MAX_EDEFAULT = 100.0;

	/**
	 * The cached value of the '{@link #getWebLabelMax() <em>Web Label Max</em>} '
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getWebLabelMax()
	 * @generated
	 * @ordered
	 */
	protected double webLabelMax = WEB_LABEL_MAX_EDEFAULT;

	/**
	 * This is true if the Web Label Max attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean webLabelMaxESet;

	/**
	 * The default value of the '{@link #getWebLabelMin() <em>Web Label Min</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getWebLabelMin()
	 * @generated
	 * @ordered
	 */
	protected static final double WEB_LABEL_MIN_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getWebLabelMin() <em>Web Label Min</em>} '
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getWebLabelMin()
	 * @generated
	 * @ordered
	 */
	protected double webLabelMin = WEB_LABEL_MIN_EDEFAULT;

	/**
	 * This is true if the Web Label Min attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean webLabelMinESet;

	/**
	 * The default value of the '{@link #getWebLabelUnit() <em>Web Label Unit</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getWebLabelUnit()
	 * @generated
	 * @ordered
	 */
	protected static final String WEB_LABEL_UNIT_EDEFAULT = "%"; //$NON-NLS-1$

	/**
	 * The cached value of the '{@link #getWebLabelUnit() <em>Web Label Unit</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getWebLabelUnit()
	 * @generated
	 * @ordered
	 */
	protected String webLabelUnit = WEB_LABEL_UNIT_EDEFAULT;

	/**
	 * This is true if the Web Label Unit attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean webLabelUnitESet;

	/**
	 * The default value of the '{@link #isFillPolys() <em>Fill Polys</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isFillPolys()
	 * @generated
	 * @ordered
	 */
	protected static final boolean FILL_POLYS_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isFillPolys() <em>Fill Polys</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isFillPolys()
	 * @generated
	 * @ordered
	 */
	protected boolean fillPolys = FILL_POLYS_EDEFAULT;

	/**
	 * This is true if the Fill Polys attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean fillPolysESet;

	/**
	 * The default value of the '{@link #isConnectEndpoints() <em>Connect
	 * Endpoints</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isConnectEndpoints()
	 * @generated
	 * @ordered
	 */
	protected static final boolean CONNECT_ENDPOINTS_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isConnectEndpoints() <em>Connect
	 * Endpoints</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isConnectEndpoints()
	 * @generated
	 * @ordered
	 */
	protected boolean connectEndpoints = CONNECT_ENDPOINTS_EDEFAULT;

	/**
	 * This is true if the Connect Endpoints attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean connectEndpointsESet;

	/**
	 * The cached value of the '{@link #getWebLabel() <em>Web Label</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getWebLabel()
	 * @generated
	 * @ordered
	 */
	protected Label webLabel;

	/**
	 * The cached value of the '{@link #getCatLabel() <em>Cat Label</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getCatLabel()
	 * @generated
	 * @ordered
	 */
	protected Label catLabel;

	/**
	 * The cached value of the '{@link #getWebLabelFormatSpecifier() <em>Web Label
	 * Format Specifier</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #getWebLabelFormatSpecifier()
	 * @generated
	 * @ordered
	 */
	protected FormatSpecifier webLabelFormatSpecifier;

	/**
	 * The cached value of the '{@link #getCatLabelFormatSpecifier() <em>Cat Label
	 * Format Specifier</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #getCatLabelFormatSpecifier()
	 * @generated
	 * @ordered
	 */
	protected FormatSpecifier catLabelFormatSpecifier;

	/**
	 * The default value of the '{@link #getPlotSteps() <em>Plot Steps</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getPlotSteps()
	 * @generated
	 * @ordered
	 */
	protected static final BigInteger PLOT_STEPS_EDEFAULT = new BigInteger("5"); //$NON-NLS-1$

	/**
	 * The cached value of the '{@link #getPlotSteps() <em>Plot Steps</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getPlotSteps()
	 * @generated
	 * @ordered
	 */
	protected BigInteger plotSteps = PLOT_STEPS_EDEFAULT;

	/**
	 * This is true if the Plot Steps attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean plotStepsESet;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected RadarSeriesImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return RadarTypePackage.Literals.RADAR_SERIES;
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
					RadarTypePackage.RADAR_SERIES__MARKER, oldMarker, newMarker);
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
						EOPPOSITE_FEATURE_BASE - RadarTypePackage.RADAR_SERIES__MARKER, null, msgs);
			}
			if (newMarker != null) {
				msgs = ((InternalEObject) newMarker).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - RadarTypePackage.RADAR_SERIES__MARKER, null, msgs);
			}
			msgs = basicSetMarker(newMarker, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, RadarTypePackage.RADAR_SERIES__MARKER, newMarker,
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
					RadarTypePackage.RADAR_SERIES__LINE_ATTRIBUTES, oldLineAttributes, newLineAttributes);
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
						EOPPOSITE_FEATURE_BASE - RadarTypePackage.RADAR_SERIES__LINE_ATTRIBUTES, null, msgs);
			}
			if (newLineAttributes != null) {
				msgs = ((InternalEObject) newLineAttributes).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - RadarTypePackage.RADAR_SERIES__LINE_ATTRIBUTES, null, msgs);
			}
			msgs = basicSetLineAttributes(newLineAttributes, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, RadarTypePackage.RADAR_SERIES__LINE_ATTRIBUTES,
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
			eNotify(new ENotificationImpl(this, Notification.SET, RadarTypePackage.RADAR_SERIES__PALETTE_LINE_COLOR,
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
			eNotify(new ENotificationImpl(this, Notification.UNSET, RadarTypePackage.RADAR_SERIES__PALETTE_LINE_COLOR,
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
	public boolean isBackgroundOvalTransparent() {
		return backgroundOvalTransparent;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setBackgroundOvalTransparent(boolean newBackgroundOvalTransparent) {
		boolean oldBackgroundOvalTransparent = backgroundOvalTransparent;
		backgroundOvalTransparent = newBackgroundOvalTransparent;
		boolean oldBackgroundOvalTransparentESet = backgroundOvalTransparentESet;
		backgroundOvalTransparentESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET,
					RadarTypePackage.RADAR_SERIES__BACKGROUND_OVAL_TRANSPARENT, oldBackgroundOvalTransparent,
					backgroundOvalTransparent, !oldBackgroundOvalTransparentESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetBackgroundOvalTransparent() {
		boolean oldBackgroundOvalTransparent = backgroundOvalTransparent;
		boolean oldBackgroundOvalTransparentESet = backgroundOvalTransparentESet;
		backgroundOvalTransparent = BACKGROUND_OVAL_TRANSPARENT_EDEFAULT;
		backgroundOvalTransparentESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET,
					RadarTypePackage.RADAR_SERIES__BACKGROUND_OVAL_TRANSPARENT, oldBackgroundOvalTransparent,
					BACKGROUND_OVAL_TRANSPARENT_EDEFAULT, oldBackgroundOvalTransparentESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetBackgroundOvalTransparent() {
		return backgroundOvalTransparentESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public LineAttributes getWebLineAttributes() {
		return webLineAttributes;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetWebLineAttributes(LineAttributes newWebLineAttributes, NotificationChain msgs) {
		LineAttributes oldWebLineAttributes = webLineAttributes;
		webLineAttributes = newWebLineAttributes;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					RadarTypePackage.RADAR_SERIES__WEB_LINE_ATTRIBUTES, oldWebLineAttributes, newWebLineAttributes);
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
	public void setWebLineAttributes(LineAttributes newWebLineAttributes) {
		if (newWebLineAttributes != webLineAttributes) {
			NotificationChain msgs = null;
			if (webLineAttributes != null) {
				msgs = ((InternalEObject) webLineAttributes).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - RadarTypePackage.RADAR_SERIES__WEB_LINE_ATTRIBUTES, null, msgs);
			}
			if (newWebLineAttributes != null) {
				msgs = ((InternalEObject) newWebLineAttributes).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - RadarTypePackage.RADAR_SERIES__WEB_LINE_ATTRIBUTES, null, msgs);
			}
			msgs = basicSetWebLineAttributes(newWebLineAttributes, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, RadarTypePackage.RADAR_SERIES__WEB_LINE_ATTRIBUTES,
					newWebLineAttributes, newWebLineAttributes));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isShowWebLabels() {
		return showWebLabels;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setShowWebLabels(boolean newShowWebLabels) {
		boolean oldShowWebLabels = showWebLabels;
		showWebLabels = newShowWebLabels;
		boolean oldShowWebLabelsESet = showWebLabelsESet;
		showWebLabelsESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, RadarTypePackage.RADAR_SERIES__SHOW_WEB_LABELS,
					oldShowWebLabels, showWebLabels, !oldShowWebLabelsESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetShowWebLabels() {
		boolean oldShowWebLabels = showWebLabels;
		boolean oldShowWebLabelsESet = showWebLabelsESet;
		showWebLabels = SHOW_WEB_LABELS_EDEFAULT;
		showWebLabelsESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, RadarTypePackage.RADAR_SERIES__SHOW_WEB_LABELS,
					oldShowWebLabels, SHOW_WEB_LABELS_EDEFAULT, oldShowWebLabelsESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetShowWebLabels() {
		return showWebLabelsESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isShowCatLabels() {
		return showCatLabels;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setShowCatLabels(boolean newShowCatLabels) {
		boolean oldShowCatLabels = showCatLabels;
		showCatLabels = newShowCatLabels;
		boolean oldShowCatLabelsESet = showCatLabelsESet;
		showCatLabelsESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, RadarTypePackage.RADAR_SERIES__SHOW_CAT_LABELS,
					oldShowCatLabels, showCatLabels, !oldShowCatLabelsESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetShowCatLabels() {
		boolean oldShowCatLabels = showCatLabels;
		boolean oldShowCatLabelsESet = showCatLabelsESet;
		showCatLabels = SHOW_CAT_LABELS_EDEFAULT;
		showCatLabelsESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, RadarTypePackage.RADAR_SERIES__SHOW_CAT_LABELS,
					oldShowCatLabels, SHOW_CAT_LABELS_EDEFAULT, oldShowCatLabelsESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetShowCatLabels() {
		return showCatLabelsESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isRadarAutoScale() {
		return radarAutoScale;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setRadarAutoScale(boolean newRadarAutoScale) {
		boolean oldRadarAutoScale = radarAutoScale;
		radarAutoScale = newRadarAutoScale;
		boolean oldRadarAutoScaleESet = radarAutoScaleESet;
		radarAutoScaleESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, RadarTypePackage.RADAR_SERIES__RADAR_AUTO_SCALE,
					oldRadarAutoScale, radarAutoScale, !oldRadarAutoScaleESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetRadarAutoScale() {
		boolean oldRadarAutoScale = radarAutoScale;
		boolean oldRadarAutoScaleESet = radarAutoScaleESet;
		radarAutoScale = RADAR_AUTO_SCALE_EDEFAULT;
		radarAutoScaleESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, RadarTypePackage.RADAR_SERIES__RADAR_AUTO_SCALE,
					oldRadarAutoScale, RADAR_AUTO_SCALE_EDEFAULT, oldRadarAutoScaleESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetRadarAutoScale() {
		return radarAutoScaleESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public double getWebLabelMax() {
		return webLabelMax;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setWebLabelMax(double newWebLabelMax) {
		double oldWebLabelMax = webLabelMax;
		webLabelMax = newWebLabelMax;
		boolean oldWebLabelMaxESet = webLabelMaxESet;
		webLabelMaxESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, RadarTypePackage.RADAR_SERIES__WEB_LABEL_MAX,
					oldWebLabelMax, webLabelMax, !oldWebLabelMaxESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetWebLabelMax() {
		double oldWebLabelMax = webLabelMax;
		boolean oldWebLabelMaxESet = webLabelMaxESet;
		webLabelMax = WEB_LABEL_MAX_EDEFAULT;
		webLabelMaxESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, RadarTypePackage.RADAR_SERIES__WEB_LABEL_MAX,
					oldWebLabelMax, WEB_LABEL_MAX_EDEFAULT, oldWebLabelMaxESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetWebLabelMax() {
		return webLabelMaxESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public double getWebLabelMin() {
		return webLabelMin;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setWebLabelMin(double newWebLabelMin) {
		double oldWebLabelMin = webLabelMin;
		webLabelMin = newWebLabelMin;
		boolean oldWebLabelMinESet = webLabelMinESet;
		webLabelMinESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, RadarTypePackage.RADAR_SERIES__WEB_LABEL_MIN,
					oldWebLabelMin, webLabelMin, !oldWebLabelMinESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetWebLabelMin() {
		double oldWebLabelMin = webLabelMin;
		boolean oldWebLabelMinESet = webLabelMinESet;
		webLabelMin = WEB_LABEL_MIN_EDEFAULT;
		webLabelMinESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, RadarTypePackage.RADAR_SERIES__WEB_LABEL_MIN,
					oldWebLabelMin, WEB_LABEL_MIN_EDEFAULT, oldWebLabelMinESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetWebLabelMin() {
		return webLabelMinESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String getWebLabelUnit() {
		return webLabelUnit;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setWebLabelUnit(String newWebLabelUnit) {
		String oldWebLabelUnit = webLabelUnit;
		webLabelUnit = newWebLabelUnit;
		boolean oldWebLabelUnitESet = webLabelUnitESet;
		webLabelUnitESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, RadarTypePackage.RADAR_SERIES__WEB_LABEL_UNIT,
					oldWebLabelUnit, webLabelUnit, !oldWebLabelUnitESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetWebLabelUnit() {
		String oldWebLabelUnit = webLabelUnit;
		boolean oldWebLabelUnitESet = webLabelUnitESet;
		webLabelUnit = WEB_LABEL_UNIT_EDEFAULT;
		webLabelUnitESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, RadarTypePackage.RADAR_SERIES__WEB_LABEL_UNIT,
					oldWebLabelUnit, WEB_LABEL_UNIT_EDEFAULT, oldWebLabelUnitESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetWebLabelUnit() {
		return webLabelUnitESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isFillPolys() {
		return fillPolys;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setFillPolys(boolean newFillPolys) {
		boolean oldFillPolys = fillPolys;
		fillPolys = newFillPolys;
		boolean oldFillPolysESet = fillPolysESet;
		fillPolysESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, RadarTypePackage.RADAR_SERIES__FILL_POLYS,
					oldFillPolys, fillPolys, !oldFillPolysESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetFillPolys() {
		boolean oldFillPolys = fillPolys;
		boolean oldFillPolysESet = fillPolysESet;
		fillPolys = FILL_POLYS_EDEFAULT;
		fillPolysESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, RadarTypePackage.RADAR_SERIES__FILL_POLYS,
					oldFillPolys, FILL_POLYS_EDEFAULT, oldFillPolysESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetFillPolys() {
		return fillPolysESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isConnectEndpoints() {
		return connectEndpoints;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setConnectEndpoints(boolean newConnectEndpoints) {
		boolean oldConnectEndpoints = connectEndpoints;
		connectEndpoints = newConnectEndpoints;
		boolean oldConnectEndpointsESet = connectEndpointsESet;
		connectEndpointsESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, RadarTypePackage.RADAR_SERIES__CONNECT_ENDPOINTS,
					oldConnectEndpoints, connectEndpoints, !oldConnectEndpointsESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetConnectEndpoints() {
		boolean oldConnectEndpoints = connectEndpoints;
		boolean oldConnectEndpointsESet = connectEndpointsESet;
		connectEndpoints = CONNECT_ENDPOINTS_EDEFAULT;
		connectEndpointsESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, RadarTypePackage.RADAR_SERIES__CONNECT_ENDPOINTS,
					oldConnectEndpoints, CONNECT_ENDPOINTS_EDEFAULT, oldConnectEndpointsESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetConnectEndpoints() {
		return connectEndpointsESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Label getWebLabel() {
		return webLabel;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetWebLabel(Label newWebLabel, NotificationChain msgs) {
		Label oldWebLabel = webLabel;
		webLabel = newWebLabel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					RadarTypePackage.RADAR_SERIES__WEB_LABEL, oldWebLabel, newWebLabel);
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
	public void setWebLabel(Label newWebLabel) {
		if (newWebLabel != webLabel) {
			NotificationChain msgs = null;
			if (webLabel != null) {
				msgs = ((InternalEObject) webLabel).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - RadarTypePackage.RADAR_SERIES__WEB_LABEL, null, msgs);
			}
			if (newWebLabel != null) {
				msgs = ((InternalEObject) newWebLabel).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - RadarTypePackage.RADAR_SERIES__WEB_LABEL, null, msgs);
			}
			msgs = basicSetWebLabel(newWebLabel, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, RadarTypePackage.RADAR_SERIES__WEB_LABEL, newWebLabel,
					newWebLabel));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Label getCatLabel() {
		return catLabel;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetCatLabel(Label newCatLabel, NotificationChain msgs) {
		Label oldCatLabel = catLabel;
		catLabel = newCatLabel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					RadarTypePackage.RADAR_SERIES__CAT_LABEL, oldCatLabel, newCatLabel);
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
	public void setCatLabel(Label newCatLabel) {
		if (newCatLabel != catLabel) {
			NotificationChain msgs = null;
			if (catLabel != null) {
				msgs = ((InternalEObject) catLabel).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - RadarTypePackage.RADAR_SERIES__CAT_LABEL, null, msgs);
			}
			if (newCatLabel != null) {
				msgs = ((InternalEObject) newCatLabel).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - RadarTypePackage.RADAR_SERIES__CAT_LABEL, null, msgs);
			}
			msgs = basicSetCatLabel(newCatLabel, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, RadarTypePackage.RADAR_SERIES__CAT_LABEL, newCatLabel,
					newCatLabel));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public FormatSpecifier getWebLabelFormatSpecifier() {
		return webLabelFormatSpecifier;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetWebLabelFormatSpecifier(FormatSpecifier newWebLabelFormatSpecifier,
			NotificationChain msgs) {
		FormatSpecifier oldWebLabelFormatSpecifier = webLabelFormatSpecifier;
		webLabelFormatSpecifier = newWebLabelFormatSpecifier;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					RadarTypePackage.RADAR_SERIES__WEB_LABEL_FORMAT_SPECIFIER, oldWebLabelFormatSpecifier,
					newWebLabelFormatSpecifier);
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
	public void setWebLabelFormatSpecifier(FormatSpecifier newWebLabelFormatSpecifier) {
		if (newWebLabelFormatSpecifier != webLabelFormatSpecifier) {
			NotificationChain msgs = null;
			if (webLabelFormatSpecifier != null) {
				msgs = ((InternalEObject) webLabelFormatSpecifier).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - RadarTypePackage.RADAR_SERIES__WEB_LABEL_FORMAT_SPECIFIER, null, msgs);
			}
			if (newWebLabelFormatSpecifier != null) {
				msgs = ((InternalEObject) newWebLabelFormatSpecifier).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - RadarTypePackage.RADAR_SERIES__WEB_LABEL_FORMAT_SPECIFIER, null, msgs);
			}
			msgs = basicSetWebLabelFormatSpecifier(newWebLabelFormatSpecifier, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET,
					RadarTypePackage.RADAR_SERIES__WEB_LABEL_FORMAT_SPECIFIER, newWebLabelFormatSpecifier,
					newWebLabelFormatSpecifier));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public FormatSpecifier getCatLabelFormatSpecifier() {
		return catLabelFormatSpecifier;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetCatLabelFormatSpecifier(FormatSpecifier newCatLabelFormatSpecifier,
			NotificationChain msgs) {
		FormatSpecifier oldCatLabelFormatSpecifier = catLabelFormatSpecifier;
		catLabelFormatSpecifier = newCatLabelFormatSpecifier;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					RadarTypePackage.RADAR_SERIES__CAT_LABEL_FORMAT_SPECIFIER, oldCatLabelFormatSpecifier,
					newCatLabelFormatSpecifier);
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
	public void setCatLabelFormatSpecifier(FormatSpecifier newCatLabelFormatSpecifier) {
		if (newCatLabelFormatSpecifier != catLabelFormatSpecifier) {
			NotificationChain msgs = null;
			if (catLabelFormatSpecifier != null) {
				msgs = ((InternalEObject) catLabelFormatSpecifier).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - RadarTypePackage.RADAR_SERIES__CAT_LABEL_FORMAT_SPECIFIER, null, msgs);
			}
			if (newCatLabelFormatSpecifier != null) {
				msgs = ((InternalEObject) newCatLabelFormatSpecifier).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - RadarTypePackage.RADAR_SERIES__CAT_LABEL_FORMAT_SPECIFIER, null, msgs);
			}
			msgs = basicSetCatLabelFormatSpecifier(newCatLabelFormatSpecifier, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET,
					RadarTypePackage.RADAR_SERIES__CAT_LABEL_FORMAT_SPECIFIER, newCatLabelFormatSpecifier,
					newCatLabelFormatSpecifier));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public BigInteger getPlotSteps() {
		return plotSteps;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setPlotSteps(BigInteger newPlotSteps) {
		BigInteger oldPlotSteps = plotSteps;
		plotSteps = newPlotSteps;
		boolean oldPlotStepsESet = plotStepsESet;
		plotStepsESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, RadarTypePackage.RADAR_SERIES__PLOT_STEPS,
					oldPlotSteps, plotSteps, !oldPlotStepsESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetPlotSteps() {
		BigInteger oldPlotSteps = plotSteps;
		boolean oldPlotStepsESet = plotStepsESet;
		plotSteps = PLOT_STEPS_EDEFAULT;
		plotStepsESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, RadarTypePackage.RADAR_SERIES__PLOT_STEPS,
					oldPlotSteps, PLOT_STEPS_EDEFAULT, oldPlotStepsESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetPlotSteps() {
		return plotStepsESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case RadarTypePackage.RADAR_SERIES__MARKER:
			return basicSetMarker(null, msgs);
		case RadarTypePackage.RADAR_SERIES__LINE_ATTRIBUTES:
			return basicSetLineAttributes(null, msgs);
		case RadarTypePackage.RADAR_SERIES__WEB_LINE_ATTRIBUTES:
			return basicSetWebLineAttributes(null, msgs);
		case RadarTypePackage.RADAR_SERIES__WEB_LABEL:
			return basicSetWebLabel(null, msgs);
		case RadarTypePackage.RADAR_SERIES__CAT_LABEL:
			return basicSetCatLabel(null, msgs);
		case RadarTypePackage.RADAR_SERIES__WEB_LABEL_FORMAT_SPECIFIER:
			return basicSetWebLabelFormatSpecifier(null, msgs);
		case RadarTypePackage.RADAR_SERIES__CAT_LABEL_FORMAT_SPECIFIER:
			return basicSetCatLabelFormatSpecifier(null, msgs);
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
		case RadarTypePackage.RADAR_SERIES__MARKER:
			return getMarker();
		case RadarTypePackage.RADAR_SERIES__LINE_ATTRIBUTES:
			return getLineAttributes();
		case RadarTypePackage.RADAR_SERIES__PALETTE_LINE_COLOR:
			return isPaletteLineColor();
		case RadarTypePackage.RADAR_SERIES__BACKGROUND_OVAL_TRANSPARENT:
			return isBackgroundOvalTransparent();
		case RadarTypePackage.RADAR_SERIES__WEB_LINE_ATTRIBUTES:
			return getWebLineAttributes();
		case RadarTypePackage.RADAR_SERIES__SHOW_WEB_LABELS:
			return isShowWebLabels();
		case RadarTypePackage.RADAR_SERIES__SHOW_CAT_LABELS:
			return isShowCatLabels();
		case RadarTypePackage.RADAR_SERIES__RADAR_AUTO_SCALE:
			return isRadarAutoScale();
		case RadarTypePackage.RADAR_SERIES__WEB_LABEL_MAX:
			return getWebLabelMax();
		case RadarTypePackage.RADAR_SERIES__WEB_LABEL_MIN:
			return getWebLabelMin();
		case RadarTypePackage.RADAR_SERIES__WEB_LABEL_UNIT:
			return getWebLabelUnit();
		case RadarTypePackage.RADAR_SERIES__FILL_POLYS:
			return isFillPolys();
		case RadarTypePackage.RADAR_SERIES__CONNECT_ENDPOINTS:
			return isConnectEndpoints();
		case RadarTypePackage.RADAR_SERIES__WEB_LABEL:
			return getWebLabel();
		case RadarTypePackage.RADAR_SERIES__CAT_LABEL:
			return getCatLabel();
		case RadarTypePackage.RADAR_SERIES__WEB_LABEL_FORMAT_SPECIFIER:
			return getWebLabelFormatSpecifier();
		case RadarTypePackage.RADAR_SERIES__CAT_LABEL_FORMAT_SPECIFIER:
			return getCatLabelFormatSpecifier();
		case RadarTypePackage.RADAR_SERIES__PLOT_STEPS:
			return getPlotSteps();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case RadarTypePackage.RADAR_SERIES__MARKER:
			setMarker((Marker) newValue);
			return;
		case RadarTypePackage.RADAR_SERIES__LINE_ATTRIBUTES:
			setLineAttributes((LineAttributes) newValue);
			return;
		case RadarTypePackage.RADAR_SERIES__PALETTE_LINE_COLOR:
			setPaletteLineColor((Boolean) newValue);
			return;
		case RadarTypePackage.RADAR_SERIES__BACKGROUND_OVAL_TRANSPARENT:
			setBackgroundOvalTransparent((Boolean) newValue);
			return;
		case RadarTypePackage.RADAR_SERIES__WEB_LINE_ATTRIBUTES:
			setWebLineAttributes((LineAttributes) newValue);
			return;
		case RadarTypePackage.RADAR_SERIES__SHOW_WEB_LABELS:
			setShowWebLabels((Boolean) newValue);
			return;
		case RadarTypePackage.RADAR_SERIES__SHOW_CAT_LABELS:
			setShowCatLabels((Boolean) newValue);
			return;
		case RadarTypePackage.RADAR_SERIES__RADAR_AUTO_SCALE:
			setRadarAutoScale((Boolean) newValue);
			return;
		case RadarTypePackage.RADAR_SERIES__WEB_LABEL_MAX:
			setWebLabelMax((Double) newValue);
			return;
		case RadarTypePackage.RADAR_SERIES__WEB_LABEL_MIN:
			setWebLabelMin((Double) newValue);
			return;
		case RadarTypePackage.RADAR_SERIES__WEB_LABEL_UNIT:
			setWebLabelUnit((String) newValue);
			return;
		case RadarTypePackage.RADAR_SERIES__FILL_POLYS:
			setFillPolys((Boolean) newValue);
			return;
		case RadarTypePackage.RADAR_SERIES__CONNECT_ENDPOINTS:
			setConnectEndpoints((Boolean) newValue);
			return;
		case RadarTypePackage.RADAR_SERIES__WEB_LABEL:
			setWebLabel((Label) newValue);
			return;
		case RadarTypePackage.RADAR_SERIES__CAT_LABEL:
			setCatLabel((Label) newValue);
			return;
		case RadarTypePackage.RADAR_SERIES__WEB_LABEL_FORMAT_SPECIFIER:
			setWebLabelFormatSpecifier((FormatSpecifier) newValue);
			return;
		case RadarTypePackage.RADAR_SERIES__CAT_LABEL_FORMAT_SPECIFIER:
			setCatLabelFormatSpecifier((FormatSpecifier) newValue);
			return;
		case RadarTypePackage.RADAR_SERIES__PLOT_STEPS:
			setPlotSteps((BigInteger) newValue);
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
		case RadarTypePackage.RADAR_SERIES__MARKER:
			setMarker((Marker) null);
			return;
		case RadarTypePackage.RADAR_SERIES__LINE_ATTRIBUTES:
			setLineAttributes((LineAttributes) null);
			return;
		case RadarTypePackage.RADAR_SERIES__PALETTE_LINE_COLOR:
			unsetPaletteLineColor();
			return;
		case RadarTypePackage.RADAR_SERIES__BACKGROUND_OVAL_TRANSPARENT:
			unsetBackgroundOvalTransparent();
			return;
		case RadarTypePackage.RADAR_SERIES__WEB_LINE_ATTRIBUTES:
			setWebLineAttributes((LineAttributes) null);
			return;
		case RadarTypePackage.RADAR_SERIES__SHOW_WEB_LABELS:
			unsetShowWebLabels();
			return;
		case RadarTypePackage.RADAR_SERIES__SHOW_CAT_LABELS:
			unsetShowCatLabels();
			return;
		case RadarTypePackage.RADAR_SERIES__RADAR_AUTO_SCALE:
			unsetRadarAutoScale();
			return;
		case RadarTypePackage.RADAR_SERIES__WEB_LABEL_MAX:
			unsetWebLabelMax();
			return;
		case RadarTypePackage.RADAR_SERIES__WEB_LABEL_MIN:
			unsetWebLabelMin();
			return;
		case RadarTypePackage.RADAR_SERIES__WEB_LABEL_UNIT:
			unsetWebLabelUnit();
			return;
		case RadarTypePackage.RADAR_SERIES__FILL_POLYS:
			unsetFillPolys();
			return;
		case RadarTypePackage.RADAR_SERIES__CONNECT_ENDPOINTS:
			unsetConnectEndpoints();
			return;
		case RadarTypePackage.RADAR_SERIES__WEB_LABEL:
			setWebLabel((Label) null);
			return;
		case RadarTypePackage.RADAR_SERIES__CAT_LABEL:
			setCatLabel((Label) null);
			return;
		case RadarTypePackage.RADAR_SERIES__WEB_LABEL_FORMAT_SPECIFIER:
			setWebLabelFormatSpecifier((FormatSpecifier) null);
			return;
		case RadarTypePackage.RADAR_SERIES__CAT_LABEL_FORMAT_SPECIFIER:
			setCatLabelFormatSpecifier((FormatSpecifier) null);
			return;
		case RadarTypePackage.RADAR_SERIES__PLOT_STEPS:
			unsetPlotSteps();
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
		case RadarTypePackage.RADAR_SERIES__MARKER:
			return marker != null;
		case RadarTypePackage.RADAR_SERIES__LINE_ATTRIBUTES:
			return lineAttributes != null;
		case RadarTypePackage.RADAR_SERIES__PALETTE_LINE_COLOR:
			return isSetPaletteLineColor();
		case RadarTypePackage.RADAR_SERIES__BACKGROUND_OVAL_TRANSPARENT:
			return isSetBackgroundOvalTransparent();
		case RadarTypePackage.RADAR_SERIES__WEB_LINE_ATTRIBUTES:
			return webLineAttributes != null;
		case RadarTypePackage.RADAR_SERIES__SHOW_WEB_LABELS:
			return isSetShowWebLabels();
		case RadarTypePackage.RADAR_SERIES__SHOW_CAT_LABELS:
			return isSetShowCatLabels();
		case RadarTypePackage.RADAR_SERIES__RADAR_AUTO_SCALE:
			return isSetRadarAutoScale();
		case RadarTypePackage.RADAR_SERIES__WEB_LABEL_MAX:
			return isSetWebLabelMax();
		case RadarTypePackage.RADAR_SERIES__WEB_LABEL_MIN:
			return isSetWebLabelMin();
		case RadarTypePackage.RADAR_SERIES__WEB_LABEL_UNIT:
			return isSetWebLabelUnit();
		case RadarTypePackage.RADAR_SERIES__FILL_POLYS:
			return isSetFillPolys();
		case RadarTypePackage.RADAR_SERIES__CONNECT_ENDPOINTS:
			return isSetConnectEndpoints();
		case RadarTypePackage.RADAR_SERIES__WEB_LABEL:
			return webLabel != null;
		case RadarTypePackage.RADAR_SERIES__CAT_LABEL:
			return catLabel != null;
		case RadarTypePackage.RADAR_SERIES__WEB_LABEL_FORMAT_SPECIFIER:
			return webLabelFormatSpecifier != null;
		case RadarTypePackage.RADAR_SERIES__CAT_LABEL_FORMAT_SPECIFIER:
			return catLabelFormatSpecifier != null;
		case RadarTypePackage.RADAR_SERIES__PLOT_STEPS:
			return isSetPlotSteps();
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
		result.append(", backgroundOvalTransparent: "); //$NON-NLS-1$
		if (backgroundOvalTransparentESet) {
			result.append(backgroundOvalTransparent);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(", showWebLabels: "); //$NON-NLS-1$
		if (showWebLabelsESet) {
			result.append(showWebLabels);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(", showCatLabels: "); //$NON-NLS-1$
		if (showCatLabelsESet) {
			result.append(showCatLabels);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(", radarAutoScale: "); //$NON-NLS-1$
		if (radarAutoScaleESet) {
			result.append(radarAutoScale);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(", webLabelMax: "); //$NON-NLS-1$
		if (webLabelMaxESet) {
			result.append(webLabelMax);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(", webLabelMin: "); //$NON-NLS-1$
		if (webLabelMinESet) {
			result.append(webLabelMin);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(", webLabelUnit: "); //$NON-NLS-1$
		if (webLabelUnitESet) {
			result.append(webLabelUnit);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(", fillPolys: "); //$NON-NLS-1$
		if (fillPolysESet) {
			result.append(fillPolys);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(", connectEndpoints: "); //$NON-NLS-1$
		if (connectEndpointsESet) {
			result.append(connectEndpoints);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(", plotSteps: "); //$NON-NLS-1$
		if (plotStepsESet) {
			result.append(plotSteps);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(')');
		return result.toString();
	}

	/**
	 * @generated
	 */
	protected void set(RadarSeries src) {

		super.set(src);

		// children

		if (src.getMarker() != null) {
			setMarker(src.getMarker().copyInstance());
		}

		if (src.getLineAttributes() != null) {
			setLineAttributes(src.getLineAttributes().copyInstance());
		}

		if (src.getWebLineAttributes() != null) {
			setWebLineAttributes(src.getWebLineAttributes().copyInstance());
		}

		if (src.getWebLabel() != null) {
			setWebLabel(src.getWebLabel().copyInstance());
		}

		if (src.getCatLabel() != null) {
			setCatLabel(src.getCatLabel().copyInstance());
		}

		if (src.getWebLabelFormatSpecifier() != null) {
			setWebLabelFormatSpecifier(src.getWebLabelFormatSpecifier().copyInstance());
		}

		if (src.getCatLabelFormatSpecifier() != null) {
			setCatLabelFormatSpecifier(src.getCatLabelFormatSpecifier().copyInstance());
		}

		// attributes

		paletteLineColor = src.isPaletteLineColor();

		paletteLineColorESet = src.isSetPaletteLineColor();

		backgroundOvalTransparent = src.isBackgroundOvalTransparent();

		backgroundOvalTransparentESet = src.isSetBackgroundOvalTransparent();

		showWebLabels = src.isShowWebLabels();

		showWebLabelsESet = src.isSetShowWebLabels();

		showCatLabels = src.isShowCatLabels();

		showCatLabelsESet = src.isSetShowCatLabels();

		radarAutoScale = src.isRadarAutoScale();

		radarAutoScaleESet = src.isSetRadarAutoScale();

		webLabelMax = src.getWebLabelMax();

		webLabelMaxESet = src.isSetWebLabelMax();

		webLabelMin = src.getWebLabelMin();

		webLabelMinESet = src.isSetWebLabelMin();

		webLabelUnit = src.getWebLabelUnit();

		webLabelUnitESet = src.isSetWebLabelUnit();

		fillPolys = src.isFillPolys();

		fillPolysESet = src.isSetFillPolys();

		connectEndpoints = src.isConnectEndpoints();

		connectEndpointsESet = src.isSetConnectEndpoints();

		plotSteps = src.getPlotSteps();

		plotStepsESet = src.isSetPlotSteps();

	}

	/**
	 * @generated
	 */
	@Override
	public RadarSeries copyInstance() {
		RadarSeriesImpl dest = new RadarSeriesImpl();
		dest.set(this);
		return dest;
	}

	public static final RadarSeries create() {
		final RadarSeries se = org.eclipse.birt.chart.examples.radar.model.type.RadarTypeFactory.eINSTANCE
				.createRadarSeries();
		((RadarSeriesImpl) se).initialize();
		return se;
	}

	public static final RadarSeries createDefault() {
		final RadarSeries se = org.eclipse.birt.chart.examples.radar.model.type.RadarTypeFactory.eINSTANCE
				.createRadarSeries();
		((RadarSeriesImpl) se).initDefault();
		return se;
	}

	/**
	 * Initializes all member variables within this object recursively
	 *
	 * Note: Manually written
	 */
	@Override
	protected void initialize() {
		super.initialize();

		final LineAttributes lia = AttributeFactory.eINSTANCE.createLineAttributes();
		((LineAttributesImpl) lia).set(null, LineStyle.SOLID_LITERAL, 1);
		lia.setVisible(true);
		setLineAttributes(lia);

		final LineAttributes weblia = AttributeFactory.eINSTANCE.createLineAttributes();
		((LineAttributesImpl) weblia).set(ColorDefinitionImpl.BLACK(), LineStyle.SOLID_LITERAL, 1);
		weblia.setVisible(true);
		setWebLineAttributes(weblia);

		final Marker m = AttributeFactory.eINSTANCE.createMarker();
		m.setType(MarkerType.BOX_LITERAL);
		m.setSize(4);
		m.setVisible(true);
		LineAttributes la = AttributeFactory.eINSTANCE.createLineAttributes();
		la.setVisible(true);
		m.setOutline(la);
		setMarker(m);

		final Label lab = LabelImpl.create();
		setWebLabel(lab);
		final Label clab = LabelImpl.create();
		setCatLabel(clab);

		setPaletteLineColor(true);
	}

	@Override
	protected void initDefault() {
		super.initDefault();

		final LineAttributes lia = LineAttributesImpl.createDefault(null, LineStyle.SOLID_LITERAL, 1, true);
		setLineAttributes(lia);

		final LineAttributes weblia = LineAttributesImpl.createDefault(null, LineStyle.SOLID_LITERAL, 1, true);
		setWebLineAttributes(weblia);

		final Marker m = MarkerImpl.createDefault(MarkerType.BOX_LITERAL, 4, true);
		LineAttributes la = LineAttributesImpl.createDefault(true);
		m.setOutline(la);
		setMarker(m);

		final Label lab = LabelImpl.createDefault();
		setWebLabel(lab);
		final Label clab = LabelImpl.createDefault();
		setCatLabel(clab);

		paletteLineColor = true;
	}

	@Override
	public String getDisplayName() {
		return Messages.getString("RadarSeriesImpl.displayName"); //$NON-NLS-1$
	}

	@Override
	public NameSet getLabelPositionScope(ChartDimension dimension) {
		return LiteralHelper.outPositionSet;
	}

} // RadarSeriesImpl
