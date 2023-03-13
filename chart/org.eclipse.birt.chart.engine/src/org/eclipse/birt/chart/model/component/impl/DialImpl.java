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

package org.eclipse.birt.chart.model.component.impl;

import java.util.Collection;

import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.component.ComponentFactory;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.component.Dial;
import org.eclipse.birt.chart.model.component.DialRegion;
import org.eclipse.birt.chart.model.component.Grid;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Scale;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object
 * '<em><b>Dial</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.DialImpl#getStartAngle
 * <em>Start Angle</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.DialImpl#getStopAngle
 * <em>Stop Angle</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.DialImpl#getRadius
 * <em>Radius</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.DialImpl#getLineAttributes
 * <em>Line Attributes</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.DialImpl#getFill
 * <em>Fill</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.DialImpl#getDialRegions
 * <em>Dial Regions</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.DialImpl#getMajorGrid
 * <em>Major Grid</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.DialImpl#getMinorGrid
 * <em>Minor Grid</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.DialImpl#getScale
 * <em>Scale</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.DialImpl#isInverseScale
 * <em>Inverse Scale</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.DialImpl#getLabel
 * <em>Label</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.DialImpl#getFormatSpecifier
 * <em>Format Specifier</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DialImpl extends EObjectImpl implements Dial {

	/**
	 * The default value of the '{@link #getStartAngle() <em>Start Angle</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getStartAngle()
	 * @generated
	 * @ordered
	 */
	protected static final double START_ANGLE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getStartAngle() <em>Start Angle</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getStartAngle()
	 * @generated
	 * @ordered
	 */
	protected double startAngle = START_ANGLE_EDEFAULT;

	/**
	 * This is true if the Start Angle attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean startAngleESet;

	/**
	 * The default value of the '{@link #getStopAngle() <em>Stop Angle</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getStopAngle()
	 * @generated
	 * @ordered
	 */
	protected static final double STOP_ANGLE_EDEFAULT = 180.0;

	/**
	 * The cached value of the '{@link #getStopAngle() <em>Stop Angle</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getStopAngle()
	 * @generated
	 * @ordered
	 */
	protected double stopAngle = STOP_ANGLE_EDEFAULT;

	/**
	 * This is true if the Stop Angle attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean stopAngleESet;

	/**
	 * The default value of the '{@link #getRadius() <em>Radius</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getRadius()
	 * @generated
	 * @ordered
	 */
	protected static final double RADIUS_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getRadius() <em>Radius</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getRadius()
	 * @generated
	 * @ordered
	 */
	protected double radius = RADIUS_EDEFAULT;

	/**
	 * This is true if the Radius attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean radiusESet;

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
	 * The cached value of the '{@link #getFill() <em>Fill</em>}' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getFill()
	 * @generated
	 * @ordered
	 */
	protected Fill fill;

	/**
	 * The cached value of the '{@link #getDialRegions() <em>Dial Regions</em>}'
	 * containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getDialRegions()
	 * @generated
	 * @ordered
	 */
	protected EList<DialRegion> dialRegions;

	/**
	 * The cached value of the '{@link #getMajorGrid() <em>Major Grid</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getMajorGrid()
	 * @generated
	 * @ordered
	 */
	protected Grid majorGrid;

	/**
	 * The cached value of the '{@link #getMinorGrid() <em>Minor Grid</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getMinorGrid()
	 * @generated
	 * @ordered
	 */
	protected Grid minorGrid;

	/**
	 * The cached value of the '{@link #getScale() <em>Scale</em>}' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getScale()
	 * @generated
	 * @ordered
	 */
	protected Scale scale;

	/**
	 * The default value of the '{@link #isInverseScale() <em>Inverse Scale</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isInverseScale()
	 * @generated
	 * @ordered
	 */
	protected static final boolean INVERSE_SCALE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isInverseScale() <em>Inverse Scale</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isInverseScale()
	 * @generated
	 * @ordered
	 */
	protected boolean inverseScale = INVERSE_SCALE_EDEFAULT;

	/**
	 * This is true if the Inverse Scale attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean inverseScaleESet;

	/**
	 * The cached value of the '{@link #getLabel() <em>Label</em>}' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getLabel()
	 * @generated
	 * @ordered
	 */
	protected Label label;

	/**
	 * The cached value of the '{@link #getFormatSpecifier() <em>Format
	 * Specifier</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #getFormatSpecifier()
	 * @generated
	 * @ordered
	 */
	protected FormatSpecifier formatSpecifier;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected DialImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ComponentPackage.Literals.DIAL;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public double getStartAngle() {
		return startAngle;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setStartAngle(double newStartAngle) {
		double oldStartAngle = startAngle;
		startAngle = newStartAngle;
		boolean oldStartAngleESet = startAngleESet;
		startAngleESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.DIAL__START_ANGLE, oldStartAngle,
					startAngle, !oldStartAngleESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetStartAngle() {
		double oldStartAngle = startAngle;
		boolean oldStartAngleESet = startAngleESet;
		startAngle = START_ANGLE_EDEFAULT;
		startAngleESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.DIAL__START_ANGLE, oldStartAngle,
					START_ANGLE_EDEFAULT, oldStartAngleESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetStartAngle() {
		return startAngleESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public double getStopAngle() {
		return stopAngle;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setStopAngle(double newStopAngle) {
		double oldStopAngle = stopAngle;
		stopAngle = newStopAngle;
		boolean oldStopAngleESet = stopAngleESet;
		stopAngleESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.DIAL__STOP_ANGLE, oldStopAngle,
					stopAngle, !oldStopAngleESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetStopAngle() {
		double oldStopAngle = stopAngle;
		boolean oldStopAngleESet = stopAngleESet;
		stopAngle = STOP_ANGLE_EDEFAULT;
		stopAngleESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.DIAL__STOP_ANGLE, oldStopAngle,
					STOP_ANGLE_EDEFAULT, oldStopAngleESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetStopAngle() {
		return stopAngleESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public double getRadius() {
		return radius;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setRadius(double newRadius) {
		double oldRadius = radius;
		radius = newRadius;
		boolean oldRadiusESet = radiusESet;
		radiusESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.DIAL__RADIUS, oldRadius, radius,
					!oldRadiusESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetRadius() {
		double oldRadius = radius;
		boolean oldRadiusESet = radiusESet;
		radius = RADIUS_EDEFAULT;
		radiusESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.DIAL__RADIUS, oldRadius,
					RADIUS_EDEFAULT, oldRadiusESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetRadius() {
		return radiusESet;
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
					ComponentPackage.DIAL__LINE_ATTRIBUTES, oldLineAttributes, newLineAttributes);
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
						EOPPOSITE_FEATURE_BASE - ComponentPackage.DIAL__LINE_ATTRIBUTES, null, msgs);
			}
			if (newLineAttributes != null) {
				msgs = ((InternalEObject) newLineAttributes).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.DIAL__LINE_ATTRIBUTES, null, msgs);
			}
			msgs = basicSetLineAttributes(newLineAttributes, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.DIAL__LINE_ATTRIBUTES,
					newLineAttributes, newLineAttributes));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Fill getFill() {
		return fill;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetFill(Fill newFill, NotificationChain msgs) {
		Fill oldFill = fill;
		fill = newFill;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ComponentPackage.DIAL__FILL,
					oldFill, newFill);
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
	public void setFill(Fill newFill) {
		if (newFill != fill) {
			NotificationChain msgs = null;
			if (fill != null) {
				msgs = ((InternalEObject) fill).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.DIAL__FILL, null, msgs);
			}
			if (newFill != null) {
				msgs = ((InternalEObject) newFill).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.DIAL__FILL, null, msgs);
			}
			msgs = basicSetFill(newFill, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.DIAL__FILL, newFill, newFill));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EList<DialRegion> getDialRegions() {
		if (dialRegions == null) {
			dialRegions = new EObjectContainmentEList<>(DialRegion.class, this,
					ComponentPackage.DIAL__DIAL_REGIONS);
		}
		return dialRegions;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Grid getMajorGrid() {
		return majorGrid;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetMajorGrid(Grid newMajorGrid, NotificationChain msgs) {
		Grid oldMajorGrid = majorGrid;
		majorGrid = newMajorGrid;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					ComponentPackage.DIAL__MAJOR_GRID, oldMajorGrid, newMajorGrid);
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
	public void setMajorGrid(Grid newMajorGrid) {
		if (newMajorGrid != majorGrid) {
			NotificationChain msgs = null;
			if (majorGrid != null) {
				msgs = ((InternalEObject) majorGrid).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.DIAL__MAJOR_GRID, null, msgs);
			}
			if (newMajorGrid != null) {
				msgs = ((InternalEObject) newMajorGrid).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.DIAL__MAJOR_GRID, null, msgs);
			}
			msgs = basicSetMajorGrid(newMajorGrid, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.DIAL__MAJOR_GRID, newMajorGrid,
					newMajorGrid));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Grid getMinorGrid() {
		return minorGrid;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetMinorGrid(Grid newMinorGrid, NotificationChain msgs) {
		Grid oldMinorGrid = minorGrid;
		minorGrid = newMinorGrid;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					ComponentPackage.DIAL__MINOR_GRID, oldMinorGrid, newMinorGrid);
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
	public void setMinorGrid(Grid newMinorGrid) {
		if (newMinorGrid != minorGrid) {
			NotificationChain msgs = null;
			if (minorGrid != null) {
				msgs = ((InternalEObject) minorGrid).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.DIAL__MINOR_GRID, null, msgs);
			}
			if (newMinorGrid != null) {
				msgs = ((InternalEObject) newMinorGrid).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.DIAL__MINOR_GRID, null, msgs);
			}
			msgs = basicSetMinorGrid(newMinorGrid, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.DIAL__MINOR_GRID, newMinorGrid,
					newMinorGrid));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Scale getScale() {
		return scale;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetScale(Scale newScale, NotificationChain msgs) {
		Scale oldScale = scale;
		scale = newScale;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ComponentPackage.DIAL__SCALE,
					oldScale, newScale);
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
	public void setScale(Scale newScale) {
		if (newScale != scale) {
			NotificationChain msgs = null;
			if (scale != null) {
				msgs = ((InternalEObject) scale).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.DIAL__SCALE, null, msgs);
			}
			if (newScale != null) {
				msgs = ((InternalEObject) newScale).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.DIAL__SCALE, null, msgs);
			}
			msgs = basicSetScale(newScale, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.DIAL__SCALE, newScale, newScale));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isInverseScale() {
		return inverseScale;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setInverseScale(boolean newInverseScale) {
		boolean oldInverseScale = inverseScale;
		inverseScale = newInverseScale;
		boolean oldInverseScaleESet = inverseScaleESet;
		inverseScaleESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.DIAL__INVERSE_SCALE, oldInverseScale,
					inverseScale, !oldInverseScaleESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetInverseScale() {
		boolean oldInverseScale = inverseScale;
		boolean oldInverseScaleESet = inverseScaleESet;
		inverseScale = INVERSE_SCALE_EDEFAULT;
		inverseScaleESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.DIAL__INVERSE_SCALE,
					oldInverseScale, INVERSE_SCALE_EDEFAULT, oldInverseScaleESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetInverseScale() {
		return inverseScaleESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Label getLabel() {
		return label;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetLabel(Label newLabel, NotificationChain msgs) {
		Label oldLabel = label;
		label = newLabel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ComponentPackage.DIAL__LABEL,
					oldLabel, newLabel);
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
	public void setLabel(Label newLabel) {
		if (newLabel != label) {
			NotificationChain msgs = null;
			if (label != null) {
				msgs = ((InternalEObject) label).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.DIAL__LABEL, null, msgs);
			}
			if (newLabel != null) {
				msgs = ((InternalEObject) newLabel).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.DIAL__LABEL, null, msgs);
			}
			msgs = basicSetLabel(newLabel, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.DIAL__LABEL, newLabel, newLabel));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public FormatSpecifier getFormatSpecifier() {
		return formatSpecifier;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetFormatSpecifier(FormatSpecifier newFormatSpecifier, NotificationChain msgs) {
		FormatSpecifier oldFormatSpecifier = formatSpecifier;
		formatSpecifier = newFormatSpecifier;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					ComponentPackage.DIAL__FORMAT_SPECIFIER, oldFormatSpecifier, newFormatSpecifier);
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
	public void setFormatSpecifier(FormatSpecifier newFormatSpecifier) {
		if (newFormatSpecifier != formatSpecifier) {
			NotificationChain msgs = null;
			if (formatSpecifier != null) {
				msgs = ((InternalEObject) formatSpecifier).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.DIAL__FORMAT_SPECIFIER, null, msgs);
			}
			if (newFormatSpecifier != null) {
				msgs = ((InternalEObject) newFormatSpecifier).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.DIAL__FORMAT_SPECIFIER, null, msgs);
			}
			msgs = basicSetFormatSpecifier(newFormatSpecifier, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.DIAL__FORMAT_SPECIFIER,
					newFormatSpecifier, newFormatSpecifier));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case ComponentPackage.DIAL__LINE_ATTRIBUTES:
			return basicSetLineAttributes(null, msgs);
		case ComponentPackage.DIAL__FILL:
			return basicSetFill(null, msgs);
		case ComponentPackage.DIAL__DIAL_REGIONS:
			return ((InternalEList<?>) getDialRegions()).basicRemove(otherEnd, msgs);
		case ComponentPackage.DIAL__MAJOR_GRID:
			return basicSetMajorGrid(null, msgs);
		case ComponentPackage.DIAL__MINOR_GRID:
			return basicSetMinorGrid(null, msgs);
		case ComponentPackage.DIAL__SCALE:
			return basicSetScale(null, msgs);
		case ComponentPackage.DIAL__LABEL:
			return basicSetLabel(null, msgs);
		case ComponentPackage.DIAL__FORMAT_SPECIFIER:
			return basicSetFormatSpecifier(null, msgs);
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
		case ComponentPackage.DIAL__START_ANGLE:
			return getStartAngle();
		case ComponentPackage.DIAL__STOP_ANGLE:
			return getStopAngle();
		case ComponentPackage.DIAL__RADIUS:
			return getRadius();
		case ComponentPackage.DIAL__LINE_ATTRIBUTES:
			return getLineAttributes();
		case ComponentPackage.DIAL__FILL:
			return getFill();
		case ComponentPackage.DIAL__DIAL_REGIONS:
			return getDialRegions();
		case ComponentPackage.DIAL__MAJOR_GRID:
			return getMajorGrid();
		case ComponentPackage.DIAL__MINOR_GRID:
			return getMinorGrid();
		case ComponentPackage.DIAL__SCALE:
			return getScale();
		case ComponentPackage.DIAL__INVERSE_SCALE:
			return isInverseScale();
		case ComponentPackage.DIAL__LABEL:
			return getLabel();
		case ComponentPackage.DIAL__FORMAT_SPECIFIER:
			return getFormatSpecifier();
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
		case ComponentPackage.DIAL__START_ANGLE:
			setStartAngle((Double) newValue);
			return;
		case ComponentPackage.DIAL__STOP_ANGLE:
			setStopAngle((Double) newValue);
			return;
		case ComponentPackage.DIAL__RADIUS:
			setRadius((Double) newValue);
			return;
		case ComponentPackage.DIAL__LINE_ATTRIBUTES:
			setLineAttributes((LineAttributes) newValue);
			return;
		case ComponentPackage.DIAL__FILL:
			setFill((Fill) newValue);
			return;
		case ComponentPackage.DIAL__DIAL_REGIONS:
			getDialRegions().clear();
			getDialRegions().addAll((Collection<? extends DialRegion>) newValue);
			return;
		case ComponentPackage.DIAL__MAJOR_GRID:
			setMajorGrid((Grid) newValue);
			return;
		case ComponentPackage.DIAL__MINOR_GRID:
			setMinorGrid((Grid) newValue);
			return;
		case ComponentPackage.DIAL__SCALE:
			setScale((Scale) newValue);
			return;
		case ComponentPackage.DIAL__INVERSE_SCALE:
			setInverseScale((Boolean) newValue);
			return;
		case ComponentPackage.DIAL__LABEL:
			setLabel((Label) newValue);
			return;
		case ComponentPackage.DIAL__FORMAT_SPECIFIER:
			setFormatSpecifier((FormatSpecifier) newValue);
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
		case ComponentPackage.DIAL__START_ANGLE:
			unsetStartAngle();
			return;
		case ComponentPackage.DIAL__STOP_ANGLE:
			unsetStopAngle();
			return;
		case ComponentPackage.DIAL__RADIUS:
			unsetRadius();
			return;
		case ComponentPackage.DIAL__LINE_ATTRIBUTES:
			setLineAttributes((LineAttributes) null);
			return;
		case ComponentPackage.DIAL__FILL:
			setFill((Fill) null);
			return;
		case ComponentPackage.DIAL__DIAL_REGIONS:
			getDialRegions().clear();
			return;
		case ComponentPackage.DIAL__MAJOR_GRID:
			setMajorGrid((Grid) null);
			return;
		case ComponentPackage.DIAL__MINOR_GRID:
			setMinorGrid((Grid) null);
			return;
		case ComponentPackage.DIAL__SCALE:
			setScale((Scale) null);
			return;
		case ComponentPackage.DIAL__INVERSE_SCALE:
			unsetInverseScale();
			return;
		case ComponentPackage.DIAL__LABEL:
			setLabel((Label) null);
			return;
		case ComponentPackage.DIAL__FORMAT_SPECIFIER:
			setFormatSpecifier((FormatSpecifier) null);
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
		case ComponentPackage.DIAL__START_ANGLE:
			return isSetStartAngle();
		case ComponentPackage.DIAL__STOP_ANGLE:
			return isSetStopAngle();
		case ComponentPackage.DIAL__RADIUS:
			return isSetRadius();
		case ComponentPackage.DIAL__LINE_ATTRIBUTES:
			return lineAttributes != null;
		case ComponentPackage.DIAL__FILL:
			return fill != null;
		case ComponentPackage.DIAL__DIAL_REGIONS:
			return dialRegions != null && !dialRegions.isEmpty();
		case ComponentPackage.DIAL__MAJOR_GRID:
			return majorGrid != null;
		case ComponentPackage.DIAL__MINOR_GRID:
			return minorGrid != null;
		case ComponentPackage.DIAL__SCALE:
			return scale != null;
		case ComponentPackage.DIAL__INVERSE_SCALE:
			return isSetInverseScale();
		case ComponentPackage.DIAL__LABEL:
			return label != null;
		case ComponentPackage.DIAL__FORMAT_SPECIFIER:
			return formatSpecifier != null;
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
		result.append(" (startAngle: "); //$NON-NLS-1$
		if (startAngleESet) {
			result.append(startAngle);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(", stopAngle: "); //$NON-NLS-1$
		if (stopAngleESet) {
			result.append(stopAngle);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(", radius: "); //$NON-NLS-1$
		if (radiusESet) {
			result.append(radius);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(", inverseScale: "); //$NON-NLS-1$
		if (inverseScaleESet) {
			result.append(inverseScale);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(')');
		return result.toString();
	}

	/**
	 * @return dial instance with setting 'isSet' flag.
	 */
	public static final Dial create() {
		final Dial dl = ComponentFactory.eINSTANCE.createDial();
		((DialImpl) dl).initialize();
		return dl;
	}

	/**
	 *
	 */
	protected final void initialize() {
		// Outline
		LineAttributes lia = LineAttributesImpl.create(ColorDefinitionImpl.BLACK(), LineStyle.SOLID_LITERAL, 1);
		setLineAttributes(lia);

		// Label
		Label lb = LabelImpl.create();
		setLabel(lb);

		// MAJOR GRID
		Grid gr = ComponentFactory.eINSTANCE.createGrid();
		lia = LineAttributesImpl.create(ColorDefinitionImpl.create(196, 196, 196), LineStyle.SOLID_LITERAL, 1);
		lia.setVisible(true);
		gr.setLineAttributes(lia);
		lia = LineAttributesImpl.create(ColorDefinitionImpl.create(196, 196, 196), LineStyle.SOLID_LITERAL, 1);
		gr.setTickAttributes(lia);
		gr.setTickStyle(TickStyle.BELOW_LITERAL);
		setMajorGrid(gr);

		// MINOR GRID
		gr = ComponentFactory.eINSTANCE.createGrid();
		lia = LineAttributesImpl.create(ColorDefinitionImpl.create(225, 225, 225), LineStyle.SOLID_LITERAL, 1);
		lia.setVisible(false);
		gr.setLineAttributes(lia);
		lia = LineAttributesImpl.create(ColorDefinitionImpl.create(225, 225, 225), LineStyle.SOLID_LITERAL, 1);
		lia.setVisible(false);
		gr.setTickAttributes(lia);
		gr.setTickStyle(TickStyle.BELOW_LITERAL);
		setMinorGrid(gr);

		// SCALE
		Scale sc = ComponentFactory.eINSTANCE.createScale();
		sc.setMinorGridsPerUnit(5);
		setScale(sc);
	}

	/**
	 * @return dial instance without setting 'isSet' flag.
	 */
	public static final Dial createDefault() {
		final Dial dl = ComponentFactory.eINSTANCE.createDial();
		((DialImpl) dl).initDefault();
		return dl;
	}

	/**
	 *
	 */
	protected final void initDefault() {
		// Outline
		LineAttributes lia = LineAttributesImpl.createDefault(null, LineStyle.SOLID_LITERAL, 1);
		setLineAttributes(lia);

		// Label
		Label lb = LabelImpl.createDefault();
		setLabel(lb);

		// MAJOR GRID
		Grid gr = ComponentFactory.eINSTANCE.createGrid();
		lia = LineAttributesImpl.createDefault(null, LineStyle.SOLID_LITERAL, 1);
		gr.setLineAttributes(lia);
		lia = LineAttributesImpl.createDefault(null, LineStyle.SOLID_LITERAL, 1);
		gr.setTickAttributes(lia);
		((GridImpl) gr).tickStyle = TickStyle.BELOW_LITERAL;
		setMajorGrid(gr);

		// MINOR GRID
		gr = ComponentFactory.eINSTANCE.createGrid();
		lia = LineAttributesImpl.createDefault(null, LineStyle.SOLID_LITERAL, 1, false);
		gr.setLineAttributes(lia);
		lia = LineAttributesImpl.createDefault(null, LineStyle.SOLID_LITERAL, 1, false);
		gr.setTickAttributes(lia);
		((GridImpl) gr).tickStyle = TickStyle.BELOW_LITERAL;
		setMinorGrid(gr);

		// SCALE
		Scale sc = ComponentFactory.eINSTANCE.createScale();
		((ScaleImpl) sc).minorGridsPerUnit = 5;
		setScale(sc);
	}

	/**
	 * @generated
	 */
	@Override
	public Dial copyInstance() {
		DialImpl dest = new DialImpl();
		dest.set(this);
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set(Dial src) {

		// children

		if (src.getLineAttributes() != null) {
			setLineAttributes(src.getLineAttributes().copyInstance());
		}

		if (src.getFill() != null) {
			setFill(src.getFill().copyInstance());
		}

		if (src.getDialRegions() != null) {
			EList<DialRegion> list = getDialRegions();
			for (DialRegion element : src.getDialRegions()) {
				list.add(element.copyInstance());
			}
		}

		if (src.getMajorGrid() != null) {
			setMajorGrid(src.getMajorGrid().copyInstance());
		}

		if (src.getMinorGrid() != null) {
			setMinorGrid(src.getMinorGrid().copyInstance());
		}

		if (src.getScale() != null) {
			setScale(src.getScale().copyInstance());
		}

		if (src.getLabel() != null) {
			setLabel(src.getLabel().copyInstance());
		}

		if (src.getFormatSpecifier() != null) {
			setFormatSpecifier(src.getFormatSpecifier().copyInstance());
		}

		// attributes

		startAngle = src.getStartAngle();

		startAngleESet = src.isSetStartAngle();

		stopAngle = src.getStopAngle();

		stopAngleESet = src.isSetStopAngle();

		radius = src.getRadius();

		radiusESet = src.isSetRadius();

		inverseScale = src.isInverseScale();

		inverseScaleESet = src.isSetInverseScale();

	}

} // DialImpl
