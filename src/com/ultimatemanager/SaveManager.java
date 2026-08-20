package com.ultimatemanager;

import javax.microedition.rms.*;

public class SaveManager {
    public SaveManager() {
    }

    public boolean hasSave(String slot) {
        try {
            RecordStore rs = RecordStore.openRecordStore("UM_" + slot, false);
            int num = rs.getNumRecords();
            rs.closeRecordStore();
            return num > 0;
        } catch (RecordStoreException rse) {
            return false;
        }
    }

    public void createDefaultSave(String slot) {
        try {
            RecordStore.deleteRecordStore("UM_" + slot);
        } catch (Exception e) { /* ignore */ }
        RecordStore rs = null;
        try {
            rs = RecordStore.openRecordStore("UM_" + slot, true);
            String starter = "SLOT:" + slot + "|VERSION:0|MONEY:1000000";
            rs.addRecord(starter.getBytes(), 0, starter.length());
            rs.closeRecordStore();
        } catch (RecordStoreException rse) {
            // cannot create save - bail
        }
    }

    public byte[] readSave(String slot) {
        try {
            RecordStore rs = RecordStore.openRecordStore("UM_" + slot, false);
            if (rs.getNumRecords() > 0) {
                byte[] b = rs.getRecord(1);
                rs.closeRecordStore();
                return b;
            } else {
                rs.closeRecordStore();
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public void writeSave(String slot, byte[] data) {
        try {
            RecordStore rs = RecordStore.openRecordStore("UM_" + slot, true);
            if (rs.getNumRecords() == 0) {
                rs.addRecord(data, 0, data.length);
            } else {
                rs.setRecord(1, data, 0, data.length);
            }
            rs.closeRecordStore();
        } catch (Exception e) {
            // write failed
        }
    }
}
