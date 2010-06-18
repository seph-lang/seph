/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.parser;

import java.io.Reader;
import java.io.IOException;

import java.util.List;
import java.util.ListIterator;
import java.util.ArrayList;

import seph.lang.Runtime;
import seph.lang.ControlFlow;
import seph.lang.ast.Message;
import seph.lang.ast.NamedMessage;
import seph.lang.ast.LiteralMessage;

import gnu.math.IntNum;
import gnu.math.DFloNum;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class Parser {
    private final Runtime runtime;
    private final Reader reader;
    private final String sourcename;

    public Parser(Runtime runtime, Reader reader, String sourcename) {
        this.runtime = runtime;
        this.reader = reader;
        this.sourcename = sourcename;
    }

    public List<Message> parseFully() throws IOException, ControlFlow {
        List<Message> all = parseExpressionChain();

        if(all.isEmpty()) {
            all.add(new NamedMessage(".", null, null, sourcename, 0, 0));
        }

        return all;
    }

    private Message parseExpressions() throws IOException, ControlFlow {
        List<Message> result = new ArrayList<Message>();
        Message c;
        while((c = parseExpression()) != null) {
            result.add(c);
        }

        Message ret = null;
        for(ListIterator<Message> i = result.listIterator(result.size()); i.hasPrevious();) {
            ret = i.previous().withNext(ret);
		}

        if(ret != null) {
            while(ret.name().equals(".") && ret.next() != null) {
                ret = ret.next();
            }
        }

        return ret;
    }

    private List<Message> parseExpressionChain() throws IOException, ControlFlow {
        ArrayList<Message> chain = new ArrayList<Message>();

        Message curr = parseExpressions();
        while(curr != null) {
            chain.add(curr);
            readWhiteSpace();
            int rr = peek();
            if(rr == ',') {
                read();
                curr = parseExpressions();
                if(curr == null) {
                    fail("Expected expression following comma");
                }
            } else {
                if(curr != null && curr.name().equals(".") && curr.next() == null) {
                    chain.remove(chain.size()-1);
                }
                curr = null;
            }
        }

        return chain;
    }

    private int lineNumber = 1;
    private int currentCharacter = -1;
    private boolean skipLF = false;

    private int saved2 = -2;
    private int saved = -2;

    private int read() throws IOException {
        if(saved > -2) {
            int x = saved;
            saved = saved2;
            saved2 = -2;

            if(skipLF) {
                skipLF = false;
                if(x == '\n') {
                    return x;
                }
            }

            currentCharacter++;

            switch(x) {
            case '\r':
                skipLF = true;
            case '\n':		/* Fall through */
                lineNumber++;
                currentCharacter = 0;
            }

            return x;
        }

        int xx = reader.read();

        if(skipLF) {
            skipLF = false;
            if(xx == '\n') {
                return xx;
            }
        }

        currentCharacter++;

        switch(xx) {
        case '\r':
            skipLF = true;
        case '\n':		/* Fall through */
            lineNumber++;
            currentCharacter = 0;
        }

        return xx;
    }

    private int peek() throws IOException {
        if(saved == -2) {
            if(saved2 != -2) {
                saved = saved2;
                saved2 = -2;
            } else {
                saved = reader.read();
            }
        }
        return saved;
    }

    private int peek2() throws IOException {
        if(saved == -2) {
            saved = reader.read();
        }
        if(saved2 == -2) {
            saved2 = reader.read();
        }
        return saved2;
    }

    private Message parseExpression() throws IOException, ControlFlow {
        int rr;
        while(true) {
            rr = peek();
            switch(rr) {
            case -1:
                read();
                return null;
            case ',':
            case ')':
            case ']':
            case '}':
                return null;
            case '(':
                read();
                return parseEmptyMessageSend();
            case '[':
                read();
                return parseSquareMessageSend();
            case '{':
                read();
                return parseCurlyMessageSend();
            case '#':
                read();
                switch(peek()) {
                case '{':
                    return parseSetMessageSend();
                case '/':
                    return parseRegexpLiteral('/');
                case 'r':
                    return parseRegexpLiteral('r');
                case '!':
                    parseComment();
                    break;
                case '[':
                    return parseText('[');
                default:
                    return parseOperatorChars('#');
                }
                break;
            case '"':
                read();
                return parseText('"');
            case '.':
                read();
                if((rr = peek()) == '.') {
                    return parseRange();
                } else {
                    return parseTerminator('.');
                }
            case ';':
                read();
                parseComment();
                break;
            case ' ':
            case '\u0009':
            case '\u000b':
            case '\u000c':
                read();
                readWhiteSpace();
                break;
            case '\r':
            case '\n':
                read();
                return parseTerminator(rr);
            case '\\':
                read();
                if((rr = peek()) == '\n') {
                    read();
                    break;
                } else {
                    fail("Expected newline after free-floating escape character");
                }
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                read();
                return parseNumber(rr);
            case '+':
            case '-':
                if(isDigit(peek2())) {
                    read();
                    return parseNumber(rr);
                }
            case '*':
            case '%':
            case '<':
            case '>':
            case '!':
            case '?':
            case '~':
            case '&':
            case '|':
            case '^':
            case '$':
            case '=':
            case '@':
            case '\'':
            case '`':
            case '/':
                read();
                return parseOperatorChars(rr);
            case ':':
                read();
                if(isLetter(rr = peek()) || isIDDigit(rr)) {
                    return parseRegularMessageSend(':');
                } else {
                    return parseOperatorChars(':');
                }
            default:
                read();
                return parseRegularMessageSend(rr);
            }
        }
    }

    private void readWhiteSpace() throws IOException {
        int rr;
        while((rr = peek()) == ' ' ||
              rr == '\u0009' ||
              rr == '\u000b' ||
              rr == '\u000c') {
            read();
        }
    }

    private Message parseRegularMessageSend(int indicator) throws IOException, ControlFlow {
        int l = lineNumber; int cc = currentCharacter-1;

        StringBuilder sb = new StringBuilder();
        sb.append((char)indicator);
        int rr = -1;
        while(isLetter(rr = peek()) || isIDDigit(rr) || rr == ':' || rr == '!' || rr == '?' || rr == '$') {
            read();
            sb.append((char)rr);
        }

        List<Message> args = null;
        if(rr == '(') {
            read();
            args = parseExpressionChain();
            parseCharacter(')');
        }
        return new NamedMessage(sb.toString(), args, null, sourcename, l, cc);
    }

    private void parseComment() throws IOException {
        int rr;
        while((rr = peek()) != '\n' && rr != '\r' && rr != -1) {
            read();
        }
    }

    private final static String[] RANGES = {
        "",
        ".",
        "..",
        "...",
        "....",
        ".....",
        "......",
        ".......",
        "........",
        ".........",
        "..........",
        "...........",
        "............"
    };


    private Message parseRange() throws IOException {
        int l = lineNumber; int cc = currentCharacter-1;

        int count = 2;
        read();
        int rr;
        while((rr = peek()) == '.') {
            count++;
            read();
        }
        String result = null;
        if(count < 13) {
            result = RANGES[count];
        } else {
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i<count; i++) {
                sb.append('.');
            }
            result = sb.toString();
        }

        return new NamedMessage(result, null, null, sourcename, l, cc);
    }

    private Message parseTerminator(int indicator) throws IOException {
        int l = lineNumber; int cc = currentCharacter-1;

        int rr;
        int rr2;
        if(indicator == '\r') {
            rr = peek();
            if(rr == '\n') {
                read();
            }
        }

        while(true) {
            rr = peek();
            rr2 = peek2();
            if((rr == '.' && rr2 != '.') ||
               (rr == '\n')) {
                read();
            } else if(rr == '\r' && rr2 == '\n') {
                read(); read();
            } else {
                break;
            }
        }

        return new NamedMessage(".", null, null, sourcename, l, cc);
    }

    private Message parseRegexpLiteral(int indicator) throws IOException, ControlFlow {
        int l = lineNumber; int cc = currentCharacter-1;

        StringBuilder sb = new StringBuilder();
        boolean slash = indicator == '/';

        read();

        if(!slash) {
            parseCharacter('[');
        }

        String name = null;
        List<Message> args = new ArrayList<Message>();

        int rr;
        while(true) {
            switch(rr = peek()) {
            case -1:
                fail("Expected end of regular expression, found EOF");
                break;
            case '/':
                read();
                if(slash) {
                    String pattern = sb.toString();

                    sb = new StringBuilder();
                    while(true) {
                        switch(rr = peek()) {
                        case 'x':
                        case 'i':
                        case 'u':
                        case 'm':
                        case 's':
                            read();
                            sb.append((char)rr);
                            break;
                        default:
                            if(name == null) {
                                return new LiteralMessage(runtime.newRegexp(pattern, sb.toString()), null, sourcename, l, cc);
                            }
                            if(pattern.length() > 0) {
                                args.add(new LiteralMessage(runtime.newUnescapedText(pattern), null, sourcename, l, cc));
                            }
                            args.add(new LiteralMessage(runtime.newText(sb.toString()), null, sourcename, l, cc));
                            return new NamedMessage(name, args, null, sourcename, l, cc);
                        }
                    }
                } else {
                    sb.append((char)rr);
                }
                break;
            case ']':
                read();
                if(!slash) {
                    String pattern = sb.toString();

                    sb = new StringBuilder();
                    while(true) {
                        switch(rr = peek()) {
                        case 'x':
                        case 'i':
                        case 'u':
                        case 'm':
                        case 's':
                            read();
                            sb.append((char)rr);
                            break;
                        default:
                            if(name == null) {
                                return new LiteralMessage(runtime.newRegexp(pattern, sb.toString()), null, sourcename, l, cc);
                            }
                            if(pattern.length() > 0) {
                                args.add(new LiteralMessage(runtime.newUnescapedText(pattern), null, sourcename, l, cc));
                            }
                            args.add(new LiteralMessage(runtime.newText(sb.toString()), null, sourcename, l, cc));
                            return new NamedMessage(name, args, null, sourcename, l, cc);
                        }
                    }
                } else {
                    sb.append((char)rr);
                }
                break;
            case '#':
                read();
                if((rr = peek()) == '{') {
                    read();
                    args.add(new LiteralMessage(runtime.newUnescapedText(sb.toString()), null, sourcename, l, cc));
                    sb = new StringBuilder();
                    name = "internal:compositeRegexp";
                    args.add(parseExpressions());
                    readWhiteSpace();
                    parseCharacter('}');
                } else {
                    sb.append((char)'#');
                }
                break;
            case '\\':
                read();
                parseRegexpEscape(sb);
                break;
            default:
                read();
                sb.append((char)rr);
                break;
            }
        }
    }

    private Message parseText(int indicator) throws IOException, ControlFlow {
        int l = lineNumber; int cc = currentCharacter-1;

        StringBuilder sb = new StringBuilder();
        boolean dquote = indicator == '"';

        if(!dquote) {
            read();
        }

        int rr;

        String name = null;
        List<Message> args = new ArrayList<Message>();

        while(true) {
            switch(rr = peek()) {
            case -1:
                fail("Expected end of text, found EOF");
                break;
            case '"':
                read();
                if(dquote) {
                    if(name == null) {
                        return new LiteralMessage(runtime.newText(sb.toString()), null, sourcename, l, cc);
                    }
                    if(sb.length() > 0) {
                        args.add(new LiteralMessage(runtime.newText(sb.toString()), null, sourcename, l, cc));
                    }
                    return new NamedMessage(name, args, null, sourcename, l, cc);
                } else {
                    sb.append((char)rr);
                }
                break;
            case ']':
                read();
                if(!dquote) {
                    if(name == null) {
                        return new LiteralMessage(runtime.newText(sb.toString()), null, sourcename, l, cc);
                    }
                    if(sb.length() > 0) {
                        args.add(new LiteralMessage(runtime.newText(sb.toString()), null, sourcename, l, cc));
                    }
                    return new NamedMessage(name, args, null, sourcename, l, cc);
                } else {
                    sb.append((char)rr);
                }
                break;
            case '#':
                read();
                if((rr = peek()) == '{') {
                    read();
                    args.add(new LiteralMessage(runtime.newText(sb.toString()), null, sourcename, l, cc));
                    sb = new StringBuilder();
                    name = "internal:concatenateText";
                    args.add(parseExpressions());
                    readWhiteSpace();
                    parseCharacter('}');
                } else {
                    sb.append((char)'#');
                }
                break;
            case '\\':
                read();
                parseDoubleQuoteEscape(sb);
                break;
            default:
                read();
                sb.append((char)rr);
                break;
            }
        }
    }

    private void parseRegexpEscape(StringBuilder sb) throws IOException, ControlFlow {
        sb.append('\\');
        int rr = peek();
        switch(rr) {
        case 'u':
            read();
            sb.append((char)rr);
            for(int i = 0; i < 4; i++) {
                rr = peek();
                if((rr >= '0' && rr <= '9') ||
                   (rr >= 'a' && rr <= 'f') ||
                   (rr >= 'A' && rr <= 'F')) {
                    read();
                    sb.append((char)rr);
                } else {
                    fail("Expected four hexadecimal characters in unicode escape - got: " + charDesc(rr));
                }
            }
            break;
        case '0':
        case '1':
        case '2':
        case '3':
        case '4':
        case '5':
        case '6':
        case '7':
            read();
            sb.append((char)rr);
            if(rr <= '3') {
                rr = peek();
                if(rr >= '0' && rr <= '7') {
                    read();
                    sb.append((char)rr);
                    rr = peek();
                    if(rr >= '0' && rr <= '7') {
                        read();
                        sb.append((char)rr);
                    }
                }
            } else {
                rr = peek();
                if(rr >= '0' && rr <= '7') {
                    read();
                    sb.append((char)rr);
                }
            }
            break;
        case 't':
        case 'n':
        case 'f':
        case 'r':
        case '/':
        case '\\':
        case '\n':
        case '#':
        case 'A':
        case 'd':
        case 'D':
        case 's':
        case 'S':
        case 'w':
        case 'W':
        case 'b':
        case 'B':
        case 'z':
        case 'Z':
        case '<':
        case '>':
        case 'G':
        case 'p':
        case 'P':
        case '{':
        case '}':
        case '[':
        case ']':
        case '*':
        case '(':
        case ')':
        case '$':
        case '^':
        case '+':
        case '?':
        case '.':
        case '|':
            read();
            sb.append((char)rr);
            break;
        case '\r':
            read();
            sb.append((char)rr);
            if((rr = peek()) == '\n') {
                read();
                sb.append((char)rr);
            }
            break;
        default:
            fail("Undefined regular expression escape character: " + charDesc(rr));
            break;
        }
    }

    private void parseDoubleQuoteEscape(StringBuilder sb) throws IOException, ControlFlow {
        sb.append('\\');
        int rr = peek();
        switch(rr) {
        case 'u':
            read();
            sb.append((char)rr);
            for(int i = 0; i < 4; i++) {
                rr = peek();
                if((rr >= '0' && rr <= '9') ||
                   (rr >= 'a' && rr <= 'f') ||
                   (rr >= 'A' && rr <= 'F')) {
                    read();
                    sb.append((char)rr);
                } else {
                    fail("Expected four hexadecimal characters in unicode escape - got: " + charDesc(rr));
                }
            }
            break;
        case '0':
        case '1':
        case '2':
        case '3':
        case '4':
        case '5':
        case '6':
        case '7':
            read();
            sb.append((char)rr);
            if(rr <= '3') {
                rr = peek();
                if(rr >= '0' && rr <= '7') {
                    read();
                    sb.append((char)rr);
                    rr = peek();
                    if(rr >= '0' && rr <= '7') {
                        read();
                        sb.append((char)rr);
                    }
                }
            } else {
                rr = peek();
                if(rr >= '0' && rr <= '7') {
                    read();
                    sb.append((char)rr);
                }
            }
            break;
        case 'b':
        case 't':
        case 'n':
        case 'f':
        case 'r':
        case '"':
        case ']':
        case '\\':
        case '\n':
        case '#':
        case 'e':
            read();
            sb.append((char)rr);
            break;
        case '\r':
            read();
            sb.append((char)rr);
            if((rr = peek()) == '\n') {
                read();
                sb.append((char)rr);
            }
            break;
        default:
            fail("Undefined text escape character: " + charDesc(rr));
            break;
        }
    }

    private Message parseOperatorChars(int indicator) throws IOException, ControlFlow {
        int l = lineNumber; int cc = currentCharacter-1;

        StringBuilder sb = new StringBuilder();
        sb.append((char)indicator);
        int rr;
        while(true) {
            rr = peek();
            switch(rr) {
            case '+':
            case '-':
            case '*':
            case '%':
            case '<':
            case '>':
            case '!':
            case '?':
            case '~':
            case '&':
            case '|':
            case '^':
            case '$':
            case '=':
            case '@':
            case '\'':
            case '`':
            case ':':
            case '#':
                read();
                sb.append((char)rr);
                break;
            case '/':
                if(indicator != '#') {
                    read();
                    sb.append((char)rr);
                    break;
                }
                // FALL THROUGH
            default:
                if(rr == '(') {
                    read();
                    List<Message> args = parseExpressionChain();
                    parseCharacter(')');
                    return new NamedMessage(sb.toString(), args, null, sourcename,  l, cc);
                } else {
                    return new NamedMessage(sb.toString(), null, null, sourcename,  l, cc);
                }
            }
        }
    }

    private void parseCharacter(int c) throws IOException, ControlFlow {
        readWhiteSpace();
        int l = lineNumber;
        int cc = currentCharacter;
        int rr = read();
        if(rr != c) {
            fail(l, cc, "Expected: '" + (char)c + "' got: " + charDesc(rr), "" + (char)c, charDesc(rr));
        }
    }

    private Message parseEmptyMessageSend() throws IOException, ControlFlow {
        int l = lineNumber; int cc = currentCharacter-1;

        List<Message> args = parseExpressionChain();
        parseCharacter(')');

        return new NamedMessage("", args, null, sourcename,  l, cc);
    }

    private Message parseSquareMessageSend() throws IOException, ControlFlow {
        int l = lineNumber; int cc = currentCharacter-1;

        int rr = peek();
        int r2 = peek2();


        List<Message> args = null;
        if(rr == ']' && r2 == '(') {
            read();
            read();
            args = parseExpressionChain();
            parseCharacter(')');
        } else {
            args = parseExpressionChain();
            parseCharacter(']');
        }

        return new NamedMessage("[]", args, null, sourcename,  l, cc);
    }

    private Message parseCurlyMessageSend() throws IOException, ControlFlow {
        int l = lineNumber; int cc = currentCharacter-1;

        int rr = peek();
        int r2 = peek2();

        List<Message> args = null;
        if(rr == '}' && r2 == '(') {
            read();
            read();
            args = parseExpressionChain();
            parseCharacter(')');
        } else {
            args = parseExpressionChain();
            parseCharacter('}');
        }

        return new NamedMessage("{}", args, null, sourcename,  l, cc);
    }

    private Message parseSetMessageSend() throws IOException, ControlFlow {
        int l = lineNumber; int cc = currentCharacter-1;

        parseCharacter('{');
        List<Message> args = parseExpressionChain();
        parseCharacter('}');

        return new NamedMessage("set", args, null, sourcename,  l, cc);
    }

    private int readNumbersInto(StringBuilder sb) throws IOException {
        int rr;
        while(((rr = peek()) >= '0' && rr <= '9' ) || rr == '_') {
            read();
            if(rr != '_') {
                sb.append((char)rr);
            }
        }
        return rr;
    }

    private boolean parseBaseTen(StringBuilder sb, int indicator) throws IOException, ControlFlow {
        boolean decimal = false;
        int rr = -1;
        if(indicator == '0') {
            rr = peek();
            int r2 = peek2();
            if(rr == '.' && (r2 >= '0' && r2 <= '9')) {
                decimal = true;
                sb.append((char)rr);
                sb.append((char)r2);
                read(); read();
                rr = readNumbersInto(sb);

                if(rr == 'e' || rr == 'E') {
                    read();
                    sb.append((char)rr);
                    if((rr = peek()) == '-' || rr == '+') {
                        read();
                        sb.append((char)rr);
                        rr = peek();
                    }

                    if(rr >= '0' && rr <= '9') {
                        read();
                        sb.append((char)rr);
                        rr = readNumbersInto(sb);
                    } else {
                        fail("Expected at least one decimal character following exponent specifier in number literal - got: " + charDesc(rr));
                    }
                }
            }
        } else {
            rr = readNumbersInto(sb);
            int r2 = peek2();
            if(rr == '.' && r2 >= '0' && r2 <= '9') {
                decimal = true;
                sb.append((char)rr);
                sb.append((char)r2);
                read(); read();

                rr = readNumbersInto(sb);

                if(rr == 'e' || rr == 'E') {
                    read();
                    sb.append((char)rr);
                    if((rr = peek()) == '-' || rr == '+') {
                        read();
                        sb.append((char)rr);
                        rr = peek();
                    }

                    if(rr >= '0' && rr <= '9') {
                        read();
                        sb.append((char)rr);
                        rr = readNumbersInto(sb);
                    } else {
                        fail("Expected at least one decimal character following exponent specifier in number literal - got: " + charDesc(rr));
                    }
                }
            } else if(rr == 'e' || rr == 'E') {
                decimal = true;
                read();
                sb.append((char)rr);
                if((rr = peek()) == '-' || rr == '+') {
                    read();
                    sb.append((char)rr);
                    rr = peek();
                }

                if(rr >= '0' && rr <= '9') {
                    read();
                    sb.append((char)rr);
                    rr = readNumbersInto(sb);
                } else {
                    fail("Expected at least one decimal character following exponent specifier in number literal - got: " + charDesc(rr));
                }
            }
        }

        return decimal;
    }

    private final static boolean[][] BASES = new boolean[37][128];
    static {
        for(int i=0;i<=10;i++) {
            for(int j=0;j<i;j++) {
                BASES[i]['0' + j] = true;
            }
            BASES[i]['_'] = true;
        }
        for(int i=11;i<=36;i++) {
            for(int j=0;j<10;j++) {
                BASES[i]['0' + j] = true;
            }
            for(int j=10;j<i;j++) {
                BASES[i]['a' + (i-(j+1))] = true;
                BASES[i]['A' + (i-(j+1))] = true;
            }
            BASES[i]['_'] = true;
        }
    }

    private void parseOtherBase(StringBuilder sb, int radix, String name) throws IOException, ControlFlow {
        int rr;
        boolean[] base = BASES[radix];
        if((rr = peek()) > -1 && rr < 128 && base[rr]) {
            read();
            if(rr != '_') {
                sb.append((char)rr);
            }

            while((rr = peek()) > -1 && rr < 128 && base[rr]) {
                read();
                if(rr != '_') {
                    sb.append((char)rr);
                }
            }
        } else {
            fail("Expected at least one " + name + " character in " + name + " number literal - got: " + charDesc(rr));
        }
    }

    private Message parseNumber(int indicator) throws IOException, ControlFlow {
        int l = lineNumber; int cc = currentCharacter-1;
        boolean decimal = false;
        StringBuilder sb = new StringBuilder();

        if(indicator == '-') {
            sb.append((char)indicator);
            indicator = read();
        } else if(indicator == '+') {
            indicator = read();
        }

        int rr = -1;
        int radix = 10;
        String name = "decimal";

        if((rr = peek()) == '#') {
            radix = ((char)indicator) - '0';
            read();
            switch(radix) {
            case 2:
                name = "binary";
                break;
            case 8:
                name = "octal";
                break;
            default:
                name = "base " + radix;
                break;
            }
        } else if(isDigit(indicator) && isDigit(rr) && peek2() == '#') {
            radix = Integer.valueOf(String.valueOf(new char[]{(char)indicator, (char)rr}));
            read(); read();
            switch(radix) {
            case 2:
                name = "binary";
                break;
            case 8:
                name = "octal";
                break;
            case 16:
                name = "hexadecimal";
                break;
            default:
                name = "base " + radix;
                break;
            }
        } else if(indicator == '0' && (rr == 'x' || rr == 'X')) {
            radix = 16;
            read();
            name = "hexadecimal";
        } else {
            sb.append((char)indicator);
        }

        if(radix > 36) {
            fail("Expected radix between 0 and 36 - got: " + radix);
        }

        if(radix == 10) {
            decimal = parseBaseTen(sb, indicator);
        } else {
            decimal = false;
            parseOtherBase(sb, radix, name);
        }

        try {
            return new LiteralMessage(decimal ? new DFloNum(sb.toString()) : IntNum.valueOf(sb.toString(), radix), null, sourcename, l, cc);
        } catch(NumberFormatException e) {
            fail(e.getMessage());
            return null;
        }
    }

    private boolean isLetter(int c) {
        return ((c>='A' && c<='Z') ||
                c=='_' ||
                (c>='a' && c<='z') ||
                (c>='\u00C0' && c<='\u00D6') ||
                (c>='\u00D8' && c<='\u00F6') ||
                (c>='\u00F8' && c<='\u1FFF') ||
                (c>='\u2200' && c<='\u22FF') ||
                (c>='\u27C0' && c<='\u27EF') ||
                (c>='\u2980' && c<='\u2AFF') ||
                (c>='\u3040' && c<='\u318F') ||
                (c>='\u3300' && c<='\u337F') ||
                (c>='\u3400' && c<='\u3D2D') ||
                (c>='\u4E00' && c<='\u9FFF') ||
                (c>='\uF900' && c<='\uFAFF'));
    }

    private boolean isIDDigit(int c) {
        return ((c>='0' && c<='9') ||
                (c>='\u0660' && c<='\u0669') ||
                (c>='\u06F0' && c<='\u06F9') ||
                (c>='\u0966' && c<='\u096F') ||
                (c>='\u09E6' && c<='\u09EF') ||
                (c>='\u0A66' && c<='\u0A6F') ||
                (c>='\u0AE6' && c<='\u0AEF') ||
                (c>='\u0B66' && c<='\u0B6F') ||
                (c>='\u0BE7' && c<='\u0BEF') ||
                (c>='\u0C66' && c<='\u0C6F') ||
                (c>='\u0CE6' && c<='\u0CEF') ||
                (c>='\u0D66' && c<='\u0D6F') ||
                (c>='\u0E50' && c<='\u0E59') ||
                (c>='\u0ED0' && c<='\u0ED9') ||
                (c>='\u1040' && c<='\u1049'));
    }

    private boolean isDigit(int c) {
        return c>='0' && c<='9';
    }

    public static class Failure {
        public final int line;
        public final int character;
        public final String expected;
        public final String got;
        public final String source;
        public final String message;
        public Failure(int l, int c, String e, String g, String s, String m) {
            this.line = l;
            this.character = c;
            this.expected = e;
            this.got = g;
            this.source = s;
            this.message = m;
        }
    }

    private void fail(int l, int c, String message, String expected, String got) throws ControlFlow {
        throw new ControlFlow(new Failure(l, c, expected, got, sourcename, message));
    }

    private void fail(String message) throws ControlFlow {
        fail(lineNumber, currentCharacter, message, null, null);
    }

    private static String charDesc(int c) {
        if(c == -1) {
            return "EOF";
        } else if(c == 9) {
            return "TAB";
        } else if(c == 10 || c == 13) {
            return "EOL";
        } else {
            return "'" + (char)c + "'";
        }
    }
}// Parser
