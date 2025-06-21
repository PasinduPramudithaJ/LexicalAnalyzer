# 🧠 MiniLang Analyzer

A compiler front-end for a simple C-like language, **MiniLang**, implementing:

- ✅ Lexical Analysis  
- ✅ Syntax Analysis  
- ✅ Semantic Analysis  
- ✅ Intermediate Code Generation

---

## 📚 Description

MiniLang Analyzer is a Java-based compiler component project that simulates the early phases of a compiler. It parses a simplified language with features like:

- Variable declaration (`int x;`)
- Arithmetic operations (`x = a + b * 2;`)
- Conditional statements (`if (...) { ... } else { ... }`)
- Print statements (`print(x);`)

This project is designed for learning compiler design principles, especially for **COSC 44293 – Theory of Compilers**.

---

## 📁 File Structure

```
MiniLangAnalyzer/
├── Main.java
├── SimpleLexer.java
├── SimpleParser.java
├── SemanticAnalyzer.java
├── IntermediateCodeGenerator.java
├── input.minilang
└── README.md
```

---

## 🧪 Sample MiniLang Code

```c
int n;
n = 7;
if (n < 10) {
    n = n * 2 + 1;
} else {
    n = n - 3;
}
print(n);
```

---

## 🔧 How to Compile and Run

### 1. Clone or Download the Project

```bash
git clone https://github.com/your-username/MiniLangAnalyzer.git
cd MiniLangAnalyzer
```

### 2. Ensure Java is Installed

Check with:

```bash
java -version
javac -version
```

Install JDK if not installed.

### 3. Compile All Java Files

```bash
javac Main.java SimpleLexer.java SimpleParser.java SemanticAnalyzer.java IntermediateCodeGenerator.java
```

### 4. Add Code to `input.minilang`

Example:

```c
int x;
x = 5 + 2 * 3;
print(x);
```

### 5. Run the Main Program

```bash
java Main
```

---

## ✅ Example Output

```bash
📘 Performing Lexical Analysis...
(KEYWORD, int)
(IDENTIFIER, x)
...
📗 Performing Syntax Analysis...
✅ Syntax Analysis: Passed.

📙 Performing Semantic Analysis...
✅ Semantic Analysis: Passed.

📒 Performing Intermediate Code Generation...
t0 = 2 * 3
t1 = 5 + t0
x = t1

✅ All analyses and code generation completed successfully.
```

---

## ⚠️ Common Issues

| Issue                             | Fix                                                       |
|----------------------------------|------------------------------------------------------------|
| `FileNotFoundException`          | Ensure `input.minilang` exists in project directory        |
| `javac` or `java` not recognized | Install Java SDK and set environment variables             |
| Semantic error on variable usage | Declare variable before using it                           |
| Missing semicolon or brace       | Review code formatting, use proper syntax                  |

---

## 🧠 Educational Use

This analyzer is part of:

**Course:** COSC 44293 – Theory of Compilers  
**University:** University of Kelaniya  
**Student ID:** PS/2020/010  

---

## 📄 License

This project is intended for academic and educational use. No commercial redistribution allowed.

---

## 🙌 Acknowledgements

- University of Kelaniya
- Theory of Compilers Module
- Java Compiler Design examples
