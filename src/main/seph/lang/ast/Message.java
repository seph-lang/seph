/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.ast;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public interface Message {
    String name();
    Message[] arguments();
    Message next();
}// Message

