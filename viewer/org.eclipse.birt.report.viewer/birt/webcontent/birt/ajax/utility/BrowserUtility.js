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

		this.isMozilla = this.__isMozilla();
		this.isFirefox = this.__isFirefox();
		this.isSafari = this.__isSafari();
		this.isKHTML = this.__isKHTML();
		this.isOpera = this.__isOpera();
		
		if ( this.isFirefox )
		{
			var firefoxVersion = this._getAgentVersion("Firefox");
			if ( firefoxVersion && firefoxVersion.length > 0 )
			{
				if ( firefoxVersion[0] == 2 )
				{
					this.isFirefox2 = true;
				}
				else if ( firefoxVersion[0] == 3 )
				{
					this.isFirefox3 = true;
				}				
			}
		}
	},
	
	_getAgentVersion : function( agentName )
	{
		var re = new RegExp(agentName + "\/([^\s])", "i");
		var agentVersion = re.exec( navigator.userAgent );
		if ( agentVersion && agentVersion[1] )
		{
			return this._getVersionComponents( agentVersion[1] );
		}
		else
		{
			return null;
		}
	},
	
	_getVersionComponents : function( versionString )
	{
		if ( !versionString )
		{
			return null;
		}
		return versionString.split(".");
	},
		
	__isSafari: function()
	{
		return navigator.appVersion.match(/Safari/) != null;		
	},

	__isKHTML: function()
	{
		return navigator.appVersion.match(/KHTML/) != null;		
	},

	__isOpera: function()
	{
		return navigator.appName.match(/Opera/) != null;
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
	
	__isMozilla : function()
	{
		var userAgent = navigator.userAgent.toLowerCase();
		return (userAgent.indexOf('mozilla') > -1);
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
