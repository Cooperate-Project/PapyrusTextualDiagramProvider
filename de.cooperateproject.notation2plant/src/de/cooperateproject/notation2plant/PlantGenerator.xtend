package de.cooperateproject.notation2plant

import org.apache.log4j.Logger
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.gmf.runtime.notation.Diagram
import org.eclipse.xtext.generator.IFileSystemAccess
import org.eclipse.xtext.generator.IGenerator



class PlantGenerator implements IGenerator {
	
	private static final Logger LOG =  Logger.getLogger("PlantGenerator");
	
	override doGenerate(Resource resource, IFileSystemAccess fsa) {
		for(d: resource.allContents.toIterable.filter(Diagram)) {
   			 fsa.generateFile(
      			d.name.replace(' ', '_') + ".pu",
      			d.compile)
  		}
	}
	
	def CharSequence compile(Diagram diagram) {
		if(diagram.type == "PapyrusUMLClassDiagram"){
			var generator = new ClassDiagramGenerator();
			generator.compileClassDiagram(diagram)
		}
		else if(diagram.type == "PapyrusUMLActivityDiagram"){
			var generator = new ActivityDiagramGenerator();
			generator.compileActivityDiagram(diagram)
		}
		else
		 "(empty)"
	}
	
	
	
}