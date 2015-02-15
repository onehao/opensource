package com.onehao.acts.testgen.service.engine;

import java.util.ArrayList;
import java.util.HashSet;

import com.onehao.acts.testgen.common.SUT;
import com.onehao.acts.testgen.common.TestGenProfile;
import com.onehao.acts.testgen.common.TestSet;

import edu.uta.cse.fireeye.common.Parameter;
import edu.uta.cse.fireeye.common.Relation;
import edu.uta.cse.fireeye.service.exception.OperationServiceException;

/**
 * 
 * @author wanhao01 refactor from FireEye2.8
 */

public class FireEye {
    public static final String DEFAULT_OUTPUT_FILENAME = "output.txt";
    public static boolean verbose = false;

    public static TestSet generateTestSet(TestSet ts, SUT sut) throws OperationServiceException {
        TestSet rval = null;

        TestGenProfile profile = TestGenProfile.instance();
        if (verbose) {
            System.out.println("Algorithm: " + profile.getAlgorithm());
            System.out.println("DOI: " + profile.getDOI());
            System.out.println("Mode: " + profile.getMode());
            System.out.println("VUnit: " + profile.getVUnit());
            System.out.println("Hunit: " + profile.getHUnit());
            System.out.println("ProgressOn: " + profile.isProgressOn());
            System.out.println("FastMode: " + profile.isFastMode());
        }
        ArrayList<Relation> relationsCopy = sut.getRelationManager().getRelations();
        ArrayList<Relation> relationsInUse = new ArrayList<Relation>();
        if (profile.getDOI() > 0) {
            Relation e = new Relation(profile.getDOI(), sut.getParams());
            relationsInUse.add(e);
        } else {
            relationsInUse = relationsCopy;
            HashSet<Parameter> allParam = new HashSet<Parameter>();
            for (Relation r : relationsInUse) {
                allParam.addAll(r.getParams());
            }
            if (allParam.size() < sut.getParams().size()) {
                relationsInUse.add(new Relation(1, sut.getParams()));
            }
        }
        if (relationsInUse.size() == 0) {
            Relation e = new Relation(2, sut.getParams());
            relationsInUse.add(e);
        }
        sut.getRelationManager().setRelations(relationsInUse);

        long start = System.currentTimeMillis();
        if (profile.getAlgorithm() == TestGenProfile.Algorithm.ipof) {
            Builder builder = new Builder(sut);
            rval = builder.generate(TestGenProfile.Algorithm.ipof);
        } else if (profile.getAlgorithm() == TestGenProfile.Algorithm.ipog_r) {
            Builder builder = new Builder(sut);
            rval = builder.generate(TestGenProfile.Algorithm.ipog_r);
        } else if (profile.getAlgorithm() == TestGenProfile.Algorithm.ipof2) {
            ForbesEngine forbes = new ForbesEngine(sut, 8);
            forbes.build();
            rval = forbes.getTestSet();
        }
        // TODO: onehao, current not support paintball and bush and basechoice.
        // else if (profile.getAlgorithm() == TestGenProfile.Algorithm.paintball)
        // {
        // Paintball pb = new Paintball(sut, profile.getDOI(), profile.getMaxTries());
        // rval = pb.build();
        // }
        // else if (profile.getAlgorithm() == TestGenProfile.Algorithm.bush)
        // {
        // Bush bush = new Bush(sut);
        // bush.build();
        // rval = new TestSet(sut.getParams(), sut.getOutputParameters());
        // rval.addMatrix(bush.getMatrix());
        // }
        else if (profile.getAlgorithm() == TestGenProfile.Algorithm.ipog_d) {
            BinaryBuilder builder = new BinaryBuilder(sut.getParams(), sut.getOutputParameters());
            rval = builder.getTestSet(sut.getOutputParameters());
        }
        // else if (profile.getAlgorithm() == TestGenProfile.Algorithm.basechoice)
        // {
        // BaseChoice bc = new BaseChoice(sut);
        // rval = bc.build();
        // }
        else {
            Builder builder = new Builder(sut);
            rval = builder.generate(TestGenProfile.Algorithm.ipog);
        }
        float duration = (float) (System.currentTimeMillis() - start) / 1000.0F;

        sut.getRelationManager().setRelations(relationsCopy);

        System.out.println("Number of Tests\t: " + rval.getNumOfTests());
        System.out.println("Time (seconds)\t: " + duration + " ");

        rval.setGenerationTime(duration);
        if (profile.checkCoverage()) {
            System.out.println("\nCoverage Check:");

            CoverageChecker checker = new CoverageChecker(rval, sut);
            if (checker.check()) {
                System.out.println("Coverage has been verified!");
            } else {
                System.out.println("Failed to verify coverage!");
            }
        }
        System.out.println();
        return rval;
    }
}
