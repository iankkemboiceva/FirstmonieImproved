package firstmob.firstbank.com.firstagent.model;

/**
 * Created by deeru on 18-10-2016.
 */

import com.google.gson.annotations.SerializedName;
public class ChargebackList {
    private String amount;
    private String code;
    private String refNum;
    private String catdType;
    private String id;
    private String pan;
    private String txnDate;
    private String status;

    public ChargebackList(String amount,String code,String refNum,String catdType,String id,String pan,String txnDate,String status) {
        this.amount = amount;
        this.code = code;
        this.refNum = refNum;
        this.status = status;
        this.amount = amount;
        this.catdType = catdType;
        this.pan = pan;
        this.txnDate = txnDate;
        this.id = id;
    }
    // Getter Methods

    public String getAmount() {
        return amount;
    }

    public String getCode() {
        return code;
    }

    public String getRefNum() {
        return refNum;
    }

    public String getCatdType() {
        return catdType;
    }


    public String getPan() {
        return pan;
    }

    public String getTxnDate() {
        return txnDate;
    }

    public String getStatus() {
        return status;
    }

    // Setter Methods



    public void setRefNum(String refNum) {
        this.refNum = refNum;
    }

    public void setCatdType(String catdType) {
        this.catdType = catdType;
    }


    public void setPan(String pan) {
        this.pan = pan;
    }

    public void setTxnDate(String txnDate) {
        this.txnDate = txnDate;
    }


}