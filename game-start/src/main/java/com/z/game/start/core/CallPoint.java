package com.z.game.start.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CallPoint {

    private String nodeId;

    private String portId;

    private String serviceId;

    public CallPoint(String nodeId, String portId) {
        this.nodeId = nodeId;
        this.portId = portId;
    }

}
