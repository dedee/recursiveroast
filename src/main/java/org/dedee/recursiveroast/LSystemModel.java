package org.dedee.recursiveroast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents an L-System model with rules, constants, and configuration.
 * Parses L-System definitions from text and provides access to the system parameters.
 */
public class LSystemModel {
    private static final Logger logger = LoggerFactory.getLogger(LSystemModel.class);

    private final Commands commands;
    private final List<List<CmdReplacement>> recursions;
    private final List<Cmd> constants;
    private int recursionDepth;
    private double initialAngle;
    private double angle;

    public LSystemModel(Commands commands) {
        this.commands = commands;
        recursions = new ArrayList<>();
        constants = new ArrayList<>();
    }

    public Commands getCommands() {
        return commands;
    }

    public int getRecursionDepth() {
        return recursionDepth;
    }

    public double getAngle() {
        return angle;
    }

    public List<CmdReplacement> getRecursionData(int recursionLevel) {
        if (recursionLevel >= recursions.size())
            return null;
        List<CmdReplacement> data = recursions.get(recursionLevel);
        return data != null ? Collections.unmodifiableList(data) : null;
    }

    public void load(@NotNull BufferedReader in) throws IOException {
        String line;

        while ((line = in.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty())
                continue;
            if (line.startsWith("/") || line.startsWith(";")
                    || line.startsWith("#"))
                continue;

            if (line.contains(":")) {
                parseRuleLine(line);
            } else if (line.contains("=")) {
                parseConstantLine(line);
            } else {
                throw new IOException("Illegal line: " + line);
            }
        }
    }

    private void parseConstantLine(String line) throws IOException {
        String[] s = line.split("=");
        String name = s[0];
        String value = s[1];
        addConstant(name, value);
    }

    private void parseRuleLine(String line) throws IOException {
        String[] s = line.split(":");
        String recursionDepth = s[0].trim();
        String rule = s[1].trim();
        addRule(Integer.parseInt(recursionDepth), rule);
    }

    private void addConstant(String name, String value) throws IOException {
        name = name.trim();
        value = value.trim();
        switch (name) {
            case "rektiefe", "recursion" -> {
                recursionDepth = Integer.parseInt(value);
                logger.debug("Recursion depth: {}", recursionDepth);
            }
            case "winkel", "angle" -> {
                angle = Double.parseDouble(value);
                logger.debug("Angle: {}", angle);
            }
            case "initialangle" -> {
                initialAngle = Double.parseDouble(value);
                logger.debug("Initial angle: {}", initialAngle);
            }
            default -> {
                logger.debug("User-defined constant {}: {}", name, value);
                if (name.length() > 1)
                    throw new IOException("name '" + name + "' is too long");
                constants.add(commands.createUserDefinedCommand(
                        name.charAt(0), value));
            }
        }
    }

    private void addRule(int recursionDepth, String rule) throws IOException {
        // 1: lf = rf+lf+rf
        String[] s = rule.split("=");
        String name = s[0].trim();
        String value = s[1].trim().replace(" ", "");

        if (name.contains("BASE"))
            name = name.replace("BASE", "B");

        if (name.length() > 1)
            throw new IOException("name '" + name + "' is too long");

        addRule(recursionDepth, name.charAt(0), value);
    }

    private void addRule(int recursionDepth, char name, String value)
            throws IOException {
        logger.debug("Rule {} : {} = {}", recursionDepth, name, value);

        int[] with = new int[value.length()];
        for (int i = 0; i < value.length(); i++) {

            char ch = value.charAt(i);
            Cmd cmd = commands.get(ch);
            if (cmd == null)
                throw new IOException("cmd '" + ch + "' is not defined");

            with[i] = commands.get(value.charAt(i)).getId();
        }

        if (commands.get(name) == null)
            throw new IOException("command with name '" + name
                    + "' not defined?");

        CmdReplacement cpr = new CmdReplacement(commands
                .get(name).getId(), with);

        List<CmdReplacement> listOfRecursion = null;
        if (recursionDepth < recursions.size())
            listOfRecursion = recursions.get(recursionDepth);

        if (listOfRecursion == null) {
            listOfRecursion = new ArrayList<>();
            recursions.add(recursionDepth, listOfRecursion);
        }

        listOfRecursion.add(cpr);
    }

    public String getLSystemAsString() {
        StringBuilder sb = new StringBuilder();
        sb.append("angle = ").append(getAngle()).append("\n");
        sb.append("initialangle = ").append(getInitialAngle()).append("\n");
        sb.append("recursion = ").append(getRecursionDepth()).append("\n");

        for (Cmd cmd : constants) {
            sb.append(commands.idToChar(cmd.getId()));
            sb.append(" = ");
            for (int what : cmd.getValues())
                sb.append(commands.idToChar(what));
            sb.append("\n");
        }

        for (int i = 0; i < recursions.size(); i++) {
            List<CmdReplacement> list = recursions.get(i);
            if (list != null) {
                for (CmdReplacement cpr : list) {
                    sb.append(i).append(": ").append(commands.idToChar(cpr.getWhat()));
                    sb.append(" = ");
                    int[] with = cpr.getWith();
                    if (with != null) {
                        for (int j = 0; j < with.length; j++)
                            sb.append(commands.idToChar(
                                    with[j]));
                    }
                    sb.append("\n");
                }
            }
        }

        return sb.toString();
    }


    public double getInitialAngle() {
        return initialAngle;
    }

}
