package com.onehao.acts.testgen.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import com.onehao.acts.testgen.common.Constraint;
import com.onehao.acts.testgen.common.SUT;
import com.onehao.acts.testgen.common.TestGenProfile;
import com.onehao.acts.testgen.common.TestSet;

import edu.uta.cse.fireeye.common.Parameter;
import edu.uta.cse.fireeye.common.Relation;
import edu.uta.cse.fireeye.gui.FireEyeMainWin;
import edu.uta.cse.fireeye.service.exception.OperationServiceException;
import edu.uta.cse.fireeye.util.CSVPrinter;

public class TestSetWrapper {
    private TestSet ts;
    private SUT sut;
    private FireEyeMainWin callerInstance;

    public TestSetWrapper(TestSet ts, SUT sut) {
        this.ts = ts;
        this.sut = sut;
    }

    public TestSetWrapper(FireEyeMainWin win, TestSet ts, SUT sut) {
        this.ts = ts;
        this.sut = sut;
        this.callerInstance = win;
    }

    private void importRCFormat(String path, BufferedReader reader) throws IOException {
        String line = reader.readLine();
        boolean outputParameterPresent = false;
        int rowLength = 0;
        int fileLength = 0;
        try {
            if (line.contains("output parameter")) {
                String[] outputParam = line.split(":");
                String[] oPNames = outputParam[1].split(",");
                line = reader.readLine();
                outputParameterPresent = true;
                if (oPNames.length != this.ts.getOutputParams().size()) {
                    throw new OperationServiceException("Please specify valid number of output parameters");
                }
                rowLength = this.ts.getNumOfParams() + this.ts.getOutputParams().size();
                fileLength = getFileLength(path) - 2;
            } else {
                StringTokenizer tokenizer = new StringTokenizer(line.trim(), ",");
                if (tokenizer.countTokens() != this.ts.getNumOfParams() + this.ts.getOutputParams().size()) {
                    throw new OperationServiceException("Invalid test set format.");
                }
                rowLength = this.ts.getNumOfParams();
                fileLength = getFileLength(path) - 1;
            }
            reader = new BufferedReader(new FileReader(path));
            line = reader.readLine();
            if (outputParameterPresent) {
                line = reader.readLine();
            }
            for (int k = 0; k < fileLength; k++) {
                int[] row = new int[rowLength];
                int i = 0;
                line = reader.readLine();
                if (line.contains(",,")) {
                    line = line.replace(",,", ",!,!");
                }
                if (line.charAt(line.length() - 1) == ',') {
                    line = line.concat("!");
                }
                StringTokenizer tokenizer1 = new StringTokenizer(line.trim(), ",");
                while (tokenizer1.hasMoreTokens()) {
                    tokenizer1.countTokens();
                    String token = tokenizer1.nextToken();
                    Pattern valuePattern = Pattern.compile("\\s*(\\S+(\\s+\\S+)*)\\s*");
                    System.out.println("value is" + token);
                    if (token.equals("!")) {
                        System.out.println("inside");
                        token = "1000000000";
                    }
                    Matcher valueMatcher = valuePattern.matcher(token);
                    if (valueMatcher.matches()) {
                        String value = valueMatcher.group(1);
                        if (value.equals("*")) {
                            row[i] = -1;
                        } else if (value.equals("1000000000")) {
                            row[i] = 1000000000;
                        } else {
                            if (i < this.ts.getNumOfParams()) {
                                row[i] = this.ts.getParam(i).getIndex(value.toLowerCase());
                            } else {
                                row[i] =
                                        ((Parameter) this.ts.getOutputParams().get(i - this.ts.getNumOfParams()))
                                                .getIndex(value.toLowerCase());
                            }
                            if (row[i] == -1) {
                                throw new OperationServiceException("Invalid test values.");
                            }
                        }
                        i++;
                    } else {
                        throw new OperationServiceException("Invalid test set format.");
                    }
                }
                this.ts.addTest(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getFileLength(String path) throws Exception {
        int noOfLines = 0;
        BufferedReader reader = new BufferedReader(new FileReader(path));
        while (reader.readLine() != null) {
            noOfLines++;
        }
        return noOfLines;
    }

    public void outputInCSVFormat(String fn) {
        try {
            int numOfTests = this.ts.getNumOfTests();
            int numOfParams = this.ts.getNumOfParams();

            File csvFile = new File(fn);

            PrintWriter p = new PrintWriter(csvFile);
            CSVPrinter printer = new CSVPrinter(p);
            for (Parameter pa : this.sut.getParams()) {
                printer.write(pa.getName());
            }
            printer.writeln();
            for (int i = 0; i < numOfTests; i++) {
                for (int j = 0; j < numOfParams; j++) {
                    Parameter param = this.sut.getParam(j);

                    int col = this.ts.getColumnID(param.getID());
                    int value = this.ts.getValue(i, col);
                    if (value != -1) {
                        if (param.getParamType() == 1) {
                            printer.setAlwaysQuote(true);
                            printer.write(param.getValue(value));
                            printer.setAlwaysQuote(false);
                        } else {
                            printer.write(param.getValue(value));
                        }
                    } else {
                        printer.write("*");
                    }
                }
                printer.writeln();
            }
            p.close();
            printer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void outputInNistFormat(String fn) {
        System.out.println("Output File in outputInNistFormat write::" + fn);
        try {
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(fn)));

            writer.println();
            writer.println(" Default degree of interaction coverage: " + TestGenProfile.instance().getDOI());
            writer.println("Number of parameters: " + this.ts.getNumOfParams());
            writer.println("Number of configurations: " + this.ts.getNumOfTests());
            writer.println();
            writer.println("Parameters:");
            for (Parameter parameter : this.ts.getParams()) {
                writer.println(parameter.getName() + ":" + parameter.getValues());
            }
            writer.println();
            writer.println("Output parameters: ");
            for (Parameter parameter : this.ts.getOutputParams()) {
                writer.println(parameter.getOutputParamname() + ":" + parameter.getValues());
            }
            writer.println();
            writer.println("Relations :");
            for (Relation relation : this.sut.getRelations()) {
                writer.println("[" + relation.getStrength() + ",(" + relation.getParamNames() + ")]");
            }
            writer.println();
            if (this.sut.getConstraintManager().getConstraints().size() > 0) {
                writer.println("Constraints :");
                for (Constraint constraint : this.sut.getConstraintManager().getConstraints()) {
                    writer.println(constraint.getText());
                }
            }
            writer.println();

            writer.println("------------Test Cases--------------");
            writer.println();

            int numOfTests = this.ts.getNumOfTests();
            int numOfParams = this.ts.getNumOfParams();
            int numOfOutputParams = this.ts.getOutputParams().size();
            for (int i = 0; i < numOfTests; i++) {
                writer.println("Configuration #" + (i + 1) + ":");
                writer.println();
                for (int j = 0; j < numOfParams; j++) {
                    Parameter param = this.sut.getParam(j);
                    int col = this.ts.getColumnID(param.getID());
                    int value = this.ts.getValue(i, col);
                    if (value != -1) {
                        writer.println(j + 1 + " = " + param.getName() + "=" + param.getValue(value));
                    } else {
                        writer.println(j + 1 + " = " + "(don't care)");
                    }
                }
                for (int k = 0; k < numOfOutputParams; k++) {
                    Parameter opParam = this.sut.getOutputParam(k);
                    int opValue = this.ts.getValue(i, numOfParams + k);
                    if (opValue != -1) {
                        if (opValue == 1000000000) {
                            writer.println(numOfParams + 1 + " = " + opParam.getOutputParamname() + "=");
                        } else {
                            writer.println(numOfParams + 1 + " = " + opParam.getOutputParamname() + "="
                                    + opParam.getValue(opValue));
                        }
                    }
                }
                writer.println();
                writer.println("-------------------------------------");
                writer.println();
            }
            writer.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this.callerInstance,
                    "The file you are trying to save is open please close it and then try export.");
        }
    }

    public void outputInNumericFormat(String fn) {
        System.out.println("Output File in Numeric write::" + fn);
        try {
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(fn)));
            int numOfOutputParams = this.ts.getOutputParams().size();

            writer.println();
            writer.println("Degree of interaction coverage: " + TestGenProfile.instance().getDOI());
            writer.println("Number of parameters: " + this.sut.getNumOfParams());
            writer.println("Number of tests: " + this.ts.getNumOfTests());
            writer.println("Number of output parameters: " + numOfOutputParams);

            writer.println();
            writer.println("-------------------------------------");
            writer.println();

            int numOfTests = this.ts.getNumOfTests();
            int numOfParams = this.ts.getNumOfParams();
            for (int i = 0; i < numOfTests; i++) {
                for (int j = 0; j < numOfParams; j++) {
                    Parameter param = this.sut.getParam(j);
                    int col = this.ts.getColumnID(param.getID());
                    int value = this.ts.getValue(i, col);
                    if (value == -1) {
                        writer.print("* ");
                    } else {
                        writer.print(value + " ");
                    }
                }
                for (int k = 0; k < numOfOutputParams; k++) {
                    Parameter opParam = this.sut.getOutputParam(k);
                    int opValue = this.ts.getValue(i, numOfParams + k);
                    if (opValue == 1000000000) {
                        writer.print(" ");
                    } else {
                        writer.print(opValue + " ");
                    }
                }
                writer.println();
            }
            writer.close();
        } catch (IOException ex) {
            System.out.println("Cannot open the output file!");
        }
    }

    private String getExtension(String fileName) {
        String ext = null;
        String nameOfFile = fileName;
        int i = nameOfFile.lastIndexOf('.');
        if ((i > 0) && (i < nameOfFile.length() - 1)) {
            ext = nameOfFile.substring(i + 1).toLowerCase();
        }
        return ext;
    }
}
