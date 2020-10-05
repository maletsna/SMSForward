package com.smsforward;

public class Rule {

    private boolean forwardAll;
    private String from;
    private String to;
    private String name;

    public Rule(boolean forwardAll, String from, String to, String name) {
        this.forwardAll = forwardAll;
        this.from = from;
        this.to = to;
        this.name = name;
    }
    public boolean isForwardAll() {
        return forwardAll;
    }

    public void setForwardAll(boolean forwardAll) {
        this.forwardAll = forwardAll;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
