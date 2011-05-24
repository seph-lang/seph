/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import java.util.List;
import java.util.ArrayList;

import seph.lang.ast.Abstraction;
import seph.lang.ast.Message;
import seph.lang.compiler.AbstractionCompiler;
import seph.lang.compiler.CompilationAborted;
import seph.lang.persistent.IPersistentList;
import seph.lang.persistent.PersistentList;
import seph.lang.persistent.PersistentArrayMap;
import seph.lang.persistent.ISeq;
import seph.lang.persistent.RT;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class DefaultAbstraction {
    public final static SephObject createFrom(Abstraction message, LexicalScope scope, String abstractionName) {
        ISeq seq = RT.seq(message.arguments());
        return AbstractionCompiler.compile(seq, scope, message.scope, new AbstractionCompiler.SemiStaticScope(new ArrayList<String>(), null), abstractionName);
    }
}// DefaultAbstraction
