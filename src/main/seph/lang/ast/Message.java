/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.ast;

import java.util.List;

import seph.lang.SephObject;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public interface Message {
    String name();
    List<Message> arguments();
    Message next();
    Message withNext(Message newNext);
    boolean isLiteral();
    SephObject literal();
    String filename();
    int line();
    int position();
}// Message

