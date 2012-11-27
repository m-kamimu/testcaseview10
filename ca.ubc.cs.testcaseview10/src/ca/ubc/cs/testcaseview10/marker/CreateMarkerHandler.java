package ca.ubc.cs.testcaseview10.marker;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

public class CreateMarkerHandler extends AbstractHandler {

    public CreateMarkerHandler() {
    }

    public Object execute(ExecutionEvent event) throws ExecutionException {
        IEditorPart editorPart = HandlerUtil.getActiveEditor(event);
        IEditorInput editorInput = editorPart.getEditorInput();
        IResource resource = (IResource)editorInput.getAdapter(IResource.class);

        SampleMarker.createMarker(resource, null, null);
        return null;
    }
}