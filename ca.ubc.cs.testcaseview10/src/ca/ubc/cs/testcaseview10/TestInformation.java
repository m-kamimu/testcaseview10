package ca.ubc.cs.testcaseview10;

import java.util.ArrayList;
import java.util.List;

public class TestInformation {
	
	private List<String> methodDList = new ArrayList<String>(); // test case name
	
	private List<String> methodIDList = new ArrayList<String>(); // ordinary list
	private List<String> methodADList = new ArrayList<String>(); // assert list
	
	private List<String> methodIList = new ArrayList<String>(); // ordinary list
	private List<String> methodAList = new ArrayList<String>(); // assert list
	
	private List<Integer> methodIintsList = new ArrayList<Integer>();
	private List<Integer> methodIintlList = new ArrayList<Integer>();

	private List<Integer> methodAintsList = new ArrayList<Integer>();
	private List<Integer> methodAintlList = new ArrayList<Integer>();

	//private List<String> sourceFiles = new ArrayList<String>();
	private boolean lock = false;
	
	/**
	 * @return the sourceFiles
	 */
	/*public List<String> getSourceFiles() {
		return sourceFiles;
	}
	/**
	 * @param sourceFiles the sourceFiles to set
	 */
	/*
	public void setSourceFile(String sourceFile) {
		this.sourceFiles.add(sourceFile);
	}
	*/
	
	/**
	 * @return the lock
	 */
	public boolean isLock() {
		return lock;
	}
	/**
	 * @param lock the lock to set
	 */
	public void setLock(boolean lock) {
		this.lock = lock;
	}
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
	
	public int getMethodAOccurenceInTest(String methodname, String methodDname) {
		int occurence = 0;
		for(int i = 0; i < methodADList.size(); i++) {
			String dname = methodADList.get(i);
			String name = methodAList.get(i);
			if (name.equals(methodname) && dname.equals(methodDname)) {
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

	public int getMethodIOccurenceInTest(String methodname, String methodDname) {
		int occurence = 0;
		for(int i = 0; i < methodIDList.size(); i++) {
			String dname = methodIDList.get(i);
			String name = methodIList.get(i);
			if (name.equals(methodname) && dname.equals(methodDname)) {
				occurence++;
			}
		}
		return occurence;
	}

	/**
	 * @return the methodDList
	 */
	public List<String> getMethodDList() {
		return methodDList;
	}
	/**
	 * @param methodDList the methodDList to set
	 */
	public void setMethodDList(List<String> methodDList) {
		this.methodDList = methodDList;
	}
	/**
	 * @return the methodIList
	 */
	public List<String> getMethodIList() {
		return methodIList;
	}
	/**
	 * @param methodIList the methodIList to set
	 */
	public void setMethodIList(List<String> methodIList) {
		this.methodIList = methodIList;
	}
	/**
	 * @return the methodAList
	 */
	public List<String> getMethodAList() {
		return methodAList;
	}
	/**
	 * @param methodAList the methodAList to set
	 */
	public void setMethodAList(List<String> methodAList) {
		this.methodAList = methodAList;
	}
	/**
	 * @return the methodIDList
	 */
	public List<String> getMethodIDList() {
		return methodIDList;
	}
	/**
	 * @param methodIDList the methodIDList to set
	 */
	public void setMethodIDList(List<String> methodIDList) {
		this.methodIDList = methodIDList;
	}
	/**
	 * @return the methodADList
	 */
	public List<String> getMethodADList() {
		return methodADList;
	}
	/**
	 * @param methodADList the methodADList to set
	 */
	public void setMethodADList(List<String> methodADList) {
		this.methodADList = methodADList;
	}
	
	
	
}
