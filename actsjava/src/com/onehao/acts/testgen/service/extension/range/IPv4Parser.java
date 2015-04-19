package com.onehao.acts.testgen.service.extension.range;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPv4Parser implements ITypeParser {

    public static final String IPV4_EXT_PATTERN =
            "((^?\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b)([\\|\\$;:,]|%7C|%24|%3B|%3A|%2C|)?)+";
    public static final String IPV4_EXT_SINGLE_PATTERN =
            "((^?\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b)([\\|\\$;:,]|%7C|%24|%3B|%3A|%2C|)?)";
    public static final String IPV4_SINGLE_PATTERN =
            "\\b(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\."
            + "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\."
            + "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\."
            + "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b";

    protected Pattern fullPattern = null;
    protected Pattern extSinglePattern = null;
    protected Pattern singlePattern = null;

    protected int maxCombineCount = 0;
    protected String splitter = null;

    protected boolean isMatchSingleOnly = false;

    public IPv4Parser() {
        this.init();
    }

    public IPv4Parser(boolean matchSingleOnly) {
        this.isMatchSingleOnly = matchSingleOnly;
        this.init();
    }

    protected void init() {
        fullPattern = Pattern.compile(IPV4_EXT_PATTERN);
        extSinglePattern = Pattern.compile(IPV4_EXT_SINGLE_PATTERN);
        singlePattern = Pattern.compile(IPV4_SINGLE_PATTERN);
    }

    @Override
    public boolean parse(Collection<String> valList) {
        if (null == valList) {
            return false;
        }

        this.reset();

        for (String item : valList) {
            if (!this.verify(item)) {
                return false;
            }
        }

        return true;
    }

    protected void reset() {
        this.maxCombineCount = 0;
        this.splitter = null;
    }

    protected boolean verify(String ipv4) {

        if (this.isMatchSingleOnly) {
            return this.singlePattern.matcher(ipv4).matches();
        }

        boolean ret = this.fullPattern.matcher(ipv4).matches();

        Matcher matcher = this.extSinglePattern.matcher(ipv4);
        int matchCount = 0;
        while (matcher.find()) {
            matchCount++;
            if (matcher.groupCount() < 3) {
                continue;
            }

            // String ip = matcher.group(2);
            String token = matcher.group(3);
            if ((token != null) && !token.isEmpty()) {
                this.splitter = token;
            }
        }

        if (this.maxCombineCount < matchCount) {
            this.maxCombineCount = matchCount;
        }

        return ret;
    }

}
