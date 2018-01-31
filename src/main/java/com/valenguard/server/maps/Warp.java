package com.valenguard.server.maps;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Warp {
    private String mapName;
    private int x;
    private int y;
}
