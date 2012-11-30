package ca.ubc.cs.testcaseview10.marker;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.texteditor.MarkerUtilities;

import ca.ubc.cs.testcaseview10.TestInformation;

public class SampleMarker {

	private static final String MARKER_ID = "ca.ubc.cs.testcaseview10.SampleMarker";
	private static StringBuffer lastm = new StringBuffer();
	
	/**
	 * @return the lastm
	 */
	public static String getLastm() {
		return lastm.toString();
	}

	public static int createMarker(IResource resource, TestInformation ti, TestInformation global) {
		lastm = new StringBuffer();
		int marked = 0;
		if (ti == null || global == null) {
			return marked;
		}
		
		try {
			resource.deleteMarkers(SampleMarker.MARKER_ID, false, IResource.DEPTH_ZERO);
			//resource.createMarker("ca.ubc.cs.testcaseview10.mymarker");
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    for(int i = 0; i < ti.getMethodAintsList().size(); i++) {
	    	String methodname = ti.getMethodAList().get(i);
	    	String methodDname = ti.getMethodADList().get(i);
	    	int occurence = global.getMethodAOccurence(methodname) - ti.getMethodAOccurenceInTest(methodname, methodDname);
	    	if (occurence >= 1) {
	    		continue;
	    	}
	    	
		    Map<Object,Object> attributes = new HashMap<Object,Object>();
		    attributes.put(IMarker.TRANSIENT, true);
		    attributes.put(IMarker.PRIORITY, Integer.valueOf(IMarker.PRIORITY_NORMAL));
		    attributes.put(IMarker.SEVERITY, Integer.valueOf(IMarker.SEVERITY_WARNING));
		    //attributes.put(IMarker.LINE_NUMBER, Integer.valueOf(100));

	    	attributes.put(IMarker.CHAR_START, Integer.valueOf(ti.getMethodAintsList().get(i)));
		    attributes.put(IMarker.CHAR_END, Integer.valueOf(ti.getMethodAintsList().get(i) + ti.getMethodAintlList().get(i)));
		    lastm.append(methodname + "\n");
	    	
		    //attributes.put(IMarker.CHAR_START, Integer.valueOf(1000));
		    //attributes.put(IMarker.CHAR_END, Integer.valueOf(1005));
		    attributes.put(IMarker.MESSAGE, "methodname: " + methodname + " occurence(in others):" + occurence);
		    try {
				MarkerUtilities.createMarker(resource, attributes, SampleMarker.MARKER_ID);
				marked++;
			} catch (CoreException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    }
		return marked;
	}
}
