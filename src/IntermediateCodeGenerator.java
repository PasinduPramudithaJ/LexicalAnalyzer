import java.util.*;

/**
 * MiniLang Intermediate Code Generator
 * ------------------------------------
 * Generates simple 3-address code from parsed tokens.
 * Assumes semantic analysis has already passed.
 */
public class IntermediateCodeGenerator {

    private final List<SimpleLexer.LexToken> tokens;
    private int currentIndex = 0;
    private final List<String> intermediateCode = new ArrayList<>();
    private int tempVarCounter = 0;

    public IntermediateCodeGenerator(List<SimpleLexer.LexToken> tokens) {
        this.tokens = tokens;
    }

    public void generate() {
        while (!isAtEnd()) {
            parseStatement();
        }
        System.out.println("\nIntermediate Code:");
        for (String line : intermediateCode) {
            System.out.println(line);
        }
    }

    private void parseStatement() {
        if (match(SimpleLexer.LexType.KEYWORD, "int")) {
            advance(); // identifier
            advance(); // semicolon
        } else if (check(SimpleLexer.LexType.IDENTIFIER)) {
            String var = advance().lexValue;
            match(SimpleLexer.LexType.ASSIGN_OP);
            String expr = parseExpression();
            match(SimpleLexer.LexType.SEMICOLON);
            intermediateCode.add(var + " = " + expr);
        } else {
            advance(); // skip unsupported
        }
    }

    private String parseExpression() {
        String left = parseTerm();
        while (match(SimpleLexer.LexType.OPERATOR, "+", "-")) {
            String op = previous().lexValue;
            String right = parseTerm();
            String temp = newTemp();
            intermediateCode.add(temp + " = " + left + " " + op + " " + right);
            left = temp;
        }
        return left;
    }

    private String parseTerm() {
        String left = parseFactor();
        while (match(SimpleLexer.LexType.OPERATOR, "*", "/")) {
            String op = previous().lexValue;
            String right = parseFactor();
            String temp = newTemp();
            intermediateCode.add(temp + " = " + left + " " + op + " " + right);
            left = temp;
        }
        return left;
    }

    private String parseFactor() {
        if (match(SimpleLexer.LexType.NUMBER) || match(SimpleLexer.LexType.IDENTIFIER)) {
            return previous().lexValue;
        } else if (match(SimpleLexer.LexType.LEFT_PAREN)) {
            String expr = parseExpression();
            match(SimpleLexer.LexType.RIGHT_PAREN);
            return expr;
        } else {
            return "0"; // fallback
        }
    }

    private String newTemp() {
        return "t" + (tempVarCounter++);
    }

    private boolean match(SimpleLexer.LexType type, String... values) {
        if (check(type)) {
            String lexeme = peek().lexValue;
            for (String v : values) {
                if (lexeme.equals(v)) {
                    advance();
                    return true;
                }
            }
        }
        return false;
    }

    private boolean match(SimpleLexer.LexType type) {
        if (check(type)) {
            advance();
            return true;
        }
        return false;
    }

    private boolean check(SimpleLexer.LexType type) {
        return !isAtEnd() && peek().lexType == type;
    }

    private SimpleLexer.LexToken peek() {
        return tokens.get(currentIndex);
    }

    private SimpleLexer.LexToken advance() {
        if (!isAtEnd()) currentIndex++;
        return previous();
    }

    private SimpleLexer.LexToken previous() {
        return tokens.get(currentIndex - 1);
    }

    private boolean isAtEnd() {
        return currentIndex >= tokens.size();
    }
} // End of IntermediateCodeGenerator
