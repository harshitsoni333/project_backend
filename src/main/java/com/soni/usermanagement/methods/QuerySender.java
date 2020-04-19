package com.soni.usermanagement.methods;

public class QuerySender {

    public static String sendQuery() {
        return "select aar.account_id, a.iban, a.bank_code, a.entity, fm.is_kmt54, aar.last_updated_user, aar.last_updated_date, f.file_code, f.app_code, faft.file_type_code from file_type_management fm inner join (file_app_relation f inner join(file_app_filetype faft inner join (accounts a inner join account_app_relation aar on a.id = aar.account_id) on aar.file_app_id = faft.id) on f.id = faft.file_app_id) on fm.file_type_code = faft.file_type_code";
    }
}