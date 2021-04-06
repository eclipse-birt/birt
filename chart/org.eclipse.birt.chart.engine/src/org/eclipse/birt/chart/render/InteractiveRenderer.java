/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.render;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.computation.GObjectFactory;
import org.eclipse.birt.chart.computation.IGObjectFactory;
import org.eclipse.birt.chart.device.IUpdateNotifier;
import org.eclipse.birt.chart.event.PrimitiveRenderEvent;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.event.StructureType;
import org.eclipse.birt.chart.event.WrappedStructureSource;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Gradient;
import org.eclipse.birt.chart.model.attribute.Image;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.MultipleFill;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.emf.common.util.EList;

public class InteractiveRenderer {

	private final Map<String, Set<ActionType>> targets = new HashMap<String, Set<ActionType>>();
	private IUpdateNotifier iun;

	private final Map<ColorDefinition, ColorDefinition> savedColors = new HashMap<ColorDefinition, ColorDefinition>();
	private final Map<LineAttributes, LineAttributes> savedLines = new HashMap<LineAttributes, LineAttributes>();
	private final Map<Label, Label> savedLabels = new HashMap<Label, Label>();

	private static final IGObjectFactory goFactory = GObjectFactory.instance();

	/**
	 * Register a chart element with an interactive action
	 * 
	 * @param target
	 * @param type
	 */
	public boolean registerAction(StructureSource target, ActionType type) {
		if (iun == null)
			return false;

		String source = getSource(target);
		Set<ActionType> set = targets.get(source);
		if (set != null) {
			return set.add(type);
		} else {
			set = new HashSet<ActionType>();
			set.add(type);
			targets.put(source, set);
			return true;
		}

	}

	public void unregisterAction(StructureSource target, ActionType type) {
		if (iun == null)
			return;

		Object source = getSource(target);
		Set<ActionType> set = targets.get(source);
		if (set != null && set.size() > 1) {
			set.remove(type);
		} else {
			targets.remove(source);
		}
	}

	public boolean isRegisteredAction(StructureSource target, ActionType type) {
		if (iun == null) {
			return false;
		}

		Object source = getSource(target);
		Set<ActionType> set = targets.get(source);
		if (set != null && set.size() > 0) {
			return set.contains(type);
		}
		return false;
	}

	public void modifyEvent(PrimitiveRenderEvent event) {
		if (iun == null)
			return;

		restoreEvent();

		String source = getSource((StructureSource) event.getSource());
		if (source == null)
			return;

		Set<ActionType> typeSet = targets.get(source);

		if (typeSet != null) {
			for (Iterator<ActionType> iter = typeSet.iterator(); iter.hasNext();) {
				ActionType type = iter.next();

				switch (type.getValue()) {
				case ActionType.HIGHLIGHT:
					highlight(event);
					break;
				case ActionType.TOGGLE_DATA_POINT_VISIBILITY:
					hideLabel(event);
					break;
				case ActionType.TOGGLE_VISIBILITY:
					hideElement(event);
					break;
				}
			}
		}
	}

	private void hideLabel(PrimitiveRenderEvent event) {
		Label label = event.getLabel();
		if (label != null) {
			saveLabel(label);
			label.setVisible(false);
		}

	}

	public String getSource(StructureSource src) {
		if (src.getType() == StructureType.SERIES || src.getType() == StructureType.SERIES_DATA_POINT) {

			if (isColoredByCategories()) {

				if (src.getSource() instanceof DataPointHints) {
					DataPointHints hints = (DataPointHints) src.getSource();

					int index = hints.getIndex();

					return "category_" + String.valueOf(index); //$NON-NLS-1$
				} else
					return null;
			} else {
				Series series;
				if (src.getType() == StructureType.SERIES) {
					series = (Series) src.getSource();
				} else {
					series = (Series) ((WrappedStructureSource) src).getParent().getSource();
				}

				return String.valueOf(series.hashCode());
			}
		} else if (src instanceof WrappedStructureSource) {
			return getSource(((WrappedStructureSource) src).getParent());
		}
		return null;
	}

	private boolean isColoredByCategories() {
		return this.iun.getRunTimeModel().getLegend().getItemType() == LegendItemType.CATEGORIES_LITERAL;
	}

	private void hideElement(PrimitiveRenderEvent event) {
		Fill fill = event.getBackground();
		if (fill != null) {
			hideFill(fill);
		}

		LineAttributes lineAttributes = event.getLineAttributes();
		if (lineAttributes != null) {
			saveLine(lineAttributes);
			lineAttributes.setVisible(false);
		}
		Label label = event.getLabel();
		if (label != null) {
			saveLabel(label);
			label.setVisible(false);
		}

	}

	private void hideFill(Fill fill) {

		if (fill instanceof ColorDefinition) {
			((ColorDefinition) fill).setTransparency(0);
		} else if (fill instanceof Gradient) {
			((Gradient) fill).setTransparency(0);
		} else if (fill instanceof Image) {
			// FIXME can't hide image
		} else if (fill instanceof MultipleFill) {
			EList<Fill> list = ((MultipleFill) fill).getFills();
			for (int i = 0; i < list.size(); i++) {
				hideFill(list.get(i));
			}
		}

	}

	private void highlight(PrimitiveRenderEvent event) {
		Fill fill = event.getBackground();
		if (fill != null) {
			highlightFill(fill);
		}
		LineAttributes lineAttributes = event.getLineAttributes();
		if (lineAttributes != null)
			highlightLine(lineAttributes);

		Label label = event.getLabel();
		if (label != null)
			highlightLabel(label);
	}

	private void highlightLabel(Label label) {
		saveLabel(label);
		ColorDefinition color = label.getCaption().getColor();
		if (color != null)
			color.brighter();

	}

	private void highlightLine(LineAttributes la) {
		saveLine(la);
		// la.setThickness( 3 );
		ColorDefinition color = la.getColor();
		if (color != null)
			color.brighter();

	}

	private void highlightFill(Fill fill) {
		if (fill instanceof ColorDefinition) {
			ColorDefinition cd = ((ColorDefinition) fill);
			cd.setRed((cd.getRed() + 255) / 2);
			cd.setGreen((cd.getGreen() + 255) / 2);
			cd.setBlue((cd.getBlue() + 255) / 2);
			saveColor(cd);
		} else if (fill instanceof Gradient) {
			highlightFill(((Gradient) fill).getStartColor());
			highlightFill(((Gradient) fill).getEndColor());
		} else if (fill instanceof Image) {
			// FIXME can't highlight image
		} else if (fill instanceof MultipleFill) {
			EList<Fill> list = ((MultipleFill) fill).getFills();
			for (int i = 0; i < list.size(); i++) {
				highlightFill(list.get(i));
			}
		}
	}

	private void saveColor(ColorDefinition cd) {
		if (!savedColors.containsKey(cd))
			savedColors.put(cd, goFactory.copyOf(cd));

	}

	private void saveLine(LineAttributes line) {
		if (!savedLines.containsKey(line))
			savedLines.put(line, goFactory.copyOf(line));
	}

	private void saveLabel(Label label) {
		if (!savedLabels.containsKey(label))
			savedLabels.put(label, goFactory.copyOf(label));
	}

	protected void restoreEvent() {
		if (iun == null)
			return;

		for (Iterator<ColorDefinition> iter = savedColors.keySet().iterator(); iter.hasNext();) {
			ColorDefinition original = iter.next();
			ColorDefinition copy = savedColors.get(original);
			original.setBlue(copy.getBlue());
			original.setRed(copy.getRed());
			original.setGreen(copy.getGreen());
			original.setTransparency(copy.getTransparency());
		}

		savedColors.clear();

		for (Iterator<LineAttributes> iter = savedLines.keySet().iterator(); iter.hasNext();) {
			LineAttributes original = iter.next();
			LineAttributes copy = savedLines.get(original);

			original.setVisible(copy.isVisible());
		}
		savedLines.clear();

		for (Iterator<Label> iter = savedLabels.keySet().iterator(); iter.hasNext();) {
			Label original = iter.next();
			Label copy = savedLabels.get(original);
			original.setVisible(copy.isVisible());
			original.getCaption().setFont(copy.getCaption().getFont());
		}
		savedLabels.clear();

	}

	public void setUpdateNotifier(IUpdateNotifier _iun) {
		this.iun = _iun;
	}

	public void reset() {
		iun = null;
		savedColors.clear();
		savedLabels.clear();
		savedLines.clear();
		targets.clear();
	}

}
