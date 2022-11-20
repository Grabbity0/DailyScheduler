package org.techtown.schedulerproject;

public class Memo {
    String time;
    String subject;
    String content;

    public Memo(String time, String subject, String content) {
        this.time = time;
        this.subject = subject;
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
