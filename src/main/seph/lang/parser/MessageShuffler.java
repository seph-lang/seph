/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.parser;

import java.util.List;
import java.util.ListIterator;
import java.util.ArrayList;

import seph.lang.ast.Message;

import seph.lang.persistent.IPersistentList;
import seph.lang.persistent.IPersistentCollection;
import seph.lang.persistent.PersistentList;
import seph.lang.persistent.ISeq;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class MessageShuffler {
    private boolean isOperator(String name) {
        return name.equals("+") || 
            name.equals("<=>") || 
            name.equals("<")  || 
            name.equals(">")  || 
            name.equals(">=")  || 
            name.equals("<=")  || 
            name.equals("≤")  || 
            name.equals("≥") ||
            name.equals("!=") ||
            name.equals("≠") ||
            name.equals("==") ||
            name.equals("===") ||
            name.equals("=~") ||
            name.equals("!~") ||
            name.equals("-") ||
            name.equals("+") ||
            name.equals("*") ||
            name.equals("/") ||
            name.equals("**") ||
            name.equals("%") ||
            name.equals("=>") ||
            name.equals("..") ||
            name.equals("...") ||
            name.equals("<<") ||
            name.equals(">>") ||
            name.equals("&") ||
            name.equals("|") ||
            name.equals("^") ||
            name.equals("&&") ||
            name.equals("||") ||
            name.equals("?&") ||
            name.equals("?|") ||
            name.equals("or") ||
            name.equals("and") ||
            name.equals("->") ||
            name.equals("+>") ||
            name.equals("!>") ||
            name.equals("<>") ||
            name.equals("&>") ||
            name.equals("%>") ||
            name.equals("#>") ||
            name.equals("@>") ||
            name.equals("/>") ||
            name.equals("*>") ||
            name.equals("?>") ||
            name.equals("|>") ||
            name.equals("^>") ||
            name.equals("~>") ||
            name.equals("->>") ||
            name.equals("+>>") ||
            name.equals("!>>") ||
            name.equals("<>>") ||
            name.equals("&>>") ||
            name.equals("%>>") ||
            name.equals("#>>") ||
            name.equals("@>>") ||
            name.equals("/>>") ||
            name.equals("*>>") ||
            name.equals("?>>") ||
            name.equals("|>>") ||
            name.equals("^>>") ||
            name.equals("~>>") ||
            name.equals("=>>") ||
            name.equals("**>") ||
            name.equals("**>>") ||
            name.equals("&&>") ||
            name.equals("&&>>") ||
            name.equals("||>") ||
            name.equals("||>>") ||
            name.equals("$>") ||
            name.equals("$>>") ||
            name.equals("<->") ||
            name.equals("<-") ||
            name.equals("nand") ||
            name.equals("nor") ||
            name.equals("xor") ||
            name.equals("return");
    }

    public Message shuffle(Message input) {
        Message current = input;
        List<Message> separated = new ArrayList<Message>();
        while(current != null) {
            String name = current.name();
            if(current.arguments().count() == 0 && isOperator(name)) {
                separated.add(current.withArguments(new PersistentList(current.next())));
                current = null;
            } else {
                separated.add(current.withArguments(shuffle(current.arguments())));
                current = current.next();
            }
        }

        Message ret = null;
        for(ListIterator<Message> i = separated.listIterator(separated.size()); i.hasPrevious();) {
            ret = i.previous().withNext(ret);
		}

        return ret;
    }

    private IPersistentList shuffle(IPersistentList args) {
        return (IPersistentList)shuffle(PersistentList.EMPTY, args.seq());
    }

    private IPersistentCollection shuffle(IPersistentCollection current, ISeq args) {
        if(args == null) {
            return current;
        }

        return shuffle(current, args.next()).cons(shuffle((Message)args.first()));
    }
}// MessageShuffler
