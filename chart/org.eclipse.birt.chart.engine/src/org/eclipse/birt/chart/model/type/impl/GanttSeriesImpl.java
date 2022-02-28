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

import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.MarkerType;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.attribute.impl.MarkerImpl;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.LabelImpl;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.type.GanttSeries;
import org.eclipse.birt.chart.model.type.TypeFactory;
import org.eclipse.birt.chart.model.type.TypePackage;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.chart.util.NameSet;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Gantt
 * Series</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.GanttSeriesImpl#getStartMarker
 * <em>Start Marker</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.GanttSeriesImpl#getStartMarkerPosition
 * <em>Start Marker Position</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.GanttSeriesImpl#getEndMarker
 * <em>End Marker</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.GanttSeriesImpl#getEndMarkerPosition
 * <em>End Marker Position</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.GanttSeriesImpl#getConnectionLine
 * <em>Connection Line</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.GanttSeriesImpl#getOutline
 * <em>Outline</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.GanttSeriesImpl#getOutlineFill
 * <em>Outline Fill</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.GanttSeriesImpl#isUseDecorationLabelValue
 * <em>Use Decoration Label Value</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.GanttSeriesImpl#getDecorationLabel
 * <em>Decoration Label</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.GanttSeriesImpl#getDecorationLabelPosition
 * <em>Decoration Label Position</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.GanttSeriesImpl#isPaletteLineColor
 * <em>Palette Line Color</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GanttSeriesImpl extends SeriesImpl implements GanttSeries {

	/**
	 * The cached value of the '{@link #getStartMarker() <em>Start Marker</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getStartMarker()
	 * @generated
	 * @ordered
	 */
	protected Marker startMarker;

	/**
	 * The default value of the '{@link #getStartMarkerPosition() <em>Start Marker
	 * Position</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getStartMarkerPosition()
	 * @generated
	 * @ordered
	 */
	protected static final Position START_MARKER_POSITION_EDEFAULT = Position.ABOVE_LITERAL;

	/**
	 * The cached value of the '{@link #getStartMarkerPosition() <em>Start Marker
	 * Position</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getStartMarkerPosition()
	 * @generated
	 * @ordered
	 */
	protected Position startMarkerPosition = START_MARKER_POSITION_EDEFAULT;

	/**
	 * This is true if the Start Marker Position attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean startMarkerPositionESet;

	/**
	 * The cached value of the '{@link #getEndMarker() <em>End Marker</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getEndMarker()
	 * @generated
	 * @ordered
	 */
	protected Marker endMarker;

	/**
	 * The default value of the '{@link #getEndMarkerPosition() <em>End Marker
	 * Position</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getEndMarkerPosition()
	 * @generated
	 * @ordered
	 */
	protected static final Position END_MARKER_POSITION_EDEFAULT = Position.ABOVE_LITERAL;

	/**
	 * The cached value of the '{@link #getEndMarkerPosition() <em>End Marker
	 * Position</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getEndMarkerPosition()
	 * @generated
	 * @ordered
	 */
	protected Position endMarkerPosition = END_MARKER_POSITION_EDEFAULT;

	/**
	 * This is true if the End Marker Position attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean endMarkerPositionESet;

	/**
	 * The cached value of the '{@link #getConnectionLine() <em>Connection
	 * Line</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @see #getConnectionLine()
	 * @generated
	 * @ordered
	 */
	protected LineAttributes connectionLine;

	/**
	 * The cached value of the '{@link #getOutline() <em>Outline</em>}' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getOutline()
	 * @generated
	 * @ordered
	 */
	protected LineAttributes outline;

	/**
	 * The cached value of the '{@link #getOutlineFill() <em>Outline Fill</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getOutlineFill()
	 * @generated
	 * @ordered
	 */
	protected Fill outlineFill;

	/**
	 * The default value of the '{@link #isUseDecorationLabelValue() <em>Use
	 * Decoration Label Value</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #isUseDecorationLabelValue()
	 * @generated
	 * @ordered
	 */
	protected static final boolean USE_DECORATION_LABEL_VALUE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isUseDecorationLabelValue() <em>Use
	 * Decoration Label Value</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #isUseDecorationLabelValue()
	 * @generated
	 * @ordered
	 */
	protected boolean useDecorationLabelValue = USE_DECORATION_LABEL_VALUE_EDEFAULT;

	/**
	 * This is true if the Use Decoration Label Value attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean useDecorationLabelValueESet;

	/**
	 * The cached value of the '{@link #getDecorationLabel() <em>Decoration
	 * Label</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @see #getDecorationLabel()
	 * @generated
	 * @ordered
	 */
	protected Label decorationLabel;

	/**
	 * The default value of the '{@link #getDecorationLabelPosition() <em>Decoration
	 * Label Position</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @see #getDecorationLabelPosition()
	 * @generated
	 * @ordered
	 */
	protected static final Position DECORATION_LABEL_POSITION_EDEFAULT = Position.ABOVE_LITERAL;

	/**
	 * The cached value of the '{@link #getDecorationLabelPosition() <em>Decoration
	 * Label Position</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @see #getDecorationLabelPosition()
	 * @generated
	 * @ordered
	 */
	protected Position decorationLabelPosition = DECORATION_LABEL_POSITION_EDEFAULT;

	/**
	 * This is true if the Decoration Label Position attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean decorationLabelPositionESet;

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
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected GanttSeriesImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TypePackage.Literals.GANTT_SERIES;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Marker getStartMarker() {
		return startMarker;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetStartMarker(Marker newStartMarker, NotificationChain msgs) {
		Marker oldStartMarker = startMarker;
		startMarker = newStartMarker;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					TypePackage.GANTT_SERIES__START_MARKER, oldStartMarker, newStartMarker);
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
	public void setStartMarker(Marker newStartMarker) {
		if (newStartMarker != startMarker) {
			NotificationChain msgs = null;
			if (startMarker != null) {
				msgs = ((InternalEObject) startMarker).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - TypePackage.GANTT_SERIES__START_MARKER, null, msgs);
			}
			if (newStartMarker != null) {
				msgs = ((InternalEObject) newStartMarker).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - TypePackage.GANTT_SERIES__START_MARKER, null, msgs);
			}
			msgs = basicSetStartMarker(newStartMarker, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.GANTT_SERIES__START_MARKER,
					newStartMarker, newStartMarker));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Position getStartMarkerPosition() {
		return startMarkerPosition;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setStartMarkerPosition(Position newStartMarkerPosition) {
		Position oldStartMarkerPosition = startMarkerPosition;
		startMarkerPosition = newStartMarkerPosition == null ? START_MARKER_POSITION_EDEFAULT : newStartMarkerPosition;
		boolean oldStartMarkerPositionESet = startMarkerPositionESet;
		startMarkerPositionESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.GANTT_SERIES__START_MARKER_POSITION,
					oldStartMarkerPosition, startMarkerPosition, !oldStartMarkerPositionESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetStartMarkerPosition() {
		Position oldStartMarkerPosition = startMarkerPosition;
		boolean oldStartMarkerPositionESet = startMarkerPositionESet;
		startMarkerPosition = START_MARKER_POSITION_EDEFAULT;
		startMarkerPositionESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, TypePackage.GANTT_SERIES__START_MARKER_POSITION,
					oldStartMarkerPosition, START_MARKER_POSITION_EDEFAULT, oldStartMarkerPositionESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetStartMarkerPosition() {
		return startMarkerPositionESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Marker getEndMarker() {
		return endMarker;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetEndMarker(Marker newEndMarker, NotificationChain msgs) {
		Marker oldEndMarker = endMarker;
		endMarker = newEndMarker;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					TypePackage.GANTT_SERIES__END_MARKER, oldEndMarker, newEndMarker);
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
	public void setEndMarker(Marker newEndMarker) {
		if (newEndMarker != endMarker) {
			NotificationChain msgs = null;
			if (endMarker != null) {
				msgs = ((InternalEObject) endMarker).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - TypePackage.GANTT_SERIES__END_MARKER, null, msgs);
			}
			if (newEndMarker != null) {
				msgs = ((InternalEObject) newEndMarker).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - TypePackage.GANTT_SERIES__END_MARKER, null, msgs);
			}
			msgs = basicSetEndMarker(newEndMarker, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.GANTT_SERIES__END_MARKER, newEndMarker,
					newEndMarker));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Position getEndMarkerPosition() {
		return endMarkerPosition;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setEndMarkerPosition(Position newEndMarkerPosition) {
		Position oldEndMarkerPosition = endMarkerPosition;
		endMarkerPosition = newEndMarkerPosition == null ? END_MARKER_POSITION_EDEFAULT : newEndMarkerPosition;
		boolean oldEndMarkerPositionESet = endMarkerPositionESet;
		endMarkerPositionESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.GANTT_SERIES__END_MARKER_POSITION,
					oldEndMarkerPosition, endMarkerPosition, !oldEndMarkerPositionESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetEndMarkerPosition() {
		Position oldEndMarkerPosition = endMarkerPosition;
		boolean oldEndMarkerPositionESet = endMarkerPositionESet;
		endMarkerPosition = END_MARKER_POSITION_EDEFAULT;
		endMarkerPositionESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, TypePackage.GANTT_SERIES__END_MARKER_POSITION,
					oldEndMarkerPosition, END_MARKER_POSITION_EDEFAULT, oldEndMarkerPositionESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetEndMarkerPosition() {
		return endMarkerPositionESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public LineAttributes getConnectionLine() {
		return connectionLine;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetConnectionLine(LineAttributes newConnectionLine, NotificationChain msgs) {
		LineAttributes oldConnectionLine = connectionLine;
		connectionLine = newConnectionLine;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					TypePackage.GANTT_SERIES__CONNECTION_LINE, oldConnectionLine, newConnectionLine);
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
	public void setConnectionLine(LineAttributes newConnectionLine) {
		if (newConnectionLine != connectionLine) {
			NotificationChain msgs = null;
			if (connectionLine != null) {
				msgs = ((InternalEObject) connectionLine).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - TypePackage.GANTT_SERIES__CONNECTION_LINE, null, msgs);
			}
			if (newConnectionLine != null) {
				msgs = ((InternalEObject) newConnectionLine).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - TypePackage.GANTT_SERIES__CONNECTION_LINE, null, msgs);
			}
			msgs = basicSetConnectionLine(newConnectionLine, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.GANTT_SERIES__CONNECTION_LINE,
					newConnectionLine, newConnectionLine));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public LineAttributes getOutline() {
		return outline;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetOutline(LineAttributes newOutline, NotificationChain msgs) {
		LineAttributes oldOutline = outline;
		outline = newOutline;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					TypePackage.GANTT_SERIES__OUTLINE, oldOutline, newOutline);
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
	public void setOutline(LineAttributes newOutline) {
		if (newOutline != outline) {
			NotificationChain msgs = null;
			if (outline != null) {
				msgs = ((InternalEObject) outline).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - TypePackage.GANTT_SERIES__OUTLINE, null, msgs);
			}
			if (newOutline != null) {
				msgs = ((InternalEObject) newOutline).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - TypePackage.GANTT_SERIES__OUTLINE, null, msgs);
			}
			msgs = basicSetOutline(newOutline, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.GANTT_SERIES__OUTLINE, newOutline,
					newOutline));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Fill getOutlineFill() {
		return outlineFill;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetOutlineFill(Fill newOutlineFill, NotificationChain msgs) {
		Fill oldOutlineFill = outlineFill;
		outlineFill = newOutlineFill;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					TypePackage.GANTT_SERIES__OUTLINE_FILL, oldOutlineFill, newOutlineFill);
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
	public void setOutlineFill(Fill newOutlineFill) {
		if (newOutlineFill != outlineFill) {
			NotificationChain msgs = null;
			if (outlineFill != null) {
				msgs = ((InternalEObject) outlineFill).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - TypePackage.GANTT_SERIES__OUTLINE_FILL, null, msgs);
			}
			if (newOutlineFill != null) {
				msgs = ((InternalEObject) newOutlineFill).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - TypePackage.GANTT_SERIES__OUTLINE_FILL, null, msgs);
			}
			msgs = basicSetOutlineFill(newOutlineFill, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.GANTT_SERIES__OUTLINE_FILL,
					newOutlineFill, newOutlineFill));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isUseDecorationLabelValue() {
		return useDecorationLabelValue;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setUseDecorationLabelValue(boolean newUseDecorationLabelValue) {
		boolean oldUseDecorationLabelValue = useDecorationLabelValue;
		useDecorationLabelValue = newUseDecorationLabelValue;
		boolean oldUseDecorationLabelValueESet = useDecorationLabelValueESet;
		useDecorationLabelValueESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.GANTT_SERIES__USE_DECORATION_LABEL_VALUE,
					oldUseDecorationLabelValue, useDecorationLabelValue, !oldUseDecorationLabelValueESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetUseDecorationLabelValue() {
		boolean oldUseDecorationLabelValue = useDecorationLabelValue;
		boolean oldUseDecorationLabelValueESet = useDecorationLabelValueESet;
		useDecorationLabelValue = USE_DECORATION_LABEL_VALUE_EDEFAULT;
		useDecorationLabelValueESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET,
					TypePackage.GANTT_SERIES__USE_DECORATION_LABEL_VALUE, oldUseDecorationLabelValue,
					USE_DECORATION_LABEL_VALUE_EDEFAULT, oldUseDecorationLabelValueESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetUseDecorationLabelValue() {
		return useDecorationLabelValueESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Label getDecorationLabel() {
		return decorationLabel;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetDecorationLabel(Label newDecorationLabel, NotificationChain msgs) {
		Label oldDecorationLabel = decorationLabel;
		decorationLabel = newDecorationLabel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					TypePackage.GANTT_SERIES__DECORATION_LABEL, oldDecorationLabel, newDecorationLabel);
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
	public void setDecorationLabel(Label newDecorationLabel) {
		if (newDecorationLabel != decorationLabel) {
			NotificationChain msgs = null;
			if (decorationLabel != null) {
				msgs = ((InternalEObject) decorationLabel).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - TypePackage.GANTT_SERIES__DECORATION_LABEL, null, msgs);
			}
			if (newDecorationLabel != null) {
				msgs = ((InternalEObject) newDecorationLabel).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - TypePackage.GANTT_SERIES__DECORATION_LABEL, null, msgs);
			}
			msgs = basicSetDecorationLabel(newDecorationLabel, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.GANTT_SERIES__DECORATION_LABEL,
					newDecorationLabel, newDecorationLabel));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Position getDecorationLabelPosition() {
		return decorationLabelPosition;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setDecorationLabelPosition(Position newDecorationLabelPosition) {
		Position oldDecorationLabelPosition = decorationLabelPosition;
		decorationLabelPosition = newDecorationLabelPosition == null ? DECORATION_LABEL_POSITION_EDEFAULT
				: newDecorationLabelPosition;
		boolean oldDecorationLabelPositionESet = decorationLabelPositionESet;
		decorationLabelPositionESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.GANTT_SERIES__DECORATION_LABEL_POSITION,
					oldDecorationLabelPosition, decorationLabelPosition, !oldDecorationLabelPositionESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetDecorationLabelPosition() {
		Position oldDecorationLabelPosition = decorationLabelPosition;
		boolean oldDecorationLabelPositionESet = decorationLabelPositionESet;
		decorationLabelPosition = DECORATION_LABEL_POSITION_EDEFAULT;
		decorationLabelPositionESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, TypePackage.GANTT_SERIES__DECORATION_LABEL_POSITION,
					oldDecorationLabelPosition, DECORATION_LABEL_POSITION_EDEFAULT, oldDecorationLabelPositionESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetDecorationLabelPosition() {
		return decorationLabelPositionESet;
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
			eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.GANTT_SERIES__PALETTE_LINE_COLOR,
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
			eNotify(new ENotificationImpl(this, Notification.UNSET, TypePackage.GANTT_SERIES__PALETTE_LINE_COLOR,
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
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case TypePackage.GANTT_SERIES__START_MARKER:
			return basicSetStartMarker(null, msgs);
		case TypePackage.GANTT_SERIES__END_MARKER:
			return basicSetEndMarker(null, msgs);
		case TypePackage.GANTT_SERIES__CONNECTION_LINE:
			return basicSetConnectionLine(null, msgs);
		case TypePackage.GANTT_SERIES__OUTLINE:
			return basicSetOutline(null, msgs);
		case TypePackage.GANTT_SERIES__OUTLINE_FILL:
			return basicSetOutlineFill(null, msgs);
		case TypePackage.GANTT_SERIES__DECORATION_LABEL:
			return basicSetDecorationLabel(null, msgs);
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
		case TypePackage.GANTT_SERIES__START_MARKER:
			return getStartMarker();
		case TypePackage.GANTT_SERIES__START_MARKER_POSITION:
			return getStartMarkerPosition();
		case TypePackage.GANTT_SERIES__END_MARKER:
			return getEndMarker();
		case TypePackage.GANTT_SERIES__END_MARKER_POSITION:
			return getEndMarkerPosition();
		case TypePackage.GANTT_SERIES__CONNECTION_LINE:
			return getConnectionLine();
		case TypePackage.GANTT_SERIES__OUTLINE:
			return getOutline();
		case TypePackage.GANTT_SERIES__OUTLINE_FILL:
			return getOutlineFill();
		case TypePackage.GANTT_SERIES__USE_DECORATION_LABEL_VALUE:
			return isUseDecorationLabelValue();
		case TypePackage.GANTT_SERIES__DECORATION_LABEL:
			return getDecorationLabel();
		case TypePackage.GANTT_SERIES__DECORATION_LABEL_POSITION:
			return getDecorationLabelPosition();
		case TypePackage.GANTT_SERIES__PALETTE_LINE_COLOR:
			return isPaletteLineColor();
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
		case TypePackage.GANTT_SERIES__START_MARKER:
			setStartMarker((Marker) newValue);
			return;
		case TypePackage.GANTT_SERIES__START_MARKER_POSITION:
			setStartMarkerPosition((Position) newValue);
			return;
		case TypePackage.GANTT_SERIES__END_MARKER:
			setEndMarker((Marker) newValue);
			return;
		case TypePackage.GANTT_SERIES__END_MARKER_POSITION:
			setEndMarkerPosition((Position) newValue);
			return;
		case TypePackage.GANTT_SERIES__CONNECTION_LINE:
			setConnectionLine((LineAttributes) newValue);
			return;
		case TypePackage.GANTT_SERIES__OUTLINE:
			setOutline((LineAttributes) newValue);
			return;
		case TypePackage.GANTT_SERIES__OUTLINE_FILL:
			setOutlineFill((Fill) newValue);
			return;
		case TypePackage.GANTT_SERIES__USE_DECORATION_LABEL_VALUE:
			setUseDecorationLabelValue((Boolean) newValue);
			return;
		case TypePackage.GANTT_SERIES__DECORATION_LABEL:
			setDecorationLabel((Label) newValue);
			return;
		case TypePackage.GANTT_SERIES__DECORATION_LABEL_POSITION:
			setDecorationLabelPosition((Position) newValue);
			return;
		case TypePackage.GANTT_SERIES__PALETTE_LINE_COLOR:
			setPaletteLineColor((Boolean) newValue);
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
		case TypePackage.GANTT_SERIES__START_MARKER:
			setStartMarker((Marker) null);
			return;
		case TypePackage.GANTT_SERIES__START_MARKER_POSITION:
			unsetStartMarkerPosition();
			return;
		case TypePackage.GANTT_SERIES__END_MARKER:
			setEndMarker((Marker) null);
			return;
		case TypePackage.GANTT_SERIES__END_MARKER_POSITION:
			unsetEndMarkerPosition();
			return;
		case TypePackage.GANTT_SERIES__CONNECTION_LINE:
			setConnectionLine((LineAttributes) null);
			return;
		case TypePackage.GANTT_SERIES__OUTLINE:
			setOutline((LineAttributes) null);
			return;
		case TypePackage.GANTT_SERIES__OUTLINE_FILL:
			setOutlineFill((Fill) null);
			return;
		case TypePackage.GANTT_SERIES__USE_DECORATION_LABEL_VALUE:
			unsetUseDecorationLabelValue();
			return;
		case TypePackage.GANTT_SERIES__DECORATION_LABEL:
			setDecorationLabel((Label) null);
			return;
		case TypePackage.GANTT_SERIES__DECORATION_LABEL_POSITION:
			unsetDecorationLabelPosition();
			return;
		case TypePackage.GANTT_SERIES__PALETTE_LINE_COLOR:
			unsetPaletteLineColor();
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
		case TypePackage.GANTT_SERIES__START_MARKER:
			return startMarker != null;
		case TypePackage.GANTT_SERIES__START_MARKER_POSITION:
			return isSetStartMarkerPosition();
		case TypePackage.GANTT_SERIES__END_MARKER:
			return endMarker != null;
		case TypePackage.GANTT_SERIES__END_MARKER_POSITION:
			return isSetEndMarkerPosition();
		case TypePackage.GANTT_SERIES__CONNECTION_LINE:
			return connectionLine != null;
		case TypePackage.GANTT_SERIES__OUTLINE:
			return outline != null;
		case TypePackage.GANTT_SERIES__OUTLINE_FILL:
			return outlineFill != null;
		case TypePackage.GANTT_SERIES__USE_DECORATION_LABEL_VALUE:
			return isSetUseDecorationLabelValue();
		case TypePackage.GANTT_SERIES__DECORATION_LABEL:
			return decorationLabel != null;
		case TypePackage.GANTT_SERIES__DECORATION_LABEL_POSITION:
			return isSetDecorationLabelPosition();
		case TypePackage.GANTT_SERIES__PALETTE_LINE_COLOR:
			return isSetPaletteLineColor();
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
		result.append(" (startMarkerPosition: "); //$NON-NLS-1$
		if (startMarkerPositionESet) {
			result.append(startMarkerPosition);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(", endMarkerPosition: "); //$NON-NLS-1$
		if (endMarkerPositionESet) {
			result.append(endMarkerPosition);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(", useDecorationLabelValue: "); //$NON-NLS-1$
		if (useDecorationLabelValueESet) {
			result.append(useDecorationLabelValue);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(", decorationLabelPosition: "); //$NON-NLS-1$
		if (decorationLabelPositionESet) {
			result.append(decorationLabelPosition);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(", paletteLineColor: "); //$NON-NLS-1$
		if (paletteLineColorESet) {
			result.append(paletteLineColor);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(')');
		return result.toString();
	}

	/**
	 * A convenience method to create an initialized 'Series' instance
	 *
	 */
	public static Series create() {
		final GanttSeries gs = TypeFactory.eINSTANCE.createGanttSeries();
		((GanttSeriesImpl) gs).initialize();
		return gs;
	}

	/**
	 * Initializes all member variables within this object recursively
	 *
	 * Note: Manually written
	 */
	@Override
	protected final void initialize() {
		super.initialize();

		Marker sm = MarkerImpl.create(MarkerType.NABLA_LITERAL, 4);
		sm.setVisible(false);
		setStartMarker(sm);

		Marker em = MarkerImpl.create(MarkerType.NABLA_LITERAL, 4);
		setEndMarker(em);
		em.setVisible(false);

		setStartMarkerPosition(Position.BELOW_LITERAL);
		setEndMarkerPosition(Position.ABOVE_LITERAL);

		setLabelPosition(Position.ABOVE_LITERAL);

		LineAttributes la = LineAttributesImpl.create(ColorDefinitionImpl.BLACK(), LineStyle.SOLID_LITERAL, 8);
		this.setPaletteLineColor(true);
		setConnectionLine(la);

		la = LineAttributesImpl.create(ColorDefinitionImpl.BLACK(), LineStyle.SOLID_LITERAL, 1);
		la.setVisible(true);
		setOutline(la);

		Label lb = LabelImpl.create();
		setDecorationLabel(lb);
	}

	/**
	 * A convenience method to create an initialized 'Series' instance
	 *
	 */
	public static Series createDefault() {
		final GanttSeries gs = TypeFactory.eINSTANCE.createGanttSeries();
		((GanttSeriesImpl) gs).initDefault();
		return gs;
	}

	/**
	 * Initializes all member variables within this object recursively
	 *
	 * Note: Manually written
	 */
	@Override
	protected final void initDefault() {
		super.initDefault();

		Marker sm = MarkerImpl.createDefault(MarkerType.NABLA_LITERAL, 4, false);
		setStartMarker(sm);

		Marker em = MarkerImpl.createDefault(MarkerType.NABLA_LITERAL, 4, false);
		setEndMarker(em);

		startMarkerPosition = Position.BELOW_LITERAL;
		endMarkerPosition = Position.ABOVE_LITERAL;

		labelPosition = Position.ABOVE_LITERAL;

		LineAttributes la = LineAttributesImpl.createDefault(null, LineStyle.SOLID_LITERAL, 8);
		paletteLineColor = true;
		setConnectionLine(la);

		la = LineAttributesImpl.createDefault(null, LineStyle.SOLID_LITERAL, 1, true);
		setOutline(la);

		Label lb = LabelImpl.createDefault();
		setDecorationLabel(lb);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.model.component.Series#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		return Messages.getString("GanttSeriesImpl.displayName"); //$NON-NLS-1$
	}

	/**
	 * @generated
	 */
	@Override
	public GanttSeries copyInstance() {
		GanttSeriesImpl dest = new GanttSeriesImpl();
		dest.set(this);
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set(GanttSeries src) {

		super.set(src);

		// children

		if (src.getStartMarker() != null) {
			setStartMarker(src.getStartMarker().copyInstance());
		}

		if (src.getEndMarker() != null) {
			setEndMarker(src.getEndMarker().copyInstance());
		}

		if (src.getConnectionLine() != null) {
			setConnectionLine(src.getConnectionLine().copyInstance());
		}

		if (src.getOutline() != null) {
			setOutline(src.getOutline().copyInstance());
		}

		if (src.getOutlineFill() != null) {
			setOutlineFill(src.getOutlineFill().copyInstance());
		}

		if (src.getDecorationLabel() != null) {
			setDecorationLabel(src.getDecorationLabel().copyInstance());
		}

		// attributes

		startMarkerPosition = src.getStartMarkerPosition();

		startMarkerPositionESet = src.isSetStartMarkerPosition();

		endMarkerPosition = src.getEndMarkerPosition();

		endMarkerPositionESet = src.isSetEndMarkerPosition();

		useDecorationLabelValue = src.isUseDecorationLabelValue();

		useDecorationLabelValueESet = src.isSetUseDecorationLabelValue();

		decorationLabelPosition = src.getDecorationLabelPosition();

		decorationLabelPositionESet = src.isSetDecorationLabelPosition();

		paletteLineColor = src.isPaletteLineColor();

		paletteLineColorESet = src.isSetPaletteLineColor();

	}

	@Override
	public NameSet getLabelPositionScope(ChartDimension dimension) {
		return LiteralHelper.notOutPositionSet;
	}

	@Override
	public int[] getDefinedDataDefinitionIndex() {
		// Task label can be undefined
		return new int[] { 0, 1 };
	}

} // GanttSeriesImpl
