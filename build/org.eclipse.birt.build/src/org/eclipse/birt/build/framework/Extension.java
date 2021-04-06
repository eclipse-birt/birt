package org.eclipse.birt.build.framework;

import org.w3c.dom.Element;

public class Extension {
	Bundle bundle;
	Element element;

	Extension(Bundle bundle, Element element) {
		this.bundle = bundle;
		this.element = element;
	}

	public String getPoint() {
		return element.getAttribute("point");
	}

	public void setPoint(String point) {
		element.setAttribute("point", point);
	}

	public String getId() {
		return element.getAttribute("id");
	}

	public void setId(String id) {
		element.setAttribute("id", id);
	}

	public String getName() {
		return element.getAttribute("name");
	}

	public void setName(String name) {
		element.setAttribute("name", name);
	}

	public Element getElement() {
		return element;
	}
}
