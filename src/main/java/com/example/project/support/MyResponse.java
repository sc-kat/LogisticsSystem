package com.example.project.support;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class MyResponse {

    private String message;
    private List<?> toReturnId;

}
