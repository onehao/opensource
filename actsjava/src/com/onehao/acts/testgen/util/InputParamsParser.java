package com.onehao.acts.testgen.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.onehao.acts.testgen.service.extension.Utils;
import com.onehao.acts.testgen.service.extension.common.ParameterType;
import com.onehao.acts.testgen.service.extension.common.ParameterWrapper;
import com.onehao.acts.testgen.service.extension.dict.DictDataManager;
import com.onehao.acts.testgen.service.extension.range.FixedGenSizeStrategy;
import com.onehao.acts.testgen.service.extension.range.IRangeValueGenerator;
import com.onehao.acts.testgen.service.extension.range.RangeGenManager;

import edu.uta.cse.fireeye.common.Parameter;

public class InputParamsParser {

	private Logger logger = Logger.getLogger(InputParamsParser.class);
	private List<Parameter> inputParameters;
	private JSONArray reqParamsArray;
	private IRangeValueGenerator rangeGenManager;

	public static final String RANGE_SPLITTER = "~";

	protected InputParamsParser() {
		inputParameters = new ArrayList<Parameter>();

		rangeGenManager = RangeGenManager.buildDefRangeGenManager()
				.setGenSizeStrategy(
						new FixedGenSizeStrategy(25));
	}

	public static InputParamsParser newInst() {
		InputParamsParser inst = new InputParamsParser();
		return inst;
	}

	public String parseReqAndGenStrategyXml(String apiVerName,
			JSONArray reqArray) {
		reqParamsArray = reqArray;
		this.parseInputParameters();
		return StrategyXmlGenerator.newInst().genStrategyXml(
				apiVerName, this.inputParameters);
	}

	protected void parseInputParameters() {
		if (null == this.reqParamsArray) {
			return;
		}

		for (int i = 0; i < this.reqParamsArray.size(); i++) {
			JSONObject obj = this.reqParamsArray.getJSONObject(i);
			this.parseOneParameter(obj);
		}
	}

	protected void parseOneParameter(JSONObject obj) {
		if (null == obj) {
			return;
		}

		String paramName = obj.getString("name");
		String refVal = obj.getString("refValue");
		String paramType = obj.getString("type");
		JSONArray paramValues = obj.getJSONArray("values");

		boolean isRequired = false;
		try {
			isRequired = obj.getBoolean("isRequired");
		} catch (Exception ex) {
			logger.info("InputParamsParser.genRangeValues:"
					+ ex.toString());
		}

		List<String> valList = this.parseParamValues(refVal, paramType,
				isRequired, paramValues);

		if ((!paramName.trim().isEmpty()) && (valList != null)
				&& (valList.size() > 0)) {
			ParameterWrapper paramWrapper = new ParameterWrapper(
					new Parameter(paramName));
			paramWrapper.getParameter().getValues().addAll(valList);
			paramWrapper.getParameter().setType(
					this.getCommonParamType(paramType));
			paramWrapper.setIsRequired(isRequired);
			paramWrapper.setBoundaryGen(true);
			inputParameters.add(paramWrapper.getParameter());
		}
	}

	protected List<String> parseParamValues(String refVal, String type,
			boolean isRequired, JSONArray paramValues) {
		List<String> valList = new ArrayList<String>();

		if ((null == paramValues) || (paramValues.size() <= 0)) {
			if (isRequired) {
				valList.add(refVal);
			}

			return valList;
		}

		if ((type != null) && type.equalsIgnoreCase("range")) {
			Collection<String> uList = new HashSet<String>();
			for (int i = 0; i < paramValues.size(); i++) {
				this.genRangeValues(uList,
						paramValues.getString(i));
			}
			valList.addAll(uList);
		} else if ((type != null) && type.equalsIgnoreCase("dict")) {
			for (int i = 0; i < paramValues.size(); i++) {
				Collection<String> vals = DictDataManager
						.getInst()
						.pickRandomValues(
								paramValues.getString(i));
				if (vals != null && !vals.isEmpty()) {
					valList.addAll(vals);
				}
			}
		} else {
			for (int i = 0; i < paramValues.size(); i++) {
				Utils.addUniqValToCollection(valList,
						paramValues.getString(i));
			}
		}

		return valList;
	}

	protected void genRangeValues(Collection<String> valList,
			String rangeStr) {
		if ((null == rangeStr) || rangeStr.isEmpty()) {
			return;
		}

		if (!rangeStr.contains(RANGE_SPLITTER)) {
			Utils.addUniqValToCollection(valList, rangeStr);
			return;
		}

		try {
			String[] tokens = rangeStr.split(RANGE_SPLITTER);
			if (tokens.length < 2) {
				Utils.addUniqValToCollection(valList, tokens[0]);
				return;
			}

			// TODO: if required, allow to change GenSizeStrategy
			// here.
			rangeGenManager.genRangeValues(valList, tokens[0],
					tokens[1]);

		} catch (Exception ex) {
			logger.info("InputParamsParser.genRangeValues:"
					+ ex.toString());
		}
	}

	private static List<String> getVocabulary(String path) {
		if (path == null) {
			return null;
		}
		File file = new File(path);
		InputStreamReader freader;
		try {
			freader = new InputStreamReader(new FileInputStream(
					file), "utf8");
			List<String> result = new ArrayList<String>();
			if (path.endsWith(".txt") || path.endsWith(".csv")) {
				BufferedReader reader = new BufferedReader(
						freader);
				String str = reader.readLine();
				while (str != null) {
					result.add(str);
					str = reader.readLine();
				}
				reader.close();
				freader.close();
				return result;
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 词表解析及随机抽样
	 * 
	 * @param path
	 * @param count
	 * @return
	 */
	public static List<String> getValuesFromVocabulary(String path,
			int count) {
		if (path == null || count <= 0) {
			return null;
		}
		List<String> vocabulary = getVocabulary(path);
		if (vocabulary == null || vocabulary.size() == 0) {
			return null;
		}
		if (count >= vocabulary.size()) {
			return vocabulary;
		}
		List<String> result = new ArrayList<String>();
		int size = vocabulary.size() / count;
		for (int i = 0; i < count; i++) {
			int max = 0;
			if (i == count - 1) {
				max = vocabulary.size();
			} else {
				max = (i + 1) * size;
			}
			int min = i * size;
			Random random = new Random();
			int index = random.nextInt(max) % (max - min + 1) + min;
			result.add(vocabulary.get(index));
			System.out.println(index);
		}
		return null;
	}

	protected int getCommonParamType(String type) {
		String typeStr = type.trim().toLowerCase();
		int ret = 1;
		switch (typeStr) {
		case "num":
			ret = ParameterType.CommonType.NUMBER;
			break;
		case "enum":
			ret = ParameterType.CommonType.ENUM;
			break;
		case "boolean":
			ret = ParameterType.CommonType.BOOLEAN;
			break;
		case "string":
			ret = ParameterType.CommonType.STRING;
			break;
		case "MD5":
			ret = ParameterType.CommonType.STRING;
			break;
		case "range":
			ret = ParameterType.CommonType.RANGE;
			break;
		case "dict":
			ret = ParameterType.CommonType.DICT;
			break;
		default:
			break;
		}

		return ret;
	}
}
