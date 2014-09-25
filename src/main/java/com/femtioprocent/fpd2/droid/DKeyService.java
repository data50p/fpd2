/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.femtioprocent.fpd2.droid;

import java.util.List;

/**
 *
 * @author lars
 */
class DKeyService {

    static DKeyService locate(Class<DKeyService> aClass) {
	return new DKeyService();
    }

    List<DKey> getByName(DKey.Name k) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void append(DKey value) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void deleteBeforeByName(DKey.Name name, int appendedId) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
