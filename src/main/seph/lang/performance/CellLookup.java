/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang.performance;

import seph.lang.*;
import seph.lang.structure.*;
import seph.lang.persistent.*;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class CellLookup {
    private final static String KEY = "foobar";


    private final static void single(int iterations, SephObject obj, long[] result) throws Exception {
        long before, after;
        for(int j = 0; j<10; j++) {
            before = System.currentTimeMillis();
            for(int i = 0; i<iterations; i++) {
                obj.get(KEY);
            }
            after = System.currentTimeMillis();
            result[j] = after - before; 
        }
    }

    private final static String RESET = "\33[0m";
    private final static String RED = "\33[31m";
    private final static String GREEN = "\33[32m";
    private final static String YELLOW = "\33[33m";

    private static void bench(String desc, int iterations, boolean warmup, SephObject hard, SephObject array, SephObject hash) throws Exception {
        long[] hard_results = new long[10];
        long[] array_results = new long[10];
        long[] hash_results = new long[10];

        single(iterations, hard, hard_results);
        single(iterations, array, array_results);
        single(iterations, hash, hash_results);

        if(!warmup) {
            java.util.Arrays.sort(hard_results);
            java.util.Arrays.sort(array_results);
            java.util.Arrays.sort(hash_results);

            String color = (hard_results[0] <= array_results[0] && hard_results[0] <= hash_results[0]) ? GREEN :
                (array_results[0] <= hash_results[0] ? YELLOW : RED);

            System.out.printf(" %s%-40s hard: %-6d array: %-6d hash: %-6d%s\n", color, desc, hard_results[0], array_results[0], hash_results[0], RESET);
        } else {
            System.out.print(".");
        }
    }

    private static void bench1(String desc, int iterations, boolean warmup) throws Exception {
        SephObject hard = new SephObject_0_1(null, KEY, null);
        SephObject array = new SephObject_0_n(null, new PersistentArrayMap(new Object[] {KEY, null}));
        SephObject hash = new SephObject_0_n(null, PersistentHashMap.create(KEY, null));
        bench(desc, iterations, warmup, hard, array, hash);
    }

    private static void bench2(String desc, int iterations, boolean warmup) throws Exception {
        SephObject hard = new SephObject_0_1(null, "another key", null);
        SephObject array = new SephObject_0_n(null, new PersistentArrayMap(new Object[] {"another key", null}));
        SephObject hash = new SephObject_0_n(null, PersistentHashMap.create("another key", null));
        bench(desc, iterations, warmup, hard, array, hash);
    }

    private static void bench3(String desc, int iterations, boolean warmup) throws Exception {
        SephObject hard = new SephObject_0_2(null, KEY, null, "another key", null);
        SephObject array = new SephObject_0_n(null, new PersistentArrayMap(new Object[] {KEY, null, "another key", null}));
        SephObject hash = new SephObject_0_n(null, PersistentHashMap.create(KEY, null, "another key", null));
        bench(desc, iterations, warmup, hard, array, hash);
    }

    private static void bench4(String desc, int iterations, boolean warmup) throws Exception {
        SephObject hard = new SephObject_0_2(null, "another key", null, KEY, null);
        SephObject array = new SephObject_0_n(null, new PersistentArrayMap(new Object[] {"another key", null, KEY, null}));
        SephObject hash = new SephObject_0_n(null, PersistentHashMap.create("another key", null, KEY, null));
        bench(desc, iterations, warmup, hard, array, hash);
    }

    private static void bench5(String desc, int iterations, boolean warmup) throws Exception {
        SephObject hard = new SephObject_0_2(null, "another key", null, "newer key", null);
        SephObject array = new SephObject_0_n(null, new PersistentArrayMap(new Object[] {"another key", null, "newer key", null}));
        SephObject hash = new SephObject_0_n(null, PersistentHashMap.create("another key", null, "newer key", null));
        bench(desc, iterations, warmup, hard, array, hash);
    }

    private static void bench6(String desc, int iterations, boolean warmup) throws Exception {
        SephObject hard = new SephObject_0_10(null, KEY, null, "another key", null, "another key2", null, "another key3", null, "another key4", null, "another key5", null, "another key6", null, "another key7", null, "another key8", null, "another key9", null);
        SephObject array = new SephObject_0_n(null, new PersistentArrayMap(new Object[] {KEY, null, "another key", null, "another key2", null, "another key3", null, "another key4", null, "another key5", null, "another key6", null, "another key7", null, "another key8", null, "another key9", null}));
        SephObject hash = new SephObject_0_n(null, PersistentHashMap.create(KEY, null, "another key", null, "another key2", null, "another key3", null, "another key4", null, "another key5", null, "another key6", null, "another key7", null, "another key8", null, "another key9", null));
        bench(desc, iterations, warmup, hard, array, hash);
    }

    private static void bench7(String desc, int iterations, boolean warmup) throws Exception {
        SephObject hard = new SephObject_0_10(null, "another key", null, "another key2", null, "another key3", null, "another key4", null, "another key5", null, "another key6", null, "another key7", null, "another key8", null, "another key9", null, KEY, null);
        SephObject array = new SephObject_0_n(null, new PersistentArrayMap(new Object[] {"another key", null, "another key2", null, "another key3", null, "another key4", null, "another key5", null, "another key6", null, "another key7", null, "another key8", null, "another key9", null, KEY, null, }));
        SephObject hash = new SephObject_0_n(null, PersistentHashMap.create("another key", null, "another key2", null, "another key3", null, "another key4", null, "another key5", null, "another key6", null, "another key7", null, "another key8", null, "another key9", null, KEY, null));
        bench(desc, iterations, warmup, hard, array, hash);
    }

    private static void bench8(String desc, int iterations, boolean warmup) throws Exception {
        SephObject hard = new SephObject_0_10(null, "another key", null, "another key2", null, "another key3", null, "another key4", null, "another key5", null, "another key6", null, "another key7", null, "another key8", null, "another key9", null, "another key10", null);
        SephObject array = new SephObject_0_n(null, new PersistentArrayMap(new Object[] {"another key", null, "another key2", null, "another key3", null, "another key4", null, "another key5", null, "another key6", null, "another key7", null, "another key8", null, "another key9", null, "another key10", null, }));
        SephObject hash = new SephObject_0_n(null, PersistentHashMap.create("another key", null, "another key2", null, "another key3", null, "another key4", null, "another key5", null, "another key6", null, "another key7", null, "another key8", null, "another key9", null, "another key10", null));
        bench(desc, iterations, warmup, hard, array, hash);
    }

    private static void bench9(String desc, int iterations, boolean warmup) throws Exception {
        SephObject hard = new SephObject_0_30(null, KEY, null, "another key", null, "another key2", null, "another key3", null, "another key4", null, "another key5", null, "another key6", null, "another key7", null, "another key8", null, "another key9", null, "1another key", null, "1another key2", null, "1another key3", null, "1another key4", null, "1another key5", null, "1another key6", null, "1another key7", null, "1another key8", null, "1another key9", null, "2another key", null, "2another key2", null, "2another key3", null, "2another key4", null, "2another key5", null, "2another key6", null, "2another key7", null, "2another key8", null, "2another key9", null, "3another key7", null, "3another key8", null);
        SephObject array = new SephObject_0_n(null, new PersistentArrayMap(new Object[] {KEY, null, "another key", null, "another key2", null, "another key3", null, "another key4", null, "another key5", null, "another key6", null, "another key7", null, "another key8", null, "another key9", null, "1another key", null, "1another key2", null, "1another key3", null, "1another key4", null, "1another key5", null, "1another key6", null, "1another key7", null, "1another key8", null, "1another key9", null, "2another key", null, "2another key2", null, "2another key3", null, "2another key4", null, "2another key5", null, "2another key6", null, "2another key7", null, "2another key8", null, "2another key9", null, "3another key7", null, "3another key8", null}));
        SephObject hash = new SephObject_0_n(null, PersistentHashMap.create(KEY, null, "another key", null, "another key2", null, "another key3", null, "another key4", null, "another key5", null, "another key6", null, "another key7", null, "another key8", null, "another key9", null, "1another key", null, "1another key2", null, "1another key3", null, "1another key4", null, "1another key5", null, "1another key6", null, "1another key7", null, "1another key8", null, "1another key9", null, "2another key", null, "2another key2", null, "2another key3", null, "2another key4", null, "2another key5", null, "2another key6", null, "2another key7", null, "2another key8", null, "2another key9", null, "3another key7", null, "3another key8", null));
        bench(desc, iterations, warmup, hard, array, hash);
    }

    private static void bench10(String desc, int iterations, boolean warmup) throws Exception {
        SephObject hard = new SephObject_0_30(null, "another key", null, "another key2", null, "another key3", null, "another key4", null, "another key5", null, "another key6", null, "another key7", null, "another key8", null, "another key9", null, "1another key", null, "1another key2", null, "1another key3", null, "1another key4", null, "1another key5", null, "1another key6", null, "1another key7", null, "1another key8", null, "1another key9", null, "2another key", null, "2another key2", null, "2another key3", null, "2another key4", null, "2another key5", null, "2another key6", null, "2another key7", null, "2another key8", null, "2another key9", null, "3another key7", null, "3another key8", null, KEY, null);
        SephObject array = new SephObject_0_n(null, new PersistentArrayMap(new Object[] {"another key", null, "another key2", null, "another key3", null, "another key4", null, "another key5", null, "another key6", null, "another key7", null, "another key8", null, "another key9", null, "1another key", null, "1another key2", null, "1another key3", null, "1another key4", null, "1another key5", null, "1another key6", null, "1another key7", null, "1another key8", null, "1another key9", null, "2another key", null, "2another key2", null, "2another key3", null, "2another key4", null, "2another key5", null, "2another key6", null, "2another key7", null, "2another key8", null, "2another key9", null, "3another key7", null, "3another key8", null, KEY, null}));
        SephObject hash = new SephObject_0_n(null, PersistentHashMap.create("another key", null, "another key2", null, "another key3", null, "another key4", null, "another key5", null, "another key6", null, "another key7", null, "another key8", null, "another key9", null, "1another key", null, "1another key2", null, "1another key3", null, "1another key4", null, "1another key5", null, "1another key6", null, "1another key7", null, "1another key8", null, "1another key9", null, "2another key", null, "2another key2", null, "2another key3", null, "2another key4", null, "2another key5", null, "2another key6", null, "2another key7", null, "2another key8", null, "2another key9", null, "3another key7", null, "3another key8", null, KEY, null));
        bench(desc, iterations, warmup, hard, array, hash);
    }

    private static void bench11(String desc, int iterations, boolean warmup) throws Exception {
        SephObject hard = new SephObject_0_30(null, "another key", null, "another key2", null, "another key3", null, "another key4", null, "another key5", null, "another key6", null, "another key7", null, "another key8", null, "another key9", null, "1another key", null, "1another key2", null, "1another key3", null, "1another key4", null, "1another key5", null, "1another key6", null, "1another key7", null, "1another key8", null, "1another key9", null, "2another key", null, "2another key2", null, "2another key3", null, "2another key4", null, "2another key5", null, "2another key6", null, "2another key7", null, "2another key8", null, "2another key9", null, "3another key7", null, "3another key8", null, "3another key9", null);
        SephObject array = new SephObject_0_n(null, new PersistentArrayMap(new Object[] {"another key", null, "another key2", null, "another key3", null, "another key4", null, "another key5", null, "another key6", null, "another key7", null, "another key8", null, "another key9", null, "1another key", null, "1another key2", null, "1another key3", null, "1another key4", null, "1another key5", null, "1another key6", null, "1another key7", null, "1another key8", null, "1another key9", null, "2another key", null, "2another key2", null, "2another key3", null, "2another key4", null, "2another key5", null, "2another key6", null, "2another key7", null, "2another key8", null, "2another key9", null, "3another key7", null, "3another key8", null, "3another key9", null}));
        SephObject hash = new SephObject_0_n(null, PersistentHashMap.create("another key", null, "another key2", null, "another key3", null, "another key4", null, "another key5", null, "another key6", null, "another key7", null, "another key8", null, "another key9", null, "1another key", null, "1another key2", null, "1another key3", null, "1another key4", null, "1another key5", null, "1another key6", null, "1another key7", null, "1another key8", null, "1another key9", null, "2another key", null, "2another key2", null, "2another key3", null, "2another key4", null, "2another key5", null, "2another key6", null, "2another key7", null, "2another key8", null, "2another key9", null, "3another key7", null, "3another key8", null, "3another key9", null));
        bench(desc, iterations, warmup, hard, array, hash);
    }

    public static void main(String[] args) throws Exception {
        System.out.print("warmup");
        bench1(null, 1000000, true);
        bench2(null, 1000000, true);
        bench3(null, 1000000, true);
        bench4(null, 1000000, true);
        bench5(null, 1000000, true);
        bench6(null, 1000000, true);
        bench7(null, 1000000, true);
        bench8(null, 1000000, true);
        bench9(null, 1000000, true);
        bench10(null, 1000000, true);
        bench11(null, 1000000, true);
        System.out.println();
        System.out.println();

        bench1("1 cell/0 parents - hit", 1000000, false);
        bench2("1 cell/0 parents - no hit", 1000000, false);
        System.out.println();
        bench3("2 cells/0 parents - hit first", 1000000, false);
        bench4("2 cells/0 parents - hit second", 1000000, false);
        bench5("2 cells/0 parents - no hit", 1000000, false);
        System.out.println();
        bench6("10 cells/0 parents - hit first", 1000000, false);
        bench7("10 cells/0 parents - hit last", 1000000, false);
        bench8("10 cells/0 parents - no hit", 1000000, false);
        System.out.println();
        bench9("30 cells/0 parents - hit first", 1000000, false);
        bench10("30 cells/0 parents - hit last", 1000000, false);
        bench11("30 cells/0 parents - no hit", 1000000, false);
    }
}// CellLookup
