package de.cooperateproject.plantumlpp.notation2plant.generator.tests;

//import java.io.File;
//import java.io.IOException;
import java.util.ArrayList;
//import java.util.Iterator;
import java.util.List;

//import static org.mockito.Mockito.*;

import org.eclipse.core.resources.IFile;
/*import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;*/
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
/*import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;*/

import de.cooperateproject.generator.PlantGeneratorHandler;

public class PlantGeneratorHandlerTest {
		
	private static PlantGeneratorHandler generatorHandler;

	@BeforeClass
	public static void setUp() {
		generatorHandler = new PlantGeneratorHandler();
	}

	@Test
	public void PlantGeneratorHandlerNullTest() throws Exception {
		generatorHandler.executeEcore2TxtGenerator(null, null, null);
	}

	@Test
	public void PlantGeneratorHandlerEmptyListTest() throws Exception {
		List<IFile> mockList = new ArrayList<IFile>();
		generatorHandler.executeEcore2TxtGenerator(mockList, null, null);
	}

	/*@Test
	public void PlantGeneratorHandlerListTest() throws IOException {
		MockitoAnnotations.initMocks(this);
		List<IFile> mockList = new ArrayList<IFile>();		
		String filename = "test";		
		String s = "file:/C:/Users/czogalik/Documents/cooperate/workspace/PlantUMLPrinterParser/de.cooperateproject.plantumlpp.notation2plant.generator.tests/test";
		
		IFile resource = mock(IFile.class);
		
		IPath path = mock(IPath.class);
		when(path.toString()).thenReturn(filename);
		
		File file = mock(File.class);
		when(file.getName()).thenReturn(filename);
		when(file.getPath()).thenReturn(filename);
		when(file.getAbsolutePath()).thenReturn(filename);
		
		when(path.toFile()).thenReturn(file);
		when(path.toOSString()).thenReturn(filename);
		when(path.removeFileExtension()).thenReturn(path);
		//URI u = mock(URI.class);
		//when(path.getFileExtension()).thenReturn(split[1]);

		when(resource.getLocation()).thenReturn(path);

		// separates "filename" of ".java"
		when(resource.getName()).thenReturn(filename);
		when(resSet.getResource(URI.createFileURI(s), true)).thenReturn(mock(Resource.class));
		
		//when(resource.getFileExtension()).thenReturn(split[1]);
		
		when(path.addFileExtension("uml")).thenReturn(path);
		
		
		
		
		mockList.add(resource);
		mockHandler.executeEcore2TxtGenerator(mockList, null);
	}*/

	@AfterClass
	public static void cleanUp() {
		generatorHandler = null;
	}

}
