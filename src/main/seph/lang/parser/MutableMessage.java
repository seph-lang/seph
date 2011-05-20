/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.parser;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import seph.lang.ast.Message;
import seph.lang.ast.NamedMessage;

import seph.lang.SephObject;
import seph.lang.Runtime;
import seph.lang.LexicalScope;
import seph.lang.SThread;
import seph.lang.persistent.IPersistentList;
import seph.lang.persistent.PersistentList;
import seph.lang.persistent.ISeq;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class MutableMessage implements Message {
    public List<Message> arguments;
    public Message realArguments;
    public Message firstArgument;

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
        throw new RuntimeException("ESCAPED MUTABLE MESSAGE");
    }

    public Message next() {
        throw new RuntimeException("ESCAPED MUTABLE MESSAGE");
    }

    public Message withNext(Message newNext) {
        IPersistentList args = realArguments == null ? null :
            (firstArgument == null ? new PersistentList(realArguments) : PersistentList.create(Arrays.asList(firstArgument, realArguments)));

        return NamedMessage.create(name, args, newNext, filename, line, position, null);
    }

    public Message withArguments(IPersistentList args) {
        throw new RuntimeException("ESCAPED MUTABLE MESSAGE");
    }

    public boolean isLiteral() {
        throw new RuntimeException("ESCAPED MUTABLE MESSAGE");
    }

    public SephObject literal() {
        throw new RuntimeException("ESCAPED MUTABLE MESSAGE");
    }

    public String filename() {
        throw new RuntimeException("ESCAPED MUTABLE MESSAGE");
    }

    public int line() {
        throw new RuntimeException("ESCAPED MUTABLE MESSAGE");
    }

    public int position() {
        throw new RuntimeException("ESCAPED MUTABLE MESSAGE");
    }

    public SephObject sendTo(SThread thread, LexicalScope scope, SephObject receiver, boolean first) {
        throw new RuntimeException("ESCAPED MUTABLE MESSAGE");
    }

    public SephObject go(SThread thread, LexicalScope scope, SephObject receiver, boolean first) {
        throw new RuntimeException("ESCAPED MUTABLE MESSAGE");
    }

    public String toString() {
        return "mutable<" + name + ">";
    }
}// MutableMessage
