package org.eclipse.birt.report.engine.css.engine;

import java.net.URI;

import org.eclipse.birt.report.engine.css.engine.value.css.FontFamilyManager;
import org.w3c.dom.css.CSSValue;
import org.w3c.flute.parser.Parser;


public class BIRTCSSEngine extends CSSEngine {

	/**
	 * Creates a new SVGCSSEngine.
	 * 
	 * @param doc
	 *            The associated document.
	 * @param uri
	 *            The document URI.
	 * @param p
	 *            The CSS parser to use.
	 * @param ctx
	 *            The CSS context.
	 */
	public BIRTCSSEngine() {
		super(new Parser(), new BIRTPropertyManagerFactory(), new BIRTContext());
	}
	
	
	static class BIRTContext implements CSSContext
	{
		public CSSValue getSystemColor(String ident) {
			return SystemColorSupport.getSystemColor(ident);
		}

		public CSSValue getDefaultFontFamily() {
			return FontFamilyManager.DEFAULT_VALUE;
		}

		public float getLighterFontWeight(float f) {
			return 0;
		}

		public float getBolderFontWeight(float f) {
			return 0;
		}

		public float getPixelUnitToMillimeter() {
			return 0;
		}

		public float getPixelToMillimeter() {
			return 0;
		}

		public float getMediumFontSize() {
			return 0;
		}
	}


	public URI getCSSBaseURI() {
		return null;
	}
}
