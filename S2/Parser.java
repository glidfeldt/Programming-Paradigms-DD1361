import java.util.*;

/**
 * En rekursiv medåknings-parser för binära träd
 *
 * Utgått ifrån kod av: Per Austrin
 */
public class Parser {
	private Lexer lexer;

	public Parser(Lexer lexer) {
		this.lexer = lexer;
	}

	public ParseTree parse() throws SyntaxError {
		// Startsymbol är Expr
		ParseTree result = expression();
		// Borde inte finnas något kvar av indata när vi parsat ett bintree
		Token t = lexer.nextToken();
		if (t.getType() != TokenType.LastToken) {
			throw new SyntaxError(t.getRow());
		}
		return result;
	}

	private ParseTree expression() throws SyntaxError {
		// Kolla så att nästa token är en instruktion
		List<ParseTree> list = new ArrayList<ParseTree>();
		while(lexer.peekToken().getType()==TokenType.Forw||
				lexer.peekToken().getType()==TokenType.Back||
				lexer.peekToken().getType()==TokenType.Left||
				lexer.peekToken().getType()==TokenType.Right||
				lexer.peekToken().getType()==TokenType.Down||
				lexer.peekToken().getType()==TokenType.Up||
				lexer.peekToken().getType()==TokenType.Color||
				lexer.peekToken().getType()==TokenType.Rep){
			list.add(instruction());

		}

		return new ExprNode(list);
	}


	private ParseTree instruction() throws SyntaxError{
		Token t = lexer.nextToken();

		if(t.getType()==TokenType.Forw){
			return checkMoveNode(t);

		}else if(t.getType()==TokenType.Back){
			return checkMoveNode(t);

		}else if(t.getType()==TokenType.Left){
			return checkTurnNode(t);

		}else if(t.getType()==TokenType.Right){
			return checkTurnNode(t);

		}else if(t.getType()==TokenType.Down){
			return checkPenNode(t);

		}else if(t.getType()==TokenType.Up){
			return checkPenNode(t);

		}else if(t.getType()==TokenType.Color){
			return checkColorNode(t);

		}else if(t.getType()==TokenType.Rep){
			return checkRepNode(t);

		}else{
			throw new SyntaxError(t.getRow());
		}
	}



	//*****Check moveNode(Forw|Back, Unit)*****
	private MoveNode checkMoveNode(Token t) throws SyntaxError{
		//Get row
		int row = t.getRow();
		if(!confirmNum(row)){
			throw new SyntaxError(lexer.nextToken().getRow()); //Om inte num
		}

		//Get num token
		Token numToken = lexer.nextToken();
		if (!confirmDot(numToken.getRow()))
			throw new SyntaxError(lexer.nextToken().getRow()); //Om inte punkt


		//Ignore dot
		lexer.nextToken();

		//Return Forw|Back, Unit
		return new MoveNode(t.getType(), (int)numToken.getData());
	}

	//*****Check moveNode(Left|Right, Unit)*****
	private TurnNode checkTurnNode(Token t) throws SyntaxError{
		//Get row
		int row = t.getRow();
		if(!confirmNum(row)){
			throw new SyntaxError(lexer.nextToken().getRow()); //Om inte num
		}

		//Get num token
		Token numToken = lexer.nextToken();
		if (!confirmDot(numToken.getRow()))
			throw new SyntaxError(lexer.nextToken().getRow()); //Om inte punkt


		//Ignore dot
		lexer.nextToken();

		//Return Forw|Back, Unit
		return new TurnNode(t.getType(), (int)numToken.getData());
	}

	//*****Check penNode(Up|Down)*****
	private PenNode checkPenNode(Token t) throws SyntaxError{
		if (!confirmDot(t.getRow()))
			throw new SyntaxError(lexer.nextToken().getRow()); //Om inte dot
		lexer.nextToken(); //Bort me dot
		return new PenNode(t.getType());
	}

	private ColorNode checkColorNode(Token t) throws SyntaxError{
		//Confirm color
		if (!confirmColor(t.getRow()))
			throw new SyntaxError(lexer.nextToken().getRow()); //Om inte legit color

		//Confirm hexcode
		Token colorToken = lexer.nextToken();
		if (!confirmDot(colorToken.getRow()))
			throw new SyntaxError(lexer.nextToken().getRow()); //Om inte dot

		lexer.nextToken(); //ta bort dot
		return new ColorNode((String) colorToken.getData());
	}

	private RepeatNode checkRepNode(Token t) throws SyntaxError{
		if (!confirmNum(t.getRow()))
			throw new SyntaxError(lexer.nextToken().getRow()); // if something else then num
		Token repeatsToken = lexer.nextToken(); // antal repeats
		if (confirmQuote(repeatsToken.getRow())) { // om quote
			lexer.nextToken(); // delte quoten
			ParseTree expr = expression();
			if (confirmQuote(lexer.prevToken().getRow())) {
				lexer.nextToken(); // tar bort quote
				if(expr.isEmpty()){
					throw new SyntaxError(lexer.prevToken().getRow());
				}
				return new RepeatNode((int)repeatsToken.getData(), expr);
			} else {
				throw new SyntaxError(lexer.nextToken().getRow());
			}
		} else {
			ParseTree inst = instruction();
			return new RepeatNode((int)repeatsToken.getData(), inst);
		}
	}

	//*****Check syntax****
	private boolean confirmQuote(int row) throws SyntaxError{
		Token t = lexer.peekToken();
		if (t.getType() == TokenType.Quote)
			return true;
		else if (t.getType() == TokenType.LastToken)
			throw new SyntaxError(row);
		return false;
	}

	private boolean confirmDot(int row) throws SyntaxError{
		Token t = lexer.peekToken();
		if (t.getType() == TokenType.Dot)
			return true;
		else if (t.getType() == TokenType.LastToken)
			throw new SyntaxError(row);
		return false;

	}

	private boolean confirmColor(int row) throws SyntaxError{
		Token t = lexer.peekToken();
		if (t.getType() == TokenType.LegitColor)
			return true;
		else if (t.getType() == TokenType.LastToken)
			throw new SyntaxError(row);
		return false;
	}

	private boolean confirmNum(int row) throws SyntaxError{
		Token t = lexer.peekToken();
		if (t.getType() == TokenType.Num)
			return true;
		else if (t.getType() == TokenType.LastToken)
			throw new SyntaxError(row);
		return false;
	}
}