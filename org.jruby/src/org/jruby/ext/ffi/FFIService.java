/***** BEGIN LICENSE BLOCK *****
 * Version: CPL 1.0/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Common Public
 * License Version 1.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.eclipse.org/legal/cpl-v10.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * Copyright (C) 2010 JRuby project
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either of the GNU General Public License Version 2 or later (the "GPL"),
 * or the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the CPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the CPL, the GPL or the LGPL.
 ***** END LICENSE BLOCK *****/

package org.jruby.ext.ffi;

import java.io.IOException;
import org.jruby.Ruby;
import org.jruby.RubyInstanceConfig;
import org.jruby.RubyModule;
import org.jruby.runtime.load.Library;

public class FFIService implements Library {
    public void load(final Ruby runtime, boolean wrap) throws IOException {
        if (!RubyInstanceConfig.nativeEnabled) {
            throw runtime.newLoadError("Native API access is disabled");
        }
        if (!Platform.getPlatform().isSupported()) {
            throw runtime.newLoadError("Unsupported platform: " + Platform.getPlatform().getName());
        }

        RubyModule ffi = runtime.defineModule("FFI");
        try {
            Factory.getInstance().init(runtime, ffi);
        } catch (Exception e) {
            throw runtime.newLoadError("Could not load FFI Provider: " + e.getLocalizedMessage()
                    + " See http://jira.codehaus.org/browse/JRUBY-4583");
        }
    }
}
