/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.parser;

import java.util.List;
import java.util.ArrayList;

import seph.lang.ast.Message;
import seph.lang.ast.NamedMessage;

import seph.lang.SephObject;
import seph.lang.Runtime;
import seph.lang.LexicalScope;
import seph.lang.persistent.IPersistentList;
import seph.lang.persistent.PersistentList;
import seph.lang.persistent.ISeq;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class MutableMessage implements Message {
    public List<Message> arguments;

    private final String name;
    private final Message next;

    private final String filename;
    private final int line;
    private final int position;

    public MutableMessage(String name, Message next, String filename, int line, int position) {
        this.name = name == null ? null : name.intern();
        this.next = next;
        this.filename = filename;
        this.line = line;
        this.position = position;
    }

    public String name() {
        return this.name;
    }

    public IPersistentList arguments() {
        return null;
    }

    public Message next() {
        return this.next;
    }

    public Message withNext(Message newNext) {
        return null;
    }

    public Message withArguments(IPersistentList args) {
        return null;
    }

    public boolean isLiteral() {
        return false;
    }

    public SephObject literal() {
        return null;
    }

    public String filename() {
        return this.filename;
    }

    public int line() {
        return this.line;
    }

    public int position() {
        return this.position;
    }

    public SephObject sendTo(LexicalScope scope, SephObject receiver, Runtime runtime) {
        return null;
    }

    public NamedMessage fix() {
        return new NamedMessage(name, arguments != null ? PersistentList.create(arguments) : null, next, filename, line, position);
    }
}// MutableMessage
