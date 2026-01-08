package org.dedee.recursiveroast;

import java.util.List;

public class LSystemEngine {

    private final LSystemModel model;
    private CmdList value;
    private int recursionLevel;

    public LSystemEngine(LSystemModel model) {
        this.model = model;
        reset();
    }

    private void reset() {
        recursionLevel = 0;
        if (model.getRecursionData(0) != null)
            value = new CmdList(model.getRecursionData(0).get(0).with);
    }

    public CmdList normalize() {
        if (value == null)
            return null;

        CmdList newList = new CmdList(value.length());

        for (int i = 0; i < value.length(); i++) {
            int cmd = value.get(i);
            int id = Cmd.getId(cmd);
            if (id >= Commands.ID_USERDEFINED_MIN
                    && id <= Commands.ID_USERDEFINED_MAX) {
                int[] values = Commands.getInstance().get(id).getValues();
                newList.append(values);
            } else {
                newList.append(cmd);
            }
        }

        // System.out.println("NORM => " + newList);
        return newList;
    }

    public void calculateNext() {
        if (value == null)
            return;

        recursionLevel++;

        List<CmdReplacement> recursionData = model
                .getRecursionData(recursionLevel);

        if (recursionData == null) {
            // If no rule defined for this level, use last one
            int i = recursionLevel - 1;
            while (recursionData == null && i >= 0) {
                recursionData = model.getRecursionData(i--);
            }
        }

        value = value.replace(recursionData);
        // System.err.println("=FREEMEM=> "+Runtime.getRuntime().freeMemory()/1000+" kB");
    }

    public void calculatePrevious() {
        int level = recursionLevel;
        reset();
        for (int i = 0; i < level - 1; i++)
            calculateNext();
    }

    public int getRecursionLevel() {
        return recursionLevel;
    }

}
