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
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.FontDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.GradientImpl;
import org.eclipse.birt.chart.model.attribute.impl.ImageImpl;
import org.eclipse.birt.chart.model.attribute.impl.InsetsImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.attribute.impl.Location3DImpl;
import org.eclipse.birt.chart.model.attribute.impl.LocationImpl;
import org.eclipse.birt.chart.model.attribute.impl.MarkerImpl;
import org.eclipse.birt.chart.model.attribute.impl.ScriptValueImpl;
import org.eclipse.birt.chart.model.attribute.impl.SeriesValueImpl;
import org.eclipse.birt.chart.model.attribute.impl.TextAlignmentImpl;
import org.eclipse.birt.chart.model.attribute.impl.TextImpl;
import org.eclipse.birt.chart.model.attribute.impl.TooltipValueImpl;
import org.eclipse.birt.chart.model.attribute.impl.URLValueImpl;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.impl.LabelImpl;
import org.eclipse.birt.chart.model.data.Action;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.model.data.impl.ActionImpl;
import org.eclipse.birt.chart.model.data.impl.TriggerImpl;

/**
 * 
 */

public class GObjectFactory implements IGObjectFactory {

	private static IGObjectFactory goFactory = new GObjectFactory();

	public static IGObjectFactory instance() {
		return goFactory;
	}

	public static void initInstance(IGObjectFactory tFactory) {
		goFactory = tFactory;
	}

	public Location createLocation(double dX, double dY) {
		return LocationImpl.create(dX, dY);
	}

	public Location[] createLocations(double[] xa, double[] ya) {
		return LocationImpl.create(xa, ya);
	}

	public Location3D createLocation3D(double x, double y, double z) {
		return Location3DImpl.create(x, y, z);
	}

	public Location3D[] createLocation3Ds(double[] xa, double[] ya, double[] za) {
		return Location3DImpl.create(xa, ya, za);
	}

	public ColorDefinition createColorDefinition(int iRed, int iGreen, int iBlue, int iAlpha) {
		return ColorDefinitionImpl.create(iRed, iGreen, iBlue, iAlpha);
	}

	public ColorDefinition createColorDefinition(int iRed, int iGreen, int iBlue) {
		return ColorDefinitionImpl.create(iRed, iGreen, iBlue);
	}

	public LineAttributes createLineAttributes(ColorDefinition cd, LineStyle ls, int iThickness) {
		return LineAttributesImpl.create(cd, ls, iThickness);
	}

	public TextAlignment createTextAlignment() {
		return TextAlignmentImpl.create();
	}

	public Text createText(String sValue) {
		return TextImpl.create(sValue);
	}

	public Insets createInsets(double dTop, double dLeft, double dBottom, double dRight) {
		return InsetsImpl.create(dTop, dLeft, dBottom, dRight);
	}

	public Label copyOf(Label src) {
		if (src == null) {
			return null;
		}
		return src.copyInstance();
	}

	public Label copyCompactLabel(Label src) {
		return LabelImpl.copyCompactInstance(src);
	}

	public Label createLabel() {
		return LabelImpl.create();
	}

	public ColorDefinition BLACK() {
		return ColorDefinitionImpl.BLACK();
	}

	public ColorDefinition TRANSPARENT() {
		return ColorDefinitionImpl.TRANSPARENT();
	}

	public ColorDefinition GREY() {
		return ColorDefinitionImpl.GREY();
	}

	public Gradient createGradient(ColorDefinition cdStart, ColorDefinition cdEnd, double dDirectionInDegrees,
			boolean bCyclic) {
		return GradientImpl.create(cdStart, cdEnd, dDirectionInDegrees, bCyclic);
	}

	public ColorDefinition copyOf(ColorDefinition src) {
		if (src == null) {
			return null;
		}
		return src.copyInstance();
	}

	public Fill copyOf(Fill src) {
		if (src == null) {
			return null;
		}
		return src.copyInstance();
	}

	public Gradient copyOf(Gradient src) {
		if (src == null) {
			return null;
		}
		return src.copyInstance();
	}

	public FontDefinition createFontDefinition(String sName, float fSize, boolean bBold, boolean bItalic,
			boolean bUnderline, boolean bStrikethrough, boolean bWordWrap, double dRotation, TextAlignment ta) {
		return FontDefinitionImpl.create(sName, fSize, bBold, bItalic, bUnderline, bStrikethrough, bWordWrap, dRotation,
				ta);
	}

	public Text copyOf(Text src) {
		if (src == null) {
			return null;
		}
		return src.copyInstance();
	}

	public FontDefinition copyOf(FontDefinition src) {
		if (src == null) {
			return null;
		}
		return src.copyInstance();
	}

	public Insets copyOf(Insets src) {
		if (src == null) {
			return null;
		}
		return src.copyInstance();
	}

	public LineAttributes copyOf(LineAttributes src) {
		if (src == null) {
			return null;
		}
		return src.copyInstance();
	}

	public Bounds copyOf(Bounds src) {
		if (src == null) {
			return null;
		}
		return src.copyInstance();
	}

	public Image copyOf(Image src) {
		if (src == null) {
			return null;
		}
		return src.copyInstance();
	}

	public Gradient createGradient(ColorDefinition cdStart, ColorDefinition cdEnd) {
		return GradientImpl.create(cdStart, cdEnd);
	}

	public Image createImage(String sUrl) {
		return ImageImpl.create(sUrl);
	}

	public ColorDefinition WHITE() {
		return ColorDefinitionImpl.WHITE();
	}

	public ColorDefinition RED() {
		return ColorDefinitionImpl.RED();
	}

	public ColorDefinition brighter(ColorDefinition src) {
		if (src == null) {
			return null;
		}
		return src.brighter();
	}

	public ColorDefinition darker(ColorDefinition src) {
		if (src == null) {
			return null;
		}
		return src.darker();
	}

	public ColorDefinition translucent(ColorDefinition src) {
		if (src == null) {
			return null;
		}
		ColorDefinition dest = copyOf(src);
		dest.setTransparency(127);
		return dest;
	}

	public Bounds createBounds(double dLeft, double dTop, double dWidth, double dHeight) {
		return BoundsImpl.create(dLeft, dTop, dWidth, dHeight);
	}

	public Bounds adjusteBounds(Bounds bo, Insets ins) {
		if (bo == null) {
			return null;
		}
		Bounds dest = copyOf(bo);
		dest.adjust(ins);
		return dest;
	}

	public Bounds scaleBounds(Bounds bo, double dScale) {
		if (bo == null) {
			return null;
		}
		Bounds dest = copyOf(bo);
		dest.scale(dScale);
		return dest;
	}

	public Bounds translateBounds(Bounds bo, double dTranslateX, double dTranslateY) {
		if (bo == null) {
			return null;
		}
		Bounds dest = copyOf(bo);
		dest.translate(dTranslateX, dTranslateY);
		return dest;
	}

	public Insets scaleInsets(Insets ins, double dScale) {
		if (ins == null) {
			return null;
		}
		return createInsets(ins.getTop() * dScale, ins.getLeft() * dScale, ins.getBottom() * dScale,
				ins.getRight() * dScale);
	}

	public Insets max(Insets ins1, Insets ins2) {
		return goFactory.createInsets(Math.max(ins1.getTop(), ins2.getTop()), Math.max(ins1.getLeft(), ins2.getLeft()),
				Math.max(ins1.getBottom(), ins2.getBottom()), Math.max(ins1.getRight(), ins2.getRight()));
	}

	public TextAlignment copyOf(TextAlignment src) {
		if (src == null) {
			return null;
		}
		return src.copyInstance();
	}

	public Trigger copyOf(Trigger src) {
		if (src == null) {
			return null;
		}
		return src.copyInstance();
	}

	public Trigger createTrigger(TriggerCondition tc, Action a) {
		return TriggerImpl.create(tc, a);
	}

	public Marker copyMarkerNoFill(Marker src) {
		return MarkerImpl.copyInstanceNoFill(src);
	}

	public ActionValue copyOf(ActionValue src) {
		if (src == null) {
			return null;
		}
		return src.copyInstance();
	}

	public Action copyOf(Action src) {
		if (src == null) {
			return null;
		}
		return src.copyInstance();
	}

	public Action createAction(ActionType at, ActionValue av) {
		return ActionImpl.create(at, av);
	}

	public ScriptValue createScriptValue(String script) {
		return ScriptValueImpl.create(script);
	}

	public SeriesValue createSeriesValue(String name) {
		return SeriesValueImpl.create(name);
	}

	public TooltipValue createTooltipValue(int iDelay, String sText) {
		return TooltipValueImpl.create(iDelay, sText);
	}

	public TooltipValue createTooltipValue(int iDelay, String sText, FormatSpecifier formatSpecifier) {
		return TooltipValueImpl.create(iDelay, sText, formatSpecifier);
	}

	public URLValue createURLValue(String sBaseUrl, String sTarget, String sBaseParameterName,
			String sValueParameterName, String sSeriesParameterName) {
		return URLValueImpl.create(sBaseUrl, sTarget, sBaseParameterName, sValueParameterName, sSeriesParameterName);
	}

}
