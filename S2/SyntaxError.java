// Klass för att representera syntaxfel.  I praktiken vill man nog
// även ha med ett litet felmeddelande om *vad* som var fel, samt på
// vilken rad/position felet uppstod
// Utgått ifrån kod av: Per Austrin
@SuppressWarnings("all")
public class SyntaxError extends Exception {
    public SyntaxError(int row) {
        super("Syntaxfel på rad " + row);
    }
}
