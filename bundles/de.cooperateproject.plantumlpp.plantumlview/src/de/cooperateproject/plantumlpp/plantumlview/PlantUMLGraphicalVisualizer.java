package de.cooperateproject.plantumlpp.plantumlview;

import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import de.cooperateproject.notation2plant.viewer.extensions.GraphicalPlantUMLVisualizer;

public class PlantUMLGraphicalVisualizer implements GraphicalPlantUMLVisualizer {

    private final Object viewLock = new Object();
    private PlantUMLView view;

    @Override
    public void visualize(String plantUML) {
        synchronized (viewLock) {
            if (view != null) {
                view.setText(plantUML);
            }
        }
    }

    @Override
    public void init() throws PartInitException {
        synchronized (viewLock) {
            if (view != null && view.isDisposed()) {
                view = null;
            }
            if (view == null) {
                view = (PlantUMLView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                        .showView(PlantUMLView.ID);    
            }
        }
    }

    @Override
    public void dispose() {
        synchronized (viewLock) {
            if (view != null) {
                view.getViewSite().getPage().hideView(view);
                view.dispose();
                view = null;
            }
        }

    }

}
