
public class Main {
    public static void main(String[] args) {
        System.out.println("---------------------------------------------------------------------------------");
        System.out.println("This is the Lexical Analyzer for MiniLang.");
        System.out.println("Please ensure you have the input.minilang file in the correct directory.");
        System.out.println("The lexical analysis will be performed on the contents of that file.");
        System.out.println("To run the analysis, please execute the SimpleLexer class.");
        System.out.println("You can find the results in the console output.");
        System.out.println("Thank you for using the MiniLang Lexical Analyzer!");
        System.out.println("---------------------------------------------------------------------------------");
        SimpleLexer simpleLexer=new SimpleLexer();
        simpleLexer.toString();

    }
}