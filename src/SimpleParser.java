import java.util.List;

public class SimpleParser {

    private final List<SimpleLexer.LexToken> tokens;
    private int currentIndex = 0;

    public SimpleParser(List<SimpleLexer.LexToken> tokens) {
        this.tokens = tokens;
    }

    public void parse() {
        while (!isAtEnd()) {
            parseStatement();
        }
        System.out.println("✅ Syntax Analysis: Passed.");
    }

    private void parseStatement() {
        if (match(SimpleLexer.LexType.KEYWORD, "int")) {
            parseDeclaration();
        } else if (check(SimpleLexer.LexType.IDENTIFIER)) {
            parseAssignment();
        } else if (match(SimpleLexer.LexType.KEYWORD, "if")) {
            parseIfStatement();
        } else if (match(SimpleLexer.LexType.KEYWORD, "while")) {
            parseWhileStatement();
        } else if (match(SimpleLexer.LexType.KEYWORD, "print")) {
            parsePrintStatement();
        } else {
            error("Expected a statement.");
        }
    }

    private void parseDeclaration() {
        consume(SimpleLexer.LexType.IDENTIFIER, "Expected variable name after 'int'.");
        consume(SimpleLexer.LexType.SEMICOLON, "Expected ';' after declaration.");
    }

    private void parseAssignment() {
        consume(SimpleLexer.LexType.IDENTIFIER, "Expected variable name.");
        consume(SimpleLexer.LexType.ASSIGN_OP, "Expected '=' in assignment.");
        parseExpression();
        consume(SimpleLexer.LexType.SEMICOLON, "Expected ';' after assignment.");
    }

    private void parseIfStatement() {
        consume(SimpleLexer.LexType.LEFT_PAREN, "Expected '(' after 'if'.");
        parseExpression(); // Supports comparisons like (x < 10)
        consume(SimpleLexer.LexType.RIGHT_PAREN, "Expected ')' after condition.");
        parseBlock();
        if (match(SimpleLexer.LexType.KEYWORD, "else")) {
            parseBlock();
        }
    }

    private void parseWhileStatement() {
        consume(SimpleLexer.LexType.LEFT_PAREN, "Expected '(' after 'while'.");
        parseExpression(); // Supports comparisons like (x > 0)
        consume(SimpleLexer.LexType.RIGHT_PAREN, "Expected ')' after condition.");
        parseBlock();
    }

    private void parsePrintStatement() {
        consume(SimpleLexer.LexType.LEFT_PAREN, "Expected '(' after 'print'.");
        parseExpression();
        consume(SimpleLexer.LexType.RIGHT_PAREN, "Expected ')' after expression.");
        consume(SimpleLexer.LexType.SEMICOLON, "Expected ';' after print statement.");
    }

    private void parseBlock() {
        consume(SimpleLexer.LexType.LEFT_BRACE, "Expected '{' to start block.");
        while (!check(SimpleLexer.LexType.RIGHT_BRACE) && !isAtEnd()) {
            parseStatement();
        }
        consume(SimpleLexer.LexType.RIGHT_BRACE, "Expected '}' to close block.");
    }

    private void parseExpression() {
        parseArithmetic();
        if (match(SimpleLexer.LexType.COMPARATOR)) {
            parseArithmetic();
        }
    }

    private void parseArithmetic() {
        parseTerm();
        while (match(SimpleLexer.LexType.OPERATOR, "+", "-")) {
            parseTerm();
        }
    }

    private void parseTerm() {
        parseFactor();
        while (match(SimpleLexer.LexType.OPERATOR, "*", "/")) {
            parseFactor();
        }
    }

    private void parseFactor() {
        if (match(SimpleLexer.LexType.IDENTIFIER) || match(SimpleLexer.LexType.NUMBER)) {
            return;
        } else if (match(SimpleLexer.LexType.LEFT_PAREN)) {
            parseExpression();
            consume(SimpleLexer.LexType.RIGHT_PAREN, "Expected ')' after expression.");
        } else {
            error("Expected number, variable, or expression.");
        }
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

    private void consume(SimpleLexer.LexType type, String errorMessage) {
        if (check(type)) {
            advance();
        } else {
            error(errorMessage);
        }
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

    private void error(String message) {
        System.err.println("❌ Syntax Error: " + message + " at token: " +
                (isAtEnd() ? "EOF" : peek()));
        System.exit(1);
    }
}
