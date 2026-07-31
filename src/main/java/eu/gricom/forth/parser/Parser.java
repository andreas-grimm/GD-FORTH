package eu.gricom.forth.parser;

import eu.gricom.forth.error.SyntaxErrorException;
import eu.gricom.forth.statements.Statement;

import java.util.List;

/**
 * Parser.java
 * <p>
 * Description: The Parser interface defines the contract for converting a sequence of tokens into an Abstract Syntax
 * Tree (AST). Implementations analyse the token stream, validate syntax according to FORTH grammar rules, and produce
 * a list of executable Statement objects.
 * <p>
 * (c) = 2020,.., by Andreas Grimm, The Netherlands / Norway
 */
public interface Parser {

    /**
     * Default constructor.
     * The constructor receives the tokenized program and parses it.
     * @return list of Java objects instantiated based on the token list.
     * @throws SyntaxErrorException for any found incorrect code
     */
    List<Statement> parse() throws SyntaxErrorException;

}
