/*
 * Copyright The Original Author or Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.jenkins.plugins.opentelemetry;

import hudson.model.InvisibleAction;
import io.jenkins.plugins.opentelemetry.job.MonitoringAction;
import io.opentelemetry.api.common.AttributeKey;

import edu.umd.cs.findbugs.annotations.NonNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @see io.opentelemetry.api.common.AttributeKey
 * @see io.opentelemetry.api.common.AttributeType
 */
public class OpenTelemetryAttributesAction extends InvisibleAction implements Serializable {
    private final static Logger LOGGER = Logger.getLogger(MonitoringAction.class.getName());

    private static final long serialVersionUID = 5488506456727905116L;

    private transient Map<AttributeKey<?>, Object> attributes;

    private transient Set<String> appliedToSpans;

    @NonNull
    public Map<AttributeKey<?>, Object> getAttributes() {
        if (attributes == null) {
            attributes = new HashMap<>();
        }
        return attributes;
    }

    /**
     * Remember a span to which these attributes are applied.
     * @param spanId
     * @return true iff a span did not previously have these attributes applied
     */
    public boolean isNotYetAppliedToSpan(String spanId) {
        if (appliedToSpans == null) {
            appliedToSpans = new HashSet<>();
        }
        return appliedToSpans.add(spanId);
    }

    @Override
    public String toString() {
        return "OpenTelemetryAttributesAction{" +
                "attributes=" + getAttributes().entrySet().stream().map(e -> e.getKey().getKey() + "-" + e.getKey().getType() + " - " + e.getValue()).collect(Collectors.joining(", ")) +
                '}';
    }
}
