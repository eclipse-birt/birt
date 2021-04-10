package org.eclipse.birt.report.engine.css.dom;

import org.eclipse.birt.report.engine.content.IStyle;
import org.w3c.dom.css.CSSValue;

public class CompositeStyle extends AbstractStyle {

	IStyle style;
	IStyle inlineStyle;

	public CompositeStyle(IStyle style, IStyle inlineStyle) {
		super(((AbstractStyle) inlineStyle).engine);
		this.style = style;
		this.inlineStyle = inlineStyle;
	}

	public CSSValue getProperty(int index) {
		CSSValue v = inlineStyle.getProperty(index);
		if (v != null) {
			return v;
		}
		if (style != null) {
			return style.getProperty(index);
		}
		return null;
	}

	public void setProperty(int index, CSSValue value) {
		inlineStyle.setProperty(index, value);
	}

	public boolean isEmpty() {
		return inlineStyle.isEmpty() && (style == null || style.isEmpty());
	}

}
