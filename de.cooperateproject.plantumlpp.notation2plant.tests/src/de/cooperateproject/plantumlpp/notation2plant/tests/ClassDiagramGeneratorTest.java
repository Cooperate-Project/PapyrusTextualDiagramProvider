package de.cooperateproject.plantumlpp.notation2plant.tests;

import java.io.IOException;

import org.eclipse.gmf.runtime.notation.Diagram;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.cooperateproject.notation2plant.ClassDiagramGenerator;

public class ClassDiagramGeneratorTest extends FileRessource {
	private static ClassDiagramGenerator generator;

	@BeforeClass
	public static void setUp() {
		generator = new ClassDiagramGenerator();
		ressourceSetUp();
	}
	@Test
	public void nullDiagramTest() {
		generator.compileClassDiagram(null);
	}
	
	@Test
	public void test1() throws IOException {
		String s = "";
		for (final Diagram d : getDiagrams("testdiagrams/class.notation")) {
			s = generator.compileClassDiagram(d).toString();
			s = s;
		}
		
	}
	
	@AfterClass
	public static void cleanUp() {
		generator = null;
	}
}
