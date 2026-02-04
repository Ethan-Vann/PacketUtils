package com.example.InteractionApi;

import com.example.EthanApiPlugin.EthanApiPlugin;
import com.example.Packets.MousePackets;
import com.example.Packets.SailingPackets;
import com.example.Packets.WidgetPackets;
import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.gameval.VarbitID;

public class SailingInteraction
{

    public static final Client client = EthanApiPlugin.getClient();

    public static final int SAILING_CONTROLS_ID = InterfaceID.SailingSidepanel.FACILITIES_CONTENT_CLICKLAYER;

    public static final int NAVIGATING_VARBIT = VarbitID.SAILING_BOAT_FACILITY_LOCKEDIN; // 0 = not navigating, 3 = navigating (? maybe != 0 == navigating)
    public static final int SPEED_VARBIT = VarbitID.SAILING_SIDEPANEL_BOAT_MOVE_MODE; // 0 = stopped, 1 = first, 2 = second, 3 = reverse

    public static void setDirection(Direction direction) {
        MousePackets.queueClickPacket();
        SailingPackets.setDirection(direction.getCode());
    }

    public static void increaseSpeed() {
        if (!isMoving()) {
            setSails();
            return;
        }

        int speed = client.getVarbitValue(SPEED_VARBIT);

        if (speed == 2) {
            return;
        }

        MousePackets.queueClickPacket();
        WidgetPackets.queueWidgetActionPacket(1, SAILING_CONTROLS_ID, -1, 2);
    }

    public static void decreaseSpeed() {
        int speed = client.getVarbitValue(SPEED_VARBIT);

        if (speed == 3) {
            return;
        }

        MousePackets.queueClickPacket();
        WidgetPackets.queueWidgetActionPacket(1, SAILING_CONTROLS_ID, -1, 1);
    }

    public static Direction getDirection() {
        int angle = client.getVarbitValue(VarbitID.SAILING_BOAT_SPAWNED_ANGLE);
        return Direction.fromAngle(angle);
    }

    public static void unsetSails() {
        if (!isMoving()) {
            return;
        }

        MousePackets.queueClickPacket();
        WidgetPackets.queueWidgetActionPacket(1, SAILING_CONTROLS_ID, -1, 0);
    }

    public static void setSails() {
        if (isMoving()) {
            return;
        }

        MousePackets.queueClickPacket();
        WidgetPackets.queueWidgetActionPacket(1, SAILING_CONTROLS_ID, -1, 0);
    }

    public static boolean isNavigating() {
        return client.getVarbitValue(NAVIGATING_VARBIT) != 0;
    }

    public static boolean isMoving() {
        return client.getVarbitValue(SPEED_VARBIT) != 0;
    }

    @Getter
    public enum Direction {
        SOUTH(0),
        SOUTH_SOUTH_WEST(1),
        SOUTH_WEST(2),
        WEST_SOUTH_WEST(3),
        WEST(4),
        WEST_NORTH_WEST(5),
        NORTH_WEST(6),
        NORTH_NORTH_WEST(7),
        NORTH(8),
        NORTH_NORTH_EAST(9),
        NORTH_EAST(10),
        EAST_NORTH_EAST(11),
        EAST(12),
        EAST_SOUTH_EAST(13),
        SOUTH_EAST(14),
        SOUTH_SOUTH_EAST(15);

        private final int code;

        Direction(int code) {
            this.code = code;
        }

        public static Direction fromCode(int code) {
            for (Direction d : values()) {
                if (d.code == code) {
                    return d;
                }
            }
            return null;
        }

        public static Direction fromAngle(int angle)
        {
            int code = angle / 128;
            return Direction.fromCode(code);
        }
    }

}
