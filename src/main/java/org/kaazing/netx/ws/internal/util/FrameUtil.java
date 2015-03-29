/*
 * Copyright 2014, Kaazing Corporation. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kaazing.netx.ws.internal.util;

import java.nio.ByteBuffer;

public final class FrameUtil {
    public static final byte[] EMPTY_MASK = new byte[] { 0x00, 0x00, 0x00, 0x00 };

    private FrameUtil() {

    }

    public static int calculateNeed(boolean masked, long payloadLength) {
        int capacity = 1; // opcode

        if (payloadLength < 126) {
            capacity++;
        } else if (payloadLength <= 0xFFFF) {
            capacity += 3;
        } else {
            capacity += 9;
        }

        if (masked) {
            capacity += 4;
        }

        capacity += payloadLength;
        return capacity;
    }

    public static void putBytes(ByteBuffer buffer, int offset, byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            buffer.put(offset + i, bytes[i]);
        }
    }
}
