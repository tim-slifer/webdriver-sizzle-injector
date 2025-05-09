/*
Copyright (c) 2019 Tim Slifer

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

 */
package dev.slifer.sizzlecss;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;

/**
 * Loads and stores the SizzleJS for injection onto the DOM.
 *
 * @author Tim Slifer
 */
public class SizzleScript {
    
    private static SizzleScript sizzleInstance;
    private String script;
    
    private static final String VERSION = "2.3.10";
    
    private SizzleScript() {
        this.script = loadSizzle();
    }
    
    public static SizzleScript getInstance() {
        if (sizzleInstance == null) {
            sizzleInstance = new SizzleScript();
        }
        
        return sizzleInstance;
    }
    
    public String getScript() {
        return script;
    }
    
    private String loadSizzle() {
        try {
            InputStream stream =
                    getClass().getResourceAsStream("/META-INF/resources/webjars/sizzle/" + VERSION + "/sizzle.js");
            Reader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder builder = new StringBuilder();
            char[] buffer = new char[8192];
            int line;
            while ((line = reader.read(buffer, 0, buffer.length)) > 0) {
                builder.append(buffer, 0, line);
            }
            reader.close();
            
            String rawSizzle = builder.toString();
            return getInjectionScript(rawSizzle);
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    private String getInjectionScript(String sizzle) {
        return "if (typeof define !== 'undefined') {" +
                "var oldDefine = define;" +
                "define = undefined;" +
                sizzle +
                "window.define = oldDefine;" +
                "} else {" +
                sizzle + "}";
    }
}
