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
package org.kaazing.netx.ws.specification.ext.primary;

import java.io.IOException;
import java.nio.charset.Charset;

import org.kaazing.netx.ws.internal.ext.WebSocketContext;
import org.kaazing.netx.ws.internal.ext.WebSocketExtensionSpi;
import org.kaazing.netx.ws.internal.ext.frame.Data;
import org.kaazing.netx.ws.internal.ext.frame.Frame;
import org.kaazing.netx.ws.internal.ext.frame.Frame.Payload;
import org.kaazing.netx.ws.internal.ext.frame.FrameFactory;
import org.kaazing.netx.ws.internal.ext.frame.OpCode;
import org.kaazing.netx.ws.internal.ext.function.WebSocketFrameSupplier;

public class PrimaryExtensionSpi extends WebSocketExtensionSpi {
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    {
        onTextFrameReceived = new WebSocketFrameSupplier() {

            @Override
            public void apply(WebSocketContext context, Frame frame) throws IOException {
                OpCode opcode = frame.getOpCode();
                int payloadLength = frame.getLength();
                Payload payload = frame.getPayload();

                String msg = "Hello, " + new String(payload.buffer().array(), payload.offset(), payloadLength);
                byte[] bytes = msg.getBytes(UTF_8);
                FrameFactory factory = FrameFactory.newInstance(8192);
                Frame transformedFrame = factory.getFrame(opcode, frame.isFin(), frame.isMasked(), bytes, 0, bytes.length);
                context.onTextFrameReceived((Data) transformedFrame);
            }
        };

        onTextFrameSent = new WebSocketFrameSupplier() {

            @Override
            public void apply(WebSocketContext context, Frame frame) throws IOException {
                OpCode opcode = frame.getOpCode();
                int payloadLength = frame.getLength();
                Payload payload = frame.getPayload();

                String msg = new String(payload.buffer().array(), payload.offset(), payloadLength).substring("Hello, ".length());
                byte[] bytes = msg.getBytes(UTF_8);
                FrameFactory factory = FrameFactory.newInstance(8192);
                Frame transformedFrame = factory.getFrame(opcode, frame.isFin(), frame.isMasked(), bytes, 0, bytes.length);

                context.onTextFrameSent((Data) transformedFrame);
            }
        };
    }

    public PrimaryExtensionSpi() {
    }
}
