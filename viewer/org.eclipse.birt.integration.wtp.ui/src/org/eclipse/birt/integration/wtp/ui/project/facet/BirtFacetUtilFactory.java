package org.eclipse.birt.integration.wtp.ui.project.facet;

import org.eclipse.jst.javaee.web.WebApp;
import org.eclipse.jst.javaee.web.WebAppVersionType;

public class BirtFacetUtilFactory {

	public static IBirtFacetUtil getInstance(Object webApp) {
		if (isWebApp25(webApp)) {
			return new BirtFacetUtil25();
		}
		return new BirtFacetUtil();
	}

	public static boolean isWebApp25(final Object webApp) {
		if (webApp instanceof WebApp && ((WebApp) webApp).getVersion() == WebAppVersionType._25_LITERAL)
			return true;
		return false;
	}

}
