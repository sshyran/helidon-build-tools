/*
 * Copyright (c) 2018, 2020 Oracle and/or its affiliates.
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

package io.helidon.build.maven.dev.echo;

import java.util.List;

import io.helidon.build.maven.link.MavenLogWriter;
import io.helidon.build.util.Log;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Maven plugin that prints configured messages.
 */
@Mojo(name = "echo", defaultPhase = LifecyclePhase.NONE)
public class EchoMojo extends AbstractMojo {

    /**
     * Skip execution for this plugin.
     */
    @Parameter(defaultValue = "false", property = "echo.skip")
    private boolean skip;

    /**
     * The messages.
     */
    @Parameter
    private List<String> messages;

    @Override
    public void execute() {
        if (skip) {
            getLog().info("execution skipped");
        } else {
            MavenLogWriter.install(getLog());
            messages.forEach(msg -> Log.info(msg == null ? "" : msg));
        }
    }
}
