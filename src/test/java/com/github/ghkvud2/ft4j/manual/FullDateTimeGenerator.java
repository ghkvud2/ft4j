package com.github.ghkvud2.ft4j.manual;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.github.ghkvud2.ft4j.generator.Generator;

public class FullDateTimeGenerator implements Generator {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    @Override
    public String generate() {
        return LocalDateTime.now().format(formatter);
    }

}