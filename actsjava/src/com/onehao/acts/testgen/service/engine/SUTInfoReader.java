package com.onehao.acts.testgen.service.engine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.onehao.acts.testgen.common.Constraint;
import com.onehao.acts.testgen.common.SUT;
import com.onehao.acts.testgen.common.TestSet;
import com.onehao.acts.testgen.util.Util;

import edu.uta.cse.fireeye.common.Parameter;
import edu.uta.cse.fireeye.common.Relation;
import edu.uta.cse.fireeye.data.SUTData;
import edu.uta.cse.fireeye.gui.model.SystemDataProcessor;
import edu.uta.cse.fireeye.service.constraint.ConstraintParser;
import edu.uta.cse.fireeye.service.exception.OperationServiceException;

/**
 * 
 * @author wanhao01 refactor from FireEye2.8
 */

public class SUTInfoReader {
    private SUTData data;
    private TestSet ts = null;
    private BufferedReader reader;
    private Section currentSection;

    public static enum Section {
        undefined, system, group, parameter, relation, constraint, misc, testset;
    }

    private int autoParamIndex = 0;
    private int numOfParams = -1;
    private int domainSize = -1;
    private boolean isXML = false;
    private boolean testSetHeader = false;
    private int[] columnIDs;

    public SUTInfoReader(String path) {
        try {
            if (path.endsWith(".xml")) {
                this.isXML = true;
                parseXML(path);
            } else {
                this.reader = new BufferedReader(new FileReader(path));
                this.currentSection = Section.undefined;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            Util.abort("Cannot read the input file!");
        }
    }

    public SUT getSUT() {
        if (this.isXML) {
            return Util.convertToSUT(this.data);
        }
        SUT sut = new SUT();
        try {
            String line;
            while ((line = this.reader.readLine()) != null) {
                if (Pattern.matches("\\[.*(?i)System.*\\]", line)) {
                    this.currentSection = Section.system;
                } else if (Pattern.matches("\\[.*(?i)Parameter.*\\]", line)) {
                    this.currentSection = Section.parameter;
                } else if (Pattern.matches("\\[.*(?i)Group.*\\]", line)) {
                    this.currentSection = Section.group;
                } else if (Pattern.matches("\\[.*(?i)Relation.*\\]", line)) {
                    this.currentSection = Section.relation;
                } else if (Pattern.matches("\\[.*(?i)Constraint.*\\]", line)) {
                    this.currentSection = Section.constraint;
                } else if (Pattern.matches("\\[.*(?i)Misc.*\\]", line)) {
                    this.currentSection = Section.misc;
                } else if (Pattern.matches("\\[.*(?i)Test Set.*\\]", line)) {
                    this.currentSection = Section.testset;
                } else if (!Pattern.matches("\\s*--.*", line)) {
                    switch (this.currentSection) {
                        case group:
                            readSystemSection(sut, line);
                            break;
                        case parameter:
                            readParameterSection(sut, line);
                            break;
                        case misc:
                            readGroupSection(sut, line);
                            break;
                        case relation:
                            readRelationSection(sut, line);
                            break;
                        case system:
                            readConstraintSection(sut, line);
                            break;
                        case testset:
                            readMiscSection(sut, line);
                            break;
                        case undefined:
                            readExistingTestSet(sut, line);
                    }
                }
            }
        } catch (IOException ex) {
            Util.abort("Errors encountered when reading input file!");
        }
        if (this.ts != null) {
            sut.setExistingTestSet(this.ts);
        }
        return sut;
    }

    private void parseXML(String path) {
        try {
            SystemDataProcessor fp = new SystemDataProcessor();
            this.data = fp.parseXMLFile(path);
            if (this.data == null) {
                Util.abort("Cannot read the input file!");
            }
            if (this.data.isParseError()) {
                Util.abort(this.data.getErrorMsg());
            }
        } catch (OperationServiceException oe) {
        	oe.printStackTrace();
            Util.abort("Cannot read the input file!");
        }
    }

    private void readSystemSection(SUT sut, String line) {
        Pattern namePattern = Pattern.compile(".*(?i:Name).*:\\s*(\\S+(?:\\s+\\S+)*)\\s*");
        Matcher nameMatcher = namePattern.matcher(line);
        if (nameMatcher.matches()) {
            sut.setName(nameMatcher.group(1));
        }
    }

    private void readParameterSection(SUT sut, String line) {
        Pattern paramPattern = Pattern.compile("\\s*(\\S+)\\s*\\(\\s*(\\S+)\\s*\\)\\s*:\\s*(\\S+(?:\\s+\\S+)*)\\s*");
        Matcher paramMatcher = paramPattern.matcher(line);
        if (paramMatcher.matches()) {
            String name = paramMatcher.group(1);
            String type = paramMatcher.group(2);
            Parameter param = sut.addParam(name);
            int paramType = getParamType(type);
            param.setType(paramType);
            String values = paramMatcher.group(3);
            StringTokenizer tokenizer = new StringTokenizer(values, ",;");
            while (tokenizer.hasMoreTokens()) {
                String value = tokenizer.nextToken().trim();
                if (checkValue(value, paramType)) {
                    param.addValue(value);
                } else {
                    Util.abort("Parameter value (" + name + ", " + value + ") does not match its type.");
                }
            }
        }
    }

    private boolean checkValue(String value, int type) {
        boolean rval = true;
        if (type == 2) {
            if ((!value.equalsIgnoreCase("true")) && (!value.equalsIgnoreCase("false"))) {
                rval = false;
            }
        } else if (type == 0) {
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(value);
            if (!matcher.matches()) {
                rval = false;
            }
        }
        return rval;
    }

    private int getParamType(String type) {
        int rval = -1;
        if ((type.equalsIgnoreCase("int")) || (type.equalsIgnoreCase("integer"))) {
            rval = 0;
        } else if ((type.equalsIgnoreCase("bool")) || (type.equalsIgnoreCase("boolean"))) {
            rval = 2;
        } else if (type.equalsIgnoreCase("enum")) {
            rval = 1;
        } else {
            Util.abort("Invalid parameter type, " + type + ", is encountered: "
                    + "Valid types include int/integer, bool/boolean, enum");
        }
        return rval;
    }

    private void readGroupSection(SUT sut, String line) {
        Pattern pattern = Pattern.compile("\\s*(\\S+(?:\\s+\\S+)*)\\s*:\\s*(\\S+(?:\\s+\\S+)*)\\s*");
        Matcher matcher = pattern.matcher(line);
        if (matcher.matches()) {
            String name = matcher.group(1);
            String value = matcher.group(2);
            if (name.equals("Num of Params")) {
                this.numOfParams = Integer.parseInt(value);
            } else if (name.equals("Domain Size")) {
                this.domainSize = Integer.parseInt(value);
            }
            if ((this.numOfParams != -1) && (this.domainSize != -1)) {
                for (int i = 0; i < this.numOfParams; i++) {
                    Parameter param = sut.addParam("P" + this.autoParamIndex++);
                    for (int j = 0; j < this.domainSize; j++) {
                        param.addValue("v" + j);
                    }
                }
                this.numOfParams = -1;
                this.domainSize = -1;
            }
        }
    }

    private void readRelationSection(SUT sut, String line) {
        Pattern relationPattern = Pattern.compile(".*\\(\\s*(.*)\\s*\\).*");
        Matcher relationMatcher = relationPattern.matcher(line);
        if (relationMatcher.matches()) {
            String relationStr = relationMatcher.group(1);
            String[] paramsAndStrength = relationStr.split("[,;]");
            if (paramsAndStrength.length >= 2) {
                String strengthStr = paramsAndStrength[(paramsAndStrength.length - 1)].trim();
                int strength = Integer.parseInt(strengthStr);
                ArrayList<Parameter> params = new ArrayList<Parameter>();
                for (int j = 0; j < paramsAndStrength.length - 1; j++) {
                    boolean found = false;
                    for (int i = 0; i < sut.getParameters().size(); i++) {
                        if (sut.getParam(i).getName().equals(paramsAndStrength[j].trim())) {
                            params.add(sut.getParam(i));
                            found = true;
                        }
                    }
                    if (!found) {
                        Util.abort("Errors encountered when reading relations: check parameter names are spelled correctly.");
                    }
                }
                if (params.size() >= strength) {
                    Relation relation = new Relation(strength, params);
                    sut.getRelationManager().addRelation(relation);
                } else {
                    Util.abort("Errors encountered when reading relations :# of params should be no less than strength.");
                }
            } else {
                Util.abort("Errors encountered when reading relations: General format: R1: (param1, param2, ..., strength)");
            }
        }
    }

    private void readConstraintSection(SUT sut, String line) {
        int pos = line.indexOf(":");
        String constraintStr = line.substring(pos + 1).trim();
        if (constraintStr.length() > 0) {
            HashMap<String, Parameter> allParams = buildParamHashMap(sut.getParams());
            ConstraintParser parser = new ConstraintParser(constraintStr, allParams);
            try {
                parser.parse();

                ArrayList<Parameter> usedParams = parser.getUsedParamList();

                Constraint constraint = new Constraint(constraintStr, usedParams);
                sut.getConstraintManager().addConstraint(constraint);
            } catch (Exception ex) {
                Util.abort("Errors encountered when paring constraints :" + line
                        + ". Please check each paramter name is spelled correctly");
            }
        }
    }

    private HashMap<String, Parameter> buildParamHashMap(ArrayList<Parameter> params) {
        HashMap<String, Parameter> rval = new HashMap<String, Parameter>();
        for (Parameter param : params) {
            rval.put(param.getName(), param);
        }
        return rval;
    }

    private void readMiscSection(SUT sut, String line) {
    }

    public void readExistingTestSet(SUT sut, String line) {
        if (line.length() > 0) {
            if (!this.testSetHeader) {
                String[] names = line.split(",");
                this.columnIDs = new int[names.length];
                int i = 0;
                for (String name : names) {
                    Parameter param;
                    if ((param = sut.getParam(name.trim())) != null) {
                        this.columnIDs[(i++)] = param.getID();
                    } else {
                        Util.abort("Invalid parameter names encountered in the Test Set section.");
                    }
                }
                this.ts = new TestSet(sut.getParams());

                this.testSetHeader = true;
            } else {
                String[] values = line.split(",");
                if (values.length == this.columnIDs.length) {
                    int[] row = new int[sut.getNumOfParams()];
                    Arrays.fill(row, -1);
                    for (int j = 0; j < values.length; j++) {
                        int index = sut.getParam(this.columnIDs[j]).getIndex(values[j].trim());
                        if (index >= 0) {
                            row[this.columnIDs[j]] = index;
                        } else {
                            Util.abort("Invalid parameter value: " + values[j].trim());
                        }
                    }
                    this.ts.addTest(row);
                } else {
                    Util.abort("# of values in each row does not match # of params in the Test Set section");
                }
            }
        }
    }
}
