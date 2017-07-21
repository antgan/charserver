package com.oocl.chatserver.html.template;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * Server Admin HTML
 * @author GANAB
 *
 */
public class AdminTemplate {
	private Element root = new Element("html");
	private String title;
	
	public Element createHead(String title){
		this.title = title;
		Element headTag = new Element("head");
		Element metaTag = new Element("meta").setAttribute("http-equiv","Content-Type").setAttribute("content","text/html; charset=utf-8");
		Element titleTag = new Element("title").setText(title);
		headTag.addContent(metaTag);
		headTag.addContent(titleTag);
		return headTag;
	}
	
	public Element createBody(Element divTag){
		Element bodyTag = new Element("body");
		bodyTag.addContent(divTag);
		return bodyTag;
	}
	
	/**
	 * @param body
	 * @param action1
	 * @param action2
	 * @param startTime
	 * @param runningTime
	 * @param divStyle
	 */
	public Element createDiv(String action1, String action2, 
			String startTime, String runningTime,List<String> onlines, List<String> regisers, Map<String,String> tokens){
		Element divTag = new Element("div").setAttribute("style", "width:920px; text-align: center; margin: 0 auto; border: 1px dotted black;");
		Element h1Tag = new Element("h1").setText(title);
		Element span1Tag = new Element("span").setText("服务器启动时间： "+startTime);
		Element span2Tag = new Element("span").setText("服务器运行时间："+runningTime);
		Element button1Tag = new Element("button").addContent(new Element("a").setAttribute("href",action1).setText("Start"));
		Element button2Tag = new Element("button").addContent(new Element("a").setAttribute("href",action2).setText("Stop"));
		Element brTag = new Element("br");
		
		//Online
		Element onlineDiv = new Element("div").setAttribute("id","online").setAttribute("style","display:inline-block;width: 200px; height: 300px; overflow: scroll;");
		Element onlineTable = new Element("table").setAttribute("style","margin:0 auto;").addContent(new Element("th").setText("在线列表"));
		for(String u : onlines){
			Element tr = new Element("tr").addContent(new Element("td").setText(u));
			onlineTable.addContent(tr);
		}
		onlineDiv.addContent(onlineTable);
		
		//Register
		Element registerDiv = new Element("div").setAttribute("id","regiser").setAttribute("style","display:inline-block;width: 200px; height: 300px; overflow: scroll;");
		Element registerTable = new Element("table").setAttribute("style","margin:0 auto;").addContent(new Element("th").setText("注册列表"));
		for(String u : regisers){
			Element tr = new Element("tr").addContent(new Element("td").setText(u));
			registerTable.addContent(tr);
		}
		registerDiv.addContent(registerTable);
		
		//token
		Element tokenDiv = new Element("div").setAttribute("id","token").setAttribute("style","display:inline-block;width: 500px; height: 300px; overflow: scroll;");
		Element tokenTable = new Element("table").setAttribute("style","margin:0 auto;").addContent(new Element("th").setText("用户名")).addContent(new Element("th").setText("Token"));
		Set<String> keys = tokens.keySet();
		for(String key : keys){
			Element tr = new Element("tr").addContent(new Element("td").setText(key)).addContent(new Element("td").setText(tokens.get(key)));
			tokenTable.addContent(tr);
		}
		tokenDiv.addContent(tokenTable);
		
		//Add
		divTag.addContent(h1Tag);
		divTag.addContent(span1Tag);
		divTag.addContent(span2Tag);
		divTag.addContent(button1Tag);
		divTag.addContent(button2Tag);
		divTag.addContent(brTag);
		divTag.addContent(onlineDiv);
		divTag.addContent(registerDiv);
		divTag.addContent(tokenDiv);

		return divTag;
	}
	
	public void build(OutputStream output,TemplateParameter param){	
		Element head = createHead(param.getTitile());
		Element body = createBody(createDiv(param.getStartAction(), param.getStopAction(), param.getStartTime(),
				param.getRunningTime(), param.getOnlines(), param.getRegisters(), param.getTokens()));
		root.addContent(head);
		root.addContent(body);
		
		
		XMLOutputter out = new XMLOutputter(Format.getPrettyFormat()); 
		try {
			output.write("HTTP/1.1 200 OK\r\n\r\n".getBytes());
			out.output(root, output);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
