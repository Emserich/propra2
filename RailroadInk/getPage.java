public static String getPage(boolean loggedIn, String name, boolean game, boolean globalChat, String context) {
		StringBuffer page = new StringBuffer(
				"<!DOCTYPE html>" +
				"<html>" +
				"<head>" + 
				"<title>JAM Server</title>" +
				"<style>");
				
				if(globalChat) {
					setGlobalChat();
					page.append(chatCSS);
				}
				
				if(game)
					page.append(gameCSS);
					
				try {
					page.append(FileHelper.getFile(mainCSS));
					page.append("</style>");
					page.append("<script>");
					page.append(FileHelper.getFile(mainJS));
					page.append("</script>");
				} catch (IOException e) {
					System.err.println("Couldnt load main.css");
				}
				
				if(game){
					page.append(gameJS);
				}
				
				page.append("<link rel=\"icon\" href=\"/img/favicon.png\" type=\"image/png\" />");
				
				page.append("</head>" +
					"<body>"+
					// head container
					"<div id=\"head\"></div>" +
					"<div id=\"top1\">" +
					"<div id=\"logo\"></div>" +
					"<div id=\"login\">");
				if(!loggedIn) {
					// no active session, show login and register links
					page.append(
							"<ul id=\"list\">" +
							"<li><a href=\"/login\">LOGIN</a>"+ SPACE +"|" + SPACE + "<a href=\"/register\">REGISTER</a></li>" +
							"</ul>"); 
				} else {
					// session found, show logout link
					String logoutlink = "";
					if(context.equals("")) {
						logoutlink = "Hallo " + name + SPACE + SPACE + "|" + SPACE + SPACE + "<a href=\""+context+"logout\">LOGOUT</a>";
					} else {
						logoutlink = "Hallo " + name + SPACE + SPACE + "|" + SPACE + SPACE + "<a href=\"javascript:sendDataToServer('logout')\">LOGOUT</a>";
					}
					page.append(logoutlink);
				}
				page.append("</div></div>");	
				
				// menu tab
				page.append("<div id=\"menu\">");
				if(loggedIn) {
					//if(UserManager.getInstance().getUserByName(name).getGameInstanceId() == 0 && !game)
					if(!game) {
						page.append("<ul id=\"list\">" +
								"<li><a href=\"/\">HOME</a>" +
								SPACE + SPACE + "|" + SPACE + SPACE +
								"<a href=\"/create\">ERSTELLEN</a>" +
								SPACE + SPACE + "|" + SPACE + SPACE +
								"<a href=\"/join\">BEITRETEN</a>" 
								/*
								 * To do: Chat
								 */
								//SPACE + SPACE + "|" + SPACE + SPACE +
								//"<a href=\"/chat\">GLOBAL CHAT</a>"
							);
					}
					if(game) {
						page.append("<ul id=\"list\">" +
									"<li>"+
									"<a href=\"javascript:sendDataToServer('quit')\">SPIEL VERLASSEN</a>");
					}
					page.append("</li></ul>");
				}
				page.append("</div>");
				
				page.append(
					// content container
					"<div id=\"main\"></div>"+
					"<div id=\"content\">");
				if(toShow != null) {
					// there is something to view
					if(feedback != null) {
						// view error/success feedback
						page.append("<div id=\"feedback\" align=\"center\">");
						page.append(feedback);
						page.append("</div>");
						feedback = null;
						page.append("<br><br><br>");
					}
					page.append(toShow);
				}


				if(game){


					page.append("<div id=\"head_new\"></div>" +
						"<div id=\"top1_new\">" +
						"<div id=\"logo_new\"></div>" +
						"<div id=\"login_new\">");
					if(!loggedIn) {
						// no active session, show login and register links
						page.append(
								"<ul id=\"list_new\">" +
								"<li><a href=\"/login\">LOGIN</a>"+ SPACE +"|" + SPACE + "<a href=\"/register\">REGISTER</a></li>" +
								"</ul>"); 
					} else {
						// session found, show logout link
						String logoutlink = "";
						if(context.equals("")) {
							logoutlink = "Hallo " + name + SPACE + SPACE + "|" + SPACE + SPACE + "<a href=\""+context+"logout\">LOGOUT</a>";
						} else {
							logoutlink = "Hallo " + name + SPACE + SPACE + "|" + SPACE + SPACE + "<a href=\"javascript:sendDataToServer('logout')\">LOGOUT</a>";
						}
						page.append(logoutlink);
					}
					page.append("</div></div>");	
					
					// menu tab
					page.append("<div id=\"menu_new\">");
					if(loggedIn) {
						
						page.append("<ul id=\"list_new\">" +
									"<li>"+
									"<a href=\"javascript:sendDataToServer('quit')\">SPIEL VERLASSEN</a>");
						
						page.append("</li></ul>");
					}
					page.append("</div>");


				}




				page.append(
				"</div>" +
				HTML_END);
		return page.toString();
}