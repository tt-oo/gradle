/*
 * Copyright 2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.internal.hash;

import java.io.IOException;
import java.io.InputStream;

/**
 * Infers whether a file is a binary file or not by checking if there are any ASCII
 * control characters in the file.  If so, then it is likely a binary file.
 */
public class FileContentTypeDetectingInputStream extends InputStream {
    private final InputStream delegate;
    private boolean controlCharactersFound;

    public FileContentTypeDetectingInputStream(InputStream delegate) {
        this.delegate = delegate;
    }

    @Override
    public int read() throws IOException {
        int next = delegate.read();
        if (isControlCharacter(next)) {
            controlCharactersFound = true;
        }
        return next;
    }

    private boolean isControlCharacter(int c) {
        return isInControlRange(c) && isNotCommonTextChar(c);
    }

    private boolean isInControlRange(int c) {
        return c >= 0x00 && c < 0x20;
    }

    private boolean isNotCommonTextChar(int c) {
        return c != 0x09  // tab
            && c != 0x0a  // line feed
            && c != 0x0c  // form feed
            && c != 0x0d; // carriage return
    }

    public FileContentType getContentType() {
        return controlCharactersFound ? FileContentType.BINARY : FileContentType.TEXT;
    }
}
