/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.compiler;

import java.dyn.MutableCallSite;
import java.dyn.MethodType;

public class SephCallSite extends MutableCallSite {
    public SephCallSite(MethodType type) {
        super(type);
    }
}// SephCallSite

