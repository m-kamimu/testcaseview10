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

	public static void createMarker(IResource resource, TestInformation ti, TestInformation global) {
		if (ti == null || global == null) {
			return;
		}
		
		try {
			resource.deleteMarkers(SampleMarker.MARKER_ID, false, IResource.DEPTH_ZERO);
			//resource.createMarker("ca.ubc.cs.testcaseview10.mymarker");
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    for(int i = 0; i < ti.getMethodAintsList().size(); i++) {
	    	if (global.getMethodAOccurence(ti.getMethodAList().get(i)) > 1) {
	    		continue;
	    	}
	    	
		    Map<Object,Object> attributes = new HashMap<Object,Object>();
		    attributes.put(IMarker.TRANSIENT, true);
		    attributes.put(IMarker.PRIORITY, Integer.valueOf(IMarker.PRIORITY_NORMAL));
		    attributes.put(IMarker.SEVERITY, Integer.valueOf(IMarker.SEVERITY_WARNING));
		    //attributes.put(IMarker.LINE_NUMBER, Integer.valueOf(100));

	    	attributes.put(IMarker.CHAR_START, Integer.valueOf(ti.getMethodAintsList().get(i)));
		    attributes.put(IMarker.CHAR_END, Integer.valueOf(ti.getMethodAintsList().get(i) + ti.getMethodAintlList().get(i)));
	    	
		    //attributes.put(IMarker.CHAR_START, Integer.valueOf(1000));
		    //attributes.put(IMarker.CHAR_END, Integer.valueOf(1005));
		    attributes.put(IMarker.MESSAGE, "occurence:" + global.getMethodAOccurence(ti.getMethodAList().get(i)));
		    try {
				MarkerUtilities.createMarker(resource, attributes, SampleMarker.MARKER_ID);
			} catch (CoreException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    }
	}	
}