<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE web-app PUBLIC "-//Sun Microsystems, Inc.//DTD Web Application 2.2//EN" "http://java.sun.com/j2ee/dtds/web-app_2_2.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Chart Preferences</title>
</head>
<body>
<table border="0" cellpadding="5" cellspacing="5">
<form name="control" action="PreferenceServlet" method="POST" target="topFrame">
  <tr>
    <td width="500">
		<table border="1" align="center" cellpadding="10" cellspacing="0" >
        <tr> 
          <td width="350" height="25" align="left" bgcolor="#D2FFFF" cellspacing="0" cellpadding="0"> 
            <p><strong><font color="#3399FF">Chart Title</font></strong></p>
          </td>
        </tr>
        <tr> 
          <td> 
		  <table cellspacing="0" cellpadding="0">
              <tr height = "25" valign="bottom"> 
                <td width="250"><p><strong>Fonts</strong></p></td>
                <td width="100"><p><strong>Style</strong></p></td>
              </tr>
              <tr> 
                <td>
                    <select name="fonts">
                      <option value="Arial">Arial</option>
                      <option value="Times New Roman">Times New Roman</option>
                    </select>
                  </td>
                <td>
                    <select name="style">
                      <option value="Bold">Bold</option>
                      <option value="Italic">Italic</option>
                    </select>
                  </td>
              </tr>
              <tr height="30" valign="bottom"> 
                <td><p><strong>Size</strong></p></td>
                <td><p><strong>Color</strong></p></td>
              </tr>
              <tr height="30" valign="top"> 
                <td>
                    <select name="size">
					 <option value="16">16</option>
					 <option value="12">12</option>
                     <option value="24">24</option>
                    </select>
                  </td>
                <td>
                    <select name="color">
                      <option value="Black">Black</option>
                      <option value="Red">Red</option>
                      <option value="Blue">Blue</option>
                    </select>
                  </td>
              </tr>
            </table></td>
        </tr>
      </table>
	</td>
  </tr>
  <tr>
    <td align = "center">
		<input type="submit" name="action" value="Submit"> 
	</td>
  </tr>
  </form>
</table>
</body>
</html>
