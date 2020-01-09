package com.talend.tmc.dom;

import lombok.Data;

@Data
public class Executable {
    private String executable;
    private String name;
    private String description;
    private String version;
    private String[] parameterNames;
    private Workspace workspace;
}