package de.cooperateproject.generator 

import org.apache.log4j.Logger
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.gmf.runtime.notation.Diagram
import org.eclipse.xtext.generator.IFileSystemAccess
import org.eclipse.xtext.generator.IGenerator
import de.cooperateproject.notation2plant.ClassDiagramGenerator
import de.cooperateproject.notation2plant.ActivityDiagramGenerator
import de.cooperateproject.notation2plant.UseCaseDiagramGenerator

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
		if(diagram.type == "PapyrusUMLClassDiagram") {
			var generator = new ClassDiagramGenerator(); 
			generator.compileClassDiagram(diagram)
		} else if(diagram.type == "PapyrusUMLActivityDiagram") {
			var generator = new ActivityDiagramGenerator();
			generator.compileActivityDiagram(diagram)
		} else if (diagram.type == "UseCase") {
			var generator = new UseCaseDiagramGenerator();
			generator.compileUseCaseDiagram(diagram)
		}
		else {
			LOG.error("no matching diagram type found for: " + diagram.name);
		 	"(empty)"
		}
	}
	
}