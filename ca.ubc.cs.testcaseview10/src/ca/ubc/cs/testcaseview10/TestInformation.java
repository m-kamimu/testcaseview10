package ca.ubc.cs.testcaseview10;

import java.util.ArrayList;
import java.util.List;

public class TestInformation {
	
	List<String> methodDList = new ArrayList<String>(); // test case name
	List<String> methodIList = new ArrayList<String>(); // ordinary list
	List<String> methodAList = new ArrayList<String>(); // assert list
	
	List<Integer> methodIintsList = new ArrayList<Integer>();
	List<Integer> methodIintlList = new ArrayList<Integer>();

	List<Integer> methodAintsList = new ArrayList<Integer>();
	List<Integer> methodAintlList = new ArrayList<Integer>();

	/**
	 * @return the methodIintlList
	 */
	public List<Integer> getMethodIintlList() {
		return methodIintlList;
	}
	/**
	 * @param methodIintlList the methodIintlList to set
	 */
	public void setMethodIintlList(List<Integer> methodIintlList) {
		this.methodIintlList = methodIintlList;
	}
	/**
	 * @return the methodAintlList
	 */
	public List<Integer> getMethodAintlList() {
		return methodAintlList;
	}
	/**
	 * @param methodAintlList the methodAintlList to set
	 */
	public void setMethodAintlList(List<Integer> methodAintlList) {
		this.methodAintlList = methodAintlList;
	}
	/**
	 * @return the methodIintList
	 */
	public List<Integer> getMethodIintsList() {
		return methodIintsList;
	}
	/**
	 * @param methodIintList the methodIintList to set
	 */
	public void setMethodIintsList(List<Integer> methodIintsList) {
		this.methodIintsList = methodIintsList;
	}
	/**
	 * @return the methodAintList
	 */
	public List<Integer> getMethodAintsList() {
		return methodAintsList;
	}
	/**
	 * @param methodAintList the methodAintList to set
	 */
	public void setMethodAintsList(List<Integer> methodAintsList) {
		this.methodAintsList = methodAintsList;
	}
	
	public int getMethodAOccurence(String methodname) {
		int occurence = 0;
		for(String name: methodAList) {
			if (name.equals(methodname)) {
				occurence++;
			}
		}
		return occurence;
	}
	
	public int getMethodIOccurence(String methodname) {
		int occurence = 0;
		for(String name: methodIList) {
			if (name.equals(methodname)) {
				occurence++;
			}
		}
		return occurence;
	}	
}
