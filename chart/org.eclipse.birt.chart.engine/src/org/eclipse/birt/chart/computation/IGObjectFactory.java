/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.computation;

import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.ActionValue;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.Gradient;
import org.eclipse.birt.chart.model.attribute.Image;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Location3D;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.ScriptValue;
import org.eclipse.birt.chart.model.attribute.SeriesValue;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.model.attribute.TextAlignment;
import org.eclipse.birt.chart.model.attribute.TooltipValue;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.attribute.URLValue;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.data.Action;
import org.eclipse.birt.chart.model.data.Trigger;

/**
 * 
 */

public interface IGObjectFactory {

	Location createLocation(double dX, double dY);

	Location[] createLocations(double[] xa, double[] ya);

	Location3D createLocation3D(double x, double y, double z);

	Location3D[] createLocation3Ds(double[] xa, double[] ya, double[] za);

	ColorDefinition createColorDefinition(int iRed, int iGreen, int iBlue, int iAlpha);

	ColorDefinition createColorDefinition(int iRed, int iGreen, int iBlue);

	LineAttributes createLineAttributes(ColorDefinition cd, LineStyle ls, int iThickness);

	TextAlignment createTextAlignment();

	Text createText(String sValue);

	Insets createInsets(double dTop, double dLeft, double dBottom, double dRight);

	Label copyOf(Label src);

	Text copyOf(Text src);

	TextAlignment copyOf(TextAlignment src);

	LineAttributes copyOf(LineAttributes src);

	Bounds copyOf(Bounds src);

	FontDefinition copyOf(FontDefinition src);

	ColorDefinition copyOf(ColorDefinition src);

	Fill copyOf(Fill src);

	Insets copyOf(Insets src);

	Trigger copyOf(Trigger src);

	Gradient copyOf(Gradient src);

	ActionValue copyOf(ActionValue src);

	Action copyOf(Action src);

	Label createLabel();

	Label copyCompactLabel(Label src);

	ColorDefinition TRANSPARENT();

	ColorDefinition BLACK();

	ColorDefinition WHITE();

	ColorDefinition GREY();

	ColorDefinition RED();

	Gradient createGradient(ColorDefinition cdStart, ColorDefinition cdEnd, double dDirectionInDegrees,
			boolean bCyclic);

	Gradient createGradient(ColorDefinition cdStart, ColorDefinition cdEnd);

	FontDefinition createFontDefinition(String sName, float fSize, boolean bBold, boolean bItalic, boolean bUnderline,
			boolean bStrikethrough, boolean bWordWrap, double dRotation, TextAlignment ta);

	Image createImage(String sUrl);

	Image copyOf(Image src);

	ColorDefinition brighter(ColorDefinition src);

	ColorDefinition darker(ColorDefinition src);

	ColorDefinition translucent(ColorDefinition src);

	Bounds createBounds(double dLeft, double dTop, double dWidth, double dHeight);

	Bounds adjusteBounds(Bounds bo, Insets ins);

	Bounds scaleBounds(Bounds bo, double dScale);

	Bounds translateBounds(Bounds bo, double dTranslateX, double dTranslateY);

	Insets scaleInsets(Insets ins, double dScale);

	Insets max(Insets ins1, Insets ins2);

	Trigger createTrigger(TriggerCondition tc, Action a);

	Marker copyMarkerNoFill(Marker src);

	Action createAction(ActionType at, ActionValue av);

	ScriptValue createScriptValue(String script);

	SeriesValue createSeriesValue(String name);

	TooltipValue createTooltipValue(int iDelay, String sText);

	TooltipValue createTooltipValue(int iDelay, String sText, FormatSpecifier formatSpecifier);

	URLValue createURLValue(String sBaseUrl, String sTarget, String sBaseParameterName, String sValueParameterName,
			String sSeriesParameterName);
}
