package org.eclipse.birt.report.engine.css.engine.value.css;

import org.eclipse.birt.report.engine.css.engine.value.IdentifierManager;
import org.eclipse.birt.report.engine.css.engine.value.StringMap;
import org.eclipse.birt.report.engine.css.engine.value.Value;

public class BackgroundAttachmentManager extends IdentifierManager {

	/**
	 * The identifier values.
	 */
	protected final static StringMap values = new StringMap();
	static {
		values.put(CSSConstants.CSS_SCROLL_VALUE,
				CSSValueConstants.SCROLL_VALUE);
		values.put(CSSConstants.CSS_FIXED_VALUE,
				CSSValueConstants.FIXED_VALUE);
	}

	public StringMap getIdentifiers() {
		return values;
	}

	public BackgroundAttachmentManager() {
	}

	public String getPropertyName() {
		return CSSConstants.CSS_BACKGROUND_ATTACHMENT_PROPERTY;
	}

	public boolean isInheritedProperty() {
		return false;
	}

	public Value getDefaultValue() {
		return CSSValueConstants.SCROLL_VALUE;
	}
}

