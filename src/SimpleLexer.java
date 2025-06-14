import java.io.*;
import java.util.*;
import java.util.regex.*;

/**
 * MiniLang Lexical Analyzer
 * --------------------------
 * This Java program implements the lexical analysis phase for a simple programming language
 * called MiniLang. The MiniLang syntax is C-like and includes features like variable declarations,
 * conditionals, arithmetic operations, and print statements.
 *
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

    /**
     * Performs lexical analysis on the given source code and returns a list of tokens.
     *
     * @param sourceCode Raw source code from the MiniLang file
     * @return List of lexical tokens
     */
    public static List<LexToken> lexAnalyze(String sourceCode) {
        List<LexToken> lexTokens = new ArrayList<>();

        // Combine all regex patterns into one big pattern
        String combinedPattern = PATTERNS.values().stream()
                .reduce((p1, p2) -> p1 + "|" + p2)
                .orElseThrow(() -> new RuntimeException("No patterns defined"));

        Pattern pattern = Pattern.compile(combinedPattern);
        Matcher matcher = pattern.matcher(sourceCode);

        // Match tokens one by one
        while (matcher.find()) {
            String matchedText = matcher.group();
            LexType currentType = null;

            // Determine which pattern matched the text
            for (Map.Entry<LexType, String> entry : PATTERNS.entrySet()) {
                if (matchedText.matches(entry.getValue())) {
                    currentType = entry.getKey();
                    break;
                }
            }

            // Check if it's a reserved keyword
            if (currentType == LexType.IDENTIFIER && RESERVED_WORDS.contains(matchedText)) {
                currentType = LexType.KEYWORD;
            }

            // Add token to list
            if (currentType != null) {
                lexTokens.add(new LexToken(currentType, matchedText));
            }
        }

        return lexTokens;
    }

    /**
     * Main method to read source file and invoke lexical analyzer.
     * Handles file-not-found and I/O exceptions gracefully.
     */
    public static void main(String[] args) {
        String fileName = "input.minilang";
        StringBuilder codeBuffer = new StringBuilder();

        // Read input.minilang file
        try (BufferedReader fileReader = new BufferedReader(new FileReader(fileName))) {
            String currentLine;
            while ((currentLine = fileReader.readLine()) != null) {
                codeBuffer.append(currentLine).append("\n");
            }

            // Perform lexical analysis
            List<LexToken> results = lexAnalyze(codeBuffer.toString());

            // Print results
            System.out.println("Lexical Tokens:");
            for (LexToken token : results) {
                System.out.println(token);
            }

        } catch (FileNotFoundException e) {
            System.err.println("❌ Error: File not found - " + fileName);
            System.err.println("Make sure the file is placed in the correct working directory.");
        } catch (IOException e) {
            System.err.println("❌ Error reading the file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
