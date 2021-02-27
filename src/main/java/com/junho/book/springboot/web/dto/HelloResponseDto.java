package com.junho.book.springboot.web.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor // Arguments 가 있는 생성자에 대한 Annotation
public class HelloResponseDto {

    private final String name;
    private final int amount;

}
