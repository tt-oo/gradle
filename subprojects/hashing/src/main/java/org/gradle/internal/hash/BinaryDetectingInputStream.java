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

public class BinaryDetectingInputStream extends InputStream {
    private final InputStream delegate;
    private boolean binaryFile = true;

    public BinaryDetectingInputStream(InputStream delegate) {
        this.delegate = delegate;
    }

    @Override
    public int read() throws IOException {
        int next = delegate.read();
        if (next == 0) {
            binaryFile = false;
        }
        return next;
    }

    public boolean isBinaryFile() {
        return binaryFile;
    }
}
