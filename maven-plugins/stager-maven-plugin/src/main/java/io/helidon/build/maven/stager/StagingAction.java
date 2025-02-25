/*
 * Copyright (c) 2020, 2022 Oracle and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.helidon.build.maven.stager;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.plexus.configuration.PlexusConfiguration;

/**
 * Staging action.
 */
interface StagingAction extends StagingElement {

    /**
     * Execute the action.
     *
     * @param context   staging context
     * @param dir       directory
     * @param variables substitution variables
     */
    void execute(StagingContext context, Path dir, Map<String, String> variables) throws IOException;

    /**
     * Execute the action.
     *
     * @param context staging context
     * @param dir     directory
     */
    default void execute(StagingContext context, Path dir) throws IOException {
        execute(context, dir, new HashMap<>());
    }

    /**
     * Describe the task.
     *
     * @param dir       stage directory
     * @param variables variables for the current iteration
     * @return String that describes the task
     */
    String describe(Path dir, Map<String, String> variables);

    /**
     * Convert a {@link PlexusConfiguration} instance to a list of {@link StagingAction}.
     *
     * @param configuration plexus configuration
     * @return list of {@link StagingAction}
     */
    static List<StagingAction> fromConfiguration(PlexusConfiguration configuration) {
        return fromConfiguration(configuration, new StagingElementFactory());
    }

    /**
     * Convert a {@link PlexusConfiguration} instance to a list of {@link StagingAction}.
     *
     * @param configuration plexus configuration
     * @param factory       staging element factory
     * @return list of {@link StagingAction}
     */
    static List<StagingAction> fromConfiguration(PlexusConfiguration configuration, StagingElementFactory factory) {
        PlexusConfigNode parent = new PlexusConfigNode(configuration, null);
        return new ConfigReader(factory).read(parent);
    }
}
