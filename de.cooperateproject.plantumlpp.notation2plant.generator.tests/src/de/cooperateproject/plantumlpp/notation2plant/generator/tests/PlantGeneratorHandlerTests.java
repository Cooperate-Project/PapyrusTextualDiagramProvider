package de.cooperateproject.plantumlpp.notation2plant.generator.tests;


import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import static org.mockito.Mockito.*;

import org.eclipse.core.resources.IFile;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.cooperateproject.generator.PlantGeneratorHandler;

public class PlantGeneratorHandlerTests {

	private static PlantGeneratorHandler generatorHandler;
	
	@BeforeClass
	public static void setUp() {
		generatorHandler = new PlantGeneratorHandler();
	}
	
	@Test
	public void PlantGeneratorHandlerNullTest() throws IOException {
		generatorHandler.executeEcore2TxtGenerator(null, null);
	}
	@Test
	@SuppressWarnings("unchecked")
	public void PlantGeneratorHandlerNullElementTest() throws IOException {		
		List<IFile> mockList = mock(List.class);
		Iterator<IFile> mockIterator = mock(Iterator.class);
		when(mockList.iterator()).thenReturn(mockIterator);
		when(mockIterator.next()).thenReturn(null);
		generatorHandler.executeEcore2TxtGenerator(mockList, null);
	}
	@AfterClass
	public static void cleanUp() {
		generatorHandler = null;
	}

}
