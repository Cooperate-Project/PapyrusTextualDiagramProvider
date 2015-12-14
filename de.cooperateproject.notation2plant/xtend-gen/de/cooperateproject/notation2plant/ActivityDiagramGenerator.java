package de.cooperateproject.notation2plant;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import java.util.Arrays;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Connector;
import org.eclipse.gmf.runtime.notation.DecorationNode;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Shape;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.ActivityEdge;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.ControlFlow;
import org.eclipse.uml2.uml.DecisionNode;
import org.eclipse.uml2.uml.FinalNode;
import org.eclipse.uml2.uml.ForkNode;
import org.eclipse.uml2.uml.InitialNode;
import org.eclipse.uml2.uml.JoinNode;
import org.eclipse.uml2.uml.MergeNode;
import org.eclipse.uml2.uml.OpaqueAction;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.ValueSpecification;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class ActivityDiagramGenerator {
  private final static Logger LOG = Logger.getLogger("PlantGenerator");
  
  private Activity rootActivity;
  
  private ActivityNode endElement;
  
  private HashMap<ActivityNode, ControlFlow> controlFlow = new HashMap<ActivityNode, ControlFlow>();
  
  public void initDiagram(final Diagram diagram) {
    EObject rootElement = diagram.getElement();
    if ((rootElement instanceof Activity)) {
      this.rootActivity = ((Activity)rootElement);
    }
    EList _edges = diagram.getEdges();
    Iterable<Connector> _filter = Iterables.<Connector>filter(_edges, Connector.class);
    final Function1<Connector, EObject> _function = (Connector c) -> {
      return c.getElement();
    };
    Iterable<EObject> _map = IterableExtensions.<Connector, EObject>map(_filter, _function);
    Iterable<ControlFlow> _filter_1 = Iterables.<ControlFlow>filter(_map, ControlFlow.class);
    for (final ControlFlow c : _filter_1) {
      ActivityNode _source = c.getSource();
      this.controlFlow.put(_source, c);
    }
  }
  
  public CharSequence compileActivityDiagram(final Diagram diagram) {
    StringConcatenation _builder = new StringConcatenation();
    this.initDiagram(diagram);
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("@startuml");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("title ");
    String _name = diagram.getName();
    _builder.append(_name, "\t");
    _builder.newLineIfNotEmpty();
    {
      EList _children = diagram.getChildren();
      Iterable<Shape> _filter = Iterables.<Shape>filter(_children, Shape.class);
      for(final Shape s : _filter) {
        _builder.append("\t");
        EObject _element = s.getElement();
        CharSequence _declaration = this.declaration(_element, s);
        _builder.append(_declaration, "\t");
        _builder.append(" ");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.append("@enduml");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _declaration(final Activity a, final Shape s) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList _children = s.getChildren();
      Iterable<DecorationNode> _filter = Iterables.<DecorationNode>filter(_children, DecorationNode.class);
      for(final DecorationNode bc : _filter) {
        _builder.append("\t", "");
        _builder.newLineIfNotEmpty();
        {
          EList _children_1 = bc.getChildren();
          Iterable<Shape> _filter_1 = Iterables.<Shape>filter(_children_1, Shape.class);
          final Function1<Shape, EObject> _function = (Shape c) -> {
            return c.getElement();
          };
          Iterable<EObject> _map = IterableExtensions.<Shape, EObject>map(_filter_1, _function);
          Iterable<InitialNode> _filter_2 = Iterables.<InitialNode>filter(_map, InitialNode.class);
          for(final InitialNode start : _filter_2) {
            CharSequence _declaration = this.declaration(start);
            _builder.append(_declaration, "");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    return _builder;
  }
  
  protected CharSequence _declaration(final InitialNode i) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("start");
    _builder.newLine();
    {
      EList<ActivityEdge> _outgoings = i.getOutgoings();
      for(final ActivityEdge flow : _outgoings) {
        ActivityNode _target = flow.getTarget();
        CharSequence _declaration = this.declaration(_target);
        _builder.append(_declaration, "");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  protected CharSequence _declaration(final FinalNode i) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("stop");
    return _builder;
  }
  
  protected CharSequence _declaration(final ForkNode i) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("fork");
    {
      EList<ActivityEdge> _outgoings = i.getOutgoings();
      boolean _hasElements = false;
      for(final ActivityEdge flow : _outgoings) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate("\nfork again", "");
        }
        _builder.newLineIfNotEmpty();
        _builder.append(" ", "");
        ActivityNode _target = flow.getTarget();
        CharSequence _declaration = this.declaration(_target);
        _builder.append(_declaration, "");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("end fork");
    _builder.newLine();
    {
      EList<ActivityEdge> _outgoings_1 = this.endElement.getOutgoings();
      for(final ActivityEdge flow_1 : _outgoings_1) {
        ActivityNode _target_1 = flow_1.getTarget();
        CharSequence _declaration_1 = this.declaration(_target_1);
        _builder.append(_declaration_1, "");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  protected CharSequence _declaration(final JoinNode i) {
    this.endElement = i;
    return "";
  }
  
  protected CharSequence _declaration(final DecisionNode i) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<ActivityEdge> _outgoings = i.getOutgoings();
      for(final ActivityEdge flow : _outgoings) {
        {
          EList<ActivityEdge> _outgoings_1 = i.getOutgoings();
          ActivityEdge _head = IterableExtensions.<ActivityEdge>head(_outgoings_1);
          boolean _equals = Objects.equal(flow, _head);
          if (_equals) {
            _builder.append("if() then (");
            ValueSpecification _guard = flow.getGuard();
            CharSequence _printBody = this.printBody(_guard);
            _builder.append(_printBody, "");
            _builder.append(")");
            _builder.newLineIfNotEmpty();
          } else {
            EList<ActivityEdge> _outgoings_2 = i.getOutgoings();
            ActivityEdge _last = IterableExtensions.<ActivityEdge>last(_outgoings_2);
            boolean _equals_1 = Objects.equal(flow, _last);
            if (_equals_1) {
              _builder.append("else (");
              ValueSpecification _guard_1 = flow.getGuard();
              CharSequence _printBody_1 = this.printBody(_guard_1);
              _builder.append(_printBody_1, "");
              _builder.append(")");
              _builder.newLineIfNotEmpty();
            } else {
              _builder.append("elseif () then (");
              ValueSpecification _guard_2 = flow.getGuard();
              CharSequence _printBody_2 = this.printBody(_guard_2);
              _builder.append(_printBody_2, "");
              _builder.append(")");
              _builder.newLineIfNotEmpty();
            }
          }
        }
        _builder.append(" ", "");
        ActivityNode _target = flow.getTarget();
        CharSequence _declaration = this.declaration(_target);
        _builder.append(_declaration, "");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("endif");
    _builder.newLine();
    {
      EList<ActivityEdge> _outgoings_3 = this.endElement.getOutgoings();
      for(final ActivityEdge flow_1 : _outgoings_3) {
        ActivityNode _target_1 = flow_1.getTarget();
        CharSequence _declaration_1 = this.declaration(_target_1);
        _builder.append(_declaration_1, "");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  protected CharSequence _declaration(final MergeNode i) {
    this.endElement = i;
    return "";
  }
  
  protected CharSequence _declaration(final OpaqueAction i) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(":");
    String _name = i.getName();
    _builder.append(_name, "");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    {
      EList<ActivityEdge> _outgoings = i.getOutgoings();
      for(final ActivityEdge flow : _outgoings) {
        ActivityNode _target = flow.getTarget();
        CharSequence _declaration = this.declaration(_target);
        _builder.append(_declaration, "");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  public CharSequence getNames(final EList<Classifier> list) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _hasElements = false;
      for(final Classifier c : list) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(",", "");
        }
        String _name = c.getName();
        _builder.append(_name, "");
      }
    }
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence _declaration(final EObject e, final Shape s) {
    EClass _eClass = e.eClass();
    String _name = _eClass.getName();
    String _plus = ("encountered a " + _name);
    ActivityDiagramGenerator.LOG.error(_plus);
    throw new UnsupportedOperationException();
  }
  
  protected CharSequence _printBody(final ValueSpecification specification) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(specification, "");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence _printBody(final OpaqueExpression specification) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<String> _bodies = specification.getBodies();
      for(final String b : _bodies) {
        _builder.append(b, "");
      }
    }
    return _builder;
  }
  
  public String leftArrow(final Property property) {
    String _xblockexpression = null;
    {
      final Property opp = property.getOtherEnd();
      String _switchResult = null;
      AggregationKind _aggregation = opp.getAggregation();
      int _value = _aggregation.getValue();
      switch (_value) {
        case AggregationKind.COMPOSITE:
          _switchResult = "*";
          break;
        case AggregationKind.SHARED:
          _switchResult = "o";
          break;
        default:
          String _xifexpression = null;
          boolean _isNavigable = opp.isNavigable();
          boolean _not = (!_isNavigable);
          if (_not) {
            _xifexpression = "<";
          } else {
            _xifexpression = "";
          }
          _switchResult = _xifexpression;
          break;
      }
      _xblockexpression = _switchResult;
    }
    return _xblockexpression;
  }
  
  public String rightArrow(final Property property) {
    String _xblockexpression = null;
    {
      final Property opp = property.getOtherEnd();
      String _switchResult = null;
      AggregationKind _aggregation = opp.getAggregation();
      int _value = _aggregation.getValue();
      switch (_value) {
        case AggregationKind.COMPOSITE:
          _switchResult = "*";
          break;
        case AggregationKind.SHARED:
          _switchResult = "o";
          break;
        default:
          String _xifexpression = null;
          boolean _isNavigable = opp.isNavigable();
          boolean _not = (!_isNavigable);
          if (_not) {
            _xifexpression = ">";
          } else {
            _xifexpression = "";
          }
          _switchResult = _xifexpression;
          break;
      }
      _xblockexpression = _switchResult;
    }
    return _xblockexpression;
  }
  
  public CharSequence cardinality(final Property p) {
    CharSequence _xifexpression = null;
    boolean _and = false;
    int _lowerBound = p.lowerBound();
    boolean _equals = (_lowerBound == 0);
    if (!_equals) {
      _and = false;
    } else {
      int _upperBound = p.upperBound();
      boolean _equals_1 = (_upperBound == (-1));
      _and = _equals_1;
    }
    if (_and) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(" ");
      _builder.append("*");
      _xifexpression = _builder;
    } else {
      CharSequence _xifexpression_1 = null;
      boolean _and_1 = false;
      int _lowerBound_1 = p.lowerBound();
      boolean _equals_2 = (_lowerBound_1 == 1);
      if (!_equals_2) {
        _and_1 = false;
      } else {
        int _upperBound_1 = p.upperBound();
        boolean _equals_3 = (_upperBound_1 == (-1));
        _and_1 = _equals_3;
      }
      if (_and_1) {
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append(" ");
        _builder_1.append("1..*");
        _xifexpression_1 = _builder_1;
      } else {
        CharSequence _xifexpression_2 = null;
        int _lowerBound_2 = p.lowerBound();
        int _upperBound_2 = p.upperBound();
        boolean _equals_4 = (_lowerBound_2 == _upperBound_2);
        if (_equals_4) {
          StringConcatenation _builder_2 = new StringConcatenation();
          int _lowerBound_3 = p.lowerBound();
          _builder_2.append(_lowerBound_3, "");
          _xifexpression_2 = _builder_2;
        } else {
          StringConcatenation _builder_3 = new StringConcatenation();
          _builder_3.append(" ");
          int _lowerBound_4 = p.lowerBound();
          _builder_3.append(_lowerBound_4, " ");
          _builder_3.append("..");
          int _upperBound_3 = p.upperBound();
          _builder_3.append(_upperBound_3, " ");
          _xifexpression_2 = _builder_3;
        }
        _xifexpression_1 = _xifexpression_2;
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  public CharSequence defineActivity(final Activity activity) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("partition ");
    String _name = activity.getName();
    _builder.append(_name, "");
    return _builder;
  }
  
  public CharSequence declaration(final EObject a, final Shape s) {
    if (a instanceof Activity) {
      return _declaration((Activity)a, s);
    } else if (a != null) {
      return _declaration(a, s);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(a, s).toString());
    }
  }
  
  public CharSequence declaration(final ActivityNode i) {
    if (i instanceof OpaqueAction) {
      return _declaration((OpaqueAction)i);
    } else if (i instanceof DecisionNode) {
      return _declaration((DecisionNode)i);
    } else if (i instanceof FinalNode) {
      return _declaration((FinalNode)i);
    } else if (i instanceof ForkNode) {
      return _declaration((ForkNode)i);
    } else if (i instanceof InitialNode) {
      return _declaration((InitialNode)i);
    } else if (i instanceof JoinNode) {
      return _declaration((JoinNode)i);
    } else if (i instanceof MergeNode) {
      return _declaration((MergeNode)i);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(i).toString());
    }
  }
  
  public CharSequence printBody(final ValueSpecification specification) {
    if (specification instanceof OpaqueExpression) {
      return _printBody((OpaqueExpression)specification);
    } else if (specification != null) {
      return _printBody(specification);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(specification).toString());
    }
  }
}
