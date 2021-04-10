package org.eclipse.birt.report.engine.css.dom;

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.w3c.dom.css.CSSValue;

public class AreaStyle extends AbstractStyle {
	protected IStyle parent;
	CSSValue[] values = new CSSValue[NUMBER_OF_STYLE];
	boolean[] resolveFlags = new boolean[NUMBER_OF_STYLE];

	public AreaStyle(AbstractStyle style) {
		super(style.engine);
		this.parent = style;
	}

	public AreaStyle(CSSEngine engine) {
		super(engine);
	}

	public CSSValue getProperty(int index) {
		if (!resolveFlags[index]) {
			if (parent != null) {
				values[index] = parent.getProperty(index);
			}
			resolveFlags[index] = true;
		}
		return values[index];

	}

	public void setProperty(int index, CSSValue value) {
		values[index] = value;
		resolveFlags[index] = true;
	}

	public boolean isEmpty() {
		return false;
	}

}
