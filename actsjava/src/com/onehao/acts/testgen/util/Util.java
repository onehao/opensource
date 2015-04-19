package com.onehao.acts.testgen.util;

import java.util.ArrayList;
import java.util.Iterator;

import com.onehao.acts.testgen.common.SUT;

import edu.uta.cse.fireeye.common.Parameter;
import edu.uta.cse.fireeye.data.SUTData;

/**
 * 
 * @author wanhao01 refactor from FireEye2.8
 */

public class Util {
    public static final String EQUAL = "==";
    public static final String NOT_EQUAL = "!=";
    public static final String LESS_THAN = "<";
    public static final String LESS_THAN_OR_EQUAL = "<=";
    public static final String GREATER_THAN_OR_EQUAL = ">=";
    public static final String GREATER_THAN = ">";
    public static final String EMPTY = "";
    public static final String SPACE = " ";

    public static void abort(String errMsg) {
        System.err.println(errMsg);
        //System.exit(1);
    }

    public static int getMaxPrime(int value) {
        int rval = 0;
        int[] primes = { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31 };
        for (int i = primes.length - 1; i > 0; i--) {
            if (value >= primes[i]) {
                rval = primes[i];
                break;
            }
        }
        return rval;
    }

    public static boolean isPrime(int value) {
        boolean rval = false;
        int[] primes = { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 51 };
        for (int i = 0; i < primes.length; i++) {
            if (value == primes[i]) {
                rval = true;
                break;
            }
        }
        return rval;
    }

    public static ArrayList<Parameter> orderParams(ArrayList<Parameter> unordered) {
        ArrayList<Parameter> rval = new ArrayList<Parameter>();
        for (Parameter aParam : unordered) {
            int i = 0;
            for (; i < rval.size(); i++) {
                Parameter bParam = (Parameter) rval.get(i);
                if (aParam.getDomainSize() > bParam.getDomainSize()) {
                    break;
                }
            }
            rval.add(i, aParam);
        }
        return rval;
    }

    public static void dump(ArrayList<int[]> matrix) {
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < ((int[]) matrix.get(i)).length; j++) {
                System.out.print(((int[]) matrix.get(i))[j] + " ");
            }
            System.out.println();
        }
    }

    public static SUT convertToSUT(SUTData sutData) {
        SUT sut = new SUT();
        sut.setName(sutData.getSystemName());
        ArrayList<?> parameters = sutData.getParameters();
        ArrayList<Parameter> parametersToSet = new ArrayList<Parameter>();
        for (Iterator<?> paramsIter = parameters.iterator(); paramsIter.hasNext();) {
            Parameter parameter = (Parameter) paramsIter.next();
            if (parameter.getInputOrOutput() == 0) {
                parametersToSet.add(parameter);
            }
        }
        sut.setParameters(parametersToSet);
        sut.setOutputParameters(sutData.getOutputParameters());

        ArrayList constraints = sutData.getConstraints();
        if ((constraints != null) && (!constraints.isEmpty())) {
            sut.getConstraintManager().setConstraints(constraints);
        }
        ArrayList relations = sutData.getRelations();
        if ((relations != null) && (!relations.isEmpty())) {
            sut.getRelationManager().setRelations(relations);
        }
        return sut;
    }

    public static String getRealSystemName(String displayName) {
        int index = displayName.indexOf("[SYSTEM-");
        int startIndex = displayName.indexOf("-");
        int lastIndex = displayName.lastIndexOf("]");
        String realName = "";
        if ((index != -1) && (startIndex != -1) && (lastIndex != -1)) {
            realName = displayName.substring(startIndex + 1, lastIndex);
        }
        return realName.trim();
    }

    public static void dump(int[] test) {
        for (int j = 0; j < test.length; j++) {
            System.out.print(test[j] + " ");
        }
        System.out.println();
    }

    public static String[] splitParamTableValues(String string) {
        StringBuffer buff = new StringBuffer(string);
        buff.deleteCharAt(string.indexOf("["));
        buff.deleteCharAt(string.lastIndexOf("]") - 1);
        String newStr = buff.toString();
        if (newStr.contains("\\,")) {
            newStr = newStr.replace("\\,", "|");
        }
        String[] res = newStr.split(",");
        return res;
    }

    public static String getDelimiterSeparatedString(ArrayList<?> values, String delimiter) {
        if (values == null) {
            return "";
        }
        if ((delimiter == null) || (delimiter.isEmpty())) {
            delimiter = ",";
        }
        StringBuffer buffer = new StringBuffer();
        int size = values.size();

        int commaCnt = size - 1;
        for (int i = 0; i < size; i++) {
            Parameter p = (Parameter) values.get(i);
            buffer.append(p.getName());
            if (i < commaCnt) {
                buffer.append(",");
            }
        }
        return buffer.toString();
    }

    public static boolean isNodeARelation(String relName) {
        StringBuffer name = new StringBuffer(relName);
        int index = name.indexOf("Relation(");
        int endInd = name.lastIndexOf(")");
        if ((index != -1) && (endInd != -1)) {
            return true;
        }
        return false;
    }

    public static String getRelationStrength(String relName) {
        System.out.println("relname is--->" + relName);

        int ind = relName.indexOf("(");
        int end = relName.lastIndexOf(")");

        return relName.substring(ind + 1, end);
    }

    public static String getRelationStrengthFromString(String relName) {

        // TODO: onehao, why using charAt(1)?
        return String.valueOf(relName.charAt(1));
    }

    public static String[] getParamsArray(String relString) {
        int ind = relString.indexOf("(");
        int end = relString.lastIndexOf(")");
        String[] params = relString.substring(ind + 1, end).split(",");
        return params;
    }

    public static String formatMsg(String rawMsg) {
        if (rawMsg == null) {
            return rawMsg;
        }
        StringBuffer buf = new StringBuffer();
        int c = 0;
        for (int i = 0; i < rawMsg.length(); i++) {
            buf.append(rawMsg.charAt(i));
            c++;
            if (c >= 50) {
                buf.append("\n");
                c = 0;
            }
        }
        return buf.toString();
    }

    public static boolean isBlank(String str) {
        int strLen;
        if ((str == null) || ((strLen = str.length()) == 0)) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static int indexOfDifference(String str1, String str2) {
        if (str1.equals(str2)) {
            return -1;
        }
        if ((str1 == null) || (str2 == null)) {
            return 0;
        }
        int i = 0;
        for (; (i < str1.length()) && (i < str2.length()); i++) {
            if (str1.charAt(i) != str2.charAt(i)) {
                break;
            }
        }
        if ((i < str2.length()) || (i < str1.length())) {
            return i;
        }
        return -1;
    }

    public static String difference(String str1, String str2) {
        if (str1 == null) {
            return str2;
        }
        if (str2 == null) {
            return str1;
        }
        int at = indexOfDifference(str1, str2);
        if (at == -1) {
            return "";
        }
        return str2.substring(at);
    }

    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String join(Object[] array, char separator) {
        if (array == null) {
            return null;
        }
        int arraySize = array.length;
        int bufSize = arraySize == 0 ? 0 : ((array[0] == null ? 16 : array[0].toString().length()) + 1) * arraySize;
        StringBuffer buf = new StringBuffer(bufSize);
        for (int i = 0; i < arraySize; i++) {
            if (i > 0) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }

    public static String join(Object[] array) {
        return join(array, null);
    }

    public static String join(Object[] array, String separator) {
        if (array == null) {
            return null;
        }
        if (separator == null) {
            separator = "";
        }
        int arraySize = array.length;

        int bufSize =
                arraySize == 0 ? 0 : arraySize
                        * ((array[0] == null ? 16 : array[0].toString().length()) + separator.length());

        StringBuffer buf = new StringBuffer(bufSize);
        for (int i = 0; i < arraySize; i++) {
            if (i > 0) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }

    public static String join(Iterator<?> iterator, char separator) {
        if (iterator == null) {
            return null;
        }
        StringBuffer buf = new StringBuffer(256);
        while (iterator.hasNext()) {
            Object obj = iterator.next();
            if (obj != null) {
                buf.append(obj);
            }
            if (iterator.hasNext()) {
                buf.append(separator);
            }
        }
        return buf.toString();
    }

    public static String join(Iterator<?> iterator, String separator) {
        if (iterator == null) {
            return null;
        }
        StringBuffer buf = new StringBuffer(256);
        while (iterator.hasNext()) {
            Object obj = iterator.next();
            if (obj != null) {
                buf.append(obj);
            }
            if ((separator != null) && (iterator.hasNext())) {
                buf.append(separator);
            }
        }
        return buf.toString();
    }

    public static String formatToolBarDisplay(String text, int size) {
        if (text.equals("-1")) {
            text = "Mixed";
        }
        int textSize = text.length();
        int rem = size - textSize;
        if (rem > 0) {
            int newSize = rem / 2;
            for (int i = 0; i < newSize; i++) {
                text = text + " ";
            }
            text = " " + text;
        }
        return text;
    }

    public static int[] cloneTest(int[] test) {
        int[] rval = new int[test.length];
        for (int i = 0; i < test.length; i++) {
            rval[i] = test[i];
        }
        return rval;
    }
}
