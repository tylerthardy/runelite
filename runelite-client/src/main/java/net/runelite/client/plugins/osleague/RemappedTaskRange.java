package net.runelite.client.plugins.osleague;

import com.google.common.collect.ImmutableMap;

import java.util.Map;


public enum RemappedTaskRange
{
    REMAPPED_RANGE_1(0, 80, 0),
    REMAPPED_RANGE_2(81, 103, 246),
    REMAPPED_RANGE_3(104, 112, 477),
    REMAPPED_RANGE_4(113, 122, -314),
    REMAPPED_RANGE_5(123, 131, -123),
    REMAPPED_RANGE_6(132, 134, 181),
    REMAPPED_RANGE_7(135, 135, 13),
    REMAPPED_RANGE_8(136, 147, -12),
    REMAPPED_RANGE_9(148, 148, -526),
    REMAPPED_RANGE_10(149, 158, 372),
    REMAPPED_RANGE_11(159, 173, 212),
    REMAPPED_RANGE_12(174, 185, 63),
    REMAPPED_RANGE_13(186, 200, 121),
    REMAPPED_RANGE_14(201, 289, -815),
    REMAPPED_RANGE_15(290, 312, 179),
    REMAPPED_RANGE_16(313, 313, -23),
    REMAPPED_RANGE_17(314, 314, 24),
    REMAPPED_RANGE_18(315, 321, 461),
    REMAPPED_RANGE_19(322, 340, -311),
    REMAPPED_RANGE_20(341, 343, -134),
    REMAPPED_RANGE_21(344, 344, -335),
    REMAPPED_RANGE_22(345, 349, 336),
    REMAPPED_RANGE_23(350, 374, 189),
    REMAPPED_RANGE_24(375, 375, -462),
    REMAPPED_RANGE_25(376, 391, 292),
    REMAPPED_RANGE_26(392, 415, 211),
    REMAPPED_RANGE_27(416, 433, 51),
    REMAPPED_RANGE_28(434, 454, 118),
    REMAPPED_RANGE_29(455, 521, -746),
    REMAPPED_RANGE_30(522, 535, 135),
    REMAPPED_RANGE_31(536, 554, 454),
    REMAPPED_RANGE_32(555, 577, -310),
    REMAPPED_RANGE_33(578, 592, -149),
    REMAPPED_RANGE_34(593, 608, 199),
    REMAPPED_RANGE_35(609, 609, -410),
    REMAPPED_RANGE_36(610, 626, 240),
    REMAPPED_RANGE_37(627, 646, 218),
    REMAPPED_RANGE_38(647, 661, 49),
    REMAPPED_RANGE_39(662, 676, 124),
    REMAPPED_RANGE_40(677, 758, -693),
    REMAPPED_RANGE_41(759, 774, 66),
    REMAPPED_RANGE_42(775, 794, 457),
    REMAPPED_RANGE_43(795, 812, -307),
    REMAPPED_RANGE_44(813, 825, -152),
    REMAPPED_RANGE_45(826, 844, 202),
    REMAPPED_RANGE_46(845, 845, -346),
    REMAPPED_RANGE_47(846, 846, 347),
    REMAPPED_RANGE_48(847, 863, -173),
    REMAPPED_RANGE_49(864, 881, 221),
    REMAPPED_RANGE_50(882, 887, 46),
    REMAPPED_RANGE_51(888, 888, -288),
    REMAPPED_RANGE_52(889, 906, 289),
    REMAPPED_RANGE_53(907, 928, 115),
    REMAPPED_RANGE_54(929, 930, -632),
    REMAPPED_RANGE_55(931, 931, 79),
    REMAPPED_RANGE_56(932, 935, 476),
    REMAPPED_RANGE_57(936, 939, -293),
    REMAPPED_RANGE_58(940, 943, -143),
    REMAPPED_RANGE_59(944, 947, 218),
    REMAPPED_RANGE_60(948, 951, -159),
    REMAPPED_RANGE_61(952, 955, 303),
    REMAPPED_RANGE_62(956, -1, 133);


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
