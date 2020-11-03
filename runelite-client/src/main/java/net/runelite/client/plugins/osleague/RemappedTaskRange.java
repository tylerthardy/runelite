package net.runelite.client.plugins.osleague;

import com.google.common.collect.ImmutableMap;

import java.util.Map;


public enum RemappedTaskRange
{
    REMAPPED_RANGE_0(0, 80, 0),
    REMAPPED_RANGE_1(81, 103, 244),
    REMAPPED_RANGE_2(104, 112, 720),
    REMAPPED_RANGE_3(113, 122, 405),
    REMAPPED_RANGE_4(123, 131, 281),
    REMAPPED_RANGE_5(132, 134, 461),
    REMAPPED_RANGE_6(135, 135, 473),
    REMAPPED_RANGE_7(136, 147, 460),
    REMAPPED_RANGE_8(148, 148, -66),
    REMAPPED_RANGE_9(149, 158, 304),
    REMAPPED_RANGE_10(159, 173, 515),
    REMAPPED_RANGE_11(174, 185, 577),
    REMAPPED_RANGE_12(186, 200, 697),
    REMAPPED_RANGE_13(201, 289, -118),
    REMAPPED_RANGE_14(290, 312, 59),
    REMAPPED_RANGE_15(313, 313, 35),
    REMAPPED_RANGE_16(314, 314, 58),
    REMAPPED_RANGE_17(315, 321, 518),
    REMAPPED_RANGE_18(322, 341, 206),
    REMAPPED_RANGE_19(342, 344, 71),
    REMAPPED_RANGE_20(345, 345, -264),
    REMAPPED_RANGE_21(346, 350, 70),
    REMAPPED_RANGE_22(351, 375, 258),
    REMAPPED_RANGE_23(376, 376, -204),
    REMAPPED_RANGE_24(377, 392, 86),
    REMAPPED_RANGE_25(393, 416, 296),
    REMAPPED_RANGE_26(417, 434, 346),
    REMAPPED_RANGE_27(435, 455, 463),
    REMAPPED_RANGE_28(456, 522, -283),
    REMAPPED_RANGE_29(523, 536, -150),
    REMAPPED_RANGE_30(537, 555, 303),
    REMAPPED_RANGE_31(556, 578, -8),
    REMAPPED_RANGE_32(579, 593, -158),
    REMAPPED_RANGE_33(594, 609, 40),
    REMAPPED_RANGE_34(610, 610, -370),
    REMAPPED_RANGE_35(611, 627, -132),
    REMAPPED_RANGE_36(628, 647, 85),
    REMAPPED_RANGE_37(648, 662, 133),
    REMAPPED_RANGE_38(663, 677, 256),
    REMAPPED_RANGE_39(678, 759, -437),
    REMAPPED_RANGE_40(760, 775, -373),
    REMAPPED_RANGE_41(776, 795, 83),
    REMAPPED_RANGE_42(796, 813, -225),
    REMAPPED_RANGE_43(814, 826, -378),
    REMAPPED_RANGE_44(827, 845, -177),
    REMAPPED_RANGE_45(846, 846, 114),
    REMAPPED_RANGE_46(847, 847, -178),
    REMAPPED_RANGE_47(848, 864, -352),
    REMAPPED_RANGE_48(865, 882, -132),
    REMAPPED_RANGE_49(883, 888, -87),
    REMAPPED_RANGE_50(889, 889, -376),
    REMAPPED_RANGE_51(890, 907, -88),
    REMAPPED_RANGE_52(908, 929, 26),
    REMAPPED_RANGE_53(930, 931, -607),
    REMAPPED_RANGE_54(932, 932, -529),
    REMAPPED_RANGE_55(933, 936, -54),
    REMAPPED_RANGE_56(937, 940, -348),
    REMAPPED_RANGE_57(941, 944, -492),
    REMAPPED_RANGE_58(945, 948, -275),
    REMAPPED_RANGE_59(949, 952, -435),
    REMAPPED_RANGE_60(953, 956, -133),
    REMAPPED_RANGE_61(957, 957, -1),
    REMAPPED_RANGE_62(958, 1000, -1);

    private final int beginId;
    private final int endId;
    private final int offset;

    RemappedTaskRange(int beginId, int endId, int offset)
    {
        this.beginId = beginId;
        this.endId = endId;
        this.offset = offset;
    }

    private static final Map<Integer, Integer> OFFSETS;

    static
    {
        ImmutableMap.Builder<Integer, Integer> builder = new ImmutableMap.Builder<>();

        for (RemappedTaskRange remappedTaskRange : values())
        {
            for (int id = remappedTaskRange.beginId; id <= remappedTaskRange.endId; id++)
            {
                builder.put(id, remappedTaskRange.offset);
            }
        }

        OFFSETS = builder.build();
    }

    static Integer getOffset(int id)
    {
        return OFFSETS.get(id);
    }
}
