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
package org.eclipse.birt.report.engine.css.engine;

import java.io.StringReader;
import java.net.URI;

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.dom.StyleDeclaration;
import org.eclipse.birt.report.engine.css.engine.value.InheritValue;
import org.eclipse.birt.report.engine.css.engine.value.Value;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSValueConstants;
import org.eclipse.birt.report.engine.css.engine.value.css.RawStringManager;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.DocumentHandler;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.Parser;
import org.w3c.css.sac.SACMediaList;
import org.w3c.css.sac.SelectorList;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSValue;

/**
 * This is the base class for all the CSS engines.
 *
 * @version $Id: CSSEngine.java,v 1.5 2006/06/06 09:40:41 wyan Exp $
 */
public abstract class CSSEngine implements CSSConstants, CSSValueConstants{

    /**
     * The CSS context.
     */
    protected CSSContext cssContext;
    
    protected Parser parser;
    
    protected PropertyManagerFactory pm;

     /**
     * The style declaration document handler.
     */
    protected StyleDeclarationBuilder styleDeclarationBuilder =
        new StyleDeclarationBuilder();

    /**
     * Creates a new CSSEngine.
     * @param doc The associated document.
     * @param uri The document URI.
     * @param p The CSS parser.
     * @param vm The property value managers.
     * @param sm The shorthand properties managers.
     * @param ctx The CSS context.
     */
    protected CSSEngine(Parser p,
    		PropertyManagerFactory pm,
                        CSSContext ctx) {
        this.parser = p;
        this.cssContext = ctx;
        this.pm = pm;
    }

    /**
     * Disposes the CSSEngine and all the attached resources.
     */
    public void dispose() {
    }

    /**
     * Returns the CSS context.
     */
    public CSSContext getCSSContext() {
        return cssContext;
    }

    public int getNumberOfProperties() {
		return pm.getNumberOfProperties();
	}

	public int getPropertyIndex(String name) {
		return pm.getPropertyIndex(name);
	}

	public String getPropertyName(int idx) {
		return pm.getPropertyName(idx);
	}

	/**
     * 
     * @param style1
     * @param style2
     * @return
     */
    public CSSStyleDeclaration mergeStyle(CSSStyleDeclaration namedStyle, CSSStyleDeclaration inlineStyle)
    {
    	if (namedStyle == null || inlineStyle == null)
    	{
        	if (namedStyle != null)
        	{
        		return new StyleDeclaration((StyleDeclaration)namedStyle);
        	}
        	
        	if (inlineStyle != null)
        	{
        		return new StyleDeclaration((StyleDeclaration)inlineStyle);
        	}
    		return null;
    	}
    	
    	StyleDeclaration s = new StyleDeclaration(this);
    	StyleDeclaration ns = (StyleDeclaration)namedStyle;
    	StyleDeclaration is = (StyleDeclaration)inlineStyle;
    	
    	for (int i = 0; i < IStyle.NUMBER_OF_STYLE; i++)
    	{
    		CSSValue value = is.getProperty(i);
    		if (value == null)
    		{
    			value = ns.getProperty(i);
    		}
    		s.setProperty(i, value);
    	}
    	return s;
    }
    
    /**
     * Parses and creates a property value from elt.
     * @param elt  The element property is from.
     * @param prop The property name.
     * @param value The property value.
     */
    public CSSValue parsePropertyValue(String prop, String value) {
        int idx = pm.getPropertyIndex(prop);
        if (idx == -1) return null;
        return parsePropertyValue(idx, value);
    }
    
    public CSSValue parsePropertyValue(int idx, String value)
    {
    	assert idx >= 0 && idx < pm.getNumberOfProperties();
        ValueManager vm = pm.getValueManager(idx);
        if ( vm instanceof RawStringManager || idx == IStyle.STYLE_BACKGROUND_IMAGE )
        {
        	return vm.createStringValue( CSSPrimitiveValue.CSS_STRING, value, this );
        }
        try {
            LexicalUnit lu;
            lu = parser.parsePropertyValue(new InputSource(new StringReader(value)));
            return vm.createValue(lu, this);
        } catch (Exception e) {
        	/** @todo: logout the error messages*/
        	//logout the 
        } 
        return vm.getDefaultValue();
    }

    /**
     * Parses and creates a style declaration.
     * @param value The style declaration text.
     */
    public CSSStyleDeclaration parseStyleDeclaration(String value) {
        styleDeclarationBuilder.styleDeclaration = new StyleDeclaration(this);
        try {
            parser.setDocumentHandler(styleDeclarationBuilder);
            parser.parseStyleDeclaration(new InputSource(new StringReader(value)));
        } catch (Exception e) {
        	/**@todo: logout the error message */
        } 
        return styleDeclarationBuilder.styleDeclaration;
    }


     /**
     * To build a StyleDeclaration object.
     */
    protected class StyleDeclarationBuilder
        extends DocumentAdapter
    {
        StyleDeclaration styleDeclaration;
    
        /**
         * <b>SAC</b>: Implements {@link
         * DocumentHandler#property(String,LexicalUnit,boolean)}.
         */
        public void property(String name, LexicalUnit value, boolean important)
            throws CSSException {
            int i = pm.getPropertyIndex(name);
            if (i == -1) {
                    // Unknown property
                    return;
            }
            ValueManager vm = pm.getValueManager(i);
            CSSValue v = vm.createValue(value, CSSEngine.this);
            styleDeclaration.setProperty(i, v);
        }
    }


    /**
     * Provides an adapter for the DocumentHandler interface.
     */
    protected static class DocumentAdapter implements DocumentHandler {

        /**
         * <b>SAC</b>: Implements {@link
         * DocumentHandler#startDocument(InputSource)}.
         */
        public void startDocument(InputSource source)
            throws CSSException {
            throw new InternalError();
        }
    
        /**
         * <b>SAC</b>: Implements {@link
         * DocumentHandler#endDocument(InputSource)}.
         */
        public void endDocument(InputSource source) throws CSSException {
            throw new InternalError();
        }
    
        /**
         * <b>SAC</b>: Implements {@link
         * DocumentHandler#comment(String)}.
         */
        public void comment(String text) throws CSSException {
            // We always ignore the comments.
        }
    
        /**
         * <b>SAC</b>: Implements {@link
         * DocumentHandler#ignorableAtRule(String)}.
         */
        public void ignorableAtRule(String atRule) throws CSSException {
            throw new InternalError();
        }
    
        /**
         * <b>SAC</b>: Implements {@link
         * DocumentHandler#namespaceDeclaration(String,String)}.
         */
        public void namespaceDeclaration(String prefix, String uri) 
            throws CSSException {
            throw new InternalError();
        }
    
        /**
         * <b>SAC</b>: Implements {@link
         * DocumentHandler#importStyle(String,SACMediaList,String)}.
         */
        public void importStyle(String       uri,
                                SACMediaList media, 
                                String       defaultNamespaceURI)
            throws CSSException {
            throw new InternalError();
        }
    
        /**
         * <b>SAC</b>: Implements {@link
         * DocumentHandler#startMedia(SACMediaList)}.
         */
        public void startMedia(SACMediaList media) throws CSSException {
            throw new InternalError();
        }
    
        /**
         * <b>SAC</b>: Implements {@link
         * DocumentHandler#endMedia(SACMediaList)}.
         */
        public void endMedia(SACMediaList media) throws CSSException {
            throw new InternalError();
        }
    
        /**
         * <b>SAC</b>: Implements {@link
         * DocumentHandler#startPage(String,String)}.
         */    
        public void startPage(String name, String pseudo_page)
            throws CSSException {
            throw new InternalError();
        }
    
        /**
         * <b>SAC</b>: Implements {@link
         * DocumentHandler#endPage(String,String)}.
         */
        public void endPage(String name, String pseudo_page)
            throws CSSException {
            throw new InternalError();
        }
    
        /**
         * <b>SAC</b>: Implements {@link DocumentHandler#startFontFace()}.
         */
        public void startFontFace() throws CSSException {
            throw new InternalError();
        }
    
        /**
         * <b>SAC</b>: Implements {@link DocumentHandler#endFontFace()}.
         */
        public void endFontFace() throws CSSException {
            throw new InternalError();
        }
        
        /**
         * <b>SAC</b>: Implements {@link
         * DocumentHandler#startSelector(SelectorList)}.
         */
        public void startSelector(SelectorList selectors) throws CSSException {
            throw new InternalError();
        }
    
        /**
         * <b>SAC</b>: Implements {@link
         * DocumentHandler#endSelector(SelectorList)}.
         */
        public void endSelector(SelectorList selectors) throws CSSException {
            throw new InternalError();
        }
    
        /**
         * <b>SAC</b>: Implements {@link
         * DocumentHandler#property(String,LexicalUnit,boolean)}.
         */
        public void property(String name, LexicalUnit value, boolean important)
            throws CSSException {
            throw new InternalError();
        }
    }

    /**
     * if the prop is inheritable.
     * @param propidx
     * @return true, it is inheritable
     */
    public boolean isInheritedProperty( int propidx )
	{
		ValueManager vm = pm.getValueManager( propidx );
		return vm.isInheritedProperty( );
	}
    
    /**
     * compute a single style value. 
     * @param elt the element which owns the style
     * @param propidx style index
     * @param v specified value
     * @param pcs parent computed style
     * @return
     */
    public Value resolveStyle(CSSStylableElement elt, int propidx, Value sv, IStyle pcs)
    {
		ValueManager vm = pm.getValueManager(propidx);
		//# If the cascade results in a value, use it.
		if (sv != null && sv != InheritValue.INSTANCE)
		{
			sv = vm.computeValue(elt, this, propidx, sv );
		}
		//if the property is inherited and the element is not the root of the document tree, 
		//use the computed value of the parent element.    		
		else if ((sv == InheritValue.INSTANCE || vm.isInheritedProperty()) && pcs != null)
		{
			sv = (Value)pcs.getProperty(propidx);
		}
		//  Otherwise use the property's initial value.
		else
		{
			sv = vm.computeValue(elt, this, propidx, vm.getDefaultValue());
		}
    	return sv;
    }
    
    public abstract URI getCSSBaseURI();
}
