// Utgått ifrån kod av: Per Austrin

// De olika token-typer vi har i grammatiken
enum TokenType {
	Down, Left, Right, Num, Invalid, Forw, Dot, LastToken, Up, Rep, Back, Color, LegitColor, Quote
}

// Klass för att representera en token
// I praktiken vill man nog även spara info om vilken rad/position i
// indata som varje token kommer ifrån, för att kunna ge bättre
// felmeddelanden
class Token {
	private TokenType type;
	private Object data;
	private int row;

	public Token(TokenType type) {
		this.type = type;
		this.data = null;
	}

	public Token(TokenType type, int row){
		this.type=type;
		this.row=row;
	}

	public Token(TokenType type, int row, Object data){
		this.type=type;
		this.row=row;
		this.data=data;
	}


	//Return classes
	public TokenType getType() { return type; }
	public Object getData() { return data; }
	public int getRow(){return row;}

}
