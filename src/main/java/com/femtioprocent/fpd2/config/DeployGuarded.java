package com.femtioprocent.fpd2.config;

import java.util.ArrayList;
import java.util.List;

/**
*   All Values must be the same as specified in annotation. You can't deploy unless this is so.
*/
public class DeployGuarded {

// Strings
    @DeployGuard(deployStringValue = "") public static String _test_only_s_ = "";

// Trues

// Falses
    @DeployGuard(deployBooleanValue = false) public static boolean forceAggregation = false;
    @DeployGuard(deployBooleanValue = false) public static boolean documentTransformertest = false;
    @DeployGuard(deployBooleanValue = false) public static boolean dryRunZeroIntegration = false;
    @DeployGuard(deployBooleanValue = false) public static boolean reportUsingProd = false;

// Integers
    @DeployGuard(deployIntValue = 123456789) public static int _test_only_ = 123456789;

}
