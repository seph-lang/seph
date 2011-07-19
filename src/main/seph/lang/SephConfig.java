/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class SephConfig {
    private final boolean tco;

    public SephConfig() {
        this(true);
    }

    public SephConfig(boolean tco) {
        this.tco = tco;
    }

    public boolean doTailCallOptimization() {
        return tco;
    }

    public SephConfig withTailCallOptimization() {
        return new SephConfig(true);
    }

    public SephConfig withoutTailCallOptimization() {
        return new SephConfig(false);
    }
}// SephConfig
