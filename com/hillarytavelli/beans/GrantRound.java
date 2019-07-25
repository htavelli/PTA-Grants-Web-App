package com.hillarytavelli.beans;

import java.time.LocalDate;

/**
 * Bean to represent details about a grant round
 * @author Hillary Tavelli
 *
 */
public class GrantRound {
	private String cycle_name;
	private LocalDate cycle_start;
	private LocalDate cycle_end;
	private boolean cycle_complete;
	
	public String getCycle_name() {
		return cycle_name;
	}
	public void setCycle_name(String cycle_name) {
		this.cycle_name = cycle_name;
	}
	public LocalDate getCycle_start() {
		return cycle_start;
	}
	public void setCycle_start(LocalDate cycle_start) {
		this.cycle_start = cycle_start;
	}
	public LocalDate getCycle_end() {
		return cycle_end;
	}
	public void setCycle_end(LocalDate cycle_end) {
		this.cycle_end = cycle_end;
	}
	public boolean isCycle_complete() {
		return cycle_complete;
	}
	public void setCycle_complete(boolean cycle_complete) {
		this.cycle_complete = cycle_complete;
	}
}
