
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * En klass för att göra lexikal analys, konvertera indataströmmen
 * till en sekvens av tokens.  Den här klassen läser in hela
 * indatasträngen och konverterar den på en gång i konstruktorn.  Man
 * kan tänka sig en variant som läser indataströmmen allt eftersom
 * tokens efterfrågas av parsern, men det blir lite mer komplicerat.
 *
 * Utgått ifrån kod av: Per Austrin
 */
public class Lexer {
	private String input;
	private List<Token> tokens;
	private int currentToken;
	StringBuilder parseInt;

	// Hjälpmetod som läser in innehållet i en inputstream till en
	// sträng
	private static String readInput(InputStream f) throws java.io.IOException {
		Reader stdin = new InputStreamReader(f);
		StringBuilder buf = new StringBuilder();
		char input[] = new char[1024];
		int read = 0;
		while ((read = stdin.read(input)) != -1) {
			buf.append(input, 0, read);
		}
		return buf.toString();
	}

	private static String readManInput() throws java.io.IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder buf = new StringBuilder();
		String line=br.readLine();
		int inp = Integer.parseInt(line);
		for(int i=0; i<inp; i++){
			buf.append(br.readLine()+"\n");
		}

		return buf.toString();
	}


	public Lexer(InputStream in) throws java.io.IOException {
		//input = Lexer.readManInput().toLowerCase();
		input = Lexer.readInput(in).toLowerCase();

		// Ett regex som beskriver hur ett token kan se ut, plus whitespace (som vi här vill ignorera helt)
		Pattern tokenPattern = Pattern.compile(
				"%.*"+
						"|down" +
						"|up" +
						"|forw" +
						"|back" +
						"|left" +
						"|right" +
						"|color" +
						"|rep" +
						"|[1-9][0-9]*" +
						"|[#][0-9a-f]{6}" +
						"|\\." +
						"|\"" +
						"|\\s"
		);



		Matcher m = tokenPattern.matcher(input);
		int inputPos = 0;
		tokens = new ArrayList<Token>();
		currentToken = 0;
		int rowCounter=1;
		boolean spaceNeeded = false;


		// Hitta förekomster av tokens/whitespace i indata
		while (m.find()) {
			// Om matchningen inte börjar där den borde har vi hoppat
			// över något skräp i indata, markera detta som ett
			// "Invalid"-token
			if (m.start() != inputPos) {
				tokens.add(new Token(TokenType.Invalid, rowCounter));
			}
			String test = m.group().replace("(%.*)", "");
			// Kolla vad det var som matchade

			if(spaceNeeded && !test.matches("\\s|%.*")){
				tokens.add(new Token(TokenType.Invalid, rowCounter));
				spaceNeeded=false;
			}else if(test.matches("\\s|%.*")){
				spaceNeeded=false;
			}

			if (test.matches("\\n")){
				rowCounter++;
			}

			if (test.equals("forw")) {
				tokens.add(new Token(TokenType.Forw, rowCounter));
				spaceNeeded=true;
			}
			else if (test.equals(".")){
				tokens.add(new Token(TokenType.Dot, rowCounter));
			}
			else if (test.equals("back")){
				tokens.add(new Token(TokenType.Back, rowCounter));
				spaceNeeded=true;
			}
			else if (test.equals("left")){
				tokens.add(new Token(TokenType.Left, rowCounter));
				spaceNeeded=true;
			}
			else if (test.equals("right")){
				tokens.add(new Token(TokenType.Right, rowCounter));
				spaceNeeded=true;
			}
			else if (test.equals("up")){
				tokens.add(new Token(TokenType.Up, rowCounter));
			}
			else if (test.equals("down")) {
				tokens.add(new Token(TokenType.Down, rowCounter));
			}
			else if (test.equals("color")){
				tokens.add(new Token(TokenType.Color, rowCounter));
				spaceNeeded=true;
			}
			else if (test.equals("rep")) {
				tokens.add(new Token(TokenType.Rep, rowCounter));
				spaceNeeded=true;
			}
			else if (test.equals("\"")){
				tokens.add(new Token(TokenType.Quote, rowCounter));
			}
			else if (Character.isDigit(test.charAt(0))){
				tokens.add(new Token(TokenType.Num, rowCounter, Integer.parseInt(test)));
				if(tokens.get(tokens.size()-2).getType()==TokenType.Rep){
					spaceNeeded=true;
				}
			}
			else if (test.matches("[#][0-9a-f]{6}")){
				tokens.add(new Token(TokenType.LegitColor, rowCounter, test));
			}

			inputPos = m.end();
		}
		// Kolla om det fanns något kvar av indata som inte var ett token
		if (inputPos != input.length()) {
			tokens.add(new Token(TokenType.Invalid));
		}

		// Token som signalerar slut på indata
		tokens.add(new Token(TokenType.LastToken, rowCounter));

		// Debug-kod för att skriva ut token-sekvensen
		//for (Token token: tokens)
		//   System.out.println(token.getType());
	}

	// Kika på nästa token i indata, utan att gå vidare
	public Token peekToken() throws SyntaxError {
		return tokens.get(currentToken);
	}

	// Hämta nästa token i indata och gå framåt i indata
	public Token nextToken() throws SyntaxError {
		Token res = peekToken();
		++currentToken;
		return res;
	}

	public Token prevToken() throws SyntaxError{
		return tokens.get(currentToken-1);
	}

	public boolean hasMoreTokens() {
		return currentToken < tokens.size();
	}
}