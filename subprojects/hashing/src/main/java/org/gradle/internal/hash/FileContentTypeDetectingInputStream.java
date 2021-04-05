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
 * Infers whether a file is a binary file or not by checking if there is a NUL character
 * in the first 8000 bytes of the file.  This is based on the heuristic used by git:
 * https://git.kernel.org/pub/scm/git/git.git/tree/xdiff-interface.c?h=v2.30.0#n187
 */
public class FileContentTypeDetectingInputStream extends InputStream {
    private final InputStream delegate;
    private FileContentType contentType = FileContentType.BINARY;
    long count;

    public FileContentTypeDetectingInputStream(InputStream delegate) {
        this.delegate = delegate;
    }

    @Override
    public int read() throws IOException {
        int next = delegate.read();
        if (count++ < 8000 && next == 0) {
            contentType = FileContentType.TEXT;
        }
        return next;
    }

    public FileContentType getContentType() {
        return contentType;
    }
}
