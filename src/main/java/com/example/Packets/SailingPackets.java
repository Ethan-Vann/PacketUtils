package com.example.Packets;

import com.example.PacketUtils.PacketDef;
import com.example.PacketUtils.PacketReflection;

public class SailingPackets {
    public static void setDirection(int direction) {
        PacketReflection.sendPacket(PacketDef.getSetHeading(), direction);
    }
}
