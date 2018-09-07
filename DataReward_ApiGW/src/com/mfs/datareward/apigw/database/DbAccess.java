/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfs.datareward.apigw.database;

/**
 *
 * @author TrisTDT
 */
public class DbAccess extends BaseDbAccess {

    private static final DbAccess instance = new DbAccess();

    private DbAccess() {

    }

    public static synchronized DbAccess getInstance() {
        if (instance == null) {
            return new DbAccess();
        } else {
            return instance;
        }
    }
}
