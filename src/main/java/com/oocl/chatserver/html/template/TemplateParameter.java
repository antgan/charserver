package com.oocl.chatserver.html.template;

import java.util.List;
import java.util.Map;

/**
 * 参数
 * @author GANAB
 *
 */
public class TemplateParameter {

	private String titile;
	private String startAction;
	private String stopAction;
	private String startTime;
	private String runningTime;
	private List<String> onlines;
	private List<String> registers;
	private Map<String,String> tokens;
	
	public TemplateParameter() {
		// TODO Auto-generated constructor stub
	}
	
	public String getTitile() {
		return titile;
	}
	public void setTitile(String titile) {
		this.titile = titile;
	}
	public String getStartAction() {
		return startAction;
	}
	public void setStartAction(String startAction) {
		this.startAction = startAction;
	}
	public String getStopAction() {
		return stopAction;
	}
	public void setStopAction(String stopAction) {
		this.stopAction = stopAction;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getRunningTime() {
		return runningTime;
	}
	public void setRunningTime(String runningTime) {
		this.runningTime = runningTime;
	}
	public List<String> getOnlines() {
		return onlines;
	}
	public void setOnlines(List<String> onlines) {
		this.onlines = onlines;
	}
	public List<String> getRegisters() {
		return registers;
	}
	public void setRegisters(List<String> registers) {
		this.registers = registers;
	}
	public Map<String, String> getTokens() {
		return tokens;
	}
	public void setTokens(Map<String, String> tokens) {
		this.tokens = tokens;
	}
	
	
	
}
