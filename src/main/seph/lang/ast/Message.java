/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.ast;

import seph.lang.Runtime;
import seph.lang.SephObject;
import seph.lang.LexicalScope;
import seph.lang.SThread;
import seph.lang.persistent.IPersistentList;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public interface Message {
    String name();
    IPersistentList arguments();
    Message next();
    Message withNext(Message newNext);
    Message withArguments(IPersistentList args);
    boolean isLiteral();
    SephObject literal();
    String filename();
    int line();
    int position();
}// Message

