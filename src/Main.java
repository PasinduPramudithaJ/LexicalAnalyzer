import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("---------------------------------------------------------------------------------");
        System.out.println("This is the MiniLang Analyzer.");
        System.out.println("Ensure the 'input.minilang' file is present in the correct directory.");
        System.out.println("The analyzer will perform: Lexical ➤ Syntax ➤ Semantic ➤ Intermediate Code Generation.");
        System.out.println("---------------------------------------------------------------------------------");

        String fileName = "input.minilang";
        StringBuilder codeBuffer = new StringBuilder();

        try (BufferedReader fileReader = new BufferedReader(new FileReader(fileName))) {
            String currentLine;
            while ((currentLine = fileReader.readLine()) != null) {
                codeBuffer.append(currentLine).append("\n");
            }

            // Lexical Analysis
            System.out.println("\n📘 Performing Lexical Analysis...");
            List<SimpleLexer.LexToken> tokens = SimpleLexer.lexAnalyze(codeBuffer.toString());
            for (SimpleLexer.LexToken token : tokens) {
                System.out.println(token);
            }

            // Syntax Analysis
            System.out.println("\n📗 Performing Syntax Analysis...");
            SimpleParser parser = new SimpleParser(tokens);
            parser.parse();

            // Semantic Analysis
            System.out.println("\n📙 Performing Semantic Analysis...");
            SemanticAnalyzer analyzer = new SemanticAnalyzer();
            analyzer.analyze(tokens);

            // Intermediate Code Generation
            System.out.println("\n📒 Performing Intermediate Code Generation...");
            IntermediateCodeGenerator icg = new IntermediateCodeGenerator(tokens);
            icg.generate();

            System.out.println("\n✅ All analyses and code generation completed successfully.");
        } catch (IOException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }
}
