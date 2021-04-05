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

package org.gradle.api.internal.changedetection.state;

import org.gradle.cache.PersistentIndexedCache;
import org.gradle.internal.hash.FileContentType;
import org.gradle.internal.hash.FileContentTypeCacheService;
import org.gradle.internal.hash.HashCode;

public class DefaultFileContentTypeCacheService implements FileContentTypeCacheService {
    private final PersistentIndexedCache<HashCode, FileContentType> cache;

    public DefaultFileContentTypeCacheService(PersistentIndexedCache<HashCode, FileContentType> cache) {
        this.cache = cache;
    }

    @Override
    public FileContentType getFileContentType(String path, HashCode hashCode) {
        return cache.get(hashCode, () -> FileContentType.BINARY);
    }

    @Override
    public void storeContentType(String path, HashCode hashCode, FileContentType contentType) {
        cache.put(hashCode, contentType);
    }
}
