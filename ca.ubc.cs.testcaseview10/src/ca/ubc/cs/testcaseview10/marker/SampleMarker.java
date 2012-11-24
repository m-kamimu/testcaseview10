package ca.ubc.cs.testcaseview10.marker;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.texteditor.MarkerUtilities;

public class SampleMarker {

	private static final String MARKER_ID = IMarker.BOOKMARK;

	public static void createMarker(IResource resource) {
		try {
			resource.deleteMarkers(SampleMarker.MARKER_ID, false, IResource.DEPTH_ZERO);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    Map<Object,Object> attributes = new HashMap<Object,Object>();
	    attributes.put(IMarker.TRANSIENT, true);
	    attributes.put(IMarker.PRIORITY, Integer.valueOf(IMarker.PRIORITY_NORMAL));
	    attributes.put(IMarker.SEVERITY, Integer.valueOf(IMarker.SEVERITY_WARNING));
	    attributes.put(IMarker.LINE_NUMBER, Integer.valueOf(100));
	    attributes.put(IMarker.MESSAGE, "sample marker");
	    try {
			MarkerUtilities.createMarker(resource, attributes, SampleMarker.MARKER_ID);
		} catch (CoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
