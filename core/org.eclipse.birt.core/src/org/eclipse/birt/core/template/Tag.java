package org.eclipse.birt.core.template;

public interface Tag {
	int doEndTag();

	int doStartTag();

	Tag getParent();

	void release();

	void setPageContext(Object context);

	void setParent(Tag t);
}
