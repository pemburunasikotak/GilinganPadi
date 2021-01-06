package com.example.mygilingan;

public class sample {
    public String appendJab(String jobpost, String singjab, String deptname){
        String data = "";
        String jobpost_child = jobpost.toLowerCase();
        String deptname_child = deptname.toLowerCase();
        String[] jobposts = jobpost.split(" ");
        String[] deptnames = deptname.split(" ");

        if(singjab.equalsIgnoreCase("") || singjab.equalsIgnoreCase("-")){
            if(deptname.equalsIgnoreCase("-")) {
                return data+jobpost;
            } else {
                if(jobpost_child.contains("direktur") && deptname_child.contains("direktorat")){
                    if(jobpost_child.contains("wakil") && deptname_child.contains("wakil")){
                        return (jobpost + deptname.replace("Direktorat", "").replace("Wakil", "")).replaceAll("\\s+"," ");
                    } else {
                        return (jobpost + deptname.replace("Direktorat", "")).replaceAll("\\s+"," ");
                    }

                } else {
                    for (int i = 0; i < jobposts.length; i++) {
                        if(!deptname.contains(jobposts[i]) && !jobposts[i].equalsIgnoreCase("Deputi") && !jobposts[i].equalsIgnoreCase("Direktur")){
                            data = data+jobposts[i]+" ";
                        }
                    }
                }
                return data+deptname;
            }
        } else {
            if(!deptname.equalsIgnoreCase("-")) {
                for (int i = 0; i < deptnames.length; i++) {
                    if(singjab.equalsIgnoreCase("Dir.") && deptname_child.contains("direktorat")) {
                        if(!deptnames[i].equalsIgnoreCase("Direktorat")){
                            data = data+" "+deptnames[i];
                        }
                    } else {
                        if(!singjab.contains(deptnames[i])){
                            data = data+" "+deptnames[i];
                        }
                    }
                }
                if(singjab.equalsIgnoreCase("Dir.") && deptname_child.contains("direktorat")) {
                    data = "Direktur"+data;
                } else {
                    data = singjab+data;
                }
            } else {
                data = jobpost;
            }
            return data;
        }
    }
}
