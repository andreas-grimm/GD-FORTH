package eu.gricom.forth.memoryManager;

import eu.gricom.forth.error.SyntaxErrorException;
import eu.gricom.forth.helper.Logger;
import eu.gricom.forth.tokenizer.ForthLexer;
import eu.gricom.forth.tokenizer.Token;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Directory {
    private final Logger _oLogger = new Logger(this.getClass().getName());
    private final static Map<String, String> _aoDefinedWords = new HashMap<>();
    private final static Map<String, List<Token>> _aoWordByToken = new HashMap<>(); // Key: Token, Basic Source Line: Value

    public Directory() {
    }

    public void storeWord(String strWord, String strFunction) throws SyntaxErrorException {
        _aoDefinedWords.put(strWord, strFunction);
        _aoWordByToken.put(strWord, tokenizeNewWord(strFunction));
    }

    public String getWord(String strWord) {
        if (_aoDefinedWords.containsKey(strWord)) {
            return _aoDefinedWords.get(strWord);
        } else {
            return null;
        }
    }

    public List<Token> getToken(String strWord) {
        if (_aoDefinedWords.containsKey(strWord)) {
            return _aoWordByToken.get(strWord);
        } else {
            return null;
        }
    }

    public List<Token> tokenizeNewWord (String strNewWordDefinition) throws SyntaxErrorException {
        ForthLexer oTokenizer = new ForthLexer();

        return oTokenizer.tokenize(strNewWordDefinition);
    }

    public String[] listWords() {
        String[] astrWords = _aoDefinedWords.keySet().toArray(new String[0]);
        Arrays.sort(astrWords);

        String[] astrResult = new String[astrWords.length];
        for (int i = 0; i < astrWords.length; i++) {
            String strWord = astrWords[i];
            String strDefinition = _aoDefinedWords.get(strWord);
            astrResult[i] = strWord + " -> " + strDefinition;
        }

        return astrResult;
    }
}
