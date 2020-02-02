package com.example.etkinliktakipsystemi.Models;

public class History {

    private String scan_result;
    private Long process_time;
    boolean isSuccessful;

    public History(String scan_result, Long process_time, boolean isSuccessful) {
        this.scan_result = scan_result;
        this.process_time = process_time;
        this.isSuccessful = isSuccessful;
    }

    public History() {
    }

    public String getScan_result() {
        return scan_result;
    }

    public void setScan_result(String scan_result) {
        this.scan_result = scan_result;
    }

    public Long getProcess_time() {
        return process_time;
    }

    public void setProcess_time(Long process_time) {
        this.process_time = process_time;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    @Override
    public String toString() {
        return "History{" +
                "scan_result='" + scan_result + '\'' +
                ", process_time=" + process_time +
                ", isSuccessful=" + isSuccessful +
                '}';
    }
}
