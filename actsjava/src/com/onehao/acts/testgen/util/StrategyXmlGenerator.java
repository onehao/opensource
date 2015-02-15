package com.onehao.acts.testgen.util;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.onehao.acts.testgen.service.extension.common.ParameterWrapper;

import edu.uta.cse.fireeye.common.Parameter;

public class StrategyXmlGenerator {

    private String systemName = null;
    //private Map<String, List<String>> inputParameters = null;
    private List<Parameter> inputParameters = null;
    // private Map<String, List<String>> outputParameters = null;
    private Document doc = null;
    private Element root = null;

    private StrategyXmlGenerator() {
        root = new Element("System");
        doc = new Document(root);
    }

    public static StrategyXmlGenerator newInst() {
        return new StrategyXmlGenerator();
    }

    public String genStrategyXml(String sysName, List<Parameter> params) {
        if ((null == sysName) || sysName.isEmpty() || (null == params) || params.isEmpty()) {
            return null;
        }

        this.setSystemName(sysName);
        this.setInputParameters(params);

        this.buildXmlDoc();

        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        StringWriter writer = new StringWriter();
        try {
            outputter.output(this.doc, writer);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return writer.toString();
    }

    public Document buildXmlDoc() {
        this.buildSystem();
        this.buildInputParameters();
        this.buildOutputParameters();
        this.buildRelations();
        this.buildConstrains();
        return this.doc;
    }

    protected void buildSystem() {
        root.setAttribute(new Attribute("name", this.systemName));
    }

    protected void buildInputParameters() {
        Element paramsElem = new Element("Parameters");
        root.addContent(paramsElem);
        if ((null == this.inputParameters) || (this.inputParameters.size() <= 0)) {
            return;
        }

        int internalId = 0;
        for (Parameter entry : this.inputParameters) {
            ParameterWrapper param = new ParameterWrapper(entry);
            Element paramEl = new Element("Parameter");
            Attribute paramNameAttr = new Attribute("name", param.getParameter().getName());
            Attribute paramTypeAttr = new Attribute("type", String.valueOf(param.getParameter().getParamType()));
            Attribute idTypeAttr = new Attribute("id", String.valueOf(internalId));
            paramEl.setAttribute(idTypeAttr);
            paramEl.setAttribute(paramNameAttr);
            paramEl.setAttribute(paramTypeAttr);

            internalId++;

            paramsElem.addContent(paramEl);

            Element valuesEl = new Element("values");

            for (String val : param.getParameter().getValues()) {
                Element valueEl = new Element("value");
                valueEl.setText(val);
                valuesEl.addContent(valueEl);
            }

            paramEl.addContent(valuesEl);

            Element baseChoicesE1 = new Element("basechoices");
            baseChoicesE1.addContent(new Element("basechoice"));
            paramEl.addContent(baseChoicesE1);
        }
    }

    protected void buildOutputParameters() {
        // TODO: To be implemented if required
        Element outputParamsElem = new Element("OutputParameters");
        root.addContent(outputParamsElem);
    }

    protected void buildRelations() {
        Element relationsElem = new Element("Relations");
        root.addContent(relationsElem);

        // build default relations
        this.buildDefaultRelations(relationsElem);
    }

    protected void buildConstrains() {
        // TODO: To be implemented if required
        Element constraintRoot = new Element("Constraints");
        root.addContent(constraintRoot);
    }

    protected void buildDefaultRelations(Element relationsElem) {
        Element relationEl = new Element("Relation");
        relationsElem.addContent(relationEl);

        Attribute strengthAttr = new Attribute("Strength", "2");
        relationEl.setAttribute(strengthAttr);
        Attribute defaultRelationAttr = new Attribute("Default", "true");
        relationEl.setAttribute(defaultRelationAttr);

        for (Parameter entry : this.inputParameters) {
            Element relParamEl = new Element("Parameter");
            Attribute relParamNameAttr = new Attribute("name", entry.getName());
            relParamEl.setAttribute(relParamNameAttr);

            relationEl.addContent(relParamEl);

            for (String val : entry.getValues()) {
                Element valueEl = new Element("value");
                valueEl.setText(val);
                relParamEl.addContent(valueEl);
            }
        }
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public List<Parameter> getInputParameters() {
        return inputParameters;
    }

    public void setInputParameters(List<Parameter> inputParameters) {
        this.inputParameters = inputParameters;
    }

    // UT
    public static void main(String[] args) {
        String system_name = "Test System";
        
        Parameter p1 = new Parameter("p1");
        p1.addValue("v1");
        p1.addValue("v2");
        Parameter p2 = new Parameter("p2");
        p2.addValue("v3");
        p2.addValue("v4");

        String xml = StrategyXmlGenerator.newInst().genStrategyXml(system_name, Arrays.asList(p1, p2));
        System.out.println(xml);
    }
}
