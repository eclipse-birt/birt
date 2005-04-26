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

package org.eclipse.birt.chart.model.layout.impl;

import java.util.Collection;

import org.eclipse.birt.chart.computation.LegendBuilder;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.exception.GenerationException;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Direction;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.Size;
import org.eclipse.birt.chart.model.attribute.Stretch;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.model.attribute.impl.TextImpl;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.model.layout.ClientArea;
import org.eclipse.birt.chart.model.layout.LayoutFactory;
import org.eclipse.birt.chart.model.layout.LayoutPackage;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Legend</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.LegendImpl#getHorizontalSpacing <em>Horizontal Spacing</em>}
 * </li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.LegendImpl#getVerticalSpacing <em>Vertical Spacing</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.LegendImpl#getClientArea <em>Client Area</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.LegendImpl#getText <em>Text</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.LegendImpl#getOrientation <em>Orientation</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.LegendImpl#getDirection <em>Direction</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.LegendImpl#getSeparator <em>Separator</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.LegendImpl#getPosition <em>Position</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.layout.impl.LegendImpl#getItemType <em>Item Type</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class LegendImpl extends BlockImpl implements Legend
{

    /**
     * The default value of the '{@link #getHorizontalSpacing() <em>Horizontal Spacing</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getHorizontalSpacing()
     * @generated
     * @ordered
     */
    protected static final int HORIZONTAL_SPACING_EDEFAULT = 0;

    /**
     * The cached value of the '{@link #getHorizontalSpacing() <em>Horizontal Spacing</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getHorizontalSpacing()
     * @generated
     * @ordered
     */
    protected int horizontalSpacing = HORIZONTAL_SPACING_EDEFAULT;

    /**
     * This is true if the Horizontal Spacing attribute has been set. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    protected boolean horizontalSpacingESet = false;

    /**
     * The default value of the '{@link #getVerticalSpacing() <em>Vertical Spacing</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getVerticalSpacing()
     * @generated
     * @ordered
     */
    protected static final int VERTICAL_SPACING_EDEFAULT = 0;

    /**
     * The cached value of the '{@link #getVerticalSpacing() <em>Vertical Spacing</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getVerticalSpacing()
     * @generated
     * @ordered
     */
    protected int verticalSpacing = VERTICAL_SPACING_EDEFAULT;

    /**
     * This is true if the Vertical Spacing attribute has been set. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    protected boolean verticalSpacingESet = false;

    /**
     * The cached value of the '{@link #getClientArea() <em>Client Area</em>}' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getClientArea()
     * @generated
     * @ordered
     */
    protected ClientArea clientArea = null;

    /**
     * The cached value of the '{@link #getText() <em>Text</em>}' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #getText()
     * @generated
     * @ordered
     */
    protected Text text = null;

    /**
     * The default value of the '{@link #getOrientation() <em>Orientation</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getOrientation()
     * @generated
     * @ordered
     */
    protected static final Orientation ORIENTATION_EDEFAULT = Orientation.HORIZONTAL_LITERAL;

    /**
     * The cached value of the '{@link #getOrientation() <em>Orientation</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getOrientation()
     * @generated
     * @ordered
     */
    protected Orientation orientation = ORIENTATION_EDEFAULT;

    /**
     * This is true if the Orientation attribute has been set. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    protected boolean orientationESet = false;

    /**
     * The default value of the '{@link #getDirection() <em>Direction</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #getDirection()
     * @generated
     * @ordered
     */
    protected static final Direction DIRECTION_EDEFAULT = Direction.LEFT_RIGHT_LITERAL;

    /**
     * The cached value of the '{@link #getDirection() <em>Direction</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #getDirection()
     * @generated
     * @ordered
     */
    protected Direction direction = DIRECTION_EDEFAULT;

    /**
     * This is true if the Direction attribute has been set. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    protected boolean directionESet = false;

    /**
     * The cached value of the '{@link #getSeparator() <em>Separator</em>}' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @see #getSeparator()
     * @generated
     * @ordered
     */
    protected LineAttributes separator = null;

    /**
     * The default value of the '{@link #getPosition() <em>Position</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #getPosition()
     * @generated
     * @ordered
     */
    protected static final Position POSITION_EDEFAULT = Position.ABOVE_LITERAL;

    /**
     * The cached value of the '{@link #getPosition() <em>Position</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #getPosition()
     * @generated
     * @ordered
     */
    protected Position position = POSITION_EDEFAULT;

    /**
     * This is true if the Position attribute has been set. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    protected boolean positionESet = false;

    /**
     * The default value of the '{@link #getItemType() <em>Item Type</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #getItemType()
     * @generated
     * @ordered
     */
    protected static final LegendItemType ITEM_TYPE_EDEFAULT = LegendItemType.SERIES_LITERAL;

    /**
     * The cached value of the '{@link #getItemType() <em>Item Type</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #getItemType()
     * @generated
     * @ordered
     */
    protected LegendItemType itemType = ITEM_TYPE_EDEFAULT;

    /**
     * This is true if the Item Type attribute has been set. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    protected boolean itemTypeESet = false;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected LegendImpl()
    {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected EClass eStaticClass()
    {
        return LayoutPackage.eINSTANCE.getLegend();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public int getHorizontalSpacing()
    {
        return horizontalSpacing;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setHorizontalSpacing(int newHorizontalSpacing)
    {
        int oldHorizontalSpacing = horizontalSpacing;
        horizontalSpacing = newHorizontalSpacing;
        boolean oldHorizontalSpacingESet = horizontalSpacingESet;
        horizontalSpacingESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.LEGEND__HORIZONTAL_SPACING,
                oldHorizontalSpacing, horizontalSpacing, !oldHorizontalSpacingESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void unsetHorizontalSpacing()
    {
        int oldHorizontalSpacing = horizontalSpacing;
        boolean oldHorizontalSpacingESet = horizontalSpacingESet;
        horizontalSpacing = HORIZONTAL_SPACING_EDEFAULT;
        horizontalSpacingESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, LayoutPackage.LEGEND__HORIZONTAL_SPACING,
                oldHorizontalSpacing, HORIZONTAL_SPACING_EDEFAULT, oldHorizontalSpacingESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isSetHorizontalSpacing()
    {
        return horizontalSpacingESet;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public int getVerticalSpacing()
    {
        return verticalSpacing;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setVerticalSpacing(int newVerticalSpacing)
    {
        int oldVerticalSpacing = verticalSpacing;
        verticalSpacing = newVerticalSpacing;
        boolean oldVerticalSpacingESet = verticalSpacingESet;
        verticalSpacingESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.LEGEND__VERTICAL_SPACING,
                oldVerticalSpacing, verticalSpacing, !oldVerticalSpacingESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void unsetVerticalSpacing()
    {
        int oldVerticalSpacing = verticalSpacing;
        boolean oldVerticalSpacingESet = verticalSpacingESet;
        verticalSpacing = VERTICAL_SPACING_EDEFAULT;
        verticalSpacingESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, LayoutPackage.LEGEND__VERTICAL_SPACING,
                oldVerticalSpacing, VERTICAL_SPACING_EDEFAULT, oldVerticalSpacingESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isSetVerticalSpacing()
    {
        return verticalSpacingESet;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public ClientArea getClientArea()
    {
        return clientArea;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public NotificationChain basicSetClientArea(ClientArea newClientArea, NotificationChain msgs)
    {
        ClientArea oldClientArea = clientArea;
        clientArea = newClientArea;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                LayoutPackage.LEGEND__CLIENT_AREA, oldClientArea, newClientArea);
            if (msgs == null)
                msgs = notification;
            else
                msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setClientArea(ClientArea newClientArea)
    {
        if (newClientArea != clientArea)
        {
            NotificationChain msgs = null;
            if (clientArea != null)
                msgs = ((InternalEObject) clientArea).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
                    - LayoutPackage.LEGEND__CLIENT_AREA, null, msgs);
            if (newClientArea != null)
                msgs = ((InternalEObject) newClientArea).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
                    - LayoutPackage.LEGEND__CLIENT_AREA, null, msgs);
            msgs = basicSetClientArea(newClientArea, msgs);
            if (msgs != null)
                msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.LEGEND__CLIENT_AREA, newClientArea,
                newClientArea));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public Text getText()
    {
        return text;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public NotificationChain basicSetText(Text newText, NotificationChain msgs)
    {
        Text oldText = text;
        text = newText;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, LayoutPackage.LEGEND__TEXT,
                oldText, newText);
            if (msgs == null)
                msgs = notification;
            else
                msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setText(Text newText)
    {
        if (newText != text)
        {
            NotificationChain msgs = null;
            if (text != null)
                msgs = ((InternalEObject) text).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
                    - LayoutPackage.LEGEND__TEXT, null, msgs);
            if (newText != null)
                msgs = ((InternalEObject) newText).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
                    - LayoutPackage.LEGEND__TEXT, null, msgs);
            msgs = basicSetText(newText, msgs);
            if (msgs != null)
                msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.LEGEND__TEXT, newText, newText));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public Orientation getOrientation()
    {
        return orientation;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setOrientation(Orientation newOrientation)
    {
        Orientation oldOrientation = orientation;
        orientation = newOrientation == null ? ORIENTATION_EDEFAULT : newOrientation;
        boolean oldOrientationESet = orientationESet;
        orientationESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.LEGEND__ORIENTATION, oldOrientation,
                orientation, !oldOrientationESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void unsetOrientation()
    {
        Orientation oldOrientation = orientation;
        boolean oldOrientationESet = orientationESet;
        orientation = ORIENTATION_EDEFAULT;
        orientationESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, LayoutPackage.LEGEND__ORIENTATION, oldOrientation,
                ORIENTATION_EDEFAULT, oldOrientationESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isSetOrientation()
    {
        return orientationESet;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public Direction getDirection()
    {
        return direction;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setDirection(Direction newDirection)
    {
        Direction oldDirection = direction;
        direction = newDirection == null ? DIRECTION_EDEFAULT : newDirection;
        boolean oldDirectionESet = directionESet;
        directionESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.LEGEND__DIRECTION, oldDirection,
                direction, !oldDirectionESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void unsetDirection()
    {
        Direction oldDirection = direction;
        boolean oldDirectionESet = directionESet;
        direction = DIRECTION_EDEFAULT;
        directionESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, LayoutPackage.LEGEND__DIRECTION, oldDirection,
                DIRECTION_EDEFAULT, oldDirectionESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isSetDirection()
    {
        return directionESet;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public LineAttributes getSeparator()
    {
        return separator;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public NotificationChain basicSetSeparator(LineAttributes newSeparator, NotificationChain msgs)
    {
        LineAttributes oldSeparator = separator;
        separator = newSeparator;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                LayoutPackage.LEGEND__SEPARATOR, oldSeparator, newSeparator);
            if (msgs == null)
                msgs = notification;
            else
                msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setSeparator(LineAttributes newSeparator)
    {
        if (newSeparator != separator)
        {
            NotificationChain msgs = null;
            if (separator != null)
                msgs = ((InternalEObject) separator).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
                    - LayoutPackage.LEGEND__SEPARATOR, null, msgs);
            if (newSeparator != null)
                msgs = ((InternalEObject) newSeparator).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
                    - LayoutPackage.LEGEND__SEPARATOR, null, msgs);
            msgs = basicSetSeparator(newSeparator, msgs);
            if (msgs != null)
                msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.LEGEND__SEPARATOR, newSeparator,
                newSeparator));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public Position getPosition()
    {
        return position;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setPosition(Position newPosition)
    {
        Position oldPosition = position;
        position = newPosition == null ? POSITION_EDEFAULT : newPosition;
        boolean oldPositionESet = positionESet;
        positionESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.LEGEND__POSITION, oldPosition,
                position, !oldPositionESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void unsetPosition()
    {
        Position oldPosition = position;
        boolean oldPositionESet = positionESet;
        position = POSITION_EDEFAULT;
        positionESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, LayoutPackage.LEGEND__POSITION, oldPosition,
                POSITION_EDEFAULT, oldPositionESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isSetPosition()
    {
        return positionESet;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public LegendItemType getItemType()
    {
        return itemType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setItemType(LegendItemType newItemType)
    {
        LegendItemType oldItemType = itemType;
        itemType = newItemType == null ? ITEM_TYPE_EDEFAULT : newItemType;
        boolean oldItemTypeESet = itemTypeESet;
        itemTypeESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, LayoutPackage.LEGEND__ITEM_TYPE, oldItemType,
                itemType, !oldItemTypeESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void unsetItemType()
    {
        LegendItemType oldItemType = itemType;
        boolean oldItemTypeESet = itemTypeESet;
        itemType = ITEM_TYPE_EDEFAULT;
        itemTypeESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, LayoutPackage.LEGEND__ITEM_TYPE, oldItemType,
                ITEM_TYPE_EDEFAULT, oldItemTypeESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isSetItemType()
    {
        return itemTypeESet;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass,
        NotificationChain msgs)
    {
        if (featureID >= 0)
        {
            switch (eDerivedStructuralFeatureID(featureID, baseClass))
            {
                case LayoutPackage.LEGEND__CHILDREN:
                    return ((InternalEList) getChildren()).basicRemove(otherEnd, msgs);
                case LayoutPackage.LEGEND__BOUNDS:
                    return basicSetBounds(null, msgs);
                case LayoutPackage.LEGEND__INSETS:
                    return basicSetInsets(null, msgs);
                case LayoutPackage.LEGEND__MIN_SIZE:
                    return basicSetMinSize(null, msgs);
                case LayoutPackage.LEGEND__OUTLINE:
                    return basicSetOutline(null, msgs);
                case LayoutPackage.LEGEND__BACKGROUND:
                    return basicSetBackground(null, msgs);
                case LayoutPackage.LEGEND__TRIGGERS:
                    return ((InternalEList) getTriggers()).basicRemove(otherEnd, msgs);
                case LayoutPackage.LEGEND__CLIENT_AREA:
                    return basicSetClientArea(null, msgs);
                case LayoutPackage.LEGEND__TEXT:
                    return basicSetText(null, msgs);
                case LayoutPackage.LEGEND__SEPARATOR:
                    return basicSetSeparator(null, msgs);
                default:
                    return eDynamicInverseRemove(otherEnd, featureID, baseClass, msgs);
            }
        }
        return eBasicSetContainer(null, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public Object eGet(EStructuralFeature eFeature, boolean resolve)
    {
        switch (eDerivedStructuralFeatureID(eFeature))
        {
            case LayoutPackage.LEGEND__CHILDREN:
                return getChildren();
            case LayoutPackage.LEGEND__BOUNDS:
                return getBounds();
            case LayoutPackage.LEGEND__ANCHOR:
                return getAnchor();
            case LayoutPackage.LEGEND__STRETCH:
                return getStretch();
            case LayoutPackage.LEGEND__INSETS:
                return getInsets();
            case LayoutPackage.LEGEND__ROW:
                return new Integer(getRow());
            case LayoutPackage.LEGEND__COLUMN:
                return new Integer(getColumn());
            case LayoutPackage.LEGEND__ROWSPAN:
                return new Integer(getRowspan());
            case LayoutPackage.LEGEND__COLUMNSPAN:
                return new Integer(getColumnspan());
            case LayoutPackage.LEGEND__MIN_SIZE:
                return getMinSize();
            case LayoutPackage.LEGEND__OUTLINE:
                return getOutline();
            case LayoutPackage.LEGEND__BACKGROUND:
                return getBackground();
            case LayoutPackage.LEGEND__VISIBLE:
                return isVisible() ? Boolean.TRUE : Boolean.FALSE;
            case LayoutPackage.LEGEND__TRIGGERS:
                return getTriggers();
            case LayoutPackage.LEGEND__HORIZONTAL_SPACING:
                return new Integer(getHorizontalSpacing());
            case LayoutPackage.LEGEND__VERTICAL_SPACING:
                return new Integer(getVerticalSpacing());
            case LayoutPackage.LEGEND__CLIENT_AREA:
                return getClientArea();
            case LayoutPackage.LEGEND__TEXT:
                return getText();
            case LayoutPackage.LEGEND__ORIENTATION:
                return getOrientation();
            case LayoutPackage.LEGEND__DIRECTION:
                return getDirection();
            case LayoutPackage.LEGEND__SEPARATOR:
                return getSeparator();
            case LayoutPackage.LEGEND__POSITION:
                return getPosition();
            case LayoutPackage.LEGEND__ITEM_TYPE:
                return getItemType();
        }
        return eDynamicGet(eFeature, resolve);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void eSet(EStructuralFeature eFeature, Object newValue)
    {
        switch (eDerivedStructuralFeatureID(eFeature))
        {
            case LayoutPackage.LEGEND__CHILDREN:
                getChildren().clear();
                getChildren().addAll((Collection) newValue);
                return;
            case LayoutPackage.LEGEND__BOUNDS:
                setBounds((Bounds) newValue);
                return;
            case LayoutPackage.LEGEND__ANCHOR:
                setAnchor((Anchor) newValue);
                return;
            case LayoutPackage.LEGEND__STRETCH:
                setStretch((Stretch) newValue);
                return;
            case LayoutPackage.LEGEND__INSETS:
                setInsets((Insets) newValue);
                return;
            case LayoutPackage.LEGEND__ROW:
                setRow(((Integer) newValue).intValue());
                return;
            case LayoutPackage.LEGEND__COLUMN:
                setColumn(((Integer) newValue).intValue());
                return;
            case LayoutPackage.LEGEND__ROWSPAN:
                setRowspan(((Integer) newValue).intValue());
                return;
            case LayoutPackage.LEGEND__COLUMNSPAN:
                setColumnspan(((Integer) newValue).intValue());
                return;
            case LayoutPackage.LEGEND__MIN_SIZE:
                setMinSize((Size) newValue);
                return;
            case LayoutPackage.LEGEND__OUTLINE:
                setOutline((LineAttributes) newValue);
                return;
            case LayoutPackage.LEGEND__BACKGROUND:
                setBackground((Fill) newValue);
                return;
            case LayoutPackage.LEGEND__VISIBLE:
                setVisible(((Boolean) newValue).booleanValue());
                return;
            case LayoutPackage.LEGEND__TRIGGERS:
                getTriggers().clear();
                getTriggers().addAll((Collection) newValue);
                return;
            case LayoutPackage.LEGEND__HORIZONTAL_SPACING:
                setHorizontalSpacing(((Integer) newValue).intValue());
                return;
            case LayoutPackage.LEGEND__VERTICAL_SPACING:
                setVerticalSpacing(((Integer) newValue).intValue());
                return;
            case LayoutPackage.LEGEND__CLIENT_AREA:
                setClientArea((ClientArea) newValue);
                return;
            case LayoutPackage.LEGEND__TEXT:
                setText((Text) newValue);
                return;
            case LayoutPackage.LEGEND__ORIENTATION:
                setOrientation((Orientation) newValue);
                return;
            case LayoutPackage.LEGEND__DIRECTION:
                setDirection((Direction) newValue);
                return;
            case LayoutPackage.LEGEND__SEPARATOR:
                setSeparator((LineAttributes) newValue);
                return;
            case LayoutPackage.LEGEND__POSITION:
                setPosition((Position) newValue);
                return;
            case LayoutPackage.LEGEND__ITEM_TYPE:
                setItemType((LegendItemType) newValue);
                return;
        }
        eDynamicSet(eFeature, newValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void eUnset(EStructuralFeature eFeature)
    {
        switch (eDerivedStructuralFeatureID(eFeature))
        {
            case LayoutPackage.LEGEND__CHILDREN:
                getChildren().clear();
                return;
            case LayoutPackage.LEGEND__BOUNDS:
                setBounds((Bounds) null);
                return;
            case LayoutPackage.LEGEND__ANCHOR:
                unsetAnchor();
                return;
            case LayoutPackage.LEGEND__STRETCH:
                unsetStretch();
                return;
            case LayoutPackage.LEGEND__INSETS:
                setInsets((Insets) null);
                return;
            case LayoutPackage.LEGEND__ROW:
                unsetRow();
                return;
            case LayoutPackage.LEGEND__COLUMN:
                unsetColumn();
                return;
            case LayoutPackage.LEGEND__ROWSPAN:
                unsetRowspan();
                return;
            case LayoutPackage.LEGEND__COLUMNSPAN:
                unsetColumnspan();
                return;
            case LayoutPackage.LEGEND__MIN_SIZE:
                setMinSize((Size) null);
                return;
            case LayoutPackage.LEGEND__OUTLINE:
                setOutline((LineAttributes) null);
                return;
            case LayoutPackage.LEGEND__BACKGROUND:
                setBackground((Fill) null);
                return;
            case LayoutPackage.LEGEND__VISIBLE:
                unsetVisible();
                return;
            case LayoutPackage.LEGEND__TRIGGERS:
                getTriggers().clear();
                return;
            case LayoutPackage.LEGEND__HORIZONTAL_SPACING:
                unsetHorizontalSpacing();
                return;
            case LayoutPackage.LEGEND__VERTICAL_SPACING:
                unsetVerticalSpacing();
                return;
            case LayoutPackage.LEGEND__CLIENT_AREA:
                setClientArea((ClientArea) null);
                return;
            case LayoutPackage.LEGEND__TEXT:
                setText((Text) null);
                return;
            case LayoutPackage.LEGEND__ORIENTATION:
                unsetOrientation();
                return;
            case LayoutPackage.LEGEND__DIRECTION:
                unsetDirection();
                return;
            case LayoutPackage.LEGEND__SEPARATOR:
                setSeparator((LineAttributes) null);
                return;
            case LayoutPackage.LEGEND__POSITION:
                unsetPosition();
                return;
            case LayoutPackage.LEGEND__ITEM_TYPE:
                unsetItemType();
                return;
        }
        eDynamicUnset(eFeature);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean eIsSet(EStructuralFeature eFeature)
    {
        switch (eDerivedStructuralFeatureID(eFeature))
        {
            case LayoutPackage.LEGEND__CHILDREN:
                return children != null && !children.isEmpty();
            case LayoutPackage.LEGEND__BOUNDS:
                return bounds != null;
            case LayoutPackage.LEGEND__ANCHOR:
                return isSetAnchor();
            case LayoutPackage.LEGEND__STRETCH:
                return isSetStretch();
            case LayoutPackage.LEGEND__INSETS:
                return insets != null;
            case LayoutPackage.LEGEND__ROW:
                return isSetRow();
            case LayoutPackage.LEGEND__COLUMN:
                return isSetColumn();
            case LayoutPackage.LEGEND__ROWSPAN:
                return isSetRowspan();
            case LayoutPackage.LEGEND__COLUMNSPAN:
                return isSetColumnspan();
            case LayoutPackage.LEGEND__MIN_SIZE:
                return minSize != null;
            case LayoutPackage.LEGEND__OUTLINE:
                return outline != null;
            case LayoutPackage.LEGEND__BACKGROUND:
                return background != null;
            case LayoutPackage.LEGEND__VISIBLE:
                return isSetVisible();
            case LayoutPackage.LEGEND__TRIGGERS:
                return triggers != null && !triggers.isEmpty();
            case LayoutPackage.LEGEND__HORIZONTAL_SPACING:
                return isSetHorizontalSpacing();
            case LayoutPackage.LEGEND__VERTICAL_SPACING:
                return isSetVerticalSpacing();
            case LayoutPackage.LEGEND__CLIENT_AREA:
                return clientArea != null;
            case LayoutPackage.LEGEND__TEXT:
                return text != null;
            case LayoutPackage.LEGEND__ORIENTATION:
                return isSetOrientation();
            case LayoutPackage.LEGEND__DIRECTION:
                return isSetDirection();
            case LayoutPackage.LEGEND__SEPARATOR:
                return separator != null;
            case LayoutPackage.LEGEND__POSITION:
                return isSetPosition();
            case LayoutPackage.LEGEND__ITEM_TYPE:
                return isSetItemType();
        }
        return eDynamicIsSet(eFeature);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public String toString()
    {
        if (eIsProxy())
            return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (horizontalSpacing: ");
        if (horizontalSpacingESet)
            result.append(horizontalSpacing);
        else
            result.append("<unset>");
        result.append(", verticalSpacing: ");
        if (verticalSpacingESet)
            result.append(verticalSpacing);
        else
            result.append("<unset>");
        result.append(", orientation: ");
        if (orientationESet)
            result.append(orientation);
        else
            result.append("<unset>");
        result.append(", direction: ");
        if (directionESet)
            result.append(direction);
        else
            result.append("<unset>");
        result.append(", position: ");
        if (positionESet)
            result.append(position);
        else
            result.append("<unset>");
        result.append(", itemType: ");
        if (itemTypeESet)
            result.append(itemType);
        else
            result.append("<unset>");
        result.append(')');
        return result.toString();
    }

    /**
     * 
     * Note: Manually written
     * 
     * @return
     */
    public boolean isLegend()
    {
        return true;
    }

    /**
     * 
     * Note: Manually written
     * 
     * @return
     */
    public boolean isCustom()
    {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.model.layout.Block#getPreferredSize(org.eclipse.birt.chart.device.IDisplayServer,
     *      org.eclipse.birt.chart.model.Chart, org.eclipse.birt.chart.factory.RunTimeContext)
     */
    public final Size getPreferredSize(IDisplayServer xs, Chart cm, RunTimeContext rtc) throws GenerationException
    {
        // COMPUTE THE LEGEND CONTENT (TO ENSURE THAT THE PREFERRED SIZE IS OBTAINED)
        final LegendBuilder lb = new LegendBuilder();
        final SeriesDefinition[] seda = cm.getSeriesForLegend();

        Size sz = null;
        sz = lb.compute(xs, cm, seda);
        sz.scale(72d / xs.getDpiResolution()); // CONVERT TO POINTS
        final Insets ins = this.getInsets();
        sz.setWidth(sz.getWidth() + ins.getLeft() + ins.getRight());
        sz.setHeight(sz.getHeight() + ins.getTop() + ins.getBottom());
        return sz;
    }

    /**
     * A convenience method to create an initialized 'Legendt' instance
     * 
     * @return
     */
    public static final Block create()
    {
        final Legend lg = LayoutFactory.eINSTANCE.createLegend();
        ((LegendImpl) lg).initialize();
        return lg;
    }

    /**
     * Resets all member variables within this object recursively
     * 
     * Note: Manually written
     */
    protected final void initialize()
    {
        super.initialize();
        setPosition(Position.RIGHT_LITERAL);
        setOrientation(Orientation.VERTICAL_LITERAL);
        setDirection(Direction.TOP_BOTTOM_LITERAL);
        setItemType(LegendItemType.SERIES_LITERAL);

        final ClientArea ca = LayoutFactory.eINSTANCE.createClientArea();
        ((ClientAreaImpl) ca).initialize();
        ca.getInsets().set(2, 2, 2, 2);
        setClientArea(ca);

        setText(TextImpl.create(null));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.model.layout.Legend#updateLayout(org.eclipse.birt.chart.model.Chart)
     */
    public final void updateLayout(Chart cm)
    {
        final Legend lg = this;
        final Plot pl = cm.getPlot();
        final EList el = pl.getChildren();
        final Position p = lg.getPosition();
        final boolean bLegendInsidePlot = p.getValue() == Position.INSIDE;
        final boolean bPlotContainsLegend = el.indexOf(lg) >= 0;

        // IF PLOT DOESNT CONTAIN LEGEND AND LEGEND IS SUPPOSED TO BE INSIDE
        if (!bPlotContainsLegend && bLegendInsidePlot)
        {
            el.add(lg); // ADD LEGEND TO PLOT
        }
        else if (bPlotContainsLegend && !bLegendInsidePlot)
        {
            cm.getBlock().getChildren().add(lg); // ADD LEGEND TO BLOCK
        }
        else
        {
            // PLOT/LEGEND RELATIONSHIP IS FINE; DON'T DO ANYTHING
        }
    }

} //LegendImpl
