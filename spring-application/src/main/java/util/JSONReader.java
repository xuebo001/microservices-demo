package util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Convert JSON String to Object.
 * 
 * @author Cai Zhibin
 *
 */
public class JSONReader {

    private static final Object OBJECT_END = new Object();
    private static final Object ARRAY_END = new Object();
    private static final Object COLON = new Object();
    private static final Object COMMA = new Object();
    public static final int FIRST = 0;
    public static final int CURRENT = 1;
    public static final int NEXT = 2;

    private static Map<Character, Character> escapes = new HashMap<Character, Character>();
    static {
        escapes.put(new Character('"'), new Character('"'));
        escapes.put(new Character('\\'), new Character('\\'));
        escapes.put(new Character('/'), new Character('/'));
        escapes.put(new Character('b'), new Character('\b'));
        escapes.put(new Character('f'), new Character('\f'));
        escapes.put(new Character('n'), new Character('\n'));
        escapes.put(new Character('r'), new Character('\r'));
        escapes.put(new Character('t'), new Character('\t'));
    }

    private CharacterIterator it;
    private char c;
    private Object token;
    private StringBuffer buf = new StringBuffer();

    private char next() {
        c = it.next();
        return c;
    }

    private void skipWhiteSpace() {
        while (Character.isWhitespace(c)) {
            next();
        }
    }

    public Object read(CharacterIterator ci, int start) throws Exception {
        it = ci;
        switch (start) {
        case FIRST:
            c = it.first();
            break;
        case CURRENT:
            c = it.current();
            break;
        case NEXT:
            c = it.next();
            break;
        }
        return read();
    }

    public Object read(CharacterIterator it) throws Exception {
        return read(it, NEXT);
    }

    public Object read(String string) throws Exception {
        return read(new StringCharacterIterator(string), FIRST);
    }

    private Object read() throws Exception {
        skipWhiteSpace();
        char ch = c;
        next();
        switch (ch) {
            case '"': token = string(); break;
            case '[': token = array(); break;
            case ']': token = ARRAY_END; break;
            case ',': token = COMMA; break;
            case '{': token = object(); break;
            case '}': token = OBJECT_END; break;
            case ':': token = COLON; break;
            case 't':
                next(); next(); next(); // assumed r-u-e
                token = Boolean.TRUE;
                break;
            case'f':
                next(); next(); next(); next(); // assumed a-l-s-e
                token = Boolean.FALSE;
                break;
            case 'n':
                next(); next(); next(); // assumed u-l-l
                token = null;
                break;
            case CharacterIterator.DONE:
            	token = CharacterIterator.DONE;
            	break;
            default:
                c = it.previous();
                if (Character.isDigit(c) || c == '-') {
                    token = number();
                } else {
                	throw new Exception("error json field");
                }
        }
        return token;
    }
    
    private Object object() throws Exception {
        Map<Object, Object> ret = new HashMap<Object, Object>();
        Object key = read();
        while (token != OBJECT_END && c != CharacterIterator.DONE) {
            read(); // should be a colon
            if (token != OBJECT_END) {
                ret.put(key, read());
                if (read() == COMMA) {
                    key = read();
                }
            }
        }

        return ret;
    }

    private Object array() throws Exception {
        List<Object> ret = new ArrayList<Object>();
        Object value = read();
        while (token != ARRAY_END  && c != CharacterIterator.DONE) {
            ret.add(value);
            if (read() == COMMA) {
                value = read();
            }
        }
        return ret;
    }

    private Object number() {
        int length = 0;
        boolean isFloatingPoint = false;
        buf.setLength(0);
        
        if (c == '-') {
            add();
        }
        length += addDigits();
        if (c == '.') {
            add();
            length += addDigits();
            isFloatingPoint = true;
        }
        if (c == 'e' || c == 'E') {
            add();
            if (c == '+' || c == '-') {
                add();
            }
            addDigits();
            isFloatingPoint = true;
        }
 
        String s = buf.toString();
        return isFloatingPoint 
            ? (length < 17) ? (Object)Double.valueOf(s) : new BigDecimal(s)
            : (length < 19) ? (Object)Long.valueOf(s) : new BigInteger(s);
    }
 
    private int addDigits() {
        int ret;
        for (ret = 0; Character.isDigit(c); ++ret) {
            add();
        }
        return ret;
    }

    private Object string() {
        buf.setLength(0);
        while (c != '"' && c != CharacterIterator.DONE) {
            if (c == '\\') {
                next();
                if (c == 'u') {
                    add(unicode());
                } else {
                    Object value = escapes.get(new Character(c));
                    if (value != null) {
                        add(((Character) value).charValue());
                    }
                }
            } else {
                add();
            }
        }
        next();

        return buf.toString();
    }

    private void add(char cc) {
        buf.append(cc);
        next();
    }

    private void add() {
        add(c);
    }

    private char unicode() {
        int value = 0;
        for (int i = 0; i < 4; ++i) {
            switch (next()) {
            case '0': case '1': case '2': case '3': case '4': 
            case '5': case '6': case '7': case '8': case '9':
                value = (value << 4) + c - '0';
                break;
            case 'a': case 'b': case 'c': case 'd': case 'e': case 'f':
                value = (value << 4) + (c - 'a') + 10;
                break;
            case 'A': case 'B': case 'C': case 'D': case 'E': case 'F':
                value = (value << 4) + (c - 'A') + 10;
                break;
            }
        }
        return (char) value;
    }
}

