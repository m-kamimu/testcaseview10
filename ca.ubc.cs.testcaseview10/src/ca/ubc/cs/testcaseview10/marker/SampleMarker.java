package ca.ubc.cs.testcaseview10.marker;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.AbstractMarkerAnnotationModel;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.MarkerUtilities;
import org.eclipse.ui.texteditor.SimpleMarkerAnnotation;

public class SampleMarker {

	private static final String MARKER_ID = "ca.ubc.cs.testcaseview10.SampleMarker";

	public static void createMarker(IResource resource) {
		try {
			resource.deleteMarkers(SampleMarker.MARKER_ID, false, IResource.DEPTH_ZERO);
			resource.createMarker("ca.ubc.cs.testcaseview10.mymarker");
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    Map<Object,Object> attributes = new HashMap<Object,Object>();
	    attributes.put(IMarker.TRANSIENT, true);
	    attributes.put(IMarker.PRIORITY, Integer.valueOf(IMarker.PRIORITY_NORMAL));
	    attributes.put(IMarker.SEVERITY, Integer.valueOf(IMarker.SEVERITY_WARNING));
	    //attributes.put(IMarker.LINE_NUMBER, Integer.valueOf(100));
	    attributes.put(IMarker.CHAR_START, Integer.valueOf(1000));
	    attributes.put(IMarker.CHAR_END, Integer.valueOf(1005));
	    attributes.put(IMarker.MESSAGE, "sample marker");
	    AbstractMarkerAnnotationModel model =  getModel(resource);
	    //model.
	    try {
			MarkerUtilities.createMarker(resource, attributes, SampleMarker.MARKER_ID);
		} catch (CoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    /*
		       IMarker marker = null;
		//note: you use the id that is defined in your plugin.xml
		       marker = resource.createMarker("ca.ubc.cs.testcaseview10.mymarkers");
		       marker.setAttribute("description," "this is one of my markers");
		//note: you can also use attributes from your supertype
		       marker.setAttribute(IMarker.MESSAGE, "My Marker");
	*/
	}
	
    private static AbstractMarkerAnnotationModel getModel(IResource resource) {
    	IEditorPart editorp = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        IDocumentProvider provider = ((ITextEditor)editorp).getDocumentProvider();
        IAnnotationModel model = provider.getAnnotationModel(editorp.getEditorInput());
        if (model instanceof AbstractMarkerAnnotationModel) {
            return (AbstractMarkerAnnotationModel) model;
        }
        return null;
    }

	
	
    public static void addAnnotation(IMarker marker, ITextSelection selection, 
			ITextEditor editor) {
		//The DocumentProvider enables to get the document currently loaded in the editor
		IDocumentProvider idp = editor.getDocumentProvider();
		
		//This is the document we want to connect to. This is taken from 
		//the current editor input.
		IDocument document = idp.getDocument(editor.getEditorInput());
		
		//The IannotationModel enables to add/remove/change annotation to a Document 
		//loaded in an Editor
		IAnnotationModel iamf = idp.getAnnotationModel(editor.getEditorInput());
		
		//Note: The annotation type id specify that you want to create one of your 
		//annotations
		SimpleMarkerAnnotation ma = new SimpleMarkerAnnotation("sample.baseMarkerAnnotation",marker);
		
		//Finally add the new annotation to the model
		iamf.connect(document);
		iamf.addAnnotation(ma,new Position(selection.getOffset(),selection.getLength()));
		iamf.disconnect(document);
	} 
}
