package com.onehao.acts.testgen.service.extension;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import com.onehao.acts.testgen.common.SUT;

import edu.uta.cse.fireeye.common.Parameter;

public class Utils {

    public static String repeatStr(String src, int count) {
        if ((null == src) || src.isEmpty() || (1 == count)) {
            return src;
        }

        if (count <= 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder(src.length() * count);

        for (int i = 0; i < count; i++) {
            sb.append(src);
        }

        return sb.toString();
    }

    public static void addUniqValToParameter(Parameter param, String val) {
        if (!param.getValues().contains(val)) {
            param.addValue(val);
        }
    }

    public static void addUniqValToCollection(Collection<String> list, String val) {
        if (list instanceof Set) {
            list.add(val);
        } else {
            if (!list.contains(val)) {
                list.add(val);
            }
        }
    }

    public static long ipv4ToLong(String ipAddr) {
        try {
            InetAddress address = InetAddress.getByName(ipAddr);
            return (new BigInteger(1, address.getAddress())).longValue();
        } catch (UnknownHostException e) {
            return -1;
        }
    }

    public static String longToIPv4(Long ip) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.SIZE / 8);
        buffer.putInt(ip.intValue());
        try {
            InetAddress addr = InetAddress.getByAddress(buffer.array());
            return addr.getHostAddress();
        } catch (UnknownHostException e) {
            return null;
        }
    }

    public static Map<String, Collection<String>> newParameterValuesMap(SUT sut) {
        Map<String, Collection<String>> ret = new HashMap<String, Collection<String>>();
        if ((sut != null) && (sut.getParameters() != null)) {
            for (Parameter param : sut.getParameters()) {
                ret.put(param.getName(), new HashSet<String>());
            }
        }

        return ret;
    }

    public static <T> T as(Class<T> classType, Object obj) {
        if (classType.isInstance(obj)) {
            return classType.cast(obj);
        }

        return null;
    }

    public static Map<String, Integer> recordParamDomainSize(SUT sut) {
        Map<String, Integer> ret = new HashMap<String, Integer>();
        if ((sut != null) && (sut.getParameters() != null) && !sut.getParameters().isEmpty()) {
            for (Parameter param : sut.getParameters()) {
                ret.put(param.getName(), param.getDomainSize());
            }
        }

        return ret;
    }

    public static int getRandomInt(int n) {
        return getThreadLocalRandom().nextInt(n);
    }

    public static ThreadLocalRandom getThreadLocalRandom() {
        return ThreadLocalRandom.current();
    }

    public static String genRandomString() {
        int len = getRandomInt(23) + 1;
        List<Integer> asciiBase = Arrays.asList(65, 97);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int randChar = getRandomInt(26);
            sb.append(Character.toChars(randChar + asciiBase.get(getRandomInt(asciiBase.size()))));
        }

        return sb.toString();
    }
    
    public static BitSet convertIntToBitSet(int value) {
        BitSet bits = new BitSet();
        int index = 0;
        while (value != 0) {
            if ((value % 2) != 0) {
                bits.set(index);
            }
            ++index;
            value = value >>> 1;
        }
        return bits;
    }

    public static int convertBitSetToInt(BitSet bitSet) {
        int value = 0;
        for (int i = 0; i < bitSet.length(); ++i) {
            value += bitSet.get(i) ? (1 << i) : 0L;
        }
        return value;
    }

}
