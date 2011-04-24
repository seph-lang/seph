/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package seph.lang;

import java.io.InputStreamReader;
import java.io.StringReader;

import java.util.Properties;

/**
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class Main {
    private final static String HELP =
        "Usage: seph [switches] -- [programfile] [arguments]\n" +
        " -h, --help      help, this message\n" +
        " --copyright     print the copyright\n" +
        " --version       print current version\n";

    public static void main(String[] args) throws Throwable {
        int start = 0;
        boolean done = false;
        boolean printedSomething = false;

        for(;!done && start<args.length;start++) {
            String arg = args[start];
            if(arg.length() > 0) {
                if(arg.charAt(0) != '-') {
                    done = true;
                    break;
                } else {
                    if(arg.equals("--")) {
                        done = true;
                    } else if(arg.equals("-h") || arg.equals("--help")) {
                        System.err.print(HELP);
                        return;
                    } else if(arg.equals("--version")) {
                        System.err.println(getVersion());
                        printedSomething = true;
                    } else if(arg.equals("--copyright")) {
                        System.err.print(COPYRIGHT);
                        printedSomething = true;
                    }
                }
            }
        }

        if(args.length > start) {
            String file = args[start];
            if(file.startsWith("\"")) {
                file = file.substring(1, file.length());
            }

            if(file.length() > 1 && file.charAt(file.length()-1) == '"') {
                file = file.substring(0, file.length()-1);
            }

            Runtime r = new Runtime();
            r.evaluateFile(file);
        }
    }

    public static String getVersion() {
        try {
            Properties props = new Properties();
            props.load(Main.class.getResourceAsStream("/seph/lang/version.properties"));

            String version = props.getProperty("seph.build.versionString");
            String date = props.getProperty("seph.build.date");
            String commit = props.getProperty("seph.build.commit");

            return version + " [" + date + " -- " + commit + "]";
        } catch(Exception e) {
        }

        return "";
    }

    private final static String COPYRIGHT =
        "Copyright (c) 2010-2011 Ola Bini, ola.bini@gmail.com\n"+
        "\n"+
        "Permission is hereby granted, free of charge, to any person obtaining a copy\n"+
        "of this software and associated documentation files (the \"Software\"), to deal\n"+
        "in the Software without restriction, including without limitation the rights\n"+
        "to use, copy, modify, merge, publish, distribute, sublicense, and/or sell\n"+
        "copies of the Software, and to permit persons to whom the Software is\n"+
        "furnished to do so, subject to the following conditions:\n"+
        "\n"+
        "The above copyright notice and this permission notice shall be included in\n"+
        "all copies or substantial portions of the Software.\n"+
        "\n"+
        "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\n"+
        "IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,\n"+
        "FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE\n"+
        "AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER\n"+
        "LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,\n"+
        "OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN\n"+
        "THE SOFTWARE.\n";
}// Main
