
package org.eclipse.birt.report.engine.css.dom;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.CSSStylableElement;
import org.eclipse.birt.report.engine.css.engine.value.Value;
import org.w3c.dom.css.CSSValue;

public class ComputedStyle extends AbstractStyle {
	Map<String, ComputedStyle> cachedStyles = new HashMap<String, ComputedStyle>();
	boolean[] caculated;
	CSSStylableElement elt;
	CSSValue[] values;

	public ComputedStyle(CSSStylableElement elt) {
		super(elt.getCSSEngine());
		this.elt = elt;
	}

	public CSSValue getProperty(int index) {
		if (values == null) {
			values = new CSSValue[NUMBER_OF_STYLE];
			caculated = new boolean[NUMBER_OF_STYLE];
		}
		if (caculated[index]) {
			return values[index];
		}

		Value cv = resolveProperty(index);

		values[index] = cv;
		caculated[index] = true;

		return cv;
	}

	// TODO: review, move the engine.resolveStyle here, so we needn't call
	// parent.getComputedStyle() for none-inheireted styles.
	protected Value resolveProperty(int index) {
		CSSStylableElement parent = (CSSStylableElement) elt.getParent();
		IStyle pcs = null;
		if (parent != null) {
			pcs = parent.getComputedStyle();
		}

		// get the specified style
		IStyle s = elt.getStyle();

		Value sv = s != null ? (Value) s.getProperty(index) : null;
		Value cv = engine.resolveStyle(elt, index, sv, pcs);

		return cv;
	}

	public boolean isEmpty() {
		return false;
	}

	public void setProperty(int index, CSSValue value) {
		caculated[index] = false;
		values[index] = null;
		elt.getStyle().setProperty(index, value);
	}

	public void addCachedStyle(String styleClass, ComputedStyle style) {
		cachedStyles.put(styleClass, style);
	}

	public ComputedStyle getCachedStyle(String styleClass) {
		return (ComputedStyle) cachedStyles.get(styleClass);
	}
}
