package de.cooperateproject.notation2plant

import java.util.HashMap
import org.apache.log4j.Logger
import org.eclipse.emf.common.util.EList
import org.eclipse.emf.ecore.EObject
import org.eclipse.gmf.runtime.notation.Connector
import org.eclipse.gmf.runtime.notation.DecorationNode
import org.eclipse.gmf.runtime.notation.Diagram
import org.eclipse.gmf.runtime.notation.Shape
import org.eclipse.uml2.uml.Activity
import org.eclipse.uml2.uml.ActivityNode
import org.eclipse.uml2.uml.AggregationKind
import org.eclipse.uml2.uml.Classifier
import org.eclipse.uml2.uml.ControlFlow
import org.eclipse.uml2.uml.DecisionNode
import org.eclipse.uml2.uml.FinalNode
import org.eclipse.uml2.uml.ForkNode
import org.eclipse.uml2.uml.InitialNode
import org.eclipse.uml2.uml.JoinNode
import org.eclipse.uml2.uml.MergeNode
import org.eclipse.uml2.uml.OpaqueAction
import org.eclipse.uml2.uml.OpaqueExpression
import org.eclipse.uml2.uml.Property
import org.eclipse.uml2.uml.ValueSpecification

class ActivityDiagramGenerator {
	
	private static final Logger LOG =  Logger.getLogger("PlantGenerator");
	var Activity rootActivity;
	var ActivityNode endElement;
	var controlFlow = new HashMap<ActivityNode, ControlFlow>();
	
	def initDiagram(Diagram diagram){
		var rootElement = diagram.element
 		if(rootElement instanceof Activity)
			rootActivity = rootElement
		for(c: diagram.edges.filter(Connector).map[c|c.element].filter(ControlFlow))
			controlFlow.put(c.source, c)
	}
	
	
	def compileActivityDiagram(Diagram diagram)'''
	«initDiagram(diagram)»
		@startuml
		title «diagram.name»
		«FOR s: diagram.children.filter(Shape)»
			«s.element.declaration(s)» 
		«ENDFOR»
		@enduml
	'''
	
	
	def dispatch CharSequence declaration(Activity a, Shape s)'''
«««		«defineActivity(a)» {
		«FOR bc: s.children.filter(DecorationNode)»«"\t"»
		«FOR start: bc.children.filter(Shape).map[c|c.element].filter(InitialNode)»
			«start.declaration»
		«ENDFOR»
		«ENDFOR»
«««		}
	'''
	
	
	
	def dispatch CharSequence declaration(InitialNode i)'''
	start
	«FOR flow: i.outgoings»
	«flow.target.declaration()»
	«ENDFOR»
	'''
	
	def dispatch CharSequence declaration(FinalNode i)'''
	stop'''
	
	def dispatch CharSequence declaration(ForkNode i)'''
	fork«FOR flow: i.outgoings SEPARATOR "\nfork again"»
	«" "»«flow.target.declaration()»
	«ENDFOR»
	end fork
	«FOR flow: endElement.outgoings»
	«flow.target.declaration()»
	«ENDFOR»
	'''
	
	def dispatch CharSequence declaration(JoinNode i){
			endElement = i;		
			return ""
	}
	
	def dispatch CharSequence declaration(DecisionNode i)'''
	«FOR flow: i.outgoings»
	«IF flow == i.outgoings.head»
	if() then («flow.guard.printBody»)
	«ELSEIF flow == i.outgoings.last»
	else («flow.guard.printBody»)
	«ELSE»
	elseif () then («flow.guard.printBody»)
	«ENDIF»
	«" "»«flow.target.declaration()»
	«ENDFOR»
	endif
	«FOR flow: endElement.outgoings»
	«flow.target.declaration()»
	«ENDFOR»
	'''
	
	
	
	def dispatch CharSequence declaration(MergeNode i){
		endElement = i;
		return ""
	}
	
	def dispatch CharSequence declaration(OpaqueAction i)'''
	 :«i.name»;
	 «FOR flow: i.outgoings»
	 «flow.target.declaration()»
	 «ENDFOR»
	 '''
	
	
	def getNames(EList<Classifier> list)'''
	«FOR c : list SEPARATOR ','»«c.name»«ENDFOR»
	'''
	

	
	
	
	def dispatch declaration(EObject e, Shape s){
		LOG.error("encountered a " + e.eClass.name);
		throw new UnsupportedOperationException		
	}
	
	
	
	
	
	def dispatch CharSequence printBody(ValueSpecification specification)'''
	«specification»
	'''
	
	def dispatch CharSequence printBody(OpaqueExpression specification)
	'''«FOR b :specification.bodies»«b»«ENDFOR»'''
	
	
	
	def leftArrow(Property property){
		val opp = property.otherEnd 
		switch opp.aggregation.value{
			case AggregationKind.COMPOSITE : "*"
		 	case AggregationKind.SHARED : "o"
		 	default :
		 		if(!opp.navigable)
		 			"<"
				else
					""
			}
	}
	
	def rightArrow(Property property){
		val opp = property.otherEnd
		switch opp.aggregation.value{
			case AggregationKind.COMPOSITE : "*"
		 	case AggregationKind.SHARED : "o"
		 	default :
		 		if(!opp.navigable)
		 			">"
				else
					""
			}
	}
		
	def cardinality(Property p){
	if(p.lowerBound == 0 && p.upperBound == -1)
		''' *'''
	else if(p.lowerBound == 1 && p.upperBound == -1)
		''' 1..*'''
	else if(p.lowerBound == p.upperBound)
		'''«p.lowerBound»'''
	else
		''' «p.lowerBound»..«p.upperBound»'''	
	}
	
	
	
	
	def defineActivity(Activity activity)'''
	partition «activity.name»'''
	
	
}