package com.github.protocolfuzzing.protocolstatefuzzer.statefuzzer.core;

import java.util.Random;

public class ParameterizedServer {
    private static final Integer MAX_MSG_ID = Integer.MAX_VALUE;

    private Integer nextMsgId;
    private Random rand;

    public ParameterizedServer() {
        nextMsgId = null;
        rand = new Random(1);
    }

    public Ack send(Msg m) {
        if (nextMsgId == null || m.msgId.equals(nextMsgId)) {
            nextMsgId = rand.nextInt(MAX_MSG_ID);
            return new Ack(nextMsgId);
        } else {
            return null;
        }
    }

    public static class Msg {
        public final Integer msgId;

        public Msg(Integer msgId) {
            this.msgId = msgId;
         }
    }

    public static class Ack {
        public final Integer msgId;

        public Ack(Integer msgId){
            this.msgId = msgId;
        }
    }
}
