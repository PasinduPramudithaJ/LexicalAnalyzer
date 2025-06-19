import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class SemanticAnalyzer {

    private final Set<String> declaredVariables = new HashSet<>();

    public void analyze(List<SimpleLexer.LexToken> tokens) {
        for (int i = 0; i < tokens.size(); i++) {
            SimpleLexer.LexToken token = tokens.get(i);

            if (token.lexType == SimpleLexer.LexType.KEYWORD && token.lexValue.equals("int")) {
                if (i + 1 < tokens.size() && tokens.get(i + 1).lexType == SimpleLexer.LexType.IDENTIFIER) {
                    String varName = tokens.get(i + 1).lexValue;
                    if (declaredVariables.contains(varName)) {
                        System.err.println("❌ Semantic Error: Variable '" + varName + "' already declared.");
                        System.exit(1);
                    }
                    declaredVariables.add(varName);
                }
            } else if (token.lexType == SimpleLexer.LexType.IDENTIFIER) {
                if (!declaredVariables.contains(token.lexValue)) {
                    System.err.println("❌ Semantic Error: Variable '" + token.lexValue + "' used before declaration.");
                    System.exit(1);
                }
            }
        }
        System.out.println("✅ Semantic Analysis: Passed.");
    }
}
