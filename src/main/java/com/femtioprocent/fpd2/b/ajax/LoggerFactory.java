/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.femtioprocent.fpd2.b.ajax;

import java.util.logging.Logger;

/**
 *
 * @author lars
 */
public class LoggerFactory {
    public static Logger getLogger(Class type) {
	return Logger.getLogger(type.getSimpleName());
    }
}
