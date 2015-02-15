package com.onehao.acts.testgen.common;

import edu.uta.cse.fireeye.util.Util;

/**
 * 
 * @author wanhao01 refactor from FireEye2.8
 */

public class TestGenProfile {
    private String combine = "default";
    public static final String PN_DOI = "doi";
    public static final String PN_MODE = "mode";
    public static final String PV_SCRATCH = "scratch";
    public static final String PV_EXTEND = "extend";
    public static final String PN_ALGORITHM = "algo";
    public static final String PV_IPOG = "ipog";
    public static final String PV_IPOG_D = "ipog_d";
    public static final String PV_IPOG_R = "ipog_r";
    public static final String PV_BUSH = "bush";
    public static final String PV_REC = "rec";
    public static final String PV_PAINTBALL = "paintball";
    public static final String PV_IPOF = "ipof";
    public static final String PV_IPOF2 = "ipof2";
    public static final String PV_BASECHOICE = "basechoice";
    public static final String PN_PROGRESS = "progress";
    public static final String ON = "on";
    public static final String OFF = "off";
    public static final String PN_HUNIT = "hunit";
    public static final short DEFAULT_HUNIT = 50;
    public static final String PN_VUNIT = "vunit";
    public static final short DEFAULT_VUNIT = 1000;
    public static final String PN_CHECK = "check";
    public static final String PN_FASTMODE = "fastMode";
    public static final String PN_DEBUG = "debug";
    public static final String PN_OUTPUTFORMAT = "output";
    public static final String PV_NUMERIC = "numeric";
    public static final String PV_NIST = "nist";
    public static final String PV_CSV = "csv";
    public static final String PV_EXCEL = "excel";
    public static final String DEFAULT_PAINTBALL_MAX_TRIES = "100";
    public static final String ALL_COMBINATION = "all";
    public static final String PN_RANDOM = "random";
    public static final String PN_RANDSTAR = "randstar";
    public static final String PN_TIEBREAK = "tiebreak";
    public static final String PV_RANDOM = "random";
    public static final String PV_BUILTIN = "builtin";

    public static enum Mode {
        scratch, extend;
    }

    public static enum ConstraintMode {
        no, solver, tuples;
    }

    public static enum Algorithm {
        ipog, ipog_d, bush, rec, paintball, ipof, ipof2, basechoice, ipog_r, ipog_hybrid;
    }

    public static enum OutputFormat {
        numeric, nist, csv, excel;
    }

    public static enum TieBreaker {
        builtin, random;
    }

    private int opt_level = -1;
    private int doi;
    private Mode mode;
    private ConstraintMode constraintMode;
    private OutputFormat output;
    private Algorithm algorithm;
    private boolean progress;
    private boolean ignoreConstraints;
    private short hunit;
    private short vunit;
    private boolean check;
    private boolean fastMode = false;
    private boolean debug;
    private boolean random;
    private boolean randstar = true;
    public int maxTries = 100;
    private TieBreaker tiebreaker;
    private static TestGenProfile profile = null;
    public static int DEFAULT_DOI = 2;

    public static TestGenProfile instance() {
        if (profile == null) {
            profile = new TestGenProfile();
            profile.setDOI(2);
            profile.setAlgorithm("ipog");
            profile.setVUnit("1000");
            profile.setHUnit("50");
            profile.setProgress("Off");
            profile.setMode("scratch");
        }
        return profile;
    }

    public int getDOI() {
        return this.doi;
    }

    public void setOptLevel(int level) {
        this.opt_level = level;
    }

    public int getOptLevel() {
        return this.opt_level;
    }

    public void setDOI(int doi) {
        this.doi = doi;
    }

    public void setCombine(String combine) {
        this.combine = combine;
    }

    public String getCombine() {
        return this.combine;
    }

    public Mode getMode() {
        return this.mode;
    }

    public ConstraintMode getConstraintMode() {
        return this.constraintMode;
    }

    public boolean isIgnoreConstraints() {
        return this.ignoreConstraints;
    }

    public boolean isProgressOn() {
        return this.progress;
    }

    public boolean checkCoverage() {
        return this.check;
    }

    public boolean fastMode() {
        return isFastMode();
    }

    public boolean debug() {
        return this.debug;
    }

    public boolean random() {
        return this.random;
    }

    public boolean randstar() {
        return this.randstar;
    }

    public OutputFormat getOutputFormat() {
        return this.output;
    }

    public short getHUnit() {
        return this.hunit;
    }

    public short getVUnit() {
        return this.vunit;
    }

    public Algorithm getAlgorithm() {
        return this.algorithm;
    }

    public String getAlgorithmStr() {
        String rval = null;
        if (this.algorithm == Algorithm.ipog) {
            rval = "ipog";
        } else if (this.algorithm == Algorithm.ipog_d) {
            rval = "ipog_d";
        } else if (this.algorithm == Algorithm.bush) {
            rval = "bush";
        } else if (this.algorithm == Algorithm.rec) {
            rval = "rec";
        } else if (this.algorithm == Algorithm.paintball) {
            rval = "paintball";
        } else if (this.algorithm == Algorithm.ipof) {
            rval = "ipof";
        } else if (this.algorithm == Algorithm.ipof2) {
            rval = "ipof2";
        } else if (this.algorithm == Algorithm.basechoice) {
            rval = "basechoice";
        }
        return rval;
    }

    public void setMode(String modeIn) {
        if (modeIn == null) {
            this.mode = Mode.scratch;
        } else if (modeIn.equals("scratch")) {
            this.mode = Mode.scratch;
        } else if (modeIn.equals("extend")) {
            this.mode = Mode.extend;
        } else {
            System.out.println("Invalid test generation mode!");
            System.exit(1);
        }
    }

    public void setAlgorithm(String algo) {
        String prop = algo;
        if (prop == null) {
            this.algorithm = Algorithm.ipog;
        } else if (prop.equals("ipog")) {
            this.algorithm = Algorithm.ipog;
        } else if (prop.equals("ipog_d")) {
            this.algorithm = Algorithm.ipog_d;
        } else if (prop.equals("bush")) {
            this.algorithm = Algorithm.bush;
        } else if (prop.equals("rec")) {
            this.algorithm = Algorithm.rec;
        } else if (prop.equals("paintball")) {
            this.algorithm = Algorithm.paintball;
        } else if (prop.equals("ipof")) {
            this.algorithm = Algorithm.ipof;
        } else if (prop.equals("ipof2")) {
            this.algorithm = Algorithm.ipof2;
        } else if (prop.equals("basechoice")) {
            this.algorithm = Algorithm.basechoice;
        } else if (prop.equals("ipog_r")) {
            this.algorithm = Algorithm.ipog_r;
        } else {
            System.out.println("prp value is " + prop);
            System.out.println("Invalid test generation algorithm!");
            System.exit(1);
        }
    }

    public void setProgress(String prog) {
        String prop = prog;
        if (prop == null) {
            this.progress = true;
        } else if (prop.equalsIgnoreCase("on")) {
            this.progress = true;
        } else if (prop.equalsIgnoreCase("off")) {
            this.progress = false;
        } else {
            Util.abort("Invalid progress option!");
        }
    }

    public void setFastMode(String fastMod) {
        String prop = fastMod;
        if (prop == null) {
            this.fastMode = false;
        } else if (prop.equalsIgnoreCase("on")) {
            this.fastMode = true;
        } else if (prop.equalsIgnoreCase("off")) {
            this.fastMode = false;
        } else {
            Util.abort("Invalid fast mode option");
        }
    }

    public void setDebugMode(String inDebug) {
        String prop = inDebug;
        if (prop == null) {
            this.debug = false;
        } else if (prop.equalsIgnoreCase("on")) {
            this.debug = true;
        } else if (prop.equalsIgnoreCase("off")) {
            this.debug = false;
        } else {
            Util.abort("Invalid debug mode option");
        }
    }

    public void setDebugMode(boolean debug) {
        this.debug = debug;
    }

    public void setRandomMode(String randMode) {
        String prop = randMode;
        if (prop == null) {
            this.random = false;
        } else if (prop.equalsIgnoreCase("on")) {
            this.random = true;
        } else if (prop.equalsIgnoreCase("off")) {
            this.random = false;
        } else {
            Util.abort("Invalid random mode option");
        }
    }

    public void setRandstar(String randstar) {
        String prop = randstar;
        if (prop == null) {
            this.randstar = true;
        } else if (prop.equalsIgnoreCase("on")) {
            this.randstar = true;
        } else if (prop.equalsIgnoreCase("off")) {
            this.randstar = false;
        } else {
            Util.abort("Invalid randstar mode option");
        }
    }

    public void setRandstar(boolean randstar) {
        this.randstar = randstar;
    }

    public void setIgnoreConstraints(boolean ig) {
        this.ignoreConstraints = ig;
    }

    public void setConstraintMode(ConstraintMode m) {
        this.constraintMode = m;
    }

    public void setCheckCoverage(String cover) {
        String prop = cover;
        if (prop == null) {
            this.check = false;
        } else if (prop.equalsIgnoreCase("on")) {
            this.check = true;
        } else if (prop.equalsIgnoreCase("off")) {
            this.check = false;
        } else {
            Util.abort("Invalid check option!");
        }
    }

    public void setHUnit(String hUnit) {
        String prop = hUnit;
        if (prop == null) {
            this.hunit = 50;
        } else {
            try {
                this.hunit = Short.parseShort(prop);
            } catch (NumberFormatException ex) {
                Util.abort("hunit property must be an integer > 0.");
            }
        }
    }

    public void setVUnit(String vUnit) {
        String prop = vUnit;
        if (prop == null) {
            this.vunit = 1000;
        } else {
            try {
                this.vunit = Short.parseShort(prop);
            } catch (NumberFormatException ex) {
                Util.abort("hunit property must be an integer > 0.");
            }
        }
    }

    public void setOutputFormat(String outFormat) {
        String prop = outFormat;
        if (prop == null) {
            this.output = OutputFormat.numeric;
        } else if (prop.equals("nist")) {
            this.output = OutputFormat.nist;
        } else if (prop.equals("numeric")) {
            this.output = OutputFormat.numeric;
        } else if (prop.equals("csv")) {
            this.output = OutputFormat.csv;
        } else if (prop.equals("excel")) {
            this.output = OutputFormat.excel;
        } else {
            Util.abort("Invalid output format!");
        }
    }

    public TieBreaker getTieBreaker() {
        return this.tiebreaker;
    }

    public void setTieBreaker(String tiebreakerStr) {
        if (tiebreakerStr == null) {
            this.tiebreaker = TieBreaker.builtin;
        } else if (tiebreakerStr.equalsIgnoreCase("builtin")) {
            this.tiebreaker = TieBreaker.builtin;
        } else if (tiebreakerStr.equalsIgnoreCase("random")) {
            this.tiebreaker = TieBreaker.random;
        } else {
            Util.abort("Invalid tie breakers!");
        }
    }

    public boolean isFastMode() {
        return this.fastMode;
    }

    public int getMaxTries() {
        return this.maxTries;
    }

    public void setMaxTries(int maxTries) {
        this.maxTries = maxTries;
    }

    public String toString() {
        StringBuffer rval = new StringBuffer();

        rval.append("\nStrength: ").append(this.doi).append("\n");
        rval.append("Mode: ").append(this.mode).append("\n");

        rval.append("Algorithm: ").append(this.algorithm).append("\n");
        rval.append("Constraint Handling: ");
        if (this.ignoreConstraints) {
            rval.append("Ignored\n");
        } else if (this.constraintMode == ConstraintMode.solver) {
            rval.append("Using CSP solver\n");
        } else if (this.constraintMode == ConstraintMode.tuples) {
            rval.append("Using forbidden tuples\n");
        }
        rval.append("Verify Coverage: ");
        if (this.check) {
            rval.append("on\n");
        } else {
            rval.append("off\n");
        }
        return rval.toString();
    }
}
