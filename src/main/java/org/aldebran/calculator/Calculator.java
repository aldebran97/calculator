package org.aldebran.calculator;

import java.util.*;

public class Calculator {
    enum Signal {
        SIN,
        COS,
        TAN,
        ARC_TAN,
        ARC_COS,
        ARC_SIN,
        LOG,
        LN,
        LG,
        POW,
        EX,
        LEFT,
        RIGHT,
        ADD,
        SUB,
        MUL,
        DIV
    }

    static Map<Signal, Integer> signalIntegerMap;

    static {
        signalIntegerMap = new HashMap<>();
        signalIntegerMap.put(Signal.ADD, 2);
        signalIntegerMap.put(Signal.SUB, 2);
        signalIntegerMap.put(Signal.MUL, 3);
        signalIntegerMap.put(Signal.DIV, 3);
    }

    public static boolean isFunc(Signal signal) {
        if (signal == Signal.SIN ||
                signal == Signal.COS ||
                signal == Signal.TAN ||
                signal == Signal.ARC_SIN ||
                signal == Signal.ARC_COS ||
                signal == Signal.ARC_TAN ||
                signal == Signal.POW ||
                signal == Signal.LOG ||
                signal == Signal.EX ||
                signal == Signal.LN ||
                signal == Signal.LG) {
            return true;
        } else {
            return false;
        }
    }


    public static List preprocess(String str) {
        str = str.toUpperCase();
        str = str.replaceAll("\\s+", "");
        char[] chars = str.toCharArray();
        StringBuilder numsb = new StringBuilder();
        StringBuilder funsb = new StringBuilder();
        List list = new ArrayList();
        for (char c : chars) {
            if (c >= '0' && c <= '9' || c == '.') {
                numsb.append(c);
                continue;
            }
            if (c >= 'A' && c <= 'Z') {
                funsb.append(c);
                continue;
            }
            if (!numsb.isEmpty()) {
                list.add(Double.parseDouble(numsb.toString()));
                numsb = new StringBuilder();
            }
            if (!funsb.isEmpty()) {
                String s = funsb.toString();
                funsb = new StringBuilder();
                Signal signal = Signal.valueOf(s);
                list.add(signal);
            }
            switch (c) {
                case '+' -> list.add(Signal.ADD);
                case '-' -> list.add(Signal.SUB);
                case '*' -> list.add(Signal.MUL);
                case '/' -> list.add(Signal.DIV);
                case '(' -> list.add(Signal.LEFT);
                case ')' -> list.add(Signal.RIGHT);
                default -> throw new RuntimeException("unknown char: " + c);
            }
        }
        if (!numsb.isEmpty()) {
            list.add(Double.parseDouble(numsb.toString()));
            numsb = new StringBuilder();
        }
        if (!funsb.isEmpty()) {
            String s = funsb.toString();
            funsb = new StringBuilder();
            Signal signal = Signal.valueOf(s);
            list.add(signal);
        }
        return list;
    }


    public static List postPrefix(String str) {
        List list = preprocess(str);
        List result = new ArrayList();
        Stack<Signal> stack = new Stack<>();
        for (Object obj : list) {
            if (obj instanceof Double v) {
                result.add(v);
                continue;
            }
            Signal signal = (Signal) obj;
            if (isFunc(signal) || signal == Signal.LEFT) {
                stack.push(signal);
                continue;
            }
            if (signal == Signal.RIGHT) {
                while (!stack.isEmpty()) {
                    Signal pop = stack.pop();
                    if (pop != Signal.LEFT) {
                        result.add(pop);
                    } else {
                        break;
                    }
                }
                if (!stack.isEmpty() && isFunc(stack.peek())) {
                    result.add(stack.pop());
                }
            } else {
                Signal top = stack.isEmpty() ? null : stack.peek();
                if (top == null || isFunc(top) || top == Signal.LEFT) {
                    stack.push(signal);
                    continue;
                }
                while (!stack.isEmpty()) {
                    top = stack.peek();
                    if (signalIntegerMap.get(top) >= signalIntegerMap.get(signal)) {
                        result.add(stack.pop());
                    } else {
                        break;
                    }
                }
                stack.push(signal);
            }
        }
        while (!stack.isEmpty()) {
            result.add(stack.pop());
        }
        return result;

    }


    public static double calc(String str) {
        List postPrefix = postPrefix(str);
        Stack<Double> stack = new Stack<>();
        for (Object obj : postPrefix) {
            if (obj instanceof Double v) {
                stack.push(v);
                continue;
            }
            Signal signal = (Signal) obj;
            switch (signal) {
                case ADD -> {
                    Double v2 = stack.pop();
                    Double v1 = stack.pop();
                    stack.push(v1 + v2);
                }
                case SUB -> {
                    Double v2 = stack.pop();
                    Double v1 = stack.pop();
                    stack.push(v1 - v2);
                }
                case MUL -> {
                    Double v2 = stack.pop();
                    Double v1 = stack.pop();
                    stack.push(v1 * v2);
                }
                case DIV -> {
                    Double v2 = stack.pop();
                    Double v1 = stack.pop();
                    stack.push(v1 / v2);
                }
                case SIN -> {
                    Double v1 = stack.pop();
                    stack.push(Math.sin(v1));
                }
                case COS -> {
                    Double v1 = stack.pop();
                    stack.push(Math.cos(v1));
                }
                case TAN -> {
                    Double v1 = stack.pop();
                    stack.push(Math.tan(v1));
                }
                case ARC_SIN -> {
                    Double v1 = stack.pop();
                    stack.push(Math.asin(v1));
                }
                case ARC_COS -> {
                    Double v1 = stack.pop();
                    stack.push(Math.acos(v1));
                }
                case ARC_TAN -> {
                    Double v1 = stack.pop();
                    stack.push(Math.atan(v1));
                }
                case POW -> {
                    Double v2 = stack.pop();
                    Double v1 = stack.pop();
                    stack.push(Math.pow(v1, v2));
                }
                case LOG -> {
                    Double v2 = stack.pop();
                    Double v1 = stack.pop();
                    stack.push(Math.log(v2) / Math.log(v1));
                }
                case EX -> {
                    Double v1 = stack.pop();
                    stack.push(Math.pow(Math.E, v1));
                }
                case LN -> {
                    Double v1 = stack.pop();
                    stack.push(Math.log(Math.log(v1)));
                }
                case LG -> {
                    Double v1 = stack.pop();
                    stack.push(Math.log(Math.log10(v1)));
                }
                default -> throw new RuntimeException("illegal Signal! " + signal);
            }
        }
        return stack.pop();
    }
}
