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
import seph.lang.Text;
import seph.lang.ast.Message;
import seph.lang.ast.NamedMessage;
import seph.lang.ast.LiteralMessage;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class Parser {
    private final Runtime runtime;
    private final Reader reader;

    public Parser(Runtime runtime, Reader reader) {
        this.runtime = runtime;
        this.reader = reader;
    }

    public Message parseFully() throws IOException {
        Message result = parseExpressions();

        if(result == null) {
            result = new NamedMessage(".", null, null);
        }

        return result;
    }

    private Message parseExpressions() throws IOException {
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

    private List<Message> parseExpressionChain() throws IOException {
        ArrayList<Message> chain = new ArrayList<Message>();

        Message curr = parseExpressions();
        while(curr != null) {
            chain.add(curr);
            readWhiteSpace();
            int rr = peek();
            if(rr == ',') {
                read();
                curr = parseExpressions();
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

    private Message parseExpression() throws IOException {
        int rr;
        while(true) {
            rr = peek();
            switch(rr) {
            case -1:
                read();
                return null;
            case ')':
                return null;
            case '(':
                read();
                return parseEmptyMessageSend();
            case '[':
                read();
                return parseSquareMessageSend();
            case '#':
                read();
                switch(peek()) {
                case '!':
                    parseComment();
                    break;
                }
                break;
            case '"':
                read();
                return parseText('"');
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
                }
            case ':':
                read();
                if(isLetter(rr = peek())) {
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

    private Message parseRegularMessageSend(int indicator) throws IOException {
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
        return new NamedMessage(sb.toString(), args, null);
    }

    private void parseComment() throws IOException {
        int rr;
        while((rr = peek()) != '\n' && rr != '\r' && rr != -1) {
            read();
        }
    }

    private Message parseTerminator(int indicator) throws IOException {
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

        return new NamedMessage(".", null, null);
    }

    private Message parseText(int indicator) throws IOException {
        StringBuilder sb = new StringBuilder();

        int rr;

        while(true) {
            switch(rr = peek()) {
            case '"':
                read();
                return new LiteralMessage(new Text(new StringUtils().replaceEscapes(sb.toString())), null);
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

    private void parseDoubleQuoteEscape(StringBuilder sb) throws IOException {
        sb.append('\\');
        int rr = peek();
        switch(rr) {
        case '\n':
        case '\\':
            read();
            sb.append((char)rr);
            break;
        }
    }

    private Message parseOperatorChars(int indicator) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append((char)indicator);
        int rr;
        while(true) {
            rr = peek();
            switch(rr) {
            case ':':
                read();
                sb.append((char)rr);
                break;
            default:
                if(rr == '(') {
                    read();
                    List<Message> args = parseExpressionChain();
                    parseCharacter(')');
                    return new NamedMessage(sb.toString(), args, null);
                } else {
                    return new NamedMessage(sb.toString(), null, null);
                }
            }
        }
    }

    private void parseCharacter(int c) throws IOException {
        readWhiteSpace();
        int rr = read();
    }

    private Message parseEmptyMessageSend() throws IOException {
        List<Message> args = parseExpressionChain();
        parseCharacter(')');

        return new NamedMessage("", args, null);
    }

    private Message parseSquareMessageSend() throws IOException {
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

        return new NamedMessage("[]", args, null);
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
}// Parser
