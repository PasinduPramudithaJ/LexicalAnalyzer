import java.io.*;
import java.util.*;
import java.util.regex.*;

/**
 * MiniLang Lexical and Syntax Analyzer
 * -------------------------------------
 * Author: PS/2020/010
 * Course: COSC 44293 – Theory of Compilers – Final Assignment Phase 01
 */
public class SimpleLexer {

    /**
     * Enumeration of all lexical token types supported in MiniLang.
     */
    enum LexType {
        KEYWORD,        // Reserved words: int, if, else, while, print
        IDENTIFIER,     // Variable names
        NUMBER,         // Integer numbers
        ASSIGN_OP,      // Assignment operator: =
        SEMICOLON,      // ;
        OPERATOR,       // Arithmetic operators: +, -, *, /
        COMPARATOR,     // Relational operators: <, >
        LEFT_PAREN,     // (
        RIGHT_PAREN,    // )
        LEFT_BRACE,     // {
        RIGHT_BRACE     // }
    }

    /**
     * Class representing a single lexical token.
     */
    static class LexToken {
        LexType lexType;
        String lexValue;

        LexToken(LexType lexType, String lexValue) {
            this.lexType = lexType;
            this.lexValue = lexValue;
        }

        @Override
        public String toString() {
            return "(" + lexType + ", " + lexValue + ")";
        }
    }

    // Regular expressions for each token type
    private static final Map<LexType, String> PATTERNS = new LinkedHashMap<>();

    // Reserved keywords in MiniLang
    private static final Set<String> RESERVED_WORDS = Set.of("int", "if", "else", "while", "print");

    // Static block to initialize patterns
    static {
        PATTERNS.put(LexType.KEYWORD, "\\b(int|if|else|while|print)\\b");
        PATTERNS.put(LexType.IDENTIFIER, "\\b[a-zA-Z_][a-zA-Z0-9_]*\\b");
        PATTERNS.put(LexType.NUMBER, "\\b\\d+\\b");
        PATTERNS.put(LexType.ASSIGN_OP, "=");
        PATTERNS.put(LexType.SEMICOLON, ";");
        PATTERNS.put(LexType.OPERATOR, "[+\\-*/]");
        PATTERNS.put(LexType.COMPARATOR, "[<>]");
        PATTERNS.put(LexType.LEFT_PAREN, "\\(");
        PATTERNS.put(LexType.RIGHT_PAREN, "\\)");
        PATTERNS.put(LexType.LEFT_BRACE, "\\{");
        PATTERNS.put(LexType.RIGHT_BRACE, "\\}");
    }

    public static List<LexToken> lexAnalyze(String sourceCode) {
        List<LexToken> lexTokens = new ArrayList<>();
        String combinedPattern = PATTERNS.values().stream()
                .reduce((p1, p2) -> p1 + "|" + p2)
                .orElseThrow(() -> new RuntimeException("No patterns defined"));
        Pattern pattern = Pattern.compile(combinedPattern);
        Matcher matcher = pattern.matcher(sourceCode);

        while (matcher.find()) {
            String matchedText = matcher.group();
            LexType currentType = null;

            for (Map.Entry<LexType, String> entry : PATTERNS.entrySet()) {
                if (matchedText.matches(entry.getValue())) {
                    currentType = entry.getKey();
                    break;
                }
            }

            if (currentType == LexType.IDENTIFIER && RESERVED_WORDS.contains(matchedText)) {
                currentType = LexType.KEYWORD;
            }

            if (currentType != null) {
                lexTokens.add(new LexToken(currentType, matchedText));
            }
        }

        return lexTokens;
    }

    public static void main(String[] args) {
        String fileName = "input.minilang";
        StringBuilder codeBuffer = new StringBuilder();

        try (BufferedReader fileReader = new BufferedReader(new FileReader(fileName))) {
            String currentLine;
            while ((currentLine = fileReader.readLine()) != null) {
                codeBuffer.append(currentLine).append("\n");
            }

            // Lexical Analysis
            List<LexToken> results = lexAnalyze(codeBuffer.toString());
            System.out.println("Lexical Tokens:");
            for (LexToken token : results) {
                System.out.println(token);
            }

            // Syntax Analysis
            System.out.println("\nPerforming Syntax Analysis...");
            SimpleParser parser = new SimpleParser(results);
            parser.parse();

        } catch (FileNotFoundException e) {
            System.err.println("❌ Error: File not found - " + fileName);
        } catch (IOException e) {
            System.err.println("❌ Error reading the file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
