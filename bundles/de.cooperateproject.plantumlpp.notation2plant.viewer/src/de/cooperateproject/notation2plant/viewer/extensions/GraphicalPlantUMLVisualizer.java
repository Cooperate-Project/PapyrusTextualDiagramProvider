package de.cooperateproject.notation2plant.viewer.extensions;

import org.eclipse.ui.PartInitException;

public interface GraphicalPlantUMLVisualizer {
    
    public void init() throws PartInitException;
    public void visualize(String plantUML);
    public void dispose();
    
}
