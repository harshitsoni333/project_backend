package com.soni.usermanagement.methods;

import com.soni.usermanagement.exception.InvalidEntry;
import com.soni.usermanagement.model.FileTypeManagement;

public class FileTypeValidator {

    public static boolean validateFileType(FileTypeManagement newFileType) {

        // checking constraints of fileTypeCode
        // should be alphanumeric
        String fileTypeCode = newFileType.getFileTypeCode();
        if(!IsAlphaNumeric.isAlphaNumeric(fileTypeCode))
        throw new InvalidEntry(fileTypeCode);
        // should have length <= 6
        if(fileTypeCode.length() > 6) 
        throw new InvalidEntry(String.format("length of '%s' is more than 6 characters", fileTypeCode));

        // checking for invalid values of isBankFile and isKMT54
        String isBankFile = newFileType.getIsBankFile();
        String isKMT54 = newFileType.getIsKMT54();
        if(!(isBankFile.equalsIgnoreCase("yes") || isBankFile.equalsIgnoreCase("no")))
        throw new InvalidEntry("isBankFile = " + isBankFile);
        if(!(isKMT54.equalsIgnoreCase("yes") || isKMT54.equalsIgnoreCase("no")))
        throw new InvalidEntry("isKMT54 = " + isKMT54);

        return true;
    }
}