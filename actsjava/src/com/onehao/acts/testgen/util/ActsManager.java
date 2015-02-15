package com.onehao.acts.testgen.util;

import java.util.Map;

import com.onehao.acts.testgen.common.SUT;
import com.onehao.acts.testgen.common.TestGenProfile;
import com.onehao.acts.testgen.common.TestSet;
import com.onehao.acts.testgen.service.engine.FireEye;
import com.onehao.acts.testgen.service.engine.SUTInfoReader;
import com.onehao.acts.testgen.service.extension.PostExtensionManager;
import com.onehao.acts.testgen.service.extension.PreExtensionManager;
import com.onehao.acts.testgen.service.extension.Utils;
import com.onehao.acts.testgen.service.extension.post.OneWayExtStrategy;
import com.onehao.acts.testgen.service.extension.post.PreGenValueAppendStrategy;
import com.onehao.acts.testgen.service.extension.post.RandFillingStrategy;
import com.onehao.acts.testgen.service.extension.pre.BoundaryValueGenStrategy;
import com.onehao.acts.testgen.service.extension.pre.IPreExtensionStrategy;

import edu.uta.cse.fireeye.service.exception.OperationServiceException;
import edu.uta.cse.fireeye.util.Util;

//import edu.uta.cse.fireeye.service.engine.SUTInfoReader;
/**
 * 
 * @author yanchen
 *
 */
public class ActsManager {
    public static final String DEFAULT_OUTPUT_FILENAME = "output.txt";
    public static boolean verbose = true;

    public static void main(String[] args) {
        args =
                new String[] { "",
                        "D:\\document\\onedrive\\LBS\\\\FireEyeacts_beta_v1_r9.1\\direction_beijing_v2.xml" };
        
//        StrategyCaseParaService service = new StrategyCaseParaService();
//        List<TestCase> list = service.getTestCaseFromConf(args[1],"1");
        SUT sut = doActs(args);
        
    }

    public static SUT doActs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            System.out.println("value is--->" + args[i]);
        }
        String inputFileName = null;
        String outputFileName = "output.txt";
        if (args.length <= 1) {
            return null;
        } else if (args.length == 2) {
            inputFileName = args[1];
        } else if (args.length == 3) {
            inputFileName = args[1];
            outputFileName = args[2];
        } else {
            Util.abort("The number of arguments cannot be greater than 2.");
        }
        TestGenProfile profile = TestGenProfile.instance();
        setCommandLineProperties(profile);

        SUTInfoReader reader = new SUTInfoReader(inputFileName);
        SUT sut = reader.getSUT();

        System.out.println("System Name: " + sut.getName());
        System.out.println(profile);

        System.out.println(sut);
        
        Map<String, Integer> originalParamDomainSize = Utils.recordParamDomainSize(sut);
        OneWayExtStrategy oneWayExtStrategy = new OneWayExtStrategy();
        
        PreExtensionManager preExtensionManager = new PreExtensionManager();
        preExtensionManager.setOriginalParamDomainSize(originalParamDomainSize);
        ((PreExtensionManager)preExtensionManager
                .register(BoundaryValueGenStrategy.buildStrategy(IPreExtensionStrategy.GenValueAppendStrategy.Post))
                .register(oneWayExtStrategy))
                    .process(sut);

        profile.setRandstar(false);
        TestSet ts = null;

        try {
            ts = FireEye.generateTestSet(ts, sut);
        } catch (OperationServiceException e) {
            e.printStackTrace();
            System.out.println("OperationServiceException: " + e.getMessage() + e.getStackTrace());
        }
        
        PostExtensionManager postExtensionManager = new PostExtensionManager();
        postExtensionManager.setOriginalParamDomainSize(originalParamDomainSize);
        ((PostExtensionManager)postExtensionManager
                .register(oneWayExtStrategy)
                .register(new PreGenValueAppendStrategy(preExtensionManager.getGenValuesMap()).setVerticalExtStrategy(PreGenValueAppendStrategy.VerticalExtStrategy.Smart))
                .register(new RandFillingStrategy(RandFillingStrategy.RandomRange.OriginalValues)))
                    .extend(ts, sut);

        System.out.println("Output file: " + outputFileName);

        TestSetWrapper wrapper = new TestSetWrapper(ts, sut);
        wrapper.outputInCSVFormat(outputFileName);
        System.out.println("After Output File Generation: " + outputFileName);
        // if (TestGenProfile.instance().checkCoverage()) {
        // System.out.println("\nCoverage Check:");
        // System.out.println("===========================================");
        // CoverageChecker checker = new CoverageChecker(ts, sut);
        // if (checker.check()) {
        // System.out.println("\nCoverage has been verified!");
        // } else {
        // System.out.println("\nFailed to verify coverage!");
        // }
        // }
        // System.out.println("Coverage Check: " + TestGenProfile.instance().checkCoverage());
        return sut;
    }

    private static void setCommandLineProperties(TestGenProfile profile) {
        String out = System.getProperty("output");
        profile.setOutputFormat("excel");
        if ((out != null) && (out.length() > 0)) {
            profile.setOutputFormat(out);
        }
        out = System.getProperty("algo");
        if ((out != null) && (out.length() > 0)) {
            profile.setAlgorithm(out);
        }
        out = System.getProperty("doi");
        if ((out != null) && (out.length() > 0)) {
            profile.setDOI(Integer.parseInt(out));
        }
        String rand = System.getProperty("randstar");
        if ((rand != null) && (rand.length() > 0)) {
            profile.setRandstar(rand);
        }
        String fastMode = System.getProperty("fastMode");
        if ((fastMode != null) && (fastMode.length() > 0)) {
            profile.setFastMode(fastMode);
        }
        String mode = System.getProperty("mode");
        if ((mode != null) && (mode.length() > 0)) {
            profile.setMode(mode);
        }
        String check = System.getProperty("check");
        if ((check != null) && (check.length() > 0)) {
            profile.setCheckCoverage(check);
        }
        String progress = System.getProperty("progress");
        if ((progress != null) && (progress.length() > 0)) {
            profile.setProgress(progress);
        }
        String combine = System.getProperty("combine");
        if ((combine != null) && (!combine.isEmpty())) {
            profile.setCombine(combine);
        }
    }
}
