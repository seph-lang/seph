/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

/**
 * Represents a running thread of Seph computation. This
 * might not correspond to any real threads.
 *
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class SThread {
    public final static SephObject TAIL_MARKER = null;

    //    public SephObject receiver;
    public final Runtime runtime;
    
    public SThread(Runtime runtime) {
        this.runtime = runtime;
    }
}// SThread
