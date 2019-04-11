// Utgått ifrån kod av: Per Austrin

import java.util.*;

// Ett syntaxträd
abstract class ParseTree {
	abstract public void process(Turtle obj);

    public abstract boolean isEmpty();
}

class ExprNode extends ParseTree {
	public List<ParseTree> list;

	public ExprNode(List<ParseTree> list) {
		this.list = list;
	}


	public void process(Turtle obj) {
		for (ParseTree instruction : list)
			instruction.process(obj);
	}

	public boolean isEmpty(){
	    return this.list.isEmpty();
    }
}

abstract class InstructionNode extends ParseTree {

}


//PenNode (Dir: Up|Down)
class PenNode extends InstructionNode{
	TokenType type;

	public PenNode(TokenType type){
		this.type=type;
	}

	public void process(Turtle obj){
		if(type==TokenType.Down){
			obj.activate(true);
		}else{
			obj.activate(false);
		}
	}
    public boolean isEmpty(){
        return true;
    }
}

//MoveNode (Dir: Forw|back, Unit: int)
class MoveNode extends InstructionNode{
	TokenType type;
	int unit;

	public MoveNode(TokenType type, int unit){
		this.type=type;
		this.unit=unit;
	}

	public void process(Turtle obj){
		if(type==TokenType.Forw){
			obj.moveForw(this.unit);
		}else{
			obj.moveBack(this.unit);
		}
	}

    public boolean isEmpty(){
        return true;
    }
}

//TurnNode (Dir: Left|Right, Unit: double)
class TurnNode extends InstructionNode{
	TokenType type;
	int unit;

	public TurnNode(TokenType type, int unit){
		this.type=type;
		this.unit=unit;
	}

	public void process(Turtle obj){
		if(type==TokenType.Left){
			obj.turnLeft(this.unit);
		}else{
			obj.turnRight(this.unit);
		}
	}

    public boolean isEmpty(){
        return true;
    }
}

//RepeatNode(Dir: Rep, Unit: int, REPS: InstructionNode|
class RepeatNode extends InstructionNode {
	int repeats;
	ParseTree expr;

	public RepeatNode(int repeats, ParseTree expr) {
		this.expr = expr;
		this.repeats = repeats;
	}

	public boolean isEmpty(){
	    return this.expr.isEmpty();
    }

	public void process(Turtle obj) {
		for (int i = 0; i < repeats; i++) {
			expr.process(obj);
		}
	}
}

class ColorNode extends InstructionNode{
    String color;

    public ColorNode(String color){
        this.color=color;
    }

    public void process(Turtle obj){
        obj.setColor(this.color);
    }
    public boolean isEmpty(){
        return true;
    }
}
