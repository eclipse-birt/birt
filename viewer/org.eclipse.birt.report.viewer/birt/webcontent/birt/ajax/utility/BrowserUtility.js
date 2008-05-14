// Copyright 1994-2006, Actuate Software Corp., All rights reserved.

BrowserUtility = Class.create();

BrowserUtility.prototype = {
	
	initialize: function()
	{
		this.isIE = this.__isIE();
		if ( this.isIE )
		{
			if ( window.XMLHttpRequest )
			{
				this.isIE6 = false;
				this.isIE7 = true;
			}
			else
			{
				this.isIE6 = true;
				this.isIE7 = false;
			}
		}
		
		this.isFirefox = this.__isFirefox();
		this.isSafari = this.__isSafari();
		this.isKHTML = this.__isKHTML();
	},
		
	__isSafari: function()
	{
		return navigator.appVersion.match(/Safari/);		
	},

	__isKHTML: function()
	{
		return navigator.appVersion.match(/KHTML/);		
	},

	__isIE: function()
	{
		var userAgent = navigator.userAgent.toLowerCase();
		var useIFrame;
		if(userAgent.indexOf('msie') > -1)
		{
			//Internet Explorer
			return true;
			
		}
		else 
		{
			return false;
		}
	},
	
	__isFirefox : function()
	{
		var userAgent = navigator.userAgent.toLowerCase();
		return (userAgent.indexOf('firefox') > -1);
	},
	
	useIFrame: function()
	{
		return this.isIE;
	}
}
